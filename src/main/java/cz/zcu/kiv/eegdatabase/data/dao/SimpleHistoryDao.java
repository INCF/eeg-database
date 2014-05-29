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
 *   SimpleHistoryDao.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
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
 * This class extends powers (extend from) class SimpleGenericDao.
 * Class is determined only for History.
 * @author pbruha
 */
public class SimpleHistoryDao extends SimpleGenericDao<History, Integer> implements HistoryDao {

  private final int maxResults = 5;

  public SimpleHistoryDao() {
    super(History.class);
  }
/**
 * Returns list of download history by constant history type (DAILY, WEEKLY, MONTHLY).
 * @param historyType - history type (DAILY, WEEKLY, MONTHLY)
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return list of download history in given time interval(DAILY, WEEKLY, MONTHLY)
 */
  public List<History> getHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
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
/**
 * Returns string left join for hql query if user has some group and his role is GROUP_ADMIN
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return string left join for hql query
 */
  private String getLeftJoin(boolean isGroupAdmin, int groupId) {
	
	  String joinedTables = " left join fetch h.scenario left join fetch h.person as person left join fetch h.dataFile";
	  
    if (isGroupAdmin && groupId > 0) {
      return joinedTables + " left join person.researchGroupMemberships m";
    }
    return joinedTables;
  }
  
  private String getLeftJoinForTopDownload(boolean isGroupAdmin, int groupId){
	  if (isGroupAdmin && groupId > 0) {
	      return " join h.person as person join person.researchGroupMemberships m";
	    }
	    return "";
  }
  
/**
 * Returns group condition for hql query
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return group condition
 */
  private String getGroupCondition(boolean isGroupAdmin, int groupId) {
    if (isGroupAdmin && groupId > 0) {
      return " and ( m.researchGroup.researchGroupId =" + groupId + ")";
    }
    return "";
  }
/**
 * Returns count of download files by constant history type (DAILY, WEEKLY, MONTHLY)
 * @param historyType - history type (DAILY, WEEKLY, MONTHLY)
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return count of download files
 */
  public long getCountOfFilesHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {
    String whereCondition = "";
    List<DownloadStatistic> dCount = null;
    String leftJoin = "";
    String groupCondition = "";
    leftJoin = getLeftJoinForTopDownload(isGroupAdmin, groupId);
    whereCondition = getWhereCondition(historyType);
    groupCondition = getGroupCondition(isGroupAdmin, groupId);

    String HQLselect = "select distinct new cz.zcu.kiv.eegdatabase.logic.controller.history.DownloadStatistic(count(h.historyId)) from History as h" + leftJoin + whereCondition + groupCondition;

    dCount = getHibernateTemplate().find(HQLselect);
    return dCount.get(0).getCount();
  }
/**
 * Returns list of five last downloaded files
 * @param historyType - history type (DAILY, WEEKLY, MONTHLY)
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return list of five last downloaded files
 */
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
/**
 * Returns list of five last downloaded files
 * @param session - session for create query
 * @param HQLquery - hql query
 * @return list of top five last downloaded files
 */
  private List<History> getTopFiveLastDownloadedHistory(Session session, String HQLquery) {
    Query query = null;
    List<History> topHistory = null;
    query = session.createQuery(HQLquery);
    query.setMaxResults(maxResults);
    topHistory = query.list();
    return topHistory;
  }
 /**
  * Return list of top five downloaded files
  * @param session - session for create query
  * @param HQLquery - hql query
  * @return list of top five downloaded files
  */
  private List<DownloadStatistic> getTopFiveHistory(Session session, String HQLquery) {
    Query query = null;
    List<DownloadStatistic> topHistory = null;
    query = session.createQuery(HQLquery);
    query.setMaxResults(maxResults);
    topHistory = query.list();
    return topHistory;
  }
/**
 * Returns where hql condition(time interval(daily, weekly, monthly))
 * @param historyType - history type (DAILY, WEEKLY, MONTHLY)
 * @return hql where condition
 */
  private String getWhereCondition(ChoiceHistory historyType) {
    String whereCondition = "";
    switch (historyType) {
      case DAILY:
        whereCondition = " where h.dateOfDownload >= date_trunc('day', current_date)";
        break;

      case WEEKLY:
        whereCondition = " where h.dateOfDownload >= date_trunc('week', current_date)";
        break;

      case MONTHLY:
        whereCondition = " where h.dateOfDownload >= date_trunc('month', current_date)";
        break;

      default:
        whereCondition = "";
        break;
    }

    return whereCondition;
  }
/**
 * Return list of top five downloaded files
 * @param historyType - history type (DAILY, WEEKLY, MONTHLY)
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return list of top five downloaded files
 */
  public List<DownloadStatistic> getTopDownloadHistory(ChoiceHistory historyType, boolean isGroupAdmin, int groupId) {

    Session session = null;
    String whereCondition = "";
    String fromTable = " from History as h";
    List<DownloadStatistic> topHistory = null;
    String leftJoin = "";
    String groupCondition = "";
    leftJoin = getLeftJoinForTopDownload(isGroupAdmin, groupId);
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
/**
 * Returns search results
 * @param requests - search request
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return list of search results
 */
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
/**
 * Returns groups cnodition
 * @param isGroupAdmin - determined role GROUP_ADMIN
 * @param groupId - group id
 * @return groups condition for history search
 */
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
/**
 * Returns comparative condition(">=" or "<=" or "like")
 * @param choice - element of date interval (fromDateDownload, toDateDownload)
 * @return comparative condition (">=" or "<=" or "like")
 */
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
