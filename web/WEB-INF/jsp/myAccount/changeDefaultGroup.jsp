<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:myAccountTemplate pageTitle="pageTitle.changeDefaultGroup">

    <h1><fmt:message key='pageTitle.changeDefaultGroup'/></h1>
    <c:url value="/my-account/change-default-group.html" var="formUrl" />
    <form:form action="${formUrl}" method="post" commandName="changeDefaultGroup" name="changeDefaultGroup" cssClass="standardInputForm">
      <form:select path="defaultGroup" cssClass="selectBox">
        <form:option value="-1"><fmt:message key="select.option.noResearchGroupSelected"/></form:option>
        <c:forEach items="${researchGroupList}" var="researchGroup">
          <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
             <c:out value="${researchGroup.title}" />
          </option>
        </c:forEach>
      </form:select>
     <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" />
    </form:form>

</ui:myAccountTemplate>
