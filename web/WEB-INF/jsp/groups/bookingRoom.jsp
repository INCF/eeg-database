<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">
<!--<script type="text/javascript" src="<c:url value='/files/js/bookRoom.js'/>"></script>-->

<h1><fmt:message key="pageTitle.bookingRoom"/></h1>
<c:url value="/groups/book-room.html" var="formUrl"/>
<script type="text/javascript" src="<c:url value='/files/js/jquery-1.3.2.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/files/js/jquery-ui-1.7.1.custom.min.js' />"></script>

<c:if test="${param.status=='booked'}"><h2><fmt:message key="bookRoom.success"/></h2><c:out
        value="${param.comment}"/><hr></c:if>
<c:if test="${param.status=='fail'}"><h2><fmt:message key="bookRoom.fail"/></h2><c:out
        value="${param.comment}"/><hr></c:if>

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
            <br>
            <fmt:message key='label.chooseStartTime'/><br/>
            <form:input path="startTime" cssClass="combobox" cssErrorClass="error" onblur="showChosenData()"/>
            <img src="<c:url value='/files/images/combo_arrow.png' />" alt="" class="combo"
                 onclick="changeTime('startTime')"/>
            <br/>
            <fmt:message key='label.chooseEndTime'/><br/>
            <form:input path="endTime" cssClass="combobox" cssErrorClass="error" onblur="showChosenData()"/>
            <img src="<c:url value='/files/images/combo_arrow.png' />" alt="" class="combo"
                 onclick="changeTime('endTime')"/>

        </div>
        <div id="right">
            <div id="datePicker"></div>
            <b><fmt:message key='label.chooseRepeating'/>:</b><br/>
            <fmt:message key='label.repeatFor'/>:<br/>
            <form:select path="repType" onchange="showChosenData()">
                <option value="1">Every</option>
                <option value="2">Every odd</option>
                <option value="3">Every even</option>
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
            <input type="submit" value="<fmt:message key='bookRoom.create'/>" class="submitButton lightButtonLink"/>

            <div id="chosenData">&nbsp;</div>
        </div>
    </div>
    <div id="changeTime">
        <ul>
            <c:forEach var="hour" begin="6" end="21" step="1">
                <c:forEach var="min" begin="0" end="45" step="15">
                    <li onclick="selectTime(this)"><c:if test="${hour==0}">0</c:if><c:out value="${hour}"/>:<c:if
                            test="${min==0}">0</c:if><c:out value="${min}"/></li>
                </c:forEach>
            </c:forEach>
        </ul>
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

        $("#startTime").attr('value', (d.getHours() + 1) + ':00');
        $("#endTime").attr('value', (d.getHours() + 2) + ':00');

        var day = d.getDate() + '';
        if (day.length == 1) day = "0" + day;
        var month = (d.getMonth() + 1) + '';
        if (month.length == 1) month = "0" + month;
        $("#date").attr('value', day + "/" + month + "/" + d.getFullYear());

        showChosenData();

    };


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
        }
        return false;
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
                $("#chosenData").html("<center><img src='<c:url value='/files/images/loading.gif' />' alt=''></center>");

            },
            success: function(data) {
                var answer = data.split('#');
                if (trim(answer[0]) == "OK") {
                    $("#chosenData").html(answer[1]);
                    //alert(answer[1]);
                    document.getElementById("collision").value = answer[2];
                }
                else {
                    $("#chosenData").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)<hr>" + data.length + "<br/>" + data);
                }
                //alert(data);
            },
            error: function (xhr, ajaxOptions, thrownError) {
                $("#chosenData").html("Error while getting data... :-(");
                alert(xhr.status);
            }
        }
                )
                ;
    }

    var timeElement;

    function changeTime(elem) {
        //if not visible, then show it
        if ($("#changeTime").css('display') == 'none') {
            timeElement = document.getElementById(elem);

            var pos = $("#" + elem).position();

            $("#changeTime").css('left', pos.left);
            $("#changeTime").css('top', (pos.top + $("#" + elem).height() + 5));

            showTimeCombobox(true);
        }
        else {
            showTimeCombobox(false);
        }
    }

    function showTimeCombobox(show) {
        if (show == true) {
            $("#changeTime").fadeIn(100);
        }
        else {
            $("#changeTime").fadeOut(100);
        }
    }

    function selectTime(pointer) {
        timeElement.value = $(pointer).text();
        showTimeCombobox(false);
    }


</script>
</ui:groupsTemplate>
