<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="" method="post" autocomplete="on">
    <div class="form-item">

        <h1>Nový testovací subjekt</h1>

        <div class="form-sub-item">
            <label for="givenname">Jméno</label>
            <input type="text" id="givenname" class="required" name="givenname" required/>
        </div>

        <div class="form-sub-item">
            <label for="surname">Příjmení</label>
            <input type="text" id="surname" class="required" name="surname" required/>
        </div>

        <div class="form-sub-item">
            <label for="dateOfBirth" required>Datum narození</label>
            <input type="date" class="required date" id="dateOfBirth" name="dateOfBirth" required>
        </div>

        <div class="form-sub-item">
            <label for="gender">Pohlaví</label>
            <ol class="radio-group">
                <li>
                    <input name="gender" type="radio" value="M" checked="checked">
                    <label for="male">Muž</label>
                </li>
                <li>
                    <input name="gender" type="radio" value="F">
                    <label for="female">Žena</label>
                </li>
            </ol>
            <div class="close"></div>
        </div>

        <div class="form-sub-item added">
            <label for="education">Vzdělání</label>
            <select name="education" id="education">
                <option value="edu0" selected="selected">Vzdělání nebylo vybráno</option>
                <option value="edu1">Základní</option>
                <option value="edu2">Středoškolské</option>
                <option value="edu3">Středoškolské s maturitou</option>
                <option value="edu4">Vysokoškolské</option>
            </select>
        </div>

        <div class="form-sub-item">
            <label for="email">E-mail</label>
            <input type="email" id="email" class="required mail" name="email"/>
        </div>

        <div class="form-sub-item">
            <label for="phoneNumber">Tel. číslo</label>
            <input type="text" id="phoneNumber" class="required phone" name="phoneNumber"/>
        </div>

        <div class="form-sub-item">
            <label for="note">Poznámka</label>
            <input type="text" id="note" name="note" class="required"/>
        </div>

        <div class="close"></div>

        <label for="submitProjectType"></label>
        <input type="reset" class="closeDialog" name="cancelProjectType" value="Zrušit">
        <input type="submit" class="closeDialog" name="submitProjectType" value="Přidat">
    </div>
</form>