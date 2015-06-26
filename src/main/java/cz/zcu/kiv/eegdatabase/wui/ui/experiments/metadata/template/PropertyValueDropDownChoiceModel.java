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
 *   PropertyValueDropDownChoiceModel.java, 2015/06/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.Serializable;

import odml.core.Property;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.Model;

/**
 * Wicket model used in {@link DropDownChoice} for select {@link Value} from list in {@link Property}.
 * Because we can't work directly with {@link Value} objects. We work with methods of {@link Property}.
 * 
 * Selected {@link Value} is always on index 0 in vector of objects.
 * Model swap {@link Value} at index 0 with new selected {@link value}. Old {@link Value} is stored on index
 * of new selected item.
 * 
 * @author Jakub Rinkes
 * 
 */
public class PropertyValueDropDownChoiceModel extends Model<Serializable> {

    private static final long serialVersionUID = 1L;

    private Property property;

    public PropertyValueDropDownChoiceModel(Property property) {
        this.property = property;
    }

    @Override
    public Serializable getObject() {

        if (property.valueCount() == 0)
            return null;

        return (Serializable) property.getValue();
    }

    @Override
    public void setObject(Serializable object) {

        int oldIndex = property.getValueIndex(object);
        Object oldValue = property.getValue(0);
        property.setValueAt(object, 0);
        property.setValueAt(oldValue, oldIndex);
    }

}
