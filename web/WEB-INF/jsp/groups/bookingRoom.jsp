<%@page import="java.util.Calendar"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">
  <h1><fmt:message key="pageTitle.bookingRoom"/></h1>
  <c:url value="/groups/book-room.html" var="formUrl" />
  <script type="text/javascript" src="<c:url value='/files/js/jquery-1.3.2.min.js' />"></script>
  <script type="text/javascript" src="<c:url value='/files/js/jquery-ui-1.7.1.custom.min.js' />"></script>

  <div id="box">
    <div id="left">
      <form:form action="${formUrl}" method="post" commandName="bookRoomCommand" name="bookRoomCommand" cssClass="standardInputForm">
        <fmt:message key='label.chooseGroup'/>:<br />
        <form:select path="selectedGroup" cssClass="selectBox" cssStyle="width: 195px;">
          <c:forEach items="${researchGroupList}" var="researchGroup">
            <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
              <c:out value="${researchGroup.title}" />
            </option>
          </c:forEach>
        </form:select>
        <br>
        <fmt:message key='label.chooseStartTime'/>
        <form:input path="startTime" cssClass="combobox" cssErrorClass="error" />
        <img src="<c:url value='/files/images/combo_arrow.png' />" alt="" class="combo" onclick="changeTime('startTime')"/>
        <br />
        <fmt:message key='label.chooseEndTime'/>
        <form:input path="endTime" cssClass="combobox" cssErrorClass="error"/>
        <img src="<c:url value='/files/images/combo_arrow.png' />" alt="" class="combo" onclick="changeTime('endTime')"/>
      </form:form>
    </div>
    <div id="right">
      <div id="datePicker"></div>
      <fmt:message key='label.chooseRepeating'/>
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
            <td><c:out value="${group.title}" /></td>
          </tr>
        </c:forEach>
      </table>
    </div>
  </div>
  <div id="changeTime">
    <ul>
      <c:forEach var="hour" begin="7" end="21" step="1">
        <c:forEach var="min" begin="0" end="45" step="15">
          <li onclick="selectTime(this)"><c:if test="${hour==0}">0</c:if><c:out value="${hour}" />:<c:if test="${min==0}">0</c:if><c:out value="${min}" /></li>
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
      onSelect: function( selectedDate ) {
        date = selectedDate;
        showChosenData();
      }
    });

    //default values
    $("#startTime").attr('value','10:00');
    $("#endTime").attr('value','11:00');




    function showChosenData()
    {
      var sel = document.getElementById("selectedGroup");

      $.ajax({
        type: "POST",
        url: "<c:url value='/files/ajax/bookRoom.jsp' />",
        cache: false,
        data: "group="+sel.value+"&date="+date+"&startTime="+$("#startTime").attr('value')+"&endTime="+$("#endTime").attr('value'),
        beforeSend: function(){
          $("#chosenData").html("<center><img src='<c:url value='/files/images/loading.gif' />' alt=''></center>");

        },
        success: function(data){
          

          $("#chosenData").html("Selected time range: "+date+", "+"<br />Selected group:"+sel.options[sel.selectedIndex].text+"<br/>"+data);
        }
      });
    }

    var timeElement;

    function changeTime(elem)
    {
      //if not visible, then show it
      if ($("#changeTime").css('display')=='none')
      {
        timeElement = document.getElementById(elem);

        var pos = $("#"+elem).position();

        $("#changeTime").css('left',pos.left);
        $("#changeTime").css('top',(pos.top+$("#"+elem).height()+5));

        showTimeCombobox(true);
      }
      else
      {
        showTimeCombobox(false);
      }
    }

    function showTimeCombobox(show)
    {
      if (show==true)
      {
        $("#changeTime").fadeIn(100);
      }
      else
      {
        $("#changeTime").fadeOut(100);
      }
    }

    function selectTime(pointer)
    {
      timeElement.value = $(pointer).text();
      showTimeCombobox(false);
    }


  </script>

</ui:groupsTemplate>
