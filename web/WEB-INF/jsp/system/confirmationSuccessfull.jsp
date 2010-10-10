<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:standardTemplate pageTitle="pageTitle.registrationSuccessfull">

    <h1><fmt:message key='pageTitle.confirmationSuccessfull'/></h1>

    <p><fmt:message key='text.registrationSuccessfull.youCanLogIn.part1'/> <a href="<c:url value='/login.html' />" title="<fmt:message key='system.logIn'/>"><fmt:message key='system.logIn'/></a> <fmt:message key='text.registrationSuccessfull.youCanLogIn.part2'/></p>
    
</ui:standardTemplate>
