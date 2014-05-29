/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   AddExperimentScenarioForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.wizard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.extensions.yui.calendar.DateTimeField;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;

import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.UniqueEntityValidator;
import cz.zcu.kiv.eegdatabase.wui.components.model.LoadableListModel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.ProjectTypeFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddGroupPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddPersonPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddProjectPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddScenarioPage;

public class AddExperimentScenarioForm extends WizardStep {

    private static final long serialVersionUID = 7317609466468641022L;
    protected static Log log = LogFactory.getLog(AddExperimentScenarioForm.class);

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;
    @SpringBean
    private PersonFacade personFacade;
    @SpringBean
    private ScenariosFacade scenarioFacade;
    @SpringBean
    private ProjectTypeFacade projectTypeFacade;
    @SpringBean
    private SecurityFacade securityFacade;

    private DateTimeField finishDate;
    private DateTimeField startDate;
    private ModalWindow window;
    private IModel<Experiment> model;
    private ListMultipleChoice<Person> coexperimenters;
    private DropDownChoice<ResearchGroup> researchGroupChoice;

    public AddExperimentScenarioForm(IModel<Experiment> model) {
        setOutputMarkupId(true);
        this.model = model;

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

        CheckBox privateExperiment = new CheckBox("privateExperiment", new PropertyModel<Boolean>(model.getObject(), "privateExperiment"));
        add(privateExperiment);
    }

    private void addEndDate() {

        finishDate = new DateTimeField("finish", new PropertyModel<Date>(model.getObject(), "finishDate"));
        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(finishDate);
        final FeedbackPanel feedback = new FeedbackPanel("finishFeedback", repeatableFilter);

        feedback.setOutputMarkupId(true);
        finishDate.setOutputMarkupId(true);
        finishDate.setRequired(true);

        prepareEndTime();
        add(finishDate);
        add(feedback);
    }

    private void prepareEndTime() {

        Experiment experiment = model.getObject();
        Scenario scenario = experiment.getScenario();
        if (scenario == null)
            return;
        else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(experiment.getStartDate());
            cal.add(Calendar.MINUTE, scenario.getScenarioLength());
            experiment.setFinishDate(cal.getTime());
        }

    }

    private void addStartDate() {

        startDate = new DateTimeField("start", new PropertyModel<Date>(model.getObject(), "startDate"));
        ComponentFeedbackMessageFilter repeatableFilter = new ComponentFeedbackMessageFilter(startDate);
        final FeedbackPanel feedback = new FeedbackPanel("startFeedback", repeatableFilter);

        feedback.setOutputMarkupId(true);
        startDate.setOutputMarkupId(true);
        startDate.setRequired(true);

        FormComponent component = (FormComponent) startDate.get("hours");
        component.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                doDateFieldModelUpdate(target);
            }
        });

        FormComponent minutes = (FormComponent) startDate.get("minutes");
        minutes.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                doDateFieldModelUpdate(target);
            }
        });

        FormComponent date = (FormComponent) startDate.get("date");
        date.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                doDateFieldModelUpdate(target);
            }
        });

        add(startDate);
        add(feedback);
    }

    private void addProject() {

        // added listmultiplechoice for project types
        ChoiceRenderer<ProjectType> renderer = new ChoiceRenderer<ProjectType>("title", "projectTypeId");
        LoadableListModel<ProjectType> choiceModel = new LoadableListModel<ProjectType>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<ProjectType> load() {

                Experiment experiment2 = model.getObject();
                ResearchGroup researchGroup = experiment2.getResearchGroup();

                if (researchGroup != null)
                    return projectTypeFacade.getRecordsByGroup(researchGroup.getResearchGroupId());
                else
                    return Collections.EMPTY_LIST;
            }

        };
        ListMultipleChoice<ProjectType> projectTypes = new ListMultipleChoice<ProjectType>("projectTypes", new PropertyModel<List<ProjectType>>(model.getObject(), "projectTypes"), choiceModel,
                renderer);
        projectTypes.setLabel(ResourceUtils.getModel("label.projectType"));
        add(projectTypes);
    }

    private void addResearchGroup() {

        // added dropdown choice for research group
        ChoiceRenderer<ResearchGroup> renderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());

        researchGroupChoice = new DropDownChoice<ResearchGroup>("researchGroup", new PropertyModel<ResearchGroup>(model.getObject(), "researchGroup"), choices, renderer);

        researchGroupChoice.setRequired(true);
        researchGroupChoice.setLabel(ResourceUtils.getModel("label.group"));
        researchGroupChoice.add(new CustomGroupValidator());
        researchGroupChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                
                ResearchGroup group = researchGroupChoice.getModelObject();
                if (group != null && group.isLock()) {
                    researchGroupChoice.error(ResourceUtils.getString("text.group.lock.experiment.create", group.getTitle()));
                }
                target.add(AddExperimentScenarioForm.this);
            }
        });
        final FeedbackPanel researchGroupFeedback = createFeedbackForComponent(researchGroupChoice, "groupFeedback");

        add(researchGroupChoice, researchGroupFeedback);

    }

    private void addScenario() {

        // added autocomplete textfield for scenario
        AutoCompleteSettings settings = prepareAutoCompleteSettings();

        AbstractAutoCompleteTextRenderer<Scenario> renderer = new AbstractAutoCompleteTextRenderer<Scenario>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected String getTextValue(Scenario object) {
                return object.getTitle();
            }
        };

        final AutoCompleteTextField<Scenario> scenarioField = new AutoCompleteTextField<Scenario>("scenario", new PropertyModel<Scenario>(model.getObject(), "scenario"),
                Scenario.class, renderer, settings) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<Scenario> getChoices(String input) {

                List<Scenario> choices;
                List<Scenario> allChoices = scenarioFacade.getAllRecords();
                if (Strings.isEmpty(input)) {
                    choices = allChoices;
                } else {
                    choices = new ArrayList<Scenario>(10);
                    for (Scenario t : allChoices) {
                        if ((t.getTitle() != null) &&
                                t.getTitle().toLowerCase().contains(input.toLowerCase())) {
                            choices.add(t);
                        }
                    }
                }
                Collections.sort(choices);
                return choices.iterator();
            }
        };

        final FeedbackPanel scenarioFeedback = createFeedbackForComponent(scenarioField, "scenarioFeedback");
        //FIXME:  doenst work on Experiment EDIT form (change scenario) after migration to potgres + blob type
//       scenarioField.add(new UniqueEntityValidator<Scenario>(scenarioFacade));
        scenarioField.setRequired(true);
        scenarioField.setLabel(ResourceUtils.getModel("label.scenario"));
        add(scenarioField, scenarioFeedback);

        scenarioField.add(new AjaxFormComponentUpdatingBehavior("onBlur") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                prepareEndTime();
                target.add(finishDate);
            }

        });

    }

    private void addTestedSubject() {

        // added autocomplete textfield for subject
        AutoCompleteSettings settings = prepareAutoCompleteSettings();

        AbstractAutoCompleteTextRenderer<Person> renderer = new AbstractAutoCompleteTextRenderer<Person>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected String getTextValue(Person object) {
                return object.getAutoCompleteData();
            }
        };

        final AutoCompleteTextField<Person> personField = new AutoCompleteTextField<Person>("personBySubjectPersonId", new PropertyModel<Person>(model.getObject(), "personBySubjectPersonId"),
                Person.class, renderer, settings) {

            private static final long serialVersionUID = 1L;

            @Override
            protected Iterator<Person> getChoices(String input) {
                List<Person> choices;
                List<Person> allChoices = personFacade.getAllRecords();

                if (Strings.isEmpty(input)) {
                    choices = allChoices;
                } else {

                    choices = new ArrayList<Person>(10);
                    for (Person t : allChoices) {
                        if ((t.getFullName() != null) &&
                                t.getFullName().toLowerCase().contains(input.toLowerCase())) {
                            choices.add(t);
                        }
                    }
                }
                Collections.sort(choices);
                return choices.iterator();
            }
        };

        personField.add(new UniqueEntityValidator<Person>(personFacade));
        personField.setRequired(true);
        personField.setLabel(ResourceUtils.getModel("label.subjectPerson"));
        final FeedbackPanel personFeedback = createFeedbackForComponent(personField, "personFeedback");

        add(personField, personFeedback);
    }

    private void addCoExperimenters() {

        // added listmultiplechoice for coexperimenters
        ChoiceRenderer<Person> renderer = new ChoiceRenderer<Person>("fullName", "personId");
        List<Person> choices = personFacade.getAllRecords();
        Collections.sort(choices);
        coexperimenters = new ListMultipleChoice<Person>("persons", new PropertyModel<List<Person>>(model.getObject(), "persons"), choices, renderer);
        coexperimenters.setMaxRows(10);
        coexperimenters.setLabel(ResourceUtils.getModel("label.coExperimenters"));
        add(coexperimenters);
    }

    private void createModalWindows() {

        window = new ModalWindow("modalWindow");
        window.setResizable(true);
        window.setAutoSize(true);
        window.setMinimalHeight(600);
        window.setMinimalWidth(600);
        add(window);

        addModalWindowAndButton(this, "add-group",
                "addGroup", AddGroupPage.class.getName(), window);

        addModalWindowAndButton(this, "new-project",
                "newProject", AddProjectPage.class.getName(), window);

        addModalWindowAndButton(this, "new-scenario",
                "newScenario", AddScenarioPage.class.getName(), window);

        addModalWindowAndButton(this, "add-tested",
                "addTested", AddPersonPage.class.getName(), window);

        addModalWindowAndButton(this, "add-co-experimenter",
                "addCoExperimenter", AddPersonPage.class.getName(), window);
    }

    private AutoCompleteSettings prepareAutoCompleteSettings() {

        AutoCompleteSettings settings = new AutoCompleteSettings();
        settings.setShowListOnEmptyInput(true);
        settings.setShowCompleteListOnFocusGain(true);
        settings.setUseHideShowCoveredIEFix(false);
        settings.setMaxHeightInPx(200);
        settings.setAdjustInputWidth(false);
        return settings;
    }

    private void addModalWindowAndButton(MarkupContainer container, final String cookieName,
            final String buttonName, final String targetClass, final ModalWindow window) {
        window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClose(AjaxRequestTarget target) {
                ChoiceRenderer<Person> renderer = new ChoiceRenderer<Person>("fullName", "personId");
                List<Person> choices = personFacade.getAllRecords();
                Collections.sort(choices);
                coexperimenters.setChoiceRenderer(renderer);
                coexperimenters.setChoices(choices);

                ChoiceRenderer<ResearchGroup> groupRenderer = new ChoiceRenderer<ResearchGroup>("title", "researchGroupId");
                List<ResearchGroup> groupChoices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(EEGDataBaseSession.get().getLoggedUser());
                researchGroupChoice.setChoiceRenderer(groupRenderer);
                researchGroupChoice.setChoices(groupChoices);

                target.add(AddExperimentScenarioForm.this);


            }
        });

        AjaxButton ajaxButton = new AjaxButton(buttonName)
        {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                window.setCookieName(cookieName);
                window.setPageCreator(new ModalWindow.PageCreator() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Page createPage() {
                        try {
                            Constructor<?> cons = null;
                            cons = Class.forName(targetClass).getConstructor(
                                    PageReference.class, ModalWindow.class);

                            return (Page) cons.newInstance(
                                    getPage().getPageReference(), window);
                        } catch (NoSuchMethodException e) {
                            log.error(e.getMessage(), e);
                        } catch (IllegalAccessException e) {
                            log.error(e.getMessage(), e);
                        } catch (InstantiationException e) {
                            log.error(e.getMessage(), e);
                        } catch (InvocationTargetException e) {
                            log.error(e.getMessage(), e);
                        } catch (ClassNotFoundException e) {
                            log.error(e.getMessage(), e);
                        }
                        return null;
                    }
                });
                window.show(target);
            }
        };

        ajaxButton.setDefaultFormProcessing(false);
        container.add(ajaxButton);
    }
    
    private void doDateFieldModelUpdate(AjaxRequestTarget target) {
        try {
            Experiment experiment = model.getObject();

            Calendar newOne = Calendar.getInstance();
            Calendar oldOne = Calendar.getInstance();
            oldOne.setTime(startDate.getDate());
            newOne.set(oldOne.get(Calendar.YEAR), oldOne.get(Calendar.MONTH), oldOne.get(Calendar.DAY_OF_MONTH), startDate.getHours(), startDate.getMinutes());
            experiment.setStartDate(newOne.getTime());

            if (experiment.getScenario() != null)
                newOne.add(Calendar.MINUTE, experiment.getScenario().getScenarioLength());

            experiment.setFinishDate(newOne.getTime());

            target.add(AddExperimentScenarioForm.this);
        }
        catch (Exception e) {

        }
    }

    private FeedbackPanel createFeedbackForComponent(FormComponent component, String id) {

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(component);
        final FeedbackPanel feedback = new FeedbackPanel(id, filter);
        feedback.setOutputMarkupId(true);

        return feedback;
    }
    
    private class CustomGroupValidator implements IValidator<ResearchGroup> {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<ResearchGroup> validatable) {
            final ResearchGroup group = validatable.getValue();

            if (!securityFacade.personAbleToWriteIntoGroup(group.getResearchGroupId())) {
                error(ResourceUtils.getString("invalid.notAbleToAddExperimentInGroup"));
            }
            
        }
        
    }
}
