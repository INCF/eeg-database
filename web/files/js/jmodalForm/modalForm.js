var selectedId = -1;
$(function() {
    // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
    $("#dialog:ui-dialog").dialog("destroy");

    var weatherTitle = $("#weatherTitle"),
            weatherDescription = $("#weatherDescription"),
            allFields = $([]).add(weatherTitle).add(weatherDescription),
            tips = $(".validateTips");
    var hardwareTitle = $("#hardwareTitle"),
            hardwareType = $("#hardwareType"),
            hardwareDescription = $("#hardwareDescription"),
            allHardwareFields = $([]).add(hardwareTitle).add(hardwareType).add(hardwareDescription),
            hardwareTips = $(".validateTips");
     var givenname = $("#givenname"),
            surname = $("#surname"),
            dateOfBirth = $("#dateOfBirth"),
            email = $("#email"),
            phoneNumber = $("#phoneNumber"),
            note = $("#note"),
            allPersonsFields= $([]).add(givenname).add(surname).add(dateOfBirth).add(email).add(phoneNumber).add(note),
            personsTips = $(".validateTips");
    var scenarioTitle = $("#scenarioTitle"),
            length = $("#length"),
            scenarioDescription = $("#scenarioDescription"),
            allScenariosFields= $([]).add(scenarioTitle).add(length).add(scenarioDescription),
            scenariosTips = $(".validateTips");

    function updateTips(t) {
        tips
                .text(t)
                .addClass("ui-state-highlight");
        setTimeout(function() {
            tips.removeClass("ui-state-highlight", 1500);
        }, 500);
    }

    function checkLength(o, n, min, max) {
        if (o.val().length > max || o.val().length < min) {
            o.addClass("ui-state-error");
            updateTips("Length of " + n + " must be between " +
                    min + " and " + max + ".");
            return false;
        } else {
            return true;
        }
    }

    function checkRegexp(o, regexp, n) {
        if (!( regexp.test(o.val()) )) {
            o.addClass("ui-state-error");
            updateTips(n);
            return false;
        } else {
            return true;
        }
    }

    function get_hostname_from_url(url) {
        return url.match(/:\/\/(.[^/]+)/)[1];
    }

    function get_url(url) {
        var hn = window.location.hostname;
        if (hn == "eegdatabase.kiv.zcu.cz") {
            url = 'http://eegdatabase.kiv.zcu.cz';
        }
        else if (hn == "localhost") {
            url = 'http://localhost:8080/EEGDatabase/';
        }
        else {
            url = 'http://147.228.64.173:8080/EEGDatabase/';
        }
        return url;
    }

    $("#dialog-form-weather").dialog({
        autoOpen: false,
        height: 300,
        width: 350,
        modal: true,
        buttons: {
            "Create new weather": function() {
                var bValid = true;
                allFields.removeClass("ui-state-error");

                bValid = bValid && checkLength(weatherTitle, "title", 3, 30);
                bValid = bValid && checkLength(weatherDescription, "description", 6, 80);

                bValid = bValid && checkRegexp(weatherTitle, /^[a-z]([0-9a-z_ ])+$/i, "Weather title may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/

                bValid = bValid && checkRegexp(weatherDescription, /^([0-9a-zA-Z ])+$/, "Weather description field only allow : a-z 0-9");


                if (bValid) {
                    var req = "title=" + weatherTitle.val() + "&description=" + weatherDescription.val();
                    var addNewWeather = 'experiments/addNewWeather.html';
                    var url;
                    url = get_url(url);

                    url = url + addNewWeather;

                    $.ajax({
                        type: "GET",
                        url: url,
                        cache: false,
                        data: req,
                        async:false,
                        beforeSend: function() {

                        },
                        success: function(data) {
                            var newId;
                            var answer = data.split(':');
                            //alert(url);
                            if ((answer[1].substring(0, 4)) == "true") {
                                //alert(answer[2].substring(0, answer[2].length - 1));
                                newId = parseInt(answer[2].substring(0, answer[2].length - 1), 10);
                                $("#selectWeather").append(
                                        '<option selected="' + newId + '" value="' + newId + '">' + weatherTitle.val() +
                                                '</option>'
                                        )
                                        ;
                            }
                            else {
                                alert("Data not saved!");
                            }
                        },

                        error: function (xmlHttpRequest, textStatus, errorThrown) {
                            if (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0) {
                                return;  // it's not really an error
                            }
                            else {
                                alert("Error E3:" + xmlHttpRequest.status);
                            }
                        }
                    });
                    $(this).dialog("close");
                }
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        },
        close:
                function() {
                    allFields.val("").removeClass("ui-state-error");
                }
    });

    $("#dialog-form-hardware").dialog({
        autoOpen: false,
        height: 360,
        width: 350,
        modal: true,
        buttons: {
            "Create new hardware": function() {
                var bValid = true;
                allHardwareFields.removeClass("ui-state-error");

                bValid = bValid && checkLength(hardwareTitle, "title", 3, 30);
                bValid = bValid && checkLength(hardwareType, "type", 3, 30);
                bValid = bValid && checkLength(hardwareDescription, "description", 6, 80);

                bValid = bValid && checkRegexp(hardwareTitle, /^[a-z]([0-9a-z_ ])+$/i, "Hardware title may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                bValid = bValid && checkRegexp(hardwareTitle, /^[a-z]([0-9a-z_ ])+$/i, "Hardware type may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                bValid = bValid && checkRegexp(hardwareDescription, /^([0-9a-zA-Z ])+$/, "Hardware description field only allow : a-z 0-9");


                if (bValid) {
                    var req = "title=" + hardwareTitle.val() + "&type=" + hardwareType.val() + "&description=" + hardwareDescription.val();
                    var addNewHardware = 'experiments/addNewHardware.html';
                    var url;
                    url = get_url(url);

                    url = url + addNewHardware;

                    $.ajax({
                        type: "GET",
                        url: url,
                        cache: false,
                        data: req,
                        async:false,
                        beforeSend: function() {

                        },
                        success: function(data) {
                            var newId;
                            var answer = data.split(':');
                            if ((answer[1].substring(0, 4)) == "true") {
                                newId = parseInt(answer[2].substring(0, answer[2].length - 1), 10);
                                $("#selectHardware").append(
                                        '<option selected="' + newId + '" value="' + newId + '">' + hardwareTitle.val() +
                                                '</option>'
                                        )
                                        ;
                            }
                            else {
                                alert("Data not saved!");
                            }
                        },

                        error: function (xmlHttpRequest, textStatus, errorThrown) {
                            if (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0) {
                                return;  // it's not really an error
                            }
                            else {
                                alert("Error E3:" + xmlHttpRequest.status);
                            }
                        }
                    });
                    $(this).dialog("close");
                }
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        },
        close:
                function() {
                    allHardwareFields.val("").removeClass("ui-state-error");
                }
    });

$("#dialog-form-person").dialog({
        autoOpen: false,
        height: 600,
        width: 350,
        modal: true,
        buttons: {
            "Create new person": function() {
                var bValid = true;
                allPersonsFields.removeClass("ui-state-error");

                bValid = bValid && checkLength(givenname, "givenname", 3, 30);
                bValid = bValid && checkLength(surname, "surname", 3, 30);
                bValid = bValid && checkLength(dateOfBirth, "dateOfBirth", 10, 30);
                bValid = bValid && checkLength(email, "email", 6, 80);
                bValid = bValid && checkLength(phoneNumber, "phonenumber", 9, 30);
               // bValid = bValid && checkLength(note, "note", 6, 80);

                bValid = bValid && checkRegexp(givenname, /^[a-z]([0-9a-z_ ])+$/i, "Givenname may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                bValid = bValid && checkRegexp(surname, /^[a-z]([0-9a-z_ ])+$/i, "Surname may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                bValid = bValid && checkRegexp(dateOfBirth, /^[0-9/]+$/i, "Date of birth may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
               // From jquery.validate.js (by joern), contributed by Scott Gonzalez: http://projects.scottsplayground.com/email_address_validation/
		        bValid = bValid && checkRegexp( email, /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i, "eg. ui@jquery.com" );
                bValid = bValid && checkRegexp(phoneNumber, /^[0-9]+$/i, "Phone number may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                //bValid = bValid && checkRegexp(note, /^([0-9a-zA-Z ])+$/, "Note field only allow : a-z 0-9");

                if (bValid) {

                    var req = $("#addPersonForm").serialize();
                    var addNewPerson = 'experiments/addNewPerson.html';
                    //alert(req);

                    var url;
                    url = get_url(url);

                    url = url + addNewPerson;

                    $.ajax({
                        type: "GET",
                        url: url,
                        cache: false,
                        data: req,
                        async:false,
                        beforeSend: function() {

                        },
                        success: function(data) {
                            var newId;
                            var answer = data.split(':');
                            if ((answer[1].substring(0, 4)) == "true") {
                                newId = parseInt(answer[2].substring(0, answer[2].length - 1), 10);
                                $("#subjectPerson").append(
                                        '<option selected="' + newId + '" value="' + newId + '">' + givenname.val() + ' '+ surname.val()+
                                                '</option>'
                                        )
                                        ;
                            }
                            else {
                                alert("Data not saved!");
                            }
                        },

                        error: function (xmlHttpRequest, textStatus, errorThrown) {
                            if (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0) {
                                return;  // it's not really an error
                            }
                            else {
                                alert("Error E3:" + xmlHttpRequest.status);
                            }
                        }
                    });
                    $(this).dialog("close");
                }
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        },
        close:
                function() {
                    allPersonsFields.val("").removeClass("ui-state-error");
                }
    });

    $("#dialog-form-scenario").dialog({
        autoOpen: false,
        height: 500,
        width: 350,
        modal: true,
        buttons: {
            "Create new scenario": function() {
                var bValid = true;
                allScenariosFields.removeClass("ui-state-error");

                bValid = bValid && checkLength(scenarioTitle, "scenario title", 3, 30);
                bValid = bValid && checkLength(length, "length", 1, 30);
                bValid = bValid && checkLength(scenarioDescription, "scenario description", 6, 30);

                bValid = bValid && checkRegexp(scenarioTitle, /^[a-z]([0-9a-z_ ])+$/i, "Scenario title may consist of a-z, 0-9, underscores, spaces, begin with a letter.");
                bValid = bValid && checkRegexp(length, /^[0-9]+$/i, "Scenario length may consist of 0-9.");
                bValid = bValid && checkRegexp(scenarioDescription, /^[0-9a-z_ ]+$/i, "Scenario description may consist of a-z, 0-9, underscores, spaces, begin with a letter.");


                if (bValid) {

                    var req = $("#addScenarioForm").serialize();
                    var addNewScenario = 'experiments/addNewScenario.html';
                    //alert(req);

                    var url;
                    url = get_url(url);

                    url = url + addNewScenario;

                    $.ajax({
                        type: "POST",
                        url: url,
                        cache: false,
                        data: req,
                        async:false,
                        beforeSend: function() {

                        },
                        success: function(data) {
                            var newId;
                            var answer = data.split(':');
                            if ((answer[1].substring(0, 4)) == "true") {
                                newId = parseInt(answer[2].substring(0, answer[2].length - 1), 10);
                                $("#selectScenario").append(
                                        '<option selected="' + newId + '" value="' + newId + '">' + scenarioTitle +
                                                '</option>'
                                        )
                                        ;
                            }
                            else {
                                alert("Data not saved!");
                            }
                        },

                        error: function (xmlHttpRequest, textStatus, errorThrown) {
                            if (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0) {
                                return;  // it's not really an error
                            }
                            else {
                                alert("Error E3:" + xmlHttpRequest.status);
                            }
                        }
                    });
                    $(this).dialog("close");
                }
            },
            Cancel: function() {
                $(this).dialog("close");
            }
        },
        close:
                function() {
                    allScenariosFields.val("").removeClass("ui-state-error");
                }
    });



    $("#create-weather")
            .button()
            .click(function() {
        $("#dialog-form-weather").dialog("open");
    });
    $("#create-hardware")
            .button()
            .click(function() {
        $("#dialog-form-hardware").dialog("open");
    });
    $("#create-person")
            .button()
            .click(function() {
        $("#dialog-form-person").dialog("open");
    });
    $("#create-scenario")
            .button()
            .click(function() {
        $("#dialog-form-scenario").dialog("open");
    });

})
        ;
