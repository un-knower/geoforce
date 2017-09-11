var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var carGridWith = 86;
$(function(){
    initGrid();
	loadNavGrid();
});
function initGrid(){
	var colNames = ['员工名称','门店名称','id','status','计划时间','状态', '计划内容','审批时间','审批内容','操作'];
	var colModel = [{index:'person.name',name:'person.name', width:60},
	                {index:'personStore.name',name:'personStore.name', width:60},
	                {index:'id',name:'id', width:60,hidden:true},
	                {index:'status',name:'status', width:60,hidden:true},
	                {index:'personPlan.begintdate',name:'personPlan.begintdate', width:60},
					{index :'personPlan.status',name : 'personPlan.status',width:40,sortable : false,
	                formatter : function(cellvalue, options, rowObject) {
        				if (cellvalue == '0') {
        					return "待审批";
        				} else if(cellvalue == '1') {
        					return "审批通过";
        					}else{
        						return "驳回";
        					}
        				}	
					},
					{index:'personPlan.content',name:'personPlan.content', width:60},
					{index:'personPlan.sureDate',name:'personPlan.sureDate', width:60},
					{index:'personPlan.opinion',name:'personPlan.opinion', width:100},
					{index:'view',name:'view', width:40}];
	var status = $("#status").val();
	var url =basePath+"/com/supermap/planList?status="+status;
	loadGrid(grid_selector,url,"","工作计划列表",colNames,colModel);
}
function loadGrid(obj, url,data ,title,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height:  function(){var h=getWindowHeight()-240;return h+"";}(),
		width: '100%',
		loadtext : "加载中...",
		emptyrecords : "无记录",
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
		scrollrows: false, // 是否显示行滚动条 
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
            repeatitems : false    
        },
        loadComplete : function() {
        	var table = this;
			setTimeout(function() {
				updatePagerIcons(table);
			}, 0);
		}, gridComplete:function(){
	    　	　	var ids=jQuery(obj).jqGrid('getDataIDs');
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i]; 
	        	var data = jQuery("#grid-table").getRowData(id);
	        	if(data.status==0){
	            focusNum = "<a class='glyphicon glyphicon-saved' style='cursor:hand' alt='通过' title='通过' onclick='pass(\"" + id + "\")'>通过 </a>";
	            focusNum += "<a  class='glyphicon glyphicon-record' style='cursor:hand'  alt='驳回' title='驳回' onclick='reject(\"" + id + "\")'>驳回</a>";
	            jQuery(obj).jqGrid('setRowData', ids[i], { view: focusNum});
	        	}
	        }
	    　　},
		caption: title,
		autowidth: true
	});
	
	$(window).triggerHandler('resize.jqGrid');
}
function loadNavGrid(){
    jQuery(grid_selector).jqGrid('navGrid',pager_selector,
		{ 	//navbar options
			edit: false,
			editicon : 'ace-icon fa fa-pencil blue',
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
			searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
			searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
			addfunc:test,
			editfunc:test,
			delfunc:test,
			alertcap : "提示",
			alerttext : "请选择一行记录"
		}
	);}

/**
 * 驳回初始化；
 */
var rejectPersonIs=null;
function reject(id){
	if(id==null){
		return;
	}
	rejectPersonIs=id;
	var data = jQuery("#grid-table").getRowData(id);
	if(data.status==1){
		bootbox.alert("审批已经通过,不能重复处理；");
	}else if(data.status==2){
		bootbox.alert("已经驳回不能再次处理；");
	}else{
		$("#alarmOpinionText").val('');
		$('#personPlanDiv').modal('show');
	}
}
function pass(id){
	if(id==null){
		return;
	}
	rejectPersonIs=id;
	var data = jQuery("#grid-table").getRowData(id);
	if(data.status==1){
		bootbox.alert("审批已经通过,不能重复处理；");
	}else if(data.status==2){
		bootbox.alert("已经驳回不能再次处理；");
	}else{
		var ret=ajaxReject(id,"1","审批通过");
	}
}
function saveReject(){
	var opinion =$("#alarmOpinionText").val();
	if(opinion == null || opinion == "" ){
		bootbox.alert("请填写驳回意见；");
		return false;
	}
	if(opinion.length > 60){
		bootbox.alert("驳回意见不能大于60");
		return false;
	}
	ajaxReject(rejectPersonIs,"2",opinion);
	$('#personPlanDiv').modal('toggle');
	rejectPersonIs=null;
}

function ajaxReject(planId,status,opinion){
	$.ajax({
		url : basePath+"/com/supermap/updatePersonPlan?planId=" + planId+ "&status=" + status+ "&opinion=" + opinion,
		type : "POST",
		success : function(obj) {
			if (obj.status == 1) {
				bootbox.alert("操作成功!");
				reload();
			} else {
				var msg = obj.info
				showWarning(msg)
				return false;
				
			}
			
		}
	});
}
/**
 * 页面查询
 * @return
 */
function reload() {
	var name = $('#name').val();
	var status = $("#status").val();
	var begindate =$("#begindate").val();
	var enddate = $('#enddate').val();
	var status = $("#status").val();
	var storeNanme = $("#storeNanme").val();
	var parms={name:name,status:status,storeNanme:storeNanme,begindate:begindate,enddate:enddate};
	var url =basePath+"/com/supermap/planList";
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	$("#grid-table").trigger("reloadGrid");
}
function test(){
	
}


