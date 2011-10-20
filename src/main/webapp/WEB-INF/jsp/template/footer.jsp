<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="footer">
    <fmt:message key='web.footer.note'/>
    <br/>
    <fmt:message key='web.footer.copyright.part1'/>
    <a href="<fmt:message key='web.footer.copyright.linkAddress'/>" title="<fmt:message key='web.footer.copyright.linkTitle'/>">
    <jsp:useBean id="now" class="java.util.Date" scope="page"/>
    <fmt:message
        key='web.footer.copyright.linkTitle'/></a> <fmt:message key='web.footer.copyright.part2'/><fmt:formatDate value="${now}" pattern="yyyy"/>
</div>

