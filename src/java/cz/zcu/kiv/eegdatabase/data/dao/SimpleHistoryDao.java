/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
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

  public long getCountOfFilesDailyHistory() {
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate)";
    return getHibernateTemplate().find(HQLselect).size();
  }

  public long getCountOfFilesWeeklyHistory() {
    String HQLselect = "from History history where history.dateOfDownload >= trunc(sysdate, 'iw')";
    return getHibernateTemplate().find(HQLselect).size();
  }

  public long getCountOfFilesMonthlyHistory() {
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate,'mm')";
    return getHibernateTemplate().find(HQLselect).size();
  }

  public List<History> getWeeklyHistory() {
    String HQLselect = "from History history where history.dateOfDownload >= trunc(sysdate, 'iw')";
    return getHibernateTemplate().find(HQLselect);
  }

  public List<History> getMonthlyHistory() {
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate,'mm')";
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

  public List<History> getLastWeeklyDownloadHistory() {
    int maxResults = 5;
    List<History> lastHistoryList = null;
    String HQLselect = "from History history where history.dateOfDownload >= trunc(sysdate, 'iw') order by history.dateOfDownload desc ";
    lastHistoryList = getHibernateTemplate().find(HQLselect);
    if (lastHistoryList.size() > maxResults) {
      lastHistoryList.subList(maxResults, lastHistoryList.size()).clear();
    }
    return lastHistoryList;
  }

  public List<History> getLastMonthlyDownloadHistory() {
    int maxResults = 5;
    List<History> lastHistoryList = null;
    String HQLselect = "from History history where history.dateOfDownload > trunc(sysdate,'mm') order by history.dateOfDownload desc ";
    lastHistoryList = getHibernateTemplate().find(HQLselect);
    if (lastHistoryList.size() > maxResults) {
      lastHistoryList.subList(maxResults, lastHistoryList.size()).clear();
    }
    return lastHistoryList;
  }

  public List<DownloadStatistic> getDailyTopDownloadHistory() {
    int maxResults = 5;
    List<DownloadStatistic> topHistory = null;

    String HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.scenario.scenarioId, h.scenario.title, count(h.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload > trunc(sysdate)" + " group by h.scenario.scenarioId, h.scenario.title" + " order by count(h.scenario.scenarioId) desc";
    topHistory = getHibernateTemplate().find(HQLselect);
    HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title, count(h.experiment.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload > trunc(sysdate)" + " group by h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title" + " order by count(h.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getHibernateTemplate().find(HQLselect));
    HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename, count(h.dataFile.experiment.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload > trunc(sysdate)" + " group by h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename" + " order by count(h.dataFile.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getHibernateTemplate().find(HQLselect));
    Collections.sort(topHistory);
    if (topHistory.size() > maxResults) {
      topHistory.subList(maxResults, topHistory.size()).clear();
    }

    return topHistory;
  }

  public List<DownloadStatistic> getWeeklyTopDownloadHistory() {
    int maxResults = 5;
    List<DownloadStatistic> topHistory = null;

    String HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.scenario.scenarioId, h.scenario.title, count(h.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload >= trunc(sysdate, 'iw')" + " group by h.scenario.scenarioId, h.scenario.title" + " order by count(h.scenario.scenarioId) desc";
    topHistory = getHibernateTemplate().find(HQLselect);
    HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title, count(h.experiment.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload >= trunc(sysdate, 'iw')" + " group by h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title" + " order by count(h.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getHibernateTemplate().find(HQLselect));
    HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename, count(h.dataFile.experiment.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload >= trunc(sysdate, 'iw')" + " group by h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename" + " order by count(h.dataFile.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getHibernateTemplate().find(HQLselect));
    Collections.sort(topHistory);
    if (topHistory.size() > maxResults) {
      topHistory.subList(maxResults, topHistory.size()).clear();
    }

    return topHistory;
  }

  public List<DownloadStatistic> getMonthlyTopDownloadHistory() {
    int maxResults = 5;
    List<DownloadStatistic> topHistory = null;

    String HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.scenario.scenarioId, h.scenario.title, count(h.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload > trunc(sysdate,'mm')" + " group by h.scenario.scenarioId, h.scenario.title" + " order by count(h.scenario.scenarioId) desc";
    topHistory = getHibernateTemplate().find(HQLselect);
    HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title, count(h.experiment.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload > trunc(sysdate,'mm')" + " group by h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title" + " order by count(h.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getHibernateTemplate().find(HQLselect));
    HQLselect =
            " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename, count(h.dataFile.experiment.scenario.scenarioId))" + " from History as h" + " where h.dateOfDownload > trunc(sysdate,'mm')" + " group by h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename" + " order by count(h.dataFile.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getHibernateTemplate().find(HQLselect));
    Collections.sort(topHistory);
    if (topHistory.size() > maxResults) {
      topHistory.subList(maxResults, topHistory.size()).clear();
    }

    return topHistory;
  }

  public List<History> getHistorySearchResults(List<SearchRequest> requests) {
    boolean ignoreChoice = false;
    String hqlQuery = "from History where ";
    for (SearchRequest request : requests) {
      if (request.getCondition().equals("")) {
        if (request.getChoice().equals("")) {
          ignoreChoice = true;
        }
        continue;
      }
      if (!ignoreChoice) {
        hqlQuery += request.getChoice();

      }
      if (request.getSource().endsWith("DateOfDownload")) {
        hqlQuery += "dateOfDownload" + getCondition(request.getSource()) + "'" + request.getCondition() + "'";
      } else {
        hqlQuery += "lower("+request.getSource() + ") like lower('%" + request.getCondition() + "%')";
      }
    }
    List<History> results;
    try {
      results = getHibernateTemplate().find(hqlQuery);
    } catch (Exception e) {
      return new ArrayList<History>();
    }
    return results;
  }

  private String getCondition(String choice) {
    if (choice.equals("fromDateOfDownload")) {
      return ">=";
    }
    if (choice.equals("toDateOfDownload")) {
      return "<=";
    }
    return " like ";
  }
}
