package test.data.dao;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioDao;
import cz.zcu.kiv.eegdatabase.data.dao.ScenarioTypeDao;
import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import test.data.AbstractDataAccessTest;
import oracle.xdb.XMLType;
import org.hibernate.Hibernate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.Blob;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 10.6.11
 * Time: 16:00
 * To change this template use File | Settings | File Templates.
 */
public class SimpleScenarioTypeDaoTest extends AbstractDataAccessTest {

    //@Autowired
    //private Scenario scenario;
    @Autowired
    private ScenarioTypeNonXml scenarioTypeNonXml = null;
    @Autowired
    private ScenarioTypeNonSchema scenarioNonSchema = null;
    @Autowired
    private ScenarioTypeSchema1 scenarioTypeSchema1 = null;
    @Autowired
    private ScenarioTypeSchema2 scenarioTypeSchema2 = null;
    @Autowired
    private ScenarioTypeSchema3 scenarioTypeSchema3 = null;

    //autowiring String datatype fails in Spring 2.5.x
    //@Resource
    //private String nonXmlContent = null;

    private PersonDao personDao;

    private ScenarioDao scenarioDao;
    private ScenarioTypeDao scenarioTypeDao;
    Scenario scenario;

    String path1 = "src/java/cz/zcu/kiv/eegdatabase/test/data/dao/files/noxml.txt";
    String path2 = "src/java/cz/zcu/kiv/eegdatabase/test/data/dao/files/subjects.xml";
    String path3 = "src/java/cz/zcu/kiv/eegdatabase/test/data/dao/files/p300.xml";
    String path4 = "src/java/cz/zcu/kiv/eegdatabase/test/data/dao/files/scenarios.xml";

    public void setup() throws Exception {
        Person person = personDao.getPerson("pitrs");
        ResearchGroup group = new ResearchGroup();
        group.setResearchGroupId(6);
        scenario = new Scenario();
        scenario.setResearchGroup(group);
        scenario.setDescription("description test");
        scenario.setMimetype("test");
        scenario.setPerson(person);
        scenario.setResearchGroup(group);
        scenario.setScenarioName("scenario name test");
        scenario.setScenarioLength(10);
        scenarioDao.create(scenario);
    }


    @Test
    public void testGetScenarioType() throws Exception{
        String query = "select scenario_xml from scenario_type_nonschema s where scenario_id = (\n" +
                "       select scenario_id from scenario_type_parent p where p.scenario_id = 148)";
        XMLType xml = (XMLType) this.jdbcTemplate.queryForObject(query, XMLType.class);
        assertNotNull(xml);
        Document xmlNext = (Document)scenarioTypeDao.read(148).getScenarioXml();
        assertNotNull(xmlNext);
        }

    @Test
    public void testCreateScenarioTypeNonXml() throws Exception {

        setup();

        ScenarioType<Blob> scenarioType = scenarioTypeNonXml;
        InputStream stream = new FileInputStream(new File(path2));
        scenarioType.setScenarioXml(Hibernate.createBlob(stream));
        scenarioType.setScenario(scenario);
        scenarioTypeDao.create(scenarioType);
        scenarioTypeDao.delete(scenarioType);
    }

    @Test
    public void testCreateScenarioTypeSchema1() throws Exception {

        setup();

        ScenarioType<Document> scenarioType = scenarioTypeSchema1;
        InputStream stream = new FileInputStream(new File(path2));
        scenarioType.setScenarioXml(stringToDocument(stream));
        scenarioTypeDao.create(scenarioType);
        scenarioTypeDao.delete(scenarioType);
    }

    @Test
    public void testCreateScenarioTypeSchema2() throws Exception {

        setup();

        ScenarioType<Document> scenarioType = scenarioTypeSchema2;
        InputStream stream = new FileInputStream(new File(path3));
        scenarioType.setScenarioXml(stringToDocument(stream));
        scenarioTypeDao.create(scenarioType);
        scenarioTypeDao.delete(scenarioType);
    }

    public void setScenarioTypeDao(ScenarioTypeDao scenarioTypeDao) {
        this.scenarioTypeDao = scenarioTypeDao;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public void setScenarioTypeNonXml(ScenarioTypeNonXml scenarioTypeNonXml) {
        this.scenarioTypeNonXml = scenarioTypeNonXml;
    }

    public void setScenarioNonSchema(ScenarioTypeNonSchema scenarioNonSchema) {
        this.scenarioNonSchema = scenarioNonSchema;
    }

    public void setScenarioTypeSchema1(ScenarioTypeSchema1 scenarioTypeSchema1) {
        this.scenarioTypeSchema1 = scenarioTypeSchema1;
    }

    public void setScenarioTypeSchema2(ScenarioTypeSchema2 scenarioTypeSchema2) {
        scenarioTypeSchema2 = scenarioTypeSchema2;
    }

    public void setScenarioTypeSchema3(ScenarioTypeSchema3 scenarioTypeSchema3) {
        scenarioTypeSchema3 = scenarioTypeSchema3;
    }

    public void setPersonDao(PersonDao personDao) {
        this.personDao = personDao;
    }

    public void setScenarioDao(ScenarioDao scenarioDao) {
        this.scenarioDao = scenarioDao;
    }

    public Document stringToDocument(InputStream xmlString) throws SAXException, ParserConfigurationException, IOException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlString);
        return document;
    }
}
