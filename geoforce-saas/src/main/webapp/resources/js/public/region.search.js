
/*
 * 区划查询
 */
var Region = {
	/*
	 * 初始化
	 */
	init: function(){
		layer_region = new SuperMap.Layer.Vector("region", {renderers:["Canvas2"]});
		var strategy = new SuperMap.Strategy.GeoText();
		strategy.groupField = "style_status";
		strategy.styleGroups = Dituhui.Zone.getRegionTextStyleGroups();
		layer_region_label = new SuperMap.Layer.Vector("Label", {renderers:["SVG"], strategies: [strategy]});
		map.addLayers([ layer_region,  layer_region_label]);
		
		layer_region.setVisibility(false);
		layer_region_label.setVisibility(false);
	},
	/*
	 * 选择事件
	 */
	select: function(feature) {
		if(feature.attributes.style_status === 2.5) {
			//不是自己的区划不能点击
			return;
		}
		selectedFeature = feature;
		$('#txt_areaName').val(feature.attributes.name);
		$('#txt_areaId').val(feature.attributes.id);		
		feature.style = Dituhui.Zone.getRegionSelectStyle( feature.attributes.name );	
		layer_region.redraw();
	},
	/*
	 * 取消选择
	 */
	unselect: function(feature) {
		feature.style = feature.attributes.oldStyle;
		layer_region.redraw();
		selectedFeature = null;
		$('#txt_areaName, #txt_areaId').val('');
	},
	/*
	 * 显示区划
	 */
	show: function() {
		var me = $(this);
		var bool = me.prop('checked');
		if( bool ) {
			var code = $('.smcity').attr("admincode");
			if( !code || code === "" ) {
				Dituhui.showHint("如果查看区划，请选择省市");
				me.prop("checked", false);
				return;
			}
			var zoom = map.getZoom(), zoom_min = typeof(Baidu) != "undefined" && Baidu.using ? 3 : 5;
			if(zoom < zoom_min) {
				Dituhui.showHint("请将地图放大到"+ zoom_min +"级以上再显示区划");
				me.prop("checked", false);
				return;
			}
			Region.search();
			me.attr("isShowRegion", "true");
		}
		else {
			me.removeAttr("isShowRegion");
		}
		layer_region.setVisibility(bool);
		layer_region_label.setVisibility(bool);
	},
	/*
	 * 查询区划
	 */
	search: function() {
		layer_region.removeAllFeatures();
		layer_region_label.removeAllFeatures();
		var smcity = $('.smcity');
		var admincode = smcity.attr('admincode');
		var level = smcity.attr('level');
		if( admincode === "" ) {
			Dituhui.showPopover("查询区划请选择省");
			return;
		}
		Dituhui.showMask();
		var param = {
			admincode: admincode,
			level: level
		}
	
		Dituhui.Zone.search(param, 
			function(data){
				Dituhui.hideMask();
				Region.display(data);
			},
			function(error){
				Dituhui.hideMask();
				Dituhui.showPopover("当前查询到0条区划数据");
				layer_region.removeAllFeatures();
				layer_region_label.removeAllFeatures();
			}
		)
	},
	/*
	 * 将区划绘制到地图上
	 */
	display: function(data) {
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
	
	        if( typeof(Baidu) != "undefined" && Baidu && Baidu.using) {
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
//			geotextFeature.style = Dituhui.Zone.getRegionTextStyle();
			var attr_label = attr;
			attr_label.type = "text-region";
			geotextFeature.attributes = attr_label;
			ls.push(geotextFeature);
		}
		layer_region.addFeatures(fs);	
		layer_region_label.addFeatures(ls);	
	}
};

Map.searchRegions = function() {
	Region.search();
}
