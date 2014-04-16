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
 *   ListScenariosDataProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.scenarios;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import cz.zcu.kiv.eegdatabase.data.pojo.Person;
import cz.zcu.kiv.eegdatabase.data.pojo.Scenario;
import cz.zcu.kiv.eegdatabase.wui.app.session.EEGDataBaseSession;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.scenarios.ScenariosFacade;

/**
 * Dataprovider implementation used in table on ListScenarioPage.
 * 
 * @author Jakub Rinkes
 *
 */
public class ListScenariosDataProvider extends BasicDataProvider<Scenario> {

    private static final long serialVersionUID = 3555979400074686801L;

    protected Log log = LogFactory.getLog(getClass());

    ScenariosFacade facade;


    public ListScenariosDataProvider(ScenariosFacade facade) {
        super("scenarioId", SortOrder.ASCENDING);

        List<Scenario> list;
        int size;
        
        Person loggedUser = EEGDataBaseSession.get().getLoggedUser();
        size = facade.getScenarioCountForList(loggedUser);
        list = facade.getScenariosForList(loggedUser, 0, size);
        
        super.listModel.setObject(list);
    }

}
