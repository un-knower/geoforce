$(function(){
	Map.init();
});

//自由画区
var control_drawPolygon = null;
//沿路画区
var control_drawPathRegion = null;
//编辑区划
var control_editPolygon = null;
//线拆分
var control_drawSplitLine = null;
//面拆分
var control_drawSplitArea = null;

var Map = {
	//是否允许选择区划
	allowSelect: true,
	//是否正在进行沿路画区
	isDrawingPathRegion: false
};
Map.init = function() {
	controls_zoombars = [        
	    new SuperMap.Control.PanZoomBar({showSlider:true}),
	    new SuperMap.Control.Navigation({
	        dragPanOptions: {
	            enableKinetic: true
	        }
	    })
	];
	map = new SuperMap.Map("map", 
		{ 
			controls: controls_zoombars,
			allOverlays: true
		}
	);

	layer_map = new SuperMap.Layer.CloudLayer({url: urls.map_img});

	layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});
	layer_orders = new SuperMap.Layer.Vector("orders", {renderers: ["Canvas2"]});

	var strategy = new SuperMap.Strategy.GeoText();
	strategy.style = SuperMap.Egisp.Zone.getRegionTextStyle();
	layer_region_label = new SuperMap.Layer.Vector("Label", {renderers:["Canvas2"], strategies: [strategy]});
	layer_region = new SuperMap.Layer.Vector("region", {renderers:["Canvas2"]});
	layer_edit = new SuperMap.Layer.Vector("edit");

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	map.addLayers([ layer_map, layer_boundry, layer_cloudpois, layer_region, layer_region_label, layer_branches, layer_orders, layer_edit]);
	layer_branches.setVisibility(false);
	layer_region.setVisibility(false);
	layer_region_label.setVisibility(false);
	
	//自由画区
	control_drawPolygon = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Polygon, { multi: false });
	control_drawPolygon.events.on({"featureadded": Map.drawRegionCompleted});
	
	control_drawSplitArea = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Polygon, { multi: false });
	control_drawSplitArea.events.on({"featureadded": Map.drawFeatureCompleted});
	control_drawSplitLine = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Path, { multi: false });
	control_drawSplitLine.events.on({"featureadded": Map.drawFeatureCompleted});
	control_drawPathRegion = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Point, { multi: true });
	control_drawPathRegion.events.on({"featureadded": Map.drawCompleted});
	control_editPolygon = new SuperMap.Control.ModifyFeature(layer_edit);
	map.addControls([ 
		control_drawPolygon,
		control_drawPathRegion,
		control_editPolygon,
		control_drawSplitLine,
		control_drawSplitArea
	])

	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	$('.olAlphaImg').attr('title', '11');
	Map.ipLocate();

	var selectStyle = SuperMap.Egisp.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature([layer_orders, layer_branches, layer_region], {
	    onSelect: Map.regionSelect,
	    onUnselect: Map.regionUnSelect,
	    multiple: false,
	    hover: false,
	    multipleKey: "ctrlKey",
	    clickout: false
	});	
	map.addControl(control_select);
	control_select.activate();
	var ovMap = new SuperMap.Control.OverviewMap({maximized: false});
	var layer_ovmap = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	ovMap.layers = [layer_ovmap];
	map.addControl(ovMap);
		
	map.events.on({"zoomend": Map.zoomEnd});
}

Map.ipLocate = function() {
	SuperMap.Egisp.showMask();
	SuperMap.Egisp.IPLocate(urls.ip_locate, 
		function(e) {	
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.SMCity.init(e);
		}, 
		function() {
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.SMCity.init({city: "北京"});
		}
	);	
}

/**
 * 地图缩放完成
 */
Map.zoomEnd = function(e) {
	var zoom = e.object.zoom;
	$('.olAlphaImg').attr('title', zoom);

	var btn = $('#btn_showRegion');
	var isShowRegion = btn.attr("isShowRegion") ? true : false;
	if( zoom > 4 ) {
		btn.prop("checked", isShowRegion);
		layer_region.setVisibility(isShowRegion);	
		layer_boundry.setVisibility(true);
	}
	else {
		btn.prop("checked", false);
		layer_region.setVisibility(false);		
		layer_boundry.setVisibility(false);	
	}
}

/**
 * 显示网点图层
 */
Map.showBranchLayer = function() {
	var me = $(this);
	var bool = me.prop('checked');
	layer_branches.setVisibility(bool);

	if( bool && layer_branches.features.length === 0 ) {
		Map.searchBranches();
	}
	else {
		if(!Map.isDragging) {
			map.removeAllPopup();
		}
	}
}
/**
 * 查询网点
 */
Map.searchBranches = function() {
	layer_branches.removeAllFeatures();

	var param = { pageNo: -1 };
	SuperMap.Egisp.Point.Search(param, 
		function(data) {				
			Map.showBranchesToMap(data);
		},
		function(error) {

		}
	);
}
/**
 * 地图上显示网点数据
 */
Map.showBranchesToMap = function(data) {
	// map.removeAllPopup();
	layer_branches.removeAllFeatures();
	var len = data.length;
	if( len === 0 ) {
		SuperMap.Egisp.showPopover("当前查询到0条网点数据");
		return;
	}
	var pois = [];
	for( var i=0; i<len; i++ ) {
		var item = data[i];
		if(!item.smx || item.smx == 0) {
			continue;
		}

		if( !item.iconStyle ) {
			item.iconStyle = "assets/map/red/s.png";
		}
		var style = SuperMap.Egisp.Point.getBranchStyle( item.iconStyle );
		var size = new SuperMap.Size(style.width, style.height);

		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
		var poi = new SuperMap.Feature.Vector(geo_point);
		poi.style = {
			externalGraphic: item.iconStyle,
			graphicWidth: style.width,
			graphicHeight: style.height,
			graphicTitle: item.name,
        	cursor: "pointer"
		};

		poi.attributes = item;
		poi.attributes.type = "point";
		pois.push(poi);
	}
	layer_branches.addFeatures(pois);
}
/**
 * 选择区划
 */
Map.regionSelect = function(feature) {
	var type = feature.attributes.type;

	switch( type ) {
		case "point":
			Map.selectBranch(feature);
			break;
		case "region":
			// Map.clearSelectedFeatures();
			selectedFeature = feature;
			selectedFeatures.push(feature);
			feature.style = SuperMap.Egisp.Zone.getRegionSelectStyle();	
			layer_region.redraw();
			SuperMap.Egisp.Zone.Table.scrollToFeature(feature);
			Map.openRegionAttrPopup(feature);
			break;
		case "order":
			Map.selectOrder(feature);
			break;
		case "order-edit":
			Map.openEditOrderPopup(feature.attributes);
			break;
	}
}
/**
 * 取消选择区划
 */
Map.regionUnSelect = function(feature) {
	var type = feature.attributes.type;
	if( type === "region" ) {
		feature.style = feature.attributes.oldStyle;
		layer_region.redraw();
		selectedFeature = null;
		$('#txt_areaName, #txt_areaId').val('');
	}	
}
/**
 * 清空已选择的区划
 */
Map.clearSelectedFeatures = function() {
	var len = selectedFeatures.length;
	if(len === 0) {
		return;
	}
	for(var i=len; i--; ) {
		var sf = selectedFeatures[i];
		var f = Map.getFeatureFromLayerById( sf.attributes.id );
		if(f) {
			f.style = f.attributes.oldStyle;
		}		
	}
	layer_region.redraw();
	selectedFeatures = [];
	selectedFeature = null;
}

/**
 * 弹出区划属性框
 */
Map.openRegionAttrPopup = function(feature) {
	var attr = feature.attributes;
	var h = SuperMap.Egisp.Zone.getAttrPopupHtml(attr);	
	var lonlat = new SuperMap.LonLat(attr.center.x, attr.center.y);
	Map.popup = null;
	Map.popup = new SuperMap.Popup.FramedCloud("popup-region",
		lonlat,
		new SuperMap.Size(300, 70),
		h,
		null,
		false,
		null
	);	
	
	Map.popup.autoSize = true;
	Map.popup.panMapIfOutOfView = true;
	Map.popup.relativePosition = "tr";
	map.removeAllPopup();
	map.addPopup(Map.popup);
	$('.popup-close').unbind("click").click(function(){
		Map.popup.hide();
	});

	var span = $(".region-adminname");
	SuperMap.Egisp.Zone.getAdminnameByCode( attr.admincode, 
		function(data){
			span.html(data);
		},
		function(){
			span.html("");
		}
	);

}

/**
 * 点击网点
 */
Map.selectBranch = function(poi) {
	var marker = poi.attributes;
	marker.lonlat = new SuperMap.LonLat(marker.smx, marker.smy);
	Map.openOrderAttrPopup(marker);
}
/**
 * 点击网点，弹出属性
 */
Map.openOrderAttrPopup = function(marker) {
	var netPicPath = marker.netPicPath ? 
		urls.server +'/pointService/getImg?path=' + marker.netPicPath 
		: "assets/map/branch.png";

	var h =  '<div class="map-popup">';
		h += SuperMap.Egisp.Point.getAttrPopupHtml(marker, false);
		h += '</div>';

	Map.popup = new SuperMap.Popup.FramedCloud("popup-near",
		marker.lonlat,
		new SuperMap.Size(300, 70),
		h,
		null,
		false,
		null
	);
	Map.popup.autoSize = true;
	Map.popup.panMapIfOutOfView = true;
	Map.popup.relativePosition = "tr";
	map.removeAllPopup();
	map.addPopup(Map.popup);

	var span = $(".bind-cars");
	SuperMap.Egisp.Point.getBindingCars( marker.id, 
		function(data){
			span.html(data);
		},
		function(){
			span.html("");
		}
	);

	$('.popup-close').unbind("click").click(function(){
		Map.popup.hide();
	});
	$('.popup-edit').unbind("click").click(function(){
		Map.showEditOrder(marker);
	});
}
/**
 * 显示区划
 */
Map.showRegionLayer = function() {
	var me = $(this);
	var bool = me.prop('checked');
	if( bool ) {
		var code = $('.smcity').attr("admincode");
		if( !code || code === "" ) {
			SuperMap.Egisp.showHint("如果查看区划，请选择省市");
			me.prop("checked", false);
			return;
		}
		var zoom = map.getZoom();
		if(zoom < 5) {
			SuperMap.Egisp.showHint("请将地图放大到5级以上再显示区划");
			me.prop("checked", false);
			return;
		}
		if( layer_region.features.length === 0 ) {
			Map.searchRegions();
		}
		me.attr("isShowRegion", "true");
	}
	else {
		me.removeAttr("isShowRegion");
	}
	layer_region.setVisibility(bool);
	layer_region_label.setVisibility(bool);
}
/**
 * 查询区划
 */
Map.searchRegions = function() {
	var keyword = $('#txt_keyword_search_regions').val();
	var smcity = $('.smcity');
	var admincode = smcity.attr('admincode');
	var level = smcity.attr('level');
	if( admincode === "" ) {
		SuperMap.Egisp.showPopover("查询区划请选择省");
		return;
	}
	SuperMap.Egisp.showMask();
	var param = {
		admincode: admincode,
		level: level
	}
	if( keyword != "" && keyword.length > 0 ) {
		param.areaName = keyword;
		param.areaNumber = keyword;
	}

	SuperMap.Egisp.Zone.search(param, 
		function(data){
			SuperMap.Egisp.hideMask();
			
			Map.showRegionsToMap(data);
			SuperMap.Egisp.Zone.Table.refresh(data);
		},
		function(error){
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.showHint("区划查询失败");
			layer_region.removeAllFeatures();
		}
	)
}
/**
 * 将区划显示至地图上
 */
Map.showRegionsToMap = function(data) {
	layer_region.removeAllFeatures();
	layer_region_label.removeAllFeatures();
	var len = data.length;
	if(len === 0) {
		SuperMap.Egisp.showPopover("当前查询到0条区划数据");
		return;
	}
	var fs = [], ls = [];
	for(var i = len; i--; ) {
		var record = data[i];
		if(!record.name) {
			record.name = "";
		}
		var point2Ds = record.points;
		var parts = record.parts;
		if(!parts || parts.length == 0 ) {
			continue;
		}
		var attr = record;
		attr.parts = null;
		attr.points = null;
		attr.type = "region";
		attr.branch_name = SuperMap.Egisp.Zone.getBranchName(record.pointnames);

		var geometry = SuperMap.Egisp.Zone.DrawRegion(parts, point2Ds);
		var style = SuperMap.Egisp.Zone.getRegionStyle(record.name);
		attr.oldStyle = style;
		var feature = new SuperMap.Feature.Vector( geometry, attr, style );		
		fs.push(feature);

		var geoText = new SuperMap.Geometry.GeoText(record.center.x, record.center.y, record.name);

		var geotextFeature = new SuperMap.Feature.Vector(geoText);
		geotextFeature.style = SuperMap.Egisp.Zone.getRegionTextStyle();
		geotextFeature.attributes = { type: "text-region" };
		ls.push(geotextFeature);
	}
	layer_region.addFeatures(fs);	
    layer_region_label.addFeatures(ls);	
}
/**
 * 清空
 */
Map.clear = function() {
	$('#btn_showBranch, #btn_showRegion').prop('checked', false);
	$('#btn_showRegion').removeAttr('isShowRegion');
	layer_region.removeAllFeatures();
	layer_region.setVisibility(false);
	layer_region_label.removeAllFeatures();
	layer_region_label.setVisibility(false);
	layer_branches.removeAllFeatures();
	layer_branches.setVisibility(false);
	map.removeAllPopup();
	Map.popup = null;
	SuperMap.Egisp.Order.Table.clear();
	layer_orders.removeAllFeatures();
	Map.endDragBranch();
	layer_cloudpois.removeAllFeatures();
	$('.cloud-pois').hide();
	SuperMap.Egisp.Zone.Table.clear();
}

/**
 * 平移
 */
Map.pan = function() {	
	Map.endDragBranch();
	map.removeAllPopup();
	Map.popup = null;
}

/**
 * 查询区划
 */
Map.search = function() {		
	Map.searchRegions();
}

/**
 * 根据ID获取区划
 */
Map.getFeatureFromLayerById = function(id) {
	var features = layer_region.features;
	var len = features.length;
	if(len < 1 ){
		return null;
	}
	for( var i=len; i--; ) {
		var f = features[i];
		if( f.attributes.id === id ) {
			return f;
		}
	}
	return null;
}

/**
 * 点击工具栏中的自由画区
 */
Map.addRegionClick = function() {	
	if( control_editPolygon.active ) {
		SuperMap.Egisp.showHint("正在编辑区划\""+ selectedFeature.attributes.name +"\"的节点");
		return;
	}
	$('#btn_showRegion').prop('checked', true).attr('isShowRegion', 'true');
	layer_region.setVisibility(true);
	layer_region_label.setVisibility(true);
	if(layer_region.features.length == 0) {
		$('.data-list .search-input').val('');
		Map.search();
	}
	map.removeAllPopup();
	control_drawPolygon.activate();
}

/**
 * 点击工具栏中的自由画区
 */
Map.drawRegionCompleted = function(feature) {
	control_drawPolygon.deactivate();
	
}

/**
 * 点击工具栏中的沿路画区
 */
Map.addPathRegionClick = function() {

}
/**
 * 点击工具栏中的编辑
 */
Map.editRegionClick = function() {

}
/**
 * 点击工具栏中的属性
 */
Map.attrRegionClick = function() {

}
/**
 * 点击工具栏中的删除
 */
Map.deleteRegionClick = function() {
	if( control_editPolygon.active ) {
		SuperMap.Egisp.showHint("正在编辑区划\""+ selectedFeature.attributes.name +"\"的节点");
		return;
	}
	if( selectedFeature == null || selectedFeatures.length != 1 ) {
		SuperMap.Egisp.showHint("请选择待删除的区划");
		return;		
	}
	var attr = selectedFeature.attributes;
	$('button[option="delete-region"]').attr({
		'data-id': attr.id,
		'data-name': attr.name
	});
	$('.delete-region-name').html( attr.name );
	$("#modal_delete_region").modal({
		backdrop: "static",
		keyboard: false
	});			
}
Map.deleteRegion = function() {
	var me = $(this);
	SuperMap.Egisp.Zone.remove(me.attr('data-id'),
		function() {
			$("#modal_delete_region").modal('hide');
			SuperMap.Egisp.showPopover("区划\""+ me.attr('data-name') +"\"删除成功");
			Map.search();
			map.removeAllPopup();
		},
		function(info) {
			info = info == "" ? "区划\"" + m.attr('data-name') + "\"删除失败" : info;
			SuperMap.Egisp.showPopover(info);			
		}	
	);
}

/**
 * 点击工具栏中的合并
 */
Map.mergeRegionClick = function() {

}
/**
 * 点击工具栏中的线拆分
 */
Map.lineSplitRegionClick = function() {

}
/**
 * 点击工具栏中的面拆分
 */
Map.areaSplitRegionClick = function() {

}





















/**
 * 从云平台搜索
 */
Map.searchFromCloud = function() {
	layer_cloudpois.removeAllFeatures();
	map.removeAllPopup();	

    $('.header .search-from-cloud .child').hide();
	var keyword = $("#txt_keyword_cloud").val();
	if( keyword.length < 1 ) {
		SuperMap.Egisp.showPopover("请输入搜索关键字");
		return;
	}
	var bounds = map.getExtent();
	var param = {
		keyword: keyword,
		max_x: bounds.right,
		min_x: bounds.left,
		max_y: bounds.top,
		min_y: bounds.bottom
	};
	SuperMap.Egisp.Point.searchFromCloud(param, 
		function(e){				
			var data = e.results;
			var len = data.length;
			var pois = [];
			for( var i=0; i<len; i++ ) {
				var item = data[i];
				var position = new SuperMap.LonLat(item.smx, item.smy);				
				
				var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = {
					externalGraphic: "assets/num/" + (i+1) + ".gif",
					graphicWidth: 37,
					graphicHeight: 27,
					graphicTitle: item.name,
		        	cursor: "pointer"
				};
				item.type = "point-cloud";
				poi.attributes = item;
				pois.push(poi);
			}
			layer_cloudpois.addFeatures(pois);
			Map.showCloudSearchResult(e);
		}, 
		function(){
			layer_cloudpois.removeAllFeatures(); 
            $("#pager_cloudpois").hide();
    		$('.header .search-from-cloud .cloud-pois .content').html("未查询到结果，请尝试重新输入关键字");
    		$('.header .search-from-cloud .child').show();
		}
	);
}

/**
 * 显示云平台搜索结果到div
 */
Map.showCloudSearchResult = function(e){	
    var div = $('.header .search-from-cloud .cloud-pois .content');
    var h = SuperMap.Egisp.Point.getCloudSearchHtml(e.results);
    div.html(h);     

    $('.header .search-from-cloud .child').show();
    $('a[option="to-cloud-poi"]').unbind('click').click(function(){
        var me = $(this);
        var poi = Map.getCloudPoiById( me.attr("data-id") );
        if(!poi) {
        	return;
        }
        var attr = poi.attributes;
        Map.openCloudPopup(attr);
    });
}
Map.getCloudPoiById = function(id) {
	var pois = layer_cloudpois.features.concat();
	if(pois.length < 1) {
		return false;
	}
	var len = pois.length;
	for(var i=len; i--; ) {
		var poi = pois[i];
		if(poi.attributes.poi_id === id) {
			return poi;
		}
	}
	return false;
}
/**
 * 显示云平台搜索结果popup
 */
Map.openCloudPopup = function(attr) {
	var position = new SuperMap.LonLat( attr.smx, attr.smy );
    attr.lonlat = position;

	var h =  '<div class="map-popup" style="min-height: 60px;">';
		h +=  '<div class="map-popup-content">';
		h += SuperMap.Egisp.Point.getCloudPopupHtml(attr);
		h += '</div>';

		h += '</div>';

	Map.popup = new SuperMap.Popup.FramedCloud("popup-cloud",
		position,
		new SuperMap.Size(300, 70),
		h,
		null,
		false,
		null,
		true
	);
	Map.popup.autoSize = true;
	Map.popup.panMapIfOutOfView = true;
	Map.popup.relativePosition = "tr";
	map.removeAllPopup();
	map.addPopup(Map.popup);
	// var zoom = map.getZoom();
	// map.setCenter(position, (zoom > 11 ? zoom : 11) );
	$('.popup-close').unbind("click").click(function(){
		map.removeAllPopup();
	});		
}