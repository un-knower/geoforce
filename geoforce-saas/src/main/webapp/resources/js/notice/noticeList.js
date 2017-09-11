var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var gridWith = 86;
$(function(){
	//bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	
})

/**
 * 初始化列表
 * @return
 */
function initGrid(){
	
	var colNames = ['标题','内容','发送人数', '发送人', '发送时间'];
	var colModel = [{index:'title',name:'title', width:60},
					{index:'content',name:'content', width:60},
					{index:'personNum',name:'personNum', width:60},
					{index:'pubUserName',name:'pubUserName', width:60},
					{index:'pubDate',name:'pubDate', width:60}]
					
	var name = $("#name").val();
	var parms={name:name};
	var url =ctx+"/com/supermap/noticeList"
	loadGrid(grid_selector,url,"","发送信息",parms,colNames,colModel);
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
			root: "result",
			total: "pageNum",// 表示总页数
			page: "currentPageNum",//当前页
			records: "totalNum",//总行数
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
	var name = $("#name").val();
	var parms={name:name};
	var url =ctx+"/com/supermap/noticeList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}

