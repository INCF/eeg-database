<%--
    Document   : articleDeleted
    Created on : 6.5.2010, 22:06:46
    Author     : Jiri Vlasimsky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:articlesTemplate pageTitle="pageTitle.articlesSettings">
    <h1><fmt:message key="heading.ArticlesSettings"/></h1>
    <h2><fmt:message key="heading.KeyWordFilter"/></h2>
    <form:form method="post" cssClass="standardInputForm">
       <br />
       <fmt:message key="label.filterHelp1"/><br>
       <fmt:message key="label.filterHelp2"/>
       <br />
       <br />
       <textarea id="keywords" name="keywords" rows="8" cols="65"/><c:out value="${keywords}"/></textarea>
       <br />
       <input type="submit" style="margin-left: 0px; margin-top: 10px" value="<fmt:message key='button.changeFilter'/>" class="submitButton lightButtonLink" />
    </form:form>
<br />
<h2><fmt:message key="heading.ArticlesSubscriptions"/></h2>
<table class="dataTable" id="ResearchGroupsList">
    <thead>
        <tr>
            <th class="columnTitle"><fmt:message key="dataTable.heading.groupTitle"/></th>
            <th></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${researchGroupList}" var="group">
            <tr>
                <td><c:out value="${group.title}" /></td>
                <td>
                    <c:choose>
                        <c:when test="${fn:contains(articlesGroupSubscribtions, group)}">
                            <a href="<c:url value="subscribeGroupArticles.html?groupId=${group.researchGroupId}&amp;subscribe=false" />"><fmt:message key="label.unsubscribe" /> </a>
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value="subscribeGroupArticles.html?groupId=${group.researchGroupId}&amp;subscribe=true" />"><fmt:message key="label.subscribe" /> </a>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </tbody>

</table>


</ui:articlesTemplate>
