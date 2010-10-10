<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<ul class="commonButtonMenu">
  <li><a href="<c:url value='/experiments/list.html'/>"><fmt:message key='menuItem.experiments.allExperiments'/></a></li>
  <li><a href="<c:url value='/experiments/my-experiments.html'/>"><fmt:message key='menuItem.experiments.myExperiments'/></a></li>
  <li><a href="<c:url value='/experiments/me-as-subject.html'/>"><fmt:message key='menuItem.experiments.meAsSubject'/></a></li>
</ul>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='/experiments/search.html'/>"><fmt:message key="menuItem.searchMeasuration"/></a></li>
</ul>

<auth:experimenter>
  <ul class="commonButtonMenu">
    <li><a href="<c:url value='/experiments/add-experiment.html'/>" title="Add experiment">Add experiment</a></li>
  </ul>
</auth:experimenter>
