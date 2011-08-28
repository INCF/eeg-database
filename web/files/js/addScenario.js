/**
 * Created by IntelliJ IDEA.
 * User: Jan Koren
 * Date: 28.5.11
 * Time: 23:25
 * To change this template use File | Settings | File Templates.
 */

$(document).ready(function() {    //nacteni

    $('#isScenarioXml').click(function() {
        if ($("#isScenarioXml").is(':checked')) {
            $("#dataFileXml").show();
            $("#schemaSelect").show();
            $("#dataFile").hide();
            $("#schemaList").hide();

            if ($("#fromSchemaList").is(':checked')) {
                $("#schemaList").show();
                $.cookie("xmlWithSchema", "yes");
            }

            $.cookie("xmlOptionSelected", "yes");
            $.cookie("xmlWithSchema", "no");
        }
        else {
            $('#dataFile').show();
            $('#dataFileXml').hide();
            $('#schemaSelect').hide();

            $.cookie("xmlOptionSelected", "no");
        }
    });

    //click on the radio button to select a schema
    $('#fromSchemaList').click(function() {
        if ($('#fromSchemaList').is(':checked')) {
            $('#schemaList').show();
            //$("#schemaDescription").show();
            $.cookie("xmlWithSchema", "yes");
        }
    });

    //click on the radio button not to use any schema
    $('#noSchema').click(function() {
        if ($('#noSchema').is(':checked')) {
            $('#schemaList').hide();
            //$('#schemaDescription').hide();
            $.cookie("xmlWithSchema", "no");
        }
    });

    //COOKIES
    var xmlOptionSelected = $.cookie("xmlOptionSelected");
    var xmlWithSchema = $.cookie("xmlWithSchema");

    if(xmlOptionSelected == "yes") {
        $("#isScenarioXml").attr('checked', true);
        $("#dataFileXml").show();
        $("#schemaSelect").show();
        $("#dataFile").hide();
        $("#schemaList").hide();

        if(xmlWithSchema == "yes") {
            $("#fromSchemaList").attr('checked',true);
            $("#schemaList").show();
        }
        else {
            $("#fromSchemaList").attr('checked',false);
            $("#noSchema").attr('checked',true);
        }
    }
    else {
        $("#isScenarioXml").attr('checked', false);
        $('#dataFile').show();
        $("#schemaList").show();
        $('#dataFileXml').hide();
        $('#schemaSelect').hide();

        if(xmlWithSchema == "yes") {
            $("#fromSchemaList").attr('checked',true);
            $("#schemaList").show();
        }
        else {
            $("#fromSchemaList").attr('checked',false);
        }
    }
});
