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
 *   ScenarioXMLProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.scenarios;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.core.file.FileDTO;
import cz.zcu.kiv.eegdatabase.wui.core.history.HistoryFacade;
import cz.zcu.kiv.eegdatabase.wui.core.person.PersonFacade;

/**
 * Provider for prepare downloaded file for scenario.
 * 
 * @author Jakub Rinkes
 *
 */
public class ScenarioXMLProvider {

    protected Log log = LogFactory.getLog(getClass());

    ScenariosFacade scenarioFacade;
    HistoryFacade historyFacade;
    PersonFacade personFacade;

    @Required
    public void setScenarioFacade(ScenariosFacade scenarioFacade) {
        this.scenarioFacade = scenarioFacade;
    }

    @Required
    public void setHistoryFacade(HistoryFacade historyFacade) {
        this.historyFacade = historyFacade;
    }

    @Required
    public void setPersonFacade(PersonFacade personFacade) {
        this.personFacade = personFacade;
    }
    
    /**
     * Prepare scenario file for download.
     * 
     * @param scenarioId
     * @param loggedUserName
     * @return
     */
    @Transactional
    public FileDTO getXmlFileForScenario(int scenarioId, String loggedUserName) {

        try {
            
            FileDTO file = new FileDTO();
            Scenario scenario = scenarioFacade.read(scenarioId);

            Person user = personFacade.getPerson(loggedUserName);
            Timestamp currentTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
            History history = new History();
            log.debug("Setting downloading scenario");
            history.setScenario(scenario);
            log.debug("Setting user");
            history.setPerson(user);
            log.debug("Setting time of download");
            history.setDateOfDownload(currentTimestamp);
            log.debug("Saving download history");
            historyFacade.create(history);
            return file;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
