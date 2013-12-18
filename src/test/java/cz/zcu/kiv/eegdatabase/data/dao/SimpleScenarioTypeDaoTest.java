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
 *   SimpleScenarioTypeDaoTest.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.*;
import cz.zcu.kiv.eegdatabase.data.pojo.ScenarioType;
import cz.zcu.kiv.eegdatabase.data.AbstractDataAccessTest;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.Blob;

import static org.junit.Assert.*;

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
    private JdbcTemplate jdbcTemplate;

    //autowiring String datatype fails in Spring 2.5.x
    //@Resource
    //private String nonXmlContent = null;

    @Autowired
    private PersonDao personDao;
    @Autowired
    private ScenarioDao scenarioDao;
    @Autowired
    private ScenarioTypeDao scenarioTypeDao;

    Scenario scenario;

    String path1 = "src/test/resources/cz/zcu/kiv/eegdatabase/data/dao/files/noxml.txt";
    String path2 = "src/test/resources/cz/zcu/kiv/eegdatabase/data/dao/files/subjects.xml";
    String path3 = "src/test/resources/cz/zcu/kiv/eegdatabase/data/dao/files/p300.xml";
    String path4 = "src/test/resources/cz/zcu/kiv/eegdatabase/data/dao/files/scenarios.xml";

    @Before
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
        //scenarioDao.create(scenario);
    }
		

    @Test
    @Transactional
    public void testCreateScenarioTypeNonXml() throws Exception {
        ScenarioType<Blob> scenarioType = scenarioTypeNonXml;
        scenarioType.setScenario(scenario);
        scenarioTypeDao.create(scenarioType);
        scenarioDao.create(scenario);
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

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
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
