<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:servicesTemplate pageTitle="pageTitle.services">

    <h1><fmt:message key="pageTitle.services"/></h1>

    <c:url value="select-service.html?experimentId=${experimentId}" var="next"/>

    <form:form action="${next}" method="post" commandName="chooseCmd" cssClass="standardInputForm" >


        <fieldset>
           <div class="itemBox">
                <form:label path="headerName" cssClass="selectBoxLabel"
                            cssErrorClass="selectBoxLabel errorLabel"><fmt:message key="label.header"/></form:label>
                <form:select path="headerName" cssClass="selectBox">
                    <c:forEach items="${headers}" var="chan">
                        <option value="${chan}">${chan}</option>
                    </c:forEach>
                </form:select>
               <form:errors path="headerName" cssClass="errorBox"/>

            </div>
            <div class="itemBox">
                <label class="selectBoxLabel"><fmt:message key="label.wavelets"/></label>
                <form:select path="service" cssClass="selectBox">
                    <c:forEach items="${services}" var="srv">
                        <option value="${srv.name}">${srv.name}</option>
                    </c:forEach>
                </form:select>
                </div>
        </fieldset>
        <div class="actionBox">
            <input type="submit" value="<fmt:message key="button.select"/>" class="lightButtonLink"/>
        </div>
    </form:form>


</ui:servicesTemplate>