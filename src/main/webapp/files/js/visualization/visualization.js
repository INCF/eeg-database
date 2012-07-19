/*jslint white: true, browser: true, undef: true, nomen: true, eqeqeq: true, plusplus: false, bitwise: true, regexp: true, strict: true, newcap: true, immed: true, maxerr: 14 */

var ctx,
// set intial values for x, y and r
    x = 0,
    y = 0,
    r = 15,
// delta values
    dx = 3.532,
    dy = 7.5,
    dr = 0.1,
// canvas width and height - defined in init()
    width, height,
// define radius range (r will be between radius and radius*2)
    radius = 15,
// pause in ms - initialized in init() and changed with range control
    pause,
// boolean flag (true - draw circles; false - stop drawing)
    run,
// define functions
    draw,
    animationStart,
    animationStop,
    canvasClear,
    setSpeed,
    originx = 0,
    originy = 0,
    rndColor;
var canvas = document.getElementById("canvas");
var emptyTree;
var signalData = new Array();

getWidth = function () {
    var canvas = document.getElementById('canvas');
    return canvas.width;
};

getHeight = function () {
    var canvas = document.getElementById('canvas');
    return canvas.height;
};

createArray = function (index) {
    signalData[index] = new Array();
    console.log(index);
};

addToArray = function (index, index2, data) {
    if (data != null) {
        signalData[index][index2] = data;
    }
    console.log(signalData[index].toString());
};

function tonclick(id) {
    document.getElementById("playbtn").setAttribute("disabled", true);
    canvasClear();
    init();
    run = true;
    draw();
};

init = function () {
    canvasClear();
    ctx.lineWidth = 1;
    ctx.moveTo(5,getHeight()-30);
    ctx.lineTo(getWidth(),getHeight()-30);
    ctx.strokeStyle = '#303030';
    ctx.stroke();
    ctx.moveTo(15,5);
    ctx.lineTo(15,395);
    ctx.stroke();
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

initPosition = function () {
    ctx.moveTo(15, originy + 100);
    ctx.beginPath();
    ctx.lineWidth = 0.1;
    ctx.lineJoin = 'round';
    x = 15;
};

// initialization
window.onload = function () {
    emptyTree = 1;
    var canvas = document.getElementById('canvas');
    // set initial pause (read value from slider)
    setSpeed();
    // set canvas width & height
    width = getWidth();
    height = getHeight();
    // set 2D rendering context
    ctx = canvas.getContext('2d');
    init();
};


// draw circles
draw = function () {
    rndCoord();
    y = 200 + rndCoord();
    ctx.lineTo(x, y);
    ctx.strokeStyle = '#12112e';
    ctx.stroke();
    x += 10;
    if (x >= (getWidth()-30)) {
        run = false;
        document.getElementById("playbtn").setAttribute("disabled", true);
        document.getElementById("pausebtn").setAttribute("disabled", true);
        document.getElementById("stopbtn").setAttribute("disabled", true);
    }

    // if "run" variable is true, then set timeout and call draw() again
    if (run) {
        setTimeout(draw, 50 - 2 * pause);
    }
};

// button actions (start / stop)
animationStart = function () {
    run = true;
    draw();
};
animationPause = function () {
    run = false;
    document.getElementById("playbtn").removeAttribute("disabled");
};
// clear canvas
canvasClear = function () {
    ctx.clearRect(0, 0, getWidth(), getHeight());
};
// zoom canvas
canvasZoom = function (value) {
    ctx.scale(1 + value, 1 + value);
};

//export the canvas context as a PNG image
exportAs = function(value) {
    var format = "";
    if (value > 0)
    {
        switch (value) {
            case '1':format = "image/png";
                break;
            case '2':format = "image/jpeg";
                break;
        }
        var canvas = document.getElementById('canvas');
        var img = canvas.toDataURL(format);
        window.open(document.getElementById('canvas').toDataURL(format));
    }
};

// set speed
setSpeed = function (value) {
    pause = document.getElementById('slider').value;
    document.getElementById('pause_value').innerHTML = pause;
};
// generate random color
rndColor = function () {
    var r = Math.floor(Math.random() * 256),
        g = Math.floor(Math.random() * 256),
        b = Math.floor(Math.random() * 256);
    return 'rgba(' + r + ',' + g + ',' + b + ',0.4)';
};

rndCoord = function() {
    var coordy = Math.floor((Math.random() * 2 - 1) * 150);
    return coordy;
    //console.log(coordy);
};

// shows the slickbox DIV on clicking the link with an ID of "slick-show"

function toggleDiv(div, show)
{
    if ($('#' + div).is(':visible')) {
        $('#' + div).toggle(500);
    }
    else {
        $('#' + div).toggle(500);
        if(emptyTree == 1) {
            emptyTree = 0;
        }
    }
}

function getImageUrl(dir)
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
                return src.substring(0, src.lastIndexOf("/") + 1) + '../../images/' + dir;
            }
        }
    }
}