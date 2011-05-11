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

<c:set var="isCollision" value="${collisionsExist}"/>

<c:if test="${collisionsInNextCount>0}">
    <h3 style="margin-left: 1cm; color:red;"><fmt:message key="bookRoom.collisionsList"/></h3>
    <table class="dataTable listOfResearchGroupsDataTable" style="border: 2px solid red;">
        <thead>
        <tr>
            <th class="columnDescription" style="width: 70px;">
            <span style="width:70px; display:block;">
                &nbsp;
            </span>
            </th>
            <th class="columnDescription"><fmt:message key="bookRoom.group"/></th>
            <th class="columnDescription"><fmt:message key="bookRoom.day"/></th>
            <th class="columnDescription"><fmt:message key="bookRoom.time"/></th>
            <th class="columnDescription" style="cursor: default; width: 60px; text-align:center;"><fmt:message key="bookRoom.info"/></th>
            <th class="columnDescription" style="cursor: default; text-align:center;">
            <span style="width: 145px; display:block; text-align:center;">
                <fmt:message key="bookRoom.deleteYourReservation"/>
            </span>
            </th>
        </tr>
        </thead>
        <c:forEach items="${collisionsInNext}" var="reservation">
            <c:set var="datetime" value="${fn:split(reservation.startTime,' ')}"/>
            <c:set var="tmpdate" value="${fn:split(datetime[0],'-')}"/>
            <c:set var="startdate" value="${tmpdate[2]}/${tmpdate[1]}/${tmpdate[0]}"/>
            <c:set var="starttime" value="${fn:split(datetime[1],':')}"/>
            <c:set var="datetime" value="${fn:split(reservation.endTime,' ')}"/>
            <c:set var="endtime" value="${fn:split(datetime[1],':')}"/>

            <tr class='collision' title='Collide with selected time range!'>
                <td>&nbsp;</td>
                <td><c:out value="${reservation.researchGroup.title}"/></td>
                <td><c:out value="${startdate}"/></td>
                <td><c:out value="${starttime[0]}:${starttime[1]} - ${endtime[0]}:${endtime[1]}"/></td>
                <td style="text-align:center;">
                    <span style="width: 60px; display: block; vertical-align: middle;">
                        <span class="infoicon" onclick="showInfo(<c:out value="${reservation.reservationId}"/>)" title="<fmt:message key="bookRoom.more.info"/>"></span>
                        <span class="pdficon" onclick="downloadPDF(<c:out value="${reservation.reservationId}"/>)" title="<fmt:message key="bookRoom.more.download"/>"></span>
                    </span>
                </td>
                <td style="text-align:center;"><c:if test="${reservation.person.username==loggedUser.username}"><span class="deleteicon"
                                                                                                                      onclick="deleteReservation(<c:out value="${reservation.reservationId}"/>)"
                                                                                                                      title="Delete this reservation"></span></c:if>
                </td>
            </tr>
        </c:forEach>
    </table>
</c:if>

<h2><fmt:message key="bookRoom.selectedTime"/>: <c:out value="${timerange}"/></h2>
(<c:out value="${displayed}"/>)
<table class="dataTable listOfResearchGroupsDataTable">
    <thead>
    <tr>
        <th class="columnDescription" style="width:70px;">
            <span style="width:70px; display:block;" onclick="selectAllReservations();" title="<fmt:message key="bookRoom.select.all"/>">
                <fmt:message key="bookRoom.select.all.short"/>
            </span>
        </th>
        <th class="columnDescription"><fmt:message key="bookRoom.group"/></th>
        <th class="columnDescription"><fmt:message key="bookRoom.day"/></th>
        <th class="columnDescription"><fmt:message key="bookRoom.time"/></th>
        <th class="columnDescription" style="cursor: default; width: 60px; text-align:center;"><fmt:message key="bookRoom.info"/></th>
        <th class="columnDescription" style="cursor: default; text-align:center;">
            <span style="width: 145px; display:block; text-align:center;">
                <fmt:message key="bookRoom.deleteYourReservation"/>
            </span>
        </th>
    </tr>
    </thead>

    <c:set var="reservationsCount" value="0"/>
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
                    <c:set var="collision" value="2"/>
                </c:when>
                <c:otherwise>
                    <c:set var="collision" value="1"/>
                </c:otherwise>
            </c:choose>
        </c:if>

        <tr<c:if test="${collision==1}"> class='sameDay' title='<fmt:message key="bookRoom.reservationToSameDate"/> <c:out value="${startdate}"/>. <fmt:message
                key="bookRoom.clickToIcon"/>.'</c:if><c:if
                test="${collision==2}"> class='collision' title='<fmt:message key="bookRoom.collisionWithSelected"/>!'</c:if>>
            <td style=" text-align:center;"><input type="checkbox" id="check_<c:out value="${reservationsCount}"/>" title="<fmt:message key="bookRoom.select.this"/>" checked="true"/></td>
            <td><c:out value="${reservation.researchGroup.title}"/></td>
            <td><c:out value="${startdate}"/></td>
            <td><c:out value="${starttime[0]}:${starttime[1]} - ${endtime[0]}:${endtime[1]}"/></td>
            <td style="text-align:center;">
                <span style="width: 60px; display: block; vertical-align: middle;">
                    <span class="infoicon" onclick="showInfo(<c:out value="${reservation.reservationId}"/>)" title="<fmt:message key="bookRoom.more.info"/>"></span>
                    <span class="pdficon" onclick="downloadPDF(<c:out value="${reservation.reservationId}"/>)" title="<fmt:message key="bookRoom.more.download"/>"></span>
                </span>
            </td>
            <td style="text-align:center;" id="delete<c:out value="${reservation.reservationId}"/>"><c:if test="${reservation.person.username==loggedUser.username}">
                <span class="deleteicon" onclick="deleteReservation(<c:out value="${reservation.reservationId}"/>)"
                      title="Delete this reservation"></span></c:if>
            </td>
        </tr>

        <c:set var="reservationsCount" value="${reservationsCount+1}"/>
    </c:forEach>

    <c:if test="${reservationsCount==0}">
        <tr>
            <td colspan="6"><h2><fmt:message key="bookRoom.noReservationsMatch"/></h2></td>
        </tr>
    </c:if>

    <c:if test="${collisionsCount>0}">
        <tr>
            <td colspan="6"><fmt:message key="bookRoom.collisionInNotVisible"/></td>
        </tr>

        <c:forEach items="${collisions}" var="reservation">
            <c:set var="datetime" value="${fn:split(reservation.startTime,' ')}"/>
            <c:set var="tmpdate" value="${fn:split(datetime[0],'-')}"/>
            <c:set var="startdate" value="${tmpdate[2]}/${tmpdate[1]}/${tmpdate[0]}"/>
            <c:set var="starttime" value="${fn:split(datetime[1],':')}"/>
            <c:set var="start" value="${starttime[0]}${starttime[1]}"/>

            <c:set var="datetime" value="${fn:split(reservation.endTime,' ')}"/>
            <c:set var="endtime" value="${fn:split(datetime[1],':')}"/>
            <c:set var="end" value="${endtime[0]}${endtime[1]}"/>

            <tr class='collision' title='<fmt:message key="bookRoom.collisionWithSelected"/>!'>
                <td><input type="checkbox" id="check_<c:out value="${reservationsCount}"/>" title="<fmt:message key="bookRoom.select.this"/>" checked="true"/></td>
                <td><c:out value="${reservation.researchGroup.title}"/></td>
                <td><c:out value="${startdate}"/></td>
                <td><c:out value="${starttime[0]}:${starttime[1]} - ${endtime[0]}:${endtime[1]}"/></td>
                <td style="text-align:center;">
                <span style="width: 60px; display: block; vertical-align: middle;">
                    <span class="infoicon" onclick="showInfo(<c:out value="${reservation.reservationId}"/>)" title="<fmt:message key="bookRoom.more.info"/>"></span>
                    <span class="pdficon" onclick="downloadPDF(<c:out value="${reservation.reservationId}"/>)" title="<fmt:message key="bookRoom.more.download"/>"></span>
                </span>
                </td>
                <td style="text-align:center;" id="delete<c:out value="${reservation.reservationId}"/>"><c:if test="${reservation.person.username==loggedUser.username}">
                <span class="deleteicon" onclick="deleteReservation(<c:out value="${reservation.reservationId}"/>)"
                      title="Delete this reservation"></span></c:if>
                </td>
            </tr>

            <c:set var="reservationsCount" value="${reservationsCount+1}"/>
        </c:forEach>

        <tr>
            <td colspan="6" style="cursor: pointer; font-style: italic;" onclick="resetFilter();"><fmt:message key="bookRoom.clickToResetFilter"/></td>
        </tr>
    </c:if>
</table>
#!#<c:out value="${isCollision}"/>#!#<c:out value="${reservationsCount}"/>