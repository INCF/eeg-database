package cz.zcu.kiv.eegdatabase.wui.ui.lists.components;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

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

    private Model<ResearchGroup> criteriaModel = new Model<ResearchGroup>(null);

    protected abstract List<T> loadList(ResearchGroup group);

    @Override
    protected List<T> load() {

        List<T> loadList = loadList(criteriaModel.getObject());
        return loadList;
    }

    public Model<ResearchGroup> getCriteriaModel() {
        return criteriaModel;
    }

    public void setCriteriaModel(Model<ResearchGroup> criteriaModel) {
        this.criteriaModel = criteriaModel;
    }
}
