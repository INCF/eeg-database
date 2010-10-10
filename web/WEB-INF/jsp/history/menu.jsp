<%-- 
    Document   : menu
    Created on : 28.9.2010, 14:06:21
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<ul class="commonButtonMenu">
  <li><a href="<c:url value='/history/daily-history.html'/>"><fmt:message key='menuItem.history.dailyHistory'/></a></li>
  <li><a href="<c:url value='/history/weekly-history.html'/>"><fmt:message key='menuItem.history.weeklyHistory'/></a></li>
  <li><a href="<c:url value='/history/monthly-history.html'/>"><fmt:message key='menuItem.history.monthlyHistory'/></a></li>
</ul>

<ul class="commonButtonMenu">
  <li><a href="<c:url value='/history/search.html'/>"><fmt:message key='menuItem.history.searchByExperiment'/></a></li>
</ul>