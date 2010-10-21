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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.impl.QueryImpl;

/**
 *
 * @author pbruha
 */
public class SimpleHistoryDao<T, PK extends Serializable> extends SimpleGenericDao<T, PK> implements HistoryDao<T, PK> {

  public SimpleHistoryDao(Class<T> type) {
    super(type);
  }

  public List<History> getHistory(String historyType) {
    String whereCondition = "";
    whereCondition = getWhereCondition(historyType);
    String HQLselect = " from History as h" +whereCondition;
    return getHibernateTemplate().find(HQLselect);
  }

  public long getCountOfFilesHistory(String historyType) {
    String whereCondition = "";
    List<DownloadStatistic> dCount = null;
    whereCondition = getWhereCondition(historyType);
    String HQLselect = "select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(count(h.historyId)) from History as h"+whereCondition;
    dCount = getHibernateTemplate().find(HQLselect);
    return dCount.get(0).getCount();
  }


  public List<History> getLastDownloadHistory(String historyType) {
    int maxResults = 5;
    Session session = null;
    String whereCondition = "";
    List<History> lastHistoryList = null;
    whereCondition = getWhereCondition(historyType);
    session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    String HQLselect = "from History as h"+whereCondition+" order by h.dateOfDownload desc ";
    lastHistoryList = getTopFiveLastDownloadedHistory(session, maxResults, HQLselect);
    if (lastHistoryList.size() > maxResults) {
      lastHistoryList.subList(maxResults, lastHistoryList.size()).clear();
    }
    return lastHistoryList;
  }

   public List<History> getTopFiveLastDownloadedHistory(Session session, int maxResults, String HQLselect) {
    Query query = null;
    List<History> topHistory = null;
    query = session.createQuery(HQLselect);
    query.setMaxResults(maxResults);
    topHistory = query.list();
    return topHistory;
  }

  public List<DownloadStatistic> getTopFiveHistory(Session session, int maxResults, String HQLselect) {
    Query query = null;
    List<DownloadStatistic> topHistory = null;
    query = session.createQuery(HQLselect);
    query.setMaxResults(maxResults);
    topHistory = query.list();
    return topHistory;
  }

  public String getWhereCondition(String historyType) {
    String whereCondition = "";
    if (historyType.equals("daily")) {
      whereCondition = " where h.dateOfDownload > trunc(sysdate)";
    } else if (historyType.equals("weekly")) {
      whereCondition = " where h.dateOfDownload >= trunc(sysdate, 'iw')";
    } else {
      whereCondition = " where h.dateOfDownload > trunc(sysdate,'mm')";
    }
    return whereCondition;
  }

  public List<DownloadStatistic> getTopDownloadHistory(String historyType) {
    int maxResults = 5;
   
    Session session = null;
    String whereCondition = "";
    String fromTable = " from History as h";
    List<DownloadStatistic> topHistory = null;
    String selectAndCreateObject = " select new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic";
    session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    whereCondition = getWhereCondition(historyType);

    String HQLselect =
            selectAndCreateObject + "(h.scenario.scenarioId, h.scenario.title, count(h.scenario.scenarioId))" + fromTable + whereCondition + " group by h.scenario.scenarioId, h.scenario.title" + " order by count(h.scenario.scenarioId) desc";
    topHistory = getTopFiveHistory(session, maxResults, HQLselect);
    HQLselect =
            selectAndCreateObject + "(h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title, count(h.experiment.scenario.scenarioId))" + fromTable + whereCondition + " group by h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title" + " order by count(h.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getTopFiveHistory(session, maxResults, HQLselect));
    HQLselect =
            selectAndCreateObject + "(h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename, count(h.dataFile.experiment.scenario.scenarioId))" + fromTable + whereCondition + " group by h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename" + " order by count(h.dataFile.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getTopFiveHistory(session, maxResults, HQLselect));
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
        hqlQuery += "lower(" + request.getSource() + ") like lower('%" + request.getCondition() + "%')";
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
