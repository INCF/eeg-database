<%@tag description="Formats the global authority codes to readable text" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="code" required="true"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
  <c:when test="${code == 'ROLE_ADMIN'}">
    <fmt:message key="tag.globalAuthority.roleAdmin"/>
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${code == 'ROLE_USER'}">
        <fmt:message key="tag.globalAuthority.roleUser"/>
      </c:when>
      <c:otherwise>
        <c:out value="${code}"/>
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>
