<%-- 
    Document   : Advanced search page
    Created on : 9.4.2010, 12:59
    Author     : Petr Bruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:experimentsTemplate pageTitle="pageTitle.advancedSearch" addExperimentCalendar="true">
  <c:url value="/experiments/search-results.html" var="formUrl"/>
  <h1><fmt:message key="pageTitle.advancedSearch"/></h1>

 <form:form action="${formUrl}" method="post" commandName="experimentsSearcherCommand" cssClass="standardInputForm" name="searchMeasuration">
    <table class="formTable">
      <span class="note"><fmt:message key="form.note.dateFormatDDMMYYYYHHMM"/></span>
      <tr class="1">
        <td class="dummySearchColumn"></td>
        <td><input type="text" name="condition_1" class="condition"/></td>
        <td>&nbsp;<fmt:message key="label.in"/>&nbsp;</td>
        <td>
          <select name="source_1">
            <option value="startTime"><fmt:message key="label.startDateTime"/></option>
            <option value="endTime"><fmt:message key="label.endDateTime"/></option>
            <option value="usedHardware"><fmt:message key="label.usedHardware"/></option>
            <option value="scenario.title"><fmt:message key="label.scenarioTitle"/></option>
            <option value="personBySubjectPersonId.gender"><fmt:message key="label.gender"/></option>
            <option value="weather.title"><fmt:message key="label.weather"/></option>
            <option value="ageMin"><fmt:message key="label.ageMin"/></option>
            <option value="ageMax"><fmt:message key="label.ageMax"/></option>
          </select>
        </td>
        <td>&nbsp;</td>
      </tr>
    </table>

    <a class="addNext" id="experiment"><fmt:message key="label.addAnotherField"/></a>
    <br /><br />
    <input type="submit" value="<fmt:message key="button.search"/>"/>
    <input type="reset" value="<fmt:message key="button.reset"/>"/>
 </form:form>
 
</ui:experimentsTemplate>
