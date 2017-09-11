/**
 * monitor位置监控页面初始化及地图下方列表的操作类
 */
var jqgridId = "#carGpsTable";
var mapWd = 83;
var jqgridWidth;
$(document).ready(function(){

	initSMapSelWidget();	
	//窗口变化自动
	$(window).resize(function(){
		resizeMapDiv();
		layerCarTreeHidden();
	});
	resizeMapDiv();
	//source in monitorUtil.js
	initCarTree();//生成树
	//source in mapSupport.js
	initMap();
	//jqgrid 列表设置resize事件
	initJqGrid();
	createTable();
	mapWd = 0;
//	layerCarTreeHidden();
});
var carTreeLayerIndex;//车辆树展开的弹出框
function layerCarTree(btnEvent){
	var off = $(btnEvent).offset();
	var ht = '450';
	var mapHt = $("div[id*='mapDiv_']").height();
	if(mapHt)
		ht = mapHt;
	carTreeLayerIndex = $.layer({
		type:1,
		title: false,
		offset: [off.top+'px',(off.left-280+40)+'px'],
		area:['280',(ht-10)],
		shade : [0.1 , '#000' , true],
		shade:[0],
		closeBtn:[0,false],
//		shift: 'right-top',
		page:{dom:'#carTreeDiv'}
	});
	$("#carTreePanelDiv").css({height:(ht-10)});
	$("#carDeptTree").parent().css({height:(ht-125)});
	$("#carTreeHiddenDiv").hide();
	
}
function layerCarTreeHidden(){
	layerCarTreeClose();
	$("#carTreeHiddenDiv").show();
}
function layerCarTreeClose(){
	layer.close(carTreeLayerIndex);
}
/**
 * 地图div高度自适应
 * @return
 */
function resizeMapDiv(){
	var wh = $(window).height();
	var top = $("#navbar").height()+5;
	var navBarh = $("#monitorBtns").height()+5;
	var footer = 0;
	var tbHt = $("#mapTable").height();
//	alert($("#monitorBtns").width()+":"+mapWidthoffset);
	//地图初始化高度
	var navBarWd = $("#monitorBtns").width();
	$(jqgridId).jqGrid( 'setGridWidth',  navBarWd+mapWd);
	$("div[id*='mapDiv_']").css({width:(navBarWd+mapWd),height:(wh-top-footer-navBarh-tbHt)});
	setTimeout(function() {
		if($("#monitorBtns").width() < navBarWd){
			$("div[id*='mapDiv_']").css({width:($("#monitorBtns").width()+mapWd),height:(wh-top-footer-navBarh-tbHt)});
			$(jqgridId).jqGrid( 'setGridWidth', $("#monitorBtns").width()+mapWd);
		}
	},100);
	
	refreshMap();
}
/**
 * 初始化jqGrid
 * @return
 */
function initJqGrid(){
	
//	$(window).on('resize.jqGrid', function () {
//		$(jqgridId).jqGrid( 'setGridWidth',  $("#monitorBtns").width());
//    });
//	var parent_column = $(jqgridId).closest('[class*="col-"]');
//	$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
//		if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
//			//setTimeout is for webkit only to give time for DOM changes and then redraw!!!
//			setTimeout(function() {
//				
//			}, 0);
//			$(jqgridId).jqGrid( 'setGridWidth', $("#monitorBtns").width()+100);
//		}
//    });
}
/**
 * 生成地图下方列表
 * data json数据
 * @return
 */
function createTable(data){
	$(jqgridId).jqGrid({
		data:data,
		datatype:'local',
		height: '120',
		width: '100%',
		colNames:['车牌号','终端号','状态','定位时间','位置','速度km/h','方向',
		          '里程/km','拍照','告警','lng','lat','bdLng','bdLat'],
		colModel:[{index:'license',name:'license',width:90},
				  {index:'temCode',name:'temCode', width:90},
				  {index:'status',name:'status', width:55},
				  {index:'gpsTime',name:'gpsTime'},
				  {index:'addr',name:'addr'},
				  {index:'speed',name:'speed', width:60},
				  {index:'direction',name:'direction', width:70},
				  {index:'mile',name:'mile', width:70},
				  {index:'img',name:'img', width:50},
				  {index:'alarm',name:'alarm'},
				  {index:'lng',name:'lng',hidden:true},
				  {index:'lat',name:'lat',hidden:true},
				  {index:'bdLng',name:'bdLng',hidden:true},
				  {index:'bdLat',name:'bdLat',hidden:true}], 
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rownumbers: true,
		altRows: true,
		caption: '车辆位置信息',
		autowidth: true,
		shrintToFit:true,
		viewsortcols:[false,'vertical',false],
		onSelectRow: function(id){//选择一行
			var obj = $(this).jqGrid('getRowData',id);
			ReShowGPSInMap(obj);
		},
		gridComplete: function(){//加载表格完成	
			$(jqgridId).jqGrid( 'setGridWidth', $("#monitorBtns").width()+100);
		},
		onHeaderClick: function(status){//表格显示 隐藏
			resizeMapDiv();
		},
		onSortCol: function(){
			setTimeout(function(){
				resizeMapDiv();
				
			},0);
//			
		}
	});
//	$(window).triggerHandler('resize.jqGrid');
	//隐藏表格
	$(jqgridId).jqGrid( 'setGridState',  "hidden");	
	
}
/**
 * 根据不同功能初始化地图下方数据列表
 * @param name
 * @return
 */

function initMapList(name){
	cleanMapList();
}
/**
 * 清空地图下方列表的数据
 * @return
 */
function cleanMapList(){
	$(jqgridId)[0].addJSONData(null);
}
/**
 * 弹出地图下方列表栏
 * @return
 */
function mapListShow(){
//	$(jqgridId).jqGrid( 'setGridState',  "visible");
	
}
/**
 * 选中地图下方列表中的数据在地图聚焦
 * @return
 */
function ReShowGPSInMap(obj){
	if(obj){
		rePanToMark(obj);//in mapSupport.js 
	}
}
/**
 * 在gps数据列表中批量添加数据
 * @param carObj
 * @return
 */
function addGpsDataList(carObjs){
	if(carObjs == null ||　carObjs.length == 0){
		return;
	}
	var onLineCars = new Array();
	var noLineCars = new Array();
	var unLineCars = new Array();
	var carObj,carId,temCode,status,license,gpsTime,addr,
		speed,direction,directionStr,otherStr,zfTurn,oil,
		picpath,mile,alarmStr,zfTurnStr = "",statusStr= "";
	for(var i in carObjs){
		carObj = carObjs[i];
		carId = carObj.carId;
		temCode = carObj.temCode;
		status = carObj.status || '';
		license = carObj.license;
		gpsTime = carObj.gpsTime || '';
		addr = carObj.addr || '';
		speed = carObj.speed || 0.0;
		direction = carObj.direction || '';
		directionStr = carObj.directionStr || '';
		picpath = carObj.picpath || '';
		otherStr = carObj.othersStr || '';
		zfTurn = carObj.zfTurn || '';
		oil = carObj.oil || '';
		mile = carObj.mile || '';
		alarmStr = carObj.alarmStr || '';
		if(carId == null || carId == ""){
			continue;
		}
		if(zfTurn == "1"){
			zfTurnStr = "正转";
		}else if(zfTurn == "2"){
			zfTurnStr = "反转";
		}
		if(picpath){
			picpath = '<img src="'+picpath+'" width="30" height="30" onmouseover="showCarImg(this,\'top\')" />';
		}else{
			picpath = "";
		}
		if(status == 1){//在线
			statusStr = '<font color = "blue">在线</font>';		
		}else if(status ==2){//离线
			statusStr = '<font color = "red">离线</font>';
		}else {
			statusStr = '<font color="gray">未上线</font>';
		}
		var tmp = new Object();
		tmp.carId = carId;
		tmp.temCode = temCode;
		tmp.license = license;
		tmp.status = statusStr;
		tmp.gpsTime = gpsTime;
		tmp.addr = addr;
		tmp.speed = speed;
		tmp.direction = directionStr;
		tmp.picpath = picpath;
		tmp.mile = mile;
		tmp.zfTurn = zfTurnStr;
		tmp.alarm = alarmStr;
		if(status == 1){
			onLineCars.push(tmp);
		}else if(status == 2){
			noLineCars.push(tmp);
		}else{
			unLineCars.push(tmp);
		}
		tmp.lng = carObj.lng;
		tmp.lat = carObj.lat;
		tmp.bdLng = carObj.bdLng;
		tmp.bdLat = carObj.bdLat;
	}
	var obj = onLineCars.concat(noLineCars).concat(unLineCars);
	$(jqgridId)[0].addJSONData(obj);
	$(jqgridId).jqGrid( 'setGridWidth', $("#monitorBtns").width()+mapWd);
}
/**
 * 图像通过tooltip方式弹出
 * @param imgId
 * @return
 */
function showCarImg(imgEvent,position){
	var imgObj = $(imgEvent);
	if(!imgObj) return;
	var picpath = imgObj.attr("src");
	if(!picpath)return;
	if(!position)  position = 'top';
	
	var html = '<img src="'+picpath+'" />';
	imgObj.tooltip({
		position: position,
		content:  $('<div>'+html+'</div>'),
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
 * 在位置信息列表添加历史数据，随轨迹播放进行
 * @param carObj 车辆信息对象
 * @param gpsData 历史轨迹一条gps信息对象
 * @return
 */
function addGpsHistoryList(carObj,gpsData){
	if(carObj == null ||　carObj == ""){
		return;
	}
	if(gpsData == null || gpsData == ""){
		return;
	}
	var license = carObj.license;
	var carId = carObj.carId;
	var temCode = gpsData.temCode;
	var gpsTime = gpsData.gpsTime || '';
	var mile = gpsData.mile || '';
//	mile = (mile/1000).toFixed(3);
	var addr = gpsData.addr || '';
	var speed = gpsData.speed || '';
	var direction = gpsData.direction || '';
	var directionStr = gpsData.directionStr || '';
	var picpath = gpsData.picPath || '';
	var oil = gpsData.oil || '';
	var zfTurn = gpsData.zfTurn || '';
	var alarmStr = gpsData.alarmStr || '';
	
	var zfTurnStr = "";
	if(zfTurn == "1"){
		zfTurnStr = "正转";
	}else if(zfTurn == "2"){
		zfTurnStr = "反转";
	}
	if(picpath){
		picpath = '<img src="'+picpath+'" width="30" height="30" onmouseover="showCarImg(this,\'top\')" />';
	}else{
		picpath = "";
	}
	var tmp = new Object();
	tmp.id = carId;
	tmp.carId = carId;
	tmp.temCode = temCode;
	tmp.license = license;
	tmp.status = '';
	tmp.gpsTime = gpsTime;
	tmp.addr = addr;
	tmp.speed = speed;
	tmp.direction = directionStr;
	tmp.picpath = picpath;
//	tmp.oil = oil;
	tmp.mile = mile;
	tmp.zfTurn = zfTurnStr;
	tmp.alarm = alarmStr;
	
	tmp.ctLng = gpsData.ctLng;
	tmp.ctLat = gpsData.ctLat;
	tmp.bdLng = gpsData.bdLng;
	tmp.bdLat = gpsData.bdLat;
	
//	var index = $("#tt_gpsList").datagrid("getRowIndex",gpsTime);
//	if(index != -1){
//		$("#tt_gpsList").datagrid("deleteRow",index);
//	}
	//最新的记录增加到最前面
//	$(jqgridId).jqGrid('addRowData',  carId,tmp);
	$(jqgridId)[0].addJSONData([tmp]);
	$(jqgridId).jqGrid( 'setGridWidth', $("#monitorBtns").width()+mapWd);
}

