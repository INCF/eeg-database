/*jslint white: true, browser: true, undef: true, nomen: true, eqeqeq: true, plusplus: false, bitwise: true, regexp: true, strict: true, newcap: true, immed: true, maxerr: 14 */
/*global window: false */

/* enable strict mode */
//"use strict";


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

getWidth = function () {
    var canvas = document.getElementById('canvas');
    return canvas.width;
};

getHeight = function () {
    var canvas = document.getElementById('canvas');
    return canvas.height;
};

init = function () {
    ctx.lineWidth = 0.1;
    ctx.moveTo(5,200);
    ctx.lineTo(730,200);
    ctx.strokeStyle = '#303030';
    ctx.stroke();
    ctx.moveTo(15,5);
    ctx.lineTo(15,395);
    ctx.stroke();
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
    ctx.moveTo(15, originy + 100);
    ctx.beginPath();
    ctx.lineWidth = 0.1;
    ctx.lineJoin = 'round';
    x = 15;
};


// draw circles
draw = function () {
    /*ctx.fillStyle = rndColor();
     // start drawing
     ctx.beginPath();
     // draw arc: arc(x, y, radius, startAngle, endAngle, anticlockwise)
     ctx.arc(x, y, r, 0, Math.PI * 2, true);
     // fill circle
     ctx.fill();
     // set X direction
     if (x + dx > width || x + dx < 0) {
     dx = -dx;
     }
     // set Y direction
     if (y + dy > height || y + dy < 0) {
     dy = -dy;
     }
     // set radius size
     if (r + dr > radius * 2 || r + dr < radius) {
     dr = -dr;
     }
     // calculate new x, y and r values
     x += dx;
     y += dy;
     r += dr;
     */
    rndCoord();
    y = 200 + rndCoord();
    ctx.lineTo(x, y);
    ctx.strokeStyle = '#12112e';
    ctx.stroke();
    console.log('[' + x + ',' + y + ']');
    x += 5;
    if (x >= 728) {
        run = false;
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
};
// clear canvas
canvasClear = function () {
    /*var canvas = document.getElementById('canvas');
     canvas.width = canvas.width;*/
    ctx.clearRect(0, 0, getWidth(), getHeight());
};
// zoom canvas
canvasZoom = function (value) {
    ctx.scale(1 + value, 1 + value);
};

//export the canvas context as a PNG image
exportAs = function() {
    var canvas = document.getElementById('canvas');
    var img = canvas.toDataURL("image/png");
    window.open(document.getElementById('canvas').toDataURL("image/png"));
    //document.getElementById('export').innerHTML = '<h2>Exported image</h2><img src="' + img + '" />';
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
            //fillTree();
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

function fillTree() {
    tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
    tree.setSkin('dhx_skyblue');
    tree.setImagePath("/EEGDatabase/files/images/imgs/");
    tree.enableDragAndDrop(false);
    //tree.setOnRightClickHandler(_rclick);
    tree.deleteChildItems(0);
    tree.insertNewChild(0, 1, "Channels");
    tree.insertNewChild(1, 11, "Fp1");
    tree.insertNewChild(1, 12, "Fp2");
    tree.insertNewChild(1, 13, "F3");
    tree.insertNewChild(1, 14, "F4");
    tree.insertNewChild(1, 15, "C3");
    tree.insertNewChild(1, 16, "C4");
    tree.insertNewChild(1, 17, "P3");
    tree.insertNewChild(1, 18, "P4");
    tree.insertNewChild(1, 19, "01");
    tree.insertNewChild(1, 20, "02");
    tree.insertNewChild(1, 21, "F7");
    tree.insertNewChild(1, 21, "F8");
    tree.insertNewChild(1, 22, "T7");
    tree.insertNewChild(1, 23, "T8");
    tree.insertNewChild(1, 24, "P7");
    tree.insertNewChild(1, 25, "P8");
    tree.insertNewChild(1, 26, "Fz");
    tree.insertNewChild(1, 27, "Pz");
    tree.insertNewChild(1, 28, "Cz");
    tree.insertNewChild(1, 29, "M1");
    tree.insertNewChild(1, 30, "M2");
}