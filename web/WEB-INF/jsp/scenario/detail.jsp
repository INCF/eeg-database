<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:scenariosTemplate pageTitle="pageTitle.scenarioDetail">
    <h1><fmt:message key="pageTitle.scenarioDetail"/></h1>

    <table class="standardValueTable">
        <tr>
            <th><fmt:message key="valueTable.scenarioTitle"/></th>
            <td><c:out value="${scenarioDetail.title}" /></td>
        </tr>
        <tr>
            <th><fmt:message key="valueTable.scenarioLength"/></th>
            <td>${scenarioDetail.scenarioLength} <fmt:message key="valueTable.scenarioLength.minutes"/></td>
        </tr>
        <tr>
            <th><fmt:message key="valueTable.scenarioDescription"/></th>
            <td><c:out value="${scenarioDetail.description}" /></td>
        </tr>
        <tr>
            <th><fmt:message key="label.private"/></th>
            <td>${scenarioDetail.privateScenario}</td>
        </tr>
    </table>

    <div class="actionBox">
        <a href="<c:url value='/scenarios/download-xml.html?scenarioId=${scenarioDetail.scenarioId}' />"
           class="lightButtonLink"><fmt:message key="link.downloadXMLFile"/></a>
        <c:if test="${isOwner}">
            <a href="<c:url value='/scenarios/edit.html?id=${scenarioDetail.scenarioId}' />" class="lightButtonLink"><fmt:message key="button.edit"/></a>
        </c:if>
    </div>


</ui:scenariosTemplate>
