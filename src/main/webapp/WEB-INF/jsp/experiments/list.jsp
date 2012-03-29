<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:experimentsTemplate pageTitle="${measurationListTitle}">

  <h1><fmt:message key="${measurationListTitle}"/></h1>

  <c:if test="${userNotMemberOfAnyGroup}">
    <fmt:message key="text.notMemberOfAnyGroup"/><br/><br/>
  </c:if>

  <table class="dataTable measurationListDataTable">
    <thead>
      <tr>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.number"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.date"/></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.gender" /></th>
        <th style="width: 80px;"><fmt:message key="dataTable.heading.yearOfBirth" /></th>
        <th style="width: 80px;"><fmt:message key="dataTable.heading.services" /></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.detail" /></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.download" /></th>
      </tr>
    </thead>
    <c:forEach items="${measurationList}" var="measuration" varStatus="status">
      <tr>
        <td>
            <c:out value="${status.index}" />
        </td>
        <td>
            <a href="<c:url value='/scenarios/detail.html?scenarioId=${measuration.scenario.scenarioId}' />">
                <c:out value="${measuration.scenario.title}" />
            </a>
        </td>
        <td><fmt:formatDate value="${measuration.startTime}" pattern="dd.MM.yyyy, HH:mm" /></td>
        <td><c:out value="${measuration.personBySubjectPersonId.gender}" /></td>
        <td><fmt:formatDate value="${measuration.personBySubjectPersonId.dateOfBirth}" pattern="yyyy" /></td>
        <td>
          <c:choose>
            <c:when test="${measuration.suitable}">
              <a href="<c:url value='../services/index.html?experimentId=${measuration.experimentId}'/>">
                <fmt:message key='menuItem.services'/>
              </a>
            </c:when>
            <c:otherwise><fmt:message key="label.notAvailable"/></c:otherwise>
          </c:choose>
        </td>
        <td>
          <a href="<c:url value='detail.html?experimentId=${measuration.experimentId}'/>"><fmt:message key="link.detail"/></a>
        </td>
        <td>
          <a href="<c:url value='choose-metadata.html?id=${measuration.experimentId}' />"><fmt:message key="link.download"/></a>
        </td>
      </tr>
    </c:forEach>
  </table>
</ui:experimentsTemplate>
