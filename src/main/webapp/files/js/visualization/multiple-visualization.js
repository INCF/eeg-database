var popupStatus = 0;
var canvasWidth = 400;
var canvasHeight = 200;
var active = 0;
var id = 0;
var activeID;
var context;
var signalData = new Array();
var plots = new Array();

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

//disabling popup with jQuery magic!
function disablePopup(){
    //disables popup only if it is enabled
    if(popupStatus==1){
        $("#backgroundPopup").fadeOut("slow");
        $("#wrapper").fadeOut("slow");
        $("#add_canvas").fadeOut("slow");
        $("#panel").slideUp();
        popupStatus = 0;
    }
}

//centering popup
function centerPopup(){
    //request data for centering
    var windowWidth = document.documentElement.clientWidth;
    var windowHeight = document.documentElement.clientHeight;
    //centering
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

    $("#add").click(function() {
        var wraID = 'canwrapper' + id;
        var spaID = 'label' + id;
        id++;
        $('<div>').attr({
            id: wraID,
            "class": "canvasbox"
        }).css({
            }).prependTo('#wrapper');

        $(".del").click(function(){
            $(this).parent().remove();
        });
        $(".canvasbox").mousedown(function(event) {
            if(active == 0) {
                if (event.which == 1) {
                    $(this).addClass("active");
                    activeID = this.id;
                    var windowWidth = document.documentElement.clientWidth;
                    var windowHeight = document.documentElement.clientHeight;
                    $(this).css({
                        "z-index" : "3",
                        "top" : "200px",
                        "position" : "absolute"
                    });
                    $(this).stop().animate({ top: windowHeight/2-350, position: "absolute", left: windowWidth/2-500}, 250);
                    $(this).removeClass("canvasbox");
                    active = 1;
                    $("#tree").toggle();
                    $(this).click(function(){
                        $("#"+activeID).stop().animate({
                            top : "auto",
                            left : "auto",
                            width : 1000+"px",
                            height : 700+"px",
                            background : "#cecece",
                            border : "1px solid #e6e6e6"
                        }, 250);
                        $("#"+activeID).css({
                            "position" : "relative",
                            top : "auto",
                            left : "auto",
                            "z-index" : 2,
                        });
                        $("#"+activeID).removeClass("active");
                        $("#"+activeID).addClass("canvasbox");
                        $("#tree").hide();
                        active = 0;
                        if (typeof plots[activeID] != 'undefined') {
                            plots[activeID].replot();
                        }
                    });
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
            //canvasClear();
            run = true;
            init();
            draw();
        });
    });
}

canvasClear = function () {
    ctx.clearRect(0, 0, getWidth(), getHeight());
    x = 0;
    y = 0;
};

getWidth = function () {
    return $("#"+activeID).width();
};

getHeight = function () {
    return $("#"+activeID).height();
};

function _click(id) {
    $("#"+activeID).empty();
    drawGraph(activeID, tree.getSelectedItemText(), id);
}

function drawGraph(place, headline, channel) {
    plots[place] = $.jqplot (place, [signalData[channel]], {
        title: headline,
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
            show: true,
            sizeAdjust: 7.5
        },
        cursor: {
            tooltipLocation: 'n',
            zoom: true,
            show: true
        }
    });
    $('#zoom').click(function() { plot1.resetZoom() });
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