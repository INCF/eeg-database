<%-- 
    Document   : searchResults
    Created on : 9.4.2010, 13:03:49
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:experimentsTemplate pageTitle="pageTitle.measurationSearch">

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
                <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
                <th style="width: 60px;"><fmt:message key="dataTable.heading.id"/></th>
                <th><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                <th style="width: 80px;"></th>
                <th style="width: 80px;"></th>
            </tr>
            </thead>
            <c:forEach items="${suitable}" var="experiment">
                <c:if test="${experiment.userMemberOfGroup || !experiment.privateExperiment}">
                    <tr>

                        <td><fmt:formatDate value="${experiment.startTime}" pattern="dd.MM.yyyy, HH:mm"/></td>
                        <td><c:out value="${experiment.experimentId}"/>(${experiment.researchGroup.researchGroupId})
                        </td>
                        <td><c:out value="${experiment.scenario.title}"/></td>
                        <td><a href="<c:url value='detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message
                                key="link.detail"/></a></td>
                        <td>
                            <a href="<c:url value='../services/index.html?experimentId=${experiment.experimentId}'/>"><fmt:message
                                    key='menuItem.services'/></a></td>
                    </tr>
                </c:if>
            </c:forEach>
            <c:forEach items="${notSuitable}" var="experiment">
                <c:if test="${experiment.userMemberOfGroup || !experiment.privateExperiment}">
                    <tr>

                        <td><fmt:formatDate value="${experiment.startTime}" pattern="dd.MM.yyyy, HH:mm"/></td>
                        <td><c:out value="${experiment.experimentId}"/>(${experiment.researchGroup.researchGroupId})
                        </td>
                        <td><c:out value="${experiment.scenario.title}"/></td>
                        <td><a href="<c:url value='detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message
                                key="link.detail"/></a></td>
                        <td><fmt:message key="label.notAvailable"/></td>

                    </tr>
                </c:if>
            </c:forEach>
        </c:otherwise>
    </c:choose>
    </table>

</ui:experimentsTemplate>