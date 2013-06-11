var popupStatus = 0;
var canvasWidth = 400;
var canvasHeight = 200;
var active = 0;
var id = 0;
var activeID;
var context;
var signalData = new Array();
var plots = new Array();
var zoomControl = new Array();

createArray = function (index) {
    signalData[index] = new Array();
};

addToArray = function (index, index2, data) {
    if (data != null) {
        signalData[index][index2] = data;
    }
};

//loading popup with jQuery magic!
function loadPopup(){
    //loads popup only if it is disabled
    if(popupStatus==0){
        $("#backgroundPopup").css({
            "opacity": "0.8"
        });
        $("#backgroundPopup").fadeIn("slow");
        $("#wrapper").fadeIn("slow");
        $("#add_canvas").fadeIn("slow");
        $("#panel").slideDown();
        popupStatus = 1;
    }
}

function disablePopup(){
    if(popupStatus==1){
        $("#backgroundPopup").fadeOut("slow");
        $("#wrapper").fadeOut("slow");
        $("#add_canvas").fadeOut("slow");
        $("#panel").slideUp();
        $("#tree").hide();
        $("#dataRange").hide();
        $("#close_canvas").hide();
        popupStatus = 0;
    }
}

function dataRange(index,headline) {
    $("#draw").button();
    $("#draw").css({
        "float" : "right",
        "margin-top" : "10px"
    });
    $("#sliderDataRange").slider({
        range: true,
        values: [1,200],
        min: 1,
        max: signalData[index].length,
        slide: function( event, ui ) {
            $("#labelDataRange").text(ui.values[0] + " - " + ui.values[1]);
            $("#startSlide").val(ui.values[0]);
            $("#endSlide").val(ui.values[1]);
        }
    });
    $("#draw").click(function(){
        $("#"+activeID).empty();
        $("#"+activeID+"_zoom").empty();
        drawGraph(activeID, headline, index, $("#endSlide").val(), $("#startSlide").val());
    });
    $("#dataRange").css({
        "position" : "absolute",
        top : 760+"px",
        left : getWindowWidth()/2-500 + 20,
        "z-index" : 3,
        width : 1000+"px",
        height : 30 + "px"
    });
    $("#dataRange").show(250);
}

function showCloseButton() {
    $("#close_canvas").css({
        "position" : "absolute",
        "left" : getWindowWidth()/2-500 + getWidth() - $("#close_canvas").width()/2 + 20,
        "top" : 0
    });
    $("#close_canvas").show();
}

function centerPopup(){
    var windowWidth = document.documentElement.clientWidth;
    var windowHeight = document.documentElement.clientHeight;
    $("#sliderWidth").slider({
        value:1,
        min: 1,
        max: 5,
        slide: function( event, ui ) {
            $("#lineWidth").val(ui.value);
        }
    });

    $("#panel").css({
        "left": windowWidth/2-$("#panel").width()/2
    });

    $("#panel img").hover(function() {
        $(this).stop().animate({ marginBottom: "25px" }, 250);
    },function(){
        $(this).stop().animate({ marginBottom: "0px" }, 450);
    });

    $("#wrapper").css({
        "position" : "absolute",
        "top" : "20px",
        "left" : "20px"
    });

    $("#backgroundPopup").css({
        "height": windowHeight
    });

    $("#close_canvas").click(function() {
        $("#"+activeID).stop().animate({
            top : "auto",
            left : "auto",
            width : 1000+"px",
            height : 450+"px",
            background : "#cecece",
            border : "1px solid #e6e6e6"
        }, 250);
        $("#"+activeID).css({
            "position" : "relative",
            top : "auto",
            left : "auto",
            "z-index" : 2
        });
        $("#"+activeID).removeClass("active");
        $("#"+activeID).addClass("canvasbox");
        $("#tree").hide();
        $("#dataRange").hide();
        $("#"+activeID+"_zoom").hide();
        $("#close_canvas").hide();
        active = 0;
        if (typeof plots[activeID] != 'undefined') {
            plots[activeID].replot();
        }
    });

    $("#add").click(function() {
        var wraID = 'canwrapper' + id;
        var spaID = 'label' + id;
        id++;
        $('<div>').attr({
            id: wraID,
            "class": "canvasbox"
        }).css({
            }).prependTo('#wrapper');
        $('<div>').attr({
            id: wraID+"_zoom",
            "class": "zoomControl"
        }).css({
            "display" : "none"
        }).prependTo('#wrapper');

        $(".canvasbox").mousedown(function(event) {
            if(active == 0) {
                if (event.which == 1) {
                    $(this).addClass("active");
                    activeID = this.id;
                    var windowWidth = document.documentElement.clientWidth;
                    var windowHeight = document.documentElement.clientHeight;
                    $(this).css({
                        "z-index" : "3",
                        "top" : "0px",
                        "position" : "absolute"
                    });
                    $(this).stop().animate({ top: 0, position: "absolute", left: windowWidth/2-500}, 250);
                    $(this).removeClass("canvasbox");
                    active = 1;
                    $("#tree").toggle();
                    $("#"+activeID+"_zoom").css({
                        "position" : "absolute",
                        top : 470+"px",
                        left : windowWidth/2-500,
                        "z-index" : 3,
                        width : 1000+"px",
                        height : 250 + "px"
                    });
                    $("#"+activeID+"_zoom").show(250);
                    showCloseButton();
                    if (typeof plots[activeID] != 'undefined') {
                        plots[activeID].replot();
                    }
                }
                if (event.which == 3) {
                    $(this).parent().remove();
                }
            }
        });


        $("#detail").click(function() {
            $("#"+activeID).stop().animate({
                top : "auto",
                left : "auto",
                background : "#cecece",
                border : "1px solid #e6e6e6"
            }, 250);
            $("#"+activeID).css({
                "position" : "static"
            });
            $("#"+activeID).removeClass("active");
            $("#"+activeID).addClass("canvasbox");
            $("#tree").hide();
            active = 0;
        });

        $("#info").click(function() {
            $("#"+activeID).draggable();
            $("#"+activeID).css({
                "cursor" : "move"
            });
        });

        $("#zoom").click(function() {
            if (active != 0) {
                plots[activeID].resetZoom();
            }
        });

        $("#tree a").click(function() {
            ctx = $("#"+activeID)[0].getContext('2d');
            run = true;
            init();
            draw();
        });
    });
}

function getWindowWidth() {
    return document.documentElement.clientWidth;
}

function getWindowHeight() {
    return document.documentElement.clientHeight;
}

getWidth = function () {
    return $("#"+activeID).width();
};

getHeight = function () {
    return $("#"+activeID).height();
};

function _click(id) {
    $("#"+activeID).empty();
    $("#"+activeID+"_zoom").empty();
    dataRange(id, tree.getSelectedItemText());
    drawGraph(activeID, tree.getSelectedItemText(), id, 200, 0);
}

function drawGraph(place, headline, channel, end, start) {
    var cut = signalData[channel].slice(start,end);

    var signalRenderer = function() {
        var data = [[]];
        var index = parseInt(start);
        for (var i = 1; i < cut.length; i++) {
            data[0].push([index + i, cut[i]]);
        }
        alert(data);
        return data;
    };

    plots[place] = $.jqplot (place, [], {
        title: headline,
        dataRenderer: signalRenderer,
        animate: true,
        axesDefaults: {
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        },
        seriesDefaults: {
            color: $("#color1").val(),
            lineWidth: $("#lineWidth").val(),
            showMarker: false,
            rendererOptions: {
                smooth: true
            }
        },
        axes: {
            xaxis: {
                label: "Time [ms]",
                pad: 0
            },
            yaxis: {
                label: "Voltage [µV]"
            }
        },
        highlighter: {
            tooltipAxes: 'y',
            show: true,
            sizeAdjust: 7.5,
            formatString: '%.6f'
        },
        cursor: {
            tooltipLocation: 'n',
            zoom: true,
            show: true
        }
    });

    zoomControl[place] = $.jqplot (place+"_zoom", [], {
        dataRenderer: signalRenderer,
        axesDefaults: {
            //labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        },
        seriesDefaults: {
            color: "#000000",
            lineWidth: 1,
            showMarker: false,
            rendererOptions: {
                smooth: true
            }
        },
        axes: {
            xaxis: {
                 min: start,
                 max: end,
                 padMax: 0,
                 padMin: 0
            }
        },
        cursor: {
            zoom: true,
            show: true
        }
    });

    $.jqplot.Cursor.zoomProxy(plots[place], zoomControl[place]);
    zoomControl[place].replot({ resetAxes:true });
    $('#zoom').click(function() { plots[place].resetZoom() });
}

$(document).ready(function() {
    $('#color1').colorPicker();

    $("#visualization").click(function(){
        centerPopup();
        loadPopup();
    });

    $("#popupContactClose").click(function(){
        $("#tree").hide();
        disablePopup();
    });
    $(document).keypress(function(e){
        if(e.keyCode==27 && popupStatus==1){
            disablePopup();
        }
    });
    $(document).bind("contextmenu",function(e){
        return false;
    });
});