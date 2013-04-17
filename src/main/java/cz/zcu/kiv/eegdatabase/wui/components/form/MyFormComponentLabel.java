package cz.zcu.kiv.eegdatabase.wui.components.form;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.LabeledWebMarkupContainer;
import org.apache.wicket.markup.html.form.SimpleFormComponentLabel;

/**
 *
 * @author Jakub Danek
 */
public class MyFormComponentLabel extends SimpleFormComponentLabel {

    public MyFormComponentLabel(String id, LabeledWebMarkupContainer labelProvider) {
        super(id, labelProvider);
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        super.onComponentTag(tag);
        //FormComponentPanel arent represented by form contro tags in the markup
        //specifying 'for' attribute for the label would result in invalid html
        if(this.getFormComponent() instanceof FormComponentPanel) {
            tag.remove("for");
        }
    }



}