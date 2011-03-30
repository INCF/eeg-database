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
    var fadeSpeed = 150;
    var collapseSpeed = 1000;
    var timeoutBeforeMessageHide = 15000;
</script>

<h1><fmt:message key="pageTitle.bookingRoom"/></h1>
<c:url value="/groups/book-room.html" var="formUrl"/>

<div id="message">
    <c:if test="${status=='booked'}"><h2><fmt:message key="bookRoom.success"/></h2>
        <c:out value="${comment}" escapeXml="false"/>
    </c:if>
    <c:if test="${status=='failed'}"><h2><fmt:message key="bookRoom.fail"/></h2><c:out value="${comment}"/>
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
                    <c:if test="${status==''}">
                        <script type="text/javascript">
                            //TODO alert('late');
                            $("#message").html("<h2><fmt:message key="bookRoom.gettingLate"/></h2>");
                        </script>
                    </c:if>

                    <c:if test="${status!=''}">
                        <script type="text/javascript">
                            //TODO alert('status = <c:out value="${status}"/>');
                            //TODO $("#message").html("<h2><fmt:message key="bookRoom.gettingLate"/></h2>");
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
                <b><fmt:message key='bookRoom.label.chooseRepeating'/>:</b><br/>
                <fmt:message key='bookRoom.label.repeatFor'/>:<br/>
                <form:select path="repType" onchange="showChosenData()">
                    <option value="0">Every</option>
                    <option value="1">Every odd</option>
                    <option value="2">Every even</option>
                </form:select>
                <fmt:message key='bookRoom.label.week'/>
                <form:select path="repCount" onchange="showChosenData()">
                    <c:forEach var="d" begin="0" end="5" step="1">
                        <option value="${d}">${d}</option>
                    </c:forEach>
                </form:select>
                <fmt:message key='bookRoom.label.repeatTimes'/>
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

<div id="flowbox">
    <span id="close" onclick="toggleInfo(false);" title="<fmt:message key='bookRoom.ajax.info.closeButtonHint'/>"></span>

    <div id="flowContent"></div>
</div>
<div id="shadow">&nbsp;</div>


<script type="text/javascript">

function showInfo(id)
{
    var req = "type=info&id=" + id;

    $.ajax({
        type: "GET",
        url: "<c:url value='book-room-ajax.html' />",
        cache: false,
        data: req,
        beforeSend: function()
        {
            toggleInfo(true);
            $("#flowContent").html("<center><img src='<c:url value='/files/images/loading.gif'/>' style='width: 100%;' alt=''></center>");
        },
        success: function(data)
        {
            var answer = data.split('#!#');
            if (trim(answer[0]) == "OK")
            {
                $("#flowContent").html(answer[1]);
            }
            else
            {
                $("#flowContent").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)");
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            $("#flow").html("Error while getting data... :-(<br>Error " + xhr.status);
            alert("<fmt:message key='bookRoom.error'/> E3:" + xhr.status);
        }
    });
}


function toggleInfo(show)
{
    if (show)
    {
        var x = $(document).scrollLeft();
        var y = $(document).scrollTop();
        var width = $(document).width();
        var height = $(document).height();

        $('#shadow').css('width', width + x);
        $('#shadow').css('height', height + y);

        $("#flowbox").css('left', (x + width) / 2);
        $("#flowbox").css('top', (y + height) / 2);

        $('#shadow').fadeIn(fadeSpeed, function()
        {
        });
        $('#flowbox').fadeIn(fadeSpeed, function()
        {
        });
    }
    else
    {
        $('#shadow').fadeOut(fadeSpeed, function()
        {
        });
        $('#flowbox').fadeOut(fadeSpeed, function()
        {
        });
    }
}

function deleteReservation(id)
{
    if (window.confirm("<fmt:message key='bookRoom.deleteConfirmation'/>"))
    {
        var req = "type=delete&id=" + id;

        $.ajax({
            type: "GET",
            url: "<c:url value='book-room-ajax.html' />",
            cache: false,
            data: req,
            beforeSend: function()
            {
            },
            success: function(data)
            {
                var answer = data.split('#!#');
                if (trim(answer[0]) == 'OK') showChosenData();
                alert(trim(answer[1]));
            },
            error: function (xhr, ajaxOptions, thrownError)
            {
                alert("<fmt:message key='bookRoom.error'/> E4:" + xhr.status);
            }
        });
    }
}


window.onload = function()
{
    //initialization:
    //create datepicker

    $("#datePicker").datepicker({
        minDate: <c:out value="${minDate}" />,
        firstDay: 1,
        dateFormat: "dd/mm/yy",
        <c:if test="${defaultDay!=''}">defaultDate: <c:out value="${defaultDay}" />,</c:if>
        onSelect: function(selectedDate)
        {
            $("#date").attr('value', selectedDate);
            newTime();
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
    if (trim($("#message").html()) != '') showMessage(true);

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


    //calls also showChosenData()
    newTime();

};

function showMessage(show)
{
    if (show)
    {
        $('#message').show(collapseSpeed);
        setTimeout('showMessage(false)', timeoutBeforeMessageHide);
    }
    else
        $('#message').hide(collapseSpeed);
}

function trim(stringToTrim)
{
    return stringToTrim.replace(/^\s+|\s+$/g, "");
}

function isAllowed()
{
    switch ($("#collision").attr('value'))
    {
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
        default:
        {
            alert("<fmt:message key='bookRoom.error'/> E1");
            return false;
        }
    }
    return false;
}

function inPast()
{
    var now = new Date();

    var tmp = $("#date").attr('value').split("/");
    var date = tmp[2] + "/" + tmp[1] + "/" + tmp[0];

    var startTime = new Date(date + " " + $("#startH").attr('value') + ":" + $("#startM").attr('value') + ":00");
    startTime.setHours(startTime.getHours() + 2);

    return (startTime < now);
}

function setEndTime(hourIndex, minuteIndex)
{
    $("#endH").attr('selectedIndex', hourIndex);
    $("#endM").attr('selectedIndex', minuteIndex);
}

function newTime()
{
    var start = $("#startH").attr('value') + $("#startM").attr('value');
    var end = $("#endH").attr('value') + $("#endM").attr('value');
    var date = $("#date").attr('value');


    //try to set end hour after start hour
    if (end <= start)
    {
        if ($("#startH").attr('value') == endHour)
        {
            if ($("#startM").attr('value') != 45)
            {
                setEndTime(endHour - startHour, 3);
            }
            else
            {
                //we can't do anything...
            }
        }
        else
        {
            setEndTime(parseInt($("#startH").attr('selectedIndex')) + 1, $("#endM").attr('selectedIndex'));
        }

        //load endtime value again for next comparison
        end = $("#endH").attr('value') + $("#endM").attr('value');
    }

    if (end <= start)
    {
        $("#collision").attr('value', '-2');
        $("#chosenData").html("<h3><fmt:message key='bookRoom.invalidTime'/></h3>");
    }
    else if (inPast())
    {
        $("#collision").attr('value', '-2');
        $("#chosenData").html("<h3><fmt:message key='bookRoom.timeInPast'/> <c:out value="${now.hours}:${now.minutes}" />!</h3>");
    }
    else
    {
        $("#startTime").attr('value', date + " " + $("#startH").attr('value') + ":" + $("#startM").attr('value') + ":00");
        $("#endTime").attr('value', date + " " + $("#endH").attr('value') + ":" + $("#endM").attr('value') + ":00");
        showChosenData();
    }
}

function showChosenData()
{
    var sel = document.getElementById("selectedGroup");
    var repType = $("#repType").attr('value');
    var repCount = $("#repCount").attr('value');
    var date = $("#date").attr('value');

    var req = "group=" + sel.value + "&date=" + date + "&startTime=" + $("#startTime").attr('value') + "&endTime=" + $("#endTime").attr('value') + "&repType=" + repType + "&repCount=" + repCount;
    //group=24&date=24/03/2011&startTime=24/03/2011 06:00:00&endTime=24/03/2011 07:00:00&repType=0&repCount=0

    $.ajax({
        type: "GET",
        url: "<c:url value='book-room-view.html' />",
        cache: false,
        data: req,
        beforeSend: function()
        {
            $("#collision").attr('value', '-1');
            $("#chosenData").html("<center><img src='<c:url value='/files/images/loading.gif' />' alt=''></center>");
        },
        success: function(data)
        {
            var answer = data.split('#!#');
            if (trim(answer[0]) == "OK")
            {
                $("#chosenData").html(answer[1]);
                $("#collision").attr('value', answer[2]);
                $("table.dataTable").tablesorter({
                    headers: {
                        1: {
                            sorter: 'days'
                        },
                        3: {
                            sorter: false
                        },
                        4: {
                            sorter: false
                        }
                    }
                });
            }
            else
            {
                $("#chosenData").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)");
                $("#collision").attr('value', '-1');
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            $("#chosenData").html("Error while getting data... :-(<br>Error " + xhr.status);
            $("#collision").attr('value', '-1');
            alert("<fmt:message key='bookRoom.error'/> E2:" + xhr.status);
        }
    });
}


</script>
</ui:groupsTemplate>
