<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.experimentOptionalParameterList">
    <h1><fmt:message key="pageTitle.experimentOptionalParameterList"/></h1>

    <table class="dataTable">
        <thead>
        <tr>
            <th style="width: 50px;"><fmt:message key="dataTable.heading.measurationAdditionalParamsId"/></th>
            <th style="width: 150px;"><fmt:message key="dataTable.heading.measurationAdditionalParamsName"/></th>
            <th><fmt:message key="dataTable.heading.measurationAdditionalParamsDataType"/></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${measurationAdditionalParamsList}" var="measurationAdditionalParam">
            <tr>
                <td><c:out value="${measurationAdditionalParam.experimentOptParamDefId}" /></td>
                <td><c:out value="${measurationAdditionalParam.paramName}" /></td>
                <td><c:out value="${measurationAdditionalParam.paramDataType}" /></td>
                <td>
                    <a href="<c:url value='/lists/experiment-optional-parameters/edit.html?id=${measurationAdditionalParam.experimentOptParamDefId}' />"><fmt:message
                            key="link.edit"/></a>
                    <auth:experimenter>
                        <a href="<c:url value='/lists/experiment-optional-parameters/delete.html?id=${measurationAdditionalParam.experimentOptParamDefId}' />"
                           onclick="return confirm('Are you sure you want to delete item?');"><fmt:message
                                key="link.delete"/></a>
                    </auth:experimenter>
                </td>
            </tr>
        </c:forEach>
    </table>

    <auth:experimenter>
        <div class="actionBox">
            <a href="<c:url value='add.html'/>" class="lightButtonLink"><fmt:message
                    key="link.addExperimentOptionalParameter"/></a>
        </div>
    </auth:experimenter>

</ui:listsTemplate>
