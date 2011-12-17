<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:dataTemplate pageTitle="pageTitle.addOptionalParameterForMeasuration">

  <h1><fmt:message key="pageTitle.addOptionalParameterForMeasuration"/></h1>

  <table class="standardValueTable">
    <tr>
      <th><fmt:message key="label.researchGroup"/></th>
      <td><c:out value="${researchGroupTitle}" /></td>
    </tr>
    <tr>
      <th><fmt:message key="dataTable.heading.fileName"/></th>
      <td><c:out value="${dataDetail.filename}" /></td>
    </tr>
    <tr>
      <th><fmt:message key="dataTable.heading.samplingRate"/></th>
      <td><c:out value="${dataDetail.samplingRate}" /></td>
    </tr>
  </table>

  <h2><fmt:message key="heading.metadata"/></h2>
  <c:url value="detail.html?fileId=${dataDetail.dataFileId}" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="addMetadata" cssClass="standardInputForm">
    <fieldset>

      <input type="hidden" name="dataId" value="${dataDetail.dataFileId}" />

      <table class="dataTable" style="width: 450px;">
        <thead>
          <tr>
            <th style="width: 180px;"><fmt:message key="dataTable.heading.parameterName"/></th>
            <th><fmt:message key="dataTable.heading.value"/></th>
          </tr>
        </thead>
        <c:forEach items="${dataDetail.fileMetadataParamVals}" var="metadata">
          <tr>
            <td>${metadata.fileMetadataParamDef.paramName}</td>
            <td>${metadata.metadataValue}</td>
          </tr>
        </c:forEach>

        <c:if test="${userIsOwnerOrCoexpOfCorrespExperiment}">
          <tr>
            <td>
              <form:select path="paramId" cssClass="selectBox" cssStyle="width: 160px;">
                <form:option value="-1"><fmt:message key="select.option.noParameterSelected"/></form:option>
                <c:forEach items="${fileMetadataParams}" var="metadataItem">
                  <form:option value="${metadataItem.fileMetadataParamDefId}" label="${metadataItem.paramName}"></form:option>
                </c:forEach>
              </form:select>

              <form:errors path="paramId" cssClass="errorLabel" element="div" />
            </td>
            <td>
              <form:input path="paramValue" cssClass="textField paramValueField" cssErrorClass="textField paramValueField errorField" cssStyle="width: 180px;" />

              <input type="submit" value="<fmt:message key='button.add'/>" class="lightButtonLinkSmall" />

              <form:errors path="paramValue" cssClass="errorLabel" element="div" />

            </td>
          </tr>
        </c:if>

      </table>
    </fieldset>
  </form:form>

  <div class="actionBox">
    <a href="<c:url value='download?fileId=${dataDetail.dataFileId}' />" class="lightButtonLink"><fmt:message key="button.downloadFile"/></a>
    <a href="<c:url value='../detail.html?experimentId=${dataDetail.experiment.experimentId}' />" class="lightButtonLink"><fmt:message key="button.backToMeasuration"/></a>
  </div>
</ui:dataTemplate>