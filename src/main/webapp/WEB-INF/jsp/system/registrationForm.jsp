<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:standardTemplate pageTitle="pageTitle.registration" dateOfBirthCalendar="true">

  <h1><fmt:message key="pageTitle.registration"/></h1>

  <c:url value="registration.html" var="formUrl"/>
  <form:form action="${formUrl}" method="post" commandName="registration" cssClass="standardInputForm" name="registration">
    <fieldset>

      <div class="itemBox">
        <form:label path="givenname" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.name"/></form:label>

        <form:input path="givenname" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="givenname" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="surname" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.surname"/></form:label>

        <form:input path="surname" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="surname" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="dateOfBirth" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.dateOfBirth"/></form:label>

        <form:input path="dateOfBirth" cssClass="textField dateField" cssErrorClass="textField dateField errorField" />

        <span class="note"><fmt:message key="form.note.dateFormatDDMMYYYY"/></span>

        <form:errors path="dateOfBirth" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="gender" cssClass="radio2ButtonsLabel" cssErrorClass="radio2ButtonsLabel errorLabel"><fmt:message key="label.gender"/></form:label>

        <form:radiobutton path="gender" value="M" /> <form:label path="gender" for="gender1"><fmt:message key="label.gender.male"/></form:label>
        <form:radiobutton path="gender" value="F" /> <form:label path="gender" for="gender2"><fmt:message key="label.gender.female"/></form:label>

        <form:errors path="gender" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="email" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.email"/></form:label>

        <form:input path="email" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="email" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="username" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.username"/></form:label>

        <form:input path="username" cssClass="textField" cssErrorClass="textField errorField" />

        <span class="note"><fmt:message key="form.note.usernameFormatNote"/></span>

        <form:errors path="username" cssClass="errorBox" />
      </div>

      <div class="itemBox">
        <form:label path="password" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.password"/></form:label>

        <form:password path="password" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="password" cssClass="errorBox" />
      </div>
      <div class="itemBox">
        <form:label path="password2" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.repeatPassword"/></form:label>

        <form:password path="password2" cssClass="textField" cssErrorClass="textField errorField" />

        <form:errors path="password2" cssClass="errorBox" />
      </div>
      
      <div class="itemBox">
       <form:label path="controlText" cssClass="textFieldLabel" cssErrorClass="textFieldLabel errorLabel"><fmt:message key="label.controlText"/></form:label>
        <input type="text" name="j_captcha_response"/>
      </div>
      
        <img src="captcha.html?captchaId=${pageContext.session.id}" align="absmiddle" hspace="150" alt="cap"/>
       <c:forEach items="${status.errorMessages}" var="errorMessage">
          <li style="color:red"><c:out value="${errorMessage}"/></li>
       </c:forEach>
       
     

      <div class="itemBox">
        <input type="submit" value="<fmt:message key='button.createAccount'/>" class="submitButton lightButtonLink" />
      </div>
    </fieldset>
  </form:form>
</ui:standardTemplate>
