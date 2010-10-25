<%@tag description="Forms basic template - includes header, main part and footer" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pageTitle" required="true"%>
<%@attribute name="addExperimentCalendar" %>
<%@attribute name="dateOfBirthCalendar" %>
<%@attribute name="allowJQuery" %>
<%@attribute name="mainPage" %>
<%@attribute name="history" %>
<%@attribute name="search" %>
<%@attribute name="allowWYSIWYG" %>
<%@attribute name="tableSorter" %>
<%@attribute name="jqueryLatest" %>
<%@attribute name="tableSort" %>
<%@attribute name="tableSortMin" %>
<%@attribute name="jMetadata" %>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
    <title><fmt:message key="${pageTitle}"/> - EEGbase</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" href="<c:url value='/files/style.css'/>" type="text/css" />
    <script type="text/javascript" src="<c:url value='/files/js/metadata.js' />"></script>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript" src="<c:url value="/files/js/global.js" />"></script>

    <c:if test="${mainPage}">
      <script type="text/javascript" src="<c:url value='/files/js/mainPage.js' />"></script>
    </c:if>
    <c:if test="${tableSort}">
      <script type="text/javascript" src="<c:url value='/files/js/table-sort.js' />"></script>
    </c:if>
    <c:if test="${history}">
      <script type="text/javascript" src="<c:url value='/files/js/history.js' />"></script>
    </c:if>
    <c:if test="${jMetadata}">
      <script type="text/javascript" src="<c:url value='/files/js/jquery.metadata.js' />"></script>
    </c:if>

    <c:if test="${allowWYSIWYG}">
      <script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>
      <script type="text/javascript">bkLib.onDomLoaded(nicEditors.allTextAreas);</script>
    </c:if>
    <c:if test="${search}">
      <script type="text/javascript" src="<c:url value='/files/js/search.js' />" > </script>
    </c:if>
    <c:if test="${addExperimentCalendar}">
      <script type="text/javascript">
        $(function() {
          $("#startDate").datepicker({
            changeMonth: true,
            changeYear: true,
            showOn: 'button',
            buttonImage: '<c:url value="/files/images/calendar.gif" />',
            buttonImageOnly: true,
            dateFormat: 'dd/mm/yy'
          });
          $("#endDate").datepicker({
            changeMonth: true,
            changeYear: true,
            showOn: 'button',
            buttonImage: '<c:url value="/files/images/calendar.gif" />',
            buttonImageOnly: true,
            dateFormat: 'dd/mm/yy'
          });
        });
      </script>
    </c:if>
    <c:if test="${dateOfBirthCalendar}">
      <script type="text/javascript">
        $(function() {
          $("#dateOfBirth").datepicker({
            changeMonth: true,
            changeYear: true,
            showOn: 'button',
            buttonImage: '<c:url value="/files/images/calendar.gif" />',
            buttonImageOnly: true,
            dateFormat: 'dd/mm/yy',
            yearRange: ('1920:2000')
          });
        });
      </script>
    </c:if>
  </head>
  <body>
    <div class="page">
      <jsp:include page="/WEB-INF/jsp/template/header.jsp"/>

      <jsp:doBody/>

      <jsp:include page="/WEB-INF/jsp/template/footer.jsp"/>
    </div>
  </body>
</html>