<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">
    <h1><fmt:message key="pageTitle.bookingRoom"/></h1>
    <c:url value="/groups/book-room.html" var="formUrl"/>
    <script type="text/javascript" src="<c:url value='/files/js/jquery-1.3.2.min.js' />"></script>
    <script type="text/javascript" src="<c:url value='/files/js/jquery-ui-1.7.1.custom.min.js' />"></script>

    <div id="box">
        <div id="left">
            <form:form action="${formUrl}" method="post" commandName="bookRoomCommand" name="bookRoomCommand"
                       cssClass="standardInputForm">
                <fmt:message key='label.chooseGroup'/>:<br />
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
                <form:input path="startTime" cssClass="combobox" cssErrorClass="error"/>
                <img src="<c:url value='/files/images/combo_arrow.png' />" alt="" class="combo"
                     onclick="changeTime('startTime')"/>
                <br/>
                <fmt:message key='label.chooseEndTime'/><br/>
                <form:input path="endTime" cssClass="combobox" cssErrorClass="error"/>
                <img src="<c:url value='/files/images/combo_arrow.png' />" alt="" class="combo"
                     onclick="changeTime('endTime')"/>
            </form:form>
        </div>
        <div id="right">
            <div id="datePicker"></div>
            <b><fmt:message key='label.chooseRepeating'/>:</b><br/>
            <fmt:message key='label.repeatFor'/>:<br/>
            <select id="repType">
                <option value="1">Every</option>
                <option value="2">Every odd</option>
                <option value="3">Every even</option>
            </select>
            week,&nbsp;
            <select id="repTime">
                <c:forEach var="d" begin="1" end="5" step="1">
                    <option value="">${d}</option>
                </c:forEach>
            </select>
            &nbsp;<fmt:message key='label.repeatTimes'/>
        </div>
        <div id="bottom">
            <div id="chosenData">&nbsp;</div>
            <table class="dataTable listOfResearchGroupsDataTable">
                <thead>
                <tr>
                    <th class="columnGroupTitle"><fmt:message key="bookRoom.day"/></th>
                    <th class="columnDescription"><fmt:message key="bookRoom.time"/></th>
                    <th class="columnDescription"><fmt:message key="bookRoom.group"/></th>
                </tr>
                </thead>
                <c:forEach items="${researchGroupList}" var="group">
                    <tr>
                        <td>4.11.2010</td>
                        <td>12:00-13:00</td>
                        <td><c:out value="${group.title}"/></td>
                    </tr>
                </c:forEach>
            </table>
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
    <script type="text/javascript">
        //initialization:
        //create datepicker
        var date;

        $("#datePicker").datepicker({
            minDate: 0,
            firstDay: 1,
            dateFormat: "dd.mm.yy",
            onSelect: function(selectedDate) {
                date = selectedDate;
                showChosenData();
            }
        });

        //default values
        $("#startTime").attr('value', '10:00');
        $("#endTime").attr('value', '11:00');


        function trim(stringToTrim) {
            return stringToTrim.replace(/^\s+|\s+$/g, "");
        }

        function showChosenData() {
            var sel = document.getElementById("selectedGroup");

            $.ajax({
                type: "GET",
                url: "<c:url value='book-room-view.html' />",
                cache: false,
                data: "group=" + sel.value + "&date=" + date + "&startTime=" + $("#startTime").attr('value') + "&endTime=" + $("#endTime").attr('value'),
                beforeSend: function() {
                    $("#chosenData").html("<center><img src='<c:url value='/files/images/loading.gif' />' alt=''></center>");

                },
                success: function(data) {
                    var answer = data.split('#');
                    if (trim(answer[0]) == "OK")
                        $("#chosenData").html("Selected time range: " + date + "; " + $("#startTime").attr('value') + " -> " + $("#endTime").attr('value') + "<br />Selected group:" + sel.options[sel.selectedIndex].text + "<br/><hr>" + answer[1]);
                    else {
                        $("#chosenData").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)");
                    }

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
