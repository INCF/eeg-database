<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>

<c:set var="submitDisable">
  <fmt:message key='button.submitDisable'/>
</c:set>

<ui:experimentsTemplate pageTitle="pageTitle.dataFileDetail">
  <h1><fmt:message key="pageTitle.dataFileDetail"/></h1>

  <c:url value="add-optional-parameter.html?experimentId=${measurationDetail.experimentId}" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="addMeasurationAdditionalParameter" cssClass="standardInputForm" onsubmit='document.getElementById("saveButton").disabled=true;document.getElementById("saveButton").value="${submitDisable}";'>
    <fieldset>

      <f:errorBox/>

      <input type="hidden" name="measurationFormId" value="${measurationDetail.experimentId}" />
      <div class="itemBox">
        <form:label path="researchGroupTitle" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>
        <form:input path="researchGroupTitle" cssClass="textField" value="${researchGroupTitle}" disabled="true" maxlength="50" />
      </div>
      <div class="itemBox">
        <form:label path="paramId" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.parameterType"/></form:label>

        <form:select path="paramId" cssClass="selectBox">
          <form:option value="-1"><fmt:message key='select.option.noParameterTypeSelected'/></form:option>
          <c:forEach items="${measurationAdditionalParams}" var="parameter">
            <form:option value="${parameter.experimentOptParamDefId}">${parameter.paramName}</form:option>
          </c:forEach>
        </form:select>

        <form:errors path="paramId" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="paramValue" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.parameterValue"/></form:label>

        <form:input path="paramValue" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="paramValue" cssClass="errorBox" />
      </div>


      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.addExperimentOptionalParameter'/>" class="submitButton lightButtonLink" id="saveButton"/>
      </div>

    </fieldset>
  </form:form>
</ui:experimentsTemplate>
