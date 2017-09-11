var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var mapWd = 83
$(function(){

	bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	initComboDeptTree($('#deptTree'));
	
//	$(window).resize(function(){
//		resizeMapDiv();
//	});
//	resizeMapDiv();
//	initMap()
//	mapWd = 0;
	
})

/**
 * 初始化列表
 * @return
 */
function initGrid(){
	
	var colNames = ['车牌号','部门','报警名称', '报警时间 ', '持续时长','类型','报警位置','时速','状态','处理人','处理意见','处理时间','carId','longitude','latitude','direction','speed','alarmDate'];
	var colModel = [{index:'carLicense',name:'carLicense', width:60},
	                {index:'deptName',name:'deptName', width:60},
					{index:'typeName',name:'typeName',width:60},
					{index:'alarmDateStr',name:'alarmDateStr', width:60},
					{index:'difTime',name:'difTime', width:60},
					{index:'typeName',name:'typeName', width:60},
					{index:'addr',name:'addr', width:60},
					{index:'speed',name:'speed',width:60},
					{index:'stautsName',name:'stautsName', width:60},
					{index:'userName',name:'userName', width:60},
					{index:'opinion',name:'opinion', width:60,hidedlg:true,hidden:true},
					{index:'dealDateStr',name:'dealDateStr', width:60},
					{index:'carId',name:'carId',hidedlg:true,hidden:true},
					{index:'longitude',name:'longitude',hidedlg:true,hidden:true},
					{index:'latitude',name:'latitude',hidedlg:true,hidden:true},
					{index:'direction',name:'direction',hidedlg:true,hidden:true},
					{index:'speed',name:'speed',hidedlg:true,hidden:true},
					{index:'alarmDate',name:'alarmDate',hidedlg:true,hidden:true}]
					//{index:'view',name:'view', width:40}]
					
	var deptCode = $("#deptCode").val();
	var license = $("#license").val();
	var status = $("#status").val();
	var typeId = $("#typeId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,deptCode:deptCode,status:status,typeId:typeId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/alarmList"
	loadGrid(grid_selector,url,"","报警统计",parms,colNames,colModel);
}


/**
* 加载列表数据
* @param obj
* @param url
* @param data
* @param title
* @param postData
* @param colNames
* @param colModel
* @return
*/
function loadGrid(obj, url,data ,title,postData,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		postData: postData,
		height:  function(){
		var h=getWindowHeight()-280;
		return h+"";}(),
		
		colNames:colNames,
		colModel:colModel, 
		
		sortable:false,
		viewsortcols:[false,'vertical',false],
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		multiselect: false,
        multiboxonly: true,
        jsonReader: {
			root: "rows",
			// total: "pageNum",// 表示总页数
			// page: "currentPageNum",//当前页
			// records: "totalNum",//总行数
            repeatitems : false    
        },  

		loadComplete : function() {
		},
		onSelectRow: function(id){
			 $("#carId").val(id); 
	    },
	    gridComplete:function(){
	    　　	var ids=jQuery(obj).jqGrid('getDataIDs');
	    　　	
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i]; 
	            focusNum = "<a href='#' class='ace-icon fa fa-pencil blue' style='color:#f60' alt='查看' onclick='mapView(\"" + id + "\")'>查看</a>";
	            jQuery(obj).jqGrid('setRowData', ids[i], { view: focusNum});
	        }
	    　　},
		caption: title,
		autowidth: true

	});
	$(window).triggerHandler('resize.jqGrid');
}

/**
 * 加载列表分页
 * @return
 */
function loadNavGrid(){
    jQuery(grid_selector).jqGrid('navGrid',pager_selector,{ edit: false,add: false,del: false,search: false,refresh: false})
}

/**
 * 显示地图
 * @param id
 * @return
 */
function mapView(id){
	
	$("#mapDiv").show();
	var datarow = jQuery("#grid-table").jqGrid('getRowData', id); 
	var obj = new Object();
	var carObjs = new Array();
	
	obj.carId = datarow.carId;
	obj.license = datarow.carLicense;
	obj.lng = datarow.longitude;
	obj.lat = datarow.latitude;
	obj.speed = datarow.speed;
	obj.addr = datarow.addr;
	obj.directionStr = datarow.direction;
	obj.gpsTimeStr = datarow.alarmDate;
	obj.status=2
	carObjs.push(obj);
	
	$(grid_selector).jqGrid( 'setGridState',  "hidden");
	if(carObjs.length > 0){
		var mapApi = loadMapApi();
		mapApi.clearAllFeatures();
		map_addMarks(carObjs);
		mapApi.featureCenter();
	}
}

/**
 * 搜索刷新
 * @return
 */
function reload() {
	$("#mapDiv").hide();
	$(grid_selector).jqGrid( 'setGridState',  "visible");
	var deptId = $("#deptId").val();
	var license = $("#license").val();
	var status = $("#status").val();
	var typeId = $("#typeId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	if(!comptime(starttime,endtime)){
		bootbox.alert("开始时间不能大于结束时间!");
		return false;
	}
	var starttimes = starttime.split("-");
	var endtimes = endtime.split("-");
	if(starttimes[1] != endtimes[1]){
		bootbox.alert("查询时间不能跨月!");
		return false;
	}
	var parms={license:license,deptId:deptId,status:status,typeId:typeId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/alarmList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}

/**
 * 判断开始时间结束时间是否正确
 * @param beginTime
 * @param endTime
 * @return
 */
function comptime(beginTime,endTime) {
	 var date1 = new Date(beginTime.replace(/\-/g, "\/"));
    var date2 = new Date(endTime.replace(/\-/g, "\/"));
    if(date1-date2 > 0 )
    return false;
    
    return true;
}


/**
 * 点击部门树回调方法
 */
function onClick(e, treeId, treeNode) {
	var source = $('#deptTree').attr("source");
	
	if(treeNode){
		var deptId = treeNode.id;
		var deptCode = treeNode.code;
		var deptName = treeNode.name;
			$("#deptId").val(deptId);
			$("#deptName").val(deptName)
		
	}
	hideMenu();
}



function showMenu() {
	var deptObj = $("#deptName");
	var deptOffset = $("#deptName").offset();
	$('#deptTree').css("background-color", " background: none repeat scroll 0 0 #f0f6e4;");
	$("#menuContent").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");
	
	$("body").bind("mousedown", onBodyDown);
}

function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}

function resizeMapDiv(){
	var wh = $(window).height();
	var top = $("#navbar").height();
	var footer = $(".footer-content").height();
	var tbHt = $("#grid-table").height();
////	alert($("#monitorBtns").width()+":"+mapWidthoffset);
//	//地图初始化高度
	
	
	$(grid_selector).jqGrid( 'setGridWidth',  navBarWd+mapWd);
    $("div[id*='mapDiv_']").css({width:(navBarWd+mapWd),height:(wh-top-footer-tbHt-100)});
	setTimeout(function() {
		if($("#monitorBtns").width() < navBarWd){
			$("div[id*='mapDiv_']").css({width:($("#monitorBtns").width()+mapWd),height:(wh-top-footer-tbHt-100)});
			
		}
	},100);
	
	refreshMap();
//
}

function exportData(){
	var row = $(grid_selector).getGridParam("reccount");
	if(row == 0){
		bootbox.alert("无数据导出!");
		return false;
	}
	var form = $("#searchForm");
	form.attr("action",ctx+"/com/supermap/alarmExport");
	form.submit();
	
}
