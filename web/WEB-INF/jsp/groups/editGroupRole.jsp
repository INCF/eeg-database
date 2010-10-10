<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:groupsTemplate pageTitle="pageTitle.groupRoleEdit">
  <h1><fmt:message key="pageTitle.groupRoleEdit"/></h1>
  <c:url value="/groups/edit-group-role.html" var="formUrl" />

  <form:form action="${formUrl}" method="post" commandName="editGroupRoleCommand" name="editGroupRoleCommand" cssClass="standardInputForm">
    <fieldset>
      <form:label path="editedGroup" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='label.chooseGroup'/></form:label>
      <form:select path="editedGroup" cssClass="selectBox">
        <form:option value="-1"><fmt:message key="select.option.noResearchGroupSelected"/></form:option>
        <c:forEach items="${researchGroupList}" var="researchGroup">
          <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
             <c:out value="${researchGroup.title}" />
          </option>
        </c:forEach>
      </form:select>

      <div class="itemBox">
        <form:label path="editedGroup" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='text.experimenter'/></form:label>
        <input type="radio" name="role"  value="GROUP_EXPERIMENTER" <c:if test="${status.value == true}">checked</c:if>/>
      </div>
      <div class="itemBox">
        <form:label path="editedGroup" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='select.option.roleAdmin'/></form:label>
        <input type="radio" name="role"  value="GROUP_ADMIN" <c:if test="${status.value == true}">checked</c:if>/>
      </div>

    
    

     <div class="itemBox">
       <input type="submit" value="<fmt:message key='button.sendRequest'/>" class="submitButton lightButtonLink" />
     </div>
    </fieldset>
  </form:form>
  </ui:groupsTemplate>
