<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<ui:standardTemplate pageTitle="pageTitle.homePage">
<c:if test="${not empty param.login_error}">
                <span class="errorMessage"><fmt:message key="system.loginNotSuccessfull"/> <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.</span>
</c:if>
<security:authorize ifNotGranted="ROLE_ANONYMOUS">
    <h1 class="homepageHeading"><fmt:message key="pageTitle.homePage"/></h1>

    <div class="homepageLeftColumn">

        <h2>
            <fmt:message key="heading.myArticles"/>
            <a href="<c:url value='/articles/list.html'/>"><fmt:message key="seeAll"/></a>
            <a class="resetArticlesFilter"><fmt:message key="resetArticlesFilter"/></a>

        </h2>
        <table class="dataTable">
            <thead>
            <tr>
                <th class="columnDate"><fmt:message key="dataTable.heading.date"/></th>
                <th class="columnTitle"><fmt:message key="dataTable.heading.articleTitle"/></th>
                <th class="columnGroup"><fmt:message key="dataTable.heading.groupTitle"/></th>
                <th class="columnCommentsCount"><fmt:message key="heading.comments"/></th>
            </tr>
            </thead>

            <tbody id="homepageMyArticlesList">

            <c:forEach items="${articleList}" var="article" varStatus="status">
                <c:if test="${article.userMemberOfGroup}">
                    <tr class="${article.researchGroup.title}">
                        <td><fmt:formatDate value="${article.time}"/></td>
                        <td class="tableArticleTitle"><a
                                href="<c:url value="/articles/detail.html?articleId=${article.articleId}" />"><c:out
                                value="${article.title}"/></a></td>
                        <td>
                            <c:if test="${article.researchGroup != null}">
                                <c:out value="${article.researchGroup.title}"/>
                            </c:if>
                            <c:if test="${article.researchGroup == null}">
                                Public article
                            </c:if>
                        </td>
                        <td>
                            <c:out value="${fn:length(article.articleComments)}"/>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>

        </table>
        <h2><fmt:message key="heading.myExperiments"/><a
                href="<c:url value='/experiments/my-experiments.html'/>"><fmt:message key="seeAll"/></a></h2>

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
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${myExperiments}" var="experiment">
                        <tr>
                            <td><f:dateTime value="${experiment.startTime}"/></td>
                            <td>${experiment.experimentId}</td>
                            <td>${experiment.scenario.title}</td>
                            <td>
                                <a href="<c:url value='/experiments/detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message
                                        key="link.detail"/></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>
            </c:otherwise>
        </c:choose>

        <h2><fmt:message key="heading.meAsSubject"/><a
                href="<c:url value='/experiments/me-as-subject.html'/>"><fmt:message key="seeAll"/></a></h2>

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
                            <td>
                                <a href="<c:url value='/experiments/detail.html?experimentId=${experiment.experimentId}'/>"><fmt:message
                                        key="link.detail"/></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>
            </c:otherwise>
        </c:choose>


    </div>

    <div class="homepageRightColumn">
        <h2><fmt:message key="heading.myScenarios"/><a href="<c:url value='/scenarios/my-scenarios.html'/>"><fmt:message
                key="seeAll"/></a></h2>

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
                            <td>
                                <a href="<c:url value='/scenarios/detail.html?scenarioId=${scenario.scenarioId}'/>"><fmt:message
                                        key="link.detail"/></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>
            </c:otherwise>
        </c:choose>


        <h2><fmt:message key="heading.myMemberGroups"/><a href="<c:url value='/groups/my-groups.html'/>"><fmt:message
                key="seeAll"/></a></h2>


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
                            <td><a id="<c:out value="${group.title}" />" class="changeGroup"><c:out
                                    value="${group.title}"/></a></td>
                            <td>
                                <a href="<c:url value='/groups/detail.html?groupId=${group.researchGroupId}' />"><fmt:message
                                        key="link.detail"/></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>
            </c:otherwise>
        </c:choose>

    </div>


</security:authorize>
<security:authorize ifAllGranted="ROLE_ANONYMOUS">
    <div class="notLogged">
        <div class="homepageLeftColumn">
            <h1><fmt:message key="homePage.welcomeHeading"/></h1>

            <p>
                    <fmt:message key="homePage.description1"/>

            <div class="left">
                <img src="./files/images/eeg-foto-main.jpg"/>
            </div>
            <div class="right">
                <h2><fmt:message key="homePage.featuresHeading"/></h2>
                <fmt:message key="homePage.featuresList"/>
            </div>
            <div class="clear"></div>
            </p>
            <p><fmt:message key="homePage.description2"/></p>

            <div class="homePageActionBoxes">
                <a href="<c:url value='registration.html'/>" class="homepageRegistrationLink"><fmt:message
                        key="system.register"/></a>
            </div>
            <div class="clear"></div>
            <h2><fmt:message key="homePage.discoverHeading"/></h2>

            <div class="showreel">
                <ul>
                    <li><a href="http://www.youtube.com/watch?v=B6RKeipMgvghttp://www.youtube.com" target="_blank"><img
                            src="./files/images/video1.jpg" alt="Downloading" title="Downloading"></a></li>
                    <li><a href="http://www.youtube.com/watch?v=EmlR6vfGdD4" target="_blank"><img
                            src="./files/images/video2.jpg" alt="Add group" title="Add group"></a></li>
                    <li><a href="http://www.youtube.com/watch?v=FJLo8qM0HfM" target="_blank"><img
                            src="./files/images/video3.jpg" alt="Account management" title="Account management"></a>
                    </li>
                    <li><a href="http://www.youtube.com/watch?v=qufQ6sLtyAE" target="_blank"><img
                            src="./files/images/video4.jpg" alt="Wizard - add experiment"
                            title="Wizard - add experiment"></a></li>
                </ul>
            </div>
        </div>

        <div class="homepageRightColumn">
            <h2>Login</h2>

            <a href="<c:url value='/forgotten-password.html'/>"><fmt:message key="system.forgottenPassword"/>?</a>

            <form action="j_spring_security_check" class="loginForm" method="post">
                <fieldset>
                    <c:if test="${not empty param.login_error}">
                        <span class="errorMessage"><fmt:message key="system.loginNotSuccessfull"/> <c:out
                                value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.</span>
                    </c:if>
                    <div>
                        <label for="j_username" class="fieldLabel"><fmt:message key="label.email"/></label>
                        <input type="text" name="j_username" id="j_username"/>
                    </div>
                    <div>
                        <label for="j_password" class="fieldLabel"><fmt:message key="label.password"/></label>
                        <input type="password" name="j_password" id="j_password"/>
                    </div>
                    <div class="rememberMeBox">
                        <input type="checkbox" name="_spring_security_remember_me" id="_spring_security_remember_me"/>
                        <label for="_spring_security_remember_me"><fmt:message key="label.rememberMe"/></label>
                    </div>
                    <div>
                        <input type="submit" value="<fmt:message key='button.logIn'/>" class="lightButtonLink"/>
                    </div>
                    <div>OR</div>
                    <div>
                        <a href="<c:url value='/social/login.html' />">
                            <img src="<c:url value='files/images/facebook-login-button.png' />"
                                 alt="facebook login button"/>
                        </a>
                    </div>
                </fieldset>
            </form>
            <h2>Partners</h2>
            <ul class="partners">
                <li>
                <a href="http://www.neuinfo.org/" target="_blank">
                    <img src="http://neuinfo.org/images/registered_with_nif_button.jpg" alt="Registered with NIF" title = "Registered with NIF"
                         style="border:none;"/>
                </a>
                </li>
                <li>
                    <a href="http://www.incf.org/" target="_blank">
                    <img src="files/incf-logo.jpg" alt="INCF National node of Czech Republic"  title = INCF National node of Czech Republic;
                         style="border:none;"/>
                </a>
                </li>
            </ul>
        </div>
    </div>

</security:authorize>
</ui:standardTemplate>
