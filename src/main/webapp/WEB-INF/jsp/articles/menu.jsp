<%-- 
    Document   : menu
    Created on : 27.4.2010, 19:00:36
    Author     : Jiri Vlasimsky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<ul class="commonButtonMenu">
  <li><a href="<c:url value='list.html'/>"><fmt:message key='menuItem.articles.allArticles'/></a></li>
  <c:if test="${userIsAdminInAnyGroup}">
    <li><a href="<c:url value='add-article.html'/>"><fmt:message key='menuItem.articles.addArticle'/></a></li>
  </c:if>
    <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
      <li><a href="<c:url value='settings.html'/>"><fmt:message key='menuItem.articles.settings'/></a></li>
  </security:authorize>
</ul>
