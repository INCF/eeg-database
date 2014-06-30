package cz.zcu.kiv.eegdatabase.wui.ui.experiments.WicketTestForm;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prokop on 25.6.2014.
 */
public class SectionCell extends Panel {

    private String id;

    public SectionCell(final String id, IModel model) {
        super(id);
        this.id = id;
        RowData data = (RowData)model.getObject();
        List<Integer> nums = new ArrayList<Integer>();
        final int maxCount = data.getMaxCount();
        nums.add(1);

        for(int i = 2; i <= maxCount; i++)
        {
            nums.add(i);
        }
        final DropDownChoice dropDownChoice = new DropDownChoice("maxCount", new Model(nums.get(0)), nums);

        add(dropDownChoice);

        final CheckBox box = new CheckBox("check", Model.of(Boolean.TRUE)){
            @Override
            protected void onConfigure() {
                super.onConfigure();
                dropDownChoice.setVisible(this.getModelObject());
            }
        };
        box.setEnabled(!data.getRequired());

/*        OnChangeAjaxBehavior standardChangeListener = new OnChangeAjaxBehavior() {
            protected void onUpdate(AjaxRequestTarget target) {
                dropDownChoice.setEnabled(box.getModelObject());
                boolean b = dropDownChoice.isEnabled();
            }
        };*/
        //box.add(standardChangeListener);
        add(box);
        add(new Label("name"));
    }
}
