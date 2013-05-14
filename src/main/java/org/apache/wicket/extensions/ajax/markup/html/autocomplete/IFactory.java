package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 9:23
 */
public interface IFactory<T> {
    T create();

    Class<T> getClassForConverter();
}
