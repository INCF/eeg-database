/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.dao;

import cz.zcu.kiv.eegdatabase.data.pojo.History;
import cz.zcu.kiv.eegdatabase.logic.controller.history.ChoiceHistory;
import cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic;
import cz.zcu.kiv.eegdatabase.logic.controller.search.SearchRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author pbruha
 */
public class SimpleHistoryDao<T, PK extends Serializable> extends SimpleGenericDao<T, PK> implements HistoryDao<T, PK> {

  private final int maxResults = 5;

  public SimpleHistoryDao(Class<T> type) {
    super(type);
  }

  public List<History> getHistory(ChoiceHistory historyType, boolean isGroupAdmin, int personId, int groupId) {
    String whereCondition = "";
    String leftJoin = "";
    String groupCondition = "";
    List<History> results = null;
    leftJoin = getLeftJoin(isGroupAdmin, groupId);
    whereCondition = getWhereCondition(historyType);
    groupCondition = getGroupCondition(isGroupAdmin, groupId);
    String HQLselect = "select distinct h from History as h" + leftJoin + whereCondition + groupCondition;
    if (!isGroupAdmin) {
      return getHibernateTemplate().find(HQLselect);
    }
    results = getHibernateTemplate().find(HQLselect);
    return results;
  }

  private String getLeftJoin(boolean isGroupAdmin, int groupId) {
    if (isGroupAdmin && groupId > 0) {
      return " left join h.person.researchGroupMemberships m";
    }
    return "";
  }

  private String getGroupCondition(boolean isGroupAdmin, int groupId) {
    if (isGroupAdmin && groupId > 0) {
      return " and ( m.researchGroup.researchGroupId =" + groupId + ")";
    }
    return "";
  }

  public long getCountOfFilesHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
    String whereCondition = "";
    List<DownloadStatistic> dCount = null;
    String leftJoin = "";
    String groupCondition = "";
    leftJoin = getLeftJoin(isGroupAdmin, groupId);
    whereCondition = getWhereCondition(historyType);
    groupCondition = getGroupCondition(isGroupAdmin, groupId);

    String HQLselect = "select distinct new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(count(h.historyId)) from History as h" + leftJoin + whereCondition + groupCondition;

    dCount = getHibernateTemplate().find(HQLselect);
    return dCount.get(0).getCount();
  }

  public List<History> getLastDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
    Session session = null;
    String whereCondition = "";
    List<History> lastHistoryList = null;
    String leftJoin = "";
    String groupCondition = "";
    leftJoin = getLeftJoin(isGroupAdmin, groupId);
    whereCondition = getWhereCondition(historyType);
    groupCondition = getGroupCondition(isGroupAdmin, groupId);
    session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    String HQLselect = "select distinct h from History as h" + leftJoin + whereCondition + groupCondition + " order by h.dateOfDownload desc ";
    lastHistoryList = getTopFiveLastDownloadedHistory(session, HQLselect);
    return lastHistoryList;
  }

  private List<History> getTopFiveLastDownloadedHistory(Session session, String HQLselect) {
    Query query = null;
    List<History> topHistory = null;
    query = session.createQuery(HQLselect);
    query.setMaxResults(maxResults);
    topHistory = query.list();
    return topHistory;
  }

  private List<DownloadStatistic> getTopFiveHistory(Session session, String HQLselect) {
    Query query = null;
    List<DownloadStatistic> topHistory = null;
    query = session.createQuery(HQLselect);
    query.setMaxResults(maxResults);
    topHistory = query.list();
    return topHistory;
  }

  private String getWhereCondition(ChoiceHistory historyType) {
    String whereCondition = "";
    switch (historyType) {
      case DAILY:
        whereCondition = " where h.dateOfDownload > trunc(sysdate)";
        break;

      case WEEKLY:
        whereCondition = " where h.dateOfDownload >= trunc(sysdate, 'iw')";
        break;

      case MONTHLY:
        whereCondition = " where h.dateOfDownload > trunc(sysdate,'mm')";
        break;

      default:
        whereCondition = "";
        break;
    }

    return whereCondition;
  }

  public List<DownloadStatistic> getTopDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {

    Session session = null;
    String whereCondition = "";
    String fromTable = " from History as h";
    List<DownloadStatistic> topHistory = null;
    String leftJoin = "";
    String groupCondition = "";
    leftJoin = getLeftJoin(isGroupAdmin, groupId);
    groupCondition = getGroupCondition(isGroupAdmin, groupId);
    String selectAndCreateObject = "select distinct new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic";
    session = getHibernateTemplate().getSessionFactory().getCurrentSession();
    whereCondition = getWhereCondition(historyType);

    String HQLselect =
            selectAndCreateObject + "(h.scenario.scenarioId, h.scenario.title, count(h.scenario.scenarioId))" + fromTable + leftJoin + whereCondition + groupCondition + " group by h.scenario.scenarioId, h.scenario.title" + " order by count(h.scenario.scenarioId) desc";
    topHistory = getTopFiveHistory(session, HQLselect);
    HQLselect =
            selectAndCreateObject + "(h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title, count(h.experiment.scenario.scenarioId))" + fromTable + leftJoin + whereCondition + groupCondition + " group by h.experiment.scenario.scenarioId, h.experiment.experimentId, h.experiment.scenario.title" + " order by count(h.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getTopFiveHistory(session, HQLselect));
    HQLselect =
            selectAndCreateObject + "(h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename, count(h.dataFile.experiment.scenario.scenarioId))" + fromTable + leftJoin + whereCondition + groupCondition + " group by h.dataFile.experiment.scenario.scenarioId, h.dataFile.experiment.scenario.title, h.dataFile.filename" + " order by count(h.dataFile.experiment.scenario.scenarioId) desc";
    topHistory.addAll(getTopFiveHistory(session, HQLselect));
    Collections.sort(topHistory);
    if (topHistory.size() > maxResults) {
      topHistory.subList(maxResults, topHistory.size()).clear();
    }
    return topHistory;
  }

  public List<History> getHistorySearchResults(List<SearchRequest> requests, boolean isGroupAdmin, List<Integer> groupsId) {
    boolean ignoreChoice = false;
    String leftJoin = "";
    leftJoin = getLeftJoin(isGroupAdmin, groupsId.get(0));
    String hqlQuery = "select distinct h from History as h" + leftJoin + " where ";
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
        hqlQuery += "h.dateOfDownload" + getCondition(request.getSource()) + "'" + request.getCondition() + "'";
      } else {
        hqlQuery += "lower(h." + request.getSource() + ") like lower('%" + request.getCondition() + "%')";
      }
    }
    hqlQuery += getGroupsCondition(isGroupAdmin, groupsId);
    List<History> results;
    try {
      results = getHibernateTemplate().find(hqlQuery);
      System.out.println(results.size());
    } catch (Exception e) {
      return new ArrayList<History>();
    }
    return results;
  }

  private String getGroupsCondition(boolean isGroupAdmin, List<Integer> groupsId) {
    if (isGroupAdmin) {
      boolean first = true;
      String query = " and (";
      for (int groupId : groupsId) {
        if (first) {
          query += " m.researchGroup.researchGroupId =" + groupId;
          first = false;
        } else {
          query += " or m.researchGroup.researchGroupId =" + groupId;
        }
      }
      return query + ")";
    }
    return "";
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
