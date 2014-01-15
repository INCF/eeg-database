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
 *   ScenarioForm.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments.forms;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.w3c.dom.Document;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.common.StimulusFacade;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.type.ScenarioTypeFacade;

public class ScenarioForm extends Form<Scenario> {

    private static final long serialVersionUID = 4355778752386684554L;

    @SpringBean
    private ScenariosFacade scenariosFacade;

    @SpringBean
    private ScenarioTypeFacade scenarioTypeFacade;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    @SpringBean
    private StimulusFacade stimulusFacade;

    public ScenarioForm(String id, final ModalWindow window) {
        super(id, new CompoundPropertyModel<Scenario>(new Scenario()));

        add(new Label("addScenarioHeader", ResourceUtils.getModel("pageTitle.addScenario")));
        
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);

        setMultiPart(true);

        Person owner = getModel().getObject().getPerson();
        if (owner == null)
            owner = EEGDataBaseSession.get().getLoggedUser();

        getModel().getObject().setPerson(owner);

        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(owner);
        if (choices == null || choices.isEmpty())
            choices = Arrays.asList(getModel().getObject().getResearchGroup());

        DropDownChoice<ResearchGroup> groups = new DropDownChoice<ResearchGroup>("researchGroup", choices, new ChoiceRenderer<ResearchGroup>(
                "title"));
        groups.setRequired(true);
        
        TextField<String> title = new TextField<String>("title");
        title.setLabel(ResourceUtils.getModel("label.scenarioTitle"));
        title.setRequired(true);
        title.add(new TitleExistsValidator());

        TextField<Integer> length = new TextField<Integer>("scenarioLength", Integer.class);
        length.setRequired(true);
        length.add(RangeValidator.minimum(0));

        TextArea<String> description = new TextArea<String>("description");
        description.setRequired(true);
        description.setLabel(ResourceUtils.getModel("label.scenarioDescription"));
        description.add(StringValidator.maximumLength(255));

        final WebMarkupContainer fileContainer = new WebMarkupContainer("contailer");
        fileContainer.setVisibilityAllowed(false);
        fileContainer.setOutputMarkupPlaceholderTag(true);

        /*
         * TODO file field for xml was not visible in old portal. I dont know why. So I hided it but its implemented and not tested.
         */
        // hidded line in old portal
        final DropDownChoice<ScenarioSchemas> schemaList = new DropDownChoice<ScenarioSchemas>("schemaList", new Model<ScenarioSchemas>(), scenariosFacade.getListOfScenarioSchemas(),
                new ChoiceRenderer<ScenarioSchemas>("schemaName", "schemaId"));
        schemaList.setOutputMarkupPlaceholderTag(true);
        schemaList.setRequired(true);
        schemaList.setLabel(ResourceUtils.getModel("label.scenarioSchema"));
        schemaList.setVisibilityAllowed(false);

        final FileUploadField file = new FileUploadField("file", new ListModel<FileUpload>());
        file.setOutputMarkupId(true);
        file.setLabel(ResourceUtils.getModel("description.fileType.dataFile"));
        file.setOutputMarkupPlaceholderTag(true);

        // hidded line in old portal
        final FileUploadField xmlfile = new FileUploadField("xmlfile", new ListModel<FileUpload>());
        xmlfile.setOutputMarkupId(true);
        xmlfile.setLabel(ResourceUtils.getModel("label.xmlDataFile"));
        xmlfile.setOutputMarkupPlaceholderTag(true);
        xmlfile.setVisibilityAllowed(false);

        // hidded line in old portal
        final RadioGroup<Boolean> schema = new RadioGroup<Boolean>("schema", new Model<Boolean>(Boolean.FALSE));
        schema.add(new Radio<Boolean>("noschema", new Model<Boolean>(Boolean.FALSE), schema));
        schema.add(new Radio<Boolean>("fromschema", new Model<Boolean>(Boolean.TRUE), schema));
        schema.setOutputMarkupPlaceholderTag(true);
        schema.setVisibilityAllowed(false);
        schema.add(new AjaxFormChoiceComponentUpdatingBehavior() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                schemaList.setVisibilityAllowed(schema.getModelObject());
                xmlfile.setRequired(!xmlfile.isRequired());

                target.add(xmlfile);
                target.add(schemaList);
            }
        });

        CheckBox privateCheck = new CheckBox("privateScenario");
        final CheckBox dataAvailable = new CheckBox("dataAvailable", new Model<Boolean>());
        dataAvailable.add(new AjaxFormComponentUpdatingBehavior("onChange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {

                fileContainer.setVisibilityAllowed(dataAvailable.getModelObject());
                file.setRequired(!file.isRequired());
                target.add(fileContainer);
            }
        });

        fileContainer.add(file, xmlfile, schema, schemaList);

        add(groups, title, length, description, privateCheck, dataAvailable, fileContainer);

        add(new AjaxButton("submitForm", this) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

                FileUpload fileUpload = file.getFileUpload();
                FileUpload xmlfileUpload = xmlfile.getFileUpload();

                Scenario scenario = ScenarioForm.this.getModelObject();
                
                target.add(ScenarioForm.this);
                
                // loading non-XML
                if ((fileUpload != null) && (!(fileUpload.getSize() == 0))) {
                    // File uploaded
                    String filename = fileUpload.getClientFileName().replace(" ", "_");
                    scenario.setScenarioName(filename);
                    if (fileUpload.getContentType().length() > CoreConstants.MAX_MIMETYPE_LENGTH) {
                        int index = filename.lastIndexOf(".");
                        scenario.setMimetype(filename.substring(index));
                    } else {
                        scenario.setMimetype(fileUpload.getContentType());
                    }

                }

                // loading XML
                if ((xmlfileUpload != null) && (!(xmlfileUpload.getSize() == 0))) {
                    // load the XML file to a table with the XMLType column
                    String filename = xmlfileUpload.getClientFileName().replace(" ", "_");
                    scenario.setScenarioName(filename);

                    scenario.setMimetype(xmlfileUpload.getContentType());

                    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder;
                    Document doc = null;
                    try {
                        docBuilder = docFactory.newDocumentBuilder();
                        InputStream inputStream = xmlfileUpload.getInputStream();
                        doc = docBuilder.parse(inputStream);
                    } catch (Exception e) {
                        feedback.error(e.getMessage());
                        target.add(feedback);
                        return;
                    }

                    // getting the right scenarioType bean
                    // no schema - binary storage
                    Boolean isSchemaSelected = schema.getModelObject();
                    if (!isSchemaSelected) {
                    }
                    // schema selected - structured storage
                    else {
                        Class c;
                        try {
                            c = Class.forName("cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeSchema" + schemaList.getModelObject().getSchemaId());
                        } catch (Exception e) {
                            feedback.error(e.getMessage());
                            target.add(feedback);
                            return;
                        }
                    }

                }

                if (!dataAvailable.getModelObject()) {
                }
                scenariosFacade.create(scenario);
                window.close(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {

                target.add(feedback);
            }
        });

        add(new AjaxButton("closeForm", ResourceUtils.getModel("button.close")) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        }.setDefaultFormProcessing(false));

        setOutputMarkupId(true);
    }
    
    private class TitleExistsValidator implements IValidator<String> {

        private static final long serialVersionUID = 1L;

        @Override
        public void validate(IValidatable<String> validatable) {
            final String title = validatable.getValue();

            if (scenariosFacade.existsScenario(title)) {
                error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
            }
        }
    }
}
