package cz.zcu.kiv.eegdatabase.wui.ui.experiments.models;

import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import org.apache.wicket.model.IModel;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 7.5.13
 * Time: 15:34
 */
public class PharmaceuticalTitleModel implements IModel<String> {
    private Pharmaceutical object;

    public PharmaceuticalTitleModel(Pharmaceutical pharmaceutical) {
        object = pharmaceutical;
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
