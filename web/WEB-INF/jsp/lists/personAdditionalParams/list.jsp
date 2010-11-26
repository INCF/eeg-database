<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.personAdditionalParamsList">
  <h1><fmt:message key="pageTitle.personAdditionalParamsList"/></h1>

  <table class="dataTable">
    <thead>
      <tr>
        <th style="width: 50px;"><fmt:message key="dataTable.heading.personAdditionalParamsId"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.personAdditionalParamsName"/></th>
        <th><fmt:message key="dataTable.heading.personAdditionalParamsDataType"/></th>
      </tr>
    </thead>
    <c:forEach items="${personAdditionalParamsList}" var="personAdditionalParam">
      <tr>
        <td>${personAdditionalParam.personOptParamDefId}</td>
        <td>${personAdditionalParam.paramName}</td>
        <td>${personAdditionalParam.paramDataType}</td>
      </tr>
    </c:forEach>
  </table>

  <auth:experimenter>
    <div class="actionBox">
      <a href="<c:url value='add.html'/>" class="lightButtonLink"><fmt:message key="link.addPersonAdditionalParam"/></a>
    </div>
  </auth:experimenter>

</ui:listsTemplate>
