/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.history;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 *
 * @author pbruha
 */
public class WeeklyHistoryController extends AbstractController {
   private Log log = LogFactory.getLog(getClass());

    public WeeklyHistoryController() {
    }
    
    protected ModelAndView handleRequestInternal(
            HttpServletRequest request, 
            HttpServletResponse response) throws Exception {
         ModelAndView mav = new ModelAndView("history/weeklyHistory");
         return mav;
    }

}