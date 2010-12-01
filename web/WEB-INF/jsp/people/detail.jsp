<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<ui:personsTemplate pageTitle="pageTitle.personDetail">

    <h1><fmt:message key="pageTitle.personDetail"/></h1>

    <table class="standardValueTable">
        <tr>
            <th><fmt:message key="label.name"/></th>
            <td>${personDetail.givenname} ${personDetail.surname}</td>
        </tr>
        <tr>
            <th><fmt:message key="label.gender"/></th>
            <td>
                <c:if test="${personDetail.gender == 'M'}">
                    <fmt:message key="label.gender.male"/>
                </c:if>
                <c:if test="${personDetail.gender == 'F'}">
                    <fmt:message key="label.gender.female"/>
                </c:if>
            </td>
        </tr>
        <tr>
            <th><fmt:message key="label.dateOfBirth"/></th>
            <td><fmt:formatDate value="${personDetail.dateOfBirth}" pattern="dd.MM.yyyy" /></td>
        </tr>
        <tr>
            <th><fmt:message key="label.email"/></th>
            <td>${personDetail.email}</td>
        </tr>
        <tr>
            <th><fmt:message key="label.phoneNumber"/></th>
            <td>${personDetail.phoneNumber}</td>
        </tr>
        <tr>
            <th><fmt:message key="label.username"/></th>
            <td>${personDetail.username}</td>
        </tr>
        <tr>
            <th><fmt:message key="label.userNote"/></th>
            <td>${personDetail.note}</td>
        </tr>
    </table>

    <h2><fmt:message key="heading.hearingDefects"/></h2>
    <table class="dataTable" style="width: 250px;">
        <thead>
            <tr>
                <th><!-- just a heading without title --></th>
            </tr>
        </thead>
        <c:forEach items="${personDetail.hearingImpairments}" var="hearingImpairment">
            <tr>
                <td>${hearingImpairment.description}</td>
            </tr>
        </c:forEach>
    </table>

    <h2><fmt:message key="heading.eyesDefects"/></h2>
    <table class="dataTable" style="width: 250px;">
        <thead>
            <tr>
                <th><!-- just a heading without title --></th>
            </tr>
        </thead>
        <c:forEach items="${personDetail.visualImpairments}" var="visualImpairment">
            <tr>
                <td>${visualImpairment.description}</td>
            </tr>
        </c:forEach>
    </table>

    <h2><fmt:message key="heading.optionalParameters"/></h2>
     <table class="dataTable" style="width: 450px;">
        <thead>
            <tr>
                <th style="width: 250px;"><fmt:message key="dataTable.heading.personOptionalParamName"/></th>
                <th><fmt:message key="dataTable.heading.personOptionalParamValue"/></th>
            </tr>
        </thead>
        <c:forEach items="${personDetail.personOptParamVals}" var="additionalParameter">
            <tr>
                <td>${additionalParameter.personOptParamDef.paramName}</td>
                <td>${additionalParameter.paramValue}</td>
            </tr>
        </c:forEach>
    </table>

    <div class="actionBox">
        <a href="<c:url value='add-hearing-defect.html?personId=${personDetail.personId}'/>" class="lightButtonLink"><fmt:message key="button.addHearingImpairment"/></a>
        <a href="<c:url value='add-eyes-defect.html?personId=${personDetail.personId}'/>" class="lightButtonLink"><fmt:message key="button.addVisualImpairment"/></a>
        <a href="<c:url value='add-optional-parameter.html?personId=${personDetail.personId}'/>" class="lightButtonLink"><fmt:message key="button.addOptionalParameter"/></a>
    </div>

</ui:personsTemplate>
