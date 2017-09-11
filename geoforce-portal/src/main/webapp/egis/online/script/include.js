
//获取地图名称
function getMapName() {
    var mapName = "quanguo";
    return mapName;
}

//获取比例尺
function getMapScales(){
    //var scales = [ 1 / 470000000, 1 / 235000000,1 / 117500000, 1 / 58750000, 1 / 29375000, 1 / 14687500, 1 / 7343750, 1 / 3671875, 1 / 1835937, 1 / 917968, 1 / 458984, 1 / 229492, 1 / 114746, 1 / 57373, 1 / 28686, 1 / 14343, 1 / 14343, 1 / 7171, 1 / 3585, 1 / 1792];
	var scales = [1 / 470000000, 1 / 235000000,1 / 117500000, 1 / 58750000, 1 / 29375000, 1 / 14687500, 1 / 7343750, 1 / 3671875, 1 / 1835937, 1 / 917968, 1 / 458984, 1 / 229492, 1 / 114746, 1 / 57373, 1 / 28686, 1 / 14343, 1 / 7171, 1 / 3585, 1 / 1792];
    return scales;
}

function getMaxScale() {
   var scale = 1 / 429687;
    return scale;
}

//获取地图范围
function getMapBounds(){
	//20037508.3427892
    var mapBounds = new SuperMap.Web.Core.Rectangle2D(-20037508.342789244,-20037508.3427891,20037508.3427892440,20037508.3427891);
	return mapBounds;
}

//获取初始化参数 iServer2008
function getMapStatus(){
    var mapBounds = getMapBounds();
	var mapStatus = new SuperMap.Web.iServerJava2.GetMapStatusResult();
	var sys = new SuperMap.Web.Core.CoordinateReferenceSystem(1, 1001745329);
	var referScale = 2.1787E-6;
	var referViewBounds = new SuperMap.Web.Core.Rectangle2D(1.306021962879583E7,4730293.757866534,1.3099371416559177E7,4769445.54562988);
	var referView = new SuperMap.Web.Core.Rectangle(0, 0, 256, 256);
	mapStatus.crs = sys;
	mapStatus.mapBounds = mapBounds;
	mapStatus.mapName = getMapName();
	mapStatus.referScale = referScale;
	mapStatus.referViewBounds = referViewBounds;
	mapStatus.referViewer = referView;
	return mapStatus; 
}

//获取初始化Map
//mapDiv-地图展示DIV的ID
function getMap(mapDiv){

    var mapStatus = getMapStatus();
    var scales = getMapScales();
    var layer = $create(SuperMap.OSP.Mapping.CloudLayer, {
        mapName: getMapName(),
        cachedUrl: "http://t0.supermapcloud.com/WebTool/image"
        // cachedUrl: "http://t0.supermapcloud.com/FileService/image"
    }, null, null, null);
    var map = $create(SuperMap.Web.Mapping.Map, null, {
        //mouseMove: addMousePosition,
		//staticPanelLoaded: addCopy
    }, null, $get(mapDiv));
    map.setLayers([layer]);
	map.zoomToLevel(11, new SuperMap.Web.Core.Point2D(12958399.4681885, 4852082.44060595));
    return map;
}
//获取服务端访问地址
function getServiceUrl() {
    var serviceUrl = "http://services.supermapcloud.com";
    return serviceUrl;
}

//获取量算服务访问地址
function getMeasureService(){
	// var xingyueUrl = "http://localhost/iserver/services/map-JiangSu/rest/maps/JiangSu";
	var measureService = "http://112.125.32.202:8081/iserver/services/map-cloud_base_data/rest/maps/quanguo";
	// var measureService = "http://services.supermapcloud.com/iserver/services/map-cloud_base_data/rest/maps/quanguo";
	return measureService;
}

//版权
function addCopy(div) {
    var copyDiv = document.createElement("div");
    copyDiv.innerHTML = "&copy;2010 <a href='http://www.supermap.com.cn' target='_blank'>SuperMap</a>";
    copyDiv.style.position = "absolute";
    copyDiv.style.bottom = "10px";
    copyDiv.style.right = "10px";
    copyDiv.style.zIndex = 99999;
    copyDiv.style.fontSize = "10px";
    div.appendChild(copyDiv);

}
