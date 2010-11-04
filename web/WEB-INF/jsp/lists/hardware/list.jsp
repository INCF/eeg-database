<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.hardwareList">
  <h1><fmt:message key="pageTitle.hardwareList"/></h1>

  <table class="dataTable">
    <thead>
      <tr>
        <th style="width: 50px;"><fmt:message key="dataTable.heading.hardwareId"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.hardwareTitle"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.hardwareType"/></th>
        <th><fmt:message key="dataTable.heading.hardwareDescription"/></th>
      </tr>
    </thead>
    <c:forEach items="${hardwareList}" var="hardware">
      <tr>
        <td>${hardware.hardwareId}</td>
        <td>${hardware.title}</td>
        <td>${hardware.type}</td>
        <td>${hardware.description}</td>
      </tr>
    </c:forEach>
  </table>

  <auth:experimenter>
    <div class="actionBox">
      <a href="<c:url value='hardware/add.html'/>" class="lightButtonLink"><fmt:message key="link.addHardware"/></a>
    </div>
  </auth:experimenter>

</ui:listsTemplate>
