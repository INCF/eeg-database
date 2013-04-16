package cz.zcu.kiv.eegdatabase.wui.components.table;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Checkbox column from the book Apache Wicket Cookbook by Igor Vaynberg.
 *
 * @author Jakub Danek
 */
public abstract class CheckBoxColumn <T, S> extends AbstractColumn <T, S> {

    private IModel<Boolean> selectAllModel = new Model<Boolean>();

    /**
     *
     * @param displayModel Column header
     */
    public CheckBoxColumn(IModel<String> displayModel) {
	super(displayModel);
    }

    @Override
    public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
	cellItem.add(new CheckPanel(componentId, newCheckBoxModel(rowModel)));
    }

    /**
     * Override this to use a checkbox component implementation different from the
     * default (AjaxCheckBox with no onUpdate action).
     * @param id id for the component
     * @param checkModel boolean model for the checkbox
     * @return the checkbox
     */
    protected CheckBox newCheckBox(String id, IModel<Boolean> checkModel) {
	return new AjaxCheckBox(id, checkModel) {

	    @Override
	    protected void onUpdate(AjaxRequestTarget target) {
		//do nothing
	    }

	};
    }

    /**
     * Implement this method to provide model for the checkbox. It should
     * also handle any related actions - e.g. maintaining set of selected items.
     *
     * See AbstractCheckboxModel.
     * @param rowModel model of the row the checkbox is displayed on
     * @return model for the checkbox
     */
    protected abstract IModel<Boolean> newCheckBoxModel(IModel<T> rowModel);

    /**
     * Panel with checkbox. Just for convenience (markup provided).
     */
    private class CheckPanel extends Panel {

	public CheckPanel(String id, IModel<Boolean> model) {
	    super(id);
	    add(newCheckBox("check", model));
	}

    }
}
