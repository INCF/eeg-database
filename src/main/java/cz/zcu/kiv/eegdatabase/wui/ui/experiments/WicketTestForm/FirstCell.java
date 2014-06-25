package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Created by Prokop on 25.6.2014.
 */
public class FirstCell extends Panel {

    private RowData data;
    private String id;

    public FirstCell(final RowData data, final String id) {
        super(id);
        this.id = id;
        this.data = data;

        CheckBox box = new CheckBox("check", Model.of(Boolean.TRUE));
        add(box);
        add(new Label("name", data.getName()));
    }
}
