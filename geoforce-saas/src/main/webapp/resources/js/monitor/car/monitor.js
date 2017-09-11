/**
 * js 监控类 定位、跟踪、图像监控、历史轨迹
 * */

/**
 * 及时定位
 * @return
 */
function carLocate(carIds){
	
	if(carIds == null || carIds == ""){
		var array = getSelectedCarId();
		if(array.length == 0){
			alert("请选择车辆");
			return;
		}
		carIds = array.join(",");
	}
	initMapList("locate");
	initMonitor("locate");
	//底部列表栏弹出in mapList.js
	mapListShow();
//	$("#mapLoading").show();
	var timer = $.trim($("#locationNum").val());
	if(timer == null || timer == ""){
		timer = 30;
		$("#locationNum").val(timer);
	}
	stopLocateFlag = false;
	//in mapSupport.js 点击及时定位 表示首次定位 满屏显示所有车辆
	setInitLocateFlag(true);
	ajaxLocate(carIds,timer*1000);
}

/**
 * 及时定位操作方法
 * @return
 */
function ajaxLocate(carIds,timer){
	if(stopLocateFlag){
		return;
	}
	$.ajax({
		type : "POST",
		async : true,
		url : ctx+"/carMonitor/locate",
		data : "carIds="+carIds,
		dataType : "json",
		success : function(msg) {
			if(msg){
				if(msg.status == 1){//定位成功
					if(stopLocateFlag){//如果定位停止，不在继续打点
						return;
					}
					map_Locate(msg.info);//地图上定位打点初始化方法 in mapSupport.js
					//保证只存在一个carLocateTimer
					if(carLocateTimer){
						clearTimeout(carLocateTimer);
						carLocateTimer = null;
					}
					carLocateTimer = setTimeout(function(){
						ajaxLocate(carIds,timer);},timer);
				}else{
					alert(msg.info);
				}
			}else{
				alert("无位置信息");
			}
		},
		error : function() {
			alert("无位置信息");
        }
	});
}
/**
 * 锁定跟踪
 * @return
 */
function carTrack(carIds){
	
	if(carIds == null || carIds == ""){
		var array = getSelectedCarId();
		if(array.length == 0){
			alert("请选择车辆");
			return;
		}
		if(array.length > 1){
			alert("只能选择一辆车进行跟踪");
			return;
		}
		carIds = array[0];
	}
	
	initMapList("track");
	initMonitor("track");
	//底部列表栏弹出
	mapListShow();
	
//	$("#mapLoading").show();
	var timer = $.trim($("#trackNum").val());
	if(timer == null || timer == ""){
		timer = 30;
		$("#trackNum").val(timer);
	}
	stopTrackFlag = false;
	ajaxTrack(carIds,timer*1000);
}
/**
 * 通过ajax拉方式跟踪车辆
 * @param carIds
 * @param timer
 * @return
 */
function ajaxTrack(carIds,timer){
	if(stopTrackFlag){
		return;
	}
	$.ajax({
		type : "POST",
		async : true,
		url : ctx+"/carMonitor/locate",
		data : "carIds="+carIds,
		dataType : "json",
		success : function(msg) {
//			$("#mapLoading").hide();
			
			if(msg && msg.status == 1 && msg.info[0].status != 3){
				if(stopTrackFlag){
					return;
				}
//				map_initActive(msg,"gz");
				map_Track(msg.info);// in mapSupport.js 跟踪
				//保证只存在一个carTimer
				if(carTimer){
					clearTimeout(carTimer);
					carTimer = null;
				}
				carTimer = setTimeout(function(){
					ajaxTrack(carIds,timer);},timer);
			}else{
				alert("无位置信息");
			}
		},
		error : function() {
			alert("无位置信息");
        }
	});
}

/**
 * 历史轨迹初始化
 * @return
 */
function initHistory(){
	initMonitor("history");
	initMapList("history");//in mapList.js初始化地图下方列表
	resetCarSearch();
	//将其他div隐藏 只显示历史轨迹div
	$("#historyDiv").show().siblings().hide();
	
	//获取当前时间 in common.js
	var today = formatDate(getMilTime());
	$("#startTime").val(today.substring(0,11)+"00:00:00");
	$("#endTime").val(today);
//	$("#startTime").val("2013-09-27 00:00:00");
//	$("#endTime").val("2013-09-27 23:00:00");
	//加载车辆树
	createCarTree('radio',null);
}
/**
 * 历史轨迹查询
 * @return
 */
function carHistory(startTime, endTime){
	
	var array = getSelectedCarId();
	if(array.length == 0){
		alert("请选择车辆");
		return;
	}
	if(array.length > 1){
		alert("只能选择一个车辆进行轨迹查询");
		return;
	}
	initMapList("history");
	initMonitor("history");
	var carId = array[0];
	if(!startTime || !endTime){
		//获取当前时间 in common.js
		var today = formatDate(getMilTime());
		startTime = today.substring(0,11)+"00:00:00";
		endTime = today;
	}
//	$("#mapLoading").show();
	ajaxHistory(carId,startTime,endTime,false);
}
/**
 * ajax历史轨迹查询
 * @param carId
 * @param startTime
 * @param endTime
 * @param isShowTime 出错时是否弹出历史轨迹时间窗口
 * @return
 */
function ajaxHistory(carId,startTime,endTime,isShowTime){
	startTime = "2015-05-13 00:00:00";
	endTime = "2015-05-13 23:59:59";
	var loading = layer.load('加载中',0);
	$.ajax({
		type : "POST",
		async : true,
		url : ctx+"/carMonitor/history",
		data : "carId="+carId+"&startDate="+startTime+"&endDate="+endTime,
		dataType : "json",
		success : function(msg) {
			layer.close(loading);
			if(msg){
				if(msg.status == 1){
					if(isShowTime){
						$("#historyTime").modal('hide');			
					}
					history_callback(msg.info);
				}else{
					alert(msg.info);
				}
			}else{
				alert("无历史轨迹");
			}
		},
		error : function() {
			layer.close(loading);
//			$("#mapLoading").hide();
//			if(isShowTime){//其它方式进入的轨迹回放 出错时再次弹出时间
//				$("#historyTimeWindow").window("open");//历史轨迹设置时间window 关闭
//			}
			alert("无历史轨迹数据");
        }
	});
}
/**
 * 轨迹回放 回调函数
 * @param msg
 * @return
 */
function history_callback(msg){
	initMapList("history");
	initMonitor("history");
	map_History(msg);// in mapSupport.js 历史轨迹
}
/**
 * 在非历史轨迹页面中
 * 选择历史轨迹操作，
 * 弹出时间选择框
 * @return
 */
function historyShowTime(carId){
	if(carId == null || carId == ""){
		var array = getSelectedCarId();
		if(array.length == 0){
			alert("请选择车辆");
			return;
		}
		if(array.length > 1){
			alert("只能选择一个车辆进行查询");
			return;
		}
		carId = array[0];
	}
	
	var t = new Date();
	var date = formatDate(t.getTime());
	var defStartTime = date.split(" ")[0]+" 00:00:00";
	var defEndTime = date;
	$("#hTimeCarId").val(carId);
	$("#hTimeStart").val(defStartTime);
	$("#hTimeEnd").val(defEndTime);
	$("#historyTime").modal('show');
	
}
/**
 * 在非历史轨迹页面中
 * 在时间弹出window中确定后查询历史轨迹
 * @return
 */
function historyTimeSave(){
	var startTime = $("#hTimeStart").val();
	var endTime = $("#hTimeEnd").val();
	var carId = $("#hTimeCarId").val();
	if(carId == null || carId == ""){
		alert("车辆不存在");//该车辆不存在
		return false;
	}
	if(startTime == null || startTime == ""){
		alert("开始时间不能为空");//开始时间不能为空
		return false;
	}
	if(endTime == null || endTime == ""){
		alert("结束时间不能为空");//开始时间不能为空
		return false;
	}
//	$("#mapLoading").show();
	ajaxHistory(carId,startTime,endTime,true);
	
}
/**
 * 鼠标滑过区域查询 弹出查询方式
 * @param butEvent
 * @return
 */
function showRegSearch(butEvent){
	var but = $(butEvent);
	if(!but) return;
	
	but.tooltip({
		position: 'bottom',
		content:  function(){
			return $('#regionToolbar');
		},
		onShow: function(){
			var t = $(this);
			t.tooltip('tip').unbind().bind('mouseover', function(){
				t.tooltip('show');
			}).bind('mouseleave', function(){
				t.tooltip('hide');
			});
		}
	}).tooltip('show');
}
/**
 * 区域查车
 * @return
 */
function regionSearch(type){
	initMonitor("regionSearch");
	if(type == "circle"){//画圆
		map_drawCircle();//in mapSupport.js
	}else if(type == "rectangle"){//画矩形
		map_drawRectangle();//in mapSupport.js
	}else{//画多边形
		map_drawPolygon();//in mapSupport.js
	}
	
	
}
/**
 * 区域查车回调函数
 * @param regionStr 区域经纬度串
 * @param mapType 地图类型
 * @return
 */
function regionSearch_callBack(regionStr,mapType){
	if(regionStr == null || regionStr == ""){
		alert("无效的区域");//无效的区域
		return;
	}
	if(mapType == null || mapType == ""){
		mapType = "supermap";
	}
//	if(boundMinXY == null || boundMinXY == ""){
//		$.messager.alert(commonLang.tooltip,monitorLang.wuxiaoquyu,"error");
//		return;
//	}
//	if(boundMaxXY == null || boundMaxXY == ""){
//		$.messager.alert(commonLang.tooltip,monitorLang.wuxiaoquyu,"error");
//		return;
//	}
	var retData = regionStr;
	var url = ctx + "/carMonitor/regSearch";
	$.ajax({
		type : "POST",
		async : false,
		url : url,
		data : "lngLats="+retData+"&mapType="+mapType,
		dataType : "json",
		error : function() {// 请求失败处理函数
			retData = null;
		},
		success : function(data) {
			if(data && data.status == 1){
				retData = data.info;
			}else{
				retData = null;
			}
		}
	});
	if(retData == null || retData.length == 0){
		alert("所选区域内没有车辆");//所选范围内没有车辆
		return;
	}
	addGpsDataList(retData);//列表中添加记录 in mapList.js
	map_addMarks(retData);//in mapSupport.js 地图打点
	
}
/**
 * 最近警力查询
 * @return
 */
function shortDistance(){
	initMonitor("shortDistance");
	map_drawPoint();//绘制点
}
/**
 * 最近警力绘制点后的回调函数
 * @param xyStr(x,y)
 * @param mapType 地图类型
 * @return
 */
function shortDistance_callback(xyStr,mapType){
	if(xyStr == null || xyStr == ""){
		$.messager.alert(commonLang.tooltip,monitorLang.wuxiaoquyu,"error");//无效的区域
		return;
	}
	if(mapType == null || mapType == ""){
		mapType = "supermap";
	}
	var retData = xyStr;
	var url = ctx + "/regSearch/regSearchAction!shortDistance.do";
	$.ajax({
		type : "POST",
		async : false,
		url : url,
		data : "searchRegion="+retData+"&mapType="+mapType,
		dataType : "json",
		error : function() {// 请求失败处理函数
			retData = null;
		},
		success : function(data) {
			//权限及sesion校验
			if(!checkSession(data)){
				return;
			}
			if (data){
				retData = data;
			}else{
				retData = null
			}
		}
	});
	if(retData == null || retData.length == 0){
		$.messager.alert(commonLang.tooltip,monitorLang.wuchefanwei,"error");//所选范围内没有车辆
		return;
	}
	map_addMarks([retData]);//in mapSupport.js 地图打点
	map_featureCenter();//一屏显示
	
}
/**
 * 定位、跟踪时启动拍照
 * @return
 */
function carPhoto(carIds){
	if(!carIds){
		var array = getSelectedCarId();
		if(array.length == 0){
			$.messager.alert(commonLang.tooltip,monitorLang.xuanzeche,"error");
			return;
		}
		carIds = array.join(",");
	}
	$.ajax({
		type : "POST",
		async : false,
		url :  ctx+"/location/locationAction!carPhoto.do",
		data : "carIds="+carIds,
		dataType : "text",
		error : function() {// 请求失败处理函数
			$.messager.alert(commonLang.tooltip,monitorLang.photoFail,"error");//拍照失败
		},
		success : function(data) {
			//权限及sesion校验
			if(!checkSession(data)){
				return;
			}
			if (data && data == 'success'){
				$.messager.alert(commonLang.tooltip,monitorLang.photoSuccess);//拍照成功
			}else{
				$.messager.alert(commonLang.tooltip,data,"error");
			}
		}
	});
}