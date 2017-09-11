var map = null;
var featuresLayer = null;
var markerLayer = null;
var layer = null;
var framedCloudPopup;//弹出框
/**
 * 初始化地图
 */
function initMap() {
	layer = new SuperMap.Layer.CloudLayer();
	markerLayer = new SuperMap.Layer.Markers("maker");
	featuresLayer = new SuperMap.Layer.Vector();
	map = new SuperMap.Map("mapDiv", {
		controls : [ new SuperMap.Control.PanZoomBar(),
				new SuperMap.Control.Navigation({
					dragPanOptions : {
						enableKinetic : true
					}
				}) ],
		allOverlays : true
	});
	map.addLayer(layer);
	map.addLayer(featuresLayer);
	map.addLayer(markerLayer);
	map.setCenter(new SuperMap.LonLat(12958399.4681885, 4852082.44060595),
			11);
}
/**
 * 初始化地区选择控件
 * @author: mwang
 */
var divisionCode = '110000';
var parentCode = '';
var cityName = "北京市";
function initSMapSelWidget(){
	if (typeof(currentCityInfo) != "undefined" && currentCityInfo != null && typeof(currentCityInfo.cityName)!="") {
		cityName = currentCityInfo.cityName;
	}
	var smapCity = new SMap.CitySelect({
		id:"divSMapSel",//页面div
		defaultData:{text:cityName,value:"100100"}//默认显示的数据
	});
	
	smapCity.setAfterSelect(function(data){
		cityName = data.text;
		
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
			searchService.url = poiSearchUrl;
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
					layer_vector.addFeatures(lineFeature);
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
	setTimeout(cityChange,1000);
	var level = map.getZoom();
	var center = map.getCenter();
	var div = jQuery("#selectCityDiv");
	var previousRegionName = div.html();
	var geometry = new SuperMap.OSP.Utility.Geometry();
	geometry.point2Ds[0] = new SuperMap.OSP.Core.Point2D(center.lon,center.lat);

	var searchService = new SuperMap.OSP.Service.Search();
	searchService.url = poiSearchUrl;

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

/**
 * 页面加载完成后执行初始化操作
 */
$(function(){
	//初始化地图
	initMap();
	// currentCityInfo = {"provinceName":"四川省","cityName":"成都市","x":1.1584427298484452E7,"y":3588485.98588592,"level":11};
	if( typeof(currentCityInfo) !== "undefined" && currentCityInfo ) {
		map.setCenter(new SuperMap.LonLat(currentCityInfo.x, currentCityInfo.y), currentCityInfo.level);
		cityChange();
	}
	else {
		map.setCenter(new SuperMap.LonLat(11649793.33766, 3699297.96313), 15);
	}
	//地区选择控件
	initSMapSelWidget();
	new PCAS("province","city","area");
});