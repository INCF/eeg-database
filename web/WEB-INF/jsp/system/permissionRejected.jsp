<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:systemTemplate pageTitle="pageTitle.permissionRejected">
    <h1><fmt:message key="pageTitle.permissionRejected"/></h1>

    <p><a href="<c:url value='/system/write-requests.html'/>" ><fmt:message key='page.permissionGrantedRejected.backToTheList'/></a></p>
</ui:systemTemplate>
