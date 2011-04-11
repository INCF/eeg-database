<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jstl/core_rt' %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Facebook login</title>
</head>
<body>


<table>
    <tr>
        <td><img src="https://graph.facebook.com/<c:out value='${picture}'/>/picture?type=large"/></td>
        <td></td>
    </tr>
    <tr>
        <td>Name:</td>
        <td><c:out value='${facebookProfile.name}'/></td>
    </tr>
    <tr>
        <td>First Name</td>
        <td><c:out value='${facebookProfile.firstName}'/></td>
    </tr>
    <tr>
        <td>Last Name</td>
        <td><c:out value='${facebookProfile.lastName}'/></td>
    </tr>
    <tr>
        <td>Birhtday</td>
        <td><c:out value='${facebookProfile.birthday}'/></td>
    </tr>
    <tr>
        <td>E-mail</td>
        <td><c:out value='${facebookProfile.email}'/></td>
    </tr>
</table>

</body>
</html>