package cz.zcu.kiv.eegdatabase.logic.controller.social;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.User;
import org.springframework.beans.factory.annotation.Autowired;
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
    private FacebookClient facebookClient;

    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView(this.fbStuffKeeper.fbLoginAuthenticate());
        return mav;
    }


    public ModelAndView user(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("social/user");
        String code = request.getParameter("code");
        String accessToken = fbStuffKeeper.retrieveAccessToken(code);
        facebookClient = new DefaultFacebookClient(accessToken);
        User user = facebookClient.fetchObject("me", User.class);
        mav.addObject("facebookProfile",user);
        mav.addObject("picture", user.getId());
        return mav;
    }

}
