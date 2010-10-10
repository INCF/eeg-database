<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:systemTemplate pageTitle="pageTitle.writeRequestsList">
    <h1><fmt:message key="pageTitle.writeRequestsList"/></h1>

    <table class="dataTable measurationListDataTable" style="width: 100%;">
        <thead>
            <th style="width: 150px;"><fmt:message key="dataTable.heading.username"/></th>
            <th><fmt:message key="dataTable.heading.requirementReason"/></th>
            <th style="width: 80px;"></th>
        </thead>
        <c:forEach items="${personList}" var="person">
            <tr>
                <td>${person.username}</td>
                <td>${person.requirementReason}</td>
                <td>
                    <a href="<c:url value='/system/write-requests.html?grant=${person.personId}'/>" title="<fmt:message key='page.writeRequestsList.grant'/>"><fmt:message key='page.writeRequestsList.grant'/></a>
                    <a href="<c:url value='/system/write-requests.html?reject=${person.personId}'/>" title="<fmt:message key='page.writeRequestsList.reject'/>"><fmt:message key='page.writeRequestsList.reject'/></a>
                </td>
            </tr>
        </c:forEach>
    </table>
</ui:systemTemplate>
