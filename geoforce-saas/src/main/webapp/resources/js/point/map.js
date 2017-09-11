
$(function(){
	Map.initMap();
});

var Map = {
	popup: null,
	pop_labels: []
};

var layer_point_label = null;
var layer_point_cluster = null;

/**
 * 地图初始化
 */
Map.initMap = function(){
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

	layer_branches = new SuperMap.Layer.Vector("branches");
	// layer_branches = new SuperMap.Layer.Vector("branches", {renderers: ["Canvas2"]});

	layer_point_label = new SuperMap.Layer.Vector("pointLabel");

	// layer_edit_branch = new SuperMap.Layer.Vector("edit_branch", {renderers: ["Canvas2"]});
	layer_edit_branch = new SuperMap.Layer.Vector("edit_branch");

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	layer_point_cluster = new SuperMap.Layer.ClusterLayer("pointCluster", 
		{clusterStyles: Dituhui.Point.Cluster.getStyle()}
	);
	map.addLayers([ layer_boundry, layer_cloudpois ]);
	Region.init();
	map.addLayers([layer_edit_branch, layer_point_label, layer_branches]);

	layer_branches.setVisibility(false);		
	layer_point_label.setVisibility(false);	
	
	control_drawPoint = new SuperMap.Control.DrawFeature(
		layer_edit_branch, 
		SuperMap.Handler.Point, 
		{ multi: false }
	);
	control_drawPoint.events.on({"featureadded": Map.drawPointCompleted});
	/*
    control_dragPoint = new SuperMap.Control.DragFeature(layer_edit_branch);
    control_dragPoint.geometryTypes = ['SuperMap.Geometry.Point'];
    control_dragPoint.onComplete = Map.dragPointCompelete;
    */
	map.addControls([control_drawPoint]);
	
	var ovMap = new SuperMap.Control.OverviewMap({id:"overviewMap", maximized: false});
	var layer_ovmap = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	ovMap.layers = [layer_ovmap];
	map.addControl(ovMap);
	
	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	
	Dituhui.SMCity.initUserCity(Map.initDatas);
	
	var selectStyle = Dituhui.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature([layer_branches, layer_cloudpois, layer_region], {
	    onSelect: Map.regionSelect,
	    onUnselect: Map.regionUnSelect,
	    multiple: false,
	    hover: false
	});
	
	/*var select_cluster = new SuperMap.Control.SelectCluster(layer_point_cluster,{
		callbacks:{
			click:function(f){ 
				if(!f.isCluster){ 
					console.log(f)
				}
				console.log(f)
			},
			clickout:function(){ 
				
			}
		}
	});*/
	
	map.addControls([control_select]);
	control_select.activate();
//	select_cluster.activate();
	
	map.events.on({"zoomend": Map.zoomEnd});

	Map.init(true);
	Baidu.init();
}

/**
 * IP定位
 */
Map.ipLocate = function(){
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
 * 初始化数据
 */
Map.initDatas = function() {
	Dituhui.SMCity.cityTagClick = function(cluster) {
		$('.search-area .select-group').val('-1');
		$('#txt_keyword_search_branches').val('');
		var isSearchRegion = $('#btn_showRegion').prop("checked");
		if(cluster === false) {
			Map.searchBranches(false, isSearchRegion?Map.searchRegions:false);
		}
		else {
			Map.searchBranches(null, isSearchRegion?Map.searchRegions:false);
		}
		
		var sc = $(".smcity");
		poisearch.city = sc.attr("admincode");
		if(sc.attr("level") == 2) {
			poisearch.city = sc.attr("admincode").substr(0, 4) + "00";
		}
		poisearch.changeCity();
	}
	Map.getColumns(true);
	Dituhui.Point.getImportBranches();
	
	poisearch = new POISearch({
		id: "txt_keyword_cloud",
		city: $(".smcity").attr("admincode"),
		map: map,
		layer: layer_cloudpois,
		savePOI: true
	});
}

/**
 * 显示网点图层
 */
Map.showBranchLayer = function() {
	var me = $(this);
	var bool = me.prop('checked');

	if( bool && layer_branches.features.length === 0 ) {
		Map.searchBranches();
	}
	else {
		if(!Map.isDragging) {
			map.removeAllPopup();
		}
		if( !bool && $('#show-point-label').prop('checked') == true ) {
			$('#show-point-label').prop('checked', false);
			layer_point_label.setVisibility(false);
		}
	}
	layer_branches.setVisibility(bool);
}

/**
 * 查询网点
 */
Map.searchBranches = function(cluster, callback) {
	layer_branches.removeAllFeatures();
	layer_point_cluster.destroyCluster();
	layer_edit_branch.removeAllFeatures();
	layer_point_label.removeAllFeatures();
	Map.endDragBranch();
	Dituhui.Point.Table.clear();
	map.removeAllPopup();

	var val = $('.data-list .search-input').val();
	var groupid = $('.data-list .select-group').val();
	var isCluster = $('#btn_showCluster').prop('checked');
	if(cluster === false || val.length > 0 || groupid != '-1') {
		isCluster = false;
	}
	
	var param = { pageNo: -1 };		
	if( val.length > 0 ) {
		param.name = val;
	}
	if(groupid != -1) {
		param.groupid = groupid;
	}
	
	param.isForceConverge = isCluster;
	
	Dituhui.Point.SearchByCluster(param, 
		function(e) {
			if(e.result.isConverge == true ) {
				Dituhui.Point.National.display(e.result);
			}
			else {
				/*console.log(e.result.level)
				if(e.result.level == '3' && e.result.records.length >= 500) {
					Map.showBranchesClusterByCounty(e.result.records);
				}*/
				
				Map.showBranchesToMap(e.result.records);			
			}
			
			if(typeof callback === "function") {
				callback();
			}
		},
		function(error) {
			if(typeof callback === "function") {
				callback();
			}
		}
	);
}

Map.searchBranchesUnclear = function() {
	var val = $('.data-list .search-input').val();
	var param = { pageNo: -1 };		
	if( val.length > 0 ) {
		param.name = val;
	}
	Dituhui.Point.Search(param, 
		function(data) {	
			var level = $('.smcity').attr('level');
			if(level != '0') {
				Map.showBranchesToMap(data);
//				Dituhui.Point.Table.refresh(data);
			}
			else {
				Dituhui.Point.National.display(data);
			}
		},
		function(error) {

		}
	);
}

/**
 * 区县网点数量大于500时聚合
 */
Map.showBranchesClusterByCounty = function(count) {
	var citydom = $('.smcity');
	var x = citydom.attr('smx');
	var y = citydom.attr('smy');
	var e = {
		level: 3,
		records: [
			{
				"count": count + '',
				"county": citydom.attr('adminname'),
				"province":null,
				"admincode": citydom.attr('admincode'),
				"city":null,
				"y": y,
				"x": x,
				countyCluster: true
			}
		]
	};
	Dituhui.Point.National.display(e);
}

/**
 * 地图上显示网点数据
 */
Map.showBranchesToMap = function(data) {
	layer_branches.removeAllFeatures();
	layer_point_label.removeAllFeatures();
	var len = data ? data.length : 0;
	if( len === 0 ) {
		Dituhui.showPopover("当前查询到0条网点数据");
		return;
	}
	var data_success = [], data_failed = [], pois = [], lbls = [], dcode = Dituhui.User.dcode;
	for( var i=0; i<len; i++ ) {
		var item = data[i];	
		
		//状态非0的网点都是解析失败的
		if(item.status != 0) {
			data_failed.push(item);
			continue;
		}
		data_success.push(item);

		if( !item.iconStyle ) {
			item.iconStyle = urls.server + "/resources/assets/map/red/s.png";
		}
		if(Baidu.using) {
			var coord = Baidu.getCoord(item.smx, item.smy);
			item.smx = coord.x;
			item.smy = coord.y;
		}
		
		/**
		 * 不是自己的区划
		 */
		item.belongToOthers = false;
		
		item.dcode = !!item.dcode ? item.dcode : dcode.substr(0, 8);
		if(item.dcode.substr(0, dcode.length) !== dcode) {
			item.belongToOthers = true;
		}
		var style = Dituhui.Point.getBranchStyle( item );
		
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);	
		var poi = new SuperMap.Feature.Vector(geo_point);

		poi.style = style;
		poi.attributes = item;
		poi.attributes.type = "point";
		pois.push(poi);

		var geoText = new SuperMap.Geometry.Point(item.smx, item.smy);
		var offset_y  = style.graphicHeight ? Number(style.graphicHeight) : 0;
		var ly = offset_y;
		
		offset_y = offset_y*0.5 + 32;
		ly = offset_y - 15;

		var css = Dituhui.Point.Style.getLabelStyle( item.name, -offset_y, ly);
		
		var geotextFeature = new SuperMap.Feature.Vector(geoText, { type: "text-point" }, css);
		lbls.push(geotextFeature);
	}
	layer_branches.addFeatures(pois);
	layer_point_label.addFeatures(lbls);

	Dituhui.Point.Table.data_success = data_success.concat();
	Dituhui.Point.Table.data_failed = data_failed.concat();
	$('.a-data-success > span').html('('+ data_success.length +')');
	$('.a-data-failed > span').html('('+ data_failed.length +')');
	
	Dituhui.Point.Table.refresh(data_success);	
}

/**
 * 地图上显示网点聚合数据,区县级别以下
 */
Map.showBranchesClusterToMap = function(data) {
	layer_point_cluster.destroyCluster();
	
	var len = data ? data.length : 0;
	if( len === 0 ) {
		Dituhui.showPopover("当前查询到0条网点数据");
		return;
	}
	var data_success = [], data_failed = [], pois = [], lbls = [], dcode = Dituhui.Users.dcode;
	for( var i=0; i<len; i++ ) {
		var item = data[i];	
		
		if(item.status != 0) {
			data_failed.push(item);
			continue;
		}
		data_success.push(item);

		if( !item.iconStyle ) {
			item.iconStyle = urls.server + "/resources/assets/map/red/s.png";
		}
		if(Baidu.using) {
			var coord = Baidu.getCoord(item.smx, item.smy);
			item.smx = coord.x;
			item.smy = coord.y;
		}		
		/**
		 * 不是自己的区划
		 */
		item.belongToOthers = false;
		item.dcode = !!item.dcode ? item.dcode : dcode.substr(0, 8);
		if(item.dcode.substr(0, dcode.length) !== dcode) {
			item.belongToOthers = true;
		}
		var style = Dituhui.Point.getBranchStyle( item );
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);	
		var poi = new SuperMap.Feature.Vector(geo_point);

		poi.style = style;
		poi.attributes = item;
		poi.attributes.type = "point";
		pois.push(poi);

		var geoText = new SuperMap.Geometry.Point(item.smx, item.smy);
		var offset_y  = style.graphicHeight ? Number(style.graphicHeight) : 0;
		var ly = offset_y;
		
		offset_y = offset_y*0.5 + 32;
		ly = offset_y - 15;

		var css = Dituhui.Point.Style.getLabelStyle( item.name, -offset_y, ly);
		
		var geotextFeature = new SuperMap.Feature.Vector(geoText, { type: "text-point" }, css);
		lbls.push(geotextFeature);
	}
	layer_point_cluster.addFeatures(pois);
	layer_point_label.addFeatures(lbls);

	Dituhui.Point.Table.data_success = data_success.concat();
	Dituhui.Point.Table.data_failed = data_failed.concat();
	$('.a-data-success > span').html('('+ data_success.length +')');
	$('.a-data-failed > span').html('('+ data_failed.length +')');
}

/**
 * 显示定位成功的网点
 */
Map.showSuccessData = function() {
	Map.showDatas( Dituhui.Point.Table.data_success );
}
/**
 * 显示定位失败的网点
 */
Map.showFailedData = function() {
	Map.showDatas( Dituhui.Point.Table.data_failed );
}
/**
 * 重构网点列表数据,单独显示定位成功或失败的网点数据
 */
Map.showDatas = function(data){
	map.removeAllPopup();
	layer_branches.removeAllFeatures();
	layer_point_label.removeAllFeatures();
	Map.endDragBranch();
	var len = data.length;

	if( len === 0 ) {			
	    $('.data-list table tbody').html('');
	    $('.data-list .totality').html('(0条)');
		return;		
	}
	var pois = [], lbls = [], dcode = Dituhui.User.dcode;
	for( var i=0; i<len; i++ ) {
		var item = data[i];
		var position = new SuperMap.LonLat(item.smx, item.smy);
		if( !item.iconStyle ) {
			item.iconStyle = urls.server + "/resources/assets/map/red/s.png";
		}
		var style = Dituhui.Point.getBranchStyle( item.iconStyle );
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
		/**
		 * 不是自己的区划
		 */
		item.belongToOthers = false;
		item.dcode = !!item.dcode ? item.dcode : dcode.substr(0, 8);
		if(item.dcode.substr(0, dcode.length) !== dcode) {
			item.belongToOthers = true;
		}
		var poi = new SuperMap.Feature.Vector(geo_point);
		poi.style = Dituhui.Point.getBranchStyle( item );

		poi.attributes = item;
		poi.attributes.type = "point";
		pois.push(poi);

		var geoText = new SuperMap.Geometry.Point(item.smx, item.smy);
		var offset_y  = style.graphicHeight ? Number(style.graphicHeight) : 0;
		var ly = offset_y;
		if( !style.externalGraphic.match('\/pointService\/getImg') ) {
			offset_y =  Number(offset_y) + 15;
		}
		else {
			ly = offset_y - 15;
		}

		var css = Dituhui.Point.Style.getLabelStyle( item.name, -offset_y, ly);
		var geotextFeature = new SuperMap.Feature.Vector(geoText, { type: "text-point" }, css);
		lbls.push(geotextFeature);
	}
	layer_branches.addFeatures(pois);
	layer_point_label.addFeatures(lbls);
	Dituhui.Point.Table.refresh(data);
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
		h += Dituhui.Point.getAttrPopupHtml(marker, marker.belongToOthers ? false : true);
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

	Dituhui.Point.getBindingCars( marker.id, 
        function(data){
            $('.bind-cars').html(data);
        },
        function(){}
    );

	
	$('#txt_upload_point_picture_pointid').val( marker.id );
    Map.Pictures.search(marker.belongToOthers ? false : true);

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
		Dituhui.Modal.alert('是否删除网点 <strong>'+ marker.name +'</strong> ? 删除后不可恢复。', 
			Map.deleteBranch,
			{"data-id": marker.id, "data-name": marker.name}
		);
	});

    $('.data-list table tbody tr').removeClass('action');
    var tr = $('.data-list table tbody tr[data-id="'+ marker.id +'"]').addClass('action');
    var data_table = $('.data-list .data-results .overflow-y-auto');
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
        externalGraphic: urls.server +'/resources/assets/map/editBranch.png',
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
var marker_editing = null;
Map.needCaculateCoord = false;
Map.openEditBranchPopup = function(marker) {	
	marker_editing = marker;
	var me = marker;

	var h =  '<div class="map-popup">';
		h +=  '<div class="map-popup-content">';
		var isDelete = marker.failed ? true : false;
		h += Dituhui.Point.getEditPopupHeaderHtml( me.name ? me.name : "", isDelete);

		var feature_name = selectedFeature ? (selectedFeature.attributes.areaName ? selectedFeature.attributes.areaName : "") : "";
		var feature_id = selectedFeature ? (selectedFeature.attributes.areaId ? selectedFeature.attributes.areaId : "") : "";
		h += Dituhui.Point.getAddPopupFormHtml(2, marker, feature_name, feature_id);
		h += '</div>';

		// h += Dituhui.Point.getIconsListHtml();

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

	Dituhui.Point.getBindingCars( marker.id, 
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

	Dituhui.Point.setPopupDivStyle();

	$('.close-popup').unbind("click").click(function(){
		Dituhui.Modal.ask('确定不保存吗？', Map.cancelAddBranch)
	});
	$('.popup-sure').unbind("click").click(Map.updateBranch);
	$('.popup-delete').unbind("click").click(function(){
		Dituhui.Modal.alert('是否删除网点 <strong>'+ marker.name +'</strong> ? 删除后不可恢复。', 
			Map.deleteBranch,
			{"data-id": marker.id, "data-name": marker.name}
		);
	});
	Map.initSelectGroupInPopup(marker);
	
	$('.photo').click(function(){
		// $('input[name="netPicFile"]').click();
		$('.upload-pictures').removeClass("hide");
	});
	$('.popup-setting').unbind('click').click(function(){
		$('.setting-fields').removeClass("hide");
	});
	
	var count = $('.ul-point-pictures li.normal').length;
	if(count == 0) {
		$('.preview-img').hide();
	}
	else {
		var src = $('.ul-point-pictures li.normal:first-child img').attr('src');
		var html  = '<img src="'+ src + '">';
			html += '<div class="count"><span>'+ count +'张</span></div>';
		$('.preview-img').html(html).unbind('click').click(function(){
			Map.Pictures.viewPictures();
			$('.view-pictures').removeClass("hide");
		});
	}	

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
	Map.needCaculateCoord = false;
	$('.smx-popup').change(function(){
		Map.needCaculateCoord = true;
	});
	$('.smy-popup').change(function(){
		Map.needCaculateCoord = true;
	});
	$('#txt_name').focus().val( $('#txt_name').val() );
}

Map.initSelectGroupInPopup = function(me) {
	if( me && me.groupid) {
        if(me.groupid.styleid) {
            me.styleid = me.groupid.styleid;
            me.styleid.groupid = me.groupid.id;
        }
        else {
            me.styleid = {};
            me.styleid.groupid = me.groupid.id;
        }
    }
	var data = Dituhui.Point.Group.data.concat();
	var h = '<option value="0">无</option>';
	for(var i=data.length; i--; ) {
        var item = data[i];
        if( me.styleid.groupid == item.id ) {
        	h += '<option value="'+ item.id +'" selected="true">'+ Dituhui.setStringEsc(item.groupname) +'</option>';
        }
        else {
        	h += '<option value="'+ item.id +'">'+ Dituhui.setStringEsc(item.groupname) +'</option>';
        }
    }
    $('.select-group-popup').html(h).change(function(){
    	var me = $(this);
    	$('input[name="groupid"]').val( me.val() );
    	
    	var select = $('#txt_branchGroup');
    	select.val( me.val() );
    	
    	var groupname = $('#txt_branchGroup option:selected').text();
		$('#txt_styleName').val( groupname );
		$('.icon-demo + .bottom').html(groupname);
    	
		if(me.val() == 'none' || me.val() == '0') {
			$('input[name="styleid"], input[name="stylename"], input[name="groupid"]').val('');
			/*groupname = '无';
			$('#txt_styleName').val( groupname );
			$('.icon-demo + .bottom').html(groupname);*/
			Map.Group.switchStyle(Dituhui.Point.Style.defaultStyle, true);
			return;
		}
		
		//刷新样式
		if(select.hasClass('icon-style-input')) {
			Map.Group.getStyle(me.val());
			$('input[name="groupid"]').val(me.val());
		}
    });
}

/**
 * 更新网点
 */
Map.updateBranch = function() {
	var name = $('#txt_name').val();
	if(name.length < 1) {
		Dituhui.showHint("请输入网点名称");
		return;
	}
	var address = $('#txt_address').val();
	if(address.length < 1) {
		Dituhui.showHint("请输入网点地址");
		return;
	}
    var fileSizeOK = checkFileSize();
    if( fileSizeOK == false ) {
        $('.hint').html("图片大于100K，请重新选择");
        return;
    }

    if( Map.needCaculateCoord ) {
    	var smx = Number($('.smx-popup').val());
    	var smy = Number($('.smy-popup').val());

    	if( isNaN(smx) || smx > 180 || smx < -180 || smx == 0 ) {
    		Dituhui.showHint("请正确输入经度");
    		return;
    	}
    	if( isNaN(smy) || smy > 90 || smy < -90 || smy == 0  ) {
    		Dituhui.showHint("请正确输入纬度");
    		return;
    	}
    	var coord = Dituhui.latLonToMeters( new SuperMap.LonLat(Number(smx), Number(smy)) );   
        $('input[name="smx"]').val(coord.lon);
        $('input[name="smy"]').val(coord.lat);
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
				Dituhui.showHint('外观和图案不允许都为无');
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
		    	Dituhui.showHint('请输入样式名称');
		    	return;
    		}
			$('input[name="stylename"]').val(name);

			Dituhui.Modal.alert('将更新所属分组为“'+ $('#txt_branchGroup option:selected').text()+'”的图标样式，确定继续？',
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
			Dituhui.showPopover("修改成功");
			map.removeAllPopup();

			Map.endDragBranch();

			Map.searchBranches();

			if( $('.data-list').attr('status') != 'min' && $('.icon-edit-container').is(':visible')
				&& $('.icon-save-container').is(':hidden') 
			) {
				$('.icon-save-container, .tab-icon-list').show();
				$('.tab-edit').removeClass("hide");
				$('.data-list .head .tab-text').click();
			}	
        }
        else {
            Dituhui.showPopover(e.info);
        }
        Map.Style.search();
    });
    Dituhui.Modal.hide();
}
/**
 * 删除网点
 */
Map.deleteBranch = function() {
	var me = $(this);
	var id = me.attr("data-id");
	Dituhui.Point.remove(id, 
		function(){
			Dituhui.showPopover("网点\""+ me.attr("data-name") +"\"删除成功");
			$(".data-modal").addClass("hide");
			map.removeAllPopup();
			Map.searchBranches();
		},
		function(){
			Dituhui.showPopover("网点\""+ me.attr("data-name") +"\"删除失败");
		}
	)
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
			Region.select(feature);
			break;
		case 'point-cloud':
			poisearch.openPopup(feature.attributes);
			break;
		case 'point-national':
			var attr = feature.attributes;
			
			var name = attr.adminname, code = attr.admincode, level = attr.level;
			$('.current-city').attr({
	        	smx: attr.x,
	        	smy: attr.y
	        });
			if(level == 1) {
        		var iszhixia = Dituhui.SMCity.isZhixia( code.substring(0, 2) + '0000' );
	        	if(iszhixia) {
	        		Dituhui.SMCity.showCurrentCounty(name, code, '');
	        		
	        		/*if(attr.count >= 500 && $('#btn_showCluster').prop('checked')) {
		        		Map.showBranchesClusterByCounty(attr.count);
		        		break;
		        	}*/
	        	}
	        	else {
	        		Dituhui.SMCity.showCurrentCity(name, code);
	        		/*if(attr.count >= 500 && $('#btn_showCluster').prop('checked')) {
		        		Map.showBranchesClusterByCounty(attr.count);
		        		break;
		        	}*/
	        	}
	        }
	        else if(level == 2) {
	        	Dituhui.SMCity.showCurrentCounty(name, code, '');
	        }
	        else {
	        	Dituhui.SMCity.showCurrentProvince(name, code);
	        }
        	Dituhui.SMCity.cityTagClick();
        	break;
        case 'point-county':
        	Map.searchBranches(false);
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
	$('#btn_showBranch, #btn_showRegion, #show-point-label').prop('checked', false);
	$('#btn_showRegion').removeAttr('isShowRegion');
	layer_region.removeAllFeatures();
	layer_region.setVisibility(false);
	layer_region_label.removeAllFeatures();
	layer_region_label.setVisibility(false);
	layer_branches.removeAllFeatures();
	layer_branches.setVisibility(false);
	layer_point_label.removeAllFeatures();
	layer_point_label.setVisibility(false);
	layer_edit_branch.removeAllFeatures();
	layer_cloudpois.removeAllFeatures();
    $('.header .search-from-cloud .child').addClass("hide");
	control_drawPoint.deactivate();
	map.removeAllPopup();
	Map.popup = null;
	Map.endDragBranch();

	Dituhui.Point.Table.clear();

	$('.search-area .select-group').val('-1');
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
    poi.style = Dituhui.Point.getBranchStyle(obj);
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
		h += Dituhui.Point.getEditPopupHeaderHtml(attr.name, false, true);
	}
	else {
		h += Dituhui.Point.getAddPopupHeaderHtml();
	}
	
	var b_coord = lonlat;
    if(Baidu && Baidu.using) {
    	var b_ll = lonlat;
        var coord = Baidu.restoreCoord(b_ll.lon, b_ll.lat);
        b_coord = new SuperMap.LonLat(coord.x, coord.y);
    }

	var feature_name = selectedFeature ? 
		(selectedFeature.attributes.name ? selectedFeature.attributes.name : "") : "";
	var feature_id = selectedFeature ? 
		(selectedFeature.attributes.id ? selectedFeature.attributes.id : "") : "";
	var kind = type ? type : 1;
	h += Dituhui.Point.getAddPopupFormHtml(kind, b_coord, feature_name, feature_id, attr);
	h += '</div>';

	h += Dituhui.Point.getIconsListHtml();

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

	Dituhui.Point.setPopupDivStyle();

	$('.close-popup').unbind("click").click(function(){
		Dituhui.Modal.ask( '确定不保存吗？',  Map.cancelAddBranch);
	});
	$('.popup-sure').unbind("click").click(Map.addBranch);
	$('.photo').click(function(){
		$('.upload-pictures').removeClass("hide");
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
	if($('.ul-point-pictures li.normal').length == 0){
		$('.preview-img').hide();
	}
	$('#txt_name').focus();
}

/**
 * 取消新增网点操作
 */
Map.cancelAddBranch = function() {
	map.removeAllPopup();
	layer_edit_branch.removeAllFeatures();
	Map.endDragBranch();
	$('.data-modal').addClass("hide");
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
		Dituhui.showHint("请输入网点名称");
		return;
	}
	// name = Dituhui.setStringEsc(name);
	var address = $('#txt_address').val();
	if(address.length < 1) {
		Dituhui.showHint("请输入网点地址");
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
				Dituhui.showHint('外观和图案不允许都为无');
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
		    	Dituhui.showHint('请输入样式名称');
		    	return;
    		}    		
			$('input[name="stylename"]').val(name);

			Dituhui.Modal.alert('将更新所属分组为“'+ $('#txt_branchGroup option:selected').text() +'”的图标样式，确定继续？',
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
			Dituhui.showPopover("添加成功");
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
            Dituhui.showPopover(e.info);
        }
        Map.Style.search();
    });
    Dituhui.Modal.hide();
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
	Dituhui.Point.regeocode( param, 
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
					Dituhui.SMCity.showCurrentCounty(data.county, data.admincode, data.city);
					Map.searchBranchesUnclear();
				}
				else if(data.city && data.city.length != 0) {
					Dituhui.SMCity.showCurrentCity(data.city, data.admincode);
					Map.searchBranchesUnclear();
				}
				else if(data.province && data.province.length != 0) {
					Dituhui.SMCity.showCurrentProvince(data.province, data.admincode);		
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
	        Dituhui.showHint( "请选择文件" );
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
        Dituhui.showHint( "请选择文件" );
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
	Dituhui.Modal.loading('正在导入网点，请稍候...');
	$('a.btn-select-file').removeClass('btn-select-file').addClass('btn-during-import');
	var end = false;
    $("#form_import_branches").ajaxSubmit({
    	timeout: 90000,
    	error: function(e) {
    		end = true;
    		Map.resetWindowClose();
    		Dituhui.Modal.hide();
    		Dituhui.showHint('导入失败');
			$('a.btn-during-import').removeClass('btn-during-import').addClass('btn-select-file');
    	},
    	success: function(e) { 
    		end = true;
	    	// Dituhui.hideMask();
			$('a.btn-during-import').removeClass('btn-during-import').addClass('btn-select-file');
	    	if( urls.ie_case ) {
	    		e = $.parseJSON($(e).text());
	    	}
	    	else {
	    		e = JSON.parse(e);
	    	}	    	
	        if(e.isSuccess) {
	        	$('.data-import').fadeOut('fast');
	        	Dituhui.Modal.loading( e.info ? e.info + '</br>正在解析数据...<br>' : '正在解析数据...<br>' );
	        	Map.analysisPoints(e.info);
	        }
	        else {
	        	Map.resetWindowClose();
	            Dituhui.Modal.loaded_wrong('数据导入失败<br>' + e.info, Map.afterImportWrong);
	        }
    	}
    });	    
    //延迟设置窗口关闭事件，解决IE下自动弹出关闭窗
	setTimeout(function(){    		
		if(end == false) {
    		Map.setWindowClose();
    	}
    }, 200);
    
    
	
}
Map.analysisPoints = function(importInfo){
	Dituhui.Point.analysisPoints(true, function(){
		Map.resetWindowClose();
		Dituhui.Modal.loaded_right('数据已导入完成。<br>' + importInfo, Map.afterImportRight); 
	}, function(r){
		Map.resetWindowClose();
		if(!r) {
			r = importInfo;
		}
		Dituhui.Modal.loaded_wrong('数据导入失败<br>' + r, Map.afterImportWrong);
	});
}
Map.afterImportRight = function() {
	map.removeAllPopup();
	Map.endDragBranch();
	layer_edit_branch.removeAllFeatures();
	Map.getColumns(false);								
	Map.Group.search();
	$('a[option="showWholeCountry"]').click();
	Dituhui.Modal.hide();
	var btn_showBranch = $('#btn_showBranch');
	if(btn_showBranch.prop('checked') == false) {
		btn_showBranch.click();
	}
}
Map.afterImportWrong = function() {
	Dituhui.Modal.hide();
}
/**
 * 获取网点自定义字段
 */
Map.getColumns = function(first) {
	Dituhui.Point.createSysThead();
	Dituhui.Point.getCustomColumns(first);
}
/**
 * 更新单元格数据
 */
Map.updateCell = function() {
	Dituhui.Point.Table.updateCell();
}
/**
 * 更新单元格数据
 */
Map.updateCellGroup = function() {
	Dituhui.Point.Table.updateCellGroup();
}
/**
 * 新增列
 */
Map.addColumn = function() {
	Dituhui.Point.Table.addColumn();
}
/**
 * 删除列
 */
Map.removeColumn = function() {
	Dituhui.Point.Table.removeColumn();
}
/**
 * 重命名表头列名称
 */
Map.renameColumn = function() {
	Dituhui.Point.Table.renameColumn();
}

/**
 * 地图缩放完成
 */
Map.zoomEnd = function(e) {
	var zoom = e.object.zoom;

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

Map.showPointLabels = function(){
	var bool = $('#show-point-label').prop('checked');
	layer_point_label.setVisibility(bool);
	if(bool == true && $('#btn_showBranch').prop('checked') == false ) {
		$('#btn_showBranch').prop('checked', true);
		layer_branches.setVisibility(bool);		
	}
	return;
	if(bool) {
		var fs = layer_branches.features;
		for(var i=fs.length; i--; ) {
			var attr = fs[i].attributes;

			var pop = new SuperMap.Popup.Anchored(null,
				new SuperMap.LonLat(attr.smx, attr.smy),
				new SuperMap.Size(80, 22),
				attr.name,
				null,
				false
			);
			pop.setBorder('solid 1px #aaaaaa');
			Map.pop_labels.push(pop);
			map.addPopup(pop);
		}
	}
	else {

	}
}

/**
 * 从云平台搜索
 
Map.searchFromCloud = function() {
	layer_cloudpois.removeAllFeatures();
	map.removeAllPopup();
	Map.endDragBranch();
	layer_edit_branch.removeAllFeatures();

    $('.header .search-from-cloud .child').addClass("hide");
	var keyword = $("#txt_keyword_cloud").val();
	if( keyword.length < 1 ) {
		Dituhui.showPopover("请输入搜索关键字");
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
	Dituhui.Point.searchFromCloud(param, 
		function(e){				
			var data = e.pois ? e.pois : e.results;
			var len = data.length;
			var pois = [];
			for( var i=0; i<len; i++ ) {
				var item = data[i];
				var position = new SuperMap.LonLat(item.smx, item.smy);		
				if(Baidu.using) {
					var coord = Baidu.getCoord(item.smx, item.smy);
					item.smx = coord.x;
					item.smy = coord.y;
				}	
				var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = {
					externalGraphic: urls.server + "/resources/assets/num/" + (i+1) + ".gif",
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
    		$('.header .search-from-cloud .child').removeClass("hide");
		}
	);
}
*/
/**
 * 显示云平台搜索结果到div
 
Map.showCloudSearchResult = function(e){	
    var div = $('.header .search-from-cloud .cloud-pois .content');
    var h = Dituhui.Point.getCloudSearchHtml(e.pois ? e.pois : e.results);
    div.html(h);     

    $('.header .search-from-cloud .child').removeClass("hide");
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
}*/
/**
 * 显示云平台搜索结果popup
 
Map.openCloudPopup = function(attr) {
	var position = new SuperMap.LonLat( attr.smx, attr.smy );
    attr.lonlat = position;

	var h =  '<div class="map-popup" style="min-height: 110px;">';
		h +=  '<div class="map-popup-content">';
		h += Dituhui.Point.getCloudPopupHtml(attr, true);
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

	$('#txt_upload_point_picture_pointid').val( attr.id );
    Map.Pictures.search();

	$('a.a-save-as-branch').unbind('click').click(function(){
		Map.openAddBranchPopup( attr.lonlat,3,attr);
		if(attr.address == '' || $('.smcity').attr('level') == '0') {
			Map.regeocode(attr.lonlat, true);
		}
	});
}
*/

Map.checkAction = function() {
    return true;
}




























