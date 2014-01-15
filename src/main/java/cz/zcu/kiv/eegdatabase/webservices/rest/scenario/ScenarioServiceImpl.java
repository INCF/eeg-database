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
 *   ScenarioServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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

        scenario.setPrivateScenario(scenarioData.isPrivate());
        return scenarioDao.create(scenario);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void getFile(int id, HttpServletResponse response) throws RestServiceException, SQLException, IOException, TransformerException, RestNotFoundException {
        Scenario scenario = scenarioDao.read(id);

        //if is user member of group, then he has rights to download file
        //basic verification, in future should be extended
        ResearchGroup expGroup = scenario.getResearchGroup();
        if (!isInGroup(personDao.getLoggedPerson(), expGroup.getResearchGroupId())) throw new RestServiceException("User does not have access to this file!");

        
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
