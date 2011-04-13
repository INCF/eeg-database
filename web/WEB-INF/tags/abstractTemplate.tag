<%@tag description="Forms basic template - includes header, main part and footer" pageEncoding="UTF-8" %>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pageTitle" required="true" %>
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
<%@attribute name="jspinner" %>
<%@attribute name="jaddWeather" %>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title><fmt:message key="${pageTitle}"/> - EEGbase</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link rel="stylesheet" href="<c:url value='/files/style.css'/>" type="text/css"/>
    <link rel="stylesheet" href="<c:url value='/files/jquery.timeentry.css'/>" type="text/css"/>
    <link rel="stylesheet" href="<c:url value='/files/jquery.dateentry.css'/>" type="text/css"/>
    <script type="text/javascript" src="<c:url value='/files/js/metadata.js' />"></script>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script src="<c:url value='/files/js/jquery-ui.js' />"></script>

    <script src="<c:url value='/files/js/jmodalForm/modalForm.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.button.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.core.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.dialog.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.draggable.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.mouse.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.position.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.resizable.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.ui.widget.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.effects.core.js' />"></script>
    <script src="<c:url value='/files/js/jmodalForm/jquery.bgiframe-2.1.2.js' />"></script>

    <script src="<c:url value='/files/js/jquery.tablesorter.js'/>"></script>
    <script src="<c:url value='/files/js/jquery.timeentry.js'/>"></script>
    <script src="<c:url value='/files/js/jquery.dateentry.js'/>"></script>
    <script type="text/javascript" src="<c:url value="/files/js/global.js" />"></script>


    <c:if test="${mainPage}">
        <script type="text/javascript" src="<c:url value='/files/js/mainPage.js' />"></script>
    </c:if>
    <c:if test="${jaddWeather}">
        <script type="text/javascript" src="<c:url value='/files/js/addWeather.js'/>"></script>
    </c:if>
    <c:if test="${history}">
        <script type="text/javascript" src="<c:url value='/files/js/history.js' />"></script>
    </c:if>
    <c:if test="${jMetadata}">
        <script type="text/javascript" src="<c:url value='/files/js/jquery.metadata.js' />"></script>
    </c:if>

    <c:if test="${allowWYSIWYG}">
        <%-- <script src="http://js.nicedit.com/nicEdit-latest.js" type="text/javascript"></script>
        <script type="text/javascript">bkLib.onDomLoaded(nicEditors.allTextAreas);</script> --%>
    </c:if>
    <c:if test="${search}">
        <script type="text/javascript" src="<c:url value='/files/js/search.js' />"></script>
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
                var year = (new Date).getFullYear();
                $("#dateOfBirth").datepicker({
                    changeMonth: true,
                    changeYear: true,
                    buttonImage: '<c:url value="/files/images/calendar.gif" />',
                    buttonImageOnly: true,
                    dateFormat: 'dd/mm/yy',
                    yearRange: (year - 90 + ':' + year)
                });
            });
        </script>
    </c:if>
    <c:if test="${jspinner}">
        <script type="text/javascript">
            jQuery().ready(function($) {
                $("#startDate").dateEntry({
                    spinnerImage: '<c:url value="/files/images/spinnerDefault.png" />',
                    dateFormat: 'dmy/'
                });
                $("#endDate").dateEntry({
                    spinnerImage: '<c:url value="/files/images/spinnerDefault.png" />' ,
                    dateFormat: 'dmy/'
                });
                $("#startTime").timeEntry({
                    spinnerImage: '<c:url value="/files/images/spinnerDefault.png" />',
                    show24Hours: true
                });
                $("#endTime").timeEntry({
                    spinnerImage: '<c:url value="/files/images/spinnerDefault.png" />',
                    show24Hours: true
                });
            });
        </script>
        <script type="text/javascript" src="<c:url value='/files/js/dateTimeEdit.js' />"></script>

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