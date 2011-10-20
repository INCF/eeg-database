<%@tag description="Formats the date into date and time format." pageEncoding="UTF-8"%>

<%@attribute name="value" required="true" type="java.util.Date"%>

<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatDate value="${value}" pattern="dd.MM.yyyy, HH:mm" />