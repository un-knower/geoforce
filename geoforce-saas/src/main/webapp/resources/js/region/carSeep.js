var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var carGridWith = 86;
$(function(){
	$(window).on('resize.jqGrid', function () {
		$(grid_selector).jqGrid( 'setGridWidth', $("#monitorBtns").width()+carGridWith );
		carGridWith=1;
    });
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
		if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
			setTimeout(function() {
				$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
			}, 0);
		}	
    })
    initGrid();
	loadNavGrid();
});
function initGrid(){
	var colNames = ['id','名称','状态', '限速', '开始时间','结束时间','描述'];
	var colModel = [{index:'id',name:'id', width:60,hidden:true},
	                {index:'name',name:'name', width:60},
					{index :'status',name : 'status',width:40,sortable : false,
	                formatter : function(cellvalue, options, rowObject) {
        				if (cellvalue == '0') {
        					return "未启用";
        				} else {
        					return "启用";
        					}
        				}	
					},
					{index:'speed',name:'speed', width:60},
					{index:'startTime',name:'startTime', width:60},
					{index:'endTime',name:'endTime', width:60},
					{index:'remark',name:'remark', width:60}];
	var url =basePath+"/com/supermap/seepdingList";
	loadGrid(grid_selector,url,"","车辆超速报警",colNames,colModel);
}
function loadGrid(obj, url,data ,title,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height:  function(){var h=getWindowHeight()-255;return h+"";}(),
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
			root: "rows",
			// total: "pageNum",// 表示总页数
			// page: "currentPageNum",//当前页
			// records: "totalNum",//总行数
            repeatitems : false    
        },
        loadComplete : function() {
        	var table = this;
			setTimeout(function() {
				updatePagerIcons(table);
			}, 0);
		},
		caption: title,
		autowidth: true
	});
	caption : "超速报警信息"
	$(window).triggerHandler('resize.jqGrid');
}
function loadNavGrid(){
    jQuery(grid_selector).jqGrid('navGrid',pager_selector,
		{ 	//navbar options
			edit: true,
			editicon : 'ace-icon fa fa-pencil blue',
			add: true,
			addicon : 'ace-icon fa fa-plus-circle purple',
			del: true,
			delicon : 'ace-icon fa fa-trash-o red',
			search: false,
			searchicon : 'ace-icon fa fa-search orange',
			refresh: true,
			refreshicon : 'ace-icon fa fa-refresh green',
			view: true,
			viewicon : 'ace-icon fa fa-search-plus grey',
			searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
			searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
			addfunc:addSeep,
			editfunc:updartSeep,
			delfunc:speedDel,
			alertcap : "提示",
			alerttext : "请选择一行记录"
		}
	)
	.navButtonAdd(pager_selector,{  
		 caption:"超速关联车辆",   
		 buttonicon:"ace-icon fa fa-pencil blue", 
		 onClickButton: function(){
			var regionId = jQuery(grid_selector).jqGrid('getGridParam','selrow');
			if(regionId==null){
				bootbox.alert("请选择一条数据!");
			}else{
				carRegionFun(regionId)
			}
		},   
		position:"last"  
	});
}
/**
 * 超速车辆关联围栏
 * @param id
 * @return
 */
function carRegionFun(id){
	 var rows = jQuery(grid_selector).jqGrid('getGridParam','selarrrow');
	 //判断是否选择多个车辆
	 if(rows.length>1){
		 bootbox.alert("只能选择一条数据！");
		 return;
	 }
	   $('#seeplinkCarsDiv').modal('show');
	   createCarTree();
	   tableReload(id);
}
/**
 * 添加初始化
 * @return
 */
var type=null;
function addSeep(){
	type="add";
	$('#seepDiv').modal('show');//div 显示
	$("#warning").hide();//非空提示div
	var txts = document.getElementById('seepDiv').getElementsByTagName("input"); 
	for(var i=0;i<txts.length;i++){
			txts[i].value =""; //text 清空 
	} 
	$("[name=workTime]").removeAttr("checked");
	
}
function updartSeep(id){
	$("#warning").hide();//非空提示div
	$("[name=workTime]").removeAttr("checked");
	$.post(basePath+"/com/supermap/editInit",{speedId:id},function(data){
			$('#seppName').val(data.name);
			$('#seepId').val(data.id);
			$('#seepValue').val(data.speed);
			$('#seepStatus').val(data.status);
			$('#seepStaTime').val(data.startTime);
			$('#seepEndTime').val(data.endTime);
			var str = data.week;
			var arr = new Array();
			arr = str.split(",");
			for(var i=0;i<arr.length;i++){
					$("#"+arr[i]).prop("checked",true);//选中所有奇数   
				}
			
			$('#remark').val(data.remark);
		});
		$("#seepDiv").modal('show');
		type="edit";
	
}
/**
 * 提交判断
 * @return
 */
function typeFun(){
		var falg=validate();
		if(falg){
			if(type=="add"){
				ajaxAddSeep();
			}else if(type=="edit") {
				ajaxUpateSeep();
			}
		}else{
			return false;
		}
	}
/**
 * 添加提交
 * @return
 */
  function ajaxAddSeep(){
	  var alarmIdArray = new Array();
	  var alarmIds=null;
		var addCarSeepWook  = document.getElementsByName("workTime");
		var i=0;
		for(i=0;i<addCarSeepWook.length;i++){
			 if(addCarSeepWook[i].checked)
			 {   
				 alarmIdArray.push(addCarSeepWook[i].id);
			}
		}
		alarmIds = alarmIdArray.join(",");
		$.ajax({
			url : basePath+"/com/supermap/addCarSeepding?ram=" + Math.random()+ "&wookTIime=" + alarmIds,
			type : "POST",
			data :$("#seepForm").serialize(),
			success : function(obj) {
			if (obj.status == 1) {
				bootbox.alert("操作成功!");
				$('#seepDiv').modal('toggle');
				reload();
			}else {
				var msg = obj.info;
				showWarning(msg)
				return false;
				}
			}
		});
		alarmIds=null;
  }
  
  /**
   * 修改提交
   * @return
   */
 function ajaxUpateSeep(){
	 var alarmIdArray = new Array();
		var inputs = document.getElementsByName("workTime");
		for(var i=0;i<inputs.length;i++){
			 if(inputs[i].checked)
		 {   
				 alarmIdArray.push(inputs[i].id);
			}
		}
		var alarmIds = alarmIdArray.join(",");
		$.ajax({
			type : "POST",
			url : basePath+"/com/supermap/updateCarSeepding?ram=" + Math.random()+"&wookTIime=" + alarmIds,
			data : $("#seepForm").serialize(),
			success : function(obj) {
			if (obj.status == 1) {
				bootbox.alert("操作成功!");
				$('#seepDiv').modal('toggle');
				reload();
			} else {
				var msg = obj.info
				showWarning(msg);
				return false;
			}
			}
		});
		alarmIds=null;
	 
 }
 function simTest(sim){
		var regPartton=/1[3-8]+\d{9}/;
		if(regPartton.test(sim)){
			return true;
		}
		return false;
	}
function validate(){
	var name = $('#seppName').val();
	if(name == ""|| name.length==0){
		showWarning("请填写超速名称！")
		return false;
	}
	var value = $("#seepValue").val();
	if(value == ""|| value.length==0){
		showWarning("请填写限速值！")
		return false;
	}
	var regPartton = /^[0-9]+.?[0-9]*$/;  
//	var regPartton =/^[0-9]{0}([0-9]|[.])+$/;
	if(regPartton.test(value)){
		return true;
	}else {
		showWarning("限速值只能是数字！")
		return false;
	}
	return false;
	var status = $("#seepStatus").val();
	if(status == ""|| status.length==0){
		showWarning("请选择状态！")
		return false;
	}
	var staTime = $("#seepStaTime").val();
	if(staTime == ""|| staTime.length==0){
		showWarning("请选择开始时间！")
		return false;
	}
	var endTime = $("#seepEndTime").val();
	if(endTime == ""|| endTime.length==0){
		showWarning("请选择结束时间！")
		return false;
	}
	$("#warning").hide();//非空提示div
	return true;
}
/**
 * 显示错误
 * @param content
 * @return
 */
function showWarning(content){
	$("#content").empty().append(content)
	$("#warning").show();
}

function speedDel(){
	 var rows = jQuery(grid_selector).jqGrid('getGridParam','selarrrow');
		var carIdArray = new Array();
		if(rows){
			var len = rows.length;
			for(var i=0;i<len;i++){
				carIdArray.push(rows[i]);
			}
		}
		var speedIds = carIdArray.join(",");
		ajaxDelSpeeds(speedIds);
}
/**
 * 删除
 * @return
 */
function ajaxDelSpeeds(speedIds){
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url :basePath+"/com/supermap/delSpeeds?ram=" + Math.random() + "&speedIds="+speedIds,
				success : function(obj) {
					if (obj.flag == "ok") {
						bootbox.alert("操作成功!");
						reload();
					} else {
						bootbox.alert("操作失败!");
					}
				}
			});
		}
	});
};
/**
 * 页面查询
 * @return
 */
function reload() {
	var speedName = $('#name').val();
	var speedStatus = $("#status").val();
	var parms={speedName:speedName,speedStatus:speedStatus};
	var url =basePath+"/com/supermap/seepdingList";
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	$("#grid-table").trigger("reloadGrid");
}

function updatePagerIcons(table) {
	var replacement = {
		'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

		if ($class in replacement)
			icon.attr('class', 'ui-icon ' + replacement[$class]);
	});
}



