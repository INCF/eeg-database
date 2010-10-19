<%-- 
    Document   : searchResults
    Created on : 19.10.2010, 11:48:32
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:historyTemplate pageTitle="pageTitle.searchResult"/>">
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
    <table class="dataTable">
        <thead>
            <tr>
                <th style="width: 300px;"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                 <th style="width: 100px;">Date</th>
            </tr>
        </thead>
       <c:forEach items="${historyResults}" var="history">
        
            <tr>

                <td><c:out value="${history.scenario.title}" /></td>
                <td><c:out value="${history.dateOfDownload}" /></td>
                <%--   <td><a href="<c:url value='/history/detail.html?scenarioId=${history.scenario.scenarioId}' />"><fmt:message key="link.detail"/></a></td> --%>
            </tr>
        
        </c:forEach>
        </c:otherwise>
      </c:choose>
    </table>
</ui:historyTemplate>