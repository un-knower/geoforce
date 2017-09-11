
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
	map.addLayer(layer_edit_temp);
	layer_edit_temp.addFeatures(poi);
	layer_edit_branch.removeAllFeatures();

	drag = new SuperMap.Control.DragFeature(layer_edit_temp);
	map.addControl( drag );
	drag.activate();
	Map.isDragging = true;
	drag.onComplete = function(feature, pixel) {	
        var lonlat = map.getLonLatFromPixel(pixel);		
        feature.attributes.lonlat = lonlat;
        if(Map.popup != null && Map.popup.id === "popup-edit" ) {
            Map.popup.hide();
            Map.popup.lonlat = lonlat;
            Map.popup.show();            
        }
        else {
            var attr = feature.attributes;
            Map.openEditAddressPopup(attr);
        }
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
