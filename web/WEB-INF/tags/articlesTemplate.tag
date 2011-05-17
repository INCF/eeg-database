<%-- 
    Document   : articlesTemplate
    Created on : 27.4.2010, 18:58:20
    Author     : Jiri Vlasimsky
--%>

<%@tag description="Tag for article template" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="pageTitle" required="true"%>


<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<ui:abstractTemplate pageTitle="${pageTitle}" allowJQuery="true" allowWYSIWYG="true">
  <div class="leftMenu">
    <jsp:include page="/WEB-INF/jsp/articles/menu.jsp"/>
  </div>

  <div class="mainContentWithMenu">
    <jsp:doBody/>
  </div>
</ui:abstractTemplate>
