<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<ui:articlesTemplate pageTitle="pageTitle.addEditArticle">
  <c:choose>
    <c:when test="${addArticle.articleId > 0}">
      <h1><fmt:message key="label.addComment" /></h1>
      <c:url value="add-article-comment.html" var="formUrl" />
    </c:when>
    <c:otherwise>
      <h1><fmt:message key="label.addComment" /></h1>
      <c:url value="edit-article-comment.html" var="formUrl" />
    </c:otherwise>
  </c:choose>
  <c:if test="${userIsAdminInAnyGroup}">
    <form:form action="${formUrl}" method="post" commandName="addArticleComment" cssClass="standardInputForm" name="addArticleComment">
      <form:hidden path="articleId" /> 
      <form:hidden path="parentId" /> 
      <h3><fmt:message key="label.text"/></h3>
      <div class="itemBox">
        <form:textarea path="text" cssClass="textAreaBig"  />
        <form:errors path="text" cssClass="errorBox" />
      </div>
      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" />
      </div>
    </form:form>
  </c:if>
</ui:articlesTemplate>