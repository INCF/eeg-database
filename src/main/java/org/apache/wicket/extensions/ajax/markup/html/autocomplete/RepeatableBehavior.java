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
 *   RepeatableBehavior.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.resource.CoreLibrariesContributor;
import org.apache.wicket.util.string.Strings;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 9:37
 */
abstract public class RepeatableBehavior<T> extends AutoCompleteBehavior<T> {
    private static final ResourceReference AUTOCOMPLETE_JS = new JavaScriptResourceReference(
            RepeatableBehavior.class, "wicket-autocomplete.js");

    public RepeatableBehavior(IAutoCompleteRenderer<T> renderer, AutoCompleteSettings settings) {
        super(renderer, settings);
    }

    public RepeatableBehavior(IAutoCompleteRenderer<T> renderer, boolean preselect) {
        super(renderer, preselect);
    }

    public RepeatableBehavior(IAutoCompleteRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    public void renderHead(final Component component, final IHeaderResponse response)
    {
        super.renderHead(component, response);
        CoreLibrariesContributor.contributeAjax(component.getApplication(), response);
        renderAutocompleteHead(response);
    }

    /**
     * Render autocomplete init javascript and other head contributions
     *
     * @param response
     */
    private void renderAutocompleteHead(final IHeaderResponse response)
    {
        response.render(JavaScriptHeaderItem.forReference(AUTOCOMPLETE_JS));
        final String id = getComponent().getMarkupId();

        String indicatorId = findIndicatorId();
        if (Strings.isEmpty(indicatorId))
        {
            indicatorId = "null";
        }
        else
        {
            indicatorId = "'" + indicatorId + "'";
        }

        String initJS = String.format("new Wicket.AutoComplete('%s','%s',%s,%s);", id,
                getCallbackUrl(), constructSettingsJS(), indicatorId);
        response.render(OnDomReadyHeaderItem.forScript(initJS));
    }

    @Override
    protected void respond(AjaxRequestTarget target) {
        final RequestCycle requestCycle = RequestCycle.get();
        String val = requestCycle.getRequest().getRequestParameters().getParameterValue("sv").toString();
//        String val = requestCycle.getRequest().getRequestParameters().getParameterValue("sv").toString();
        if (val != null)
            onSelectionChange(target, val);
        else {
            val = requestCycle.getRequest().getRequestParameters().getParameterValue("q").toString();
//            val = requestCycle.getRequest().getRequestParameters().getParameterValue("q").toString();
            onRequest(val, requestCycle);
        }
    }

    protected abstract void onSelectionChange(AjaxRequestTarget target, String val);
}
