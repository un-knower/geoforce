$(function(){
	Map.initMap();
});

var Map = {}, layer_edit_branch_label = null;
Map.initMap = function() {
	controls_zoombars = [        
//	    new SuperMap.Control.PanZoomBar({showSlider:true}),
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
	Map.init();

	layer_branches = new SuperMap.Layer.Vector("branches");
	// layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});
	layer_orders = new SuperMap.Layer.Vector("orders", {renderers: ["Canvas2"]});

	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch");
	var strategy_temp = new SuperMap.Strategy.GeoText();
		strategy_temp.groupField = "style_status";
		strategy_temp.styleGroups = Dituhui.Zone.getRegionTextStyleGroups();
	layer_edit_branch_label = new SuperMap.Layer.Vector("edit_branch_label", { strategies: [strategy_temp]});

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	
	layer_branches.setVisibility(false);
	
	map.addLayers([ layer_boundry, layer_cloudpois ]);
	Region.init();
	map.addLayers([layer_edit_branch, layer_edit_branch_label, layer_orders, layer_branches]);

	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	$('.olAlphaImg').attr('title', '11');
	Dituhui.SMCity.initUserCity(Map.initDatas);

	var selectStyle = Dituhui.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature([layer_orders, layer_branches, layer_cloudpois, layer_region], {
	    onSelect: Map.regionSelect,
	    onUnselect: Map.regionUnSelect,
	    multiple: false,
	    hover: false
	});	
	map.addControl(control_select);
	control_select.activate();
	
	var ovMap = new SuperMap.Control.OverviewMap({id:"overviewMap", maximized: false});
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
	Dituhui.SMCity.cityTagClick = function() {
		Map.searchBranches();
		
		var sc = $(".smcity");
		poisearch.city = sc.attr("admincode");
		if(sc.attr("level") == 2) {
			poisearch.city = sc.attr("admincode").substr(0, 4) + "00";
		}
		poisearch.changeCity();
	}
	
	poisearch = new POISearch({
		id: "txt_keyword_cloud",
		city: $(".smcity").attr("admincode"),
		map: map,
		layer: layer_cloudpois,
		savePOI: false
	});
}

Map.ipLocate = function() {
	Dituhui.showMask();
	Dituhui.IPLocate(urls.ip_locate, 
		function(e) {	
			Dituhui.hideMask();
			Dituhui.SMCity.init(e);
		}, 
		function() {
			Dituhui.hideMask();
			Dituhui.SMCity.init({city: "北京"});
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
		Dituhui.showPopover("请输入地址");
		return;
	}
	layer_orders.removeAllFeatures();
	layer_edit_branch.removeAllFeatures();
	layer_edit_branch_label.removeAllFeatures();
	map.removeAllPopup();
	Map.popup = null;
	Dituhui.Geocoder.Table.clear();
	$('.a-data-success, .a-data-failed').hide();
	Dituhui.Geocoder.geocode(keyword, 
		function(e) {
			var geo = e.result;			
			var data = [geo];
			Dituhui.Geocoder.Table.refresh(data, false);
			if(!geo.smx || geo.smx == 0) {
				Dituhui.showPopover("未找到坐标");
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
			Dituhui.showPopover(error);
		}
	);
}

/**
 * 单独显示地址匹配成功后的所属区划
 */
Map.showRegionForAddress = function(id) {	
	Dituhui.showMask();
	Dituhui.Zone.searchById(id,
		function(record) {
			Dituhui.hideMask();
			if(!record.name) {
				record.name = "";
			}
			var point2Ds = record.points, dcode = Dituhui.User.dcode;
			var parts = record.parts;
			if(!parts || parts.length == 0 ) {
				return;
			}
			var attr = record;
			attr.parts = null;
			attr.points = null;
			attr.type = "region_single";
			var geometry = Dituhui.Zone.DrawRegion(parts, point2Ds);
			var style = Dituhui.Zone.getRegionStyle(record.name);
			/**
			 * 不是自己的区划
			 */
			attr.style_status = record.area_status;
			if(attr.dcode.substr(0, dcode.length) !== dcode) {
				style = Dituhui.Zone.getNonEditableRegionStyle(record.name);
				attr.style_status = 2.5;
			}
			attr.oldStyle = style;
			var feature = new SuperMap.Feature.Vector( geometry, attr, style );
			layer_edit_branch.addFeatures([feature]);

			var geoText = new SuperMap.Geometry.GeoText(record.center.x, record.center.y, record.name);
			var geotextFeature = new SuperMap.Feature.Vector(geoText);
			var attr_label = attr;
			attr_label.type = "text-region";
			geotextFeature.attributes = attr_label;
			layer_edit_branch_label.addFeatures([geotextFeature]);
		},
		function(info) {
			Dituhui.hideMask();
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
			Region.select(feature);
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
			poisearch.openPopup(feature.attributes);
			break;
		case 'point-national':
			Dituhui.SMCity.showCurrentProvince(feature.attributes.province, feature.attributes.admincode);
        	Dituhui.SMCity.cityTagClick();
        	break;
	}
}
/**
 * 取消选择区划
 */
Map.regionUnSelect = function(feature) {
	var type = feature.attributes.type;
	if( type === "region" ) {
		Region.unselect(feature);
	}
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
	Dituhui.Geocoder.Table.clear();
	layer_cloudpois.removeAllFeatures();
    $('.header .search-from-cloud .child').addClass("hide");
    layer_edit_branch.removeAllFeatures();
    layer_edit_branch_label.removeAllFeatures();
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
    Dituhui.Geocoder.Table.clear();
	var pager = $('#data_pager');
	var pageIndex = Number(pager.attr('page')) + 1;
	var param = {
		pageSize: 10,
		pageNo: pageIndex
	};
	var keyword = $('.data-list .search-input').val();
	param.keyword = keyword;
	Dituhui.showMask();
	layer_orders.removeAllFeatures();
	Dituhui.Geocoder.search(param,
		function(data) {
			Dituhui.hideMask();
			Dituhui.Geocoder.Table.refresh(data);
			$('.a-data-success, .a-data-failed').show();
			Map.showAddressToMap(data);
		},
		function(info) {
			Dituhui.hideMask();
			Dituhui.Geocoder.Table.clear();
			Dituhui.showPopover(info);
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
			externalGraphic: urls.server + '/resources/assets/map/poi-lightblue.png',
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
	Dituhui.Geocoder.Table.data_success = data_success.concat();
	Dituhui.Geocoder.Table.data_failed = data_failed.concat();

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
	var h = Dituhui.Geocoder.getAttrPopupHtml(attr);
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
	Map.showDatas( Dituhui.Geocoder.Table.data_success );
}
/**
 * 显示分单失败的订单
 */
Map.showFailedData = function() {
	Map.showDatas( Dituhui.Geocoder.Table.data_failed );
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
			externalGraphic: urls.server + '/resources/assets/map/poi-lightblue.png',
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
	
	Dituhui.Geocoder.Table.refresh(data);
}


