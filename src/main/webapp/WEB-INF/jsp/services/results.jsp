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
            <table class="dataTable">
                <thead>
                <tr>
                    <th style="width: 350px;"><fmt:message key="dataTable.heading.resultTitle"/></th>
                    <th><fmt:message key="dataTable.heading.resultStatus"/></th>
                    <th style="width: 60px;"><fmt:message key="dataTable.heading.download"/></th>
                    <th style="width: 60px;"><fmt:message key="dataTable.heading.delete"/></th>
                </tr>
                </thead>
                <c:forEach items="${results}" var="result" >
                    <tr>
                        <td><c:out value="${result.title}"/></td>
                        <td><c:out value="${result.status}"/></td>
                        <c:set var="str" value="${result.status}" />
                        <jsp:useBean id="str" type="java.lang.String" />
                        <c:if test='<%=!str.equals("running")%>'>
                            <td><a href="<c:url value='download.html?serviceId=${result.serviceResultId}' />"><fmt:message key="link.download"/>
                            </a></td>
                            <td><a href="<c:url value='delete.html?serviceId=${result.serviceResultId}' />"><fmt:message key="link.delete"/>
                            </a></td>
                        </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:otherwise>
    </c:choose>

</ui:servicesTemplate>