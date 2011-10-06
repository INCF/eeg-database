<%@tag description="Formats the group authority codes to readable text" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="code" required="true"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
  <c:when test="${code == 'GROUP_ADMIN'}">
    <fmt:message key="tag.groupAuthority.groupAdmin"/>
  </c:when>
  <c:otherwise>
    <c:choose>
      <c:when test="${code == 'GROUP_EXPERIMENTER'}">
        <fmt:message key="tag.groupAuthority.groupExperimenter"/>
      </c:when>
      <c:otherwise>
        <c:choose>
          <c:when test="${code == 'GROUP_READER'}">
            <fmt:message key="tag.groupAuthority.groupReader"/>
          </c:when>
          <c:otherwise>
            <c:out value="${code}"/>
          </c:otherwise>
        </c:choose>
      </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>
