<%--
  Created by IntelliJ IDEA.
  User: pbruha
  Date: 10.3.11
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ui" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="f" tagdir="/WEB-INF/tags/format/" %>
<ui:experimentsTemplate pageTitle="pageTitle.addDataFile" jaddData="true">

    <h1><fmt:message key="pageTitle.addDataFile"/></h1>
     <c:url value="http://eegdatabase.kiv.zcu.cz/experiments/add-experiment.html" var="next"/>

    <form:form method="post" commandName="addExperimentWizard" cssClass="standardInputForm" name="addExperimentWizard"
               enctype="multipart/form-data" action="${next}">
        <fieldset>
            <table class="formTable">

                <f:errorBox/>

                <form:hidden path="measurationId"/>


                <tr>
                    <div class="itemBox">
                        <td>

                            <form:label path="samplingRate" cssClass="textFieldLabel"
                                        cssErrorClass="textFieldLabel errorLabel">
                                <fmt:message
                                        key="label.samplingRate"/>
                            </form:label>
                        </td>
                        <td>
                            <form:input path="samplingRate" cssClass="textField dateField"
                                        cssErrorClass="textField dateField errorField"/> Hz

                            <form:errors path="samplingRate" cssClass="errorBox"/>

                        </td>
                    </div>
                </tr>
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

            <div class="actionBox">
                <input type="submit" value="Previous" name="_target1" class="submitButton lightButtonLink"/>
                <input type="submit" value="Finish" name="_finish" class="lightButtonLink"/>
                <input type="submit" value="Cancel" name="_cancel" class="lightButtonLink"/>
            </div>


        </fieldset>
    </form:form>
</ui:experimentsTemplate>