<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:servicesTemplate pageTitle="pageTitle.services">
    <c:set var="type" value="${type}" />
    <jsp:useBean id="type" type="java.lang.String" />
    <c:if test='<%=type.equals("DWT")%>'>
      <h1><fmt:message key="link.discreteWavelet"/></h1>
    </c:if>
    <c:if test='<%=type.equals("CWT")%>'>
      <h1><fmt:message key="link.continuousWavelet"/></h1>
    </c:if>


    <c:url value="resultsWT.html?experimentId=${experimentId}" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="wavelet" cssClass="standardInputForm"
               name="wavelet">

        <fieldset>

            <div class="itemBox">
                <form:label path="channel" cssClass="selectBoxLabel"
                            cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.channel"/></form:label>
                <form:select path="channel" cssClass="selectBox">
                    <c:forEach items="${channels}" var="chan">
                        <option value="${chan.number}">${chan.name}</option>
                    </c:forEach>
                </form:select>

            </div>
            <div class="itemBox">
                <label class="selectBoxLabel"><fmt:message key="label.wavelets"/></label>
                <form:select path="wavelet" cssClass="selectBox">
                    <c:forEach items="${wavelets}" var="wav">
                        <option value="${wav}">${wav}</option>
                    </c:forEach>
                </form:select>
                </div>
            <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.run'/>" class="submitButton lightButtonLink" />
      </div>

        </fieldset>
    </form:form>


</ui:servicesTemplate>