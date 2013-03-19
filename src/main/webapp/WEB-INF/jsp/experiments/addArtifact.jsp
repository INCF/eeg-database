<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="" method="post" autocomplete="on">
    <div class="form-item">

        <h1>Nový artefakt</h1>

        <div class="form-sub-item">
            <label for="compensation">Kompenzace</label>
            <textarea rows="3" cols="40" name="compensation"></textarea>
        </div>

        <div class="form-sub-item">
            <label for="rejectCondition" class="long-label">Podmínky zahození</label>
            <textarea rows="3" cols="40" name="rejectCondition" class="after-long-label"></textarea>
        </div>

        <div class="close"></div>

        <label for="submitProjectType"></label>
        <input type="reset" class="closeDialog" name="cancelProjectType" value="Zrušit">
        <input type="submit" class="closeDialog" name="submitProjectType" value="Přidat">
    </div>
</form>