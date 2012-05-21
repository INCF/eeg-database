<%--
    Document   : detail
    Created on : 4.5.2010, 20:45:54
    Author     : Jiri Vlasimsky
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="auth" tagdir="/WEB-INF/tags/auth/" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<ui:articlesTemplate pageTitle="pageTitle.experimentDetail">
          <div class="articleDetail">
            <h1><c:out value="${article.title}"/></h1>

            <div class="subheading">
              <span class="researchGroup">
                <c:if test="${article.researchGroup != null}">
                    <c:out value="${article.researchGroup.title}"/>
                </c:if>
                <c:if test="${article.researchGroup == null}">
                  Public article
                </c:if>
              </span>
              |
              <span class="date">
                <fmt:formatDate value="${article.time}"/>
              </span>
              |
              <span class="author">
                <c:out value="${article.person.givenname}"/>
                <c:out value="${article.person.surname}"/>
              </span>
              <c:if test="${userCanEdit}">
                | <a href="<c:url value="edit.html?articleId=${article.articleId}" />"><fmt:message
                    key="label.edit"/></a>
                | <a href="<c:url value="delete.html?articleId=${article.articleId}"  />" class="confirm"><fmt:message
                    key="label.delete"/></a>
              </c:if>
                |
                <c:choose>
                  <c:when test="${subscribed}">
                    <a href="<c:url value="subscribe.html?articleId=${article.articleId}&amp;subscribe=false" />"><fmt:message
                            key="label.unsubscribe"/></a>
                  </c:when>
                  <c:otherwise>
                    <a href="<c:url value="subscribe.html?articleId=${article.articleId}&amp;subscribe=true" />"><fmt:message
                            key="label.subscribe"/></a>
                  </c:otherwise>
                </c:choose>
            </div>
            <div class="content">
            <c:out value="${article.text}"/>
            </div>


        <h2><fmt:message key="label.addComment"/></h2>
        <c:url value="edit-article-comment.html" var="formUrl"/>
        <form:form action="${formUrl}" method="post" commandName="command" cssClass="standardInputForm"
                   name="addArticleComment">
            <form:hidden path="articleId"/>

              <div class="itemBox">
                <form:textarea path="text" cssClass="textAreaSmall"/>
                <form:errors path="text" cssClass="errorBox"/>
              </div>
              <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink"/>
              </div>
            </form:form>
        <h2><fmt:message key="heading.comments"/></h2>
            <ul class="comments">
              <c:forEach items="${commentsList}" var="comment" varStatus="status">
                <li class="comment">
                  <span class="subheading">
                    <span class="date"><fmt:formatDate value="${comment.time}" type="both" dateStyle="default"
                                                       timeStyle="default"/></span> |
                    <span class="author">
                        <c:out value="${comment.person.givenname}"/>
                        <c:out value="${comment.person.surname}"/>
                    </span>

                  <span class="comment">
                    <a href="<c:url value="add-article-comment.html?articleId=${article.articleId}&amp;parentId=${comment.commentId}" />"><fmt:message
                            key="label.comment"/></a>
                  </span>
                  <br/>
                  <span class="text"><c:out value="${comment.text}"/></span>


                  <c:if test="${fn:length(comment.children) > 0}">
                    <c:set var="node" value="${comment}" scope="request"/>
                    <c:import url="node.jsp"/>
                  </c:if>
                </li>
              </c:forEach>
            </ul>
          </div>

</ui:articlesTemplate>