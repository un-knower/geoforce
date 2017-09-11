/**
 * monitor位置监控页面初始化及地图下方列表的操作类
 */
var jqgridId = "#personGpsTable";
var mapWd = 83;
var jqgridWidth;
$(document).ready(function(){
	
	initSMapSelWidget();
	
	//窗口变化自动
	$(window).resize(function(){
		resizeMapDiv();
		layerPersonTreeHidden();
	});
	resizeMapDiv();
	//source in person/main.js
	initPersonTree();//生成树
	//source in mapSupport.js
	initMap();
	//jqgrid 列表设置resize事件
	initJqGrid();
	createTable();
	mapWd = 0;
	//初始化默认加载 门店信息
	abortStoreLoad();
	getStores();//加载门店
});
var personTreeLayerIndex;//员工树展开的弹出框
function layerPersonTree(btnEvent){
	var off = $(btnEvent).offset();
	var ht = '450';
	var mapHt = $("div[id*='mapDiv_']").height();
	if(mapHt)
		ht = mapHt;
	personTreeLayerIndex = $.layer({
		type:1,
		title: false,
		offset: [off.top+'px',(off.left-280+40)+'px'],
		area:['280',(ht-10)],
		shade : [0.1 , '#000' , true],
		shade:[0],
		closeBtn:[0,false],
//		shift: 'right-top',
		page:{dom:'#personTreeDiv'}
	});
	$("#personTreePanelDiv").css({height:(ht-10)});
	$("#personDeptTree").parent().css({height:(ht-125)});
	$("#personTreeHiddenDiv").hide();
	
}
function layerPersonTreeHidden(){
	layerPersonTreeClose();
	$("#personTreeHiddenDiv").show();
}
function layerPersonTreeClose(){
	layer.close(personTreeLayerIndex);
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
		colNames:['姓名','终端号','手机号','状态','定位时间','位置','方向',
		          '最近工作','拍照','lng','lat','bdLng','bdLat'],
		colModel:[{index:'personName',name:'personName',width:60},
				  {index:'termCode',name:'termCode', width:80},
				  {index:'mobile',name:'mobile', width:70},
				  {index:'status',name:'status', width:55},
				  {index:'gpsTime',name:'gpsTime'},
				  {index:'addr',name:'addr'},
				  {index:'direction',name:'direction', width:70},
				  {index:'sign',name:'sign', width:110},
				  {index:'img',name:'img', width:50},
				  {index:'lng',name:'lng',hidden:true},
				  {index:'lat',name:'lat',hidden:true},
				  {index:'bdLng',name:'bdLng',hidden:true},
				  {index:'bdLat',name:'bdLat',hidden:true}], 
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rownumbers: true,
		altRows: true,
		caption: '巡店人员位置信息',
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
function addGpsDataList(personObjs){
	if(personObjs == null ||　personObjs.length == 0){
		return;
	}
	var onLinepersons = new Array();
	var noLinepersons = new Array();
	var unLinepersons = new Array();
	var personObj,personId,termCode,status,personName,gpsTime,addr,signInfo,
		storeName,mobile,direction,directionStr,otherStr,picId,statusStr= "";
	for(var i in personObjs){
		personObj = personObjs[i];
		personId = personObj.personId;
		termCode = personObj.termCode;
		mobile = personObj.mobile || '';
		status = personObj.status || '';
		personName = personObj.personName;
		gpsTime = personObj.gpsTime || '';
		addr = personObj.addr || '';
		direction = personObj.direction || '';
		directionStr = personObj.directionStr || '';
		picId = personObj.picId || '';
		signInfo = personObj.signInfo || '';
		
		if(personId == null || personId == ""){
			continue;
		}
//		if(picId){
//			picId = '<img src="'+picId+'" width="30" height="30" onmouseover="showCarImg(tahis,\'top\')" />';
//		}else{
//			picId = "";
//		}
		if(status == 1){//在线
			statusStr = '<font color = "blue">在线</font>';		
		}else if(status ==2){//离线
			statusStr = '<font color = "red">离线</font>';
		}else {
			statusStr = '<font color="gray">未上线</font>';
		}
		var tmp = new Object();
		tmp.personId = personId;
		tmp.personName = personName;
		tmp.termCode = termCode;
		tmp.mobile = mobile;
		tmp.status = statusStr;
		tmp.gpsTime = gpsTime;
		tmp.addr = addr;
		tmp.direction = directionStr;
		tmp.sign = signInfo;
		tmp.img = '';
		if(status == 1){
			onLinepersons.push(tmp);
		}else if(status == 2){
			noLinepersons.push(tmp);
		}else{
			unLinepersons.push(tmp);
		}
		tmp.lng = personObj.lng;
		tmp.lat = personObj.lat;
		tmp.bdLng = personObj.bdLng;
		tmp.bdLat = personObj.bdLat;
	}
	var obj = onLinepersons.concat(noLinepersons).concat(unLinepersons);
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


