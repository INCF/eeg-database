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
 *   ExceptionResolver.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.logic.controller.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Extends Spring MVC SimpleMappingExceptionResolver to log all received exceptions
 * Created by IntelliJ IDEA.
 * User: jnovotny
 */
public class ExceptionResolver extends org.springframework.web.servlet.handler.SimpleMappingExceptionResolver {
        private Log log = LogFactory.getLog(getClass());


    @Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler,	Exception ex) {

        log.warn("An exception occured", ex);//Log the exception
        ex.printStackTrace();//Show also in the error output

        return super.doResolveException(request,response,handler,ex);
    }
}
