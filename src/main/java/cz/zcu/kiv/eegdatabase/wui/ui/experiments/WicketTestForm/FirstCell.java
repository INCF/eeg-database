package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import org.apache.velocity.util.ArrayListWrapper;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prokop on 25.6.2014.
 */
public class FirstCell extends Panel {

    private String id;

    public FirstCell(final String id, IModel model) {
        super(id);
        this.id = id;
        RowData data = (RowData)model.getObject();
        CheckBox box = new CheckBox("check", Model.of(Boolean.TRUE));
        box.setVisible(!data.getRequired());
        add(box);
        add(new Label("name"));
        List<Integer> nums = new ArrayList<Integer>();
        int maxCount = data.getMaxCount();
        for(int i = 0; i < maxCount; i++)
        {
           nums.add(i);
        }
        DropDownChoice dropDownChoice = new DropDownChoice("maxCount", nums);
        add(dropDownChoice);
    }
}
