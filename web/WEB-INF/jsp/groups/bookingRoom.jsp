<%@ page import="java.util.GregorianCalendar" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">

<c:set var="now" value="<%=new java.util.Date()%>"/>

<h1><fmt:message key="pageTitle.bookingRoom"/></h1>
<c:url value="/groups/book-room.html" var="formUrl"/>
<script type="text/javascript" src="<c:url value='/files/js/jquery-1.3.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/files/js/jquery-ui.js' />"></script>

<div id="message">
    <c:if test="${param.status=='booked'}"><h2><fmt:message key="bookRoom.success"/></h2>
        <c:out value="${param.comment}" escapeXml="false"/>
    </c:if>
    <c:if test="${param.status=='failed'}"><h2><fmt:message key="bookRoom.fail"/></h2><c:out
            value="${param.comment}"/>
    </c:if>
</div>

<form:form action="${formUrl}" method="post" commandName="bookRoomCommand" name="bookRoomCommand"
           cssClass="standardInputForm" onsubmit="return isAllowed();">
    <div id="box">
        <div id="left">

            <fmt:message key='label.chooseGroup'/>:<br/>
            <form:select path="selectedGroup" cssClass="selectBox" cssStyle="width: 195px;">
                <c:forEach items="${researchGroupList}" var="researchGroup">
                    <option value="${researchGroup.researchGroupId}" label="" <c:if
                            test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
                        <c:out value="${researchGroup.title}"/>
                    </option>
                </c:forEach>
            </form:select>

            <br/>
            <fmt:message key='label.chooseStartTime'/><br/>
            <form:hidden path="startTime"/>

            <select id="startH" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="hour" begin="6" end="21" step="1">
                    <option value="<c:if test="${hour<10}">0</c:if>${hour}" <c:if test="${hour==(now.hours+1)}">selected</c:if>><c:if test="${hour<10}">0</c:if>${hour}</option>
                </c:forEach>
            </select>
            &nbsp;
            <select id="startM" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="min" begin="00" end="45" step="15">
                    <option value="<c:if test="${min==0}">0</c:if>${min}"><c:if test="${min==0}">0</c:if>${min}</option>
                </c:forEach>
            </select>
            <br/>

            <fmt:message key='label.chooseEndTime'/><br/>
            <form:hidden path="endTime"/>
            <select id="endH" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="hour" begin="6" end="21" step="1">
                    <option value="<c:if test="${hour<10}">0</c:if>${hour}" <c:if test="${hour==(now.hours+2)}">selected</c:if>><c:if test="${hour<10}">0</c:if>${hour}</option>
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
            <b><fmt:message key='label.chooseRepeating'/>:</b><br/>
            <fmt:message key='label.repeatFor'/>:<br/>
            <form:select path="repType" onchange="showChosenData()">
                <option value="0">Every</option>
                <option value="1">Every odd</option>
                <option value="2">Every even</option>
            </form:select>
            week,&nbsp;
            <form:select path="repCount" onchange="showChosenData()">
                <c:forEach var="d" begin="0" end="5" step="1">
                    <option value="${d}">${d}</option>
                </c:forEach>
            </form:select>
            &nbsp;<fmt:message key='label.repeatTimes'/>
        </div>
        <div id="bottom">
            <div id="button">
                <input type="submit" value="<fmt:message key='bookRoom.create'/>"
                       title="<fmt:message key='bookRoom.create'/>" class="submitButton lightButtonLink"/>
            </div>
            <div id="chosenData">&nbsp;</div>
        </div>
    </div>
    <form:hidden path="date"/>
</form:form>
<input type="hidden" id="collision" value="-1"/>


<script type="text/javascript">
    var date;

    window.onload = function() {
        //initialization:
        //create datepicker

        $("#datePicker").datepicker({
            minDate: 0,
            firstDay: 1,
            dateFormat: "dd/mm/yy",
            onSelect: function(selectedDate) {
                $("#date").attr('value', selectedDate);
                showChosenData();
            }
        });

        //default values
        var d = new Date();

        var day = d.getDate() + '';
        if (day.length == 1) day = "0" + day;
        var month = (d.getMonth() + 1) + '';
        if (month.length == 1) month = "0" + month;
        $("#date").attr('value', day + "/" + month + "/" + d.getFullYear());

        //calls also showChosenData()
        newTime();

    <c:if test="${param.status!=''}">
        setTimeout('hideMessage()', 10000);
    </c:if>
    };

    function hideMessage() {
        $('#message').fadeOut(1500, function() {
        });
    }

    function trim(stringToTrim) {
        return stringToTrim.replace(/^\s+|\s+$/g, "");
    }

    function isAllowed() {
        switch ($("#collision").attr('value')) {
            case '0':
            {
                return true;
            }
                break;
            case '1':
            {
                alert("<fmt:message key='bookRoom.collisions'/>");
                return false;
            }
                break;
            case '-1':
            {
                alert("<fmt:message key='bookRoom.waitForControl'/>");
                showChosenData();
                return false;
            }
                break;
            case '-2':
            {
                alert("<fmt:message key='bookRoom.invalidTime'/>");
                return false;
            }
                break;
        }
        return false;
    }

    function newTime() {
        var start = $("#startH").attr('value') + $("#startM").attr('value');
        var end = $("#endH").attr('value') + $("#endM").attr('value');

        if (end < start) {
            $("#collision").attr('value', '-2');
            $("#chosenData").html("<h3><fmt:message key='bookRoom.invalidTime'/></h3>");
        } else {
            $("#startTime").attr('value', $("#startH").attr('value') + ":" + $("#startM").attr('value'));
            $("#endTime").attr('value', $("#endH").attr('value') + ":" + $("#endM").attr('value'));
            showChosenData();
        }
    }

    function showChosenData() {
        var sel = document.getElementById("selectedGroup");
        var repType = $("#repType").attr('value');
        var repCount = $("#repCount").attr('value');

        $.ajax({
            type: "GET",
            url: "<c:url value='book-room-view.html' />",
            cache: false,
            data: "group=" + sel.value + "&date=" + $("#date").attr('value') + "&startTime=" + $("#startTime").attr('value') + "&endTime=" + $("#endTime").attr('value') + "&repType=" + repType + "&repCount=" + repCount,
            beforeSend: function() {
                $("#collision").attr('value', '-1');
                $("#chosenData").html("<center><img src='<c:url value='/files/images/loading.gif' />' alt=''></center>");
            },
            success: function(data) {
                var answer = data.split('#!#');
                if (trim(answer[0]) == "OK") {
                    $("#chosenData").html(answer[1]);
                    $("#collision").attr('value', answer[2]);
                }
                else {
                    $("#chosenData").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)<hr>" + data.length + "<br/>" + data);
                    $("#collision").attr('value', '-1');
                }
                //alert(data);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#chosenData").html("Error while getting data... :-(<br>Error " + xhr.status);
                $("#collision").attr('value', '-1');
            }
        }
                )
                ;
    }


</script>
</ui:groupsTemplate>
