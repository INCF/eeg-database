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
 *   ResearchGroupServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.webservices.rest.groups;

import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.webservices.rest.groups.wrappers.ResearchGroupData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Research group service implementation.
 *
 * @author Petr Miko
 */
@Service
public class ResearchGroupServiceImpl implements ResearchGroupService {

    private final Comparator<ResearchGroupData> nameComparator = new Comparator<ResearchGroupData>() {

        @Override
        public int compare(ResearchGroupData o1, ResearchGroupData o2) {
            return o1.getGroupName().compareTo(o2.getGroupName());
        }
    };
    @Autowired
    @Qualifier("personDao")
    private PersonDao personDao;
    @Autowired
    @Qualifier("researchGroupDao")
    private ResearchGroupDao groupDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupData> getAllGroups() {
        List<ResearchGroup> groups = groupDao.getAllRecords();
        List<ResearchGroupData> list = new ArrayList<ResearchGroupData>();

        for (ResearchGroup g : groups) {
            ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
            list.add(d);
        }

        Collections.sort(list, nameComparator);
        return list;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResearchGroupData> getMyGroups() {

        Set<ResearchGroup> groups = personDao.getLoggedPerson().getResearchGroups();
        List<ResearchGroupData> list = new ArrayList<ResearchGroupData>();

        for (ResearchGroup g : groups) {
            ResearchGroupData d = new ResearchGroupData(g.getResearchGroupId(), g.getTitle());
            list.add(d);
        }
        Collections.sort(list, nameComparator);
        return list;
    }
}
