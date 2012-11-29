<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='list.html'/>" title="List of people">List of people</a></li>
  <li><a href="<c:url value='search.html'/>"><fmt:message key="menuItem.searchPeople"/></a></li>
</ul>
<auth:experimenter>
  <ul class="commonButtonMenu">
      <li><a href="<c:url value='add-person.html'/>" title="Add person">Add person</a></li>
  </ul>
</auth:experimenter>

