/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 28.5.11
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function() {
  $('#schemaSelect').hide();
  $('#dataFileXml').hide();
  $(':submit').attr('disabled',true);
  $("#fromSchemaList").attr("checked", "checked");


  //kliknuti na checkbox kvuli tomu, jestli je scenar XML nebo ne
  $('#isScenarioXml').click(function() {
    if($('#isScenarioXml').is(':checked')) {
      $('#dataFileXml').show();
      $('#schemaSelect').show();
      $('#dataFile').hide();
      $('#newSchema').hide();
      $('#schemaList').hide();

      if($('#fromSchemaList').is(':checked')) {
        $('#schemaList').show();
      }

      if($('#schemaAddNew').is(':checked')) {
      $('#newSchema').show();
      }
    }
    else {
      $('#dataFile').show();
      $('#dataFileXml').hide();
      $('#schemaSelect').hide();
    }
  });

// klik na radio button pro vyber schematu
  $('#fromSchemaList').click(function() {
    if($('#fromSchemaList').is(':checked')) {
      $('#schemaList').show();
      $('#newSchema').hide();
    }
  });

  //kliknuti na radio button pro pridani noveho schematu
  $('#schemaAddNew').click(function() {
    if($('#schemaAddNew').is(':checked')) {
      $('#newSchema').show();
      $('#schemaList').hide();
    }
  });

});
