$(document).ready(function(){
    var coExperimentatorsAmount = 1;

    $(".tab").click(function(){
        var actual = $('.selected').children().first();

        $(".tab").parent().removeClass("selected");
        $(this).parent().addClass("selected");
        var openedTab = "." + $(this).attr("name");
        $(".tabContent").addClass("hidden");
        $(openedTab).removeClass("hidden");

        // validate current tab.
        var actualTab = $("." + actual.attr("name"));
        if(actualTab.find("form").first().valid()) {
            actual.find("img").remove();
            actual.append('<img src="/files/images/passed.png">');
        } else {
            actual.find("img").remove();
            actual.append('<img src="/files/images/failed.png">');
        }
        return false;
    });

    $(".autocomplete").each(function(idx, elm){
        var jsonSource = $(elm).attr('autosrc');
        $(elm).autocomplete({
            source: jsonSource,
            change: addCopyIfMultiple
        });
    });

    function addCopyIfMultiple(){
        if($(this).hasClass("multiple")) {
            var parent = $(this).parent();
            var newElement = parent.clone();
            newElement.val("");
            newElement.insertAfter(parent);
        }
    }

    $(".openurl").each(function(idx, elm){
        var urlToLoad = $(elm).attr('data');
        // Open layer under.
        $(elm).click(function(){
            createOverlay();
            // Insert everything as overlay into newly inserted overlay layer.
            getFetchedData(urlToLoad);
        });
    });

    function createOverlay(){
        $("body").prepend("<div class='overlay'></div>");
    }

    function getFetchedData(url){
        $.ajax(url,{
            dataType: 'html',
            success: function(data){
                $("body").prepend("<div class='modal'></div>");
                $(".modal").append(data);

                $(".closeDialog").bind('click', function(){
                    $('.overlay').remove();
                    $('.modal').remove();
                });
            }
        });
    }

    var today = new Date();
    today = today.format("{FullYear}-{Month:2}-{Date:2}");
    var now = new Date();
    now = now.format("{Hours:2}:{Minutes:2}");

    $("#startDay").attr("value",today);
    $("#endDay").attr("value", today);
    $("#startTime").attr("value", now);
    $("#endTime").attr("value", now);
});