<%-- 
    Document   : historyTemplate
    Created on : 28.9.2010, 17:27:29
    Author     : pbruha
--%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>




<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@attribute name="pageTitle" required="true"%>
<ui:abstractTemplate pageTitle="${pageTitle}" history="true" allowJQuery="true" search="true" tableSorter="true">
  <div class="leftMenu">
    <jsp:include page="/WEB-INF/jsp/history/menu.jsp"/>
  </div>

  <div class="mainContentWithMenu">
    <jsp:doBody/>
  </div>
</ui:abstractTemplate>