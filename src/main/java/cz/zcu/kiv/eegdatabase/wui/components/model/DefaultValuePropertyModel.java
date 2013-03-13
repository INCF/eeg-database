package cz.zcu.kiv.eegdatabase.wui.components.model;

import java.io.Serializable;

import org.apache.wicket.model.PropertyModel;

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
