<div id="wrapper"></div>
<img alt="Add another canvas" title="Add another canvas" id="add_canvas" src="files/images/add.png" />
<div id="tree"></div>

<script type="text/javascript">
    tree = new dhtmlXTreeObject("tree", "200px", "300px", 0);
    tree.setSkin('dhx_skyblue');
    tree.setImagePath("/files/images/imgs/csh_bluebooks/");
    tree.enableDragAndDrop(false);
    tree.setOnClickHandler(_click);
    tree.deleteChildItems(0);
    tree.insertNewChild(0, 1, "Channels");
    ${generate.tree}
    ${generate.data}
</script>

<div id="panel">
    <img title="Show info about the experiment" id="info" src="files/images/panel_detail.png" />
    <img id="clear" title="Clear the canvas" src="files/images/panel_config.png" />
    <img alt="Add another canvas" title="Add another canvas" id="add" src="files/images/panel_run.png" />
    <img id="zoom" title="Reset zoom" src="files/images/panel_zoom.png" />
    <input id="color1" type="text" name="color1" value="#333399" />
</div>
<div id="backgroundPopup">
    <a id="popupContactClose" title="Close"><img style="float: right;" src="files/images/close.png"></a>
</div>
