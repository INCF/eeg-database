package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentEnvironmentDTO;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentResultsDTO;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentResultsForm extends Form<AddExperimentResultsDTO> {
    public AddExperimentResultsForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentResultsDTO>(new AddExperimentResultsDTO()));


        setMultiPart(true);
        add(new FileUploadField("resultFile"));

        /* TODO add submit button for the wizard */
    }

    public boolean isValid(){
        validate();
        System.out.println("VALIDUJI RES S VYSLEDKEM: "+!hasError());
        return !hasError();
    }
}