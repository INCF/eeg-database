<%--
  Created by IntelliJ IDEA.
  User: pbruha
  Date: 10.3.11
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<ui:experimentsTemplate pageTitle="pageTitle.addEditExperiment" addExperimentCalendar="false">

    <h1><fmt:message key="pageTitle.addExperiment"/></h1>

    <form:form method="post" commandName="addExperimentWizard" cssClass="standardInputForm" name="addExperimentWizard"
               enctype="multipart/form-data">
        <input type="hidden" name="param"/>
        <fieldset>
            <div class="itemBox">
                <form:label path="scenario" cssClass="selectBoxLabel"
                            cssErrorClass="selectBoxLabel errorLabel">
                    <fmt:message key="label.scenario"/>
                </form:label>

                <form:select path="scenario" cssClass="selectBox">
                <form:option value="-1">
                    <fmt:message key="select.option.noScenarioSelected"/>
                </form:option>
                <c:forEach items="${scenarioList}" var="scenario">
                    <form:option value="${scenario.scenarioId}" label="${scenario.title}"></form:option>
                </c:forEach>
                </form:select>


                    <form:errors path="scenario" cssClass="errorBox"/>

                <div class="itemBox">
                    <form:label path="hardware" cssClass="multipleSelectBoxLabel"
                                cssErrorClass="multipleSelectBoxLabel errorLabel"><fmt:message
                            key="label.hardware"/></form:label>

                    <form:select path="hardware" multiple="multiple" cssClass="multipleSelectBox">
                        <c:forEach items="${hardwareList}" var="hardware">
                            <form:option value="${hardware.hardwareId}" label="${hardware.title}"></form:option>
                        </c:forEach>
                    </form:select>

                    <form:errors path="hardware" cssClass="errorBox"/>
                </div>

                <div class="itemBox">
                    <form:label path="weather" cssClass="selectBoxLabel"
                                cssErrorClass="selectBoxLabel errorLabel"><fmt:message
                            key="label.weather"/></form:label>

                    <form:select path="weather" id="selectWeather"  multiple="false" cssClass="selectBox">
                        <option selected="-1" value="-1"><fmt:message key="select.option.noWeatherSelected"/></option>
                        <c:forEach items="${weatherList}" var="weather">
                            <option value="${weather.weatherId}">${weather.title}</option>
                        </c:forEach>
                    </form:select>
                    <input type="button" id="create-weather" value="<fmt:message key="button.addWeatherDefinition"/>">

                    <form:errors path="weather" cssClass="errorBox"/>
                </div>

                <div id="dialog-form" title="Create new weather">
                    <p class="validateTips">All form fields are required.</p>

                    <form>
                    <fieldset>
                        <label for="weatherTitle">Title</label>
                        <input type="text" name="weatherTitle" id="weatherTitle" class="text ui-widget-content ui-corner-all" />
                        <label for="weatherDescription">Description</label>
                        <input type="text" name="weatherDescription" id="weatherDescription" class="text ui-widget-content ui-corner-all" />
                    </fieldset>
                    </form>
                </div>





                    <div class="itemBox">
                        <form:label path="weatherNote" cssClass="textFieldLabel"
                                    cssErrorClass="textFieldLabel errorLabel"><fmt:message
                                key="label.weatherNote"/></form:label>

                        <form:input path="weatherNote" cssClass="textField" cssErrorClass="textField errorField"/>

                        <form:errors path="weatherNote" cssClass="errorBox"/>
                    </div>

                    <div class="itemBox">
                        <form:label path="temperature" cssClass="textFieldLabel"
                                    cssErrorClass="textFieldLabel errorLabel"><fmt:message
                                key="label.temperature"/></form:label>

                        <form:input path="temperature" cssClass="textField" cssErrorClass="textField errorField"/> °C

                        <form:errors path="temperature" cssClass="errorBox"/>
                    </div>
                    <div class="itemBox">

                        <form:label path="privateNote" cssClass="textFieldLabel"
                                    cssErrorClass="textFieldLabel errorLabel"><fmt:message
                                key="label.private"/></form:label>

                        <form:checkbox path="privateNote" cssClass="checkBox"
                                       value="${addExperimentWizard.privateNote}"/>

                        <form:errors path="privateNote" cssClass="errorBox"/>
                    </div>
                </div>


                <div class="actionBox">
                    <input type="submit" value="Previous" name="_target0" class="submitButton lightButtonLink"/>
                    <input type="submit" value="Next" name="_target2" class="lightButtonLink"/>
                    <input type="submit" value="Cancel" name="_cancel" class="lightButtonLink"/>
                </div>




        </fieldset>
    </form:form>
    </ui:experimentsTemplate>