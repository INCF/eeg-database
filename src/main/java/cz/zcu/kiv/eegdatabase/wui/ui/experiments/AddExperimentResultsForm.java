package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.ExperimentsFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.welcome.WelcomePage;
import oracle.jdbc.rowset.OracleSerialBlob;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.GenericModel;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private GenericModel<List<FileUpload>> fileUploads;

    @SpringBean
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

        fileUploads = new GenericModel<List<FileUpload>>(new ArrayList<FileUpload>());
        fileUploadField = new FileUploadField("resultFile", fileUploads);

        ComponentFeedbackMessageFilter fileFilter = new ComponentFeedbackMessageFilter(fileUploadField);
        final FeedbackPanel fileFeedback = new FeedbackPanel("fileFeedback", fileFilter);
        fileFeedback.setOutputMarkupId(true);
        fileUploadField.add(new AjaxFeedbackUpdatingBehavior("blur", fileFeedback));

        add(fileUploadField);
        add(fileFeedback);
    }


    private void addSubmitButton(){

        AjaxButton saveAjax = new AjaxButton("submitFormButton", this){
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
                save(); //saves this tab into backing model (Experiment)

                if(isValid()) {
                    if(!fileUploadField.hasErrorMessage()) {
                        saveExperiment();
                        setResponsePage(WelcomePage.class);
                    } else {
                        setResponsePage(getPage());
                    }
                }
                else {
                    setResponsePage(getPage());    //refresh of the page to show errors
                }
            }
        };

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

            try{
                List<FileUpload> fileUploadList = fileUploads.getObject();
                for(FileUpload fileUpload : fileUploadList) {
                    DataFile file = new DataFile();

                    byte[] fileBytes = fileUpload.getBytes();
                    OracleSerialBlob blob = new OracleSerialBlob(fileBytes);
                    file.setFileContent(blob);

                    files.add(file);
                }
                experiment.setDataFiles(files);
            }
            catch(Exception ex) {
                fileUploadField.error("File saving failed");
            }
        }
    }

    private void saveExperiment() {
        Experiment experiment = getModelObject();
        // TODO user must somehow insert this data instead of us.
        Person logged = EEGDataBaseSession.get().getLoggedUser();
        experiment.setPersonByOwnerId(logged);
        Digitization digitization = new Digitization();
        digitization.setDigitizationId(52);
        experiment.setDigitization(digitization);
        SubjectGroup subjectGroup = new SubjectGroup();
        subjectGroup.setSubjectGroupId(11);
        experiment.setSubjectGroup(subjectGroup);
        Artifact artifact = new Artifact();
        artifact.setArtifactId(5);
        experiment.setArtifact(artifact);
        ElectrodeConf conf = new ElectrodeConf();
        conf.setElectrodeConfId(6);
        experiment.setElectrodeConf(conf);
        // TODO End of todo.
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