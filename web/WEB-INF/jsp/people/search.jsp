<%-- 
    Document   : search
    Created on : 22.04.2010, 17:43
    Author     : Jiri Vlasimsky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:personsTemplate pageTitle="pageTitle.advancedSearch">
  <h1><fmt:message key="pageTitle.advancedSearch"/></h1>
  <c:url value="/people/search-results.html" var="formUrl"/>

  <form:form action="${formUrl}" method="post" commandName="peopleSearcherCommand" cssClass="standardInputForm" name="searchPeople">
    <table class="formTable">
      <tr class="1">
        <td class="dummySearchColumn"></td>
        <td><input type="text" name="condition_1" class="condition"/></td>
        <td>&nbsp;<fmt:message key="label.in"/>&nbsp;</td>
        <td>
          <select name="source_1">
            <option value="givenname"><fmt:message key="label.givenname"/></option>
            <option value="surname"><fmt:message key="label.surname"/></option>
            <option value="email"><fmt:message key="label.email"/></option>
            <option value="gender"><fmt:message key="label.gender"/></option>
            <option value="ageMin"><fmt:message key="label.ageMin"/></option>
            <option value="ageMax"><fmt:message key="label.ageMax"/></option>
            <option value="defect"><fmt:message key="label.defect"/></option>
          </select>
        </td>
        <td>&nbsp;</td>
      </tr>
    </table>
    <a class="addNext" id="people"><fmt:message key="label.addAnotherField"/></a>
    <br /><br />
    <input type="submit" value="<fmt:message key="button.search"/>"/>
    <input type="reset" value="<fmt:message key="button.reset"/>"/>
  </form:form>

</ui:personsTemplate>










































<%-- <form action="search">
  <fieldset class="fieldset" >
    <legend class="legend"><fmt:message key="legend.advancedSearchPerson"/></legend>
    <table>
      <tbody>

          <tr>
            <td><p><select name="Select1">
                  <option><fmt:message key="option.And"/></option>
                  <option><fmt:message key="option.Or"/></option>
                  <option><fmt:message key="option.None"/></option>
                </select><fmt:message key="label.age"/></p></td>
            <td><p><label><fmt:message key="label.From"/></label></p>
              <p><label><fmt:message key="label.To"/></label></p>
            </td>
            <td>
              <p><input type="text" name="date1" value="" id="d1"></p>
              <p><input type="text" name="date2" value="" id="d2"></p>
            </td>
          </tr>
          <tr>

            <td><p><select name="Select2">
                 <option><fmt:message key="option.And"/></option>
                  <option><fmt:message key="option.Or"/></option>
                  <option><fmt:message key="option.None"/></option>
                </select><fmt:message key="label.givenname"/></p></td>
            <td></td>
            <td><input name="Text1" type="text" ></td>
          </tr>
          <tr>

            <td><p><select name="Select3">
                 <option><fmt:message key="option.And"/></option>
                  <option><fmt:message key="option.Or"/></option>
                  <option><fmt:message key="option.None"/></option>
                </select><fmt:message key="dataTable.heading.surname"/></p></td>
            <td></td>
            <td><input name="Text1" type="text" ></td>
          </tr>

          <tr>

            <td><p><select name="Select4">
                 <option><fmt:message key="option.And"/></option>
                  <option><fmt:message key="option.Or"/></option>
                  <option><fmt:message key="option.None"/></option>
                </select><fmt:message key="label.email"/></p></td>
            <td></td>
            <td>
              <input name="Text2" type="text" ></td>
          </tr>
          <tr>
            <td><p><select name="Select5">
                  <option><fmt:message key="option.And"/></option>
                  <option><fmt:message key="option.Or"/></option>
                  <option><fmt:message key="option.None"/></option>
                </select><fmt:message key="label.Gender"/></p></td>
            <td></td>
            <td><p><input name="Radio1" type="radio" ><label id="Label1"><fmt:message key="label.gender.male"/></label></p>
              <p><input name="Radio1" type="radio" ><label id="Label1"><fmt:message key="label.gender.female"/></label></p>



            </td>
          </tr>
          <tr>

            <td><p><select name="Select6">
                 <option><fmt:message key="option.And"/></option>
                 <option><fmt:message key="option.Or"/></option>
                 <option><fmt:message key="option.None"/></option>
                </select><fmt:message key="label.defect"/></p></td>
            <td></td>
            <td>&nbsp;</td>
          </tr>

          <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><input type="submit" value="<fmt:message key="button.advancedSearch"/>" name="searchBT" class="searchB" ></td>

          </tr>
        </tbody>
      </table>



    </fieldset>
  </form>--%>


