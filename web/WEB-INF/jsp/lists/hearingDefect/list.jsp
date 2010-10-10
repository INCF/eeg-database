<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.hearingDefectList">
  <h1><fmt:message key="pageTitle.hearingDefectList"/></h1>

  <table class="dataTable">
    <thead>
      <tr>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.hearingDefectId"/></th>
        <th><fmt:message key="dataTable.heading.hearingDefectDescription"/></th>
      </tr>
    </thead>
    <c:forEach items="${hearingDefectList}" var="hearingImpairment">
      <tr>
        <td>${hearingImpairment.hearingImpairmentId}</td>
        <td>${hearingImpairment.description}</td>
      </tr>
    </c:forEach>
  </table>

  <auth:experimenter>
    <div class="actionBox">
      <a href="<c:url value='/lists/hearing-defects/add.html'/>" class="lightButtonLink"><fmt:message key="link.addHearingDefect"/></a>
    </div>
  </auth:experimenter>

</ui:listsTemplate>
