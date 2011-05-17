<%-- 
    Document   : accessDeniedNotAdmin
    Created on : 26.10.2010, 17:14:33
    Author     : pbruha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:standardTemplate pageTitle="pageTitle.accessDenied">

    <h1><fmt:message key='pageTitle.accessDenied'/></h1>

    <p><fmt:message key='text.accessDenied.description'/></p>
    <p><fmt:message key="title.accessInfo"/></p>

</ui:standardTemplate>
