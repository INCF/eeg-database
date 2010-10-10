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
    </head>
    <body>
        <div class="page">
            <tiles:insert name="header" />
            <div class="mainContent">
                <tiles:insert name="content" />
            </div>
            <tiles:insert name="footer" />
        </div>
    </body>
</html>