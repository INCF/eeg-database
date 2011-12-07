<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:listsTemplate pageTitle="pageTitle.personOptionalParameterList">
    <h1><fmt:message key="pageTitle.personOptionalParameterList"/></h1>

    <form:form method="post" commandName="selectGroupCommand" cssClass="standardInputForm"
               name="selectGroupCommand" action="${next}" >
        <form:label path="researchGroupId" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.researchGroup"/></form:label>

        <form:select path="researchGroupId" cssClass="selectBox" onChange="this.form.submit()">
                        <c:forEach items="${researchGroupList}" var="researchGroup">
                            <option value="${researchGroup.researchGroupId}" label="" <c:if test="${selectGroupCommand.researchGroupId==researchGroup.researchGroupId}"><c:out value="selected=\"selected\""/></c:if>>
                                <c:out value="${researchGroup.title}"/>
                            </option>
                        </c:forEach>
        </form:select>

                    <form:errors path="researchGroupId" cssClass="errorBox"/>
    </form:form>

    <table class="dataTable">
        <thead>
        <tr>
            <th style="width: 50px;"><fmt:message key="dataTable.heading.id"/></th>
            <th style="width: 150px;"><fmt:message key="dataTable.heading.personAdditionalParamsName"/></th>
            <th><fmt:message key="dataTable.heading.personAdditionalParamsDataType"/></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${personOptParamDefList}" var="personAdditionalParam">
            <tr>
                <td><c:out value="${personAdditionalParam.personOptParamDefId}" /></td>
                <td><c:out value="${personAdditionalParam.paramName}" /></td>
                <td><c:out value="${personAdditionalParam.paramDataType}" /></td>
                <td>
                    <a href="<c:url value='/lists/person-optional-parameters/edit.html?id=${personAdditionalParam.personOptParamDefId}&groupid=${selectGroupCommand.researchGroupId}' />"><fmt:message key="link.edit"/></a>
                    <auth:experimenter>
                        <a href="<c:url value='/lists/person-optional-parameters/delete.html?id=${personAdditionalParam.personOptParamDefId}&groupid=${selectGroupCommand.researchGroupId}' />"
                           onclick="return confirm('Are you sure you want to delete item?');"><fmt:message key="link.delete"/></a>
                    </auth:experimenter>
                </td>
            </tr>
        </c:forEach>
    </table>

    <auth:experimenter>
        <div class="actionBox">
            <a href="<c:url value='add.html?groupid=${selectGroupCommand.researchGroupId}'/>" class="lightButtonLink"><fmt:message
                    key="link.addPersonOptionalParameter"/></a>
        </div>
    </auth:experimenter>

</ui:listsTemplate>
