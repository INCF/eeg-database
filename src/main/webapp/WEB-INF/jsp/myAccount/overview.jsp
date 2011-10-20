<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<ui:myAccountTemplate pageTitle="pageTitle.accountOverview">

    <h1><fmt:message key='pageTitle.accountOverview'/></h1>

    <table class="standardValueTable">
        <tr>
            <th><fmt:message key="label.userName"/></th>
            <td>${userInfo.username}</td>
        </tr>
        <tr>
            <th><fmt:message key="label.fullName"/></th>
            <td>${userInfo.givenname} ${userInfo.surname}</td>
        </tr>
        <tr>
            <th><fmt:message key="label.globalPermissionLevel"/></th>
            <td><f:globalAuthority code="${userInfo.authority}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="label.facebookId"/></th>
            <td>
                <c:choose>
                    <c:when test="${facebookConnected}">
                        <c:out value="${userFacebookId}" />
                    </c:when>
                    <c:otherwise>
                    <a href="<c:url value="/social/login.html" />">
                        <fmt:message key="page.myAccount.connectWithFacebook"/>
                    </a>
                    </c:otherwise>
                </c:choose>

        </tr>
    </table>

    <h2><fmt:message key="heading.membershipInGroups"/></h2>
    <c:choose>
        <c:when test="${membershipListEmpty}">
            <div class="emptyDataTable">
                <fmt:message key="emptyTable.notMemberOfAnyGroup"/>
            </div>
        </c:when>
        <c:otherwise>
            <table class="dataTable accountOverviewGroupMembershipDataTable">
                <thead>
                <tr>
                    <th class="columnGroupName"><fmt:message key="dataTable.heading.groupName"/></th>
                    <th class="columnRole"><fmt:message key="dataTable.heading.roleInGroup"/></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${membershipList}" var="info">
                    <tr>
                        <td><c:out value="${info.groupTitle}"/></td>
                        <td><f:groupAuthority code="${info.authority}"/></td>
                        <td><a href="<c:url value='/groups/detail.html?groupId=${info.groupId}'/>"><fmt:message
                                key="link.detail"/></a></td>
                    </tr>
                </c:forEach>
                </tbody>

            </table>
        </c:otherwise>
    </c:choose>

</ui:myAccountTemplate>
