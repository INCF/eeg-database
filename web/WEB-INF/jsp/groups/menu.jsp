<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='/groups/list.html'/>"><fmt:message key="menuItem.listOfGroups"/></a></li>
  <li><a href="<c:url value='/groups/my-groups.html'/>"><fmt:message key="menuItem.myGroups"/></a></li>
  <li><a href="<c:url value='/groups/edit-group-role.html'/>"><fmt:message key="menuItem.editGroupRole"/></a></li>
  <li><a href="<c:url value='/groups/book-room.html'/>"><fmt:message key="menuItem.bookingRoom"/></a></li>
</ul>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='/groups/create-group.html'/>"><fmt:message key="menuItem.createGroup"/></a></li>
</ul>
