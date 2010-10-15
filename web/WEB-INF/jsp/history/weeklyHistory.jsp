<%-- 
    Document   : weeklyList
    Created on : 28.9.2010, 14:30:59
    Author     : pbruha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">
<ui:historyTemplate pageTitle="pageTitle.weeklyDownloadHistory">

  <h1><fmt:message key="pageTitle.weeklyDownloadHistory"/></h1>

  <table class="dataTable">
    <thead>
      <tr>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.id"/></th>
        <th><fmt:message key="dataTable.heading.fileType"/></th>
        <th><fmt:message key="dataTable.heading.scenarioTitle"/></th>
        <th><fmt:message key="dataTable.heading.username"/></th>

        <th style="width: 80px;"><fmt:message key="dataTable.heading.detailOfUser"/></th>
      </tr>
    </thead>
    <c:forEach items="${historyList}" var="historyList">
      <tr>
        <td><fmt:formatDate value="${historyList.dateOfDownload}" pattern="dd.MM.yyyy, HH:mm" /></td>
        <td><c:out value="${historyList.historyId}" /></td>
        <c:if test="${historyList.scenario != null}">
          <td><fmt:message key="description.fileType.scenario"/></td>
          <td><c:out value="${historyList.scenario.title}" /></td>
        </c:if><c:if test="${historyList.experiment != null}">
          <td><fmt:message key="description.fileType.experiment"/></td>
          <td><c:out value="${historyList.experiment.scenario.title}" /></td>
        </c:if>
        <c:if test="${historyList.dataFile != null}">
          <td><fmt:message key="description.fileType.dataFile"/> - <c:out value="${historyList.dataFile.filename}" /></td>
          <td><c:out value="${historyList.dataFile.experiment.scenario.title}" /></td>
        </c:if>


        <td><c:out value="${historyList.person.username}" /></td>
        <td><a href="<c:url value='/people/detail.html?personId=${historyList.person.personId}'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
  </table>
  <h2><fmt:message key="pageTitle.dailyStatistic"/></h2>

  <h3><fmt:message key="text.downloadFiles"/><b>${countOfDownloadedFiles}</b></h3>

   <h2><fmt:message key="title.topDownloads"/></h2>
  <table class="standardValueTable">
    <thead>
      <tr>
        <th><fmt:message key="dataTable.heading.fileType"/></th>
        <th><fmt:message key="dataTable.heading.scenarioTitle"/></th>
        <th style="width: 5px;"><fmt:message key="dataTable.heading.count"/></th>
      </tr>
    </thead>
    <c:forEach items="${topDownloadedFilesList}" var="topDownloadedFilesList">
      <tr>
        <td>${topDownloadedFilesList.fileType}</td>
        <td>${topDownloadedFilesList.title}</td>
        <td>${topDownloadedFilesList.count}</td>
      </tr>
    </c:forEach>
  </table>
     
  <h2><fmt:message key="title.lastDownloaded"/></h2>
  <table class="standardValueTable">
    <thead>
      <tr>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
         <th><fmt:message key="dataTable.heading.fileType"/></th>
        <th><fmt:message key="dataTable.heading.scenarioTitle"/></th>
        <th style="width: 80px;"><fmt:message key="dataTable.heading.detailOfUser"/></th>
      </tr>
    </thead>
    <c:forEach items="${lastDownloadedFilesHistoryList}" var="lastDownloadedFilesHistoryList">
      <tr>
        <td><fmt:formatDate value="${lastDownloadedFilesHistoryList.dateOfDownload}" pattern="dd.MM.yyyy, HH:mm"/></td>
        <c:if test="${lastDownloadedFilesHistoryList.scenario != null}">
          <td><fmt:message key="description.fileType.scenario"/></td>
          <td><c:out value="${lastDownloadedFilesHistoryList.scenario.title}" /></td>
        </c:if>
        <c:if test="${lastDownloadedFilesHistoryList.experiment != null}">
          <td><fmt:message key="description.fileType.experiment"/></td>
          <td><c:out value="${lastDownloadedFilesHistoryList.experiment.scenario.title}" /></td>
        </c:if>
        <c:if test="${lastDownloadedFilesHistoryList.dataFile != null}">
          <td><fmt:message key="description.fileType.dataFile"/> - <c:out value="${lastDownloadedFilesHistoryList.dataFile.filename}" /></td>
          <td><c:out value="${lastDownloadedFilesHistoryList.dataFile.experiment.scenario.title}" /></td>
        </c:if>
        <td><a href="<c:url value='/people/detail.html?personId=${lastDownloadedFilesHistoryList.person.personId}'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
  </table>
 

</ui:historyTemplate>
