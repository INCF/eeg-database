<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:scenariosTemplate pageTitle="pageTitle.listOfScenarios">
    <h1><fmt:message key="pageTitle.listOfScenarios"/></h1>

    <table class="dataTable">
        <thead>
            <tr>
                <th style="width: 300px;"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                <th><fmt:message key="dataTable.heading.scenarioLength"/></th>
                <th style="width: 100px;"></th>
            </tr>
        </thead>
        <c:forEach items="${scenarioList}" var="scenario">
          <c:if test="${scenario.userMemberOfGroup || !scenario.privateScenario}" >
            <tr>

                <td><c:out value="${scenario.title}" /></td>
                <td><c:out value="${scenario.scenarioLength}" /></td>
                <td><a href="<c:url value='/scenarios/detail.html?scenarioId=${scenario.scenarioId}' />"><fmt:message key="link.detail"/></a></td>
            </tr>
          </c:if>
        </c:forEach>
    </table>
</ui:scenariosTemplate>
