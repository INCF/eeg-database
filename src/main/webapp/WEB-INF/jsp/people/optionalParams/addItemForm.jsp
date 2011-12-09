<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="submitDisable">
  <fmt:message key='button.submitDisable'/>
</c:set>

<ui:personsTemplate pageTitle="pageTitle.addPersonOptionalParameter">
    <h1><fmt:message key="pageTitle.addPersonOptionalParameter"/></h1>

    <c:url value="add-optional-parameter.html?personId=${personDetail.personId}" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="addPersonAdditionalParameter" cssClass="standardInputForm" onsubmit='document.getElementById("saveButton").disabled=true;document.getElementById("saveButton").value="${submitDisable}";'>
        <fieldset>

            <input type="hidden" name="personFormId" value="${personDetail.personId}" />

            <div class="itemBox">
                <form:label path="paramId" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.parameterType"/></form:label>

                <form:select path="paramId" cssClass="selectBox">
                    <form:option value="-1"><fmt:message key='select.option.noParameterTypeSelected'/></form:option>
                    <c:forEach items="${personAdditionalParams}" var="parameter">
                        <form:option value="${parameter.personOptParamDefId}">${parameter.paramName}</form:option>
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
                <input type="submit" value="<fmt:message key='button.addPersonOptionalParameter'/>" class="submitButton lightButtonLink" id="saveButton"/>
            </div>

        </fieldset>
    </form:form>
</ui:personsTemplate>