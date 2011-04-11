package cz.zcu.kiv.eegdatabase.logic.controller.social;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.Normalizer;

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

        /* Getting user¥s credentials from Facebook */
        String code = request.getParameter("code");
        String accessToken = fbStuffKeeper.retrieveAccessToken(code);
        facebookClient = new DefaultFacebookClient(accessToken);
        User userFb = facebookClient.fetchObject("me", User.class);

        /* Is already stored in application? */
        if (personDao.fbUidExists(userFb.getId())) {

        }
        /* New FB user to application */
        if (personDao.emailExists(userFb.getEmail())) {
            // TODO: prompt user to connect accounts in the administration
        } else {

        }
        String bez = Normalizer.normalize("ûluùouËk˝", Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        mav.addObject("facebookProfile", userFb);
        mav.addObject("picture", userFb.getId());
        return mav;
    }

}
