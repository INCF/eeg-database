<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:standardTemplate pageTitle="pageTitle.forgottenPassword">

    <h1><fmt:message key='pageTitle.forgottenPassword'/></h1>

    <c:url value="forgotten-password.html" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="forgottenPassword" cssClass="standardInputForm">
        <fieldset>

            <div class="itemBox">
                <form:label path="username" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='label.email'/></form:label>

                <form:input path="username" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />

                <form:errors path="username" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.send'/>" class="submitButton lightButtonLink" />
            </div>

            <p><fmt:message key="text.forgottenPassword.passwordWillBeSent"/></p>

        </fieldset>
    </form:form>

</ui:standardTemplate>
