<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:experimentsTemplate pageTitle="${experimentListTitle}">

  <h1><fmt:message key="${experimentListTitle}"/></h1>

  <c:if test="${userNotMemberOfAnyGroup}">
    <fmt:message key="text.notMemberOfAnyGroup"/><br/><br/>
  </c:if>

  ${paginator}

  <table class="dataTable measurationListDataTable">
    <thead>
      <tr>
        <th style="width: 40px;"><fmt:message key="dataTable.heading.number"/></th>
        <th><fmt:message key="dataTable.heading.scenarioTitle"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.gender" /></th>
        <th style="width: 80px;"><fmt:message key="dataTable.heading.yearOfBirth" /></th>
        <th style="width: 80px;"><fmt:message key="dataTable.heading.services" /></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.detail" /></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.download" /></th>
      </tr>
    </thead>
    <c:forEach items="${experimentList}" var="experiment" varStatus="status">
      <tr>
        <td>
            <c:out value="${experiment.experimentId}" />
        </td>
        <td>
            <a href="<c:url value='/scenarios/detail.html?scenarioId=${experiment.scenario.scenarioId}' />">
                <c:out value="${experiment.scenario.title}" />
            </a>
        </td>
        <td><fmt:formatDate value="${experiment.startTime}" pattern="dd.MM.yyyy, HH:mm" /></td>
        <td><c:out value="${experiment.personBySubjectPersonId.gender}" /></td>
        <td><fmt:formatDate value="${experiment.personBySubjectPersonId.dateOfBirth}" pattern="yyyy" /></td>
        <td>
          <c:choose>
            <c:when test="${experiment.suitable}">
              <a href="<c:url value='../services/index.html?experimentId=${experiment.experimentId}'/>">
                <fmt:message key='menuItem.services'/>
              </a>
            </c:when>
            <c:otherwise><fmt:message key="label.notAvailable"/></c:otherwise>
          </c:choose>
        </td>
        <td>
          <a href="<c:url value='detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message key="link.detail"/></a>
        </td>
        <td>
          <a href="<c:url value='choose-metadata.html?id=${experiment.experimentId}' />"><fmt:message key="link.download"/></a>
        </td>
      </tr>
    </c:forEach>
  </table>

  ${paginator}
</ui:experimentsTemplate>
