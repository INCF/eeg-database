<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="af" tagdir="/WEB-INF/tags/autoforms/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:groupsTemplate pageTitle="pageTitle.addMemberToGroup">
  <div class="preH1"><fmt:message key="pageTitle.addMemberToGroup"/></div>
  <h1><c:out value="${groupTitle}"/></h1>

  <c:url value="add-member.html?groupId=${groupId}" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="addMemberToGroup" cssClass="standardForm">
    <fieldset>

      <input type="hidden" name="groupFormId" value="${groupId}"/>

      <af:textField path="userName" labelKey="label.email"/>


      <div class="selectBox">
        <form:label path="userRole" cssErrorClass="error"><fmt:message key="label.userRole"/></form:label>

        <form:select path="userRole">
          <form:option value="-1"><fmt:message key="select.option.noUserRoleSelected"/></form:option>
          <form:option value="GROUP_READER"><fmt:message key="select.option.groupReader"/></form:option>
          <form:option value="GROUP_EXPERIMENTER"><fmt:message key="select.option.groupExperimenter"/></form:option>
          <form:option value="GROUP_ADMIN"><fmt:message key="select.option.groupAdmin"/></form:option>
        </form:select>

        <form:errors path="userRole" cssClass="error" />
      </div>

      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.addMemberToGroup'/>" class="submitButton lightButtonLink" />
      </div>

    </fieldset>
  </form:form>
</ui:groupsTemplate>