package cz.zcu.kiv.eegdatabase.wui.components.feedback;

import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Base feedback panel for global messages.
 * 
 * @author Jakub Rinkes
 * 
 */
public class BaseFeedbackMessagePanel extends FeedbackPanel {

    private static final long serialVersionUID = 2978096327670025839L;

    public BaseFeedbackMessagePanel(String id) {
        this(id, null);
    }

    public BaseFeedbackMessagePanel(String id, IFeedbackMessageFilter filter) {
        super(id, filter);

        this.setOutputMarkupId(true);
        this.setOutputMarkupPlaceholderTag(true);
    }

}
