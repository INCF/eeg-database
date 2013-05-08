package cz.zcu.kiv.eegdatabase.wui.ui.experiments.models;

import cz.zcu.kiv.eegdatabase.data.pojo.Stimulus;
import org.apache.wicket.model.IModel;

import java.security.PrivateKey;

/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 7.5.13
 * Time: 15:27
 */
public class StimulusDescriptioinModel implements IModel<String> {

    private Stimulus object;

    public StimulusDescriptioinModel(Stimulus stimulus){
        this.object = stimulus;
    }

    @Override
    public String getObject() {
        if(object.getDescription() == null)
            object.setDescription("");
        return object.getDescription();
    }

    @Override
    public void setObject(String s) {
        this.object.setDescription(s);
    }

    @Override
    public void detach() {
    }
}
