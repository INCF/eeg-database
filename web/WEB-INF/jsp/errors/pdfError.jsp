<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <title><fmt:message key="error.databaseError.title"/> &ndash; EEGbase</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="/EEGDatabase/files/style.css" type="text/css"/>
</head>
<body>
<div class="page">
    <div class="header">

        <div class="pageTitle">EEGbase</div>

        <div class="cleaner"></div>

    </div>


    <div class="mainContent">

        <h1>PDF error</h1>

        <p>error has occurred</p>

        <div><c:out value="${error}"/></div>

        <ul>
            <li><a href="<c:url value='/'/>" title="<fmt:message key='gotoHomepage'/>"><fmt:message key="gotoHomepage"/></a></li>
        </ul>

    </div>

    <div class="footer">
        EEGbase - database for data gained in encephalography research.
        <br/>
        Copyright &copy; <a href="http://www.zcu.cz/" title="The University of West Bohemia">The University of West Bohemia</a> 2008-2010
    </div>
</div>
</body>
</html>
