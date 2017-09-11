
$(function(){
	Map.initMap();
});

var Map = {
	popup: null
};

/**
 * 地图初始化
 */
Map.initMap = function(){
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

	layer_branches = new SuperMap.Layer.Vector("branches");
	// layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});

	layer_region = new SuperMap.Layer.Vector("region", {renderers:["Canvas2"]});

	var strategy = new SuperMap.Strategy.GeoText();
	strategy.style = SuperMap.Egisp.Zone.getRegionTextStyle();
	layer_region_label = new SuperMap.Layer.Vector("Label", {renderers:["Canvas2"], strategies: [strategy]});

	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch", {renderers: ["Canvas2"]});

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});

	map.addLayers([ layer_boundry, layer_cloudpois, layer_region, layer_region_label,  layer_edit_branch, layer_branches]);

	Map.init();

	layer_branches.setVisibility(false);
	layer_region.setVisibility(false);
	layer_region_label.setVisibility(false);		

	control_drawPoint = new SuperMap.Control.DrawFeature(layer_edit_branch, SuperMap.Handler.Point, { multi: false });
	control_drawPoint.events.on({"featureadded": Map.drawPointCompleted});
	/*
    control_dragPoint = new SuperMap.Control.DragFeature(layer_edit_branch);
    control_dragPoint.geometryTypes = ['SuperMap.Geometry.Point'];
    control_dragPoint.onComplete = Map.dragPointCompelete;
    */	
	map.addControls([control_drawPoint]);
	
	var ovMap = new SuperMap.Control.OverviewMap({maximized: false});
	var layer_ovmap = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	ovMap.layers = [layer_ovmap];
	map.addControl(ovMap);
	
	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	$('.olAlphaImg').attr('title', '11');
	SuperMap.Egisp.SMCity.initUserCity(Map.initDatas);
	
	var selectStyle = SuperMap.Egisp.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature([layer_branches, layer_cloudpois, layer_region], {
	    onSelect: Map.regionSelect,
	    onUnselect: Map.regionUnSelect,
	    multiple: false,
	    hover: false
	});
	
	map.addControl(control_select);
	control_select.activate();
	
	map.events.on({"zoomend": Map.zoomEnd});

	Baidu.init();
}

/**
 * IP定位
 */
Map.ipLocate = function(){
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
 * 初始化数据
 */
Map.initDatas = function() {
	SuperMap.Egisp.SMCity.cityTagClick = function() {
		Map.searchBranches();
	}
	Map.getColumns(true);
	SuperMap.Egisp.Point.getImportBranches();
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
	layer_edit_branch.removeAllFeatures();
	Map.endDragBranch();
	SuperMap.Egisp.Point.Table.clear();
	map.removeAllPopup();

	var val = $('.data-list .search-input').val();
	var param = { pageNo: -1 };		
	if( val.length > 0 ) {
		param.name = val;
	}
	SuperMap.Egisp.Point.Search(param, 
		function(data) {	
			var level = $('.smcity').attr('level');
			if(level != '0') {
				Map.showBranchesToMap(data);
				SuperMap.Egisp.Point.Table.refresh(data);
			}
			else {
				SuperMap.Egisp.Point.National.display(data);
			}
		},
		function(error) {

		}
	);
}
Map.searchBranchesUnclear = function() {
	var val = $('.data-list .search-input').val();
	var param = { pageNo: -1 };		
	if( val.length > 0 ) {
		param.name = val;
	}
	SuperMap.Egisp.Point.Search(param, 
		function(data) {	
			var level = $('.smcity').attr('level');
			if(level != '0') {
				Map.showBranchesToMap(data);
				SuperMap.Egisp.Point.Table.refresh(data);
			}
			else {
				SuperMap.Egisp.Point.National.display(data);
			}
		},
		function(error) {

		}
	);
}
/**
 * 地图上显示网点数据
 */
Map.showBranchesToMap = function(data) {
	layer_branches.removeAllFeatures();
	var len = data ? data.length : 0;
	if( len === 0 ) {
		SuperMap.Egisp.showPopover("当前查询到0条网点数据");
		return;
	}
	var data_success = [], data_failed = [], pois = [];
	for( var i=0; i<len; i++ ) {
		var item = data[i];	

		/*SuperMap.Egisp.Point.remove(item.id, function(){}, function(){});
		continue*/

		if(item.status != 0) {
			data_failed.push(item);
			continue;
		}
		data_success.push(item);

		if( !item.iconStyle ) {
			item.iconStyle = "assets/map/red/s.png";
		}
		if(Baidu.using) {
			var coord = Baidu.getCoord(item.smx, item.smy);
			item.smx = coord.x;
			item.smy = coord.y;
		}		
		var style = SuperMap.Egisp.Point.getBranchStyle( item );
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);	
		var poi = new SuperMap.Feature.Vector(geo_point);

		poi.style = style;

		poi.attributes = item;
		poi.attributes.type = "point";
		pois.push(poi);
	}
	layer_branches.addFeatures(pois);
	SuperMap.Egisp.Point.Table.data_success = data_success.concat();
	SuperMap.Egisp.Point.Table.data_failed = data_failed.concat();
	$('.a-data-success > span').html('('+ data_success.length +')');
	$('.a-data-failed > span').html('('+ data_failed.length +')');
}
/**
 * 显示定位成功的网点
 */
Map.showSuccessData = function() {
	Map.showDatas( SuperMap.Egisp.Point.Table.data_success );
}
/**
 * 显示定位失败的网点
 */
Map.showFailedData = function() {
	Map.showDatas( SuperMap.Egisp.Point.Table.data_failed );
}
/**
 * 重构网点列表数据,单独显示定位成功或失败的网点数据
 */
Map.showDatas = function(data){
	map.removeAllPopup();
	layer_branches.removeAllFeatures();
	Map.endDragBranch();
	var len = data.length;

	if( len === 0 ) {			
	    $('.data-list table tbody').html('');
	    $('.data-list .totality').html('(0条)');
		return;		
	}
	for( var i=0; i<len; i++ ) {
		var item = data[i];
		var position = new SuperMap.LonLat(item.smx, item.smy);
		if( !item.iconStyle ) {
			item.iconStyle = "assets/map/red/s.png";
		}
		var style = SuperMap.Egisp.Point.getBranchStyle( item.iconStyle );
		
		if( !item.iconStyle ) {
			item.iconStyle = "assets/map/red/s.png";
		}
		var style = SuperMap.Egisp.Point.getBranchStyle( item.iconStyle );
		var size = new SuperMap.Size(style.width, style.height);

		if(Baidu.using) {
			var coord = Baidu.getCoord(item.smx, item.smy);
			item.smx = coord.x;
			item.smy = coord.y;
		}		
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);		
		if(Baidu.using) {
			var coord = Baidu.getCoord(item.smx, item.smy);
			geo_point = new SuperMap.Geometry.Point(coord.x, coord.y);
		}			
		var poi = new SuperMap.Feature.Vector(geo_point);
		poi.style = SuperMap.Egisp.Point.getBranchStyle( item );

		poi.attributes = item;
		poi.attributes.type = "point";
		layer_branches.addFeatures(poi);
	}
	SuperMap.Egisp.Point.Table.refresh(data);
}
/**
 * 点击网点
 */
Map.selectBranch = function(poi) {
	var marker = poi.attributes;
	marker.lonlat = new SuperMap.LonLat(marker.smx, marker.smy);
	Map.openBranchAttrPopup(marker);
}


/**
 * 点击网点，弹出属性
 */
Map.openBranchAttrPopup = function(marker) {
	var netPicPath = marker.netPicPath ? 
		urls.server +'/pointService/getImg?path=' + marker.netPicPath 
		: "assets/map/branch.png";

	var h =  '<div class="map-popup">';
		h += SuperMap.Egisp.Point.getAttrPopupHtml(marker, true);
		h += '</div>';

	var lonlat = marker.lonlat;
    /*if(Baidu && Baidu.using) {
        var coord = Baidu.restoreCoord(lonlat.lon, lonlat.lat);
        marker.lonlat.lon = coord.x;
        marker.lonlat.lat = coord.y;
    }*/
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

	SuperMap.Egisp.Point.getBindingCars( marker.id, 
        function(data){
            $('.bind-cars').html(data);
        },
        function(){}
    );

	$('.popup-close').unbind("click").click(function(){
		Map.popup.hide();
		if(layer_branches.visibility === false) {
			layer_edit_branch.removeAllFeatures();
		}
	});
	$('.popup-edit').unbind("click").click(function(){
		Map.showEditBranch(marker);
	});

	$('.popup-delete').unbind("click").click(function(){
		SuperMap.Egisp.Modal.alert('是否删除网点 <strong>'+ marker.name +'</strong> ? 删除后不可恢复。', 
			Map.deleteBranch,
			{"data-id": marker.id, "data-name": marker.name}
		);
	});

    $('.data-list table tbody tr').removeClass('action');
    var tr = $('.data-list table tbody tr[data-id="'+ marker.id +'"]').addClass('action');
    var data_table = $('.data-list .data-results');
    var offset_top = tr[0].offsetTop, scroll_top = data_table[0].scrollTop;
    if( Math.abs( offset_top - scroll_top ) > data_table.height() || offset_top < scroll_top  ) {
    	data_table.animate({scrollTop: offset_top}, 100);
    }

}
/**
 * 根据ID获取marker
 */
Map.getMarkerById = function(id) {
	var features = layer_branches.features;
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
 * 编辑网点，弹出窗口
 */
Map.showEditBranch = function(marker) {
	layer_edit_branch.removeAllFeatures();
	var lonlat = marker.lonlat;

    var geometry = new SuperMap.Geometry.Point(lonlat.lon, lonlat.lat);
    /*if(Baidu.using) {
    	var b_ll = lonlat;
		var coord = Baidu.getCoord(b_ll.lon, b_ll.lat);
		geometry = new SuperMap.Geometry.Point(coord.x, coord.y);
	}*/
    var poi = new SuperMap.Feature.Vector(geometry);
    poi.style = {
        externalGraphic: "assets/map/editBranch.png",
        graphicWidth: 20,
        graphicHeight: 30,
        graphicTitle: "修改网点",
        cursor: "pointer"
    };
    poi.attributes = {
        x: geometry.x,
        y: geometry.y,
        type: "point-edit"
    };
	layer_edit_branch.addFeatures(poi);

	Map.enableEdit();

	var lonlat = new SuperMap.LonLat( geometry.x, geometry.y );
	Map.openEditBranchPopup( marker );
}
/**
 * 显示编辑网点的弹窗
 */
Map.openEditBranchPopup = function(marker) {	
	var me = marker;

	var h =  '<div class="map-popup">';
		h +=  '<div class="map-popup-content">';
		var isDelete = marker.failed ? true : false;
		h += SuperMap.Egisp.Point.getEditPopupHeaderHtml( me.name ? me.name : "", isDelete);

		var feature_name = selectedFeature ? (selectedFeature.attributes.areaName ? selectedFeature.attributes.areaName : "") : "";
		var feature_id = selectedFeature ? (selectedFeature.attributes.areaId ? selectedFeature.attributes.areaId : "") : "";
		h += SuperMap.Egisp.Point.getAddPopupFormHtml(2, marker, feature_name, feature_id);
		h += '</div>';

		h += SuperMap.Egisp.Point.getIconsListHtml();

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

	SuperMap.Egisp.Point.getBindingCars( marker.id, 
		function(license, ids, data){
			$("#txt_car_license").val(license);
			$("#txt_car_ids").val(ids);
			Cars.selected_cars = data;
		},
		function(){
			$("#txt_car_license").val('');
			$("#txt_car_ids").val('');			
			Cars.selected_cars = [];
		}
	);

	SuperMap.Egisp.Point.setPopupDivStyle();

	$('.close-popup').unbind("click").click(function(){
		SuperMap.Egisp.Modal.ask('确定不保存吗？', Map.cancelAddBranch)
	});
	$('.popup-sure').unbind("click").click(Map.updateBranch);
	$('.popup-delete').unbind("click").click(function(){
		SuperMap.Egisp.Modal.alert('是否删除网点 <strong>'+ marker.name +'</strong> ? 删除后不可恢复。', 
			Map.deleteBranch,
			{"data-id": marker.id, "data-name": marker.name}
		);
	});
	
	$('.photo').click(function(){
		$('input[name="netPicFile"]').click();
	});
	//点击修改样式
	$('.branch-icon-popup').click(Page.initPoiUpdateStyle);
	$('.close-icons').unbind("click").click(function(){
		$('.map-popup-content').show();
		$('.map-popup-icons').hide();
	});
	$('.map-popup-icons > ul > li > img').unbind("click").click(function(){
		$('input[name="iconStyle"]').val( $(this).attr("data-src") );
		$('.map-popup-content').show();
		$('.map-popup-icons').hide();
	});
	
	$('.choose-cars').unbind('click').click(function(){
		Cars.init();
	});
}
/**
 * 更新网点
 */
Map.updateBranch = function() {
	var name = $('#txt_name').val();
	if(name.length < 1) {
		SuperMap.Egisp.showHint("请输入网点名称");
		return;
	}
	var address = $('#txt_address').val();
	if(address.length < 1) {
		SuperMap.Egisp.showHint("请输入网点地址");
		return;
	}
    var fileSizeOK = checkFileSize();
    if( fileSizeOK == false ) {
        $('.hint').html("图片大于100K，请重新选择");
        return;
    }

    var name = '', groupid = $('input[name="groupid"]').val();
    if($('.icon-edit-container').is(':visible') && $('.icon-save-container').is(':hidden') ) {
    	name = $('#txt_styleName').val();
    	var demo = $('.icon-demo');
	    if(!demo.attr('data-use-custom-ico')) {
			var icon = {
				appearance: demo.attr('data-back'),
				apppic: demo.attr('data-ico')
			};			
			if( (icon.appearance == 'transparent' || icon.appearance == '' ) && 
				(icon.apppic == '' || icon.apppic == 'none' ) 
			) {
				SuperMap.Egisp.showHint('外观和图案不允许都为无');
				return;
			}
		}
    	//无分组时直接保存子集
    	if( groupid == '' || groupid == 'none' ) {
    		$('input[name="stylename"]').val(name);
    		Map.updateBranchServer(); 
    	}
    	else {
    		if(name == '') {    			
		    	SuperMap.Egisp.showHint('请输入样式名称');
		    	return;
    		}
			$('input[name="stylename"]').val(name);

			SuperMap.Egisp.Modal.alert('将更新所属分组为“'+ $('#txt_branchGroup option:selected').text() +'”的图标样式，确定继续？',
				Map.updateBranchServer
			);
    	}
    	return;
    }
    // $('input[name="stylename"]').val(name);
    Map.updateBranchServer();    
}
Map.updateBranchServer = function(){
	$("#form_add_branch").ajaxSubmit(function(e) { 
    	if( urls.ie_case ) {
    		e = $.parseJSON($(e).text());
    	}
    	else {
    		e = JSON.parse(e);
    	}
    	// e = $.parseJSON($(e).text());
        if(e.isSuccess) {
			SuperMap.Egisp.showPopover("修改成功");
			map.removeAllPopup();

			Map.endDragBranch();

			Map.searchBranches();

			if( $('.data-list').attr('status') != 'min' && $('.icon-edit-container').is(':visible') && $('.icon-save-container').is(':hidden') ) {
				$('.icon-save-container, .tab-icon-list').show();
				$('.tab-edit').hide();
				$('.data-list .head .tab-text').click();
			}	
        }
        else {
            SuperMap.Egisp.showPopover(e.info);
        }
        Map.Style.search();
    });
    SuperMap.Egisp.Modal.hide();
}
/**
 * 删除网点
 */
Map.deleteBranch = function() {
	var me = $(this);
	var id = me.attr("data-id");
	SuperMap.Egisp.Point.remove(id, 
		function(){
			SuperMap.Egisp.showPopover("网点\""+ me.attr("data-name") +"\"删除成功");
			$(".data-modal").hide();
			map.removeAllPopup();
			Map.searchBranches();
		},
		function(){
			SuperMap.Egisp.showPopover("网点\""+ me.attr("data-name") +"\"删除失败");
		}
	)
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
		var zoom = map.getZoom(), zoom_min = Baidu.using ? 3 : 5;
		if(zoom < zoom_min) {
			SuperMap.Egisp.showHint("请将地图放大到"+ zoom_min +"级以上再显示区划");
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

        if(Baidu && Baidu.using) {
        	for(var k=point2Ds.length; k--; ) {
        		var item = point2Ds[k];
        		var coord = Baidu.getCoord(item.x, item.y);
        		point2Ds[k].x = coord.x;
        		point2Ds[k].y = coord.y;
        	}
            var coord = Baidu.getCoord(record.center.x, record.center.y);
            record.center.x = coord.x;
            record.center.y = coord.y;
        }
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
 * 选择区划
 */
Map.regionSelect = function(feature) {
	var type = feature.attributes.type;
	switch(type) {
		case 'point':
			if( layer_branches.visibility ) {
				layer_edit_branch.removeAllFeatures();
	        	Map.endDragBranch();				
			}
			Map.selectBranch(feature);
			break;
		case 'region': 
			selectedFeature = feature;
			$('#txt_areaName').val(feature.attributes.name);
			$('#txt_areaId').val(feature.attributes.id);		
			feature.style = SuperMap.Egisp.Zone.getRegionSelectStyle( feature.attributes.name );	
			layer_region.redraw();
			break;
		case 'point-cloud':
			Map.openCloudPopup(feature.attributes);
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
	layer_edit_branch.removeAllFeatures();
	layer_cloudpois.removeAllFeatures();
	$('.cloud-pois').hide();
	control_drawPoint.deactivate();
	map.removeAllPopup();
	Map.popup = null;
	Map.endDragBranch();

	SuperMap.Egisp.Point.Table.clear();
}
/**
 * 平移
 */
Map.pan = function() {
	control_drawPoint.deactivate();
}
/**
 * 标点
 */
Map.drawPoint = function() {
	var input = $('#btn_showBranch');
	if(!input.prop('checked')) {
		input.prop('checked', true);
		layer_branches.setVisibility(true);
		if( layer_branches.features.length === 0) {
			Map.searchBranches();
		}			
	}

	control_drawPoint.activate();
}
/**
 * 标点完成，地图添加新网点marker
 */
Map.drawPointCompleted = function(e) {
	control_drawPoint.deactivate();		
	layer_edit_branch.removeAllFeatures();

    var geometry = e.feature.geometry;
    var geo_point = new SuperMap.Geometry.Point(geometry.x, geometry.y);
    var poi = new SuperMap.Feature.Vector( geo_point );
    poi.id = "add_branch";

    var obj = {name: '新增网点'};
    poi.style = SuperMap.Egisp.Point.getBranchStyle(obj);
    poi.attributes = {
        x: geometry.x,
        y: geometry.y,
        type: 1
    };
	layer_edit_branch.addFeatures(poi);

	Map.enableEdit();

	var lonlat = new SuperMap.LonLat( geometry.x, geometry.y );

	Map.openAddBranchPopup( lonlat );
    Map.regeocode(lonlat);
}
/**
 * 标点完成，弹出新增网点窗口
 */
Map.openAddBranchPopup = function(lonlat, type, attr) {
	var h =  '<div class="map-popup">';
		h +=  '<div class="map-popup-content">';
	if( type && type === 3 ) {
		h += SuperMap.Egisp.Point.getEditPopupHeaderHtml(attr.name);
	}
	else {
		h += SuperMap.Egisp.Point.getAddPopupHeaderHtml();
	}
	
	var b_coord = lonlat;	
    if(Baidu && Baidu.using) {
    	var b_ll = lonlat;
        var coord = Baidu.restoreCoord(b_ll.lon, b_ll.lat);
        b_coord = new SuperMap.LonLat(coord.x, coord.y);
    }

	var feature_name = selectedFeature ? (selectedFeature.attributes.name ? selectedFeature.attributes.name : "") : "";
	var feature_id = selectedFeature ? (selectedFeature.attributes.id ? selectedFeature.attributes.id : "") : "";
	var kind = type ? type : 1;
	h += SuperMap.Egisp.Point.getAddPopupFormHtml(kind, b_coord, feature_name, feature_id, attr);
	h += '</div>';

	h += SuperMap.Egisp.Point.getIconsListHtml();

	h += '</div>';

	Map.popup = new SuperMap.Popup.FramedCloud("popup-near",
		lonlat,
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

	SuperMap.Egisp.Point.setPopupDivStyle();

	$('.close-popup').unbind("click").click(function(){
		SuperMap.Egisp.Modal.ask( '确定不保存吗？',  Map.cancelAddBranch);
	});
	$('.popup-sure').unbind("click").click(Map.addBranch);
	$('.photo').click(function(){
		$('input[name="netPicFile"]').click();
	});
	$('.branch-icon-popup').click(Page.initPoiUpdateStyle);
	$('.close-icons').unbind("click").click(function(){
		$('.map-popup-content').show();
		$('.map-popup-icons').hide();
	});
	$('.map-popup-icons > ul > li > img').unbind("click").click(function(){
		$('input[name="iconStyle"]').val( $(this).attr("data-src") );
		$('.map-popup-content').show();
		$('.map-popup-icons').hide();
	});
	$('.choose-cars').unbind('click').click(function(){
		Cars.init();
	});
}
/**
 * 取消新增网点操作
 */
Map.cancelAddBranch = function() {
	map.removeAllPopup();
	layer_edit_branch.removeAllFeatures();
	Map.endDragBranch();
	$('.data-modal').hide();
	if( $('.data-list').attr('status') != 'min' && $('.icon-edit-container').is(':visible') && $('.icon-save-container').is(':hidden') ) {
		$('.icon-save-container, .tab-icon-list').show();
		$('.tab-edit').hide();
		$('.data-list .head .tab-text').click();
	}
}
/**
 * 添加网点
 */
Map.addBranch = function() {
	var name = $('#txt_name').val();
	if(name.length < 1) {
		SuperMap.Egisp.showHint("请输入网点名称");
		return;
	}
	// name = SuperMap.Egisp.setStringEsc(name);
	var address = $('#txt_address').val();
	if(address.length < 1) {
		SuperMap.Egisp.showHint("请输入网点地址");
		return;
	}
    var fileSizeOK = checkFileSize();
    if( fileSizeOK == false ) {
        $('.hint').html("图片大于100K，请重新选择");
        return;
    }    

    var name = '', groupid = $('input[name="groupid"]').val();
    if($('.icon-edit-container').is(':visible') && $('.icon-save-container').is(':hidden') ) {
    	name = $('#txt_styleName').val();    	
    	var demo = $('.icon-demo');
    	if(!demo.attr('data-use-custom-ico')) {
			var icon = {
				appearance: demo.attr('data-back'),
				apppic: demo.attr('data-ico')
			};			
			if( (icon.appearance == 'transparent' || icon.appearance == '' ) && 
				(icon.apppic == '' || icon.apppic == 'none' ) 
			) {
				SuperMap.Egisp.showHint('外观和图案不允许都为无');
				return;
			}
		}
    	//无分组时直接保存子集
    	if( groupid == '' || groupid == 'none' ) {
    		$('input[name="stylename"]').val(name);
    		Map.addBranchServer(); 
    	}
    	else {    		
    		if(name == '') {    			
		    	SuperMap.Egisp.showHint('请输入样式名称');
		    	return;
    		}    		
			$('input[name="stylename"]').val(name);

			SuperMap.Egisp.Modal.alert('将更新所属分组为“'+ $('#txt_branchGroup option:selected').text() +'”的图标样式，确定继续？',
				Map.addBranchServer
			);
    	}
    	return;
    }
    Map.addBranchServer();
}
Map.addBranchServer = function() {	
    $("#form_add_branch").ajaxSubmit(function(e) { 
    	if( urls.ie_case ) {
    		e = $.parseJSON($(e).text());
    	}
    	else {
    		e = JSON.parse(e);
    	}
        if(e.isSuccess) {
			SuperMap.Egisp.showPopover("添加成功");
			map.removeAllPopup();
			Map.endDragBranch();

			Map.searchBranches();
			if( $('.data-list').attr('status') != 'min' && $('.icon-edit-container').is(':visible') && $('.icon-save-container').is(':hidden') ) {
				$('.icon-save-container, .tab-icon-list').show();
				$('.tab-edit').hide();
				$('.data-list .head .tab-text').click();
			}
        }
        else {
            SuperMap.Egisp.showPopover(e.info);
        }
        Map.Style.search();
    });
    SuperMap.Egisp.Modal.hide();
}
/**
 * 反向地址匹配
 */
Map.regeocode = function(lonlat, isClearInput) {
	var input = $("#txt_address");
	var oldAddress = input.val();
	input.attr("disabled", "disabled").val("正在进行反向地址匹配...");

    if(Baidu && Baidu.using) {
        var coord = Baidu.restoreCoord(lonlat.lon, lonlat.lat);
        lonlat = new SuperMap.LonLat(coord.x, coord.y);
    }
	var param = {
		smx: lonlat.lon,
		smy: lonlat.lat
	}
	SuperMap.Egisp.Point.regeocode( param, 
		function(data) {
			if(!data.address) {
				data.address = oldAddress;
			}
			if(typeof isClearInput != 'undefined' && oldAddress != '') {
				data.address = oldAddress;
			}
			input.val(data.address).removeAttr('disabled');
			

			var level = $('.smcity').attr('level');
			//全国范围下自动放大
			if(level == '0' && data.admincode && data.admincode.length > 0) {
				if(data.county && data.county.length != 0 ) {
					SuperMap.Egisp.SMCity.showCurrentCounty(data.county, data.admincode, data.city);
					Map.searchBranchesUnclear();
				}
				else if(data.city && data.city.length != 0) {
					SuperMap.Egisp.SMCity.showCurrentCity(data.city, data.admincode);
					Map.searchBranchesUnclear();
				}
				else if(data.province && data.province.length != 0) {
					SuperMap.Egisp.SMCity.showCurrentProvince(data.province, data.admincode);		
					Map.searchBranchesUnclear();			
				}
			}
		},
		function() {
			input.val(oldAddress).removeAttr('disabled');
		}
	)
}
/**
 * 检查导入的网点文件
 */
Map.checkImportFileSize = function() {	
	var input = document.getElementById("txt_import_branches");
	var div = $('.data-import .body .import-hint.black');

	if(urls.ie_case) {
		if( input.value == "" ) {    			
	        SuperMap.Egisp.showHint( "请选择文件" );
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
        SuperMap.Egisp.showHint( "请选择文件" );
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
Map.setWindowClose = function(){
	window.onbeforeunload = function () {		
        return '您还有数据未导入完，是否确定离开？';
    } 
}
Map.resetWindowClose = function(){
	window.onbeforeunload = function () {		
        
    } 
}
/**
 * 导入网点
 */
Map.importBranches = function() {
	var flag = Map.checkImportFileSize();
	if( !flag ) {
		return;
	}
	Map.setWindowClose();
	SuperMap.Egisp.Modal.loading('正在导入网点，请稍候...');
	$('a.btn-select-file').removeClass('btn-select-file').addClass('btn-during-import');
    $("#form_import_branches").ajaxSubmit({
    	timeout: 90000,
    	error: function(e) {
    		Map.resetWindowClose();
    		SuperMap.Egisp.Modal.hide();
    		SuperMap.Egisp.showHint('导入失败');
			$('a.btn-during-import').removeClass('btn-during-import').addClass('btn-select-file');
    	},
    	success: function(e) { 
	    	// SuperMap.Egisp.hideMask();
			$('a.btn-during-import').removeClass('btn-during-import').addClass('btn-select-file');
	    	if( urls.ie_case ) {
	    		e = $.parseJSON($(e).text());
	    	}
	    	else {
	    		e = JSON.parse(e);
	    	}	    	
	        if(e.isSuccess) {
	        	$('.data-import').fadeOut('fast');
	        	SuperMap.Egisp.Modal.loading( e.info ? e.info + '</br>正在解析数据...<br>' : '正在解析数据...<br>' );
	        	Map.analysisPoints(e.info);
	        }
	        else {
	        	Map.resetWindowClose();
	            SuperMap.Egisp.Modal.loaded_wrong('数据导入失败<br>' + e.info, Map.afterImportWrong);
	        }
    	}
    });	    
}
Map.analysisPoints = function(importInfo){
	SuperMap.Egisp.Point.analysisPoints(true, function(){
		Map.resetWindowClose();
		SuperMap.Egisp.Modal.loaded_right('数据已导入完成。<br>' + importInfo, Map.afterImportRight); 
	}, function(r){
		Map.resetWindowClose();
		if(!r) {
			r = importInfo;
		}
		SuperMap.Egisp.Modal.loaded_wrong('数据导入失败<br>' + r, Map.afterImportWrong);
	});
}
Map.afterImportRight = function() {
	map.removeAllPopup();
	Map.endDragBranch();
	layer_edit_branch.removeAllFeatures();
	Map.getColumns(false);								
	Map.Group.search();
	$('a[option="showWholeCountry"]').click();
	SuperMap.Egisp.Modal.hide();
	var btn_showBranch = $('#btn_showBranch');
	if(btn_showBranch.prop('checked') == false) {
		btn_showBranch.click();
	}
}
Map.afterImportWrong = function() {
	SuperMap.Egisp.Modal.hide();
}
/**
 * 获取网点自定义字段
 */
Map.getColumns = function(first) {
	SuperMap.Egisp.Point.createSysThead();
	SuperMap.Egisp.Point.getCustomColumns(first);
}
/**
 * 更新单元格数据
 */
Map.updateCell = function() {
	SuperMap.Egisp.Point.Table.updateCell();
}
/**
 * 更新单元格数据
 */
Map.updateCellGroup = function() {
	SuperMap.Egisp.Point.Table.updateCellGroup();
}
/**
 * 新增列
 */
Map.addColumn = function() {
	SuperMap.Egisp.Point.Table.addColumn();
}
/**
 * 删除列
 */
Map.removeColumn = function() {
	SuperMap.Egisp.Point.Table.removeColumn();
}
/**
 * 重命名表头列名称
 */
Map.renameColumn = function() {
	SuperMap.Egisp.Point.Table.renameColumn();
}

/**
 * 地图缩放完成
 */
Map.zoomEnd = function(e) {
	var zoom = e.object.zoom;
	$('.olAlphaImg').attr('title', zoom);

	var btn = $('#btn_showRegion');
	var isShowRegion = btn.attr("isShowRegion") ? true : false;

	var zoom_sep = 4;
	if(Baidu.using) {	
		zoom_sep = 2;
	}
	if( zoom > zoom_sep ) {
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
				if(Baidu.using) {
					var coord = Baidu.getCoord(item.smx, item.smy);
					item.smx = coord.x;
					item.smy = coord.y;
				}	
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
		h += SuperMap.Egisp.Point.getCloudPopupHtml(attr, true);
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
	var zoom = map.getZoom();
	map.setCenter(position, zoom);
	$('.popup-close').unbind("click").click(function(){
		map.removeAllPopup();
	});		
	$('a.a-save-as-branch').unbind('click').click(function(){
		Map.openAddBranchPopup( attr.lonlat,3,attr);

		if(attr.address == '' || $('.smcity').attr('level') == '0') {
			Map.regeocode(attr.lonlat, true);
		}
	});
}


Map.checkMapAction = function() {
    if(control_drawPoint.active) {
    	SuperMap.Egisp.showHint();
    }
}