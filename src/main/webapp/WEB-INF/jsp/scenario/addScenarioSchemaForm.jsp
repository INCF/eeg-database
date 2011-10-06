<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:scenariosTemplate pageTitle="pageTitle.addEditScenarioSchema">
    <c:choose>
        <c:when test="${addScenarioSchema.id > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.editScenarioSchema"/></h1>

            <c:url value="/scenarios/edit.html?id=${addScenario.id}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addScenarioSchema"/></h1>

            <c:url value="add-scenario-schema.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>


    <form:form action="${formUrl}" method="post" commandName="addScenarioSchema" cssClass="standardInputForm"
               enctype="multipart/form-data">
        <fieldset>

            <div class="itemBox">
                <form:label path="schemaDescription" cssClass="textareaFieldLabel"
                            cssErrorClass="textareaFieldLabel errorLabel"><fmt:message key="label.scenarioSchemaDescription"/></form:label>

                <form:textarea path="schemaDescription" cssClass="textareaField" cssErrorClass="textareaField errorField"
                               rows="5" cols="40"/>

                <form:errors path="schemaDescription" cssClass="errorBox"/>
            </div>

            <div class="itemBox" id="schemaFile">
                <form:label path="schemaFile" cssClass="fileFieldLabel"
                            cssErrorClass="fileFieldLabel errorLabel"><fmt:message key="label.xsdDataFile"/></form:label>

                <input type="file" name="schemaFile" class="fileField"/>

                <form:errors path="schemaFile" cssClass="errorBox"/>
            </div>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink"/>
            </div>


        </fieldset>
    </form:form>
</ui:scenariosTemplate>