/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AbstractSearchController.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.zcu.kiv.eegdatabase.logic.controller.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
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
            } else if (param.startsWith("condition")) {
                condition.add(param);
            } else {
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
                    (request.getParameter(andOr.get(i - 1)))));
        }
        return mav;
    }


}
