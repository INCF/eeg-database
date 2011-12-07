<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="submitDisable">
  <fmt:message key='button.submitDisable'/>
</c:set>

<ui:listsTemplate pageTitle="pageTitle.addEditVisualImpairment">
    <c:choose>
        <c:when test="${addVisualImpairment.visualImpairmentId > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.editVisualImpairment"/></h1>

            <c:url value="edit.html?id=${addVisualImpairment.visualImpairmentId}&groupid=${addVisualImpairment.researchGroupId}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addVisualImpairment"/></h1>

            <c:url value="add.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>

    <form:form action="${formUrl}" method="post" commandName="addVisualImpairment" cssClass="standardInputForm" onsubmit='document.getElementById("saveButton").disabled=true;document.getElementById("saveButton").value="${submitDisable}";'>
        <fieldset>
            <div class="itemBox">
                <form:label path="researchGroupTitle" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>
                <form:input path="researchGroupTitle" cssClass="textField" value="${addVisualImpairment.researchGroupTitle}" disabled="true" maxlength="50" />
            </div>
            <form:hidden path="visualImpairmentId"/>

            <div class="itemBox">
                <form:label path="description" cssClass="textFieldLabel"
                            cssErrorClass="textFieldLabel errorLabel"><fmt:message
                        key="label.description"/></form:label>

                <form:input path="description" cssClass="textField" cssErrorClass="textField errorField"/>

                <form:errors path="description" cssClass="errorBox"/>
            </div>


            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" id="saveButton" />
            </div>

        </fieldset>
    </form:form>
</ui:listsTemplate>
