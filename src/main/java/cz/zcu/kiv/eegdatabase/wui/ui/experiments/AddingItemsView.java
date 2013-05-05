package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
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
public class AddingItemsView<T> extends AutoCompleteTextField {
    @SpringBean
    private PersonFacade personFacade;

    private Integer AUTOCOMPLETE_ROWS = 10;

    public AddingItemsView(String id, IModel model,
                           final ListView listView, final MarkupContainer container) {
        super(id, model);

        setRequired(true);
        setOutputMarkupId(true);
        add(new AjaxEventBehavior("onblur") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                listView.getModelObject().add(1);
                target.add(container);
            }
        });
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
}
