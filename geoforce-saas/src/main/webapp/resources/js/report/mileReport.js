var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var gridWith = 86;
$(function(){
	bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	initComboDeptTree($('#deptTree'));
	
	
})

/**
 * 初始化列表
 * @return
 */
function initGrid(){
	
	var colNames = ['车牌号','部门','开始时间', '开始里程 /km', '结束时间','结束里程/km','行驶总里程/km'];
	var colModel = [{index:'license',name:'license', width:60},
	                {index:'deptName',name:'deptName', width:60},
					{index:'startGpsTime',name:'startGpsTime',width:60},
					{index:'startMile',name:'startMile', width:60},
					{index:'endGpsTime',name:'endGpsTime', width:60},
					{index:'endMile',name:'endMile', width:60},
					{index:'diffMile',name:'diffMile', width:60}]
					
	var deptCode = $("#deptCode").val();
	var license = $("#license").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,deptCode:deptCode,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/mileList"
	loadGrid(grid_selector,url,"","里程统计",parms,colNames,colModel);
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
		height:  function(){var h=getWindowHeight()-255;return h+"";}(),
		
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		sortable:false,
		viewsortcols:[false,'vertical',false],
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
 * 搜索刷新
 * @return
 */
function reload() {
	var deptId = $("#deptId").val();
	var license = $("#license").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	if(starttime=="" || starttime.length==0){
		bootbox.alert("开始时间 不能为空!");
		return false;
	}
	if(endtime=="" || starttime.length==0){
		bootbox.alert("结束时间不能为空!");
		return false;
	}
	if(!comptime(starttime,endtime)){
		bootbox.alert("开始时间不能大于结束时间!");
		return false;
	}
	var starttimes = starttime.split(" ")[0].split("-");
	var endtimes = endtime.split(" ")[0].split("-");
	if(starttimes[2] != endtimes[2]){
		bootbox.alert("查询时间不能跨天!");
		return false;
	}
	var parms={license:license,deptId:deptId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/mileList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}
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

/**
 * 判断开始时间结束时间是否正确
 * @param beginTime
 * @param endTime
 * @return
 */
function comptime(beginTime,endTime) {
var date1 = new Date(beginTime.replace(/\-/g, "\/"));
   var date2 = new Date(endTime.replace(/\-/g, "\/"));
  
   if(date1-date2 > 0 ){
	   return false;
   }
   return true;
}

