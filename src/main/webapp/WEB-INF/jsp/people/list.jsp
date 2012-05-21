<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:personsTemplate pageTitle="pageTitle.listOfPeople">
    <h1><fmt:message key="pageTitle.listOfPeople"/></h1>

    ${paginator}

    <table class="dataTable listOfPersonsDataTable">
        <thead>
            <tr>
                <th style="width: 100px;"><fmt:message key="dataTable.heading.surname"/></th>
                <th style="width: 100px;"><fmt:message key="dataTable.heading.name"/></th>
                <th><fmt:message key="dataTable.heading.note"/></th>
                <th style="width: 50px;"></th>
            </tr>
        </thead>
        <c:forEach items="${personList}" var="person">
            <tr>
                <td><c:out value="${person.surname}" /></td>
                <td><c:out value="${person.givenname}" /></td>
                <td><c:out value="${fn:substring(person.note, 1, 70)}" />&hellip;</td>
                <td><a href="<c:url value='detail.html?personId=${person.personId}' />"><fmt:message key="link.detail"/></a></td>
            </tr>
        </c:forEach>
    </table>

    ${paginator}

</ui:personsTemplate>
