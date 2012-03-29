<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:groupsTemplate pageTitle="pageTitle.acceptRoleRequest">
  <h1><fmt:message key="pageTitle.acceptRoleRequest"/></h1>
  <c:url value="accept-role-request.html" var="formUrl" />



  <form:form action="${formUrl}" method="post" commandName="editGroupRoleCommand" name="editGroupRoleCommand" cssClass="standardInputForm">
    <fieldset>
       <div class="itemBox">
      <form:label path="user" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='label.email'/></form:label>
      <c:out value="${person.username}" />
      </div>

      <div class="itemBox">
       <form:label path="group" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='text.choosenGroup'/></form:label>
      <c:out value="${choosenGroup}" />
        </div>

      <div class="itemBox">
        <form:label path="editedGroup" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='text.experimenter'/></form:label>
        <input type="radio" name="role"  value="GROUP_EXPERIMENTER" <c:if test="${requestRole == 'GROUP_EXPERIMENTER'}">checked</c:if>/>
      </div>
      <div class="itemBox">
        <form:label path="editedGroup" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='select.option.roleAdmin'/></form:label>
        <input type="radio" name="role"  value="GROUP_ADMIN" <c:if test="${requestRole == 'GROUP_ADMIN'}">checked</c:if>/>
      </div>




     <div class="itemBox">
       <input type="submit" value="<fmt:message key='button.confirm'/>" class="submitButton lightButtonLink" />
     </div>

 
    </fieldset>
  </form:form>

</ui:groupsTemplate>




