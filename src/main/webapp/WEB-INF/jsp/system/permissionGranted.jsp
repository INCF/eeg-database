<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:systemTemplate pageTitle="pageTitle.permissionGranted">
    <h1><fmt:message key="pageTitle.permissionGranted"/></h1>

    <p><fmt:message key="text.system.permissionGranted.part1"/> <em><c:out value="${person.username}"/></em> <fmt:message key="text.system.permissionGranted.part2"/>
</ui:systemTemplate>
