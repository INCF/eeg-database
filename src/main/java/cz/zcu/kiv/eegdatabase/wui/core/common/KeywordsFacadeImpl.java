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
 *   KeywordsFacadeImpl.java, 2014/05/13 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.common;

import cz.zcu.kiv.eegdatabase.data.pojo.ExperimentPackage;
import cz.zcu.kiv.eegdatabase.data.pojo.Keywords;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class KeywordsFacadeImpl implements KeywordsFacade {

    private KeywordsService service;

    @Required
    public void setService(KeywordsService service) {
        this.service = service;
    }

    @Override
    public Integer create(Keywords newInstance) {
        return service.create(newInstance);
    }

    @Override
    public Keywords read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<Keywords> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(Keywords transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(Keywords persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<Keywords> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<Keywords> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<Keywords> getUnique(Keywords example) {
        return service.getUnique(example);
    }

    @Override
    public String getKeywords(int groupId) {
        return service.getKeywords(groupId);
    }

    @Override
    public int getID(int groupId) {
        return service.getID(groupId);
    }

    @Override
    public List<Integer> getKeywordsFromPackage(ExperimentPackage pck) {
        return service.getKeywordsFromPackage(pck);
    }
}
