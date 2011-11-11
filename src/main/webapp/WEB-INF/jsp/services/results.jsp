<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:servicesTemplate pageTitle="pageTitle.services">
   <h1><fmt:message key="pageTitle.servicesResult"/></h1>
    <c:choose>
        <c:when test="${resultsEmpty}">
             <div class="emptyDataTable">
                <fmt:message key="emptyTable.noItems"/>
            </div>
        </c:when>
        <c:otherwise>

        </c:otherwise>
    </c:choose>

</ui:servicesTemplate>