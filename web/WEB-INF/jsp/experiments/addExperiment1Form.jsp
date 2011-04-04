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
        </c:when>
        <c:otherwise>
            <!-- creating new -->
            <h1><fmt:message key="pageTitle.addExperiment"/></h1>

            <c:url value="add-experiment.html" var="formUrl"/>
        </c:otherwise>
    </c:choose>
    <!--action="${formUrl}" -->
    <form:form method="post" commandName="addExperimentWizard" cssClass="standardInputForm"
               name="addExperimentWizard">
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

                <form:select path="subjectPerson" cssClass="selectBox">
                    <form:option value="-1"><fmt:message key="select.option.noSubjectPersonSelected"/></form:option>
                    <c:forEach items="${personList}" var="person">
                        <form:option value="${person.personId}"
                                     label="${person.givenname} ${person.surname}"></form:option>
                    </c:forEach>
                </form:select>

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


            <div class="itemBox">
                <input type="submit" value="Next" name="_target1"/>
                <input type="submit" value="Cancel" name="_cancel"/>
            </div>


        </fieldset>
    </form:form>
   

</ui:experimentsTemplate>
