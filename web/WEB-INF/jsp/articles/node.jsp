<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- <c:out value="${fn:length(requestScope.node.children)}" /> -->
<ul>
  <c:forEach items="${requestScope.node.children}" var="comment" varStatus="status">
    <li>
      <span class="subheading">
        <span class="date"><fmt:formatDate value="${comment.time}" type="both" dateStyle="default" timeStyle="default" /></span> |
        <span class="author"><c:out value="${comment.person.username}" /></span>
      </span>

      <span class="comment">
        <a href="<c:url value="/articles/add-article-comment.html?articleId=${comment.article.articleId}&amp;parentId=${comment.commentId}" />"><fmt:message key="label.comment" /></a>
      </span>
      <br />
      <span class="text"><c:out value="${comment.text}" escapeXml="false" /></span>
      <c:if test="${fn:length(comment.children) > 0}">
        <c:set var="node" value="${comment}" scope="request"/>
        <c:import url="node.jsp" />
      </c:if>
    </li>
  </c:forEach>
</ul>