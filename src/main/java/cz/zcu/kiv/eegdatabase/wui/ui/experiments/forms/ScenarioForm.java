package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.EducationLevel;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.core.educationlevel.EducationLevelFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Jakub Balhar
 * Date: 16.4.13
 * Time: 16:33
 */
public class ScenarioForm extends Form<Scenario> {
    @SpringBean
    private ScenariosFacade scenariosFacade;

    @SpringBean
    private EducationLevelFacade educationLevelFacade;


    public ScenarioForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Scenario>(new Scenario()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("addScenarioHeader", "Create scenario"));

        List<EducationLevel> educationLevels =  educationLevelFacade.getAllRecords();
        List<String> choices = new ArrayList<String>();
        for(EducationLevel level: educationLevels){
            choices.add(level.getTitle());
        }
        add(new DropDownChoice<String>("group", choices));

        add(
                new RequiredTextField<String>("title").
                        add(new TitleExistsValidator())
        );

        add(new RequiredTextField<String>("scenarioLength"));
        add(new TextArea<String>("description").setRequired(true));
        add(new CheckBox("availableFile"));

        setMultiPart(true);
        final FileUploadField fileUpload = new FileUploadField("dataFile");
        add(fileUpload);

        add(
                new AjaxButton("submitForm", this) {
                    @Override
                    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                        Scenario scenario = (Scenario) form.getModelObject();

                        final FileUpload upload = fileUpload.getFileUpload();
                        scenario.setScenarioName(upload.getClientFileName());
                        scenario.setMimetype(upload.getContentType());
                        if(upload == null) {
                            info("No file uploaded");
                        } else {
                            info("Uploaded");
                        }

                        validate();
                        target.add(feedback);

                        if(!hasError()){
                            scenario.setPerson(EEGDataBaseSession.get().getLoggedUser());
                            scenariosFacade.create(scenario);
                            window.close(target);
                        }
                    }
                }.add(new AjaxEventBehavior("onclick") {
                    @Override
                    protected void onEvent(AjaxRequestTarget target) {
                    }
                })
        );

        add(
                new AjaxButton("closeForm", this) {}.
                        add(new AjaxEventBehavior("onclick") {
                            @Override
                            protected void onEvent(AjaxRequestTarget target) {
                                window.close(target);
                            }
                        })
        );

        setMaxSize(Bytes.megabytes(1));
        setOutputMarkupId(true);
        AjaxFormValidatingBehavior.addToAllFormComponents(this, "focus", Duration.ONE_SECOND);
    }

    private class TitleExistsValidator implements IValidator<String> {

        @Override
        public void validate(IValidatable<String> validatable) {
            final String title = validatable.getValue();

            if(scenariosFacade.existsScenario(title)){
                error("Disease with given name already exists.");
            }
        }
    }
}
