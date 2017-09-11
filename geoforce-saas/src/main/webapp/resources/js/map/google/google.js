/**
 * 谷歌地图接入操作js
 *
 */
//var map = null;//谷歌mapLibs工具js中使用
var gg = {
	overlayArray:[],//临时数组用于存放除聚合点外的所有覆盖物，清除所有覆盖物时使用
	map:null,
	mapTypeId:null,//地图类型id
	mapProjection:null,
	poiMarkerClusterer:null,//poi聚合类
	carMarkerClusterer:null,//车辆点集合类
	popInfoWindow:null,//marker弹出框对象
	infoWinPoint:null,//弹出框marker对象
	isShowPoi:true,//是否加载poi
	calculate:null,//是否计算线路距离 和多边形面积
	drawManager:null,//鼠标绘制点线面的对象
	zoomBoxType:null,//拉框放大缩小 0 放大 1缩小
	editFeature:null,//正在编辑的对象 包括线路 多边形
	init:function(){
		$("div[id*='mapDiv_']").hide();
		$("#mapDiv_google").show();
		if(this.map){
			return;
		}
		var mapOptions = {
			zoomControl: true,
			zoomControlOptions: {
		      style: google.maps.ZoomControlStyle.DEFAULT
		    },
		    scaleControl: true,
		    mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		this.map = new google.maps.Map(document.getElementById('mapDiv_google'),mapOptions);
		this.drawManager = new google.maps.drawing.DrawingManager({
			drawingControl: false,
			circleOptions:{strokeColor:"#304DBE", strokeWeight:3, strokeOpacity:0.8},
			polylineOptions:{strokeColor:"#304DBE", strokeWeight:3, strokeOpacity:0.8},
			polygonOptions:{strokeColor:"#304DBE", strokeWeight:3, strokeOpacity:0.8},
			rectangleOptions:{strokeColor:"#304DBE", strokeWeight:3, strokeOpacity:0.8},
		});
		google.maps.event.addListener(this.drawManager, 'overlaycomplete', function(event) {
			if (event.type == google.maps.drawing.OverlayType.POLYLINE) {//绘制线完成方法
				gg.drawLineCompleted(event.overlay);
			}else if(event.type == google.maps.drawing.OverlayType.CIRCLE){//绘制圆完成方法
				gg.drawCircleCompleted(event.overlay);
			}else if(event.type == google.maps.drawing.OverlayType.RECTANGLE){//绘制矩形完成方法
				gg.drawRectangleCompleted(event.overlay);
			}else if(event.type == google.maps.drawing.OverlayType.POLYGON){//绘制多边形完成方法
				gg.drawPolygonCompleted(event.overlay);
			}else if(event.type == google.maps.drawing.OverlayType.MARKER){//绘制点完成方法
				gg.drawPointCompleted(event.overlay);
			}
		});
		this.mapTypeId = this.map.getMapTypeId();
		google.maps.event.addListener(this.map,'maptypeid_changed',function(){
			gg.mapTypeId = gg.map.getMapTypeId();
			var active = getCurActive();//in mapSupport.js
			if(active == 'locate'){//定位切换时转换坐标 其它暂时不做
				var carMarkers = gg.carMarkerClusterer.getMarkers();
				if(carMarkers == null || carMarkers.length == 0 ) return;
				var infos = [];
				for(var i=0,carMarker;carMarker = carMarkers[i]; i++){
					var carInfo = carMarker.attributes.jsonObj;
					infos.push(carInfo);
				}
				gg.clearAllFeatures();
				map_addMarks(infos);
			}
			
		});
		this.drawManager.setMap(this.map);
		
		this.mapCenter();
		google.maps.event.addListener(this.map,"projection_changed",function(){
			gg.mapProjection = gg.map.getProjection();
		});
		this.poiMarkerClusterer = new MarkerClusterer(this.map,[],{maxZoom:16});
		this.carMarkerClusterer = new MarkerClusterer(this.map,[],{maxZoom:16,styles:[{
			url: imagesPath+"/mapcloud/mkCluster3.png",
	        width: 37,
	        height: 38,
	        anchor: [6, 12],
		},{
			url: imagesPath+"/mapcloud/mkCluster2.png",
	        width: 41,
	        height: 46,
	        anchor: [10,11],
		},{
			url: imagesPath+"/mapcloud/mkCluster1.png",
	        width: 48,
	        height: 53,
	        anchor: [12,11],
		},{
			url: imagesPath+"/mapcloud/mkCluster1.png",
	        width: 48,
	        height: 53,
	        anchor: [12,7],
		}]});
		this.redrawPoi();
	},
	redrawPoi:function(){//封装当前可视范围经纬度
		if(!this.isShowPoi){
			return;
		}
		
		this.clearAllPois();
		abortPoiLoad();//in mapSupport.js 先中断ajax请求 然后再重新加载
		loadPois({curPage:1});//in mapSupport.js 异步加载poi点	
	},
	refreshMap:function(){//刷新地图	
		if(this.map){
			google.maps.event.trigger(this.map, 'resize');
		}
	},
	mapCenter:function(){//地图更具坐标居中
		var point;
		if(ps_lng){
			point = new google.maps.LatLng(ps_lat,ps_lng);
		}else{
			point = new google.maps.LatLng(39.91,116.41);//默认北京
		}
		this.map.setCenter(point);
		this.map.setZoom(11);
	},
	featureCenter:function(){//图层所有地物一屏显示
		var bounds = new google.maps.LatLngBounds();
		var isCenterFlag = false;
		if(this.overlayArray && this.overlayArray.length > 0){
			for(var i=0,overlay;overlay=this.overlayArray[i];i++){
				if(overlay instanceof google.maps.Marker){//点
					bounds.extend(overlay.getPosition());
				}
				if(overlay instanceof google.maps.Polyline ){//线
					var points = overlay.getPath().getArray();
					for(var j=0,point; point = points[j];j++){
						bounds.extend(point);
					}
				}
				if(overlay instanceof google.maps.Polygon ){//多边形
					var paths = overlay.getPaths().getArray();
					for(var j=0,path; path = paths[j];j++){
						var points = path.getArray();
						for(var k=0,point; point = points[k]; k++){
							bounds.extend(point);
						}
					}
				}
			}
			isCenterFlag = true;
		}
		//车辆点
		var carMarkers = this.carMarkerClusterer.getMarkers();
		if(carMarkers){
			for(var i=0,marker;marker = carMarkers[i]; i++){
				bounds.extend(marker.getPosition());
			}
			isCenterFlag = true;
		}
		if(isCenterFlag){
			this.map.fitBounds(bounds);
			setTimeout(function(){
				var zoom = gg.map.getZoom();
				
				if(zoom > 17) gg.map.setZoom(17);
			},25);
		}
	},
	markerCenter:function(infoObj){//根据对象经纬度获取marker后聚焦
		if(infoObj){
			var x = infoObj.ctLng,y = infoObj.ctLat;
			if(x && y){
				var point = new google.maps.LatLng(y,x);
				this.map.setCenter(point);
				this.map.setZoom(17);
			}
		}
	},
	clearAllFeatures:function(){//清空图层所有矢量元素
		if(this.overlayArray && this.overlayArray.length > 0){
			for(var i=0,overlay;overlay=this.overlayArray[i];i++){
				overlay.setMap(null);
			}
			this.overlayArray.length = 0;
		}
		if(this.carMarkerClusterer){
			this.carMarkerClusterer.clearMarkers();
		}
	},
	clearAllPois:function(){//清除所有兴趣点
		if(this.poiMarkerClusterer)
			this.poiMarkerClusterer.clearMarkers();
	},
	addFeatures:function(overlays){//添加覆盖物
		if(overlays == null){
			return;
		}
		var carMarkers = [],poiMarkers = [];
		for(var i=0,overlay;overlay=overlays[i];i++){
			if(overlay.attributes && overlay.attributes.type == "carMarker"){
				carMarkers.push(overlay);
			}else if(overlay.attributes && overlay.attributes.type == "poiMarker"){					
				poiMarkers.push(overlay);
			}else{//非 聚合点覆盖物
				overlay.setMap(this.map);
				this.overlayArray.push(overlay);//覆盖物添加到临时数组 删除时使用
			}
		}
		if(carMarkers.length > 0){
			this.carMarkerClusterer.addMarkers(carMarkers);
		}
		if(poiMarkers.length > 0 && this.isShowPoi){
			this.poiMarkerClusterer.addMarkers(poiMarkers);
		}
	},
	panToPoint:function(centerMarker){//地图平移到指定点
		var bounds = this.map.getBounds();
		var center = centerMarker.getPosition();
		if(bounds && !bounds.contains(center)){
			this.map.panTo(center);
		}
	},
	addCarMarker:function(infoObj){//车辆点
		var obj = infoObj;
		var carId = obj.carId,license = obj.license,
		direction=obj.direction,status = obj.status;
		
		var x = obj.ctLng,y = obj.ctLat;
		if(this.mapTypeId == 'satellite' || this.mapTypeId == 'hybrid'){//卫星地图用原始坐标
			x = obj.gpsLng;
			y = obj.gpsLat;
		}
		//默认车辆标签颜色
		var carLabelColor = "blue";
		var carImgUrl = imagesPath+"/mapcloud/carBlue.png";
		if(status && status != 1){//不是在线状态要改成红色
			carLabelColor = "red";
			carImgUrl = imagesPath+"/mapcloud/carRed.png";
		}
		
		var latLng = new google.maps.LatLng(y,x);
		var marker = null;
		var me = this;
		if(license){//生成带样式的point的marker
			var labelXOffset = parseInt(license.length/2)*7;
			markerOpts = {
				position: latLng,
				icon: {
					url: carImgUrl,
					anchor: new google.maps.Point(16,16)
				},
				title: license,
				text: license, 
				labelAnchor: new google.maps.Point(labelXOffset,29),
				labelStyle:{
					color: carLabelColor,
					fontSize: '12px',
					fontWeight:"bold"
				}
			};
			marker = this.createMarkerWithLabel(markerOpts);
			
			google.maps.event.addListener(marker, "click", function (e) { 
				if(me.popInfoWindow) me.popInfoWindow.close();
				
				var html = getCarOpenHtml(infoObj);//in mapsupport.js
				me.popInfoWindow = new google.maps.InfoWindow({
			       content: html,
			       pixelOffset: new google.maps.Size(0,16)
			    });
				me.popInfoWindow.open(me.map, marker); 
				me.infoWinPoint = marker;
				google.maps.event.addListener(me.popInfoWindow,'closeclick',function(){
					me.infoWinPoint = null;
				});
			});
			marker.attributes = {
				id:carId,
				type:"carMarker",
				jsonObj:infoObj
			};
			
			if(me.infoWinPoint && me.infoWinPoint.attributes.id == carId){//监测到弹出框弹出状态并是当前车辆时弹出
//				me.popInfoWindow.open(me.map, marker);
				google.maps.event.trigger(marker,'click');
			}
		}else{//生成一个纯粹的marker
			marker = new google.maps.Marker({position: latLng});
		}
		return marker;	
	},
	addPoiMarker:function(infoObj,type){//兴趣点打点
		var obj = infoObj;
		var id = obj.id,name = obj.name,
		x = obj.ctLng,y = obj.ctLat;
		var latLng = new google.maps.LatLng(y,x);
		var markerOpts = null,attributes = null,html = null,infoWin=null;
		if(type == 0){//地图初始化poi点样式
			var labelXOffset = parseInt(name.length/2)*12;
			markerOpts = {
				position: latLng,
				icon: imagesPath+"/poimarker.gif",
				title: name,
				text: name, 
				labelAnchor: new google.maps.Point(labelXOffset,28),
				labelStyle:{
					color: '#302D21',
					fontSize: '11px'
				}
			};
			attributes = {
					id: id,
					type: "poiMarker",
					jsonObj:infoObj
			};
			html = getPoiOpenHtml(infoObj);//in mapsupport.js
		}else{//兴趣点管理的poi样式
			markerOpts = {
				position: latLng,
				icon: {
					url: imagesPath+"/poi/red_marker.gif",
					anchor: new google.maps.Point(11,34)
				}
			};
		}
		var marker = null;
		if(markerOpts.text){//生成带label的marker
//			marker = new google.maps.Marker(markerOpts);
			marker = this.createMarkerWithLabel(markerOpts);
		}else{
			marker = new google.maps.Marker(markerOpts);
		}
		if(attributes){
			marker.attributes = attributes;
		}
		if(html)
			google.maps.event.addListener(marker, "click", function (e) {
				if(gg.popInfoWindow) gg.popInfoWindow.close();
				gg.popInfoWindow = new google.maps.InfoWindow({
			       content: html,
			       pixelOffset: new google.maps.Size(0,8)
			    });
				gg.popInfoWindow.open(this.map, this); 
				gg.infoWinPoint = null;//将车辆的弹出框标记设置成false
			});
		return marker;
	},
	addLine:function(markers,lineId){//画线
		//找到已有线路线路
		var overlays = this.overlayArray;
		var polyline = null;
		for(var i =0,lay; lay = overlays[i]; i++){
			if(lay.id == lineId){
				polyline = lay;
			}
		}
		var points = [];
		var haspts = [];
		for(var i = 0,marker; marker = markers[i]; i++){
			points.push(marker.getPosition());
		}
		if(polyline){//轨迹线存在
			haspts = polyline.getPath().getArray();	
		}else{
			polyline = new google.maps.Polyline({
				clickable: false,
				path: [],
				strokeColor: "#304DBE",
				strokeOpacity: 0.80,
				strokeWeight: 3
			});
			polyline.id = lineId;
		}
		points = haspts.concat(points);
		polyline.setPath(points);
		return polyline;
	},
	addHisStartEndMark:function(seObjs){//获取历史轨迹始发点 目的点	
		var markers = [];
		for(var i in seObjs){
			var obj = seObjs[i];
			var x = obj.ctLng,y = obj.ctLat;
			
			var imgUrl;
			if(i == 0){
				imgUrl = startImg;
			}else{
				imgUrl = endImg;
			}
			var point = new google.maps.LatLng(y,x);
			var marker = new google.maps.Marker({
				clickable: false,
				position: point,
				icon: imgUrl
			});
			
			markers.push(marker);
		}
		return markers;
	},
	addPolygon:function(xyObjs,name){//根据经纬度坐标数据在地图上添加围栏
		var pts = [];
		for(var i=0;i<xyObjs.length;i++){
			var xyObj = xyObjs[i];
			var x = xyObj.x;
			var y = xyObj.y;
			var point = new google.maps.LatLng(y,x);
			pts.push(point);
		}
		var polygon = new google.maps.Polygon({
			map: this.map,
			paths: pts,
			strokeColor: "#304DBE", 
			strokeWeight: 3, 
			strokeOpacity: 0.5
		});
		this.addFeatures([polygon]);
	},
	drawDeactive:function(){
		this.drawCalculate(false);
		this.drawManager.setDrawingMode(null);
		this.zoomBoxType = null;
	},
	drawCalculate:function(isAble){//是否计算距离或面积
		this.calculate = isAble;
	},
	drawPointActive:function(){//绘制点激活
		this.drawManager.setDrawingMode(google.maps.drawing.OverlayType.MARKER);//设置当前绘制类型	
	},
	drawPointDeactive:function(){//绘制点还原未激活
		this.drawManager.setDrawingMode(null);
	},
	drawLineActive:function(){//绘制线激活
		this.drawManager.setDrawingMode(google.maps.drawing.OverlayType.POLYLINE);//设置当前绘制类型	
	},
	drawLineDeactive:function(){//绘制线还原未激活
		this.drawManager.setDrawingMode(null);
	},
	drawCircleActive:function(){//绘制圆形激活
		this.drawManager.setDrawingMode(google.maps.drawing.OverlayType.CIRCLE);//设置当前绘制类型	
	},
	drawCircleDeactive:function(){//绘制圆形还原未激活
		this.drawManager.setDrawingMode(null);
	},
	drawRectangleActive:function(){//绘制矩形激活
		this.drawManager.setDrawingMode(google.maps.drawing.OverlayType.RECTANGLE);//设置当前绘制类型	
	},
	drawRectangleDeactive:function(){//绘制矩形还原未激活
		this.drawManager.setDrawingMode(null);
	},
	drawPolygonActive:function(){//绘制多边形激活
		this.drawManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON);//设置当前绘制类型	
	},
	drawPolygonDeactive:function(){//绘制多边形还原未激活
		this.drawManager.setDrawingMode(null);//绘制模式关闭
	},
	zoomBoxActive:function(type){//拉框缩放激活 type=true 放大 type=false 缩小
		if(type){
			this.zoomBoxType = 0;
		}else{
			this.zoomBoxType = 1;
		}
		this.drawManager.setDrawingMode(google.maps.drawing.OverlayType.RECTANGLE);//设置当前绘制类型
	},
	zoomBoxDeactive:function(){//拉框缩放关闭
		this.zoomBoxType = null;
		this.drawManager.setDrawingMode(null);//绘制模式关闭
	},
	editFeatureActive:function(){//编辑feature激活
		var overlays = this.overlayArray;
		if(!overlays) return;
		for(var i=overlays.length-1; i>-1;i--){
			var overlay = overlays[i];
			if(overlay instanceof google.maps.Polyline//线
				|| overlay instanceof google.maps.Polygon){//多边形
				overlay.setEditable(true);
				this.editFeature = overlay;
				google.maps.event.addListenerOnce(this.map,"dblclick",function(){
					gg.editFeatureCompleted();
				});
				break;
			}
		}
	},
	editFeatureDeactive:function(){//编辑覆盖物关闭
		if(!this.editFeature) return;
		this.editFeature.setEditable(false);
		this.editFeature = null;
	},
	drawPointCompleted:function(marker){//绘制点完成后方法
		if(!marker) return;
		marker.setIcon({
			url: imagesPath+"/poi/red_marker.gif",
			anchor: new google.maps.Point(11,34)
		});
		gg.addFeatures([marker]);
		
		var xyObj = new Object();
		var lngLat = marker.getPosition();
		xyObj.x = lngLat.lng();
		xyObj.y = lngLat.lat();
		map_drawPointCompleted(xyObj);
	},
	drawLineCompleted:function(line){//绘制线完成后方法
		if(!line) return;
		gg.addFeatures([line]);
		var points = line.getPath().getArray();
		var array=[];
		for(var i=0,point; point = points[i]; i++){
			var obj = new Object();
			obj.x = point.lng();
			obj.y = point.lat();
			array.push(obj);
		}
		if(gg.calculate){//是否计算线路距离
			var meter = 0,lngLat=null,lngLat1=null,pointArray=[];
			for(var i=0;i<points.length;i++ ){
				if(i != (points.length -1)){
					lngLat = points[i];
					lngLat1 = points[i+1];
					meter += gg.distanceBetweenPoints(lngLat,lngLat1);
				}
			}
			//保留小数点后两位
			meter = Math.floor(meter*100)/100+"米";
			var label = gg.createMarkerWithLabel({
				position: points[points.length-1],
				text: meter,
				icon: {
					url: ctx+'/images/mapcloud/s.gif',
					anchor: new google.maps.Point(15,20)
				},
				labelStyle:{
					color: "red",
					fontSize: '12px',
					fontWeight:"bold"
				}
			});
			gg.addFeatures([label]);
		}
		map_drawLineCompleted(array);//in mapSupport.js 绘制线路回调
	},
	drawCircleCompleted:function(circle){//绘制圆完成后方法
		if(!circle) return;
		gg.addFeatures([circle]);
		var bounds = circle.getBounds();
		var points = [bounds.getNorthEast(),bounds.getSouthWest(),bounds.getCenter()];
		var array = [];
		for(var i=0,point;point = points[i];i++){
			var obj = new Object();
			obj.x = point.lng();
			obj.y = point.lat();
			array.push(obj);
		}
		map_drawCircleCompleted(array);//in mapSupport.js 绘制圆形回调
	},
	drawRectangleCompleted:function(rectangle){//绘制矩形完成后方法
		if(!rectangle) return;
		gg.addFeatures([rectangle]);
		
		var bounds = rectangle.getBounds();
		if(gg.zoomBoxType == 0 || gg.zoomBoxType == 1){//拉框放大缩小
			if(gg.zoomBoxType == 0){//放大
				gg.map.fitBounds(bounds);
			}else{//缩小
				var mapBounds = gg.map.getBounds();
				var center = bounds.getCenter();
				var south = bounds.getSouthWest();
				var north = bounds.getNorthEast();
				var minLng = center.lng()-north.lng();
				var minLat = center.lat()-north.lat();
				var maxLng = south.lng()-center.lng();
				var maxLat = south.lat()-center.lat();
				var mapSouth = mapBounds.getSouthWest();
				var mapNorth = mapBounds.getNorthEast();
				minLng = mapNorth.lng()-minLng;
				minLat = mapNorth.lat()-minLat;
				maxLng = mapSouth.lng()+maxLng;
				maxLat = mapSouth.lat()+maxLat;
				
				var minPoint = new google.maps.LatLng(minLat,minLng);
				var maxPoint = new google.maps.LatLng(maxLat,maxLng);
				mapBounds.extend(minPoint);
				mapBounds.extend(maxPoint);
				
				gg.map.fitBounds(mapBounds);
			}
			rectangle.setMap(null);
			gg.zoomBoxDeactive();
		}else{//区域查车
			var points = [bounds.getNorthEast(),bounds.getSouthWest(),bounds.getCenter()];
			var array = [];
			for(var i=0,point;point = points[i];i++){
				var obj = new Object();
				obj.x = point.lng();
				obj.y = point.lat();
				array.push(obj);
			}
			map_drawRectangleCompleted(array);//in mapSupport.js 绘制矩形回调
		}
	},
	drawPolygonCompleted:function(polygon){//绘制多边形完成后方法
		if(!polygon) return;
		gg.addFeatures([polygon]);
		
		var paths = polygon.getPaths().getArray();
		var bounds = new google.maps.LatLngBounds();
		for(var i=0,path; path = paths[i];i++){
			var points = path.getArray();
			for(var j=0,point; point = points[j]; j++){
				bounds.extend(point);
			}
		}
		var points = [bounds.getNorthEast(),bounds.getSouthWest(),bounds.getCenter()];
		var array = [];
		for(var i=0,point;point = points[i];i++){
			var obj = new Object();
			obj.x = point.lng();
			obj.y = point.lat();
			array.push(obj);
		}
		map_drawPolygonCompleted(array);//in mapSupport.js 绘制多边形回调
	},
	editFeatureCompleted:function(){//编辑覆盖物完成后调用方法
		var overlay = gg.editFeature;
		if(!overlay) return;
		
		var points = [];
		if(overlay instanceof google.maps.Polyline){//线路编辑
			points = overlay.getPath().getArray();
		}else if(overlay  instanceof google.maps.Polygon){//围栏编辑
			var paths = overlay.getPaths().getArray();
			for(var i in paths){
				points = points.concat(paths[i].getArray());
			}
		}
		gg.editFeatureDeactive();
		if(!points || points.length == 0) return;
		
		var array = [];
		for(var i in points){
			var point = points[i];
			var obj = new Object();
			obj.x = point.lng();
			obj.y = point.lat();
			array.push(obj);
		}
		//in mapSupport.js 编辑覆盖物回调函数
		map_editFeatureCompleted(array);
	},
	/**创建带有文本描述的标注*/
    createMarkerWithLabel:function(markerOption){
    	var id = markerOption.id || null;
    	var position = markerOption.position || null;
    	if(position == null) return;
    	var text = markerOption.text || "";
    	var labelAnchor = markerOption.labelAnchor || new google.maps.Point(-1,15)
    	var labelClass = markerOption.labelClass || "labels";
    	var labelStyle = markerOption.labelStyle || {};
    	var icon = markerOption.icon || null;
    	
    	var color = labelStyle.color || 'navy';
    	var opacity = labelStyle.opacity || 1.0;
    	var borderColor = labelStyle.borderColor || 'white';
    	var fontSize = labelStyle.fontSize || '12px';
    	var fontWeight = labelStyle.fontWeight || 'normal';
        var marker = new MarkerWithLabel({
            position: position,
            draggable: false,
            map: this.map,
            labelContent: text,
            labelAnchor: labelAnchor,
            labelClass: labelClass,
            icon: icon,
            labelStyle: {
        		opacity: opacity,
        		borderColor:borderColor,
        		color:color,
        		fontSize:fontSize,
        		fontWeight:fontWeight,
        		fontFamily:'宋体',
        		whiteSpace:'nowrap'
        	}
         });
        return marker;
    },
	/**两点间距离 p=LngLat*/
	distanceBetweenPoints:function (p1, p2) {
		var R = 6371; // Radius of the Earth in km
		var dLat = (p2.lat() - p1.lat()) * Math.PI / 180;
		var dLon = (p2.lng() - p1.lng()) * Math.PI / 180;
		var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	    Math.cos(p1.lat() * Math.PI / 180) * Math.cos(p2.lat() * Math.PI / 180) *
	    Math.sin(dLon / 2) * Math.sin(dLon / 2);
		var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		//测距单位米
		var d = R * c*1000;
		
		return d;
	}
}
