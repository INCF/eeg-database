<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="footer">
    <fmt:message key='web.footer.note'/>
    <br/>
    <fmt:message key='web.footer.copyright.part1'/> <a href="<fmt:message key='web.footer.copyright.linkAddress'/>"
                                                       title="<fmt:message key='web.footer.copyright.linkTitle'/>"><fmt:message
        key='web.footer.copyright.linkTitle'/></a> <fmt:message key='web.footer.copyright.part2'/>

</div>
<div class="nifButton">
    <p>
        <a href="http://www.neuinfo.org/">
            <img src="http://neuinfo.org/images/registered_with_nif_button.jpg" alt="Registered with NIF"
                 style="border:none;"/>
        </a>
    </p>
</div>
