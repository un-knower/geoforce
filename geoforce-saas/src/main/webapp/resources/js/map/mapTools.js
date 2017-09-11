/**
*地图操作
*/
var curActive;//当前操作的功能名称如定位、跟踪、轨迹等
var map = null;
var poiLayer = null;//轨迹图层
var featuresLayer = null;//轨迹图层
var markersLayer = null;//车辆点图层
var historySELayer = null;//历史轨迹开始 和结束点的图层
var selectControll = null;
var popup;
var initLocateFlag = true;//点击及时定位 表示首次定位 满屏显示所有车辆

var drawLine = null;//绘制图层线
var drawPoint = null;//绘制图层点
var drawPolygon = null;//绘制图层多边形
var modifyFeature = null;//编辑多边形
//轨迹线样式
var lineStyle = {
    strokeColor: "#304DBE",
    strokeWidth: 3,
    pointerEvents: "visiblePainted",
    fillColor: "#304DBE",
    fillOpacity: 0.5,
    pointRadius:2
};
//多边形样式
var polygonStyle = {fillColor:"#F97979", fillOpacity:0.6, strokeColor: "#7C3CBC", strokeOpacity:0.8, strokeWidth:3};
var trackFeature = null;//车辆跟踪的轨迹线
var historyFeature = null;//轨迹回放的轨迹线
//车辆icon
var carImgUrl = imagesPath+"/mapcloud/car32.png";
//历史轨迹起点
var startImg = imagesPath+"/mapcloud/stPoint.png";
//历史轨迹终点
var endImg = imagesPath+"/mapcloud/edPoint.png";
var playSpeed = 1;//播放步长
var gpsIndex = 0;//当前点
var gpsCount = 0;//轨迹播放总记录
var historyTimer;//定时播放器
var historyData;//历史轨迹数据
var startMile;//开始里程
var historySTimeStr;//轨迹播放第一条定位时间
var historyETimeStr;//轨迹播放最后一条定位时间

var iClientMeter = 0;
/**
 * 初始化地图
 * @return
 */
function initMap(){
	try{
		var layer = new SuperMap.Layer.CloudLayer();
//		var layer = new SuperMap.Layer.CloudLayer({resolutions:[156605.46875, 78302.734375, 39151.3671875, 19575.68359375, 9787.841796875, 4893.9208984375, 2446.96044921875, 1223.48022460937, 611.740112304687, 305.870056152344, 152.935028076172, 76.4675140380859, 38.233757019043, 19.1168785095215, 9.55843925476074, 4.77921962738037, 2.38960981369019, 1.19480490684509]});
		if(layer == null || layer == ""){
			alert("无网络连接");
			return;
		}
		
		//layer.resolutions = [156697.2575744, 78348.6287872, 39174.3143936, 19587.1571968, 9793.5785984, 4896.7892992, 2448.3946496, 1224.1973248, 612.0986624, 306.0493312, 153.0246656, 76.5123328, 38.2561664, 19.1280832, 9.5640416, 4.7820208, 2.3910104, 1.1955052, 0.5977526];
		poiLayer = new SuperMap.Layer.Vector();
		markersLayer = new SuperMap.Layer.Vector();
		featuresLayer = new SuperMap.Layer.Vector();
		historySELayer = new SuperMap.Layer.Vector();
		featuresLayer.style = {fillColor: "blue",strokeColor: "blue",pointRadius:5,strokeWidth:2};
		
		modifyFeature = new SuperMap.Control.ModifyFeature(featuresLayer);
		//feature点击控件
		var witchWindow = '';
		if(curActive == 'poiManager'){
			witchWindow = openInfowindow4point;
		}else{
			witchWindow = openInfowindow;
		}
		selectControll = new SuperMap.Control.SelectFeature([markersLayer], {onSelect: witchWindow});
		drawLine = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Path, { multi: true });
		drawPoint = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Point, { multi: false });
		drawPolygon = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Polygon, { multi: true });
		drawLine.events.on({"featureadded":drawLineCompleted});
		drawPoint.events.on({"featureadded": drawPointCompleted});
		drawPolygon.events.on({"featureadded": drawPolygonCompleted});
		
		
//		var overviewMap = new SuperMap.Control.OverviewMap();
		map = new SuperMap.Map("mapObj", { controls: [
//		          new SuperMap.Control.ScaleLine({geodesic:true}),
//				  overviewMap,
		          new SuperMap.Control.PanZoomBar({showSlider:true}),
	              new SuperMap.Control.Navigation({
	                  dragPanOptions: {
	                      enableKinetic: true
	                  }
	              }),drawLine,drawPoint,drawPolygon,modifyFeature]
	    });
		map.addLayer(layer);
		map.addLayer(poiLayer);
		map.addLayer(featuresLayer);
		map.addLayer(historySELayer);
		map.addLayer(markersLayer);
		
//		map.addControl(overviewMap);
		map.addControl(selectControll);
		selectControll.activate();
		//鹰眼最小化
//		overviewMap.minimizeControl();
		mapCenter();
		$("#mapLoading").hide();
		//loadPois(1);//加载兴趣点
		//云的poi类库
	//	createPoiService();
	}catch(e){
	}
}
/**
 * 获取兴趣点
 * @return
 */
function loadPois(curPage){
	if(map){
		$.ajax({
			type : "POST",
			async : true,
			url : ctx+"/icenter/poiAction!loadPois.do?currentPageNum="+curPage,
			dataType : "json",
			success : function(res) {
				if(res && res != "-999"){
					var pageNum = res.total/100+1;
					showPoiInMap(res.rows);//in poi.js 地图上绘制兴趣点
					//if(curPage <= pageNum) loadPois(curPage+1);
				}
			},
			error : function() {
	        }
		});
	}
}

/**
 * 设置地图中心
 * @return
 */
function mapCenter(){
	
	if(ps_lng){//in taglibs.jsp
		var xyObj = geoLoc2MeterXY(ps_lng,ps_lat);
		var lng = xyObj.lngX;
		var lat = xyObj.latY;
		//默认北京
		map.setCenter(new SuperMap.LonLat(lng,lat),11);
	}else{
		//默认北京
		map.setCenter(new SuperMap.LonLat(12958399.4681885, 4852082.44060595),11);
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

/**
 * 鼠标点击弹出信息框
 * @param feature
 * @return
 */
function openInfowindow(feature){
	var carObj = feature.attributes;
	if(carObj){
		if(carObj.license){
			feature.geometry.calculateBounds();
			var size = new SuperMap.Size(0,0);
			//图标在地图上的偏移量
			var offset = new SuperMap.Pixel(0,-16);
			var icon = new SuperMap.Icon("", size, offset);
			if(popup){//防止弹出多个弹出框
				map.removePopup(popup);
			}
			popup = new SuperMap.Popup.FramedCloud("chicken", 
					feature.geometry.getBounds().getCenterLonLat(),
					size,
		            getMarkInfo(carObj),
		            icon,
		            true,
		            function(){
						if(carObj.carId){
							closeInfowindow(carObj.carId);
						}
					},
					true
			);
			feature.popup = popup;
		    map.addPopup(popup);
		}
	}
}
/**
 * 清除弹出框
 * @param feature
 * @return
 */
function closeInfowindow(carId){
	var feature = markersLayer.getFeatureById("point_"+carId);
	if(feature == null){
		return;
	}
	if(feature.popup){
		map.removePopup(feature.popup);
	    feature.popup.destroy();
	    feature.popup = null;
	}
}
/**
 * 车辆弹出框内容
 * @param obj
 * @return
 */
function getMarkInfo(obj){
	var carId = obj.carId;
	var license = obj.license;
	var gpstime = obj.gpsTimeStr;
	var alarmTime = obj.alarmTimeStr;
	var drivers = obj.drivers==null?"":obj.drivers;
	var lng = obj.longitude;
	var lat = obj.latitude;
	var addr = obj.addr == null?'':obj.addr;
	var speed = obj.speed == null?'':obj.speed;
	var direction = obj.direction==null?'':obj.direction;
	var directionStr = obj.directionStr==null?'':obj.directionStr;
	var alarmType = obj.alarmType==null?'':obj.alarmType;
	var zfTurn = obj.zfTurn==null?'':obj.zfTurn;
	var mile = obj.mile == null?'':obj.mile;
	
	var showAddr = addr;
	if(addr.length > 15){
		showAddr = addr.substring(0,15)+"..";
	}
	var showLicense = license;
	if(license.length > 7){
		showLicense = license.substring(0,7)+"..";
	}
	var showDrivers = drivers;
	if(drivers.length > 15){
		showDrivers = drivers.substring(0, 15)+"..";
	}
	var zfTurnStr;
	if(zfTurn == "1"){
		zfTurnStr = "正转";
	}else if(zfTurn == "2"){
		zfTurnStr = "反转";
	}
//	else{
//		zfTurnStr = "不转";
//	}
	var markInfo = '<div class="PopUpBox" style="width:255px;margin-left:5px;font-size:13px;">'
				+'<div style="height:35px;vertical-align:middle;font-size:14px;"><div title="'+license+'位置信息"><b>'+showLicense+'位置信息</b></div>'
				+'<hr style="width:100%;">';
	if(gpstime){		
		markInfo += '<p>定位时间：<span>'+gpstime+'</span></p>';
	}
	if(alarmTime){
		markInfo += '<p>报警时间：<span>'+alarmTime+'</span></p>';
	}
	if(alarmType){
		markInfo += '<p>报警类型：<span>'+alarmType+'</span></p>';
	}
	if(mile){
		markInfo += '<p>行驶里程：<span>'+mile+' km</span></p>';
	}
	markInfo += '<p>行驶速度：<span>'+speed+' km/h | 行驶方向：'+directionStr+'</span></p>';
	if(drivers){
		markInfo += '<p>车辆司机：<span title="'+drivers+'">'+showDrivers+'</span></p>';
	}
	if(zfTurnStr){
		markInfo += '<p>业务信息：<span>'+zfTurnStr+'</span></p>';
	}
	if(addr){
		markInfo += '<p>位置信息：<span title="'+addr+'">'+showAddr+'</span></p>';
	}
	if(curActive){
		var linkHtml = '<div>';
		
		if(curActive == "locate"){//定位
			linkHtml += '<a style="color:black;cursor: default;">及时定位</a>|<a onclick=carTrack("'+carId+'")>锁定跟踪</a>|<a onclick=historyShowTime("'+carId+'")>历史轨迹</a>';
		}else if(curActive == "track"){
			linkHtml += '<a onclick=carLocate("'+carId+'")>及时定位</a>|<a style="color:black;cursor: default;">锁定跟踪</a>|<a onclick=historyShowTime("'+carId+'")>历史轨迹</a>';
		}else if(curActive == "history"){
			linkHtml += '<a onclick=carLocate("'+carId+'")>及时定位</a>|<a onclick=carTrack("'+carId+'")>锁定跟踪</a>|<a style="color:black;cursor: default;">历史轨迹</a>';
		}else{
			linkHtml += '<a onclick=carLocate("'+carId+'")>及时定位</a>|<a onclick=carTrack("'+carId+'")>锁定跟踪</a>|<a onclick=historyShowTime("'+carId+'")>历史轨迹</a>';
		}
		linkHtml += '</div>';
		markInfo += linkHtml;
	}
	markInfo += '</div>';
	return markInfo;
}
/**
 * 地图上清除已有的点和线的feature
 * 包括弹出框
 * @return
 */
function clearHasFeatures(){
	//先清除当前点的图标，再画新的点
	var ptFeatures = markersLayer.features;
	if(ptFeatures){
		//如果弹出框存在先删除,在创建
		for(var i=0;i<ptFeatures.length;i++){
			var feature = ptFeatures[i];
			var carObj = feature.attributes;
			if(carObj){
				closeInfowindow(carObj.carId);
			}
		}
	}
	featuresLayer.removeAllFeatures();
	markersLayer.removeAllFeatures();
}
/**
 * 计算所有点的边界 一屏显示
 * @return
 */
function pointsCenter(pointLayer){
	//重新计算边界，一屏显示所有车辆
	var bounds = pointLayer.getDataExtent();
	var zoom = map.getZoomForExtent(bounds,true);
	if(zoom > 14) zoom = 14;
	var center = bounds.getCenterLonLat();
	map.setCenter(center,zoom-1);
}
/**
 * 创建车辆点的feature
 * @param carObj
 * @return
 */
function createCarMark(carObj){
	var carId = carObj.carId,license = carObj.license,
	lng = carObj.longitude,lat = carObj.latitude,
	status = carObj.status;
	//转成墨卡托
	var xyObj = geoLoc2MeterXY(lng,lat);
	lng = xyObj.lngX;
	lat = xyObj.latY;
	//默认车辆标签颜色
	var carLabelColor = "blue";
	carImgUrl = imagesPath+"/mapcloud/carBlue.png";
	if(status && status != 1){//不是在线状态要改成红色
		carLabelColor = "red";
		carImgUrl = imagesPath+"/mapcloud/carRed.png";
	}
	var point = new SuperMap.Geometry.Point(lng,lat);
	var pointFeature = new SuperMap.Feature.Vector(point);
	pointFeature.style = {cursor:"pointer", externalGraphic: carImgUrl, graphicWidth:32, graphicHeight:32,
			graphicXOffset:-16, graphicYOffset:-16, rotation:carObj.direction, graphicTitle:license, 
			label:license, labelAlign:"cm", labelXOffset:0, labelYOffset:20,
			fontColor:carLabelColor, fontFamily:"宋体", fontSize:"12px", fontWeight:"bold"};
	
	pointFeature.id = "point_"+carId;
	pointFeature.attributes = carObj;
	return pointFeature;
}
/**
 * 地图打点
 * @return
 */
function addMarkPoint2d(carObj){
	var pointFeature = createCarMark(carObj);
	if(pointFeature){		
		markersLayer.addFeatures(pointFeature);
		return pointFeature.geometry;
	}
	return null;
}
//地图下放列表点击一个车 在地图上聚焦在该车辆
function showCarInMap(carId){
	if(carId == null || carId == ""){
		return;
	}
	var feature = markersLayer.getFeatureById("point_"+carId);
	if(feature == null){
		return;
	}
	map.panTo(feature.geometry.getBounds().getCenterLonLat());
}
/**
 * 车辆定位
 * @param carObjs
 * @return
 */
function addMarker_car(carObjs){
	if(carObjs == null || carObjs.length == 0){
		return;
	}
	//清除已有的点
	clearHasFeatures();
	var pointFeas = new Array();
	for(var i=0;i<carObjs.length;i++){
		var carObj = carObjs[i];
		pointFeas.push(createCarMark(carObj));
	}
	markersLayer.addFeatures(pointFeas);
	if(initLocateFlag){
		initLocateFlag = false;
		pointsCenter(markersLayer);
	}
}
/**
 * 车辆跟踪
 * @param carObjs
 * @return
 */
function addMarkLine_car(carObjs){
	if(carObjs == null || carObjs.length == 0){
		return;
	}
	var carObj = carObjs[0];
	//先清除 已有的点和线
	var oldPoint = markersLayer.getFeatureById("point_"+carObj.carId);
	var flag = false;//标识上个marker有没有打开弹出框
	if(oldPoint && oldPoint.popup){
		flag = true;
	}
	clearHasFeatures();
	//跟踪 加点
	var point = addMarkPoint2d(carObj);
	var pointFeature = markersLayer.getFeatureById("point_"+carObj.carId);

	//如果已有弹出框，则在新点上打开
	if(flag){
		openInfowindow(pointFeature);
	}
	var points;
	//锁定跟踪初始化
	if(trackFeature == null){
		trackFeature = new SuperMap.Feature.Vector();
		trackFeature.style = lineStyle;
		
		points = new Array();
		points.push(point);

		trackFeature.geometry = new SuperMap.Geometry.LineString(points);		
		featuresLayer.addFeatures(trackFeature);
		//第一次追踪 缩放到最小级别
		map.zoomTo(14);
	}else{
		points = trackFeature.geometry.components;
		points.push(point);
		lineStr = new SuperMap.Geometry.LineString(points);
		trackFeature.geometry = lineStr;
		featuresLayer.addFeatures(trackFeature);
	}
	//地图当前的边界
	var mapBound = map.getExtent();
	//车辆当前点的经纬度坐标
	var LonLat = new SuperMap.LonLat(point.x,point.y);
	//如果边界存在并且当前点超出边界则移动车辆保证显示
	if(mapBound && !mapBound.containsLonLat(LonLat)){		
		map.panTo(LonLat);
	}
}
/**
 * 历史轨迹
 * @return
 */
function drawHistory_car(data){
	if(data == null || data.length == 0){
		return;
	}
	historyData = data[0];
	if(historyData == null || historyData.length == 0){
		return;
	}
	var list = historyData.list;
	gpsCount = list.length-1;//以数据节点为总数量
	playSpeed = 1;
	gpsIndex = 0;
	startMile = (list[0].mile==null || list[0].mile == "")?0:list[0].mile;
	historySTimeStr = list[0].gpsTimeStr;
	historyETimeStr = list[gpsCount].gpsTimeStr;
	drawAllPoint();
	resetControll();
	historyControll();
}
/**
 * 重置控制面板各个控件到初始化状态
 * @return
 */
function resetControll(){
	//清空当前时间和当前里程
	$("#historyTime").val("");
	$("#historyMile").val("");
	//重新计算播放进度条
	$('#historySlider').slider('setValue',0);
	$('#historySlider').slider({
		min:0,
		max:gpsCount
	});
	$("#playLast").attr("disabled","disabled");
	$("#playLast").attr("title","x1");
	$("#playStart").find("img").attr("src",imagesPath+"/suspend.png");
	$("#playFast").removeAttr("disabled");
	$("#playFast").attr("title","x1");
	$("#playFiler").attr("checked","checked");
}
/**
 * 显示/隐藏轨迹播放器
 * @return
 */
function historyControll(){
	//控制面板属性
	var cotrollOptions = $("#historyWindow").window("options");
	var closed = cotrollOptions.closed;
	if(closed){//当控制面板为关闭状态则打开
		$("#historyWindow").window('open');

	}else{
		$("#historyWindow").window('close');
	}
}
/**
 * 一次性绘制所有历史轨迹点
 * @param carObjs
 * @return
 */
function drawAllPoint(){
	//清空图层
	clearHasFeatures();
	
	var list = historyData.list;
	var parts = historyData.lngLats;

	var points = new Array();
	//取出所有点放入数组中
	var listLen = parts.length;
	for(var i=0;i<listLen;i++){
		var point = new SuperMap.Geometry.Point(parts[i].x,parts[i].y);
		parts[i] = point;
	}
	historyFeature = new SuperMap.Feature.Vector();
	historyFeature.style = lineStyle;
	historyFeature.geometry = new SuperMap.Geometry.LineString(parts);
	
	featuresLayer.addFeatures(historyFeature);
	var startCar = parts[0];
	var endCar = parts[parts.length-1];
	//加入起点和终点
	addStartEndMarker(startCar,"start");
	addStartEndMarker(endCar,"end");
	//车的点打在最后
	var sCarObj = list[0];
	sCarObj.carId = historyData.carId;
	sCarObj.license = historyData.license;
	addMarkPoint2d(sCarObj);
	//计算边界 显示全部
	pointsCenter(featuresLayer);
}
/**
 * 历史轨迹加入起点 终点 
 * @param carObj
 * @return
 */
function addStartEndMarker(LngLat,type){
	var lng = LngLat.x;
	var lat = LngLat.y;
	var imgUrl = "";
	if(type == "start"){
		imgUrl = startImg;
	}else{
		imgUrl = endImg;
	}
	var point = new SuperMap.Geometry.Point(lng,lat);
	var feature = new SuperMap.Feature.Vector(point);
	feature.style = {externalGraphic: imgUrl, graphicWidth:40, graphicHeight:40,
			graphicXOffset:-20, graphicYOffset:-45};
	
	historySELayer.addFeatures(feature);
}
/**
 * 绘制当前点和之前的轨迹线
 * @return
 */
function drawCurrentHistory(){
	if(gpsIndex >= gpsCount){
		gpsIndex = gpsCount;
	}
	var list = historyData.list;
	var parts = historyData.lngLats;
	var curGpsData = list[gpsIndex];
	if(curGpsData == null){
		return;
	}
	curGpsData.carId = historyData.carId;
	curGpsData.license = historyData.license;
	//设置当前时间和当前里程
	$("#historyTime").val(curGpsData.gpsTimeStr);
	var mile = curGpsData.mile;
	mile = (mile==null || mile == "")?0:mile;
	mile = (mile-startMile);
	$("#historyMile").val(mile.toFixed(3));
	//清空图层
	var oldPoint = markersLayer.getFeatureById("point_"+historyData.carId);
	var flag = false;//标识上个marker有没有打开弹出框
	if(oldPoint && oldPoint.popup){
		flag = true;
	}
	clearHasFeatures();
	var newPoint = addMarkPoint2d(curGpsData);
	
	var pointFeature = markersLayer.getFeatureById("point_"+historyData.carId);
	if(flag){
		openInfowindow(pointFeature);
	}
	var hasPts = historyFeature.geometry.components;//在轨迹线上已存在的
	var position = hasPts.length - 1;
	if(gpsIndex > position){
		for(var i=position;i<=gpsIndex;i++){
			if(i == position){
				continue;
			}
			var lngLat = parts[i];
			var lng = lngLat.x;
			var lat = lngLat.y;
			var point = new SuperMap.Geometry.Point(lng,lat);
			hasPts.push(point);
		}
	}else if(gpsIndex < position){
		hasPts = hasPts.slice(0,gpsIndex);
		hasPts.push(newPoint);
	}
	historyFeature.geometry =  new SuperMap.Geometry.LineString(hasPts);
	
	featuresLayer.addFeatures(historyFeature);

	//地图当前的边界
	var mapBound = map.getExtent();
	//车辆当前点的经纬度坐标
	var newLonLat = new SuperMap.LonLat(newPoint.x,newPoint.y);
	//如果边界存在并且当前点超出边界则移动车辆保证显示
	if(mapBound && !mapBound.containsLonLat(newLonLat)){		
		map.panTo(newLonLat);
	}
}
/**
 * 历史轨迹播放器进度条控制
 * @param curVal
 * @return
 */
function history_sliderMove(curVal){
	$("#historySlider").slider('setValue',curVal);
}
/**
 * 轨迹播放slider 提示tip格式
 * @return
 */
function historyTipFmt(num){
//	var time = $("#historyTime").val();
//	var mile = $("#historyMile").val();
//	var timeStr = "";
//	var tip = "";
//	if(time){
//		timeStr = time.substring(11,time.length)+"|";
//	}
//	if(mile)
//		mile = mile+"KM";
//	tip = timeStr+mile;
//	return tip;
}
/**
 * 历史轨迹播放
 * @return
 */
	
function history_play(){
	var filer = $("#playFiler").attr("checked");
	if(filer){//如果忽略静止点选中，则变为加速模式 否则为普通模式
		var list = historyData.list;
		var mile = list[gpsIndex].mile;
		mile = (mile == null || mile == "")?0:mile;
		var endFlag = true;//防止遍历到最后也没有大过当前里程的情况
		var listLen = list.length;
		for(var i=gpsIndex;i<listLen;i++){
			var tmpMile = list[i].mile;
			tmpMile = (tmpMile == null || tmpMile == "")?0:tmpMile;
			if(tmpMile > mile){
				gpsIndex = i;
				endFlag = false;
				break;
			}
		}
		gpsIndex = gpsIndex + playSpeed;
		if(endFlag){
			gpsIndex = gpsCount;
		}
	}else{
		gpsIndex = gpsIndex + playSpeed;
	}
	if(gpsIndex >= gpsCount){
		gpsIndex = gpsCount;
		historyStart();
	}else{
		if(historyTimer){
			clearTimeout(historyTimer);
			historyTimer = null;
		}
		historyTimer = setTimeout("history_play()",1000);
	}
	drawCurrentHistory();
	history_sliderMove(gpsIndex);
	
	var hisList = historyData.list;
	var carObj = new Object();
	carObj.carId = historyData.carId;
	carObj.license = historyData.license;
	
	addGpsHistoryList(carObj,hisList[gpsIndex]);//in main.js在列表下方显示
}

/**
 * 关闭历史轨迹
 * @return
 */
function historyClosed(){
	controllClosed();
	clearHasFeatures();
	//清空起始点
	historySELayer.removeAllFeatures();
	cleanMapList();//清除地图下方列表数据 in main.js
}
/**
 * 关闭播放器控制面板
 * @return
 */
function controllClosed(){
	clearTimeout(historyTimer);
	historyTimer = null;
	$("#playStart").find("img").attr("src",imagesPath+"/suspend.png");
}
/**
 * 历史轨迹播放开始/暂停
 * @return
 */
function historyStart(){
	if(historyData){
		var obj = $("#playStart").find("img");
		var imgSrc = obj.attr("src");
		if(imgSrc.indexOf("suspend.png") != -1){
			if(gpsIndex == gpsCount){//播放到终点 重新播放
				gpsIndex = 0;
			}
			if(historyTimer){
				clearTimeout(historyTimer);
				historyTimer = null;
			}
			historyTimer=setTimeout("history_play()",1000);
			obj.attr("src",imagesPath+"/begin.png");
		}else{
			clearTimeout(historyTimer);
			historyTimer = null;
			obj.attr("src",imagesPath+"/suspend.png");
		}
	}
}
//播放进度条拖动开始，如果是播放状态则暂停
function historySliderStart(){
	var obj = $("#playStart").find("img");
	var imgSrc = obj.attr("src");
	if(imgSrc.indexOf("suspend.png") == -1){//当前是播放状态
		historyStart();
	}
}
//进度条拖动完成 事假
function historySliderEnd(curValue){
	if(historyData){
		if(curValue > gpsCount){
			curValue = gpsCount;
		}
		gpsIndex = curValue;
		historyStart();
	}else{
		historyClosed();
	}
}

/**
 * 历史轨迹播放加速*2
 * @return
 */
function historyFast(){
	if(historyData){
		var tmp = playSpeed;
		tmp = tmp * 2;
		if(tmp > 16){
			return;
		}
		playSpeed = tmp;
		var playFast = $("#playFast");
		var playLast = $("#playLast");
		playFast.attr("title","x"+tmp);
		if(tmp == 16){
			playFast.attr("disabled","disabled");
		}
		playLast.attr("title","x"+tmp);
		playLast.removeAttr("disabled");
	}
}
/**
 * 历史轨迹播放减速*2
 * @return
 */
function historyLast(){
	if(historyData){
		var tmp = playSpeed;
		tmp = tmp / 2;
		if(tmp < 1){
			return;
		}
		playSpeed = tmp;
		var playFast = $("#playFast");
		var playLast = $("#playLast");
		playFast.attr("title","x"+tmp);
		playFast.removeAttr("disabled");
		
		playLast.attr("title","x"+tmp);
		if(tmp == 1){
			playLast.attr("disabled","disabled");
		}
	}
}
/**
 * 在地图上画围栏
 * @param polygon
 * @return
 */
function addPolygon(obj){
	var region = obj.region;
	var lngLats = region;
	if(lngLats == null || lngLats.length == 0){
		return;
	}
	featuresLayer.removeAllFeatures();
	
	var lngLatArray = lngLats.split(";");
	var pts = new Array();
	for(var i=0;i<lngLatArray.length;i++){
		var lngLat = lngLatArray[i];
		var lng = lngLat.split(",")[0];
		var lat = lngLat.split(",")[1];
		//转成墨卡托
		var xyObj = geoLoc2MeterXY(lng,lat);
		lng = xyObj.lngX;
		lat = xyObj.latY;
		
		var point = new SuperMap.Geometry.Point(lng,lat);
		pts.push(point);
	}
	//根据点生成封闭的环线
	var linearRing = new SuperMap.Geometry.LinearRing(pts);
	//生成多边形
	var newPolygon = new SuperMap.Geometry.Polygon([linearRing]);

	var polygonFeature = new SuperMap.Feature.Vector(newPolygon);
	polygonFeature.style = polygonStyle;
	polygonFeature.attributes = obj;
	featuresLayer.addFeatures(polygonFeature);
	
	//重新计算边界，一屏显示所有车辆
	var bounds = featuresLayer.getDataExtent();
	var zoom = map.getZoomForExtent(bounds,true);
	var center = bounds.getCenterLonLat();
	map.setCenter(center,zoom-1);
}

/**
 * 多边形绘制激活
 * @return
 */
function drawPolygonActive(){
	featuresLayer.removeAllFeatures();
	drawPolygon.activate();
}
/**
 * 多边形绘制 还原未激活状态
 * @return
 */
function drawPolygonDeactive(){
	featuresLayer.removeAllFeatures();
	drawPolygon.deactivate();
}
/**
 * @param drawGeometryArgs
 * @return
 */
function drawPolygonCompleted(drawGeometryArgs){
	featuresLayer.removeAllFeatures();
	var polygonFeature = new SuperMap.Feature.Vector();
	polygonFeature.geometry = drawGeometryArgs.feature.geometry;
	polygonFeature.style = polygonStyle;
	featuresLayer.addFeatures(polygonFeature);
	//取得点坐标
	var polygon = polygonFeature.geometry.components[0];//取得多边形
	var lineRing = polygon.components[0];//取得闭路线
	var points = lineRing.components;//取得点的数组
	
	var array = new Array();
	for(var i=0;i<points.length;i++){
		var point = points[i];
		//墨卡托投影转经纬度坐标并保留小数点后7位
		var lngLat = meterXY2GeoLoc(point.x,point.y,7);
		var tmp = lngLat.lngX+","+lngLat.latY;
		array.push(tmp);
	}
	//curActive在monior.js中 表示当前操作类型
	//regionSearch表示区域查车 alarmFence绘制围栏
	if(curActive == "regionSearch"){
//		debugger
		var bound = polygon.getBounds();
		//in monitor.js 区域查车回调函数
		regionSearch_callBack(array.join(";"),bound.toString());
		drawPolygon.deactivate();
	}else if(curActive == "alarmFence"){
		//in fenceEdit.jsp 添加围栏
		addFence(array.join(";"));
	}
}

//绘制点完成监听事件
function drawPointCompleted(drawGeometryArgs) {
	featuresLayer.removeAllFeatures();
    //取得点坐标
	var point = drawGeometryArgs.feature.geometry;
	var lngX = point.x;
	var latY = point.y;
	var lngLat = meterXY2GeoLoc(lngX,latY,7);
	setLnglat(lngLat.lngX,lngLat.latY);
	var point = new SuperMap.Geometry.Point(lngX,latY);
	var pointFeature = new SuperMap.Feature.Vector(point);
	pointFeature.style = {cursor:"pointer", externalGraphic: imagesPath+"/jpoi.gif", graphicWidth:32, graphicHeight:32,
			graphicXOffset:-16, graphicYOffset:-16, rotation:"", graphicTitle:"", 
			label:"", labelAlign:"cm", labelXOffset:0, labelYOffset:20,
			fontColor:"", fontFamily:"宋体", fontSize:"12px", fontWeight:"bold"};
	//墨卡托投影转经纬度坐标并保留小数点后7位
	featuresLayer.addFeatures(pointFeature);
	drawPoint.deactivate();
	showPointInMap();
}
//画点
function drawGeometryPoint() {
    //先清除上次的显示结果
	//featuresLayer.removeAllFeatures();
	clearHasFeatures();
    drawPoint.activate();
} 
/**
 * 取消绘制点
 * @return
 */
function drawPointDeactive(){
	featuresLayer.removeAllFeatures();
	drawPoint.deactivate();
}
//激活编辑点线面
function editFentureInMap() {
//	selectControll.deactivate();//layer图层选中监听变成未激活状态
//	drawPolygon.deactivate();
//	drawLine.deactivate();
//	drawPoint.deactivate();
	var markerLayerIndex = map.getLayerIndex(markersLayer);
	var featureLayerIndex = map.getLayerIndex(featuresLayer);
	if(markerLayerIndex > featureLayerIndex)
		map.setLayerIndex(featuresLayer,markerLayerIndex+1);
	
	featuresLayer.events.on({"afterfeaturemodified":editFeatureCompleted});
	drawPoint.deactivate();
	modifyFeature.activate();
}
/**
 * 编辑点线面完成
 * @return
 */
function editFeatureCompleted(event){
	var feature = event.feature;
	//取得点坐标
	var points = null;
	if(curActive == "alarmFence"){//围栏
		points = feature.geometry.components[0].components[0].components;//取得多边形
		points = (points==null || points.length == 0)? feature.geometry.components[0].components:points;
	}else if(curActive == "alarmLine"){//线路
		points = feature.geometry.components;
	}else if(curActive == "poiManager"){
		selectControll.activate();
		modifyFeature.deactivate();
		drawGeometryPoint();
	}
	if(points == null || points.length == 0){
		return;
	}
//	selectControll.activate();
	modifyFeature.deactivate();
	map.setLayerIndex(featuresLayer,1);
	var array = new Array();
	for(var i=0;i<points.length;i++){
		var point = points[i];
		//墨卡托投影转经纬度坐标并保留小数点后7位
		var lngLat = meterXY2GeoLoc(point.x,point.y,7);
		var tmp = lngLat.lngX+","+lngLat.latY;
		array.push(tmp);
	}
	
	if(curActive == "alarmFence"){//围栏报警
		//in fenceEdit.jsp 添加围栏
		addFence(array.join(";"));
	}else if(curActive == "alarmLine"){//线路
		//in lineEdit.jsp 添加线路
		addLine(array.join(";"));
	}
}
/** 地图操作部分*/
/**
 * 地图缩小
 */
function mapZoomOut(){
	if(map){
		map.zoomOut();
	}
}
/**
 * 地图放大
 * @return
 */
function mapZoomIn(){
	if(map)
		map.zoomIn();
}
/**
 * 地图测距
 * @return
 */
function mapRuler(){
	initFunction("mapRuler");
	drawLine.activate();
}
/**
 * 线路偏移报警绘制线
 * @return
 */
function drawLineActive(){
	mapFeatureClear();
	drawLine.activate();
}
/**
 * 取消绘制线
 * @return
 */
function drawLineDeactive(){
	featuresLayer.removeAllFeatures();
	drawLine.deactivate();
}
/**
 * 绘制线完成后触发事件
 * @param drawGeometryArgs
 * @return
 */
function drawLineCompleted(drawGeometryArgs){
	var active = curActive;//in main.js 区分当前进行的操作，这里主要是测距mapRuler和线路报警绘制线路alarmLine
	drawLine.deactivate();
	
	var geometry = drawGeometryArgs.feature;
	var pointLength = geometry.geometry.components[0].components.length;
	var ptArray = new Array();//测试使用
	var lineArray = new Array();//线路报警使用
	for(var k = 0; k < pointLength; k++){
		var lngX = geometry.geometry.components[0].components[k].x;
		var latY = geometry.geometry.components[0].components[k].y;
		
		//墨卡托投影转经纬度坐标并保留小数点后7位
		var lngLat = meterXY2GeoLoc(lngX,latY,7);
		var ptLonLat = new SuperMap.LonLat(lngLat.lngX,lngLat.latY);
		ptArray.push(ptLonLat);
		lineArray.push(lngLat.lngX+","+lngLat.latY);
	}
	mapFeatureClear();
	addLineInMap(lineArray.join(";"));
	if(active == "alarmLine"){//绘制报警线路
		
		//in lineEdit.jsp 添加线路
		addLine(lineArray.join(";"));
	}else{//测距		
		iClientMeter = 0;
		
		//计算
		for(var j = 0; j < ptArray.length; j++){
			if(j != (ptArray.length -1)){
				var p = ptArray[j];
				var p1 = ptArray[j+1];
				iClientMeter += SuperMap.Util.distVincenty(p,p1);
			}
		}
		var distance = iClientMeter;
		var distanceinfo = '';
		if(distance * 100 < 100){
			distanceinfo =  parseInt(distance * 100) * 10 + "米";
		}else{
		    distanceinfo = distance.toFixed(2) + "公里";
		}
		var endLonLat = ptArray[ptArray.length-1];
		//转成墨卡托
		var xyObj = geoLoc2MeterXY(endLonLat.lon,endLonLat.lat);
		var endPt = new SuperMap.Geometry.Point(xyObj.lngX,xyObj.latY);
		var endPtFeature = new SuperMap.Feature.Vector(endPt);
		endPtFeature.style = {label:distanceinfo, labelAlign:"rm", labelXOffset:50, labelYOffset:-15,
				fontColor:"red",fontFamily:"宋体", fontSize:"12px", fontWeight:"bold"};
		featuresLayer.addFeatures(endPtFeature);
	}
	
}
/**
 * 在地图上画线路
 * @param line
 * @return
 */
function addLineInMap(line){
	var lngLats = line;
	if(lngLats == null || lngLats.length == 0){
		return;
	}
	
	var lngLatArray = lngLats.split(";");
	var pts = new Array();
	for(var i=0;i<lngLatArray.length;i++){
		var lngLat = lngLatArray[i];
		var lng = lngLat.split(",")[0];
		var lat = lngLat.split(",")[1];
		//转成墨卡托
		var xyObj = geoLoc2MeterXY(lng,lat);
		lng = xyObj.lngX;
		lat = xyObj.latY;
		
		var point = new SuperMap.Geometry.Point(lng,lat);
		var pointFeature = new SuperMap.Feature.Vector(point);
		pointFeature.style  = {fillColor: "#fffff",strokeColor: "#FF0000",pointRadius:6,strokeOpacity:0.5,fillOpacity:0.5};
		featuresLayer.addFeatures(pointFeature);
		pts.push(point);
	}
	var lineFeature = new SuperMap.Feature.Vector();
	lineFeature.style = lineStyle;
	lineFeature.geometry = new SuperMap.Geometry.LineString(pts);
	
	featuresLayer.addFeatures(lineFeature);
	pointsCenter(featuresLayer);
}
/**
 * 截图
 */
function mapScreen(){
	var mapCapturerService = new SuperMap.OSP.Service.MapCapturer();
    mapCapturerService.url = "http://services.supermapcloud.com";
    //组织截图参数
    var getImageParam = new SuperMap.OSP.Service.GetImageParam();
    getImageParam.viewBounds = map.getExtent();
    getImageParam.mapName = "quanguo";
	var level = map.getZoom();
	var scales = SuperMap.OSP.Core._Scales;
    getImageParam.mapScale = scales[level];
    getImageParam.picWidth = parseInt(document.getElementById("mapDiv").style.width.replace("px", ""));
	
    if (!getImageParam.picWidth) {
        getImageParam.picWidth = document.getElementById("mapDiv").offsetWidth;
    }
    getImageParam.features = null;
    getImageParam.poiGroups = null;
    
    //提交服务端获取截图图片url
    mapCapturerService.getImage(getImageParam, function(result){
        if (result) {
            window.open(result);
        }
    }, function(errorResult){
        alert(errorResult.information);
    });
}
//地图打印
function printMap(){
    var printService = new SuperMap.OSP.Service.PrintService();
    printService.printMap();
}
/**
 * 清屏
 * @return
 */
function mapClear(){
	clearHasFeatures4point();
	initFunction("mapClear");
}
/**
 * 清空map地图上的点线面--主要是报警模块使用
 * @return
 */
function mapFeatureClear(){
	if(map == null){
		return;
	}
	clearHasFeatures();
	//绘制多边形变成未激活状态
	drawPolygonDeactive();
	//绘制点变成未激活状态
	drawPointDeactive();
	//绘制线变成未激活状态
	drawLine.deactivate();
	
	selectControll.activate();
}