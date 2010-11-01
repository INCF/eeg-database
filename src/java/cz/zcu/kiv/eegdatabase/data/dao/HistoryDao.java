/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.Choice;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import java.io.Serializable;
import java.util.List;

/**
 * DAO for fetching and saving download history.
 * @author pbruha
 */
public interface HistoryDao<T, PK extends Serializable> extends GenericDao<T, PK> {

  public List<History> getHistory(Choice historyType, boolean isGroupAdmin, int personId, int groupId);

  public long getCountOfFilesHistory(Choice historyType, boolean isGroupAdmin, int groupId);

  public List<History> getLastDownloadHistory(Choice historyType, boolean isGroupAdmin, int groupId);

  public List<DownloadStatistic> getTopDownloadHistory(Choice historyType, boolean isGroupAdmin, int groupId);

  public List<History> getHistorySearchResults(List<SearchRequest> requests, boolean isGroupAdmin, List<Integer> groupsId);
}
