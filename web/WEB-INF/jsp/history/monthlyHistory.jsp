<%--
    Document   : monthlyHistory
    Created on : 28.9.2010, 14:31:52
    Author     : pbruha
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
  "http://www.w3.org/TR/html4/loose.dtd">

<ui:historyTemplate pageTitle="pageTitle.monthlyDownloadHistory">
  <c:if test="${!access}">
    <c:redirect url="/access-denied-not-admin.html"/>
  </c:if>
  <h1><fmt:message key="pageTitle.monthlyDownloadHistory"/></h1>

  <c:url value="/history/monthly-history.html" var="formUrl" />
  <form:form action="${formUrl}" method="post" commandName="changeDefaultGroup" name="changeDefaultGroup" cssClass="standardInputForm">
    <form:select path="defaultGroup" cssClass="selectBox">
      <option value="-1"><fmt:message key="select.option.noResearchGroupSelected"/></option>
      <c:if test="${isAdmin}"><option value="0" <c:if test="${defaultGroupId==0}"> selected </c:if> ><fmt:message key="select.option.allMonthlyRecords"/></option></c:if>
       <c:forEach items="${researchGroupList}" var="researchGroup">
         <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
        <c:out value="${researchGroup.title}" />
      </option>
      </c:forEach>
    </form:select>
    <input type="submit" value="<fmt:message key='button.show'/>" class="submitButton lightButtonLink" />
  </form:form>

  <h2><fmt:message key="title.monthlyStatistic"/></h2>

  <h3><fmt:message key="text.downloadFiles"/><b>${countOfDownloadedFiles}</b></h3>

  <h2><fmt:message key="title.topDownloads"/></h2>
  <table class="standardValueTable">
    <thead>
      <tr>
        <th><fmt:message key="dataTable.heading.fileName"/></th>
        <th style="width: 5px;"><fmt:message key="dataTable.heading.count"/></th>
      </tr>
    </thead>
    <c:forEach items="${topDownloadedFilesList}" var="topDownloadedFilesList">
      <tr>
        <td>${topDownloadedFilesList.fileName}</td>
        <td>${topDownloadedFilesList.count}</td>
      </tr>
    </c:forEach>
  </table>
  <input type="image" src="<c:url value='/history/graph.html?graphType=MONTHLY&groupId=${defaultGroupId}'/>" name="testgraph" alt="Graph" onclick="location.href(<c:url value='/history/graph.html'/>);" />
  <h2><fmt:message key="title.lastDownloaded"/></h2>
  <table class="standardValueTable">
    <thead>
      <tr>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
        <th><fmt:message key="dataTable.heading.fileName"/></th>
        <th style="width: 80px;"><fmt:message key="dataTable.heading.detailOfUser"/></th>
      </tr>
    </thead>
    <c:forEach items="${lastDownloadedFilesHistoryList}" var="lastDownloadedFilesHistoryList">
      <tr>
        <td><fmt:formatDate value="${lastDownloadedFilesHistoryList.dateOfDownload}" pattern="dd.MM.yyyy, HH:mm"/></td>
        <c:if test="${lastDownloadedFilesHistoryList.scenario != null}">
          <td><c:out value="${lastDownloadedFilesHistoryList.scenario.title}" />-<c:out value="${lastDownloadedFilesHistoryList.scenario.scenarioId}" /><fmt:message key="text.fileTypeXml" /></td>
        </c:if>
        <c:if test="${lastDownloadedFilesHistoryList.experiment != null}">
          <td><c:out value="${lastDownloadedFilesHistoryList.experiment.scenario.title}" /><fmt:message key="text.fileTypeZip" /></td>
        </c:if>
        <c:if test="${lastDownloadedFilesHistoryList.dataFile != null}">
          <td><c:out value="${lastDownloadedFilesHistoryList.dataFile.filename}" /></td>
        </c:if>
        <td><a href="<c:url value='/people/detail.html?personId=${lastDownloadedFilesHistoryList.person.personId}'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
  </table>
  <h2><fmt:message key="title.allMonthlyRecords"/></h2>
  <a class="showAll" id="history"><fmt:message key="seeAll"/></a>
  <table class="dataTable tableSorter" id="monthlyHistory">
    <thead>
      <tr>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.id"/></th>
        <th><fmt:message key="dataTable.heading.fileName"/></th>
        <th><fmt:message key="dataTable.heading.scenarioTitle"/></th>
        <th><fmt:message key="dataTable.heading.username"/></th>

        <th style="width: 80px;"><fmt:message key="dataTable.heading.detailOfUser"/></th>
      </tr>
    </thead>
    <c:forEach items="${historyList}" var="historyList">

      <tr class="1" id="data">
        <td><fmt:formatDate value="${historyList.dateOfDownload}" pattern="dd.MM.yyyy, HH:mm" /></td>
        <td><c:out value="${historyList.historyId}" /></td>
        <c:if test="${historyList.scenario != null}">
          <td><c:out value="${historyList.scenario.title}" />-<c:out value="${historyList.scenario.scenarioId}" /><fmt:message key="text.fileTypeXml" /></td>
          <td ><c:out value="${historyList.scenario.title}" /></td>
        </c:if><c:if test="${historyList.experiment != null}">
          <td><c:out value="${historyList.experiment.scenario.title}" /><fmt:message key="text.fileTypeZip" /></td>
          <td ><c:out value="${historyList.experiment.scenario.title}" /></td>
        </c:if>
        <c:if test="${historyList.dataFile != null}">
          <td ><c:out value="${historyList.dataFile.filename}" /></td>
          <td ><c:out value="${historyList.dataFile.experiment.scenario.title}" /></td>
        </c:if>


        <td ><c:out value="${historyList.person.username}" /></td>
        <td ><a href="<c:url value='/people/detail.html?personId=${historyList.person.personId}'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
  </table>


</ui:historyTemplate>