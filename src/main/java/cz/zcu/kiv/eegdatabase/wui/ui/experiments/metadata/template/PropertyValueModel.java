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
 *   PropertyValueModel.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.Serializable;

import odml.core.Property;
import odml.core.Value;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.Model;

/**
 * Model for working with {@link Value} in {@link Property}.
 * Model is used in {@link TemplateForm} for property with values in template.
 * 
 * Because we can't work directly with {@link Value} objects. We work with methods of {@link Property}.
 * 
 * @author Jakub Rinkes
 * 
 */
public class PropertyValueModel extends Model<Serializable> {

    protected Log log = LogFactory.getLog(getClass());

    private static final long serialVersionUID = 1L;

    private Property property;
    private int valueIndex;

    public PropertyValueModel(Property property) {
        this(property, 0);
    }

    public PropertyValueModel(Property property, int valueIndex) {
        this.property = property;
        this.valueIndex = valueIndex;
    }

    @Override
    public Serializable getObject() {

        if (property.valueCount() == 0)
            return null;

        return (Serializable) property.getValue(valueIndex);
    }

    @Override
    public void setObject(Serializable object) {

        if (property.valueCount() == 0) {
            property.addValue(object);
            return;
        }

        if ((object instanceof String && ((String) object).equalsIgnoreCase("...")) || object == null) {
            // fixed default empty value from AjaxEditableLabel - default value is cleared - textfield will be empty.
            // fixed posible NullPointerException on Property.setValueAt(Object value, int index) - line 903
            property.setValueAt("", valueIndex);
        } else {
            property.setValueAt(object, valueIndex);
        }
    }

}
