<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="cz.zcu.kiv.eegdatabase.logic.controller.util.ControllerUtils" %>
<div class="header">
  <div class="pageTitle"><fmt:message key='web.title'/></div>
  <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
    <div class="loggedUserBox">
      <fmt:message key="system.loggedUser" />: <%= ControllerUtils.getLoggedUserName()%>
      <span><a href="<c:url value='/my-account/overview.html'/>" title="<fmt:message key='system.myAccount'/>"><fmt:message key='system.myAccount'/></a></span>
      <a href="<c:url value='/logout'/>" title="<fmt:message key='system.logOut'/>"><fmt:message key='system.logOut'/></a>
    </div>
  </security:authorize>
  <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN">
    <div class="loggedUserBox">
      <fmt:message key='system.noUserLogged'/>
      <span><a href="<c:url value='/login.html'/>" title="<fmt:message key='system.logIn'/>"><fmt:message key='system.logIn'/></a></span>
    </div>
  </security:authorize>
 <c:url value="/experiments/fulltext.html" var="formUrl"/>
 <form:form action="${formUrl}" method="post" commandName="fullTextSearchCommand" cssClass="globalSearch">
      <input type="text" name="searchTI" value="" size="20" class="textfield" />
      <input type="submit" value="<fmt:message key='button.fulltextSearch'/>" name="searchBT" class="button" />
  </form:form>

  <ul class="mainMenu">
    <li><a href="<c:url value='/home.html'/>" title="<fmt:message key='menuItem.home'/>"><fmt:message key='menuItem.home'/></a></li>

    <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
      <li><a href="<c:url value='/articles/list.html'/>" title="<fmt:message key='menuItem.articles'/>"><fmt:message key='menuItem.articles'/></a></li>
      <li><a href="<c:url value='/experiments/list.html'/>" title="<fmt:message key='menuItem.experiments'/>"><fmt:message key='menuItem.experiments'/></a></li>
      <li><a href="<c:url value='/scenarios/list.html'/>" title="<fmt:message key='menuItem.scenarios'/>"><fmt:message key='menuItem.scenarios'/></a></li>
      <li><a href="<c:url value='/groups/list.html'/>" title="<fmt:message key='menuItem.groups'/>"><fmt:message key='menuItem.groups'/></a></li>
      <li><a href="<c:url value='/people/list.html'/>" title="<fmt:message key='menuItem.people'/>"><fmt:message key='menuItem.people'/></a></li>
      <li><a href="<c:url value='/lists/index.html'/>" title="<fmt:message key='menuItem.lists'/>"><fmt:message key='menuItem.lists'/></a></li>

      <security:authorize ifAllGranted="ROLE_ADMIN">
        <li><a href="<c:url value='/administration/change-user-role.html'/>" title="<fmt:message key='menuItem.administration'/>"><fmt:message key='menuItem.administration'/></a></li>
        <li><a href="<c:url value='/history/daily-history.html'/>" title="<fmt:message key='menuItem.history'/>"><fmt:message key='menuItem.history'/></a></li>
      </security:authorize>
    </security:authorize>
  </ul>

  <div class="cleaner"></div>
</div>