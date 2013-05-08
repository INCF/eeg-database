package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.models.PersonNameModel;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.models.StimulusDescriptioinModel;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AddingItemsView;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
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

import java.util.*;


/**
 * Created by IntelliJ IDEA.
 * User: Matheo
 * Date: 26.3.13
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
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

    private Experiment experiment = new Experiment();
    private Scenario scenarioEntity = new Scenario();

    private final List<Person> coExperimentersData = new ArrayList<Person>();
    private final List<Person> testedSubjectsData = new ArrayList<Person>();

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
        isDefaultGroup.setRequired(true);
        add(isDefaultGroup);

        TextField<String> project = new TextField<String>("project");
        //TODO autocomplete and create ProjectTypeFacade
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
        final List<Stimulus> stimulusList = new ArrayList<Stimulus>();
        stimulusList.add(new Stimulus());
        final WebMarkupContainer stimulusContainer = new WebMarkupContainer("addStimulusInputContainer");

        final ListView<Stimulus> stimulusListView = new ListView<Stimulus>("addStimulusInput", stimulusList) {
             @Override
            protected void populateItem(ListItem item) {
                final Stimulus stimulusItem = (Stimulus) item.getModelObject();
                final AddingItemsView<String> stimulus =
                        new AddingItemsView<String>(
                                "stimuli",
                                new StimulusDescriptioinModel(stimulusItem),
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
                return new AddTestedSubjectPage(getPage().getPageReference(), addTested);
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
                //TODO refactor AddTestedSubjectPage or create own class (AddCoExperimenterPage)
                return new AddTestedSubjectPage(getPage().getPageReference(), newCoExperimenter);
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
        return !hasError() && validPersons;
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

}
