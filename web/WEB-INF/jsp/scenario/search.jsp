<%--
    Document   : search
    Created on : 18.11.2009, 8:50:32
    Author     : Meda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<ui:scenariosTemplate pageTitle="pageTitle.advancedSearch" >
  <c:url value="search-results.html" var="formUrl"/>
  <h1><fmt:message key="pageTitle.advancedSearch"/></h1>
  <h2><fmt:message key="heading.searchFor"/></h2>
  <form action="${formUrl}" method="post" class="standardInputForm" name="searchScenario">
    <table class="formTable">
      <tr class="1">
        <td class="dummySearchColumn"></td>
        <td><input type="text" name="condition_1" class="condition"/></td>
        <td>&nbsp;<fmt:message key="label.in"/>&nbsp;</td>
        <td>
          <select name="source_1">
            <option value="title" selected><fmt:message key="form.option.scenarioTitle"/></option>
            <option value="minScenarioLength"><fmt:message key="form.option.minLength"/></option>
            <option value="maxScenarioLength"><fmt:message key="form.option.maxLength"/></option>
            <option value="person"><fmt:message key="label.author"/></option>
          </select>
        </td>
        <td>&nbsp;</td>
      </tr>
    </table>

    <a class="addNext" id="scenario"><fmt:message key="label.addAnotherField"/></a>
    <br /><br />
    <input type="submit" value="<fmt:message key="button.search"/>"/>
    <input type="reset" value="<fmt:message key="button.reset"/>"/>

  </form>
</ui:scenariosTemplate>


