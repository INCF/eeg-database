<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="af" tagdir="/WEB-INF/tags/autoforms/" %>

<ui:groupsTemplate pageTitle="pageTitle.createEditGroup">
    <c:choose>
        <c:when test="${createGroup.id > 0}">
            <!-- editing -->
            <h1><fmt:message key="pageTitle.editGroup"/></h1>

            <c:url value="edit.html?id=${createGroup.id}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.createGroup"/></h1>

            <c:url value="create-group.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>

    <form:form action="${formUrl}" method="post" commandName="createGroup" cssClass="standardForm">
        <fieldset>

            <div class="textBox">

                <form:label path="researchGroupTitle" cssErrorClass="error"><fmt:message
                        key="label.researchGroupTitle"/></form:label>

                <form:input path="researchGroupTitle" cssErrorClass="error"/>

                <form:errors path="researchGroupTitle" cssClass="error"/>
            </div>

            <div class="textareaBox">

                <form:label path="researchGroupDescription" cssErrorClass="error"><fmt:message
                        key="label.researchGroupDescription"/></form:label>

                <form:textarea path="researchGroupDescription" cssErrorClass="error"/>

                <form:errors path="researchGroupDescription" cssClass="error"/>
            </div>

            <div class="submitBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink"/>
            </div>

        </fieldset>
    </form:form>
</ui:groupsTemplate>