<%-- 
    Document   : list.jsp
    Created on : 27.4.2010, 18:41:55
    Author     : Jiri Vlasimsky
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<ui:articlesTemplate pageTitle="${articleListTitle}">
  <h1><fmt:message key="${articleListTitle}"/></h1>
  <c:forEach items="${articleList}" var="article" varStatus="status">
    <c:if test="${article.userMemberOfGroup}">
      <div class="article">
        <div class="heading">
          <h2><a href="<c:url value="detail.html?articleId=${article.articleId}" />" ><c:out value="${article.title}" /></a></h2>
        </div>
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
          <span class="commentsCount">
            <c:out value="${fn:length(article.articleComments)}" /> <fmt:message key="heading.comments" />
          </span>
          <c:if test="${article.userIsOwnerOrAdmin}">
            | <a href="<c:url value="edit.html?articleId=${article.articleId}" />"><fmt:message key="label.edit" /> </a>
            | <a href="<c:url value="delete.html?articleId=${article.articleId}" />" class="confirm"><fmt:message key="label.delete" /> </a>
          </c:if>
        </div>

        <div class="content">
          <c:out value="${fn:substring(article.text,0,500)}" escapeXml="false" />
        </div>
      </div>
    </c:if>
  </c:forEach>
</ui:articlesTemplate>
