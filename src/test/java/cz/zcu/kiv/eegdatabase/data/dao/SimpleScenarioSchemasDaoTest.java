package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioSchemas;
import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 9.6.11
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class SimpleScenarioSchemasDaoTest extends AbstractDataAccessTest {

    @Autowired
    private ScenarioSchemasDao scenarioSchemasDao;

    public static final String SCHEMA_NAME = "schema test";
    public static final String SCHEMA_DESCRIPTION = "description test";
    public static final char SCHEMA_APPROVED = 'n';

    private ScenarioSchemas setTestScenarioSchema() {
        ScenarioSchemas schema = new ScenarioSchemas();
        schema.setSchemaName(SCHEMA_NAME);
        schema.setDescription(SCHEMA_DESCRIPTION);
        schema.setApproved(SCHEMA_APPROVED);

        return schema;
    }

    @Test
    public void testCreateScenarioSchema() {

        ScenarioSchemas schema = setTestScenarioSchema();
        scenarioSchemasDao.create(schema);
        assertNotNull("Primary key assigned", schema.getSchemaId());
        assertEquals(SCHEMA_NAME, schema.getSchemaName());

        scenarioSchemasDao.delete(schema);
    }

    @Test
    public void testUpdateScenarioSchema() {

        ScenarioSchemas schema = setTestScenarioSchema();
        scenarioSchemasDao.create(schema);
        schema.setDescription("description test update");
        scenarioSchemasDao.update(schema);

        assertEquals("description test update", schema.getDescription());

        scenarioSchemasDao.delete(schema);
    }

    @Test
    public void testDeleteScenarioSchema() {
        ScenarioSchemas schema = setTestScenarioSchema();
        scenarioSchemasDao.create(schema);

        scenarioSchemasDao.delete(schema);
        schema = scenarioSchemasDao.read(schema.getSchemaId());
        assertNull(schema);
    }

    @Test
    public void testGetNonexistentScenarioSchema() {

        ScenarioSchemas schema = scenarioSchemasDao.read(1);
        assertEquals("scenarios.xsd", schema.getSchemaName());
        schema = scenarioSchemasDao.read(0);
        assertNull(schema);

    }

    @Test
    public void testGetScenarioSchemaNames() {

        ScenarioSchemas schema = setTestScenarioSchema();
        scenarioSchemasDao.create(schema);
        List<ScenarioSchemas> schemasList = scenarioSchemasDao.getSchemaNames();

        assertEquals(3,schemasList.size());

        scenarioSchemasDao.delete(schema);
    }

    @Test
    public void testGetSchemaDescriptions() {

        List<ScenarioSchemas> schemasList = scenarioSchemasDao.getSchemaDescriptions();

        assertEquals("Information about tests and persons who took the tests.",
                schemasList.get(2).getDescription());
    }

    @Test
    public void testGetNextSchemaId() {
        int id = scenarioSchemasDao.getNextSchemaId();
        assertEquals(5, id);
    }

    // Spring will automatically inject the ScenarioSchemasDao
    public void setScenarioSchemasDao(ScenarioSchemasDao scenarioSchemasDao) {
        this.scenarioSchemasDao = scenarioSchemasDao;
    }

}
