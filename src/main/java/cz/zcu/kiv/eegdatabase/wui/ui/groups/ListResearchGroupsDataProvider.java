package cz.zcu.kiv.eegdatabase.wui.ui.groups;

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

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

/**
 * Dataprovider implementation used in table on ListResearchGroupPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListResearchGroupsDataProvider extends SortableDataProvider<ResearchGroup, String> {

    private static final long serialVersionUID = -2474056753967062150L;

    protected Log log = LogFactory.getLog(getClass());

    ResearchGroupFacade facade;

    private List<ResearchGroup> list;

    private int size;

    public ListResearchGroupsDataProvider(ResearchGroupFacade facade) {

        this.facade = facade;
        setSort("title", SortOrder.ASCENDING);

        size = facade.getCountForList();
        list = facade.getGroupsForList(0, size);

    }

    @Override
    public Iterator<? extends ResearchGroup> iterator(long first, long count) {

        if (getSort() != null)
            Collections.sort(list, new ResearchGroupsDataProviderComparator());

        if (size() < first + count)
            list.subList((int) first, (int) (first + size() - first)).iterator();

        return list.subList((int) first, (int) (first + count)).iterator();
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public IModel<ResearchGroup> model(ResearchGroup object) {
        return new Model<ResearchGroup>(object);
    }

    private class ResearchGroupsDataProviderComparator implements Comparator<ResearchGroup>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        public int compare(final ResearchGroup o1, final ResearchGroup o2) {
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
