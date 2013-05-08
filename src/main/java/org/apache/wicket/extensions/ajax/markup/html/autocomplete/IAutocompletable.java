package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 6.5.13
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
public interface IAutocompletable {

    public String getAutocompleteData();

    public GenericFacade getFacade();

    public boolean isValidOnChange();
}
