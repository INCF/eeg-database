<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title><tiles:getAsString name="title" /> - EEGbase</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

        <base href="<c:url value='/' />" />

        <link rel="stylesheet" href="<c:url value='/files/style.css'/>" type="text/css" />
        <script type="text/javascript" src="<c:url value='/files/js/jquery-1.3.2.min.js' />"></script>
        <script type="text/javascript" src="<c:url value='/files/js/jquery-ui-1.7.1.custom.min.js' />"></script>
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
    </head>
    <body>
        <div id="startDates"></div>
        <div class="page">
            <tiles:insert name="header" />

            <div class="leftMenu">
                <tiles:insert name="leftMenu" />
            </div>

            <div class="mainContentWithMenu">
                <tiles:insert name="content" />
            </div>

            <tiles:insert name="footer" />
        </div>
    </body>
</html>