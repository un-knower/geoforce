/**
 * 百度地图操作api
 */

var baidu={
	overlayArray:[],//临时数组用于存放除聚合点外的所有覆盖物，清除所有覆盖物时使用
	map: null,//地图对象
	lineStyle:{//线样式
		strokeColor:"#304DBE", strokeWeight:3, strokeOpacity:0.8},
	gonStyle:{//多边形样式
			strokeColor:"#304DBE", strokeWeight:3, strokeOpacity:0.5},
	drawManager:null,//鼠标绘制点线面的对象
	calculate:null,//是否计算线路距离 和多边形面积
	zoomBox:null,//拉框缩放的对象
	carMarkerClusterer:null,//车辆点聚合对象
	poiMarkerClusterer:null,//poi点聚合对象
	infoWinPoint:null,//弹出框marker对象
	init:function(){//地图初始化
		$("div[id*='mapDiv_']").hide();
		$("#mapDiv_baidu").show();
		if(this.map){
			return;
		}
		this.map = new BMap.Map("mapDiv_baidu");
		this.map.enableAutoResize();//自适应容器
		this.map.enableScrollWheelZoom(true);//地图缩放开
		
		this.map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP],anchor: BMAP_ANCHOR_TOP_RIGHT}));     //2D图，卫星图
		this.map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_LEFT}));//缩放条
		this.map.addControl(new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT}));//比例尺
			
		this.drawManager = new BMapLib.DrawingManager(this.map, 
		{
			isOpen: false, //是否开启绘制模式
			drawingType: BMAP_DRAWING_MARKER, //当期绘制模式 点 线 面 圆等
			enableDrawingTool: false,//是否显示绘制工具栏
		    enableCalculate: false,//绘制线 面时 是否测算距离 面积
		    polylineOptions: this.lineStyle,
		    polygonOptions: this.gonStyle
		});
		this.zoomBox = this.createZoomBox(0);//创建拉框缩放
		
		this.drawManager.addEventListener("markercomplete",this.markercomplete);
		this.drawManager.addEventListener("polylinecomplete",this.polylinecomplete);
		this.drawManager.addEventListener("polygoncomplete",this.polygoncomplete);
		this.drawManager.addEventListener("circlecomplete",this.circlecomplete);
		this.drawManager.addEventListener("rectanglecomplete",this.rectanglecomplete);
		this.calculate = false;
		this.mapCenter();
		//点聚合类初始化
		this.poiMarkerClusterer = new BMapLib.MarkerClusterer(this.map,{
			maxZoom: 16
		});
		this.carMarkerClusterer = new BMapLib.MarkerClusterer(this.map,{
			maxZoom: 16,
			styles:[{//车辆聚合点样式
				url: imagesPath+"/mapcloud/mkCluster3.png",
				size:new BMap.Size(37,38),
				anchor:new BMap.Size(11,7)
			},
			{
				url:imagesPath+"/mapcloud/mkCluster2.png",
				size:new BMap.Size(41,46),
				anchor:new BMap.Size(11,10)
			},{
				url:imagesPath+"/mapcloud/mkCluster1.png",
				size:new BMap.Size(48,53),
				anchor:new BMap.Size(13,12)
			},{
				url:imagesPath+"/mapcloud/mkCluster1.png",
				size:new BMap.Size(48,53),
				anchor:new BMap.Size(8,12)
			}]
		});
	},
	refreshMap:function(){//刷新地图	
		if(this.map){
			var bounds = this.map.getBounds();
			this.map.setCenter(bounds.getCenter());
		}
	},
	mapCenter:function(){//地图更具坐标居中
		//默认北京
		if(ps_lng){
			var point = new BMap.Point(ps_lng,ps_lat);
			this.map.centerAndZoom(point,11);
		}else{
			this.map.centerAndZoom("北京");
		}
	},
	featureCenter:function(){//图层所有地物一屏显示
		var pts = [];
		if(this.overlayArray && this.overlayArray.length > 0){
			for(var i=0,overlay; overlay = this.overlayArray[i]; i++){
				if(overlay instanceof BMap.Marker){
					pts.push(overlay.getPosition());
				}else{
					var bounds = overlay.getBounds();
					if(bounds){
						pts.push(bounds.getSouthWest());
						pts.push(bounds.getNorthEast());
					}
				}
			}
		}
		//车辆点
		var carMarkers = this.carMarkerClusterer.getMarkers();
		if(carMarkers){
			for(var i in carMarkers){
				pts.push(carMarkers[i].getPosition());
			}
		}
		this.map.setViewport(pts);
	},
	markerCenter:function(infoObj){//根据对象经纬度获取marker后聚焦
		if(infoObj){
			var x = infoObj.bdLng,y = infoObj.bdLat;
			if(x && y){
				var point = new BMap.Point(x,y);
				this.map.centerAndZoom(point,17);
			}
		}
	},
	clearAllFeatures:function(){//清空图层所有矢量元素
//		this.map.clearOverlays();
		if(this.overlayArray.length > 0){			
			for(var i=0,overlay; overlay = this.overlayArray[i]; i++){
				this.map.removeOverlay(overlay);
			}
		}
		this.map.clearOverlays();
		if(this.carMarkerClusterer)
			this.carMarkerClusterer.clearMarkers();
	},
	clearAllPois:function(){//清除所有兴趣点
		if(this.poiMarkerClusterer)
			this.poiMarkerClusterer.clearMarkers();
	},
	addFeatures:function(overlys){//添加覆盖物
		if(overlys == null){
			return;
		}
		var carMarkers = [],poiMarkers = [];
		for(var i=0,lay; lay = overlys[i]; i++){
			if(lay.attributes && 
				(lay.attributes.type == "carMarker"
				|| lay.attributes.type == "personMarker")){
				carMarkers.push(lay);
			}else if(lay.attributes && lay.attributes.type == "poiMarker"){
				poiMarkers.push(lay);
			}else{//非 marker
				this.map.addOverlay(lay);
				this.overlayArray.push(lay);
			}
		}
		if(carMarkers.length > 0){
			this.carMarkerClusterer.addMarkers(carMarkers);
		}
		if(poiMarkers.length > 0 && showPoi){//showPoi in mapSupport.js
			this.poiMarkerClusterer.addMarkers(poiMarkers);
		}
	},
	panToPoint:function(centerMarker){//地图平移到指定点
		var bounds = this.map.getBounds();
		var center = centerMarker.getPosition();
		if(bounds && !bounds.containsPoint(center)){
			this.map.panTo(center);
		}
	},
	addCarMarker:function(infoObj){//车辆点
		var obj = infoObj;
		var carId = obj.carId,license = obj.license,
		x = obj.bdLng,y = obj.bdLat,direction=obj.direction,
		status = obj.status;
		
		//默认车辆标签颜色
		var carLabelColor = "blue";
		var carImgUrl = imagesPath+"/mapcloud/carBlue.png";
		if(status && status != 1){//不是在线状态要改成红色
			carLabelColor = "red";
			carImgUrl = imagesPath+"/mapcloud/carRed.png";
		}
		
		var point = new BMap.Point(x,y);
		var marker = new BMap.Marker(point);
		if(license){//生成带样式的point的marker
			var icon = new BMap.Icon(carImgUrl,new BMap.Size(32,32));
			icon.setAnchor(new BMap.Size(16,16));
			marker.setIcon(icon);
			var label = new BMap.Label(license,{offset:new BMap.Size(-10,-18)});
			label.setStyle({
				color:carLabelColor,
				fontSize:"12px", 
				fontWeight:"bold",
				border:0,
				textAlign:"center",
				backgroundColor:'transparent'
			});
			marker.setLabel(label);
			var html = getCarOpenHtml(infoObj);//in mapsupport.js
			var infoWin = new BMap.InfoWindow(html);
			infoWin.addEventListener("clickclose",function(){baidu.infoWinPoint = null;});
			
			marker.addEventListener("click",function(){this.openInfoWindow(infoWin);baidu.infoWinPoint = marker;});
			marker.attributes = {
				id:carId,
				type:"carMarker",
				mLabel:label,
				jsonObj:infoObj
			};
			if(baidu.infoWinPoint && baidu.infoWinPoint.attributes.id == carId){//监测到弹出框弹出状态并是上次点击的marker时弹出
				baidu.map.openInfoWindow(infoWin,point);
			}
		}
		return marker;	
	},
	addPersonMarker:function(infoObj){//人员打点
		var obj = infoObj;
		var personId = obj.personId,personName = obj.personName,
		x = obj.bdLng,y = obj.bdLat,direction=obj.direction,
		status = obj.status;
		//默认人员标签颜色
		var labelColor = "blue";
		var personImgUrl = imagesPath+"/mapcloud/personblue.png";
		if(status && status != 1){//不是在线状态要改成红色
			labelColor = "red";
			personImgUrl = imagesPath+"/mapcloud/personred.png";
		}
		var point = new BMap.Point(x,y);
		var marker = new BMap.Marker(point);
		if(personName){//生成带样式的point的feature
			var icon = new BMap.Icon(personImgUrl,new BMap.Size(24,24));
			icon.setAnchor(new BMap.Size(12,12));
			marker.setIcon(icon);
			var label = new BMap.Label(personName,{offset:new BMap.Size(-6,-14)});
			label.setStyle({
				color:labelColor,
				fontSize:"12px", 
				fontWeight:"bold",
				border:0,
				textAlign:"center",
				backgroundColor:'transparent'
			});
			marker.setLabel(label);
			var html = getPersonOpenHtml(infoObj);//in mapsupport.js
			var infoWin = new BMap.InfoWindow(html);
			infoWin.addEventListener("clickclose",function(){baidu.infoWinPoint = null;});
			
			marker.addEventListener("click",function(){this.openInfoWindow(infoWin);baidu.infoWinPoint = marker;});
			marker.attributes = {
				id:personId,
				type:"personMarker",
				mLabel:label,
				jsonObj:infoObj
			};
			if(baidu.infoWinPoint && baidu.infoWinPoint.attributes.id == personId){//监测到弹出框弹出状态并是上次点击的marker时弹出
				baidu.map.openInfoWindow(infoWin,point);
			}
		}
		return marker;
	},
	addPoiMarker:function(infoObj,type){//兴趣点打点
		var obj = infoObj;
		var id = obj.id,name = obj.name,
		x = obj.bdLng,y = obj.bdLat;
		var point = new BMap.Point(x,y);
		var marker = new BMap.Marker(point);
		var icon = null,label = null,html = null,infoWin = null;
		if(type == 0){//地图初始化poi点样式
			icon = new BMap.Icon(imagesPath+"/poi/store_24.png",new BMap.Size(24,24));
			label = new BMap.Label(name,{offset:new BMap.Size(0,25)});
			label.setStyle({
				fontWeight:"bold",
				fontSize:"11px",
				border:0,
				textAlign:"center",
				backgroundColor:'transparent'
			});
			html = getPoiOpenHtml(infoObj);//in mapsupport.js
			infoWin = new BMap.InfoWindow(html);
			marker.addEventListener("click",function(){this.openInfoWindow(infoWin);});
			marker.attributes = {
				id: id,
				type: "poiMarker",
				mLabel:label,
				jsonObj:infoObj
			}
			
		}else{//兴趣点管理的poi样式
			icon = new BMap.Icon(imagesPath+"/jpoi.gif",new BMap.Size(32,32));
			icon.setAnchor(new BMap.Size(16,16));
		}
		if(icon) marker.setIcon(icon);
		
		if(label) marker.setLabel(label);
		
		return marker;
	},
	addLine:function(markers,lineId){//画线
		//找到已有线路线路
		var overlays = this.map.getOverlays();
		var polyline = null;
		var lay = null;
		for(var i in overlays){
			lay = overlays[i];
			if(lay.id == lineId){
				polyline = lay;
			}
		}
		var points = [];
		var haspts = [];
		for(var i in markers){
			var point = markers[i].getPosition();
			points.push(point);
		}
		if(polyline){//轨迹线存在
			haspts = polyline.getPath();	
		}else{
			polyline = new BMap.Polyline([],this.lineStyle);
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
			var x = obj.bdLng,y = obj.bdLat;
			
			var imgUrl;
			if(i == 0){
				imgUrl = startImg;
			}else{
				imgUrl = endImg;
			}
			var point = new BMap.Point(x,y);
			var marker = new BMap.Marker(point);
			
			var icon = new BMap.Icon(imgUrl,new BMap.Size(40,40));
			icon.setAnchor(new BMap.Size(20,40));
			marker.setIcon(icon);
			
			markers.push(marker);
		}
		return markers;
	},
	drawDeactive:function(){//绘制点线面操作不生效
		this.drawCalculate(false);
		this.drawManager.close();//绘制模式关闭
		this.zoomBox.close();//拉框缩放关闭
	},
	drawCalculate:function(isAble){//是否计算距离或面积
		this.calculate = isAble;
	},
	zoomBoxActive:function(type){//拉框缩放激活 type=true 放大 type=false 缩小
		var zoomType;
		if(type){
			zoomType = 0;
		}else{
			zoomType = 1;
		}
		this.zoomBox = this.createZoomBox(zoomType);
		this.zoomBox.open();
			
	},
	createZoomBox:function(zoomType){//创建拉框缩放对象
		this.zoomBox = new BMapLib.RectangleZoom(this.map, 
		{
			followText: "",//开启拉框缩放状态后，鼠标跟随的文字
			zoomType: zoomType,//拉框后放大还是缩小的设置
			autoClose: true,//是否在每次操作后，自动关闭拉框缩放状态
			strokeWeight: 2,//遮盖层外框的线宽
			strokeColor: "red"//线的颜色
		});
		return this.zoomBox;
	},
	drawPointActive:function(){//绘制点激活
		this.drawManager.open();
		this.drawManager.setDrawingMode(BMAP_DRAWING_MARKER);//设置当前绘制类型
		
	},
	drawPointDeactive:function(){//绘制点还原未激活
		
		this.drawManager.close();//绘制模式关闭
	},
	drawLineActive:function(){//绘制线激活
		
		this.drawManager.open();
		this.drawManager.setDrawingMode(BMAP_DRAWING_POLYLINE);//设置当前绘制类型
		if(this.calculate){			
			this.drawManager.enableCalculate();//打开计算线路距离
		}else{
			this.drawManager.disableCalculate();//关闭计算面积
		}
	},
	drawLineDeactive:function(){//绘制线还原未激活
		
		this.drawManager.close();//绘制模式关闭
	},
	drawCircleActive:function(){//绘制圆形激活
		this.drawManager.open();
		this.drawManager.setDrawingMode(BMAP_DRAWING_CIRCLE);//设置当前绘制类型
		if(this.calculate){			
			this.drawManager.enableCalculate();//打开计算线路距离
		}else{
			this.drawManager.disableCalculate();//关闭计算面积
		}	
	},
	drawCircleDeactive:function(){//绘制圆形还原未激活
		this.drawManager.close();
	},
	drawRectangleActive:function(){//绘制矩形激活
		this.drawManager.open();
		this.drawManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);//设置当前绘制类型
		if(this.calculate){			
			this.drawManager.enableCalculate();//打开计算线路距离
		}else{
			this.drawManager.disableCalculate();//关闭计算面积
		}
	},
	drawRectangleDeactive:function(){//绘制矩形还原未激活
		this.drawManager.close();
	},
	drawPolygonActive:function(){//绘制多边形激活
		this.drawManager.open();
		this.drawManager.setDrawingMode(BMAP_DRAWING_POLYGON);//设置当前绘制类型
		if(this.calculate){			
			this.drawManager.enableCalculate();//打开计算线路距离
		}else{
			this.drawManager.disableCalculate();//关闭计算面积
		}
	},
	drawPolygonDeactive:function(){//绘制多边形还原未激活
		this.drawManager.close();//绘制模式关闭
	},
	markercomplete:function(overlay){//绘制点完成后事件
		
	},
	polylinecomplete:function(overlay){//绘制线完成后事件
		var line = overlay;
		if(line){
			var points = line.getPath();
			var point = null,obj = null,array=[];
			for(var i in points){
				point = points[i];
				obj = new Object();
				obj.x = point.lng;
				obj.y = point.lat;
				array.push(obj);
			}
			map_drawLineCompleted(array);//in mapSupport.js 绘制线路回调
		}
//		baidu.overlayArray.push(overlay);
	},
	circlecomplete:function(overlay){//绘制圆完成后事件
		var circle = overlay;
		if(circle){
			baidu.addFeatures([circle]);
			var bounds = circle.getBounds();
			var points = [bounds.getNorthEast(),bounds.getSouthWest(),bounds.getCenter()];
			var array = [];
			for(var i=0,point;point = points[i];i++){
				var obj = new Object();
				obj.x = point.lng;
				obj.y = point.lat;
				array.push(obj);
			}
			map_drawCircleCompleted(array);//in mapSupport.js 绘制圆形回调
		}
	},
	rectanglecomplete:function(overlay){//绘制矩形完成后事件
		var rectangle = overlay;
		if(rectangle){
			baidu.addFeatures([rectangle]);
			var points = rectangle.getPath();
			var point = null,obj = null,array=[];
			for(var i in points){
				point = points[i];
				obj = new Object();
				obj.x = point.lng;
				obj.y = point.lat;
				array.push(obj);
			}
			map_drawRectangleCompleted(array);//in mapSupport.js 绘制矩形回调
		}
	},
	polygoncomplete:function(overlay){//绘制区域完成后事件
		var polygon = overlay;
		if(polygon){
			baidu.addFeatures([polygon]);
			var points = polygon.getPath();
			var point = null,obj = null,array=[];
			for(var i in points){
				point = points[i];
				obj = new Object();
				obj.x = point.lng;
				obj.y = point.lat;
				array.push(obj);
			}
			map_drawPolygonCompleted(array);//in mapSupport.js 绘制多边形回调
		}
	}
}