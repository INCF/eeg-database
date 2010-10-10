<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/"%>
<ui:groupsTemplate pageTitle="pageTitle.listOfGroupMembers">
  <h1><fmt:message key="pageTitle.listOfGroupMembers"/></h1>
    <c:url value="/groups/list-of-members.html" var="formUrl" />

  <h2>${groupTitle}</h2>

  <table class="dataTable listOfPersonsDataTable">
    <thead>
      <tr>
        <th><fmt:message key="dataTable.heading.name"/></th>
        <th><fmt:message key="dataTable.heading.surname"/></th>
        <th><fmt:message key="dataTable.heading.username"/></th>
        <th><fmt:message key="dataTable.heading.roleInGroup"/></th>
        <c:if test="${userIsAdminInGroup}">
          <th style="width: 250px;"><fmt:message key="dataTable.heading.changeRoleTo"/></th>
        </c:if>
      </tr>
    </thead>
    <c:forEach items="${memberList}" var="item">
      <tr>
        <td><c:out value="${item.givenname}" /></td>
        <td><c:out value="${item.surname}" /></td>
        <td><c:out value="${item.username}" default="---" /></td>
        <td><f:groupAuthority code="${item.authority}"/></td>
        <c:if test="${userIsAdminInGroup}">
          <td>
            <select name="role">
              <option <c:if test="${item.authority == 'GROUP_READER'}">selected</c:if> onclick="window.location.href='<c:url value='/groups/change-role.html?groupId=${groupId}&personId=${item.personId}&role=GROUP_READER'/>'"><fmt:message key="link.changeGroupRole.reader"/></option>
              <option <c:if test="${item.authority == 'GROUP_EXPERIMENTER'}">selected</c:if> onclick="window.location.href='<c:url value='/groups/change-role.html?groupId=${groupId}&personId=${item.personId}&role=GROUP_EXPERIMENTER'/>'">Experimenter</option>

             <option <c:if test="${item.authority == 'GROUP_ADMIN'}">selected</c:if> onclick="window.location.href='<c:url value='/groups/change-role.html?groupId=${groupId}&personId=${item.personId}&role=GROUP_ADMIN'/>'">Group administrator</option>
            </select>
            <a href="<c:url value='/groups/change-role.html?groupId=${groupId}&personId=${item.personId}&role=REMOVE'/>" onclick="return confirm('<fmt:message key="link.confirm.sureToRemoveUserFromGroup"/>');"><fmt:message key="link.changeGroupRole.remove"/></a>
            
          </td>
         
             
   
        </c:if>
      </tr>
    </c:forEach>
  </table>

  <div class="actionBox">
    <a href="<c:url value='/groups/detail.html?groupId=${groupId}'/>" class="lightButtonLink"><fmt:message key="button.backToGroupDetail"/></a>
    <c:if test="${userIsAdminInGroup}">
      <a href="<c:url value='/groups/add-member.html?groupId=${groupId}' />" class="lightButtonLink"><fmt:message key="button.addMemberToGroup"/></a>
    </c:if>
  </div>
</ui:groupsTemplate>

