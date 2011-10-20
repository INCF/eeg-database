
package cz.zcu.kiv.eegdatabase.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.web.servlet.view.tiles.ComponentControllerSupport;

/**
 *
 * @author JiPER
 */
public class LoggedUserController extends ComponentControllerSupport {

    @Override
    protected void doPerform(ComponentContext context, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = null;
        if (o instanceof UserDetails) {
            userName = ((UserDetails) o).getUsername();
        } else {
            userName = o.toString();
        }
        context.putAttribute("loggedUserName", userName);
    }

}
