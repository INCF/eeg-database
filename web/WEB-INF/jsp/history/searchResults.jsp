<%-- 
    Document   : searchResults
    Created on : 19.10.2010, 11:48:32
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:historyTemplate pageTitle="pageTitle.searchResult">
  <h1><fmt:message key="heading.searchResult"/></h1>
  <c:choose>
    <c:when test="${resultsEmpty}">
      <div class="emptyDataTable">
        <fmt:message key="emptyTable.noItems"/>
      </div>
    </c:when>
    <c:when test="${error}">
      <c:out value="${mistake}"/>
    </c:when>
    <c:otherwise>
      <table id="myTable" class="tablesorter">
        <thead>
          <tr>
            <th style="height: 20px;"><fmt:message key="dataTable.heading.date"/></th>
            <th style="height: 20px;"><fmt:message key="dataTable.heading.id"/></th>
            <th style="height: 20px;"><fmt:message key="dataTable.heading.fileType"/></th>
            <th style="height: 20px;"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
            <th style="height: 20px;"><fmt:message key="dataTable.heading.username"/></th>
            <th style="height: 20px;"><fmt:message key="dataTable.heading.detailOfUser"/></th>
          </tr>
        </thead>
        <tbody>
          <c:forEach items="${historyResults}" var="historyResults">
            <tr>
              <td><fmt:formatDate value="${historyResults.dateOfDownload}" pattern="dd.MM.yyyy, HH:mm" /></td>
              <td><c:out value="${historyResults.historyId}" /></td>
              <c:if test="${historyResults.scenario != null}">
                <td><fmt:message key="description.fileType.scenario"/></td>
                <td><c:out value="${historyResults.scenario.title}" /></td>
              </c:if><c:if test="${historyResults.experiment != null}">
                <td><fmt:message key="description.fileType.experiment"/></td>
                <td><c:out value="${historyResults.experiment.scenario.title}" /></td>
              </c:if>
              <c:if test="${historyResults.dataFile != null}">
                <td><fmt:message key="description.fileType.dataFile"/> - <c:out value="${historyResults.dataFile.filename}" /></td>
                <td><c:out value="${historyResults.dataFile.experiment.scenario.title}" /></td>
              </c:if>


              <td><c:out value="${historyResults.person.username}" /></td>
              <td><a href="<c:url value='/people/detail.html?personId=${historyResults.person.personId}'/>"><fmt:message key="link.detail"/></a></td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</ui:historyTemplate>