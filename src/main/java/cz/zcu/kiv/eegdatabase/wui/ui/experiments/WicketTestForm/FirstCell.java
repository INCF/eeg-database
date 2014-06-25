package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import org.apache.wicket.markup.html.panel.Panel;

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


    }
}
