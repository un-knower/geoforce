$(function(){
	Map.init();
});

var Map = {};
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

	layer_branches = new SuperMap.Layer.Vector("branches");
	// layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});
	layer_orders = new SuperMap.Layer.Vector("orders", {renderers: ["Canvas2"]});

	var strategy = new SuperMap.Strategy.GeoText();
	strategy.style = SuperMap.Egisp.Zone.getRegionTextStyle();
	layer_region_label = new SuperMap.Layer.Vector("Label", {renderers:["Canvas2"], strategies: [strategy]});
	layer_region = new SuperMap.Layer.Vector("region", {renderers:["Canvas2"]});

	var strategy_temp = new SuperMap.Strategy.GeoText();
	strategy_temp.style = SuperMap.Egisp.Zone.getRegionTextStyle();
	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch", {renderers: ["Canvas2"], strategies: [strategy_temp]});

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	map.addLayers([ layer_map, layer_boundry, layer_cloudpois, layer_region, layer_region_label, layer_branches, layer_orders, layer_edit_branch]);
	layer_branches.setVisibility(false);
	layer_region.setVisibility(false);
	layer_region_label.setVisibility(false);

	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	$('.olAlphaImg').attr('title', '11');
	SuperMap.Egisp.SMCity.initUserCity(Map.initDatas);

	var selectStyle = SuperMap.Egisp.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature([layer_orders, layer_branches, layer_cloudpois, layer_region], {
	    onSelect: Map.regionSelect,
	    onUnselect: Map.regionUnSelect,
	    multiple: false,
	    hover: false
	});	
	map.addControl(control_select);
	control_select.activate();
	
	var ovMap = new SuperMap.Control.OverviewMap({maximized: false});
	var layer_ovmap = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	ovMap.layers = [layer_ovmap];
	map.addControl(ovMap);
	map.events.on({"zoomend": Map.zoomEnd});
	map.events.on({"moveend": Map.moveEnd});
}

/**
 * 初始化数据
 */
Map.initDatas = function() {
	SuperMap.Egisp.SMCity.cityTagClick = function() {
		Map.searchBranches();
	}
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
		layer_region_label.setVisibility(isShowRegion);	
		layer_boundry.setVisibility(true);
	}
	else {
		btn.prop("checked", false);
		layer_region.setVisibility(false);		
		layer_region_label.setVisibility(false);	
		layer_boundry.setVisibility(false);	
	}
}
/**
 * 地图缩放平移时重绘显示单个区划的临时图层
 */
Map.moveEnd = function() {
	if(layer_edit_branch.features.length > 0) {
		layer_edit_branch.redraw();
	}
}

/**
 * 地址匹配
 */
Map.geocode = function() {
	var keyword = $.trim($('.text-geocode').val());
	if( keyword == "" || keyword.length == 0 ) {
		SuperMap.Egisp.showPopover("请输入地址");
		return;
	}
	layer_orders.removeAllFeatures();
	layer_edit_branch.removeAllFeatures();
	map.removeAllPopup();
	Map.popup = null;
	SuperMap.Egisp.Geocoder.Table.clear();
	$('.a-data-success, .a-data-failed').hide();
	SuperMap.Egisp.Geocoder.geocode(keyword, 
		function(e) {
			var geo = e.result;			
			var data = [geo];
			SuperMap.Egisp.Geocoder.Table.refresh(data, false);
			if(!geo.smx || geo.smx == 0) {
				SuperMap.Egisp.showPopover("未找到坐标");
				return;
			}
			geo.order = "1";
			var c = new SuperMap.LonLat(geo.smx, geo.smy);
			map.setCenter( c, map.getZoom() );
			Map.showAddressToMap( data, false );
			if( layer_region.visibility == false && geo.areaId && geo.areaId.length > 0 ) {
				Map.showRegionForAddress(geo.areaId);
			}
			Map.openAddressAttrPopup(geo);
		},
		function(error) {
			SuperMap.Egisp.showPopover(error);
		}
	);
}

/**
 * 单独显示地址匹配成功后的所属区划
 */
Map.showRegionForAddress = function(id) {	
	SuperMap.Egisp.showMask();
	SuperMap.Egisp.Zone.searchById(id,
		function(record) {
			SuperMap.Egisp.hideMask();
			if(!record.name) {
				record.name = "";
			}
			var point2Ds = record.points;
			var parts = record.parts;
			if(!parts || parts.length == 0 ) {
				return;
			}
			var attr = record;
			attr.parts = null;
			attr.points = null;
			attr.type = "region_single";
			var geometry = SuperMap.Egisp.Zone.DrawRegion(parts, point2Ds);
			var style = SuperMap.Egisp.Zone.getRegionStyle(record.name);
			attr.oldStyle = style;
			var feature = new SuperMap.Feature.Vector( geometry, attr, style );
			layer_edit_branch.addFeatures(feature);

			var geoText = new SuperMap.Geometry.GeoText(record.center.x, record.center.y, record.name);
			var geotextFeature = new SuperMap.Feature.Vector(geoText);
			geotextFeature.style = SuperMap.Egisp.Zone.getRegionTextStyle();
			geotextFeature.attributes = { type: "text-region" };
			layer_edit_branch.addFeatures(geotextFeature);
			layer_edit_branch.redraw();
		},
		function(info) {
			SuperMap.Egisp.hideMask();
		}
	);
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
 * 选择区划
 */
Map.regionSelect = function(feature) {
	var type = feature.attributes.type;
	switch( type ) {
		case "point":
			Map.selectBranch(feature);
			break;
		case "region":
			selectedFeature = feature;
			/*
			$('#txt_areaName').val(feature.attributes.name);
			$('#txt_areaId').val(feature.attributes.id);
			*/		
			feature.style = SuperMap.Egisp.Zone.getRegionSelectStyle( feature.attributes.name );	
			layer_region.redraw();
			break;
		case "address":
			Map.selectAddress(feature);
			var attr = feature.attributes;
			$('.data-list table tbody tr').removeClass('action');
		    var tr = $('.data-list table tbody tr[data-id="'+ attr.id +'"]').addClass('action');
		    var data_table = $('.data-list .data-results');
		    var offset_top = tr[0].offsetTop, scroll_top = data_table[0].scrollTop;
		    if( Math.abs( offset_top - scroll_top ) > data_table.height() || offset_top < scroll_top ) {
		    	data_table.animate({scrollTop: (offset_top)}, 100);
		    }
			break;
		case "point-cloud":
			Map.openCloudPopup( feature.attributes );
			break;
		case 'point-national':
			SuperMap.Egisp.SMCity.showCurrentProvince(feature.attributes.province, feature.attributes.admincode);
        	SuperMap.Egisp.SMCity.cityTagClick();
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
		Map.searchRegions();
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
	layer_region.removeAllFeatures();
	layer_region_label.removeAllFeatures();
	layer_edit_branch.removeAllFeatures();
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

	SuperMap.Egisp.Zone.search(param, 
		function(data){
			SuperMap.Egisp.hideMask();
			Map.showRegionsToMap(data);
		},
		function(error){
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.showPopover("当前查询到0条区划数据");
			layer_region.removeAllFeatures();
		}
	)
}
/**
 * 将区划显示至地图上
 */
Map.showRegionsToMap = function(data) {
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
	layer_orders.removeAllFeatures();
	map.removeAllPopup();
	SuperMap.Egisp.Geocoder.Table.clear();
	layer_cloudpois.removeAllFeatures();
	$('.cloud-pois').hide();
}

/**
 * 平移
 */
Map.pan = function() {
	
}

/**
 * 查询订单
 */
Map.search = function() {	  
    SuperMap.Egisp.Geocoder.Table.clear();
	var pager = $('#data_pager');
	var pageIndex = Number(pager.attr('page')) + 1;
	var param = {
		pageSize: 10,
		pageNo: pageIndex
	};
	var keyword = $('.data-list .search-input').val();
	param.keyword = keyword;
	SuperMap.Egisp.showMask();
	layer_orders.removeAllFeatures();
	SuperMap.Egisp.Geocoder.search(param,
		function(data) {
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.Geocoder.Table.refresh(data);
			$('.a-data-success, .a-data-failed').show();
			Map.showAddressToMap(data);
		},
		function(info) {
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.Geocoder.Table.clear();
			SuperMap.Egisp.showPopover(info);
		}
	);
}

/**
 * 订单显示至地图上
 */
Map.showAddressToMap = function(data, shows) {	
	map.removeAllPopup();
	var fs = [], len = data.length, data_success = [], data_failed = [];
	for( var i=len; i--; ) {
		var item = data[i];
		if( item.smx && item.smx > 0 ) {
			data_success.push(item);
		}
		else {
			data_failed.push(item);
		}
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
		var poi = new SuperMap.Feature.Vector(geo_point);
		poi.style = {
			externalGraphic: 'assets/map/poi-lightblue.png',
			graphicWidth: 20,
			graphicHeight: 30,
			graphicTitle: item.address,
        	cursor: "pointer"
		};
		item.order = (i+1);
		poi.attributes = item;
		poi.attributes.type = "address";
		fs.push(poi);
	}
	layer_orders.addFeatures(fs);
	SuperMap.Egisp.Geocoder.Table.data_success = data_success.concat();
	SuperMap.Egisp.Geocoder.Table.data_failed = data_failed.concat();

	if( typeof(shows) === "undefined" ) {
		$('.a-data-success > span').html('('+ data_success.length +')');
		$('.a-data-failed > span').html('('+ data_failed.length +')');
	}	
}

/**
 * 点击订单点
 */
Map.selectAddress = function(feature) {
	var attr = feature.attributes;
	Map.openAddressAttrPopup(attr);
}
/**
 * 点击订单点，弹出信息窗
 */
Map.openAddressAttrPopup = function(attr) {
	var h = SuperMap.Egisp.Geocoder.getAttrPopupHtml(attr);
	var lonlat = new SuperMap.LonLat(attr.smx, attr.smy);
	Map.popup = null;
	Map.popup = new SuperMap.Popup.FramedCloud("popup-near",
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
}
/**
 * 根据ID从订单图层查找订单点
 */
Map.getOrderFromLayerById = function(id) {
	var features = layer_orders.features;
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
 * 显示分单成功的订单
 */
Map.showSuccessData = function() {
	Map.showDatas( SuperMap.Egisp.Geocoder.Table.data_success );
}
/**
 * 显示分单失败的订单
 */
Map.showFailedData = function() {
	Map.showDatas( SuperMap.Egisp.Geocoder.Table.data_failed );
}
Map.showDatas = function(data){
	console.log(data)
	map.removeAllPopup();
	layer_orders.removeAllFeatures();
	var len = data.length;
	if( len === 0 ) {			
	    $('.data-list table tbody').html('');
	    $('.data-list .totality').html('(0条)');
		return;
	}
	map.removeAllPopup();

	var fs = [], len = data.length, data_success = [], data_failed = [];
	for( var i=len; i--; ) {
		var item = data[i];
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
		var poi = new SuperMap.Feature.Vector(geo_point);
		poi.style = {
			externalGraphic: 'assets/map/poi-lightblue.png',
			graphicWidth: 20,
			graphicHeight: 30,
			graphicTitle: item.address,
        	cursor: "pointer"
		};
		item.order = (i+1);
		poi.attributes = item;
		poi.attributes.type = "address";
		fs.push(poi);
	}
	layer_orders.addFeatures(fs);
	
	SuperMap.Egisp.Geocoder.Table.refresh(data);
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