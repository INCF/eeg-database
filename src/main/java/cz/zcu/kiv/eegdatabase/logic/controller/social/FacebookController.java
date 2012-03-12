package cz.zcu.kiv.eegdatabase.logic.controller.social;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.service.HibernatePersonService;
import cz.zcu.kiv.eegdatabase.data.service.PersonService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 29.3.11
 * Time: 14:05
 */


public class FacebookController extends MultiActionController {
    @Autowired
    private IFBStuffManager fbStuffKeeper;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private HierarchicalMessageSource messageSource;
    @Autowired
    private PersonService personService;

    private FacebookClient facebookClient;
    private Log log = LogFactory.getLog(getClass());

    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView(this.fbStuffKeeper.fbLoginAuthenticate());
        return mav;
    }


    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("social/user");

        /* Getting user�s credentials from Facebook */
        String code = request.getParameter("code");
        String accessToken = fbStuffKeeper.retrieveAccessToken(code);
        facebookClient = new DefaultFacebookClient(accessToken);
        User userFb = facebookClient.fetchObject("me", User.class);

        /* Connecting accounts */
        if (personDao.getLoggedPerson() != null) {
            Person person = personDao.getLoggedPerson();
            if (personDao.fbUidExists(userFb.getId())) {
                /* Account already connected page */
                mav = new ModelAndView("infoPage");
                mav.addObject("message", "infoPage.FacebookDuplicateFbUid");

                return mav;
            } else {
                /* Connect account and display account page */
                person.setFacebookId(userFb.getId());
                personDao.update(person);
                mav = new ModelAndView("redirect:/my-account/overview.html");
                return mav;
            }
        } else {
            /* Login or register user */
            Person person;
            /* Is already stored in application? */
            if (personDao.fbUidExists(userFb.getId())) {
                person = personDao.getPersonByFbUid(userFb.getId());
            } else if (personDao.emailExists(userFb.getEmail())) {
                mav = new ModelAndView("infoPage");
                mav.addObject("message", "infoPage.FacebookEmailExists");
                return mav;
            }
            else {
                person = personService.createPerson(userFb, null);
            }
            GrantedAuthority grantedAuthority = new GrantedAuthorityImpl(person.getAuthority());
            List<GrantedAuthority> grantedAuthorities = Collections.singletonList(grantedAuthority);
            Authentication a = new FacebookAuthenticationToken(grantedAuthorities, person);
            a.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(a);

            mav = new ModelAndView("redirect:/home.html");
            return mav;
        }
    }

}
