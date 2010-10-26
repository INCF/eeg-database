<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:experimentsTemplate pageTitle="pageTitle.fulltextSearch">
  <fieldset class="resultf">
    <h1><fmt:message key="heading.searchResult"/></h1>

    <h2><fmt:message key="web.searchedTitle"/> <c:out value="${searchedString}"/></h2>

    <c:if test="${errors != null}">
      <c:forEach var="error" items="${errors}">
        <p><strong>${error}</strong></p>
      </c:forEach>
    </c:if>

  <table class="dataTable measurationListDataTable">
    <thead>
      <tr>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.entity"/></th>
        <th style="width: 60px;"><fmt:message key="dataTable.heading.searchedString"/></th>
        <th style="width: 150px;"><fmt:message key="dataTable.heading.link"/></th>

      </tr>
    </thead>
    <c:forEach items="${searchResults}" var="sceresult">
      <tr>
        <td><c:out value="${sceresult.section}" escapeXml="false" /></td>
        <td><c:out value="${sceresult.foundString}" escapeXml="false" /></td>
        <%--  <td><a href="<c:url value='/scenarios/detail.html?scenarioId=${sceresult.id}' />"><fmt:message key="link.detail"/></a></td> --%>
      </tr>
    </c:forEach>
  </table>
    <%-- <c:forEach items="${exResults}" var="exresult">
      <tr>
        <td><c:out value="${exresult.class}" /></td>
        <td><c:out value="${exresult.weathernote}" /></td>
        <td><a href="<c:url value='/experiments/detail.html?experimentId=${exresult.experimentId}'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
    <c:forEach items="${personResults}" var="perresult">
      <tr>
        <td><c:out value="${perresult.class}" /></td>
        <td><c:out value="${perresult.note}" /></td>
        <td><a href="<c:url value='/people/detail.html?personId=${perresult.personId}' />"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>

    <c:forEach items="${hardwareResults}" var="hardResults">
      <tr>
        <td><c:out value="${hardResults.class}" /></td>
        <td><c:out value="${hardResults.title}" /></td>
        <td><a href="<c:url value='/lists/hardware/list.html'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
    <c:forEach items="${hearingImpairmentResults}" var="hearingImpairmentResults">
      <tr>
        <td><c:out value="${hearingImpairmentResults.class}" /></td>
        <td><c:out value="${hearingImpairmentResults.description}" /></td>
        <td><a href="<c:url value='/lists/hearing-defects/list.html'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>
    <c:forEach items="${visualImpairmentResults}" var="visualImpairmentResults">
      <tr>
        <td><c:out value="${visualImpairmentResults.class}" /></td>
        <td><c:out value="${visualImpairmentResults.description}" /></td>
        <td><a href="<c:url value='/lists/eyes-defects/list.html'/>"><fmt:message key="link.detail"/></a></td>
      </tr>
    </c:forEach>

    <c:forEach items="${weatherResults}" var="weatherResults">
      <tr>
        <td><c:out value="${weatherResults.class}" /></td>
        <td><c:out value="${weatherResults.title}" /></td>
      </tr>
    </c:forEach>


    <c:forEach items="${expOptParDefResults}" var="expOptParDefResults">
      <tr>
        <td><c:out value="${expOptParDefResults.class}" /></td>
        <td><c:out value="${expOptParDefResults.paramName}" /></td>
      </tr>
    </c:forEach>


  </table>
        <hr/>

    <c:forEach items="${articleResults}" var="artresult">
      <h3><a href="<c:url value='/articles/detail.html?articleId=${artresult.articleId}' />"><c:out value="${artresult.title}" /></a></h3>
      <div><c:out value="${artresult.text}" escapeXml="false"/></div>
      <h4>http://eegdatabase.kiv.zcu.cz<c:url value='/articles/detail.html?articleId=${artresult.articleId}' /></h4>
    </c:forEach> --%>
  </fieldset>
</ui:experimentsTemplate>
