<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="" method="post" autocomplete="on">

    <div class="form-item">
        <label for="groupSelect">Skupina</label>
        <select name="groupSelect">
            <option value="gr0" selected="selected">Výzkumná skupina nevybrána</option>
            <option value="grA">Skupina A</option>
            <option value="grB">Skupina B</option>
            <option value="grC">Skupina C</option>
        </select>
    </div>

    <div class="form-item">
        <label for="scenarioTitle" class="text title">Název</label>
        <input type="text" name="scenarioTitle" required/>
    </div>

    <div class="form-item">
        <label for="length">Délka</label>
        <input type="text" name="length" required/>
    </div>

    <div class="form-item">
        <label for="scenarioDescription">Popis</label>
        <textarea rows="3" cols="40" name="scenarioDescription"></textarea>
    </div>

    <div class="form-item">
        <label for="dataFileAvailable" class="long-label">Dostupný soubor s daty</label>
        <input type="checkbox" name="dataFileAvailable"/>
    </div>

    <div class="form-item">
        <label for="isScenarioXml" class="long-label">Scénář jako XML</label>
        <input type="checkbox" name="isScenarioXML"/>
    </div>

    <div class="form-item">
        <label for="dataFile">Soubor s daty</label>
        <input type="file" name="dataFile"/>
    </div>

    <div class="form-item">
        <label for="dataFileXml">Soubor s daty (XML)</label>
        <input type="file" name="dataFileXml"/>
    </div>


    <div class="form-item">
        <label for="scenarioSchemaLabel" id="scenarioschemalabel">Schéma</label>

        <ol class="radio-group">
            <li><input type="radio" name="noSchema" value="noSchema" id="noSchema">žádné</li>
            <li><input type="radio" name="fromSchemaList" value="fromList" id="fromSchemaList">schéma ze seznamu
            </li>
        </ol>

        <select name="scenarioSchema" id="schemaList" class="after-long-label">
            <option value="-1">Žádné schéma nevybráno</option>
            <option value="sch1">Schéma 1</option>
            <option value="sch1">Schéma 2</option>
        </select>
    </div>

    <input type="reset" class="closeDialog" name="cancelProjectType" value="Zrušit">
    <input type="submit" class="closeDialog" name="submitProjectType" value="Přidat">

</form>