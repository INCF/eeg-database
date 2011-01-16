<%-- 
    Document   : bookRoom
    Created on : 11.11.2010, 12:41:27
    Author     : Jenda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="group" value="${param.group}"/>
<c:set var="date" value="${param.date}"/>
<c:set var="startTime" value="${param.startTime}"/>
<c:set var="endTime" value="${param.endTime}"/>

<c:out value="OK#${group}/${date}/${startTime}/${endTime}"/>
<hr>

<h2><c:out value="${timerange}"/></h2>

<c:forEach items="${reservations}" var="reservation">

        <c:out value="${reservation.researchGroup.title} - ${reservation.creationTime}"/><br>

</c:forEach>