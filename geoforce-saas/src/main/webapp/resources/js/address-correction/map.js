$(function(){
	Map.initMap();
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

	layer_branches = new SuperMap.Layer.Vector("branches");
	// layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});
	layer_orders = new SuperMap.Layer.Vector("orders", {renderers: ["Canvas2"]});

	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch", {renderers: ["Canvas2"]});

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	
	
	layer_branches.setVisibility(false);
	
	map.addLayers([ layer_boundry, layer_cloudpois ]);
	Region.init();
	map.addLayers([layer_edit_branch, layer_orders, layer_branches]);
	

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
	map.removeAllPopup();
	Dituhui.Address.Table.clear();
	layer_orders.removeAllFeatures();
	layer_edit_branch.removeAllFeatures();
	Map.endDragBranch();
	Map.popup = null;
	layer_cloudpois.removeAllFeatures();
    $('.header .search-from-cloud .child').addClass("hide");
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
var currStatus=$("#status_option").val();
	if(currStatus=="已纠错"){
		currStatus=1;
	}
	else if (currStatus=="未纠错"){
		currStatus=0;
	}
	else{
		currStatus=2;
	}

$("#status_option").on('change',function(){
	currStatus=$(this).val();
	if(currStatus=="已纠错"){
		currStatus=1;
	}
	else if (currStatus=="未纠错"){
		currStatus=0;
	}
	else{
		currStatus=2;
	}

	Map.search();
})

var currNum=$("#numCurr").val();
$("#numCurr").on('change',function(){
	currNum=$(this).val();
	Map.search();

})


Map.search = function(isShowFirstAddress) {
	var pager = $('#data_pager');
	var pageIndex = Number(pager.attr('page')) + 1;
	var param = {
		pageSize: currNum,
		pageNo: pageIndex,
		status:currStatus
	};
	var keyword = $('.data-list .search-input').val();
	if(keyword != "") {
		param.keyword = keyword;
	}
	Dituhui.showMask();
	layer_orders.removeAllFeatures();
	Dituhui.Address.search(param,
		function(data) {
			Dituhui.hideMask();
			Dituhui.Address.Table.refresh(data);
			Map.showAddressToMap(data, isShowFirstAddress);
		},

		function(info) {
			Dituhui.hideMask();
			Dituhui.Address.Table.clear();
			Dituhui.showPopover(info);
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
				Dituhui.showHint("请拖动地址(红色标注)进行纠错");
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
	var h = Dituhui.Address.getAttrPopupHtml(attr);
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
		$(this).removeClass("popup-edit").addClass('popup-sure');
		$("#lonText").hide();
		$("#latText").hide();
		$('#pointLon').show();
		$('#pointLat').show();
		$(this).unbind("click").click(function(){
			if(!pointLon||!pointLat){
				var str='请补充经纬度进行纠错';
				Dituhui.Modal.alert(str,0,0);
				return;
			}
			else{
				Map.opencorrectAddressSure(attr,pointLon,pointLat);
			}
		})
		var pointLon=$('#pointLon').val();
		var pointLat=$('#pointLat').val()
		$('#pointLon').on("change",function(){
			pointLon=$(this).val();
		});
		$('#pointLat').on("change",function(){
			pointLat=$(this).val();
		});
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
 * 添加纠错地址
 */
Map.add = function() {
	var txt = $('.text-geocode').val();
	if(txt == "" || txt.length == 0) {
		Dituhui.showPopover("请输入地址");
		return;
	}
	if( txt.length > 50 ) {
		Dituhui.showPopover("地址不能超过50位");
		return;
	}
	Dituhui.Address.add(txt, 
		function() {
            Dituhui.showPopover('添加纠错地址成功');
			$('#data_pager').attr({
				"page": 0,
				"data-total-page": 0
			});
			$('.data-list .search-input').val("");
			Map.search(true);
		},
		function(info) {
			Dituhui.showPopover(info);
		}
	)
}

/**
 * 删除纠错地址
 */
Map.deleteAddress = function() {
	var me = $(this);
	var id = me.attr("data-id");
	Dituhui.Address.remove(id, 
		function(){
			Dituhui.showPopover("纠错地址\""+ me.attr("data-address") +"\"删除成功");
			Dituhui.Modal.hide();
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
			Dituhui.showHint("纠错地址\""+ me.attr("data-address") +"\"删除失败");
		}
	);
}

/**
 * 地址纠错-显示点
 */
Map.showEditAddress = function(marker) {
	layer_edit_branch.removeAllFeatures();
	var lonlat='';
		lonlat = marker.lonlat;

	// if(marker.lonlat){
	// 	lonlat = marker.lonlat;
	// }
	// else{
	// 	lonlat = {
	// 		lon:marker.x,
	// 		lat:marker.y
	// 	};
	// }
    var geometry = new SuperMap.Geometry.Point(lonlat.lon, lonlat.lat);
    var poi = new SuperMap.Feature.Vector(geometry);
    poi.style = {
        externalGraphic: urls.server + "/resources/assets/map/poi-red.png",
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
	// Map.openEditAddressPopuphand(marker);
	// var lonlat = new SuperMap.LonLat( geometry.x, geometry.y );
	// Map.openEditOrderPopup( marker );
}

/**
 * 手动分单-显示信息窗
 */
Map.openEditAddressPopuphand = function(marker) {
	var h = Dituhui.Address.getEditPopupHtml(marker);
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
	$('.popup-sure').attr('data-id', marker.id).unbind("click")
			.click(function(){
				Map.opencorrectAddress(marker,marker.lonlat.lon,marker.lonlat.lat);
			});
}



Map.openEditAddressPopup = function(marker,nul) {
	var h = Dituhui.Address.getAttrPopupHtml(marker);
	// var h = Dituhui.Address.openCorrectionAddressPopup(marker);

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
	$('.popup-edit').unbind("click").click(function(){
		$(this).removeClass("popup-edit").addClass('popup-sure');	
		$("#lonText").hide();
		$("#latText").hide();
		$('#pointLon').show();
		$('#pointLat').show();		
		$(this).unbind("click").click(function(){
			if(!pointLon||!pointLat){
				var str='请补充经纬度进行纠错';
				Dituhui.Modal.alert(str,0,0);
				return;
			}
			else{
				Map.opencorrectAddressSure(marker,pointLon,pointLat);

			}

		})

	})
	var pointLon=$('#pointLon').val();
	var pointLat=$('#pointLat').val()
	$('#pointLon').on("change",function(){
		pointLon=$(this).val();
	});
	$('#pointLat').on("change",function(){
		pointLat=$(this).val();
	});

}

// 移动点-----数据更新弹框
Map.openUpdataAddressPopup = function(marker,nul) {
    if(pointLon=='支持高德经度'||pointLat=='支持高德纬度'){
        pointLon=marker.lonlat.lon;
        pointLat=marker.lonlat.lat;
    }
	var h = Dituhui.Address.getUpdataPopupHtml(marker);
	// var h = Dituhui.Address.openCorrectionAddressPopup(marker);

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
	$('#pointLon').show();
	$('#pointLat').show();
	var pointLon=$('#pointLon').val();
	var pointLat=$('#pointLat').val();
	$('.popup-edit').removeClass("popup-edit").addClass('popup-sure')
		.unbind("click").click(function(){
		if(!pointLon||!pointLat){
			var str='请补充经纬度进行纠错';
			Dituhui.Modal.alert(str,0,0);
			return;
		}
		else if(pointLon=='支持高德经度'||pointLat=='支持高德纬度'){
			var str='请补充经纬度进行纠错';
			Dituhui.Modal.alert(str,0,0);
			return;			
		}
		else{			
			Map.opencorrectAddressSure(marker,pointLon,pointLat);

		}
	})
	

	$('#pointLon').on("change",function(){
		pointLon=$(this).val();
	});
	$('#pointLat').on("change",function(){
		pointLat=$(this).val();
	});

}


Map.opencorrectAddress=function(marker,pointLon,pointLat){
    // var h =  '<div class="map-popup" style="min-height: 100px;">';
    //     h += '  <div class="popLeft"></div>';
    //     h += '  <div class="popRight">';
    //     h += '      <div class="title_tit">';
    //     h += '          <p>提示</p><p>纠错后的地址将在分单时被调用，确定对此条地址进行纠错码？</p>';
    //     h += '      </div>';
    //     h += '      <div class="content">';
    //     h += '          <div class="contentInput">'
    //     h += '              <input id="deleteCorrection" value="取消" type="submit">';
    //     h += '              <input id="SureCorrection" value="确定" type="submit">';
    //     h += '          </div>'      
    //     h += '      </div>';
    //     h += '  </div>';
    //     h += '</div>';

        Dituhui.Modal.ask('纠错后的地址将在分单时被调用，确定对此条地址进行纠错吗？', 
			function(){
				Map.correctAddress(marker,pointLon,pointLat);
			},
			{'data-id': marker.id},
			function(){
				Map.deleteCorrection();
			}

		);

	// console.log(Dituhui.Address.getEditPopupHtmlSure(marker));
	// var h = Dituhui.Address.getEditPopupHtmlSure(marker);
	// Map.popup = new SuperMap.Popup.FramedCloud("popup-correction",
	// 	marker.lonlat,
	// 	new SuperMap.Size(300, 70),
	// 	h,
	// 	null,
	// 	false,
	// 	null
	// );

	// Map.popup.autoSize = true;
	// Map.popup.panMapIfOutOfView = true;
	// Map.popup.relativePosition = "tr";
	// map.removeAllPopup();	
	// map.addPopup(Map.popup);
	// $('#deleteCorrection').unbind("click").click(function(){
	// 	Map.endDragBranch();
	// 	map.removeAllPopup();	
	// 	Map.popup = null;		
	// });
	// $('#SureCorrection').attr('data-id', marker.id)
	// 				.unbind("click").click(function(){
	// 					Map.correctAddress(marker,pointLon,pointLat);

	// 				});

}
Map.opencorrectAddressSure=function(marker,pointLon,pointLat){
    // var h =  '<div class="map-popup" style="min-height: 100px;">';
    //     h += '  <div class="popLeft"></div>';
    //     h += '  <div class="popRight">';
    //     h += '      <div class="title_tit">';
    //     h += '          <p>提示</p><p>纠错后的地址将在分单时被调用，确定对此条地址进行纠错码？</p>';
    //     h += '      </div>';
    //     h += '      <div class="content">';
    //     h += '          <div class="contentInput">'
    //     h += '              <input id="deleteCorrection" value="取消" type="submit">';
    //     h += '              <input id="SureCorrection" value="确定" type="submit">';
    //     h += '          </div>'      
    //     h += '      </div>';
    //     h += '  </div>';
    //     h += '</div>';


        Dituhui.Modal.ask('纠错后的地址将在分单时被调用，确定对此条地址进行纠错吗？', 
			function(){
				Map.correctAddressSure(marker,pointLon,pointLat);
			},
			{'data-id': marker.id},
			function(){Map.deleteCorrection()}

		);

	// // console.log(Dituhui.Address.getEditPopupHtmlSure(marker));
	// // var h = Dituhui.Address.getEditPopupHtmlSure(marker);
	// Map.popup = new SuperMap.Popup.FramedCloud("popup-correction",
	// 	marker.lonlat,
	// 	new SuperMap.Size(300, 70),
	// 	h,
	// 	null,
	// 	false,
	// 	null
	// );

	// Map.popup.autoSize = true;
	// Map.popup.panMapIfOutOfView = true;
	// Map.popup.relativePosition = "tr";
	// map.removeAllPopup();	
	// map.addPopup(Map.popup);
	// $('#deleteCorrection').unbind("click").click(function(){
	// 	Map.endDragBranch();
	// 	map.removeAllPopup();	
	// 	Map.popup = null;		
	// });
	// $('#SureCorrection').attr('data-id', marker.id)
	// 				.unbind("click").click(function(){
	// 					Map.correctAddressSure(marker,pointLon,pointLat);

	// 				});

}
Map.deleteCorrection=function(){
		Map.endDragBranch();
		map.removeAllPopup();	
		Map.popup = null;		
}


/**
 * 手动分单
 */
 // 保存值----移动点时
Map.correctAddress = function(marker,pointLon,pointLat) {
	var id = $(this).attr('data-id');
	var param = {
		id:marker.id,
		x: pointLon,
		y: pointLat
	}

	Dituhui.showMask();
	Dituhui.Address.correct(param, 
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


// 保存值-----输入经纬度时
Map.correctAddressSure = function(marker,pointLon,pointLat) {
	// var id = $(this).attr('data-id');
	var lonlat=Dituhui.latLonToMeters( new SuperMap.LonLat(Number(pointLon), Number(pointLat)) )
	var param = {
		id:marker.id,
		x: lonlat.lon,
		y: lonlat.lat
	}
	var param2 = {
		id:marker.id,
		x: lonlat.lon,
		y: lonlat.lat,
		address:marker.address
	}
	// Dituhui.showMask();
	Dituhui.Address.correct(param, 
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





