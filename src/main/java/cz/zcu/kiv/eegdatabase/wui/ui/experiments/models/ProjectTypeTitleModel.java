package cz.zcu.kiv.eegdatabase.wui.ui.experiments.models;

import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import org.apache.wicket.model.IModel;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 10.5.13
 * Time: 11:28
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTypeTitleModel implements IModel<String> {
    private ProjectType object;

    public ProjectTypeTitleModel(ProjectType project) {
        object = project;
    }

    @Override
    public String getObject() {
        return object.getTitle();
    }

    @Override
    public void setObject(String object) {
        this.object.setTitle(object);
    }

    @Override
    public void detach() {

    }
}