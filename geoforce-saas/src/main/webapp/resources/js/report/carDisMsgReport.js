var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var gridWith = 86;
$(function(){
	bootstrapAutocomplete("license",basePath+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	
})

/**
 * 初始化列表
 * @return
 */
function initGrid(){
	
	var colNames = ['车牌号','消息','消息类型', '描述状态', '发送时间'];
	var colModel = [{index:'license',name:'license', width:60},
					{index:'content',name:'content', width:60},
					{index:'type',name:'type', width:60,formatter:function(cellvalue, options, rowObject) {if (cellvalue == '1') {return "通知";}else{return "任务";}}},
					{index:'status',name:'status', width:60,formatter:function(cellvalue, options, rowObject) {if (cellvalue == '1') {return "成功";}else{return "失败";}}},
					{index:'sendDate',name:'sendDate', width:60}]
					
	var types = $("#type").val();
	var license = $("#license").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,type:types,starttime:starttime,endtime:endtime};
	var url =basePath+"/com/supermap/msgList"
	loadGrid(grid_selector,url,"","调度信息",parms,colNames,colModel);
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
function reload() {
	var types = $("#type").val();
	var license = $("#license").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,type:types,starttime:starttime,endtime:endtime};
	var url =basePath+"/com/supermap/msgList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}

