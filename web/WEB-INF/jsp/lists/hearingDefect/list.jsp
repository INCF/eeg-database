<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:listsTemplate pageTitle="pageTitle.hearingImpairmentList">
    <h1><fmt:message key="pageTitle.hearingImpairmentList"/></h1>

    <table class="dataTable">
        <thead>
        <tr>
            <th style="width: 150px;"><fmt:message key="dataTable.heading.id"/></th>
            <th><fmt:message key="dataTable.heading.description"/></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${hearingDefectList}" var="hearingImpairment">
            <tr>
                <td>${hearingImpairment.hearingImpairmentId}</td>
                <td>${hearingImpairment.description}</td>
                <td>
                    <a href="<c:url value='/lists/hearing-impairments/edit.html?id=${hearingImpairment.hearingImpairmentId}' />"><fmt:message
                            key="link.edit"/></a>
                    <auth:experimenter>
                        <a href="<c:url value='/lists/hearing-impairments/delete.html?id=${hearingImpairment.hearingImpairmentId}' />" onclick="return confirm('Are you sure you want to delete item?');"><fmt:message
                                key="link.delete"/></a>
                    </auth:experimenter>
                </td>
            </tr>
        </c:forEach>
    </table>

    <auth:experimenter>
        <div class="actionBox">
            <a href="<c:url value='/lists/hearing-impairments/add.html'/>" class="lightButtonLink"><fmt:message
                    key="link.addHearingImpairment"/></a>
        </div>
    </auth:experimenter>

</ui:listsTemplate>
