package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.logic.Util;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.models.PersonNameModel;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.models.StimulusDescriptionModel;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AddingItemsView;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutocompletable;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Matej Sutr
 * Date: 26.3.13
 * Time: 17:43
 */
public class AddExperimentScenarioForm extends Form<AddExperimentScenarioDTO> {
    @SpringBean
    private ResearchGroupFacade researchGroupFacade;
    @SpringBean
    private ScenariosFacade scenariosFacade;
    @SpringBean
    private PersonFacade personFacade;
    @SpringBean
    private StimulusFacade stimulusFacade;
    @SpringBean
    private ScenariosFacade scenarioFacade;
    @SpringBean
    private ProjectTypeFacade projectTypeFacade;

    private Experiment experiment = new Experiment();
    private Scenario scenarioEntity = new Scenario();

    private final List<Person> coExperimentersData = new ArrayList<Person>();
    private final List<Person> testedSubjectsData = new ArrayList<Person>();
    private final List<Stimulus> stimuliData = new ArrayList<Stimulus>();

    final int AUTOCOMPLETE_ROWS = 10;

    public AddExperimentScenarioForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentScenarioDTO>(new AddExperimentScenarioDTO()));

        //scenario autocomplete
        final AutoCompleteTextField<String> scenario = new AutoCompleteTextField<String>("scenario",
                new PropertyModel<String>(experiment, "scenario.scenarioName"))
        {
            @Override
            protected Iterator<String> getChoices(String input)
            {
                if (Strings.isEmpty(input))
                {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                List<String> choices = new ArrayList<String>(AUTOCOMPLETE_ROWS);
                List<Scenario> scenarios = scenariosFacade.getAllRecords();
                for (final Scenario s : scenarios)
                {
                    final String data = s.getTitle();
                    if (data.toUpperCase().startsWith(input.toUpperCase()))
                    {
                        choices.add(data);
                        if (choices.size() == AUTOCOMPLETE_ROWS) break;
                    }
                }
                return choices.iterator();
            }
        };
        scenario.setRequired(true);
        scenario.add(new AjaxEventBehavior("onblur") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                scenarioEntity = scenariosFacade.getScenarioByTitle(scenario.getInput());
            }
        });
        this.add(scenario);

        //research group autocomplete
        final AutoCompleteTextField<String> group = new AutoCompleteTextField<String>("group",
                new PropertyModel<String>(experiment, "researchGroup.title"))
        {
            @Override
            protected Iterator<String> getChoices(String input)
            {
                if (Strings.isEmpty(input))
                {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                List<String> choices = new ArrayList<String>(AUTOCOMPLETE_ROWS);
                List<ResearchGroup> groups = researchGroupFacade.getAllRecords();
                for (final ResearchGroup rg : groups)
                {
                    final String data = rg.getTitle();
                    if (data.toUpperCase().startsWith(input.toUpperCase()))
                    {
                        choices.add(data);
                        if (choices.size() == AUTOCOMPLETE_ROWS) break;
                    }
                }
                return choices.iterator();
            }
        };
        group.setRequired(true);
        add(group);

        CheckBox isDefaultGroup = new CheckBox("isDefaultGroup");
        add(isDefaultGroup);

        AutoCompleteTextField<String> project = new AutoCompleteTextField<String>("project") {
            @Override
            protected Iterator<String> getChoices(String input)
            {
                if (Strings.isEmpty(input))
                {
                    List<String> emptyList = Collections.emptyList();
                    return emptyList.iterator();
                }
                List<String> choices = new ArrayList<String>(AUTOCOMPLETE_ROWS);
                List<ProjectType> projectTypes = projectTypeFacade.getAllRecords();
                for (final ProjectType pt : projectTypes)
                {
                    final String data = pt.getTitle();
                    if (data.toUpperCase().startsWith(input.toUpperCase()))
                    {
                        choices.add(data);
                        if (choices.size() == AUTOCOMPLETE_ROWS) break;
                    }
                }
                return choices.iterator();
            }
        };
        project.setRequired(true);
        add(project);


        Date now = new Date();
        getModelObject().setStart(now);
        DateTimeField startDate = new DateTimeField("start");
        startDate.setRequired(true);
        add(startDate);

        if(scenarioEntity != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.MINUTE, scenarioEntity.getScenarioLength());
            now = cal.getTime();
        }
        getModelObject().setFinish(now);
        DateTimeField finishDate = new DateTimeField("finish");
        finishDate.setRequired(true);
        add(finishDate);

        addTestedSubject();
        addStimulus();
        addCoExperimenters();

        createModalWindows();
    }
    
    private void addStimulus() {
        stimuliData.add(new Stimulus());
        final WebMarkupContainer stimulusContainer = new WebMarkupContainer("addStimulusInputContainer");

        final ListView<Stimulus> stimulusListView = new ListView<Stimulus>("addStimulusInput", stimuliData) {
             @Override
            protected void populateItem(ListItem item) {
                final Stimulus stimulusItem = (Stimulus) item.getModelObject();
                final AddingItemsView<String> stimulus =
                        new AddingItemsView<String>(
                                "stimuli",
                                new StimulusDescriptionModel(stimulusItem),
                                this,
                                stimulusContainer,
                                Stimulus.class
                        );
                stimulus.setRequired(true);
                item.add(stimulus);
            }
        };
        stimulusListView.setOutputMarkupId(true);
        stimulusListView.setReuseItems(true);

        stimulusContainer.add(stimulusListView);
        stimulusContainer.setOutputMarkupId(true);
        add(stimulusContainer);
    }

    private void addTestedSubject() {
        testedSubjectsData.add(new Person());
        final WebMarkupContainer testedSubjectContainer = new WebMarkupContainer("addTestedInputContainer");

        final ListView<Person> testedSubjects = new ListView<Person>("addTestedInput",testedSubjectsData) {
            @Override
            protected void populateItem(ListItem item) {
                final Person person = (Person) item.getModelObject();
                final AddingItemsView<String> testSubject =
                        new AddingItemsView<String>(
                                "subjects",
                                new PersonNameModel(person),
                                this,
                                testedSubjectContainer,
                                Person.class
                        );
                testSubject.setRequired(true);
                item.add(testSubject);
            }
        };
        testedSubjects.setOutputMarkupId(true);
        testedSubjects.setReuseItems(true);

        testedSubjectContainer.add(testedSubjects);
        testedSubjectContainer.setOutputMarkupId(true);
        add(testedSubjectContainer);
    }
    
    private void addCoExperimenters() {
        coExperimentersData.add(new Person());
        final WebMarkupContainer coExperimentersContainer =
                new WebMarkupContainer("addCoExperimentersInputContainer");

        final ListView<Person> coExperimenters =
                new ListView<Person>("addCoExperimentersInput",coExperimentersData) {
            @Override
            protected void populateItem(ListItem item) {
                final Person person = (Person) item.getModelObject();
                final AddingItemsView<String> testSubject =
                        new AddingItemsView<String>(
                                "coExperimenters",
                                new PersonNameModel(person),
                                this,
                                coExperimentersContainer,
                                Person.class
                        );
                item.add(testSubject);
            }
        };
        coExperimenters.setOutputMarkupId(true);
        coExperimenters.setReuseItems(true);

        coExperimentersContainer.add(coExperimenters);
        coExperimentersContainer.setOutputMarkupId(true);
        add(coExperimentersContainer);
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


        final ModalWindow newStimulus;
        add(newStimulus = new ModalWindow("addStimulusModal"));
        newStimulus.setCookieName("add-stimulus");

        newStimulus.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddStimulusPage(getPage().getPageReference(), newStimulus);
            }
        });

        AjaxButton newStimulusAjax = new AjaxButton("addStimulus", this)
        {};
        newStimulusAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                newStimulus.show(target);
            }
        });
        newStimulusAjax.setLabel(ResourceUtils.getModel("label.stimulus"));

        FormComponentLabel stimulusLabel = new FormComponentLabel("stimulusLb", newStimulusAjax);
        add(newStimulusAjax, stimulusLabel);


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

    public boolean isValid(){
        this.validate();

        boolean validPersons = isPersonValid(testedSubjectsData) &&
                isPersonValid(coExperimentersData);

        boolean isStimulusValid = isAutocompletableValid(stimuliData);

        return !hasError() && validPersons && isStimulusValid;
    }

    private boolean isPersonValid(List<Person> persons) {
        int validPersons = 0;
        for(Person person: persons){
            if(!person.getSurname().equals("")){
                validPersons++;
            }
        }
        return validPersons > 0;
    }

    private boolean isAutocompletableValid(List<? extends IAutocompletable> list){
         for (IAutocompletable item: list){
             if(item.isValidOnChange())
                 return true;
         }
         return false;
    }

    public Experiment getValidExperimentPart(Experiment experiment){
        AddExperimentScenarioDTO dto = getModelObject();

        if(dto == null) return null;

        if(dto.getScenario() == null) return null;
        if(dto.getGroup() == null) return null;
        if(dto.getProject() == null) return null;
        if(dto.getStart() == null) return null;
        if(dto.getFinish() == null) return null;

        if(testedSubjectsData.isEmpty()) return null;
        //if(coExperimentersData.isEmpty()) return null; //valid (not required)
        if(stimuliData.isEmpty()) return null;
        debug("AddExperimentScenarioForm required attributes are set");

        Scenario scenario = scenarioFacade.getScenarioByTitle(dto.getScenario());
        if(scenario == null) return null;
        experiment.setScenario(scenario);
        debug("AddExperimentScenarioForm.scenario is valid");

        ResearchGroup group = researchGroupFacade.getGroupByTitle(dto.getGroup());
        if(group == null) return null;
        experiment.setResearchGroup(group);
        debug("AddExperimentScenarioForm.group is valid");

        //TODO check if experiment has one or more project types
        List<ProjectType> projects = projectTypeFacade.getAllRecords();
        ProjectType projectType = null;
        for (ProjectType pt : projects){
            if(pt.getTitle().equals(dto.getProject()))
                projectType = pt;
        }
        if(projectType == null) return null;
        Set<ProjectType> projectTypes = new HashSet<ProjectType>();
        projectTypes.add(projectType);
        experiment.setProjectTypes(projectTypes);
        debug("AddExperimentScenarioForm.projectType is valid");

        if(dto.getStart().getTime() >= System.currentTimeMillis()) return null;
        if(dto.getStart().getTime() >= dto.getFinish().getTime()) return null;
        if(dto.getFinish().getTime() >= System.currentTimeMillis()) return null;
        experiment.setStartTime(new Timestamp(dto.getStart().getTime()));
        experiment.setEndTime(new Timestamp(dto.getFinish().getTime()));
        debug("AddExperimentScenarioForm.start,end are valid");

        //TODO add tests for CoExperimenters and TestedSubjects
        List<Person> allPersons = personFacade.getAllRecords();
        for (Person subject : testedSubjectsData){  }
        for (Person subject : coExperimentersData){  }
//        experiment.setPersons(new HashSet<Person>(allPersons));
        debug("AddExperimentScenarioForm.testedSubjects are valid");
        debug("AddExperimentScenarioForm.coExperimenters are valid");

        Set<Stimulus> stimuliSet = new HashSet<Stimulus>();
        for (Stimulus stimulus : stimuliData){
            stimuliSet.add(stimulus);
        }
        if(stimuliSet.size() != stimuliData.size()) return null;
        //TODO add attribute stimuliSet into Experiment
        //experiment.setStimuli(stimuliSet);
        debug("AddExperimentScenarioForm.stimuli are valid");

        return experiment;
    }
}
