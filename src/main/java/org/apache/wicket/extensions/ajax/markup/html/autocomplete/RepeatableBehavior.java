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
