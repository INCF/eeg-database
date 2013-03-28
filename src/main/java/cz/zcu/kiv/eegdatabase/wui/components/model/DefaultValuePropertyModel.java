package cz.zcu.kiv.eegdatabase.wui.components.model;

import java.io.Serializable;

import org.apache.wicket.model.PropertyModel;

/**
 * Implementation of wicket model with default value. If model is null then return default value.
 * 
 * @author Jakub Rinkes
 * 
 * @param <T>
 *            type of model object.
 */
public class DefaultValuePropertyModel<T extends Serializable> extends PropertyModel<T> {

    private static final long serialVersionUID = 1L;
    private T defaultValue;

    public DefaultValuePropertyModel(Object object, String propertyKey, T defaultValue) {
        
        super(object, propertyKey);
        this.defaultValue = defaultValue;
    }

    @Override
    public T getObject() {
        
        T object = super.getObject();
        return object != null ? object : defaultValue;
    }

}
