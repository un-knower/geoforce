$(function(){
	Dituhui.User.allowTownOption = true;
	Map.initMap();
	$('#map').on('change', "#select_region_province", Dituhui.Zone.selectRegionProvince);
	$('#map').on('change', "#select_region_city", Dituhui.Zone.selectRegionCity);
	$('#map').on('change', "#select_region_county", Dituhui.Zone.selectRegionCounty);
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
var layer_countyboundry = null;
var isCtrlKeydown = false;
var isFirefox = false;

var layer_snap = null;

//当不显示区划时，临时图层用于显示选中的区划
var layer_region_temp = null, layer_region_label_temp = null;
var relatedSelect;

var Map = {
	//是否允许选择区划
	allowSelect: true,
	//是否正在进行沿路画区
	isDrawingPathRegion: false,
	navigation: new SuperMap.Control.Navigation({
		id: "control_navigation",
        dragPanOptions: {
            enableKinetic: true
        }
    })
};

Map.initMap = function() {
	var pan = new SuperMap.Control.PanZoomBar({showSlider:true})
	map = new SuperMap.Map("map", 
		{ 
			controls: [
//			    pan,
			    Map.navigation
			],
			allOverlays: true
		}
	);
	
	layer_countyboundry = new SuperMap.Layer.CloudLayer(
		{
			url: urls.map_countyboundry,
			resolutions: Map.cloud_resolutions.slice(10)
		}
	);
	map.addLayer(layer_countyboundry);
	layer_countyboundry.setVisibility(false);
	
	layer_branches = new SuperMap.Layer.Vector("branches");
	layer_orders = new SuperMap.Layer.Vector("orders", {renderers: ["Canvas2"]});
	layer_snap = new SuperMap.Layer.Vector("snap", {renderers: ["Canvas2"]});
	layer_snap.style = Dituhui.Zone.getSnapPointStyle();

	var strategy = new SuperMap.Strategy.GeoText();
	strategy.groupField = "style_status";
	strategy.styleGroups = Dituhui.Zone.getRegionTextStyleGroups();
	layer_region_label = new SuperMap.Layer.Vector("Label", {renderers:["SVG"], strategies: [strategy]});
	
	layer_region = new SuperMap.Layer.Vector("region", {renderers: ["SVG"]});
	
	var strategy_temp = new SuperMap.Strategy.GeoText();
	strategy_temp.groupField = "style_status";
	strategy_temp.styleGroups = Dituhui.Zone.getRegionTextStyleGroups();
	layer_region_temp = new SuperMap.Layer.Vector("region_temp", {renderers:["SVG"]});
	layer_region_label_temp = new SuperMap.Layer.Vector("region_temp_label", {renderers:["SVG"], strategies: [strategy_temp]});
	
	layer_edit = new SuperMap.Layer.Vector("edit");
	layer_edit.style = Dituhui.Zone.getRegionEditStyle();
	layer_edit_regionroute = new SuperMap.Layer.Vector("region_route");

	layer_boundry = new SuperMap.Layer.Vector("boundry",{renderers: ["Canvas2"]});
	layer_cloudpois = new SuperMap.Layer.Vector("cloud_pois", {renderers: ["Canvas2"]});
	map.addLayers([ layer_boundry, layer_cloudpois, layer_region_temp, layer_region_label_temp, layer_region, layer_region_label, layer_branches, layer_orders, layer_edit_regionroute, layer_snap, layer_edit]);
	layer_branches.setVisibility(false);
	layer_region.setVisibility(false);
	layer_region_label.setVisibility(false);
	
	Map.init(true);
	
	Measure.init();
	
	//自由画区
	control_drawPolygon = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Polygon, { multi: false });
	control_drawPolygon.events.on({"featureadded": Map.drawRegionCompleted});
	var snap_addregion = new SuperMap.Snap([layer_region],10,10, {actived:true});
	/*snap_addregion.events.on({
		"snapping": function(e) {
			var f = new SuperMap.Feature.Vector(
				new SuperMap.Geometry.Point(e.result.x, e.result.y));			
			layer_snap.addFeatures([f])
		}
	});*/
	control_drawPolygon.snap = snap_addregion;
	
	//面拆分
	control_drawSplitArea = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Polygon, { multi: false });
	control_drawSplitArea.events.on({"featureadded": Map.drawSplitAreaCompleted});
	
	//线拆分
	control_drawSplitLine = new SuperMap.Control.DrawFeature(
		layer_edit,
		SuperMap.Handler.Path,
		{ multi: false },
		Dituhui.Zone.getSplitLineStyle()
	);	
	control_drawSplitLine.events.on({"featureadded": Map.drawSplitLineCompleted});
	
	//沿路画区
	control_drawPathRegion = new SuperMap.Control.DrawFeature(layer_edit, SuperMap.Handler.Point, { multi: true });
	control_drawPathRegion.events.on({"featureadded": Regionroute.drawRouteRegionCompleted});
	var snap_editregion = new SuperMap.Snap([layer_region],10,10, {actived:true});
	control_editPolygon = new SuperMap.Control.ModifyFeature(layer_edit, {
		standalone: true,
		geometryTypes: ['SuperMap.Geometry.Polygon'],
		toggle: false,
		deleteCodes: [46, 68]
	});
	
	/*control_editPolygon.vertexRenderIntent = "blue";
	control_editPolygon.virtualStyle.fillColor = "blue";
	control_editPolygon.virtualStyle.fillOpacity = 0.8;
	control_editPolygon.virtualStyle.strokeColor = "blue";*/
	
	control_editPolygon.snap = snap_editregion;
	map.addControls([
		control_drawPolygon,
		control_drawPathRegion,
		control_editPolygon,
		control_drawSplitLine,
		control_drawSplitArea
	]);	

//	map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	
	var selectStyle = Dituhui.Zone.getRegionSelectStyle();
	control_select = new SuperMap.Control.SelectFeature(
		[layer_cloudpois, layer_branches, Measure.layer, layer_region, layer_region_label], 
		{
			callbacks: {
				rightclick: Rightmenu.createMenu,
				click: Map.regionSelect,
				clickout: Map.regionUnSelect
			},
		    multiple: false,
		    hover: false,
		    multipleKey: "ctrlKey",
		    clickout: false,
		    repeat: true
		}
	);
	map.addControl(control_select);
	control_select.activate();
	var ovMap = new SuperMap.Control.OverviewMap({id:"overviewMap", maximized: false});
	var layer_ovmap = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	ovMap.layers = [layer_ovmap];
	map.addControl(ovMap);

	map.events.on({"zoomend": Map.zoomEnd});
	
	Dituhui.SMCity.initUserCity(Map.initDatas);
	Baidu.init();
	
	/*var doms = pan.getDoms();
	var zoommaxextent= doms.zoommaxextent;//罗盘中心的按钮
	$(zoommaxextent).unbind('mousedown').on('click', function(){
		console.log('11')
	})
	map.zoomToMaxExtent= function(options) {
	    map.setCenter(new SuperMap.LonLat(12958400.883957,4852082.440605948), 4);
	}*/
	
	Map.bindKeyPan();
	Map.setMapIndexTop();
	
	relatedSelect = new RelatedSelect();
}

/*
 * 显示/隐藏区县界
 */
Map.showCountryBoundry = function(){
	var bool = $(this).prop('checked');
	layer_countyboundry.setVisibility(bool);
}

/*
 * 禁止双击放大 在编辑区划及自由画区的时候可用
 */
Map.disableDbClick = function() {
	var ctrl = map.getControl('control_navigation');	
	ctrl.handlers.click['double'] = false;
}

/*
 * 允许双击放大 
 */
Map.enableDbClick = function() {
	var ctrl = map.getControl('control_navigation');	
	ctrl.handlers.click['double'] = true;
}

/*
 * 禁止地图拖拽  在编辑区划及自由画区的时候可用
 */
Map.disableDrag = function() {
	var ctrl = map.getControl('control_navigation');	
	ctrl.dragPan.deactivate();
}

/*
 * 允许地图拖拽 
 */
Map.enableDrag = function() {
	var ctrl = map.getControl('control_navigation');	
	ctrl.dragPan.activate();
}

/*
 * 初始化数据
 */
Map.initDatas = function() {
	Dituhui.SMCity.isCheckAction = true;
	Dituhui.SMCity.cityTagClick = function(cluster) {
		$('.search-area .select-group').val('-1');
		$('#txt_keyword_search_branches').val('');
		/*if(cluster === false) {
			Map.searchBranches(false);
		}
		else {
			Map.searchBranches();
		}*/
		if($('#btn_showBranch').prop("checked") ) {
			Map.searchBranches();
		}
		if($('#btn_showRegion').prop("checked") || $('.data-list tbody tr').length > 0) {
			Map.searchRegions();
		}
		map.removeAllPopup();
		
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
	if(Dituhui.User.regionTableExtend) {
		//海尔账号，表格定制
		var h = '<td class="td-150">网格组编码</td>'
			  + '<td class="td-150">网格组名称</td>'
			  + '<td class="td-150">线路编码</td>'
			  + '<td class="td-150">线路名称</td>';
		
		$(".data-results .data-table thead tr .thead-split").before(h);
	}
	if(!Dituhui.User.hasTownAuthority) {
		$(".shuttle-town .no-rights").show();
	}
	else {
		$(".shuttle-town .no-rights").hide();
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

Map.getSelectedFeatureIndex = function(id) {
	var fs = selectedFeatures.concat();
	for(var i=fs.length; i--; ) {
		if(fs[i].attributes.id === id) {
			return i;
		}
	}
	return -1;
}

Map.selectRegion = function(feature) {
	/**
	 * 关联区划的时候，只能选择自己的区划
	 */
	if(relatedSelect.active) {
		//是自己的区划
		if(feature.attributes.style_status !== 2.5) {
			relatedSelect.select(feature);
		}
		return;
	}
	
	/**
	 * 不是自己的区划
	 */
	if(feature.attributes.style_status === 2.5) {
		$(".tool-button.region-editable").addClass("non").unbind("click");
		Map.clearSelectedFeatures();
	}
	else {
		/**
		 * 已经选过不是自己的区划的时候，就清除
		 */
		for(var k=selectedFeatures.length; k--; ) {
			if(selectedFeatures[k].attributes.style_status === 2.5) {
				Map.clearSelectedFeatures();
				break;
			}
		}
		$(".tool-button.region-editable").removeClass("non");
		$('.toolbox .edit').click(Regionedit.editRegionClick);
		$('.toolbox .delete').click(Map.deleteRegionClick);
		$('.toolbox .merge').click(Map.mergeRegionClick);
		$('.toolbox .line-split').click(Map.lineSplitRegionClick);
		$('.toolbox .area-split').click(Map.areaSplitRegionClick);
	}
	
	var theEvent = window.event;
	
	//按住ctrl可多选	
	if( (isFirefox && !isCtrlKeydown) || (!isFirefox && !theEvent.ctrlKey)) {
		Map.clearSelectedFeatures();
	}
	var index = Map.getSelectedFeatureIndex(feature.attributes.id);//重复选择判断	
	if(index < 0) {
		feature.attributes.oldStyle = feature.style;
		feature.style = Dituhui.Zone.getRegionSelectStyle();
		//不重复添加
		selectedFeatures.push(feature);
	}
	
	selectedFeature = feature;
	Dituhui.Zone.Table.scrollToFeature(feature);
	
	if(typeof(openInfoWindow) === "undefined" || openInfoWindow == true ) {
		Map.openRegionAttrPopup(feature);
	}
	
	if(layer_region.visibility == false) {
		var record = selectedFeature.attributes;
		var geoText = new SuperMap.Geometry.GeoText(record.center.x, record.center.y, record.name);
		var geotextFeature = new SuperMap.Feature.Vector(geoText, record);
		layer_region_temp.addFeatures([selectedFeature.clone()]);
		layer_region_label_temp.addFeatures([geotextFeature]);
	}
	
	layer_region.redraw();
}

/**
 * 选择区划
 */
Map.regionSelect = function(feature, openInfoWindow) {
	var type = feature.attributes.type;

	switch( type ) {
		case "point":
			Map.selectBranch(feature);
			break;
		case "region":
			Map.selectRegion(feature);
			break;
			
		case "text-region":
			feature = Map.getFeatureFromLayerById( feature.attributes.id, layer_region.features);
			Map.selectRegion(feature);
			break;
		case "clear-measure":
			Measure.clearMeasure(feature);
			break;
		case "point-cloud":
			poisearch.openPopup(feature.attributes);
		 	break;
	}
}

/**
 * 取消选择区划
 */
Map.regionUnSelect = function(feature) {
	return;
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
	layer_region_temp.removeAllFeatures();
	layer_region_label_temp.removeAllFeatures();
	selectedFeatures = [];
	selectedFeature = null;
}

/**
 * 弹出区划属性框
 */
Map.openRegionAttrPopup = function(feature) {
	var attr = feature.attributes;
	var h = Dituhui.Zone.getAttrRegionHtml(attr);	
	var lonlat = new SuperMap.LonLat(attr.center.x, attr.center.y);
	attr.lonlat = lonlat;
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
		map.removeAllPopup();
		layer_region_temp.removeAllFeatures();
		layer_region_label_temp.removeAllFeatures();
	});
	$('.popup-edit').unbind("click").click(function(){
		Map.openEditRegionPopup(attr, false);
	});

	var span = $(".region-adminname");
	Dituhui.Zone.getAdminnameByCode( attr.admincode, 
		function(data){
			span.html(data);
		},
		function(){
			span.html("");
		}
	);
}

/**
 * 弹出编辑区划属性的信息窗
 */
Map.openEditRegionPopup = function(attr, surecancel) {
	var h = Dituhui.Zone.getEditRegionHtml(1, attr);
	Map.popup = new SuperMap.Popup.FramedCloud("popup-region",
		attr.lonlat,
		new SuperMap.Size(320, 200),
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
	
	$('#txt_number').unbind('keyup').keyup(function(e){
		if(e.keyCode == 13) {
			Map.getBranchName();
		}
	});
	$('.popup-close').unbind('click').on('click', function(){
		if(typeof surecancel != "undefined" && surecancel === true) {
			Map.cancelAddRegion();
		}
		else {
			Dituhui.Modal.ask('确定不保存吗？', Map.cancelAddRegion);
		}
	});
	$('.popup-sure').unbind('click').on('click', function(){
		Map.updateRegionAttr(attr, surecancel);
	});
	if(attr.area_status==1) {
		relatedSelect.activate(selectedFeature);
	}
	$(".popup-select-area-status").unbind("change").change(function(){
		var val = $(this).val(), con = $(".relate-region-content"), mapc = $('.map-popup .content');
		//正常
		if(val == 0 || val == 2) {
			relatedSelect.deactivate(true);
			con.addClass("hide").find("input").val("");
		}
		//停用
		else {
			relatedSelect.activate(selectedFeature);
			con.removeClass("hide"),mapc.scrollTop( mapc[0].scrollHeight);
		}
	});
	Dituhui.Zone.initRegionPopupAdmincode(attr);
	
}

/**
 * 更新区划属性
 * @param {Object} attr
 */
Map.updateRegionAttr = function(attr, surecancel) {
	var name = $('#txt_name').val();
	if(name === "" || name.length < 1)  {
		Dituhui.showHint("请输入区划名称");
		return;
	}
	if(name.length > 50) {
		Dituhui.showHint("区划名称长度不能大于50位");
		return;
	}
	
	var number = $('#txt_number').val();
	if(number === "" || number.length < 1) {
		Dituhui.showHint("请输入区划编号");
		return;
	}
	if(number.length > 50) {
		Dituhui.showHint("区划编号长度不能大于50位");
		return;
	}
	
	var areastatus = $('.popup-select-area-status').val();
	var relationareaid = $('#txt_relation_areaid').val();
	
	var select_province = $("#select_region_province").val();
	var select_city = $("#select_region_city").val();
	var select_county = $("#select_region_county").val();
	var select_town = (Dituhui.User.hasTownAuthority ? $("#select_region_town").val() : "-1" );
	
	var code = "";
	if(Dituhui.User.hasTownAuthority && select_town && select_town != "-1"){
		code = select_town;
	}
	else if(select_county && select_county != "-1") {
		code = select_county;
	}
	else if(select_city && select_city != "-1") {
		code = select_city;
	}
	else if(select_province && select_province != "-1") {
		code = select_province;
	} 
	else {
		Dituhui.showHint("请选择区划归属");
		return;
	}
	
	var param = {
		id: attr.id,
		areaName: name,
		areaNum: number,
		admincode: code,
		pointid: pointid,
		areastatus: areastatus,
		relationareaid: relationareaid
	};
	
	if(Dituhui.User.regionTableExtend) {  
		param.wgzCode = $("#txt_wgzCode").val();
		param.wgzName = $("#txt_wgzName").val();
		param.lineCode = $("#txt_lineCode").val();
		param.lineName = $("#txt_lineName").val();
	}
	
	var pointid = $('#txt_name').attr("poi-id");
	if(name === attr.name && number === attr.areaNumber && code === attr.admincode
		&& areastatus == attr.area_status && relationareaid == attr.relation_areaid
		&& (Dituhui.User.regionTableExtend && param.wgzCode === attr.wgzCode 
			&& param.wgzName === attr.wgzName && param.lineCode === attr.lineCode
			&& param.lineName === attr.lineName)
	) {
		if(typeof surecancel != "undefined" && surecancel === true) {
			Map.cancelAddRegion();
		}
		else {
			Dituhui.showHint("未进行修改");
		}
		return;
	}
	
	
	Dituhui.Zone.updateAttr(
		param,
		function(){
			Dituhui.showPopover("区划 \""+ (name ? name : item.name) +"\" 属性修改成功");
			map.removeAllPopup();
			Map.clearSelectedFeatures();
			Map.search();
			relatedSelect.deactivate(false);
		},
		function(info){
			info = info ? info : "区划 \""+ (name ? name : item.name) +"\" 属性修改失败";
			Dituhui.Modal.loaded_wrong(info, function(){
				$('.data-modal').addClass("hide");
			});
		}
	);
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
			Dituhui.showHint("如果查看区划，请选择省市");
			me.prop("checked", false);
			return;
		}
		var zoom = map.getZoom();
		if(zoom < 5) {
			Dituhui.showHint("请将地图放大到5级以上再显示区划");
			me.prop("checked", false);
			return;
		}
		if( layer_region.features.length === 0 ) {
			Map.searchRegions();
		}
		me.attr("isShowRegion", "true");		
		layer_region_temp.removeAllFeatures();
		layer_region_label_temp.removeAllFeatures();
	}
	else {
		me.removeAttr("isShowRegion");
		map.removeAllPopup();
	}
	layer_region.setVisibility(bool);
	layer_region_label.setVisibility(bool);
	layer_region_temp.setVisibility(!bool);
	layer_region_label_temp.setVisibility(!bool);
}

/**
 * 查询区划
 */
Map.searchRegions = function(callbacks) {
	var keyword = $('#txt_keyword_search_regions').val();
	var smcity = $('.smcity');
	var admincode = smcity.attr('admincode');
	var level = smcity.attr('level');
	if( admincode === "" ) {
		Dituhui.showPopover("查询区划请选择省");
		return;
	}
	
	layer_region.removeAllFeatures();
	layer_region_label.removeAllFeatures();
	layer_region_temp.removeAllFeatures();	
	map.removeAllPopup();
	Map.clearSelectedFeatures();
	Dituhui.Zone.Table.clear();
	
	Dituhui.showMask();
	var param = {
		admincode: admincode,
		level: level
	}
	if( keyword != "" && keyword.length > 0 ) {
		param.areaName = keyword;
		param.areaNumber = keyword;
	}

	Dituhui.Zone.search(param, 
		function(data){
			Dituhui.hideMask();			
			Map.showRegionsToMap(data);
			Dituhui.Zone.Table.refresh(data);
			
			if(typeof(callbacks) === "function") {
				callbacks();
			}
		},
		function(error){
			Dituhui.hideMask();		
			
			if(typeof(callbacks) === "function") {
				callbacks();
			}
			else {				
				Dituhui.showHint("当前查询到0条区划数据");
			}
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
		Dituhui.showPopover("当前查询到0条区划数据");
		return;
	}
	var fs = [], ls = [], dcode = Dituhui.User.dcode;
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
		attr.branch_name = Dituhui.Zone.getBranchName(record.pointnames);
		
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

		var geometry = Dituhui.Zone.DrawRegion(parts, point2Ds, 1);
		
		var style = Dituhui.Zone.getRegionStyle(record.name);
		/**
		 * 不是自己的区划
		 */
		record.style_status = record.area_status;
		if(attr.dcode.substr(0, dcode.length) !== dcode) {
			style = Dituhui.Zone.getNonEditableRegionStyle(record.name);
			record.style_status = 2.5;
		}
		
		attr.oldStyle = style;
		var feature = new SuperMap.Feature.Vector( geometry, attr, style );		
		fs.push(feature);

		var geoText = new SuperMap.Geometry.GeoText(record.center.x, record.center.y, record.name);
		var geotextFeature = new SuperMap.Feature.Vector(geoText);
//		geotextFeature.style = Dituhui.Zone.getNonEditableRegionTextStyle(record.area_status);
		
		var attr_label = attr;
		attr_label.type = "text-region";
		geotextFeature.attributes = attr_label;
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
	Dituhui.Order.Table.clear();
	layer_orders.removeAllFeatures();
	
	layer_cloudpois.removeAllFeatures();
    $('.header .search-from-cloud .child').addClass("hide");
	
	Dituhui.Zone.Table.clear();
	Measure.control.deactivate();
	layer_edit.removeAllFeatures();
	
	control_drawPolygon.deactivate();	
	Map.regionAdding = false;
	control_editPolygon.deactivate();
	control_drawSplitLine.deactivate();
	Regionroute.cancel();
	control_drawSplitArea.deactivate();	
	Map.enableDrag();
	Measure.clear();
}

/**
 * 平移
 */
Map.pan = function() {	
	map.removeAllPopup();
	Map.popup = null;
	Measure.control.deactivate();
	control_drawPolygon.deactivate();
	Map.regionAdding = false;
	control_editPolygon.deactivate();
	Map.enableDrag();
	control_drawSplitArea.deactivate();	
	control_drawSplitLine.deactivate();
	layer_edit.removeAllFeatures();
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
Map.getFeatureFromLayerById = function(id, features) {
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
	var flag = Map.checkAction();
	if(!flag) {
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
	control_select.deactivate();
	control_drawPolygon.activate();
	Map.disableDrag();
	Map.regionAdding = true;
	//可以使用方向键
	Map.setMapIndexTop();
}

/**
 * 自由画区完成
 */
Map.regionAdding = false;
Map.drawRegionCompleted = function(e) {
	control_drawPolygon.deactivate();
	Map.enableDrag();
	e.feature.style = Dituhui.Zone.getRegionStyle();
	layer_edit.redraw();
	
	var center = e.feature.geometry.getCentroid();
	var lonlat = new SuperMap.LonLat( center.x, center.y );
	Map.openAddRegionPopup( lonlat );
}

/**
 * 自由画区完成后弹出信息窗
 */
Map.openAddRegionPopup = function(lonlat) {
	var h = Dituhui.Zone.getEditRegionHtml(0);
	Map.popup = new SuperMap.Popup.FramedCloud("popup-region",
		lonlat,
		new SuperMap.Size(300, 200),
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
	
	$('#txt_number').unbind('keyup').keyup(function(e){
		if(e.keyCode == 13) {
			Map.getBranchName();
		}
	});
	$('.popup-close').unbind('click').on('click', function(){
		Dituhui.Modal.ask('确定不保存吗？', Map.cancelAddRegion);
	});
	$('.popup-sure').unbind('click').on('click', Map.addRegion);
	Dituhui.Zone.initRegionPopupAdmincode(null);
}

/*
 * 根据区划编号查询网点名称
 */
Map.getBranchName = function() {
	var number = $('#txt_number').val();
	if(number === "" || number.length < 1) 
	{
		Dituhui.showHint("请输入区划编号");
		return;
	}
	if(number.length > 50) 
	{
		hint.text = "区划编号长度不能大于50位";
		return;
	}	
	
	var name = $('#txt_name').val();
	if(name != "" || name.length > 0) 
	{
//		Map.addRegion();
		return;
	}
	
	Dituhui.Zone.getBranchNameByAreaNumber(number, function(e){
		if( e && e.isSuccess && e.result && e.result.pointname) 
		{
			var pnames = "", oldpointnames = e.result.oldpointnames;
			var branchName = e.result.pointname;
			var branchId = e.result.pointid;
			if(  oldpointnames && oldpointnames.length > 0 ) {
				var len = oldpointnames.length;
				if(len > 0) {
					for(var k=0; k<len; k++) {
						pnames += oldpointnames[k];
						if(k < (len-1)) {
							pnames += ",";
						}
					}
				}
			}
			if(pnames != "") {
				Dituhui.Modal.alert('该区划已绑定网点\"'+ pnames +'\"，确定要解除吗？', 
					function(){
						$('#txt_name').val(branchName).attr('poi-id', branchId);
					},
					null
				);	
			}
			else {
				$('#txt_name').val(branchName).attr('poi-id', branchId);
			}
		}
		else {
			var info = e && e.info ? e.info : "获取网点名称失败";
			Dituhui.showHint(info);
		}
	}, function(info){
		var info = info ? info : "获取网点名称失败";
		Dituhui.showHint(info);
	});
}

/*
 * 添加区划
 */
Map.addRegion = function() {
	var feature = layer_edit.features[0];
	
	var name = $('#txt_name').val();
	if(name === "" || name.length < 1)  {
		Dituhui.showHint("请输入区划名称");
		return;
	}
	if(name.length > 50) {
		Dituhui.showHint("区划名称长度不能大于50位");
		return;
	}
	
	var number = $('#txt_number').val();
	if(number === "" || number.length < 1) {
		Dituhui.showHint("请输入区划编号");
		return;
	}
	if(number.length > 50) {
		Dituhui.showHint("区划编号长度不能大于50位");
		return;
	}
	
	var select_province = $("#select_region_province").val();
	var select_city = $("#select_region_city").val();
	var select_county = $("#select_region_county").val();
	var select_town = (Dituhui.User.hasTownAuthority ? $("#select_region_town").val() : "-1" );
	
	var code = "";
	if(Dituhui.User.hasTownAuthority && select_town && select_town != "-1"){
		code = select_town;
	}
	else if(select_county && select_county != "-1") {
		code = select_county;
	}
	else if(select_city && select_city != "-1") {
		code = select_city;
	}
	else if(select_province && select_province != "-1") {
		code = select_province;
	}
	else {
		Dituhui.showHint("请选择区划归属");
		return;
	}
	
	var pointid = $('#txt_name').attr("poi-id");
	var param = {
		areaName: name,
		areaNum: number,
		point2Ds: Dituhui.Zone.getPoint2DsFromRegion(feature, "region", Baidu.using),
		parts: Dituhui.Zone.getPartsFromRegion(feature),
		admincode: code,
		pointid: pointid
	}
	if(Dituhui.User.regionTableExtend) {
		param.wgzCode = $("#txt_wgzCode").val();
		param.wgzName = $("#txt_wgzName").val();
		param.lineCode = $("#txt_lineCode").val();
		param.lineName = $("#txt_lineName").val();
	}
	Dituhui.Zone.add(
		param,
		function(){
			Dituhui.showPopover("区划添加成功");
			map.removeAllPopup();
			layer_edit.removeAllFeatures();
			Map.search();
			control_select.activate();
			Map.regionAdding = false;
			Map.clearSelectedFeatures();
		},
		function(info){
			control_select.activate();
			Map.regionAdding = true;
			if(info && info !== "") {
				Dituhui.Modal.loaded_wrong(info, function(){
					$('.data-modal').addClass("hide");
				});
			}
			else {
				Dituhui.showHint("新增区划失败，请稍候重试");					
			}
		}
	)
}

/*
 * 取消添加区划
 */
Map.cancelAddRegion = function() {
	map.removeAllPopup();
	layer_edit.removeAllFeatures();
	layer_region_temp.removeAllFeatures();
	layer_region_label_temp.removeAllFeatures();
	$('.data-modal').addClass('hide');
	Map.regionAdding = false;
	control_select.activate();
	relatedSelect.deactivate(true);
}


/**
 * 点击工具栏中的属性
 */
Map.attrRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	if( selectedFeature == null || selectedFeatures.length != 1 ) {
		Dituhui.showHint("请先选择一个区划");
		return;
	}
	var center = selectedFeature.geometry.getCentroid();
	map.panTo(new SuperMap.LonLat(center.x, center.y));
	Map.openRegionAttrPopup(selectedFeature);
}


/**
 * 点击工具栏中的删除
 */
Map.deleteRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	if( selectedFeature == null || selectedFeatures.length != 1 ) {
		Dituhui.showHint("请选择待删除的区划");
		return;		
	}
	var attr = selectedFeature.attributes;
	$('button[option="delete-region"]').attr({
		'data-id': attr.id,
		'data-name': attr.name
	});
	$('.delete-region-name').html( attr.name );
	Dituhui.Modal.alert(
		'是否删除区划 <strong>'+ attr.name +'</strong> ? 删除后不可恢复。',
		Map.deleteRegion,
		{"data-id": attr.id, "data-name": attr.name}
	);	
}

/**
 * 删除区划
 */
Map.deleteRegion = function() {
	var me = $(this);
	Dituhui.Zone.remove(
		me.attr('data-id'),
		function() {
			$(".data-modal").addClass('hide');
			Dituhui.showPopover("区划\""+ me.attr('data-name') +"\"删除成功");
			Map.search();
			map.removeAllPopup();
			Map.clearSelectedFeatures();
		},
		function(info) {
			$(".data-modal").addClass('hide');
			if(info == "") {
				info = "区划\"" + me.attr('data-name') + "\"删除失败";
			}
			Dituhui.Modal.loaded_wrong(info, function(){
            	Dituhui.Modal.hide();
            });
		}
	);
}


/**
 * 点击工具栏中的合并
 */
Map.mergeRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	if( selectedFeatures.length < 2 ) {
		Dituhui.showHint("请先选择至少两个区划, 按住ctrl可同时多选区划");
		return;		
	}
	map.removeAllPopup();
	
	var fs = selectedFeatures.concat();
	var name_first = selectedFeatures[0].attributes.name;
	var ids = "";
	for(var i=0,len=fs.length; i<len; i++) {
		ids += fs[i].attributes.id + (i<len-1 ? "_" : "");
	}
	Dituhui.Modal.ask('是否合并所选区划？合并后区划属性为“'+ name_first +'”。', 
		Map.mergeRegions,
		{"data-id": ids}
	);
}

/*
 * 合并区划
 */
Map.mergeRegions = function(attr) {
	Dituhui.Zone.merge(attr['data-id'],
		function() {
			$(".data-modal").addClass('hide');
			Dituhui.showPopover("区划合并成功");
			Map.search();
			map.removeAllPopup();
			Map.clearSelectedFeatures();
		},
		function(info) {
			$(".data-modal").addClass('hide');
			info = info == "" ? "区划合并失败" : info;			
			Dituhui.showHint(info);		
			Map.clearSelectedFeatures();
		}	
	);
}

/**
 * 点击工具栏中的线拆分
 */
Map.lineSplitRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	if( selectedFeature == null || selectedFeatures.length != 1 ) {
		Dituhui.showHint("请选择待拆分的区划");
		return;
	}
	Dituhui.showHint("请绘制与选中区划相交叉的线");
	map.removeAllPopup();
	control_drawSplitLine.activate();	
	//可以使用方向键
	Map.setMapIndexTop();
}

/**
 * 线拆分, 画线完成
 */
Map.drawSplitLineCompleted = function(e) {
	control_drawSplitLine.deactivate();
	var f = e.feature.clone();
	f.style = Dituhui.Zone.getSplitLineStyle();
	
	layer_edit.removeAllFeatures();
	layer_edit.addFeatures([f]);
	
	Dituhui.Modal.ask('确定要拆分该区划吗？', 
		Map.lineSplitRegion,
		{},
		function(){
			layer_edit.removeAllFeatures();
		}
	);
}
/**
 * 线拆分
 */
Map.lineSplitRegion = function() {
	var f = layer_edit.features[0];
	var param = {
		id: selectedFeature.attributes.id,
		point2Ds: Dituhui.Zone.getPoint2DsFromRegion(f, "line", Baidu.using)
	}
	Dituhui.Zone.lineSplit(param, function(){
		$(".data-modal").addClass('hide');
		Dituhui.showPopover("区划线拆分成功");
		Map.search();
		map.removeAllPopup();
		layer_edit.removeAllFeatures();
		Map.clearSelectedFeatures();
	}, function(info){
		$(".data-modal").addClass('hide');
		layer_edit.removeAllFeatures();
		info = info == "" ? "区划线拆分删除失败" : info;			
		Dituhui.showHint(info);			
		Map.clearSelectedFeatures();
	});
}

/**
 * 点击工具栏中的面拆分
 */
Map.areaSplitRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	if( selectedFeature == null || selectedFeatures.length != 1 ) {
		Dituhui.showHint("请选择待拆分的区划");
		return;		
	}
	Dituhui.showHint("请绘制与选中区划相交叉的面");
	map.removeAllPopup();
	control_drawSplitArea.activate();	
	//可以使用方向键
	Map.setMapIndexTop();
}

/**
 * 面拆分，画面完成
 */
Map.drawSplitAreaCompleted = function(e) {
	control_drawSplitArea.deactivate();	
	var f = e.feature.clone();
	f.style = Dituhui.Zone.getSplitAreaStyle();
	
	layer_edit.removeAllFeatures();
	layer_edit.addFeatures([f]);
	
	Dituhui.Modal.ask('确定要拆分该区划吗？', 
		Map.areaSplitRegion,
		{},
		function(){
			layer_edit.removeAllFeatures();
		}
	);
}

/**
 * 面拆分
 */
Map.areaSplitRegion = function() {
	var f = layer_edit.features[0];
	var param = {
		id: selectedFeature.attributes.id,
		point2Ds: Dituhui.Zone.getPoint2DsFromRegion(f, "region", Baidu.using),
		operType: 2
	}
	Dituhui.Zone.lineSplit(param, function(){
		$(".data-modal").addClass('hide');
		Dituhui.showPopover("区划面拆分成功");
		Map.search();
		map.removeAllPopup();
		layer_edit.removeAllFeatures();
		Map.clearSelectedFeatures();
	}, function(info){
		$(".data-modal").addClass('hide');
		info = info == "" ? "区划面拆分失败" : info;			
		Dituhui.showHint(info);		
		layer_edit.removeAllFeatures();
		Map.clearSelectedFeatures();
	});
}

/**
 * 右键平移地图
 */
Map.setPan = function(){
	
}

/**
 * 检查当前操作
 */
Map.checkAction = function() {
	if( control_drawPolygon.active || Map.regionAdding ) {
		Dituhui.showHint("正在进行自由画区操作");
		return false;
	}
	if( control_editPolygon.active ) {
		Dituhui.showHint("正在编辑区划\""+ selectedFeature.attributes.name +"\"的节点");
		return false;
	}
	if( control_drawPathRegion.active ) {
		Dituhui.showHint("正在进行沿路画区操作");
		return false;
	}
	if( control_drawSplitLine.active ) {
		Dituhui.showHint("正在对区划\""+ selectedFeature.attributes.name +"\"进行线拆分操作");
		return false;
	}
	if( control_drawSplitArea.active ) {
		Dituhui.showHint("正在对区划\""+ selectedFeature.attributes.name +"\"进行面拆分操作");
		return false;
	}
	return true;
}

/*
 * 点击导航栏中的导入按钮
 */
Map.regionImportClick = function() {
	$('.select-region-import').click();	
}

/*
 * 导入区划
 */
Map.regionImport = function() {
	var options = {    
       success: function(e) { 
       		Dituhui.hideMask();			
	        if(e.isSuccess) {
				Dituhui.showPopover("导入区划边界成功");
				Map.search();
	        }
	        else {
	            Dituhui.Modal.loaded_wrong(e.info, function(){
	            	Dituhui.Modal.hide();
	            });
	        }
    	},
    	error: function() {
    		Dituhui.hideMask();
    	},
    	timeout: 30000
    };   
	Dituhui.showMask();	
	$("#form_import_region").ajaxSubmit(options);	
}

/*
 * 导出区划
 */
Map.regionExport = function() {
	if(selectedFeatures == null || selectedFeatures.length < 1) {
		Dituhui.showHint("请先选择需要导出的区划");
		return;
	}
	var length = selectedFeatures.length, ids="";
	for(var i=length; i--; ) {
		var f = selectedFeatures[i];
		
		ids += f.attributes.id;
		if(i != 0) {
			ids += "_";
		}
	}
	var t = new Date().getTime() + "";

	window.open(urls.server + "/areaService/export?" + "&t="+ t + "&ids=" + ids, "_blank");
}

/**
 * 绑定键盘方向键
 */
Map.bindKeyPan = function(){
	$("#map").keyup(function(e){
		var e = e||event, panOffset = 150;
		
		switch(e.keyCode) {
			//左键
			case 37:
				map.pan(-panOffset, 0);
				break;
			//上键
			case 38:
				map.pan(0, -panOffset);
				break;
			//右键
			case 39:
				map.pan(panOffset, 0);
				break;
			//下键
			case 40:
				map.pan(0, panOffset);
				break;
		}
	}).on("click", function(){
		Map.setMapIndexTop();
	});	
}

/**
 * 
 */
Map.setMapIndexTop = function() {
	$("#map").attr("tabindex",0).focus();
}

/**
 * 更新子账号属性
 */
Map.setUserBelonging = function(){
	if(!selectedFeature) {
		Dituhui.showHint("请先选择一个区划");
		return;
	}
	var areaid = selectedFeature.attributes.id;
	var tree = $.fn.zTree.getZTreeObj("tree_usebelonging");
	var node = tree.getSelectedNodes(true)[0];
	var userid = node.id; 
	Dituhui.Zone.setUserBelonging(areaid, userid, 
		function(){
			Dituhui.showPopover("更新区划所属用户成功");
			$(".data-user-belonging").fadeOut("slow");
			Map.searchRegions();
		},
		function(info){
			Dituhui.showHint(info ? info : "更新区划所属用户失败");
		}
	);
}

/**
 * 点击行政区划，首先检查区划个数
 */
Map.checkRegionsCount = function() {
	var smcity = $(".smcity");
	var admincode = smcity.attr("admincode");
	var level = smcity.attr("level");	
	if(level == 0 ) {
		Dituhui.showHint("请选择省、市、区" + (Dituhui.User.hasTownAuthority ? "或乡镇" : "" ));
		return;
	}
	$('#btn_showRegion').prop('checked', true).attr('isShowRegion', 'true');
	layer_region.setVisibility(true);
	layer_region_label.setVisibility(true);
	if(layer_region.features.length == 0) {
		$('.data-list .search-input').val('');
		Map.searchRegions(Map.saveReverseSelectionArea);
		return;
	}	
	Map.saveReverseSelectionArea();
}

/*
 * 反选行政区划面
 */
Map.saveReverseSelectionArea = function() {		
	var smcity = $(".smcity");
	var admincode = smcity.attr("admincode");
	var level = smcity.attr("level");
	
	currentAdminame = smcity.attr("adminname");
	var cityname = $(".smcity-title a.city:not(.hide)").attr("data-value");
	if(typeof cityname !== "undefined") {
		cityname += "/";
	}
	else {
		cityname = "";
	}
	var adminame = currentAdminame;
	switch(level) {
		case "2": 
			adminame = $(".smcity-title a.province").attr("data-value") + "/" + adminame;
			break;
		case "3":			
			adminame = $(".smcity-title a.province").attr("data-value") + "/"
					 + cityname + adminame;
			break;
		case "4":
			adminame = $(".smcity-title a.province").attr("data-value") + "/"
					 + cityname
					 + $(".smcity-title a.county").attr("data-value") + "/"
					 + adminame;
			break;
	}
	var regionsCount = Number($('.data-list .totality').attr("data-value"));
	var alertText = "当前为"+ currentAdminame + (level != 1 ? "（"+ adminame +"）" : "")
					+"，已有区划"+ regionsCount +"个，确定将空白区域按照红色行政边界转化为业务区划？";
	
	Dituhui.Modal.ask(alertText, function(){
		Dituhui.Modal.hide();
		Dituhui.showMask();
		Dituhui.Zone.saveReverseSelectionArea(admincode, level, 
			function(id) {
				Map.searchRegions(function(){
					Map.autoSelectFeature(id);
					Dituhui.Modal.loaded_right("区划已经生成，请填写区划名称和编号", function(){
						Dituhui.Modal.hide();
					});
				});
			},
			function(info) {
				Dituhui.Modal.loaded_wrong(info || "区划生成失败，请重新操作", 
					Dituhui.Modal.hide
				);
			}
		)
	});	
}

/**
 * 根据ID自动选择区划
 * @param {String} featureid 区划id
 * @param {Boolean} isEditAttr 是否激活属性编辑
 */
Map.autoSelectFeature = function(featureid, isEditAttr) {
	var feature = Map.getFeatureFromLayerById( featureid, layer_region.features);
	
	feature.attributes.oldStyle = feature.style;
	feature.style = Dituhui.Zone.getRegionSelectStyle();
	selectedFeature = feature;
	
	selectedFeatures.push(selectedFeature);
	
	Dituhui.Zone.Table.scrollToFeature(feature);
	
	var attr = feature.attributes;
	attr.lonlat = new SuperMap.LonLat(attr.center.x, attr.center.y);
	Map.openEditRegionPopup(attr, true);
	
	if(layer_region.visibility == false) {
		var record = selectedFeature.attributes;
		var geoText = new SuperMap.Geometry.GeoText(record.center.x, record.center.y, record.name);
		var geotextFeature = new SuperMap.Feature.Vector(geoText);
		geotextFeature.style = Dituhui.Zone.getRegionTextStyle();
		layer_region_temp.addFeatures([selectedFeature.clone()]);
		layer_region_label_temp.addFeatures([geotextFeature]);
	}
	layer_region.redraw();
}





