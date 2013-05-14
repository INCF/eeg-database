package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.Util;
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
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.*;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

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

    private GenericModel<Date> startDate;
    private GenericModel<Date> endDate;
    private List<GenericModel<ProjectType>> projectType;
    private GenericModel<ResearchGroup> researchGroup;
    private GenericModel<Scenario> scenario;
    private GenericModel<Person> testedSubject;
    private List<GenericModel<Person>> coExperimenters;

    public AddExperimentScenarioForm(String id, Experiment experiment) {
        super(id);
        this.experiment = experiment;

        addScenario();
        addResearchGroup();
        addProject();
        addStartDate();
        addEndDate();
        addTestedSubject();
        addCoExperimenters();

        createModalWindows();
    }

    private void addEndDate() {
        Date now = new Date();
        if(scenarioEntity != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MINUTE, scenarioEntity.getScenarioLength());
            now = cal.getTime();
        }
        DateTimeField finishDate = new DateTimeField("finish", endDate = new GenericModel<Date>(now));
        finishDate.setRequired(true);

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(finishDate);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("finishFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);
        finishDate.add(new AjaxFeedbackUpdatingBehavior("blur", repeatableFeedback));

        add(finishDate);
        add(repeatableFeedback);
    }

    private void addStartDate() {
        DateTimeField startDate = new DateTimeField("start", this.startDate = new GenericModel<Date>(new Date()));
        startDate.setRequired(true);

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(startDate);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("startFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);
        startDate.add(new AjaxFeedbackUpdatingBehavior("blur", repeatableFeedback));

        add(startDate);
        add(repeatableFeedback);
    }

    private void addProject() {
        GenericFactory<ProjectType> factory = new GenericFactory<ProjectType>(ProjectType.class);
        GenericValidator<ProjectType> validator = new GenericValidator<ProjectType>(projectTypeFacade);

        RepeatableInputPanel repeatable =
                new RepeatableInputPanel<ProjectType>("project", factory,
                        validator, projectTypeFacade);
        projectType = repeatable.getData();
        add(repeatable);
    }

    private void addResearchGroup() {
        ResearchGroup group = new ResearchGroup();
        this.researchGroup = new GenericModel<ResearchGroup>(group);
        GenericValidator<ResearchGroup> validator = new GenericValidator<ResearchGroup>(researchGroupFacade);

        final RepeatableInput<ResearchGroup> researchGroup =
                new RepeatableInput<ResearchGroup>("group", this.researchGroup, ResearchGroup.class,
                        researchGroupFacade);
        researchGroup.add(validator);
        researchGroup.setRequired(true);

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(researchGroup);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("groupFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);
        researchGroup.add(new AjaxFeedbackUpdatingBehavior("blur", repeatableFeedback));

        add(researchGroup);
        add(repeatableFeedback);

        CheckBox isDefaultGroup = new CheckBox("isDefaultGroup");
        add(isDefaultGroup);
    }

    private void addScenario() {
        Scenario scenario = new Scenario();
        this.scenario = new GenericModel<Scenario>(scenario);
        GenericValidator<Scenario> validator = new GenericValidator<Scenario>(scenarioFacade);

        final RepeatableInput<Scenario> scenarioField =
                new RepeatableInput<Scenario>("scenario", this.scenario, Scenario.class,
                        scenarioFacade);
        scenarioField.setRequired(true);
        scenarioField.add(validator);
        scenarioField.add(new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                scenarioEntity = (Scenario) getDefaultModelObject();
                // setEndTime if it was not already set.
            }
        });


        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(scenarioField);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("scenarioFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);
        scenarioField.add(new AjaxFeedbackUpdatingBehavior("blur", repeatableFeedback));

        add(scenarioField);
        add(repeatableFeedback);
    }

    private void addTestedSubject() {
        Person person = new Person();
        this.testedSubject = new GenericModel<Person>(person);
        GenericValidator<Person> validator = new GenericValidator<Person>(personFacade);
        final RepeatableInput<Person> testedSubject =
                new RepeatableInput<Person>("testedSubject", this.testedSubject, Person.class,
                        personFacade);
        testedSubject.add(validator);
        testedSubject.setRequired(true);

        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(testedSubject);
        final FeedbackPanel repeatableFeedback = new FeedbackPanel("testedSubjectFeedback", repeatableFilter);
        repeatableFeedback.setOutputMarkupId(true);
        testedSubject.add(new AjaxFeedbackUpdatingBehavior("blur", repeatableFeedback));

        add(testedSubject);
        add(repeatableFeedback);
    }
    
    private void addCoExperimenters() {
        GenericFactory<Person> factory = new GenericFactory<Person>(Person.class);
        GenericValidator<Person> validator = new GenericValidator<Person>(personFacade);

        RepeatableInputPanel repeatable =
                new RepeatableInputPanel<Person>("coExperimenters", factory,
                        validator, personFacade);
        coExperimenters = repeatable.getData();
        add(repeatable);
    }

    private void createModalWindows() {
        final ModalWindow addGroup;
        add(addGroup = new ModalWindow("addGroupModal"));
        addGroup.setCookieName("add-group");

        addGroup.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddGroupPage(getPage().getPageReference(), addGroup);
            }
        });

        AjaxButton addGroupAjax = new AjaxButton("addGroup", this)
        {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form){
                addGroup.show(target);
            }
        };
        addGroupAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                addGroup.show(target);
            }
        });
        add(addGroupAjax);

        final ModalWindow newProject;
        add(newProject = new ModalWindow("newProjectModal"));
        newProject.setCookieName("new-project");

        newProject.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddProjectPage(getPage().getPageReference(), newProject);
            }
        });

        AjaxButton newProjectAjax = new AjaxButton("newProject", this)
        {};
        newProjectAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                newProject.show(target);
            }
        });
        add(newProjectAjax);

        final ModalWindow newScenario;
        add(newScenario = new ModalWindow("newScenarioModal"));
        newScenario.setCookieName("new-scenario");

        newScenario.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddScenarioPage(getPage().getPageReference(), newScenario);
            }
        });

        AjaxButton newScenarioAjax = new AjaxButton("newScenario", this)
        {};
        newScenarioAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                newScenario.show(target);
            }
        });
        add(newScenarioAjax);

        final ModalWindow addTested;
        add(addTested = new ModalWindow("addTestedModal"));
        addTested.setCookieName("add-tested");

        addTested.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddPersonPage(getPage().getPageReference(),
                        addTested, Util.ROLE_READER);
            }
        });

        AjaxButton addTestedAjax = new AjaxButton("addTested", this)
        {};
        addTestedAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                addTested.show(target);
            }
        });
        addTestedAjax.setLabel(ResourceUtils.getModel("label.subjectPerson"));

        FormComponentLabel testedLabel = new FormComponentLabel("subjectsLb", addTestedAjax);
        add(addTestedAjax, testedLabel);



        final ModalWindow newCoExperimenter;
        add(newCoExperimenter = new ModalWindow("addCoExperimenterModal"));
        newCoExperimenter.setCookieName("add-co-experimenter");

        newCoExperimenter.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddPersonPage(getPage().getPageReference(),
                        newCoExperimenter, Util.GROUP_EXPERIMENTER);
            }
        });

        AjaxButton newCoExperimenterAjax = new AjaxButton("addCoExperimenter", this)
        {};
        newCoExperimenterAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                newCoExperimenter.show(target);
            }
        });
        newCoExperimenterAjax.setLabel(ResourceUtils.getModel("label.coExperimenters"));

        FormComponentLabel coExperimenterLabel = new FormComponentLabel("coExperimentersLb", newCoExperimenterAjax);
        add(newCoExperimenterAjax, coExperimenterLabel);
    }

    /**
     * It takes data from the model and based on them get valid data.
     */
    public void save() {
        validate();
        if(!hasError()) {
            experiment.setEndTime(new Timestamp(this.endDate.getObject().getTime()));
            experiment.setStartTime(new Timestamp(this.startDate.getObject().getTime()));
            experiment.setProjectTypes(getSet(this.projectType));
            experiment.setResearchGroup(this.researchGroup.getObject());
            experiment.setScenario(this.scenario.getObject());
            experiment.setPersonBySubjectPersonId(this.testedSubject.getObject());
            experiment.setPersons(getSet(this.coExperimenters));
        }
    }

    private Set getSet(List objects) {
        Set result = new HashSet();
        for(Object object: objects) {
            result.add(((GenericModel) object).getObject());
        }
        return result;
    }

    /**
     * It returns whether the form is valid.
     *
     * @return
     */
    public boolean isValid(){
        validate();
        return !hasError();
    }
}
