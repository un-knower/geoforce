var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var driverZtree = null;
var carGridWith = 86;
$(function(){
	bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	initComboDeptTree($('#deptTree'));
	
	
})

/**
 * 显示列表
 * @return
 */
function initGrid(){
	
	var colNames = ['车牌号','部门','终端号', '司机', '类型','颜色','品牌','操作时间','操作'];
	//var colNames = ['车牌号','部门','终端号', '司机', '类型','操作时间','操作'];
	var colModel = [{index:'license',name:'license', width:40},
					{index:'deptName',name:'deptName', width:40},
					{index:'terminalCode',name:'terminalCode', width:40},
					{index:'driverName',name:'driverName', width:80},
					{index:'typeName',name:'typeName', width:30},
					{index:'color',name:'color', width:40},
					{index:'brand',name:'brand', width:40},
					{index:'operDate',name:'operDate', width:60},
					{index:'LOOK',name:'LOOK', width:35}];
	var license = $("#license").val();
	var depId = $("#deptId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,deptId:depId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/carList"
	loadGrid(grid_selector,url,"","车辆信息",parms,colNames,colModel);
}




/**
 * 加载Grid主方法
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
		multiselect: true,
        multiboxonly: true,
        jsonReader: { 
            root: "rows",
			// total: "total",// 表示总页数
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
	            focusNum = "<a href='#' class='glyphicon glyphicon-edit'  alt='绑定司机' title='绑定司机' onclick='carBind(\"" + id + "\")'></a>&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='glyphicon glyphicon-share'  alt='绑定司机' title='取消绑定司机' onclick='cancelCarBind(\"" + id + "\")'></a>";
	            jQuery(obj).jqGrid('setRowData', ids[i], { LOOK: focusNum});
	        }
	    　　},
		caption: title,
		autowidth: true

	});
	$(window).triggerHandler('resize.jqGrid');
}
/**
 * 加载菜单
 */
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
			
			searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',carBindText:'绑定司机',
			searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',carBindtitle:'绑定司机',
			addfunc:toAddCar,
			editfunc:toEditCar,
			delfunc:delCar,
			carBind:carBind,
			alertcap : "提示",
			alerttext : "请选择一行记录"
		}).navButtonAdd(pager_selector,{  
			 caption:"司机绑定",   
			 buttonicon : "ace-icon fa fa-check-square-o orange",  
			 onClickButton: function(){
				
				carBind('');
			},   
			position:"last"  

		}).navButtonAdd(pager_selector,{  
			 caption:"调整部门",   
			 buttonicon : "ace-icon fa fa-check-square-o orange",
			 onClickButton: function(){
				changDept('')
			},   
			position:"last"  

		});
	 
}


/**
 * 菜单主方法
 * @param obj
 * @param url
 * @param data
 * @param title
 * @param colNames
 * @param colModel
 * @return
 */
function loaddriverGrid(obj, url,data ,title,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height: '208',
		width: '567', 
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		//recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:6,
		sortable:false,
		viewsortcols:[false,'vertical',false],
		rowList:[6,12,24],
		pager : '#binddriver-pager',
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
	    }
	});
	$(obj).jqGrid('navGrid','#binddriver-pager',{add:false,edit:false,del:false});
}

/**
 * 删除车辆
 * @param id
 * @return
 */
function delCar(){
	var ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
	if(ids.length == 0){
		bootbox.alert("请选择要删除车辆!");
		return false;
	}
	var id = ids.toString();
	
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : ctx+"/com/supermap/delCar?ram=" + Math.random() + "&carIds=" + id,
				success : function(obj) {
					if (obj.flag == "ok") {
						bootbox.alert("操作成功!");
						reload();
					} else if (obj.flag == "poointErr"){
						bootbox.alert("不能删除绑定网点的车辆！");
						
					}else {
						bootbox.alert("操作失败!");
					}
				}
			});
		}
	});	
}

/**
 * 添加车辆弹出
 * @return
 */
function toAddCar(){
	bootbox.dialog({
		  message: $("#addcar").html(),
		  title: "添加车辆",
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
	    		 	addCar()
		    	 }
	    		 return false;
		    	
		      }
		    }
		    
		  }
	});
}

/**
 * 编辑车辆弹出
 * @param id
 * @return
 */
function toEditCar(){
	
	var ids = $("#grid-table").jqGrid('getGridParam','selarrrow');//ids 字符串数组
	
	if(ids==""){
		bootbox.alert("请选择一行数据进行编辑!");
		return false;
	}
		
	if(ids.length>1){
		bootbox.alert("请选择一个车辆对象进行编辑!");
		return false;
	}
	var datarow = jQuery("#grid-table").getRowData(ids);
	$.post(ctx+"/com/supermap/carInfo",{carId:ids[0]},function(data){
		var id = data.car.id;
		var license = data.car.license;
		var depId = data.car.depId;
		var color = data.car.color;
		var brand = data.car.brand;
		var type = data.car.type;
		var petrol = data.car.petrol;
		var others = data.car.others;
		var terminalCode = data.terminal.code;
		var mobile = data.terminal.mobile
		var terminalType = data.terminal.typeId;
		var mobile = data.terminal.mobile;
		var deptName = datarow.deptName;
		// .find("#sex" + sexFlag).attr("checked", true).end()
		var html = $("#addcar").html();
		var str = $(html).find("#carId").val(id).end()
		                 .find("#license").val(license).end()
		                 .find("#boxdepId").val(depId).end()
		                 .find("#color").val(color).end()
		                 .find("#brand").val(brand).end()
		                 .find("#"+type).attr("selected", "selected").end()
		                 .find("#petrol").val(petrol).end()
		                 .find("#other").val(others).end()
		                 .find("#terminalCode").val(terminalCode).end()
		                 .find("#" + terminalType).attr("selected", "selected").end()
		                 .find("#mobile").val(mobile).end()
		                 .find("#boxdeptName").val(deptName).end()
		bootbox.dialog({
			  message: str,
			  title: "修改车辆",
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
			    		editCar();
			    	}
		    		return false;
			    	
			    	
			      }
			    }
			  }
		});
		
		
	});
	
}

/**
 * 添加车辆操作
 * @return
 */
function addCar(){
	$.ajax({
		url : ctx+"/com/supermap/addCar?ram=" + Math.random(),
		type : "POST",
		data :$(".bootbox #addCarForm").serialize(),
		success : function(obj) {
			if (obj.flag == "ok") {
				bootbox.hideAll();
				bootbox.alert("操作成功!");
				reload();
				
			} else {
				var flag = obj.flag
				if(flag =="err_06"){
					showWarning("车牌号码已存在！")
					return false;
				}
				if(flag == "err_07"){
					showWarning("终端号码已存在！")
					return false;
				}
				if(flag == "err_08"){
					showWarning("SIM卡号已存在！")
					return false;
				}
			}
		}
	});
}

/**
 * 编辑车辆
 * @return
 */
function editCar(){
	$.ajax({
		type : "POST",
		url : ctx+"/com/supermap/updateCar?ram=" + Math.random(),
		data : $(".bootbox #addCarForm").serialize(),
		success : function(obj) {
			if (obj.flag == "ok") {
				bootbox.hideAll();
				bootbox.alert("操作成功!");
				reload();
			} else {
				var flag = obj.flag
				if(flag =="err_06"){
					showWarning("车牌号码已存在！")
					
					return ;
				}
				if(flag == "err_07"){
					showWarning("终端号码已存在！")
					return ;
				}
				if(flag == "err_08"){
					showWarning("SIM卡号已存在！")
					return ;
				}
			}
		}
	});
	
}

/**
 * 刷新列表
 * @return
 */
function reload() {
	var license = $("#license").val();
	var depId = $("#deptId").val();
	var starttime = $("#starttime").val()
	var endtime = $("#endtime").val()
	var parms={license:license,deptId:depId,starttime:starttime,endtime:endtime};
	var url =ctx+"/com/supermap/carList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}

/**
 * 弹出绑定司机
 * @param id
 * @return
 */
function carBind(id){
	var ids = "";
	if(id == ""){
		ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
		id = ids;
		if(ids.length == 0){
			bootbox.alert("请选要绑定的车辆!");
			return false;
		}
	}
	$("#carWarning").empty()
	$("#driverName").val("")
	$("#carDriverId").val("").val(id)
	$("#bindButton").empty().append('绑定');
	$("#bindButton").attr("onclick","bindDirver()");
	$("#seachbtn").attr("onclick","reloadbind()")
	$("#gbox_binddriver-table").remove();
	var table = $("<table />").attr("id","binddriver-table");
	var divpager = $("<div />").attr("id","binddriver-pager");
	$("#bindRiverBody").append(table);
	$("#bindRiverBody").append(divpager)
	var url =ctx+"/com/supermap/unbindDriverList?carId="+id;
	driverGrid(url,'绑定司机');
	
	$('#myModal').modal('show');
}

/**
 * 调整部门
 * @param id
 * @return
 */

function changDept(id){
	var ids = "";
	if(id == ""){
		ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
		id = ids;
		if(ids.length == 0){
			bootbox.alert("请选择车辆!");
			return false;
		}
	}
	$("#changCarId").val("").val(id)
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
/**
 * 改变部门
 * @return
 */
function doChangDept(){
	  var carId = $('#changCarId').val();
	  var deptId = $("#changDeptId").val();
	  var data ="carIds="+carId+"&deptId="+deptId;
	  $.ajax({
			type : "POST",
			url : ctx+"/com/supermap/changDept?ram=" + Math.random(),
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
/**
 * 取消司机车辆绑定
 * @param id
 * @return
 */
function cancelCarBind(id){
	$("#carWarning").empty()
	$("#driverName").val("")
	$("#carDriverId").val("").val(id)
	$("#bindButton").empty().append('解除绑定');
	$("#bindButton").attr("onclick","cancelBind()");
	$("#seachbtn").attr("onclick","reloadUnbind()")
	$("#gbox_binddriver-table").remove();
	var table = $("<table />").attr("id","binddriver-table");
	var divpager = $("<div />").attr("id","binddriver-pager");
	$("#bindRiverBody").append(table);
	$("#bindRiverBody").append(divpager)
	var url =ctx+"/com/supermap/bindDriverList?carId="+id;
	driverGrid(url,'解除绑定');
	$('#myModal').modal('show');
}
/**
 * 司机列表
 * @return
 */
function driverGrid(url,title){
	
	var objtable = $("#binddriver-table");
	var colNames = ['姓名','部门','手机号码']; 
	var colModel = [
	    			{index:'name',name:'name', width:180},
	    			{index:'deptName',name:'deptName', width:190},
	    			{index:'phone',name:'phone', width:180}]
	loaddriverGrid(objtable,url,"",title,colNames,colModel);	
}

/**
 * 刷新司机列表
 * @return
 */
function reloadbind(){
	var driverName = $("#driverName").val();
	var carId = $("#carDriverId").val();
	var parms={name:driverName,carId:carId};
	var url =ctx+"/com/supermap/unbindDriverList";
   jQuery("#binddriver-table").jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")		
}

function reloadUnbind(){
	var driverName = $("#driverName").val();
	var carId = $("#carDriverId").val();
	var parms={name:driverName,carId:carId};
	var url =ctx+"/com/supermap/bindDriverList";
   jQuery("#binddriver-table").jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")		
	
}
/**
 * 绑定司机操作
 * @return
 */
function bindDirver(){
	
    var s = $("#binddriver-table").jqGrid('getGridParam','selarrrow');
    if(s == "" || s.length == 0){
    	$("#carWarning").append("请选择要绑定的司机！")
    	return false;
    }
    var carId = $('#carDriverId').val();
    var data ="carIds="+carId+"&driverIds="+s;
    $.ajax({
		type : "POST",
		url : ctx+"/com/supermap/BindDriver?ram=" + Math.random(),
		data : data,
		success : function(obj) {
			if (obj.flag == "ok") {
				$('#myModal').modal('hide');
				bootbox.alert("操作成功!");
				reload();
			} else {
				$('#myModal').modal('hide');
				bootbox.alert("操作失败!");
			}
		}
	});
    
}

/**
 * 取消司机绑定
 * @return
 */
function cancelBind(){
	  var carId = $('#carDriverId').val();
	  var s = $("#binddriver-table").jqGrid('getGridParam','selarrrow');
	  if(s == "" || s.length == 0){
	    	$("#carWarning").append("请选择解除绑定的司机！")
	    	return false;
	    }
	  var data ="carIds="+carId+"&driverIds="+s;
	  $.ajax({
			type : "POST",
			url : ctx+"/com/supermap/cancelBind?ram=" + Math.random(),
			data : data,
			success : function(obj) {
				if (obj.flag == "ok") {
					$('#myModal').modal('hide');
					bootbox.alert("操作成功!");
					reload();
				} else {
					$('#myModal').modal('hide');
					bootbox.alert("操作失败!");
				}
			}
		});
}

/**
 * 验证
 * @return
 */
function validate(){
	var license = $(".bootbox #license").val();
	if(license == ""|| license.length==0){
		showWarning("请填写车牌号码！")
		return false;
	}
	
	if(!licenseTest(license)){
		showWarning("车牌号格式错误！")
		return false;
	}
	
	var teminalCode = $(".bootbox #terminalCode").val();
	if(teminalCode == "" || teminalCode.length==0){
		showWarning("请填写终端号码！")
		return false;
	}
	if(!CheckCode(teminalCode)){
		showWarning("终端号码中有特殊字符！")
		return false;
	}
	var mobile = $(".bootbox #mobile").val();
	if(mobile == "" || mobile.length==0){
		showWarning("请填写SIM号码！")
		return false;
	}
	if(!simTest(mobile)){
		showWarning("SIM号码格式错误,请填写11位数字！")
		return false;
	}
	
	return true;
	
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
 * sim卡格式验证
 * @param sim
 * @return
 */
function simTest(sim){
	var regPartton=/1[0-9]+\d{9}/;
	if(regPartton.test(sim)){
		return true;
	}
	return false;
}
/**
 * 车牌号格式验证
 * @param license
 * @return
 */
function licenseTest(license){
	var el = /^[\u4E00-\u9FA5][\da-zA-Z]{6}$/;
	if(el.test(license)){
		return true;
	}
	return false;
}


/**
 * 显示错误
 * @param content
 * @return
 */
function showWarning(content){
	$(".bootbox #content").empty().append(content)
	$(".bootbox #warning").show();
}


/**
 * 点击树事件
 * @param e
 * @param treeId
 * @param treeNode
 * @return
 */
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

/**
 * 显示树下拉菜单
 * @return
 */
function showMenu() {
	var deptObj = $("#deptName");
	var deptOffset = $("#deptName").offset();
	$('#deptTree').attr("source","page");
	$('#deptTree').css("background-color", " background: none repeat scroll 0 0 #f0f6e4;");
	$("#menuContent").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");
	
	$("body").bind("mousedown", onBodyDown);
}
/**
* 弹出框显示树下拉菜单
* @return
*/
function showboxMenu() {
	var deptObj = $(".bootbox #boxdeptName");
	var deptOffset = $(".bootbox #boxdeptName").offset();
	$('#deptTree').attr("source","bootbox");
	$('#deptTree').css("background-color", " background: none repeat scroll 0 0 #f0f6e4;");
	$("#menuContent").css({left:deptOffset.left + "px", top:deptOffset.top + deptObj.outerHeight() + "px"}).slideDown("fast");
	
	$("body").bind("mousedown", onBodyDown);
}
/**
 * 隐藏下拉菜单
 * @return
 */
function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
} 




