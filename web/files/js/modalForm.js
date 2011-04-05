var selectedId = -1;
$(function() {
    // a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
    $("#dialog:ui-dialog").dialog("destroy");

    var weatherTitle = $("#weatherTitle"),
            weatherDescription = $("#weatherDescription"),
            allFields = $([]).add(weatherTitle).add(weatherDescription),
            tips = $(".validateTips");

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

    $("#dialog-form").dialog({
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
                    $.ajax({
                        type: "GET",
                        url: "http://localhost:8080/EEGDatabase/experiments/addNewWeather.html",
                        cache: false,
                        data: req,
                        beforeSend: function() {

                        },
                        async:false,
                        success: function(data) {
                            alert("OK");
                            var answer = data.getAttribute("commit").split('#!#');
                            if (trim(answer[0]) == "OK") {
                                alert("OK");
                            }
                            else {

                            }
                        },

                        error: function (xmlHttpRequest, textStatus, errorThrown) {
                            if (xmlHttpRequest.readyState == 0 || xmlHttpRequest.status == 0) {
                                return;  // it's not really an error
                            }
                            else {
                               // alert("Error E3:" + xmlHttpRequest.status);
                            }
                        }
                    });


                    $("#selectWeather").append(
                            '<option selected="' + selectedId + '" value="'+ selectedId +'">' + weatherTitle.val() +
                                    '</option>'
                            )
                            ;
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

    $("#create-weather")
            .button()
            .click(function() {
        $("#dialog-form").dialog("open");
    });
})
        ;
