package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Created by Prokop on 25.6.2014.
 */
public class FirstCell extends Panel {

    private String id;

    public FirstCell(final String id, IModel model) {
        super(id);
        this.id = id;

        CheckBox box = new CheckBox("check", Model.of(Boolean.TRUE));
        add(box);
        add(new Label("name"));
    }
}
