
/**
 * 临时的拖动控件
 */
var drag = null;
/**
 * 临时的编辑图层
 */
var layer_edit_temp = null;
/**
 * 网点图层编辑，添加临时图层编辑
 */
Map.isDragging = false;
Map.enableEdit = function() {
	var poi = layer_edit_branch.features[0];

	layer_edit_temp = new SuperMap.Layer.Vector("edit_temp");
	map.addLayers([layer_edit_temp]);

    var geometry = new SuperMap.Geometry.Point(poi.geometry.x, poi.geometry.y);
    var attr = poi.attributes;
    var style = poi.style;

    var f = new SuperMap.Feature.Vector(geometry, attr, style);
	layer_edit_temp.addFeatures(f);

	// layer_edit_branch.removeAllFeatures();

	drag = new SuperMap.Control.DragFeature(layer_edit_temp);
	map.addControl( drag );
	drag.activate();
	Map.isDragging = true;
	drag.onComplete = function(feature, pixel) {	
        var lonlat = map.getLonLatFromPixel(pixel);		
        if(Map.popup != null ) {
            Map.popup.hide();
            Map.popup.lonlat = lonlat;
            Map.popup.show();            
        }
        else {
	        if(poi.attributes.type === 1) {
				Map.openAddBranchPopup( lonlat );
	        }
        }
        Map.regeocode(lonlat);

        if(Baidu && Baidu.using) {
            var coord = Baidu.restoreCoord(lonlat.lon, lonlat.lat);
            lonlat.lon = coord.x;
            lonlat.lat = coord.y;
        }
        $('input[name="smx"]').val(lonlat.lon);
        $('input[name="smy"]').val(lonlat.lat);

        var coord = Dituhui.metersToLatLon( new SuperMap.LonLat(lonlat.lon, lonlat.lat) );
        $('.smx-popup').val(coord.lon.toFixed(2));
        $('.smy-popup').val(coord.lat.toFixed(2));
        Map.needCaculateCoord = false;
	}
}
Map.endDragBranch = function() {
	if(!layer_edit_temp){
		return;
	}
	layer_edit_temp.removeAllFeatures();
	drag.deactivate();
	map.removeControl(drag);
	
	drag = null;
	map.removeLayer(layer_edit_temp,true);
	layer_edit_temp.destroy();
	layer_edit_temp = null;
	Map.isDragging = false;
}
