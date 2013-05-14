package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 13.5.13
 * Time: 18:06
 */
public class AjaxFeedbackUpdatingBehavior extends AjaxFormComponentUpdatingBehavior {
    private FeedbackPanel feedback;

    /**
     * Construct.
     *
     * @param event event to trigger this behavior
     */
    public AjaxFeedbackUpdatingBehavior(String event,
                                        FeedbackPanel feedback) {
        super(event);

        this.feedback = feedback;
    }

    @Override
    protected void onUpdate(AjaxRequestTarget target) {
        target.add(feedback);
    }

    @Override
    protected void onError(AjaxRequestTarget target, RuntimeException ex){
        super.onError(target,ex);
        target.add(feedback);
    }
}
