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
 *   KeywordsServiceImpl.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.dao.SimpleKeywordsDao;
import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Keywords;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class KeywordsServiceImpl implements KeywordsService {

    private SimpleKeywordsDao dao;

    @Required
    public void setDao(SimpleKeywordsDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public Integer create(Keywords newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public Keywords read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Keywords> readByParameter(String parameterName, Object parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(Keywords transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(Keywords persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Keywords> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Keywords> getRecordsAtSides(int first, int max) {
        return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Keywords> getUnique(Keywords example) {
        return dao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public String getKeywords(int groupId) {
        return dao.getKeywords(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public int getID(int groupId) {
        return dao.getID(groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> getKeywordsFromPackage(ExperimentPackage pck) {
        return dao.getKeywordsFromPackage(pck);
    }
}
