//引用超图api

/*
document.writeln(link4);
var script3 = '<' + 'script type="text/javascript" src="http://file.supermapcloud.com/api/lib/SuperMap.Web.js"' + '><' + '/script>';
document.writeln(script3);
var script4 = '<' + 'script type="text/javascript" src="http://file.supermapcloud.com/api/lib/SuperMap.Web.iServerJava6R.js"' + '><' + '/script>';
document.writeln(script4);
var script5 = '<' + 'script type="text/javascript" src="http://file.supermapcloud.com/api/lib/SuperMap.Web.iServerJava2.js"' + '><' + '/script>';
document.writeln(script5);
var script6 = '<' + 'script type="text/javascript" src="http://file.supermapcloud.com/api/lib/osp.js"' + '><' + '/script>';
document.writeln(script6);
var script7 = '<' + 'script type="text/javascript" src="http://file.supermapcloud.com/api/lib/MicrosoftAjax.js"' + '><' + '/script>';
document.writeln(script7);

var script8 = '<' + 'script type="text/javascript" src="script/include.js"' + '><' + '/script>';
document.writeln(script8);
*/
	
	var drawPointCommon  = null; //绘制普通点的全局变量
	var drawPointPOI = null; //绘制客户端POI的全局变量
	
	
	var drawPolygonArea = null;//绘制量算面积的多边形的全局变量
	var drawPointCircle = null;//绘制圆的中心点的全局变量
	
	var drawLineCommon = null; //绘制普通线的全局变量
	var drawLineBuffer = null; //绘制缓冲区分析功能的线变量
	
	
	var drawFeaturePoint = null; //绘制地物标记点的全局变量
	var drawFeatureLine = null; //绘制地物标记线的全局变量
	var drawFeaturePolygon = null; //绘制地物标记面的全局变量
	
	var poiManager = null; //客户端poiManager的全局变量
	//范例切换函数
	function selectSample(index, obj){
		jQuery("#div_examples_description ul li").css("display", "none");	//隐藏所有描述
		jQuery("#div_examples_description ul li:eq("+index+")").css("display", "block");	//显示当前选中项的描述信息
		
		if(index == 0){
			//概述
			jQuery("#mapDiv").css("width","500");
			jQuery("#mapDiv").css("heigh","400");
			var div = jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:500px;' src='samplesCode/map/map.html'></iframe>");
		}
	
		if(index == 1){
			//地图浏览
			//clearMap();
			jQuery("#mapDiv").css("width","500");
			jQuery("#mapDiv").css("heigh","400");
			var div = jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:500px;' src='samplesCode/map/map.html'></iframe>");
			//showMap();
			//document.getElementById("sourceCodeFrame").src="samples/map/map_info.html";
		}
		
		if(index == 2){
			//地图操作
			//clearMap();
			//showMap();
			//清除上一级操作
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:700px;' src='samplesCode/action/action.html'></iframe>");
		}
		if(index == 3){
			//地物标记
			jQuery("#mapDiv").css("width","650");
			jQuery("#mapDiv").css("heigh","420");
			var div = jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:650px;' src='samplesCode/addFeatures/addFeatures.html'></iframe>");
		}
		
		if(index == 4){
			//关联数据库的POI管理
			//clearMap();
			//loadMapPOI();
			/*document.getElementById("operatePanelPoiDB").style.display = "block";
			document.getElementById("divInfoDB").style.display = "block";
			//document.getElementById("sourceCodeFrame").src="samples/poiManager/poiManager2_info.html";
		}else{
			document.getElementById("operatePanelPoiDB").style.display = "none";
			document.getElementById("divInfoDB").style.display = "none";*/
		}
		
		if(index == 5){
			//关键字搜索
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:715px;' src='samplesCode/poiSearch/searchByKeyword.html'></iframe>");
		}
		
		if(index == 6){
			//几何对象搜索
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:715px;' src='samplesCode/poiSearch/searchByGeometry.html'></iframe>");
		}
		
		if(index == 7){
			//缓冲区分析
			clearMap();
			loadMap2();
			document.getElementById("analysisBufferDiv").style.display = "block";
			document.getElementById("divBufferAnalysis").style.display = "block";
			//document.getElementById("sourceCodeFrame").src="samples/spatialAnalyst/bufferAnalyst_info.html";
		}else{
			document.getElementById("analysisBufferDiv").style.display = "none";
			document.getElementById("divBufferAnalysis").style.display = "none";
		}
		if(index == 8){
			//路径分析
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:715px;' src='samplesCode/findPath/findPath.html'></iframe>");
		}
		
		if(index == 9){
			//地图打印
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:715px;' src='samplesCode/printMap/printMap.html'></iframe>");
		}
		
		if(index == 10){
			//地图测量
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:715px;' src='samplesCode/measure/measure.html'></iframe>");
		}
		if(index == 11){
			//地址匹配
			jQuery("#mapDiv").css("width","700");
			jQuery("#mapDiv").css("heigh","420");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:405px;width:715px;' src='samplesCode/geocoding/geocoder.html'></iframe>");
			//document.getElementById("sourceCodeFrame").src="samples/geocoder/geocoder_info.html";
		}else{
			document.getElementById("geocoderDiv").style.display = "none";
			document.getElementById("divGeocoder").style.display = "none";
		}
		if(index == 12){
			//圆选查询
			jQuery("#mapDiv").css("width","500");
			jQuery("#mapDiv").css("heigh","400");
			jQuery("#mapDiv").html("<iframe style='border:solid 0px gray;height:400px;width:500px;' src='samplesCode/poiSearch/searchByNear.html'></iframe>");
		}
		
		/*if(index == 8){
			//缓冲区搜索
			clearMap();
			showMap();
			document.getElementById("searchByBufferDiv").style.display = "block";
			document.getElementById("divBufferSearch").style.display = "block";
			document.getElementById("sourceCodeFrame").src="samples/search/queryByBuffer_info.html";
		}else{
			document.getElementById("searchByBufferDiv").style.display = "none";
			document.getElementById("divBufferSearch").style.display = "none";
		}*/
		/*
		if(index == 10){
			//地图截屏
			clearMap();
			showMap();
			document.getElementById("mapCaptureDiv").style.display = "block";
			document.getElementById("divMapCut").style.display = "block";
			document.getElementById("sourceCodeFrame").src="samples/mapCapturer/mapCapturer_info.html";
		}else{
			document.getElementById("mapCaptureDiv").style.display = "none";
			document.getElementById("divMapCut").style.display = "none";
		}
		*/
		/*if(index == 11){
			//地图编辑
			clearMap();
			loadMap3();
			if( drawPointCommon ){
				drawPointCommon.remove_actionCompleted(drawCompleted);
			}
			if(drawPointPOI){
				drawPointPOI.remove_actionCompleted(bufferAnalyst);
			}
			document.getElementById("divMapEdit").style.display = "block";
			document.getElementById("editMapDiv").style.display = "block";
			document.getElementById("sourceCodeFrame").src="samples/dataManager/dataManager_info.html";
		}else{
			document.getElementById("divMapEdit").style.display = "none";
			document.getElementById("editMapDiv").style.display = "none";
		}*/
		/*
		if(index == 15){
			//地图共享
			clearMap();
			showMap();
			document.getElementById("divShareMap").style.display = "block";
			document.getElementById("shareMapDiv").style.display = "block";
			document.getElementById("sourceCodeFrame").src="samples/shareMap/shareMap_info.html";
		}else{
			document.getElementById("shareMapDiv").style.display = "none";
			document.getElementById("divShareMap").style.display = "none";
		}
		*/
		/*if(index == 16){
			//统计分析
			clearMap();
			showMap();
			document.getElementById("statisticeDiv").style.display = "block";
			document.getElementById("divStatistic").style.display = "block";
			document.getElementById("sourceCodeFrame").src="samples/statistics/statistics_info.html";
		}else{
			document.getElementById("statisticeDiv").style.display = "none";
			document.getElementById("divStatistic").style.display = "none";
		}*/
	}
	
	//清空地图容器
	function clearMap(){
		//document.getElementById("mapDiv").innerHTML = "";
		
		if(featuresLayer!=null){
			featuresLayer.removeAllFeatures();
		}
		if(poiGroup!=null){
			poiGroup.clearPOIs();
		}
		if(poiSearchGroup!=null){
			poiSearchGroup.clearPOIs();
		}
		if(poiManager!=null){
			poiManager.refreshPOI();
		}
		if(map != null){
			//map.removeLayer(markerLayer);
			//markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, { bounds: map.get_bounds() }, null, null, null);
			//map.addLayer(markerLayer);
		}
		
		poiSearchGroup = null;
		poiManager = null;
		
		poiIndex = 0; //POI的编号索引
        poiGroup = null;
        poiIds = null;
		poiGroupIndex = 0;
		/*
		drawPointCommon  = null; //绘制普通点的全局变量
		drawPointPOI = null; //绘制客户端POI的全局变量
		poiManager = null; //客户端poiManager的全局变量
		
		drawLineCommon = null; //绘制普通线的全局变量
		drawLineBuffer = null; //绘制缓冲区分析功能的线变量
		*/
	}
	
	//var map = null;
	var markerLayer = null;
	var featuresLayer = null;
	var layer = null;
	
	/*----------地图显示函数--begin---------*/
	// function showMap(){
			// layer = new SuperMap.OSP.Core.CloudLayer();
			// layer.resolutions = [156697.2575744, 78348.6287872, 39174.3143936, 19587.1571968, 9793.5785984, 4896.7892992, 2448.3946496, 1224.1973248, 612.0986624, 306.0493312, 153.0246656, 76.5123328, 38.2561664, 19.1280832, 9.5640416, 4.7820208, 2.3910104, 1.1955052, 0.5977526];
			// markerLayer = new SuperMap.Layer.Markers("maker");
			// featuresLayer = new SuperMap.Layer.Vector();
			// drawLine = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Path, { multi: true });
			// drawLine.events.on({"featureadded": drawCompleted});
			// drawPoint = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Point, { multi: true });
			// drawPolygon = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Polygon, { multi: true });
			 // map = new SuperMap.Map("mapDiv", { controls: [
                      // new SuperMap.Control.PanZoomBar(),
                      // new SuperMap.Control.Navigation({
                          // dragPanOptions: {
                              // enableKinetic: true
                          // }
                      // }),drawLine,drawPoint,drawPolygon], allOverlays: true
            // });
			// map.addLayer(layer);
			// map.addLayer(featuresLayer);
			// map.addLayer(markerLayer);
			// map.setCenter(new SuperMap.LonLat(12958399.4681885, 4852082.44060595),11);
	// }
	/*----------地图显示函数--end----------*/
	
	/*----------地图操作----begin-----------*/
	function setZoomIn(){
		 var zoomInAction = $create(SuperMap.Web.Actions.ZoomIn, { map: map }, null, null, null);
            map.set_action(zoomInAction);
	}
	//缩小
    function setZoomOut() {
        var zoomOutAction = $create(SuperMap.Web.Actions.ZoomOut, { map: map }, null, null, null);
        map.set_action(zoomOutAction);
    }
	 //平移地图
    function setPan() {
        var panAction = $create(SuperMap.Web.Actions.Pan, { map: map }, null, null, null);
        map.set_action(panAction);
    }
	//绘制点
    function drawPointEntity() {
		// if(drawPoint){
			// drawPoint.remove_actionCompleted(drawCompleted);
			// drawPoint.dispose();
		// }
        drawPointCommon = $create(SuperMap.OSP.UI.Actions.DrawPoint, { map: map }, null, null, null);
		// drawPoint = drawAction;
        drawPointCommon.add_actionCompleted(drawCompleted);
        map.set_action(drawPointCommon);
    }
	 //绘制线
    function drawLineEntity() {
		drawLineCommon = $create(SuperMap.OSP.UI.Actions.DrawLine, { map: map }, null, null, null);
            drawLineCommon.add_actionCompleted(drawCompleted);
            map.set_action(drawLineCommon);
    }
	//绘制面
    function drawPolygonEntity() {
		// if(drawPoint){
			// drawPoint.remove_actionCompleted(drawCompleted);
		// }
		drawPolygonArea = $create(SuperMap.OSP.UI.Actions.DrawPolygon, { map: map }, null, null, null);
            drawPolygonArea.add_actionCompleted(drawPolygonCompleted);
            map.set_action(drawPolygonArea);
    }
	//将绘制的图形加载到矢量要素图层上。
	function drawCompleted(drawGeometryArgs){
            var fillColor = document.getElementById("color1").value;
            if (fillColor.indexOf("#") != 0) {
                alert("填充颜色为非法值!");
                return false;
            }
            var strokeColor = document.getElementById("color2").value;
            if (strokeColor.indexOf("#") != 0) {
                alert("边线颜色为非法值!");
                return false;
            }
            var opacity = document.getElementById("txtOpacity").value;
            if (isNaN(opacity)) {
                alert("透明度的范围在【0-1.0】之间!");
                return false;
            }
            var lineWidth = document.getElementById("txtLineWidth").value;
            if (isNaN(lineWidth)) {
                alert("边线宽度应该是数值型");
                return false;
            }
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
            style.stroke = true;
            style.strokeColor = strokeColor;
            style.fill = true;
            style.fillColor = fillColor;
            style.opacity = opacity;
            style.lineWidth = parseInt(lineWidth);
            feature.geometry = drawGeometryArgs.geometry;
            feature.style = style;
            featuresLayer.addFeature(feature);
    }	
	var polygons = [];
		 //将绘制的图形加载到矢量要素图层上。绘制点、线和面结束后传回的回调函数中都是DrawGeometryArgs类型的参数，而绘制圆后传回的回调函数的参数为 DrawCircleArgs，因此以下这个函数不能将圆加载到矢量图层上
        function drawPolygonCompleted(drawGeometryArgs) {
			if(jQuery("div[id^='divMeasureArea_']")){
				jQuery("div[id^='divMeasureArea_']").remove();
			}
            var fillColor = document.getElementById("color1").value;
            if (fillColor.indexOf("#") != 0) {
                alert("填充颜色为非法值!");
                return false;
            }
            var strokeColor = document.getElementById("color2").value;
            if (strokeColor.indexOf("#") != 0) {
                alert("边线颜色为非法值!");
                return false;
            }
            var opacity = document.getElementById("txtOpacity").value;
            if (isNaN(opacity)) {
                alert("透明度的范围在【0-1.0】之间!");
                return false;
            }
            var lineWidth = document.getElementById("txtLineWidth").value;
            if (isNaN(lineWidth)) {
                alert("边线宽度应该是数值型");
                return false;
            }
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
            style.stroke = true;
            style.strokeColor = strokeColor;
            style.fill = true;
            style.fillColor = fillColor;
            style.opacity = opacity;
            style.lineWidth = parseInt(lineWidth);
            feature.geometry = drawGeometryArgs.geometry;
			if(polygons.length == 0){
				feature.id = 1;
			}
			else{
				feature.id = polygons.length + 1;
			}
            feature.style = style;
            featuresLayer.addFeature(feature);
			polygons.push(feature);
				//面积量算
				var region = drawGeometryArgs.geometry;
				var pois = region.parts[0];
					for(var i=0, len = pois.length ; i < len; i++){
						//将线段的点的坐标变成经纬度
						pois[i] = SuperMap.OSP.Core.Utility.metersToLatLon(pois[i]);
					}
				//服务地址
				var serverUrl = getMeasureService();
				var unit = new SuperMap.Web.Core.Units().meter;
				var measureParameters = new SuperMap.Web.iServerJava6R.MeasureParameters(region, unit);
				var measureService = new SuperMap.Web.iServerJava6R.MeasureService(serverUrl);
				//设置设置量算模式 MeasureMode，包括距离量算模式0和面积量算模式1
				measureService.set_measureMode(1);
				measureService.add_processCompleted(function(result) {
					var measureArea = result.get_result().get_area().toFixed(1);
					/*
					var divArea = document.getElementById("divMeasureArea");
					divArea.innerHTML = "面积为：";
					divArea.innerHTML += measureArea;
					divArea.innerHTML += "平方米";
					*/
					var divArea = document.createElement("div");
					divArea.id = "divMeasureArea_"+feature.id;
					divArea.setAttribute("target", feature.id);
					divArea.style.display = "block";
					var offset = jQuery("#"+feature.id).offset();
					var styleData = "position: absolute; z-index: 2; background-color: #CCFF00";
					//设置追加的div属性
					if (Sys.Browser.name != "Microsoft Internet Explorer") {
						divArea.setAttribute("style", styleData);
					}else{
						divArea.style.setAttribute("cssText", styleData);
					}
					divArea.style.left = offset.left;
					divArea.style.top = offset.top;
					document.getElementById("mapDiv").appendChild(divArea);
					
					divArea.innerHTML = "面积为：";
					divArea.innerHTML += measureArea;
					divArea.innerHTML += "平方米";
					map.add_viewBoundsChanged(function() {
						jQuery("div[id^='divMeasureArea_']").remove();
					});
				});
				measureService.processAsync(measureParameters);
				//debugger;
        }
	//添加比例尺
	function addScaleBar(){
            //单线比例尺控件样式
            var simpleScaleBarStyle = new SuperMap.Web.Controls.ScaleBarStyle();
            simpleScaleBarStyle.color = "black";
            simpleScaleBarStyle.fontSize = 1;
            simpleScaleBarStyle.mode = SuperMap.Web.Controls.ScaleBarMode.SIMPLE;

            //单线比例尺控件
            var simpleScaleBar = $create(SuperMap.Web.Controls.ScaleBar, { scaleBarStyle: simpleScaleBarStyle, map: map }, null, null, document.getElementById("simpleScaleBarContainer"));

    }	
	 
	 //编辑操作，必设指定编辑的地图和矢量图层
    function editEntity() {
		 var drawAction = $create(SuperMap.Web.Actions.EditFeature, { map: map, featuresLayer: featuresLayer }, null, null, null);
            //drawAction.add_actionCompleted(function (arg) { debugger});
            map.set_action(drawAction);
    }
	
	//清除所有要素
    function clearFeatures() {
        featuresLayer.clearFeatures();
		featuresLayer.refresh();
    }

    //获取地图当前可视范围
    function getMapViewBounds() {
        var objViewBounds = document.getElementById("divViewBounds");
        var view = map.get_viewBounds();
        objViewBounds.innerHTML = "LeftBottom坐标为,<br/>x:" + view.leftBottom.x.toFixed(6) + "<br/>y:" + view.leftBottom.y.toFixed(6) + "<br/>";
        objViewBounds.innerHTML += "RightTop坐标为，<br/>x:" + view.rightTop.x.toFixed(6) + ",<br/>y:" + view.rightTop.y.toFixed(6);
    }
	
	//调整窗口大小
    function mapResize() {
        var width = document.getElementById("txtWidth").value;
        var height = document.getElementById("txtHeight").value;
        if (isNaN(width) || isNaN(height)) {
            alert("宽和高必须为数值类型");
            return false;
        }
        map.resize(parseInt(width), parseInt(height));
    }
    
	//恢复窗口大小
    function mapRecovery() {
        map.resize(500, 400);
    }

    var isshow = false;
    function showLegend() {
         if (isshow) {
            document.getElementById("maplegend").style.display = "none";
            isshow = false;
        } else {
            document.getElementById("maplegend").style.display = "block";
            isshow = true;
        }
    }
	//根据正确的半径绘制圆
	//鼠标点击添加一个圆的中点
			function drawCircleEntity() {
				drawPointCircle = $create(SuperMap.Web.Actions.DrawPoint, { map: map }, null, null, null);
				drawPointCircle.add_actionCompleted(drawCircleCompleted);
				map.set_action(drawPointCircle);
			}
			//根据起点和终点生成线段
			function getGeoLine(center, radius){
				var d = Math.PI * 2;
				var lineP = new SuperMap.Web.Core.GeoLine();
				var sidePoints = [];
				sidePoints.push(center);
				var x = center.x + radius * Math.cos(d);
				var y = center.y + radius * Math.sin(d);
				var point = new SuperMap.Web.Core.Point2D(x, y);
				sidePoints.push(point);
				lineP.parts = [];
				lineP.parts[0] = sidePoints;
				return lineP;		
			}
			
			//根据中心点及半径计算圆
			function markRoundGeometry(center, radius){
				var d360 = Math.PI * 2;
				var roundRegion = new SuperMap.Web.Core.GeoRegion();
				var sidePoints = [];
				var n = 36;
				var d = d360 / n;
				for(var i = 1; i <= n; i++){
					var rd = d * i;
					var x = center.x + radius * Math.cos(rd);
					var y = center.y + radius * Math.sin(rd);
					var sidePoint = new SuperMap.Web.Core.Point2D(x, y);
					sidePoints.push(sidePoint);
				}
				roundRegion.parts = [];
				roundRegion.parts[0] = sidePoints;
				return roundRegion;
			}
			
			//画圆
			function drawCircleCompleted(drawGeometryArgs){
				var center = new SuperMap.Web.Core.GeoPoint();
				center = drawGeometryArgs.geometry;
				var txtRadius = document.getElementById("circleRadius").value;
				var line = new SuperMap.Web.Core.Feature();
					line.geometry = getGeoLine(center, txtRadius);
					//debugger;
					var pois = line.geometry.parts[0];
					for(var i=0, len = pois.length ; i < len; i++){
						//将线段的点的坐标变成经纬度
						pois[i] = SuperMap.OSP.Core.Utility.metersToLatLon(pois[i]);
					}
				//服务地址
				var serverUrl = getMeasureService();
				var unit = new SuperMap.Web.Core.Units().meter;
				var measureParameters = new SuperMap.Web.iServerJava6R.MeasureParameters(line.geometry, unit);
				var measureService = new SuperMap.Web.iServerJava6R.MeasureService(serverUrl);
				
				measureService.add_processCompleted(function(result) {
					measureDistance = result.get_result().get_distance().toFixed(1);
					
					//利用比例将地图上要显示的正确圆半径的半径值折算出来
					var realRadius = (txtRadius * txtRadius / measureDistance);
					var circle = new SuperMap.Web.Core.Feature();
					circle.geometry = markRoundGeometry(center, realRadius);
					var style = new SuperMap.Web.Core.Style();
					style.stroke = true;
					style.strokeColor = 1;
					style.strokeColor = "#0000FF";
					style.fill = true;
					style.fillColor = "#00FFCC";
					style.opacity = 0.7;
					style.lineWidth = 2;
					circle.style = style;
					circle.id = 0;
					circle.title = "title";
					featuresLayer.addFeature(circle);
				});
				measureService.processAsync(measureParameters);
			}
	/*----------地图操作----end-----------*/
    var poiIndex = 0; //POI的编号索引
        var poiGroup = null;
        var poiIds = null;
		var poiGroupIndex = 0;
	
	/*----------客户端POI管理----begin-----------
	//客户端POI管理
		var poiIndex = 0; //POI的编号索引
        var poiGroup = null;
        var poiIds = null;
		var poiGroupIndex = 0;
		//地图加载
		/*function loadMapPOI(){
			map = getMap("mapDiv");
            markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, { bounds: map.get_bounds() }, null, null, null);
            map.addLayer(markerLayer);
            var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "images/controls/compass.png"});
            var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});
            var controls = new Array(compass, scaleBar);
            map.addControls(controls);
		}
        
		//清除所有POI
        function clearPOI_User() {
            if (poiIds && poiIds.length > 0) {
                for (var i = 0; i < poiIds.length; i++) {
                    poiGroup.removePOIs(poiIds[i]);
                }
                poiIds = null;
                poiIds = [];
                poiManager.refreshPOI();
            }
        }

        //鼠标点击添加一个POI，并调用地图编辑接口在数据库里添加一个点对象
        function addPOIByMouse() {
            drawPointPOI = $create(SuperMap.Web.Actions.DrawPoint, { map: map }, null, null, null);
            drawPointPOI.add_actionCompleted(addPOIHandler);
            map.set_action(drawPointPOI);
		}
		function addPOIHandler(arg){
            addPOI(arg.geometry.x, arg.geometry.y);
		}
        function addPOI(x, y, type) {			
            //初始化POI管理参数
			if(poiManager==null){
				poiManager = new SuperMap.OSP.UI.POIManager(map);
				poiManager.markerLayer = markerLayer;
				poiGroup = new SuperMap.OSP.UI.POIGroup("osp_poigroup_id");
				poiGroup.caption = "测试POI分组";
				poiGroup.scaledContent = new SuperMap.OSP.UI.ScaledContent();
				poiGroup.scaledContent.content = "<img src='http://www.supermapcloud.com/demo/china/images/blue.png' />";
				poiManager.addPOIGroup(poiGroup);
				poiIds = [];
            }
			
            var point2d = new SuperMap.Web.Core.Point2D(x, y);
            var title = document.getElementById("txtPinName").value;
            var poi = new SuperMap.OSP.UI.POI("osp_poi" + poiIndex);
            poiIds.push("osp_poi" + poiIndex++);
            poi.position = point2d;

            var pin = "1";
            var pinRadio = document.getElementsByName("radioPin");
            for (var i = 0; i < pinRadio.length; i++) {
                if (pinRadio[i].checked) {
                    pin = pinRadio[i].value;
                    break;
                }
            }
            var scaledContent = new SuperMap.OSP.UI.ScaledContent();
            if (pin == "1") {
                scaledContent.content = "<img src='http://www.supermapcloud.com/demo/china/images/pin1.png' />";
                scaledContent.offset = new SuperMap.Web.Core.Point(5, 17);
            } else if (pin == "2") {
                scaledContent.content = "<img src='http://www.supermapcloud.com/demo/china/images/pin2.png' />";
                scaledContent.offset = new SuperMap.Web.Core.Point(9, 15);
            } else if (pin == "3") {
                scaledContent.content = "<img src='http://www.supermapcloud.com/demo/china/images/pin3.png' />";
                scaledContent.offset = new SuperMap.Web.Core.Point(8, 8);
            }
            //如果是文本
            if (type == "lable") {
                scaledContent.content = title;
            }
            poi.scaledContents = scaledContent;
            poi.title = title;
            poi.addEventListerner("click", function(e) {
                var osp_poi = eval('(' + e.get_info() + ')');
                var strHtml = 'POI标题：' + osp_poi.title;
                strHtml += '<br/><div style="width:99%; height:50px;background-color:#E4EDF3"><a href="javascript:movePoi(\'' + osp_poi.gid + '\',\'' + osp_poi.pid + '\')">移动</a></div>';
                var location = new SuperMap.Web.Core.Point2D(e._x, e._y);
                //打开弹窗信息框
                map.openInfoWindow(location, osp_poi.title, strHtml);
            });
						
            poiGroup.addPOIs(poi);
            poiManager.editPOIGroup(poiGroup);
            poiManager.refreshPOI();
            setPan();

            if (type == "move" || type == "lable") {
                map.zoomToScale(getMaxScale(), point2d);
            }
        }

        function movePoi(gid, pid) {
            var drawPointAction = $create(SuperMap.Web.Actions.DrawPoint, { map: map }, null, null, null);
            map.closeInfoWindow();
            drawPointAction.add_actionCompleted(function(arg) {
                var smx = arg.geometry.x;
                var smy = arg.geometry.y;
                var group = poiManager.getPOIGroup(gid);
                var poi = group.getPOIs(pid);
                poi.position.x = smx;
                poi.position.y = smy;
                poiManager.editPOIGroup(poiGroup);
                poiManager.refreshPOI();
                setPan();
            });
            map.set_action(drawPointAction);
        }
	
	/*----------客户端POI管理----end-----------*/
	
	/*-----------地物标记------begin----------------*/
			//定义所绘制的元素的类型的变量
			var featureType = null;
			//绘制点
			function drawPointFeature(){
				featureType = "point:";
				drawFeaturePoint = $create(SuperMap.OSP.UI.Actions.DrawPoint, {map:map}, null,null,null);
				drawFeaturePoint.add_actionCompleted(drawFeatureCompleted);
				map.set_action(drawFeaturePoint);
			}
			//绘制线
			function drawLineFeature() {
				featureType = "line:";
				drawFeatureLine = $create(SuperMap.OSP.UI.Actions.DrawLine, { map: map }, null, null, null);
				drawFeatureLine.add_actionCompleted(drawFeatureCompleted);
				map.set_action(drawFeatureLine);
			}
			//绘制多边形
			function drawPolygonFeature(){
				featureType = "region:";
				drawFeaturePolygon = $create(SuperMap.OSP.UI.Actions.DrawPolygon, {map:map}, null,null,null);
				drawFeaturePolygon.add_actionCompleted(drawFeatureCompleted);
				map.set_action(drawFeaturePolygon);
			}
			//定义地物要素（线）的集合
			var features = new Array();
			var count =0;//坐标添加箭头的标识
			//将绘制的图形加载到矢量要素图层上。绘制点、线和面结束后传回的回调函数中都是DrawGeometryArgs类型的参数，而绘制圆后传回的回调函数的参数为 DrawCircleArgs，因此以下这个函数不能将圆加载到矢量图层上
			function drawFeatureCompleted(drawGeometryArgs) { 
				var style = new SuperMap.Web.Core.Style();
				//获取用户自定义的标题
				var mytitle = featureType + document.getElementById("markerName").value;
				//获取用户自定义的属性内容
				var mycaption = document.getElementById("markerContent").value;
				style.stroke = true;
				style.strokeColor = "#00F";
				style.fill = true;
				style.fillColor = "#990033";
				style.opacity = "0.6";
				style.lineWidth = 3;
				var feature = new SuperMap.Web.Core.Feature();
				feature.style = style;
				//设置地物要素的id
				//feature.id = ++features.length;
				if(features.length == 0){
					feature.id = 1;
				}
				else{
					feature.id = features.length + 1;
				}
				
				feature.title = mytitle;
				feature.caption = mycaption;
				feature.geometry = drawGeometryArgs.geometry;
				//将要素加入要素集合
				features.push(feature);
				// alert(feature.id);
				var geoPoint = new SuperMap.Web.Core.GeoPoint();
				var geoLine = new SuperMap.Web.Core.GeoLine();
				var geoPolygon = new SuperMap.Web.Core.GeoRegion();
				feature.onclick = function(feature){
					var type = feature.title.charAt(0);
					var infowinHtml = "";
					var offset = { x: 10, y: -15 };
					infowinHtml = '<div style="width:auto;height:auto;">';
					infowinHtml += '<div class="div1">';
					infowinHtml += '<ul>';
					infowinHtml += '<li>此地物的标题为：'+ this.title +'</li>';
					infowinHtml += '<li>此地物的内容为：'+ this.caption +'</li>';
					infowinHtml += '</ul>';
					infowinHtml += '</div><br/>';
					infowinHtml += '</div>';
					var location = "";
					if(type == "p"){
						geoPoint = feature.geometry;
						var location = new SuperMap.Web.Core.Point2D(geoPoint.x,geoPoint.y);
					}
					if(type == "l"){
						geoLine = feature.geometry;
						location = geoLine.parts[0][0];
					}
					if(type == "r"){
						geoPolygon = feature.geometry;
						location = geoPolygon.parts[0][0];
					}
					map.openInfoWindowByAnchor(location , feature.title, infowinHtml, SuperMap.Web.Mapping.InfoWindowAnchor.ANCHOR_BOTTOMCENTER, offset);
					
					jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer>div.sm_infoWindowClose").one("click", function(e){
				
					jQuery("img[id^='img_left_'][isselect = 1]").removeAttr("isselect");
						//jQuery("#SuperMap_img").css("top","100px");
					});
					var img = '<img id="SuperMap_img" src="images/iw_tail.png" style="position:absolute;height:25px;left:85px;top:100px;"/>';
					if(count == 0){
						jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer").append(img);
						count = 1;
					}
					if(parseInt(jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) > 250 && parseInt(jQuery("#smLayer_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) < 280){
						jQuery("#SuperMap_img").css("left","100px");
					}else if(parseInt(jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) > 281 && parseInt(jQuery("#smLayer_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) < 319){
						jQuery("#SuperMap_img").css("left","120px");
					}else if(parseInt(jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) > 320 && parseInt(jQuery("#smLayer_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) < 350){
						jQuery("#SuperMap_img").css("left","130px");
					}else if(parseInt(jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) > 350){
						jQuery("#SuperMap_img").css("left","160px");
					}else if(parseInt(jQuery("#mapDiv_workLayer>div.sm_infoWindowContainer").css("width").substr(0,3)) < 250){
						jQuery("#SuperMap_img").css("left","85px");
					}
	
					jQuery("#SuperMap_img").css("top","100px");
	
					var popEle = jQuery('.sm_infoWindowContainer');
					popEle.css('background-color', 'white');
					jQuery('.sm_infoWindowContent').css('border', '0px');
					jQuery('.sm_infoWindowClose').css('background-image', 'url("images/popup_close.png")');
					featuresLayer.refresh();
				}
				featuresLayer.addFeature(feature);
				//停止画线，将地图操作变为平移
				setPan();
			}
			//删除地物元素
			function deleteFeature(){
				featuresLayer.clearFeature(features.length);
				if(features.length > 0){
					features.length--;
				}
				featuresLayer.refresh();
			}
	
	/*-----------地物标记--------end--------------*/
	
	
	/*-----------数据库poi管理----begin---------*/
		var poiService = null; //poi服务
		
        //添加POI分组并保存到服务器端
        function addPOIGroup() {
            var groupName = document.getElementById("txtPoiGroup").value;
			
			poiService = new SuperMap.OSP.UI.POIService();
			poiService.url = getServiceUrl();
            if (groupName != "") {
                var poiGroup = new SuperMap.OSP.UI.POIGroup("osp_poigroup_id_" + poiGroupIndex++);
                var scaledContent = new SuperMap.OSP.UI.ScaledContent();
                scaledContent.content = "<img src='../../images/blue.png' />";
                scaledContent.offset = new SuperMap.Web.Core.Point(8, 15);
                poiGroup.scaledContents = scaledContent;
                poiGroup.caption = groupName;
                poiService.savePOIGroup(poiGroup, function(poiGroup) {
                    if (poiGroup) {
                        document.getElementById("groupServerId").value = poiGroup.serverId;
                        document.getElementById("divInfoDB").innerHTML = "成功添加<b>" + poiGroup.caption + "</b>的poi分组";
                        //清除上个分组下的POI
                        clearPOI();
                    }
                }, function(error) {
                    alert(error.information);
                });
            }
        }
        //添加POI到该分组并保存到服务器端
        function addPOI1() {
            var poiGroupServerId = document.getElementById("groupServerId").value;
            if (poiManager==null) {
                //初始化POI管理参数
                poiManager = new SuperMap.OSP.UI.POIManager(map);
                poiManager.markerLayer = markerLayer;
                poiGroup = new SuperMap.OSP.UI.POIGroup("osp_poigroup_id");
                poiGroup.caption = "测试POI分组";
                poiGroup.scaledContents = new SuperMap.OSP.UI.ScaledContent();
                poiGroup.scaledContents.content = "<img src='../../images/blue.png' />";
                poiManager.addPOIGroup(poiGroup);
                poiIds = [];
            }
            if (poiGroupServerId != "") {
                var drawPointAction = $create(SuperMap.Web.Actions.DrawPoint, { map: map }, null, null, null);
                drawPointAction.add_actionCompleted(drawComplete);
                map.set_action(drawPointAction);
            }
        }
        function drawComplete(arg) {
            if (arg) {
                var poiGroupServerId = document.getElementById("groupServerId").value;
                var point2d = new SuperMap.Web.Core.Point2D(arg.geometry.x, arg.geometry.y);
                var poi = new SuperMap.OSP.UI.POI("osp_poi" + poiIndex);
                poiIds.push("osp_poi" + poiIndex);
                poi.position = point2d;
                poi.title = "测试POI" + poiIndex++;
                var scaledContents = new SuperMap.OSP.UI.ScaledContent();
                scaledContents.content = "<img src='../../images/blue.png' />";
                scaledContents.offset = new SuperMap.Web.Core.Point(8, 15);
                poi.scaledContents = scaledContents;
                //保存数据库
                poiService.savePois(poiGroupServerId, [poi.clone()], function(pois) {
                    poiGroup.addPOIs(poi);
                    poiManager.refreshPOI();
                }, function(error) {
                    alert(error.information);
                });
            }
            panMap();
        }

        //读取所有POI分组信息
        function loadPOIGroup() {
            var divInfo = document.getElementById("divInfoDB");
            var filter = "";
			
			poiService = new SuperMap.OSP.UI.POIService();
			poiService.url = getServiceUrl();
            poiService.findPOIGroups(filter,
            function(result) {
                if (result) {
                    divInfo.innerHTML = "共查到:" + result.length + "个POI分组</br>";
                    var tableHtml = "<table>";
                    for (var i = 0; i < result.length; i++) {
                        tableHtml += "<tr><td>名称:<b>" + result[i].caption + "</b></td><td>服务端编号:<b>" + result[i].serverId + "</b></td></tr>";
                    }
                    tableHtml += "</table>";
                    divInfo.innerHTML += tableHtml;
                }
            },
            function(errorResult) {
                divInfo.innerHTML = "<li><font color=red>服务端返回错误信息为：" + errorResult.information + "</font></li>";
            });
        }

        //清除地图POI信息
        function clearPOI() {
            if (poiIds && poiIds.length > 0) {
                for (var i = 0; i < poiIds.length; i++) {
                    poiGroup.removePOIs(poiIds[i]);
                }
                poiIds = null;
                poiIds = [];
                poiManager.refreshPOI();
            }
        }

        //将地图操作变为平移
        function panMap() {
            var panAction = $create(SuperMap.Web.Actions.Pan, { map: map }, null, null, null);
            map.set_action(panAction);
        }      
	/*-------------数据库poi管理-----end---------*/
	
	/*-----------------关键字搜索-----begin-------------------*/
		var poiSearchGroup = null; //poi搜索结果分组
		//加载用于关键字搜索的地图
		function loadMap()
		{
			/*map = getMap("mapDiv");
			markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, {
			bounds: map.get_bounds()
			}, null, null, null);
			if (Sys.Browser.name != "Internet Explorer" || Sys.Browser.name != "Microsoft Internet Explorer") {
				featuresLayer = $create(SuperMap.Web.Mapping.SVGLayer, { bounds: map.get_bounds() }, null, null, null);
			}
			else {
				featuresLayer = $create(SuperMap.Web.Mapping.VMLLayer, { bounds: map.get_bounds() }, null, null, null);
			}
			map.addLayer(featuresLayer);
			map.addLayer(markerLayer);
			var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "../../images/controls/compass.png"});
			var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});	
			var controls = new Array(compass, scaleBar);
			map.addControls(controls);*/
			poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
			poiManager = new SuperMap.OSP.UI.POIManager(map);
			poiManager.markerLayer = markerLayer;
			poiManager.addPOIGroup(poiSearchGroup);
		}
		
        var searchService = null;
        //关键字搜索
        var ospPager = null;//分页控件
		var pageSize = 10;//每页显示记录数
		var pageSize = 10;//每页显示记录数
		var keywordTextId = "txtKeyword";
		var resultDivId = "divInfo";
		var datasetName ='PbeijingP'
		var pagerId = "divInfo1";
		function searchByKeyword(){
			if(!!ospPager){
				ospPager.page = 1;
			}
			searchPOIByPager();
		}
		
		function searchPOIByPager(rowIndex){
		//折叠面板
			if (!ospPager) {
				ospPager = new OspPager("ospPager", searchPOIByPager);
			}
			if (!rowIndex) {
				rowIndex = 0;//起始记录号
			}
			var keyword = document.getElementById(keywordTextId).value;
			var poiSearchService = new SuperMap.OSP.Service.POISearch();
			var resultObj = document.getElementById(resultDivId);
			if (keyword == "") {
				var str = "搜索的关键字不能为空!";
				alert(str);
				return false;
			}
			resultObj.innerHTML = '正在查询……';
			//poi查询参数
			var getPOIsParam = new SuperMap.OSP.Service.GetPOIsByGeometryParam();
			var geometry = new SuperMap.Web.Core.GeoRegion();
			var viewBounds = map.get_viewBounds();
			geometry.parts[0] = [new SuperMap.Web.Core.Point2D(viewBounds.leftBottom.x, viewBounds.rightTop.y), new SuperMap.Web.Core.Point2D(viewBounds.rightTop.x, viewBounds.rightTop.y), new SuperMap.Web.Core.Point2D(viewBounds.rightTop.x, viewBounds.leftBottom.y), new SuperMap.Web.Core.Point2D(viewBounds.leftBottom.x, viewBounds.leftBottom.y)];
			getPOIsParam.geometry = geometry;
			getPOIsParam.keyword = keyword;
			getPOIsParam.expectCount = pageSize;
			getPOIsParam.startRecord = rowIndex;
			poiSearchService._search.url = "http://www.supermapcloud.com";
			getPOIsParam.datasetName=datasetName;
			getPOIsParam.DataSourceName = 'china_poi';
			poiSearchService.getPOIsByGeometry(getPOIsParam, function(result){
				if (result) {
					//openOrCloseDistrictPanel(true);
					//openOrCloseTypesPanel(true);
					//设置分页控件的总页数
					ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
					//显示分页
					ospPager.printHtml("yahoo", pagerId);
					showPoiResult(result.records);
				}
			}, function(error){
				alert(error.information)
				resultObj.innerHTML = error.information;
			});
		}

		//-----------------搜索结果---------------//
		var poiHashMap = null;
		function showPoiResult(records){
			var strHtml = "";
			if (records) {
				//装载查询到的POI对象
				poiHashMap = null;
				poiHashMap = new SuperMap.OSP.Core.HashMap();
				poiSearchGroup.clearPOIs();
				for (var i = 0; i < records.length; i++) {
					var poiInfo = records[i];
					poiHashMap.add(poiInfo.code, poiInfo);
					strHtml += '<table cellspacing="0" cellpadding="0" border="0" class="tab_color">';
					strHtml += '<tr style="height:5px"><td colspan="2"></td></tr>';
					strHtml += '<tr style="height:5px"><td align="center" width="54" valign="top" rowspan="3"><img src="http://www.supermapcloud.com/demo/china/images/' + (i + 1) + '.gif"></td>';
					strHtml += '<td width="448" valign="top"><span style="float: right; margin-right: 15px;"></span><strong><span href="javascript:positionPoi(\'' + poiInfo.code + '\',1)">' + poiInfo.name + '</span></strong></td></tr>';
					//strHtml += '<tr><td colspan="2">地址：' + poiInfo.address + '</td></tr>';
					strHtml += '<tr style="height:5px;"><td colspan="2"></td></tr></table>';
				
					//组织poi对象
					var poi = new SuperMap.OSP.UI.POI("search_POI_" + i);
					poi.position = new SuperMap.Web.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
					var scaledContent = new SuperMap.OSP.UI.ScaledContent();
					scaledContent.content = '<img src="http://www.supermapcloud.com/demo/china/images/num_map/' + (i + 1) + '.png" />';
					scaledContent.offset = new SuperMap.Web.Core.Point(17, 26);
					poi.title = poiInfo.name;
					poi.scaledContents = scaledContent;
					poi.properties = {
						code: poiInfo.code
					};
					poiSearchGroup.addPOIs(poi);
				}
				document.getElementById("divInfo1").innerHTML = "";
				document.getElementById(resultDivId).innerHTML = strHtml;
				//更新poi分组并显示
				poiManager.editPOIGroup(poiSearchGroup);
				poiManager.refreshPOI();
			}else {
				document.getElementById(resultDivId).innerHTML = "没有找到您要的结果！";
			}
		}
	/*-----------------关键字搜索-----end-------------------*/
	
	/*-----------------几何对象搜索-----begin-------------------*/
	//基于几何对象的搜索
		//var pagerId1 = "pageDiv";
        function loadMap1() {
            /*map = getMap("mapDiv");
			markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, {
			bounds: map.get_bounds()
			}, null, null, null);
            if (Sys.Browser.name != "Internet Explorer" || Sys.Browser.name != "Microsoft Internet Explorer") {
                featuresLayer = $create(SuperMap.Web.Mapping.SVGLayer, { bounds: map.get_bounds() }, null, null, null);
            }
            else {
                featuresLayer = $create(SuperMap.Web.Mapping.VMLLayer, { bounds: map.get_bounds() }, null, null, null);
            }
            map.addLayer(featuresLayer);
			map.addLayer(markerLayer);
           	var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "images/controls/compass.png"});
    		var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});	
			var controls = new Array(compass, scaleBar);
			map.addControls(controls);*/
			poiManager = new SuperMap.OSP.UI.POIManager(map);
			poiManager.markerLayer = markerLayer;
			poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
			poiSearchGroup.caption = "poi搜索分组";
			var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			scaledContent.content = '<img src="images/num_map/1.png" />';
			scaledContent.offset = new SuperMap.Web.Core.Point(0, 0);
			poiSearchGroup.scaledContents = scaledContent;
			poiManager.addPOIGroup(poiSearchGroup);
        }
        //绘制多边形
        function drawGeometry() {
            clearAll();
            var drawAction = $create(SuperMap.OSP.UI.Actions.DrawPolygon, { map: map }, null, null, null);
            drawAction.add_actionCompleted(searchByGeometry);
            map.set_action(drawAction);
        }
		//多边形搜索
        function searchByGeometry(arg,rowIndex) {
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
            style.stroke = true;
            style.strokeColor = "#00F";
            style.fill = true;
            style.fillColor = "#F00";
            style.opacity = "0.5";
            feature.geometry = arg.geometry;
            feature.style = style;
            featuresLayer.addFeature(feature);
			
			if (!ospPager) {
				ospPager = new OspPager("ospPager", searchByGeometry);
			}
			
			if (!rowIndex) {
				rowIndex = 0;//起始记录号
			}
			
            var keyword = document.getElementById("txtKeyword1").value;
			var poiSearchService = new SuperMap.OSP.Service.POISearch();
			var resultObj = document.getElementById("divGEOSearch");
            if (keyword == "") {
                alert("关键字不能为空");
                return false;
            }
			resultObj.innerHTML = '正在查询……';
			
		
            var getPOIsParam = new SuperMap.OSP.Service.GetPOIsByGeometryParam();
			getPOIsParam.geometry = arg.geometry;
			getPOIsParam.keyword = keyword;
			getPOIsParam.expectCount = pageSize;
			getPOIsParam.startRecord = rowIndex;
			poiSearchService._search.url = getServiceUrl();
			getPOIsParam.datasetName="PbeijingP";
			getPOIsParam.DataSourceName = 'china_poi';
			
			poiSearchService.getPOIsByGeometry(getPOIsParam, function(result){
				if (result) {
					//openOrCloseDistrictPanel(true);
					//openOrCloseTypesPanel(true);
					//设置分页控件的总页数
					//ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
					//显示分页
					//ospPager.printHtml("yahoo", pagerId1);
					showPoiResult1(result.records);
				}
			}, function(error){
				alert(error.information)
				resultObj.innerHTML = error.information;
			});
        }
		
		function showPoiResult1(records){
			var strHtml = "";
			if (records) {
				//装载查询到的POI对象
				poiHashMap = null;
				poiHashMap = new SuperMap.OSP.Core.HashMap();
				poiSearchGroup.clearPOIs();
				for (var i = 0; i < records.length; i++) {
					var poiInfo = records[i];
					poiHashMap.add(poiInfo.code, poiInfo);
					strHtml += '<table cellspacing="0" cellpadding="0" border="0" class="tab_color">';
					strHtml += '<tr style="height:5px"><td colspan="2"></td></tr>';
					strHtml += '<tr style="height:5px"><td align="center" width="54" valign="top" rowspan="3"><img src="images/' + (i + 1) + '.gif"></td>';
					strHtml += '<td width="448" valign="top"><span style="float: right; margin-right: 15px;"></span><strong><a href="javascript:positionPoi(\'' + poiInfo.code + '\',1)">' + poiInfo.name + '</a></strong></td></tr>';
					//strHtml += '<tr><td colspan="2">地址：' + poiInfo.address + '</td></tr>';
					strHtml += '<tr style="height:5px;"><td colspan="2"></td></tr></table>';
            
					//组织poi对象
					var poi = new SuperMap.OSP.UI.POI("search_POI_" + i);
					poi.position = new SuperMap.Web.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
					var scaledContent = new SuperMap.OSP.UI.ScaledContent();
					scaledContent.content = '<img src="images/num_map/' + (i + 1) + '.png" />';
					scaledContent.offset = new SuperMap.Web.Core.Point(17, 26);
					poi.title = poiInfo.name;
					poi.scaledContents = scaledContent;
					poi.properties = {
						code: poiInfo.code
					};
					poiSearchGroup.addPOIs(poi);
				}
				document.getElementById("divGEOSearch").innerHTML = strHtml;
				//更新poi分组并显示
				poiManager.editPOIGroup(poiSearchGroup);
				poiManager.refreshPOI();
			}else {
				document.getElementById("divGEOSearch").innerHTML = "没有找到您要的结果！";
			}
		}
        //清除
        function clearAll() {
            featuresLayer.clearFeatures();
			markerLayer.removeMarker();
            document.getElementById("divGEOSearch").innerHTML = "";
        }
	/*-----------------几何对象搜索-----end-------------------*/
	
	/*-----------------缓冲区搜索-----begin-------------------*/
		function drawLineGeometry() {
            clearAll1();
            var drawAction = $create(SuperMap.OSP.UI.Actions.DrawLine, { map: map }, null, null, null);
            drawAction.add_actionCompleted(searchByBuffer);
            map.set_action(drawAction);
        }
        //缓冲区查询
        function searchByBuffer(arg) {
            var bufferRrzae = parseFloat(document.getElementById("txtBufferRreze").value);
            if (isNaN(bufferRrzae)) {
                alert("缓存半径应该为数值型");
                return false;
            }
            var keyword = document.getElementById("txtKeyword2").value;
            if (keyword == "") {
                alert("关键字不能为空");
                return false;
            }
            
            
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
            style.stroke = true;
            style.strokeColor = "#00F";
            style.fill = true;
            style.fillColor = "#F00";
            style.opacity = "0.5";
            feature.geometry = arg.geometry;
            feature.style = style;
            featuresLayer.addFeature(feature);

            if (!searchService) {
                searchService = new SuperMap.OSP.Service.Search();
                searchService.url = getServiceUrl();
            }
            var queryParam = new SuperMap.OSP.Service.QueryParam();
            queryParam.expectCount = 25;
            queryParam.startRecord = 0;
            queryParam.returnResultSetInfo = SuperMap.Web.iServerJava6R.QueryOption.ATTRIBUTEANDGEOMETRY; //ATTRIBUTE,GEOMETRY,ATTRIBUTEANDGEOMETRY
            var queryDatasetParam1 = new SuperMap.OSP.Service.QueryDatasetParam();
            queryDatasetParam1.name = "Navi_pointP";
            queryDatasetParam1.sqlParam = new SuperMap.OSP.Service.SqlParam();
            queryDatasetParam1.sqlParam.returnFields = ["SmX", "SmY", "Name"];
            queryDatasetParam1.sqlParam.attributeFilter = "Name like '%" + keyword + "%'";
            queryParam.queryDatasetParams = [queryDatasetParam1];

            var bufferParam = new SuperMap.Web.iServerJava2.BufferAnalystParam();
            bufferParam.bufferEndType = SuperMap.Web.iServerJava2.BufferEndType.ROUND;
            bufferParam.bufferSideType = SuperMap.Web.iServerJava2.BufferSideType.FULL;
            bufferParam.leftDistance = bufferRrzae;
            bufferParam.rightDistance = bufferRrzae;

            var queryByBufferParam = new SuperMap.OSP.Service.QueryByBufferParam();
            queryByBufferParam.dataSourceName = "POI";
            queryByBufferParam.queryParam = queryParam;
            queryByBufferParam.bufferParam = bufferParam;
            queryByBufferParam.geometry = arg.geometry;
            queryByBufferParam.returnBufferAnalsyResult = true;
            document.getElementById("divBufferSearch").innerHTML = "正在读取数据，请稍后……";
            searchService.queryByBuffer(queryByBufferParam, function(result) {
                if (result) {
                    var records = result.records;
                    var bufferAnalystResult = result.bufferAnalystResult;
                    //绘制缓冲区分析结果面对象
                    var feature1 = new SuperMap.Web.Core.Feature();
                    var style = new SuperMap.Web.Core.Style();
                    style.stroke = true;
                    style.strokeColor = "#00F";
                    style.fill = true;
                    style.fillColor = "#00F";
                    style.opacity = "0.3";
                    feature1.geometry = bufferAnalystResult.shape;
                    feature1.style = style;
                    featuresLayer.addFeature(feature1);

                    var tableHtml = "<div>共查到:" + result.totalCount + "条结果,当前返回的结果有:" + result.currentCount + "条</br>";
                    tableHtml += "分析区域的面积为:" + bufferAnalystResult.area + ";周长为:" + bufferAnalystResult.perimeter + "</div>";
                    tableHtml += "<table>";
                    for (var i = 0; i < result.currentCount; i++) {
                        var record = records[i];
                        tableHtml += "<tr>";
                        for (var j = 0; j < record.fields.length; j++) {
                            tableHtml += "<td>" + record.fields[j] + ":" + record.fieldValues[j] + "</td>";
                        }
                        tableHtml += "</tr>";
                    }
                    tableHtml += "</table>";
                    document.getElementById("divBufferSearch").innerHTML = tableHtml;
                }
            }, function(error) {
                document.getElementById("divBufferSearch").innerHTML = error.information;
            });
        }
		 //清除
        function clearAll1() {
            featuresLayer.clearFeatures();
            document.getElementById("divBufferSearch").innerHTML = "";
        }
	/*-----------------缓冲区搜索-----end-------------------*/
	
	/*-----------------缓冲区分析----begin-------------------*/
        var spatialAnalystService = null;
		var resultGeometry = "";
        //绘制缓冲线
        function drawLineGeometry1() {
            drawLineBuffer = $create(SuperMap.OSP.UI.Actions.DrawLine, { map: map }, null, null, null);
            drawLineBuffer.add_actionCompleted(bufferAnalyst);
            map.set_action(drawLineBuffer);
        }
        //缓冲区分析
        function bufferAnalyst(arg) {
var bufferRrzae = parseFloat(document.getElementById("txtBufferRreze").value);
            if (isNaN(bufferRrzae)) {
                alert("缓存半径应该为数值型");
                return false;
            }
            clearAll();
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
            style.stroke = true;
            style.strokeColor = "#00F";
            style.fill = true;
            style.fillColor = "#F00";
            style.opacity = "0.5";
            feature.geometry = arg.geometry;
            feature.style = style;
            featuresLayer.addFeature(feature);

            if (!spatialAnalystService) {
                spatialAnalystService = new SuperMap.OSP.Service.SpatialAnalyst();
                spatialAnalystService.url = getServiceUrl();
            }
            var bufferParam = new SuperMap.Web.iServerJava2.BufferAnalystParam();
            bufferParam.bufferEndType = SuperMap.Web.iServerJava2.BufferEndType.ROUND;
            bufferParam.bufferSideType = SuperMap.Web.iServerJava2.BufferSideType.FULL;
            bufferParam.leftDistance = bufferRrzae; //左缓冲半径
            bufferParam.rightDistance = bufferRrzae; //右缓冲半径
            var bufferAnalystParam = new SuperMap.OSP.Service.BufferAnalystParam();
            bufferAnalystParam.bufferParam = bufferParam;
            bufferAnalystParam.dataSourceName = "china_poi";
			var tempGeometry = arg.geometry;
			for(var i = 0; i< tempGeometry.parts[0].length; i++){
				var point = SuperMap.OSP.Core.Utility.metersToLatLon(tempGeometry.parts[0][i]);
				tempGeometry.parts[0][i] = point;
			}
            bufferAnalystParam.geometry = tempGeometry;
            document.getElementById("divBufferAnalysis").innerHTML = "数据已经提交，请稍后……";
            spatialAnalystService.bufferAnalyst(bufferAnalystParam, function(result) {
                if (result) {
                    /*//缓存结果面
                    var style1 = new SuperMap.Web.Core.Style();
                    style1.stroke = true;	
                    style1.strokeColor = "#00F";
                    style1.fill = true;
                    style1.fillColor = "#00F";
                    style1.opacity = "0.3";
                    var feature1 = new SuperMap.Web.Core.Feature();
                    feature1.geometry = result.shape;
                    feature1.style = style1;
                    featuresLayer.addFeature(feature1);
                    var tableHtml = "<div>缓存面积为:" + result.area + ",周长为:" + result.perimeter + "</div>";
                    document.getElementById("divBufferAnalysis").innerHTML = tableHtml;*/
					//缓存结果面
					var point2d = result.shape;
                    var style1 = new SuperMap.Web.Core.Style();
                    style1.stroke = true;
                    style1.strokeColor = "#00F";
                    style1.fill = true;
                    style1.fillColor = "#00F";
                    style1.opacity = "0.3";
                    var feature1 = new SuperMap.Web.Core.Feature();
					resultGeometry = result.shape.parts[0];
					for(var j = 0; j< resultGeometry.length; j++){
						var tempPoint = SuperMap.OSP.Core.Utility.latLonToMeters(resultGeometry[j]);
						result.shape.parts[0][j] = tempPoint;
					}
                    feature1.geometry = result.shape;
                    feature1.style = style1;
                    featuresLayer.addFeature(feature1);
                    var tableHtml = "<div>缓存面积为:" + result.area + ",周长为:" + result.perimeter + "</div>";
                    document.getElementById("divBufferAnalysis").innerHTML = tableHtml;
					//查询区域内的POI
					var poiSearchService = new SuperMap.OSP.Service.POISearch();
					var getPOIsParam = new SuperMap.OSP.Service.GetPOIsByGeometryParam();
					getPOIsParam.geometry = point2d;
					getPOIsParam.keyword = "公园";
					getPOIsParam.expectCount = 10;
					getPOIsParam.startRecord = 0;
					poiSearchService._search.url = "http://services.supermapcloud.com";
					getPOIsParam.datasetName="PbeijingP";
					getPOIsParam.DataSourceName = 'china_poi';
					poiSearchService.getPOIsByGeometry(getPOIsParam, function(result){
						if (result) {
							//openOrCloseDistrictPanel(true);
							//openOrCloseTypesPanel(true);
							//设置分页控件的总页数
							//ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
							//显示分页
							//ospPager.printHtml("yahoo", pagerId);
							showPoiResult(result.records);
						}
					}, function(error){
						alert(error.information)
						//resultObj.innerHTML = error.information;
					});
                }
            }, function(error) {
                document.getElementById("divBufferAnalysis").innerHTML = error.information;
            });
        }

        //清除
        function clearAll2() {
            featuresLayer.clearFeatures();
            document.getElementById("divBufferAnalysis").innerHTML = "";
        }
	/*-----------------缓冲区分析----end-------------------*/
	
	/*-------------------地图截屏---begin-----------------
		//var poiGroups = null;
		var tempFeature = null;
		
        //添加临时矢量图和POI
        function addOverly() {
            if (poiManager==null) {
                poiGroups = [];
                features = [];
                poiManager = new SuperMap.OSP.UI.POIManager(map);
                poiManager.markerLayer = markerLayer;
                var poiGroup = new SuperMap.OSP.UI.POIGroup("osp_poigroup_id");
                poiGroup.caption = "测试POI分组";
                var scaledContent = new SuperMap.OSP.UI.ScaledContent();
                scaledContent.content = "<img src='http://www.supermapcloud.com/demo/china/images/blue.png' />";
                scaledContent.offset = new SuperMap.Web.Core.Point(0, 0);
                poiGroup.scaledContents = scaledContent;
                poiManager.addPOIGroup(poiGroup);
                var poi = new SuperMap.OSP.UI.POI("poitest");
                poi.position = new SuperMap.Web.Core.Point2D(113.774398, 22.986241);
                poi.title = "osp测试POI";
                poi.scaledContents = scaledContent;
                poiGroup.addPOIs(poi);
                poiManager.refreshPOI();

                poiGroups.push(poiGroup.clone());
                //测试矢量图
                var lPoint1 = new SuperMap.Web.Core.Point2D(113.833878, 23.033573);
                var lPoint2 = new SuperMap.Web.Core.Point2D(113.772489, 22.949560);
                var line = new SuperMap.Web.Core.GeoLine();
                line.parts = [];
                line.parts[0] = [lPoint1, lPoint2];
                var feature = new SuperMap.Web.Core.Feature();
                feature.id = -1;
                var style = new SuperMap.Web.Core.Style();
                style.stroke = true;
                style.strokeColor = "#4980DB";
                style.fill = true;
                style.fillColor = "#4980DB";
                style.opacity = "0.5";
                feature.geometry = line;
                feature.style = style;
                featuresLayer.addFeature(feature);
                //克隆
                tempFeature = feature.clone();
                tempFeature.geometry = line;
            }
        }
        
		//地图截屏
        function mapCapturer() {
            var mapCapturerService = new SuperMap.OSP.Service.MapCapturer();
            mapCapturerService.url = getServiceUrl();
            //组织截图参数
            var getImageParam = new SuperMap.OSP.Service.GetImageParam();
            getImageParam.viewBounds = map.get_viewBounds();
            getImageParam.mapName = getMapName();
			var level = map.get_level();
			var scales = getMapScales();
			getImageParam.mapScale = scales[level];
			getImageParam.picWidth = parseInt(document.getElementById("divMapCut").style.width.replace("px", ""));
			if (!getImageParam.picWidth) {
				getImageParam.picWidth = document.getElementById("divMapCut").offsetWidth;
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
	/*-------------------地图截屏---end-----------------*/
	
	/*-------------------路径分析---begin--------------*/
        function loadMap2() {
            /*map = getMap("mapDiv");
            markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, { bounds: map.get_bounds() }, null, null, null);
            if (Sys.Browser.name != "Internet Explorer" || Sys.Browser.name != "Microsoft Internet Explorer") {
                featuresLayer = $create(SuperMap.Web.Mapping.SVGLayer, { bounds: map.get_bounds() }, null, null, null);
            }
            else {
                featuresLayer = $create(SuperMap.Web.Mapping.VMLLayer, { bounds: map.get_bounds() }, null, null, null);
            }
            map.addLayer(featuresLayer);
            map.addLayer(markerLayer);
            var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "../../images/controls/compass.png"});
    				var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});	
						var controls = new Array(compass, scaleBar);
						map.addControls(controls);*/


            //初始化POI管理
            poiManager = new SuperMap.OSP.UI.POIManager(map);
            poiManager.markerLayer = markerLayer;
            poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poipathGroupId");
            poiManager.addPOIGroup(poiSearchGroup);
        }

		// 路径分析-起始点和结束点固定ID
		var findPath_StartID = 'findPath_POI_start';
		var findPath_EndID = 'findPath_POI_end';
		//路径分析结果处理
		var drawLine = "";
		var pathFeatures = [];
		var pathItemPoints = []; //路径途径节点集合
		var routePoints = [];
		var pointMap;
		var poiIndex = 0;//途经点索引
		var pPntWay = [];//途经点坐标集合

		//新路径分析方法
		function findNewPath() {
			map.zoomToLevel(12, new SuperMap.Web.Core.Point2D(12958879.900879698,4864286.157687547));
			// 创建一个查询点
			var createFindPathPoint = function(id, name, x, y){
				var poi = new SuperMap.OSP.UI.POI(id);
				poi.position = new SuperMap.Web.Core.Point2D(parseFloat(x), parseFloat(y));
				var scaledContent = new SuperMap.OSP.UI.ScaledContent();
				if(id.indexOf('start') != -1){
					scaledContent.content = "<img src='images/start.png' />";
				}else {
					scaledContent.content = "<img src='images/end.png' />";
				}
		
				scaledContent.offset = new SuperMap.Web.Core.Point(15, 33);
				poi.title = name;
				poi.scaledContents = scaledContent;
				poi.properties = {
					code: 'searchBuffer'
				};
			
				poi.addEventListerner("click", function(e){
				//    	alert('zzz');
			
				});
				return poi;
			}
			var path_start = jQuery("#sel_path_start");
			var path_end = jQuery("#sel_path_end");
			var point_start = createFindPathPoint(findPath_StartID, path_start.text(), path_start.val().split(',')[0], path_start.val().split(',')[1]);
			var point_end = createFindPathPoint(findPath_EndID, path_end.text(), path_end.val().split(',')[0], path_end.val().split(',')[1]);
			poiSearchGroup.clearPOIs();
			poiSearchGroup.addPOIs(point_start);
			poiSearchGroup.addPOIs(point_end);
			poiManager.editPOIGroup(poiSearchGroup);
			poiManager.refreshPOI();
		
		
			var param = new SuperMap.OSP.Service.TransportionAnalystParameter();
			var startPoint = new SuperMap.OSP.Service.SePoint();
			var tempFirstPoint = poiSearchGroup.getPOIs(findPath_StartID).position;
			var tempLastPoint = poiSearchGroup.getPOIs(findPath_EndID).position;
			var firstPoint = SuperMap.OSP.Core.Utility.metersToLatLon(tempFirstPoint);
			startPoint.x = firstPoint.x;
			startPoint.y = firstPoint.y;
			param.startPoint = startPoint;
			var endPoint = new SuperMap.OSP.Service.SePoint("SuperMap.OSP.Service.SePoint");
			var lastPoint = SuperMap.OSP.Core.Utility.metersToLatLon(tempLastPoint);
			endPoint.x = lastPoint.x;
			endPoint.y = lastPoint.y;
			param.endPoint = endPoint;


			var p = SuperMap.OSP.Core.Utility.metersToLatLon(new SuperMap.LonLat(1.2954997E7, 4864384.6));
			param.pPntWay = [ {x: p.lon, y: p.lat} ];

			// param.pPntWay = pPntWay;
			//var radioPathMode = jQuery("input[name='radioPathMode']:checked").val();
			//param.nSearchMode = radioPathMode;
			var transportationAnalyst = new SuperMap.OSP.Service.TransportationAnalyst();
			transportationAnalyst.url = getServiceUrl();
			document.getElementById("routeDiv").innerHTML = "请求已提交，请稍后……";
			transportationAnalyst.findPath(param, function(result) {
				var style = new SuperMap.Web.Core.Style();
				style.stroke = true;
				style.strokeColor = "#00F";
				style.fill = true;
				style.fillColor = "#00F";
				style.opacity = "0.6";
				style.lineWidth = 3;
				var feature = new SuperMap.Web.Core.Feature();
				feature.geometry = result.path;
				feature.style = style;
				featuresLayer.addFeature(feature);
				pathFeatures.push(feature);
				var pathInfoList = result.pathInfoList;
				findPathResult(result);
				drawLine = result;
			}, function(error) {
				alert(error.information);
				return false;
			});

		}

		// 路径分析的添加方法
		function addFindPathPointAction(stat){
			if(!stat){
				stat = 'start'
			}

			var z = stat;

			var onDrawCompleted = function(drawGeometryArgs) {
				var panAction = $create(SuperMap.Web.Actions.Pan, { map: map }, null, null, null);
				map.set_action(panAction);
        
				// 清除
				featuresLayer.clearFeatures();
        
				if(z == 'start'){
					var startPOI = createFindPathPoint(findPath_StartID, '标注起始点', drawGeometryArgs.geometry.x, drawGeometryArgs.geometry.y);
					var endPOI = poiSearchGroup.getPOIs(findPath_EndID);
				
					poiSearchGroup.clearPOIs();
					poiSearchGroup.addPOIs(startPOI);
					if(endPOI){
						poiSearchGroup.addPOIs(endPOI);
					}
					poiManager.editPOIGroup(poiSearchGroup);
					poiManager.refreshPOI();
            
					$('#findPath_start').val('');
					$('#findPath_start').val(startPOI.name?startPOI.name:startPOI.title);
					//  $('#findPath_start').val('标注起始点');
            
					// findPath_end
				} else {
					var endPOI = createFindPathPoint(findPath_EndID, '标注结束点', drawGeometryArgs.geometry.x, drawGeometryArgs.geometry.y);
					var startPOI = poiSearchGroup.getPOIs(findPath_StartID);
				
					poiSearchGroup.clearPOIs();
					poiSearchGroup.addPOIs(endPOI);
					if(startPOI){
						poiSearchGroup.addPOIs(startPOI);
					}
					poiManager.editPOIGroup(poiSearchGroup);
					poiManager.refreshPOI();
				
					$('#findPath_end').val('');
					$('#findPath_end').val(endPOI.name?endPOI.name:endPOI.title);
					//            $('#findPath_end').val('标注结束点');
				}
			}
	
	
			var drawAction = $create(SuperMap.OSP.UI.Actions.DrawPoint, { map: map }, null, null, null);
			drawAction.add_actionCompleted(onDrawCompleted);
			map.set_action(drawAction);
		}

		// 创建一个查询点
		var createFindPathPoint = function(id, name, x, y){
			var poi = new SuperMap.OSP.UI.POI(id);
			poi.position = new SuperMap.Web.Core.Point2D(parseFloat(x), parseFloat(y));
			var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			if(id.indexOf('start') != -1){
				scaledContent.content = "<img src='images/start.png' />";
			}else {
				scaledContent.content = "<img src='images/end.png' />";
			}
    
			scaledContent.offset = new SuperMap.Web.Core.Point(15, 33);
			poi.title = name;
			poi.scaledContents = scaledContent;
			poi.properties = {
				code: 'searchBuffer'
			};
		
			poi.addEventListerner("click", function(e){
			//    	alert('zzz');
    	
			});
			return poi;
		}

		function findPathResult(result){
			
			//	setTab(-1, null);
			// 获取转向信息
			var getDirToSwerve = function(i){
				var turn = '';
				switch (i) {
					case 0:
						turn = '直行';
						break;
					case 1:
						turn = '左前转弯';
						break;
					case 2:
						turn = '右前转弯';
						break;
					case 3:
						turn = '左转弯';
						break;
					case 4:
						turn = '右转弯';
						break;
					case 5:
						turn = '左后转弯';
						break;
					case 6:
						turn = '右后转弯';
					case 7:
						turn = '调头';
						break;
					case 8:
						turn = '右转弯绕行至左';
						break;
					case 9:
						turn = '直角斜边右转弯';
						break;
					case 10:
						turn = '环岛';
						break;
				}
				return turn;
			}
	
			/**
			* @index 转向信息索引号
			* @dRouteLength 道路长度
			* @iDirToSwerve 转向信息
			* @strRouteName 道路名称
			* @xy			 经纬度
			* @NextstrRouteName 下条道路名称，最后点无效
			* @markName 当是起点或者终点时，显示点名称
			*/
			var getRountTable = function(index, dRouteLength, iDirToSwerve, strRouteName, x, y, NextstrRouteName, markName, isEnd){
		
				var rout = getDirToSwerve(iDirToSwerve);
				var tab = '';
				var length = loadLengthFormat(dRouteLength);
				if(index == 0){
					tab = '<tr onmouseover="drawLineRed('+index+');" onmouseout="deleteRedLine('+index+');">' +
						'<th>' + (index + 1) + '.</th>' +
						'<td>从<strong>' + markName + '</strong>出发，沿<span><a href="#">' + strRouteName + '</a></span>行驶' + length + '，' + rout + '进入<span><a href="#">' + NextstrRouteName + '</a></span></td>' + 
						'<td>' + '</td>' + 
					'</tr>';
				} else if(index > 0 && !isEnd) {
					tab = '<tr onmouseover="drawLineRed('+index+');" onmouseout="deleteRedLine('+index+');">' +
						'<th>' + (index + 1) + '.</th>' +
						'<td>沿<span><a href="#">' + strRouteName + '</a></span>行驶' + length + '，' + rout + '进入<span><a href="#">' + NextstrRouteName + '</a></span></td>' +
						'<td>' + '</td>' + 
						'</tr>';
				} else if(isEnd){
					// 结束点
					tab = '<tr onmouseover="drawLineRed('+index+');" onmouseout="deleteRedLine('+index+');">' +
						'<th>' + (index + 1) + '.</th>' +
						'<td>沿<span><a href="#">' + strRouteName + '</a></span>行驶' + length + '' + '，到达<strong>' + markName + '</strong></td>' + 
						'<td>' + '</td>' + 
					'</tr>';
				}
				return tab;
			}
	
	
			// search_result
			//var result = getJSON();
			var path = result.path;
			var rout = result.pathInfoList;
	
			// dLength
			var html = '<table class="search_result">';
			var routLength = rout.length;
			if(routLength && routLength > 0){
				html += '<caption><a href="#">全程</a> 约' + loadLengthFormat(eval(rout[0].dLength)) + '</caption>';
				var i = 0;
				for(; i < routLength; i++){
					var markName = '起点';
					if(i == (routLength - 1)){
						markName = '终点';
						// 结束点
						html += getRountTable(i, rout[i].dRouteLength, rout[i].iDirToSwerve, rout[i].strRouteName, rout[i].x, rout[i].y, null, markName, true);
					}else if(i == 0){
						html += getRountTable(i, rout[i].dRouteLength, rout[i].iDirToSwerve, rout[i].strRouteName, rout[i].x, rout[i].y, rout[i + 1].strRouteName, markName, false);
					}else {
						html += getRountTable(i, rout[i].dRouteLength, rout[i].iDirToSwerve, rout[i].strRouteName, rout[i].x, rout[i].y, rout[i + 1].strRouteName, null, false);
					}
				}
			}
	
			html += '</table>';
			//<div class="search_bottom">查看:<a href="#">返程</a>│<a href="#">公交</a></div>
			//	$('#search_result').empty();
			//	$('#fujianArea_head').append(html);
			//	$('#search_result').css("display", 'none');
	
			document.getElementById("routeDiv").innerHTML = html;
		}
		// 将米数据转化为较为方便的读取方式

		function loadLengthFormat(length){
	
			var getFormat = function(val){
				var v = Math.round(val   *   10)   /10;
				return v;
			}
	
			var unit = '米';
			var dLength = eval(length);
			if(dLength > 900) {
				dLength = dLength / 1000.0;
				unit = '公里';
			}
	
			return getFormat(dLength) + unit;
		}
        
        //清除路径分析高亮信息 
        function clearPath() {
            featuresLayer.clearFeatures(pathFeatures);
            pathFeatures = [];
            poiPathGroup.pois = [];
            poiManager.editPOIGroup(poiPathGroup);
            poiManager.refreshPOI();
			document.getElementById("routeDiv").innerHTML = "";
		}
	/*--------------------路径分析-------end--------------------*/
	
	/*--------------------地图打印----begin------------------*/
        function printMap() {
            var printService = new SuperMap.OSP.Service.PrintService();
            printService.printMap();
        } 
	/*--------------------地图打印----end------------------*/
	
	/*--------------------地图测量----begin------------------*/
		function setMeasure(){
			
			// if( drawLine != null && !drawLine.remove_actionCompleted(drawCompleted) ){
				// drawLine.remove_actionCompleted(drawCompleted);
			// }
			var measureAction = $create(SuperMap.Web.Actions.MeasureDistance, {map: map}, null, null, null);
			map.set_action(measureAction);
		}

        //清除
        function clearAll3() {
            featuresLayer.clearFeatures();
            document.getElementById("divMapMeasure").innerHTML = "";
        }
	/*--------------------地图测量----end------------------*/
	
	
	/*--------------------地图编辑----begin------------------*/
        var dataSourceName = "beijing"; //数据源名称
        var datasetName1 = "Cinema_usr"; //数据集
        var cinemaGroupId = "myCinema";
        var dataManager = null;

        function loadMap3() {
            map = getMap("mapDiv");
            markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, { bounds: map.get_bounds() }, null, null, null);
            map.addLayer(markerLayer);
            var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "../../images/controls/compass.png"});
    				var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});	
						var controls = new Array(compass, scaleBar);
						map.addControls(controls);

            poiManager = new SuperMap.OSP.UI.POIManager(map);
            var poiGroup = new SuperMap.OSP.UI.POIGroup(cinemaGroupId);
            poiGroup.level = 5;
            poiGroup.visible = true;
            poiGroup.scaledContents = new SuperMap.OSP.UI.ScaledContent();
            poiGroup.scaledContents.content = "<img src='http://www.supermapcloud.com/sample/images/blue.png' />";
            poiGroup.scaledContents.offset = new SuperMap.Web.Core.Point(8, 18);
            poiGroup.caption = "OSP";
            poiManager.addPOIGroup(poiGroup);
            poiManager.markerLayer = markerLayer;

            dataManager = new SuperMap.OSP.Service.DataManager();
            dataManager.url = getServiceUrl();
        }


        //获取一个数据集下的所有实体
        function getEntitys() {
            var dataManagerService = new SuperMap.OSP.Service.DataManager();
            dataManagerService.url = getServiceUrl();
            var objInfo = document.getElementById("divMapEdit");
            objInfo.innerHTML = "正在获取数据，请稍候……";
            dataManagerService.getEntities(dataSourceName, datasetName1, function(entitys) {
                if (entitys) {
                    var tableHtml = "获取到" + entitys.length + "个对象<br/><table>";
                    for (var i = 0; i < entitys.length; i++) {
                        var fieldNames = entitys[i].fieldNames;
                        var fieldValues = entitys[i].fieldValues;
                        tableHtml += "<tr><td>编号:" + entitys[i].id + "</td>";
                        for (var j = 0; j < fieldNames.length; j++) {
                            tableHtml += "<td>" + fieldNames[j] + ":" + fieldValues[j] + "</td>";
                        }
                        tableHtml += "</tr>";
                    }
                    objInfo.innerHTML = tableHtml;
                }
            }, function(error) {
                objInfo.innerHTML = "错误提示：" + error.information;
            });
        }

        //鼠标点击添加一个POI，并调用地图编辑接口在数据库里添加一个点对象
        function addCinema() {
            var drawPointAction = $create(SuperMap.Web.Actions.DrawPoint, { map: map }, null, null, null);
            drawPointAction.add_actionCompleted(function(arg) {
                var pindex = poiIndex++;
                var poi = new SuperMap.OSP.UI.POI("noNamePoi" + pindex);
                poi.properties = { name: "未命名" + pindex, title: "未命名" + pindex, address: "", filminfo: "", group: cinemaGroupId };
                poi.position = new SuperMap.Web.Core.Point2D(arg.geometry.x, arg.geometry.y);
                poi.title = "未命名" + pindex;
                poi.addEventListerner("click", function(e) {
                    var osp_poi = eval('(' + e.get_info() + ')');
                    var strEdit = '影院名称：' + osp_poi.properties.title;
                    strEdit += '<br/>影院地址：' + osp_poi.properties.address;
                    strEdit += '<br/>最新影讯：' + osp_poi.properties.filminfo;
                    var location = new SuperMap.Web.Core.Point2D(e._x, e._y);
                    map.openInfoWindow(location, osp_poi.properties.title, strEdit);
                });

                var poiGroup = null;
                for (var i = 0; i < poiManager.poiGroups.length; i++) {
                    if (poiManager.poiGroups[i].id == cinemaGroupId) {
                        poiGroup = poiManager.poiGroups[i];
                        break;
                    }
                }
                //添加实体
                addEntity(poi.properties.name, poi.properties.title, poi.properties.address, poi.properties.filminfo, cinemaGroupId, arg.geometry.x, arg.geometry.y, poi.id);

                poiGroup.addPOIs(poi);
                poiManager.editPOIGroup(poiGroup);
                poiManager.refreshPOI();
                setPan();
            });
            map.set_action(drawPointAction);
        }

        //调用地图编辑接口，添加一个实体
        function addEntity(name, title, address, filminfo, gid, x, y, pid) {
            var entityParam = new SuperMap.OSP.Service.EntityParam();
            entityParam.dataSourceName = dataSourceName;
            entityParam.datasetName = datasetName1;
            var entity = new SuperMap.Web.iServerJava2.Entity();
            entity.id = -1;
            entity.fieldNames = ["name", "title", "address", "filminfo", "cinemaGroupId"];
            entity.fieldValues = [name, title, address, filminfo, gid];
            var geometry = new SuperMap.Web.iServerJava2.ServerGeometry();
            geometry.id = 0;
            geometry.feature = SuperMap.Web.iServerJava2.ServerFeatureType.POINT;
            geometry.parts = [1];
            geometry.point2Ds = [new SuperMap.Web.Core.Point2D(x, y)];
            entity.shape = geometry;
            entityParam.entities = [entity];
            //向数据集中添加实体
            dataManager.addEntity(entityParam, function(result) {
                if (result) {
                    var group = poiManager.getPOIGroup(gid);
                    var poi = group.getPOIs(pid);
                    poi.properties.smid = result[0];
                    poiManager.refreshPOI();
                    alert("添加成功");
                }
            }, function(errorResult) {
                alert(errorResult.information);
            });
        }
	
	/*--------------------地图编辑----end------------------*/
	
	/*--------------------地图共享----begin------------------
        var poiGroups = null;
        var feature2 = null;
        
        // 添加地图覆盖物
        function addOverly1() {
            if (poiManager==null) {
                poiGroups = [];
                features = [];
                poiManager = new SuperMap.OSP.UI.POIManager(map);
                poiManager.markerLayer = markerLayer;
                var poiGroup = new SuperMap.OSP.UI.POIGroup("osp_poigroup_id");
                poiGroup.caption = "测试POI分组";
                var scaledContent = new SuperMap.OSP.UI.ScaledContent();
                scaledContent.content = "<img src='http://www.supermapcloud.com/demo/china/images/blue.png' />";
                scaledContent.offset = new SuperMap.Web.Core.Point(8, 13);
                poiGroup.scaledContents = scaledContent;
                poiManager.addPOIGroup(poiGroup);
                var poi = new SuperMap.OSP.UI.POI("poitest");
                poi.position = new SuperMap.Web.Core.Point2D(113.774398, 22.986241);
                poi.title = "osp测试POI";
                poi.scaledContents = scaledContent;
                poiGroup.addPOIs(poi);
                poiManager.refreshPOI();
                
                // 临时克隆POIGroup
                var poiGroupClone = new SuperMap.OSP.UI.POIGroup("osp_test");
                poiGroupClone.scaledContents = scaledContent;
                var poi1 = new SuperMap.OSP.UI.POI("poitest");
                poi1.position = new SuperMap.Web.Core.Point2D(113.774398, 22.986241);
                poi1.title = "osp测试POI";
                poi1.scaledContents = scaledContent;
                poiGroupClone.addPOIs(poi1);

                poiGroups.push(poiGroupClone);
                // 测试矢量图
                        
                var feature = new SuperMap.Web.Core.Feature();
                feature.id = -1;
                var style = new SuperMap.Web.Core.Style();
                style.stroke = true;
                style.strokeColor = "#00F";
                style.fill = true;
                style.fillColor = "#F00";
                style.opacity = "0.5";
                feature.geometry = getLine();
                feature.style = style;
                featuresLayer.addFeature(feature);

                feature2 = feature.clone();
                feature2.geometry = getLine();                
            }
        }

        function getLine() {
            var lPoint1 = new SuperMap.Web.Core.Point2D(113.833878, 23.033573);
            var lPoint2 = new SuperMap.Web.Core.Point2D(113.772489, 22.949560); 
            var line = new SuperMap.Web.Core.GeoLine();
            line.parts = [];
            line.parts[0] = [lPoint1, lPoint2];
            return line;
        }

        //地图共享
        function shareMap() {
            var clientInfo = new SuperMap.OSP.Service.ClientStateInfo();
            clientInfo.viewBounds = map.get_viewBounds();
            clientInfo.mapScale = map.get_scale();
            clientInfo.mapName = getMapName();
            if (poiManager) {
                clientInfo.poiGroups = poiGroups;
                feature2.geometry = getLine();
                clientInfo.features = [feature2];
            } else {
            clientInfo.poiGroups = [];
            clientInfo.features = [];
            }

            var shareMapService = new SuperMap.OSP.ExtentionService.ShareMap();
            shareMapService.url = getServiceUrl();
            var shareObj = document.getElementById("divShareMap");
            shareObj.innerHTML = "数据正在提交，请稍候……";
            shareMapService.saveClientStateInfo(clientInfo, null,
            function(result) {
                if (result) {
                    //返回结果为保存的临时key，用来获取客户端保存的参数
                    //shareObj.innerHTML = "数据保存成功，读取编号为：" + result;
                    shareObj.innerHTML = "<br/><a href='share.html?keyId=" + result + "' target='_blank' >查看分享的地图</a>";
                }
            },
             function(errorResult) {
                 alert(errorResult.information);
             });
        }
	/*--------------------地图共享----end------------------*/
	
	/*--------------------统计分析----begin------------------*/
		//绘制自定义面
        function drawGeometry1() {
            var drawAction = $create(SuperMap.OSP.UI.Actions.DrawPolygon, { map: map }, null, null, null);
            drawAction.add_actionCompleted(statistice);
            map.set_action(drawAction);
        }
		
		//统计自定义面内的餐饮店的个数
        function statistice(arg) {

            var statisticParam = new SuperMap.OSP.ExtentionService.StatisticParam();
            statisticParam.dataSourceName = "POI";
            statisticParam.returnFieldNames = ["myIndex"];
            statisticParam.fieldFilters = [null];
            statisticParam.ids = [1];
            statisticParam.customRegions = [arg.geometry];
            var indexObj = document.getElementsByName("radioIndex");
            var indexName = "个数";
            var index = "hotel宾馆酒店.Name";
            var statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.COUNT;
            for (var i = 0; i < indexObj.length; i++) {
                if (indexObj[i].checked) {
                    indexName = indexObj[i].value;
                    break;
                }
            }
            switch (indexName) {
                case "平均值":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.AVERAGE;
                    break;
                case "最大值":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.MAX;
                    break;
                case "最小值":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.MIN;
                    break;
                case "求和":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.SUM;
                    break;
                case "方差":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.VARIANCE;
                    break;
                case "标准差":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.STDDEVIATION;
                    break;
                case "个数":
                    index = "hotel宾馆酒店.SMID";
                    statisticMode = SuperMap.OSP.ExtentionService.StatisticMode.COUNT;
                    break;
            }
            statisticParam.fieldExpressions = [index];
            statisticParam.statisticModes = [statisticMode];
            //绘制矢量图形
            drawFeature(arg.geometry);
            var tjObj = document.getElementById("divStatistic");
            tjObj.innerHTML = "数据正在提交，请稍候……";
            var statisticService = new SuperMap.OSP.ExtentionService.StatisticService();
            statisticService.url = getServiceUrl();
            statisticService.calculate(statisticParam,
                function(result) {
                    if (result) {
                        tjObj.innerHTML = "<li>自定义区域的餐饮店的<b>" + indexName + "</b>统计结果为:<font color=red><b>" + result[0].get("myIndex") + "</b></font></li>";
                    }
                },
                function(errorResult) {
                    tjObj.innerHTML = "<li><font color=red>服务端返回错误信息为：" + errorResult.information + "</font></li>";
                }
                );
        }

		//绘制矢量图 
        function drawFeature(geo) {
            featuresLayer.clearFeatures();
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
            style.stroke = true;
            style.strokeColor = "#00F";
            style.fill = true;
            style.fillColor = "#F00";
            style.opacity = "0.5";
            feature.geometry = geo;
            feature.style = style;
            featuresLayer.addFeature(feature);
        }
	
	/*--------------------统计分析----end------------------*/
	
	
	/*------------------------地址匹配-------begin--------------------------*/
	//地址匹配
		function loadMap4() {
			if(poiManager != null || poiGroup != null){
				poiManager = null;
				poiGroup = null;
			}
            /*map = getMap("mapDiv");
			markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, { bounds: map.get_bounds() }, null, null, null);
			//map.get_compass().setImgLocation("../../images/controls");
			//var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "../../images/controls/compass.png"});
			var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});
			map.addLayer(markerLayer);*/
			poiManager = new SuperMap.OSP.UI.POIManager(map);
			poiManager.markerLayer = markerLayer;
			poiGroup = new SuperMap.OSP.UI.POIGroup("group");
			poiManager.addPOIGroup(poiGroup); 
        }
		 //地址匹配
        function doGeocorder() {
            var myGeo = new SuperMap.OSP.ExtentionService.Geocoder();
			myGeo.url = getServiceUrl();
			// 将地址解析结果显示在地图上,并调整地图视野
			var address = document.getElementById("txtAddress").value;
			var geocoderParam = new SuperMap.OSP.ExtentionService.GeocodeParam();
			geocoderParam.dataSourceName = "zhongguo";
			geocoderParam.datasetName = "table_1P";
			geocoderParam.expectCount = 10;
			geocoderParam.startRecord = 0;
			geocoderParam.address = address;
			geocoderParam.province = document.getElementById("txtProvince").value;
			geocoderParam.city = document.getElementById("txtCity").value;
			var objInfo = document.getElementById("divGeocoder");
			objInfo.innerHTML = "正在获取数据，请稍候……";
			if (address != "") {
				myGeo.getPoint(geocoderParam, function(result) {
					//if (result) {
					//var entitys = result.entities;
					// var point = SuperMap.OSP.Core.Utility.latLonToMeters(result.entities[0].shape.point2Ds[0]);
					var point = result.entities[0].shape.point2Ds[0];
					//var tableHtml = "匹配到" + entitys.length + "个对象<br/><table>";
					if (!!point.x && !!point.y) {
						var x = point.x;
						var y = point.y;
						document.getElementById("geocoderLng").innerHTML = x.toFixed(6);
						document.getElementById("geocoderLat").innerHTML = y.toFixed(6);
						map.zoomToLevel(16, new SuperMap.Web.Core.Point2D(x, y));
						var poi = new SuperMap.OSP.UI.POI("poi" + poiIndex++);
						poi.title = document.getElementById("txtAddress").value;
						var scaledContent = new SuperMap.OSP.UI.ScaledContent();
						scaledContent.content = "<img src='images/pin2.png' />";
						scaledContent.offset = new SuperMap.Web.Core.Point(7, 13);
						poi.scaledContents = scaledContent;
						poi.position = new SuperMap.Web.Core.Point2D(x, y);
						poi.addEventListerner("click", function(e) {
						var osp_poi = eval('(' + e.get_info() + ')');
						var strHtml = 'POI标题：' + osp_poi.title;
						strHtml += '<br/><div style="width:99%; height:50px;background-color:#E4EDF3"></div>';
						var location = new SuperMap.Web.Core.Point2D(e._x, e._y);
						//打开弹窗信息框
						map.openInfoWindow(location, osp_poi.title, strHtml);
						});
						poiGroup.addPOIs(poi);
						poiManager.editPOIGroup(poiGroup);
						poiManager.refreshPOI();
						objInfo.innerHTML = "";
						//} else {
						// objInfo.innerHTML = "没查到结果";
						// }
						// objInfo.innerHTML = "";
						}
					},	function(error){
						objInfo.innerHTML = "很抱歉，你所输入的地址未找到匹配结果";
				}, "北京市");
			} else {
				alert("查询的地址不能为空");
			} 
        }
	/*------------------------地址匹配-------begin--------------------------*/
	
	/*-------------------圆选搜索-----begin----------------*/
			//初始化地图
			function loadMap5() { 
				// map = getMap("mapDiv");
				// markerLayer = $create(SuperMap.Web.Mapping.MarkerLayer, { bounds: map.get_bounds() }, null, null, null);
				// if (Sys.Browser.name != "Microsoft Internet Explorer") {
					// featuresLayer = $create(SuperMap.Web.Mapping.SVGLayer, { bounds: map.get_bounds() }, null, null, null);
				// }
				// else {
					// featuresLayer = $create(SuperMap.Web.Mapping.VMLLayer, { bounds: map.get_bounds() }, null, null, null);
				// }
				// map.addLayer(markerLayer);
				// map.addLayer(featuresLayer);
				// var compass = new SuperMap.Web.Controls.Compass({id:1, imageSrc: "http://www.supermapcloud.com/demo/china/images/controls/compass.png"});
				// var scaleBar = new SuperMap.Web.Controls.ScaleBar({id:2,scaleBarStyle: {"color":"black", "fontSize": 2,"mode":1},pos: {"x":10, "y":10}});
				// var controls = new Array(compass, scaleBar);
				// map.addControls(controls);
				document.getElementById("searchByCircleDiv").innerHTML="";
				document.getElementById("divCircleSearch").innerHTML="";
				
				createMenu();
				createPoiService();
			}
			
			//添加地图右键
			function createMenu(){
				//右键菜单样式，三个参数分别为：菜单背景色，鼠标悬停在某个子项时的背景色和菜单淡入淡出的速度
				var style = new SuperMap.Web.Controls.MenuStyle("#FFFFFF", "#D6E9F8", 300);
				var items = new Array();
				items.push(new SuperMap.Web.Controls.MenuItem("添加中心点",addPOIByMouseCircle));
				//创建右键菜单控件，items 必设，$get('mapDiv')表示在哪个 DOM 元素中创建右键菜单功能，其中 mapDiv 为地图控件的 id 号
				var contentMenu = $create(SuperMap.Web.Controls.ContextMenu, {
					items: items,
					style: style,
					quitButton: true
				}, null, null, $get("mapDiv"));
			}
			
			//初始化POI管理
			function createPoiService(){
				poiManager = new SuperMap.OSP.UI.POIManager(map);
				poiManager.markerLayer = markerLayer;
				poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
				poiSearchGroup.caption = "poi搜索分组";
				var scaledContent = new SuperMap.OSP.UI.ScaledContent();
				scaledContent.content = '<img src="images/num_map/1.png" />';
				scaledContent.offset = new SuperMap.Web.Core.Point(0, 0);
				poiSearchGroup.scaledContents = scaledContent;
				poiManager.addPOIGroup(poiSearchGroup);
			}
			
			//鼠标点击添加一个中心点
			function addPOIByMouseCircle() {
				drawPointCommon = $create(SuperMap.Web.Actions.DrawPoint, { map: map }, null, null, null);
				drawPointCommon.add_actionCompleted(circleDrawCompleted);
				map.set_action(drawPointCommon);
			}
			//缓冲区半径
			var bufferDistance = 1000;
			var realRadius = "";
			var center = null; //定义用户所画的点为圆的中心点
		//画图完成触发事件
		function circleDrawCompleted(drawGeometryArgs) {
            var feature = new SuperMap.Web.Core.Feature();
            var style = new SuperMap.Web.Core.Style();
			var geoPoint = new SuperMap.Web.Core.GeoPoint();
			geoPoint = drawGeometryArgs.geometry;
			center = new SuperMap.Web.Core.Point2D(geoPoint.x,geoPoint.y);
            style.stroke = true;
            style.strokeColor = "#000066";
            style.fill = true;
            style.fillColor = "#000066";
            style.opacity = 0.5;
            feature.geometry = drawGeometryArgs.geometry;
            feature.style = style;
            featuresLayer.addFeature(feature);
			
				var line = new SuperMap.Web.Core.Feature();
					line.geometry = getGeoLine(center, bufferDistance);
					//debugger;
					var pois = line.geometry.parts[0];
					for(var i=0, len = pois.length ; i < len; i++){
						//将线段的点的坐标变成经纬度
						pois[i] = SuperMap.OSP.Core.Utility.metersToLatLon(pois[i]);
					}
				//服务地址
				var serverUrl = getMeasureService();
				var unit = new SuperMap.Web.Core.Units().meter;
				var measureParameters = new SuperMap.Web.iServerJava6R.MeasureParameters(line.geometry, unit);
				var measureService = new SuperMap.Web.iServerJava6R.MeasureService(serverUrl);
				measureService.add_processCompleted(function(result) {
					var measureDistance = result.get_result().get_distance().toFixed(1);
					//利用比例将地图上要显示的正确圆半径的半径值折算出来
					document.getElementById("realRadius").value = (bufferDistance * bufferDistance / measureDistance);
				});
				measureService.processAsync(measureParameters);
			realRadius = document.getElementById("realRadius").value;
			
			//打开弹窗信息框
			var title = "圆选查询";
            var infowinHtml = "";
			var offset = { x: 0, y: -5 };
			infowinHtml = '<div style="width:auto;height:auto;">';
			infowinHtml += '<div class="div1">';
			infowinHtml += '<div class="pop_info_div" style="">';
			infowinHtml += '<ul>';
			infowinHtml += '</ul>';
			infowinHtml += '</div>';
			infowinHtml += '</div><br/>';
			infowinHtml += '<div class="pop_expInfo_div">';
			infowinHtml += '<div id="near" >';
			infowinHtml += '<ul>';
			infowinHtml += '<li>关键字:<input id="txt" type="text" value="银行" size="8"/></li>';
			infowinHtml += '<li><input type="button" value="确定" onclick="circleSearch()"/></li>';
			infowinHtml += '</ul>'
			infowinHtml += '</div>';
			infowinHtml += '</div>';
			map.openInfoWindowByAnchor(center, title, infowinHtml, SuperMap.Web.Mapping.InfoWindowAnchor.ANCHOR_BOTTOMCENTER, offset);
            setPan();
        }
		
		//根据中心点及半径计算圆
			var round = null;
			function markRoundGeometry(center, radius){
				var d360 = Math.PI * 2;
				var roundRegion = new SuperMap.Web.Core.GeoRegion();
				var sidePoints = [];
				var n = 36;
				var d = d360 / n;
				for(var i = 1; i <= n; i++){
					var rd = d * i;
					var x = center.x + radius * Math.cos(rd);
					var y = center.y + radius * Math.sin(rd);
					var sidePoint = new SuperMap.Web.Core.Point2D(x, y);
					sidePoints.push(sidePoint);
				}
				roundRegion.parts = [];
				roundRegion.parts[0] = sidePoints;
				round = new SuperMap.Web.Core.GeoRegion();
				round.parts = [];
				var param = [center, sidePoints[0]];
				round.parts[0] = param;
				return roundRegion;
			}
			
			//根据起点和终点生成线段
			function getGeoLine(center, radius){
				var d = Math.PI * 2;
				var lineP = new SuperMap.Web.Core.GeoLine();
				var sidePoints = [];
				sidePoints.push(center);
				var x = center.x + radius * Math.cos(d);
				var y = center.y + radius * Math.sin(d);
				var point = new SuperMap.Web.Core.Point2D(x, y);
				sidePoints.push(point);
				lineP.parts = [];
				lineP.parts[0] = sidePoints;
				return lineP;		
			}
			
			//圆选查询
			var circleSearchKeyWord = "";
			var circleSearchPoint = "";
			//圆选查询
			function circleSearch(keyword,bufferCenter,point){
				if(!keyword){
					keyword = jQuery("#txt").val();
				}
				if(circleSearchKeyWord = ""){
					circleSearchKeyWord = keyword;
				}
				circleSearchPoint = point;
				if(!bufferCenter){
					bufferCenter = new SuperMap.Web.Core.Point2D(center.x, center.y);
				}
				var radius = realRadius;
				var searchGeometry = markRoundGeometry(bufferCenter,radius);
				var getPOIsParam = new SuperMap.OSP.Service.GetPOIsByGeometryParam();
				var poiSearchService = new SuperMap.OSP.Service.POISearch();
				//getPOIsParam.geometry = searchGeometry;
				getPOIsParam.geometry = round;
				getPOIsParam.keyword = keyword;
				getPOIsParam.expectCount = 10;
				getPOIsParam.startRecord = 0;
				poiSearchService._search.url = getServiceUrl();
				getPOIsParam.datasetName = 'PbeijingP';
				getPOIsParam.DataSourceName = 'china_poi';
				var resultObj = document.getElementById("searchByCircleDiv");
				poiSearchService.getPOIsByGeometry(getPOIsParam, function(result){
					if(result.totalCount != 0){
						circleSearchResult(result.records, result.totalCount);
					}
					else{
						resultObj.innerHTML = "没有找到结果呢！";
						jQuery("#divCircleSearch").html("");
						return false;
					}
					featuresLayer.clearFeature(feature);
					var feature = new SuperMap.Web.Core.Feature();
					feature.geometry = markRoundGeometry(bufferCenter, radius);
					feature.style = getGeoStyle('buffer');
					feature.id = 0;
					feature.title = "圆选查询";
					featuresLayer.addFeature(feature);
					map.get_infoWindow().hide();
					jQuery("#divCircleSearch").html("");
				},function(error){
					//alert(error.infomation);
					resultObj.innerHTML = error.infomation;
				});
			}
			//取得圆的风格
			function getGeoStyle(type) {
				//线条颜色
				var style = new SuperMap.Web.Core.Style();
				style.stroke = true;
				style.strokeColor = 1;
				style.strokeColor = "#0000FF";
				style.fill = true;
				if (type = "buffer") {
					style.fillColor = "#0000FF";
					style.opacity = 0.2;
					style.lineWidth = 2;
				} else {
					style.fillColor = "#FF0000";
					style.opacity = 0.5;
					style.lineWidth = 2;
				}
				return style;
			}
			
			var nearCenterPoi = null;
			var nearBySearchHashMap = null;
			//圆选查询结果函数
			function circleSearchResult(records, totalCount){
				var strHtml = "";
				if (records) {
					//装载查询到的POI对象
					nearBySearchHashMap = new SuperMap.OSP.Core.HashMap();
					poiSearchGroup.clearPOIs();
					for (var i = 0; i < records.length; i++) {
						var poiInfo = records[i];
						nearBySearchHashMap.add(poiInfo.code, poiInfo);
						var itemId = "poi_info_item_"+ i;
						strHtml += '<div id="'+ itemId +'" onmouseover="poiInfoItemMouseover(\'' + itemId + '\')" onmouseout="poiInfoItemMouseout(\'' + itemId + '\')" class="poi_info_item">';
						//添加图标
						strHtml += '<div style="float:left;"><img src="images/' + (i + 1) + '.gif"></div>';
						//添加内容
						strHtml += '<div style="margin-left:20px; padding-left:4px;"><div style="clear:right;"><div style="float:right; width:50px;"></div></div>';
						//添加信息标题
						if(!!poiInfo.name) {
							strHtml += '<div id="poi_title"><span><strong><a href="javascript:positionPoi(\''  + poiInfo.code + '\',1)">' + poiInfo.name + '</a></strong></span></div>';
							//<strong><a href="javascript:positionPoi(\'' + poiInfo.code + '\',1)">' + poiInfo.name + '</a></strong>
						}
						//添加地址信息
						if(!!poiInfo.address) {
							strHtml += '<div id="poi_address"><span style="margin-top:4px; font-size:12px;">地址：' + poiInfo.address + '</span></div>';
						}
						//添加邮编信息
						if(!!poiInfo.zipCode) {
							strHtml += '<div id="poi_zipcode"><span style="margin-top:4px; font-size:12px;">邮编：' + poiInfo.zipCode + '</span></div>';
						}
						//添加电话信息
						if(!!poiInfo.telephone) {
							strHtml += '<div id="poi_telephone"><span style="margin-top:4px; font-size:12px;">联系电话：' + poiInfo.telephone + '</span></div>';
						}
			
						strHtml += '</div></div>';
						
            
						//组织poi对象
						var poi = new SuperMap.OSP.UI.POI("search_POI_" + i);
						poi.position = new SuperMap.Web.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
						var scaledContent = new SuperMap.OSP.UI.ScaledContent();
						//服务端约定好'|_['代表'<'符号，']_|'代表'>'符号
						scaledContent.content = '<img src="images/num_map/' + (i + 1) + '.png" />';
						scaledContent.offset = new SuperMap.Web.Core.Point(17, 26);
						poi.title = poiInfo.name;
						poi.scaledContents = scaledContent;
						poi.properties = {
							code: poiInfo.code
						};
						poiSearchGroup.addPOIs(poi);
					}
					document.getElementById("searchByCircleDiv").innerHTML = strHtml;
					poiManager.editPOIGroup(poiSearchGroup);
					//debugger;
					poiManager.refreshPOI();
					
					if(totalCount == 1){
						positionPoi(poiInfo.code, 1);
					}
					if(center != null){
						map.zoomToLevel(15, new SuperMap.Web.Core.Point2D(center.x, center.y));
					}
				}
				else {
					document.getElementById("searchByCircleDiv").innerHTML = "没有找到您要的结果！";
				}
			}
		//面板POI内容移入事件
		function poiInfoItemMouseover(id) {
			document.getElementById(id).className = "poi_info_item poi_info_mouseover";
		}

		//面板POI内容移出事件
		function poiInfoItemMouseout(id) {
			document.getElementById(id).className = "poi_info_item";
		}
		
		//------实现定位------//
		function positionPoi(code){
			/*if(poiHashMap){
				var poiInfo = poiHashMap.get(code);
				if(poiInfo == null){
					poiInfo = nearBySearchHashMap.get(code);
				}
			}else{*/
				var poiInfo = nearBySearchHashMap.get(code);
			//}
    
			var x = poiInfo.x;
			var y = poiInfo.y;
			var point2d = new SuperMap.Web.Core.Point2D(x, y);
			//左侧列表单击定位
			//map.zoomToLevel(15, point2d);
			map.panTo(point2d);
			//popup(code);
		}
		
	
	
	/*-------------------圆选搜索-----end----------------*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	