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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="group" value="${param.group}"/>
<c:set var="date" value="${param.date}"/>
<c:set var="startTime" value="${param.startTime}"/>
<c:set var="endTime" value="${param.endTime}"/>

<c:out value="OK#"/>
<h2><c:out value="${timerange}"/></h2>


<table class="dataTable listOfResearchGroupsDataTable">
    <thead>
    <tr>
        <th class="columnDescription"><fmt:message key="bookRoom.group"/></th>
        <th class="columnGroupTitle"><fmt:message key="bookRoom.day"/></th>
        <th class="columnDescription"><fmt:message key="bookRoom.time"/></th>
    </tr>
    </thead>
    <c:forEach items="${reservations}" var="reservation">

        <c:set var="datetime" value="${fn:split(reservation.startTime,' ')}"/>
        <c:set var="tmpdate" value="${fn:split(datetime[0],'-')}"/>
        <c:set var="startdate" value="${tmpdate[2]}/${tmpdate[1]}/${tmpdate[0]}"/>
        <c:set var="starttime" value="${fn:split(datetime[1],':')}"/>

        <c:set var="datetime" value="${fn:split(reservation.endTime,' ')}"/>
        <c:set var="endtime" value="${fn:split(datetime[1],':')}"/>

        <tr class="">
            <td><c:out value="${reservation.researchGroup.title}"/></td>
            <td><c:out value="${startdate}"/></td>
            <td><c:out value="${starttime[0]}:${starttime[1]} - ${endtime[0]}:${endtime[1]}"/></td>
        </tr>
    </c:forEach>
</table>