<%-- 
    Document   : checkBox
    Created on : 17.3.2010, 17:01:07
    Author     : pbruha
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="path" required="true"%>
<%@attribute name="labelKey" required="true" %>

<div class="checkBox">
  <form:label path="${path}" cssErrorClass="error"><fmt:message key="${labelKey}"/></form:label>

  <form:checkbox path="${path}" cssErrorClass="error"/>

  <form:errors path="${path}" cssClass="error"/>
</div>