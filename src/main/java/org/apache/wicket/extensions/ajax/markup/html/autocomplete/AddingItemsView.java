package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 5.5.13
 * Time: 20:40
 */
public class AddingItemsView<T> extends AutoCompleteTextField<T> {
    @SpringBean
    private PersonFacade personFacade;

    private Integer AUTOCOMPLETE_ROWS = 10;
    private ListView listView;
    private MarkupContainer container;

    public AddingItemsView(String id, IModel model,
                           final ListView listView, final MarkupContainer container) {
        super(id, model);

        setRequired(true);
        setOutputMarkupId(true);
        this.listView = listView;
        this.container = container;
    }

    @Override
    protected Iterator getChoices(String input) {
        if (Strings.isEmpty(input))
        {
            List<String> emptyList = Collections.emptyList();
            return emptyList.iterator();
        }
        List<String> choices = new ArrayList<String>(AUTOCOMPLETE_ROWS);
        List<Person> persons = personFacade.getAllRecords();
        for (final Person p : persons)
        {
            final String data = p.getGivenname() +" "+ p.getSurname();
            if (data.toUpperCase().startsWith(input.toUpperCase()))
            {
                choices.add(data);
                if (choices.size() == AUTOCOMPLETE_ROWS) break;
            }
        }
        return choices.iterator();
    }

    protected void onSelectionChange(AjaxRequestTarget target, String newValue){
        getModel().setObject((T) newValue);
        List<Person> persons = listView.getList();
        boolean add = true;
        for(Person person: persons) {
            if(person.getSurname().equals("")){
                add = false;
            }
        }
        if(add){
            listView.getModelObject().add(new Person());
            target.add(container);
        }
    }

    @Override
    protected AutoCompleteBehavior<T> newAutoCompleteBehavior(
            final IAutoCompleteRenderer<T> renderer, final AutoCompleteSettings settings) {
        return new AddingItemBehavior<T>(renderer, settings) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<T> getChoices(String input) {
                return AddingItemsView.this.getChoices(input);
            }

            @Override
            protected void onSelectionChange(AjaxRequestTarget target, String newValue) {
                AddingItemsView.this.onSelectionChange(target, newValue);
            }
        };
    }
}
