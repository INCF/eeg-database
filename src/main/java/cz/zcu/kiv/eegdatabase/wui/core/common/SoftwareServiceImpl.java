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
 *   SoftwareServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleSoftwareDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;
import cz.zcu.kiv.eegdatabase.data.pojo.Software;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 13:28
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareServiceImpl implements SoftwareService{

    protected Log log = LogFactory.getLog(getClass());

    SimpleSoftwareDao softwareDao;

    @Required
    public void setSoftwareDao(SimpleSoftwareDao softwareDao){
        this.softwareDao = softwareDao;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDefaultTitle(String title, int swId) {
        return softwareDao.canSaveDefaultTitle(title, swId);
    }

    @Override
    @Transactional
    public Integer create(Software newInstance) {
        return softwareDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Software read(Integer id) {
        return softwareDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> readByParameter(String parameterName, Object parameterValue) {
        return softwareDao.readByParameter(parameterName, parameterValue);
    }


    @Override
    @Transactional
    public void update(Software transientObject) {
        softwareDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Software persistentObject) {
        softwareDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getAllRecords() {
        return softwareDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getRecordsAtSides(int first, int max) {
        return softwareDao.getRecordsAtSides(first,max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return softwareDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getUnique(Software example) {
        return softwareDao.findByExample(example);
    }
    
    @Override
    @Transactional
    public void createGroupRel(Software persistent, ResearchGroup researchGroup) {
        softwareDao.createGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getItemsForList() {
        return softwareDao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Software> getRecordsByGroup(int groupId) {
        return softwareDao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return softwareDao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return softwareDao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(Software persistent, ResearchGroup researchGroup) {
        softwareDao.deleteGroupRel(persistent, researchGroup);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title) {
        return softwareDao.canSaveTitle(title);
    }
}
