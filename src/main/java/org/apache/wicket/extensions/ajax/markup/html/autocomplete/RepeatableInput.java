package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 11.5.13
 * Time: 9:28
 */
public class RepeatableInput<T extends IAutoCompletable> extends AutoCompleteTextField<T> {
    private final int AUTO_COMPLETE_CHOICES = 10;
    protected GenericFacade<T, Integer> service;

    public RepeatableInput(String id,
                           IModel<T> model,
                           Class<T> type,
                           GenericFacade<T, Integer> service) {
        super(id, model, type, new AbstractAutoCompleteTextRenderer<T>() {
            @Override
            protected String getTextValue(T object) {
                return object.getAutoCompleteData();
            }
        }, new AutoCompleteSettings());

        this.service = service;

        setOutputMarkupId(true);
    }

    @Override
    protected Iterator<T> getChoices(String input) {
        if(Strings.isEmpty(input)){
            return new ArrayList<T>().iterator();
        }
        List<T> allChoices = service.getAllRecords();
        List<T> choices = new ArrayList<T>(AUTO_COMPLETE_CHOICES);
        for(T t: allChoices) {
            if((t.getAutoCompleteData() != null) &&
                    t.getAutoCompleteData().toLowerCase().startsWith(input.toLowerCase())){
                choices.add(t);
            }
            if(choices.size() >= AUTO_COMPLETE_CHOICES) {
                break;
            }
        }
        return choices.iterator();
    }
}
