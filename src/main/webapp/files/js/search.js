
/*
 *
 * JavaScript for advanced search dynamic form
 * @author Jiri Vlasimsky
 */

var fieldCounter = 1;

$(document).ready(function(){
  $("a.addNext").click(function(){
    fieldCounter++;
    var scenarioCombo =
    '<select name="source_' + fieldCounter + '">'+
    '<option value="title" selected>Scenario title</option>'+
    '<option value="minScenarioLength">Min length</option>'+
    '<option value="maxScenarioLength">Max length</option>'+
    '<option value="person">Author</option>'+
    '</select>';
    var experimentCombo =
    '<select name="source_' + fieldCounter + '">'+
    '<option value="startTime">Start date and time</option>'+
    '<option value="endTime">End date and time</option>'+
    '<option value="usedHardware">Used hardware</option>'+
    '<option value="scenario.title">Scenario title</option>'+
    '<option value="personBySubjectPersonId.gender">Gender</option>'+
    '<option value="weather.title">Weather</option>'+
    '<option value="ageMin">Minimum age</option>'+
    '<option value="ageMax">Maximum age</option>'+
    '</select>';
    var peopleCombo =
    '<select name="source_' + fieldCounter + '">'+
    '<option value="givenname">Givenname</option>'+
    '<option value="surname">Surname</option>'+
    '<option value="email">E-mail</option>'+
    '<option value="gender">Gender</option>'+
    '<option value="ageMin">Minimum age</option>'+
    '<option value="ageMax">Maximum age</option>'+
    '<option value="defect">Without defect</option>'+
    '</select>';
    var historyCombo=
    '<select name="source_' + fieldCounter + '">'+
    '<option value="scenario.title" selected>Scenario title</option>'+
    '<option value="fromDateOfDownload">Start date</option>'+
    '<option value="toDateOfDownload">End date</option>'+
    '</select>';
    var selectedCombo;
    switch ($("a.addNext").attr("id")){
      case "scenario" :
        selectedCombo = scenarioCombo;
        break;
      case "experiment" :
        selectedCombo = experimentCombo;
        break;
      case "people" :
        selectedCombo = peopleCombo;
        break;
      case "historySearch" :
        selectedCombo = historyCombo;
        break;
      default:
        alert("Unable to resolve a form template");
    }
    $("table.formTable").append(
      '<tr class="'+fieldCounter+'">'+
      '<td>'+
      '<select name="andOr_' + fieldCounter + '">'+
      '<option value=" and ">AND  </option>'+
      '<option value=" or ">OR</option>'+
      '</select>'+
      '</td>'+
      '<td><input type="text" name="condition_' + fieldCounter + '" class="condition" /></td>'+
      '<td>&nbsp;in&nbsp;</td>'+
      '<td>'+
      selectedCombo +
      '</td>'+
      '<td><a class="delete" onClick="deleteRow(' + fieldCounter + ')">delete</a></td>'+
      '</tr>'
      );
   // $("tr."+fieldCounter).hide();
  //  $("tr."+fieldCounter).fadeIn();

  });

});
function deleteRow(rowNumber) {
  $("tr."+rowNumber).remove();
}


