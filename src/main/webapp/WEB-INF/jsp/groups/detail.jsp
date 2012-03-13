<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:groupsTemplate pageTitle="pageTitle.researchGroupDetail">
    <h1><c:out value="${researchGroup.title}"/></h1>

    <p>
        <c:out value="${researchGroup.description}"/>
    </p>

  <div class="actionBox">
    <c:if test="${not userIsMemberInGroup}">
          <a href="<c:url value='membership-request.html?groupId=${researchGroup.researchGroupId}' />" class="lightButtonLink"><fmt:message key="button.membershipRequest"/></a>
    </c:if>
    <c:if test="${userIsExperimenterInGroup}">
          <a href="<c:url value='list-of-members.html?groupId=${researchGroup.researchGroupId}' />" class="lightButtonLink"><fmt:message key="button.listOfMembers"/></a>
    </c:if>
    <c:if test="${userIsAdminInGroup}">
          <a href="<c:url value='add-member.html?groupId=${researchGroup.researchGroupId}' />" class="lightButtonLink"><fmt:message key="button.addMemberToGroup"/></a>
          <a href="<c:url value='edit.html?groupId=${researchGroup.researchGroupId}' />" class="lightButtonLink"><fmt:message key="button.edit"/></a>
    </c:if>
  </div>

</ui:groupsTemplate>
