<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<ui:dataTemplate pageTitle="pageTitle.addDataFile" jaddData="true">

    <h1><fmt:message key="pageTitle.addDataFile"/></h1>

    <c:url value="add.html?experimentId=${addData.measurationId}" var="formUrl"/>
    <form:form action="${formUrl}" method="post" commandName="addData" cssClass="standardInputForm" name="addData"
               enctype="multipart/form-data">
        <fieldset>

            <table class="formTable">

                <f:errorBox/>

                <form:hidden path="measurationId"/>

            <%--     <tr>
                    <div class="itemBox">
                        <td>

                           <form:label path="description" cssClass="textFieldLabel"
                                        cssErrorClass="textFieldLabel errorLabel">
                                <fmt:message
                                        key="label.description"/>
                            </form:label>
                        </td>
                        <td>
                            <form:textarea path="description" cssClass="textareaField"
                                           cssErrorClass="textareaField errorField"
                                           rows="5" cols="40"/>

                            <form:errors path="description" cssClass="errorBox"/>

                        </td>
                    </div>
                </tr>       --%>
                <tr>
                    <div class="itemBox">
                        <td>

                            <form:label path="dataFile" cssClass="fileFieldLabel"
                                        cssErrorClass="fileFieldLabel errorLabel">
                                <fmt:message key="label.dataFile"/>
                            </form:label>
                        </td>
                        <td>
                            <input type="file" name="dataFile" class="fileField"/>

                            <form:errors path="dataFile" cssClass="errorBox"/>

                        </td>
                    </div>
                </tr>
            </table>
            <a class="addFile"><fmt:message key="label.addFile"/></a>

            <div class="itemBox">
                <input type="submit" value="<fmt:message key='button.addDataFile'/>"
                       class="submitButton lightButtonLink"/>
            </div>


        </fieldset>
    </form:form>
</ui:dataTemplate>