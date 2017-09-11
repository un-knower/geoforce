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
	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch", {renderers: ["Canvas2"]});

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	map.addLayers([ layer_map, layer_boundry, layer_region, layer_region_label, layer_cloudpois, layer_branches, layer_orders, layer_edit_branch]);
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
			layer_region_label.removeAllFeatures();
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
	map.removeAllPopup();
	SuperMap.Egisp.Address.Table.clear();
	layer_orders.removeAllFeatures();
	Map.endDragBranch();
	Map.popup = null;
	layer_cloudpois.removeAllFeatures();
	$('.cloud-pois').hide();
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
 * 查询地址
 * @param: isShowFirstAddress - boolean 是否显示第一个点
 */
Map.search = function(isShowFirstAddress) {
	var pager = $('#data_pager');
	var pageIndex = Number(pager.attr('page')) + 1;
	var param = {
		pageSize: 20,
		pageNo: pageIndex
	};
	var keyword = $('.data-list .search-input').val();
	if(keyword != "") {
		param.keyword = keyword;
	}
	SuperMap.Egisp.showMask();
	layer_orders.removeAllFeatures();
	SuperMap.Egisp.Address.search(param,
		function(data) {
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.Address.Table.refresh(data);
			Map.showAddressToMap(data, isShowFirstAddress);
		},
		function(info) {
			SuperMap.Egisp.hideMask();
			SuperMap.Egisp.Address.Table.clear();
			SuperMap.Egisp.showPopover(info);
		}
	);
}

/**
 * 纠错的显示至地图上
 */
Map.showAddressToMap = function(data, isShowFirstAddress) {
	var fs = [], len = data.length, data_success = [], data_failed = [];
	for( var i=len; i--; ) {
		var item = data[i];
		item.smx = item.x;
		item.smy = item.y;

		if( i === 0 && isShowFirstAddress ){
			if( !item.smx || item.smx == 0 ) {
				item.lonlat = map.getCenter();
				SuperMap.Egisp.showHint("请拖动地址(红色标注)进行纠错");
				Map.showEditAddress(item);
			}
			else {				
	            var lonlat = new SuperMap.LonLat(item.smx, item.smy), zoom = map.getZoom();
	            map.setCenter( lonlat, zoom > 10 ? zoom : 10);
				Map.openAddressAttrPopup(item);
			}
			var list = $('.data-list');
			if( list.attr('status') == "min" ) {
				$('.data-list .head .second').click();
			}
		}	
		if( !item.smx || item.smx == 0 ) {
			continue;
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
}

/**
 * 点击地址点
 */
Map.selectAddress = function(feature) {
	var attr = feature.attributes;
	Map.openAddressAttrPopup(attr);
}
/**
 * 点击地址点，弹出信息窗
 */
Map.openAddressAttrPopup = function(attr) {	
	Map.endDragBranch();
	var h = SuperMap.Egisp.Address.getAttrPopupHtml(attr);
	var lonlat = new SuperMap.LonLat(attr.smx, attr.smy);
	attr.lonlat = lonlat;
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

	$('.popup-edit').unbind("click").click(function(){
		Map.showEditAddress(attr);
	});

	$('.data-list table tbody tr').removeClass('action');
    var tr = $('.data-list table tbody tr[data-id="'+ attr.id +'"]').addClass('action');
    var data_table = $('.data-list .data-results');
    var offset_top = tr[0].offsetTop, scroll_top = data_table[0].scrollTop;
    if( Math.abs( offset_top - scroll_top ) > data_table.height() || offset_top < scroll_top ) {
    	data_table.animate({scrollTop: (offset_top)}, 100);
    }
}
/**
 * 根据ID从地址点图层查找地址点
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
 * 从云平台搜索
 */
Map.searchFromCloud = function() {
	layer_cloudpois.removeAllFeatures();
	map.removeAllPopup();
	Map.endDragBranch();
	layer_edit_branch.removeAllFeatures();

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
				/*
				var size = new SuperMap.Size(37,27);
				var offset = new SuperMap.Pixel(-(size.w/2), -size.h);
				var icon = new SuperMap.Icon("assets/num/"+(i+1)+".gif", size, offset);
				var marker = new SuperMap.Marker(position, icon);
				layer_branches.addMarker( marker );
				var me = $(marker.events.element);
				item.title = "名称：" + item.name;
				me.attr({
					title: "名称：" + item.name,
					name: item.name
				});*/
				var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = {
					externalGraphic: "assets/num/" + (i+1) + ".gif",
					graphicWidth: 37,
					graphicHeight: 27,
					graphicTitle: item.name,
		        	cursor: "pointer"
				};
				poi.attributes = item;
				poi.attributes.type = "point-cloud";
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
        map.setCenter(new SuperMap.LonLat(attr.smx, attr.smy), map.getZoom() );
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

	var h =  '<div class="map-popup" style="min-height: 110px;">';
		h +=  '<div class="map-popup-content">';
		h += SuperMap.Egisp.Point.getCloudPopupHtml(attr);
		h += '</div>';

		h += '</div>';

	Map.popup = new SuperMap.Popup.FramedCloud("popup-near",
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
	$('.popup-close').unbind("click").click(function(){
		map.removeAllPopup();
	});		
}

/**
 * 添加纠错地址
 */
Map.add = function() {
	var txt = $('.text-geocode').val();
	if(txt == "" || txt.length == 0) {
		SuperMap.Egisp.showPopover("请输入地址");
		return;
	}
	if( txt.length > 50 ) {
		SuperMap.Egisp.showPopover("地址不能超过50位");
		return;
	}
	SuperMap.Egisp.Address.add(txt, 
		function() {
            SuperMap.Egisp.showPopover('添加纠错地址成功');
			$('#data_pager').attr({
				"page": 0,
				"data-total-page": 0
			});
			$('.data-list .search-input').val("");
			Map.search(true);
		},
		function(info) {
			SuperMap.Egisp.showPopover(info);
		}
	)
}

/**
 * 删除纠错地址
 */
Map.deleteAddress = function() {
	var me = $(this);
	var id = me.attr("data-id");
	SuperMap.Egisp.Address.remove(id, 
		function(){
			SuperMap.Egisp.showPopover("纠错地址\""+ me.attr("data-address") +"\"删除成功");
			$("#modal_delete_address").modal('hide');
			map.removeAllPopup();
			if( $('.data-list table tr').length === 2 ) {
				var pager = $('#data_pager');
				var currentPage = Number(pager.attr("page")) + 1;
				var totalPage = Number(pager.attr("data-total-page"));
				if(currentPage == totalPage) {
					var page = currentPage - 2;
					page = page < 0 ? 0 : page;
					pager.attr("page", page);
				}
			}
			Map.search();
		},
		function(){
			SuperMap.Egisp.showPopover("纠错地址\""+ me.attr("data-address") +"\"删除失败");
		}
	);
}

/**
 * 地址纠错-显示点
 */
Map.showEditAddress = function(marker) {
	layer_edit_branch.removeAllFeatures();
	var lonlat = marker.lonlat;

    var geometry = new SuperMap.Geometry.Point(lonlat.lon, lonlat.lat);
    var poi = new SuperMap.Feature.Vector(geometry);
    poi.style = {
        externalGraphic: "assets/map/poi-red.png",
        graphicWidth: 20,
        graphicHeight: 30,
        graphicTitle: "地址纠错",
        cursor: "pointer"
    };
    marker.x = geometry.x;
    marker.y = geometry.y;
    marker.type = "address-edit";
    poi.attributes = marker;
	layer_edit_branch.addFeatures(poi);

	Map.enableEdit();

	/*var lonlat = new SuperMap.LonLat( geometry.x, geometry.y );
	Map.openEditOrderPopup( marker );*/
}

/**
 * 手动分单-显示信息窗
 */
Map.openEditAddressPopup = function(marker) {
	var h = SuperMap.Egisp.Address.getEditPopupHtml(marker);
	Map.popup = new SuperMap.Popup.FramedCloud("popup-edit",
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
	$('.popup-close').unbind("click").click(function(){
		Map.endDragBranch();
		map.removeAllPopup();	
		Map.popup = null;		
	});
	$('.popup-sure').attr('data-id', marker.id).unbind("click").click(Map.correctAddress);
}

/**
 * 手动分单
 */
Map.correctAddress = function() {
	var id = $(this).attr('data-id');
	var lonlat = Map.popup.lonlat;
	var param = {
		id: id,
		x: lonlat.lon,
		y: lonlat.lat
	}
	SuperMap.Egisp.showMask();
	SuperMap.Egisp.Address.correct(param, 
		function() {
			SuperMap.Egisp.showPopover('操作成功');
			map.removeAllPopup();
			Map.endDragBranch();
			Map.search();
		},
		function(e) {
			var info = e.info ? e.info : "操作失败";
			SuperMap.Egisp.showPopover(info);
		}
	);
}