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
 *   InternalErrorPage.java, 2014/03/15 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.page;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.http.WebResponse;

import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.ui.home.HomePage;

/**
 * Internal error page for EEGDatabase, with header, footer and menu section.
 * 
 * @author Jakub Rinkes
 * 
 */
public class InternalErrorPage extends MenuPage {

    private static final long serialVersionUID = 1L;

    public InternalErrorPage() {
        super();

        setPageTitle(ResourceUtils.getModel("title.page.internal.error"));
        add(new BookmarkablePageLink<MenuPage>("goToHomeLink", HomePage.class));
    }

    @Override
    protected void setHeaders(final WebResponse response) {

        super.setHeaders(response);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public boolean isErrorPage() {

        return true;
    }
}
