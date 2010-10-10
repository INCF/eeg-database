<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<ui:standardTemplate pageTitle="pageTitle.homePage">
  <security:authorize ifAnyGranted="ROLE_USER,ROLE_ADMIN">
    <h1 class="homepageHeading"><fmt:message key="pageTitle.homePage"/></h1>

    <div class="homepageLeftColumn">

      <h2>
        <fmt:message key="heading.myArticles"/>
        <a href="<c:url value='/articles/list.html'/>"><fmt:message key="seeAll"/></a>
        <a class="resetArticlesFilter"><fmt:message key="resetArticlesFilter"/></a>

      </h2>
      <table class="dataTable" >
        <thead>
          <tr>
            <th class="columnDate"><fmt:message key="dataTable.heading.date"/></th>
            <th class="columnTitle"><fmt:message key="dataTable.heading.articleTitle"/></th>
            <th class="columnGroup"><fmt:message key="dataTable.heading.groupTitle"/></th>
          </tr>
        </thead>

        <tbody id="homepageMyArticlesList">

          <c:forEach items="${articleList}" var="article" varStatus="status">
            <c:if test="${article.userMemberOfGroup}">
              <tr class ="${article.researchGroup.title}">
                <td> <fmt:formatDate value="${article.time}" /></td>
                <td><a href="<c:url value="/articles/detail.html?articleId=${article.articleId}" />" ><c:out value="${article.title}" /></a></td>
                <td>
                  <c:if test="${article.researchGroup != null}">
                    <c:out value="${article.researchGroup.title}" />
                  </c:if>
                  <c:if test="${article.researchGroup == null}">
                    Public article
                  </c:if>
                </td>
              </tr>
            </c:if>
          </c:forEach>
        </tbody>

      </table>
      <h2><fmt:message key="heading.myExperiments"/><a href="<c:url value='/experiments/my-experiments.html'/>"><fmt:message key="seeAll"/></a></h2>

      <c:choose>
        <c:when test="${myExperimentsEmpty}">
          <div class="emptyDataTable">
            <fmt:message key="emptyTable.noItems"/>
          </div>
        </c:when>
        <c:otherwise>
          <table class="dataTable" id="homepageMyExperimentsList">
            <thead>
              <tr>
                <th class="columnDate"><fmt:message key="dataTable.heading.date"/></th>
                <th class="columnId"><fmt:message key="dataTable.heading.id"/></th>
                <th class="columnScenarioTitle"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                <th class="columnGroup"><fmt:message key="dataTable.heading.groupTitle"/></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${myExperiments}" var="experiment">
                <tr>
                  <td><f:dateTime value="${experiment.startTime}"/></td>
                  <td>${experiment.experimentId}</td>
                  <td>${experiment.scenario.title}</td>
                  <td>${experiment.researchGroup.title}</td>
                  <td><a href="<c:url value='/experiments/detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message key="link.detail"/></a></td>
                </tr>
              </c:forEach>
            </tbody>

          </table>
        </c:otherwise>
      </c:choose>

      <h2><fmt:message key="heading.meAsSubject"/><a href="<c:url value='/experiments/me-as-subject.html'/>"><fmt:message key="seeAll"/></a></h2>

      <c:choose>
        <c:when test="${meAsSubjectListEmpty}">
          <div class="emptyDataTable">
            <fmt:message key="emptyTable.noItems"/>
          </div>
        </c:when>
        <c:otherwise>
          <table class="dataTable" id="homepageMeAsSubjectList">
            <thead>
              <tr>
                <th class="columnDate"><fmt:message key="dataTable.heading.date"/></th>
                <th class="columnId"><fmt:message key="dataTable.heading.id"/></th>
                <th class="columnScenarioTitle"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${meAsSubjectList}" var="experiment">
                <tr>
                  <td><f:dateTime value="${experiment.startTime}"/></td>
                  <td>${experiment.experimentId}</td>
                  <td>${experiment.scenario.title}</td>
                  <td><a href="<c:url value='/experiments/detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message key="link.detail"/></a></td>
                </tr>
              </c:forEach>
            </tbody>

          </table>
        </c:otherwise>
      </c:choose>


    </div>

    <div class="homepageRightColumn">
      <h2><fmt:message key="heading.myScenarios"/><a href="<c:url value='/scenarios/my-scenarios.html'/>"><fmt:message key="seeAll"/></a></h2>

      <c:choose>
        <c:when test="${myScenariosEmpty}">
          <div class="emptyDataTable">
            <fmt:message key="emptyTable.noItems"/>
          </div>
        </c:when>
        <c:otherwise>
          <table class="dataTable" id="homepageMyScenariosList">
            <thead>
              <tr>
                <th class="columnTitle"><fmt:message key="dataTable.heading.scenarioTitle"/></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${myScenarios}" var="scenario">
                <tr>
                  <td>${scenario.title}</td>
                  <td><a href="<c:url value='/scenarios/detail.html?scenarioId=${scenario.scenarioId}'/>"><fmt:message key="link.detail"/></a></td>
                </tr>
              </c:forEach>
            </tbody>

          </table>
        </c:otherwise>
      </c:choose>


      <h2><fmt:message key="heading.myMemberGroups"/><a href="<c:url value='/groups/my-groups.html'/>"><fmt:message key="seeAll"/></a></h2>



      <c:choose>
        <c:when test="${groupListEmpty}">
          <div class="emptyDataTable">
            <fmt:message key="emptyTable.noItems"/>
          </div>
        </c:when>
        <c:otherwise>
          <table class="dataTable" id="homepageMyGroupsList">
            <thead>
              <tr>
                <th class="columnTitle"><fmt:message key="dataTable.heading.groupTitle"/></th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${groupList}" var="group">
                <tr>
                  <td><a id="<c:out value="${group.title}" />" class="changeGroup"><c:out value="${group.title}" /></a></td>
                  <td><a href="<c:url value='/groups/detail.html?groupId=${group.researchGroupId}' />"><fmt:message key="link.detail"/></a></td>
                </tr>
              </c:forEach>
            </tbody>

          </table>
        </c:otherwise>
      </c:choose>

    </div>


  </security:authorize>
  <security:authorize ifNotGranted="ROLE_USER,ROLE_ADMIN">
    <h1><fmt:message key="homePage.welcomeHeading" /></h1>

    <p><fmt:message key="homePage.description1" /></p>

    <p><fmt:message key="homePage.description2" /></p>

    <div class="homePageActionBoxes">
      <a href="<c:url value='/login.html'/>" class="homepageLoginLink"><fmt:message key="system.logIn" /></a>
      <a href="<c:url value='/registration.html'/>" class="homepageRegistrationLink"><fmt:message key="system.register" /></a>
    </div>
    <a href="<c:url value='/forgotten-password.html'/>"><fmt:message key="system.forgottenPassword" /></a>
  </security:authorize>




</ui:standardTemplate>
