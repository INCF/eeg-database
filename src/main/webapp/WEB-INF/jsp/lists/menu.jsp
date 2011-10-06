<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<ul class="commonButtonMenu">
    <li><a href="<c:url value='/lists/hearing-impairments/list.html'/>"><fmt:message key='menuItem.hearingImpairments'/></a></li>
    <li><a href="<c:url value='/lists/visual-impairments/list.html'/>"><fmt:message key='menuItem.visualImpairments'/></a></li>
    <li><a href="<c:url value='/lists/hardware-definitions/list.html'/>"><fmt:message key='menuItem.hardwareDefinitions'/></a></li>
    <li><a href="<c:url value='/lists/person-optional-parameters/list.html'/>"><fmt:message key='menuItem.optionalParametersForPeople'/></a></li>
    <li><a href="<c:url value='/lists/experiment-optional-parameters/list.html'/>"><fmt:message key='menuItem.optionalParametersForExperiments'/></a></li>
    <li><a href="<c:url value='/lists/file-metadata-definitions/list.html'/>"><fmt:message key='menuItem.fileMetadataDefinitions'/></a></li>
    <li><a href="<c:url value='/lists/weather-definitions/list.html'/>"><fmt:message key='menuItem.weatherDefinitions'/></a></li>
</ul>
