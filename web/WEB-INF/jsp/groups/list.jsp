<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>

<ui:groupsTemplate pageTitle="pageTitle.listOfGroups">
    <h1><fmt:message key="pageTitle.listOfGroups"/></h1>

      <table class="dataTable listOfResearchGroupsDataTable">
        <thead>
            <tr>
                <th class="columnGroupTitle"><fmt:message key="dataTable.heading.groupTitle"/></th>
                <th class="columnDescription"><fmt:message key="dataTable.heading.description"/></th>
                <th></th>
            </tr>
        </thead>
        <c:forEach items="${groupList}" var="group">
            <tr>
                <td><c:out value="${group.title}" /></td>
                <td><c:out value="${group.description}" /></td>
                <td><a href="<c:url value='/groups/detail.html?groupId=${group.researchGroupId}' />"><fmt:message key="link.detail"/></a></td>
            </tr>
        </c:forEach>
    </table>
</ui:groupsTemplate>
