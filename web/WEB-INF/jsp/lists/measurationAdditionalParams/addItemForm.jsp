<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:listsTemplate pageTitle="pageTitle.addEditExperimentOptionalParameterDefinition">
   <c:choose>
        <c:when test="${addMeasurationAdditionalParam.id > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.editExperimentOptionalParameterDefinition"/></h1>

            <c:url value="edit.html?id=${addMeasurationAdditionalParam.id}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addExperimentOptionalParameter"/></h1>

            <c:url value="add.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>

    <form:form action="${formUrl}" method="post" commandName="addMeasurationAdditionalParam" cssClass="standardInputForm">
        <fieldset>

            <div class="itemBox">
                <form:label path="paramName" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.parameterName"/></form:label>

                <form:input path="paramName" cssClass="textField" cssErrorClass="textField errorField" maxlength="30" />

                <form:errors path="paramName" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="paramDataType" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.parameterDataType"/></form:label>

                <form:input path="paramDataType" cssClass="textField" cssErrorClass="textField errorField" maxlength="20" />

                <form:errors path="paramDataType" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" />
            </div>

        </fieldset>
    </form:form>
</ui:listsTemplate>
