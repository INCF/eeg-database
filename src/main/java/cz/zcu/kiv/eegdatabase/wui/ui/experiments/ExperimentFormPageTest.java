package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import com.google.common.io.Files;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.XMLTemplate;
import cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate.XMLTemplateReader;
import cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate.XMLTemplateWriter;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.model.LoadableListModel;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.TemplateFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.odMLForms.SectionCell;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms.odMLForms.SubsectionsCell;
import cz.zcu.kiv.eegdatabase.wui.ui.experiments.modals.AddSaveAsPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the ${PROJECT_NAME} project
 * <p/>
 * ==========================================
 * <p/>
 * Copyright (C) 2014 by University of West Bohemia (http://www.zcu.cz/en/)
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * ${NAME}, 2014/07/01 11:57 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
@AuthorizeInstantiation(value = {"ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN"})
public class ExperimentFormPageTest extends MenuPage {

    protected static Log log = LogFactory.getLog(ExperimentFormPageTest.class);

    @SpringBean
    private TemplateFacade templateFacade;
    private ModalWindow window;
    private final Form<XMLTemplate> form;
    private final CheckBox defBox;
    private final FeedbackPanel feedback;
    private final DropDownChoice<Template> dropDownChoice;

    public ExperimentFormPageTest() {
        setPageTitle(ResourceUtils.getModel("pageTitle.experimentDetail"));
        add(new ButtonPageMenu("leftMenu", ExperimentsPageLeftMenu.values()));

        feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);

        //----------dropdown---------
        ChoiceRenderer<Template> renderer = new ChoiceRenderer<Template>("name", "templateId");
        LoadableListModel<Template> choiceModel = new LoadableListModel<Template>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected List<Template> load() {
                return getTemplates();
            }

        };
        dropDownChoice = new DropDownChoice<Template>("templateDD",
                new Model<Template>((choiceModel.getObject().get(0))), choiceModel, renderer);
        //---------form---------
        XMLTemplate xmlTemplate = new XMLTemplate();
        try {
            xmlTemplate = readTemplate(dropDownChoice.getModelObject());
        } catch (JAXBException e) {
            log.error(e.getMessage());
        }

        CompoundPropertyModel<XMLTemplate> formModel
                = new CompoundPropertyModel<XMLTemplate>(xmlTemplate);
        form = new Form<XMLTemplate>("form", formModel) {
            @Override
            protected void onSubmit() {
                super.onSubmit();
            }

            @Override
            protected void onError() {
                add(feedback);
                super.onError();
            }
        };

        form.setOutputMarkupId(true);
        //--------listView-------
        final ListView<SectionType> view = new PropertyListView<SectionType>("sections") {
            @Override
            protected void populateItem(final ListItem<SectionType> item) {
                final SubsectionsCell subsectionsCell = new SubsectionsCell("cell2", item.getModel());
                final SectionCell sectionCell = new SectionCell("cell1", item.getModel());
                item.add(sectionCell);
                item.add(subsectionsCell);
            }
        };

        //-----save components-----
        defBox = new CheckBox("defBox", Model.of(Boolean.FALSE));
        defBox.setVisible(false);
        final Label defLabel = new Label("defLabel", "");
        defLabel.setVisible(false);
        //visible only for admin
        if (EEGDataBaseSession.get().getLoggedUser().getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
            defBox.setVisible(true);
            defLabel.setVisible(true);
        }
        final TextField<String> saveName = new TextField<String>("name");
        saveName.setOutputMarkupId(true);

        final AjaxButton save = new AjaxButton("saveBT", ResourceUtils.getModel("label.saveTemplate")) {
            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> f) {
                XMLTemplate xmlTemplate = form.getModelObject();
                String name = saveName.getModelObject();
                Person user = EEGDataBaseSession.get().getLoggedUser();
                Template template = templateFacade.getTemplateByPersonAndName(user.getPersonId(), name);
                boolean isDefault = (defBox.isVisible() && defBox.getModelObject());
                //template with same name exists => update
                if (template != null) {
                    try {
                        updateTemplate(xmlTemplate, template, isDefault);
                    } catch (JAXBException e) {
                        log.error(e.getMessage(), e);
                        error(ResourceUtils.getString("error.writingTemplate"));
                    }

                } else { //template does not exists => create
                    try {
                        template = createTemplate(xmlTemplate, isDefault);
                        templateFacade.create(template);
                    } catch (JAXBException e) {
                        log.error(e.getMessage(), e);
                        error(ResourceUtils.getString("error.writingTemplate"));
                    }
                }
                target.add(feedback);
                target.add(dropDownChoice);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        };

        //---------behavior---------
        OnChangeAjaxBehavior onChangeFormBehavior = new OnChangeAjaxBehavior() {
            protected void onUpdate(AjaxRequestTarget target) {
                try {
                    form.setDefaultModel(
                            new CompoundPropertyModel<XMLTemplate>(readTemplate(dropDownChoice.getModelObject())));
                } catch (JAXBException e) {
                    log.error(e.getMessage());
                }
                form.add(view);
                form.add(saveName);
                target.add(form);
            }
        };
        //---------------------------

        dropDownChoice.add(onChangeFormBehavior);
        dropDownChoice.setOutputMarkupId(true);
        form.add(view);
        add(dropDownChoice);
        form.add(feedback);
        add(form);
        form.add(save);
        form.add(saveName);
        form.add(defBox);
        form.add(defLabel);
        createModalWindow();
    }

    /**
     * Loads default and user's templates from database
     *
     * @return templates
     */
    private List<Template> getTemplates() {
        int loggedUserId = EEGDataBaseSession.get().getLoggedUser().getPersonId();
        List<Template> userTemplates = templateFacade.getUsableTemplates(loggedUserId);
        if (userTemplates == null) userTemplates = new ArrayList<Template>();

        return userTemplates;
    }

    /**
     * Reads xml template into list of sections
     *
     * @param template selected template
     * @return list of sections
     */
    private XMLTemplate readTemplate(Template template) throws JAXBException {
        return XMLTemplateReader.read(template.getTemplate());
    }

    /**
     * Writes sections into xml and returns it as Template
     *
     * @param xmlTemplate  Template to write to XML
     * @param isDefault true if this template is default for all users
     * @return Template that can be saved to database
     */
    private Template createTemplate(XMLTemplate xmlTemplate, boolean isDefault) throws JAXBException {
        Template template = new Template();
        byte[] xmlData = XMLTemplateWriter.writeTemplate(xmlTemplate);
        template.setTemplate(xmlData);
        template.setIsDefault(isDefault);
        template.setName(xmlTemplate.getName());
        template.setPersonByPersonId(EEGDataBaseSession.get().getLoggedUser());

        return template;
    }

    /**
     * Updates existing template
     *
     * @param xmlTemplate  new Template content
     * @param template  Original Template
     * @param isDefault true if updated template is default for all users
     */
    private void updateTemplate(XMLTemplate xmlTemplate, Template template, boolean isDefault) throws JAXBException {
        byte[] xmlData = XMLTemplateWriter.writeTemplate(xmlTemplate);
        template.setTemplate(xmlData);
        template.setIsDefault(isDefault);
        templateFacade.update(template);
    }

    private void loadTemplateFromFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        Template def = new Template();
        def.setPersonByPersonId(EEGDataBaseSession.get().getLoggedUser());
        def.setName("DEF_TEMPLATE");
        def.setIsDefault(false);
        byte[] data = Files.toByteArray(file);
        def.setTemplate(data);
        templateFacade.create(def);
    }

    private void createModalWindow() {

        window = new ModalWindow("modalWindow");
        window.setResizable(true);
        window.setAutoSize(true);
        window.setMinimalHeight(300);
        window.setMinimalWidth(600);
        window.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClose(AjaxRequestTarget target) {
                target.add(dropDownChoice);
            }
        });

        add(window);
        addModalWindowAndButton(form, "save-as",
                "saveAsBT", AddSaveAsPage.class.getName(), window);
    }

    private void addModalWindowAndButton(MarkupContainer container, final String cookieName,
                                         final String buttonName, final String targetClass, final ModalWindow window) {

        AjaxButton ajaxButton = new AjaxButton(buttonName, ResourceUtils.getModel("label.saveAsTemplate")) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, final Form<?> f) {

                window.setCookieName(cookieName);
                window.setPageCreator(new ModalWindow.PageCreator() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Page createPage() {
                        try {
                            Constructor<?> cons = Class.forName(targetClass).getConstructor(
                                    PageReference.class, ModalWindow.class, Form.class, CheckBox.class);

                            return (Page) cons.newInstance(
                                    getPage().getPageReference(), window, form, defBox);
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
                //target.appendJavaScript("$('#modalWindow').css('top',  window.pageYOffset + 50+\"px\");"); //not working
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        };
        ajaxButton.setDefaultFormProcessing(false);
        container.add(ajaxButton);
    }
}
