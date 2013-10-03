/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   CheckBoxColumn.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
