<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:experimentsTemplate pageTitle="pageTitle.experimentDetail">

  <h1><fmt:message key="pageTitle.experimentDetail"/></h1>

  <table class="standardValueTable">
    <tr>
      <th><fmt:message key="label.measurationId"/></th>
      <td>${experimentDetail.experimentId}</td>
    </tr>
    <tr>
      <th><fmt:message key="label.beginningOfMeasuration"/></th>
      <td><fmt:formatDate value="${experimentDetail.startTime}" pattern="dd.MM.yyyy, HH:mm" /></td>
    </tr>
    <tr>
      <th><fmt:message key="label.endOfMeasuration"/></th>
      <td><fmt:formatDate value="${experimentDetail.endTime}" pattern="dd.MM.yyyy, HH:mm" /></td>
    </tr>
    <tr>
      <th><fmt:message key="label.temperature"/></th>
      <td>${experimentDetail.temperature}</td>
    </tr>
    <tr>
      <th><fmt:message key="label.weather"/></th>
      <td>${experimentDetail.weather.title}</td>
    </tr>
    <tr>
      <th><fmt:message key="label.weatherNote"/></th>
      <td>${experimentDetail.weathernote}</td>
    </tr>
    <tr>
      <th><fmt:message key="label.private" /></th>
      <td>${experimentDetail.privateExperiment}</td>
    </tr>
  </table>

  <h2><fmt:message key="heading.subjectPerson"/></h2>
  <table class="standardValueTable">
    <tr>
      <th><fmt:message key="label.gender"/></th>
      <td>
        <c:if test="${experimentDetail.personBySubjectPersonId.gender == 'M'}">
          <fmt:message key="label.gender.male"/>
        </c:if>
        <c:if test="${experimentDetail.personBySubjectPersonId.gender == 'F'}">
          <fmt:message key="label.gender.female"/>
        </c:if>
      </td>
    </tr>
    <tr>
      <th><fmt:message key="label.yearOfBirth"/></th>
      <td><fmt:formatDate value="${experimentDetail.personBySubjectPersonId.dateOfBirth}" pattern="yyyy" /></td>
    </tr>
    <c:if test="${userCanViewPersonDetails}">
      <tr>
        <td colspan="2"><a href="<c:url value='/people/detail.html?personId=${experimentDetail.personBySubjectPersonId.personId}' />"><fmt:message key="link.viewDetailOfPerson"/></a></td>
      </tr>
    </c:if>
  </table>

  <h2><fmt:message key="heading.scenario"/></h2>
  <table class="standardValueTable">
    <tr>
      <th><fmt:message key="label.scenarioTitle"/></th>
      <td>${experimentDetail.scenario.title}</td>
    </tr>
    <tr>
      <td colspan="2"><a href="<c:url value='/scenarios/detail.html?scenarioId=${experimentDetail.scenario.scenarioId}' />"><fmt:message key="link.viewDetailOfScenario"/></a></td>
    </tr>
  </table>

  <h2><fmt:message key="heading.usedHardware"/></h2>
  <table class="dataTable" style="width: 450px;">
    <thead>
      <tr>
        <th style="width: 250px;"><fmt:message key="dataTable.heading.hardwareTitle"/></th>
        <th><fmt:message key="dataTable.heading.hardwareType"/></th>
      </tr>
    </thead>
    <c:forEach items="${experimentDetail.hardwares}" var="usedHardware">
      <tr>
        <td>${usedHardware.title}</td>
        <td>${usedHardware.type}</td>
      </tr>
    </c:forEach>
  </table>

  <h2><fmt:message key="heading.optionalParameters"/></h2>
  <table class="dataTable" style="width: 450px;">
    <thead>
      <tr>
        <th style="width: 250px;"><fmt:message key="dataTable.heading.measurationOptionalParamName"/></th>
        <th><fmt:message key="dataTable.heading.measurationOptionalParamValue"/></th>
      </tr>
    </thead>
    <c:forEach items="${experimentDetail.experimentOptParamVals}" var="additionalParameter">
      <tr>
        <td>${additionalParameter.experimentOptParamDef.paramName}</td>
        <td>${additionalParameter.paramValue}</td>
      </tr>
    </c:forEach>
  </table>

  <h2><fmt:message key="heading.dataFiles"/></h2>
  <table class="dataTable" style="width: 500px;">
    <thead>
      <tr>
        <th style="width: 250px;"><fmt:message key="dataTable.heading.fileName"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.samplingRate"/></th>
        <th><!-- column with link for detail --></th>
      </tr>
    </thead>
    <tbody>
      <c:forEach items="${experimentDetail.dataFiles}" var="dataItem">
        <tr>
          <td>${dataItem.filename}</td>
          <td>${dataItem.samplingRate}</td>
          <td><a href="<c:url value='data/detail.html?fileId=${dataItem.dataFileId}' />"><fmt:message key="link.detail"/></a></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <div class="actionBox">
    <c:if test="${userIsOwnerOrCoexperimenter}">
      <a href="<c:url value='add-optional-parameter.html?experimentId=${experimentDetail.experimentId}' />" class="lightButtonLink"><fmt:message key="button.addOptionalParameter"/></a>
      <a href="<c:url value='data/add.html?experimentId=${experimentDetail.experimentId}' />" class="lightButtonLink"><fmt:message key="button.addDataFile"/></a>
      <a href="<c:url value='edit.html?id=${experimentDetail.experimentId}' />" class="lightButtonLink"><fmt:message key="button.editExperiment"/></a>
    </c:if>
    <a href="<c:url value='choose-metadata.html?id=${experimentDetail.experimentId}' />" class="lightButtonLink"><fmt:message key="button.downloadMetadataZip"/></a>
  </div>

</ui:experimentsTemplate>
