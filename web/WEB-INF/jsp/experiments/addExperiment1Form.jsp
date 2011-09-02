<%--
  Created by IntelliJ IDEA.
  User: pbruha
  Date: 10.3.11
  Time: 14:07
  To change this template use File | Settings | File Templates.
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:experimentsTemplate pageTitle="pageTitle.addEditExperiment" jspinner="true" addExperimentCalendar="true">

    <c:choose>
        <c:when test="${addExperimentWizard.measurationId > 0}">
            <!-- editation -->
            <h1><fmt:message key="pageTitle.editExperiment"/></h1>

            <c:url value="edit.html?id=${addExperimentWizard.measurationId}" var="formUrl"/>
            <c:url value="http://eegdatabase.kiv.zcu.cz/experiments/add-experiment.html" var="next"/>
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addExperiment"/></h1>
        </c:otherwise>
    </c:choose>
    <!-- -->
    <form:form method="post" commandName="addExperimentWizard" cssClass="standardInputForm"
               name="addExperimentWizard" action="${next}" >
        <fieldset>

            <form:hidden path="measurationId"/>

            <c:if test="${addExperimentWizard.measurationId <= 0}">
                <!-- It is edit of existing experiment - we don't want to change the research group -->
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
            </c:if>
            <div class="itemBox">
                <label class="textFieldLabel"><fmt:message key="label.startDateTime"/></label>
                <form:input path="startDate" cssClass="textField dateField"
                            cssErrorClass="textField dateField errorField" onchange="setDate(this.value)"/>
                <form:input path="startTime" cssClass="textField timeField" maxlength="5"
                            cssErrorClass="textField timeField errorField" onchange="startTimeChange(this.value)"/>

                <form:errors path="startDate" cssClass="errorBox"/>
                <form:errors path="startTime" cssClass="errorBox"/>
                <span class="note"><fmt:message key="form.note.timeFormatHHMM"/></span>

            </div>

            <div class="itemBox">
                <label class="textFieldLabel"><fmt:message key="label.endDateTime"/></label>
                <form:input path="endDate" cssClass="textField dateField"
                            cssErrorClass="textField dateField errorField" onchange="changeEndDate()"/>

                <form:input path="endTime" cssClass="textField timeField" maxlength="5"
                            cssErrorClass="textField timeField errorField"/>

                <form:errors path="endDate" cssClass="errorBox"/>
                <form:errors path="endTime" cssClass="errorBox"/>
                <span class="note"><fmt:message key="form.note.timeFormatHHMM"/></span>
            </div>

            <div class="itemBox">
                <form:label path="subjectPerson" cssClass="selectBoxLabel"
                            cssErrorClass="selectBoxLabel errorLabel"><fmt:message
                        key="label.subjectPerson"/></form:label>

                <form:select path="subjectPerson" id="subjectPerson" cssClass="selectBox">
                    <option value="-1"><fmt:message key="select.option.noSubjectPersonSelected"/></option>
                    <c:forEach items="${personList}" var="person">
                        <option value="${person.personId}">${person.givenname} ${person.surname}</option>
                    </c:forEach>
                </form:select>
                <input type="button" name="new_person" id="create-person"
                       value="<fmt:message key="button.addPerson"/>">
                <form:errors path="subjectPerson" cssClass="errorBox"/>
            </div>

            <div class="itemBox">
                <form:label path="coExperimenters" cssClass="multipleSelectBoxLabel"
                            cssErrorClass="multipleSelectBoxLabel errorLabel"><fmt:message key="label.coExperimenters"/></form:label>

                <form:select path="coExperimenters" multiple="multiple" cssClass="multipleSelectBox">
                    <c:forEach items="${personList}" var="person">
                        <form:option value="${person.personId}"
                                     label="${person.givenname} ${person.surname}"></form:option>
                    </c:forEach>
                </form:select>

                <form:errors path="coExperimenters" cssClass="errorBox"/>
            </div>


            <div class="actionBox">
                <input type="submit" value="Next" name="_target1" class="submitButton lightButtonLink"/>
                <input type="submit" value="Cancel" name="_cancel" class="lightButtonLink"/>
            </div>


        </fieldset>
    </form:form>

    <div id="dialog-form-person" title="Create new person">
        <p class="validateTips">All form fields are required.</p>

        <form id="addPersonForm">
            <fieldset>
                <%--@declare id="gender"--%><%--@declare id="female"--%><%--@declare id="male"--%><label for="givenname"><fmt:message key="label.givenname"/></label>
                <input type="text" name="givenname" id="givenname" class="text ui-widget-content ui-corner-all"/>

                <label for="surname"><fmt:message key="label.surname"/></label>
                <input type="text" name="surname" id="surname" class="text ui-widget-content ui-corner-all"/>

                <label for="dateOfBirth"><fmt:message key="label.dateOfBirth"/></label>
                <input type="text" name="dateOfBirth" id="dateOfBirth" class="text textField dateField ui-widget-content ui-corner-all"/>

                <label for="gender"><fmt:message key="label.gender"/></label>
                <input id="gender" name="gender" type="radio" value="M" checked="checked" class="radio ui-widget-content ui-corner-all">
                <label for="male"><fmt:message key="label.gender.male"/></label>
                <input id="gender" name="gender" type="radio" value="F" class="radio ui-widget-content ui-corner-all">
                <label for="female"><fmt:message key="label.gender.female"/></label>

                <label for="email" class="text email"><fmt:message key="label.email"/></label>
                <input type="text" name="email" id="email" class="text ui-widget-content ui-corner-all"/>

                <label for="phoneNumber"><fmt:message key="label.phoneNumber"/></label>
                <input type="text" name="phoneNumber" id="phoneNumber" class="text ui-widget-content ui-corner-all"/>

                <label for="note"><fmt:message key="label.note"/></label>
                <input type="text" name="note" id="note" class="text ui-widget-content ui-corner-all"/>

            </fieldset>
        </form>
    </div>


</ui:experimentsTemplate>
