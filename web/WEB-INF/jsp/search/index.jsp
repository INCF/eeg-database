<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:searchTemplate pageTitle="pageTitle.search">

    <h1><fmt:message key="pageTitle.search"/></h1>

    <c:url value="fulltext.html" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="fullTextSearchCommand" cssClass="standardInputForm">
        <div class="itemBox">
            <label class="textFieldLabel"><fmt:message key="label.searchText"/></label>
            <form:input path="searchTI" cssClass="textField dateField"/>
        </div>
        <div class="itembox">
            <input type="submit" value="<fmt:message key='button.fulltextSearch'/>" name="searchBT"
                   class="submitButton lightButtonLink"/>
        </div>
    </form:form>
</ui:searchTemplate>