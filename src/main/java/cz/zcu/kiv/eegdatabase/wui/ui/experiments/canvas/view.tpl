<div id="wrapper">
    <img alt="Add another canvas" title="Add another canvas" id="add" src="files/images/add.png" />
</div>
<div id="tree"></div>
<div id="dataRange">
    <span id="labelDataRange">1 - 200</span>
    <div id="sliderDataRange"></div>
    <input type="hidden" id="startSlide" />
    <input type="hidden" id="endSlide" />
    <button id="draw">Draw</button>
</div>
<img alt="Close this canvas" title="Close this canvas" id="close_canvas" src="files/images/close_canvas.png" />

<script type="text/javascript">
    tree = new dhtmlXTreeObject("tree", "200px", "350px", 0);
    tree.setSkin('dhx_skyblue');
    tree.setImagePath("/files/images/imgs/csh_bluebooks/");
    tree.enableDragAndDrop(false);
    tree.setOnClickHandler(_click);
    tree.deleteChildItems(0);
    ${generate.tree}
    ${generate.data}
</script>

<div id="panel">
    <img title="Show info about the experiment" id="info" src="files/images/panel_detail.png" />
    <img id="zoom" title="Reset zoom" src="files/images/panel_zoom.png" />
    <!--<img id="setWidth" title="Set width" src="files/images/panel_config.png" />-->
    <input id="color1" type="text" name="color1" value="#333399" />
    <div id="slider">
        <div id="sliderWidth"></div>
        <input id="lineWidth" type="hidden" />
    </div>
</div>
<div id="backgroundPopup">
    <a id="popupContactClose" title="Close"><img style="float: right;" src="files/images/close.png"></a>
</div>
