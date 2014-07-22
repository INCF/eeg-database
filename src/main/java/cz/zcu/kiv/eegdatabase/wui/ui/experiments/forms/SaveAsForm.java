package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.data.xmlObjects.odMLSection.SectionType;
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
import org.apache.wicket.spring.injection.annot.SpringBean;

import javax.xml.stream.XMLStreamException;
import java.util.List;

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
    /**
     * Constructs a form with no validation.
     *
     * @param id See Component
     */
    public SaveAsForm(String id, final ModalWindow window, final Form<List<SectionType>> form,
                      final CheckBox defBox) {
        super(id, Model.of("New Template"));

        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        add(new Label("saveAsHeader", ResourceUtils.getModel("pageTitle.saveAs")));

        final RequiredTextField<String> name = new RequiredTextField<String>("nameField",
                Model.of("New Template"));
        add(name);
        add(new AjaxButton("submitForm", ResourceUtils.getModel("button.submitForm"), this) {

            private static final long serialVersionUID = -975100666951875819L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> f) {
                try {
                    List<SectionType> sections = form.getModelObject();
                    boolean isDefault = (defBox.isVisible() && defBox.getModelObject());
                    Template template = createTemplate(sections, name.getModelObject(), isDefault);
                    templateFacade.create(template);
                } catch (XMLStreamException e) {
                    log.error(e.getMessage(), e);
                    error(ResourceUtils.getString("error.writingTemplate"));
                }
                window.close(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }

        });

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
     * @param sections Sections to write to XML
     * @param name Template name
     * @param isDefault true if this template is default for all users
     * @return Template that can be saved to database
     * @throws XMLStreamException XML writing exception
     */
    private Template createTemplate(List<SectionType> sections, String name, boolean isDefault) throws XMLStreamException {
        Template template = new Template();
        byte[] xmlData = XMLTemplateWriter.writeTemplate(sections);
        template.setTemplate(xmlData);
        template.setIsDefault(isDefault);
        template.setName(name);
        template.setPersonByPersonId(EEGDataBaseSession.get().getLoggedUser());

        return template;
    }
}
