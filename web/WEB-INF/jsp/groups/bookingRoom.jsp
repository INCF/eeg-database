<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:groupsTemplate pageTitle="pageTitle.bookingRoom">
  <h1><fmt:message key="pageTitle.bookingRoom"/></h1>
  <c:url value="/groups/book-room.html" var="formUrl" />

  <div id="box">
    <div id="left">
      <form:form action="${formUrl}" method="post" commandName="bookRoomCommand" name="bookRoomCommand" cssClass="standardInputForm">

        <form:select path="selectedGroup" cssClass="selectBox">
          <c:forEach items="${researchGroupList}" var="researchGroup">
            <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
              <c:out value="${researchGroup.title}" />
            </option>
          </c:forEach>
        </form:select>
      </form:form>
    </div>
    <div id="right">
      CALENDAR
    </div>
    <div id="bottom">
      <table class="dataTable listOfResearchGroupsDataTable">
        <thead>
          <tr>
            <th class="columnGroupTitle"><fmt:message key="bookRoom.day"/></th>
            <th class="columnDescription"><fmt:message key="bookRoom.time"/></th>
            <th class="columnDescription"><fmt:message key="bookRoom.group"/></th>
          </tr>
        </thead>
        <c:forEach items="${ownedList}" var="group">
          <tr>
            <td>4.11.2010</td>
            <td>12:00-13:00</td>
            <td><c:out value="${group.title}" /></td>
          </tr>
        </c:forEach>
      </table>
    </div>
  </div>

</ui:groupsTemplate>
