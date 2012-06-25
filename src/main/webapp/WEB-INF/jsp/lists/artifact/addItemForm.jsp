<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="submitDisable">
  <fmt:message key='button.submitDisable'/>
</c:set>

<ui:listsTemplate pageTitle="pageTitle.addEditArtifactDefinition">
     <c:choose>
        <c:when test="${addArtifact.id > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.addEditArtifactDefinition"/> </h1>

            <c:url value="edit.html?id=${addArtifact.id}&groupid=${addArtifact.researchGroupId}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addArtifactDefinition"/></h1>

            <c:url value="add.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>

    <form:form action="${formUrl}" method="post" commandName="addArtifact" cssClass="standardInputForm" onsubmit='document.getElementById("saveButton").disabled=true;document.getElementById("saveButton").value="${submitDisable}";'>


        <fieldset>
            <div class="itemBox">
                <form:label path="researchGroupTitle" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>
                <form:input path="researchGroupTitle" cssClass="textField" value="${addArtifact.researchGroupTitle}" disabled="true" maxlength="50" />
            </div>
            <div class="itemBox">
                <form:label path="compensation" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.compensation"/></form:label>

                <form:input path="compensation" cssClass="textField" cssErrorClass="textField errorField" maxlength="50" />

                <form:errors path="compensation" cssClass="errorBox" />
            </div>

            <div class="itemBox">
                <form:label path="rejectCondition" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.rejectCondition"/></form:label>

                <form:input path="rejectCondition" cssClass="textField" cssErrorClass="textField errorField" maxlength="30" />

                <form:errors path="rejectCondition" cssClass="errorBox" />
            </div>


            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" id="saveButton" />
            </div>

        </fieldset>
    </form:form>
</ui:listsTemplate>
