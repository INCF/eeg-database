package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import org.apache.wicket.model.IModel;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 9:59
 */
public class GenericModel<T> implements IModel<T>, Serializable {
    protected T modelObject;

    public GenericModel(T modelObject){
        this.modelObject = modelObject;
    }

    @Override
    public T getObject() {
        return modelObject;
    }

    @Override
    public void setObject(T modelObject) {
        this.modelObject = modelObject;
    }

    @Override
    public void detach() {
    }
}
