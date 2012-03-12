<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:groupsTemplate pageTitle="pageTitle.membershipRequest">
  <h1><fmt:message key="pageTitle.membershipRequest"/></h1>
  <c:url value="membership-request.html" var="formUrl"/>

  <form:form action="${formUrl}" method="post" commandName="editGroupRoleCommand" name="editGroupRoleCommand" cssClass="standardInputForm">
    <fieldset>
        <fmt:message key='label.group'/>:
        <c:out value="${researchGroup.title}" />
      <p>
        <fmt:message key='text.confirmSendingMembershipRequest'/>
      </p>
      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.sendRequest'/>" class="submitButton lightButtonLink"/>
      </div>
    </fieldset>
  </form:form>
</ui:groupsTemplate>
