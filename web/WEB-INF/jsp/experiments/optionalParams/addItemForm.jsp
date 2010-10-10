<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>

<ui:experimentsTemplate pageTitle="pageTitle.addExperimentOptionalParameter">
  <h1><fmt:message key="pageTitle.addExperimentOptionalParameter"/></h1>

  <c:url value="/experiments/add-optional-parameter.html?experimentId=${measurationDetail.experimentId}" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="addMeasurationAdditionalParameter" cssClass="standardInputForm">
    <fieldset>

      <f:errorBox/>

      <input type="hidden" name="measurationFormId" value="${measurationDetail.experimentId}" />

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
        <input type="submit" value="<fmt:message key='button.addMeasurationOptionalParameter'/>" class="submitButton lightButtonLink" />
      </div>

    </fieldset>
  </form:form>
</ui:experimentsTemplate>
