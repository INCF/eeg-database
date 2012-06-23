<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:scenariosTemplate pageTitle="pageTitle.addEditScenario">
    <c:choose>
        <c:when test="${addScenario.id > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.editScenario"/></h1>

            <c:url value="edit.html?id=${addScenario.id}" var="formUrl"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addScenario"/></h1>

            <c:url value="add-scenario.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>


    <form:form action="${formUrl}" method="post" commandName="addScenario" cssClass="standardInputForm"
               enctype="multipart/form-data">
        <fieldset>

            <div class="itemBox">
                <form:label path="researchGroup" cssClass="selectBoxLabel"
                            cssErrorClass="selectBoxLabel errorLabel"><fmt:message
                        key="label.researchGroup"/></form:label>

                <form:select path="researchGroup" cssClass="selectBox">
                    <form:option value="-1"><fmt:message key="select.option.noResearchGroupSelected"/></form:option>
                    <c:forEach items="${researchGroupList}" var="researchGroup">
                        <option value="${researchGroup.researchGroupId}" label="" <c:if
                                test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
                            <c:out value="${researchGroup.title}"/>
                        </option>
                    </c:forEach>
                </form:select>

                <form:errors path="researchGroup" cssClass="errorBox"/>
            </div>

            <div class="itemBox">
                <form:label path="title" cssClass="textFieldLabel"
                            cssErrorClass="textFieldLabel errorLabel"><fmt:message
                        key="label.scenarioTitle"/></form:label>

                <form:input path="title" cssClass="textField" cssErrorClass="textField errorField"/>

                <form:errors path="title" cssClass="errorBox"/>
            </div>

            <div class="itemBox">
                <form:label path="length" cssClass="textFieldLabel"
                            cssErrorClass="textFieldLabel errorLabel"><fmt:message
                        key="label.scenarioLength"/></form:label>

                <form:input path="length" cssClass="textField" cssErrorClass="textField errorField"
                            cssStyle="width: 80px;"/> <fmt:message key="label.minutes"/>

                <form:errors path="length" cssClass="errorBox"/>
            </div>

            <div class="itemBox">
                <form:label path="description" cssClass="textareaFieldLabel"
                            cssErrorClass="textareaFieldLabel errorLabel"><fmt:message key="label.scenarioDescription"/></form:label>

                <form:textarea path="description" cssClass="textareaField" cssErrorClass="textareaField errorField"
                               rows="5" cols="40"/>

                <form:errors path="description" cssClass="errorBox"/>
            </div>

            <div class="itemBox">
                <form:label path="dataFileAvailable" cssClass="fileFieldLabel"
                            cssErrorClass="fileFieldLabel errorLabel"><fmt:message key="description.fileType.dataFileAvailable"/></form:label>

                <%-- <form:checkbox path="dataFileAvailable" cssClass="checkBox" id="dataFileAvailableCheckbox""/> --%>
                <input type="checkbox" name="dataFileAvailable" class="checkBox" checked="true" id="dataFileAvailableCheckbox"/>

                <form:errors path="dataFileAvailable" cssClass="errorBox"/>
            </div>

       <%--     <div class="itemBox" id="scenarioXml">

                <form:label path="xmlFileCheckBox" cssClass="textFieldLabel"
                            cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.dataFileIsXml"/></form:label>

                <form:checkbox path="xmlFileCheckBox" cssClass="checkBox" id="isScenarioXml"/>

                <form:errors path="xmlFileCheckBox" cssClass="errorBox"/>
            </div>             --%>

            <div class="itemBox" id="dataFile">
                <form:label path="dataFile" cssClass="fileFieldLabel"
                            cssErrorClass="fileFieldLabel errorLabel"><fmt:message key="description.fileType.dataFile"/></form:label>

                <input type="file" name="dataFile" class="fileField"/>

                <form:errors path="dataFile" cssClass="errorBox"/>
            </div>

            <div class="itemBox" id="dataFileXml">
                <form:label path="dataFileXml" cssClass="fileFieldLabel"
                            cssErrorClass="fileFieldLabel errorLabel"><fmt:message key="label.xmlDataFile"/></form:label>

                <input type="file" name="dataFileXml" class="fileField"/>

                <form:errors path="dataFileXml" cssClass="errorBox"/>
            </div>

            <div class="itemBox" id="schemaSelect">
                <form:label path="scenarioSchema" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.scenarioSchema"/></form:label>

                <form:radiobutton path="scenarioOption" cssClass="radioButton" value="noSchema" id="noSchema"/>
                <fmt:message key="radio.noSchema"/>

                <form:radiobutton path="scenarioOption" cssClass="radioButton" value="fromList" id="fromSchemaList"/>
                <fmt:message key="radio.fromSchemaList"/>

                <form:select path="scenarioSchema" cssClass="selectBox"  id="schemaList">
                    <form:option value="0"><fmt:message key="select.option.noScenarioSchemaSelected"/></form:option>
                    <c:forEach items="${schemaNamesList}" var="schemaName">
                        <option value="${schemaName.schemaId}" label="" id="schema${schemaName.schemaId}">
                            <c:out value="${schemaName.schemaName}" />
                        </option>
                    </c:forEach>
                </form:select>
                <form:errors path="scenarioSchema" cssClass="errorBox" />
            </div>

            <%--
            <div class="itemBox" id="schemaDescription">
                 --%>



            <div class="itemBox">

                <form:label path="privateNote" cssClass="textFieldLabel"
                            cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.private"/></form:label>

                <form:checkbox path="privateNote" cssClass="checkBox"/>

                <form:errors path="privateNote" cssClass="errorBox"/>
            </div>


            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink"/>
            </div>


        </fieldset>
    </form:form>
</ui:scenariosTemplate>