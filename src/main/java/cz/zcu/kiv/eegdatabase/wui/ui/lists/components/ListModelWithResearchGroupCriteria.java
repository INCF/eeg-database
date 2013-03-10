package cz.zcu.kiv.eegdatabase.wui.ui.lists.components;

import java.util.List;

import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

public abstract class ListModelWithResearchGroupCriteria<T> extends ListModel<T> {

    private static final long serialVersionUID = -8410077884285556462L;

    private Model<ResearchGroup> criteriaModel = new Model<ResearchGroup>(null);

    protected abstract List<T> loadList(ResearchGroup group);

    @Override
    public List<T> getObject() {
        return loadList(criteriaModel.getObject());
    }

    public Model<ResearchGroup> getCriteriaModel() {
        return criteriaModel;
    }

    public void setCriteriaModel(Model<ResearchGroup> criteriaModel) {
        this.criteriaModel = criteriaModel;
    }
}
