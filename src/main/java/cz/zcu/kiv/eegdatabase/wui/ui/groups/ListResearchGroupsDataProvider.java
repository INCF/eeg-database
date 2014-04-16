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
 *   ListResearchGroupsDataProvider.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.groups;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;

import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.wui.components.repeater.BasicDataProvider;
import cz.zcu.kiv.eegdatabase.wui.core.group.ResearchGroupFacade;

/**
 * Dataprovider implementation used in table on ListResearchGroupPage.
 * 
 * @author Jakub Rinkes
 * 
 */
public class ListResearchGroupsDataProvider extends BasicDataProvider<ResearchGroup> {

    private static final long serialVersionUID = -2474056753967062150L;

    protected Log log = LogFactory.getLog(getClass());

    public ListResearchGroupsDataProvider(ResearchGroupFacade facade) {
        super("title", SortOrder.ASCENDING);

        List<ResearchGroup> list;
        int size;

        size = facade.getCountForList();
        list = facade.getGroupsForList(0, size);

        super.listModel.setObject(list);
    }

}
