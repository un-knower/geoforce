$(function(){
	Map.initMap();

	$(".foot .btn-export").on("click", function(){
    	var url = "http://" + location.host + urls.server + "/statistic/order/exportOrderDetail?";
    	url += "&batch=" + $("#txt_keyword_search_branches").val() + "&resulttype=-1" 
    	+ "&end=" + new Date().format("yyyy-MM-dd hh:mm:ss");;
	    
	    window.open(url, "_blank");
    });
});

var Map = {};
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

	// layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});
	layer_branches = new SuperMap.Layer.Vector("branches");
	layer_orders = new SuperMap.Layer.Vector("orders", {renderers: ["Canvas2"]});

	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch", {renderers: ["Canvas2"]});

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
		
	map.addLayers([ layer_boundry, layer_cloudpois ]);
	Region.init();
	map.addLayers([layer_edit_branch, layer_orders, layer_branches]);
	
	layer_branches.setVisibility(false);
	
	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	
	Dituhui.SMCity.initUserCity(Map.initDatas);

	var selectStyle = Dituhui.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature([layer_orders, layer_branches, layer_region], {
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
}


/**
 * 初始化数据
 */
Map.initDatas = function() {
	Dituhui.SMCity.cityTagClick = function() {
		Map.searchBranches();
	}
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
		case "order":
			Map.selectOrder(feature);
			break;
		case "order-edit":
			Map.openEditOrderPopup(feature.attributes);
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
	map.removeAllPopup();
	Map.popup = null;
	Dituhui.Order.Table.clear();
	layer_orders.removeAllFeatures();
	Map.endDragBranch();
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
 * 查询订单
 */
Map.search = function() {		
	var keyword = $('.data-list .search-input').val();

	var pager = $('#data_pager');
	var pageIndex = Number(pager.attr('page')) + 1;
	var param = {
		pageSize: 10,
		pageNo: pageIndex
	};
	if(keyword != "") {
		param.batch = keyword;
	}

	Dituhui.showMask();
	layer_orders.removeAllFeatures();
	Dituhui.Order.search(param,
		function(data) {
			Dituhui.hideMask();
			Dituhui.Order.Table.refresh(data);
			Map.showOrdersToMap(data);
			var input = $('.data-list .search-input');
			if( input.val() == "" ) {
				input.val(data[0].batch);
			}			
			if(data.length == 0) {
				$('.foot .btn-export').addClass("hide");
			}
			else {
				$('.foot .btn-export').removeClass("hide");
			}
		},
		function(info) {
			Dituhui.hideMask();
			Dituhui.Order.Table.clear();
			Dituhui.showPopover(info);
			$('.foot .btn-export').addClass("hide");
		}
	);
}

/**
 * 订单显示至地图上
 */
Map.showOrdersToMap = function(data) {
	layer_orders.removeAllFeatures();
	var fs = [], len = data.length;
	for( var i=len; i--; ) {
		var item = data[i];
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
		var poi = new SuperMap.Feature.Vector(geo_point);
		poi.style = {
			externalGraphic: urls.server + '/resources/assets/map/poi-green.png',
			graphicWidth: 20,
			graphicHeight: 30,
			graphicTitle: item.address,
        	cursor: "pointer"
		};
		item.order = (i+1);
		poi.attributes = item;
		poi.attributes.type = "order";
		fs.push(poi);
	}
	layer_orders.addFeatures(fs);
}
/**
 * 点击订单点
 */
Map.selectOrder = function(feature) {
	var attr = feature.attributes;
	Map.openOrderAttrPopup(attr);
}
/**
 * 点击订单点，弹出信息窗
 */
Map.openOrderAttrPopup = function(attr) {
	var h = Dituhui.Order.getAttrPopupHtml(attr);
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

	attr.lonlat = lonlat;
	$('.popup-close').unbind("click").click(function(){
		Map.popup.hide();
	});
	$('.popup-edit').unbind("click").click(function(){
		Map.showEditOrder(attr);
	});

	$('#table_orders tbody tr').removeClass('action');
    var tr = $('#table_orders tbody tr[data-id="'+ attr.id +'"]').addClass('action');
    var data_table = $('.data-list .data-results');
    var offset_top = tr[0].offsetTop, scroll_top = data_table[0].scrollTop;
    if( Math.abs( offset_top - scroll_top ) > data_table.height() || offset_top < scroll_top ) {
    	data_table.animate({scrollTop: (offset_top)}, 100);
    }
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
 * 检查导入的网点文件
 */
Map.checkImportFileSize = function() {	
	var input = document.getElementById("txt_import_orders");
	var div = $('.data-import .body .import-hint.black');

	if(urls.ie_case) {
		if( input.value == "" ) {    			
	        Dituhui.showPopover( "请选择文件" );
    		div.html("");
    		return false;	
		}	
		else {
			var val = input.value;
			var arr = val.split('\\');
			val = arr[ arr.length - 1 ];

			div.html( val );
			return true;
		}
	}

	var fs = input.files;
	var div = $('.data-import .body .import-hint.black');
	if(!fs || fs.length < 1 ) {
        Dituhui.showPopover( "请选择文件" );
		div.html("");
		return false;
	}
	var file = fs[0];
	if(file.size > 2048000) {
		div.html("文件大于2M，请重新选择");
		return false;
	}
	div.html( file.name + "("+ file.size +"b)" );
	return true;
}
/**
 * 导入订单
 */
Map.importOrders = function() {
	var flag = Map.checkImportFileSize();
	if( !flag ) {
		return;
	}
	Dituhui.showMask();
	var options = {
       /* beforeSend: function(xhr) {
			xhr.setRequestHeader("Content-Type",'application/octet-stream')
		},*/
        success: function(e) { 
			Dituhui.hideMask();
	    	if( urls.ie_case ) {
	    		e = $.parseJSON($(e).text());
	    	}
	    	else {
	    		e = JSON.parse(e);
	    	}
	    	
	        if(e.isSuccess) {
	        	$('.data-import').fadeOut('fast');
	        	$('p.unclose-hint').html( e.info );
				$('#modal_showHint').modal({
					backdrop: "static",
					keyboard: false
				});			
				map.removeAllPopup();
				Map.endDragBranch();
				layer_edit_branch.removeAllFeatures();
				Dituhui.showPopover("文件上传成功");
				var data = [];
				if(e.result && e.result.records && e.result.records.length > 0) {
					data = e.result.records;
					Dituhui.Order.Table.refresh(data);
					Map.showOrdersToMap(data);

                    var html_page = Dituhui.setPage(e.result.totalCount, (e.result.page-1), '\'data_pager\''); 
                    $("#data_pager > ul").html(html_page);   
                    $("#data_pager").show();   
                    $(".page-marker").html( '第' + e.result.page + '/' + Math.ceil(e.result.totalCount/10) + '页' );
                    $('.data-list .search-input').val(data[0].batch);
				}
	        }
	        else {
	            Dituhui.showPopover(e.info ? e.info : "订单导入失败");
	        }
	    }
    };
	$("#form_import_data").ajaxSubmit(options);	    
}
/**
 * 显示分单成功的订单
 */
Map.showSuccessData = function() {
	Map.showDatas( Dituhui.Order.Table.data_success );
}
/**
 * 显示分单失败的订单
 */
Map.showFailedData = function() {
	Map.showDatas( Dituhui.Order.Table.data_failed );
}
Map.showDatas = function(data){
	map.removeAllPopup();
	layer_orders.removeAllFeatures();
	Map.endDragBranch();
	var len = data.length;
	if( len === 0 ) {			
	    $('.data-list table tbody').html('');
	    $('.data-list .totality').html('(0条)');
		return;
	}
	Map.showOrdersToMap(data);
	Dituhui.Order.Table.refresh(data);
}
/**
 * 手动分单-显示点
 */
Map.showEditOrder = function(marker) {
	layer_edit_branch.removeAllFeatures();
	var lonlat = marker.lonlat;

    var geometry = new SuperMap.Geometry.Point(lonlat.lon, lonlat.lat);
    var poi = new SuperMap.Feature.Vector(geometry);
    poi.style = {
        externalGraphic: urls.server + "/resources/assets/map/editBranch.png",
        graphicWidth: 20,
        graphicHeight: 30,
        graphicTitle: "手动分单",
        cursor: "pointer"
    };
    marker.x = geometry.x;
    marker.y = geometry.y;
    marker.type = "order-edit";
    poi.attributes = marker;
	layer_edit_branch.addFeatures(poi);

	Map.enableEdit();

	/*var lonlat = new SuperMap.LonLat( geometry.x, geometry.y );
	Map.openEditOrderPopup( marker );*/
}
/**
 * 手动分单-显示信息窗
 */
Map.openEditOrderPopup = function(marker) {
	var h = Dituhui.Order.getEditPopupHtml(marker);
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
	$('.popup-sure').attr('data-id', marker.id).unbind("click").click(Map.editOrder);
}
/**
 * 手动分单
 */
Map.editOrder = function() {
	var id = $(this).attr('data-id');
	var lonlat = Map.popup.lonlat;
	var param = {
		id: id,
		smx: lonlat.lon,
		smy: lonlat.lat
	}
	Dituhui.showMask();
	Dituhui.Order.manual(param, 
		function() {
			Dituhui.showPopover('操作成功');
			map.removeAllPopup();
			Map.endDragBranch();
			Map.search();
		},
		function(e) {
			var info = e.info ? e.info : "操作失败";
			Dituhui.showPopover(info);
		}
	);
}