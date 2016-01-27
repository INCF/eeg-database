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
 *   HistoryServiceImpl.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.core.history;

import cz.zcu.kiv.eegdatabase.data.dao.HistoryDao;
import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.search.SearchRequest;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class HistoryServiceImpl implements HistoryService {

    HistoryDao dao;

    @Required
    public void setDao(HistoryDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional
    public Integer create(History newInstance) {
        return dao.create(newInstance);
    }

    @Override
    @Transactional(readOnly = true)
    public History read(Integer id) {
        return dao.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<History> readByParameter(String parameterName, Object parameterValue) {
        return dao.readByParameter(parameterName, parameterValue);
    }

    @Override
    @Transactional
    public void update(History transientObject) {
        dao.update(transientObject);
    }

    @Override
    @Transactional
    public void delete(History persistentObject) {
        dao.delete(persistentObject);
    }

    @Override
    @Transactional(readOnly = true)
    public List<History> getAllRecords() {
        return dao.getAllRecords();
    }

    @Override
    @Transactional(readOnly = true)
    public List<History> getRecordsAtSides(int first, int max) {
        return dao.getRecordsAtSides(first, max);
    }

    @Override
    @Transactional(readOnly = true)
    public int getCountRecords() {
        return dao.getCountRecords();
    }

    @Override
    public List<History> getUnique(History example) {
        return dao.findByExample(example);
    }

    @Override
    @Transactional(readOnly = true)
    public List<History> getHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return dao.getHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountOfFilesHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return dao.getCountOfFilesHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<History> getLastDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return dao.getLastDownloadHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DownloadStatistic> getTopDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
        return dao.getTopDownloadHistory(historyType, isGroupAdmin, groupId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<History> getHistorySearchResults(List<SearchRequest> requests, boolean isGroupAdmin, List<Integer> groupsId) {
        return dao.getHistorySearchResults(requests, isGroupAdmin, groupsId);
    }
}
