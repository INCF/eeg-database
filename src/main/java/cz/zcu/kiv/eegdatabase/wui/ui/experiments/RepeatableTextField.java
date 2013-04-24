package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 23.4.13
 * Time: 15:30
 */
public class RepeatableTextField extends RepeatingView {
    public RepeatableTextField(String id) {
        super(id);
    }

    public RepeatableTextField(String id, IModel<?> model) {
        super(id, model);
    }

    protected void populateItem(ListItem item) {

    }
}
