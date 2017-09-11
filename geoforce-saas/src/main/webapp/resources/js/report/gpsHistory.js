var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var mapWd = 86
var jqGridHeight = 0
$(function(){
	bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	jqGridHeight = $(grid_selector).getGridParam("height");
	
	resizeMapDiv();
	initMap();
	$("#mapDiv").hide();
})


/**
 * 初始化列表
 * @return
 */
function initGrid(){
	
	var colNames = ['车牌号','经度','纬度','carId','direction', '位置', '时速(km/h)','里程(km)','时间','操作'];
	var colModel = [{index:'license',name:'license', width:60},
	                {index:'longitude',name:'longitude', width:60},
					{index:'latitude',name:'latitude',width:60},
					{index:'carId',name:'carId',hidedlg:true,hidden:true},
					{index:'direction',name:'direction',hidedlg:true,hidden:true},
					{index:'addr',name:'addr', width:60},
					{index:'speed',name:'speed', width:60},
					{index:'mile',name:'mile', width:60},
					{index:'gpsTime',name:'gpsTime', width:60},
					{index:'view',name:'view', width:40}]
					
	
	var license = $("#license").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/historyList"
	loadGrid(grid_selector,url,"","历史轨迹统计",parms,colNames,colModel);
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
		sortable:false,
		viewsortcols:[false,'vertical',false],
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		multiselect: false,
        multiboxonly: false,
        jsonReader: {
			root: "rows",
			// total: "pageNum",// 表示总页数
			// page: "currentPageNum",//当前页
			// records: "totalNum",//总行数
            repeatitems : false,
           // id:"carId" 
        },  

		loadComplete : function() {
        	
		},
	    gridComplete:function(){
	    　　	var ids=jQuery(obj).jqGrid('getDataIDs');
	    　　	
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i]; 
	           // focusNum = "<a href='#' data-toggle='collapse' data-parent='#accordion' aria-expanded='true' aria-controls='collapseOne' class='ace-icon fa fa-pencil blue' style='color:#f60' alt='查看' onclick='mapView(\"" + id + "\")'>查看</a>";
	        	focusNum = "<a href='#' class='ace-icon fa fa-pencil blue' style='color:#f60' alt='查看' onclick='mapView(\"" + id + "\")'>查看</a>";
	            jQuery(obj).jqGrid('setRowData', ids[i], { view: focusNum});
	        }
	    　　},
		caption: title,
		autowidth: true

	});
	
}

/**
 * 加载列表分页
 * @return
 */
function loadNavGrid(){
    jQuery(grid_selector).jqGrid('navGrid',pager_selector,{ edit: false,add: false,del: false,search: false,refresh: false}).navButtonAdd(pager_selector,{  
		 caption:"查看地图",   
		 buttonicon : "ace-icon fa fa-search-plus grey",
		 onClickButton: function(){
    	 var mapDivAttr = $("#mapDiv").css("display");
    		if(mapDivAttr == 'none'){
    			//resizeMapDiv();
    			$("#mapDiv").show();
    			jQuery(grid_selector).jqGrid('setGridHeight','150')
    			$(".ui-pg-div").empty().append('<span class="glyphicon glyphicon-chevron-up"></span>').append('收起地图');
    			
    		}else{
    			$("#mapDiv").hide();
    			jQuery(grid_selector).jqGrid('setGridHeight',jqGridHeight)
    			$(".ui-pg-div").empty().append('<span class="ui-icon ace-icon fa fa-search-plus grey"></span>').append('查看地图');
    		}
		},   
		position:"last"  

	});
}

/**
 * 显示地图
 * @param id
 * @return
 */
function mapView(id){
	jQuery(grid_selector).jqGrid('setGridHeight','150')
	resizeMapDiv();
	$("#mapDiv").show();
	$(".ui-pg-div").empty().append('<span class="glyphicon glyphicon-chevron-up"></span>').append('收起地图');
	var datarow = jQuery("#grid-table").jqGrid('getRowData', id); 
	var obj = new Object();
	var carObjs = new Array();
	obj.carId = datarow.carId;
	obj.license = datarow.license;
	obj.lng = datarow.longitude;
	obj.lat = datarow.latitude;
	obj.speed = datarow.speed;
	obj.addr = datarow.addr;
	obj.mile = datarow.mile;
	obj.directionStr = datarow.direction;
	obj.gpsTimeStr = datarow.gpsTime;
	carObjs.push(obj);
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
	
	var license = $("#license").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	
	
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
	var parms={license:license,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/historyList"
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
  
   if(date1-date2 > 0 ){
	   return false;
   }
   return true;
}


/**
 * 设置地图大小并刷新
 * @return
 */
function resizeMapDiv(){
	var wh = $(window).height();
	var top = $("#navbar").height();
	var footer = $(".footer-content").height();
	var tbHt = $("#grid-table").height();

	var navBarWd = $("#monitorBtns").width();
    $("div[id*='mapDiv_']").css({width:(navBarWd),height:(wh-top-tbHt-170)});
	refreshMap();

}


/**
 * 导出数据操作
 * @return
 */
function exportData(){
	var row = $(grid_selector).getGridParam("reccount");
	if(row == 0){
		bootbox.alert("无数据导出!");
		return false;
	}
	var form = $("#searchForm");
	form.attr("action",ctx+"/com/supermap/historyExport");
	form.submit();
	
}
