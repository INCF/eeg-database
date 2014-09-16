package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.XMLTemplate;
import cz.zcu.kiv.eegdatabase.logic.xml.XMLTemplate.XMLTemplateWriter;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.common.TemplateFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.xml.bind.JAXBException;

/**
 * ********************************************************************************************************************
 * <p/>
 * This file is part of the eegdatabase project
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
 * SaveAsForm, 2014/07/22 15:46 Prokop
 * <p/>
 * ********************************************************************************************************************
 */
public class SaveAsForm extends Form {

    protected static Log log = LogFactory.getLog(SaveAsForm.class);

    @SpringBean
    private TemplateFacade templateFacade;
    private boolean update = false;

    /**
     * Constructs a form with no validation.
     *
     * @param id See Component
     */
    public SaveAsForm(String id, final ModalWindow window, final Form<XMLTemplate> form,
                      final CheckBox defBox) {
        super(id, Model.of("New Template"));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        final FeedbackPanel warningFeedback = new FeedbackPanel("warning");
        feedback.setOutputMarkupId(true);
        warningFeedback.setOutputMarkupId(true);
        add(warningFeedback);
        add(feedback);
        add(new Label("saveAsHeader", ResourceUtils.getModel("pageTitle.saveAs")));

        final RequiredTextField<String> name = new RequiredTextField<String>("Name", new PropertyModel<String>(form.getModelObject(), "name"));
        add(name);

        add(new AjaxButton("submitBtn", ResourceUtils.getModel("button.save"), this) {

            private static final long serialVersionUID = -97510066651875819L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                try {
                    if(!update) {
                        String templateName = name.getModelObject();
                        if (templateFacade.canSaveName(templateName,
                                EEGDataBaseSession.get().getLoggedUser().getPersonId())) {
                            XMLTemplate xmlTemplate = form.getModelObject();
                            xmlTemplate.setName(name.getModelObject());
                            boolean isDefault = (defBox.isVisible() && defBox.getModelObject());
                            Template template = createTemplate(xmlTemplate, isDefault);
                            templateFacade.create(template);
                        } else {
                            update = true;
                            warningFeedback.warn(ResourceUtils.getString("warning.overrideTemplate"));
                            target.add(warningFeedback);
                            return;
                        }
                    } else {
                        String templateName = name.getModelObject();
                        XMLTemplate xmlTemplate = form.getModelObject();
                        boolean isDefault = (defBox.isVisible() && defBox.getModelObject());
                        if (templateFacade.canSaveName(templateName,
                                EEGDataBaseSession.get().getLoggedUser().getPersonId())) {
                            Template template = createTemplate(xmlTemplate, isDefault);
                            templateFacade.create(template);
                        } else {
                            Template template = templateFacade.getTemplateByPersonAndName(
                                    EEGDataBaseSession.get().getLoggedUser().getPersonId(), templateName);
                            updateTemplate(xmlTemplate, template, isDefault);
                            update = false;
                        }
                    }
                } catch (JAXBException e) {
                    log.error(e.getMessage(), e);
                    feedback.error(ResourceUtils.getString("error.writingTemplate"));
                    target.add(feedback);
                    update = false;
                    return;
                }
                window.close(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }

        }.setOutputMarkupId(true));

        add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close"), this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        }.setDefaultFormProcessing(false));

        setOutputMarkupId(true);
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
}
