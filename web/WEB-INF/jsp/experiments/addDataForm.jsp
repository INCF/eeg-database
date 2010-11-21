<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<ui:experimentsTemplate pageTitle="pageTitle.addDataFile">

  <h1><fmt:message key="pageTitle.addDataFile"/></h1>

  <c:url value="add.html?experimentId=${addData.measurationId}" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="addData" cssClass="standardInputForm" name="addData" enctype="multipart/form-data">
    <fieldset>

      <f:errorBox/>

      <form:hidden path="measurationId" />


      <div class="itemBox">
        <form:label path="samplingRate" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.samplingRate"/></form:label>

        <form:input path="samplingRate" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />

        <form:errors path="samplingRate" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="dataFile" cssClass="fileFieldLabel" cssErrorClass="fileFieldLabel errorLabel"><fmt:message key="label.dataFile"/></form:label>

        <input type="file" name="dataFile" class="fileField" />

        <form:errors path="dataFile" cssClass="errorBox" />
      </div>


      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.addDataFile'/>" class="submitButton lightButtonLink" />
      </div>


    </fieldset>
  </form:form>
</ui:experimentsTemplate>