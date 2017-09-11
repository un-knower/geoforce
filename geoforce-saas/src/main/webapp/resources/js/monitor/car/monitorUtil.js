/**
 * 位置监控模块一些工具类js
 */

/**及时定位 config*/
var stopLocateFlag = true;//及时定位停止
var carLocateTimer = null;//及时定位定时器
/**及时定位 config*/

/**锁定跟踪 config*/
var stopTrackFlag = true;//锁定跟踪停止
var carTimer = null;//锁定跟踪定时器
/**锁定跟踪 config*/

var carZtree = null;//车辆树
/**
 * 初始化进入位置监控某一功能时调用的状态清除方法
 * 如进入定位功能初始化时调用
 * @param active
 * @return
 */
function initMonitor(active){
	setCurActive(active);//in mapSupport.js
	
	clearAllInterval();
	
	var mapApiObj = loadMapApi();//in mapSupport.js
	if(mapApiObj == null){
		return;
	}
	var map = mapApiObj.map;
	if(map == null){
		return;
	}
	deactiveMapDraw();//让画点线面方式处于非激活状态 in mapSupport.js
	clearLayerFeatures();//in mapSupport.js 清除所有点线面
}
/**
 * 清除位置监控所有定时器 setTimeout setInterval等
 * @return
 */
function clearAllInterval(){//清空所有的setTimeout setInterval等
	carLocateClosed();//in monitor.js 
	carTrackClosed();//in monitor.js 
	controllClosed();//in historyControl.js
}
/**
 * 定时定位停止
 * @return
 */
function carLocateClosed(){
	stopLocateFlag =true;
	clearTimeout(carLocateTimer);
	carLocateTimer = null;
}
/**
 * 锁定跟踪停止
 * @return
 */
function carTrackClosed(){
	stopTrackFlag =true;
	clearTimeout(carTimer);
	carTimer = null;
}
/**
 * 生成树
 * @param checkType
 * @param param请求参数
 * @return
 */
function initCarTree(param){
	//全部车辆树
	var url = ctx+"/com/supermap/carTree";
	//请求参数
	var search = "";
	if(param){
		//检索出的车辆生成树
		url = ctx+"/com/supermap/carTreeSearch";
		search = param.searchValue;
	}
	var setting ={
		view: {
			dblClickExpand: false,
			showTitle: true
		},
		check:{enable: true,chkStyle:"checkbox"},
		data: {
			simpleData: {
				enable: true
			},
			key: {
				name:'ename',
				title:'name'
			}
		},
		async: {
			enable: true,
			url: url,
			autoParam:["id=treeId"],
			otherParam:{"carSearch":search}
		},
		callback: {
//			onClick: carTreeOnClick
		}
	};
	$('#carDeptTree').css("background-color", "transparent");
	carZtree = $.fn.zTree.init($('#carDeptTree'), setting);
}
function carTreeOnClick(event, treeId, treeNode){
	if(!treeNode.isParent)
		showCarDetail(treeNode.id,treeNode.tId);
}
/**
 * 获得车辆树中已选车辆id
 * @return
 */
function getSelectedCarId(){
	var selectCars = new Array();
	if(carZtree){
		var nodes = carZtree.getCheckedNodes(true);
		for(var i=0;i<nodes.length;i++){
			if(!nodes[i].isParent)
				selectCars.push(nodes[i].id);
		}
	}
	return selectCars;
}


/**
 * 主页面车辆搜索
 * @return
 */
function monitorCarSearch(){
	var searchValue = $("#searchValue").val();
	//in common.js 特殊字符校验
	if(searchValue && !textCheck(searchValue)){
		alert("不能输入特殊字符");//搜索内容不能输入特殊字符
		return;
	}
	var param = null;
	if(searchValue){
		param = new Object();
		param.searchValue = searchValue;
	}
	//加载车辆树
	initCarTree(param);
}

/**
 * 点击车牌号显示车辆详细信息
 * @param carId
 * @param clickId 点击的控件对象id
 * @return
 */
function showCarDetail(carId,clickId){
	
	if(carId == null || carId == ""){
		return;
	}
	$.ajax({
		type : "POST",
		async : false,
		url : ctx+"/carOther/carAction!getCarDetail.do",
		data : "t="+t+"&carId="+carId,
		dataType : "json",
		success : function(msg) {
			//权限及sesion校验
			if(!checkSession(msg)){
				return;
			}
			if(msg){
				carDetail_callback(msg);
			}else{
				$.messager.alert(commonLang.tooltip,monitorLang.chexiangqing,"error");//车辆详情不存在
			}
		},
		error : function() {
			$.messager.alert(commonLang.tooltip,monitorLang.chexiangqing,"error");
        }
	});
	function carDetail_callback(msg){
		
		var carObj = msg.car;
		var temObj = msg.teminal;
		var carTypeObj = msg.type;
		var drivers = msg.driverNames == null?'':msg.driverNames;
		if(carObj == null || carObj == ""){
			return;
		}
		var license="",carTypeName="",teminalName="",teminalCode="",mobile="";
		if(carTypeObj){
			carTypeName = carTypeObj.name==null?'':carTypeObj.name;
		}
		var carAlarmTime = carObj.alarmTime==null?'':carObj.alarmTime;//报警上传时长
		
		var showDrivers = drivers;
		if(drivers.length > 11){
			showDrivers = drivers.substring(0, 11)+"..";
		}
		var showCarTypeName = carTypeName;
		if(carTypeName.length > 11){
			showCarTypeName = carTypeName.substring(0, 11)+"..";
		}
		if(temObj){
			teminalName = temObj.name;
			teminalCode = temObj.code;
			mobile = temObj.mobile==null?'':temObj.mobile;
		}
		var showTeminalName = teminalName;
		if(teminalName.length > 11){
			showTeminalName = teminalName.substring(0, 11)+"..";
		}
		var showTeminalCode = teminalCode;
		if(teminalCode.length> 11){
			showTeminalCode = teminalCode.substring(0, 11)+"..";
		}
		var showMobile = mobile;
		if(mobile.length > 11){
			showMobile = mobile.substring(0, 11)+"..";
		}
		var inHtml = "";
		if(carTypeName){
			inHtml += '<p>'+monitorLang.carType+'：<span title="'+carTypeName+'">'+showCarTypeName+'</span></p>';
		}else{
			inHtml += '<p>'+monitorLang.carType+'：<span>'+monitorLang.notCarType+'</span></p>';
		}
		if(drivers){
			inHtml += '<p>notCarType：<span title="'+drivers+'">'+showDrivers+'</span></p>';
		}else{
			inHtml += '<p>'+monitorLang.drivers+'：<span>'+monitorLang.notDriver+'</span></p>';
		}
		if(teminalName){
			inHtml += '<p>'+monitorLang.terminal+'：<span title="'+teminalName+'">'+showTeminalName+'</span></p>';
		}else{
			inHtml += '<p>'+monitorLang.terminal+'：<span>'+monitorLang.notTerminal+'</span></p>';
		}
		if(teminalCode){
			inHtml += '<p>&nbsp;'+monitorLang.teminalCode+'：<span title="'+teminalCode+'">'+showTeminalCode+'</span></p>';
		}
		if(mobile){
			inHtml += '<p>'+monitorLang.mobile+'：<span title="'+mobile+'">'+showMobile+'</span></p>';
		}
		if(carAlarmTime){
			inHtml += '<p>'+monitorLang.carAlarmTime+'：<span title="'+carAlarmTime+commonLang.minutes+'">'+carAlarmTime+''+monitorLang.onetimeAlarm+'</span></p>';
		}else{
			inHtml += '<p>'+monitorLang.carAlarmTime+'：<span>'+monitorLang.notAlarmTime+'</span></p>';
		}
		
		$.messager.alert(monitorLang.carDetail,'<div>'+inHtml+'</div>','info');//车辆详情
	}
}