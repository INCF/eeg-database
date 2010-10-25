<%-- 
    Document   : detail
    Created on : 4.5.2010, 20:45:54
    Author     : Jiri Vlasimsky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>

<ui:articlesTemplate pageTitle="pageTitle.experimentDetail">

  <div class="articleDetail">
    <h1>${article.title}</h1>
    <div class="subheading">
      <span class="researchGroup">
        <c:if test="${article.researchGroup != null}">
          <c:out value="${article.researchGroup.title}" />
        </c:if>
        <c:if test="${article.researchGroup == null}">
          Public article
        </c:if>
      </span>
      |
      <span class="date">
        <fmt:formatDate value="${article.time}" />
      </span>
      |
      <span class="author">
        <c:out value="${article.person.username}" />
      </span>
      |
      <span class="comment">
        <a href="<c:url value="/articles/add-article-comment.html?articleId=${article.articleId}" />"><fmt:message key="label.addComment" /></a>
      </span>
      <c:if test="${userCanEdit}">
        | <a href="<c:url value="/articles/edit.html?articleId=${article.articleId}" />"><fmt:message key="label.edit" /> </a>
        | <a href="<c:url value="/articles/delete.html?articleId=${article.articleId}"  />" class="confirm"><fmt:message key="label.delete" /> </a>
      </c:if>

    </div>
    <div class="content">
      <c:out value="${article.text}" escapeXml="false" />
    </div>

    <h2><fmt:message key="heading.comments" /></h2>
    <span class="comment">
      <a href="<c:url value="/articles/add-article-comment.html?articleId=${article.articleId}" />"><fmt:message key="label.addComment" /></a>
    </span>
    <ul class="comments">
      <c:forEach items="${commentsList}" var="comment" varStatus="status">
        <li class="comment">
          <span class="subheading">
            <span class="date"><fmt:formatDate value="${comment.time}" type="both" dateStyle="default" timeStyle="default" /></span> |
            <span class="author"><c:out value="${comment.person.username}" /></span>
          </span>
          <!--|
          <span class="comment">
            <a href="<c:url value="/articles/add-article-comment.html?articleId=${article.articleId}&amp;parentId=${comment.commentId}" />"><fmt:message key="label.comment" /></a>
          </span> -->
          <br />
          <span class="text"><c:out value="${comment.text}" escapeXml="false" /></span>
        </li>
      </c:forEach>
    </ul>
  </div>
</ui:articlesTemplate>