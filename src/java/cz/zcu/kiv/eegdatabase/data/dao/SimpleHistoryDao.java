/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author pbruha
 */
public class SimpleHistoryDao<T, PK extends Serializable> extends SimpleGenericDao<T, PK> implements HistoryDao<T, PK> {

  public SimpleHistoryDao(Class<T> type) {
    super(type);
  }

  public List<History> getDailyHistory() {
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate)";
    return getHibernateTemplate().find(HQLselect);
  }

  public List<History> getWeeklyHistory() {
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate)";
    return getHibernateTemplate().find(HQLselect);
  }

  public List<History> getLastDownloadHistory() {
    int maxResults = 5;
    List<History> lastHistoryList = null;
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate) order by history.dateOfDownload desc ";
    lastHistoryList = getHibernateTemplate().find(HQLselect);
    if (lastHistoryList.size() > maxResults) {
      lastHistoryList.subList(maxResults, lastHistoryList.size()).clear();
    }
    return lastHistoryList;
  }

  public List<DownloadStatistic> getTopDownloadHistory() {
    int maxResults = 5;
    List<DownloadStatistic> topHistory = null;

    String HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.scenario.scenarioId, h.scenario.title, count(h.scenario.scenarioId))" +
            " from History as h" +
            " where h.dateOfDownload > trunc(sysdate)" +
            " group by h.scenario.scenarioId, h.scenario.title"+
            " order by count(h.scenario.scenarioId) desc";
    
      getHibernateTemplate().setMaxResults(maxResults);
      topHistory = getHibernateTemplate().find(HQLselect);

    
    return topHistory;
  }
}

