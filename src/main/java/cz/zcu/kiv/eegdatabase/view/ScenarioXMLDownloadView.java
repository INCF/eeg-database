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
 *   ScenarioXMLDownloadView.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.view;

import java.sql.Blob;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.view.AbstractView;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;

/**
 * This view serves for downloading the data files.
 *
 * @author Jindra
 */
public class ScenarioXMLDownloadView extends AbstractView {
    Log log = LogFactory.getLog(getClass());

    @Override
    protected void renderMergedOutputModel(Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Processing view for scenario XML output");

        Scenario scenario = (Scenario) map.get("dataObject");
        
        log.debug("Loading Scenario object - ID " + scenario.getScenarioId());

        log.debug("Setting MIME to XML");
        response.setHeader("Content-Type", "text/xml");

        log.debug("Setting filename");
        response.setHeader("Content-Disposition", "attachment;filename=scenario-" + scenario.getScenarioId());

        log.debug("End of the view");
    }

}
