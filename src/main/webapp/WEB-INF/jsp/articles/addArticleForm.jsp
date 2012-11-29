<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:articlesTemplate pageTitle="pageTitle.addEditArticle">
  <c:choose>
    <c:when test="${addArticle.articleId > 0}">
      <h1><fmt:message key="pageTitle.editArticle" /></h1>
      <c:url value="edit.html" var="formUrl" />
    </c:when>
    <c:otherwise>
      <h1><fmt:message key="pageTitle.addArticle" /></h1>
      <c:url value="add-article.html" var="formUrl" />
    </c:otherwise>
  </c:choose>
  <c:if test="${userIsAdminInAnyGroup}">
    <form:form action="${formUrl}" method="post" commandName="addArticle" cssClass="standardInputForm" name="addArticle">
      <form:hidden path="articleId" />
      <div class="itemBox">
        <c:if test="${addArticle.articleId <= 0}" > <%-- EDIT => disable researchGroup choose option --%>
          <form:label path="researchGroup" cssClass="selectBoxLabel" cssErrorClass="selectBoxLabel errorLabel">
            <fmt:message key="label.researchGroup"/>
          </form:label>
          <form:select path="researchGroup" cssClass="selectBox">
            <security:authorize ifAllGranted="ROLE_ADMIN">
              <form:option value="0"><fmt:message key="select.option.article.public"/></form:option>
            </security:authorize>
            <c:forEach items="${researchGroupList}" var="researchGroup">
              <option value="${researchGroup.researchGroupId}" label="" <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> selected </c:if> >
                <c:out value="${researchGroup.title}" /> <c:if test="${researchGroup.researchGroupId == defaultGroupId}"> (default) </c:if>
              </option>
            </c:forEach>
          </form:select>
        </c:if>

        <form:errors path="researchGroup" cssClass="errorBox" />
      </div>
      <div class="itemBox">
        <form:label path="title" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.title"/></form:label>

        <form:input path="title" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="title" cssClass="errorBox" />
      </div>
      <h3><fmt:message key="label.text"/></h3>
      <div class="itemBox">
        <form:textarea path="text" cssClass="textAreaBig"  />
        <form:errors path="text" cssClass="errorBox" />
      </div>
      <div class="itemBox">
           <input type="checkbox" name="publishOnLinkedIn" value="publish" /><fmt:message key="label.linkedin"/>
      </div>
      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.save'/>" class="submitButton lightButtonLink" />
      </div>
    </form:form>
  </c:if>
</ui:articlesTemplate>