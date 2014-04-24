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
 *   ProjectTypeServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleProjectTypeDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ProjectType;
import cz.zcu.kiv.eegdatabase.data.pojo.ResearchGroup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 16.4.13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class ProjectTypeServiceImpl implements ProjectTypeService {

    protected Log log = LogFactory.getLog(getClass());

    SimpleProjectTypeDao projectTypeDao;

    public void setProjectTypeDao(SimpleProjectTypeDao projectTypeDao) {
        this.projectTypeDao = projectTypeDao;
    }

    @Override
    @Transactional
    public Integer create(ProjectType newInstance) {
        return projectTypeDao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectType read(Integer id) {
        return projectTypeDao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> readByParameter(String parameterName, Object parameterValue) {
        return projectTypeDao.readByParameter(parameterName, parameterValue);
    }


    @Override
    @Transactional
    public void update(ProjectType transientObject) {
        projectTypeDao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(ProjectType persistentObject) {
        projectTypeDao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getAllRecords() {
        return projectTypeDao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getRecordsAtSides(int first, int max) {
        return projectTypeDao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return projectTypeDao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getUnique(ProjectType example) {
        return projectTypeDao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveTitle(String title, int groupId, int projectId) {
        return projectTypeDao.canSaveTitle(title, groupId, projectId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean isDefault(int id) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canSaveDefaultTitle(String title, int projectId) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    @Transactional
    public void createGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        projectTypeDao.createGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getItemsForList() {
        return projectTypeDao.getItemsForList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectType> getRecordsByGroup(int groupId) {
        return projectTypeDao.getRecordsByGroup(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canDelete(int id) {
        return projectTypeDao.canDelete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasGroupRel(int id) {
        return projectTypeDao.hasGroupRel(id);
    }

    @Override
    @Transactional
    public void deleteGroupRel(ProjectType persistent, ResearchGroup researchGroup) {
        projectTypeDao.deleteGroupRel(persistent, researchGroup);
    }

    @Override
    @Transactional
    public void createDefaultRecord(ProjectType project) {
        throw new UnsupportedOperationException("not implemented");
    }
}
