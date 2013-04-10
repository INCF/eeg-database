var popupStatus = 0;
var canvasWidth = 400;
var canvasHeight = 200;
var active = 0;
var id = 0;
var activeID;
var context;
var run=true;
var x,y;
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
            }).appendTo('#wrapper');

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
                            left : "auto"
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

        $("#clear").click(function() {
            if (active != 0) {
                ctx.clearRect(0, 0, getWidth(), getHeight());
                x = 0;
                y = 0;
            }
            canvasClear();
        });

        $("#export").click(function() {
            if (active != 0) {
                var canvas = document.getElementById(activeID);
                var img = canvas.toDataURL("image/jpeg");
                window.open(img);
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

rndCoord = function() {
    var coordy = Math.floor((Math.random() * 2 - 1) * 150);
    return coordy;
};

draw = function () {
    rndCoord();
    y = 200 + rndCoord();
    ctx.lineTo(x, y);
    ctx.strokeStyle = $("#color1").val();
    ctx.stroke();
    x += 10;
    if (x >= (getWidth()-30)) {
        run = false;
    }
    if (run) {
        setTimeout(draw, 50 - 2 * 10);
    }
};

initPosition = function () {
    ctx.moveTo(15, 695);
    ctx.beginPath();
    ctx.lineWidth = 1;
    ctx.lineJoin = 'round';
    x = 15;
};

init = function () {
    canvasClear();
    ctx.fillStyle = '#eeeeee';
    ctx.fillRect(0, 0, getWidth(), getHeight());
    ctx.fill();
    ctx.strokeStyle = '#303030';
    ctx.lineWidth = 1;
    ctx.moveTo(5,getHeight()-30);
    ctx.lineTo(getWidth(),getHeight()-30);
    ctx.stroke();
    ctx.moveTo(15,5);
    ctx.lineTo(15,695);
    ctx.stroke();
    ctx.fillStyle = '#303030';
    ctx.font = '12px sans-serif';
    ctx.fillText('0', 5, getHeight()-15);
    ctx.fillText('\u03BCV', 0, 15);
    ctx.fillText('s', getWidth() - 12, getHeight()-15);
    var start = 5;
    for (i = 1; i < 11; i++) {
        ctx.moveTo(start + i * 100, getHeight()-35);
        ctx.lineTo(start + i * 100, getHeight()-25);
        ctx.stroke();
        ctx.fillText(i, start + i * 100, getHeight()-15);
    }
    initPosition();
};

function _click(id) {
    drawGraph(activeID);
}

function drawGraph(place) {
    plots[place] = $.jqplot (place, [[3,7,9,1,5,3,8,2,5]], {
        title: 'Plot With Options',
        axesDefaults: {
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer
        },
        seriesDefaults: {
            rendererOptions: {
                smooth: true
            }
        },
        axes: {
            xaxis: {
                label: "X Axis",
                pad: 0
            },
            yaxis: {
                label: "Y Axis"
            }
        }
    });
}

$(document).ready(function() {
    /*tree = new dhtmlXTreeObject("tree", "200px", "300px", 0);
    tree.setSkin('dhx_skyblue');
    tree.setImagePath("files/images/imgs/csh_bluebooks/");
    tree.enableDragAndDrop(false);
    tree.setOnClickHandler(_click);*/

    $('#color1').colorPicker();

    $("#visualization").click(function(){
        centerPopup();
        loadPopup();
    });

    $("#popupContactClose").click(function(){
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