package cz.zcu.kiv.eegdatabase.wui.ui.lists.components;

import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public abstract class ListModelWithResearchGroupCriteria<T> extends LoadableDetachableModel<List<T>> {

    private static final long serialVersionUID = -8410077884285556462L;
    
    private Model<ResearchGroup> criteriaModel = new Model<ResearchGroup>(null);

    @Override
    protected List<T> load() {
        
            return loadList(criteriaModel.getObject());
    }
    
    protected abstract List<T> loadList(ResearchGroup group);
    
    public Model<ResearchGroup> getCriteriaModel() {
        return criteriaModel;
    }
    
    public void setCriteriaModel(Model<ResearchGroup> criteriaModel) {
        this.criteriaModel = criteriaModel;
    }
}
