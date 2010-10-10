<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="path" required="true"%>
<%@attribute name="labelKey" required="true" %>

<div class="textBox">
  <form:label path="${path}" cssErrorClass="error"><fmt:message key="${labelKey}"/></form:label>

  <form:input path="${path}" cssErrorClass="error"/>

  <form:errors path="${path}" cssClass="error"/>
</div>
