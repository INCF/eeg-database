<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="" method="post" autocomplete="on">
    <div class="form-item">

        <h1>Nový software</h1>

        <div class="form-sub-item">
            <label for="swTitle">Název</label>
            <input type="text" name="swTitle"/>
        </div>

        <div class="form-sub-item">
            <label for="swDescr">Popisek</label>
            <textarea rows="3" cols="40" name="swDescr"></textarea>
        </div>

        <div class="form-sub-item">
            <label for="isDefault">Defaultní</label>
            <input type="number" name="isDefault"/>
        </div>

        <div class="close"></div>

        <label for="submitProjectType"></label>
        <input type="reset" class="closeDialog" name="cancelProjectType" value="Zrušit">
        <input type="submit" class="closeDialog" name="submitProjectType" value="Přidat">
    </div>
</form>