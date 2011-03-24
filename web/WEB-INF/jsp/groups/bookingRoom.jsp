<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">

<c:set var="now" value="<%=new java.util.Date()%>"/>
<c:set var="startHour" value="6"/>
<c:set var="endHour" value="21"/>
<c:set var="defaultDay" value=""/>
<c:set var="minDate" value="0"/>

<script type="text/javascript">
    var startHour = <c:out value="${startHour}"/>;
    var endHour = <c:out value="${endHour}"/>;
</script>

<h1><fmt:message key="pageTitle.bookingRoom"/></h1>
<c:url value="/groups/book-room.html" var="formUrl"/>
<script type="text/javascript" src="<c:url value='/files/js/jquery-1.3.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/files/js/jquery-ui.js' />"></script>

<div id="message">
    <c:if test="${param.status=='booked'}"><h2><fmt:message key="bookRoom.success"/></h2>
        <c:out value="${param.comment}" escapeXml="false"/>
    </c:if>
    <c:if test="${param.status=='failed'}"><h2><fmt:message key="bookRoom.fail"/></h2><c:out value="${param.comment}"/>
    </c:if>
</div>

<form:form action="${formUrl}" method="post" commandName="bookRoomCommand" name="bookRoomCommand"
           cssClass="standardInputForm" onsubmit="return isAllowed();">
    <div id="box">
        <div id="left">

            <b><fmt:message key='label.chooseGroup'/>:</b><br/>
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
                    <script type="text/javascript">
                        $("#message").html("<h2><fmt:message key="bookRoom.gettingLate"/></h2>");
                        setTimeout('hideMessage()', 10000);
                    </script>
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

            <b><fmt:message key='label.chooseStartTime'/></b><br/>

            <select id="startH" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="hour" begin="${startHour}" end="${endHour}" step="1">
                    <option value="<c:if test="${hour<10}">0</c:if>${hour}"
                            <c:if test="${hour==startSelection}">selected</c:if>><c:if test="${hour<10}">0</c:if>${hour}</option>
                </c:forEach>
            </select>
            &nbsp;
            <select id="startM" size="1" class="timeSelect" onchange="newTime()">
                <c:forEach var="min" begin="00" end="45" step="15">
                    <option value="<c:if test="${min==0}">0</c:if>${min}"><c:if test="${min==0}">0</c:if>${min}</option>
                </c:forEach>
            </select>
            <br/>

            <b><fmt:message key='label.chooseEndTime'/></b><br/>

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
                <b><fmt:message key='label.chooseRepeating'/></b><br/>
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
            <div id="internbottom"></div>
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
    <form:hidden path="startTime"/>
    <form:hidden path="endTime"/>
</form:form>
<input type="hidden" id="collision" value="-1"/>


<script type="text/javascript">

    window.onload = function() {
        //initialization:
        //create datepicker

        $("#datePicker").datepicker({
            minDate: <c:out value="${minDate}" />,
            firstDay: 1,
            dateFormat: "dd/mm/yy",
            <c:if test="${defaultDay!=''}">defaultDate: <c:out value="${defaultDay}" />,</c:if>
            onSelect: function(selectedDate) {
                $("#date").attr('value', selectedDate);
                newTime();
            }
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

        //calls also showChosenData()
        newTime();

        <c:if test="${param.status!=''}">
        setTimeout('hideMessage()', 30000);
        </c:if>
    };

    function hideMessage() {
        $('#message').hide(1000);
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

    function setEndTime(hour, minute) {
        hour = hour - startHour;
        minute = parseInt(minute / 15);
        $("#endH").attr('selectedIndex', hour);
        $("#endM").attr('selectedIndex', minute);
    }

    function newTime() {
        var start = $("#startH").attr('value') + $("#startM").attr('value');
        var end = $("#endH").attr('value') + $("#endM").attr('value');
        var date = $("#date").attr('value');
        var d = new Date();
        var now = ((d.getHours() > 9) ? d.getHours() : "0" + d.getHours()) + "" + ((d.getMinutes() > 9) ? d.getMinutes() : "0" + d.getMinutes());
        var today = ((d.getDate() > 9) ? d.getDate() : "0" + d.getDate()) + "/" + ((d.getMonth() > 8) ? (d.getMonth() + 1) : "0" + (d.getMonth() + 1)) + "/" + d.getFullYear();

        //try to set end hour after start hour
        if (end <= start) {
            if ($("#startH").attr('value') == endHour) {
                if ($("#startM").attr('value') != 45) {
                    setEndTime(endHour, 45);
                }
                else {
                    //we can't do anything...
                }
            }
            else {
                setEndTime(parseInt($("#startH").attr('value')) + 1, $("#endM").attr('value'));
            }

            //load endtime value again for next comparison
            end = $("#endH").attr('value') + $("#endM").attr('value');
        }

        //alert("start= " + start + "\nend= " + end + "\nnow= " + now + "\ndate= " + date + "\ntoday= " + today);

        if (end <= start) {
            $("#collision").attr('value', '-2');
            $("#chosenData").html("<h3><fmt:message key='bookRoom.invalidTime'/></h3>");
        }
        else if ((start <= now) && (date <= today)) {
            $("#collision").attr('value', '-2');
            $("#chosenData").html("<h3><fmt:message key='bookRoom.timeInPast'><fmt:param value="${now.hours}:${now.minutes}" /></fmt:message></h3>");
        }
        else {
            $("#startTime").attr('value', date + " " + $("#startH").attr('value') + ":" + $("#startM").attr('value') + ":00");
            $("#endTime").attr('value', date + " " + $("#endH").attr('value') + ":" + $("#endM").attr('value') + ":00");
            showChosenData();
        }
    }

    function showChosenData() {
        var sel = document.getElementById("selectedGroup");
        var repType = $("#repType").attr('value');
        var repCount = $("#repCount").attr('value');
        var date = $("#date").attr('value');

        var req = "group=" + sel.value + "&date=" + date + "&startTime=" + $("#startTime").attr('value') + "&endTime=" + $("#endTime").attr('value') + "&repType=" + repType + "&repCount=" + repCount;

        $.ajax({
            type: "GET",
            url: "<c:url value='book-room-view.html' />",
            cache: false,
            data: req,
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
                    $("#chosenData").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)");
                    $("#collision").attr('value', '-1');
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#chosenData").html("Error while getting data... :-(<br>Error " + xhr.status);
                $("#collision").attr('value', '-1');
            }
        });
    }


</script>
</ui:groupsTemplate>
