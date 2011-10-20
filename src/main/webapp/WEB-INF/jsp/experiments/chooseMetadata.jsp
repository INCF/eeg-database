<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:experimentsTemplate pageTitle="pageTitle.chooseMetadata">

  <h1><fmt:message key="pageTitle.chooseMetadata"/></h1>
    <c:url value="download_metadata_zip.html?id=${measurationDetail.experimentId}" var="formUrl"/>
  <form:form id="metadataDownloader" action="${formUrl}" method="post" commandName="chooseMetadata" cssClass="standardInputForm" name="chooseMetadata" >
    <table style="width:27%;" class="standardValueTable">
    <tr>
      <th class="multiple"><fmt:message key="label.chooseAll"/></th>
      <td><input id="chooseAllId" type="checkbox" name="chooseAll" onclick="checkedAll('metadataDownloader', document.getElementById('chooseAllId').checked);"/></td>
    </tr>
    </table>
    <table id="personsTable" class="standardValueTable">
    <tr>
      <th class="multiple" colspan="3"><fmt:message key="label.person"/></th>
      <td><input id="personId" type="checkbox" name="person" onclick="checkedAll('personsTable', document.getElementById('personId').checked);"/></td>
    </tr>
    <tr>
      <c:if test="${userCanSeePersonDetail}">
      <th><fmt:message key="label.name"/></th>
      <td><input type="checkbox" name="name"/></td>
      <th><fmt:message key="label.phoneNumber"/></th>
      <td><input type="checkbox" name="phoneNumber"/></td>
    </tr>
    <tr>      
      <th><fmt:message key="label.email"/></th>
      <td><input type="checkbox" name="email"/></td>
      <th><fmt:message key="label.dateOfBirth"/></th>
      <td><input type="checkbox" name="birth"/></td>
    </tr>
    </c:if>
    <tr>
      <th><fmt:message key="label.gender"/></th>
      <td><input type="checkbox" name="gender"/></td>
      <th><fmt:message key="label.note"/></th>
      <td><input type="checkbox" name="note"/></td>
    </tr>
    <tr>
      <th><fmt:message key="label.eyesDefect"/></th>
      <td><input type="checkbox" name="eyesDefects"/></td>
      <th><fmt:message key="label.hearingDefect"/></th>
      <td><input type="checkbox" name="hearingDefects"/></td>
    </tr>
    <tr>
      <th><fmt:message key="label.addParams"/></th>
      <td><input type="checkbox" name="personAddParams"/></td>
    </tr>
    </table>

    <table id="scenariosTable" class="standardValueTable">
    <tr>
      <th class="multiple" colspan="3"><fmt:message key="label.scenario"/></th>
      <td><input id="scenarioId" type="checkbox" name="scenario" onclick="checkedAll('scenariosTable', document.getElementById('scenarioId').checked);"/></td>
    </tr>
    <tr>
      <th><fmt:message key="label.scenarioTitle"/></th>
      <td><input type="checkbox" name="title"/></td>
      <th><fmt:message key="label.scenarioLength"/></th>
      <td><input type="checkbox" name="length"/></td>
    </tr>
    <tr>
      <th><fmt:message key="label.scenarioDescription"/></th>
      <td><input type="checkbox" name="description"/></td>
      <th><fmt:message key="label.scenarioFile"/></th>
      <td><input type="checkbox" name="scenFile"/></td>
    </tr>
    </table>
    <table id="measurationTable" class="standardValueTable">
    <tr>
      <th class="multiple" colspan="3"><fmt:message key="label.measuration"/></th>
      <td><input id="measurationId" type="checkbox" name="measuration" onclick="checkedAll('measurationTable', document.getElementById('measurationId').checked);"/></td>
    </tr>
    <tr>
      <th><fmt:message key="label.times"/></th>
      <td><input type="checkbox" name="times"/></td>
      <th><fmt:message key="label.temperature"/></th>
      <td><input type="checkbox" name="temperature"/></td>
    </tr>
        <tr>
      <th><fmt:message key="label.weather"/></th>
      <td><input type="checkbox" name="weather"/></td>
      <th><fmt:message key="label.weatherNote"/></th>
      <td><input type="checkbox" name="weatherNote"/></td>
    </tr>
        <tr>
      <th><fmt:message key="label.hardware"/></th>
      <td><input type="checkbox" name="hardware"/></td>
      <th><fmt:message key="label.addParams"/></th>
      <td><input type="checkbox" name="measurationAddParams"/></td>
    </tr>

    </table>
      <h2>
        <fmt:message key="label.data"/>
      </h2>
      <c:forEach var="fileItem" items="${measurationDetail.dataFiles}" varStatus="fileCounter">
        <table id="measurationTable${fileCounter.count}" class="standardValueTable">
          <tr>
            <th colspan="3">${fileItem.filename}</th>
            <td><input id="measuration${fileCounter.count}Id" type="checkbox" name="dataFile" value="${fileItem.dataFileId}" onclick="checkedAll('measurationTable${fileCounter.count}', document.getElementById('measuration${fileCounter.count}Id').checked);"/></td>
          </tr>
          <tr>
            <th colspan="3"><fmt:message key="label.datafile.content"/></th>
            <td><input type="checkbox" name="content" value="${fileItem.dataFileId}"/></td>
          </tr>
          <tr>
            <td><fmt:message key="label.datafile.name"/>
            </td><td><fmt:message key="label.datafile.value"/></td>
            <td><fmt:message key="label.datafile.datatype"/></td>
            <td></td>
          </tr>
          <c:forEach var="fileParamItem" items="${fileItem.fileMetadataParamVals}" varStatus="fileMetadataCounter">
            <tr>
              <td>${fileParamItem.metadataValue}</td>
              <td>${fileParamItem.fileMetadataParamDef.paramName}</td>
              <td>${fileParamItem.fileMetadataParamDef.paramDataType}</td>
              <td><input type="checkbox" name="fileParam"  value="${fileItem.dataFileId}#${fileParamItem.id.fileMetadataParamDefId}"/></td>
            </tr>
          </c:forEach>

        </table>
      </c:forEach>
      <div class="actionBox">
        <input type="submit" value="<fmt:message key="button.downloadMetadataZip"/>" class="lightButtonLink" />
      </div>
  </form:form>
</ui:experimentsTemplate>