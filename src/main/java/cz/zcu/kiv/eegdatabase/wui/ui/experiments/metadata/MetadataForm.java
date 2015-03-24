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
 *   MetadataForm.java, 2015/02/26 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.metadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import odml.core.Reader;
import odml.core.Section;
import odml.core.Writer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.wizard.Wizard;
import org.apache.wicket.extensions.wizard.WizardModel;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.sun.xml.messaging.saaj.util.ByteInputStream;

import cz.zcu.kiv.eegdatabase.data.pojo.Template;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.form.input.AjaxDropDownChoice;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.experiments.metadata.TemplateFacade;

public class MetadataForm extends Panel {

    private static final long serialVersionUID = -8842966593408416974L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    private TemplateFacade templateFacade;

    private CompoundPropertyModel<Section> model;

    public MetadataForm(String id) {
        this(id, new Model<Section>(new Section()));
    }

    public MetadataForm(String id, IModel<Section> model) {
        super(id);

        this.model = new CompoundPropertyModel<Section>(model);
        setDefaultModel(this.model);
        setOutputMarkupId(true);
        
        // TODO select template not work.
        WizardModel wizardModel = new WizardModel();
        for (Section section : model.getObject().getSections()) {
            wizardModel.add(new MetadataWizardStep(new Model<Section>(section)));
        }

        Wizard wizard = new Wizard("wizard", wizardModel, true) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onFinish() {
                try {
                    Section data = MetadataForm.this.model.getObject();
                    FileOutputStream stream = new FileOutputStream(new File("D:\\trash\\"+ data.getName() + ".xml"));

                    Writer writer = new Writer(data, true, true);
                    if (writer.write(stream)) {
                        info("Save template done.");
                    } else {
                        error("Save template failed.");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onCancel() {
                throw new RestartResponseAtInterceptPageException(MetadataForm.this.getPage().getPageClass(), MetadataForm.this.getPage().getPageParameters());
            }
            
        };

        add(wizard);

        int personId = EEGDataBaseSession.get().getLoggedUser().getPersonId();
        List<Template> templatesByPerson = templateFacade.getTemplatesByPerson(personId);
        ChoiceRenderer<Template> templateChoiceRenderer = new ChoiceRenderer<Template>("name", "templateId");
        AjaxDropDownChoice<Template> templateSelection = new AjaxDropDownChoice<Template>("template-choice", new Model<Template>(), templatesByPerson, templateChoiceRenderer) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSelectionChangeAjaxified(AjaxRequestTarget target, Template template) {
                
                try {
                    Reader reader = new Reader();
                    Section section = reader.load(new ByteInputStream(template.getTemplate(), template.getTemplate().length));
                    section.setName(template.getName());
                    MetadataForm.this.model.setObject(section);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    error(ResourceUtils.getString("text.template.error.load"));
                }
                
                target.add(MetadataForm.this);
            }

        };

        add(templateSelection);
    }

}
