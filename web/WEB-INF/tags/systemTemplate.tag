<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pageTitle" required="true"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:abstractTemplate pageTitle="${pageTitle}">
    <div class="leftMenu">
        <jsp:include page="/WEB-INF/jsp/system/menu.jsp"/>
    </div>

    <div class="mainContentWithMenu">
        <jsp:doBody/>
    </div>
</ui:abstractTemplate>
