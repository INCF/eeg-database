package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.DataFile;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import oracle.jdbc.rowset.OracleSerialBlob;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 26.3.13
 * Time: 20:24
 */
public class AddExperimentResultsForm extends Form<Experiment> {

    private WizardTabbedPanelPage parent;

    private FileUploadField fileUploadField;
    private Experiment experiment;

    @Autowired
    private ExperimentsFacade experimentsFacade;

    public AddExperimentResultsForm(String id, WizardTabbedPanelPage parent, Experiment experiment){
        super(id, new CompoundPropertyModel<Experiment>(experiment));

        this.parent = parent;
        this.experiment = experiment;

        setMultiPart(true);

        addFileUploadField();
        addSubmitButton();

        /* feedback for whole form validated across all tabs */
        ComponentFeedbackMessageFilter formFilter = new ComponentFeedbackMessageFilter(this);
        final FeedbackPanel formFeedback = new FeedbackPanel("formFeedback", formFilter);
        formFeedback.setOutputMarkupId(true);
        add(formFeedback);

        add(new AddExperimentFormValidator());
    }

    private void addFileUploadField(){

        fileUploadField = new FileUploadField("resultFile");

        ComponentFeedbackMessageFilter fileFilter = new ComponentFeedbackMessageFilter(fileUploadField);
        final FeedbackPanel fileFeedback = new FeedbackPanel("fileFeedback", fileFilter);
        fileFeedback.setOutputMarkupId(true);
        fileUploadField.add(new AjaxFeedbackUpdatingBehavior("blur", fileFeedback));
//        fileUploadField.setRequired(true);

        add(fileUploadField);
        add(fileFeedback);
    }


    private void addSubmitButton(){

        AjaxButton saveAjax = new AjaxButton("submitFormButton", this)
        {};
        saveAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {

                save(); //saves this tab into backing model (Experiment)

                if(isValid()){
                    saveExperiment(); // saves modelObject into database
                }
                else
                    setResponsePage(getPage());    //refresh of the page to show errors
            }
        });

        saveAjax.setLabel(ResourceUtils.getModel("button.save"));
        add(saveAjax);
    }

    public boolean isValid(){
        validate();
        return !hasError();
    }

    private void save() {
        if(fileUploadField.isValid()){
            Set<DataFile> files = new HashSet<DataFile>();
            DataFile file = new DataFile();

            try{
                byte[] fileBytes = fileUploadField.getFileUpload().getBytes();
                OracleSerialBlob blob = new OracleSerialBlob(fileBytes);
                file.setFileContent(blob);

                files.add(file);
                experiment.setDataFiles(files);
            }
            catch(Exception ex) {
                fileUploadField.error("File saving failed");
            }
        }
    }

    private void saveExperiment() {
        Experiment experiment = getModelObject();
        experimentsFacade.create(experiment);
    }


    private class AddExperimentFormValidator implements IFormValidator{

        @Override
        public FormComponent<?>[] getDependentFormComponents() {
            return new FormComponent<?>[0];
        }

        @Override
        public void validate(Form<?> form) {
            AddExperimentScenarioForm scenarioForm = parent.getScenarioForm();
            AddExperimentEnvironmentForm environmentForm = parent.getEnvironmentForm();

            if(!scenarioForm.isValid()) {
                error("Scenario tab is not valid!");
            }

            if(!environmentForm.isValid()) {
                error("Environment tab is not valid!");
            }

            if(!fileUploadField.isValid()) {
                error("Result tab is not valid!");
            }

            System.out.println("VALIDATE " + hasError());
        }
    }
}