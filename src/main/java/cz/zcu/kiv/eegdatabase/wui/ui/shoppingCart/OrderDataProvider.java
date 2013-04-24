package cz.zcu.kiv.eegdatabase.wui.ui.shoppingCart;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * SortableDataProvider implementation for displaying current purchase content.
 * User: jfronek
 * Date: 4.3.2013
 */
public class OrderDataProvider extends SortableDataProvider<Experiment, String> {
    /** List of current purchae content. */
    private List<Experiment> list;

    public OrderDataProvider(List<Experiment> order){
        setSort("experimentId", SortOrder.ASCENDING);
        list = order;

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
        return list.size();
    }

    @Override
    public IModel<Experiment> model(Experiment experiment) {
        return new Model<Experiment>(experiment);
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
