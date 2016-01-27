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
 *   HistoryFacadeImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.history;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.search.SearchRequest;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class HistoryFacadeImpl implements HistoryFacade {

    HistoryService service;

    @Required
    public void setService(HistoryService service) {
        this.service = service;
    }

    @Override
    public Integer create(History newInstance) {
        return service.create(newInstance);
    }

    @Override
    public History read(Integer id) {
        return service.read(id);
    }

    @Override
    public List<History> readByParameter(String parameterName, Object parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public void update(History transientObject) {
        service.update(transientObject);
    }

    @Override
    public void delete(History persistentObject) {
        service.delete(persistentObject);
    }

    @Override
    public List<History> getAllRecords() {
        return service.getAllRecords();
    }

    @Override
    public List<History> getRecordsAtSides(int first, int max) {
        return service.getRecordsAtSides(first, max);
    }

    @Override
    public int getCountRecords() {
        return service.getCountRecords();
    }

    @Override
    public List<History> getUnique(History example) {
        return service.getUnique(example);
    }

    @Override
    public List<History> getHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return service.getHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    public long getCountOfFilesHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return service.getCountOfFilesHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    public List<History> getLastDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return service.getLastDownloadHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    public List<DownloadStatistic> getTopDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return service.getTopDownloadHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    public List<History> getHistorySearchResults(List<SearchRequest> requests, boolean isGroupAdmin, List<Integer> groupsId) {
        return service.getHistorySearchResults(requests, isGroupAdmin, groupsId);
    }

}
