var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var gridWith = 86;
$(function(){
	
    initGrid();
	loadNavGrid();
	initComboDeptTree($('#deptTree'));
})
function initGrid(){
	$("#grid-table").empty();
	var colNames = ['姓名','部门','驾照', '驾照有效期', '关联车辆','手机号码','操作时间'];
	var colModel = [
	    			{index:'name',name:'name', width:60},
	    			{index:'deptName',name:'deptName', width:60},
	    			{index:'license',name:'license', width:60},
	    			{index:'licenseDate',name:'licenseDate', width:80},
	    			{index:'carNames',name:'carNames', width:60},
	    			{index:'phone',name:'phone', width:60},
	    			{index:'modifyDate',name:'modifyDate', width:60}];
	//var parms = $("#searchForm").serialize();
	var name = $("#name").val();
	var depId = $("#deptId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={name:name,deptId:depId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/driverList"
	loadGrid(grid_selector,url,"","司机信息",parms,colNames,colModel);
}
function loadGrid(obj, url,data ,title,postData,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height:  function(){
		var h=getWindowHeight()-255;
		return h+"";}(),
		postData: postData,
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		sortable:false,
		viewsortcols:[false,'vertical',false],
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
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
        	
		},
		onSelectRow: function(id){
			 $("#driverId").val(id); 
	    },
		caption: title,
		autowidth: true

	});
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
			view: false,
			viewicon : 'ace-icon fa fa-search-plus grey',
			searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
			searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
			addfunc:toAddDriver,
			editfunc:toEditDriver,
			delfunc:delDriver,
			alertcap : "提示",
			alerttext : "请选择一行记录"
		}
	).navButtonAdd(pager_selector,{  
		 caption:"调整部门",   
		 buttonicon : "ace-icon fa fa-check-square-o orange",
		 onClickButton: function(){
			changDept('')
		},   
		position:"last"  

	});
}
function delDriver(id){
	var ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
	if(ids.length == 0){
		bootbox.alert("请选择要删除的司机!");
		return false;
	}
	var id = ids.toString();
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : ctx+"/com/supermap/delDriver?ram=" + Math.random() + "&driverIds=" + id,
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
	
}


function toAddDriver(){
	bootbox.dialog({
		  message: $("#addDriver").html(),
		  title: "添加司机",
		  buttons: {
		    danger: {
		      label: "取消",
		      className: "btn-default",
		      callback: function() {
		 		bootbox.hideAll();
		 		
		      }
		    },
		    success: {
		      label: "提交",
		      className: "btn-success",
		      callback: function() {
		    	if(validate()){
		    		addDriver();
		    	}
	    		return false;
		    	
		      }
		    }
		    
		  }
	});
	
}

function addDriver(){
	$.ajax({
		url : ctx+"/com/supermap/addDriver?ram=" + Math.random(),
		type : "POST",
		data :$(".bootbox #addDriverForm").serialize(),
		success : function(obj) {
			if (obj.flag == "ok") {
				bootbox.hideAll();
				bootbox.alert("操作成功!");
				reload();
			} else {
				var flag = obj.flag
				if(flag =="err_04"){
					showWarning("驾照编码重复！")
					return false;
				}
				if(flag == "err_05"){
					showWarning("司机姓名已存在！")
					return false;
				}
				
			}
		}
	});
}
function toEditDriver(id){
	var ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
	if(ids==""){
		bootbox.alert("请选择一行数据进行编辑!");
		return false;
	}
		
	if(ids.length>1){
		bootbox.alert("请选择一行数据进行编辑!");
		return false;
	}
	var datarow = jQuery("#grid-table").getRowData(ids);
	$.post(ctx+"/com/supermap/driverInfo",{driverId:ids[0]},function(data){
		var id = data.id;
		var license = data.license;
		var deptName = datarow.deptName;
		var name = data.name;
		var age = data.age;
		var address = data.address;
		var zipcode = data.zipcode;
		var starttime = data.licenseSdate;
		var endtime = data.licenseEdate;
		var sex = data.sex;
		var phone = data.phone;
		var deptId = data.deptId;
		
		
		var html = $("#addDriver").html();
		var str = $(html).find("#driverId").val(id).end()
		                 .find("#license").val(license).end()
		                 .find("#boxdeptName").val(deptName).end()
		                 .find("#name").val(name).end()
		                 .find("#age").val(age).end()
		                 .find("#address").val(address).end()
		                 .find("#zipcode").val(zipcode).end()
		                 .find("#starttime").val(starttime).end()
		                 .find("#endtime").val(endtime).end()
		                 .find("#sex"+sex).attr("selected", "selected").end()
		                 .find("#phone").val(phone).end()
		                 .find("#boxdepId").val(deptId).end()
		bootbox.dialog({
			  message: str,
			  title: "修改司机",
			  buttons: {
			    danger: {
			      label: "取消",
			      className: "btn-default",
			      callback: function() {
//			 		
			 		return;
			      }
			    },
			    success: {
			      label: "修改",
			      className: "btn-success",
			      callback: function() {
			    	
			        if(validate()){
			    		editDriver();
			        }
			    	return false;
			    	
			      }
			    }
			  }
		});
		
		
	});
	
}
function editDriver(){
	$.ajax({
		type : "POST",
		url : ctx+"/com/supermap/updateDriver?ram=" + Math.random(),
		data : $(".bootbox #addDriverForm").serialize(),
		success : function(obj) {
			if (obj.flag == "ok") {
				bootbox.hideAll();
				bootbox.alert("操作成功!");
				reload();
			} else {
				var flag = obj.flag
				if(flag =="err_04"){
					showWarning("驾照编码重复！")
					return false;
				}
				if(flag == "err_05"){
					showWarning("司机姓名已存在！")
					return false;
				};
			}
		}
	});
	
}
function reload() {
	var name = $("#name").val();
	var depId = $("#deptId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={name:name,deptId:depId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/driverList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
}

function validate(){
	var name = $(".bootbox #name").val();
	if(name == ""|| name.length==0){
		showWarning("请填司机姓名！")
		return false;
	}
	var s = name.substr(0, 1)
	if(!isNaN(s)){
		showWarning("司机姓名不能数字开头！")
		return false;
	}
	var reg = new RegExp("[『』`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|%_{}【】‘；：”“'。，、？]");
	if(reg.test(name)){
		showWarning("司机姓名不能有特殊字符！")
		return false;
	}
	var age = $(".bootbox #age").val();
	if(isNaN(age)){
		showWarning("年龄格式不正确！")
		return false;
	}
	var license = $(".bootbox #license").val();
	if(license == ""|| license.length==0){
		showWarning("驾驶证不能为空！")
		return false;
	}
	if(isNaN(license)){
		showWarning("驾驶证号格式不正确！")
		return false;
	}
	var zipcode = $(".bootbox #zipcode").val();
	if(isNaN(zipcode)){
		showWarning("邮编格式不正确！")
		return false;
	}
	var beginTime = $(".bootbox #starttime").val();
	var endTime = $(".bootbox #endtime").val();
	if(beginTime==""||endTime==""){
		showWarning("驾照有效期开始时间或结束时间不能为空！")
		return false;
	}
	
	if(!comptime(beginTime,endTime)){
		showWarning("驾照有效期开始时间不能大于结束时间！")
		return false;
	}
	
	var phone = $(".bootbox #phone").val();
	if(phone ==""){
		showWarning("请填写手机号码！")
		return false;
	}
	if(!simTest(phone)){
		showWarning("电话格式错误！")
		return false;
	}
	return true;
	
}

function comptime(beginTime,endTime) {
	 var date1 = new Date(beginTime.replace(/\-/g, "\/"));
     var date2 = new Date(endTime.replace(/\-/g, "\/"));
     if(date1-date2 > 0 )
     return false;
     
     return true;
}

function simTest(sim){
	var regPartton=/1[3-8]+\d{9}/;
	if(regPartton.test(sim)){
		return true;
	}
	return false;
}

function showWarning(content){
	$(".bootbox #content").empty().append(content)
	$(".bootbox #warning").show();
}

function checkPone(){
	var regPartton=/1[3-8]+\d{9}/;
	if(!regPartton.test(str)){
		return false;
	}
}


function onClick(e, treeId, treeNode) {
	var source = $('#deptTree').attr("source");
	
	if(treeNode){
		var deptId = treeNode.id;
		var deptCode = treeNode.code;
		var deptName = treeNode.name;
		if(source=='page'){
			$("#deptId").val(deptId);
			$("#deptName").val(deptName)
		}if(source=='bootbox'){
			$(".bootbox #boxdepId").val(deptId);
			$(".bootbox #boxdeptName").val(deptName)
		}if(source == 'myModa2'){
			$("#changDeptId").val(deptId);
		}
	}
	hideMenu();
}
function showMenu() {
	var deptObj = $("#deptName");
	var deptOffset = $("#deptName").offset();
	$('#deptTree').attr("source","page");
	$('#deptTree').css("background-color", " background: none repeat scroll 0 0 #f0f6e4;");
	$("#menuContent").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");
	
	$("body").bind("mousedown", onBodyDown);
}

function showboxMenu() {
	var deptObj = $(".bootbox #boxdeptName");
	var deptOffset = $(".bootbox #boxdeptName").offset();
	$('#deptTree').attr("source","bootbox");
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


function changDept(id){
	var ids = "";
	if(id == ""){
		ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
		id = ids;
		if(ids.length == 0){
			bootbox.alert("请选择司机!");
			return false;
		}
	}
	$("#changdriverId").val("").val(id)
	$("#deptTreeDiv").remove();
	
	var divObj = $("<div />").css("height","330px")
		divObj.attr("id","deptTreeDiv");
	var ulObj = $("<ul />").addClass("ztree");
		ulObj.css({ "heigth": "220px", "width": "353px" })
		ulObj.attr("id","changdeptTree");
	divObj.append(ulObj)
	
	$("#changDeptBody").append(divObj);
	$('#deptTree').attr("source","myModa2");
	initComboDeptTree($('#changdeptTree'));
	$('#myModa2').modal('show');
}

function doChangDept(){
	  var driverId = $('#changdriverId').val();
	  var deptId = $("#changDeptId").val();
	  var data ="driverIds="+driverId+"&deptId="+deptId;
	  $.ajax({
			type : "POST",
			url : ctx+"/com/supermap/driverChangDept?ram=" + Math.random(),
			data : data,
			success : function(obj) {
				if (obj.flag == "ok") {
					$('#myModa2').modal('hide');
					bootbox.alert("操作成功!");
					reload();
				} else if(obj.flag == "car_err") {
					$('#myModa2').modal('hide');
					bootbox.alert("操作失败，调整部门中有车辆不存在");
				} else if(obj.flag == "t_err"){
					$('#myModa2').modal('hide');
					bootbox.alert("操作失败，调整部门中有车辆终端不存在");
				}
			}
		});
}



