package cz.zcu.kiv.eegdatabase.logic.controller.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.AuthorizationManager;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioSchemasDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.logic.schemagen.ScenarioSchemaGenerator;
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
import java.io.InputStream;
import java.sql.Clob;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Toshiba
 * Date: 8.6.11
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class AddScenarioSchemaController extends SimpleFormController {

    private Log log = LogFactory.getLog(getClass());
    private AuthorizationManager auth;
    private ScenarioSchemasDao scenarioSchemasDao;

    public AddScenarioSchemaController() {
        setCommandClass(AddScenarioSchemaCommand.class);
        setCommandName("addScenarioSchema");
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        AddScenarioSchemaCommand data = (AddScenarioSchemaCommand) super.formBackingObject(request);

        String idString = request.getParameter("id");

        if (idString != null) {
            // Editing existing scenario schema
            int id = Integer.parseInt(idString);

            log.debug("Loading scenario schema to the command object for editing.");
            ScenarioSchemas schema = scenarioSchemasDao.read(id);

            data.setId(id);
            data.setSchemaDescription(schema.getDescription());
        }

        return data;
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) throws Exception {
        ModelAndView mav = super.showForm(request, response, errors);

        // Creating new scenario schema
        if (!auth.userIsExperimenter()) {
            mav.setViewName("scenario/userNotExperimenter");
        }
        mav.addObject("userIsExperimenter", auth.userIsExperimenter());

        return mav;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException bindException) throws Exception {
        ModelAndView mav = new ModelAndView(getSuccessView());

        log.debug("Processing form data");
        AddScenarioSchemaCommand data = (AddScenarioSchemaCommand) command;

        String idString = request.getParameter("id");
        int id = 0;
        if (idString != null) {
            id = Integer.parseInt(idString);
        }

        MultipartFile schemaFile = data.getSchemaFile();

        ScenarioSchemas scenarioSchema;

        if (id > 0) {
            // Editing existing
            log.debug("Editing existing scenario schema.");
            scenarioSchema = scenarioSchemasDao.read(id);

        } else {
            // Creating new
            log.debug("Creating new scenario schema object");
            scenarioSchema = new ScenarioSchemas();
        }

        log.debug("Setting scenario schema description: " + data.getSchemaDescription());
        scenarioSchema.setDescription(data.getSchemaDescription());

        //set scenario schema file
        if((schemaFile != null) && (!schemaFile.isEmpty())) {
            log.debug("Setting the scenario schema file");
            String schemaName = schemaFile.getOriginalFilename();
            scenarioSchema.setSchemaName(schemaName);
            byte[] schemaContent = schemaFile.getBytes();
            //Clob schemaContent = Hibernate.createClob(schemaFile.getBytes().toString());

            int newSchemaId = scenarioSchemasDao.getNextSchemaId();
            System.out.println(newSchemaId);
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

        log.debug("Saving scenario schema object");

        if (id > 0) {
            // Editing existing
            scenarioSchemasDao.update(scenarioSchema);
        } else {
            // Creating new
            scenarioSchemasDao.create(scenarioSchema);
        }

        log.debug("Returning MAV");
        return mav;
    }

    public ScenarioSchemasDao getScenarioSchemasDao() {
        return scenarioSchemasDao;
    }

    public void setScenarioSchemasDao(ScenarioSchemasDao scenarioSchemasDao) {
        this.scenarioSchemasDao = scenarioSchemasDao;
    }

    public AuthorizationManager getAuth() {
        return auth;
    }

    public void setAuth(AuthorizationManager auth) {
        this.auth = auth;
    }
}
