<%@ page language="java" contentType ="text/html; charset=utf-8" import="java.util.*,com.supermap.egispservice.base.entity.UserEntity,com.supermap.egispservice.base.entity.ComEntity" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("projectName", request.getContextPath());
	UserEntity user = (UserEntity) request.getSession().getAttribute("user");
	if(null!=user){
		request.setAttribute("eid", user.getEid().getId());
	}
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	request.setAttribute("ctx", basePath);
    
%>

<html>
<head>
<title>地图慧-企业可视化管理平台</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="${ctx }/resources/js/map/supermap/libs7C/SuperMap.Include.js" type="text/javascript"></script>

<script src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${ctx}/resources/assets/js/bootbox.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.message.cn.js"></script>
<script src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>

<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/data_citys.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/data_provinces.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/data_town.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/SMapCity.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/cloudlayer.js"></script>
<link href="${ctx}/resources/js/map/supermap/libs7C/SMap/SMapCity.css" rel="stylesheet" type="text/css"/>
<style type="text/css">

/* 信息窗使用的样式 */

.infowindowpopup {
	
}
.infowindowTitle {
	background-color: #F9F9F9;
    height: 30px;
    line-height: 30px;
    overflow: hidden;
    white-space: nowrap;
	border-bottom: 1px solid #CCCCCC;
	font-family: 'microsoft yahei';
}
.infowindowTitleTxt {
	color: #4D4D4D;
    font-size: 14px;
    font-weight: bold;
    overflow: hidden;
    padding-left: 11px;
    white-space: nowrap;
    width: 100px;
	font-family: 'microsoft yahei';
}
.infowindowContent {
	overflow: hidden;
    padding: 9px 11px 6px 9px;
	text-align: left;
	font-family: 'microsoft yahei';
}
.infowindowContent div {
	padding: 0;
	text-align: left;
	font: 12px arial,SimSun,sans-serif;
	font-family: 'microsoft yahei';
}
.infowindowContentTable {
	margin-top: 2px;
	color: #4D4D4D;
	text-align: left;
	margin: 2px;
}
.infowindowContentTable td {
	line-height: 28px;	
	font-family: 'microsoft yahei';
	color: #555;
}

.infowindowContentTable td span{
	width: 80px;
	display: inline-block;
}
.infowindowContentTable td strong{
	font-weight: normal;
	color: #307ecc;
}
.bottom_tip_div {position: absolute;left: 12px;bottom: 0px;width:100%;height:40px; display: block;text-align: center;background-color: #ff7200;}
.bottom_tip_div ul{list-style: none; overflow:hidden; zoom:1; float:left; }
.bottom_tip_div ul li{ float:left; margin-right:2px;  width:450px; height:40px; line-height:40px; text-align:center;  }
.bottom_tip_div ul li a{color:#393939;cursor:pointer;  }
#orders_para_wraper {margin: 0px;padding: 0px;overflow-y:auto; overflow-x:hidden }
#orders_para_wraper #order_para_days{margin: 0px;padding: 0px;}
#orders_para_wraper #order_para_days ul{list-style: none; overflow:hidden; float:left; padding: 0px;margin: 0px;padding: 0px;display: block;width: 300px;}
#orders_para_wraper #order_para_days ul li{ cursor: pointer;float:left; margin:0px;  width:75px; height:40px; line-height:40px; text-align:center;border: 1px solid #DBDBDB;   }

#order_para_time_div{display: block;width: 300px;margin: 40px 0px 10px 0px;padding: 0px; padding: 4px 0px 0px 2px; }
#order_para_time_div input{width: 125px;}

#orders_table_result {margin: 0px;padding: 0px;overflow-y:auto ; overflow-x:hidden;}
#orders_table_result ul{list-style: none; overflow:hidden; float:left; margin: 0px;padding: 0px;display: block;width: 300px;}
#orders_table_result ul li{ float:left; margin:0px; height:40px; line-height:40px; text-align:center; border-bottom: 1px solid #DBDBDB;   }
#orders_table_result ul li:nth-child(1){color: red; }
#orders_table_result ul li:nth-child(3){color: #FF820E; }
#orders_table_result ul li:nth-child(5){color: green; }
#orders_table_result ul li.c1{width: 40px; overflow: hidden;}
#orders_table_result ul li.c2{width: 256px; overflow: hidden;margin: 0px;padding: 0px;text-align:left;}
#orders_table_result ul li.c2 div:nth-child(1){position:relative;top:0px;left:0px;float:left; width: 256px; height: 20px;text-align: left;vertical-align:top;line-height: 20px;}
#orders_table_result ul li.c2 div:nth-child(1) p{margin: 0px;padding: 0px;float: left;}
#orders_table_result ul li.c2 div:nth-child(1) p:nth-child(2){color:#ff7200;}
#orders_table_result ul li.c2 div:nth-child(2){position:relative;bottom:0px;left:0px;float:left; width: 256px; background-color: #00A9F5;height: 14px;}
#orders_table_result ul li.c2 label{position:relative;top:0px;left:0px;float:left; width: 200px; height: 20px;}

#today_orders{font-size:20px; margin: 0px;padding: 5px 0px 0px 5px;background-color: #00A9F5;width: 120px;height: 74px;position: absolute;top: 5px;left: 330px;z-index: 2000;color: white;overflow: hidden;}
#month_orders{font-size:20px; margin: 0px;padding: 5px 0px 0px 5px;background-color: #9C26B0;width: 120px;height: 74px;position: absolute;top: 5px;left: 455px;z-index: 2000;color: white;overflow: hidden;}

#orders_legend{margin: 0px;padding: 5px 0px 0px 5px;background-color: #fff;width: 100px;height: 130px;position: absolute;bottom: 45px;left: 330px;z-index: 2000;overflow: hidden;}
#orders_legend ul{list-style: none; overflow:hidden; float:left; margin: 0px;padding: 0px;display: block;}
#orders_legend ul li {height: 25px; }
#orders_legend ul li div{width: 45px;height: 14px;float: left;margin: 2px 2px; }
#orders_legend ul li:nth-child(2) div{background-color: #006D2C;}
#orders_legend ul li:nth-child(3) div{background-color: #31A354;}
#orders_legend ul li:nth-child(4) div{background-color: #74C476;}
#orders_legend ul li:nth-child(5) div{background-color: #C7E9C0;}

.popover-content {    font-size: 14px;    font-family: "microsoft yahei";    color: #fff;}
#popover_result { top: 100px;     left: 50%;    position: fixed;     width: auto;     z-index: 9999999;     display: none;     background-color: #666;     text-align: center;}

</style>
<script type="text/javascript" >
Date.prototype.pattern=function(fmt) {     
	var o = {     
		"M+" : this.getMonth()+1, //月份     
		"d+" : this.getDate(), //日     
		"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时     
		"H+" : this.getHours(), //小时     
		"m+" : this.getMinutes(), //分     
		"s+" : this.getSeconds(), //秒     
		"q+" : Math.floor((this.getMonth()+3)/3), //季度     
		"S" : this.getMilliseconds() //毫秒     
	};     
	var week = {     
		"0" : "\u65e5",     
		"1" : "\u4e00",     
		"2" : "\u4e8c",     
		"3" : "\u4e09",     
		"4" : "\u56db",     
		"5" : "\u4e94",     
		"6" : "\u516d"     
	};     
	if(/(y+)/.test(fmt)){     
	    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));     
	}     
	if(/(E+)/.test(fmt)){     
	    fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);     
	}     
	for(var k in o){     
    if(new RegExp("("+ k +")").test(fmt)){     
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));     
        }     
    }     
    return fmt;     
}  

var PROJECT_URL='${ctx}';
var isLogined=${isLogined};
var map;//地图对象
var layer_region;
function initMap(){
	var days = '${days}';
	
	if(  ${user.sourceId} === 4 && days === '') {
		$('#modal_permission').modal({
			backdrop: 'static',
			show: true
		});
	}

	initcss();

	var layer = new SuperMap.Layer.CloudLayer({url:"http://t1.supermapcloud.com/FileService/image"});
	map = new SuperMap.Map("mapObj", { controls: [
		//new SuperMap.Control.PanZoomBar({showSlider:true}),
		new SuperMap.Control.Navigation({
		    dragPanOptions: {
		        enableKinetic: true
		    }
		})]
	});
	
	map.addLayer(layer);
	
	layer_region = new SuperMap.Layer.Vector('region');	
	var callbacks = {
	    click: showFeature
	};
    var selectFeature = new SuperMap.Control.SelectFeature(layer_region, {callbacks: callbacks});
    //map上添加控件
    map.addControl(selectFeature);
    //激活控件
    selectFeature.activate();
    
	map.addLayers([layer_region]);
	
	//var obj=geoLoc2MeterXY(104.061323,30.123465);
	var obj=geoLoc2MeterXY(104.8754,38.1353);
	map.setCenter(new SuperMap.LonLat(obj.lngX, obj.latY),4);
	refreshMap();
}	
function showFeature(feature){
	clearMapPopups();
	var attr=feature.attributes;
	var lonlat = new SuperMap.LonLat(attr.center.x,attr.center.y);
	var name = attr.name? attr.name : "无";
	var num = attr.num? attr.num : "0";
	var h =  '<div style="height:20px;width:150px;">';
	h += ''+ name;
	h += '	<div class="infowindowContent">';
	h += '订单：'+num;
	h += '	</div>';
		
		h += '</div>';

    var popup = new SuperMap.Popup.FramedCloud("popwin",lonlat,null, h,null,true);
    popup.anchor.offset = new SuperMap.Pixel(0, 0);

    popup.fixedRelativePosition = true;
    popup.relativePosition = "tr";
    map.addPopup(popup);        
}
/**
 * 清除地图上的信息窗
 */
function clearMapPopups() {
	var popups = map.popups;
	var length = popups.length;
	if(length == 0) {
		return;
	}
	for(var i=0; i<length; i++) {
		var popup = popups[i];
		map.removePopup(popup);
	}
}
/**
 * 通过缩放刷新地图
 * @return
 */
function refreshMap(){
	if(map)
		map.updateSize();
}
function initcss() {
	var mapHeight = getWindowHeight() - 70;
	var w = getWindowWidth() - 450;
	$("#mapObj").height(mapHeight-42);	
	$("#wraper").height(mapHeight);	
	$("#orders_para_wraper").height(mapHeight-42-40);	
	$("#mapObj").width($("#wraper").width()-306);	
	$("#bottom_tip_div").width($("#wraper").width());	
	$("#orders_table_result").height(mapHeight-42-40-90);	
	$(".bottom_tip_div ul").css("padding-left",(getWindowWidth()-145)/4);	
	refreshMap();
}
$(function(){
	$("#wraper").resize(function(e) {
		//resize 后initcss方法 的语句$("#mapObj").width($("#wraper").width()-306);	才有效
	});
	
	//更新系统标题
	var systitle=$("#systitle").html();
	var istry=${istry};
	if(istry){
		$("#systitle").html(systitle+"（试用版）");
	}
	if(!isLogined){
		$('#myModal').modal('show');
	}
	initMap();
	$(window).resize(initcss);

	$("#order_para_days ul li").each(function(key){
		switch(key){
		case 0:{
			$(this).data("days",1);
			break;
		}
		case 1:{
			$(this).data("days",7);
			break;
		}
		case 2:{
			$(this).data("days",30);
			break;
		}
		case 3:{
			$(this).data("days",0);
			break;
		}
		}
	});
	$("#order_para_days ul li").on("click",function(){
		var days=($(this).data("days"));
		var dt = new Date();
		var oneDayMills=1 * 24 * 60 * 60 * 1000;
		switch(days){
		case 1:{
			$("#endtime").val(dt.pattern("yyyy-MM-dd")+" 23:59:59");
			dt=new Date(dt.getTime()-1*oneDayMills);
			$("#starttime").val(dt.pattern("yyyy-MM-dd")+" 00:00:00");
			queryByParams();
			break;
		}
		case 7:{
			$("#endtime").val(dt.pattern("yyyy-MM-dd")+" 23:59:59");
			dt=new Date(dt.getTime()-7*oneDayMills);
			$("#starttime").val(dt.pattern("yyyy-MM-dd")+" 00:00:00");
			queryByParams();
			break;
		}
		case 30:{
			$("#endtime").val(dt.pattern("yyyy-MM-dd")+" 23:59:59");
			dt=new Date(dt.getTime()-30*oneDayMills);
			$("#starttime").val(dt.pattern("yyyy-MM-dd")+" 00:00:00");
			queryByParams();
			break;
		}
		case 0:{
			$("#starttime").val("");
			$("#endtime").val("");
			break;
		}
		}
	});
	
	$("#search").on("click",function(){
		queryByParams();
	});
	today();
 	month();
 	getAllArea();
 	
 	
});
function getAllArea(){
	$.ajax({
		type : "GET",
		async : true,
		url : PROJECT_URL+"areaService/queryAllAreaTop10?ram=" + Math.random(),
		success : function(obj) {
			if(obj.isSuccess){
				var obj2=[];
				for(var i=0; i<obj.result.length;i++ ){
					var temp=obj.result[i];
					var area={};
					area.id=temp.id;
					area.num="0";
					area.area_entity=temp;
					obj2.push(area);
				}
				addRecord(obj2,1);
				
			}
			
		}
	}); 
}
function ajaxQuery(param,callback){
 	$.ajax({
		type : "POST",
		async : true,
		url : PROJECT_URL+"statistic/order/getOrderNum?ram=" + Math.random(),
		data:param,
		success : function(obj) {
			var newObj={info:obj.info,isSuccess:obj.isSuccess,result:[]};
			for(var j=0; j<obj.result.length;j++ ){
				if(obj.result[j].area_entity){
					newObj.result.push(obj.result[j]);
				}
			}
			callback(newObj);
		}
	}); 
}
//增加一个查询排名前10的
function ajaxQueryTop10(param,callback){
 	$.ajax({
		type : "POST",
		async : true,
		url : PROJECT_URL+"statistic/order/getOrderNumTop10?ram=" + Math.random(),
		data:param,
		success : function(obj) {
			var newObj={info:obj.info,isSuccess:obj.isSuccess,result:[]};
			for(var j=0; j<obj.result.length;j++ ){
				if(obj.result[j].area_entity){
					newObj.result.push(obj.result[j]);
				}
			}
			callback(newObj);
		}
	}); 
}

function month(){
	var dtMonth = new Date();
 	var startM=dtMonth.pattern("yyyy-MM")+"-01 00:00:00";
 	var endM=dtMonth.pattern("yyyy-MM-dd")+" 23:59:59";
 	var param={"start":startM,"end":endM};
 	ajaxQuery(param,monthCall);
}
function monthCall(obj){
	if (obj.isSuccess == true) {
		var total=0;
		for(var j=0; j<obj.result.length;j++ ){
			if(obj.result[j].area_entity){
				total+=Number(obj.result[j].num);
			}
		}
		$("#month_orders div:nth-child(2)").html(total);
	}
}
function queryByParams(){
	var start=$("#starttime").val();
	var end=$("#endtime").val();
	if(start=="" || end==""){
		showPopover("查询时间不能为空！");
		return;
	}
	var param={"start":start,"end":end};
 	ajaxQueryTop10(param,queryByParamsCall);
}
function queryByParamsCall(obj){
	if (obj.isSuccess == true) {
		clearMapPopups();
		layer_region.removeAllFeatures();
		
		var total=0;
		for(var j=0; j<obj.result.length;j++ ){
			total+=Number(obj.result[j].num);
		}
		addAreas(obj.result,total);
		addRecord(obj.result,total);
	}
}
function today(){
	var dt = new Date();
	var start=dt.pattern("yyyy-MM-dd")+" 00:00:00";
 	var end=dt.pattern("yyyy-MM-dd")+" 23:59:59";
 	
 	var param={"start":start,"end":end};
 	ajaxQuery(param,todayCall);
 	
}
function todayCall(obj){
	if (obj.isSuccess == true) {
		var total=0;
		for(var j=0; j<obj.result.length;j++ ){
			if(obj.result[j].area_entity){
				total+=Number(obj.result[j].num);
			}
		}
		$("#today_orders div:nth-child(2)").html(total);
		//addAreas(obj.result,total);
		//addRecord(obj.result,total);
	}
}
function addRecord(areas,total){
	var result=$("#orders_table_result ul");
	result.empty();
	bubble(areas);
	if(areas.length){
		for(var i=0; i<areas.length;i++ ){
			var area=areas[i];
			var areaEntity=area.area_entity;
			var j=i+1;//编号
			var num=Number(area.num);
			if(i==0){//将第一个作为参照值
				total=num;
			}
			var rate=0;
			if(num==0 || total==0 || !total){
				rate=0;
			}else{
				rate=num/total;
			}
			
			
			var percent=(rate*100).toFixed(0)+"%";//百分比
			var areaName=areaEntity?areaEntity.name:"无区域";
			areaName="<p>"+areaName+"</p>"+"<p>【"+num+"单】</p>";
			var width=(220*rate).toFixed(0);
			var percentDiv=num?"<div>"+areaName+"</div><div style='width: "+width+"px;'></div>":areaName;
			var html="<li class='c1'>"+j+"</li>"+
			"<li class='c2'>"+percentDiv+"</li>";
			//"<li class='c3'>"+percent+"</li>";
			result.append(html);
		}
	}else{
		getAllArea();
		//var html="无数据";
		//result.append(html);
	}
	
}
function bubble(array){
	 for (var i = 0; i < array.length; i++) {
         for (var j = array.length-1; j > 0; j--) {
             if (Number(array[j].num) > Number(array[j - 1].num)) {
                 var temp = array[j - 1];
                 array[j - 1] = array[j];
                 array[j] = temp;
             }
         }
	 }
}
function addAreas(areas,total){
	if(!areas) {
		return;
	}
	bubble(areas);
	var centerPoint=null;
	if(areas.length){
		var maxnum=Number(areas[0].num);
		for(var j=0; j<areas.length;j++ ){
			
			var area=areas[j].area_entity;
			if(!area){
				continue;
			}
			var pointIndex=0;
			var points=area.points;
			var geos=[];
			var parts=area.parts;
			for(var i=0; i<parts.length;i++ ){
				var par=parts[i];
				
				var ppp = [];
				for(var k=0; k<par;k++ ){
					var p=points[pointIndex++];
					 var pp = new SuperMap.Geometry.Point(p.x, p.y);
					 ppp.push(pp);
				}
				var linearRing = new SuperMap.Geometry.LinearRing(ppp);
				
		        geos.push(linearRing);
				
			}
			var region = new SuperMap.Geometry.Polygon( geos );                
			
			var feature = new SuperMap.Feature.Vector(region, null,getLinearRingStyle(Number(areas[j].num),total,maxnum));
			centerPoint=new SuperMap.Geometry.Point(area.center.x, area.center.y);
			feature.attributes={
					areaId:area.id,
					center:centerPoint,
					name: area.name,
					num: areas[j].num
			}
		    layer_region.addFeatures([feature]);
		}
		map.zoomToExtent(layer_region.getDataExtent());
		map.zoomOut();
	}
	
}
function getLinearRingStyle(num,total,maxnum){
	var color=null,color1="#006D2C",color2="#31A354",color3="#74C476",color4="#C7E9C0";
	var rate=num/maxnum*100;
	if(rate>=0 && rate<25){color=color4;}
	else if(rate>=25 && rate<50){color=color3;}
	else if(rate>=50 && rate<75){color=color2;}
	else if(rate>=75 && rate<=100){color=color1;}
	var style = {
		    strokeColor:"#00A9F5",
		    strokeOpacity:1,
		    strokeWidth:2,
		    fillColor:color,
		    cursor: 'pointer'
	};
	
	return style;
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
 * 隐藏第一次登陆提示
 */
function hideMyModal() {
	$('#myModal').modal('hide');
}
</script>
</head>

<body>
<div id="wraper" style="margin: 0px;padding: 0px;">

<!-- 查询条件表格 -->
<div id="order_para_div" class="widget-box widget-color-blue  light-border" style="position:relative ;left: 0px;top: -3px;float:left; width: 300px;">
	<div class="widget-header" style="background-color: #DBDBDB;border: 1px solid #DBDBDB;">
		<h4 class="widget-title smaller" style="font-weight: 800;color: #393939;">订单统计</h4>
    	<div class="widget-toolbar no-border">
    	</div>
	</div>
	<div class="widget-body">
		<div id="orders_para_wraper">
			<div id="order_para_days">
				<ul>
					<li>今日</li>
					<li>7日内</li>
					<li>30日内</li>
					<li>自定义</li>
				</ul>
			</div>
			
			<div id="order_para_time_div">
				<input type="text" id="starttime"  placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				<!-- <i class="ace-ico fa fa-minus" style="color: #7A7A7A;"></i> -->
				<input type="text" id="endtime"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
				<button type="button" class="btn btn-sm "  id="search">
					<i class="ace-icon fa fa-search icon-on-right " ></i>
				</button>
			</div>
			<div id="orders_table_result">
				<ul>
				<p style="padding-left: 10px !important;">无数据</p>
				</ul>
			</div>
		</div>
	</div>
</div>
<div id="mapObj" class="smMap" style="position:relative ;right: 0px;top: 0px;width: auto; height: 100%;margin-top: 0px !important;margin-left:3px !important; float: left;z-index: 0 !important;" ></div>

<!-- 底部提示 -->
<div id="bottom_tip_div" class="bottom_tip_div" >
	<ul style="color:#fff !important; font-weight: 800;font-size: 16px;">
	<c:if test="${istry==true}">
		<li>贵公司账号的免费试用期还剩${days }天，点击<a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=172249742&amp;site=qq&amp;menu=yes"><u style="color:#fff;">立即开通商务版</u></a></li>
	</c:if>
	<c:if test="${istry==false}">
		<li>商务版还剩${days }天</li>
		<li><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&amp;uin=172249742&amp;site=qq&amp;menu=yes"><u style="color:#fff; font-weight: 400;font-size: 13px;">立即续费</u></a></li>
	</c:if>
	</ul>
</div>
<div id="today_orders">
	<div>今日订单</div>
	<div>0</div>
</div>
<div id="month_orders">
	<div>本月订单</div>
	<div>0</div>
</div>
<div id="orders_legend">
	<ul>
		<li>订单量</li>
		<li><div></div>非常多</li>
		<li><div></div>多</li>
		<li><div></div>中等</li>
		<li><div></div>少</li>
	</ul>
</div>

<!-- 提示 -->
<div id="popover_result" class="popover fade top in" style="">
  <div class="popover-content" id="popover_content"></div>
</div>

<!-- Modal -->
<div class="modal fade" id="myModal"   tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" >
    <div class="modal-content" >
      <div class="modal-header" >
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel"></h4>
      </div>
      <div class="modal-body" >
      	<div style="text-align: center;">
			<h1>超图地图慧</h1>
			<h4>物流管理如此简单</h4>
			<br>
			<br>
			<br>
			<br>
			<h4 style="cursor: pointer;" onclick="hideMyModal()">进入系统</h4>
		</div>
      </div>
    </div>
  </div>
</div>


<!-- Modal -->
<div class="modal fade" id="modal_permission"   tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog" >
    <div class="modal-content" >
      <div class="modal-body" >
      	<div style="text-align: center;">
			<h1></h1>
			<h4>您已试用超过1周，如有需求请联系商务MM开通权限</h4>
			<br>
			<br>
			<br>
			<br>
			<a class="btn btn-success" href="http://wpa.qq.com/msgrd?v=3&amp;uin=3135685957&amp;site=qq&amp;menu=yes" target="_blank">立即联系</a>
		</div>
      </div>
    </div>
  </div>
</div>



</div>

</body>
</html>
