var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var driverZtree = null;
var carGridWith = 86;
$(function(){
	//bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
    initGrid();
	loadNavGrid();
	initComboDeptTree($('#deptTree'));
	
	
})

/**
 * 显示列表
 * @return
 */
function initGrid(){
	
	var colNames = ['账号','用户姓名','部门','终端号', '终端类型', '手机','操作时间','操作'];
	//var colNames = ['车牌号','部门','终端号', '司机', '类型','操作时间','操作'];
	var colModel = [{index:'name',name:'name', width:40},
	                {index:'nickname',name:'nickname', width:40},
					{index:'deptName',name:'deptName', width:40},
					{index:'termcode',name:'termcode', width:40},
					{index:'termName',name:'termName', width:80},
					{index:'phone',name:'phone', width:30},
					{index:'operDate',name:'operDate', width:60},
					{index:'LOOK',name:'LOOK', width:35}];
	var name = $("#name").val();
	var deptCode = $("#deptCode").val();
	var parms={name:name,deptCode:deptCode};
	var url =ctx+"/com/supermap/personList"
	loadGrid(grid_selector,url,"","人员信息",parms,colNames,colModel);
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
            root: "result",
			total: "pageNum",// 表示总页数
			page: "currentPageNum",//当前页
			records: "totalNum",//总行数
            repeatitems : false    
        },  

		loadComplete : function() {
        	
		},
		onSelectRow: function(id){
			 
	    },
	     gridComplete:function(){
	    　　	var ids=jQuery(obj).jqGrid('getDataIDs');
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i];   
	            focusNum = "<a href='#' class='glyphicon glyphicon-edit'  alt='分配门店' title='分配门店' onclick='storeBind(\"" + id + "\")'></a>&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='glyphicon glyphicon-share'  alt='查看所管门店' title='查看所管门店' onclick='cancelStoreBind(\"" + id + "\")'></a>&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='glyphicon glyphicon-pencil'  alt='修改密码' title='修改密码' onclick='toUpdatePwd(\"" + id + "\")'></a>";
	            
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
			
			searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
			searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
			addfunc:toAddPerson,
			editfunc:toEditPerson,
			delfunc:delPerson,
			alertcap : "提示",
			alerttext : "请选择一行记录"
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
 * 刷新列表
 * @return
 */
function reload() {
	var name = $("#name").val();
	var deptCode = $("#deptCode").val();
	var parms={name:name,deptCode:deptCode};
	var url =ctx+"/com/supermap/personList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}



/**
 * 删除人员
 * @param id
 * @return
 */
function delPerson(){
	var ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
	if(ids.length == 0){
		bootbox.alert("请选择要删除人员!");
		return false;
	}
	var id = ids.toString();
	
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : ctx+"/com/supermap/delPerson?ram=" + Math.random() + "&personIds=" + id,
				success : function(obj) {
				if (obj.status == 1) {
						bootbox.alert("操作成功!");
						reload();
					} else {
						var msg = obj.info;
						bootbox.alert(msg);
						reload();
					}
				}
			});
		}
	});
	
}

/**
 * 添加人员弹出
 * @return
 */
function toAddPerson(){
	$("#addPwd").show();
	$("#addAge").show();
	$("#updateAge").hide();
	bootbox.dialog({
		  message: $("#addperson").html(),
		  title: "添加人员",
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
	    		 	addPerson()
		    	 }
	    		 return false;
		    	
		      }
		    }
		    
		  }
	});
}

/**
 * 编辑人员弹出
 * @param id
 * @return
 */
function toEditPerson(){
	
	
	var ids = $("#grid-table").jqGrid('getGridParam','selarrrow');//ids 字符串数组
	
	if(ids==""){
		bootbox.alert("请选择一行数据进行编辑!");
		return false;
	}
		
	if(ids.length>1){
		bootbox.alert("请选择一行数据进行编辑!");
		return false;
	}
	$("#addPwd").hide();
	$("#addAge").hide();
	$("#updateAge").show();
	var datarow = jQuery("#grid-table").getRowData(ids);
	$.post(ctx+"/com/supermap/personInfo",{id:ids[0]},function(data){
		var id = data.id;
		var name = data.name
		var depId = data.deptId;
		var nickname = data.nickname
		var password = data.password
		var age = data.age;
		var sex = data.sex;
		var email = data.email;
		var termtype = data.termtype;
		var termcode = data.termcode;
		var phone = data.phone;
		var deptName = datarow.deptName;
		var position = data.position;
		// .find("#sex" + sexFlag).attr("checked", true).end()
		var html = $("#addperson").html();
		var str = $(html).find("#id").val(id).end()
		                 .find("#name").val(name).end()
		                 .find("#boxdepId").val(depId).end()
		                 .find("#nickname").val(nickname).end()
		                 .find("#password").val(password).end()
		                 .find("#age2").val(age).end()
		                 .find("#sex"+sex).attr("selected", "selected").end()
		                 .find("#email").val(email).end()
		                 .find("#termcode").val(termcode).end()
		                 .find("#" + termtype).attr("selected", "selected").end()
		                 .find("#position").val(position).end()
		                 .find("#phone").val(phone).end()
		                 .find("#boxdeptName").val(deptName).end()
		bootbox.dialog({
			  message: str,
			  title: "修改人员",
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
			    		editPerson();
			    	}
		    		return false;
			    	
			    	
			      }
			    }
			  }
		});
		
		
	});
	
}

/**
 * 添加人员操作
 * @return
 */
function addPerson(){
	$.ajax({
		url : ctx+"/com/supermap/addPerson?ram=" + Math.random(),
		type : "POST",
		data :$(".bootbox #addPersonForm").serialize(),
		success : function(obj) {
			if (obj.status == 1) {
				bootbox.hideAll();
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
 * 编辑人员
 * @return
 */
function editPerson(){
	
	
	
	$.ajax({
		type : "POST",
		url : ctx+"/com/supermap/updatePerson?ram=" + Math.random(),
		data : $(".bootbox #addPersonForm").serialize(),
		success : function(obj) {
			if (obj.status == 1) {
				bootbox.hideAll();
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
 * 菜单主方法
 * @param obj
 * @param url
 * @param data
 * @param title
 * @param colNames
 * @param colModel
 * @return
 */
function loadStoreGrid(obj, url,data ,title,colNames,colModel){
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
		rowNum:6,
		sortable:false,
		viewsortcols:[false,'vertical',false],
		rowList:[6,12,24],
		pager : '#binddriver-pager',
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
        	
		},
		onSelectRow: function(id){
			 $("#driverId").val(id); 
	    }
	});
	$(obj).jqGrid('navGrid','#binddriver-pager',{add:false,edit:false,del:false});
	
}

/**
 * 弹出分配门店
 * @param id
 * @return
 */
function storeBind(id){
	$("#storeWarning").empty()
	var ids = "";
	if(id == ""){
		ids = $("#grid-table").jqGrid('getGridParam','selarrrow');
		id = ids;
		if(ids.length == 0){
			bootbox.alert("请选要绑定的车辆!");
			return false;
		}
	}
	$("#storeName").val("")
	$("#personId").val("").val(id)
	$("#myModalLabel").empty().append('分配门店');
	$("#bindButton").empty().append('分配门店');
	$("#bindButton").attr("onclick","bindStore()");
	$("#seachbtn").attr("onclick","reloadbind()")
	$("#gbox_binddriver-table").remove();
	var table = $("<table />").attr("id","binddriver-table");
	var divpager = $("<div />").attr("id","binddriver-pager");
	$("#bindRiverBody").append(table);
	$("#bindRiverBody").append(divpager)
	var url =ctx+"/com/supermap/storeList/Unallocated";
	storeGrid(url,'分配门店');
	
	$('#myModal').modal('show');
}

/**
 * 查看门店
 * @param id
 * @return
 */
function cancelStoreBind(id){
	$("#storeWarning").empty()
	$("#storeName").val("")
	$("#personId").val("").val(id)
	$("#myModalLabel").empty().append('门店信息')
	$("#bindButton").empty().append('删除门店');
	$("#bindButton").attr("onclick","cancelBind()");
	$("#seachbtn").attr("onclick","reloadcancelbind()")
	$("#gbox_binddriver-table").remove();
	var table = $("<table />").attr("id","binddriver-table");
	var divpager = $("<div />").attr("id","binddriver-pager");
	$("#bindRiverBody").append(table);
	$("#bindRiverBody").append(divpager)
	var url =ctx+"/com/supermap/storeByPerson?personId="+id;
	storeGrid(url,'门店信息');
	$('#myModal').modal('show');
}
/**
 * 门店列表
 * @return
 */
function storeGrid(url,title){
	
	var objtable = $("#binddriver-table");
	var colNames = ['名称','地址']; 
	var colModel = [
	    			{index:'name',name:'name', width:180},
	    			{index:'address',name:'address', width:190}]
	   loadStoreGrid(objtable,url,"",title,colNames,colModel);
	
}

/**
 * 刷新门店列表
 * @return
 */
function reloadbind(){
	var storeName = $("#storeName").val();
	
	var parms={name:storeName};
	var url =ctx+"/com/supermap/storeList/Unallocated";
   jQuery("#binddriver-table").jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")		
}
function reloadcancelbind(){
	var storeName = $("#storeName").val();
	var personId = $("#personId").val();
	var parms={name:storeName,personId:personId};
	var url =ctx+"/com/supermap/storeByPerson";
   jQuery("#binddriver-table").jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")		
}

/**
 * 绑定司机操作
 * @return
 */
function bindStore(){
	
    var s = $("#binddriver-table").jqGrid('getGridParam','selarrrow');
    if(s.length==0){
    	  $("#storeWarning").empty().append("请选择门店!");
		  return false
	  }else{
		  $("#storeWarning").empty()
	  }
    var personId = $('#personId').val();
    var data ="personId="+personId+"&storeIds="+s;
    $.ajax({
		type : "POST",
		url : ctx+"/com/supermap/setStore?ram=" + Math.random(),
		data : data,
		success : function(obj) {
			if (obj.status == 1) {
				$('#myModal').modal('hide');
				bootbox.alert("操作成功!");
				reload();
			} else {
				var msg = obj.info
				$('#myModal').modal('hide');
				bootbox.alert(msg);
			}
		}
	});
    
}

/**
 * 删除门店操作
 * @return
 */
function cancelBind(){
	  var personId = $('#personId').val();
	  var s = $("#binddriver-table").jqGrid('getGridParam','selarrrow');
	  if(s.length==0){
    	  $("#storeWarning").empty().append("请选择门店!");
		  return false
	  }else{
		  $("#storeWarning").empty()
	  }
	  var data ="personId="+personId+"&storeIds="+s;
	  $.ajax({
			type : "POST",
			url : ctx+"/com/supermap/delPersonStore?ram=" + Math.random(),
			data : data,
			success : function(obj) {
				if (obj.status == 1) {
					$('#myModal').modal('hide');
					bootbox.alert("操作成功!");
					reload();
				} else {
					var msg = obj.info
					$('#myModal').modal('hide');
					bootbox.alert(msg);
					return false
				}
			}
		});
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
			bootbox.alert("请选择人员!");
			return false;
		}
	}
	$("#changPersonId").val("").val(id)
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
	  var personIds = $('#changPersonId').val();
	  var deptId = $("#changDeptId").val();
	  var data ="personIds="+personIds+"&deptid="+deptId;
	  $.ajax({
			type : "POST",
			url : ctx+"/com/supermap/person/changDept?ram=" + Math.random(),
			data : data,
			success : function(obj) {
		  		if (obj.status == 1) {
					$('#myModa2').modal('hide');
					bootbox.alert("操作成功!");
					reload();
				} else {
					var msg = obj.info
					$('#myModa2').modal('hide');
					bootbox.alert(msg);
					reload();
				}
			}
		});
}




/**
 * 验证
 * @return
 */
function validate(){
	
	var name = $(".bootbox #name").val();
	if(name == ""|| name.length==0){
		showWarning("请填写账号！")
		return false;
	}
	if(!CheckCode(name)){
		showWarning("账号中有特殊字符！")
		return false;
	}
	var nickname = $(".bootbox #nickname").val();
	if(nickname == ""|| nickname.length==0){
		showWarning("请填写人员姓名！")
		return false;
	}
	if(!CheckCode(nickname)){
		showWarning("人员姓名中有特殊字符！")
		return false;
	}
	var password = $(".bootbox #password").val();
	if(password == ""|| password.length==0){
		showWarning("请填写密码！")
		return false;
	}
	var teminalCode = $(".bootbox #termcode").val();
	if(teminalCode == "" || teminalCode.length==0){
		showWarning("请填写终端号码！")
		return false;
	}
	if(!CheckCode(teminalCode)){
		showWarning("终端号码中有特殊字符！")
		return false;
	}
	var email = $(".bootbox #email").val();
	if(email == "" || email.length==0){
		showWarning("请填写邮箱！")
		return false;
	}
	if(!checkEmail(email)){
		showWarning("邮箱格式不正确！")
		return false;
	}
	var phone = $(".bootbox #phone").val();
	if(phone != "" && phone.length!=0){
		if(!simTest(phone)){
			showWarning("手机号格式不正确！")
			return false;
		}
	}
	var age = $(".bootbox #age").val();
	if(age != "" && age.length!=0){
		if(!checkNumber(age)){
			showWarning("年龄格式不正确！")
			return false;
		}
	}
	var age2 = $(".bootbox #age2").val();
	if(age2 != "" && age2.length!=0){
		if(!checkNumber(age2)){
			showWarning("年龄格式不正确！")
			return false;
		}
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
	var regPartton=/1[3-8]+\d{9}/;
	if(regPartton.test(sim)){
		return true;
	}
	return false;
}
/**
 * 邮箱验证
 * @param str
 * @return
 */

function checkEmail(str){
   var re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
  return re.test(str);
   
}
function checkNumber(str){
	 var reg = new RegExp("^[0-9]*$"); 
	 return reg.test(str)
}

/**
 * 修改密码方法
 * @param id
 * @return
 */
function toUpdatePwd(id ){
	$("#pwdwarning").hide();
	$(".bootbox #warning").modal('toggle');
	$(':input','#updatePwdForm')  
    .not(':button, :submit, :reset')  
    .val('')  ;
	$("#pwdpersonId").val(id);
	
	$("#UpdatePwd").modal('show');
}

function updatePwd(){
	$.ajax({
		url : ctx+"/com/supermap/person/updatePwd?ram=" + Math.random(),
		type : "POST",
		data :$("#updatePwdForm").serialize(),
		success : function(obj) {
			if (obj.status == 1) {
				$("#UpdatePwd").modal('hide');
				bootbox.alert("操作成功!");
				reload();
				
			} else {
				var msg = obj.info
				$("#pwdcontent").empty().append(msg)
				$("#pwdwarning").show();
				return false;
				
			}
		}
	});
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
			$("#deptCode").val(deptCode);
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




