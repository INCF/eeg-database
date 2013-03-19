<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="cs" dir="ltr">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <!-- change initial title -->
    <title>Scenario</title>

    <link href="<c:url value='/files/wizard-style.css'/>" type="text/css" rel="stylesheet" media="all">
    <link href="<c:url value='/files/jquery-ui-1.10.1.custom.min.css'/>" type="text/css" rel="stylesheet" media="all">
    <!-- place for alternative media styles (print?) -->

    <script type="text/javascript" src="<c:url value='/files/js/date.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/files/js/jquery-1.9.1.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/files/js/jquery-ui-1.10.1.custom.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/files/js/jquery.validate.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/files/js/wizard.js'/>"></script>
</head>

<body>

<div class="container">

    <!-- navigation -->
    <ul class="nav">

        <li class="selected">
            <a class="tab" name="scenarioTab">
                Scénář
            </a>
        </li>

        <li>
            <a class="tab" name="environment">
                Prostředí
            </a>
        </li>

        <li>
            <a class="tab" name="result">
                Výsledek
            </a>
        </li>
    </ul>
    <!-- navigation end -->


    <!-- main page content -->
    <div class="content">

        <div class="scenarioTab tabContent">
            <form id="scenarioForm" action="" method="post">

                <div class="form-item">
                    <label for="scenario">Scénář</label>
                    <input type="text" id="scenario" class="required autocomplete" autosrc="/experiments/wizard/json/scenario">
                    <input type="button" id="newScenario" name="newScenario" class="openurl" data="/experiments/wizard/addScenario" value="Přidat">
                </div>

                <div class="form-item">
                    <label for="groupSelect">Skupina</label>
                    <input type="text" id="groupSelect" class="required autocomplete" autosrc="/experiments/wizard/json/group">
                    <input type="button" id="newGroup" name="newGroup" class="openurl" data="/experiments/wizard/addGroup" value="Přidat">
                    <input type="checkbox" name="defaultGroup" value="isDefault">Nastavit jako defaultní
                </div>

                <div class="form-item added">
                    <label for="projectType">Součást projektu</label>
                    <input type="text" id="projectType" class="required autocomplete" autosrc="/experiments/wizard/json/project">
                    <input type="button" id="newProject" name="newProject" class="openurl" data="/experiments/wizard/addProjectType" value="Přidat">
                </div>

                <div class="form-item">
                    <label for="startDay">Začátek</label>
                    <input type="date" name="startDay" class="required date" id="startDay">
                    <input type="time" name="startTime" class="required" id="startTime">
                </div>

                <div class="form-item">
                    <label for="endDay">Konec</label>
                    <input type="date" name="endDay" class="required date" id="endDay">
                    <input type="time" name="endTime" class="required" id="endTime">
                </div>

                <div class="form-item">
                    <label for="subjects">Testovací osoba</label>
                    <input type="text" class="required autocomplete" id="subjects" autosrc="/experiments/wizard/json/person">
                    <input type="button" id="addTested" class="openurl" data="/experiments/wizard/addPerson" name="addTested" value="Přidat">
                </div>
                
                <div class="form-item added">
                    <label for="stimulus">Podnět</label>
                    <input type="text" name="stimulus" id="stimulus" class="required autocomplete multiple" autosrc="/experiments/wizard/json/stimulus">
                    <input type="button" name="newStimulus" class="openurl" data="/experiments/wizard/addStimulus" value="Přidat">
                </div>

                <div class="form-item">
                    <label for="coExperimenters">Další výzkumníci</label>
                    <input type="text" class="required autocomplete multiple"
                           id="coExperimenters" autosrc="experimentators.jsp">
                </div>
            </form>
        </div>

        <div class="environment tabContent hidden">
            <form action="" method="post" autocomplete="on">

                <div class="form-item">
                    <label for="hardware">Hardware</label>
                    <select name="hardware" id="hardware" class="multiple required" multiple="multiple" autosrc="/experiments/wizard/json/hardware">
                        <option value="hw1">HW 1</option>
                        <option value="hw2">HW 2</option>
                        <option value="hw3">HW 3</option>                    
                    </select>
                    <input type="button" class="openurl" data="/experiments/wizard/addHW" name="newHardware" value="Přidat">
                </div>

                <div class="form-item added">
                    <label for="software">Software</label>
                    <select name="software" id="software" class="multiple" multiple="multiple" autosrc="/experiments/wizard/json/software">
                        <option value="sw1">SW 1</option>
                        <option value="sw2">SW 2</option>
                        <option value="sw3">SW 3</option>
                    </select>
                    <input type="button" class="openurl" data="/experiments/wizard/addSW" name="newSoftware" value="Přidat">
                </div>

               <div class="form-item">
                    <label for="weather">Počasí</label>
                    <select name="weather" id="weather">
                        <option value="wea0" selected="selected">Počasí nebylo vybráno</option>
                        <option value="wea1">Slunečno</option>
                        <option value="wea2">Oblačno</option>
                        <option value="wea3">Zataženo</option>
                        <option value="wea4">Přeháňky</option>
                    </select>
                </div>

                <div class="form-item">
                    <label for="weatherNote" class="long-label">Poznámka k počasí</label>
                    <input type="text" id="weatherNote" name="weatherNote" class="after-long-label"/>
                </div>

                <div class="form-item">
                    <label for="temperature">Teplota</label>
                    <input type="number" id="temperature" name="temperature" required/>
                </div>
                
                <div class="form-item added">
                    <label for="disease">Nemoci</label>
                    <input type="text" name="disease" id="disease" class="required autocomplete" autosrc="/experiments/wizard/json/disease">
                    <input type="button" name="newDisease" class="openurl" data="/experiments/wizard/addDisease" value="Přidat">
                </div>

                <div class="form-item added">
                    <label for="pharmaceutical">Léky</label>
                    <input type="text" name="pharmaceutical" id="pharmaceutical" class="required autocomplete multiple" autosrc="/experiments/wizard/json/pharmaceutical">
                    <input type="button" name="newPharmaceutical" class="openurl" data="/experiments/wizard/addPharmaceutical" value="Přidat">
                </div>

                <div class="form-item">
                    <label for="privateNote">Osobní poznámka</label>
                    <input type="text" id="privateNote" name="privateNote"/>
                </div>
            </form>
        </div>

        <div class="result tabContent hidden">
            <form action="" method="post" autocomplete="on">

                <div class="form-item">
                    <label for="resultFile">Soubor výsledků</label>
                    <input type="file" name="resultFile" id="resultFile"/>
                </div>
            </form>
        </div>

    </div>
    <!-- main page content end -->


</div>
</body>
</html>