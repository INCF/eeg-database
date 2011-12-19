<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<ul class="commonButtonMenu">
  <li><a href="<c:url value='index.html'/>"><fmt:message key='menuItem.fulltext'/></a></li>
</ul>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='../experiments/search.html'/>"><fmt:message key='menuItem.searchMeasuration'/></a></li>
  <li><a href="<c:url value='../scenarios/search.html'/>"><fmt:message key='menuItem.searchScenario'/></a></li>
  <li><a href="<c:url value='../people/search.html'/>"><fmt:message key="menuItem.searchPeople"/></a></li>
</ul>