<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:standardTemplate pageTitle="pageTitle.registration" dateOfBirthCalendar="true">

     <h1><fmt:message key='pageTitle.confirmationRepeated'/></h1>

     <p><fmt:message key='text.registrationConfirmedOnce'/></p>

</ui:standardTemplate>
