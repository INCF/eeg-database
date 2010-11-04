<%-- 
    Document   : search
    Created on : 28.9.2010, 14:33:06
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:historyTemplate pageTitle="pageTitle.advancedSearch">
  <c:url value="search-results.html" var="formUrl"/>
  <h1><fmt:message key="pageTitle.advancedSearch"/></h1>

 <form:form action="${formUrl}" method="post" commandName="historySearcherCommand" cssClass="standardInputForm" name="searchMeasuration">
    <table class="formTable">
      <tr class="1">
        <td class="dummySearchColumn"></td>
        <td><input type="text" name="condition_1" class="condition"/></td>
        <td>&nbsp;<fmt:message key="label.in"/>&nbsp;</td>
        <td>
          <select name="source_1">
            <option value="scenario.title"><fmt:message key="label.scenarioTitle"/></option>
            <option value="fromDateOfDownload"><fmt:message key="label.startDate"/></option>
            <option value="toDateOfDownload"><fmt:message key="label.endDate"/></option>
            
          </select>
        </td>
        <td>&nbsp;</td>
      </tr>
    </table>

    <a class="addNext" id="historySearch"><fmt:message key="label.addAnotherField"/></a>
    <br /><br />
    <input type="submit" value="<fmt:message key="button.search"/>"/>
    <input type="reset" value="<fmt:message key="button.reset"/>"/>
 </form:form>

</ui:historyTemplate>