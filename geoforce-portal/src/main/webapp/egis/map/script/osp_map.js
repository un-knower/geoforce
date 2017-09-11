var busPath_start = "busPath_start";	//路线起点表单id
var busPath_end = "busPath_end";	//路线终点表单id

$(function(){
	$('.logo img').click(function(){
		location.href = "http://www.dituhui.com";
	});
	$('.logo span').click(function(){
		location.href = "../";
	});

	resizeApp('initialize');
	$(window).resize(function(){
		resizeApp('resize');
	});

	//搜索类型改变
	jQuery("ul#searchTab li").bind({
		mouseover: function(){
			jQuery(this).addClass("searchTabHover");
		},
		mouseout: function(){
			jQuery(this).removeClass("searchTabHover");
		},
		click: function(){
			//改变样式
			jQuery("ul#searchTab li").removeClass("searchTabSelect");
			jQuery(this).addClass("searchTabSelect");
			//改变搜索框
			if(jQuery("ul#searchTab li:eq(0)").attr("class").indexOf("searchTabSelect")!=-1){//关键字搜索
				jQuery("#searchTabKeywords").css("display", "block");
				jQuery("#searchTabPath").css("display", "none");
				jQuery("#busTip").css("display","none");//隐藏提示窗口
			}else{
				jQuery("#searchTabKeywords").css("display", "none");
				jQuery("#searchTabPath").css("display", "block");
				jQuery("#busTip").css("display","block");//弹出提示窗口
			}
		}
	});
	//搜索关键字事件
	jQuery("#img_searchPOI").click(function(){
		if(jQuery.trim(jQuery("#"+keywordTextId).val()) == ""){
			alert("请输入搜索关键字！");
			return;
		}
		searchPOI();
	});

	//搜索自动补全事件
	jQuery("#ssearch_box_input").onchange(function(){
		var e = typeof(event) == "undefined" ? null:event;
		autoCompleteBySearch(e,"ssearch_box_input");
	});
	
	//搜索线路提交事件
	jQuery("#"+busPath_start).initRemindText("请输入起点");
	jQuery("#"+busPath_end).initRemindText("请输入终点");
	jQuery("#img_searchPath").click(function(){
		
		//验证输入
		if(jQuery.isDomEmpty(jQuery("#"+busPath_start))){
			alert("请输入搜索关键字！");
			return;
		}
		if(jQuery.isDomEmpty(jQuery("#"+busPath_end))){
			alert("请输入搜索关键字！");
			return;
		}
		//显示搜索结果面板，隐藏推荐面板
		jQuery("#div_recommendPanel").css("display","none");
		jQuery("#div_searchResultPanel").css("display","block");
		jQuery("#searchPOIResult").css("display","none");	//隐藏线路结果div
		if(jQuery("ul#searchTab li:eq(1)").attr("class").indexOf("searchTabSelect")!=-1){//公交搜索
			jQuery("#searchBusResult").css("display","block");	//隐藏线路结果div
			jQuery("#findPath_result").css("display","none");	//隐藏线路结果div
			busPath();
		}else{//自驾搜索
			jQuery("#searchBusResult").css("display","none");	//隐藏线路结果div
			jQuery("#findPath_result").css("display","block");	//隐藏线路结果div
			busPath();
		}
	});
	//线路起始点交换
	jQuery("#img_swapSite").click(function(){
		var startSite = jQuery("#"+busPath_start).val();
		var endSite = jQuery("#"+busPath_end).val();
		jQuery("#"+busPath_start).val(endSite);
		jQuery("#"+busPath_end).val(startSite);
	});
	//热门关键字点击搜索事件
	jQuery("#div_recommendPanel ul li").click(function(){
		jQuery("#searchTab li:eq(0)").click();	//显示搜索面板
		var keywords = jQuery(this).find("a span").html();
		jQuery("#ssearch_box_input").val(keywords);
		jQuery("#img_searchPOI").click();	//查询触发
	});
	//热词面板中热词搜索事件
	jQuery("#div_recommendHotPanel dl dd ul li").click(function(){
		jQuery("#searchTab li:eq(0)").click();	//显示搜索面板
		var keywords = jQuery(this).html();	//搜索热词关键字
		keywords = jQuery.trim(keywords);
		jQuery("#ssearch_box_input").val(keywords);
		jQuery("#img_searchPOI").click();	//查询触发
		jQuery('div.hot_popup').css('display', 'none');	//隐藏热词面板
	});
	//折叠栏点击事件,显示或隐藏左侧面板
	jQuery("#hideButton img").click(function(){
		var leftFrame = jQuery("#leftbar");
		if(jQuery(this).attr("src") == "images/midArrow_left.gif"){	//收起左侧
			leftFrame.css("display", "none");
			jQuery(this).attr({title: "展开左侧栏", src: "images/midArrow_right.gif"});
		}else{//展开左侧
			leftFrame.css("display", "block");
			jQuery(this).attr({title: "收起左侧栏", src: "images/midArrow_left.gif"});
		}
		resizeApp('resize');	//地图自适应
	});
});



		
/**
 * jQuery扩展
 * @author mwang
 */
jQuery.extend({
	/**
	 * 验证表单是否输入了文字
	 * @param dom	验证的表单元素，jQuery的dom对象
	 */
	isDomEmpty: function(dom){
		if(dom.val()=="" || dom.attr("remindText") == dom.val()){
			return true;
		}else{
			return false;
		}
	}
});
/**
 * jQuery扩展
 * @author mwang
 */
jQuery.fn.extend({
	/**
	 * 文本默认显示控件
	 * @param options 传入参数
	 */
	initRemindText: function(options){
		//参数默认值
		var settings = {
			remindText: "",	//提示文字
			defaultColor: "black",	//默认文本颜色
			remindColor: "grey"	//提示输入颜色
		};
		//参数处理
		if(typeof(options) == "string"){
			settings.remindText = options;
		}else{
			jQuery.extend(settings, options);
		}
		//设置表单为提示输入状态
		var SetRemindState = function(dom){
			dom.val(settings.remindText).css("color", settings.remindColor);
		};
		//设置表单为默认状态
		var SetDefaultState = function(dom){
			dom.val("").css("color", settings.defaultColor);
		};
		
		this.each(function() {
			var itemDom = jQuery(this);
			if(itemDom.css("color") != "rgb(0, 0, 0)"){//获取每个表单的默认文本颜色
				settings.defaultColor = itemDom.css("color");
			}
			itemDom.attr("remindText", settings.remindText);	//添加提示文本属性
			SetRemindState(itemDom);
			itemDom.bind({
				focus: function(){
					if(itemDom.val() == settings.remindText){
						SetDefaultState(itemDom);
					}
				},
				blur: function(){
					if(itemDom.val() == ""){
						SetRemindState(itemDom);
					}
				}
			});
		});
	},
	/**
	 * 监听input的change事件，实践中this代表window对象
	 * @param callback	监听处理函数
	 */
	onchange: function(callback){
			var dom = jQuery(this)[0];	//原生javascript对象
			if(dom){
				dom.callback = function(){};
				if(jQuery.browser.msie){	//如果是ie浏览器
					dom.onpropertychange = function() {
						if(jQuery.isFunction(callback)){
							callback();
						}
					};
				}else{	//其他浏览器
					dom.oninput = function() {
						if(jQuery.isFunction(callback)){
							callback();
						}
					};
					/*if (window.addEventListener) {
						dom.addEventListener("input",
							function() {
								if(typeof(changedFunction) == "function"){
									callback();
								}
							},false);
					}*/
				}
			}
	}
});



////////----------------------------------------

/**
 * 清空地图的所有数据,并设置平移状态
 * @author: mwang
 */
function mapClearAll(){
	middleCount = 0;
	dragFeature.deactivate();
	spSelected = false;//是否已有起始点
	epSelected = false;//是否已有终止点
	if(map){
		//清空地图数据
		featuresLayer.removeAllFeatures();
		vectorLayer.removeAllFeatures();
		if(poiSearchGroup.pois.length != 0){
			poiManager.removePOIGroup(poiSearchGroup);
			poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
			poiSearchGroup.caption = "poi搜索分组";
			var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			scaledContent.content = '<img src="images/num_map/1.png" />';
			scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
			poiSearchGroup.scaledContents = scaledContent;
			poiManager.addPOIGroup(poiSearchGroup);
			poiManager.editPOIGroup(poiSearchGroup);
			poiManager.refreshPOI();
		}
		if(poiPathGroup.pois.length != null){
			featuresLayer.removeFeatures(pathFeatures);
			poiManager.removePOIGroup(poiPathGroup); 
			startPoint = null;
			startPoint = new SuperMap.OSP.Core.Point2D();
			endPoint = null;
			endPoint = new SuperMap.OSP.Core.Point2D();
			poiPathGroup = new SuperMap.OSP.UI.POIGroup("poipathGroupId");
			poiManager.addPOIGroup(poiPathGroup); 
		}
		if(busPathGroup != null){
			poiManager.removePOIGroup(busPathGroup); 
			busPathGroup = new SuperMap.OSP.UI.POIGroup("buspathGroupId");
			poiManager.addPOIGroup(busPathGroup);
			poiManager.refreshPOI();
		}
		if(poiMarkerGroup!=null){
			poiManager.removePOIGroup(poiMarkerGroup);
			poiMarkerGroup = new SuperMap.OSP.UI.POIGroup("poiMapMarker_id");
			poiManager.addPOIGroup(poiMarkerGroup);
			poiManager.refreshPOI();
		}
		if(poiMeasurementGroup != null){
			poiManager.removePOIGroup(poiMeasurementGroup);
			poiMeasurementGroup = new SuperMap.OSP.UI.POIGroup("poigroup_measurement_id");
			poiManager.addPOIGroup(poiMeasurementGroup);
			poiManager.refreshPOI();
		}
		//关闭信息窗
		var popCount = mapPopups.length;
		if(mapPopups.length > 0){
			for(var i=0; i<popCount; i++){
				// map.removePopup(mapPopups[i]);
				mapPopups[i].hide();
			}
		}
		jQuery("#div_searchResultPanel").css("display","none");
		jQuery("#search_result").html("");
		jQuery("#divPage").html("");
		jQuery("#findPath_r").html("");
		jQuery("#busPath_r").html("");
		jQuery("#div_recommendPanel").css("display","block");
		jQuery("#idea").css("display","block");
		jQuery("#div_recommendTitlePanel").css("display","block");
		jQuery("#div_searchPOITitlePanel").css("display","none");
		
	}
};

function copySuccess() {
    jQuery("#labelInfo").show("slow");
    setTimeout(function() {
        jQuery("#labelInfo").hide("slow");
    }, 2000);
}

// 关闭地图共享面板
function closeShareMapDiv() {
    jQuery("#divShareMap").hide();
}


//获取地图名称
function getMapName(){
    var mapName = "quanguo";
    return mapName;
}

//获取比例尺
function getMapScales(){
    //var scales = [1 / 470000000, 1 / 235000000,1 / 117500000, 1 / 58750000, 1 / 29375000, 1 / 14687500, 1 / 7343750, 1 / 3671875, 1 / 1835937, 1 / 917968, 1 / 458984, 1 / 229492, 1 / 114746, 1 / 57373, 1 / 28686, 1 / 14343, 1 / 14343, 1 / 7171, 1 / 3585, 1 / 1792];
	var scales = [1 / 470000000, 1 / 235000000,1 / 117500000, 1 / 58750000, 1 / 29375000, 1 / 14687500, 1 / 7343750, 1 / 3671875, 1 / 1835937, 1 / 917968, 1 / 458984, 1 / 229492, 1 / 114746, 1 / 57373, 1 / 28686, 1 / 14343, 1 / 7171, 1 / 3585, 1 / 1792];
    return scales;
}

//版权
function addCopy(div){
    var copyDiv = document.createElement("div");
    copyDiv.innerHTML = "&copy;2011 <a href='" + getServiceUrl + "' target='_blank'>Cloud Plan</a>";
    copyDiv.style.position = "absolute";
    copyDiv.style.bottom = "10px";
    copyDiv.style.right = "10px";
    copyDiv.style.zIndex = 99999;
    copyDiv.style.fontSize = "10px";
    div.appendChild(copyDiv);
}

//添加鼠标位置信息
function addMousePosition(obj) {
	mousePosDiv.innerHTML = "x: " + obj.mapCoord.x + "  y: " + obj.mapCoord.y;
}

var map = null;
var markersLayer = null;
var poiMarkersLayer = null;

var mapPopups = []; //保存所有打开的信息窗，在清空地图的时候关闭方便;

var featuresLayer = null;
var mapDivId = "smLayer";
var baseCloudLayer = true; //当前图层是否是导航图

var poiManager = null; //poi显示管理
var poiSearchGroup = null; //poi搜索结果分组
var poiMarkerGroup = null; //poi标记点分组
var poiPathGroup = null;
var busPathGroup = null;
var poiMeasurementGroup = null;
var resultDivId = "search_result";//结果显示div的ID
var pagerId = "divPage"; //分布div的ID
var currentMode = "2D"; //当前模式为2D
//var poiDatasetNames = ['PgansuP', 'PfujianP', 'PchongqingP', 'PbeijingP', 'PanhuiP', 'Pheilongjiang', 'PhebeiP', 'PhainanP', 'PguizhouP', 'PguangxiP', 'Phenan', 'PxizangP', 'PxinjiangP', 'PtianjinP', 'PsichuanP', 'PshanxiP', 'PshanghaiP', 'PshandongP', 'Pshan3xiP', 'PqinghaiP', 'Pningxia', 'Pneimenggu', 'Pliaoning', 'Pjilin', 'Pjiangxi', 'Pjiangsu', 'PzhejiangP', 'Phubei', 'Phunan', 'PguangdongP'];

var secondPointStart = "";//地图分享后做二次操作的起始点
var secondPointEnd = "";//地图分享后做二次操作的终止点

var mousePosDiv = document.getElementById("mousePos");
var leftBottom,mapWidth,mapHeight;
//地图初始化
var layer = null;
var measureLine;
var measurePoint;
var vectorLayer;
var dragFeature;
var mousePos;
var controls = null;
var pan_zoombar = null;
function initializeMap(){

	var param = GetUrlArgs(document.URL);
	if (param && param.keyId) {
		shareContentMap(param);
	}
	else{	
		var res = [156543.033928041, 78271.5169640203, 39135.7584820102, 
            19567.8792410051, 9783.93962050254, 4891.96981025127, 2445.98490512563, 
            1222.99245256282, 611.496226281409, 305.748113140704, 152.874056570352, 
            76.4370282851761, 38.218514142588, 19.109257071294, 9.55462853564701, 
            4.77731426782351, 2.38865713391175, 1.19432856695588, 0.59716428347793];
            // 4.77731426782351, 2.38865713391175, 1.19432856695588, 0.597164283477938,0.2985821417 ];

		// layer = new SuperMap.Layer.CloudLayer( {name:"导航地图"} );
		layer = new SuperMap.Layer.CloudLayer({resolutions: res.slice(2),getTileUrl:function (xyz) {
                    var me = this,
                            url = me.url;
                    return SuperMap.String.format(url, {
                        mapName: me.mapName,
                        type: me.type,
                        x: xyz.x,
                        y: xyz.y,
                        z: xyz.z+2
                    });
                }});
		// layer.url = cachedUrl + "/FileService/image?map=${mapName}&type=${type}&x=${x}&y=${y}&z=${z}";
		
		
		markersLayer = new SuperMap.Layer.Markers("maker");
		poiMarkersLayer = new SuperMap.Layer.Markers("poiMarker");
		featuresLayer = new SuperMap.Layer.Vector();
		vectorLayer = new SuperMap.Layer.Vector("dragVector");
		measureLine = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Path, { multi: true });
		measureLine.events.on({"featureadded": drawCompleted});
		measurePoint = new SuperMap.Control.DrawFeature(featuresLayer,SuperMap.Handler.Point, { multi: true });
		featuresLayer.style = {fillColor: "#7C9DE4",strokeColor: "#7A9BE2",pointRadius:6,strokeWidth:4};

		pan_zoombar = new SuperMap.Control.PanZoomBar({showSlider:true,levelsDesc:{levels:[7,11,1,14],imageSources:["theme/images/zoom_city.png","theme/images/zoom_province.png","theme/images/zoom_street.png","theme/images/zoom_country.png"]}});

		controls = [
				new SuperMap.Control.ScaleLine(),
				pan_zoombar,
                new SuperMap.Control.Navigation({
                    dragPanOptions: {
                        enableKinetic: true
                    }
                }),
				measureLine,
				measurePoint
		];

		map = new SuperMap.Map("smLayer", { controls: controls,units: "m", allOverlays: true});
		
		map.addLayers([layer, featuresLayer, vectorLayer, markersLayer, poiMarkersLayer]);
		
		dragFeature = new SuperMap.Control.DragFeature(vectorLayer);
		map.addControl(dragFeature);


		var ovMap = new SuperMap.Control.OverviewMap();
		var ovLayer = new SuperMap.Layer.CloudLayer({resolutions: res.slice(2,18),getTileUrl:function (xyz) {
            var me = this,
            url = me.url;
            return SuperMap.String.format(url, {
                mapName: me.mapName,
                type: me.type,
                x: xyz.x,
                y: xyz.y,
                z: xyz.z+2
            });
        }});
		ovMap.layers = [ovLayer];	 
		map.addControl(ovMap);	


		/*
		var mousePosition = new SuperMap.Control.MousePosition(
			{
				numDigits:2, 
				displayProjection: new SuperMap.Projection("EPSG:4326")
			}
		);
		map.addControl(mousePosition);
		console.log(mousePosition);
		*/
		map.render("smLayer");
		dragFeature.geometryTypes = ['SuperMap.Geometry.Point'];
		if (typeof(currentCityInfo)!="undefined" && currentCityInfo!=null && typeof(currentCityInfo.cityName)!="") {
			map.setCenter(new SuperMap.LonLat(currentCityInfo.x, currentCityInfo.y),currentCityInfo.level);
			// map.setCenter(new SuperMap.LonLat(currentCityInfo.x, currentCityInfo.y),10);
		}else{
			map.setCenter(new SuperMap.LonLat(12958399.4681885, 4852082.44060595),8);
		}
		createPoiService();
		cityChange();

		map.events.on({'moveend':cityChange});
    }
	
    //地图右键
    createMenu();
	
	jQuery('#ssearch_box_input').bind('keyup',enterSearch);
	
	//地区选择控件
	initSMapSelWidget();
	//加载分类搜索
	//loadTopTypes('1');
	jQuery("#mapNum").css('display','block');
	jQuery("#ssearch_box_input").focus();

	//屏蔽不可用的功能
	if(isDataHide){
		jQuery('#li_sharemap').css('display', 'none');
	}
	
	
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
		id:"divSMapSel",//页面div
		defaultData:{text:cityName,value:"100100"}//默认显示的数据
	});
	
	smapCity.setAfterSelect(function(data){
		cityName = data.text;
		//加载分类搜索
		//loadTopTypes(data.level);
		featuresLayer.removeAllFeatures();
		jQuery("#span_searchRegion").html(data.text);
		divisionCode = data.value;
		parentCode = data.parentCode;
		selCode = divisionCode;
		selLevel = data.level;
		selCode = divisionCode;
		
		var point2d = new SuperMap.OSP.Core.Point2D(parseFloat(data.x), parseFloat(data.y));
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
			var weatherData = new weather(); 
			weatherData.getWeather(divisionCode,data.text);
			var geometry = new SuperMap.OSP.Utility.Geometry();
			geometry.point2Ds = [point2d];
			var searchService = new SuperMap.OSP.Service.Search();
			searchService.url = ospServiceUrl;
			var queryParam = new SuperMap.OSP.Service.QueryParam();
			queryParam.returnResultSetInfo = SuperMap.REST.QueryOption.ATTRIBUTEANDGEOMETRY;
			queryParam.expectCount = 1;
			queryParam.startRecord = 0;
			var queryDatasetParam = new SuperMap.OSP.Service.QueryDatasetParam();
			queryDatasetParam.name = "ChinaProvince";
			queryDatasetParam.sqlParam = new SuperMap.OSP.Service.SqlParam();
			queryDatasetParam.sqlParam.attributeFilter = null;
			queryDatasetParam.sqlParam.returnFields = ["Name"];
			queryParam.queryDatasetParams = [queryDatasetParam];
			var queryByGeometryParam = new SuperMap.OSP.Service.QueryByGeometryParam();
			queryByGeometryParam.geometry = geometry;
			queryByGeometryParam.dataSourceName = "cloud_area_data";
			queryByGeometryParam.queryParam = queryParam;
			queryByGeometryParam.queryMode = SuperMap.REST.SpatialQueryMode.WITHIN;
			searchService.queryByGeometry(queryByGeometryParam,function(result){
				if(result){
					var line = new SuperMap.Geometry.LineString(result.records[0].shape.point2Ds);
					var lineFeature = new SuperMap.Feature.Vector(line);
					featuresLayer.addFeatures(lineFeature);
				}
			},function(error){
			
			});
		}
		map.setCenter(new SuperMap.LonLat(point2d.x,point2d.y),level );
	});	
}
	var Provinces = null;
	var City = null;
	var oldCityName = ""; //记录上一个城市名，以便在城市名改变之后更新天气
//地图和城市面板联动
var tempCenter = null;
var tempLevel = 11;
var weatherData = null; 
function cityChange(){
	var level = map.getZoom();
	var center = map.getCenter();
	var div = jQuery("#selectCityDiv");
	var previousRegionName = div.html();
	var geometry = new SuperMap.OSP.Utility.Geometry();
	geometry.point2Ds[0] = new SuperMap.OSP.Core.Point2D(center.lon,center.lat);

	var searchService = new SuperMap.OSP.Service.Search();
	searchService.url = ospServiceUrl;

	var queryParam = new SuperMap.OSP.Service.QueryParam();
	queryParam.returnResultSetInfo = SuperMap.REST.QueryOption.ATTRIBUTE;
	queryParam.expectCount = 1;
	queryParam.startRecord = 0;

	var queryDatasetParam = new SuperMap.OSP.Service.QueryDatasetParam();
	if(level <=4){
		div.html("全国");
		return false;
	}else if(center.lon != tempCenter && level <=7){
		queryDatasetParam.name = "ChinaPointSet";
		tempCenter = center.lon;
	}else if(center.lon != tempCenter && level >7){
		// queryDatasetParam.name = "ChinaCity";
		queryDatasetParam.name = "ChinaPointSet";
		tempCenter = center.lon;
	}else{
		jQuery("#span_searchRegion").html(cityName);
		jQuery("#selectCityDiv").html(cityName);
		if(oldCityName != cityName && cityName != ""){
			if(cityName.indexOf("市") != -1){
				if(!weatherData){
					weatherData = new weather(); 
				}
				weatherData.getWeather();
			}
		}
		oldCityName = jQuery("#selectCityDiv").text();
		return false;
	}
	queryDatasetParam.sqlParam = new SuperMap.OSP.Service.SqlParam();
	queryDatasetParam.sqlParam.attributeFilter = null;
	queryDatasetParam.sqlParam.returnFields = ["NSHENG", "NDI"];
	queryParam.queryDatasetParams = [queryDatasetParam];
	var queryByGeometryParam = new SuperMap.OSP.Service.QueryByGeometryParam();
	queryByGeometryParam.geometry = geometry;
	// queryByGeometryParam.dataSourceName = "cloud_area_data";
	queryByGeometryParam.dataSourceName = "china_poi";
	queryByGeometryParam.queryParam = queryParam;
	queryByGeometryParam.queryMode = SuperMap.REST.SpatialQueryMode.WITHIN;
	searchService.queryByGeometry(queryByGeometryParam,function(result){
		 var poiSearchResult = new SuperMap.OSP.Service.POISearchResult();
		 if (result && result.currentCount > 0) {
            var record = result.records[0];

            //省级名称
            if(level <= 7){
            	cityName = record.fieldValues[0];
            }
            //市级名称
            else if(level > 7){
            	cityName = record.fieldValues[1];
            }

			jQuery("#span_searchRegion").html(cityName);
			jQuery("#selectCityDiv").html(cityName);

			//改变城市后获取天气
			for(var i = 0; i < citys.length; i++){
				if( cityName == citys[i].city ){
					divisionCode = citys[i].admincode;
					if(oldCityName != cityName && cityName!=""){
						if(!weatherData){
							weatherData = new weather();
						}
						weatherData.getWeather(divisionCode, cityName);
					}
					oldCityName = jQuery("#selectCityDiv").text();
					break;
				}
			}
		}
		else{
			//没有返回结果，默认显示“全国”
			cityName = "全国";
			jQuery("#span_searchRegion").html(cityName);
			jQuery("#selectCityDiv").html(cityName);
			oldCityName = jQuery("#selectCityDiv").text();
		}
	},
	function(error){
		cityName = "全国";
		jQuery("#span_searchRegion").html(cityName);
		jQuery("#selectCityDiv").html(cityName);
		oldCityName = jQuery("#selectCityDiv").text();		
	});
}
//初始化POI管理
function createPoiService(){
	poiManager = new SuperMap.OSP.UI.POIManager(map);
    poiManager.markerLayer = markersLayer;
    poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
    poiSearchGroup.caption = "poi搜索分组";
    var scaledContent = new SuperMap.OSP.UI.ScaledContent();
    scaledContent.content = '<img src="images/num_map/1.png" />';
    scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
    poiSearchGroup.scaledContents = scaledContent;
    poiManager.addPOIGroup(poiSearchGroup);
	
	poiPathGroup = new SuperMap.OSP.UI.POIGroup("poipathGroupId");
    poiManager.addPOIGroup(poiPathGroup);   
	
	busPathGroup = new SuperMap.OSP.UI.POIGroup("buspathGroupId");
	poiManager.addPOIGroup(busPathGroup);
	
	poiMeasurementGroup = new SuperMap.OSP.UI.POIGroup("poigroup_measurement_id");
	poiManager.addPOIGroup(poiMeasurementGroup);
	//路径分析起始点、终止点初始化
	poiMarkerGroup = new SuperMap.OSP.UI.POIGroup("poiMapMarker_id");
	poiManager.addPOIGroup(poiMarkerGroup);
}

function enterSearch(e) {
	//当键盘Enter键按下，并且输入值不为空的情况进行查询
	e = e ? e : window.event;
	if(e.keyCode == 13) {
		if(jQuery.trim(jQuery("#"+keywordTextId).val()) == ""){
			// alert("请输入搜索关键字！");
			return;
		}
		else{
			searchPOI();
		}
	}
	else {
		return;
	}
}

//-------------地图控件-----------//

//右键菜单控件
function createMenu(){
    var style = new SuperMap.MenuStyle({
				backColor: "#FFFFFF",
				overColor: "#D6E9F8",
				speed: 100});
    var items = new Array();
		items.push(new SuperMap.MenuItem({
				title:"以此处为出发点的线路",
				callBack:function(e, pos){
					selectMenuPoint("start",pos);
				}
				}));
		items.push(new SuperMap.MenuItem({
				title:"途经点",
				callBack:function(e,pos){
					middlePoint("m",pos);
				}
				}));
		items.push(new SuperMap.MenuItem({
				title:"到达目的地的线路",
				callBack:function(e, pos){
					selectMenuPoint("end", pos);
				}
				}));
		items.push(null);
		items.push(new SuperMap.MenuItem({
				title: "放大",
				callBack:function(e,pos){
					map.zoomIn();
				}
				}));
		items.push(new SuperMap.MenuItem({
				title: "缩小",
				callBack:function(e,pos){
					map.zoomOut();
				}
				}));
		items.push(new SuperMap.MenuItem({
				title:"此处居中放置地图",
				callBack:function(e,pos){
					var px = new SuperMap.Pixel(pos.x,pos.y);
					var point = map.getLonLatFromPixel(px);
					map.panTo(point);
				}
				}));
		items.push(new SuperMap.MenuItem({
				title:"打开位置坐标",
				callBack:function(e,pos){
					mousePosition();
				}
				}));	
    var contextMenu = new SuperMap.ContextMenu({
		items: items,
		style: style,
		mapContainer: document.getElementById("smLayer")
		});
}
//================================鼠标位置statr================================
var current = 'open';
function mousePosition(){
	if(current == 'open'){
		var len = jQuery("#myMenu li").length;
		var ele = $($("#myMenu li")[len-1]).children('a:first-child').html("关闭位置坐标");
		$("#smLayer").bind('mousemove',function() {
			$("#divMouse").css('display','block');
			var divx =arguments[0].clientX-341;
			var divy = arguments[0].clientY-55;
			var meter = map.getLonLatFromPixel(new SuperMap.Pixel(divx,divy));
			var lonlat = SuperMap.OSP.Core.Utility.metersToLatLon(new SuperMap.LonLat(meter.lon,meter.lat));
			var lon = lonlat.lon+"";
			if(lon.substr(3,1) == "."){
				lon = lon.substr(0,6);
			}else{
				lon = lon.substr(0,5);
			}
			var lat = lonlat.lat+"";
			if(lat.substr(3,1) == "."){
				lat = lat.substr(0,6);
			}else{
				lat = lat.substr(0,5);
			}
			var divmove = document.getElementById("divMouse");
			divmove.innerHTML = '<div style="width:96px;height:17px;padding:2px;border:1px solid #ff0000;text-align:center; background-color:white;font-size:12px">' + lon + ','+lat+'</div>';
		if (!divmove) {
			return;
		}
		divmove.style.left = divx;
		divmove.style.top = divy;
		});
		current = 'close'
	}else if(current == 'close'){
		var len = jQuery("#myMenu li").length;
		var ele = $($($("#myMenu li")[len-1]).children('a')[0]).html("打开位置坐标");
		
		$("#smLayer").unbind('mousemove');
		$("#divMouse").css('display','none');
		current = 'open';
	}
}
//================================路径分析start================================

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
var startPoint = null;//路径分析起始点
var endPoint = null;//路径分析结束点
var spSelected = false;//是否已有起始点
var epSelected = false;//是否已有终止点
var findPathStart = "";//保存用户查询起始点
var findPathEnd = "";//保存用户查询终止点
//新路径分析方法
function navigationFindPath(start,end) {
	//隐藏搜索结果标题
	jQuery("#divPage").css('display', 'none');
	jQuery("#div_recommendTitlePanel").css("display","block");
	jQuery("#div_searchPOITitlePanel").css("display","none");
	jQuery("#search_result").css("display","none");
	var param = new SuperMap.OSP.Service.TransportionAnalystParameter();
	//判断start和end是否为空。如果非空，走地图分享的路径分析方法。
	if(start != null && end != null){
		var scaledContentStart = new SuperMap.OSP.UI.ScaledContent;
        scaledContentStart.content = "images/start.png";
        scaledContentStart.offset = new SuperMap.OSP.Core.Point2D(0, 0);
		var scaledContentEnd = new SuperMap.OSP.UI.ScaledContent;
		scaledContentEnd.content = "images/end.png";
		scaledContentEnd.offset = new SuperMap.OSP.Core.Point2D(0, 0);
		var poiStart = new SuperMap.OSP.UI.POI("poistartId");
		var poiEnd = new SuperMap.OSP.UI.POI("poiendId");
		var metersStrat = new SuperMap.OSP.Core.Utility.latLonToMeters(start);
		var metersEnd =  new SuperMap.OSP.Core.Utility.latLonToMeters(end);
		secondPointStart = metersStrat;
		secondPointEnd = metersEnd;
        poiStart.position = new SuperMap.OSP.Core.Point2D(metersStrat.x, metersStrat.y);
        poiStart.title = "起点";
		poiStart.imageSize = new SuperMap.Size(29,33);
        poiStart.scaledContents = scaledContentStart;
		poiEnd.position = new SuperMap.OSP.Core.Point2D(metersEnd.x, metersEnd.y);
		poiEnd.title = "终点";
		poiEnd.imageSize = new SuperMap.Size(29,33);
		poiEnd.scaledContents = scaledContentEnd;
		//createPoiService();
		if (poiPathGroup.getPOIs("poistartId")) {
                poiPathGroup.removePOIs("poistartId");
                poiManager.editPOIGroup(poiPathGroup);
                poiPathGroup.addPOIs(poiStart);
                poiManager.editPOIGroup(poiPathGroup);
		} else {
                poiPathGroup.addPOIs(poiStart);
        }
		if(poiPathGroup.getPOIs("poiendId")){
				poiPathGroup.removePOIs("poiendId");
                poiManager.editPOIGroup(poiPathGroup);
                poiPathGroup.addPOIs(poiEnd);
                poiManager.editPOIGroup(poiPathGroup);
		} else {
				poiPathGroup.addPOIs(poiEnd);
		}
		var findPathStartPoint = new SuperMap.OSP.Service.SePoint();
		findPathStartPoint.x = start.x
		findPathStartPoint.y = start.y
		var findPathEndPoint = new SuperMap.OSP.Service.SePoint("SuperMap.OSP.Service.SePoint");
		findPathEndPoint.x = end.x;
		findPathEndPoint.y = end.y
		param.startPoint = findPathStartPoint;
		param.endPoint = findPathEndPoint;
		poiManager.refreshPOI();
		start = end = null;
	}else{
		//正常的路径分析方法
		var findPathStartPoint = new SuperMap.OSP.Service.SePoint();
		//var firstPoint = poiSearchGroup.getPOIs(findPath_StartID).position;
		//var lastPoint = poiSearchGroup.getPOIs(findPath_EndID).position;
		var firstPoint = SuperMap.OSP.Core.Utility.metersToLatLon(startPoint);
		if(firstPoint.x != null){
			findPathStartPoint.x = firstPoint.x;
			findPathStartPoint.y = firstPoint.y;
		}else{
			findPathStartPoint.x = firstPoint.lon;
			findPathStartPoint.y = firstPoint.lat;
		}
		
		param.startPoint = findPathStartPoint;
		var findPathEndPoint = new SuperMap.OSP.Service.SePoint("SuperMap.OSP.Service.SePoint");
		var lastPoint = new SuperMap.OSP.Core.Utility.metersToLatLon(endPoint);
		if(lastPoint.x != null){
			findPathEndPoint.x = lastPoint.x;
			findPathEndPoint.y = lastPoint.y;
		}else{
			findPathEndPoint.x = lastPoint.lon;
			findPathEndPoint.y = lastPoint.lat;
		}
		param.endPoint = findPathEndPoint;
		var meter = Math.sqrt(Math.pow(findPathStartPoint.x
                - findPathEndPoint.x, 2)
                + Math.pow(findPathStartPoint.y
                - findPathEndPoint.y, 2))
				* 107771.68756618077251791001851769;
		if(meter < 400){
			jQuery("#div_recommendPanel").css("display","none");
			document.getElementById("findPath_r").innerHTML = "您所查询的当前距离小于400米，建议步行！";
			jQuery("#findPath_result").css("display","block");
			return false;
		}
	}
	param.pPntWay = pPntWay;
	findPathStart = findPathStartPoint;
	findPathEnd = findPathEndPoint;
	//var radioPathMode = jQuery("input[name='findPath_type']:checked").val();
	param.nSearchMode = 1;//最佳路径
	param.coordsysType = 0; //设置坐标系统参数，0为经纬度坐标，1为墨卡托坐标,默认为0
	var transportationAnalyst = new SuperMap.OSP.Service.TransportationAnalyst();

	transportationAnalyst.url = ospServiceUrl;
	//transportationAnalyst.url = "http://apps.supermapcloud.com";
	transportationAnalyst.findPath(param, function(result) {
		jQuery("#findPath_result").css('display', 'block');
		jQuery("#div_searchResultPanel").css('display', 'block');
		jQuery("#findPath_r").css('display', 'block');
		for(var i=mapPopups.length - 1; i>=0; i--){
			mapPopups[i].hide();
		}
		var line = new SuperMap.Geometry.LineString(result.path);
		var lineFeature = new SuperMap.Feature.Vector(line);
		/*lineFeature.style = {
			stroke: true,
			strokeColor: '#0066ff',
			strokeWidth: 6,
			strokeLinecap: 'square',
			strokeDashstyle: 'longdashdot'
		};*/
		featuresLayer.addFeatures(lineFeature);
		pathFeatures.push(lineFeature);
		var pathInfoList = result.pathInfoList;
		findPathResult(result);
		drawLine = result;
	}, function(error) {
		//alert("给定起始点路径分析失败!");//error.information
		jQuery("#div_recommendPanel").css("display","none");
		document.getElementById("findPath_r").innerHTML = "抱歉，没有搜到您标注的起点到终点的路径，建议你重新标注起点终点";
		jQuery("#findPath_result").css("display","block");
		return false;
	});

}
var middleCount = 0;
function middlePoint(type,point){
	if(type=="m"){
		if(spSelected || epSelected){
			var markPos = map.getLonLatFromPixel(new SuperMap.Pixel(point.x,point.y));
			 var scaledContent = new SuperMap.OSP.UI.ScaledContent;
			 scaledContent.content = "images/pin2.png";
			 scaledContent.offset = new SuperMap.OSP.Core.Point2D(0,0);
			 var poi = new SuperMap.OSP.UI.POI("poiMiddle"+middleCount);
			 poi.position = new SuperMap.OSP.Core.Point2D(markPos.lon, markPos.lat);
			 poi.title = "途经点";
			 poi.imageSize = new SuperMap.Size(14,16);
			 poi.scaledContents = scaledContent;
			 poiPathGroup.addPOIs(poi);
			 var lonlatPoint = SuperMap.OSP.Core.Utility.metersToLatLon(markPos);
			 var p = new SuperMap.OSP.Core.Point2D(lonlatPoint.lon,lonlatPoint.lat);
			 pPntWay.push(p);
			 middleCount++;
			 poiManager.refreshPOI();
		}else{
			alert("请先设置起点或者终点");
		}
	}
}
function selectMenuPoint(type, point) {
	// jQuery("#div_searchResultPanel").css("display","block");
	selectPoint(type, point.x,point.y);
}
function selectPoint(type, x,y) {
    //清除已有线路
	if(busPathGroup != null){
		poiManager.removePOIGroup(busPathGroup); 
		busPathGroup = new SuperMap.OSP.UI.POIGroup("buspathGroupId");
		poiManager.addPOIGroup(busPathGroup); 
	}
    featuresLayer.removeAllFeatures();
    pathFeatures = [];
	var markPos;
	markPos = map.getLonLatFromPixel(new SuperMap.Pixel(x, y));
		if(startPoint != null && endPoint != null){
			spSelected = true;
			epSelected = true;
		}
        if (!!markPos) {
            var scaledContent = new SuperMap.OSP.UI.ScaledContent;
            scaledContent.content = "images/start.png";
			scaledContent.offset = new SuperMap.OSP.Core.Point2D(0,0);
            if (type == "start") {
                //document.getElementById("txtPathStart").value = arg.geometry.x + "," + arg.geometry.y;
				startPoint = new SuperMap.LonLat(markPos.lon,markPos.lat);
                var poi = new SuperMap.OSP.UI.POI("poistartId");
                poi.position = new SuperMap.OSP.Core.Point2D(markPos.lon, markPos.lat);
                poi.title = "起点";
				poi.imageSize = new SuperMap.Size(29,33);
                poi.scaledContents = scaledContent;
                if (poiPathGroup.getPOIs("poistartId")) {
                    poiPathGroup.removePOIs("poistartId");
                    poiManager.editPOIGroup(poiPathGroup);
                    poiPathGroup.addPOIs(poi);
                    poiManager.editPOIGroup(poiPathGroup);
				} else {
                    poiPathGroup.addPOIs(poi);
                }
				spSelected = true;
				if(secondPointEnd != ""){
					endPoint.x = secondPointEnd.x;
					endPoint.y = secondPointEnd.y;
					epSelected = true;
					secondPointEnd = "";
					secondPointStart = "";
				}
            } else if (type == "end") {
                scaledContent.content = "images/end.png";
                //document.getElementById("txtPathEnd").value = arg.geometry.x + "," + arg.geometry.y;
				endPoint = new SuperMap.LonLat(markPos.lon,markPos.lat);
                var poi = new SuperMap.OSP.UI.POI("poiendId");
                poi.position = new SuperMap.OSP.Core.Point2D(markPos.lon, markPos.lat);
                poi.title = "终点";
				poi.imageSize = new SuperMap.Size(29,33);
                poi.scaledContents = scaledContent;
                var oldPoi = poiPathGroup.getPOIs("poiendId");
                if (oldPoi) {
                    poiPathGroup.removePOIs("poiendId");
                    poiManager.editPOIGroup(poiPathGroup);//1470259800
                    poiPathGroup.addPOIs(poi);
                    poiManager.editPOIGroup(poiPathGroup);
                } else {
                    poiPathGroup.addPOIs(poi);
                }
				epSelected = true;
				if(secondPointStart != ""){
					startPoint.x = secondPointStart.x;
					startPoint.y = secondPointStart.y;
					spSelected = true;
					secondPointStart = "";
					secondPointEnd = "";
				}
            }
			if(spSelected && epSelected) {
				navigationFindPath();
				jQuery("#div_searchResultPanel").css("display","block");
				if(poiSearchGroup.pois.length != 0){
					//jQuery("#divTime").html("");
					jQuery("#search_result").html("");
					jQuery("#divPage").html("");
					jQuery("#bottomDistrictPosition").css("display","block");
					poiManager.removePOIGroup(poiSearchGroup);
					poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
					poiSearchGroup.caption = "poi搜索分组";
					var scaledContent = new SuperMap.OSP.UI.ScaledContent();
					scaledContent.content = "images/num_map/1.png";
					scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
					poiSearchGroup.scaledContents = scaledContent;
					poiManager.addPOIGroup(poiSearchGroup);
			}
			}
            poiManager.refreshPOI();
            
        }
		
    //});
}



// 创建一个查询点
var createFindPathPoint = function(id, name, x, y){
	var poi = new SuperMap.OSP.UI.POI(id);
	poi.position = new SuperMap.OSP.Core.Point2D((x), parseFloat(y));
    var scaledContent = new SuperMap.OSP.UI.ScaledContent();
    if(id.indexOf('start') != -1){
    	scaledContent.content = "<img src='images/start.png' />";
    }else {
    	scaledContent.content = "<img src='images/end.png' />";
    }
    
    scaledContent.offset = new SuperMap.Size(15, 33);
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


function addFindPathForPOI(id){
	var poiID = 'search_POI_' + id
	var poi = poiSearchGroup.getPOIs(poiID);
	poiSearchGroup.removePOIs(poiID);
	setTab('2', this);

	if(poi){
//		alert(poi.position.x + ' ' + poi.position.y);
		
		var startPOI = poiSearchGroup.getPOIs(findPath_StartID);
		var endPOI = poiSearchGroup.getPOIs(findPath_EndID);
		
		if(startPOI && !endPOI){
			// 作为结束点
//			endPOI = createFindPathPoint(findPath_EndID, (poi.name?poi.name:poi.title), poi.position.x, poi.position.y);
			poi.id = findPath_EndID;
			endPOI = poi;
		} else if(!startPOI){
//			startPOI = createFindPathPoint(findPath_StartID, (poi.name?poi.name:poi.title), poi.position.x, poi.position.y);
			poi.id = findPath_StartID;
			startPOI = poi;
		}
		
		if(startPOI){
			startPOI.id = findPath_StartID;
			poiSearchGroup.addPOIs(startPOI);
			$('#findPath_start').val('');
            $('#findPath_start').val(startPOI.name?startPOI.name:startPOI.title);
		}
		
		if(endPOI){
			endPOI.id = findPath_EndID;
			poiSearchGroup.addPOIs(endPOI);
			$('#findPath_end').val('');
            $('#findPath_end').val(endPOI.name?endPOI.name:endPOI.title);
		}
		
		poiManager.editPOIGroup(poiSearchGroup);
        poiManager.refreshPOI();
	}
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
	            break;
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
	        case 11:
	            turn = '直角斜边左转弯';
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
		var nextIndex = index + 1;
		if(index == 0){
			tab += '<div class="line"></div>';
			tab += '<table class="drive_info">';
			tab += '<tr id="'+index+1+'" onmouseover="mseOverColor(\''+index+1+'\')" onmouseout="mseOutColor(\''+index+1+'\')" id="click"  class="BusProjectItemText"><th class="num"><span>'+(index + 1)+'</span></th>';
			tab += '<td><span style="margin-left:15px">从<b>'+markName+'</b>出发，沿<b><span onmouseover="drawLineRed('+ index +');" onmouseout="deleteRedLine('+ index +');">' + strRouteName + '</span></b>行驶<b>'+length+'</b>，<b>'+rout+'</b>驶入<b><span onmouseover="drawLineRed(' + index + 1 + ');" onmouseout="deleteRedLine(' + index + 1 + ');">' + NextstrRouteName + '</span></b></span></br></td>';
			tab += '</tr>';
		}
		else if(index > 0 && !isEnd){
			tab += '<tr id="'+index+1+'" onmouseover="mseOverColor(\''+index+1+'\')" onmouseout="mseOutColor(\''+index+1+'\')" class="BusProjectItemText"><th class="num"><span>'+(index + 1)+'</span></th>';
			tab += '<td><span style="margin-left:15px">沿<b><span onmouseover="drawLineRed(' + index + ');" onmouseout="deleteRedLine(' + index + ');">' + strRouteName + '</span></b>行驶<b>'+length+'</b>，<b>'+rout+'</b><span></p></b>驶入<b><span onmouseover="drawLineRed(' + nextIndex + ');" onmouseout="deleteRedLine(' + nextIndex + ');">' + NextstrRouteName + '</span></b></span></br></td>';
			tab += '</tr>';
		}
		else if(isEnd){
			tab += '<tr id="'+index+1+'" onmouseover="mseOverColor(\''+index+1+'\')" onmouseout="mseOutColor(\''+index+1+'\')" class="BusProjectItemText"><th class="num"><span>'+(index + 1)+'</span></th>';
			tab += '<td><span style="margin-left:15px">沿<b><span onmouseover="drawLineRed(' + index + ');" onmouseout="deleteRedLine(' + index + ');">' + strRouteName + '</span></b>行驶<b>'+length+'</b>，<span></p></b>到达<b><span onmouseover="drawLineRed(' + nextIndex + ');" onmouseout="deleteRedLine(' + nextIndex + ');">' + markName + '</span></b></span></br></td>';
			tab += '</tr></table>';
			tab += '<div class="line"></div>';
			tab += '<div class="con_title"><img src="images/bus/zhongdian.gif" align="absmiddle" /><span>终点</span></div>';
		}
		/*
		if(index == 0){
			tab += '<div class="line"></div>';
			tab +='<p class="drive_info" id="click"><span class="num">'+(index + 1)+'</span><span style="margin-left:15px">从<b>'+markName+'</b>出发，沿<b><span onmouseover="drawLineRed('+ index +');" onmouseout="deleteRedLine('+ index +');">' + strRouteName + '</span></b>行驶<b>'+length+'</b>，<b>'+rout+'</b>驶入<b><span onmouseover="drawLineRed(' + index + 1 + ');" onmouseout="deleteRedLine(' + index + 1 + ');">' + NextstrRouteName + '</span></b></span></p></br>';
		}else if(index > 0 && !isEnd){
			tab = '<p class="drive_info"><span class="num">'+(index + 1)+'</span><span style="margin-left:15px">沿<b><span onmouseover="drawLineRed(' + index + ');" onmouseout="deleteRedLine(' + index + ');">' + strRouteName + '</span></b>行驶<b>'+length+'</b>，<b>'+rout+'</b><span></p></b>驶入<b><span onmouseover="drawLineRed(' + nextIndex + ');" onmouseout="deleteRedLine(' + nextIndex + ');">' + NextstrRouteName + '</span></b></span></p></br>';
		}else if(isEnd){
			tab = '<p class="drive_info"><span class="num">'+(index + 1)+'</span><span style="margin-left:15px">沿<b><span onmouseover="drawLineRed(' + index + ');" onmouseout="deleteRedLine(' + index + ');">' + strRouteName + '</span></b>行驶<b>'+length+'</b>，<span></p></b>到达<b><span onmouseover="drawLineRed(' + nextIndex + ');" onmouseout="deleteRedLine(' + nextIndex + ');">' + markName + '</span></b></span></p></br>';
			tab += '<div class="line"></div>';
			tab +='<div class="con_title"><img src="images/bus/zhongdian.gif" align="absmiddle" /><span>终点</span></div>';
		}*/
		return tab;
	}
	
	
	// search_result
	//var result = getJSON();
	var path = result.path;
	var rout = result.pathInfoList;
	
	// dLength
	var html = '<div class="con">';
	html += '<div class="con_title"><img src="images/bus/qidian.gif" alt="起点" align="absmiddle" /><span>起点</span></div>';
	var routLength = rout.length;
	if(routLength && routLength > 0){
		html +='<div class="msg">全程：约<span>' + loadLengthFormat(eval(rout[0].dLength)) + '</span></div>';
		if(loadLengthFormat(eval(rout[0].dLength)) < 400){
			html = "当前距离小于400米，建议步行";
		}else{
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
	}
	
	//html += '</table><div class="search_bottom">查看:<a href="#">返程</a>│<a href="#">公交</a></div>';
	html += '</div>';
	
//	$('#search_result').empty();
//	$('#fujianArea_head').append(html);
//	$('#search_result').css("display", 'none');
	jQuery("#div_recommendPanel").css("display","none");
	document.getElementById("findPath_r").innerHTML = html;
	jQuery("#findPath_result").css("display","block");
}
//鼠标经过路径分析结果单元格时改变背景色
function mseOverColor(id){
	jQuery("#"+id).css("background","#EBF0F8");
	// jQuery("#"+id).css("background","#569CF1");
}
//鼠标离开路径分析结果单元格时改变背景色
function mseOutColor(id){
	jQuery("#"+id).css("background","#FFFFFF");
}
var tt = new Array();
//鼠标移上去画红线
var redLineFeature;
function drawLineRed(index){
	var points = new Array();
	var startNum,endNum;
	for(var i =0; i < drawLine.path.length; i++){
		if(index == 0 && drawLine.pathInfoList[index].x != drawLine.path[i].x ){
			points.push(new SuperMap.Geometry.Point(drawLine.path[i].x,drawLine.path[i].y));
		}else if(index !=0){
			var tempIndex = index - 1;
			if(drawLine.pathInfoList[tempIndex].x == drawLine.path[i].x){
				 startNum = i;
			}else if(drawLine.pathInfoList[index].x == drawLine.path[i].x){
				 endNum = i;
				break;
			}
		}else{
			points.push(new SuperMap.Geometry.Point(drawLine.path[i].x,drawLine.path[i].y));
			break;
		}
	}
	if(startNum !=null && endNum != null){
		for(var j = startNum; j < drawLine.path.length; j++){
			if(drawLine.path[endNum].x != drawLine.path[j].x){
				points.push(new SuperMap.Geometry.Point(drawLine.path[j].x,drawLine.path[j].y));
			}else{
				points.push(new SuperMap.Geometry.Point(drawLine.path[j].x,drawLine.path[j].y));
				break;
			}
		}
	}
	var line = new SuperMap.Geometry.LineString(points);
	redLineFeature = new SuperMap.Feature.Vector(line);
	featuresLayer.style = {fillColor: "red",strokeColor: "red",strokeWidth:4 ,pointRadius:6};
	featuresLayer.addFeatures(redLineFeature);
	featuresLayer.style = {fillColor: "#7C9DE4",strokeColor: "#7A9BE2",strokeWidth:5 ,pointRadius:6};
	tt.push(redLineFeature);
}

function deleteRedLine(){	
	featuresLayer.removeFeatures(redLineFeature);	
}
//================================路径分析end================================


//-----------地图基本操作-------------//

//测距
var iClientMeter = 0;
function setMeasure(){			
	featuresLayer.style ={fillColor: "red",strokeColor: "red",pointRadius:6};
	measureLine.activate();
}
//绘制长度
var measureIndex = 0;
var measureHashMap = null;
function drawCompleted(drawGeometryArgs){
	var featureIds = new Array();
	measureLine.deactivate();
	if(measureHashMap == null){
		measureHashMap = new SuperMap.OSP.Core.HashMap();
	}
	iClientMeter = 0;
	var index = ++measureIndex;
	var geometry = drawGeometryArgs.feature;
	featureIds.push(geometry.id);
	var start,end;
	var pois = new Array();
	for(var k = 0; k < geometry.geometry.components[0].components.length; k++){
		var point = new SuperMap.Geometry.Point(geometry.geometry.components[0].components[k].x,geometry.geometry.components[0].components[k].y);
		if(k == 0){
			start = point;
		}else if(k == (geometry.geometry.components[0].components.length -1)){
			end = point;
		}
		var pointFeature = new SuperMap.Feature.Vector(point);
		featureIds.push(pointFeature.id);
		pointFeature.style  = {fillColor: "#fffff",strokeColor: "#FF0000",pointRadius:5,strokeOpacity:0.5,fillOpacity:0.5};
		featuresLayer.addFeatures(pointFeature);
		pois.push(point);
	}
	measureHashMap.put(index,featureIds);
	for(var i = 0; i < pois.length; i++){
		pois[i] = new SuperMap.LonLat(pois[i].x,pois[i].y);
		pois[i] = SuperMap.OSP.Core.Utility.metersToLatLon(pois[i]);
	}
	for(var j = 0; j < pois.length; j++){
		if(j != (pois.length -1)){
			var p = new SuperMap.LonLat(pois[j].lon,pois[j].lat);
			var p1 = new SuperMap.LonLat(pois[j+1].lon,pois[j+1].lat);
			iClientMeter += SuperMap.Util.distVincenty(p,p1);
		}
	}
	for(var z = 0; z < pois.length; z++){
		//将线段的点变成莫卡托
		pois[z] = SuperMap.OSP.Core.Utility.latLonToMeters(pois[z]);
	}

	var poiStart = new SuperMap.OSP.UI.POI("poi_start_id" + index);
	var startContent = new SuperMap.OSP.UI.ScaledContent();
	startContent.content = "images/begin.gif";
	startContent.offset = new SuperMap.OSP.Core.Point2D(10,-10);
	poiStart.scaledContents = startContent;
	poiStart.position = new SuperMap.OSP.Core.Point2D(start.x,start.y);
	poiStart.imageSize = new SuperMap.Size(25,18);
	var poiClose = new SuperMap.OSP.UI.POI("" + index);
	var closeScaled = new SuperMap.OSP.UI.ScaledContent();
    closeScaled.content = "images/close1.gif";
	closeScaled.offset = new SuperMap.OSP.Core.Point2D(-20,-5);
    poiClose.scaledContents = closeScaled;
	poiClose.title = "清除本次测量结果";
	poiClose.imageSize = new SuperMap.Size(12,12);
    poiClose.position = new SuperMap.OSP.Core.Point2D(end.x,end.y);
	poiClose.addEventListerner("click",clearMeasure);
	var poiMeasurement = new SuperMap.OSP.UI.POI("poi_measurement_id" + index);
	poiMeasurement.position = new SuperMap.OSP.Core.Point2D(end.x,end.y);
	poiMeasurement.imageSize = new SuperMap.Size(96,20);
	var scaledContent = new SuperMap.OSP.UI.ScaledContent();
	scaledContent.offset = new SuperMap.OSP.Core.Point2D(15,-15);
	poiMeasurement.scaledContents = scaledContent;
	var distance = iClientMeter;
	var distanceinfo = '';
	if(distance * 100 < 100){
		distanceinfo = "总长：<font color=red>" + parseInt(distance * 100) * 10 + "</font>米";
	}else{
	    distanceinfo = "总长：<font color=red>" + distance.toFixed(2) + "</font>公里";
	}
	  scaledContent.content = '<div style="width:96px;height:17px;padding:2px;border:1px solid #ff0000;text-align:center; background-color:white;font-size:12px">' + distanceinfo + '</div>';
	  poiMeasurement.scaledContents = scaledContent;
	 poiMeasurementGroup.addPOIs([poiStart,poiMeasurement,poiClose]);
     poiManager.editPOIGroup(poiMeasurementGroup);
     poiManager.refreshPOI();
}
//清除量算结果
function clearMeasure(){
	var id = this.id;
	iClientMeter = 0;
	var array = measureHashMap.get(id);
	poiMeasurementGroup.removePOIs(id);
	poiMeasurementGroup.removePOIs("poi_measurement_id"+id);
	poiMeasurementGroup.removePOIs("poi_start_id"+id);
	poiManager.editPOIGroup(poiMeasurementGroup);
    poiManager.refreshPOI();
	var featureArray = new Array();
	for(var i = 0; i < array.length; i++){
		var feature = featuresLayer.getFeatureById(array[i]);
		featureArray.push(feature);
	}
	featuresLayer.removeFeatures(featureArray);
	
	measureHashMap.remove(id);
	measureIndex--;
}

//地图打印
function printMap(){
    var printService = new SuperMap.OSP.Service.PrintService();
    printService.printMap();
}

//截图
function cuteMap(){
    var mapCapturerService = new SuperMap.OSP.Service.MapCapturer();
   
    if(baseCloudLayer){
    	mapCapturerService.url = ospServiceUrl;
    }
    else{
    	mapCapturerService.url = url_screenshot_image;
    }


    //组织截图参数
    var getImageParam = new SuperMap.OSP.Service.GetImageParam();
    getImageParam.viewBounds = map.getExtent();
    getImageParam.mapName = "quanguo";
	var level = map.getZoom();
	var scales = SuperMap.OSP.Core._Scales;
    getImageParam.mapScale = scales[level];
    getImageParam.picWidth = parseInt(document.getElementById(mapDivId).style.width.replace("px", ""));
   
	//alert(getImageParam.picWidth);
    if (!getImageParam.picWidth) {
        getImageParam.picWidth = document.getElementById(mapDivId).offsetWidth;
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

var isCopyButtonInit = false;
//地图共享
var shareMapFindPathStart = "";
var shareMapFindPathEnd = "";
function shareMap() {
   var clientInfo = new SuperMap.OSP.Service.ClientStateInfo();
    clientInfo.viewBounds = map.getExtent();
    var level = map.getZoom();
	var scales = SuperMap.OSP.Core._Scales;
    clientInfo.mapScale = scales[level];
    clientInfo.mapName = getMapName();
	clientInfo.center = map.getCenter();
	clientInfo.level = level;
	var verifyPoiGroup = poiManager.getPOIGroup("poipathGroupId");
	if(verifyPoiGroup.pois.length != 0){
		if(findPathStart != "" && findPathEnd != ""){
			shareMapFindPathStart = new SuperMap.OSP.Core.Point2D(findPathStart.x,findPathStart.y);
			shareMapFindPathend = new SuperMap.OSP.Core.Point2D(findPathEnd.x, findPathEnd.y);
			clientInfo.userDefinedParam.navigationFindPathStart = shareMapFindPathStart;
			clientInfo.userDefinedParam.navigationFindPathEnd = shareMapFindPathend;
		}
	}else{
		if(shareMapKeyword != ""){
			clientInfo.userDefinedParam.shareMapKeyword = shareMapKeyword;
			clientInfo.userDefinedParam.shareMapCode = divisionCode;
			clientInfo.userDefinedParam.shareMapUserLevel = shareMapUserLevel;
			clientInfo.userDefinedParam.shareMapexpectCount = pageSize;
			clientInfo.userDefinedParam.shareMapstartRecord = shareMapRowIndex;
			clientInfo.userDefinedParam.shareMapDatasetName = datasetName;
			clientInfo.userDefinedParam.shareMapDataSourceName = 'china_poi';
		}
		if(nearBySearchKeyword != ""){
			clientInfo.userDefinedParam.shareMapNearBySearchKeyword = nearBySearchKeyword;
			clientInfo.userDefinedParam.shareMapBufferCenter = bufferCenter;
			clientInfo.userDefinedParam.shareMapPointByFindPath = pointByFindPath;
		}
		if(typeof(markerArray)!= "undefined" && markerArray.length != 0){
			clientInfo.userDefinedParam.shareMapMarker = markerArray;
		}
	}


    var shareMapService = new SuperMap.OSP.ExtentionService.ShareMap();
    shareMapService.url = ospServiceUrl;
    shareMapService.saveClientStateInfo(clientInfo, -1,
    function(result) {
        if (result) {
            //返回结果为保存的临时key，用来获取客户端保存的参数
            //debugger
            jQuery("#divShareMap").css("display","block");
            var href = document.URL;
            href = href.replace("##", "");
            href = href.replace("#", "");
		if(href.indexOf("keyId")!= -1){
				href = href.replace("index.html","").substr("0",href.indexOf("?"));
				var shareUrl = href + "?keyId=" + result;
			}else{
				var shareUrl = href.replace("index.html", "") + "?keyId=" + result;
			}
            jQuery("#txtShareMap").val(shareUrl);

            if (!isCopyButtonInit) {
                var copyCon = document.getElementById("txtShareMap").value;
                var flashvars = {
                    content: encodeURIComponent(copyCon),
                    uri: 'images/flash_copy_btn.png'
                };
                var params = {
                    wmode: "transparent",
                    allowScriptAccess: "always"
                };
                swfobject.embedSWF("script/clipboard.swf", "forLoadSwf", "52", "25", "9.0.0", null, flashvars, params);
            }
        }
    },
    function(errorResult) {
        alert(errorResult.information);
    });
}

//-------------地图搜索功能----------------//

//第一级行政区域HASH表
var topDistrictHashMap = null;
var topSelDistrictCode = null;
//当前选择的行政区域Hash表
var secondDistrictHashMap = null;
var secondSelDistrictCode = null;
//行政区域服务对象
var districtSearch = null;
//行政区域展示div的ID
var topDistDivId = "fold1Content";
var bottomDistDivId = "bottomDistrictPosition";
//当前选择的行政区域代码
var selCode = "110000";
//当前选择的行政区域级别
var selLevel = 1;
//加载第一级行政区域
//默认行政区域数据集
var datasetName ='PbeijingP';
function loadTopDistricts(arg){
    //收起分类内容
    foldBySearch(arg);
    selCode = null;
    selLevel = null;
    topDistrictHashMap = new SuperMap.OSP.Core.HashMap();
    districtSearch = new SuperMap.OSP.Service.DistrictSearch();

    document.getElementById(topDistDivId).innerHTML = '<b>中国</b>&nbsp;';
    districtSearch.getTopDistricts(function(records){
        if (records) {
            var bottomHtml = "<ul>";
            for (var i = 0; i < records.length; i++) {
                var disInfo = records[i];
                topDistrictHashMap.put(disInfo.code, disInfo);
                bottomHtml += '<li><a href="javascript:loadSecondDistrict(\'' + disInfo.code + '\', 1)">' + disInfo.name + '</a></li>';
            }
            bottomHtml += "</ul>";
            document.getElementById(bottomDistDivId).innerHTML = bottomHtml;
            loadTopTypes();
        }
    }, function(error){
    
    });
    
    // districtResultContent();
}

//获取第二级行政区域
function loadSecondDistrict(districtCode, arg){
    //收起分类内容
    foldBySearch(arg);
	/**
	 * 加载数据集
	 */
//	if(datasetMap.get(districtCode) != null){
//		datasetName= datasetMap.get(districtCode)
//	}
    var topDisInfo = topDistrictHashMap.get(districtCode);
    topSelDistrictCode = districtCode;
    selCode = districtCode;
    selLevel = 1;
    document.getElementById(topDistDivId).innerHTML = '<a href="javascript:loadTopDistricts(\'1\')">中国</a>&nbsp;&gt;&nbsp;<b>' + topDisInfo.name + '</b>';
    secondDistrictHashMap = new SuperMap.OSP.Core.HashMap();
    districtLocation(districtCode, topDistrictHashMap);
    var disInfos = districtSearch.getChildDistricts(districtCode, function(records){
        if (records) {
            var bottomHtml = "<ul>";
            for (var i = 0; i < records.length; i++) {
                var disInfo = records[i];
                secondDistrictHashMap.put(disInfo.code, disInfo);
                bottomHtml += '<li><a href="javascript:loadThirdDistrict(\'' + disInfo.code + '\', 1)">' + disInfo.name + '</a></li>';
            }
            bottomHtml += "</ul>";
            document.getElementById(bottomDistDivId).innerHTML = bottomHtml;
        }
    }, function(errorResult){
    
    });
    weather(districtCode);
    districtResultContent();
}

//获取第三级行政区域
function loadThirdDistrict(districtCode, arg){
    //收起分类内容
    foldBySearch(arg);
    var secondDisInfo = secondDistrictHashMap.get(districtCode);
    secondSelDistrictCode = districtCode;
    selCode = districtCode;
    selLevel = 2;
    var topDisInfo = topDistrictHashMap.get(topSelDistrictCode);
    document.getElementById(topDistDivId).innerHTML = '<a href="javascript:loadTopDistricts(\'1\')">中国</a>&nbsp;&gt;&nbsp;<a href="javascript:loadSecondDistrict(\'' + topDisInfo.code + '\', 1)">' + topDisInfo.name + '</a>&nbsp;&gt;&nbsp;<b>' + secondDisInfo.name + '<b>';
    document.getElementById(bottomDistDivId).innerHTML = "";
    districtLocation(districtCode, secondDistrictHashMap);
    weather(districtCode);
    districtResultContent();
}

//行政区域搜索时改变搜索结果内容
function districtResultContent(){
    //行政区域搜索时清空上次搜索结果
    document.getElementById(resultDivId).innerHTML = "";
    document.getElementById(pagerId).innerHTML = "";
}

//行政区域定位
function districtLocation(districtCode, hashMap){
    var disInfo = hashMap.get(districtCode);
    //设置不同级别的行政区域地图上显示不同的比例尺
    var level;
	if(disInfo.level == 1 && (disInfo.name == '北京市' || disInfo.name == '天津市' || disInfo.name == '重庆市' || disInfo.name == '上海市')){
		level = 11;
	}
    else if (disInfo.level == 2) {
        level = 10;
    }else if (disInfo.level == 3) {
            level = 12;
    }else{
		level = 7;
	}
    map.setCenter(disInfo.center,level);
}


//----------------------分类搜索----------------------//

//第一级分类信息Hash表
var topTypeHash = null;
var topSelTypeCode = null;
//二级分类信息Hash表
var secondTypeHash = null;
var secondSelTypeCode = null;
//三级分类信息Hash表
var thirdTypeHash = null;
var thirdSelTypeCode = null;
//分类服务对象
var poiTypeSearch = null;
//显示div
var topTypeDivId = "fold2Content";
var bottomTypeDivId = "bottomTypePosition";
//当前选择的poi分类信息
var selTypeCode = null;
var selTypeLevel = null;

//------------------搜索-------------------//
var ospPager = null;//分页控件
var pageSize = 10;//每页显示记录数
var keywordTextId = "ssearch_box_input";	//搜索关键字文本框id
var shareMapKeyword = "";
var shareMapRowIndex = ""; 
var shareMapParam  = "";
var shareMapUserLevel = null;
function searchPOI(result){
	isShow = false;
	if(poiPathGroup.pois.length != 0){
		poiPathGroup.pois = [];
		poiManager.editPOIGroup(poiPathGroup);
		poiManager.refreshPOI();
	}
	jQuery("#search_result").css("display","block");
	var keyword = jQuery("#"+keywordTextId).val();
	for(var i =0; i< provinces.length; i++){
		if(provinces[i][0].indexOf(keyword) != -1 && keyword.length >=2 ){
			if(((provinces[i][0].substr(0,3) == keyword.substr(0,3)) || (provinces[i][0].substr(0,2) == keyword.substr(0,2))) && ((provinces[i][0].length -1) == keyword.length || provinces[i][0].length == keyword.length)){
				map.setCenter(new SuperMap.LonLat(provinces[i][3],provinces[i][4]),7);
				return false;
			}
		}
	}
	
	var length_citys = citys.length;
	for(var j = 0; j < length_citys; j++){
		var city_temp = citys[j];
		if(city_temp.city.indexOf(keyword) != -1 && keyword.length >=2){
			if(((city_temp.city.substr(0,3) == keyword.substr(0,3)) || (city_temp.city.substr(0,2) == keyword.substr(0,2))) && ((city_temp.city.length -1) == keyword.length || city_temp.city.length == keyword.length)){
				if(keyword == "三沙市" || keyword == "三沙"){
					map.setCenter(new SuperMap.LonLat(city_temp.x,city_temp.y),10);
					return false;
				}else if(keyword == "钓鱼岛"){
					dyd = new SuperMap.OSP.Core.Point2D(city_temp.x,city_temp.y);
					break;
				}else{
					map.setCenter(new SuperMap.LonLat(city_temp.x,city_temp.y),11);
					return false;
				}
			}
		}
	}
	/*
	for(var j = 0; j < citys.length; j++){
		if(citys[j][0].indexOf(keyword) != -1 && keyword.length >=2){
			if(((citys[j][0].substr(0,3) == keyword.substr(0,3)) || (citys[j][0].substr(0,2) == keyword.substr(0,2))) && ((citys[j][0].length -1) == keyword.length || citys[j][0].length == keyword.length)){
				if(keyword == "三沙市" || keyword == "三沙"){
					map.setCenter(new SuperMap.LonLat(citys[j][3],citys[j][4]),10);
					return false;
				}else if(keyword == "钓鱼岛"){
					dyd = new SuperMap.OSP.Core.Point2D(citys[j][3],citys[j][4]);
					break;
				}else{
					map.setCenter(new SuperMap.LonLat(citys[j][3],citys[j][4]),11);
					return false;
				}
			}
		}
	}
	*/
	var currentCity = jQuery("#selectCityDiv").text();
	for(var k = 0; k < chinaTown.length; k++){
		if(currentCity == "北京市"&& keyword == "朝阳区"){
			map.setCenter(new SuperMap.LonLat(12972784.2150736,4858679.78444864),13);
			return false;
		}else if(currentCity == "长春市" && keyword =="朝阳区"){
			map.setCenter(new SuperMap.LonLat(13950739.7780938,5414753.3183442),13);
			return false;
		}
		else if(chinaTown[k][0].indexOf(keyword) != -1 && keyword.length >=2 && chinaTown[k][0].length == keyword.length){
			if(keyword != "朝阳区"){
				map.setCenter(new SuperMap.LonLat(chinaTown[k][1],chinaTown[k][2]),13);
				return false;
			}
		}
	}
	//显示搜索结果面板，隐藏推荐面板
	jQuery("#div_recommendTitlePanel").css("display","block");
	jQuery("#div_searchPOITitlePanel").css("display","none");
	jQuery("#div_recommendPanel").css("display","none");
	jQuery("#div_searchResultPanel").css("display","block");
	jQuery("#searchPOIResult").css("display","block");	//隐藏线路结果div
	jQuery("#searchBusResult").css("display","none");	//隐藏线路结果div
	jQuery("#findPath_result").css("display","none");	//隐藏线路结果div
    jQuery("#divPage").css("display", "none");
	
	//map.closeInfoWindow();
	if(poiPathGroup != null && spSelected && epSelected){
		featuresLayer.removeFeatures(pathFeatures);
		poiManager.removePOIGroup(poiPathGroup); 
		poiPathGroup = new SuperMap.OSP.UI.POIGroup("poipathGroupId");
		poiManager.addPOIGroup(poiPathGroup); 
		startPoint = null;
		startPoint = new SuperMap.OSP.Core.Point2D();
		endPoint = null;
		endPoint = new SuperMap.OSP.Core.Point2D();
		spSelected = false;
		epSelected = false;
	}
	if(busPathGroup != null){
		poiManager.removePOIGroup(busPathGroup); 
		busPathGroup = new SuperMap.OSP.UI.POIGroup("buspathGroupId");
		poiManager.addPOIGroup(busPathGroup);
		poiManager.refreshPOI();
	}
	//datasetName = "PbeijingP";
	shareMapParam = result;
	if(!!ospPager){
		ospPager.page = 1;
	}
	searchPOIByPager();
}
function searchPOIByPager(rowIndex){
	//关闭信息窗
	var popCount = mapPopups.length;
	if(mapPopups.length > 0){
		for(var i=0; i<popCount; i++){
			mapPopups[i].hide();
		}
	}
	if(poiSearchGroup.pois && spSelected && epSelected){
		poiSearchGroup.removePOIs(poiSearchGroup.pois);
		poiManager.editPOIGroup(poiSearchGroup);
		poiManager.refreshPOI();
	}

	featuresLayer.removeAllFeatures();
    //折叠面板
    if (!ospPager) {
        ospPager = new OspPager("ospPager", searchPOIByPager);
    }
    if (!rowIndex) {
        rowIndex = 0;//起始记录号
	}else if(rowIndex == '1') {
		rowIndex = 0;
	}else{
		rowIndex = rowIndex * 10 - 10;
	}
    shareMapRowIndex = rowIndex;
    var poiSearchService = new SuperMap.OSP.Service.POISearch();
    var resultObj = document.getElementById(resultDivId);
    
    
    //poi查询参数
    
    var getPOIsParam = new SuperMap.OSP.Service.GetPOIsByGeometryParam();
	var keywordPOIParam = new SuperMap.OSP.Service.GetPOIsParam();
    var geometry = new SuperMap.OSP.Utility.Geometry();
    var viewBounds = map.getExtent();
    geometry.point2Ds = [new SuperMap.OSP.Core.Point2D(viewBounds.left,viewBounds.top),new SuperMap.OSP.Core.Point2D(viewBounds.right,viewBounds.top),new SuperMap.OSP.Core.Point2D(viewBounds.right,viewBounds.bottom), new SuperMap.OSP.Core.Point2D(viewBounds.left,viewBounds.bottom)];
    getPOIsParam.geometry = geometry;
	var keyword = document.getElementById(keywordTextId).value;
	if (keyword == "" && typeof(shareMapParam) == "undefined") {
        var str = "搜索的关键字不能为空!";
        messageAlert(str);
        return false;
    }
	if(typeof(shareMapParam) != "number" && typeof(shareMapParam) != "undefined"){
		document.getElementById(keywordTextId).value = shareMapParam.shareMapKeyword;
		keywordPOIParam.districtCode = shareMapParam.shareMapCode;
		keywordPOIParam.keyword =  shareMapParam.shareMapKeyword;
		keywordPOIParam.districtLevel = shareMapParam.shareMapUserLevel;
		keywordPOIParam.expectCount = shareMapParam.shareMapexpectCount;
		keywordPOIParam.startRecord = shareMapParam.shareMapstartRecord;;
		keywordPOIParam.datasetName = shareMapParam.shareMapDatasetName;
		keywordPOIParam.DataSourceName = 'china_poi';
		getPOIsParam.keyword = shareMapParam.shareMapKeyword;
		getPOIsParam.expectCount = shareMapParam.shareMapexpectCount;
		getPOIsParam.startRecord = shareMapParam.shareMapstartRecord;
		getPOIsParam.datasetName = shareMapParam.shareMapDatasetName;
		getPOIsParam.DataSourceName = 'china_poi';
		poiSearchService.search.url = poiSearchUrl;
	}else{
		shareMapKeyword = keyword;
		keywordPOIParam.keyword = keyword; 
		if(cityName != null){
			var length_citys = citys.length;
			for(var j = 0; j < length_citys; j++){
				var temp_city = citys[j];
				if(cityName == temp_city.city){
					divisionCode = temp_city.admincode;
					parentCode = temp_city.admincode.slice(0, 2) + "0000";
					break;
				}
			}
			if(divisionCode.substr(2,6) == '0000'){
				for(var k = 0; k < provinces.length; k++){
					parentCode = divisionCode;
					break;
				}
			}
		}
		if(divisionCode.substr(2,6) == '0000'){
			keywordPOIParam.districtLevel = 1;
			shareMapUserLevel = 1;
		} 
		else if (divisionCode == '0') {  //全国
			keywordPOIParam.districtLevel = 0;
			shareMapUserLevel = 0;
		} else {
			keywordPOIParam.districtLevel = 2;
			shareMapUserLevel = 2;
		}
		if(divisionCode == '110000' || divisionCode == '0'){
			datasetName = keywordPOIParam.datasetName = 'PbeijingP';
		}else if(divisionCode == '500000'){
			datasetName = keywordPOIParam.datasetName = 'PchongqingP';
		}else if(divisionCode == '120000'){
			datasetName = keywordPOIParam.datasetName = 'PtianjinP';
		}else if(divisionCode == '310000'){
			datasetName = keywordPOIParam.datasetName = 'PshanghaiP';
		}else {
			for(var i = 0; i < provinces.length; i++){
				if(parentCode == provinces[i][1]){
				datasetName = keywordPOIParam.datasetName = provinces[i][6];
					break;
				}
			}
		}
		keywordPOIParam.districtCode = divisionCode;
		keywordPOIParam.expectCount = pageSize;
		keywordPOIParam.startRecord = rowIndex;
		keywordPOIParam.DataSourceName = 'china_poi';
		
		var bounds = map.getExtent();
		keywordPOIParam.max_x = bounds.right;
		keywordPOIParam.max_y = bounds.top;
		keywordPOIParam.min_x = bounds.left;
		keywordPOIParam.min_y = bounds.bottom;
		
		getPOIsParam.keyword = keyword;
		getPOIsParam.expectCount = pageSize;
		getPOIsParam.startRecord = rowIndex;
		poiSearchService.search.url = poiSearchUrl;
		// poiSearchService.search.url = "http://192.168.10.251:8080";
		getPOIsParam.datasetName=datasetName;
		getPOIsParam.DataSourceName = 'china_poi';
	}
	startPoint = null;
	endPoint = null; 
	resultObj.innerHTML = '正在查询……';
	/* 
	var str = "x<'" + keywordPOIParam.max_x + "'";
	str += " and x>'";
	str = str.replace(/%26gt;/g,">");
	str = str.replace(/%26lt;"/g,"<'");
	console.log(str)
	 */
    poiSearchService.getPois(keywordPOIParam, function(result){
		jQuery("#div_searchResultPanel").css("display","block");
		jQuery("#searchPOIResult").css("display","block");
    	jQuery("#div_recommendTitlePanel").css("display", "none");
		jQuery("#searchBusResult").css("display","none");	//隐藏线路结果div
		jQuery("#findPath_result").css("display","none");	//隐藏线路结果div
    	jQuery("#span_searchPOITotalCount").html(result.totalCount);
    	jQuery("#div_searchPOITitlePanel").css("display", "block");
        if (result.totalCount != 0) {
            //openOrCloseDistrictPanel(true);
            //openOrCloseTypesPanel(true);
            //设置分页控件的总页数
            ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
            //显示分页
            ospPager.printHtml("yahoo", pagerId);
            showPoiResult(result.records, result.totalCount);
            jQuery("#divPage").css("display", "block");
        }
		else{
			if(keyword == "钓鱼岛"){
					jQuery("#div_searchResultPanel").css("display","none");
					jQuery("#search_result").html("");
					jQuery("#divPage").html("");
					jQuery("#findPath_r").html("");
					jQuery("#busPath_r").html("");
					jQuery("#div_recommendPanel").css("display","block");
					jQuery("#idea").css("display","block");
					jQuery("#div_recommendTitlePanel").css("display","block");
					jQuery("#div_searchPOITitlePanel").css("display","none");
					map.setCenter(new SuperMap.LonLat(dyd.x,dyd.y),10);
					return false;
				}
			if( confirm("该城市未找到结果，是否在全国范围内查找")){
				/*poiSearchService.getPOIsByGeometry(getPOIsParam,function(result){
					jQuery("#span_searchPOITotalCount").html(result.totalCount);
					if(result.totalCount != 0){
						ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
						ospPager.printHtml("yahoo", pagerId);
						jQuery("#divPage").css("display", "block");
						showPoiResult(result.records, result.totalCount);
					}else{
						resultObj.innerHTML = '未找到相应结果，请您重新输入！';
						jQuery("#divPage").css("display", "none");
					}
				});*/
				keywordPOIParam.districtLevel = 0;
				poiSearchService.getPois(keywordPOIParam,function(result){
					jQuery("#span_searchPOITotalCount").html(result.totalCount);
					if(result.totalCount != 0){
						ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
						ospPager.printHtml("yahoo", pagerId);
						jQuery("#divPage").css("display", "block");
						showPoiResult(result.records, result.totalCount);
					}else{
						resultObj.innerHTML = '未找到相应结果，请您重新输入！';
						jQuery("#divPage").css("display", "none");
					}
				});
			}
			else{
				resultObj.innerHTML = '未找到相应结果，请您重新输入！';
				jQuery("#divPage").css("display", "none");
				if(poiSearchGroup.pois.length != 0){
					poiSearchGroup.removePOIs();
				}
			}
			//var index = Math.floor(Math.random() * 30);
			//datasetName = poiDatasetNames[index];
			//searchPOI();
			//ospPager.pageCount = Math.ceil(result.totalCount / pageSize);
			//datasetName = "PbeijingP";
		}
    }, function(error){
        //alert(error.information)
        //resultObj.innerHTML = error.information;
    });
	
}
/**
 * 重置左边栏
 * @author mwang
 */
function resetLeftbar(){
	mapClearAll();
	jQuery("#div_searchPOITitlePanel").css("display", "none");
	jQuery("#div_recommendTitlePanel").css("display", "block");
}

//---------------类别搜索------------------//
var ospPagerType = null;
function searchPOIByType(rowIndex){
	
	if (!selCode) {
       // messageAlert("请选择要查询的城市");
      //  return false;
    }
    if (!rowIndex) {
        rowIndex = 0; //起始记录号
    }
    if (!ospPagerType) {
        ospPagerType = new OspPager("ospPagerType", searchPOIByType);
    }
    var poiSearchService = new SuperMap.OSP.Service.POISearch();
    var resultObj = document.getElementById(resultDivId);
    var getPOIsByTypeParam = new SuperMap.OSP.Service.GetPOIsByTypeParam();
    getPOIsByTypeParam.districtCode = selCode;
    getPOIsByTypeParam.districtLevel = selLevel;
    getPOIsByTypeParam.typeCode = selTypeCode;
    getPOIsByTypeParam.typeLevel = selTypeLevel;
    getPOIsByTypeParam.startRecord = rowIndex;
    getPOIsByTypeParam.expectCount = pageSize;
    getPOIsByTypeParam.datasetName=datasetName;
    getPOIsByTypeParam.DataSourceName = 'china_poi';
    resultObj.innerHTML = '正在载入……';
    poiSearchService.getPOIsByType(getPOIsByTypeParam, function(result){
        if (result) {
            //openOrCloseDistrictPanel(true);
            //openOrCloseTypesPanel(true);
            //设置分页控件的总页数
            ospPagerType.pageCount = Math.ceil(result.totalCount / pageSize);
            //显示分页
            ospPagerType.printHtml("yahoo", pagerId);
            showPoiResult(result.records);
        }
        
    }, function(error){
        resultObj.innerHTML = error.information;
    });
}
//-----------------周边查询结果展示---------------//
var nearCenterPoi = null;
var nearBySearchHashMap = null;
function nearBySearchResult(records, totalCount){
    var strHtml = "";
    if (records) {
		nearBySearchHashMap = null;
        nearBySearchHashMap = new SuperMap.OSP.Core.HashMap();
        poiSearchGroup.clearPOIs();
		var currentPoi;
		for (var i = 0; i < records.length; i++) {
            var poiInfo = records[i];
			nearBySearchHashMap.put(poiInfo.code, poiInfo);
			var itemId = "poi_info_item_"+ i;
			strHtml += '<div id="'+ itemId +'" onmouseover="poiInfoItemMouseover(\'' + itemId + '\')" onmouseout="poiInfoItemMouseout(\'' + itemId + '\')" class="poi_info_item">';
			//添加图标
			strHtml += '<div style="float:left;"><img src="images/' + (i + 1) + '.gif"></div>';
			//添加内容
			strHtml += '<div style="margin-left:20px; padding-left:4px;"><div style="clear:right;"><div style="float:right; width:50px;"></div></div>';
			//添加信息标题
			if(!!poiInfo.name) {
				strHtml += '<div id="poi_title"><span><strong><a href="javascript:positionPoi(\''  + poiInfo.code + '\',\'img_left_'+i+'\')">' + poiInfo.name + '</a></strong></span></div>';
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
            //poi.position = latToMetersPoi(poiInfo);
			poi.name = poiInfo.name;
			poi.address = poiInfo.address;
			poi.zipCode = poiInfo.zipCode;
			poi.affiliation = poiInfo.affiliation;
			poi.code = poiInfo.code;
			poi.telephone = poiInfo.telephone;
			 poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
			var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			//服务端约定好'|_['代表'<'符号，']_|'代表'>'符号
            scaledContent.content = 'images/num_map/' + (i + 1) + '.png';
            scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
            poi.title = poiInfo.name;
            poi.scaledContents = scaledContent;
            //poi事件
            poi.addEventListerner("click",popup);
            poiSearchGroup.addPOIs(poi);
			if(totalCount == 1){
				currentPoi = poi;
			}
        }
		var poi = new SuperMap.OSP.UI.POI("nearByPoi");
		if(pointByFindPath == null){
			nearCenterPoi = nearBySearchPoint;
			poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(nearBySearchPoint.x), parseFloat(nearBySearchPoint.y));
			poi.title = nearBySearchPoint.name;
		}
		else{
			nearCenterPoi = pointByFindPath;
			for(var i = 0; i < poiSearchGroup.pois.length;i++){
				if(nearCenterPoi.position != null){
					if(nearCenterPoi.position.x == poiSearchGroup.pois[i].position.x){
						poiSearchGroup.removePOIs(poiSearchGroup.pois[i]);
					}
				}else{
					if(nearCenterPoi.x == poiSearchGroup.pois[i].position.x){
						poiSearchGroup.removePOIs(poiSearchGroup.pois[i]);
					}else if(typeof(nearCenterPoi.geometry) != "undefined" && nearCenterPoi.geometry.x == poiSearchGroup.pois[i].position.x){
						poiSearchGroup.removePOIs(poiSearchGroup.pois[i]);
					}
				}
			}
			if(pointByFindPath.position != null){
				poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(pointByFindPath.position.x), parseFloat(pointByFindPath.position.y));
			}else if(pointByFindPath.geometry != null){
				poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(pointByFindPath.geometry.x), parseFloat(pointByFindPath.geometry.y));
			}else{
				poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(pointByFindPath.x), parseFloat(pointByFindPath.y));
			}
			
		    poi.title = pointByFindPath.title;
			poi.imageSize = new SuperMap.Size(15,15);
			poi.addEventListerner("click",popup);
		}
		var scaledContent = new SuperMap.OSP.UI.ScaledContent();
		scaledContent.content = "images/pin2.png";
		scaledContent.offset = new SuperMap.OSP.Core.Point2D(0,0);
		poi.scaledContents = scaledContent;
           
            //poi事件
            // poi.addEventListerner("click", function(e){
                // var osp_poi = eval('(' + e.get_info() + ')');
				// nearCenter(nearCenterPoi);
            // });
			if(pointByFindPath.id != null){
				if(pointByFindPath.id.indexOf("poiMarker") != -1){
					poiSearchGroup.addPOIs(poi);
				}else if(pointByFindPath.id.indexOf("search") != -1){
				    poiSearchGroup.addPOIs(poi);
				}
			}else{
				if(typeof(pointByFindPath.events)!= "undefined"){
					if(pointByFindPath.events.element.id.indexOf("poiMarker") != -1){
						poiSearchGroup.addPOIs(poi);
					}
				}else{
					poiSearchGroup.addPOIs(poi);
				}
			}
			
            
		//strHtml += '<table cellspacing="0" cellpadding="0" border="0" class="tab_color"><tr style="height:5px"><td colspan="2"></td></tr><tr style="height:5px"><td align="center" width="54" valign="top" rowspan="3"><img src="images/1.gif"></td><td width="448" valign="top"><span style="float: right; margin-right: 15px;"></span><strong><a href="javascript:positionPoi(fdadfa,1)">测试POI</a></strong></td></tr><tr style="height:5px;"><td colspan="2"></td></tr></table>';
		//strHtml += '<div style="display:block; margin-bottom:10px; padding-left:6px;"><div style="float:left;"><img src="images/1.gif"></div><div style="margin-left:20px; padding-left:4px;"><div style="clear:right;"><div style="float:right; width:50px;"></div></div><div id="poi_title">1341413</div><div id="poi_address">794643496</div><div id="poi_zipcode">794643496</div><div id="poi_telephone">794643496</div></div></div>';
        document.getElementById(resultDivId).innerHTML = strHtml;
        //更新poi分组并显示,执行查询时，将路径分析的开始点和结束点还原成false。
		//spSelected = false;
		//epSelected = false;
        poiManager.editPOIGroup(poiSearchGroup);
        poiManager.refreshPOI();
		if(totalCount == 1){
			positionPoi(currentPoi,1);
		}
		//放大
		if(pointByFindPath != null && pointByFindPath.position != null){
			if(bufferDistance == 500){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.position.x, pointByFindPath.position.y),16);
			}else if(bufferDistance == 1000){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.position.x, pointByFindPath.position.y),15);
			}else if(bufferDistance == 3000){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.position.x, pointByFindPath.position.y),13);
			}else{
				map.setCenter(new SuperMap.LonLat(pointByFindPath.position.x, pointByFindPath.position.y),12);
			}
		}else if(pointByFindPath != null && pointByFindPath.geometry != null){
			if(bufferDistance == 500){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.geometry.x, pointByFindPath.geometry.y),16);
			}else if(bufferDistance == 1000){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.geometry.x, pointByFindPath.geometry.y),15);
			}else if(bufferDistance == 3000){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.geometry.x, pointByFindPath.geometry.y),13);
			}else{
				map.setCenter(new SuperMap.LonLat(pointByFindPath.geometry.x, pointByFindPath.geometry.y),12);
			}
		}else{
			if(bufferDistance == 500){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.x, pointByFindPath.y),16);
			}else if(bufferDistance == 1000){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.x, pointByFindPath.y),15);
			}else if(bufferDistance == 3000){
				map.setCenter(new SuperMap.LonLat(pointByFindPath.x, pointByFindPath.y),13);
			}else{
				map.setCenter(new SuperMap.LonLat(pointByFindPath.x, pointByFindPath.y),12);
			}
		}
		
    }
    else {
        document.getElementById(resultDivId).innerHTML = "没有找到您要的结果！";
    }
}

function changeImgToSelect(poiId){
	//var poiId = jQuery(arg.get_element().innerHTML).attr("id");
	var num = parseInt(poiId.substr(8,8)) + 1;
	jQuery("#"+poiId).attr("src","images/new_map/" + num + ".gif");
	jQuery("#img_left_" + (num - 1)).attr("src","images/left_num/" + num +".gif");
	jQuery("#poi_info_item_" + (num - 1)).css("background","#EBF0F8");

}
function changeImgToDefualt(poiId){
	//var poiId = jQuery(arg.get_element().innerHTML).attr("id");
	var num = parseInt(poiId.substr(8,8)) + 1;
	jQuery("#"+poiId).attr("src","images/num_map/" + num + ".png");
	jQuery("#img_left_" + (num - 1)).attr("src","images/" + num +".gif");
	jQuery("#poi_info_item_" + (num - 1)).css("background","");
}
//-----------------搜索结果---------------//
var poiHashMap = null;
function showPoiResult(records, totalCount){
	var strHtml = "";
    if (records) {
        //装载查询到的POI对象
        poiHashMap = null;
        poiHashMap = new SuperMap.OSP.Core.HashMap();
        poiSearchGroup.clearPOIs();
		for (var i = 0; i < records.length; i++) {
            var poiInfo = records[i];
			
			var itemId = "poi_info_item_"+ i;
			strHtml += '<div id="'+ itemId +'" onmouseover="poiInfoItemMouseover(\'' + itemId + '\',\'search_POI_' + i + '_innerImage\',\'img_left_' + i + '\',' + i + ')" onmouseout="poiInfoItemMouseout(\'' + itemId + '\',\'search_POI_' + i + '_innerImage\',\'img_left_' + i + '\',' + i + ')" class="poi_info_item">';
			//添加图标
			strHtml += '<div style="float:left;"><img id="img_left_' + i + '" src="images/' + (i + 1) + '.gif"></div>';
			//添加内容
			strHtml += '<div style="margin-left:20px; padding-left:4px;"><div style="clear:right;"><div style="float:right; width:50px;"></div></div>';
			//添加信息标题
			if(!!poiInfo.name) {
				strHtml += '<div id="poi_title"><span><strong><a href="javascript:positionPoi(\''  + poiInfo.code + '\')">' + poiInfo.name + '</a></strong></span></div>';
				//<strong><a href="javascript:positionPoi(\'' + poiInfo.code + '\',1)">' + poiInfo.name + '</a></strong>
			}
			//添加地址信息
			if(!!poiInfo.address) {
				strHtml += '<div id="poi_address"><span>地址：' + poiInfo.address + '</span></div>';
			}
			//添加邮编信息
			if(!!poiInfo.zipCode) {
				strHtml += '<div id="poi_zipcode"><span>邮编：' + poiInfo.zipCode + '</span></div>';
			}
			//添加电话信息
			if(!!poiInfo.telephone) {
				strHtml += '<div id="poi_telephone"><span>联系电话：' + poiInfo.telephone + '</span></div>';
			}
			if(!!poiInfo.affiliation){
				if(poiInfo.affiliation.substr(0,3) == poiInfo.affiliation.substr(5,3)){
					strHtml += '<div id="poi_aff" style="color:#666666;float:top;">隶属于：' + poiInfo.affiliation.substr(5) + '</div>';
				}else{
					strHtml += '<div id="poi_aff" style="color:#666666;float:top;">隶属于：' + poiInfo.affiliation.substr(5) + '</div>';
				}
			}
			strHtml += '</div></div>';
            //组织poi对象
            var poi = new SuperMap.OSP.UI.POI("search_POI_" + i);
            //poi.position = latToMetersPoi(poiInfo);
			poi.name = poiInfo.name;
			poi.address = poiInfo.address;
			poi.zipCode = poiInfo.zipCode;
			poi.affiliation = poiInfo.affiliation;
			poi.code = poiInfo.code;
			poi.telephone = poiInfo.telephone;
			poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
            var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			//服务端约定好'|_['代表'<'符号，']_|'代表'>'符号
            scaledContent.content = 'images/num_map/' + (i + 1) + '.png';
            scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
            poi.title = poiInfo.name;
            poi.scaledContents = scaledContent;
			poi.addEventListerner("click",popup);
			poi.addEventListerner("mouseover",changeColor);
			poi.addEventListerner("mouseout",changeColor);
			
            poiHashMap.put(poiInfo.code, poiInfo);
			
            poiSearchGroup.addPOIs(poi);
         }
         document.getElementById(resultDivId).innerHTML = strHtml;
         poiManager.editPOIGroup(poiSearchGroup);
         poiManager.refreshPOI();
		 if(totalCount == 1){
			 positionPoi(poi.code,1);
		 }
    }
    else {
        document.getElementById(resultDivId).innerHTML = "没有找到您要的结果！";
    }
}
//当鼠标移到POI或者点击POI时，将图片的颜色改变
function changeColor(e){
	if(e.type == "mouseover"){
		var poi = this;
		var id = poi.id.substr(11,11);
		var num = parseInt(id);
		jQuery("#"+poi.id+"_innerImage").attr("src","images/new_map/" + ( num + 1 ) + ".gif");
		jQuery("#img_left_"+id).attr("src","images/left_num/" + ( num + 1 ) + ".gif");
		jQuery("#poi_info_item_"+id).css("background","#EBF0F8");
	}else if(jQuery("#infowindow").css("display") == "block" && currentInfowindowId){
		var id = currentInfowindowId.substr(11,11);
		var num = parseInt(id);
		for(var i = 0; i < 10; i++){
			if(i != num){
				jQuery("#search_POI_"+i+"_innerImage").attr("src","images/num_map/" + ( i + 1 ) + ".png");
				jQuery("#img_left_"+i).attr("src","images/" + ( i + 1 ) + ".gif");
			}
		}
	}else{
		var poi = this;
		var id = poi.id.substr(11,11);
		var num = parseInt(id);
		jQuery("#"+poi.id+"_innerImage").attr("src","images/num_map/" + ( num + 1 ) + ".png");
		jQuery("#img_left_"+id).attr("src","images/" + ( num + 1 ) + ".gif");
		jQuery("#poi_info_item_" + id).css("background","");
	}
}

//------实现定位------//
function positionPoi(code,type){
	// var index = parseInt(id.substr(9,id.length));
	// var currentPoi = poiSearchGroup.getPOIs("search_POI_"+index);
	// currentPoi.scaledContents.content = '<img id="' + id.replace(/left/,"poi") + '" src="images/new_map/' + (index + 1) + '.gif" />';
	// var selectedDom = jQuery("img[id^='img_left_'][isselect=1]");
	// if(selectedDom.length>0){
		// selectedDom.removeAttr("isselect");
		// var poiId = selectedDom.attr("id").replace(/left/,"poi");
		// changeImgToDefualt(poiId);
		// var oldIndex = parseInt(poiId.substr(8,poiId.length));
		// var currentPoi = poiSearchGroup.getPOIs("search_POI_"+oldIndex);
		// currentPoi.scaledContents.content = '<img id="' + poiId + '" src="images/num_map/' + (oldIndex + 1) + '.png" />';
	// }
	// 选中当前点击的marker
	// var selectId = parseInt(jQuery(e.get_element().innerHTML).attr("id").substr(8,8));
	// jQuery("#"+id).attr("isselect",1);
	if(poiHashMap != null){
		var poiInfo = poiHashMap.get(code);
		if(poiInfo == null){
			if(nearBySearchHashMap != null){
				poiInfo = nearBySearchHashMap.get(code);
			}
		}
	}else{
		var poiInfo = nearBySearchHashMap.get(code);
		if(poiInfo == null){
			poiInfo = code;
		}
	}
	if(poiInfo.x == null){
		var x = poiInfo.position.x;
		var y = poiInfo.position.y;
	}else{
		var x = poiInfo.x;
		var y = poiInfo.y;
	}
    
    var point2d = new SuperMap.LonLat(x, y);
    //左侧列表单击定位
    //map.zoomToLevel(15, point2d);
	map.panTo(point2d);
	popup(poiInfo.code,type);
}

//poi弹出框
var pointByFindPath = null;//以POI点作为路径分析的起点或者终点
var infowindow;
var currentInfowindowId;
function popup(code, type) {
	var poiInfo;
	if(typeof(code) == "string"){
		poiInfo = null;
	}
	else{
		poiInfo = this;
	}
	if(poiInfo == null && poiHashMap != null && typeof(code) === "string"){
		poiInfo = poiHashMap.get(code);
	}
	if(poiInfo == null && typeof(nearBySearchHashMap) !== "undefined" && nearBySearchHashMap != null && typeof(code) == "string"){
		poiInfo = nearBySearchHashMap.get(code);
	}
	currentInfowindowId = poiInfo.id;
	pointByFindPath = poiInfo;
	// var center = new SuperMap.OSP.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
	 var infowinHtml = "";
	// var offset = { x: 60, y: -35 };
	// var isBig = false;
        
	infowinHtml = '<div style="width:auto;height:auto;">';
	
	var size = new SuperMap.Size(293,158);
	// if (jQuery.browser.msie) {
		// size.w = 293;
	// }
	//信息窗标题
	infowinHtml += '<div class="infowindowTitle">';
	if(typeof(poiInfo.title) == "undefined"){
		infowinHtml += '<span class="infowindowTitleTxt">'+poiInfo.name+'</span>';
	}else{
		infowinHtml += '<span class="infowindowTitleTxt">'+poiInfo.title+'</span>';
	}
	infowinHtml += '</div>'
		
	//详细信息
	infowinHtml += '<div class="infowindowContent">';
	infowinHtml += '<table class="infowindowContentTable">';
	if( poiInfo.address != null && poiInfo.address.length != 0 ){
		infowinHtml += ' <tr><td><strong>地址：</strong>' + (poiInfo.address) + '</td></tr>';
		size.h += 30;
	}
	if( poiInfo.zipCode != null && poiInfo.zipCode.length != 0 ){
		infowinHtml += ' <tr><td><strong>邮编：</strong>' + (poiInfo.zipCode) + '</td></tr>';
		size.h += 30;
	}
	if( poiInfo.telephone != null && poiInfo.telephone.length != 0 ){
		infowinHtml += '<tr><td><strong>传真：</strong>' + (poiInfo.telephone) + '</td></tr>';
		size.h += 30;
	}
	/* size.w = (!jQuery.browser.msie) ? 293 : 301; */
	// alert(size.h)
	infowinHtml += '</table>';
	infowinHtml += '</div>';
		
	//操作
	infowinHtml += '<div class="infowindowSearch">';
	
	infowinHtml += '<ul class="infowindowSearchTitle">';			
	infowinHtml += '<li id="li_infowindow_1" class="third blueA hover" style=" border-left: 0 none;" onclick="switchInfoWinTab(\''+1+'\', \''+ poiInfo.id +'\')">';
	infowinHtml += '<img src="images/infowindow/nearsearch.png" /><span>周边查询</span>';
	infowinHtml += '</li>';
	infowinHtml += '<li id="li_infowindow_2" class="second blueA" onclick="switchInfoWinTab(\''+2+'\', \''+ poiInfo.id +'\')">';
	infowinHtml += '<img src="images/infowindow/start.png" /><span>从这里出发</span>';
	infowinHtml += '</li>';
	infowinHtml += '<li id="li_infowindow_3" class="second blueA" onclick="switchInfoWinTab(\''+3+'\', \''+ poiInfo.id +'\')">';
	infowinHtml += '<img src="images/infowindow/end.png" /><span>到这里去</span>';
	infowinHtml += '</li>';
	infowinHtml += '</ul>';	
		
	//周边查询
	infowinHtml += '<div id="tab_infowindow_1" class="infowindowSearchContent" style="padding-top: 10px;">';	
	infowinHtml += '<ul>半径：'
	infowinHtml += '<li><a style="text-decoration:none;" href="javascript:changeMeters(' + 500 + ', 0)">500米</a></li>';
	infowinHtml += '<li><a style="text-decoration:none; font-weight:bold;" href="javascript:changeMeters(' + 1000 + ', 1)">1000米</a></li>';
	infowinHtml += '<li><a style="text-decoration:none" href="javascript:changeMeters(' + 3000 + ', 2)">3000米</a></li>';
	infowinHtml += '<li><a style="text-decoration:none" href="javascript:changeMeters(' + 5000 + ', 3)">5000米</a></li>';
	infowinHtml += '</ul>';
	infowinHtml += '</br>';
	infowinHtml += '</br>';
	infowinHtml += '<ul>';
	infowinHtml += '<li><a style="text-decoration:none" href="javascript:nearBySearchParam(\'银行\')">银行</a></li>';
	infowinHtml += '<li><a style="text-decoration:none" href="javascript:nearBySearchParam(\'饭店\')">饭店</a></li>';
	infowinHtml += '<li><a style="text-decoration:none" href="javascript:nearBySearchParam(\'公园\')">公园</a></li>';
	infowinHtml += '<li style="margin-left:5px;">其他:&nbsp;<input style="padding-top:5px;" id="txt" type="text" size="8"/></li>';
	infowinHtml += '<li style="margin-left:5px;padding-top:5px;"><input type="button" class="infowindowSearchButton" value="确定" onclick="nearBySearchParam()"/></li>';
	infowinHtml += '</ul>'
	infowinHtml += '</div>';
	
	//从这里出发
	infowinHtml += '<div id="tab_infowindow_2" class="infowindowSearchContent" style="padding-top: 17px;display:none">';	
	infowinHtml += '<ul>'
	infowinHtml += '<li>';
	if(typeof(poiInfo.position) == "undefined"){
		infowinHtml += '<span>终点:</span>&nbsp;<input id="txtToHere'+ poiInfo.id +'" type="text" class="infowindowSearchInput"></input>&nbsp;';
		infowinHtml += '<input type="button" class="infowindowSearchButton" onclick="setPathPoint(\'start\','+poiInfo.x+','+poiInfo.y+', \'txtToHere'+ poiInfo.id +'\')" value="确定"></input>'
	}else{
		infowinHtml += '<span>终点:</span>&nbsp;<input id="txtToHere'+ poiInfo.id +'" type="text" class="infowindowSearchInput"></input>&nbsp;<input type="button" class="infowindowSearchButton" onclick="setPathPoint(\'start\','+poiInfo.position.x+','+poiInfo.position.y+', \'txtToHere'+ poiInfo.id +'\')" value="确定"></input>';
	}
	infowinHtml += '</li>';
	infowinHtml += '</ul>'
	infowinHtml += '</div>';
		
	//到这里去
	infowinHtml += '<div id="tab_infowindow_3" class="infowindowSearchContent" style="padding-top: 17px; display:none;">';	
	infowinHtml += '<ul>'
	infowinHtml += '<li>';
	if(typeof(poiInfo.position) == "undefined"){
		infowinHtml += '<span>起点:</span>&nbsp;<input id="txtFromHere'+ poiInfo.id +'" type="text" class="infowindowSearchInput"></input>&nbsp;<input type="button" class="infowindowSearchButton" onclick="setPathPoint( \'end\','+poiInfo.x+','+poiInfo.y+', \'txtFromHere'+ poiInfo.id +'\')" value="确定"></input>';
	}else{
		infowinHtml += '<span>起点:</span>&nbsp;<input id="txtFromHere'+ poiInfo.id +'" type="text" class="infowindowSearchInput"></input>&nbsp;<input type="button" class="infowindowSearchButton" onclick="setPathPoint( \'end\','+poiInfo.position.x+','+poiInfo.position.y+', \'txtFromHere'+ poiInfo.id +'\')" value="确定"></input>';
	}
	infowinHtml += '</li>';
	infowinHtml += '</ul>'
	infowinHtml += '</div>';
		
	infowinHtml += '</div>';
	
	infowinHtml += '</div>';
	
	centerTitle = poiInfo.name;
	if(type != 1){
		if(typeof(poiInfo.position) == "undefined"){
			bufferCenter = new SuperMap.LonLat(poiInfo.x,poiInfo.y)
		}else{
			bufferCenter = new SuperMap.LonLat(poiInfo.position.x,poiInfo.position.y)
		}
	}
	
	var temp_posiotion = null;
	if(typeof(poiInfo.position) == "undefined"){
		temp_posiotion = new SuperMap.LonLat(poiInfo.x,poiInfo.y)
	}else{
		temp_posiotion = new SuperMap.LonLat(poiInfo.position.x,poiInfo.position.y)
	}
	
	if(infowindow == null){
		infowindow = new SuperMap.OSP.Core.InfoWindow("infowindow",
		temp_posiotion,
		size,
		infowinHtml,
		null,
		true,
		closeWindow);
		infowindow.autoSize = true;
		infowindow.panMapIfOutOfView = true;
		map.addPopup(infowindow);
		mapPopups.push(infowindow);			
		infowindow.updateSize();
		infowindow.show();
	}else{
			infowindow.lonlat = temp_posiotion;
			infowindow.setContentHTML(infowinHtml);
			infowindow.setSize(size);
			infowindow.updateSize();
			infowindow.updatePosition();
			infowindow.show();
	} 
	
    var popEle = jQuery('.sm_infoWindowContainer');
    popEle.css('background-color', 'white');
    jQuery('.sm_infoWindowContent').css('border', '0px');
    jQuery('.sm_infoWindowClose').css('background-image', 'url("images/popup_close.png")');
}
//关闭窗口时将图片颜色初始化
function closeWindow(){
	infowindow.hide();
	if(typeof(currentInfowindowId) != "undefined"){
		var id = currentInfowindowId.substr(11,11);
		var num = parseInt(id);
		jQuery("#search_POI_"+id+"_innerImage").attr("src","images/num_map/" + ( num + 1 ) + ".png");
		jQuery("#img_left_"+id).attr("src","images/" + ( num + 1 ) + ".gif");
		currentInfowindowId = null;
	}
}
//点击poi放大时，把该坐标放大到17级
function poiZoomIn(x,y){
	jQuery("#fromHere").css("display","block");
	jQuery("#toHere").css("display","none");
	var point = new SuperMap.LonLat(parseFloat(x), parseFloat(y));
	map.setCenter(point,16);
}
//缓冲区半径
var bufferDistance = 1000;
function changeMeters(meter, i, id){
	jQuery("#tab_infowindow_1 ul a").css("font-weight", "normal");
	jQuery("#tab_infowindow_1 ul a").eq(i).css("font-weight", "bold");
	jQuery("#markNear"+id+" ul a").css("border", "none");
	jQuery("#markNear"+id+" ul a").eq(i).css("border", "1px solid");
	bufferDistance = meter;
}

/**
 *信息窗的内容切换
 *@param id1, id2
 */
function switchInfoWinTab(index, id){
	switch(index){
		//周边查询
		case '1':
			jQuery("#tab_infowindow_1").css("display", "block");
			jQuery("#li_infowindow_1").attr("class", "third blueA hover");

			jQuery("#tab_infowindow_2").css("display", "none");	
			jQuery("#li_infowindow_2").attr("class", "second blueA");
			jQuery("#tab_infowindow_3").css("display", "none");	
			jQuery("#li_infowindow_3").attr("class", "second blueA");
			break;
		//从这里出发
		case '2':
			jQuery("#tab_infowindow_2").css("display", "block");
			jQuery("#li_infowindow_2").attr("class", "second blueA hover");	

			jQuery("#tab_infowindow_1").css("display", "none");		
			jQuery("#li_infowindow_1").attr("class", "third blueA");
			jQuery("#tab_infowindow_3").css("display", "none");		
			jQuery("#li_infowindow_3").attr("class", "third blueA");
			
			jQuery("#txtToHere" + id).onchange(function(){
				var e = typeof(event) == "undefined" ? null:event;
				autoCompleteBySearch(e, 'txtToHere' + id);
			});
			break;
		//到这里去	
		case '3':
			jQuery("#tab_infowindow_3").css("display", "block");
			jQuery("#li_infowindow_3").attr("class", "second blueA hover");	

			jQuery("#tab_infowindow_1").css("display", "none");		
			jQuery("#li_infowindow_1").attr("class", "third blueA");
			jQuery("#tab_infowindow_2").css("display", "none");		
			jQuery("#li_infowindow_2").attr("class", "third blueA");
			
			jQuery("#txtFromHere" + id).onchange(function(){
				var e = typeof(event) == "undefined" ? null:event;
				autoCompleteBySearch(e, "txtFromHere" + id);
			});
			break;
	}
}

//信息窗点击自驾导航设置起始点
function setPathPoint(type, poi_x, poi_y, id){
	dragFeature.deactivate();
	
	var txt = jQuery("#" + id).val();
	if( txt == "" || trim(txt).length == 0){
		return false;
	}
	
	if(type == "start" && (poi_y == "" || typeof(poi_y) == "undefined")){
		var start = poiHashMap.get(poi_x);
	}
	if(type == "end" && (poi_y == "" || typeof(poi_y) == "undefined")){
		var end = poiHashMap.get(poi_x);
	}
	var scaledContentStart = new SuperMap.OSP.UI.ScaledContent;
	scaledContentStart.content = "images/start.png";
	scaledContentStart.offset = new SuperMap.OSP.Core.Point2D(0, 0);
	
	var poiStart = new SuperMap.OSP.UI.POI("poistartId");
	poiStart.title = "起点";
	poiStart.code = "poistartId";
	poiStart.scaledContents = scaledContentStart;
	poiStart.imageSize = new SuperMap.Size(29, 33);
	
	
	var poiEnd = new SuperMap.OSP.UI.POI("poiendId");
	var scaledContentEnd = new SuperMap.OSP.UI.ScaledContent;
	scaledContentEnd.content = "images/end.png";
	scaledContentEnd.offset = new SuperMap.OSP.Core.Point2D(0, 0);
	poiEnd.code = "poiendId";
	poiEnd.title = "终点";
	poiEnd.scaledContents = scaledContentEnd;
	poiEnd.imageSize = new SuperMap.Size(29, 33);
	
	if(type == "start"){
		if(start != null){
			startPoint = new SuperMap.LonLat(start.geometry.x, start.geometry.y);
		}
		else{
			startPoint = new SuperMap.LonLat(poi_x, poi_y);
		}
		var sp = new SuperMap.OSP.Core.Point2D(startPoint.lon,startPoint.lat);
		
		poiStart.position = sp;
		
		if(pathPOIHashMap != null && pathPOIHashMap.get(txt) !== null){
			var coord = new SuperMap.OSP.Core.Point2D(pathPOIHashMap.get(txt).x,pathPOIHashMap.get(txt).y);
			endPoint = coord;

			poiEnd.position = endPoint;
			if(poiPathGroup.pois.length != 0){
				poiPathGroup.clearPOIs();
				poiManager.removePOIGroup(poiPathGroup);
				poiManager.addPOIGroup(poiPathGroup);
			}
			if(poiSearchGroup.pois.length != 0){
				poiSearchGroup.clearPOIs();
				poiManager.removePOIGroup(poiSearchGroup);
				poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
				poiSearchGroup.caption = "poi搜索分组";
				var scaledContent = new SuperMap.OSP.UI.ScaledContent();
				scaledContent.content = '<img src="images/num_map/1.png" />';
				scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
				poiSearchGroup.scaledContents = scaledContent;
				poiManager.addPOIGroup(poiSearchGroup);
				poiManager.editPOIGroup(poiSearchGroup);
				poiManager.refreshPOI();
			}
			poiPathGroup.addPOIs(poiStart);
			poiPathGroup.addPOIs(poiEnd);
			poiManager.editPOIGroup(poiPathGroup);
			poiManager.refreshPOI();
			
			if(infowindow){
				infowindow.hide();
			}else{
				if(start != null){
					deleteMarker(poi_x);
				}
			}
			navigationFindPath();
			
			if(pathPOIHashMap != null || poiMarkerGroup != null){
				pathPOIHashMap.clear();
				poiMarkerGroup.pois = [];
				poiManager.editPOIGroup(poiMarkerGroup);
				poiManager.refreshPOI();
			}
			if(addressAutoComplete != null){
				addressAutoComplete.popup.style.display = "none";
			}
		}
		else{
			poiPathGroup.addPOIs(poiStart);
			var result = pathPOIHashMap.values();
			var length = pathPOIHashMap.size;
			// map.closeInfoWindow();
			if(addressAutoComplete && addressAutoComplete.popup){
				addressAutoComplete.popup.style.display = "none";
			}
			showPathPoiResult(result, length, "end", id);
		}
	}
	if(type == "end"){
		if(end != null){
			endPoint = new SuperMap.OSP.Core.Point2D(end.geometry.x, end.geometry.y);
		}else{
			endPoint = new SuperMap.OSP.Core.Point2D(poi_x, poi_y);
		}
		
		if(pathPOIHashMap && pathPOIHashMap.get(txt)){
			var coord = new SuperMap.OSP.Core.Point2D(pathPOIHashMap.get(txt).x, pathPOIHashMap.get(txt).y);
			startPoint = coord;
			poiStart.position = startPoint;
			
			poiEnd.position = endPoint;
			if(poiPathGroup.pois.length != 0){
				poiManager.removePOIGroup(poiPathGroup);
				poiManager.addPOIGroup(poiPathGroup);
			}
			if(poiSearchGroup.pois.length != 0){
				poiManager.removePOIGroup(poiSearchGroup);
				poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
				poiSearchGroup.caption = "poi搜索分组";
				var scaledContent = new SuperMap.OSP.UI.ScaledContent();
				scaledContent.content = '<img src="images/num_map/1.png" />';
				scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
				poiSearchGroup.scaledContents = scaledContent;
				poiManager.addPOIGroup(poiSearchGroup);
				poiManager.editPOIGroup(poiSearchGroup);
				poiManager.refreshPOI();
			}
			poiPathGroup.addPOIs(poiStart);
			poiPathGroup.addPOIs(poiEnd);
			poiManager.editPOIGroup(poiPathGroup);
			poiManager.refreshPOI();
			if(infowindow){
				infowindow.hide();
			}else{
				if(end != null){
					deleteMarker(poi_x);
				}
			}
			navigationFindPath();
			
			if(pathPOIHashMap != null || poiMarkerGroup != null){
				pathPOIHashMap.clear();
				poiMarkerGroup.pois = [];
				poiManager.editPOIGroup(poiMarkerGroup);
				poiManager.refreshPOI();
			}
			if(addressAutoComplete != null){
				addressAutoComplete.popup.style.display = "none";
			}
		}
		else{
			poiEnd.position = endPoint;
			poiPathGroup.addPOIs(poiEnd);
			var result = pathPOIHashMap.values();
			var length = pathPOIHashMap.size;
			// map.closeInfoWindow();
			if(addressAutoComplete){
				addressAutoComplete.popup.style.display = "none";
			}
			showPathPoiResult(result, length, "start", id);
		}
	}
}
//信息窗设置了起点、终点中的一个之后，信息窗中选择设置另外一个点
function setPoiPath(x, y, type){
	if(type == "start"){
		if (poiPathGroup.getPOIs("poistartId")) {
                poiPathGroup.removePOIs("poistartId");
                poiManager.editPOIGroup(poiPathGroup);
				poiManager.refreshPOI();
                // poiPathGroup.addPOIs(poiStart);
		} 
		var scaledContentStart = new SuperMap.OSP.UI.ScaledContent();
		scaledContentStart.content = "images/start.png";
		scaledContentStart.offset = new SuperMap.OSP.Core.Point2D(0, 0);
		var poiStart = new SuperMap.OSP.UI.POI("poistartId");
		poiStart.title = "起点";
		poiStart.scaledContents = scaledContentStart;
		poiStart.code = "poistartId";
		
		startPoint = new SuperMap.OSP.Core.Point2D(parseFloat(x), parseFloat(y));
		poiStart.position = startPoint;
		
        poiPathGroup.addPOIs(poiStart);
		poiManager.editPOIGroup(poiPathGroup);
		poiManager.refreshPOI();
		
		navigationFindPath();
		if(poiSearchGroup.pois.length != 0){
			poiManager.removePOIGroup(poiSearchGroup);
			poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
			poiSearchGroup.caption = "poi搜索分组";
			var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			scaledContent.content = 'images/num_map/1.png';
			scaledContent.offset = new SuperMap.Size(0, 0);
			poiSearchGroup.scaledContents = scaledContent;
			poiManager.addPOIGroup(poiSearchGroup);
			poiManager.editPOIGroup(poiSearchGroup);
			poiManager.refreshPOI();
		}
		if(pathPOIHashMap != null){
			pathPOIHashMap.clear();
		}
		if(addressAutoComplete != null){
			addressAutoComplete.popup.style.display = "none";
		}
	}
	if(type == "end"){
		if(poiPathGroup.getPOIs("poiendId")){
			poiPathGroup.removePOIs("poiendId");
			poiManager.editPOIGroup(poiPathGroup);
			// poiPathGroup.addPOIs(poiEnd);
		} 
		var scaledContentEnd = new SuperMap.OSP.UI.ScaledContent();
		scaledContentEnd.content = "images/end.png";
		scaledContentEnd.offset = new SuperMap.OSP.Core.Point2D(0, 0);
		var poiEnd = new SuperMap.OSP.UI.POI("poiendId");
		poiEnd.title = "终点";
		poiEnd.scaledContents = scaledContentEnd;
		poiEnd.imageSize = new SuperMap.Size(29, 33);
		endPoint = new SuperMap.OSP.Core.Point2D(parseFloat(x), parseFloat(y));
		poiEnd.position = new SuperMap.OSP.Core.Point2D(parseFloat(x), parseFloat(y));
		
		poiPathGroup.addPOIs(poiEnd);
		
		poiManager.editPOIGroup(poiPathGroup);
		poiManager.refreshPOI();
		
		navigationFindPath();
		if(poiSearchGroup.pois.length != 0){
			poiManager.removePOIGroup(poiSearchGroup);
			poiSearchGroup = new SuperMap.OSP.UI.POIGroup("poi_searchGroupId");
			poiSearchGroup.caption = "poi搜索分组";
			var scaledContent = new SuperMap.OSP.UI.ScaledContent();
			scaledContent.content = 'images/num_map/1.png';
			scaledContent.offset = new SuperMap.Size(0, 0);
			poiSearchGroup.scaledContents = scaledContent;
			poiManager.addPOIGroup(poiSearchGroup);
			poiManager.editPOIGroup(poiSearchGroup);
			poiManager.refreshPOI();
		}
		if(pathPOIHashMap != null){
			pathPOIHashMap.clear();
		}
		if(addressAutoComplete != null){
			addressAutoComplete.popup.style.display = "none";
		}
	}
}

function setPoiPathPopup(code){
	//信息窗自驾导航中的点的点击事件，弹出信息窗
	var poiInfo = null;
	if (poiHashMap && code) {
		poiInfo = poiHashMap.get(code);
	}
	else{
		poiInfo = this;
	}
	var poiType = poiInfo.type;
	var center = new SuperMap.LonLat(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
	var infowinHtml = "";
	var offset = { x: 60, y: -35 };
	var isBig = false;
        
	infowinHtml = '<div style="width:auto;height:auto;">';
	infowinHtml += '<div class="div1">';
	infowinHtml += '<div class="pop_info_div">';
	infowinHtml += '<ul>';
	if( poiInfo.address != null && poiInfo.address.length != 0 ){
		infowinHtml += ' <li><strong>地址：</strong>' + (poiInfo.address) + '</li>';
	}
	if( poiInfo.zipCode != null && poiInfo.zipCode.length != 0 ){
		infowinHtml += ' <li><strong>邮编：</strong>' + (poiInfo.zipCode) + '</li>';
	}
	if( poiInfo.telephone != null && poiInfo.telephone.length != 0 ){
		infowinHtml += ' <li><strong>传真：</strong>' + (poiInfo.telephone) + '</li>';
	}
	infowinHtml += '</ul>';
	infowinHtml += '</div>';
	infowinHtml += '</div>';
	infowinHtml += '<div style="border-bottom:1px solid #C1D0E2;width:236px;padding:0;margin-bottom:6px; overflow:hidden;"></div>';
	if(poiType == "end"){ 
		infowinHtml += '<div style="text-align:center; width:236px;"><span><strong><a href="javascript:setPoiPath('+poiInfo.x+', '+poiInfo.y+', \'end\')">设为终点</a></strong></span></div>';
	}else{
		infowinHtml += '<div style="text-align:center; width:236px;"><span><strong><a href="javascript:setPoiPath('+poiInfo.x+', '+poiInfo.y+', \'start\')">设为起点</a></strong></span></div>';
	}
	infowinHtml += '</div>';
	
	centerTitle = poiInfo.name;
        // bufferCenter = center;
	var size = new SuperMap.Size(290, 100);
    if(inforwindow_setPathPoint == null){
		inforwindow_setPathPoint = new SuperMap.OSP.Core.InfoWindow("inforwindow_setPathPoint", center,
					size, infowinHtml, null, true);
		
		inforwindow_setPathPoint.autoSize = false;
		inforwindow_setPathPoint.panMapIfOutOfView = true;
		map.addPopup(inforwindow_setPathPoint);
		mapPopups.push(inforwindow_setPathPoint);
	}
	else{
		inforwindow_setPathPoint.lonlat = center;
		inforwindow_setPathPoint.setContentHTML(infowinHtml);
		inforwindow_setPathPoint.updatePosition();
		inforwindow_setPathPoint.show();
	}
	
    var popEle = jQuery('.sm_infoWindowContainer');
    popEle.css('background-color', 'white');
    jQuery('.sm_infoWindowContent').css('border', '0px');
    jQuery('.sm_infoWindowClose').css('background-image', 'url("images/popup_close.png")');
}

//用于“从这里出发”和“到这里去”搜索结果的设为起点或设为终点的信息窗
var inforwindow_setPathPoint = null;
function showPathPoiResult(records, totalCount, poiType, poiId){
	//信息窗自驾导航自动完成之后，若没有选择下拉列表中的点，就把它们显示在地图之上
	var strHtml = "";
    if (records) {
        //装载查询到的POI对象
        poiHashMap = null;
        poiHashMap = new SuperMap.OSP.Core.HashMap();
        poiSearchGroup.clearPOIs();
		for (var i = 0; i < records.length; i++) {
            var poiInfo = records[i];
           /*  poiHashMap.put(poiInfo.code, poiInfo); */
			var itemId = "poi_info_item_"+ i;
			strHtml += '<div id="'+ itemId +'" onmouseover="poiInfoItemMouseover(\'' + itemId + '\',\'img_poi_' + i + '\',\'img_left_' + i + '\',' + i + ')" onmouseout="poiInfoItemMouseout(\'' + itemId + '\',\'img_poi_' + i + '\',\'img_left_' + i + '\',' + i + ')"   class="poi_info_item">';
			//添加图标
			strHtml += '<div style="float:left;"><img id="img_left_' + i + '" src="images/' + (i + 1) + '.gif"></div>';
			//添加内容
			strHtml += '<div style="margin-left:20px; padding-left:4px;"><div style="clear:right;"><div style="float:right; width:50px;"></div></div>';
			//添加信息标题
			if(!!poiInfo.name) {
				if(typeof(poiType) == "undefined"){
					strHtml += '<div id="poi_title"><span><strong><a href="javascript:>' + poiInfo.name + '</a></strong></span></div>';
				}else{
					if(poiType == "end"){
						strHtml += '<div id="poi_title"><span><strong><a href="#" onclick="locateToPoi(\''  + poiInfo.x + '\',\''+ poiInfo.y  +'\')">' + poiInfo.name + '</a></strong></span>&nbsp;&nbsp;&nbsp;<span><strong><a href="javascript:setPoiPath('+poiInfo.x+', '+poiInfo.y+', \'end\')">设为终点</a></strong></span></div>';
					}else{
						strHtml += '<div id="poi_title"><span><strong><a href="javascript:locateToPoi(\''  + poiInfo.x + '\',\''+ poiInfo.y  +'\')">' + poiInfo.name + '</a></strong></span>&nbsp;&nbsp;&nbsp;<span><strong><a href="javascript:setPoiPath('+poiInfo.x+', '+poiInfo.y+', \'start\')">设为起点</a></strong></span></div>';
					}
				}
			}
			//添加地址信息
			if(!!poiInfo.address) {
				strHtml += '<div id="poi_address"><span>地址：' + poiInfo.address + '</span></div>';
			}
			strHtml += '</div></div>';
			
			
            //组织poi对象
            var poi = new SuperMap.OSP.UI.POI("search_POI_SetPath_" + i);
            
			poi.code = poiInfo.code;
			poi.position = new SuperMap.OSP.Core.Point2D(parseFloat(poiInfo.x), parseFloat(poiInfo.y));
			
            var scaledContent = new SuperMap.OSP.UI.ScaledContent();
            scaledContent.content = 'images/num_map/' + (i + 1) + '.png';
            scaledContent.offset = new SuperMap.OSP.Core.Point2D(0, 0);
            poi.title = poiInfo.name;
            poi.scaledContents = scaledContent;
            poi.properties = {
                code: poiInfo.code,
				type: poiType
            };
            poiHashMap.put(poi.id, poi);
			if(i==0){
				var positionPOI = new SuperMap.OSP.Core.Point2D(poiInfo.x, poiInfo.y);
				map.setCenter(positionPOI,11);
			}
            poiSearchGroup.addPOIs(poi);
        }
		jQuery("#div_searchResultPanel").css("display","block");
		jQuery("#search_result").css("display","block");
		jQuery("#div_recommendPanel").css("display", "none");
		jQuery("#searchPOIResult").css("display","block");
    	jQuery("#div_recommendTitlePanel").css("display", "none");
		jQuery("#searchBusResult").css("display","none");	//隐藏线路结果div
		jQuery("#findPath_result").css("display","none");	//隐藏线路结果div
    	jQuery("#span_searchPOITotalCount").html(totalCount);
    	jQuery("#div_searchPOITitlePanel").css("display", "block");
        jQuery("#search_result").html(strHtml);
		
        poiManager.editPOIGroup(poiSearchGroup);
        poiManager.refreshPOI();
		if(totalCount == 1){
			positionPoi(poiInfo.code, "img_left_0");
		}
    }
    else {
        jQuery("#search_result").html("没有找到您要的结果！");
    }
}
/**
 * 设置中心点
 * @param: x
 * @param: y
 */
function locateToPoi(x, y){
	var center = new SuperMap.LonLat(parseFloat(x), parseFloat(y));
	var level = map.level;
	map.setCenter(center, level);
}

//周边查询
var nearKeyword = "";
var ospPaperNear = null;
//地图分享周边查询关键字
var nearBySearchKeyword = "";
var nearBySearchPoint = "";
var bufferCenter;
function nearBySearchParam(keyword){
	dragFeature.deactivate();
	if(markerWindowHashMap != null && markerWindowHashMap.size != 0){
		for(var i = 0; i < markerWindowHashMap.size; i++){
			jQuery("#markWindow"+i).css("display","none");
		}
	}
	if(!keyword){
		nearKeyword = jQuery("#txt").val();
		if(nearKeyword == "" || trim(nearKeyword).length == 0){
			return false;
		}
	}else{
		nearKeyword = keyword;
	}
	if(!!ospPaperNear){
		ospPaperNear.page = 1;
	}
	nearBySearch();
}
function nearBySearch(rowIndex){
	jQuery("#div_recommendTitlePanel").css("display","block");
	jQuery("#div_searchPOITitlePanel").css("display","none");
	jQuery("#div_recommendPanel").css("display","none");
	jQuery("#div_searchResultPanel").css("display","block");
	jQuery("#searchPOIResult").css("display","block");	//隐藏线路结果div
	jQuery("#searchBusResult").css("display","none");	//隐藏线路结果div
	jQuery("#findPath_result").css("display","none");	//隐藏线路结果div
	jQuery("#divPage").css("display", "none");
	if(!ospPaperNear){
		ospPaperNear = new OspPager("ospPaperNear", nearBySearch);
	}
	if (!rowIndex) {
        rowIndex = 0;//起始记录号
	}else if(rowIndex == '1') {
		rowIndex = 0;
	}else{
		rowIndex = rowIndex * 10 - 10;
	}
	if(nearBySearchKeyword == ""){
		nearBySearchKeyword = nearKeyword;
	}
	if(pointByFindPath.position != null){
		bufferCenter = new SuperMap.LonLat(pointByFindPath.position.x, pointByFindPath.position.y);
	}else if(pointByFindPath.x != null){
		bufferCenter = new SuperMap.LonLat(pointByFindPath.x, pointByFindPath.y);
	}else{
		bufferCenter = new SuperMap.LonLat(pointByFindPath.geometry.x, pointByFindPath.geometry.y);
	}
	var radius = bufferDistance;
	var searchGeometry = markRoundGeometry(bufferCenter,radius);
	var getPOIsParam = new SuperMap.OSP.Service.GetPOIsByGeometryParam();
	var poiSearchService = new SuperMap.OSP.Service.POISearch();
	//getPOIsParam.geometry = searchGeometry;
	getPOIsParam.geometry = round;
	getPOIsParam.keyword = nearKeyword;
	getPOIsParam.expectCount = 10;
	getPOIsParam.startRecord = 0;
	poiSearchService.search.url = poiSearchUrl;
	getPOIsParam.datasetName=datasetName;
	getPOIsParam.DataSourceName = 'china_poi';
	var resultObj = document.getElementById(resultDivId);
	resultObj.innerHTML = '正在查找，请稍后.....';
	poiSearchService.getPOIsByGeometry(getPOIsParam, function(result){
        if (result.totalCount != 0) {
			jQuery("#div_recommendTitlePanel").css("display", "none");
			jQuery("#span_searchPOITotalCount").html(result.totalCount);
			jQuery("#div_searchPOITitlePanel").css("display", "block");
            //openOrCloseDistrictPanel(true);
            //openOrCloseTypesPanel(true);
            //设置分页控件的总页数
            ospPaperNear.pageCount = Math.ceil(result.totalCount / 10);
            //显示分页
            ospPaperNear.printHtml("yahoo", pagerId);
			jQuery("#divPage").css("display","block");
            nearBySearchResult(result.records, result.totalCount);
        }
		else{
			resultObj.innerHTML = '未找到相应结果，请您重新输入！';
			jQuery("#divPage").html("");
			return false;
		}
		featuresLayer.removeAllFeatures();
		featuresLayer.style = {fillColor:"#0000FF",pointRadius:5,strokeOpacity:0.2,fillOpacity:0.2};
        var feature = new SuperMap.Feature.Vector(markRoundGeometry(bufferCenter,radius));
        featuresLayer.addFeatures(feature);
		featuresLayer.style = {fillColor: "#7C9DE4",strokeColor: "#7A9BE2",pointRadius:6,strokeWidth:4};
		//jQuery("#divPage").html("");
		
    }, function(error){
        alert(error.information)
        resultObj.innerHTML = error.information;
    });
	
	if (!bufferCenter) {
        bufferCenter = new SuperMap.OSP.Core.Point2D(pointByFindPath.x, pointByFindPath.y);
    }
}

//根据中心点及半径计算圆
var round = null;
function markRoundGeometry(center, radius) {
    var d360 = Math.PI * 2;
    var sidePoints = [];
    var n = 36;
    var d = d360 / n;
    for (var i = 1; i <= n; i++) {
        var rd = d * i;
        var x = center.lon + radius * Math.cos(rd);
        var y = center.lat + radius * Math.sin(rd);
        var sidePoint = new SuperMap.Geometry.Point(x, y);
        sidePoints.push(sidePoint);
    }
	var line = new SuperMap.Geometry.LinearRing(sidePoints);
	var roundRegion = new SuperMap.Geometry.Polygon(line);
	round = new SuperMap.OSP.Utility.Geometry();
	center = new SuperMap.OSP.Core.Point2D(center.lon,center.lat);
	sidePoints[0] = new SuperMap.OSP.Core.Point2D(sidePoints[0].x,sidePoints[0].y);
	var param = [center,sidePoints[0]];
	round.point2Ds = param;
    return roundRegion;
}
//折叠或展开行政区域显示区域
function openOrCloseDistrictPanel(close){
    var topObj = document.getElementById(topDistDivId);
    if (!close) {
        if (topObj.innerHTML.indexOf("iw_minus") > 0) {
            topObj.innerHTML = '<img src="images/iw_plus.gif" />';
            document.getElementById(bottomDistDivId).style.display = "none";
        }
        else {
            topObj.innerHTML = '<img src="images/iw_minus.gif" />';
            document.getElementById(bottomDistDivId).style.display = "block";
        }
    }
    else {
        topObj.innerHTML = '<img src="images/iw_plus.gif" />';
        document.getElementById(bottomDistDivId).style.display = "none";
    }
}

//展开或折叠类型面板
function openOrCloseTypesPanel(close){
    var topObj = document.getElementById(topTypeDivId);
    if (!close) {
        if (topObj.innerHTML.indexOf("iw_minus") > 0) {
            topObj.innerHTML = '<img src="images/iw_plus.gif" />';
            document.getElementById(bottomTypeDivId).style.display = "none";
        }
        else {
            topObj.innerHTML = '<img src="images/iw_minus.gif" />';
            document.getElementById(bottomTypeDivId).style.display = "block";
        }
    }
    else {
        topObj.innerHTML = '<img src="images/iw_plus.gif" />';
        document.getElementById(bottomTypeDivId).style.display = "none";
    }
}

function mapResize(){
    if (map) {
        // var mapHeight = document.getElementById(mapDivId).clientHeight;
        // var mapWidth = document.getElementById(mapDivId).clientWidth;
        map.updateSize();
		markersLayer._width = mapWidth;
		markersLayer._height = mapHeight;
    }
}
//------------------自动完成-----------------//
var addressAutoComplete = null;
var oldName = null;
var autoCompleteHashMap = null;
var startName = null;
var endName = null;

//搜索框自动提示完成
var isShow = true;
var pathPOIHashMap = null; //命名
function autoCompleteBySearch(event,arg){
	var text_trim = trim( jQuery("#" + arg).val() );
	if(text_trim.length == 0 && addressAutoComplete){
		addressAutoComplete.hide();
		addressAutoComplete.distroy();
		// addressAutoComplete = null;
		return;
	}
	/* if(addressAutoComplete){
		addressAutoComplete.distroy();
		addressAutoComplete = null;
	} */
	if(addressAutoComplete == null){
		addressAutoComplete = new MyfAutoComplete(arg);		
	}
	else if(addressAutoComplete.popup != null){
		addressAutoComplete.distroy();
		addressAutoComplete.init(arg);		
	}
	else{
		addressAutoComplete.init(arg);	
	}
	
	// if(arg == "ssearch_box_input" || arg.match("txtToHere") || arg.match("txtFromHere") || arg.match("markToHere") || arg.match("markFromHere")){
		
		var x = jQuery("#" + arg).offset().left;
		var y = jQuery("#" + arg).offset().top + jQuery("#" + arg).height();
		addressAutoComplete.popup.style.left = x;
		addressAutoComplete.popup.style.top = y;
		pathPOIHashMap = null;
		pathPOIHashMap = new SuperMap.OSP.Core.HashMap();
		isShow = true;
	// }
	
	var keyword = document.getElementById(arg).value;
	for(var i =0; i< provinces.length; i++){
		if(provinces[i][0].indexOf(keyword) != -1){
			return false;
		}
	}
	for(var j = 0; j < citys.length; j++){
		if(citys[j].city.indexOf(keyword) != -1){
			return false;
		}
	}
	for(var k = 0; k < chinaTown.length; k++){
		if(chinaTown[k][0].indexOf(keyword) != -1){
			return false;
		}
	}
	var poiSearchService = new SuperMap.OSP.Service.POISearch();
	var keywordPOIParam = new SuperMap.OSP.Service.GetPOIsParam();
	if(keyword.length != 0 && oldName != keyword){
		if(arg == "txtToHere" || arg == "txtFromHere" || arg == "markToHere" || arg == "markFromHere"){
			oldName = keyword;
		}else{
			oldName = keyword;
		}
		keywordPOIParam.keyword = keyword; 
		if(cityName != null){
			var length_citys = citys.length;
			for(var j = 0; j < length_citys; j++){
				var temp_city = citys[j];
				if(cityName == temp_city.city){
					divisionCode = temp_city.admincode;
					parentCode = temp_city.admincode.slice(0, 2) + "0000";
					break;
				}
			}
			if(divisionCode.substr(2,6) == '0000'){
				for(var k = 0; k < provinces.length; k++){
					parentCode = divisionCode;
					break;
				}
			}
		}
		if(divisionCode.substr(2,6) == '0000'){
			keywordPOIParam.districtLevel = 1;
			shareMapUserLevel = 1;
		} else if (divisionCode == '0'){
			keywordPOIParam.districtLevel = 0;
			shareMapUserLevel = 0;
		}
		else {
			keywordPOIParam.districtLevel = 2;
			shareMapUserLevel = 2;
		}
		if(divisionCode == '110000' || divisionCode == "0"){
			datasetName = keywordPOIParam.datasetName = 'PbeijingP';
		}else if(divisionCode == '500000'){
			datasetName = keywordPOIParam.datasetName = 'PchongqingP';
		}else if(divisionCode == '120000'){
			datasetName = keywordPOIParam.datasetName = 'PtianjinP';
		}else if(divisionCode == '310000'){
			datasetName = keywordPOIParam.datasetName = 'PshanghaiP';
		}else {
			for(var i = 0; i < provinces.length; i++){
				if(parentCode == provinces[i][1]){
				datasetName = keywordPOIParam.datasetName = provinces[i][6];
					break;
				}
			}
		}
		keywordPOIParam.districtCode = divisionCode;
		keywordPOIParam.expectCount = 10;
		keywordPOIParam.startRecord = 0;
		keywordPOIParam.DataSourceName = 'china_poi';
		poiSearchService.search.url = poiSearchUrl;
		var arrData = [];
		poiSearchService.getPois(keywordPOIParam, function(result){			
			var text_trim = trim( jQuery("#" + arg).val() );
			/* if(text_trim.length == 0){
				addressAutoComplete.hide();
				addressAutoComplete.popup.style.display = "none";
			} */
			if (result.totalCount != 0 && text_trim.length > 0 && addressAutoComplete !== null) {
				if(addressAutoComplete.popup == null){
					addressAutoComplete.init(arg);
				}
				for(var i = 0; i < result.records.length; i ++){
					var poiInfo = result.records[i];
					if(!!poiInfo.affiliation){
						 if(poiInfo.affiliation.substr(0,3) == poiInfo.affiliation.substr(5,3)){
							 var affiliation = poiInfo.affiliation.substr(5,8);
						 }else{
							var affiliation = poiInfo.affiliation;
						 }
						arrData.push({value:poiInfo.name,text:'<span style="float:right;padding-right:4px">' + affiliation + '</span>'});
						
						//arrData.push(poiInfo.name);
						if(arg != "ssearch_box_input"){
							pathPOIHashMap.put(poiInfo.name, poiInfo);
						}
					}else{
						arrData.push(poiInfo.name);
						
						if(arg != "ssearch_box_input"){
							pathPOIHashMap.put(poiInfo.name, poiInfo);
						}
					}
					
				}
				addressAutoComplete.setData(arrData);
				if(isShow){
					addressAutoComplete.show();
				}
				isShow = true;
			}
		},function(error){
		
		});
	}
	
}

// 在地图页面上点击的周边查询
function popSearchBuffer(obj, name, lon, lat){
	setTab(1, obj);
	
	$('#search_point_info').empty();
	$('#search_point_info').append(name);
	
	addBufferSearchParam(lon, lat);
}

/**
 * 创建周边查询的经纬度
 * @param lon
 * @param lat
 * @return
 */
function addBufferSearchParam(lon, lat){
	bufferSearchParam.x = lon;
	bufferSearchParam.y = lat;
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


//获取网址URL参数
function GetUrlArgs(url) {
   var url = location.search; //获取url中"?"符后的字串
   var theRequest = new Object();
   if (url.indexOf("?") != -1) {
      var str = url.substr(1);
      strs = str.split("&");
      for(var i = 0; i < strs.length; i ++) {
         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
      }
   }
   return theRequest;
}

//左则面板POI内容移入事件
function poiInfoItemMouseover(id,imgPoiId,imgLeftId,i) {
	document.getElementById(id).className = "poi_info_item poi_info_mouseover";
	jQuery("#"+id).css("background","#EBF0F8");
	jQuery("#"+imgPoiId).attr("src","images/new_map/" + ( i + 1 ) + ".gif");
	jQuery("#"+imgLeftId).attr("src","images/left_num/" + ( i + 1 ) + ".gif");
}

//左侧面板POI内容移出事件
function poiInfoItemMouseout(id,imgPoiId,imgLeftId,i) {
	document.getElementById(id).className = "poi_info_item";
	if(jQuery("#"+imgLeftId).attr("isselect") != 1){
		jQuery("#"+id).css("background","");
		jQuery("#"+imgPoiId).attr("src","images/num_map/"+( i + 1 )+".png");
		jQuery("#"+imgLeftId).attr("src","images/"+( i + 1 )+".gif");
	}else{
		
	}
}

function change(){
	var center = map.get_center();
	var mapBounds = new SuperMap.Geometry.Rectangle(13678382.431223,2322443.78251245,17143213.6046691,5713827.61663605);
	if(mapBounds.contains(center.x,center.y)){
		alert("日本");
		map.setLayers([japenLayer]);
		map.removeLayer(layer);
	}else{
		alert("中国");
		map.setLayers([layer]);
		map.removeLayer(japenLayer);
		
	}
	
}
//异步加载代码
var JSLoader = function(){
	
	var scripts = {}; // scripts['a.js'] = {loaded:false,funs:[]}
	
	function getScript(url){
		var script = scripts[url];
		if (!script){
			script = {loaded:false, funs:[]};
			scripts[url] = script;
			add(script, url);
		}
		return script;
	}
	
				
	function run(script){
		var funs = script.funs,
			len = funs.length,
			i = 0;
		
		for (; i<len; i++){
			var fun = funs.pop();
			fun();
		}
	}
	
	function add(script, url){
		var scriptdom = document.createElement('script');
		scriptdom.type = 'text/javascript';
		scriptdom.loaded = false;
		scriptdom.src = url;
		
		scriptdom.onload = function(){
			scriptdom.loaded = true;
			run(script);
			scriptdom.onload = scriptdom.onreadystatechange = null;
		};
		
		//for ie
		scriptdom.onreadystatechange = function(){
			if ((scriptdom.readyState === 'loaded' || 
					scriptdom.readyState === 'complete') && !scriptdom.loaded) {
				
				run(script);
				scriptdom.onload = scriptdom.onreadystatechange = null;
			}
		};
		
		document.getElementsByTagName('head')[0].appendChild(scriptdom);
	}
	
	return {
		load: function(url){
			var arg = arguments,
				len = arg.length,
				i = 1,
				script = getScript(url),
				loaded = script.loaded;
			
			for (; i<len; i++){
				var fun = arg[i];
				if (typeof fun === 'function'){
					if (loaded) {
						fun();
					}else{
						script.funs.push(fun);
					}
				}
			}
		}
	};
}();


var poiIDs = 0;
var clientX = 0;
var clientY = 0;
function markPOI() {
	dragFeature.activate();
	map.events.on({"click":addPOIHandler});
	map.events.on({"zoomend":changeWindow});
	
	// jQuery("#smLayer").css("cursor", "pointer");
}
function changeWindow(arg){
	if(markerWindowHashMap != null && markerWindowHashMap.size !=0){
		// for(var i = 0; i < markerWindowHashMap.size; i++){
			// arg.object.popups[i].hide();
		// }
	}
}
function addPOIHandler(arg,geometry){
	/* if (jQuery.browser.msie){
		jQuery("#smLayer").css("cursor", "url(http://localhost:8080/chaotuyun/map/images/map/pan.ico), default");
	}
	else{
		jQuery("#smLayer").css("cursor", "url(http://localhost:8080/chaotuyun/map/images/map/pan.ico) 8 8, default");
	} */
	
	clientX = arg.clientX;
	clientY = arg.clientY;
	//获取浏览器页面的宽度和高度
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	//获取地图的高度和宽度
	var mapWidth = map.size.w;
	var mapHeight = map.size.h;
	//当前point的经纬度坐标=传入坐标-(当前页面高度-地图的高度)
	var px = new SuperMap.Pixel(clientX-(clientWidth-mapWidth),clientY-(clientHeight-mapHeight));
	var point = map.getLonLatFromPixel(px);
	map.events.unregister("click",map,addPOIHandler);
    addPOI(point);
}
var poiHashMap = null;
var poiMarkerHashMap = null;
var markerWindowHashMap = null;
var featureHashMap = null;
function addPOI(point2D,name,address) {
	if(infowindowHashMap == null){
		infowindowHashMap = new SuperMap.OSP.Core.HashMap();
	}
	if(featureHashMap == null){
		featureHashMap = new SuperMap.OSP.Core.HashMap();
	}
	if(poiMarkerHashMap == null){
		poiMarkerHashMap = new SuperMap.OSP.Core.HashMap();
	}
	if(poiHashMap == null){
		poiHashMap = new SuperMap.OSP.Core.HashMap();
	}
	if(markerWindowHashMap == null){
		markerWindowHashMap = new SuperMap.OSP.Core.HashMap();
	}
	if(point2D.lon != null || point2D.x != null){
		if(poiIDs == 0){
			var title = "添加标记";
		}else{
			var title = "添加标记" + poiIDs;
		}
		if(point2D.lon != null){
			var point = new SuperMap.Geometry.Point(point2D.lon,point2D.lat);
		}else{
			var point = new SuperMap.Geometry.Point(point2D.x,point2D.y);
		}
		point.id = "poiMarker"+poiIDs;
		var poi = new SuperMap.Feature.Vector(point);
		poi.style = {externalGraphic:"images/map_marker/active.png",graphicWidth:16,graphicHeight:16,graphicTitle:"标注点"};
		vectorLayer.addFeatures(poi);
		
		jQuery("#poiMarker" + poiIDs).css("cursor", "pointer");
	 }
	var html = '';
		if(name == null){
			/*
			html += "<div style='width:auto;height:auto;'><div class='div1'><table>";
			
			html += "<tr><td>名称</td><td><input id='txt_markerName' style='width:160px;height:25px;' type='text' value=''></input></td></tr>";
			html += "<tr><td>备注</td><td><input id='txt_markerRemarks' style='height:40px;width:160px;' type='text' value=''></input></td></tr>";
			html += "<tr><td></td><td><input style='float:right;border:1px solid gray; background-color:white; width:40px; height:20px; cursor:pointer' type='button' onclick='deleteMarker("+poiIDs+")' value='删除' ></input><input style='float:right;border:1px solid gray; background-color:white; width:40px; height:20px; cursor:pointer' type='button' value='保存' onclick='saveMarker("+poiIDs+")'></input></td></tr>" ;
			html += '</table></div></div>';
			*/
			
			html = '<div style="width:auto;height:auto;">';
			
			html += '<div class="infowindowTitle">';
			html += '<span class="infowindowTitleTxt">添加标记</span>';
			html += '</div>';
			
			html += '<div style="height:25px;margin-top:10px;margin-left:10px;">';
			html += '<span style="margin:3px 5px 0 0;float:left">名称：</span>';
			html += '<input id="txt_markerName" type="text" style="width:220px;height:15px;float:left;border:#b5b5b5 solid 1px;line-height:18px;padding:3px 3px;font:12px/1.5 tahoma,arial,SimSun;margin-top:3px;"></input>'
			html += '</div>';
			
			html += '<div style="margin-top:5px;margin-left:10px;">';
			html += '<span style="margin:3px 5px 0 0;float:left">备注：</span>';
			html += '<textarea id="txt_markerRemarks" style="border:#b5b5b5 solid 1px;float:left;font:12px/1.5 tahoma,arial,SimSun;height:64px;overflow-y:auto;padding:3px;width:220px;margin-top:3px;" name="co"></textarea>'
			html += '</div>';
			
			html += '<div>'
			html += '<input style="float:right;border:1px solid gray; background-color:white; width:45px; height:20px; cursor:pointer;margin-right:14px;margin-top:7px;" type="button" onclick="deleteMarker('+poiIDs+')" value="删除" ></input>'
			html += '<input style="float:right;border:1px solid gray; background-color:white; width:45px; height:20px; cursor:pointer;margin-right:5px;margin-top:7px;" type="button" value="保存" onclick="saveMarker('+poiIDs+')"></input>';
			html += '</div>'
			
			
			html += '</div>';
			
		}
		else{
			
			html +='<div style="backgroud-color:gray"><div style="float:left;">'+name+'</div><div style="position:absolute;padding-right:8px;right:20px"><img title="删除" style="cursor:pointer; margin-right:2px" src="images/map_marker/deleteMarker.png" onclick="deleteMarker('+poiIDs+')" /><image title="修改" style="cursor:pointer; margin-right:2px" src="images/map_marker/modifyMarker.png" onclick="modifyMarker('+poiIDs+')"/><img title="分享" style="cursor:pointer; margin-right:2px" src="images/map_marker/sharemap.png" onclick="shareMap()"/></div></div>';
			html += '</br><div style="width:auto;height:auto;">';
			html += '<div class="div1">';
			html += '<div class="pop_info_div" style="padding-left:10px;position:absolute;left:10px;height:30px;">';
			html += '<ul>';
			html += '<span >备注：</span><span>'+address+'</span>';
			html += '</ul>';
			html += '</div><br/>';
			html += '</div><br/>';
			html += '<div style="border-bottom:1px solid #C1D0E2;width:236px;padding:0;margin-bottom:6px; overflow:hidden;"></div>';
			html += '<ul style="margin-top:20px;">';
			html += '<li id="li_near"><a href="javascript:showSearch('+poiIDs+')" style="text-decoration:none">周边查询&nbsp;&nbsp;|&nbsp;&nbsp;</a></li>';
			html += '<li id="li_path"><a href="javascript:showFindPath('+poiIDs+')" style="text-decoration:none">自驾导航&nbsp;&nbsp;|&nbsp;&nbsp;</a></li>';
			html += '<li><a href="javascript:zoomInMarker(' + poiIDs + ')" style="text-decoration:none">在此处放大&nbsp;&nbsp;&nbsp;</a></li>';
			html += '</ul>';
			// html += '<br/><div style="border-bottom:1px solid #C1D0E2;width:236px;padding:0;margin-bottom:6px; overflow:hidden;"></div>';
			html += '<div class="pop_expInfo_div">';
			html += '<div id="markNear'+poiIDs+'" style="display:block">';
			html += '<ul>半径：'
			html += '<li><a href="javascript:changeMeters(' + 500 + ', 0,'+poiIDs+')">500米</a></li>';
			html += '<li><a href="javascript:changeMeters(' + 1000 + ', 1,'+poiIDs+')" style="border: 1px solid;">1000米</a></li>';
			html += '<li><a href="javascript:changeMeters(' + 3000 + ', 2,'+poiIDs+')">3000米</a></li>';
			html += '<li><a href="javascript:changeMeters(' + 5000 + ', 3,'+poiIDs+')">5000米</a></li>';
			html += '</ul>';
			html += '</br>';
			html += '<ul>';
			html += '<li><a href="javascript:nearBySearchParam(\'银行\')">银行</a></li>';
			html += '<li><a href="javascript:nearBySearchParam(\'饭店\')">饭店</a></li>';
			html += '<li><a href="javascript:nearBySearchParam(\'公园\')">公园</a></li>';
			html += '<li>其他:<input id="txt" type="text" size="8"/></li>';
			html += '<li><input type="button" value="确定" onclick="nearBySearchParam()"/></li>';
			html += '</ul>'
			html += '</div>';
			html += '<div id="markFindPath'+poiIDs+'" style="display:none;">';
			html += '<ul>';
			html += '<li id="li_fromHere"><a style="text-decoration:none;text-align:center; " href="javascript:showFromHere('+poiIDs+')">从这里出发</a></li>&nbsp;&nbsp;&nbsp;'
			html += '<li id="li_toHere"><a style="text-align:center; text-decoration:none;" href="javascript:showToHere('+poiIDs+')">到这里去</a></li>';
			html += '</ul>';
			html += '</div>';
			html += '<div id="mark_fromHere'+poiIDs+'" style="display:none;">'
			html += '<ul>'
			html += '<li>';
			html += '<span>终点:</span>&nbsp;<input id="markToHere" type="text" style="width:160px; height:25px;line-height:25px; border:1px solid #8e9baa;vertical-align:middle;"></input>&nbsp;<input type="button" style="border:1px solid gray; background-color:white; width:40px; height:20px;cursor:pointer" onclick="setPathPoint( \'start\','+poiIDs+')" value="确定"></input>';
			html += '</li>';
			html += '</ul>'
			html += '</div>'
			html += '<div id="mark_toHere'+poiIDs+'" style="display:none">'
			html += '<ul>'
			html += '<li>';
			html += '<span>起点:</span>&nbsp;<input id="markFromHere" type="text" style="width:160px; height:25px;line-height:25px; border:1px solid #8e9baa;vertical-align:middle;"></input>&nbsp;<input type="button" style="line-height:12px;border:1px solid gray; background-color:white; width:40px; height:20px; cursor:pointer" onclick="setPathPoint( \'end\','+poiIDs+')" value="确定"></input>';
			html += '</li>';
			html += '</ul>'
			html += '</div>'
			html += '</div>';
			poi.properties = {
				name:name,
				remarks: address
			};
			pointByFindPath = point2D;
			infowindowHashMap.put(poi.id,html);
		}
		
        poiManager.editPOIGroup(poiMarkerGroup);
		var	markWindow = new SuperMap.OSP.Core.InfoWindow("markWindow"+poiIDs,
			new SuperMap.LonLat(poi.geometry.x,poi.geometry.y),
			new SuperMap.Size(293,190),
			html,
			null,
			true);
			markWindow.panMapIfOutOfView = true;
			map.addPopup(markWindow);
			mapPopups.push(markWindow);
			markerWindowHashMap.put(markWindow.id,markWindow);
		poiMarkerHashMap.put(poi.id, markWindow);
		poiHashMap.put(poiIDs,poi);
		featureHashMap.put(poiIDs,poi);
		 poiIDs++;
		 dragFeature.onStart = function(poi){
			if(poiMarkerHashMap.get(poi.id)){
				var window = poiMarkerHashMap.get(poi.id);
				window.hide();
			}else{
				var window = markerWindowHashMap.get(poi.id);
				window.hide();
			}
			
		}
		dragFeature.onComplete = function(poi){
			if(poiMarkerHashMap.get(poi.id)){
				var window = poiMarkerHashMap.get(poi.id);
				window.lonlat = new SuperMap.LonLat(poi.geometry.x,poi.geometry.y);
				window.updatePosition();
				window.show();
			}else{
				var window = markerWindowHashMap.get(poi.id);
				window.lonlat = new SuperMap.LonLat(poi.geometry.x,poi.geometry.y);
				window.updatePosition();
				window.show();
			}			
		}
}
//保存地图标记 
var markerType;
var infowindowHashMap;
var markerArray;
function saveMarker(poi){
	if(markerArray == null){
		markerArray = new Array();
	}
	markerType = "save";
	//var pid = id;
	var markerPoi = new Object();
	var currentPoi = poiHashMap.get(poi);
		markerPoi.position = currentPoi.geometry;
		markerPoi.id = currentPoi.geometry.id;
		pointByFindPath = currentPoi;
	var namePOI = jQuery("#txt_markerName").val();
	var remarksPOI = jQuery("#txt_markerRemarks").val();
	if(namePOI == ""){
		namePOI = "我的标记";
		markerPoi.name = namePOI;
	}else{
		markerPoi.name = namePOI;
	}

	if(remarksPOI == ""){
		remarksPOI = "我的备注";
		markerPoi.address = remarksPOI;
	}else{
		markerPoi.address = remarksPOI;
	}
	markerArray.push(markerPoi);
	currentPoi.title = namePOI;
	// var location = currentPoi.position;
	var x = currentPoi.geometry.x;
	var y = currentPoi.geometry.y;
	
	var html = '';
	
	// html +='<div style="backgroud-color:gray"><div style="float:left;">'+namePOI+'</div><div style="position:absolute;padding-right:8px;right:20px"><img title="删除" style="cursor:pointer; margin-right:2px" src="images/map_marker/deleteMarker.png" onclick="deleteMarker('+poi+')" /><image title="修改" style="cursor:pointer; margin-right:2px" src="images/map_marker/modifyMarker.png" onclick="modifyMarker('+poi+')"/><img title="分享" style="cursor:pointer; margin-right:2px" src="images/map_marker/sharemap.png" onclick="shareMap()"/></div></div>';
	
	html +='<div style="backgroud-color:gray"><div style="float:left;">'+namePOI+'</div><div style="position:absolute;padding-right:8px;right:3px"><img title="删除" style="cursor:pointer; margin-right:2px" src="images/map_marker/deleteMarker.png" onclick="deleteMarker('+poi+')" /><image title="修改" style="cursor:pointer; margin-right:2px" src="images/map_marker/modifyMarker.png" onclick="modifyMarker('+poi+')"/></div></div>';
	
	html += '</br><div style="width:auto;height:auto;">';
	html += '<div class="div1">';
	html += '<div class="pop_info_div" style="padding-left:10px;position:absolute;left:10px;height:30px;">';
	html += '<ul>';
	html += '<span >备注：</span><span>'+remarksPOI+'</span>';
	html += '</ul>';
	html += '</div><br/>';
	html += '</div><br/>';
	// html += '<div style="border-bottom:1px solid #C1D0E2;width:236px;padding:0;margin-bottom:6px; overflow:hidden;"></div>';
	
	//操作
	html += '<div class="infowindowSearch" style="margin-top:20px;">';
	
	html += '<ul class="infowindowSearchTitle">';			
	html += '<li id="li_markerwindow'+ markerPoi.id +'_1" class="third blueA hover" style=" border-left: 0 none;" onclick="switchMarkerWinTab(\''+1+'\', \'' + markerPoi.id +'\')">';
	html += '<img src="images/infowindow/nearsearch.png" /><span>周边查询</span>';
	html += '</li>';
	html += '<li id="li_markerwindow'+ markerPoi.id +'_2" class="second blueA" onclick="switchMarkerWinTab(\''+2+'\', \'' + markerPoi.id +'\')">';
	html += '<img src="images/infowindow/start.png" /><span>从这里出发</span>';
	html += '</li>';
	html += '<li id="li_markerwindow'+ markerPoi.id +'_3" class="second blueA" onclick="switchMarkerWinTab(\''+3+'\', \'' + markerPoi.id +'\')">';
	html += '<img src="images/infowindow/end.png" /><span>到这里去</span>';
	html += '</li>';
	html += '</ul>';	
		
	//周边查询
	html += '<div id="tab_markerwindow'+ markerPoi.id +'_1" class="infowindowSearchContent" style="padding-top: 10px;">';	
	html += '<ul>半径：'
	html += '<li><a style="text-decoration:none;" href="javascript:changeMeters(' + 500 + ', 0)">500米</a></li>';
	html += '<li><a style="text-decoration:none; font-weight:bold;" href="javascript:changeMeters(' + 1000 + ', 1)">1000米</a></li>';
	html += '<li><a style="text-decoration:none" href="javascript:changeMeters(' + 3000 + ', 2)">3000米</a></li>';
	html += '<li><a style="text-decoration:none" href="javascript:changeMeters(' + 5000 + ', 3)">5000米</a></li>';
	html += '</ul>';
	html += '</br>';
	html += '</br>';
	html += '<ul>';
	html += '<li><a style="text-decoration:none" href="javascript:nearBySearchParam(\'银行\')">银行</a></li>';
	html += '<li><a style="text-decoration:none" href="javascript:nearBySearchParam(\'饭店\')">饭店</a></li>';
	html += '<li><a style="text-decoration:none" href="javascript:nearBySearchParam(\'公园\')">公园</a></li>';
	html += '<li style="margin-left:5px;">其他:&nbsp;<input style="padding-top:5px;" id="txt" type="text" size="8"/></li>';
	html += '<li style="margin-left:5px;padding-top:5px;"><input type="button" class="infowindowSearchButton" value="确定" onclick="nearBySearchParam()"/></li>';
	html += '</ul>'
	html += '</div>';
	
	//从这里出发
	html += '<div id="tab_markerwindow'+ markerPoi.id +'_2" class="infowindowSearchContent" style="padding-top: 17px;display:none">';	
	html += '<ul>'
	html += '<li>';
	if(typeof(poi.position) == "undefined"){
		html += '<span>终点:</span>&nbsp;<input id="markToHere'+markerPoi.id+'" type="text" class="infowindowSearchInput"></input>&nbsp;';
		html += '<input type="button" class="infowindowSearchButton" onclick="setPathPoint(\'start\','+poi.x+','+poi.y+', \'markToHere'+ markerPoi.id +'\')" value="确定"></input>'
	}else{
		html += '<span>终点:</span>&nbsp;<input id="markToHere'+markerPoi.id+'" type="text" class="infowindowSearchInput"></input>&nbsp;<input type="button" class="infowindowSearchButton" onclick="setPathPoint(\'start\','+poi.position.x+','+poi.position.y+', \'markToHere'+ markerPoi.id +'\')" value="确定"></input>';
	}
	html += '</li>';
	html += '</ul>'
	html += '</div>';
		
	//到这里去
	html += '<div id="tab_markerwindow'+ markerPoi.id +'_3" class="infowindowSearchContent" style="padding-top: 17px; display:none;">';	
	html += '<ul>'
	html += '<li>';
	if(typeof(poi.position) == "undefined"){
		html += '<span>起点:</span>&nbsp;<input id="markFromHere'+markerPoi.id+'" type="text" class="infowindowSearchInput"></input>&nbsp;<input type="button" class="infowindowSearchButton" onclick="setPathPoint( \'end\','+poi.x+','+poi.y+', \'markFromHere'+ markerPoi.id +'\')" value="确定"></input>';
	}else{
		html += '<span>起点:</span>&nbsp;<input id="markFromHere'+markerPoi.id+'" type="text" class="infowindowSearchInput"></input>&nbsp;<input type="button" class="infowindowSearchButton" onclick="setPathPoint( \'end\','+poi.position.x+','+poi.position.y+',\'markFromHere'+ markerPoi.id +'\')" value="确定"></input>';
	}
	html += '</li>';
	html += '</ul>'
	html += '</div>';
		
	html += '</div>';
	
    html += '</div>';
	currentPoi.properties = {
		name:namePOI,
		remarks:remarksPOI
	};
	var markWindow = markerWindowHashMap.get("markWindow"+poi);
	markWindow.lonlat = new SuperMap.LonLat(currentPoi.geometry.x,currentPoi.geometry.y);
	markWindow.setContentHTML(html);
	markWindow.updatePosition();
	markWindow.show();
	markerWindowHashMap.put(currentPoi.id,markWindow);
	poiMarkerHashMap.put(currentPoi.id);
	infowindowHashMap.put(currentPoi.id,html);
	 
	poiMarkerHashMap.put(currentPoi.id, markWindow);
}
/**
 *信息窗的内容切换
 *@param id1, id2
 */
function switchMarkerWinTab(index, id){
	switch(index){
		//周边查询
		case '1':
			jQuery("#tab_markerwindow"+ id +"_1").css("display", "block");
			jQuery("#li_markerwindow"+ id +"_1").attr("class", "third blueA hover");

			jQuery("#tab_markerwindow"+ id +"_2").css("display", "none");	
			jQuery("#li_markerwindow"+ id +"_2").attr("class", "second blueA");
			jQuery("#tab_markerwindow"+ id +"_3").css("display", "none");	
			jQuery("#li_markerwindow"+ id +"_3").attr("class", "second blueA");
			break;
		//从这里出发
		case '2':
			jQuery("#tab_markerwindow"+ id +"_2").css("display", "block");
			jQuery("#li_markerwindow"+ id +"_2").attr("class", "second blueA hover");	

			jQuery("#tab_markerwindow"+ id +"_1").css("display", "none");		
			jQuery("#li_markerwindow"+ id +"_1").attr("class", "third blueA");
			jQuery("#tab_markerwindow"+ id +"_3").css("display", "none");		
			jQuery("#li_markerwindow"+ id +"_3").attr("class", "third blueA");
			
			jQuery("#markToHere"+id).onchange(function(){
				var e = typeof(event) == "undefined" ? null:event;
				autoCompleteBySearch(e,"markToHere"+id);
			});
			break;
		//到这里去	
		case '3':
			jQuery("#tab_markerwindow"+ id +"_3").css("display", "block");
			jQuery("#li_markerwindow"+ id +"_3").attr("class", "second blueA hover");	

			jQuery("#tab_markerwindow"+ id +"_1").css("display", "none");		
			jQuery("#li_markerwindow"+ id +"_1").attr("class", "third blueA");
			jQuery("#tab_markerwindow"+ id +"_2").css("display", "none");		
			jQuery("#li_markerwindow"+ id +"_2").attr("class", "third blueA");
			
			jQuery("#markFromHere" + id).onchange(function(){
				var e = typeof(event) == "undefined" ? null:event;
				autoCompleteBySearch(e,"markFromHere" + id);
			});
			break;
	}
}

//删除地图标记
function deleteMarker(id){
	id = id+"";
	if(id.length <2){
		if(typeof(infowindowHashMap)!= "undefined"){
			infowindowHashMap.remove("poiMarker"+id);
			poiMarkerHashMap.remove("poiMarker"+id);
		}
		var win = markerWindowHashMap.get("markWindow"+id);
		win.destroy();
		vectorLayer.removeFeatures(featureHashMap.get(id));
		poiMarkerHashMap.remove(id);
		markerWindowHashMap.remove("markWindow"+id);
		poiIDs = poiMarkerHashMap.size;
	}else{
		var win = markerWindowHashMap.get(id);
		win.destroy();
		if(typeof(infowindowHashMap)!= "undefined"){
			infowindowHashMap.remove(id);
		}
		markerWindowHashMap.remove(id);
		vectorLayer.removeMarker(poiMarkerHashMap.get(id));
		poiMarkerHashMap.remove(id);
		poiIDs = poiMarkerHashMap.size;
	}
	dragFeature.deactivate();
}

//修改已有的标记
function modifyMarker(id){
	var currentPoi = poiHashMap.get(id);
	var location = currentPoi.geomerty;
	var title = "修改标记";
	var name = currentPoi.properties.name;
	var remarks = currentPoi.properties.remarks;
	
	var paramID = "\""+id+"\"";	
	var html = "";
		/*
		html += "<div style='width:auto;height:auto;'><div class='div1'><table>";
		html += "<tr><td>名称</td><td><input id='txt_markerName' style='width:160px;height:25px;' type='text' value='"+name+"'></input></td></tr>";
		html += "<tr><td>备注</td><td><input id='txt_markerRemarks' style='height:40px;width:160px;' type='text' value='"+remarks+"'></input></td></tr>";
		html += "<tr><td></td><td><input style='float:right;border:1px solid gray; background-color:white; width:40px; height:20px; cursor:pointer' onclick='cancelMarker("+id+")' type='button' value='取消' ></input><input style='float:right;border:1px solid gray; background-color:white; width:40px; height:20px; cursor:pointer' type='button' value='保存' onclick='saveMarker("+id+")'></input></td></tr>" ;
		html += '</table></div></div>';
		*/
	
		html = '<div style="width:auto;height:auto;">';
		
		html += '<div class="infowindowTitle">';
		html += '<span class="infowindowTitleTxt">修改标记</span>';
		html += '</div>';
		
		html += '<div style="height:25px;margin-top:10px;margin-left:10px;">';
		html += '<span style="margin:3px 5px 0 0;float:left">名称：</span>';
		html += '<input id="txt_markerName" type="text" style="width:220px;height:15px;float:left;border:#b5b5b5 solid 1px;line-height:18px;padding:3px 3px;font:12px/1.5 tahoma,arial,SimSun;margin-top:3px;" value="'+name+'"></input>'
		html += '</div>';
		
		html += '<div style="margin-top:5px;margin-left:10px;">';
		html += '<span style="margin:3px 5px 0 0;float:left">备注：</span>';
		html += '<textarea id="txt_markerRemarks" style="border:#b5b5b5 solid 1px;float:left;font:12px/1.5 tahoma,arial,SimSun;height:64px;overflow-y:auto;padding:3px;width:220px;margin-top:3px;" name="co">'+remarks+'</textarea>'
		html += '</div>';
		
		html += '<div>'
		html += '<input style="float:right;border:1px solid gray; background-color:white; width:45px; height:20px; cursor:pointer;margin-right:14px;margin-top:7px;" type="button" onclick="cancelMarker('+id+')" value="取消" ></input>'
		html += '<input style="float:right;border:1px solid gray; background-color:white; width:45px; height:20px; cursor:pointer;margin-right:5px;margin-top:7px;" type="button" value="保存" onclick="saveMarker('+id+')"></input>';
		html += '</div>'
		
		
		html += '</div>';
		
		var markWindow = markerWindowHashMap.get("markWindow"+id);
		markWindow.lonlat = new SuperMap.LonLat(currentPoi.geometry.x,currentPoi.geometry.y);
		markWindow.setContentHTML(html);
		markWindow.updatePosition();
		markWindow.show();
}

//放大标记点
function zoomInMarker(id){
	var currentPoi = poiHashMap.get(id);
	var x = currentPoi.geometry.x;
	var y = currentPoi.geometry.y;
	var point = new SuperMap.LonLat(parseFloat(x), parseFloat(y));
	map.setCenter(point,17);
}
//取消修改地图标记操作
function cancelMarker(id){
	var currentPoi = poiHashMap.get(id);
	var location = currentPoi.geometry;
	var name = currentPoi.properties.name;
	var remarks = currentPoi.properties.remarks;
	saveMarker(id);
}
//地图分享显示地图标记
function showMarker(group){
	if(poiMarkerHashMap == null){
		poiMarkerHashMap = new SuperMap.OSP.Core.HashMap();
	}
	if(group.pois.length == 0){
		return false;
	}
	for(var i=0; i<group.pois.length; i++){
		//将标注加入hashmap中
		var markerP = group.pois[i];
		var k = i+1;
		var id = "poiMaker"+k;
		var marker = new SuperMap.OSP.UI.POI(id);
		//移植旧的标注的属性到新的标注点，以便显示
		var scaledContent = new SuperMap.OSP.UI.ScaledContent();
		scaledContent.content = markerP.scaledContents.content;
		scaledContent.offset = new SuperMap.OSP.Core.Point2D();
		scaledContent.offset.x = markerP.scaledContents.offset.x;
		scaledContent.offset.y = markerP.scaledContents.offset.y;
		marker.scaledContents = scaledContent;
		
		var point2d = new SuperMap.OSP.Core.Point2D();
		point2d.x = markerP.position.x;
		point2d.y = markerP.position.y;
		marker.position = point2d;
		marker.title = markerP.title;
		var name = markerP.properties.name;
		var remarks = markerP.properties.remarks;
		marker.properties = {
			name:name,
			remarks:remarks
		};
		marker.addEventListerner("click", function(e){
			var point = eval('(' + e.get_info() + ')');
			var id = point.id;
			var nameP = point.properties.name;
			var remarksP = point.properties.remarks;
			var location = new SuperMap.OSP.Core.Point2D(e._x, e._y);
			markerPop(id, nameP, remarksP, location);
		});
		
		markerPop(marker.id, name, remarks, point2d);
		poiMarkerHashMap.put( marker.id, marker);
		poiMarkerGroup.addPOIs(marker);
	}
	poiManager.editPOIGroup(poiMarkerGroup);
	poiManager.refreshPOI();
}

//去除空格
function trim(str){
	return str.replace(/[ ]/g,""); 
}

/**
 * 卫星图与导航图切换div 鼠标移至上去
 */
function layerSwitcher_mseover(){
	jQuery("#span_layerSwitcher").css("background-color", "#DAF5FE");
}

/**
 * 卫星图与导航图切换div 鼠标移出
 */
function layerSwitcher_mseout(){
	jQuery("#span_layerSwitcher").css("background-color", "#F2F2F2");
}

/**
 * 切换卫星图与导航图
 */
function changeMapLayer(){
	var center = map.getCenter();
	var zoom = map.getZoom();
	var px = new SuperMap.Pixel(6.25, 6.25);
	//由导航图切换至影像图
	if(baseCloudLayer){		
		map.removeLayer(layer);
		map.removeControl(pan_zoombar);
		layer = new SuperMap.Layer.CloudLayer( {name:"影像图",resolutions:resolutions_dem}); 
		layer.url = imageUrl + "/FileService/image?x=${x}&y=${y}&z=${z}";		
		map.addLayers([layer]);
		map.setBaseLayer(layer);

		pan_zoombar.destroy();
		pan_zoombar = null;
		pan_zoombar = new SuperMap.Control.PanZoomBar({showSlider:true,levelsDesc:{levels:[2,6,9],imageSources:["theme/images/zoom_city.png","theme/images/zoom_province.png","theme/images/zoom_country.png"]}});
		map.addControl(pan_zoombar, px);

		map.setCenter(center, (zoom > 11 ? 11 : zoom));			

		$("#img_layerSwitcher").attr("src", "images/map/cloudlayer.jpg");
		$("#span_layerSwitcher").html("地图");
		baseCloudLayer = false;
	}
	//由影像图切至导航图
	else{
		map.removeLayer(layer);
		map.removeControl(pan_zoombar);
		layer = new SuperMap.Layer.CloudLayer1( {name:"导航地图",resolutions:resolutions_grid} );
		// layer.url = cachedUrl + "/FileService/image?map=${mapName}&type=${type}&x=${x}&y=${y}&z=${z}";
		map.addLayers([layer]);	
		map.setBaseLayer(layer);

		pan_zoombar.destroy();
		pan_zoombar = null;
		pan_zoombar = new SuperMap.Control.PanZoomBar({showSlider:true,levelsDesc:{levels:[7,11,1,14],imageSources:["theme/images/zoom_city.png","theme/images/zoom_province.png","theme/images/zoom_street.png","theme/images/zoom_country.png"]}});
		map.addControl(pan_zoombar, px);

		$("#img_layerSwitcher").attr("src", "images/map/imagelayer.jpg");
		$("#span_layerSwitcher").html("地形");
		baseCloudLayer = true;

		map.setCenter(center, zoom);
	}
}

var resolutions_dem = [
	156605.46875,  78302.734375,39151.3671875, 19575.68359375,
    9787.841796875,4893.9208984375, 2446.96044921875, 1223.48022460937,611.740112304687,
    305.870056152344,152.935028076172, 76.4675140380859, 38.233757019043
];
var resolutions_grid = [
	156543.033928041, 78271.5169640203, 39135.7584820102, 
    19567.8792410051, 9783.93962050254, 4891.96981025127, 2445.98490512563, 
    1222.99245256282, 611.496226281409, 305.748113140704, 152.874056570352, 
    76.4370282851761, 38.218514142588, 19.109257071294, 9.55462853564701, 
    4.77731426782351, 2.38865713391175, 1.19432856695588, 0.597164283477938
];