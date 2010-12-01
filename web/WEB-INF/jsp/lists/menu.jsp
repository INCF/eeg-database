<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<ul class="commonButtonMenu">
    <li><a href="<c:url value='/lists/hearing-defects/list.html'/>"><fmt:message key='menuItem.hearingDefects'/></a></li>
    <li><a href="<c:url value='/lists/eyes-defects/list.html'/>"><fmt:message key='menuItem.eyesDefects'/></a></li>
    <li><a href="<c:url value='/lists/hardware/list.html'/>"><fmt:message key='menuItem.hardware'/></a></li>
    <li><a href="<c:url value='/lists/person-optional-parameters/list.html'/>"><fmt:message key='menuItem.personOptionalParameters'/></a></li>
    <li><a href="<c:url value='/lists/measuration-optional-parameters/list.html'/>"><fmt:message key='menuItem.measurationOptionalParameters'/></a></li>
    <li><a href="<c:url value='/lists/file-metadata-names/list.html'/>"><fmt:message key='menuItem.fileMetadataNames'/></a></li>
    <li><a href="<c:url value='/lists/weather-definitions/list.html'/>"><fmt:message key='menuItem.weatherDefinitions'/></a></li>
</ul>
