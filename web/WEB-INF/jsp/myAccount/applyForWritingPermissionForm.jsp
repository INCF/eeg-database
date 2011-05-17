<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:myAccountTemplate pageTitle="pageTitle.applyForWritingPermission">

    <h1><fmt:message key='pageTitle.applyForWritingPermission'/></h1>

    <c:url value="apply-for-writing-permission.html" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="applyForWritingPermission" cssClass="standardInputForm">

        <fieldset>

            <p>
                <fmt:message key='text.myAccount.writingPermissionReason'/>
            </p>
            
            <form:input path="reason" cssClass="textField" cssErrorClass="textField errorField" cssStyle="width: 500px;" />

            <form:errors path="reason" cssClass="errorLabel" element="div"/>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.applyForWritingPermission'/>" class="lightButtonLink" />
            </div>


        </fieldset>
    </form:form>

</ui:myAccountTemplate>
