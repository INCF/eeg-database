<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:personsTemplate pageTitle="pageTitle.listOfPeople">
    <h1><fmt:message key="pageTitle.listOfPeople"/></h1>

    <table class="dataTable listOfPersonsDataTable tableSorter">
        <thead>
            <tr>
                <th style="width: 100px;"><fmt:message key="dataTable.heading.name"/></th>
                <th style="width: 100px;"><fmt:message key="dataTable.heading.surname"/></th>
                <th style="width: 400px;"><fmt:message key="dataTable.heading.note"/></th>
                <th style="width: 100px;"></th>
            </tr>
        </thead>
        <c:forEach items="${personList}" var="person">
            <tr>
                <td><c:out value="${person.givenname}" /></td>
                <td><c:out value="${person.surname}" /></td>
                <td><c:out value="${person.note}" /></td>
                <td><a href="<c:url value='detail.html?personId=${person.personId}' />"><fmt:message key="link.detail"/></a></td>
            </tr>
        </c:forEach>
    </table>
</ui:personsTemplate>
