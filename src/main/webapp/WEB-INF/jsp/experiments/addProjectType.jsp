<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="" method="post" autocomplete="on">

    <div class="form-item">

        <h1>Nový typ projektu</h1>

        <div class="form-sub-item">
            <label for="projectTypeTitle">Název:</label>
            <input type="text" name="projectTypeTitle"/>
        </div>

        <div class="form-sub-item">
            <label for="projectTypeDescr">Popisek:</label>
            <textarea rows="3" cols="40" name="projectTypeDescr"></textarea>
        </div>

        <div class="close"></div>

        <label for="submitProjectType"></label>
        <input type="reset" class="closeDialog" name="cancelProjectType" value="Zrušit">
        <input type="submit" class="closeDialog" name="submitProjectType" value="Přidat">
    </div>
</form>