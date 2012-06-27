<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="header">
  <div class="pageTitle"><fmt:message key='web.title'/></div>
  <security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <div class="loggedUserBox">
      <fmt:message key="system.loggedUser" />: <security:authentication property="principal.username"/>
      <span><a href="<c:url value='/my-account/overview.html'/>" title="<fmt:message key='system.myAccount'/>"><fmt:message key='system.myAccount'/></a></span>
      <a href="<c:url value='/logout'/>" title="<fmt:message key='system.logOut'/>"><fmt:message key='system.logOut'/></a>
    </div>
  </security:authorize>
  <security:authorize ifAnyGranted="ROLE_ANONYMOUS">
    <div class="loggedUserBox">
      <fmt:message key='system.noUserLogged'/>
      <span><a href="<c:url value='registration.html'/>"><fmt:message key="system.register" /></a></span>
    </div>
  </security:authorize>

  <ul class="mainMenu">
    <li><a href="<c:url value='/home.html'/>" title="<fmt:message key='menuItem.home'/>"><fmt:message key='menuItem.home'/></a></li>

    <security:authorize ifNotGranted="ROLE_ANONYMOUS">
      <li><a href="<c:url value='/articles/list.html'/>" title="<fmt:message key='menuItem.articles'/>"><fmt:message key='menuItem.articles'/></a></li>
       <li><a href="<c:url value='/search/index.html'/>" title="<fmt:message key='menuItem.search'/>"><fmt:message key='menuItem.search'/></a></li>
      <li><a href="<c:url value='/experiments/list.html'/>" title="<fmt:message key='menuItem.experiments'/>"><fmt:message key='menuItem.experiments'/></a></li>
      <li><a href="<c:url value='/scenarios/list.html'/>" title="<fmt:message key='menuItem.scenarios'/>"><fmt:message key='menuItem.scenarios'/></a></li>
      <li><a href="<c:url value='/groups/list.html'/>" title="<fmt:message key='menuItem.groups'/>"><fmt:message key='menuItem.groups'/></a></li>
      <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
        <li><a href="<c:url value='/people/list.html'/>" title="<fmt:message key='menuItem.people'/>"><fmt:message key='menuItem.people'/></a></li>
      </security:authorize>
      <li><a href="<c:url value='/lists/index.html'/>" title="<fmt:message key='menuItem.lists'/>"><fmt:message key='menuItem.lists'/></a></li>
      <security:authorize ifAllGranted="ROLE_ADMIN">
        <li><a href="<c:url value='/administration/change-user-role.html'/>" title="<fmt:message key='menuItem.administration'/>"><fmt:message key='menuItem.administration'/></a></li>
      </security:authorize>
      <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
        <li><a href="<c:url value='/history/daily-history.html'/>" title="<fmt:message key='menuItem.history'/>"><fmt:message key='menuItem.history'/></a></li>
      </security:authorize>
    </security:authorize>
  </ul>

  <div class="cleaner"></div>
</div>