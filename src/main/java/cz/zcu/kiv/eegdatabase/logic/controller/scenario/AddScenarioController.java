package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.*;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Controller which processes form for adding a measuration
 *
 * @author Jindra
 */
public class AddScenarioController
        extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    private ScenarioDao scenarioDao;
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private ScenarioSchemasDao scenarioSchemasDao;
    private ScenarioTypeDao scenarioTypeDao;

    public AddScenarioController() {
        setCommandClass(AddScenarioCommand.class);
        setCommandName("addScenario");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddScenarioCommand data = (AddScenarioCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");

        if (idString != null) {
            // Editing existing scenario
            int id = Integer.parseInt(idString);

            log.debug("Loading scenario to the command object for editing.");
            Scenario scenario = scenarioDao.read(id);

            data.setId(id);
            data.setTitle(scenario.getTitle());
            data.setLength(new Integer(scenario.getScenarioLength()).toString());
            data.setDescription(scenario.getDescription());
            data.setPrivateNote(scenario.isPrivateScenario());
            data.setResearchGroup(scenario.getResearchGroup().getResearchGroupId());
        }

        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        String idString = request.getParameter("id");
        if (idString != null) {
            if (!auth.isAdmin()) {
                int id = Integer.parseInt(idString);

                if ((id > 0) && (!auth.userIsOwnerOfScenario(id))) {
                    // Editing existing scenario and user is not owner
                    mav.setViewName("redirect:/scenarios/list.html");
                    return mav;
                }
            }
        }

        // Creating new scenario
        if (!auth.userIsExperimenter()) {
            mav.setViewName("scenario/userNotExperimenter");
        }
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        return mav;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap<String, Object>();
        String idString = request.getParameter("id");
        Person owner = null;
        Scenario scen = null;
        if (idString != null) {
            scen = scenarioDao.read(Integer.parseInt(idString));
            owner = scen.getPerson();
        }
        if (owner == null) {
            owner = personDao.getLoggedPerson();
        }
        List<ResearchGroup> groups = researchGroupDao.getResearchGroupsWhereAbleToWriteInto(owner);
        map.put("researchGroupList", groups);

        List<ScenarioSchemas> schemaNames = scenarioSchemasDao.getSchemaNames();
        map.put("schemaNamesList", schemaNames);

        List<ScenarioSchemas> schemaDescriptions = scenarioSchemasDao.getSchemaDescriptions();
        map.put("schemaDescriptionsList", schemaDescriptions);

        ResearchGroup defaultGroup = personDao.getLoggedPerson().getDefaultGroup();
        if (scen != null) {
            defaultGroup = scen.getResearchGroup();
        }
        int defaultGroupId = (defaultGroup != null) ? defaultGroup.getResearchGroupId() : 0;
        map.put("defaultGroupId", defaultGroupId);

        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data");
        AddScenarioCommand data = (AddScenarioCommand) command;

        String idString = request.getParameter("id");
        int id = 0;
        if (idString != null) {
            id = Integer.parseInt(idString);
        }
        if (!auth.isAdmin()) {
            if ((id > 0) && (!auth.userIsOwnerOfScenario(id))) {
                // Editing existing scenario and user is not owner
                mav.setViewName("redirect:/scenarios/list.html");
                return mav;
            }

            if (!auth.userIsExperimenter()) {
                mav.setViewName("scenario/userNotExperimenter");
                return mav;
            }
        }

        MultipartFile file = data.getDataFile();
        MultipartFile xmlFile = data.getDataFileXml();

        Scenario scenario;
        ScenarioType scenarioType = null;

        if (id > 0) {
            // Editing existing
            log.debug("Editing existing scenario.");
            scenario = scenarioDao.read(id);

        } else {
            // Creating new
            log.debug("Creating new scenario object");
            scenario = new Scenario();

            log.debug("Setting owner to the logged user.");
            scenario.setPerson(personDao.getLoggedPerson());

        }

        log.debug("Setting research group.");
        ResearchGroup group = new ResearchGroup();
        group.setResearchGroupId(data.getResearchGroup());
        scenario.setResearchGroup(group);

        log.debug("Setting scenario title: " + data.getTitle());
        scenario.setTitle(data.getTitle());

        log.debug("Setting scenario description: " + data.getDescription());
        scenario.setDescription(data.getDescription());

        log.debug("Setting scenario length: " + data.getLength());
        scenario.setScenarioLength(Integer.parseInt(data.getLength()));

        ApplicationContext context = getApplicationContext();

        //loading non-XML
        if ((file != null) && (!file.isEmpty())) {
            // File uploaded
            log.debug("Setting the data file");
            scenario.setScenarioName(file.getOriginalFilename());

            scenario.setMimetype(file.getContentType());

           // scenarioType = (ScenarioType) context.getBean("scenarioTypeNonXml");
            scenarioType = new ScenarioTypeNonXml();
            scenarioType.setScenarioXml(Hibernate.createBlob(file.getBytes()));
        }

        //loading XML
        if ((xmlFile != null) && (!xmlFile.isEmpty())) {
            //load the XML file to a table with the XMLType column
            log.debug("Setting the XML data file");
            scenario.setScenarioName(xmlFile.getOriginalFilename());

            scenario.setMimetype(xmlFile.getContentType());

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            InputStream inputStream = xmlFile.getInputStream();
            Document doc = docBuilder.parse(inputStream);

            int schemaId = data.getScenarioSchema();
            String schemaNeeded = data.getScenarioOption();

            //getting the right scenarioType bean
            //no schema - binary storage
            if (schemaNeeded.equals("noSchema")) {
                scenarioType = new ScenarioTypeNonSchema();
            }
            //schema selected - structured storage
            else {
                if (schemaId > 0) {
                    Class c = Class.forName("ScenarioTypeSchema"+schemaId);
                    scenarioType = (ScenarioType)  c.newInstance();
                }
            }

            scenarioType.setScenarioXml(doc);
        }

        log.debug("Setting private/public access");
        scenario.setPrivateScenario(data.getPrivateNote());

        log.debug("Saving scenario object");

        if (id > 0) {
            // Editing existing
            scenarioDao.update(scenario);
        } else {
            // Creating new

            scenario.setScenarioType(scenarioType);
            scenarioType.setScenario(scenario);
            scenarioDao.create(scenario);
        }

        log.debug("Returning MAV");
        return mav;
    }


    public ScenarioDao getScenarioDao() {
        return scenarioDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public ResearchGroupDao getResearchGroupDao() {
        return researchGroupDao;
    }

    public void setResearchGroupDao(ResearchGroupDao researchGroupDao) {
        this.researchGroupDao = researchGroupDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }

    public ScenarioSchemasDao getScenarioSchemasDao() {
        return scenarioSchemasDao;
    }

    public void setScenarioSchemasDao(ScenarioSchemasDao scenarioSchemasDao) {
        this.scenarioSchemasDao = scenarioSchemasDao;
    }

    public ScenarioTypeDao getScenarioTypeDao() {
        return scenarioTypeDao;
    }

    public void setScenarioTypeDao(ScenarioTypeDao scenarioTypeDao) {
        this.scenarioTypeDao = scenarioTypeDao;
    }
}
