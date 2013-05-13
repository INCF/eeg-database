package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 10:03
 */
public class GenericFactory<T> implements IFactory<T>, Serializable {
    protected Class<T> clazz;

    public GenericFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T create() {
        try {
            return clazz.newInstance();
        } catch(Exception ex){
            return null;
        }
    }

    @Override
    public Class<T> getClassForConverter() {
        return clazz;
    }
}
