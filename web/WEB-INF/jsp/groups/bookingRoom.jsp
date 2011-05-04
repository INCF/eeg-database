<%@ page import="java.util.Date" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">

<c:set var="now" value='<%= new Date() %>'/>
<c:set var="startHour" value="6"/>
<c:set var="endHour" value="21"/>
<c:set var="defaultDay" value=""/>
<c:set var="minDate" value="0"/>

<script type="text/javascript">
    var startHour = <c:out value="${startHour}"/>;
    var endHour = <c:out value="${endHour}"/>;
    var fadeSpeed = 150;
    var collapseSpeed = 1000;
    var timeoutBeforeMessageHide = 10000;
    var timeoutBeforeLongOperation = 1500;

    //array fow wating flags
    var waitings = new Array();
</script>


<script src="<c:url value="/files/timeline/timeline-api.js"/>" type="text/javascript"></script>
<script src="<c:url value="/files/js/jquery.localize.js"/>" type="text/javascript"></script>
<script src="<c:url value="/files/js/bookRoom/bookRoomFilterAndPDF.js"/>" type="text/javascript"></script>
<script src="<c:url value="/files/js/bookRoom/bookRoomView.js"/>" type="text/javascript"></script>

<h1><fmt:message key="pageTitle.bookingRoom"/></h1>
<c:url value="/groups/book-room.html" var="formUrl"/>

<c:if test="${fn:trim(status)!=''}"><span class="messageToggle" id="toggle_message" title="<fmt:message key='bookRoom.toggle.message'/>" onclick="toggleDiv('message')"><hr></span></c:if>
<div id="message">
    <c:if test="${status=='booked'}"><h2 style="padding-top: 0px;"><fmt:message key="bookRoom.success"/></h2>
        <c:out value="${comment}" escapeXml="false"/>
    </c:if>
    <c:if test="${status=='failed'}"><h2 style="padding-top: 0px;"><fmt:message key="bookRoom.fail"/></h2><c:out value="${comment}"/>
    </c:if>
</div>

<form:form action="${formUrl}" method="post" commandName="bookRoomCommand" name="bookRoomCommand"
           cssClass="standardInputForm" onsubmit="return isAllowed();">
    <div id="box">
        <div id="left">

            <b><fmt:message key='bookRoom.label.chooseGroup'/>:</b><br/>
            <form:select path="selectedGroup" cssClass="selectBox" cssStyle="width: 195px;">
                <c:forEach items="${researchGroupList}" var="researchGroup">
                    <option value="${researchGroup.researchGroupId}" label="" <c:if
                            test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
                        <c:out value="${researchGroup.title}"/>
                    </option>
                </c:forEach>
            </form:select>

            <br/>

            <c:choose>
                <%-- late hour --%>
                <c:when test="${now.hours>endHour-3}">
                    <c:set var="startSelection" value="7"/>
                    <c:set var="endSelection" value="8"/>
                    <c:if test="${fn:trim(status)==''}">
                        <script type="text/javascript">
                            $("#message").html("<h2><fmt:message key="bookRoom.gettingLate"/></h2>");
                            setTimeout(function()
                            {
                                toggleDiv("message", false);
                            }, timeoutBeforeMessageHide);
                        </script>
                    </c:if>
                    <c:set var="defaultDay" value="+1"/>
                    <c:if test="${now.hours>=endHour}">
                        <c:set var="minDate" value="+0"/>
                    </c:if>
                </c:when>
                <%-- early hour --%>
                <c:when test="${now.hours<startHour-1}">
                    <c:set var="startSelection" value="6"/>
                    <c:set var="endSelection" value="7"/>
                </c:when>
                <%-- normal hour (5-19) --%>
                <c:otherwise>
                    <c:set var="startSelection" value="${now.hours+1}"/>
                    <c:set var="endSelection" value="${now.hours+2}"/>
                </c:otherwise>
            </c:choose>

            <b><fmt:message key='bookRoom.label.chooseStartTime'/>:</b><br/>

            <select id="startH" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="hour" begin="${startHour}" end="${endHour}" step="1">
                    <option value="<c:if test="${hour<10}">0</c:if>${hour}" <c:if test="${hour==startSelection}">selected</c:if>><c:if test="${hour<10}">0</c:if>${hour}</option>
                </c:forEach>
            </select>
            &nbsp;
            <select id="startM" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="min" begin="00" end="45" step="15">
                    <option value="<c:if test="${min==0}">0</c:if>${min}"><c:if test="${min==0}">0</c:if>${min}</option>
                </c:forEach>
            </select>
            <br/>

            <b><fmt:message key='bookRoom.label.chooseEndTime'/>:</b><br/>

            <select id="endH" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="hour" begin="${startHour}" end="${endHour}" step="1">
                    <option value="<c:if test="${hour<10}">0</c:if>${hour}"
                            <c:if test="${hour==endSelection}">selected</c:if>><c:if test="${hour<10}">0</c:if>${hour}</option>
                </c:forEach>
            </select>
            &nbsp;
            <select id="endM" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="min" begin="00" end="45" step="15">
                    <option value="<c:if test="${min==0}">0</c:if>${min}"><c:if test="${min==0}">0</c:if>${min}</option>
                </c:forEach>
            </select>

        </div>
        <div id="right">
            <div id="datePicker"></div>
            <div id="internright">
                <fmt:message key="bookRoom.label.recur"/>
                <form:select path="repType" onchange="showChosenData()">
                    <option value="0">every</option>
                    <option value="1">every odd</option>
                    <option value="2">every even</option>
                </form:select><fmt:message key="bookRoom.label.week"/>,
                <br>
                <fmt:message key="bookRoom.label.endAfter"/>
                <form:select path="repCount" onchange="showChosenData()">
                    <c:forEach var="d" begin="0" end="5" step="1">
                        <option value="${d}">${d}</option>
                    </c:forEach>
                </form:select>
                <fmt:message key="bookRoom.label.occurrences"/><br/><br/>
                <fmt:message key="bookRoom.label.selectedDates"/>:
                <div id="repVis"></div>
            </div>
            <div id="internbottom"></div>
        </div>
        <div id="bottom">
            <div id="button">
                <input type="submit" value="<fmt:message key='bookRoom.create'/>"
                       title="<fmt:message key='bookRoom.create'/>" class="submitButton lightButtonLink"/>
            </div>

            <!-- DIV loaded by AJAX-->
            <span id="timeline_header"></span>

            <div id='timeline'></div>
            <div id="slowness_delete" class="slowness"></div>
            <div id="chosenData"></div>

            <!-- TOOLBAR -->
            <div id="toolbar">
                <div>
                    <!-- FILTER -->
                    <fmt:message key="bookRoom.filter.group.label"/>&nbsp;
                    <select id="filterGroup" class="selectBox" style="width: 160px; cursor: pointer;" onchange="showChosenData();">
                        <option value="0" selected="true"><fmt:message key="bookRoom.filter.group.forAll"/></option>
                        <c:forEach items="${researchGroupList}" var="researchGroup">
                            <option value="${researchGroup.researchGroupId}">
                                <fmt:message key="bookRoom.filter.group.onlyFor"/> <c:out value="${researchGroup.title}"/>
                            </option>
                        </c:forEach>
                    </select>
                    <fmt:message key="bookRoom.filter.date.label"/>
                    <input class="" style="cursor: pointer; width:90px;" id="filterDate" onchange="showChosenData();" readonly="true" value="<fmt:message key="bookRoom.filter.date.click"/>"
                           title="<fmt:message key="bookRoom.filter.date.hint"/>"/>
                    <input type="button" value="<fmt:message key="bookRoom.filter.reset"/>" onclick="resetFilter();"/>
                    <!-- PDF DOWNLOAD ICON -->
                    <span class="pdficon" onclick="downloadPDFList();"></span>
                    <span style="cursor: pointer;" onclick="downloadPDFList();" title="<fmt:message key="bookRoom.downloadPDF.hint"/>"><fmt:message key="bookRoom.downloadPDF.label"/></span>
                </div>
            </div>
            <span class="messageToggle" id="toggle_toolbar" title="<fmt:message key='bookRoom.toggle.toolbar'/>" onclick="toggleDiv('toolbar')"><hr></span>

            <!-- LEGEND -->
            <table style="width: 100%;">
                <tr>
                    <td style="border: none; background-color:white; height: 15px; text-align:left; font-weight: bold;">
                        <fmt:message key="bookRoom.label.legend"/>:
                    </td>
                </tr>
                <tr class="sameDay">
                    <td style="height: 11px; text-align:left; font-style: italic;" id="legend_day"></td>
                </tr>
                <tr class="collision">
                    <td colspan="5" style="height: 11px; text-align:left; font-style: italic;"><fmt:message key="bookRoom.collisionWithSelected"/></td>
                </tr>
            </table>
        </div>
    </div>
    <form:hidden path="date"/>
    <form:hidden path="startTime"/>
    <form:hidden path="endTime"/>
</form:form>

<form:form action="book-room-export.html" name="pdf">
    <input type="hidden" name="reservationId" id="pdfId" value=""/>
    <input type="hidden" name="type" id="pdfType" value=""/>
    <input type="hidden" name="date" id="pdfDate" value=""/>
    <input type="hidden" name="length" id="pdfLentgh" value="7"/>
</form:form>

<input type="hidden" id="collision" value="-1"/>
<input type="hidden" id="reservationsCount" value="0"/>

<div id="flowbox">
    <span id="close" onclick="toggleInfo(false);" title="<fmt:message key='bookRoom.ajax.info.closeButtonHint'/>"></span>

    <div id="flowContent"></div>
</div>
<div id="shadow">&nbsp;</div>


<script type="text/javascript">

    window.onload = function()
    {
        //initialization:
        //create datepicker

        $("#datePicker").datepicker({
            minDate: <c:out value="${minDate}" />,
            showWeekNumbers: true,
            firstDay: 1,
            dateFormat: "dd/mm/yy",
            <c:if test="${defaultDay!=''}">defaultDate: <c:out value="${defaultDay}" />,</c:if>
            onSelect: function(selectedDate)
            {
                var newMonth = selectedDate.split('/')[1];
                var oldMonth = $("#date").attr('value').split('/')[1];

                $("#date").attr('value', selectedDate);
                if (newMonth != oldMonth) reloadTimeline();
                else setCenterDate(selectedDate);
                newTime();
            }
        });

        $("#filterDate").datepicker({
            minDate: <c:out value="${minDate}" />,
            showWeekNumbers: true,
            firstDay: 1,
            dateFormat: "dd/mm/yy",
            <c:if test="${defaultDay!=''}">defaultDate: <c:out value="${defaultDay}" />,</c:if>
            onSelect: function(selectedDate)
            {
                showChosenData()
            }
        });

        //make flowbox draggable
        $("#flowbox").draggable({
            grid: [10, 10]
        });

        //default values
        var d = new Date();

        <c:if test="${defaultDay!=''}">
        d.setMilliseconds(d.getMilliseconds() + ((<c:out value="${defaultDay}" />) * 24 * 60 * 60 * 1000));
        </c:if>
        var day = d.getDate() + '';
        if (day.length == 1) day = "0" + day;
        var month = (d.getMonth() + 1) + '';
        if (month.length == 1) month = "0" + month;
        $("#date").attr('value', day + "/" + month + "/" + d.getFullYear());

        //show div#message?
        if (trim($("#message").html()) != '') toggleDiv('message', true);

        //add tablesorter parser
        $.tablesorter.addParser({
            id: 'days',
            is: function(s)
            {
                return false;
            },
            format: function(s)
            {
                var date = s.split("/");
                return date[2] + date[1] + date[0];
            },
            type: 'text'
        });

        //create timeline
        window.onresize = timelineResize();
        //cals also timelineCreate
        reloadTimeline();

        //calls also showChosenData()
        newTime();

        toggleDiv('toolbar', true);
    };
</script>
</ui:groupsTemplate>
