package cz.zcu.kiv.eegdatabase.wui.core.history;

import java.util.List;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import cz.zcu.kiv.eegdatabase.wui.core.GenericService;

public interface HistoryService extends GenericService<History, Integer> {

    List<History> getHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId);

    long getCountOfFilesHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId);

    List<History> getLastDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId);

    List<DownloadStatistic> getTopDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId);

    List<History> getHistorySearchResults(List<SearchRequest> requests, boolean isGroupAdmin, List<Integer> groupsId);
}
