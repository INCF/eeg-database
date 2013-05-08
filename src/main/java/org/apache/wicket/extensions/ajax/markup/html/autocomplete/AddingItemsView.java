package org.apache.wicket.extensions.ajax.markup.html.autocomplete;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import cz.zcu.kiv.eegdatabase.wui.core.GenericFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.opensaml.saml2.metadata.GivenName;
import sun.rmi.runtime.Log;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 5.5.13
 * Time: 20:40
 */
public class AddingItemsView<T> extends AutoCompleteTextField<T> {

    @SpringBean
    private PersonFacade personFacade;
    @SpringBean
    private StimulusFacade stimulusFacade;
    @SpringBean
    private PharmaceuticalFacade pharmaceuticalFacade;

    private Integer AUTOCOMPLETE_ROWS = 10;
    private ListView listView;
    private MarkupContainer container;
    private boolean newOne = false;
    Class<? extends IAutocompletable> modelClass;

    public AddingItemsView(String id, IModel<T> model,
                           final ListView listView, final MarkupContainer container,
                           Class<? extends IAutocompletable> modelClass) {
        super(id, model);

        this.modelClass = modelClass;
        System.out.println("Model class: " + this.modelClass);

        setRequired(true);
        setOutputMarkupId(true);
        this.listView = listView;
        this.container = container;
         add(new AjaxEventBehavior("onblur") {
             @Override
             protected void onEvent(AjaxRequestTarget target) {
                 if(newOne){
                     newOne = false;
                     target.add(container);
                 }
             }
         });
    }

    @Override
    protected Iterator getChoices(String input) {
        List<String> list = Collections.emptyList();
        if (Strings.isEmpty(input)){
            return list.iterator();
        }

        List<String> choices = new ArrayList<String>(AUTOCOMPLETE_ROWS);

        // TODO GenericFacade facade = modelClass.newInstance().getFacade();
        GenericFacade facade = getFacade(modelClass);
        if(facade == null){
            System.out.println("Type has not been added into getFacade() method");
            return list.iterator();
        }
        List<IAutocompletable> items = facade.getAllRecords();

        for (IAutocompletable item : items) {
            String data = item.getAutocompleteData();
            if (data.toUpperCase().startsWith(input.toUpperCase())) {
                choices.add(data);
                if (choices.size() == AUTOCOMPLETE_ROWS) break;
            }
        }
        return choices.iterator();
    }

    protected void onSelectionChange(AjaxRequestTarget target, String newValue){
        getModel().setObject((T) newValue);
        List<IAutocompletable> items = listView.getList();
        boolean add = true;
        for(IAutocompletable item: items) {
            if(!item.isValidOnChange()){
                add = false;
            }
        }
        if(add){
            try {
                listView.getModelObject().add(modelClass.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            newOne = true;
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

    /* TODO this method should be replaced by IAutocompletable.getFacade()
    * which does not work at this time because facades in IAutocompletable
    * are not properly autowired */
    private GenericFacade getFacade(Class<? extends IAutocompletable> c){
        if(c.equals(Person.class))
            return personFacade;
        if(c.equals(Stimulus.class))
            return stimulusFacade;
        if(c.equals(Pharmaceutical.class))
            return pharmaceuticalFacade;
        return null;
    }
}
