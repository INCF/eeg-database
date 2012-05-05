<%-- 
    Document   : list.jsp
    Created on : 27.4.2010, 18:41:55
    Author     : Jiri Vlasimsky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<ui:articlesTemplate pageTitle="${articleListTitle}">
    <h1><fmt:message key="${articleListTitle}"/></h1>

    ${paginator}

    <c:forEach items="${articleList}" var="article" varStatus="status">
        <div class="article">
            <div class="heading">
                <h2><a href="<c:url value="detail.html?articleId=${article.articleId}" />"><c:out
                        value="${article.title}"/></a></h2>
            </div>


            <div class="content">
                <c:out value="${fn:substring(article.text, 1, 500)}"/><c:if test="${fn:length(article.text) > 500}">&hellip;</c:if>
                <a href="<c:url value="detail.html?articleId=${article.articleId}" />"><fmt:message key="readMore" /></a>
            </div>

            <div class="articleInfo">
                <span class="date">
                               <fmt:formatDate value="${article.time}"/>
                           </span>
                &bull;
                <span class="researchGroup">
                    <c:choose>
                        <c:when test="${article.researchGroup != null}">
                            <span class="label"><fmt:message key="researchGroup" />:</span>  <c:out value="${article.researchGroup.title}"/>
                        </c:when>
                        <c:otherwise>
                            Public article
                        </c:otherwise>
                    </c:choose>
                </span>
                &bull;
                <span class="author">
                    <span class="label"><fmt:message key="author" />:</span> <c:out value="${article.person.givenname}"/> <c:out value="${article.person.surname}"/>
                           </span>
                &bull;
                <span class="commentsCount">
                               <c:out value="${fn:length(article.articleComments)}"/> <fmt:message key="heading.comments"/>
                           </span>
                <c:if test="${userIsGlobalAdmin || article.person.personId == loggedUserId}">
                    &bull; <a href="<c:url value="edit.html?articleId=${article.articleId}" />"><fmt:message
                        key="label.edit"/> </a>
                    &bull; <a href="<c:url value="delete.html?articleId=${article.articleId}" />"
                               class="confirm"><fmt:message key="label.delete"/> </a>
                </c:if>
            </div>
        </div>
    </c:forEach>

    ${paginator}
</ui:articlesTemplate>
