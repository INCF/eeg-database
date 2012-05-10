<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page session="false" %>


<ui:myAccountTemplate pageTitle="pageTitle.socialOverview">
    <h1><fmt:message key='pageTitle.socialOverview'/></h1>

    <c:forEach var="providerId" items="${providerIds}">
        
        <s:message code="${providerId}.displayName" var="providerDisplayName" />
        <c:set var="link" value="/connect/${providerDisplayName}.html" />
        <c:set var="connections" value="${connectionMap[providerId]}" />
        <div class="accountConnection">

            <s:message code="${providerId}.icon" var="iconUrl"/>
            <h4><img src="<c:url value="${iconUrl}" />" width="36" height="36" />${providerDisplayName}</h4>

            <p>
                <c:if test="${not empty connections}">
                    You are already connected to ${providerDisplayName}.
                </c:if>

                <c:if test="${empty connections}">
                <form action="<c:url value="${fn:toLowerCase(link)}" />" method="POST">
                    <div class="formInfo">
                        <p>
                            You are not yet connected to ${providerDisplayName}.
                        </p>
                    </div>
                    <p><button type="submit">Connect with ${providerDisplayName}</button></p>
                </form>

            </c:if>
        </p>
    </div>
</c:forEach>

</ui:myAccountTemplate>