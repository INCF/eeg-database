<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<ul class="commonButtonMenu">
    <li><a href="<c:url value='/administration/change-user-role.html'/>"><fmt:message key='menuItem.changeUserRole'/></a></li>
</ul>
