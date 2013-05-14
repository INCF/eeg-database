package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 6.5.13
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
public interface IAutoCompletable {

    /**
     * It must return some String by which I can get correct object from DB furthermore
     * it has to be in human readable form, because users will need to choose among these.
     *
     * @return Human readable String representing the object.
     */
    public String getAutoCompleteData();
}
