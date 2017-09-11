var regionListTab = "#regionListTab";
var regionGridPager = "#regionGridPager";
var mapWd = 83;
var jqgridWidth;
var carGridWith = 86;
var regionName="";
var  type="";
$(document).ready(function(){
	//窗口变化自动
	$(window).resize(function(){
		resizeMapDiv();
	});
	resizeMapDiv();
	//初始化地图
	initMap();
	initRegionGrid()
	mapWd = 0;
	setCurActive("alarmFence");//in mapSupport.js当前操作在围栏报警页面
});
/**
 * 初始化jqGrid
 * @return
 */
function initRegionGrid(){
	createTable();
	loadNavGrid();
}
/**
 * 生成地图下方列表
 * @return
 */
function createTable(){
	jQuery(regionListTab).jqGrid({
		datatype:'json',
		height: '120',
		mtype: 'POST',
		colNames:['名称','报警类型','状态','开始时间','结束时间','操作时间','描述','id',"alarmTypeid","经纬度","名称","操作"],
		colModel:[{index:'regionName',name:'regionName', width:40},
					{index:'typeName',name:'typeName', width:20},
					{index :'region.status',name : 'region.status',width:20,sortable : false,
		                formatter : function(cellvalue, options, rowObject) {
	        				if (cellvalue == '0') {
	        					return "启用";
	        				} else {
	        					return "未启用";
	        					}
	        				}	
						},
					{index:'regionSet.startTime',name:'regionSet.startTime', width:60},
					{index:'regionSet.endTime',name:'regionSet.endTime', width:60},
					{index:'regionSet.operDate',name:'regionSet.operDate', width:60},
					{index:'region.remark',name:'region.remark', width:60},//,hidden:true
					{index:'id',name:'id', width:90,hidden:true},
					{index:'typeCode',name:'typeCode', width:90,hidden:true},
					{index:'lngLan',name:'lngLan', width:90,hidden:true},
					{index:'regionName',name:'regionName', width:90,hidden:true},
					{index:'LOOK',name:'LOOK', width:30}], 
		url:basePath+'/com/supermap/regionList',
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
		rowList:[10,20,30],
		pager : regionGridPager,
        jsonReader: { 
            root: "rows",
			// total: "pageNum",// 表示总页数
			// page: "currentPageNum",//当前页
			// records: "totalNum",//总行数
            repeatitems : true    
        },
        multiselect: false,
        multiboxonly: false,
        autowidth: true,
		rownumbers: true,
		altRows: true,
		viewrecords : true,
		caption: '车辆围栏设置',
		shrintToFit:true,
		onSelectRow: function(id){//选择一行
			var obj = $(this).jqGrid('getRowData',id);
			ReShowGPSInMap(obj);
		},
		gridComplete: function(){//加载表格完成	
			mapClear();//清除地图
			var obj = $("#regionListTab").jqGrid("getRowData");
		    jQuery(obj).each(function(){
		    	//地图显示围栏方法
		    	var polygonObj = new Object();
				var lngLats=this.lngLan;
				polygonObj.xyStr = lngLats;
				polygonObj.name=this.regionName;
				map_addPolygon(polygonObj);//地图上添加围栏 in mapSupport.js
				var id=this.id;   
				//后面操作按钮
	            focusNum = "<a href='#' class='glyphicon glyphicon-pencil'  alt='编辑' title='编辑' onclick='toEditRegionMessage(\"" + id + "\")'></a>&nbsp;&nbsp;&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='fa fa-car'  alt='关联车辆' title='关联车辆' onclick='carRegionFun(\"" + id + "\")'></a>&nbsp;&nbsp;&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='glyphicon glyphicon-trash'  alt='删除' title='删除' onclick='ajaxDeleRegion(\"" + id + "\")'></a>&nbsp;&nbsp;";
	            jQuery(regionListTab).jqGrid('setRowData', id, { LOOK: focusNum});
		    });
		    //列表的宽度
		    $(regionListTab).jqGrid( 'setGridWidth', $("#monitorBtns").width());
		},
		onHeaderClick: function(status){//表格显示 隐藏
			resizeMapDiv();
		},
		onSortCol: function(){
			setTimeout(function(){
				resizeMapDiv();
				
			},0);	
		},
		//点击行数据
		ondblClickRow:function(rowid,iRow,iCol,e){
	    	var data = jQuery(regionListTab).getRowData(rowid);
	    	//清除 地图 
	    	mapClear();
	    	var polygonObj = new Object();
			var lngLats=data.lngLan
			polygonObj.xyStr = lngLats;
			polygonObj.name=data.regionName;
			map_addPolygon(polygonObj);//地图上添加围栏 in mapSupport.js
	    	
	    },
	});
	$(regionListTab).jqGrid( 'setGridState',  "hidden");	
}
function loadNavGrid(){
	 jQuery(regionListTab).jqGrid('navGrid',regionGridPager,
				{ 	//navbar options
					edit: false ,
					editicon : 'ace-icon fa fa-pencil blue',
					add: false,
					addicon : 'ace-icon fa fa-plus-circle purple',
					del: false,
					delicon : 'ace-icon fa fa-trash-o red',
					search: false,
					searchicon : 'ace-icon fa fa-search orange',
					refresh: true,
					refreshicon : 'ace-icon fa fa-refresh green',
					view: false,
					viewicon : 'ace-icon fa fa-search-plus grey',
					carBind:true,
					carBindicon:'ace-icon fa fa-search-plus grey',
					carRegion:true,
					carRegionicon:'ace-icon fa fa-search-plus grey',
					searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
					searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
					addfunc:reload,
					editfunc:toEditRegionMessage,
					delfunc:ajaxDeleRegion,
					alertcap : "提示",
					alerttext : "请选择一行记录"
				}
			) 
}
/**
 * 查询
 * @return
 */
function reload() {
	mapClear();
	var name = $('#name').val();
	var alarmType = $("#typeId").val();
	var status = $("#status").val();
	var parms={name:name,alarmType:alarmType,status:status};
	var url=basePath+"/com/supermap/regionList";
	jQuery(regionListTab).jqGrid('setGridParam',{url:url,page:1,postData:parms} 
	).trigger("reloadGrid");
}
/**
 * 绘制围栏
 * @return
 */
function drawFenceInit(editFlag){
	if(editFlag == "add"){
		mapClear();//清除地图
		$("#editFlag").val("add");
		//in mapSupport.js 激活多边形绘制
		map_drawPolygon();
		$("#editFlag").val("add");
	}else if(editFlag == "edit"){
		//in mapSupport.js 激活多边形编辑
		bootbox.alert("提示：点击围栏进行修改,双击完成修改;页面刷新;");
		map_editFeature();
		$("#editFlag").val("edit");
	}
}
/**
 * 绘制完成后调用方法
 * @param region
 * @return
 */
function addFence(region,name){//绘制围栏完成后 返回
	$("#region").val('');
	$("#region").val(region);
	//in mapSupport.js 激活多边形编辑
	var editFlag =$("#editFlag").val();
	if(editFlag == "add"){
		toAddRefion();//添加初始化
	}else if(editFlag == "edit"){
		updateRgionLanLen(name);
	}
}
/**
 * 添加围栏初始化方法
 * @return
 */
function toAddRefion(){
	$('#regionDiv').modal('show');
	$("#warning").hide();//非空提示div
	var txts = document.getElementById('regionDiv').getElementsByTagName("input"); 
	for(var i=0;i<txts.length;i++){
			txts[i].value =""; //text 清空 
	} 
	$('[name=workTime]').removeAttr('checked');
}
	
/**
 * 修改围栏
 * @param name
 * @return
 */
 function updateRgionLanLen(name){
	 var region =$("#region").val();
	 $.ajax({
			url :basePath+"/com/supermap/updateRgionLanLen?ram=" + Math.random()+"&lanLen="+region+"&name="+name,
			type : "POST",
			success : function(obj) {
					reload();
			}
		});
 }

/**
 * 编辑围栏信息初始化
 * @return
 */
function toEditRegionMessage(id){
		$.post(basePath+"/com/supermap/regionInfo",{regionId:id},function(data){
		var status=data.region.status;
		var wookTIime=data.regionSet.week;
		var remark=data.region.remark;
			$('#region').val(data.region.region);
			$('#id').val(data.regionSet.id);
			$('#regionName').val(data.region.name);
			$('#type').val(data.regionSet.alarmType);
			$('#staTime').val(data.regionSet.startTime);
			$('#endTime').val(data.regionSet.endTime);
			$('#wookTIime').val(wookTIime);
			var str = wookTIime;
			var arr = new Array();
			arr = str.split(",");
			for(var i=0;i<arr.length;i++){
				$("#"+arr[i]).attr("checked",'true');//选中所有奇数   
			}
			$('#select').val(status);
			$('#remark').val(remark);
		});
		//显示弹出层
		$("#regionDiv").modal('show');
		//编辑操作
		$("#editFlag").val("edit");
}
/**
 * 页面提示信息
 * @return
 */
function validate(){
	var name = $("#regionName").val();
	if(name == ""|| name.length==0){
		showWarning("围栏名称不能为空！")
		return false;
	}
	var value = $("#type").val();
	if(value == ""|| value.length==0){
		showWarning("报警类型不能为空！")
		return false;
	}
	var staTime = $("#staTime").val();
	if(staTime == ""|| staTime.length==0){
		showWarning("请选择开始时间！")
		return false;
	}
	var endTime = $("#endTime").val();
	if(endTime == ""|| endTime.length==0){
		showWarning("请选择结束时间！")
		return false;
	}
	var inputs = document.getElementsByName("workTime");
	if(inputs == ""|| inputs.length==0){
		showWarning("请选择有效日期！")
		return false;
	}
	var returnValue="";
	returnValue=CheckCode(name);
	if(returnValue==false){
		showWarning("不能输入特殊字符");
		return false;
	}
	return true;
}
function showWarning(content){
	$("#content").empty().append(content)
	$("#warning").show();
}
/**
 * 特殊字符验证
 * @param t
 * @return
 */
function CheckCode(str) {  
	 var myReg = /^[^@\/\'\\\"#$%&\^\*]+$/;
     if(myReg.test(str)) 
    	 return true; 
     return false; 
}
/**
 * 判断提交方法
 * @return
 */
function typeFun(){
	$("#warning").hide();//非空提示div
	var falg=validate();
	var type = $("#editFlag").val();
	if(falg){
		if(type=="add"){
			ajaxAddRegion();
		}else if(type=="edit") {
			ajaxEditRegion();
		}
	}else{
		return false;
	}
}
/**
 * 添加
 * @return
 */
function ajaxAddRegion(){
	var alarmIdArray = new Array();
	var workTimes = document.getElementsByName('workTime');
	for(var i=0;i<workTimes.length;i++){
		 if(workTimes[i].checked)
	 {   
			 alarmIdArray.push(workTimes[i].id);
		}
	}
	var workTimeIds = alarmIdArray.join(",");
	var region =$("#region").val();
	$.ajax({
		url : basePath+"/com/supermap/addRgionSet?ram=" + Math.random()+ "&wookTIime="+workTimeIds+"&region="+region,
		type : "POST",
		data :$("#regionForm").serialize(),
		success : function(obj) {
		if (obj.status == 1) {
			$('#regionDiv').modal('toggle');
			bootbox.alert("操作成功!");
			reload();
		}else if(obj.status==2){
			$('#regionDiv').modal('toggle');
			bootbox.alert("绘制的围栏不符合规定 请重新绘制");
			reload();
		}
		else {
			var msg = obj.info
			showWarning(msg)
			return false;
		}
			/*if (obj.flag == "ok") {
				
				bootbox.alert("操作成功!");
				reload();
			}else if(obj.flag == "no"){
				bootbox.alert("围栏已经存在，请修改围栏名称!");
				reload();
			} 
			else {
				bootbox.alert("操作失败！");
				 
			}*/
		}
	});
}
/**
 * 编辑围栏提交
 * @param regionSetIdValue
 * @return
 */
function ajaxEditRegion(){
	var alarmIdArray = new Array();
	var workTimes = document.getElementsByName('workTime');
	for(var i=0;i<workTimes.length;i++){
		 if(workTimes[i].checked)
	 {   
			 alarmIdArray.push(workTimes[i].id);
		}
	}
	var workTimeIds = alarmIdArray.join(",");
	$.ajax({
		type : "POST",
		url : basePath+"/com/supermap/editRegionMessage?ram=" + Math.random()+ "&wookTIime="+workTimeIds,
		data : $("#regionForm").serialize(),
		success : function(obj) {
		if (obj.status == 1) {
			$('#regionDiv').modal('toggle');
			bootbox.alert("操作成功!");
			reload();
		} else {
			var msg = obj.info
			showWarning(msg)
			return false;
			
		}
			/*if (obj.flag == "ok") {
				$('#regionDiv').modal('toggle');
				bootbox.alert("操作成功!");
				 reload();
			}else if(obj.flag == "no"){
				bootbox.alert("围栏已经存在，请修改围栏名称!");
			} 
			else {
				bootbox.alert("操作失败！");
			}	*/
		}
	});
}
/**
 * 删除围栏
 * @param 
 * @return
 */
function ajaxDeleRegion(id){
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url :basePath+"/com/supermap/delRegion?ram=" + Math.random() + "&regionSetids=" + id,
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
	$(regionListTab).jqGrid( 'setGridWidth',  navBarWd+mapWd);
	$("div[id*='mapDiv_']").css({width:(navBarWd+mapWd),height:(wh-top-footer-navBarh-tbHt)});
	setTimeout(function() {
		if($("#monitorBtns").width() < navBarWd){
			$("div[id*='mapDiv_']").css({width:($("#monitorBtns").width()+mapWd),height:(wh-top-footer-navBarh-tbHt)});
			$(regionListTab).jqGrid( 'setGridWidth', $("#monitorBtns").width()+mapWd);
		}
	},100);
	refreshMap();
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
/**
 * 车辆关联围栏
 * @param id
 * @return
 */
function carRegionFun(id){
		var data = jQuery("#regionListTab").getRowData(id);
	   createCarTree();
	   regionTableReload(id,data.typeCode);
	   $('#regionlinkCarsDiv').modal('show');
}



