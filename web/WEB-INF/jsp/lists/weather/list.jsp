<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.weatherDefinitionsList">
  <h1><fmt:message key="pageTitle.weatherDefinitionsList"/></h1>

  <table class="dataTable">
    <thead>
      <tr>
        <th style="width: 50px;"><fmt:message key="dataTable.heading.id"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.title"/></th>
        <th><fmt:message key="dataTable.heading.description"/></th>
      </tr>
    </thead>
    <c:forEach items="${weatherList}" var="weather">
      <tr>
        <td>${weather.weatherId}</td>
        <td>${weather.title}</td>
        <td>${weather.description}</td>
      </tr>
    </c:forEach>
  </table>

  <auth:experimenter>
    <div class="actionBox">
      <a href="<c:url value='add.html'/>" class="lightButtonLink"><fmt:message key="link.addWeatherDefinition"/></a>
    </div>
  </auth:experimenter>

</ui:listsTemplate>
