/**
 * 地图操作封装类
 */
//#############public options##################//
var curActive;//标记当前功能模块如定位、跟踪、轨迹等
var initLocateFlag = true;//点击及时定位 表示首次定位 满屏显示所有车辆 
var imagesPath = basePath+"/resources/images";//图片路径
var showPoi = true;//地图上是否显示POi点(门店)
//#############public optibaseons##################//

//#############public function##################//
function getCurActive(){
	return curActive;
}
function setCurActive(active){
	curActive = active;
}
function getInitLocateFlag(){
	return initLocateFlag;
}
function setInitLocateFlag(flag){
	initLocateFlag = flag;
}
/**
 * 通过地图类型获取地图api的操作对象 如supermap.js里的supermap对象
 * @return
 */
function loadMapApi(){
	//ps_mapType in taglibs.jsp
//	ps_mapType = "supermap";
	if(ps_mapType == "supermap"){//超图
		mapApiObj = supermap;
		$("#mapType").html('云地图 <span class="caret"></span>');
	}else if(ps_mapType == "baidu"){//百度
		mapApiObj = baidu;
		$("#mapType").html('百度地图 <span class="caret"></span>');
	}else if(ps_mapType == "google"){//谷歌
		mapApiObj = gg;
		$("#mapType").html('谷歌地图 <span class="caret"></span>');
	}else{
		mapApiObj = supermap;
		$("#mapType").html('云地图 <span class="caret"></span>');
	}
	return mapApiObj;
}
/**
 * 选择城市列表
 * @return
 */
function initSMapSelWidget(){
	var smapCity = new SMap.CitySelect({
		id:"divSMapSel",//页面div
		defaultData:{text:ps_cityName,value:ps_cityCode}//默认显示的数据
	});
	smapCity.setAfterSelect(function(data){
		ps_cityName = data.text;
		//加载分类搜索
		//loadTopTypes(data.level);
//		var point2d = new SuperMap.OSP.Core.Point2D(data.x, data.y);
		var level;
		if(data.level == 1
		&& (data.text == '北京市' || data.text == '天津市' || data.text == '重庆市' || data.text == '上海市' || data.text == '东营市' || data.text == '西安市')){
			level = 11;
		}
		else if (data.level == 2) {
			level = 10;
		}else if (data.level == -1) {
			level = 4;
		}else{
			level = 6;
		}
		//in tablibs.jsp
		//墨卡托转经纬度
		var lngLat = meterXY2GeoLoc(data.x,data.y,7);
		ps_lng = lngLat.lngX;
		ps_lat = lngLat.latY;
		var mapApi = loadMapApi();
		mapApi.mapCenter();
	});
}
/**
 * 将所有draw画点线面方法失效
 * @return
 */
function deactiveMapDraw(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
}
/**
 * 清空地图上所有元素
 * @return
 */
function clearLayerFeatures(){
	var mapApi = loadMapApi();
	mapApi.clearAllFeatures();
}
/**
 * 地图div窗口大小改变后重新刷新地图
 * @return
 */
function refreshMap(){
	var mapApi = loadMapApi();
	mapApi.refreshMap();
}
/**
 * 地图所有覆盖物一屏显示
 * @return
 */
function map_featureCenter(){
	var mapApi = loadMapApi();
	mapApi.featureCenter();
}
/**
 * 生成车辆弹出框信息html
 * @param obj
 * @return
 */
function getCarOpenHtml(obj){
	var carId = obj.carId || '';
	var license = obj.license || '';
	var gpstime = obj.gpsTime || '';
	var alarmTime = obj.alarmTime || '';
	var drivers = obj.drivers || '';
	var addr = obj.addr || '';
	var speed = obj.speed || 0;
	var direction = obj.direction || '';
	var directionStr = obj.directionStr || '';
	var alarmType = obj.alarmType || '';
	var zfTurn = obj.zfTurn || '';
	var mile = obj.mile || '';
	var alarmStr = obj.alarmStr || '';
	var picpath = obj.picpath || '';
	
	var showAddr = addr;
	if(addr.length > 13){
		showAddr = addr.substring(0,13)+"..";
	}
	var showLicense = license;
	if(license.length > 7){
		showLicense = license.substring(0,7)+"..";
	}
	var showDrivers = drivers;
	if(drivers.length > 13){
		showDrivers = drivers.substring(0, 13)+"..";
	}
	var showAlarm = alarmStr;
	if(alarmStr.length > 13){
		showAlarm = alarmStr.substring(0,13)+"..";
	}
	var zfTurnStr;
	if(zfTurn == "1"){
		zfTurnStr = mapSuportLang.zhengzhuan;//正转
	}else if(zfTurn == "2"){
		zfTurnStr = mapSuportLang.fanzhuan;//反正
	}
	var curActive = getCurActive();//in mapSupport.js获取当前功能的英文名
	//弹出框头div
	var title = '<div style="height:30px;vertical-align:middle;font-size:14px;"><span title="'+license+'"><b>'+showLicense+'</b></span>';
	if(curActive == "history"){
		//title += '<a onclick="" title="'+mapSuportLang.gjlcTip+'" style="font-size:12px;margin-right: 35px">'+mapSuportLang.gjlc+'</a>';//将历史轨迹另存为线路偏移报警
//		ht+=5
	}
	title += '<hr style="width:100%;border:1px solid #be1a1a;margin-top: 5px;margin-bottom: 5px;">';
	//中间内容div
	var contentHt = 45;
	var content = '';
	if(picpath){
		content += '<span style="border:1px solid #ddd; float:left;">';
		content += '<img style="display:block;width:80px;height:100px" src="'+picpath+'" onmouseover="showCarImg(this,\'left\')" />';
		content += '</span>';
		content += '<div style="margin-left:85px;margin-top:7px;">';
	}
	
	if(gpstime){		
		content += '<p><span style="color:#0577E5">定位时间：</span><span>'+gpstime+'</span></p>';//GPS时间
		contentHt += 18;
	}
	if(alarmTime){
		content += '<p><span style="color:#0577E5">报警时间：</span><span>'+alarmTime+'</span></p>';//报警时间
		contentHt += 18;
	}
	if(alarmType){
		content += '<p><span style="color:#0577E5">报警类型：</span><span>'+alarmType+'</span></p>';//报警类型
		contentHt += 18;
	}
	if(mile){
		content += '<p><span style="color:#0577E5">行驶里程：</span><span>'+mile+' km</span></p>';//行驶里程
		contentHt += 18;
	}
	content += '<p><span style="color:#0577E5">行驶速度：</span><span>'+speed+'km/h </span></p>'//行驶速度 行驶方向
			+'<p><span style="color:#0577E5">行驶方向：</span><span>'+directionStr+'</span></p>';
	contentHt += 30;
	if(drivers){
		content += '<p><span style="color:#0577E5">司机：</span><span title="'+drivers+'">'+showDrivers+'</span></p>';
		contentHt += 18;
	}
	if(zfTurnStr){
		content += '<p><span style="color:#0577E5">正反转：</span><span>'+zfTurnStr+'</span></p>';//业务信息
		contentHt += 18;
	}
	if(alarmStr){
		content += '<p><span style="color:#0577E5">报警信息：</span><span title="'+alarmStr+'">'+showAlarm+'</span></p>';//终端状态
		contentHt += 20;
	}
	if(addr){
		content += '<p><span style="color:#0577E5">位置描述：</span><span title="'+addr+'">'+showAddr+'</span></p>';//位置信息
		contentHt += 18;
	}
	if(picpath)
		content += '</div>';
	
	content = '<div style="width:100%;height:'+contentHt+'px;overflow:auto;border: 0px #3473B7 solid;">'+content+'</div>';
	//弹出框尾部div
	var linkHtml = '';
	if(curActive){
		linkHtml = '<div>';
		
		if(curActive == "locate"){//定位
			linkHtml += '<a style="color:black;cursor: default;">实时定位</a>|<a onclick=carTrack("'+carId+'")>锁定跟踪</a>|<a onclick=historyShowTime("'+carId+'")>历史轨迹</a>';
		}else if(curActive == "track"){
			linkHtml += '<a onclick=carLocate("'+carId+'")>实时定位</a>|<a style="color:black;cursor: default;">锁定跟踪</a>|<a onclick=historyShowTime("'+carId+'")>历史轨迹</a>';
		}else if(curActive == "history"){
			linkHtml += '<a onclick=carLocate("'+carId+'")>实时定位</a>|<a onclick=carTrack("'+carId+'")>锁定跟踪</a>|<a style="color:black;cursor: default;">历史轨迹</a>';
		}else{
			linkHtml += '<a onclick=carLocate("'+carId+'")>实时定位</a>|<a onclick=carTrack("'+carId+'")>锁定跟踪</a>|<a onclick=historyShowTime("'+carId+'")>历史轨迹</a>';
		}
		//及时定位  锁定跟踪 历史轨迹
		linkHtml += '</div>';
		contentHt += 30;
	}
	var htmlInfo = '';
	contentHt += 30;
	if(picpath){
		htmlInfo = '<div class="PopUpBox" style="width:335px;height:'+contentHt+'px;overflow:hidden;">'+title+content+linkHtml+'</div>';
	}else{
		htmlInfo = '<div class="PopUpBox" style="width:265px;height:'+contentHt+'px;overflow:hidden;">'+title+content+linkHtml+'</div>';
	}
	
	return htmlInfo;
}
/**
 * 获取人员弹出框信息
 * @param obj
 * @return
 */
function getPersonOpenHtml(obj){
	var personId = obj.personId || '';
	var personName = obj.personName || '';
	var termCode = obj.termCode;
	var mobile = obj.mobile || '';
	var gpstime = obj.gpsTime || '';
	var addr = obj.addr || '';
	var directionStr = obj.directionStr || '';
	var picId = obj.picId || '';
	var signInfo = obj.signInfo || '';
	
	var showAddr = addr;
	if(addr.length > 13){
		showAddr = addr.substring(0,13)+"..";
	}
	var showName = personName;
	if(personName.length > 7){
		showName = personName.substring(0,7)+"..";
	}
	var curActive = getCurActive();//in mapSupport.js获取当前功能的英文名
	//弹出框头div
	var title = '<div style="height:30px;vertical-align:middle;font-size:14px;"><span title="'+personName+'"><b>'+showName+'</b></span>';
	
	title += '<hr style="width:100%;border:1px solid #be1a1a;margin-top: 5px;margin-bottom: 5px;">';
	//中间内容div
	var contentHt = 45;
	var content = '';
//	if(picId){
//		content += '<span style="border:1px solid #ddd; float:left;">';
//		content += '<img style="display:block;width:80px;height:100px" src="'+picId+'" onmouseover="showCarImg(this,\'left\')" />';
//		content += '</span>';
//		content += '<div style="margin-left:85px;margin-top:7px;">';
//	}
	
	if(gpstime){		
		content += '<p><span style="color:#0577E5">定位时间：</span><span>'+gpstime+'</span></p>';//GPS时间
		contentHt += 18;
	}
	if(mobile){
		content += '<p><span style="color:#0577E5">手机号：</span><span>'+mobile+'</span></p>';//手机号
		contentHt += 18;
	}
	if(addr){
		content += '<p><span style="color:#0577E5">位置描述：</span><span title="'+addr+'">'+showAddr+'</span></p>';//位置信息
		contentHt += 18;
	}
	if(signInfo){
		content += '<p><span style="color:#0577E5">最近工作：</span><span>'+signInfo+'</span></p>';
		contentHt += 30;
	}
	
	content = '<div style="width:100%;height:'+contentHt+'px;overflow:auto;border: 0px #3473B7 solid;">'+content+'</div>';
	//弹出框尾部div
	var linkHtml = '';
	linkHtml = '<div>';
	linkHtml += '<a  onclick=personLocate("'+personId+'") >重新定位</a>|<a onclick=sendMsgOpen("'+personId+'")>发送通知</a>';
	
	linkHtml += '</div>';
	contentHt += 30;
	
	var htmlInfo = '';
	contentHt += 30;
//	if(picpath){
//		htmlInfo = '<div class="PopUpBox" style="width:335px;height:'+contentHt+'px;overflow:hidden;">'+title+content+linkHtml+'</div>';
//	}else{
		htmlInfo = '<div class="PopUpBox" style="width:265px;height:'+contentHt+'px;overflow:hidden;">'+title+content+linkHtml+'</div>';
//	}
	
	return htmlInfo;
}
/**
 * 生成poi点 弹出框html
 * @return
 */
function getPoiOpenHtml(obj){
	var storeId = obj.id;
	var name = obj.name || '';
	var shopkeeperName = obj.shopkeeperName || '';//店长
	var shopkeeperPhone = obj.shopkeeperPhone || '';//店长手机
	var addr = obj.address || '';
	var pics = obj.pics || null;
	var deptName = obj.deptName || '';
	var typeName = obj.typeName ||'';
	var showAddr = addr;
	if(addr.length > 15){
		showAddr = addr.substring(0,15)+"..";
	}
	
	var ht = 40;
	var markInfo = '<div style="height:30px;vertical-align:middle;font-size:14px;"><span title="'+name+'"><b>'+name+'</b></span>';
	markInfo += '<hr style="width:100%;border:1px solid #be1a1a;margin-top: 5px;margin-bottom: 5px;">';
	
	markInfo += '<div style="width:100%;overflow:auto;border: 0px #3473B7 solid;"></div>';

	if(typeName){
		markInfo += '<p><span style="color:#0577E5">门店类型：</span>&nbsp;<span title="'+typeName+'">'+typeName+'</span></p>';
		ht += 18;
	}
	if(deptName){
		markInfo += '<p><span style="color:#0577E5">所在部门：</span>&nbsp;<span title="'+deptName+'">'+deptName+'</span></p>';
		ht += 18;
	}
	if(shopkeeperName){
		markInfo += '<p><span style="color:#0577E5">店长：</span>&nbsp;<span title="店长'+shopkeeperName+'">'+shopkeeperName+'</span></p>';
		ht += 18;
	}
	if(shopkeeperPhone){
		markInfo += '<p><span style="color:#0577E5">手机号：</span><span title="店长手机号'+shopkeeperName+'">'+shopkeeperPhone+'</span></p>';
		ht += 18;
	}
	if(addr){
		markInfo += '<p><span style="color:#0577E5"> 地址：</span>&nbsp;<span title="'+addr+'">'+showAddr+'</span></p>';
		ht += 18;
	}

	//门店图片div
	if(pics && pics.length > 0){
		markInfo += '<div id="personStoreImg" style="width:265px;height:80px;overflow-x:auto;">';
		var imgHtml = '';
		for(var i=0,pic;pic=pics[i];i++){
			if(pic.url){
				markInfo += '<img onclick="storeImgShow('+i+')" src="'+pic.url+'" width="60px" height="60px" style="margin-right:10px;cursor: pointer;"/>';
			}
		}
		markInfo += '</div>';
		ht += 80;
	}
	markInfo = '<div class="PopUpBox" style="width:265px;height:'+ht+'px">'+markInfo+'</div>';
	return markInfo;
}
/**
 * 首页门店图片 大图显示
 * @return
 */
function storeImgShow(startIndex){
//	$("#storeImg").modal('show');
	if(!startIndex)
		startIndex = 0;
	layer.photos({
		page: { //直接获取页面指定区域的图片，他与上述异步不可共存，只能择用其一。
	        parent: '#personStoreImg',  //图片的父元素选择器，如'#imsbox',
	        start: startIndex, //初始显示的图片序号，默认0
	        title: '' //相册标题
	    }
	});
}
///**
// * 根据门店id获取门店图片
// * @param storeId
// * @return
// */
//function getStoreImg(storeId){
//	$.ajax({
//		type : "POST",
//		async : true,
//		url : ctx+"/com/supermap/picList",
//		data: "id="+storeId+"&type=1",//1在pic图片表中表示门店
//		dataType : "json",
//		success : function(msg) {
//			//权限及sesion校验
//			if(msg == null || msg == ""){
//				return;
//			}
//			
//			var data = msg;
//			if(!data){
//				return;
//			}
//			var imgHtml = '';
//			for(var i=0,pic;pic = data[i]; i++){
//				if(pic.url){
//					$("img_"+storeId).append('<img src = "http://localhost:8080'+pic.url+'" width="40px" height="40px"/>');
//				}
//					
//			}
//			
//		},
//		error : function() {
//        }
//	});
//}
/**
 * 搜索poi点 弹出框框
 */
function getSearchPoiOpenHtml(obj){
//	var name = obj.name==null?'':obj.name;
//	var createTime = obj.createTime==null?'':obj.createTime;
//	var type = obj.type==null?'':obj.type;
//	var describe = obj.describe==null?'':obj.describe;
//	var addr = obj.addr == null?'':obj.addr;
	//poi搜索
	var addrName = obj.addrName==null?'':obj.addrName;
	var addrDetail = obj.addrDetail == null?'':obj.addrDetail;
	var url = ctx+"/images/poi/poi_blue.gif";
	//线路搜索
	var routeName = obj.routeName==null?'':obj.routeName;
	var detail = obj.detail == null?'':obj.detail;
	var isSearch = true;
	var titleName = "";
	if(addrName != null && addrName != "") {
		titleName = mapSuportLang.ssdxx;
	}
	if(routeName != null && routeName != ""){
		titleName = mapSuportLang.tjdxx;
	}
	if(addrName.length > 15){
		addrName = addrName.substring(0,15)+"..";
	}
	var ht = 40;
	var markInfo = '<div style="height:40px;vertical-align:middle;font-size:14px;"><div title="'+name+mapSuportLang.poixx+'"><b>'+titleName+'</b></div>'
				+'<hr style="width:100%;">';
	if(routeName){		
		markInfo += '<p>'+mapSuportLang.tjd+'：<span>'+routeName+'</span></p>';
		ht += 18;
	}
	if(detail){	
		isSearch = false;
		markInfo += '<p>'+mapSuportLang.detail+'：<span>'+detail+'</span></p>';
		ht += 18;
	}
	if(addrName){		
		markInfo += '<p>'+mapSuportLang.ssd+'：<span>'+addrName+'</span></p>';
		ht += 18;
	}
	if(addrDetail == "null" || addrDetail == "" || typeof(addrDetail) == "undefined") {
		addrDetail = mapSuportLang.wxxdz;
		if(isSearch){
		}
	}else{
		markInfo += '<p>'+mapSuportLang.xxdz+'：<span>'+addrDetail+'</span></p>';
		ht += 18;
	}
	markInfo = '<div class="PopUpBox" style="width:220px;height:'+ht+'px">'+markInfo+'</div>';
	return markInfo;
}
/**
 * 根据车辆位置数据在地图上批量打点
 * @param data
 * @return
 */
function map_addMarks(data){
	var mapApi = loadMapApi();
	var markers = [];
	var obj = null,marker = null;
	for(var index in data){
		obj = data[index];
		if(obj.poiName){//生成poi类型的点
			marker = mapApi.addPoiMarker(obj);
		}if(obj.personName){//生成人员点
			marker = mapApi.addPersonMarker(obj);
		}else{			
			marker = mapApi.addCarMarker(obj);
		}
		markers.push(marker);
	}
	if(markers.length == 0){
		return;
	}
	mapApi.addFeatures(markers);
}
/**
 * 地图上打poi兴趣点
 * @param data poi数据
 * @param type 生成poi点的类型 0表示地图初始化显示的门店点 1表示兴趣点管理里的点
 * @return
 */
function map_addPois(data,type){
	var mapApi = loadMapApi();
	
	var markers = [];
	var obj = null,marker = null;
	for(var i=0,obj;obj=data[i]; i++){
		marker = mapApi.addPoiMarker(obj,type);
		markers.push(marker);
	}
	if(markers.length == 0){
		return;
	}
	mapApi.addFeatures(markers);
}
/**
 * 地图上打poi搜索点
 * @param data poi数据
 * @return
 */
function map_addSearchPois(data){
	var mapApi = loadMapApi();
	var markers = [];
	var obj = null,marker = null;
	for(var index in data){
		obj = data[index];
		marker = mapApi.addMapSearchPoiMarker(obj);
		markers.push(marker);
	}
	if(markers.length == 0){
		return;
	}
	mapApi.addFeatures(markers);
}
/**
 * 根据id查出地图已有marker 聚焦到该点
 * @param id
 * @return
 */
function rePanToMark(obj){
	var mapApi = loadMapApi();
	mapApi.markerCenter(obj);
}
/**
 * 地图操作初始化方法 用于调用各个图商的坐标转换
 * @param data 数据
 * @param active 操作标识
 * @return
 */
function map_initActive(data,active){
	if(data == null || data == ""){
		$.messager.alert(commonLang.tooltip,mapSuportLang.wwzxx,"warning");
		return;
	}
	if(active == "dw" || active == "gz"){//定位 跟踪
		var tmp = new Array();
		var obj = null;
		for(var index in data){
			obj = data[index];
			if(obj.status != 3){//离线车辆不显示
				tmp.push(obj);
			}
		}
		data = tmp;
		
	}
	
	if(data.length == 0){
		$.messager.alert(commonLang.tooltip,mapSuportLang.wwzxx,"warning");
		return;
	}
//	$("#mapLoading").show();
	//in translate.js 标准坐标转换成图商坐标
	//暂时后台进行坐标转换
//	tranGPS.tranXy(data,active,mapCallback);
	mapCallback(data,active);
}

/**
 * 坐标转换map_initActive方法的回调函数
 * @return
 */
function mapCallback(data,active){
//	$("#mapLoading").hide();
	// in translate.js
//	var data = tranGPS.infoDatas;//转换坐标后的json数据
//	var active = tranGPS.type;//当前操作
	if(active == "dw"){//定位
		map_Locate(data);
	}else if(active == "gz"){//跟踪
		map_Track(data);
	}else if(active == "ls"){//历史轨迹
		map_History(data);
	}
}

//#############public function##################//
/**
 * 人员定位
 */
function person_Locate(data){
	if(data == null || data == ""){
		return;
	}
	addGpsDataList(data);//地图下方列表中显示gps数据
	clearLayerFeatures();
	var markerInfos = [];
	for(var i=0,obj;obj=data[i];i++){
		if(obj.status != 3){
			markerInfos.push(obj);
		}
	}
	if(markerInfos.length == 0){
		return;
	}
	map_addMarks(markerInfos);//地图打点
	
	//第一次定位一屏显示所有车辆
	if(getInitLocateFlag()){
		setInitLocateFlag(false);
		map_featureCenter();
	}
}
/**
 * 地图初始化
 * @return
 */
function initMap(){
	var mapApiObj = loadMapApi();
	mapApiObj.init();
}
/**
 * 及时定位地图操作
 */
function map_Locate(data){
	if(data == null || data == ""){
		return;
	}
	addGpsDataList(data);//地图下方列表中显示gps数据 in mapList.js
	
	clearLayerFeatures();
	var markerInfos = [];
	for(var i=0,obj;obj=data[i];i++){
		if(obj.status != 3){
			markerInfos.push(obj);
		}
	}
	if(markerInfos.length == 0){
		return;
	}
	map_addMarks(markerInfos);//车辆打点
	
	//第一次定位一屏显示所有车辆
	if(getInitLocateFlag()){
		setInitLocateFlag(false);
		map_featureCenter();
	}
	
}
/**
 * 锁定跟踪地图操作
 * @param data
 * @return
 */
function map_Track(data){
	if(data == null || data == ""){
		return;
	}
	addGpsDataList(data);//地图下方列表中显示gps数据 in mapList.js
	var infoObj = data[0];
	if(infoObj.status == 3){
		return;
	}
	var mapApi = loadMapApi();
	
	var marker = mapApi.addCarMarker(infoObj);
	
	var line = mapApi.addLine([marker],"carLine_"+infoObj.carId);
	//如果最新点行驶出地图边界，则将点重新聚集到地图中心
	mapApi.panToPoint(marker);
	
	mapApi.clearAllFeatures();//清空已有
	mapApi.addFeatures([marker,line]);
}
/**
 * 历史轨迹地图操作
 * @return
 */
function map_History(data){
	if(data == null || data.length == 0){
		return;
	}
	/**所有历史轨迹参数在historyControl.js中*/
	historyData = data;
	if(historyData == null || historyData.length == 0){
		return;
	}
	
	var list = historyData.list;
	gpsCount = list.length-1;//以数据节点为总数量
	playSpeed = 1;
	gpsIndex = 0;
	hisHasPoints = new Array();
	startMile = (list[0].mile==null || list[0].mile == "")?0:list[0].mile;
	his_drawAllPoint();
	his_resetControll();//in historyControl.js
	his_controll();//in historyControl.js
}
/**
 * 历史轨迹初始化在地图画所有轨迹线
 * @return
 */
function his_drawAllPoint(){
	var mapApi = loadMapApi();
	//清空图层
	mapApi.clearAllFeatures();
	mapApi.infoWinPoint = null;//清空弹出框marker对象
	var list = historyData.list;
	var markers = [],marker = null;
	var startInfo = list[0];
	var endInfo = list[list.length-1];
	//获取起点和终点
	var seMarkers = mapApi.addHisStartEndMark([startInfo,endInfo]);
	//车信息打点
	var carInfoObj = startInfo;
	carInfoObj.carId = historyData.carId;
	carInfoObj.license = historyData.license;
	var carMarker = mapApi.addCarMarker(carInfoObj);
	
	var items = list.concat();
	setTimeout(function(){
        var start = +new Date(); // 返回当前时间戳
        do {
        	var infoObj = items.shift();
        	if(infoObj){
//        		infoObj.license = "";
        		marker = mapApi.addCarMarker(infoObj);
        		markers.push(marker);
        	}
        } while (items.length > 0 && (+new Date() - start < 50));
        
        if (items.length > 0){
          setTimeout(arguments.callee, 25);
        }else{
        	var historyLine = mapApi.addLine(markers,0);
        	mapApi.addFeatures([historyLine,seMarkers[0],seMarkers[1],carMarker]);
        	//计算边界 显示全部
        	map_featureCenter();
        }
    }, 25);
}
/**
 * 历史轨迹播放过程中绘制当前轨迹线
 * @return
 */
function his_currentLine(){
	var mapApi = loadMapApi();
	//清空图层
	mapApi.clearAllFeatures();
	
	if(gpsIndex >= gpsCount){
		gpsIndex = gpsCount;
	}
	var list = historyData.list;
	var curGpsInfo = list[gpsIndex];
	if(curGpsInfo == null){
		return;
	}
	//设置当前时间和当前里程
	$("#historyTime").val(curGpsInfo.gpsTimeStr);
	var mile = curGpsInfo.mile;
	mile = (mile==null || mile == "")?0:mile;
	mile = (mile-startMile);
	$("#historyMile").val(mile.toFixed(3));
	
	curGpsInfo.carId = historyData.carId;
	curGpsInfo.license = historyData.license;
	
	var carMarker = mapApi.addCarMarker(curGpsInfo);
	
	var position = hisHasPoints.length - 1;
	var points = [];
	if(gpsIndex > position){
		for(var i=position;i<=gpsIndex;i++){
			if(i == position){
				continue;
			}
			var gpsObj = list[i];
//			gpsObj.license = "";
			var tmpMarker = mapApi.addCarMarker(gpsObj);
			hisHasPoints.push(tmpMarker);
		}
	}else if(gpsIndex < position){
		hisHasPoints = hisHasPoints.slice(0,gpsIndex);
		hisHasPoints.push(carMarker);
	}
	var historyLine = mapApi.addLine(hisHasPoints,0);
	
	//获取起点和终点
	var seMarkers = mapApi.addHisStartEndMark([list[0],list[list.length-1]]);
	
	//如果最新点行驶出地图边界，则将点重新聚集到地图中心
	mapApi.panToPoint(carMarker);
	
	
	mapApi.addFeatures([historyLine,seMarkers[0],seMarkers[1],carMarker]);
}
/**
 * 绘制圆
 * @return
 */
function map_drawCircle(){
	var mapApi = loadMapApi();
	//清空图层
	mapApi.clearAllFeatures();
	mapApi.drawDeactive();
	mapApi.drawCalculate(false);//绘制圆形时是否计算面积
	//激活绘制圆形
	mapApi.drawCircleActive();
}
/**
 * 绘制圆形完成后的回调函数 进行后续业务处理
 * xys格式[{x:**,y:**}]圆形的经纬度点数组
 * @return
 */
function map_drawCircleCompleted(xys){
	var mapApi = loadMapApi();
	var curActive = getCurActive();//获取当前操作类型
	var xyStrs = [];
	for(var i=0,xy;xy = xys[i]; i++){
		xyStrs.push(xy.x+","+xy.y);
	}
	
	//regionSearch表示区域查车 
	if(curActive == "regionSearch"){
		regionSearch_callBack(xyStrs.join(";"),ps_mapType);//in monitor.js 访问后台查询车辆
		mapApi.drawDeactive();
	}
}
/**
 * 绘制矩形
 * @return
 */
function map_drawRectangle(){
	var mapApi = loadMapApi();
	//清空图层
	mapApi.clearAllFeatures();
	mapApi.drawDeactive();
	mapApi.drawCalculate(false);//绘制圆形时是否计算面积
	//激活绘制矩形
	mapApi.drawRectangleActive();
}
/**
 * 绘制矩形完成后的回调函数 进行后续业务处理
 * xys格式[{x:**,y:**}]矩形的经纬度点数组
 * @return
 */
function map_drawRectangleCompleted(xys){
	var mapApi = loadMapApi();
	var curActive = getCurActive();//获取当前操作类型
	var xyStrs = [];
	for(var i=0,xy;xy = xys[i]; i++){
		xyStrs.push(xy.x+","+xy.y);
	}
	
	//regionSearch表示区域查车 
	if(curActive == "regionSearch"){
		regionSearch_callBack(xyStrs.join(";"),ps_mapType);//in monitor.js 访问后台查询车辆
		mapApi.drawDeactive();
	}
}
/**
 * 绘制多边形
 * @return
 */
function map_drawPolygon(){
	var mapApi = loadMapApi();
	//清空图层
//	mapApi.clearAllFeatures();
	mapApi.drawDeactive();
	mapApi.drawCalculate(false);//绘制多边形时是否计算面积
	//激活绘制多边形
	mapApi.drawPolygonActive();
}
/**
 * 关闭绘制
 * @return
 */
function map_drawPolygonClose(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
}
/**
 * 绘制多边形完成后的回调函数 进行后续业务处理
 * xys格式[{x:**,y:**}]多边形的经纬度点数组
 * @return
 */
function map_drawPolygonCompleted(xys){
	var mapApi = loadMapApi();
	var curActive = getCurActive();//获取当前操作类型
	var xyStrs = [],xy = null;
	for(var i=0;i<xys.length;i++){
		xy = xys[i];
		xyStrs.push(xy.x+","+xy.y);
	}
	
	//regionSearch表示区域查车 alarmFence绘制围栏
	if(curActive == "regionSearch"){
		regionSearch_callBack(xyStrs.join(";"),ps_mapType);//in monitor.js 访问后台查询车辆
		mapApi.drawDeactive();
	}else if(curActive == "alarmFence"){
		//in fenceEdit.jsp 围栏经纬度返回
		addFence(xyStrs.join(";"));
		mapApi.drawDeactive();
	}else if(curActive == "mapPolygon"){
		//in PolygonEdit.jsp 围栏经纬度返回
		addPolygon(xyStrs.join(";"));
	}
}
/**
 * 编辑覆盖物
 * @return
 */
function map_editFeature(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
	mapApi.drawCalculate(false);//绘制多边形时是否计算面积
	//激活编辑多边形
	mapApi.editFeatureActive();
}
/**
 * 编辑覆盖物成功后的回调函数
 * @param xys 经纬度数组[{x:**,y:**}]
 * @return
 */
function map_editFeatureCompleted(xys,name){
	var curActive = getCurActive();//获取当前操作类型
	var xyStrs = [],xy = null;
	for(var i in xys){
		xy = xys[i];
		xyStrs.push(xy.x+","+xy.y);
	}
	if(curActive == "alarmFence"){
		//in fenceEdit.jsp 围栏经纬度返回
		addFence(xyStrs.join(";"),name);
	}else if(curActive == "mapPolygon"){
		//in PolygonEdit.jsp 围栏经纬度返回
		addPolygon(xyStrs.join(";"));
	}else if(curActive == "alarmLine"){//线路
		//in lineEdit.jsp 添加线路
		addLine(xyStrs.join(";"));
	}else if(curActive == "mapLine"){//地图线路
		//in lineEdit.jsp 添加线路
		addLine(xyStrs.join(";"));
	}
}
/**
 * 绘制线路激活
 * @return
 */
function map_drawLine(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
	mapApi.drawCalculate(false);//绘制线路时是否计算距离
	//激活绘制折线
	mapApi.drawLineActive();
}
/**
 * 绘制线路完成后回调函数
 * @param xys
 * @return
 */
function map_drawLineCompleted(xys){
	var mapApi = loadMapApi();
	var curActive = getCurActive();//获取当前操作类型
	
	if(curActive == "alarmLine"){//线路偏移报警
		var xyStrs = [],xy = null;
		for(var i in xys){
			xy = xys[i];
			xyStrs.push(xy.x+","+xy.y);
		}
		//in lineEdit.jsp 添加线路
		addLine(xyStrs.join(";"));
	}else if(curActive == "mapLine"){//地图线路
		var xyStrs = [],xy = null;
		for(var i in xys){
			xy = xys[i];
			xyStrs.push(xy.x+","+xy.y);
		}
		//in lineEdit.jsp 添加线路
		addLine(xyStrs.join(";"));
	}else{//测距
		mapApi.drawDeactive();
	}
}
/**
 * 绘制点激活
 * @return
 */
function map_drawPoint(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
	//激活绘制点
	mapApi.drawPointActive();
}
/**
 * 绘制点完成后的回调函数
 * @param xyObj {x:***,y:**}
 * @return
 */
function map_drawPointCompleted(xyObj){
	var mapApi = loadMapApi();
	var curActive = getCurActive();//获取当前操作类型
	if(curActive == "poiManager"){//兴趣点管理
		mapApi.drawDeactive();
		setLnglat(xyObj.x,xyObj.y);//in poiEdit.jsp
	}else if(curActive == "shortDistance"){//最近警力查询
		mapApi.drawDeactive();
		shortDistance_callback(xyObj.x+","+xyObj.y,ps_mapType);//in monitor.js
	}else if("storeManager"){
		mapApi.drawDeactive();
		setLngLat(xyObj.x,xyObj.y);//in poiEdit.jsp
	}
}
/**
 * 报警信息点在地图上
 * @param alarmObjs
 * @return
 */
function map_alarmPoints(alarmObjs){
	if(alarmObjs == null || alarmObjs.length == 0){
		return;
	}
	var mapApi = loadMapApi();
	map_addMarks(alarmObjs);
	map_featureCenter();
}
/**
 * 根据经纬度字符串在地图上添加区域
 * @param obj 对象 
 * 属性 xyStr 格式 x,y;x,y;x,y... 
 * name 区域名称
 * @return
 */
function map_addPolygon(polygonObj){
	if(!polygonObj)
		return;
	if(polygonObj.xyStr == null || polygonObj.xyStr == ""){
		return;
	}
	var xyObjs = new Array();
	try{
		var xyArray = polygonObj.xyStr.split(";");
		for(var i=0;i<xyArray.length;i++){
			var tmp = xyArray[i].split(",");
			var xyObj = new Object();
			xyObj.x = tmp[0];
			xyObj.y = tmp[1];
			xyObjs.push(xyObj);
		}
	}catch(e){};
	if(xyObjs.length == 0){
		return;
	}
	var mapApi = loadMapApi();
	//在地图上添加围栏
	mapApi.addPolygon(xyObjs,polygonObj.name);
	map_featureCenter();
}
/**
 * 在地图上添加线路
 * @param xyStr
 * @return
 */
function map_addLine(xyStr){
	if(xyStr == null || xyStr == ""){
		return;
	}
	var pts = [];
	var mapApi = loadMapApi();
	try{
		var xyArray = xyStr.split(";");
		for(var i in xyArray){
			var tmp = xyArray[i].split(",");
			var infoObj = new Object();
			infoObj.ctLng = tmp[0];
			infoObj.bdLng = tmp[0];
			infoObj.ctLat = tmp[1];
			infoObj.bdLat = tmp[1];
			var marker = mapApi.addCarMarker(infoObj);
			pts.push(marker);
		}
	}catch(e){};
	if(pts.length == 0){
		return;
	}
	
	//在地图上添加线路
	var line = mapApi.addLine(pts,"alarmLine11");
	mapApi.addFeatures([line]);
	map_featureCenter();
}
/**
 * 搜索poi对象信息
 * @param infoObj
 * @return
 */
function map_searchPoiDetail(infoObj){
	if(infoObj == null || infoObj == ""){
		return;
	}
	var mapApi = loadMapApi();	
	var marker = mapApi.addMapSearchPoiMarker(infoObj);
	mapApi.addFeatures([marker]);
	mapApi.openInfowindow(marker);
}
//####################地图工具操作类 如测距 放大 缩小等##########################//
/**
 * 拉框放大
 */
function mapZoomInBox(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
	//激活拉框放大
	mapApi.zoomBoxActive(true);
}
/**
 * 拉框缩小
 * @return
 */
function mapZoomOutBox(){
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
	//激活拉框缩小
	mapApi.zoomBoxActive(false);
}
/**
 * 测距
 */
function mapRuler(){
	
	var mapApi = loadMapApi();
	mapApi.drawDeactive();
	mapApi.drawCalculate(true);//绘制线路时是否计算距离
	//激活绘制折线
	mapApi.drawLineActive();
}
/**
 * 地图打印
 * @return
 */
function mapPrint(){
	window.print();
}
/**
 * 地图切换
 * @param type
 * @return
 */
function changeMap(type){
	clearLayerFeatures();
	ps_mapType = type;
	initMap();
}
/**
 * 地图清屏
 */
function mapClear(){
	clearLayerFeatures();
}
/**
 * 地图上poi点的显示/隐藏
 * @return
 */
function poiDisChange(type){
	if(type == "store_hide"){//地图上隐藏门店信息
		showPoi = false;
		clearPois();
		$("#storePoi").html('隐藏门店 <span class="caret">');
	}else if(type == "store_show"){//显示门店信息
		showPoi = true;
		getStores({curPage:1});//in monitor/person/main.js 获取门店信息
		$("#storePoi").html('显示门店 <span class="caret">');
	}
}
/**
 * 清除地图上的POI 比如（门店）
 * @return
 */
function clearPois(){
	baidu.clearAllPois();
	supermap.clearAllPois();
}
//####################地图工具操作类 如测距 放大 缩小等##########################//