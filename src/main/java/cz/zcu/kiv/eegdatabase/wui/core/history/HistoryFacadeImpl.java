package cz.zcu.kiv.eegdatabase.wui.core.history;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
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
    public List<History> readByParameter(String parameterName, int parameterValue) {
        return service.readByParameter(parameterName, parameterValue);
    }

    @Override
    public List<History> readByParameter(String parameterName, String parameterValue) {
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
