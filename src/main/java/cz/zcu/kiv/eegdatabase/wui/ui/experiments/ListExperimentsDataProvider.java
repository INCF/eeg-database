package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

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

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;

/**
 * Dataprovider implementation used in table on ListExperimentPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListExperimentsDataProvider extends SortableDataProvider<Experiment, String> {

    private static final long serialVersionUID = -2474056753967062150L;

    protected Log log = LogFactory.getLog(getClass());

    ExperimentsFacade facade;

    private List<Experiment> list;

    private int size;

    public ListExperimentsDataProvider(ExperimentsFacade facade, Person person, boolean owner, boolean subject) {

        this.facade = facade;
        setSort("experimentId", SortOrder.ASCENDING);

        if (owner) {
            size = facade.getCountForExperimentsWhereOwner(person);
            list = facade.getExperimentsWhereOwner(person, (int) 0, (int) size());
        } else if (subject) {
            size = facade.getCountForExperimentsWhereSubject(person);
            list = facade.getExperimentsWhereSubject(person, (int) 0, (int) size());
        } else {
            size = facade.getCountForAllExperimentsForUser(person);
            list = facade.getAllExperimentsForUser(person, (int) 0, (int) size());
        }

    }

    @Override
    public Iterator<? extends Experiment> iterator(long first, long count) {

        if (getSort() != null)
            Collections.sort(list, new ExperimentsDataProviderComparator());

        if (size() < first + count)
            list.subList((int) first, (int) (first + size() - first)).iterator();

        return list.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public IModel<Experiment> model(Experiment object) {
        return new Model<Experiment>(object);
    }

    private class ExperimentsDataProviderComparator implements Comparator<Experiment>, Serializable {
        
        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public int compare(final Experiment o1, final Experiment o2) {
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
