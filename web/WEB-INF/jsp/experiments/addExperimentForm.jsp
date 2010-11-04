<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:experimentsTemplate pageTitle="pageTitle.addEditExperiment" addExperimentCalendar="true">

  <c:choose>
    <c:when test="${addMeasuration.measurationId > 0}">
      <!-- editation -->
      <h1><fmt:message key="pageTitle.editExperiment"/></h1>

      <c:url value="/edit.html?id=${addMeasuration.measurationId}" var="formUrl"/>
    </c:when>
    <c:otherwise>
      <!-- creating new -->
      <h1><fmt:message key="pageTitle.addExperiment"/></h1>

      <c:url value="/add-experiment.html" var="formUrl"/>
    </c:otherwise>
  </c:choose>

  <form:form action="${formUrl}" method="post" commandName="addMeasuration" cssClass="standardInputForm" name="addMeasuration">
    <fieldset>

      <form:hidden path="measurationId" />

      <c:if test="${addMeasuration.measurationId <= 0}">
        <!-- It is edit of existing experiment - we don't want to change the research group -->
        <div class="itemBox">
          <form:label path="researchGroup" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>

          <form:select path="researchGroup" cssClass="selectBox">
            <form:option value="-1"><fmt:message key="select.option.noResearchGroupSelected"/></form:option>
            <c:forEach items="${researchGroupList}" var="researchGroup">
              <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
                 <c:out value="${researchGroup.title}" />
              </option>
            </c:forEach>
          </form:select>

          <form:errors path="researchGroup" cssClass="errorBox" />
        </div>
      </c:if>

      <div class="itemBox">
        <label class="textFieldLabel"><fmt:message key="label.startDateTime"/></label>
        <form:input path="startDate" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />
        <form:input path="startTime" cssClass="textField timeField" maxlength="5" cssErrorClass="textField timeField errorField" />
        <span class="note"><fmt:message key="form.note.timeFormatHHMM"/></span>

        <form:errors path="startDate" cssClass="errorBox" />
        <form:errors path="startTime" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <label class="textFieldLabel"><fmt:message key="label.endDateTime"/></label>
        <form:input path="endDate" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />
        <form:input path="endTime" cssClass="textField timeField" maxlength="5" cssErrorClass="textField timeField errorField" />
        <span class="note"><fmt:message key="form.note.timeFormatHHMM"/></span>

        <form:errors path="endDate" cssClass="errorBox" />
        <form:errors path="endTime" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="subjectPerson" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.subjectPerson"/></form:label>

        <form:select path="subjectPerson" cssClass="selectBox">
          <form:option value="-1"><fmt:message key="select.option.noSubjectPersonSelected"/></form:option>
          <c:forEach items="${personList}" var="person">
            <form:option value="${person.personId}" label="${person.givenname} ${person.surname}"></form:option>
          </c:forEach>
        </form:select>

        <form:errors path="subjectPerson" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="coExperimenters" cssClass="multipleSelectBoxLabel" cssErrorClass="multipleSelectBoxLabel errorLabel"><fmt:message key="label.coExperimenters"/></form:label>

        <form:select path="coExperimenters" multiple="multiple" cssClass="multipleSelectBox">
          <c:forEach items="${personList}" var="person">
            <form:option value="${person.personId}" label="${person.givenname} ${person.surname}"></form:option>
          </c:forEach>
        </form:select>

        <form:errors path="coExperimenters" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="scenario" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.scenario"/></form:label>

        <form:select path="scenario" cssClass="selectBox">
          <form:option value="-1"><fmt:message key="select.option.noScenarioSelected"/></form:option>
          <c:forEach items="${scenarioList}" var="scenario">
            <form:option value="${scenario.scenarioId}" label="${scenario.title}"></form:option>
          </c:forEach>
        </form:select>

        <form:errors path="scenario" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="hardware" cssClass="multipleSelectBoxLabel" cssErrorClass="multipleSelectBoxLabel errorLabel"><fmt:message key="label.hardware"/></form:label>

        <form:select path="hardware" multiple="multiple" cssClass="multipleSelectBox">
          <c:forEach items="${hardwareList}" var="hardware">
            <form:option value="${hardware.hardwareId}" label="${hardware.title}"></form:option>
          </c:forEach>
        </form:select>

        <form:errors path="hardware" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="weather" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.weather"/></form:label>

        <form:select path="weather" cssClass="selectBox">
          <form:option value="-1"><fmt:message key="select.option.noWeatherSelected"/></form:option>
          <c:forEach items="${weatherList}" var="weather">
            <form:option value="${weather.weatherId}" label="${weather.title}"></form:option>
          </c:forEach>
        </form:select>

        <form:errors path="weather" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="weatherNote" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.weatherNote"/></form:label>

        <form:input path="weatherNote" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="weatherNote" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="temperature" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.temperature"/></form:label>

        <form:input path="temperature" cssClass="textField" cssErrorClass="textField errorField" /> °C

        <form:errors path="temperature" cssClass="errorBox" />
      </div>
      <div class="itemBox">

        <form:label path="privateNote" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.private"/></form:label>

        <form:checkbox path="privateNote" cssClass="checkBox" value="${addMeasuration.privateNote}"/>

        <form:errors path="privateNote" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" />
      </div>

    </fieldset>
  </form:form>
</ui:experimentsTemplate>