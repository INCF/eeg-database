/**
 * *****************************************************************************
 * This file is part of the EEG-database project
 *
 * ==========================================
 *
 * Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *
 *  ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 *  ***********************************************************************************************************************
 *
 * ScenarioForm.java, 2013/28/11 00:01 Jakub Rinkes
 *****************************************************************************
 */
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.utils.PageParametersUtils;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenarioDetailPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.RangeValidator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ScenarioForm extends Form<Scenario> {

    private static final long serialVersionUID = 1L;
    protected Log log = LogFactory.getLog(getClass());

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
        if (owner == null) {
            owner = EEGDataBaseSession.get().getLoggedUser();
        }

        model.getObject().setPerson(owner);

        List<ResearchGroup> choices = researchGroupFacade.getResearchGroupsWhereAbleToWriteInto(owner);
        if (choices == null || choices.isEmpty()) {
            choices = Arrays.asList(model.getObject().getResearchGroup());
        }

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

                FileUpload uploadedFile = file.getFileUpload();
                Scenario scenario = ScenarioForm.this.getModelObject();

                // loading non-XML
                if ((uploadedFile != null) && (!(uploadedFile.getSize() == 0))) {

                    String name = uploadedFile.getClientFileName();
                    // when uploading from localhost some browsers will specify the entire path, we strip it
                    // down to just the file name
                    name = Strings.lastPathComponent(name, '/');
                    name = Strings.lastPathComponent(name, '\\');

                    // File uploaded
                    String filename = name.replace(" ", "_");
                    scenario.setScenarioName(filename);

                    scenario.setMimetype(uploadedFile.getContentType());
                    try {
                        scenario.setFileContentStream(uploadedFile.getInputStream());
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }

                if (!scenariosFacade.canSaveTitle(scenario.getTitle(), scenario.getScenarioId())) {
                    error(ResourceUtils.getString("error.titleAlreadyInDatabase"));
                    return;
                }

                if (scenario.getScenarioId() > 0) {
                    // Editing existing
                    // scenarioTypeDao.update(scenarioType);
                    scenariosFacade.update(scenario);
                } else {
                    // Creating new
                    scenariosFacade.create(scenario);
                }

                /*
                 * clean up after upload file, and set model to null or will be problem with page serialization when redirect start - DON'T DELETE IT !
                 */
                ScenarioForm.this.setModelObject(null);

                setResponsePage(ScenarioDetailPage.class, PageParametersUtils.getDefaultPageParameters(scenario.getScenarioId()));
            }
        };

        fileContainer.add(file, xmlfile, schema, schemaList);

        add(groups, title, length, description, privateCheck, dataAvailable, submit, fileContainer);
    }
}
