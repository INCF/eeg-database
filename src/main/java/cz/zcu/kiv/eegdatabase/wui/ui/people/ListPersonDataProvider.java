package cz.zcu.kiv.eegdatabase.wui.ui.people;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

/**
 * Dataprovider implementation used in table on ListPersonPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListPersonDataProvider extends SortableDataProvider<Person, String> {

    private static final long serialVersionUID = -5120095037204498930L;

    protected Log log = LogFactory.getLog(getClass());

    PersonFacade facade;

    private List<Person> list;

    private int size;

    public ListPersonDataProvider(PersonFacade facade) {

        this.facade = facade;
        setSort("personId", SortOrder.ASCENDING);

        size = facade.getCountForList();
        list = facade.getDataForList(0, size);

    }

    @Override
    public Iterator<? extends Person> iterator(long first, long count) {

        if (getSort() != null)
            Collections.sort(list, new PersonDataProviderComparator());

        if (size() < first + count)
            list.subList((int) first, (int) (first + size() - first)).iterator();

        return list.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public IModel<Person> model(Person object) {
        return new Model<Person>(object);
    }

    private class PersonDataProviderComparator implements Comparator<Person>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public int compare(final Person o1, final Person o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());

            int result = 0;

            if (model1.getObject() == null)
                result = -1;
            else if (model2.getObject() == null)
                result = 1;
            else if (model1.getObject() != null && model2.getObject() != null)
                result = model1.getObject().compareTo(model2.getObject());

            if (!getSort().isAscending()) {
                result = -result;
            }

            return result;
        }

    }
}
