<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.eyesDefectList">
  <h1><fmt:message key="pageTitle.eyesDefectList"/></h1>

  <table class="dataTable tableSorter">
    <thead>
      <tr>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.eyesDefectId"/></th>
        <th><fmt:message key="dataTable.heading.eyesDefectDescription"/></th>
      </tr>
    </thead>
    <c:forEach items="${eyesDefectList}" var="visualImpairment">
      <tr>
        <td>${visualImpairment.visualImpairmentId}</td>
        <td>${visualImpairment.description}</td>
      </tr>
    </c:forEach>
  </table>

  <auth:experimenter>
    <div class="actionBox">
      <a href="<c:url value='eyes-defects/add.html'/>" class="lightButtonLink"><fmt:message key="link.addEyesDefect"/></a>
    </div>
  </auth:experimenter>

</ui:listsTemplate>
