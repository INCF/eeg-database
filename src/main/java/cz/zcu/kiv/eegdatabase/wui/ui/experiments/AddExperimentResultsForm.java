package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.AddExperimentResultsDTO;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import oracle.jdbc.rowset.OracleSerialBlob;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentResultsForm extends Form<AddExperimentResultsDTO> {

    private WizardTabbedPanelPage parent;

    private FileUploadField fileUploadField;

    @Autowired
    private ExperimentsFacade experimentsFacade;
    @Autowired
    private ScenariosFacade scenarioFacade;

    public AddExperimentResultsForm(String id, WizardTabbedPanelPage parent){
        super(id, new CompoundPropertyModel<AddExperimentResultsDTO>(new AddExperimentResultsDTO()));

        this.parent = parent;

        setMultiPart(true);
        add(fileUploadField = new FileUploadField("resultFile"));

        AjaxButton saveAjax = new AjaxButton("submitFormButton", this)
        {};
        saveAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                saveData();
            }
        });
        saveAjax.setLabel(ResourceUtils.getModel("button.save"));
        add(saveAjax);
    }

    public boolean isValid(){
        validate();

        if(fileUploadField.getFileUpload() != null)
             getModel().getObject().setResultFile(fileUploadField.getFileUpload());
        else
            error("Result file is required!");

        return !hasError();
    }

    private void saveData(){
        AddExperimentEnvironmentForm environmentForm = parent.getEnvironmentForm();
        AddExperimentScenarioForm scenarioForm = parent.getScenarioForm();

        Experiment experiment = new Experiment();

        if(scenarioForm.isValid()){
            Experiment ex = scenarioForm.getValidExperimentPart(experiment);
            if(ex != null)
                experiment = ex;
            else error("Scenario tab is not valid!");
        }
        else error("Scenario tab is not valid!");


        if(environmentForm.isValid()){
            Experiment ex = environmentForm.getValidExperimentPart(experiment);
            if(ex != null)
                experiment = ex;
            else error("Environment tab is not valid!");
        }
        else error("Environment tab is not valid!");


        if(this.isValid()){
            Experiment ex = getValidExperimentPart(experiment);
            if(ex != null)
                experiment = ex;
            else error("Result tab is not valid!");
        }
        else error("Result tab is not valid!");


        if(!hasError()){
            experimentsFacade.create(experiment);
        }
    }

    private Experiment getValidExperimentPart(Experiment experiment){
        AddExperimentResultsDTO dto = getModelObject();
        if(dto == null) return null;

        try{
            // TODO save the file into some attribute
            // experiment.setDataFile( ... );
            new OracleSerialBlob(dto.getResultFile().getBytes());
        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return experiment;
    }
}