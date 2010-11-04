<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:myAccountTemplate pageTitle="pageTitle.changePassword">

    <h1><fmt:message key='pageTitle.changePassword'/></h1>
    
    <c:url value="change-password.html" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="myAccount" cssClass="standardInputForm">
        
        <fieldset>

            <div class="itemBox">
                <form:label path="oldPassword" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='page.myAccount.currentPassword'/></form:label>

                <form:password path="oldPassword" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />

                <form:errors path="oldPassword" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="newPassword" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='page.myAccount.newPassword'/></form:label>

                <form:password path="newPassword" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />

                <form:errors path="newPassword" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="newPassword2" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key='page.myAccount.newPasswordAgain'/></form:label>

                <form:password path="newPassword2" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />

                <form:errors path="newPassword2" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='page.myAccount.changePasswordButton'/>" class="submitButton lightButtonLink" />
            </div>


        </fieldset>
    </form:form>

</ui:myAccountTemplate>
