var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var jqGridHeight = 0
var mapShowMoveInterval = 0;
$(function(){
	bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	jqGridHeight = $(grid_selector).getGridParam("height");
	resizeMapDiv(jqGridHeight);
	initMap();
	$("#mapDiv").hide();
})
function initGrid(){
	var colNames = ['车牌号','部门','报警名称', '报警时间 ', '持续时长/分钟','类型','报警位置','时速/千米','状态','处理意见','处理时间','carId','longitude','latitude','direction','speed','alarmDate','操作'];
	var colModel = [{index:'carLicense',name:'carLicense', width:40},
	                {index:'deptName',name:'deptName', width:40},
					{index:'typeName',name:'typeName',width:40},
					{index:'alarmDateStr',name:'alarmDateStr', width:40},
					{index:'difTime',name:'difTime', width:40},
					{index:'typeName',name:'typeName', width:40},
					{index:'addr',name:'addr', width:40},
					{index:'speed',name:'speed',width:40},
					{index:'stautsName',name:'stautsName', width:40},
					{index:'opinion',name:'opinion', width:60},
					{index:'dealDateStr',name:'dealDateStr', width:60},
					{index:'carId',name:'carId',hidedlg:true,hidden:true},
					{index:'longitude',name:'longitude',hidedlg:true,hidden:true},
					{index:'latitude',name:'latitude',hidedlg:true,hidden:true},
					{index:'direction',name:'direction',hidedlg:true,hidden:true},
					{index:'speed',name:'speed',hidedlg:true,hidden:true},
					{index:'alarmDate',name:'alarmDate',hidedlg:true,hidden:true},
					{index:'view',name:'view', width:40}]
	var url =basePath+"/com/supermap/alermList"
	loadGrid(grid_selector,url,"","报警列表 ","",colNames,colModel);
}
function loadGrid(obj, url,data ,title,postData,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		postData: postData,
		height:  function(){
		var h=getWindowHeight()-250;
		return h+"";}(),
		colNames:colNames,
		colModel:colModel,
		sortable:true,
		autowidth:true,//自动宽
		viewsortcols:[false,'vertical',false],
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		multiselect: true,
        multiboxonly: true,
        jsonReader: {
			root: "result",
			total: "pageNum",// 表示总页数
			page: "currentPageNum",//当前页
			records: "totalNum",//总行数
            repeatitems : true,
        },  
	    onHeaderClick:function(){
			var display = $("#mapDiv").css("display");
			if(display == 'block'){
				$("#mapDiv").hide();
			}
	    },
	    gridComplete:function(){
	    　　		var ids=jQuery(obj).jqGrid('getDataIDs');
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i]; 
				var data = jQuery("#grid-table").getRowData(id);
				if(data.stautsName=="未处理"){
					 focusNum = "<a href='#' class='glyphicon glyphicon-zoom-in'  alt='查看' onclick='mapView(\"" + id + "\")'></a>";
					  focusNum += "<a  class='glyphicon glyphicon-ok' style='cursor:hand'  alt='处理' title='处理' onclick='alarmDeal(\"" + id +"\")'></a>";
					   focusNum += "<a  class='ace-icon fa fa-pencil blue' style='cursor:hand'  alt='已读' title='已读' onclick='alarmRead(\"" + id+"\")'></a>";
						jQuery(obj).jqGrid('setRowData', ids[i], { view: focusNum});
					
				}else{
					 focusNum = "<a href='#' class='glyphicon glyphicon-zoom-in'  alt='查看' onclick='mapView(\"" + id + "\")'></a>";
					 jQuery(obj).jqGrid('setRowData', ids[i], { view: focusNum});
				}
	           
				}
	    　　},
		caption: title,
		autowidth: true
	});
}
function loadNavGrid(){
	 jQuery(grid_selector).jqGrid('navGrid',pager_selector,
				{ 	//navbar options
					edit: false,
					editicon : 'ace-icon fa fa-plus-circle purple',
					add: false,
					addicon : 'ace-icon fa fa-plus-circle purple',
					del: false,
					delicon : 'ace-icon fa fa-trash-o red',
					search: false,
					searchicon : 'ace-icon fa fa-search orange',
					refresh: false,
					refreshicon : 'ace-icon fa fa-refresh green',
					view: false,
					viewicon : 'ace-icon fa fa-search-plus grey',
					carBind:false,
					carBindicon:'ace-icon fa fa-search-plus grey',
					carRegion:false, 
					carRegionicon:'ace-icon fa fa-search-plus grey',
					searchtext:'查找',edittext:'处理',addtext:'已读',refreshtext:'刷新', deltext:'已读',viewtext:'查看',
					searchtitle:'查找',edittitle:'处理',addtitle:'已读',refreshtitle:'刷新', deltitle:'已读',viewtitle:'查看',
					addfunc:loadNavGrid,
					editfunc:alarmDeal,
					delfunc:alarmRead,
					alertcap : "提示",
					alerttext : "请选择一行记录"
				}
			).navButtonAdd(pager_selector,{
				id:"viewmap",
				 caption:"查看地图",   
				 buttonicon : "ace-icon fa fa-search-plus grey",
				 onClickButton: function(){
		    	 var mapDivAttr = $("#mapDiv").css("display");
		    	 var divobj = $("#viewmap").children(".ui-pg-div");
		    		if(mapDivAttr == 'none'){
		    			$("#mapDiv").show();
		    			jQuery(grid_selector).jqGrid('setGridHeight',mapShowMoveInterval)
		    			divobj.empty().append('<span class="glyphicon glyphicon-chevron-up"></span>').append('收起地图');
		    			
		    		}else{
		    			$("#mapDiv").hide();
		    			jQuery(grid_selector).jqGrid('setGridHeight',jqGridHeight)
		    			divobj.empty().append('<span class="ui-icon ace-icon fa fa-search-plus grey"></span>').append('查看地图');
		    		}
				},   
				position:"first"  

			});
}

/**
 * 报警批量处理初始化
 * @return
 */
var alarmIds=null;
var  alarmTimes=null;
function alarmDeal(id){
	var status =$("#status").val();
	if(status==1){
		bootbox.alert("已经处理过，不能重复处理!");
		return;
	}
	 var alarm = jQuery("#grid-table").getRowData(id);
	var alarmIdArray = new Array();
	var alarmTimeArray=new Array();
	$("#alarmOpinionText").val('');
	alarmTimeArray.push(alarm.alarmDate);
	alarmIdArray.push(id);
	alarmIds = alarmIdArray.join(",");
	//alert(alarmIds);
	alarmTimes = alarmTimeArray.join(",");
	//alert(alarmTimes);
	$('#alarmDisposeDiv').modal('show');
}
/**
 * 报警处理
 * @return
 */
function saveAlarm(){
	var opinion =$("#alarmOpinionText").val();
	if(opinion == null || opinion == "" ){
		alert("请填写处理内容");
		return false;
	}
	
	if(opinion.length > 60){
		alert("请填写处理内容不能大于60");
		return false;
	}
	var ret = ajaxDealAlarm(alarmIds,opinion,alarmTimes,"CL");
	if(ret){
		$('#alarmDisposeDiv').modal('toggle');
		bootbox.alert("操作成功!");
		reload();
	}
}



/**
 * 未处理报警标记为已读
 * @return
 */
function alarmRead(id,time){
	alert(id);
	var status =$("#status").val();
	if(status==1){
		bootbox.alert("已经处理过，不能重复处理!");
		return;
	}
	var alarm = jQuery("#grid-table").getRowData(id);
	var opinion ="YD"
	var ret = ajaxDealAlarm(id,opinion,alarm.alarmDate,opinion);
	if(ret){
		bootbox.alert("操作成功!");
		reload();
	}
}
/**
 * 报警处理ajax方法
 * @param rows
 * @param opinion
 * @return
 */
function ajaxDealAlarm(alarmId,opinion,alarmDate,opinionType){
	$.ajax({
		type : "POST",
		async : false,
		url : basePath+"/com/supermap/alarmHandl",
		data : "alarmId="+alarmId+"&alarmTimes="+alarmDate+"&opinion="+opinion+"&opinionType="+opinionType,
		dataType : "json",
		success : function(obj) {
			if(obj.flag=="ok"){
				ret = true;
			}else{
				ret = false;
			}
		},
	});
	return ret;
}
function mapView(id){
	resizeMapDiv(jqGridHeight);
	$("#mapDiv").show();
	jQuery("#grid-table").jqGrid('setGridHeight',mapShowMoveInterval)
	var divobj = $("#viewmap").children(".ui-pg-div");
		divobj.empty().append('<span class="glyphicon glyphicon-chevron-up"></span>').append('收起地图');
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
	carObjs.push(obj);
	if(carObjs.length > 0){
		var mapApi = loadMapApi();
		mapApi.clearAllFeatures();
		map_addMarks(carObjs);
		mapApi.featureCenter();
	}
}
function reload() {
	var license = $("#license").val();
	var status = $("#status").val();
	var typeId = $("#typeId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,status:status,typeId:typeId,starttime:starttime,endtime:endtime};
	var url =basePath+"/com/supermap/alermList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
}
function resizeMapDiv(jqGridHeight){
	var wh = $(window).height()
	var top = $("#navbar").height();
	var footer = $(".footer-content").height();
	var tbHt = jqGridHeight;
	mapShowMoveInterval = wh-tbHt;
	var navBarWd = $("#monitorBtns").width();

    $("div[id*='mapDiv_']").css({width:(navBarWd),height:(wh-top-tbHt+50)});
	refreshMap();

}

