	<span id="show" title="${toggle.text}" onclick="toggleDiv('visualization')">${toggle.text}</span>
		<div id="visualization" style="display:none; width: 730px;">
            <div id="treeboxbox_tree" style="width: 110px; background-color: #f5f5f5; border: 1px solid Silver; float: right;"></div>
            <div id="encaps" style="width: 600px; height: 430px; overflow: auto;">
                <canvas id="canvas" width="1040" height="400"></canvas>
            </div>
            <script type="text/javascript">
                tree = new dhtmlXTreeObject("treeboxbox_tree", "100%", "100%", 0);
                tree.setSkin('dhx_skyblue');
                tree.setImagePath("/files/images/imgs/");
                tree.enableDragAndDrop(false);
                tree.setOnClickHandler(tonclick);
                tree.deleteChildItems(0);
                tree.insertNewChild(0, 1, "Channels");
                ${generate.tree}
            </script>
            
            <script type="text/javascript">
            ${generate.data}
             </script>
             
            <p>
                <input id="playbtn" type="button" value="Play" onclick="javascript:animationStart()" class="grey" />
                <input id="pausebtn" type="button" value="Pause" onclick="javascript:animationPause()" class="grey" />
                <input id="stopbtn" type="button" value="Stop" onclick="javascript:animationStop()" class="grey" />
                <label id="slider_label" for="slider">Speed: </label>
                <input id="slider" type="range" min="1" max="10" value="5" onchange="javascript:setSpeed()"/><span id="pause_value"></span>
                <select name="export" onchange="exportAs(this.value)">
                    <option value="0" selected="selected">Export as...</option>
                    <option value="1">PNG</option>
                    <option value="2">JPEG</option>
                </select>
            </p>
        </div>
