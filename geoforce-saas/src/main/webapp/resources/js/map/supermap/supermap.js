/**
 * 超图云地图操作类 
 */
//		this.baseLayer = new SuperMap.Layer.TiledDynamicRESTLayer("World", this.layerUrl, 
//				null, {maxResolution:"auto"});		
//		this.baseLayer.events.on({"layerInitialized": this.addLayers}); 
/**
 *创建poi点聚合的图层 
 */

function supermap_createPoiLayer(){
	var poiLayer = new SuperMap.Layer.ClusterLayer("poiCluster",{
		maxLevel:16,
		maxDiffuseAmount: 200,
		clusterStyles:[{
			"count":30,//子节点小于等于30的聚合点
			"style":{
				graphic:true,
		        externalGraphic:imagesPath+"/mapcloud/poiCluster0.png",
                graphicWidth:53,
                graphicHeight:52,
                labelXOffset:0,
                labelYOffset:0
            }
        },
        {
        	"count":60,//子节点小于等于60大于30的聚合点
        	"style":{
        		graphic:true,
        		externalGraphic:imagesPath+"/mapcloud/poiCluster1.png",
                graphicWidth:56,
                graphicHeight:55,
                labelXOffset:0,
                labelYOffset:0
            }
        },
        {
        	"count":90,
        	"style":{
        		graphic:true,
        		externalGraphic:imagesPath+"/mapcloud/poiCluster2.png",
                graphicWidth:66,
                graphicHeight:65,
                labelXOffset:0,
                labelYOffset:0
            }
        },
        {
        	"count":120,
        	"style":{
        		graphic:true,
        		externalGraphic:imagesPath+"/mapcloud/poiCluster3.png",
                graphicWidth:78,
                graphicHeight:77,
                labelXOffset:0,
                labelYOffset:0
            }
        },
        {
        	"count":"moreThanMax",// 子节点大于50的聚合点
        	"style":{
        		graphic:true,
        		externalGraphic:imagesPath+"/mapcloud/poiCluster4.png",
        		graphicWidth:90,
        		graphicHeight:89,
        		labelXOffset:0,
        		labelYOffset:0
        	 }
        }]
	});
	return poiLayer;
}
/**
 * 创建车辆点聚合图层
 * @return
 */
function supermap_createCarLayer(){
	var carLayer = new SuperMap.Layer.ClusterLayer("carCluster",{
		maxLevel:16,
		maxDiffuseAmount:500,
		clusterStyles:[{
			"count":30,//子节点小于等于30的聚合点
			"style":{
				graphic:true,
		        externalGraphic:imagesPath+"/mapcloud/mkCluster3.png",
                graphicWidth:37,
                graphicHeight:38,
                labelXOffset:-4,
                labelYOffset:5
            }
        },
        {
        	"count":60,//子节点小于等于60大于30的聚合点
        	"style":{
        		graphic:true,
        		externalGraphic:imagesPath+"/mapcloud/mkCluster2.png",
                graphicWidth:41,
                graphicHeight:46,
                labelXOffset:-3,
                labelYOffset:6
            }
        },
        {
        	"count":"moreThanMax",// 大于60
        	"style":{
        		graphic:true,
        		externalGraphic:imagesPath+"/mapcloud/mkCluster1.png",
        		graphicWidth:48,
        		graphicHeight:53,
        		labelXOffset:-5,
        		labelYOffset:8
        	 }
        }]
	});
	return carLayer;
}
var supermap = {
//	layerUrl: 'http://172.16.59.29:8090/iserver/services/MapCameroon/rest/maps/Cameroon',
	map: null,//地图对象
	baseLayer: null,//底图图层
	feaLayer: null,//点线面图层
	poiLayer: null,//poi兴趣点专用图层
	carLayer: null,//车辆打点的图层
	popup: null,//弹出框对象
	selectFeature: null,//点击点 线触发事件的control
	poiCluSelect: null,//poi聚合点点击事件
	carCluSelect: null,//车辆点点击事件
	drawPoint: null,//绘制点control
	drawLine: null,//绘制线
	drawCircle:null,//绘制圆
	drawRectangle:null,//绘制矩形
	drawPolygon: null,//绘制区域
	modifyFeature: null,//编辑线 面的feature
	lineStyle:{//线样式
		strokeColor: "#304DBE",strokeWidth: 3,pointerEvents: "visiblePainted",fillColor: "#304DBE",
		fillOpacity: 0.8,pointRadius:3,strokeOpacity:0.8},
	polygonStyle:{//多边形样式
		fillColor:"white", fillOpacity:0.5, strokeColor: "#304DBE", strokeOpacity:0.5, strokeWidth:3},
	calculate:null,//是否计算线路距离 和多边形面积
	zoomBox:null,//拉框放大
	infoWinPoint:null,//弹出框停留在的feature
	init:function(){//地图初始化
		$("div[id*='mapDiv_']").hide();
		$("#mapDiv_supermap").show();//超图地图容器
		
		try{
		if(this.map){
			return;
		}
		this.baseLayer = new SuperMap.Layer.CloudLayer({url: urls.map_img});
		if(this.baseLayer == null){
			return;
		}
		this.feaLayer = new SuperMap.Layer.Vector();
		
		this.poiLayer = supermap_createPoiLayer();
		this.poiLayer.events.on({"clickFeature": function(f){supermap.openInfowindow(f);}});
		this.poiCluSelect = new SuperMap.Control.SelectCluster(this.poiLayer);
		
		this.carLayer = supermap_createCarLayer();
		this.carLayer.events.on({"clickFeature": function(f){supermap.openInfowindow(f);}});
		this.carCluSelect = new SuperMap.Control.SelectCluster(this.carLayer);
		
		this.modifyFeature = new SuperMap.Control.ModifyFeature(this.feaLayer);
		this.selectFeature = new SuperMap.Control.SelectFeature([this.poiLayer,this.carLayer], {onSelect: this.openInfowindow});
		
		this.drawPoint = new SuperMap.Control.DrawFeature(this.feaLayer,SuperMap.Handler.Point);
		this.drawPoint.events.on({"featureadded": this.drawPointCompleted});
		this.drawLine = new SuperMap.Control.DrawFeature(this.feaLayer,SuperMap.Handler.Path, { multi: true });
		this.drawLine.events.on({"featureadded":this.drawLineCompleted});
		this.drawCircle = new SuperMap.Control.DrawFeature(this.feaLayer,SuperMap.Handler.RegularPolygon,{handlerOptions:{sides:50}});
		this.drawCircle.events.on({"featureadded":this.drawCircleCompleted});
//		this.drawRectangle = new SuperMap.Control.DrawFeature(this.feaLayer,SuperMap.Handler.Box);
		this.drawRectangle = new SuperMap.Control.DrawFeature(this.feaLayer,SuperMap.Handler.RegularPolygon);
		this.drawRectangle.events.on({"featureadded":this.drawRectangleCompleted});
		this.drawPolygon = new SuperMap.Control.DrawFeature(this.feaLayer,SuperMap.Handler.Polygon, { multi: true });
		this.drawPolygon.events.on({"featureadded": this.drawPolygonCompleted});
		
		//拉框放大缩小
		this.zoomBox = new SuperMap.Control.ZoomBox({out:false});
		this.map = new SuperMap.Map("mapDiv_supermap", { controls: [
		          new SuperMap.Control.PanZoomBar({showSlider:true}),
		          new SuperMap.Control.ScaleLine(),
		          new SuperMap.Control.Navigation({
				  dragPanOptions: {
		        	  enableKinetic: true
				  }
		          }),
		          this.zoomBox,this.modifyFeature,
		          this.drawPoint,this.drawLine,this.drawPolygon,
		          this.drawCircle,this.drawRectangle]
		});
		//地图缩放结束后事件 每次拉框完后 将zoomBox设置为未激活状态
		this.map.events.on({"zoomend":function(e){supermap.zoomBoxDeactivate();}});
		
		this.map.addLayer(this.baseLayer);
		this.map.addLayer(this.feaLayer);
		this.map.addLayer(this.poiLayer);
		this.map.addLayer(this.carLayer);
		this.map.addControl(this.selectFeature);
		this.map.addControl(this.poiCluSelect);
		this.map.addControl(this.carCluSelect);
		this.selectFeature.activate();
		this.poiCluSelect.activate();
		this.carCluSelect.activate();
		this.calculate = false;
		this.mapCenter();
		
		}catch(e){}
	},
	refreshMap:function(){//刷新地图
		if(!this.map)
			return;
		this.map.updateSize();
	},
	mapCenter:function(){//地图居中
//		var lngLat = new SuperMap.LonLat(12.35, 7.37);
//		supermap.map.setCenter(lngLat,1);
		if(ps_lng){//in taglibs.jsp
			var lngLat = new SuperMap.LonLat(ps_lng,ps_lat);
			lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
			//默认北京
			this.map.setCenter(lngLat,11);
		}else{
			//默认北京
			this.map.setCenter(new SuperMap.LonLat(12958399.4681885, 4852082.44060595),11);
		}
	},
	featureCenter:function(){//图层所有地物一屏显示
		//重新计算边界，一屏显示所有车辆
		var carBounds = this.carLayer.getDataExtent();
		var feaBounds = this.feaLayer.getDataExtent();
		var bounds = new SuperMap.Bounds();
		if(carBounds){
			bounds.extend(carBounds);
		}
		if(feaBounds){
			bounds.extend(feaBounds);
		}
//		if(carBounds && feaBounds){
//			var left,bottom,right,top;
//			left = carBounds.left < feaBounds.left?carBounds.left:feaBounds.left;
//			bottom = carBounds.bottom < feaBounds.bottom?carBounds.bottom:feaBounds.bottom;
//			right = carBounds.right > feaBounds.right?carBounds.right:feaBounds.right;
//			top = carBounds.top > feaBounds.top?carBounds.top:feaBounds.top;
//			bounds = new SuperMap.Bounds(left,bottom,right,top);
//		}else{
//			if(carBounds) bounds = carBounds;
//			if(feaBounds) bounds = feaBounds;
//		}
		var zoom = this.map.getZoomForExtent(bounds,true);
		var center = bounds.getCenterLonLat();
		this.map.setCenter(center,zoom-1);
		
	},
	markerCenter:function(infoObj){//根据传入对象的经纬度聚焦marker
		if(infoObj){
			var x = infoObj.lng,y = infoObj.lat;
			if(x && y){
				var lngLat = new SuperMap.LonLat(x,y);
				lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
				this.map.setCenter(lngLat,17);
			}
		}
	},
	clearAllFeatures:function(){//清空图层所有矢量元素
		if(this.popup){//弹出框存在的话先删除
			this.map.removePopup(this.popup);
		}
		this.feaLayer.removeAllFeatures();
		this.carLayer.removeAllFeatures();
	},
	clearAllPois:function(){//清除所有兴趣点
		if(this.poiLayer)
			this.poiLayer.removeAllFeatures();
	},
	getMarkerById:function(id){//根据id获取对象feature
		var feature = this.feaLayer.getFeatureById("carMarker_"+id);
		if(feature){
			return feature;
		}else{
			feature = this.poiLayer.getFeatureById("poiMarker_"+id);
			return feature;
		}
	},
	openInfowindow:function(feature){//点击地物弹出信息框
		var feaAttr = feature.attributes || {};
		var infoObj = feaAttr.jsonObj;
		if(infoObj == null || infoObj == "")
			return;
		var feaId = null,html = null;;
		var popType = infoObj.popType;
		if(popType == "car"){
			html = getCarOpenHtml(infoObj);//in mapsupport.js
			feaId = infoObj.carId;
			supermap.infoWinPoint = feature;//标记弹出框已打开
		}else if(popType == "person"){//人员点
			html = getPersonOpenHtml(infoObj);//in mapsupport.js
			feaId = infoObj.personId;//poi id
		}else if(popType == "poi"){//门店
			html = getPoiOpenHtml(infoObj);//in mapsupport.js
			feaId = infoObj.id;//poi id
		}else if(popType == "search"){//地图自服务搜索poi点
			html = getSearchPoiOpenHtml(infoObj);
			feaId = feaAttr.id;
		}else{
			return;
		}
		
		if(html){
			var size = new SuperMap.Size(0,0);
			//图标在地图上的偏移量
			var offset = new SuperMap.Pixel(0,0);
			var icon = new SuperMap.Icon("", size, offset);
			if(supermap.popup){//防止弹出多个弹出框
				supermap.map.removePopup(supermap.popup);
			}
			supermap.popup = new SuperMap.Popup.FramedCloud("chicken", 
					feature.geometry.getBounds().getCenterLonLat(),
					size,
					html,
		            icon,
		            true,
		            function(){
						supermap.closeInfowindow(feaId);
					},
					true
			);
			feature.popup = supermap.popup;
			supermap.map.addPopup(supermap.popup);
		}
	},
	closeInfowindow:function(id){//根据id关闭弹出的信息框

		if(supermap.popup){
			supermap.popup.destroy();
			supermap.map.removePopup(supermap.popup);
		}
		supermap.infoWinPoint = null;
	},
	panToPoint:function(pointFea){//判断地图边界移动点超出边界重新聚焦点
		var point = pointFea.geometry;
		//地图当前的边界
		var mapBound = this.map.getExtent();
		//车辆当前点的经纬度坐标
		var lonLat = new SuperMap.LonLat(point.x,point.y);
		//如果边界存在并且当前点超出边界则移动车辆保证显示
		if(mapBound && !mapBound.containsLonLat(lonLat)){		
			this.map.panTo(lonLat);
		}
	},
	addFeatures:function(feaArray){//feature数据在图层上新增矢量元素
		if(feaArray == null || feaArray.length == 0){
			return;
		}
		if(!arrayCheck(feaArray)){//in common.js 判断是否是数组
			feaArray = [feaArray];
		}
		var attr = feaArray[0].attributes || {};
		var markerType = attr.type;
		if(markerType == "poiMarker"){//地图初始化加载poi点
			if(showPoi)//in mapSupport.js
				this.poiLayer.addFeatures(feaArray);
		}else{//添加覆盖物
			var carMarkers =[],otherFeas = [];
			for(var i=0,f; f = feaArray[i];i++){
				if(f.attributes && (f.attributes.type == "carMarker" 
					|| f.attributes.type == "personMarker")){//找到车辆、人员点
					carMarkers.push(f);
				}else{
					otherFeas.push(f);
				}
			}
			if(carMarkers.length > 0) this.carLayer.addFeatures(carMarkers);
			
			if(otherFeas.length > 0) this.feaLayer.addFeatures(otherFeas);
		}
	},
	addCarMarker:function(infoObj){//车辆打点
		infoObj.popType = "car";
		var obj = infoObj;
		var carId = obj.carId,license = obj.license,
		x = obj.lng,y = obj.lat,direction=obj.direction,
		status = obj.status;
		var lngLat = new SuperMap.LonLat(x,y);
		lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
		x = lngLat.lon;
		y = lngLat.lat;
		//默认车辆标签颜色
		var carLabelColor = "blue";
		var carImgUrl = imagesPath+"/mapcloud/carBlue.png";
		if(status && status != 1){//不是在线状态要改成红色
			carLabelColor = "red";
			carImgUrl = imagesPath+"/mapcloud/carRed.png";
		}
		var point = new SuperMap.Geometry.Point(x,y);
		var pointFeature = new SuperMap.Feature.Vector(point);
		if(license){//生成带样式的point的feature
			pointFeature.style = {cursor:"pointer", externalGraphic: carImgUrl, graphicWidth:32, graphicHeight:32,
					graphicXOffset:-16, graphicYOffset:-16, rotation:direction, graphicTitle:license, 
					label:license, labelAlign:"cm", labelXOffset:0, labelYOffset:20,
					fontColor:carLabelColor, fontWeight:"bold",labelSelect:false};
			
			pointFeature.attributes = {
					id:carId,
					type:"carMarker",
					jsonObj:infoObj
			};
			if(this.infoWinPoint && this.infoWinPoint.attributes
					&& this.infoWinPoint.attributes.id == carId){//弹出框已打开
				this.openInfowindow(pointFeature);
			}
		}
		return pointFeature;
		
	},
	addPersonMarker:function(infoObj){//人员打点
		infoObj.popType = "person";
		var obj = infoObj;
		var personId = obj.personId,personName = obj.personName,
		x = obj.lng,y = obj.lat,direction=obj.direction,
		status = obj.status;
		var lngLat = new SuperMap.LonLat(x,y);
		lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
		x = lngLat.lon;
		y = lngLat.lat;
		//默认人员标签颜色
		var labelColor = "blue";
		var personImgUrl = imagesPath+"/mapcloud/personblue.png";
		if(status && status != 1){//不是在线状态要改成红色
			labelColor = "red";
			personImgUrl = imagesPath+"/mapcloud/personred.png";
		}
		var point = new SuperMap.Geometry.Point(x,y);
		var pointFeature = new SuperMap.Feature.Vector(point);
		if(personName){//生成带样式的point的feature
			pointFeature.style = {cursor:"pointer", externalGraphic: personImgUrl, graphicWidth:20, graphicHeight:24,
					graphicXOffset:-10, graphicYOffset:-12, rotation:direction, graphicTitle:personName, 
					label:personName, labelAlign:"cm", labelXOffset:0, labelYOffset:16,
					fontColor:labelColor, fontWeight:"bold",labelSelect:false};
			
			pointFeature.attributes = {
					id:personId,
					type:"personMarker",
					jsonObj:infoObj
			};
			if(this.infoWinPoint && this.infoWinPoint.attributes
					&& this.infoWinPoint.attributes.id == personId){//弹出框已打开
				this.openInfowindow(pointFeature);
			}
		}
		return pointFeature;
	},
	addPoiMarker:function(infoObj,type){//兴趣点打点
		infoObj.popType = "poi";
		var obj = infoObj;
		var id = obj.id,name = obj.name,
		x = obj.ctLng,y = obj.ctLat,ico = obj.ico;
		
		var lngLat = new SuperMap.LonLat(x,y);
		lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
		x = lngLat.lon;
		y = lngLat.lat;
		var point = new SuperMap.Geometry.Point(x,y);
		var pointFeature = new SuperMap.Feature.Vector(point);
		if(type == 0){//地图初始化poi点样式
			pointFeature.style = {	
				cursor:"pointer", externalGraphic: imagesPath+"/poi/"+ico, 
				graphicWidth:25, graphicHeight:25,graphicXOffset:-12, graphicYOffset:-16,graphicTitle:name, 
				label:name, labelAlign:"cm", labelXOffset:0, labelYOffset:-16,fontOpacity:0.5,
				fontWeight:"bold", fontSize:"11px"
			};
			pointFeature.attributes = {
					id:id,
					type:"poiMarker",
					jsonObj:infoObj
			};
		}else{//兴趣点管理的poi样式
			pointFeature.style = {
				cursor:"pointer", externalGraphic: imagesPath+"/jpoi.gif", 
				graphicWidth:32, graphicHeight:32,
				graphicXOffset:-16, graphicYOffset:-16
			};
		}
		return pointFeature;
	},
	/**
	 * wangyajun poi搜索打点
	 */
	addMapSearchPoiMarker:function(infoObj){//poi打点
		infoObj.popType = "search";
		var obj = infoObj;
		var name = obj.addrName,
		x = obj.lng,y = obj.lat,img = obj.img;
		if(obj.routeName != "" && obj.routeName != null && typeof(obj.routeName) != undefined){
			name = obj.routeName;
		}
		var lngLat = new SuperMap.LonLat(x,y);
		lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
		x = lngLat.lon;
		y = lngLat.lat;
		var point = new SuperMap.Geometry.Point(x,y);
		var pointFeature = new SuperMap.Feature.Vector(point);
//		if(obj.flag != "" && obj.flag != null && typeof(obj.flag) != undefined){
//			pointFeature.style = {	
//					cursor:"pointer", externalGraphic: img, 
//					graphicWidth:28, graphicHeight:34,graphicXOffset:-28, graphicYOffset:-34,graphicTitle:name,
//					/*label:name,*/ labelAlign:"cm", labelXOffset:0, labelYOffset:0,fontOpacity:1,
//					fontColor:"#302D21", fontFamily:"宋体", fontSize:"8px"
//			};
//		}else{
//			pointFeature.style = {	
//					cursor:"pointer", externalGraphic: img, 
//					graphicWidth:28, graphicHeight:34,graphicXOffset:-16, graphicYOffset:-18,graphicTitle:name,
//					/*label:name,*/ labelAlign:"cm", labelXOffset:0, labelYOffset:0,fontOpacity:1,
//					fontColor:"#302D21", fontFamily:"宋体", fontSize:"8px"
//			};
//		}
		pointFeature.style = {	
				cursor:"pointer", externalGraphic: img, 
				graphicWidth:28, graphicHeight:34,graphicXOffset:-16, graphicYOffset:-18,graphicTitle:name,
				/*label:name,*/ labelAlign:"cm", labelXOffset:0, labelYOffset:0,fontOpacity:1,
				fontColor:"#302D21", fontFamily:"宋体", fontSize:"8px"
		};
		pointFeature.attributes = {
				id:name,
				type:"searchMarker",
				jsonObj:infoObj
		};
		return pointFeature;
	},
	addLine:function(pointFeas,lineId){//画线
		var lineFeature = this.feaLayer.getFeatureById(lineId);
		var hasPts = new Array();
		var points = new Array();
		var len = pointFeas.length;
		for(var i=0;i<len;i++){
			var point = pointFeas[i].geometry;
			points.push(point);
		}
		if(lineFeature){//轨迹线存在
			hasPts = lineFeature.geometry.components;
		}else{
			lineFeature = new SuperMap.Feature.Vector();
			lineFeature.style = this.lineStyle;
			lineFeature.id = lineId;
		}
		points = hasPts.concat(points);
		lineFeature.geometry = new SuperMap.Geometry.LineString(points);
		
		return lineFeature;
	},
	addHisStartEndMark:function(seObjs){//获取历史轨迹始发点 目的点feature
		
		var seFeas = new Array();
		for(var i=0;i<seObjs.length;i++){
			var obj = seObjs[i];
			var x = obj.lng,y = obj.lat;
			//转成墨卡托
//			var xyObj = geoLoc2MeterXY(x,y);
//			x = xyObj.lngX;
//			y = xyObj.latY;
			var lngLat = new SuperMap.LonLat(x,y);
			lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
			x = lngLat.lon;
			y = lngLat.lat;
			
			var point = new SuperMap.Geometry.Point(x,y);
			var feature = new SuperMap.Feature.Vector(point);
			var imgUrl;
			if(i == 0){
				imgUrl = startImg;
			}else{
				imgUrl = endImg;
			}
			feature.style = {externalGraphic: imgUrl, graphicWidth:40, graphicHeight:40,
					graphicXOffset:-20, graphicYOffset:-45};
			
			seFeas.push(feature);
		}
		return seFeas;
	},
	drawDeactive:function(){
		this.drawCalculate(false);
		this.drawPointDeactive();
		this.drawLineDeactive();
		this.zoomBoxDeactivate();//拉框放大缩小
		this.drawCircleDeactive();
		this.drawRectangleDeactive();
		this.drawPolygonDeactive();
		this.modifyFeature.deactivate();
		this.map.setLayerIndex(this.feaLayer,1);
	},
	drawCalculate:function(isAble){//是否计算距离或面积
		this.calculate = isAble;
	},
	drawPointActive:function(){//绘制点激活
		this.drawPoint.activate();
	},
	drawPointDeactive:function(){//绘制点还原未激活
		this.drawPoint.deactivate();
	},
	drawLineActive:function(){//绘制线激活
		this.drawLine.activate();
	},
	drawLineDeactive:function(){//绘制线还原未激活
		this.drawLine.deactivate();
	},
	zoomBoxActive:function(type){//绘制矩形激活 type=true 拉框放大 type = false 拉框缩小
		if(type){			
			this.zoomBox.out = false;
		}else{
			this.zoomBox.out = true;
		}
		this.zoomBox.activate();
	},
	zoomBoxDeactivate:function(){//绘制矩形还原未激活
		this.zoomBox.deactivate();
	},
	drawCircleActive:function(){//绘制圆形激活
		this.drawCircle.activate();
	},
	drawCircleDeactive:function(){//绘制圆形还原未激活
		this.drawCircle.deactivate();
	},
	drawRectangleActive:function(){//绘制矩形激活
		this.drawRectangle.activate();
	},
	drawRectangleDeactive:function(){//绘制矩形还原未激活
		this.drawRectangle.deactivate();
	},
	drawPolygonActive:function(){//绘制多边形激活
		this.drawPolygon.activate();
	},
	drawPolygonDeactive:function(){//绘制多边形还原未激活
		this.drawPolygon.deactivate();
	},
	editFeatureActive:function(){//编辑feature激活
		this.feaLayer.events.on({"afterfeaturemodified":this.editFeatureCompleted});
		
		this.modifyFeature.activate();
		this.map.setLayerIndex(this.feaLayer,5);
	},
	drawPointCompleted:function(event){//绘制点完成后调用的监听方法
		var marker = event.feature;
		marker.style = {cursor:"pointer", externalGraphic: imagesPath+"/poi/18.png", graphicWidth:32, graphicHeight:32,
			graphicXOffset:-16, graphicYOffset:-16};
		supermap.addFeatures([marker]);
		var point = marker.geometry;
		var xyObj = new Object();
//		var lngLat = meterXY2GeoLoc(point.x,point.y,7);
//		xyObj.x = lngLat.lngX;
//		xyObj.y = lngLat.latY;
		var lngLat = new SuperMap.LonLat(point.x,point.y);
		lngLat.transform("EPSG:3857", "EPSG:4326");//墨卡托转经纬度
		xyObj.x = lngLat.lon;
		xyObj.y = lngLat.lat;
		map_drawPointCompleted(xyObj);
	},
	drawLineCompleted:function(event){//绘制线完成后调用的监听方法
		var line = event.feature;
		var len = line.geometry.components[0].components.length;
		line.style = supermap.lineStyle;
		supermap.addFeatures([line]);
		var array = [],x=null,y=null,tmp = null;
		for(var i=0;i<len;i++){
			x = line.geometry.components[0].components[i].x;
			y = line.geometry.components[0].components[i].y;
			
			var obj = new Object();
//			//墨卡托投影转经纬度坐标并保留小数点后7位
//			tmp = meterXY2GeoLoc(x,y,7);
//			obj.x = tmp.lngX;
//			obj.y = tmp.latY;
			var lngLat = new SuperMap.LonLat(x,y);
			lngLat.transform("EPSG:3857", "EPSG:4326");//墨卡托转经纬度
			obj.x = lngLat.lon;
			obj.y = lngLat.lat;
			array.push(obj);
		}
		if(supermap.calculate){//是否计算线路距离
			var meter = 0,lngLat=null,lngLat1=null;
			for(var i=0;i<array.length;i++ ){
				if(i != (array.length -1)){
					lngLat = new SuperMap.LonLat(array[i].x,array[i].y);
					lngLat1 = new SuperMap.LonLat(array[i+1].x,array[i+1].y);
					meter += SuperMap.Util.distVincenty(lngLat,lngLat1);
				}
			}
			meter = parseInt(meter * 100) * 10 +"米";
			var lastXy = array[array.length-1];
//			//转成墨卡托
//			var xyObj = geoLoc2MeterXY(lastXy.x,lastXy.y);
//			var lastPoint = new SuperMap.Geometry.Point(xyObj.lngX,xyObj.latY);
			var lngLat = new SuperMap.LonLat(lastXy.x,lastXy.y);
			lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
			var lastPoint = new SuperMap.Geometry.Point(lngLat.lon,lngLat.lat);
			var lastFeature = new SuperMap.Feature.Vector(lastPoint);
			lastFeature.style = {label:meter, labelAlign:"rm", labelXOffset:40, labelYOffset:3,
					fontColor:"red",fontFamily:"宋体", fontSize:"12px", fontWeight:"bold"};
			supermap.addFeatures([lastFeature]);
		}
		map_drawLineCompleted(array);//in mapSupport.js 绘制线路回调
		
	},
	drawCircleCompleted:function(event){//绘制圆完成后方法
		var circle = new SuperMap.Feature.Vector();
		circle.geometry = event.feature.geometry;
		circle.style = supermap.polygonStyle;
		supermap.addFeatures([circle]);
		//取得点坐标
		var geometry = circle.geometry.components[0];//取得多边形
		var points = geometry.components;
		var array = [];
		for(var i=0,point;point = points[i];i++){
			var obj = new Object();
//			//墨卡托投影转经纬度坐标并保留小数点后7位
//			var lngLat = meterXY2GeoLoc(point.x,point.y,7);
//			obj.x = lngLat.lngX;
//			obj.y = lngLat.latY;
			var lngLat = new SuperMap.LonLat(point.x,point.y);
			lngLat.transform("EPSG:3857", "EPSG:4326");//墨卡托转经纬度
			obj.x = lngLat.lon;
			obj.y = lngLat.lat;
			array.push(obj);
			console.log("circle:"+obj.x+","+obj.y);
		}
		map_drawCircleCompleted(array);//in mapSupport.js 绘制圆形回调
	},
	drawRectangleCompleted:function(event){//绘制矩形完成后方法
		var rectangle = new SuperMap.Feature.Vector();
		rectangle.geometry = event.feature.geometry;
		rectangle.style = supermap.polygonStyle;
		supermap.addFeatures([rectangle]);
		
		//取得点坐标
		var geometry = rectangle.geometry.components[0];//取得多边形
		var points = geometry.components;
		var array = [];
		for(var i=0,point;point = points[i];i++){
			var obj = new Object();
//			//墨卡托投影转经纬度坐标并保留小数点后7位
//			var lngLat = meterXY2GeoLoc(point.x,point.y,7);
//			obj.x = lngLat.lngX;
//			obj.y = lngLat.latY;
			var lngLat = new SuperMap.LonLat(point.x,point.y);
			lngLat.transform("EPSG:3857", "EPSG:4326");//墨卡托转经纬度
			obj.x = lngLat.lon;
			obj.y = lngLat.lat;
			array.push(obj);
		}
		map_drawRectangleCompleted(array);//in mapSupport.js 绘制圆形回调
	},
	drawPolygonCompleted:function(event){//绘制多边形完成后调用的监听方法
		var polygonFeature = new SuperMap.Feature.Vector();
		polygonFeature.geometry = event.feature.geometry;
		polygonFeature.style = supermap.polygonStyle;
		
		supermap.addFeatures(polygonFeature);
		//取得点坐标
		var polygon = polygonFeature.geometry.components[0];//取得多边形
		var lineRing = polygon.components[0];//取得闭路线
		var points = lineRing.components;//取得点的数组
		
		var array = [];
		for(var i=0;i<points.length;i++){
			var point = points[i];
			var obj = new Object();
//			//墨卡托投影转经纬度坐标并保留小数点后7位
//			var lngLat = meterXY2GeoLoc(point.x,point.y,7);
//			obj.x = lngLat.lngX;
//			obj.y = lngLat.latY;
			var lngLat = new SuperMap.LonLat(point.x,point.y);
			lngLat.transform("EPSG:3857", "EPSG:4326");//墨卡托转经纬度
			obj.x = lngLat.lon;
			obj.y = lngLat.lat;
			array.push(obj);
		}
		map_drawPolygonCompleted(array);//in mapSupport.js 绘制多边形回调
		
	},
	editFeatureCompleted:function(event){//编辑点线面完成函数
		var feature = event.feature;
		var className = feature.geometry.CLASS_NAME;
		var points = null;
		if(className.indexOf("Polygon") != -1){//围栏
			points = feature.geometry.components[0].components[0].components;//取得多边形
			points = (points==null || points.length == 0)? feature.geometry.components[0].components:points;
		}else if(className.indexOf("Line") != -1){//线路
			points = feature.geometry.components;
		}
		if(points == null || points.length == 0){
			return;
		}
		supermap.modifyFeature.deactivate();
		supermap.map.setLayerIndex(supermap.feaLayer,1);
		var array = [],point=null,lngLat = null,obj=null;
		for(var i in points){
			point = points[i];
			obj = new Object();
//			//墨卡托投影转经纬度坐标并保留小数点后7位
//			lngLat = meterXY2GeoLoc(point.x,point.y,7);
//			obj.x = lngLat.lngX;
//			obj.y = lngLat.latY;
			lngLat = new SuperMap.LonLat(point.x,point.y);
			lngLat.transform("EPSG:3857", "EPSG:4326");//墨卡托转经纬度
			obj.x = lngLat.lon;
			obj.y = lngLat.lat;
			array.push(obj);
		}
		//in mapSupport.js 编辑覆盖物回调函数
		map_editFeatureCompleted(array,feature.style.label);
	},
	addPolygon:function(xyObjs,name){//根据经纬度坐标数据在地图上添加围栏
		var pts = new Array();
		for(var i=0;i<xyObjs.length;i++){
			var xyObj = xyObjs[i];
			var x = xyObj.x;
			var y = xyObj.y;
			//转成墨卡托
//			xyObj = geoLoc2MeterXY(x,y);
//			x = xyObj.lngX;
//			y = xyObj.latY;
			var lngLat = new SuperMap.LonLat(x,y);
			lngLat.transform("EPSG:4326", "EPSG:3857");//经纬度转墨卡托
			x = lngLat.lon;
			y = lngLat.lat;
			
			var point = new SuperMap.Geometry.Point(x,y);
			pts.push(point);
		}
		//根据点生成封闭的环线
		var linearRing = new SuperMap.Geometry.LinearRing(pts);
		//生成多边形
		var polygon = new SuperMap.Geometry.Polygon([linearRing]);

		var polygonFeature = new SuperMap.Feature.Vector(polygon);
		
		if(name){
			polygonFeature.style = {//多边形样式
				fillColor:"white", fillOpacity:0.5, 
				strokeColor: "#304DBE", strokeOpacity:0.5,
				strokeWidth:3,label:name};
		}else{
			polygonFeature.style = this.polygonStyle;
		}
			
		this.feaLayer.addFeatures(polygonFeature);
	}
}