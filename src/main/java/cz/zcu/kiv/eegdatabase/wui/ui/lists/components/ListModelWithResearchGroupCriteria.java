package cz.zcu.kiv.eegdatabase.wui.ui.lists.components;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import org.apache.wicket.model.IModel;

/**
 * Model implementation used for filtering list of definitio or parameters in lists section. Filter parameters is research group.
 * 
 * @author Jakub Rinkes
 * 
 * @param <T>
 */
public abstract class ListModelWithResearchGroupCriteria<T> extends LoadableDetachableModel<List<T>> {

    private static final long serialVersionUID = -8410077884285556462L;
    protected Log log = LogFactory.getLog(getClass());

    private IModel<ResearchGroup> criteriaModel = new Model<ResearchGroup>(null) {

		@Override
		public void setObject(ResearchGroup object) {
			super.setObject(object);
			//reload model when research group changes
			ListModelWithResearchGroupCriteria.this.detach();
		}

	};

    protected abstract List<T> loadList(ResearchGroup group);

    @Override
    protected List<T> load() {

        List<T> loadList = loadList(criteriaModel.getObject());
        return loadList;
    }

    public IModel<ResearchGroup> getCriteriaModel() {
        return criteriaModel;
    }

    public void setCriteriaModel(IModel<ResearchGroup> criteriaModel) {
        this.criteriaModel = criteriaModel;
    }
}
