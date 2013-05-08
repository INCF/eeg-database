package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeNonXml;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.type.ScenarioTypeFacade;
import oracle.jdbc.rowset.OracleSerialBlob;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
    private ScenarioTypeFacade scenarioTypeFacade;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;


    public ScenarioForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Scenario>(new Scenario()));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        add(new Label("addScenarioHeader", "Create scenario"));

        final AutoCompleteTextField<String> researchGroups =
                new AutoCompleteTextField<String>("researchGroup",
                        new PropertyModel<String>(getModel(), "researchGroup.title"))
        {
            @Override
            protected Iterator<String> getChoices(String input)
            {
                if (Strings.isEmpty(input))
                {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                List<String> choices = new ArrayList<String>(10);
                List<ResearchGroup> groups = researchGroupFacade.getAllRecords();
                for (final ResearchGroup r : groups)
                {
                    final String data = r.getTitle();
                    if (data.toUpperCase().startsWith(input.toUpperCase()))
                    {
                        choices.add(data);
                        if (choices.size() == 10) break;
                    }
                }
                return choices.iterator();
            }
        };
        researchGroups.setRequired(true);
        add(researchGroups);

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

                        validate();
                        target.add(feedback);

                        if(!hasError()){
                            final FileUpload upload = fileUpload.getFileUpload();
                            if(upload != null){
                                scenario.setScenarioName(upload.getClientFileName());
                                scenario.setMimetype(upload.getContentType());
                            }

                            ScenarioTypeNonXml scenarioTypeNonXml = new ScenarioTypeNonXml();
                            scenarioTypeNonXml.setScenario(scenario);
                            //scenarioTypeNonSchema.setScenarioXml();
                            scenario.setScenarioType(scenarioTypeNonXml);
                            if(upload == null) {
                                info("No file uploaded");
                            } else {
                                info("Uploaded");

                                try{
                                    scenarioTypeNonXml.setScenarioXml(new OracleSerialBlob(upload.getBytes()));
                                } catch(SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }

                            // I need to get list of research groups and show these.
                            String researchTitle = scenario.getResearchGroup().getTitle();
                            ResearchGroup toSet = researchGroupFacade.getGroupByTitle(researchTitle);
                            scenario.setResearchGroup(toSet);
                            scenario.setPerson(EEGDataBaseSession.get().getLoggedUser());
                            Integer scenarioID = scenariosFacade.create(scenario);
                            scenario.setScenarioId(scenarioID);

                            scenarioTypeFacade.create(scenarioTypeNonXml);
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
                error("Scenario with given name already exists.");
            }
        }
    }
}
