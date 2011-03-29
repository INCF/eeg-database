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


<c:choose>
    <c:when test="${param.type=='info'}">
        <c:out value="OK#!#"/>
        <span class="reservationheader"><fmt:message key="bookRoom.ajax.info.heading"/></span>
        <fmt:message key="bookRoom.ajax.info.by"/>
        <span class="reservationperson"><c:out value="${data.person.givenname} ${data.person.surname} (${data.person.username})"/></span>
        <hr class="partial">
        <table class="reservationinfo">
            <tr>
                <td class="br_header">
                    <fmt:message key="bookRoom.ajax.info.date"/>:
                </td>
                <td class="br_value">
                    <c:out value="${data.date}"/>
                </td>
            </tr>
            <tr>
                <td class="br_header">
                    <fmt:message key="bookRoom.ajax.info.start"/>:
                </td>
                <td class="br_value">
                    <c:out value="${data.start}"/>
                </td>
            </tr>
            <tr>
                <td class="br_header">
                    <fmt:message key="bookRoom.ajax.info.end"/>:
                </td>
                <td class="br_value">
                    <c:out value="${data.end}"/>
                </td>
            </tr>
            <tr>
                <td class="br_header">
                    <fmt:message key="bookRoom.ajax.info.email"/>:
                </td>
                <td class="br_value">
                    <a href="mailto:<c:out value="${data.person.email}"/>?subject=Reservation to <c:out value="${data.date}, ${data.start} - ${data.end}"/>"><c:out
                            value="${data.person.email}"/></a>
                </td>
            </tr>
        </table>
    </c:when>

    <c:when test="${param.type=='delete'}">
        <c:out value="OK#!#"/>
        <c:out value="${status}"/>
    </c:when>

    <c:otherwise>
        FAIL#!#<fmt:message key='bookRoom.error'/> E5:<c:out value="${param.type}"/>
    </c:otherwise>

</c:choose>