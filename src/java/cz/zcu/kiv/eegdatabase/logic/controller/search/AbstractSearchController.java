/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 *
 * @author Honza
 */
public class AbstractSearchController extends SimpleFormController {

  protected Log log = LogFactory.getLog(getClass());

  protected List<SearchRequest> requests;

  protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
    logger.debug("Search scenario controller");
    ModelAndView mav = new ModelAndView(getSuccessView());

    List<String> source = new ArrayList<String>();
    List<String> condition = new ArrayList<String>();
    List<String> andOr = new ArrayList<String>();
    Enumeration enumer = request.getParameterNames();
    while (enumer.hasMoreElements()) {
      String param = (String) enumer.nextElement();
      if (param.startsWith("source")) {
        source.add(param);
      }
      else if (param.startsWith("condition")) {
        condition.add(param);
      }
      else {
        andOr.add(param);
      }
    }
    Collections.sort(andOr);
    Collections.sort(source);
    Collections.sort(condition);
    requests = new ArrayList<SearchRequest>();
    requests.add(new SearchRequest(request.getParameter(condition.get(0)),
            (request.getParameter(source.get(0))), ""));
    for (int i = 1; i < condition.size(); i++) {
          requests.add(new SearchRequest(request.getParameter(condition.get(i)),
            (request.getParameter(source.get(i))),
            (request.getParameter(andOr.get(i-1)))));
    }
    return mav;
  }


}
