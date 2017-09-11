/*
 * 测距工具
 */
var Measure = {
	control: null,
	layer: null
};

/*
 * 初始化
 */
Measure.init = function() {
	Measure.control = new SuperMap.Control.Measure(
		SuperMap.Handler.Path, {persist:true, immediate: true}
	);
	Measure.control.events.on({
		"measure": Measure.handleMeasure,
		"measurepartial": Measure.handleMeasurements,
		"click": Measure.measureLineSegment
	});
	Measure.control.style = {fillColor: "#CC3333",strokeColor: "#CC3333",pointRadius:6};
	map.addControl(Measure.control);
	
	Measure.layer = new SuperMap.Layer.Vector("layer_measureFeatures");
	map.addLayer(Measure.layer, 2);
	
	$('.tool-button.measure').on('click', function(){
		var flag = Map.checkAction();
		if(!flag) {
			return;
		}
		Measure.control.activate();
	});
}

/*
 * 开始测距
 */
Measure.active = function() {
	Measure.control.activate();
}

/**
 * 清除测量结果
 */
Measure.clear = function() {
	Measure.layer.removeAllFeatures();
	Measure.index = 0;
}

/**
 * 清除单个测量结果
 */
Measure.clearMeasure = function(e) {
	var attr = e.attributes;
	var fs = Measure.layer.features, rfs=[];
	for(var i = fs.length; i--; ) {
		var f = fs[i];
		if(f.attributes.id === attr.id) {			
			rfs.push(f);
		}
	}
	Measure.layer.removeFeatures(rfs);
}

/*
 * 当前测距矢量元素的索引
 */
Measure.index = 0;
/**
 * 测距完成
 */
Measure.handleMeasure = function(e) {
	Measure.control.deactivate();
	
	Measure.index++;
	
	var style = {
		strokeColor:"#CC3333",
		strokeOpacity:1,
		strokeWidth:3,
		pointRadius:6
	}
	var geometry = e.geometry;
	var f = new SuperMap.Feature.Vector(geometry, null, style);	
	f.attributes = {
		id: "measure-line-" + Measure.index
	}
	Measure.layer.addFeatures(f);
	
	var start,end;
	
	var length_componets = geometry.components.length;
	var distance1 = 0;
	for(var k = 0; k < length_componets; k++){
		var comp = geometry.components[k];
		var point = new SuperMap.Geometry.Point(comp.x, comp.y);
		
		var pointFeature = new SuperMap.Feature.Vector(point, {
			id: "measure-line-" + Measure.index
		});
		
		pointFeature.style = {
			fillColor: "#fffff",
			strokeColor: "#FF0000",
			pointRadius:5,
			strokeOpacity:0.5,
			fillOpacity:0.5
		};
		Measure.layer.addFeatures(pointFeature);
		
		if(k == 0){
			start = point;
		}
		else if(k == ( length_componets -1)){
			end = point;
		}
	}

	var units;
	if(e.units == "km") {
		units = "公里";
	}
	else {
		units = "米";
	}
	
    var distance = e.measure.toFixed(3);
	var distanceinfo = "总长：<font color=red>" + distance + "</font>" + units;

    var startFeature = new SuperMap.Feature.Vector(
    	new SuperMap.Geometry.Point(start.x,start.y),
    	{
    		type: "measure",
    		id: "measure-line-" + Measure.index
    	},
    	{
    		externalGraphic: urls.server + "/resources/images/measure/begin.gif",
    		graphicWidth: 34,
    		graphicHeight: 19,
    		graphicXOffset: -17,
    		graphicYOffset: -25
    	}
    );
    
    var endFeature = new SuperMap.Feature.Vector(
    	new SuperMap.Geometry.Point(end.x, end.y),
    	{
    		type: "clear-measure",
    		id: "measure-line-" + Measure.index
    	},
    	{
    		externalGraphic: urls.server + "/resources/images/measure/close1.gif",
    		graphicWidth: 12,
    		graphicHeight: 12,
    		graphicXOffset: -15,
    		graphicYOffset: -15,
    		graphicTitle: "清除本次测量结果",
    		cursor: "pointer"
    	}
    );
    
    var lblFeature = new SuperMap.Feature.Vector(
    	new SuperMap.Geometry.Point(end.x, end.y),
    	{
    		type: "measure",
    		id: "measure-line-" + Measure.index
    	},
    	{
    		externalGraphic: urls.server + "/resources/images/measure/back.png",
    		graphicWidth: 106,
    		graphicHeight: 20,
    		graphicXOffset: 6,
    		graphicYOffset: -10,
    		cursor: "pointer",
    		label: "总长：" + distance + units,
    		fontColor: "black",
    		fontFamily: "microsoft yahei",
    		fontWeight: "normal",
    		labelXOffset: 56
    	}
    );
    
    Measure.layer.addFeatures([startFeature, endFeature, lblFeature]);
    
    /*var icon_close = new SuperMap.Icon('images/close1.gif', new SuperMap.Size(12, 12), new SuperMap.Pixel(-20, -5) );
    var marker_close = new SuperMap.Marker(new SuperMap.LonLat(end.x, end.y), icon_close);
    marker_close.id = "" + index;
    marker_close.events.element.title = "";
    marker_close.events.element.style.cursor = "pointer";
    marker_close.events.on({
	   "click": Map.clearMeasure
	});
    layer_measureMarkers.addMarker(marker_close);

    var icon_measure = new SuperMap.Icon("", new SuperMap.Size(106, 20), new SuperMap.Pixel(15, -15) );
    var marker_measure = new SuperMap.Marker(new SuperMap.LonLat(end.x, end.y), icon_measure);
    var content = '<div class="measureResult">' + distanceinfo + '</div>';
    marker_measure.events.element.innerHTML = content;
    layer_measureMarkers.addMarker(marker_measure);*/
}















