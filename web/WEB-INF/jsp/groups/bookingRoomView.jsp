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

<c:set var="myGroup" value="${param.group}"/>
<c:set var="myRepCount" value="${param.repCount}"/>
<c:set var="myRepType" value="${param.repType}"/>
<c:set var="myDate" value="${param.date}"/>

<c:set var="myStart" value="${startTime}"/>
<c:set var="myEnd" value="${endTime}"/>


<c:out value="OK#!#"/>

<%--<c:out value="${date},${myStart},${myEnd}"/>--%>

<c:set var="isCollision" value="0"/>

<c:if test="${collisionsCount>0}">
    <c:set var="isCollision" value="1"/>
    <h3 style="margin-left: 1cm; color:red;"><fmt:message key="bookRoom.collisionsList"/></h3>
    <table class="dataTable listOfResearchGroupsDataTable" style="border: 2px solid red;">
        <thead>
        <tr>
            <th class="columnDescription"><fmt:message key="bookRoom.group"/></th>
            <th class="columnGroupTitle"><fmt:message key="bookRoom.day"/></th>
            <th class="columnDescription"><fmt:message key="bookRoom.time"/></th>
        </tr>
        </thead>
        <c:forEach items="${collisions}" var="reservation">
            <c:set var="datetime" value="${fn:split(reservation.startTime,' ')}"/>
            <c:set var="tmpdate" value="${fn:split(datetime[0],'-')}"/>
            <c:set var="startdate" value="${tmpdate[2]}/${tmpdate[1]}/${tmpdate[0]}"/>
            <c:set var="starttime" value="${fn:split(datetime[1],':')}"/>
            <c:set var="datetime" value="${fn:split(reservation.endTime,' ')}"/>
            <c:set var="endtime" value="${fn:split(datetime[1],':')}"/>

            <tr class='collision' title='Collide with selected time range!'>
                <td><c:out value="${reservation.researchGroup.title}"/></td>
                <td><c:out value="${startdate}"/></td>
                <td><c:out value="${starttime[0]}:${starttime[1]} - ${endtime[0]}:${endtime[1]}"/></td>
            </tr>
        </c:forEach>
    </table>
    <hr>
</c:if>

<h2><fmt:message key="bookRoom.selectedTime"/>: <c:out value="${timerange}"/></h2>
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
        <c:set var="start" value="${starttime[0]}${starttime[1]}"/>


        <c:set var="datetime" value="${fn:split(reservation.endTime,' ')}"/>
        <c:set var="endtime" value="${fn:split(datetime[1],':')}"/>
        <c:set var="end" value="${endtime[0]}${endtime[1]}"/>


        <c:set var="collision" value="0"/>
        <c:if test="${startdate==myDate}">
            <c:choose>
                <c:when test="${(start<myEnd) && (end>myStart)}">
                    <c:set var="isCollision" value="1"/>
                    <c:set var="collision" value="2"/>
                </c:when>
                <c:otherwise>
                    <c:set var="collision" value="1"/>
                </c:otherwise>
            </c:choose>
        </c:if>

        <tr<c:if test="${collision==1}"> class='sameDay' title='Reservation to same day you have selected'</c:if><c:if
                test="${collision==2}"> class='collision' title='Collission with selected time range!'</c:if>>
            <td><c:out value="${reservation.researchGroup.title}"/></td>
            <td><c:out value="${startdate}"/></td>
            <td><c:out value="${starttime[0]}:${starttime[1]} - ${endtime[0]}:${endtime[1]}"/></td>
        </tr>

    </c:forEach>

</table>
#!#<c:out value="${isCollision}"/>