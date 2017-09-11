var map;//地图对象
var layer_path;
var layer_orders_vector;//订单和网点图层
var layer_measure_vector;//测距图层
var drawLine;//划线控件
var layer_orders;
var layer_branches;
var layer_region;
function initMap(){
	initcss();
	map = new SuperMap.Map("map", { controls: [
//			new SuperMap.Control.PanZoomBar({showSlider:true}),
		new SuperMap.Control.Navigation({
		    dragPanOptions: {
		        enableKinetic: true
		    }
		})]
	});
	
	Map.init();
	
	layer_path = new SuperMap.Layer.Vector('path');
	var measure_style = {
            strokeColor: "#304DBE",
            strokeWidth: 2,
            pointerEvents: "visiblePainted",
            fillColor: "#304DBE",
            fillOpacity: 0.8
    }
	layer_measure_vector = new SuperMap.Layer.Vector('measure_vector');
	layer_measure_vector.style = measure_style;
	layer_orders_vector = new SuperMap.Layer.Vector('orders_vector');
	layer_region = new SuperMap.Layer.Vector('region');	
	/*layer_orders_vector = new SuperMap.Layer.Vector('orders_vector', {renderers: ["Canvas"]});
	layer_region = new SuperMap.Layer.Vector('region', {renderers: ["Canvas"]});*/	
	layer_region.setVisibility(false);
	layer_orders = new SuperMap.Layer.Markers("orders");
	layer_branches = new SuperMap.Layer.Markers("branches");
	//实例化 selectFeature 控件，调用了 onSelect 和 onUnselect 方法
    //地物被选中时调用 onSelect 方法，地物被取消选中时调用 onUnselect 方法
    
    var callbacks = {
	    click: showFeature
	};
    var selectFeature = new SuperMap.Control.SelectFeature(layer_orders_vector, {callbacks: callbacks});
    //map上添加控件
    map.addControl(selectFeature);
    //激活控件
    selectFeature.activate();
  	//创建画线控制，图层是lineLayer;这里DrawFeature(图层,类型,属性)；multi:true在将要素放入图层之前是否现将其放入几何图层中
    drawLine = new SuperMap.Control.Measure(SuperMap.Handler.Path, {persist:false});
    /*
    注册featureadded事件,触发drawCompleted()方法
    例如注册"loadstart"事件的单独监听
    events.on({ "loadstart": loadStartListener });
    */
    drawLine.events.on({"measure": handleMeasure});
    //map上添加控件
    map.addControl(drawLine);
    
	map.addLayers([layer_measure_vector,layer_region, layer_path, layer_orders,layer_orders_vector, layer_branches ]);
	
	//默认北京
	map.setCenter(new SuperMap.LonLat(12958399.4681885, 4852082.44060595),11);
	var ovMap = new SuperMap.Control.OverviewMap({id:"overviewMap", maximized: false});
	var layer_ovmap = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	ovMap.layers = [layer_ovmap];
	map.addControl(ovMap);
	
	refreshMap();
}	
$(function(){
	initMap();
	$(window).resize(initcss);
	//初始化城市选择
	initSMapSelWidget();
	
});
function distanceMeasure(){
	clearMeasureFeatures();
    drawLine.activate();
}

//定义 handleMeasurements 函数，触发 measure 事件会调用此函数
//事件参数 event 包含了测量要素 geometry 信息
function handleMeasure(event) {
  //获取传入参数 event 的 geometry 信息
  var geometry = event.geometry;
  var line=new SuperMap.Geometry.LineString(geometry.components);
  var pathF = new SuperMap.Feature.Vector(line, null, null);
  
  var pointStyle = {
	    strokeWidth:0,
	    pointRadius:0,
	    graphicName:'circle',
	    fontColor:'#FF0000',
	    label:(event.measure.toFixed(0)+(event.units=='m'?'米':'千米')),
	    cursor: 'pointer'
  }
  var pointVector = new SuperMap.Feature.Vector(geometry.components[geometry.components.length-1],null, pointStyle);
  layer_measure_vector.addFeatures(pathF);
  layer_measure_vector.addFeatures(pointVector);
}


//退出测距
function deactivateMeasure(){
	drawLine.deactivate();
}
//移除图层要素
function clearMeasureFeatures(){
	layer_measure_vector.removeAllFeatures();
}
/**
 * 初始化地区选择控件
 * @author: mwang
 */
var divisionCode = '110000';
var parentCode = '';
var cityName = "北京市";
function initSMapSelWidget(){
	if (typeof(currentCityInfo)!="undefined"&&currentCityInfo!=null&&typeof(currentCityInfo.cityName)!="") {
		cityName = currentCityInfo.cityName;
	}
	var smapCity = new SMap.CitySelect({
		id:"selectCityDiv",//页面div
		defaultData:{text:cityName,value:"100100"}//默认显示的数据
	});
	
	smapCity.setAfterSelect(function(data){
		cityName = data.text;
		clearMap();
		jQuery("#span_searchRegion").html(data.text);
		divisionCode = data.value;
		parentCode = data.parentCode;
		selCode = divisionCode;
		selLevel = data.level;
		selCode = divisionCode;
		
		var level;
		if(data.level == 1){
			level = data.zoomLevel;
		}
		else if (data.level == 2) {
			level = data.zoomLevel;
		}else if (data.level == -1) {
			level = 4;
		}else{
			level = 6;
		}
		map.setCenter(new SuperMap.LonLat(parseFloat(data.x),parseFloat(data.y)),level );
	});
}

function initcss() {
	//var gridHeight = $("#gbox_grid-table").height();
	var mapHeight = getWindowHeight() - 60;
	$(".container-pathplan").height(mapHeight);	
	refreshMap();
	var w = getWindowWidth() - 146;
//	$("#topToolbarDiv").width(w);	
	$("#pathResultWindowBodyMain").height(mapHeight-45-370);
}

/**
 * 墨卡托投影坐标转换为经纬度坐标
 */
function meterXY2GeoLoc(x, y, precision){
	var earthCircumferenceInMeters = new Number(40075016.685578488);
	var halfEarthCircumferenceInMeters = earthCircumferenceInMeters / 2;
	
	var geoX = x/halfEarthCircumferenceInMeters*180;
	var geoY = y/halfEarthCircumferenceInMeters*180;
	geoY = Math.atan(Math.exp(geoY * (Math.PI / 180.0)))*360.0/Math.PI - 90;
	
	geoX = setPrecision(geoX, precision);
	geoY = setPrecision(geoY, precision);
	
	var obj = new Object();
	obj.lngX = geoX;
	obj.latY = geoY;
	return obj;
}
//按输入精度保留经纬度
function setPrecision(num, precision){
	var temp = new String(num);
	
	var pos = temp.indexOf(".");
	if (temp.length > (pos + precision)){
		var num = Number(temp).toFixed(precision);
		return num;
	}
	
	return temp;
}
/**
 * 经纬度坐标转墨卡托投影坐标
 */
function geoLoc2MeterXY(x, y){
	var earthCircumferenceInMeters = new Number(40075016.685578488);
	var halfEarthCircumferenceInMeters = earthCircumferenceInMeters / 2;
	
	var geoX = new Number(x);
	var geoY = new Number(y);
	
	var mx = geoX / 180.0 * halfEarthCircumferenceInMeters;
	var my = Math.log(Math.tan((90 + geoY) * Math.PI / 360.0)) / (Math.PI / 180.0);
	my = my / 180.0 * halfEarthCircumferenceInMeters;
	
	var obj = new Object();
	obj.lngX = mx;
	obj.latY = my;
	return obj;
}
/**
 * 通过缩放刷新地图
 * @return
 */
function refreshMap(){
	if(map)
		map.updateSize();
}


/**
 * 打开结果提示
 */
var timer_popup = null;
function showPopover(string) {   
    $("#popover_content").html(string);
    $("#popover_result").css("display", "block");

    if(timer_popup){
        clearTimeout(timer_popup);
    }      
    timer_popup = setTimeout("hidePopover();", 3500);
}

/**
 * 隐藏结果提示
 */
function hidePopover() {
    // $("#table_keys").popover("hide");
    if(timer_popup){
        clearTimeout(timer_popup);
    }       
    $("#popover_result").css("display", "none");
}
/**
 * 显示网点对话框
 */
function showNetPopDiv(){
	$("#topToolbarPathDiv_net").show();
	$("#topToolbarPathDiv_orders").hide();
	$("#topToolbarPathDiv_setting").hide();
	$("#topToolbarPathDiv_cars").hide();
	clearMap();
}
/**
 * 显示订单对话框
 */
function showOrderPopDiv(){
	//如果还没有选择网点，则提示选择网点
	if(!$("#areaId_step1").val()){
		showPopover("请先选择起点");
		return;
	}
	
	if($("#grid-table-step2").jqGrid('getGridParam','datatype')=='local'){
		$("#grid-table-step2").jqGrid('setGridParam',{datatype:'json'}).trigger('reloadGrid');
	}
	
	$("#topToolbarPathDiv_net").hide();
	$("#topToolbarPathDiv_setting").hide();
	$("#topToolbarPathDiv_cars").hide();
	$("#topToolbarPathDiv_orders").show();
}
/**
 * 显示路线设置对话框
 */
function showSettingPopDiv(){
	var dt = new Date();
	$("#jobstarttime").val(dt.pattern("yyyy-MM-dd HH:mm:ss"));
	$("#jobendtime").val(dt.pattern("yyyy-MM-dd")+" 23:59:59");
	$("#topToolbarPathDiv_net").hide();
	$("#topToolbarPathDiv_orders").hide();
	$("#topToolbarPathDiv_cars").hide();
	$("#topToolbarPathDiv_setting").show();
}
/**
 * 显示车辆对话框
 */
function showCarPopDiv(){
	
	$("#topToolbarPathDiv_net").hide();
	$("#topToolbarPathDiv_orders").hide();
	$("#topToolbarPathDiv_setting").hide();
	$("#topToolbarPathDiv_cars").show();
}
/**
 * 网点确定按钮点击事件，隐藏对话框
 */
function topToolbarPathDivNetOkClick(){
	$("#topToolbarPathDiv_net").hide();
	var netName=$("#net_name_step3").val();
	if(netName){
		$("#selectNetBtnLabel").html(netName);
	}
	//在地图上显示已选择的网点
	showNetPopOnMap();
}
/**
 * 点击网点选择的确定按钮，显示网点的pop框
 */
function showNetPopOnMap(){
	var id=$("#grid-table-step3").jqGrid('getGridParam',"selrow");
	if(id){
		var data = $("#grid-table-step3").getRowData(id);
		createNetPointWhileSelectRow(data);
	}
}
/**
 * 订单弹出框确定按钮点击事件，隐藏对话框
 */
function topToolbarPathDivOrdersOkClick(){
	$("#topToolbarPathDiv_orders").hide();
	if(orders[0]){
		$("#selectOrderBtnLabel").html(orders[0].lot);
	}
	
}
/**
 * 车辆弹出框确定按钮点击事件，隐藏对话框
 */
function topToolbarPathDivCarsOkClick(){
	$("#topToolbarPathDiv_cars").hide();
	
	if(cars.length){
		var carNames=[];
		$.each(cars,function(key,val){
			carNames.push(val.license);
		});
		$("#selectedCarNames").html(carNames.join(" "));
		//将车辆的车牌存入矢量要素中
		layer_orders_vector.getFeaturesByAttribute("netId",$("#net_id_step3").val())[0].attributes.carNames=carNames.join(",");
	}
}