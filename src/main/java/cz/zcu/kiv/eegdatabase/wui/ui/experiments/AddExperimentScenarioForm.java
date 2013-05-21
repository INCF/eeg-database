package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddProjectPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddScenarioPage;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.*;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 26.3.13
 * Time: 17:43
 */
public class AddExperimentScenarioForm extends Form<Experiment> {
    @SpringBean
    private ResearchGroupFacade researchGroupFacade;
    @SpringBean
    private PersonFacade personFacade;
    @SpringBean
    private ScenariosFacade scenarioFacade;
    @SpringBean
    private ProjectTypeFacade projectTypeFacade;

    private Experiment experiment;
    private Scenario scenarioEntity = new Scenario();
    private ResearchGroup researchGroupEntity = new ResearchGroup();
    private DateTimeField finishDate;
    private Date finishDateForModel = new Date();
    private DateTimeField startDate;
    private Date startDateForModel = new Date();

    private GenericModel<Date> endDate;
    private GenericModel<Date> beginDate;
    private List<GenericModel<ProjectType>> projectTypes;
    private GenericModel<ResearchGroup> researchGroup;
    private GenericModel<Scenario> scenario;
    private GenericModel<Person> testedSubject;
    private List<GenericModel<Person>> coExperimenters;

    private CheckBox isPrivateExperiment;
    private boolean privateExperiment = false;

    private CheckBox isDefaultGroup;
    private boolean defaultGroup = false;

    public AddExperimentScenarioForm(String id, Experiment experiment) {
        super(id);
        this.experiment = experiment;

        addScenario();
        addResearchGroup();
        addPrivateExperimentCheckBox();
        addProject();
        addStartDate();
        addEndDate();
        addTestedSubject();
        addCoExperimenters();
        createModalWindows();
    }

    private void addPrivateExperimentCheckBox() {
        isPrivateExperiment = new CheckBox("isPrivateExperiment", new Model(privateExperiment));
        isPrivateExperiment.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                privateExperiment = !privateExperiment;
                isPrivateExperiment.setModelObject(privateExperiment);
                target.add(isPrivateExperiment);
            }
        });
        add(isPrivateExperiment);
    }

    private void addEndDate() {
        Date now = new Date();
        if(scenarioEntity != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MINUTE, scenarioEntity.getScenarioLength());
            finishDateForModel = cal.getTime();
        }
        finishDate = new DateTimeField("finish", endDate = new GenericModel<Date>(finishDateForModel)){
             @Override
             protected DateTextField newDateTextField(String id,  PropertyModel dateFieldModel) {
                DateTextField f = super.newDateTextField(id, dateFieldModel);
                f.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        finishDateForModel = getDateFromDateTimeField(finishDate);
                        finishDate.setModelObject(finishDateForModel);
                    }
                });
                return f;
             }
        };
        finishDate.get("hours").add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                finishDateForModel = getDateFromDateTimeField(finishDate);
                finishDate.setModelObject(finishDateForModel);
            }
        });
        finishDate.get("minutes").add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                finishDateForModel = getDateFromDateTimeField(finishDate);
                finishDate.setModelObject(finishDateForModel);
            }
        });

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(finishDate);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("finishFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);

        add(finishDate);
        add(repeatableFeedback);
    }

    public Date getDateFromDateTimeField(DateTimeField dtf){
            try {
                Calendar newOne = Calendar.getInstance();
                Calendar oldOne = Calendar.getInstance();
                oldOne.setTime(dtf.getDate());
                newOne.set(oldOne.get(Calendar.YEAR), oldOne.get(Calendar.MONTH), oldOne.get(Calendar.DAY_OF_MONTH), dtf.getHours(), dtf.getMinutes());
                return newOne.getTime();
            }
            catch (Exception e) {
                return null;
            }
    }

    private void addStartDate() {
             startDate = new DateTimeField("start", beginDate = new GenericModel<Date>(startDateForModel)){
             @Override
             protected DateTextField newDateTextField(String id,  PropertyModel dateFieldModel) {
                DateTextField f = super.newDateTextField(id, dateFieldModel);
                f.add(new AjaxFormComponentUpdatingBehavior("onchange") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        startDateForModel = getDateFromDateTimeField(startDate);
                        startDate.setModelObject(startDateForModel);
                    }
                });
                return f;
             }
        };
        startDate.get("hours").add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                startDateForModel = getDateFromDateTimeField(startDate);
                startDate.setModelObject(startDateForModel);
            }
        });
        startDate.get("minutes").add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                startDateForModel = getDateFromDateTimeField(startDate);
                startDate.setModelObject(startDateForModel);
            }
        });

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(startDate);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("startFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);

        add(startDate);
        add(repeatableFeedback);
    }

    private void addProject() {
        GenericFactory<ProjectType> factory = new GenericFactory<ProjectType>(ProjectType.class);
        GenericValidator<ProjectType> validator = new GenericValidator<ProjectType>(projectTypeFacade);
        validator.setRequired(true);

        RepeatableInputPanel repeatable =
                new RepeatableInputPanel<ProjectType>("project", factory,
                        validator, projectTypeFacade);
        projectTypes = repeatable.getData();
        validator.setList(projectTypes);
        add(repeatable);
    }

    private void addResearchGroup() {
        ResearchGroup group = new ResearchGroup();
        this.researchGroup = new GenericModel<ResearchGroup>(group);
        GenericValidator<ResearchGroup> validator = new GenericValidator<ResearchGroup>(researchGroupFacade);
        validator.setRequired(true);

        final RepeatableInput<ResearchGroup> researchGroup =
                new RepeatableInput<ResearchGroup>("group", this.researchGroup, ResearchGroup.class,
                        researchGroupFacade);
        researchGroup.add(validator);

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(researchGroup);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("groupFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);

        researchGroup.add(new AjaxAutoCompletableUpdatingBehavior("onblur",
                repeatableFeedback, validator, this.researchGroup) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                researchGroupEntity = (ResearchGroup) getDefaultModelObject();
            }
        });

        add(researchGroup);
        add(repeatableFeedback);
    }

    private void addScenario() {
        Scenario scenario = new Scenario();
        this.scenario = new GenericModel<Scenario>(scenario);
        GenericValidator<Scenario> validator = new GenericValidator<Scenario>(scenarioFacade);
        validator.setRequired(true);

        final RepeatableInput<Scenario> scenarioField =
                new RepeatableInput<Scenario>("scenario", this.scenario, Scenario.class,
                        scenarioFacade);

        scenarioField.add(validator);


        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(scenarioField);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("scenarioFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);

        scenarioField.add(new AjaxAutoCompletableUpdatingBehavior("onblur",
                repeatableFeedback, validator, this.scenario) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                scenarioEntity = (Scenario) getDefaultModelObject();
                // setEndTime if it was not already set.
            }
        });

        add(scenarioField);
        add(repeatableFeedback);
    }

    private void addTestedSubject() {
        Person person = new Person();
        this.testedSubject = new GenericModel<Person>(person);
        final GenericValidator<Person> validator = new GenericValidator<Person>(personFacade);
        validator.setRequired(true);

        final RepeatableInput<Person> testedSubject =
                new RepeatableInput<Person>("testedSubject", this.testedSubject, Person.class,
                        personFacade);

        testedSubject.add(validator);

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(testedSubject);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("testedSubjectFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);

        add(testedSubject);
        add(repeatableFeedback);

        testedSubject.add(new AjaxAutoCompletableUpdatingBehavior("onblur",
                repeatableFeedback, validator, this.testedSubject));
    }

    private void addCoExperimenters() {
        GenericFactory<Person> factory = new GenericFactory<Person>(Person.class);
        GenericValidator<Person> validator = new GenericValidator<Person>(personFacade);

        RepeatableInputPanel repeatable =
                new RepeatableInputPanel<Person>("coExperimenters", factory,
                        validator, personFacade);
        coExperimenters = repeatable.getData();
        validator.setList(coExperimenters);

        add(repeatable);
    }

    private void createModalWindows() {

        addModalWindowAndButton(this, "addGroupModal", "add-group",
                "addGroup", AddGroupPage.class.getName());

        addModalWindowAndButton(this, "newProjectModal", "new-project",
                "newProject", AddProjectPage.class.getName());

        addModalWindowAndButton(this, "newScenarioModal", "new-scenario",
                "newScenario", AddScenarioPage.class.getName());

        addModalWindowAndButton(this, "addTestedModal", "add-tested",
                "addTested", AddPersonPage.class.getName());

        addModalWindowAndButton(this, "addCoExperimenterModal", "add-co-experimenter",
                "addCoExperimenter", AddPersonPage.class.getName());
    }

    /**
     * It takes data from the model and based on them get valid data.
     */
    public void save() {
        validate();
        if(!hasError()) {
            experiment.setEndTime(new Timestamp(this.finishDateForModel.getTime()));
            experiment.setStartTime(new Timestamp(this.startDateForModel.getTime()));
            experiment.setProjectTypes(getSet(this.projectTypes));
            ResearchGroup group = this.researchGroup.getObject();
            if (this.isDefaultGroup.getModelObject()){
                Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
                loggedUser.setDefaultGroup(group);
                personFacade.update(loggedUser);
            }
            experiment.setResearchGroup(group);
            experiment.setScenario(this.scenario.getObject());
            experiment.setPersonBySubjectPersonId(this.testedSubject.getObject());
            experiment.setPersons(getSet(this.coExperimenters));
            experiment.setPrivateExperiment(this.isPrivateExperiment.getModelObject());
        }
    }

    private Set getSet(List objects) {
        Set result = new HashSet();
        Object last = null;
        for(Object object: objects) {
            if(object != null)
                last = ((GenericModel) object).getObject();
                result.add(last);
        }
        result.remove(last);    // empty field for next item
        return result;
    }

    private void validateDates(){
        if (finishDateForModel == null) {
            finishDate.error(ResourceUtils.getString("required.date"));
        }
        if (startDateForModel == null) {
            startDate.error(ResourceUtils.getString("required.date"));
        }
    }

    public void refreshDateModelObjects(){
        this.finishDate.setModelObject(this.finishDateForModel);
        this.startDate.setModelObject(this.startDateForModel);
    }

    /**
     * It returns whether the form is valid.
     *
     * @return
     */
    public boolean isValid(){
        validate();
        validateDates();
        return !hasError();
    }




    private void addModalWindowAndButton(final Form form, String modalWindowName, String cookieName,
                                         final String buttonName, final String targetClass){

        final ModalWindow addedWindow;
        form.add(addedWindow = new ModalWindow(modalWindowName));
        addedWindow.setCookieName(cookieName);
        addedWindow.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                try{
                    Constructor<?> cons = null;
                    cons = Class.forName(targetClass).getConstructor(
                    PageReference.class, ModalWindow.class);

                    return (Page) cons.newInstance(
                            getPage().getPageReference(), addedWindow);
                }catch (NoSuchMethodException e){
                    e.printStackTrace();
                }catch (IllegalAccessException e){
                    e.printStackTrace();
                }catch (InstantiationException e){
                    e.printStackTrace();
                }catch (InvocationTargetException e){
                    e.printStackTrace();
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                return null;
            }
        });

        AjaxButton ajaxButton = new AjaxButton(buttonName, this)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                addedWindow.show(target);
            }
        };
        ajaxButton.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                addedWindow.show(target);
            }
        });
        form.add(ajaxButton);
    }
}
