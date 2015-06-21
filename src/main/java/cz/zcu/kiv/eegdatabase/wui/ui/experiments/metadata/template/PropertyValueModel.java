package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata.template;

import java.io.Serializable;

import odml.core.Property;
import odml.core.Value;

import org.apache.wicket.model.Model;

/**
 * Model for working with {@link Value} in {@link Property}.
 * 
 * Value doesn't have visible getters/setters. This model work with Value via Property instance.
 * 
 * @author Jakub Rinkes
 *
 */
public class PropertyValueModel extends Model<Serializable> {

    private static final long serialVersionUID = 1L;

    private Property property;
    private int valueIndex;

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
        
        
        if (object instanceof String && ((String) object).equalsIgnoreCase("...")) {
            // fixed default empty value from AjaxEditableLabel - default value is cleared - textfield will be empty.
            property.setValueAt("", valueIndex);
        } else {
            property.setValueAt(object, valueIndex);
        }
    }

}
