/**
 * Auhor: Jenda Kolena, jendakolena@gmail.com
 */

function waitingStart(id)
{
    waitings[id] = true;
    setTimeout("waitingShow('" + id + "')", timeoutBeforeLongOperation);
}

function waitingStop(id)
{
    toggleDiv("slowness", false);
    waitings[id] = false;
}

function waitingShow(id)
{
    if (waitings[id] == true)
    {
        $("#" + id).html("<div class='slowness' id='slowness_" + id + "'></div>" + $("#" + id).html());
        $("#slowness_" + id).html(localize("bookRoom.longOperation"));
        toggleDiv("slowness_" + id, true);
    }
}

Date.prototype.getMonthName = function()
{
    var m = ['January','February','March','April','May','June','July',
        'August','September','October','November','December'];
    return m[this.getMonth()];
}

function getPageUrl(page)
{
    var url = window.location.href;
    //path to current page
    if (page == null || page == '') return url;

    //relative path
    if (page.charAt(0) != '/') return url.substring(0, url.lastIndexOf("/") + 1) + page;

    //absolute path
    var nohttp = url.split('//')[1];
    var hostPort = nohttp.split('/')[0];
    return (url.split('//')[0] + "//" + hostPort + page);
}

function getImageUrl(image)
{
    var myName = /(^|[\/\\])bookRoomView\.js(\?|$)/;
    var scripts = document.getElementsByTagName("script");
    for (var i = 0; i < scripts.length; i++)
    {
        var src;
        if (src = scripts[i].getAttribute("src"))
        {
            if (src.match(myName))
            {
                return src.substring(0, src.lastIndexOf("/") + 1) + '../../images/' + image;
            }
        }
    }
}

function toggleDiv(div, show)
{
    if (show == null) show = ($("#" + div).css('display') == 'none');
    if (show)
    {
        $("#" + div).show(collapseSpeed);
        $("#toggle_" + div).css('background-image', 'url(' + getImageUrl('arrow_down.png') + ')');
    }
    else
    {
        $("#" + div).hide(collapseSpeed);
        $("#toggle_" + div).css('background-image', 'url(' + getImageUrl('arrow_right.png') + ')');
    }
}

function showInfo(id)
{
    var req = "type=info&id=" + id;

    $.ajax({
        type: "GET",
        url: getPageUrl('book-room-ajax.html'),
        cache: false,
        data: req,
        beforeSend: function()
        {
            toggleInfo(true);
            $("#flowContent").html("<center><img src='" + getImageUrl('loading.gif') + "' style='width: 100%;' alt=''></center>");
        },
        success: function(data)
        {
            var answer = data.split('#!#');
            if (trim(answer[0]) == "OK")
            {
                $("#flowContent").html(answer[1]);
            }
            else
            {
                $("#flowContent").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)");
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            $("#flow").html("Error while getting data... :-(<br>Error " + xhr.status);
            alert(localize('bookRoom.error') + " E3:" + xhr.status);
        }
    });
}


function toggleInfo(show)
{
    if (show)
    {
        var x = $(document).scrollLeft();
        var y = $(document).scrollTop();
        var width = $(document).width();
        var height = $(document).height();

        $('#shadow').css('width', width + x);
        $('#shadow').css('height', height + y);

        $("#flowbox").css('left', (x + width) / 2);
        $("#flowbox").css('top', (y + height) / 2);

        $('#shadow').fadeIn(fadeSpeed, function()
        {
        });
        $('#flowbox').fadeIn(fadeSpeed, function()
        {
        });
    }
    else
    {
        $('#shadow').fadeOut(fadeSpeed, function()
        {
        });
        $('#flowbox').fadeOut(fadeSpeed, function()
        {
        });
    }
}

function deleteReservation(id)
{
    if (window.confirm(localize('bookRoom.deleteConfirmation') + ""))
    {
        var req = "type=delete&id=" + id;

        $.ajax({
            type: "GET",
            url: getPageUrl('book-room-ajax.html'),
            cache: false,
            data: req,
            beforeSend: function()
            {
                waitingStart("delete");
                $("#delete" + id).html("<img src='" + getImageUrl('loading_circle.gif') + "' alt=''>");
            },
            success: function(data)
            {
                waitingStop("delete");
                var answer = data.split('#!#');
                if (trim(answer[0]) == 'OK') showChosenData();
                //alert(trim(answer[1]));
            },
            error: function (xhr, ajaxOptions, thrownError)
            {
                waitingStop("delete");
                alert(localize('bookRoom.error') + " E4:" + xhr.status);
            }
        });
    }
}


function trim(stringToTrim)
{
    return stringToTrim.replace(/^\s+|\s+$/g, "");
}

function isAllowed()
{
    var coll = trim($("#collision").attr('value'));
    switch (coll)
    {
        case '0':
        {
            return true;
        }
            break;
        case '1':
        {
            alert(localize('bookRoom.collisions') + "");
            return false;
        }
            break;
        case '-1':
        {
            alert(localize('bookRoom.waitForControl') + "");
            showChosenData();
            return false;
        }
            break;
        case '-2':
        {
            alert(localize('bookRoom.invalidTime') + "");
            return false;
        }
            break;
        default:
        {
            alert(localize('bookRoom.error') + " E1 (" + coll + ")");
            return false;
        }
    }
    return false;
}

function inPast()
{
    var now = new Date();

    var tmp = $("#date").attr('value').split("/");
    var date = tmp[2] + "/" + tmp[1] + "/" + tmp[0];

    var startTime = new Date(date + " " + $("#startH").attr('value') + ":" + $("#startM").attr('value') + ":00");
    startTime.setHours(startTime.getHours() + 2);

    return (startTime < now);
}

function setEndTime(hourIndex, minuteIndex)
{
    $("#endH").attr('selectedIndex', hourIndex);
    $("#endM").attr('selectedIndex', minuteIndex);
}

function newTime()
{
    var start = $("#startH").attr('value') + $("#startM").attr('value');
    var end = $("#endH").attr('value') + $("#endM").attr('value');
    var date = $("#date").attr('value');

    //try to set end hour after start hour
    if (end <= start)
    {
        if ($("#startH").attr('value') == endHour)
        {
            if ($("#startM").attr('value') != 45)
            {
                setEndTime(endHour - startHour, 3);
            }
            else
            {
                //we can't do anything...
            }
        }
        else
        {
            setEndTime(parseInt($("#startH").attr('selectedIndex')) + 1, $("#endM").attr('selectedIndex'));
        }

        //load endtime value again for next comparison
        end = $("#endH").attr('value') + $("#endM").attr('value');
    }

    if (end <= start)
    {
        $("#collision").attr('value', '-2');
        $("#chosenData").html("<h3>" + localize('bookRoom.invalidTime') + "</h3>");
    }
    else if (inPast())
    {
        $("#collision").attr('value', '-2');
        var d = new Date();
        var now = d.getHours() + ":" + d.getMinutes();
        $("#chosenData").html("<h3>" + localize('bookRoom.timeInPast') + now + "!</h3>");
    }
    else
    {
        $("#startTime").attr('value', date + " " + $("#startH").attr('value') + ":" + $("#startM").attr('value') + ":00");
        $("#endTime").attr('value', date + " " + $("#endH").attr('value') + ":" + $("#endM").attr('value') + ":00");
        showChosenData();
    }
}

function showChosenData()
{
    var filterGroup = $("#filterGroup").attr('value');
    var filterDate = $("#filterDate").attr('value');
    if (filterDate == localize("bookRoom.filter.date.click")) filterDate = '';
    var repType = $("#repType").attr('value');
    var repCount = $("#repCount").attr('value');
    var date = $("#date").attr('value');

    var req = "filterGroup=" + filterGroup + "&filterDate=" + filterDate + "&date=" + date + "&startTime=" + $("#startTime").attr('value') + "&endTime=" + $("#endTime").attr('value') + "&repType=" + repType + "&repCount=" + repCount;
    //group=24&date=24/03/2011&startTime=24/03/2011 06:00:00&endTime=24/03/2011 07:00:00&repType=0&repCount=0

    $("#legend_day").html(localize("bookRoom.reservationToSameDate") + " " + date);

    $.ajax({
        type: "GET",
        url: getPageUrl('book-room-view.html'),
        cache: false,
        data: req,
        beforeSend: function()
        {
            waitingStart("chosenData");
            $("#collision").attr('value', '-1');
            $("#chosenData").html("<center><img src='" + getImageUrl('loading.gif') + "' alt=''></center>");
        },
        success: function(data)
        {
            waitingStop("chosenData");
            var answer = data.split('#!#');
            if (trim(answer[0]) == "OK")
            {
                $("#chosenData").html(answer[1]);
                $("#collision").attr('value', answer[2]);
                $("#reservationsCount").attr('value', answer[3]);
                $("table.dataTable").tablesorter({
                    headers: {
                        1: {
                            sorter: 'days'
                        },
                        3: {
                            sorter: false
                        },
                        4: {
                            sorter: false
                        }
                    }
                });
            }
            else
            {
                $("#chosenData").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)<hr>");
                $("#collision").attr('value', '-1');
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            waitingStop("chosenData");
            $("#chosenData").html("Error while getting data... :-(<br>Error " + xhr.status);
            $("#collision").attr('value', '-1');
            alert(localize('bookRoom.error') + " E2:" + xhr.status);
        }
    });
}

function reloadTimeline()
{
    var req = "type=timeline&date=" + $("#date").attr('value');
    $.ajax({
        type: "GET",
        url: getPageUrl('book-room-ajax.html'),
        cache: false,
        data: req,
        beforeSend: function()
        {
            waitingStart("timeline");
            $("#timeline").html("<center><img src='" + getImageUrl('loading.gif') + "' alt=''></center>");
        },
        success: function(data)
        {
            waitingStop("timeline");
            var answer = data.split('#!#');
            if (trim(answer[0]) == "OK")
            {
                $("#timeline_header").html("<b>" + localize("bookRoom.displayedMonth") + "</b>: " + new Date().getMonthName());
                timelineCreate("timeline", $("#date").attr('value'), trim(answer[1]));
            }
            else
            {
                //alert(trim(answer[0]));
                $("#timeline").html("Error while getting data...<br/>(try to refresh page, you can be logged off after timeout)<hr>");
            }
        },
        error: function (xhr, ajaxOptions, thrownError)
        {
            waitingStop("timeline");
            $("#timeline").html("Error while getting data... :-(<br>Error " + xhr.status);
            alert(localize('bookRoom.error') + "E6:" + xhr.status);
        }
    });
}
