<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.fileMetadataParamsList">
  <h1><fmt:message key="pageTitle.fileMetadataParamsList"/></h1>

  <table class="dataTable">
    <thead>
      <tr>
        <th style="width: 50px;"><fmt:message key="dataTable.heading.fileMetadataParamsId"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.fileMetadataParamsName"/></th>
        <th><fmt:message key="dataTable.heading.fileMetadataParamsDataType"/></th>
      </tr>
    </thead>
    <c:forEach items="${fileMetadataParamsList}" var="fileMetadataParam">
      <tr>
        <td>${fileMetadataParam.fileMetadataParamDefId}</td>
        <td>${fileMetadataParam.paramName}</td>
        <td>${fileMetadataParam.paramDataType}</td>
      </tr>
    </c:forEach>
  </table>

  <auth:experimenter>
    <div class="actionBox">
      <a href="<c:url value='add.html'/>" class="lightButtonLink"><fmt:message key="link.addFileMetadataParam"/></a>
    </div>
  </auth:experimenter>

</ui:listsTemplate>
