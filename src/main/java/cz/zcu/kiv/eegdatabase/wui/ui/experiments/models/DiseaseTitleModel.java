package cz.zcu.kiv.eegdatabase.wui.ui.experiments.models;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import org.apache.wicket.model.IModel;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 8.5.13
 * Time: 13:53
 */
public class DiseaseTitleModel implements IModel<String> {
    private Disease object;

    public DiseaseTitleModel(Disease disease) {
        object = disease;
    }

    @Override
    public String getObject() {
        if(object.getTitle() == null) {
            object.setTitle("");
        }
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
