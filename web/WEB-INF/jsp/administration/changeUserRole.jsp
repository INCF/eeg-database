<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="af" tagdir="/WEB-INF/tags/autoforms/" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<ui:administrationTemplate pageTitle="pageTitle.changeUserRole">
  <h1><fmt:message key="pageTitle.changeUserRole"/></h1>

  <c:url value="change-user-role.html" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="changeUserRole" cssClass="standardForm">
    <fieldset>

      <af:textField path="userName" labelKey="label.userName"/>


      <div class="selectBox">
        <form:label path="userRole" cssErrorClass="error"><fmt:message key="label.userRole"/></form:label>

        <form:select path="userRole">
          <form:option value="-1"><fmt:message key="select.option.noUserRoleSelected"/></form:option>
          <form:option value="ROLE_USER"><fmt:message key="select.option.roleUser"/></form:option>
          <form:option value="ROLE_ADMIN"><fmt:message key="select.option.roleAdmin"/></form:option>
        </form:select>

        <form:errors path="userRole" cssClass="error" />
      </div>

      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.changeRole'/>" class="submitButton lightButtonLink" />
      </div>

    </fieldset>
  </form:form>
</ui:administrationTemplate>