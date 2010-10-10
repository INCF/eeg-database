<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:listsTemplate pageTitle="pageTitle.addFileMetadataParameter">
    <h1><fmt:message key="pageTitle.addFileMetadataParameter"/></h1>

    <c:url value="/lists/file-metadata-names/add.html" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="addFileMetadataParam" cssClass="standardInputForm">
        <fieldset>

            <div class="itemBox">
                <form:label path="paramName" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.paramName"/></form:label>

                <form:input path="paramName" cssClass="textField" cssErrorClass="textField errorField" maxlength="30" />

                <form:errors path="paramName" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="paramDataType" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.paramDataType"/></form:label>

                <form:input path="paramDataType" cssClass="textField" cssErrorClass="textField errorField" maxlength="20" />

                <form:errors path="paramDataType" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.addFileMetadataParameter'/>" class="submitButton lightButtonLink" />
            </div>

        </fieldset>
    </form:form>
</ui:listsTemplate>
