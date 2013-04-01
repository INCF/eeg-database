package cz.zcu.kiv.eegdatabase.wui.ui.scenarios.form;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
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
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.validation.validator.RangeValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.hibernate.Hibernate;
import org.w3c.dom.Document;

import cz.zcu.kiv.eegdatabase.data.pojo.IScenarioType;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeNonSchema;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeNonXml;
import cz.zcu.kiv.eegdatabase.wui.app.EEGDataBaseApplication;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.menu.button.ButtonPageMenu;
import cz.zcu.kiv.eegdatabase.wui.components.page.BasePage;
import cz.zcu.kiv.eegdatabase.wui.components.page.MenuPage;
import cz.zcu.kiv.eegdatabase.wui.components.utils.ResourceUtils;
import cz.zcu.kiv.eegdatabase.wui.core.CoreConstants;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenarioXMLProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.type.ScenarioTypeFacade;
import cz.zcu.kiv.eegdatabase.wui.core.security.SecurityFacade;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ListScenariosPage;
import cz.zcu.kiv.eegdatabase.wui.ui.scenarios.ScenariosPageLeftMenu;

/**
 * Page for add / edit action on scenario.
 * 
 * @author Jakub Rinkes
 *
 */
@AuthorizeInstantiation(value = { "ROLE_READER", "ROLE_USER", "ROLE_EXPERIMENTER", "ROLE_ADMIN" })
public class ScenarioFormPage extends MenuPage {

    private static final long serialVersionUID = -7987971485930885797L;

    protected Log log = LogFactory.getLog(getClass());

    @SpringBean
    ScenariosFacade scenarioFacade;

    @SpringBean
    ScenarioTypeFacade scenarioTypeFacade;

    @SpringBean
    ScenarioXMLProvider xmlProvider;

    @SpringBean
    SecurityFacade security;

    @SpringBean
    ResearchGroupFacade groupFacade;

    public ScenarioFormPage() {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        setPageTitle(ResourceUtils.getModel("pageTitle.addScenarioSchema"));

        add(new Label("title", ResourceUtils.getModel("pageTitle.addScenarioSchema")));

        ScenarioForm scenarioForm = new ScenarioForm("form", new Model<Scenario>(new Scenario()), scenarioFacade, getFeedback());
        add(scenarioForm);

        if (!security.userIsExperimenter() && !security.isAdmin()) {
            warn(ResourceUtils.getString("pageTitle.userNotExperimenter"));
            warn(ResourceUtils.getString("pageTitle.youNeedExperimenterRole"));
            scenarioForm.setVisibilityAllowed(false);
        }
    }

    public ScenarioFormPage(PageParameters params) {

        add(new ButtonPageMenu("leftMenu", ScenariosPageLeftMenu.values()));

        setPageTitle(ResourceUtils.getModel("pageTitle.editScenarioSchema"));

        add(new Label("title", ResourceUtils.getModel("pageTitle.editScenarioSchema")));

        int scenarioId = parseParameters(params);

        if (!security.userIsOwnerOfScenario(scenarioId) && !security.isAdmin())
            throw new RestartResponseAtInterceptPageException(ListScenariosPage.class);

        add(new ScenarioForm("form", new Model<Scenario>(scenarioFacade.read(scenarioId)), scenarioFacade, getFeedback()));
    }

    private int parseParameters(PageParameters parameters) {

        StringValue value = parameters.get(BasePage.DEFAULT_PARAM_ID);
        if (value.isNull() || value.isEmpty())
            throw new RestartResponseAtInterceptPageException(EEGDataBaseApplication.get().getHomePage());
        return value.toInt();
    }
    
    // inner form for add / edit action of scenario
    private class ScenarioForm extends Form<Scenario> {

        private static final long serialVersionUID = 1L;

        public ScenarioForm(String id, IModel<Scenario> model, final ScenariosFacade scenariosFacade, final FeedbackPanel feedback) {
            super(id, new CompoundPropertyModel<Scenario>(model));

            setMultiPart(true);

            Person owner = model.getObject().getPerson();
            if (owner == null)
                owner = EEGDataBaseSession.get().getLoggedUser();

            model.getObject().setPerson(owner);

            List<ResearchGroup> choices = groupFacade.getResearchGroupsWhereAbleToWriteInto(owner);
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
             * TODO file field for xml was not visible in old portal. I dont know why. 
             * So I hided it but its implemented and not tested.
             */
            // hidded line in old portal
            final DropDownChoice<ScenarioSchemas> schemaList = new DropDownChoice<ScenarioSchemas>("schemaList", new Model<ScenarioSchemas>(), scenarioFacade.getListOfScenarioSchemas(),
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
                    IScenarioType scenarioType = null;

                    // loading non-XML
                    if ((fileUpload != null) && (!(fileUpload.getSize() == 0))) {
                        // File uploaded
                        log.debug("Setting the data file");
                        String filename = fileUpload.getClientFileName().replace(" ", "_");
                        scenario.setScenarioName(filename);
                        if (fileUpload.getContentType().length() > CoreConstants.MAX_MIMETYPE_LENGTH) {
                            int index = filename.lastIndexOf(".");
                            scenario.setMimetype(filename.substring(index));
                        } else {
                            scenario.setMimetype(fileUpload.getContentType());
                        }

                        scenarioType = new ScenarioTypeNonXml();
                        scenarioType.setScenarioXml(Hibernate.createBlob(fileUpload.getBytes()));
                    }

                    // loading XML
                    if ((xmlfileUpload != null) && (!(xmlfileUpload.getSize() == 0))) {
                        // load the XML file to a table with the XMLType column
                        log.debug("Setting the XML data file");
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
                            log.error(e.getMessage(), e);
                            error(e.getMessage());
                            setResponsePage(getPage());
                        }

                        // getting the right scenarioType bean
                        // no schema - binary storage
                        Boolean isSchemaSelected = schema.getModelObject();
                        if (!isSchemaSelected) {
                            scenarioType = new ScenarioTypeNonSchema();
                        }
                        // schema selected - structured storage
                        else {
                            Class c;
                            try {
                                c = Class.forName("cz.zcu.kiv.eegdatabase.data.pojo.ScenarioTypeSchema" + schemaList.getModelObject().getSchemaId());
                                scenarioType = (ScenarioType) c.newInstance();
                            } catch (Exception e) {
                                error(e.getMessage());
                                log.error(e.getMessage(), e);
                                setResponsePage(getPage());
                            }
                        }
                        
                        if (scenario.getScenarioId() > 0) {
                            scenarioType = scenario.getScenarioType();
                        }

                        scenarioType.setScenarioXml(doc);
                    }

                    log.debug("Saving scenario object");
                    if (!dataAvailable.getModelObject()) {
                        scenarioType = new ScenarioTypeNonXml();
                    }
                    scenario.setScenarioType(scenarioType);
                    scenarioType.setScenario(scenario);
                    
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
}
