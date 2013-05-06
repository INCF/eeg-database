package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 6.5.13
 * Time: 10:24
 */
abstract public class AddingItemBehavior<T> extends AutoCompleteBehavior<T> {
    public AddingItemBehavior(IAutoCompleteRenderer<T> renderer, AutoCompleteSettings settings) {
        super(renderer, settings);
    }

    public AddingItemBehavior(IAutoCompleteRenderer<T> renderer, boolean preselect) {
        super(renderer, preselect);
    }

    public AddingItemBehavior(IAutoCompleteRenderer<T> renderer) {
        super(renderer);
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
