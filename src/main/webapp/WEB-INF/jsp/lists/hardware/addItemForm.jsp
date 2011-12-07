<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="submitDisable">
  <fmt:message key='button.submitDisable'/>
</c:set>

<ui:listsTemplate pageTitle="pageTitle.addEditHardwareDefinition">
     <c:choose>
        <c:when test="${addHardware.id > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.editHardwareDefinition"/> </h1>

            <c:url value="edit.html?id=${addHardware.id}&groupid=${addHardware.researchGroupId}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addHardwareDefinition"/></h1>

            <c:url value="add.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>

    <form:form action="${formUrl}" method="post" commandName="addHardware" cssClass="standardInputForm" onsubmit='document.getElementById("saveButton").disabled=true;document.getElementById("saveButton").value="${submitDisable}";'>


        <fieldset>
            <div class="itemBox">
                <form:label path="researchGroupTitle" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>
                <form:input path="researchGroupTitle" cssClass="textField" value="${addHardware.researchGroupTitle}" disabled="true" maxlength="50" />
            </div>
            <div class="itemBox">
                <form:label path="title" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.title"/></form:label>

                <form:input path="title" cssClass="textField" cssErrorClass="textField errorField" maxlength="50" />

                <form:errors path="title" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="type" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.type"/></form:label>

                <form:input path="type" cssClass="textField" cssErrorClass="textField errorField" maxlength="30" />

                <form:errors path="type" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="description" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.description"/></form:label>

                <form:input path="description" cssClass="textField" cssErrorClass="textField errorField" maxlength="30" />

                <form:errors path="description" cssClass="errorBox" />
            </div>


            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" id="saveButton" />
            </div>

        </fieldset>
    </form:form>
</ui:listsTemplate>
