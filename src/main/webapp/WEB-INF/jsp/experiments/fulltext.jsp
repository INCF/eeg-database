<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:experimentsTemplate pageTitle="pageTitle.fulltextSearch">
  <fieldset class="resultf">
    <h1><fmt:message key="heading.searchResult"/></h1>

    <h2><fmt:message key="web.searchedTitle"/> <c:out value="${searchedString}"/></h2>

    <table class="dataTable measurationListDataTable">
      <c:choose>
        <c:when test="${errors != null}">
          <c:forEach var="error" items="${errors}">
            <p><strong>${error}</strong></p>
          </c:forEach>
        </c:when>
        <c:when test="${resultsEmpty}">
          <div class="emptyDataTable">
            <fmt:message key="emptyTable.noItems"/>
          </div>
        </c:when>
        <c:otherwise>

          <c:forEach items="${searchResults}" var="artresult">
            <h3><a href="<c:url value='${artresult.path}${artresult.id}' />"><c:out value="${artresult.section}: ${artresult.title}" /></a></h3>
            <c:out value="${artresult.foundString}" escapeXml="false"/>
            <h4>http://eegdatabase.kiv.zcu.cz<c:url value='${artresult.path}${artresult.id}' /></h4>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </table>
  </fieldset>
</ui:experimentsTemplate>
