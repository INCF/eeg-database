<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="org.springframework.security.core.AuthenticationException" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<ui:standardTemplate pageTitle="pageTitle.logIn">
    <form action="j_spring_security_check" class="loginForm" method="post">
        <fieldset>
            <c:if test="${not empty param.login_error}">
                <span class="errorMessage"><fmt:message key="system.loginNotSuccessfull"/> <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.</span>
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
                <input type="checkbox" name="_spring_security_remember_me" id="_spring_security_remember_me"/> <label for="_spring_security_remember_me"><fmt:message key="label.rememberMe"/></label>
            </div>
                <input type="submit" value="<fmt:message key='button.logIn'/>" class="lightButtonLink" />
        </fieldset>
    </form>
</ui:standardTemplate>
