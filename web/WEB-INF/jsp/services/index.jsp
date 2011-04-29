<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:servicesTemplate pageTitle="pageTitle.services">

    <h1><fmt:message key="pageTitle.services"/></h1>


         <h4><a href="<c:url value="matchingForm.html?experimentId=${experimentId}"/> "><fmt:message key='link.matchingPursuit'/></a></h4>
         <h4><a href="<c:url value="waveletForm.html?experimentId=${experimentId}&type=DWT"/> "><fmt:message key='link.discreteWavelet'/></a></h4>
         <h4><a href="<c:url value="waveletForm.html?experimentId=${experimentId}&type=CWT"/> "><fmt:message key='link.continuousWavelet'/></a></h4>
         <h4><a href="<c:url value="fourierForm.html?experimentId=${experimentId}"/> "><fmt:message key='link.fastFourier'/></a></h4>


</ui:servicesTemplate>