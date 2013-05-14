package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 20:24
 * To change this template use File | Settings | File Templates.
 */
public class AddExperimentResultsForm extends Form<Experiment> {

    private WizardTabbedPanelPage parent;

    private FileUploadField fileUploadField;

    @Autowired
    private ExperimentsFacade experimentsFacade;
    @Autowired
    private ScenariosFacade scenarioFacade;

    public AddExperimentResultsForm(String id, WizardTabbedPanelPage parent, Experiment experiment){
        super(id, new CompoundPropertyModel<Experiment>(experiment));

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

        /*
        if(fileUploadField.getFileUpload() != null)
             getModel().getObject().setResultFile(fileUploadField.getFileUpload());
        else
            error("Result file is required!");
        */

        return !hasError();
    }

    private void saveData(){
        AddExperimentEnvironmentForm environmentForm = parent.getEnvironmentForm();
        AddExperimentScenarioForm scenarioForm = parent.getScenarioForm();

        Experiment experiment = getModelObject();

        if(scenarioForm.isValid()){
            scenarioForm.save();
        }
        else error("Scenario tab is not valid!");


        if(environmentForm.isValid()){
            environmentForm.save();
        }
        else error("Environment tab is not valid!");


        if(this.isValid()){
            save();
        }
        else error("Result tab is not valid!");


        if(!hasError()){
            saveExperiment();
        }
    }

    private void save() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private void saveExperiment() {
        Experiment experiment = getModelObject();
        experimentsFacade.create(experiment);
    }

    /*
    private Experiment getValidExperimentPart(Experiment experiment){
        Experiment exper dto = getModelObject();
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
    */
}