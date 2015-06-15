/***********************************************************************************************************************
 *
 * This file is part of the EEG-database project
 *
 * =============================================
 *
 * Copyright (C) 2015 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 ***********************************************************************************************************************
 *
 * BootstrapAjaxNavigationToolbar.java, 15. 6. 2015 11:07:04, Jakub Krauz
 *
 **********************************************************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.components.repeater;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigatorLabel;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import de.agilecoders.wicket.core.markup.html.bootstrap.navigation.ajax.BootstrapAjaxPagingNavigator;

/**
 * Toolbar that displays (Ajax) navigator styled with Bootstrap.
 * 
 * @author Jakub Krauz
 */
public class BootstrapAjaxNavigationToolbar extends NavigationToolbar {

    private static final long serialVersionUID = 1L;


    /**
     * Constructor
     * 
     * @param table
     *            data table this toolbar will be attached to
     */
    public BootstrapAjaxNavigationToolbar(final DataTable<?, ?> table) {
        super(table);
    }
    
    
    /** {@inheritDoc} */
    @Override
    protected PagingNavigator newPagingNavigator(final String navigatorId, final DataTable<?, ?> table)
    {
        return new BootstrapAjaxPagingNavigator(navigatorId, table)
        {
            private static final long serialVersionUID = 1L;

            /**
             * Implement our own ajax event handling in order to update the datatable itself, as the
             * default implementation doesn't support DataViews.
             * 
             * @see AjaxPagingNavigator#onAjaxEvent(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            protected void onAjaxEvent(final AjaxRequestTarget target)
            {
                target.add(table);
            }
        };
    }
    

    /** {@inheritDoc} */
    @Override
    protected WebComponent newNavigatorLabel(final String navigatorId, final DataTable<?, ?> table)
    {
        WebComponent navigatorLabel = new NavigatorLabel(navigatorId, table);
        navigatorLabel.setVisible(false);  // don't show the navigator label
        return navigatorLabel;
    }


}
