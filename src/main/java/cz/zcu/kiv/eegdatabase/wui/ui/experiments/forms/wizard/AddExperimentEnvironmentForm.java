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
 *   AddExperimentEnvironmentForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.wizard;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.wizard.WizardStep;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;

import cz.zcu.kiv.eegdatabase.data.pojo.Disease;
import cz.zcu.kiv.eegdatabase.data.pojo.Experiment;
import cz.zcu.kiv.eegdatabase.data.pojo.Hardware;
import cz.zcu.kiv.eegdatabase.data.pojo.Pharmaceutical;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import cz.zcu.kiv.eegdatabase.data.pojo.Weather;
import cz.zcu.kiv.eegdatabase.wui.components.model.LoadableListModel;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.DiseaseFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.HardwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.PharmaceuticalFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.SoftwareFacade;
import cz.zcu.kiv.eegdatabase.wui.core.common.WeatherFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddDiseasePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddHardwarePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddPharmaceuticalsPage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddSoftwarePage;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddWeatherPage;

public class AddExperimentEnvironmentForm extends WizardStep {

    private static final long serialVersionUID = 3654634690930209237L;
    protected static Log log = LogFactory.getLog(AddExperimentScenarioForm.class);

    @SpringBean
    private WeatherFacade weatherFacade;
    @SpringBean
    private HardwareFacade hardwareFacade;
    @SpringBean
    private SoftwareFacade softwareFacade;
    @SpringBean
    private DiseaseFacade diseaseFacade;
    @SpringBean
    private PharmaceuticalFacade pharmaceuticalFacade;

    final int SELECT_ROWS = 5;
    private ModalWindow window;
    private IModel<Experiment> model;

    public AddExperimentEnvironmentForm(IModel<Experiment> model) {

        this.model = model;
        setOutputMarkupId(true);

        addHardware();
        addSoftware();
        addWeather();
        addTemperature();
        addEnvironmentNote();
        addDisease();
        addPharmaceutical();

        createModalWindows();
    }

    private void addHardware() {

        ChoiceRenderer<Hardware> renderer = new ChoiceRenderer<Hardware>("title", "hardwareId");
        LoadableListModel<Hardware> choiceModel = new LoadableListModel<Hardware>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Hardware> load() {

                Experiment experiment2 = model.getObject();
                ResearchGroup researchGroup = experiment2.getResearchGroup();

                if (researchGroup != null)
                    return hardwareFacade.getRecordsByGroup(researchGroup.getResearchGroupId());
                else
                    return Collections.EMPTY_LIST;
            }

        };
        ListMultipleChoice<Hardware> hardwareChoice = new ListMultipleChoice<Hardware>("hardwares", new PropertyModel<List<Hardware>>(model.getObject(), "hardwares"), choiceModel, renderer);
        hardwareChoice.setMaxRows(SELECT_ROWS);
        hardwareChoice.setLabel(ResourceUtils.getModel("label.hardware"));
        hardwareChoice.setRequired(true);

        final FeedbackPanel feedback = createFeedbackForComponent(hardwareChoice, "hardwareFeedback");
        add(hardwareChoice);
        add(feedback);
    }

    private void addSoftware() {

        ChoiceRenderer<Software> renderer = new ChoiceRenderer<Software>("title", "softwareId");
        LoadableListModel<Software> choiceModel = new LoadableListModel<Software>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Software> load() {

                Experiment experiment2 = model.getObject();
                ResearchGroup researchGroup = experiment2.getResearchGroup();

                if (researchGroup != null)
                    return softwareFacade.getRecordsByGroup(researchGroup.getResearchGroupId());
                else
                    return Collections.EMPTY_LIST;
            }

        };
        ListMultipleChoice<Software> swChoice = new ListMultipleChoice<Software>("softwares", new PropertyModel<List<Software>>(model.getObject(), "softwares"), choiceModel, renderer);
        swChoice.setMaxRows(SELECT_ROWS);
        swChoice.setLabel(ResourceUtils.getModel("label.software"));
        swChoice.setRequired(true);

        final FeedbackPanel feedback = createFeedbackForComponent(swChoice, "softwareFeedback");
        add(swChoice);
        add(feedback);
    }

    private void addWeather() {

        ChoiceRenderer<Weather> renderer = new ChoiceRenderer<Weather>("title", "weatherId");
        LoadableListModel<Weather> choiceModel = new LoadableListModel<Weather>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Weather> load() {

                Experiment experiment2 = model.getObject();
                ResearchGroup researchGroup = experiment2.getResearchGroup();

                if (researchGroup != null)
                    return weatherFacade.getRecordsByGroup(researchGroup.getResearchGroupId());
                else
                    return Collections.EMPTY_LIST;
            }

        };
        DropDownChoice<Weather> weather = new DropDownChoice<Weather>("weather", new PropertyModel<Weather>(model.getObject(), "weather"), choiceModel, renderer);
        weather.setLabel(ResourceUtils.getModel("label.weather"));
        weather.setRequired(true);

        final FeedbackPanel feedback = createFeedbackForComponent(weather, "weatherFeedback");

        add(weather);
        add(feedback);
    }

    private void addTemperature() {

        TextField<Integer> temperature = new TextField<Integer>("temperature", new PropertyModel<Integer>(model.getObject(), "temperature"), Integer.class);
        temperature.setLabel(ResourceUtils.getModel("label.temperature"));
        temperature.setRequired(true);
        temperature.add(RangeValidator.minimum(Integer.valueOf(-273)));

        final FeedbackPanel feedback = createFeedbackForComponent(temperature, "temperatureFeedback");

        add(temperature);
        add(feedback);
    }

    private void addEnvironmentNote() {

        TextField<String> environmentNote = new TextField<String>("environmentNote", new PropertyModel<String>(model.getObject(), "environmentNote"));
        environmentNote.setLabel(ResourceUtils.getModel("label.environmentNote"));
        add(environmentNote);
    }

    private void addDisease() {

        ChoiceRenderer<Disease> renderer = new ChoiceRenderer<Disease>("title", "diseaseId");
        LoadableListModel<Disease> choiceModel = new LoadableListModel<Disease>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Disease> load() {

                Experiment experiment2 = model.getObject();
                ResearchGroup researchGroup = experiment2.getResearchGroup();

                if (researchGroup != null)
                    return diseaseFacade.getRecordsByGroup(researchGroup.getResearchGroupId());
                else
                    return Collections.EMPTY_LIST;
            }

        };
        ListMultipleChoice<Disease> diseasesChoice = new ListMultipleChoice<Disease>("diseases", new PropertyModel<List<Disease>>(model.getObject(), "diseases"), choiceModel, renderer);
        diseasesChoice.setMaxRows(SELECT_ROWS);
        diseasesChoice.setLabel(ResourceUtils.getModel("label.disease"));

        add(diseasesChoice);
    }

    private void addPharmaceutical() {

        ChoiceRenderer<Pharmaceutical> renderer = new ChoiceRenderer<Pharmaceutical>("title", "pharmaceuticalId");
        LoadableListModel<Pharmaceutical> choiceModel = new LoadableListModel<Pharmaceutical>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Pharmaceutical> load() {

                Experiment experiment2 = model.getObject();
                ResearchGroup researchGroup = experiment2.getResearchGroup();

                if (researchGroup != null)
                    return pharmaceuticalFacade.getRecordsByGroup(researchGroup.getResearchGroupId());
                else
                    return Collections.EMPTY_LIST;
            }

        };
        ListMultipleChoice<Pharmaceutical> pharmaceuticalsChoice = new ListMultipleChoice<Pharmaceutical>("pharmaceuticals", new PropertyModel<List<Pharmaceutical>>(model.getObject(), "pharmaceuticals"), choiceModel, renderer);
        pharmaceuticalsChoice.setMaxRows(SELECT_ROWS);
        pharmaceuticalsChoice.setLabel(ResourceUtils.getModel("label.pharmaceutical"));

        add(pharmaceuticalsChoice);
    }

    private void createModalWindows() {

        window = new ModalWindow("modalWindow");
        window.setResizable(true);
        window.setAutoSize(true);
        window.setMinimalHeight(600);
        window.setMinimalWidth(600);
        window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(AddExperimentEnvironmentForm.this);
            }
        });

        add(window);

        addModalWindowAndButton(this, "add-disease",
                "addDisease", AddDiseasePage.class.getName(), window);

        addModalWindowAndButton(this, "add-pharma",
                "addPharma", AddPharmaceuticalsPage.class.getName(), window);

        addModalWindowAndButton(this, "add-hw",
                "addHW", AddHardwarePage.class.getName(), window);

        addModalWindowAndButton(this, "add-sw",
                "addSW", AddSoftwarePage.class.getName(), window);

        addModalWindowAndButton(this, "add-weather",
                "addWeather", AddWeatherPage.class.getName(), window);
    }

    private void addModalWindowAndButton(MarkupContainer container, final String cookieName,
            final String buttonName, final String targetClass, final ModalWindow window) {

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

    private FeedbackPanel createFeedbackForComponent(FormComponent component, String id) {

        ComponentFeedbackMessageFilter filter = new ComponentFeedbackMessageFilter(component);
        final FeedbackPanel feedback = new FeedbackPanel(id, filter);
        feedback.setOutputMarkupId(true);

        return feedback;
    }
}
