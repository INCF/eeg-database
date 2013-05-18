package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericModel;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 18.5.13
 * Time: 18:31
 */
public class AjaxAutoCompletableUpdatingBehavior extends AjaxFeedbackUpdatingBehavior{

    private GenericValidator validator;
    private GenericModel model;

    public AjaxAutoCompletableUpdatingBehavior(String event,
                                               FeedbackPanel feedback,
                                               GenericValidator validator,
                                               GenericModel model) {
        super(event, feedback);
        this.validator = validator;
        this.model = model;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        super.onUpdate(target);
        validator.setAutocompleteObject(model.getObject());
    }
}
