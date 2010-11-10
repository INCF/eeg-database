<%-- 
    Document   : searchResults
    Created on : 10.4.2010, 16:48:45
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:scenariosTemplate pageTitle="pageTitle.listOfScenarios">
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
    <table class="dataTable tableSorter">
        <thead>
            <tr>
                <th style="width: 300px;"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                <th><fmt:message key="dataTable.heading.scenarioLength"/></th>
                <th style="width: 100px;"></th>
            </tr>
        </thead>
       <c:forEach items="${scenarioResults}" var="scenario">
        <c:if test="${scenario.userMemberOfGroup || !scenario.privateScenario}" >
            <tr>

                <td><c:out value="${scenario.title}" /></td>
                <td><c:out value="${scenario.scenarioLength}" /></td>
                <td><a href="<c:url value='/scenarios/detail.html?scenarioId=${scenario.scenarioId}' />"><fmt:message key="link.detail"/></a></td>
            </tr>
         </c:if>
        </c:forEach>
        </c:otherwise>
      </c:choose>
    </table>
</ui:scenariosTemplate>