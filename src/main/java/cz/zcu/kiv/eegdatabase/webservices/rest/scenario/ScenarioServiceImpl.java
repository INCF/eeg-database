package cz.zcu.kiv.eegdatabase.webservices.rest.scenario;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestNotFoundException;
import cz.zcu.kiv.eegdatabase.webservices.rest.common.exception.RestServiceException;
import cz.zcu.kiv.eegdatabase.webservices.rest.scenario.wrappers.ScenarioData;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Scenario service implementation.
 *
 * @author Petr Miko
 */
@Service
public class ScenarioServiceImpl implements ScenarioService {

    @Autowired
    @Qualifier("scenarioDao")
    private ScenarioDao scenarioDao;
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao researchGroupDao;

    private final Comparator<ScenarioData> idComparator = new Comparator<ScenarioData>() {
        @Override
        public int compare(ScenarioData o1, ScenarioData o2) {
            return o1.getScenarioId() - o2.getScenarioId();
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ScenarioData> getAllScenarios() {
        List<Scenario> scenarios = scenarioDao.getAvailableScenarios(personDao.getLoggedPerson());
        List<ScenarioData> scenarioDatas = new ArrayList<ScenarioData>(scenarios.size());

        for (Scenario s : scenarios) {
            ScenarioData sd = new ScenarioData();
            Person owner = s.getPerson();
            sd.setScenarioId(s.getScenarioId());
            sd.setScenarioName(s.getTitle());
            sd.setOwnerName(owner.getGivenname() + " " + owner.getSurname());
            sd.setMimeType(s.getMimetype() != null ? s.getMimetype().trim() : "");
            sd.setFileName(s.getScenarioName());
            sd.setFileLength(s.getScenarioLength());
            sd.setDescription(s.getDescription());
            sd.setPrivate(s.isPrivateScenario());
            sd.setResearchGroupId(s.getResearchGroup().getResearchGroupId());
            sd.setResearchGroupName(s.getResearchGroup().getTitle());
            scenarioDatas.add(sd);
        }

        Collections.sort(scenarioDatas, idComparator);
        return scenarioDatas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ScenarioData> getMyScenarios() {
        List<Scenario> scenarios = scenarioDao.getScenariosWhereOwner(personDao.getLoggedPerson());
        List<ScenarioData> scenarioDatas = new ArrayList<ScenarioData>(scenarios.size());

        for (Scenario s : scenarios) {
            ScenarioData sd = new ScenarioData();
            Person owner = s.getPerson();
            sd.setScenarioId(s.getScenarioId());
            sd.setScenarioName(s.getTitle());
            sd.setOwnerName(owner.getGivenname() + " " + owner.getSurname());
            sd.setMimeType(s.getMimetype() != null ? s.getMimetype().trim() : "");
            sd.setFileName(s.getScenarioName());
            sd.setFileLength(s.getScenarioLength());
            sd.setDescription(s.getDescription());
            sd.setPrivate(s.isPrivateScenario());
            sd.setResearchGroupId(s.getResearchGroup().getResearchGroupId());
            sd.setResearchGroupName(s.getResearchGroup().getTitle());
            scenarioDatas.add(sd);
        }
        Collections.sort(scenarioDatas, idComparator);
        return scenarioDatas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int create(ScenarioData scenarioData, MultipartFile file) throws IOException, SAXException, ParserConfigurationException {
        Scenario scenario = new Scenario();
        scenario.setDescription(scenarioData.getDescription());
        scenario.setTitle(scenarioData.getScenarioName());
        scenario.setScenarioName(file.getOriginalFilename().replace(" ", "_"));
        scenario.setMimetype(scenarioData.getMimeType().toLowerCase().trim());

        //DB column size restriction
        if(scenario.getMimetype().length() > 30){
            scenario.setMimetype("application/octet-stream");
        }

        ResearchGroup group =  researchGroupDao.read(scenarioData.getResearchGroupId());
        scenario.setResearchGroup(group);
        Person owner = personDao.getLoggedPerson();
        scenario.setPerson(owner);

        scenario.setScenarioLength((int) file.getSize());
        IScenarioType scenarioType;

        if(scenarioData.getMimeType() != null && "application/xml".equals(scenario.getMimetype())) {
             //XML file type
            scenarioType = new ScenarioTypeNonSchema();

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            InputStream inputStream = file.getInputStream();
            Document doc = docBuilder.parse(inputStream);
            scenarioType.setScenarioXml(doc);
        }   else{
            //NonXML file type
            scenarioType = new ScenarioTypeNonXml();
            scenarioType.setScenarioXml(Hibernate.createBlob(file.getBytes()));
        }

        scenario.setScenarioType(scenarioType);
        scenarioType.setScenario(scenario);
        scenario.setPrivateScenario(scenarioData.isPrivate());
        return scenarioDao.create(scenario);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, TransformerException, RestNotFoundException {
        Scenario scenario = scenarioDao.read(id);
        IScenarioType scenarioType;

        if(scenario == null || (scenarioType = scenario.getScenarioType()) == null) throw new RestNotFoundException("No file with such id! ");

        //if is user member of group, then he has rights to download file
        //basic verification, in future should be extended
        ResearchGroup expGroup = scenario.getResearchGroup();
        if (!isInGroup(personDao.getLoggedPerson(), expGroup.getResearchGroupId())) throw new RestServiceException("User does not have access to this file!");

        if(scenarioType.getScenarioXml() == null) throw new RestNotFoundException("No present scenario file!");

        if(scenarioType.getScenarioXml() instanceof Blob){
            ScenarioType<Blob> file = (ScenarioType<Blob>) scenarioType;
            InputStream is = file.getScenarioXml().getBinaryStream();
            // copy it to response's OutputStream
            response.setContentType(scenario.getMimetype() != null ? scenario.getMimetype().trim() : "application/octet-stream");
            response.setContentLength((int) file.getScenarioXml().length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + scenario.getScenarioName() + "\"");
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        }
        else if (scenarioType.getScenarioXml() instanceof Document){
            File tmpXml = File.createTempFile("scenario", "download");
            ScenarioType<Document> doc = (ScenarioType<Document>) scenarioType;
            Source source = new DOMSource(doc.getScenarioXml());
            Result result = new StreamResult(tmpXml);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);

            InputStream inStream = null;

            try {
                response.setContentLength((int) tmpXml.length());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + scenario.getScenarioName() + "\"");
                inStream = new FileInputStream(tmpXml);
                IOUtils.copy(inStream, response.getOutputStream());
            } finally {
                if(inStream != null)
                    inStream.close();
                response.flushBuffer();
                tmpXml.delete();
            }
        }
        else throw new RestServiceException("Unsupported file type!");
    }

    /**
     * Checks if is user a member of specified research group.
     *
     * @param user            user
     * @param researchGroupId research group identifier
     * @return true if is user in group
     */
    private boolean isInGroup(Person user, int researchGroupId) {
        if (!user.getResearchGroupMemberships().isEmpty()) {
            for (ResearchGroup g : researchGroupDao.getResearchGroupsWhereMember(user)) {
                if (g.getResearchGroupId() == researchGroupId) {
                    return true;
                }
            }
        }
        return false;
    }
}
