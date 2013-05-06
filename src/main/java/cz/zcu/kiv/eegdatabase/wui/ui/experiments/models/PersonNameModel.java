package cz.zcu.kiv.eegdatabase.wui.ui.experiments.models;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import org.apache.wicket.model.IModel;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 6.5.13
 * Time: 8:59
 */
public class PersonNameModel implements IModel<String> {
    private Person object;

    public PersonNameModel(Person person) {
        object = person;
    }

    @Override
    public String getObject() {
        if(object.getSurname() == null) {
            object.setSurname("");
        }
        return object.getSurname();
    }

    @Override
    public void setObject(String object) {
        this.object.setSurname(object);
    }

    @Override
    public void detach() {

    }
}
