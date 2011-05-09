<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:servicesTemplate pageTitle="pageTitle.services">
    <h1><c:out value="${title}"/></h1>
    <c:if test="${coefs}">
        <c:out value="${name}:"/> <br><br>
        <c:forEach items="${values}" var="value">
            <c:out value="${value}"/><br>
        </c:forEach>
    </c:if>
</ui:servicesTemplate>