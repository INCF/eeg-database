<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:scenariosTemplate pageTitle="pageTitle.addScenario">
  <h1><fmt:message key="pageTitle.addScenario"/></h1>

  <c:url value="/scenarios/add-scenario.html" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="addScenario" cssClass="standardInputForm" enctype="multipart/form-data">
    <fieldset>

      <div class="itemBox">
        <form:label path="researchGroup" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>

        <form:select path="researchGroup" cssClass="selectBox">
          <form:option value="-1"><fmt:message key="select.option.noResearchGroupSelected"/></form:option>
          <c:forEach items="${researchGroupList}" var="researchGroup">
              <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
                 <c:out value="${researchGroup.title}" />
              </option>
          </c:forEach>
        </form:select>

        <form:errors path="researchGroup" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="title" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.scenarioTitle"/></form:label>

        <form:input path="title" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="title" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="length" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.scenarioLength"/></form:label>

        <form:input path="length" cssClass="textField" cssErrorClass="textField errorField" cssStyle="width: 80px;" /> <fmt:message key="label.minutes"/>

        <form:errors path="length" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="description" cssClass="textareaFieldLabel" cssErrorClass="textareaFieldLabel errorLabel"><fmt:message key="label.scenarioDescription"/></form:label>

        <form:textarea path="description" cssClass="textareaField" cssErrorClass="textareaField errorField" rows="5" cols="40" />

        <form:errors path="description" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="dataFile" cssClass="fileFieldLabel" cssErrorClass="fileFieldLabel errorLabel">Data file</form:label>

        <input type="file" name="dataFile" class="fileField" />

        <form:errors path="dataFile" cssClass="errorBox" />
      </div>
      <div class="itemBox">

        <form:label path="privateNote" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.private"/></form:label>

        <form:checkbox path="privateNote" cssClass="checkBox" />

        <form:errors path="privateNote" cssClass="errorBox" />
      </div>


      <div class="itemBox">
        <input type="submit" value="Save" class="submitButton lightButtonLink" />
      </div>

    </fieldset>
  </form:form>
</ui:scenariosTemplate>