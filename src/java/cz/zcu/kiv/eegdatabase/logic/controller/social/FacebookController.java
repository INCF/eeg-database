package cz.zcu.kiv.eegdatabase.logic.controller.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.FacebookProfile;
import org.springframework.social.facebook.FacebookTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Jiri Vlasimsky (vlasimsky.jiri@gmail.com)
 * Date: 29.3.11
 * Time: 14:05
 */


public class FacebookController extends MultiActionController{
    @Autowired
    private IFBStuffManager fbStuffKeeper;

    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("social/login");
        this.fbStuffKeeper.fbLoginAuthenticate();
        return mav;
    }


    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String accessToken = fbStuffKeeper.retrieveAccessToken(code);
        ModelAndView mav = new ModelAndView("social/user");
        FacebookTemplate facebook = new FacebookTemplate(accessToken);
        mav.addObject("picture", facebook.getUserProfile());
        FacebookProfile profile = facebook.getUserProfile();
        mav.addObject("facebookProfile", profile);
        mav.addObject("accessToken", accessToken);
        return mav;
    }

}
