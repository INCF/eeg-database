package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.*;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
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

    public AddExperimentScenarioForm(String id){
        super(id, new CompoundPropertyModel<AddExperimentScenarioDTO>(new AddExperimentScenarioDTO()));

        final AutoCompleteTextField<String> scenario = new AutoCompleteTextField<String>("scenario",
                new Model<String>(""))
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

                Locale[] locales = Locale.getAvailableLocales();

                for (final Locale locale : locales)
                {
                    final String country = locale.getDisplayCountry();

                    if (country.toUpperCase().startsWith(input.toUpperCase()))
                    {
                        choices.add(country);
                        if (choices.size() == 10)
                        {
                            break;
                        }
                    }
                }

                return choices.iterator();
            }
        };
        scenario.setRequired(true);
        this.add(scenario);

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
        add(addTestedAjax);

        final ModalWindow newStimulus;
        add(newStimulus = new ModalWindow("newStimulusModal"));
        newStimulus.setCookieName("new-stimulus");

        newStimulus.setPageCreator(new ModalWindow.PageCreator() {

            @Override
            public Page createPage() {
                return new AddStimulusPage(getPage().getPageReference(), newStimulus);
            }
        });

        AjaxButton newStimulusAjax = new AjaxButton("newStimulus", this)
        {};
        newStimulusAjax.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                newStimulus.show(target);
            }
        });
        add(newStimulusAjax);


        TextField<String> group = new TextField<String>("group");
        group.setRequired(true);
        add(group);

        CheckBox isDefaultGroup = new CheckBox("isDefaultGroup");
        isDefaultGroup.setRequired(true);
        add(isDefaultGroup);

        TextField<String> project = new TextField<String>("project");
        project.setRequired(true);
        add(project);
/*
        DateField startDate = new DateField("startDate");
        startDate.setRequired(true);
        add(startDate);

        DateTimeField startTime = new DateTimeField("startTime");
        startTime.setRequired(true);
        add(startTime);

        DateField finishDate = new DateField("finishDate");
        finishDate.setRequired(true);
        add(finishDate);

        DateTimeField finishTime = new DateTimeField("finishTime");
        finishTime.setRequired(true);
        add(finishTime);
*/
        TextField<String> startDate = new TextField<String>("startDate");
        startDate.setRequired(true);
        add(startDate);

        TextField<String> startTime = new TextField<String>("startTime");
        startTime.setRequired(true);
        add(startTime);

        TextField<String> finishDate = new TextField<String>("finishDate");
        finishDate.setRequired(true);
        add(finishDate);

        TextField<String> finishTime = new TextField<String>("finishTime");
        finishTime.setRequired(true);
        add(finishTime);

        TextField<String> testSubject = new TextField<String>("subjects");
        testSubject.setRequired(true);
        add(testSubject);

        TextField<String> stimulus = new TextField<String>("stimulus");
        stimulus.setRequired(true);
        add(stimulus);

        TextField<String> coExperimenters = new TextField<String>("coExperimenters");
        coExperimenters.setRequired(true);
        add(coExperimenters);
    }
}
