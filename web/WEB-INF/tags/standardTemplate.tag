<%@tag description="EEGbase" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pageTitle" required="true"%>
<%@attribute name="dateOfBirthCalendar" %>
<%@attribute name="mainPage" %>
<%@attribute name="allowJQuery" %>

<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:abstractTemplate pageTitle="${pageTitle}" dateOfBirthCalendar="${dateOfBirthCalendar}" mainPage="true" allowJQuery ="true">
    <div class="mainContent">
        <jsp:doBody/>
    </div>
</ui:abstractTemplate>
