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
 *   PageParametersUtils.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.utils;

import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Utilities class for page parameters.
 * 
 * @author Jakub Rinkes
 * 
 */
public class PageParametersUtils {

    public static final String GROUP_PARAM = "GROUP";
    public static final String METHOD_NAME = "METHOD_NAME";
    public static final String DATA = "DATA";

    /**
     * Created page parameters with parameter key and object
     * 
     * @param key
     *            name of parameter
     * @param object
     *            value of parameter
     * @return page parameter object with parameter: key = object.
     */
    public static PageParameters getPageParameters(String key, Object object) {

        PageParameters param = new PageParameters();
        param.add(key, object);

        return param;
    }

    /**
     * Created page parameter with default parameter key and object. Default parameter is in BasePage.class.
     * 
     * @param object
     *            value of parameter
     * @return return page parameters with parameter: DEFAULT_PARAM_ID = object.
     */
    public static PageParameters getDefaultPageParameters(Object object) {

        return getPageParameters(BasePage.DEFAULT_PARAM_ID, object);
    }

    public static PageParameters addParameters(PageParameters parameters,
            String key, Object object) {

        parameters.add(key, object);
        return parameters;
    }

    /**
     * Get url for page and parameter from wicket. Wicket will generate absolute url for this page.
     * 
     * @param page
     * @param parameters
     * @return
     */
    public static String getUrlForPage(Class page, PageParameters parameters) {

        return RequestCycle.get().getUrlRenderer().renderFullUrl(
                Url.parse(RequestCycle.get().urlFor(page, parameters).toString()));
    }
}
