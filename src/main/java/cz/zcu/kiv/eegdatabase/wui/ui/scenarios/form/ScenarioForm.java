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
 *   ScenarioForm.java, 2013/28/11 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
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
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;

public class ScenarioForm extends Form<Scenario> {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private ScenariosFacade scenariosFacade;

    @SpringBean
    ScenarioXMLProvider xmlProvider;

    @SpringBean
    private ResearchGroupFacade researchGroupFacade;

    public ScenarioForm(String id, IModel<Scenario> model, final FeedbackPanel feedback) {
        super(id, new CompoundPropertyModel<Scenario>(model));

        setMultiPart(true);

        Person owner = model.getObject().getPerson();
        if (owner == null)
            owner = EEGDataBaseSession.get().getLoggedUser();

        model.getObject().setPerson(owner);

        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(owner);
        if (choices == null || choices.isEmpty())
            choices = Arrays.asList(model.getObject().getResearchGroup());

        DropDownChoice<ResearchGroup> groups = new DropDownChoice<ResearchGroup>("researchGroup", choices, new ChoiceRenderer<ResearchGroup>(
                "title"));
        groups.setRequired(true);

        TextField<String> title = new TextField<String>("title");
        title.setLabel(ResourceUtils.getModel("label.scenarioTitle"));
        title.setRequired(true);

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

        SubmitLink submit = new SubmitLink("submit") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                FileUpload fileUpload = file.getFileUpload();
                FileUpload xmlfileUpload = xmlfile.getFileUpload();

                Scenario scenario = ScenarioForm.this.getModelObject();

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

//                    scenarioType.setScenarioXml(Hibernate.createBlob(fileUpload.getBytes()));
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
                        error(e.getMessage());
                        setResponsePage(getPage());
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
                        } catch (Exception e) {
                            error(e.getMessage());
                            setResponsePage(getPage());
                        }
                    }

                }

                if (!dataAvailable.getModelObject()) {
                }

                if (!scenariosFacade.canSaveTitle(scenario.getTitle(), scenario.getScenarioId())) {
                    error(ResourceUtils.getString("error.valueAlreadyInDatabase"));
                }

                if (scenario.getScenarioId() > 0) {
                    // Editing existing
                    // scenarioTypeDao.update(scenarioType);
                    scenariosFacade.update(scenario);
                } else {
                    // Creating new
                    scenariosFacade.create(scenario);
                }

                setResponsePage(getPage());
            }

        };

        fileContainer.add(file, xmlfile, schema, schemaList);

        add(groups, title, length, description, privateCheck, dataAvailable, submit, fileContainer);
    }
}
