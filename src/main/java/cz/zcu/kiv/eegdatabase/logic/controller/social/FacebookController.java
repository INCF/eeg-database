package cz.zcu.kiv.eegdatabase.logic.controller.social;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.logic.util.ControllerUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Random;

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

    private FacebookClient facebookClient;
    private Log log = LogFactory.getLog(getClass());

    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView(this.fbStuffKeeper.fbLoginAuthenticate());
        return mav;
    }


    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("social/user");

        /* Getting user´s credentials from Facebook */
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
                /* Creates new user and stores him into DB */
                person = new Person();
                person.setGivenname(userFb.getFirstName());
                person.setSurname(userFb.getLastName());
                person.setGender(userFb.getGender().toUpperCase().charAt(0));
                person.setEmail(userFb.getEmail());
                person.setDateOfBirth(new Timestamp(userFb.getBirthdayAsDate().getTime()));

                String username = userFb.getLastName();
                // Removing the diacritical marks
                String decomposed = Normalizer.normalize(username, Normalizer.Form.NFD);
                username = decomposed.replaceAll("[^\\p{ASCII}]", "");

                String tempUsername = username;
                while (personDao.usernameExists(tempUsername)) {
                    Random random = new Random();
                    int number = random.nextInt(999) + 1;  // not many users are expected to have the same name and surname, so 1000 numbers is enough
                    tempUsername = username + "-" + number;
                }
                username = tempUsername;
                person.setUsername(username);
                String password = ControllerUtils.getRandomPassword();
                person.setPassword(ControllerUtils.getMD5String(password));
                log.debug("Setting authority to ROLE_USER");
                person.setAuthority("ROLE_USER");
                person.setConfirmed(true);
                person.setFacebookId(userFb.getId());
                personDao.create(person);
            }
            GrantedAuthority[] grantedAuthorities = new GrantedAuthorityImpl[1];
            grantedAuthorities[0] = new GrantedAuthorityImpl(person.getAuthority());
            Authentication a = new FacebookAuthenticationToken(grantedAuthorities, person);
            a.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(a);

            mav = new ModelAndView("redirect:/home.html");
            return mav;
        }
    }

}
