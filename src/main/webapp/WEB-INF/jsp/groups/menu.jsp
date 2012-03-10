<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='list.html'/>"><fmt:message key="menuItem.listOfGroups"/></a></li>
  <li><a href="<c:url value='my-groups.html'/>"><fmt:message key="menuItem.myGroups"/></a></li>
  <c:if test="${userCanRequestGroupRole}">
      <li><a href="<c:url value='edit-group-role.html'/>"><fmt:message key="menuItem.requestForGroupRole"/></a></li>
  </c:if>
  <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
      <li><a href="<c:url value='book-room.html'/>"><fmt:message key="menuItem.bookingRoom"/></a></li>
  </security:authorize>
</ul>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='create-group.html'/>"><fmt:message key="menuItem.createGroup"/></a></li>
</ul>
