<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:servicesTemplate pageTitle="pageTitle.services">

    <h1><fmt:message key="link.matchingPursuit"/></h1>

    <c:url value="/services/results.html" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="matchingPursuit" cssClass="standardInputForm"
               name="matchingPursuit">

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
                <label class="textFieldLabel"><fmt:message key="label.atom"/></label>
                <form:input path="atom" cssClass="textField dateField"
                            cssErrorClass="textField dateField errorField"/>
                </div>
            <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.run'/>" class="submitButton lightButtonLink" />
      </div>

        </fieldset>
    </form:form>


</ui:servicesTemplate>