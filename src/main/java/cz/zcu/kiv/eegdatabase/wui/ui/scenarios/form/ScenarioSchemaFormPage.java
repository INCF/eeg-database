package cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.Hibernate;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.logic.schemagen.ScenarioSchemaGenerator;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenariosPageLeftMenu;

/**
 * Page for add scenario schema.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ScenarioSchemaFormPage extends MenuPage {

    private static final long serialVersionUID = -7987971485930885797L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    ScenariosFacade scenarioFacade;

    @SpringBean
    SecurityFacade security;

    public ScenarioSchemaFormPage() {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        setPageTitle(ResourceUtils.getModel("pageTitle.addScenarioSchema"));

        add(new Label("title", ResourceUtils.getModel("pageTitle.addScenarioSchema")));

        ScenarioSchemaForm form = new ScenarioSchemaForm("form", new Model<ScenarioSchemas>(new ScenarioSchemas()), scenarioFacade, getFeedback());
        add(form);

        if (!security.userIsExperimenter() && !security.isAdmin()) {
            warn(ResourceUtils.getString("pageTitle.userNotExperimenter"));
            warn(ResourceUtils.getString("pageTitle.youNeedExperimenterRole"));
            form.setVisibilityAllowed(false);
        }
    }

    public ScenarioSchemaFormPage(PageParameters params) {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        setPageTitle(ResourceUtils.getModel("pageTitle.editScenarioSchema"));

        add(new Label("title", ResourceUtils.getModel("pageTitle.editScenarioSchema")));

        int schemaId = parseParameters(params);

        add(new ScenarioSchemaForm("form", new Model<ScenarioSchemas>(scenarioFacade.readScenarioSchema(schemaId)), scenarioFacade, getFeedback()));
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }

    private class ScenarioSchemaForm extends Form<ScenarioSchemas> {

        private static final long serialVersionUID = 1L;

        public ScenarioSchemaForm(String id, IModel<ScenarioSchemas> model, final ScenariosFacade scenarioFacade, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<ScenarioSchemas>(model));

            final boolean isEdit = (model.getObject().getSchemaId() > 0);

            TextArea<String> description = new TextArea<String>("description");
            description.setRequired(true);
            description.setLabel(ResourceUtils.getModel("label.scenarioSchemaDescription"));
            description.add(StringValidator.maximumLength(2000));

            final FileUploadField file = new FileUploadField("schemaFile", new ListModel<FileUpload>());
            file.setOutputMarkupPlaceholderTag(true);
            file.setLabel(ResourceUtils.getModel("label.xsdDataFile"));
            file.setRequired(!isEdit);

            SubmitLink submit = new SubmitLink("submit") {

                private static final long serialVersionUID = 1L;

                @Override
                public void onSubmit() {

                    ScenarioSchemas scenarioSchema = ScenarioSchemaForm.this.getModelObject();
                    FileUpload uploadedFile = file.getFileUpload();

                    // set scenario schema file
                    try {
                        if ((uploadedFile != null) && (uploadedFile.getSize() != 0)) {
                            log.debug("Setting the scenario schema file");
                            String schemaName = uploadedFile.getClientFileName();
                            scenarioSchema.setSchemaName(schemaName);
                            byte[] schemaContent = uploadedFile.getBytes();
                            // Clob schemaContent = Hibernate.createClob(schemaFile.getBytes().toString());

                            int newSchemaId = scenarioFacade.getNextSchemaId();
                            log.debug("new schema id " + newSchemaId);
                            ScenarioSchemaGenerator schemaGenerator = new ScenarioSchemaGenerator(newSchemaId, schemaName,
                                    schemaContent);
                            String sql = schemaGenerator.generateSql();
                            String hbmXml = schemaGenerator.generateHbmXml();
                            String pojo = schemaGenerator.generatePojo();
                            String bean = schemaGenerator.generateBean();

                            scenarioSchema.setSql(Hibernate.createClob(sql));
                            scenarioSchema.setHbmXml(Hibernate.createClob(hbmXml));
                            scenarioSchema.setPojo(Hibernate.createClob(pojo));
                            scenarioSchema.setBean(bean);
                            scenarioSchema.setApproved('n');
                        }
                    } catch (Exception e) {
                        log.error(e);
                        error("Error while parsing file!");
                        setResponsePage(getPage());
                    }

                    log.debug("Saving scenario schema object");

                    if (isEdit) {
                        // Editing existing
                        scenarioFacade.updateScenarioSchema(scenarioSchema);
                    } else {
                        // Creating new
                        info(scenarioSchema.getDescription());
                        info(uploadedFile.getClientFileName());
                        scenarioFacade.createScenarioSchema(scenarioSchema);
                    }
                    setResponsePage(getPage());
                }
            };

            add(submit, description, file);
        }
    }

}
