package cz.zcu.kiv.eegdatabase.wui.components.repeater;

import java.io.Serializable;
import java.util.ArrayList;
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

/**
 * Generic implementation of SortableDataProvider. Exposes listModel variable
 * to inheriting classes for backing list model setting.
 *
 * @author Jakub Danek
 */
public class BasicDataProvider<T extends Serializable> extends SortableDataProvider<T, String> {

	private static final long serialVersionUID = -2474056753967062150L;

    protected Log log = LogFactory.getLog(getClass());

	protected IModel<List<T>> listModel = new Model(new ArrayList());

	public BasicDataProvider(String defSortPar, SortOrder order) {
		setSort(defSortPar, order);
	}

	/**
     * Constructor which wraps a list of data.
     * @param data
     */
    public BasicDataProvider(List<T> data, String defSortPar, SortOrder order) {
		this(defSortPar, order);
		this.listModel.setObject(data);
    }

    /**
     * Constructor which allows user to provide his own list model. Suitable
     * when you need to update the model from the outside (e.g. with ajax and
     * LoadableDetachableModel).
     * @param dataModel
     */
    public BasicDataProvider(IModel<List<T>> dataModel, String defSortPar, SortOrder order) {
		this(defSortPar, order);
		this.listModel = dataModel;
    }

    @Override
    public Iterator<? extends T> iterator(long first, long count) {

        if (getSort() != null) {
            Collections.sort(listModel.getObject(), new BasicDataProviderComparator());
		}

        if (size() < first + count) {
            listModel.getObject().subList((int) first, (int) (first + size() - first)).iterator();
		}

        return listModel.getObject().subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return listModel.getObject().size();
    }

    @Override
    public IModel<T> model(T object) {
        return new Model(object);
    }

    private class BasicDataProviderComparator implements Comparator<T>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public int compare(final T o1, final T o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1, getSort().getProperty());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty());

            int result = 0;

            if (model1.getObject() == null){
                result = -1;
			}
            else if (model2.getObject() == null) {
                result = 1;
			}
            else if (model1.getObject() != null && model2.getObject() != null) {
                result = model1.getObject().compareTo(model2.getObject());
			}

            if (!getSort().isAscending()) {
                result = -result;
            }

            return result;
        }

    }

}
