<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<c:out value="${fn:length(requestScope.node.children)}" />
