
//当前页面的产品id
var currentModuleId = '';
function getUrlArgs(){
    var url = location.search; 
    var theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for ( var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    } 
    else {
        theRequest = null;
    }
    return theRequest;    
}

function getChildDept() {
	$.ajax({
		type : "POST",
		url : ctx + "/dept/getChildDepts?random=" + Math.random(),
		data: {moduleId: currentModuleId},
		success : function(result) {
			$.fn.zTree.init($(".bootbox #tree"), zTreeSetting, result);
			zTree = $.fn.zTree.getZTreeObj("tree");
			var nodes = zTree.getNodes();
			zTree.expandNode(nodes[0], true);
		}
	});
}
/** ***************** tree ****************** */

function onClick(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("tree");
	nodes = zTree.getSelectedNodes();
	$(".bootbox #parentName").val(nodes[0].name);
	$(".bootbox #parentId").attr("value", nodes[0].id);
	$(".bootbox #dept").attr("value", nodes[0].id);
	hideMenu();
}

var zTreeSetting = {
	view : {
		dblClickExpand : false
	},
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "parentId",
			rootPId : 0
		}
	},
	check : {
		enable : true,
		autoCheckTrigger : true
	},
	callback : {
		onClick : onClick
	}
};

function showMenu() {
	var parentNameNode = $("#parentName");
	var parentNameOffset = $("#parentName").offset();
	$(".bootbox #treeWrapDIV").css({
		left : parentNameOffset.left + "px",
		top : parentNameOffset.top + parentNameNode.outerHeight() + 34 + "px"
	}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "parentName" || event.target.id == "treeWrapDIV" || $(event.target)
			.parents("#treeWrapDIV").length > 0)) {
		hideMenu();
	}
}

function hideMenu() {
	$(".bootbox #treeWrapDIV").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}
/**
 * 添加用户
 */
function addUser() {
	var html = $("#adduser").html();
	getChildDept();
	var str = $(html);
	bootbox.dialog({
		size: "small",
		message : str,
		title : "添加",
		buttons : {
			cancel : {
				label : "取消",
				className : "btn-default",
				callback : function() {
				}
			},
			submit : {
				label : "提交",
				className : "btn-success",
				callback : function(result) {
					
					var validater=$(".bootbox #addUserForm").validate({
						onsubmit:true,
						onfocusout:true,
						rules: {
							email: { 
		                        required:true,
		                        email:true
		                    },
							parentName: {  
						        required: true
						    },
							sex: {  
						        required: true
						    },
						    zipcode : {
								digits : true
							}
						},
				        submitHandler:function(form){
				        	$.ajax({
								type : "POST",
								url : PROJECT_URL+"/user/save?ram=" + Math.random() + "&sex=" + ($(".bootbox input[name='sex']:checked").val()) + "&moduleId=" + currentModuleId,
								data : $(".bootbox #addUserForm").serializeArray(),
								success : function(obj) {
									if (obj.flag == "ok") {
										bootbox.alert("操作成功!");
										reload();
									} else {
										bootbox.alert(obj.info);
									}
								}
							});
				        },
				        errorPlacement: function(error, element) {
			        	   if (element.attr('type') === 'radio') {
			        	       error.insertAfter($(".bootbox input[name='sexHidden']"));
			        	   }else {
			        		   error.insertAfter(element);
			        	   }
			        	},
				        messages:{
				        	 email: { 
		                        required: "必填！",
		                        email:"请输入正确格式的电子邮件"
		                     },
		                     realname: { 
		                    	 required: "请输入用户名！"
		                     },
		                     password: { 
		                    	 required: "必填！"
		                     },
		                     check_password: { 
		                    	 required: "必填！"
		                     },
		                     parentId: { 
		                    	 required: "必填！"
		                     },
		                     sex: { 
		                    	 required: "必填！"
		                     },
		                     mobilephone2: { 
		                    	 required: "必填！"
		                     },
		                     parentName: { 
		                    	 required: "必填！"
		                     },
		                     zipcode : {
								 digits: "只能输入数字"
							 },
							 username3: {
							 	required: "必填！"
							 }
		                     
		                }
				    }); 
					$(".bootbox #addUserForm").trigger("submit");
					if(!validater.valid()){
						return false;
					}
				}
			}

		}
	});
}

function updateUser(id) {
	var data = jQuery("#grid-table").getRowData(id);
	getChildDept();
	var sexFlag = (data.sex == "男" ? "1" : "0");
	var html = $("#adduser").html();
	var str = $(html)
	.find("#username3").val(data.username).attr("readonly","readonly").end()
	.find("#realname").val(data.realname).end()
	.find("#sex" + sexFlag).attr("checked", true).end()
	.find("#address").val(data.address).end()
	.find("#mobilephone2").val(data.mobilephone).end()
	.find("#email").val(data.email).attr("readonly","readonly").end()
	.find("#parentName").val(data["deptId.name"]).end()
	.find("#parentId").val(data["deptId.id"]).end()
	.find("#dept").val(data["deptId.id"]).end()
	.find("#zipcode").val(data.zipCode).end();
	$(str).find("#password").parent().parent().remove();
	bootbox.dialog({
		message : str,
		title : "编辑",
		buttons : {
			cancel : {
				label : "取消",
				className : "btn-default",
				callback : function() {
				}
			},
			submit : {
				label : "提交",
				className : "btn-success",
				callback : function(result) {

					var validater=$(".bootbox #addUserForm").validate({
						onsubmit:true,
						onfocusout:true,
						rules: {
							parentName: {  
						        required: true
						    },
							sex: {  
						        required: true
						    }
						},
				        submitHandler:function(form){
				        	$.ajax({
								type : "POST",
								url : PROJECT_URL+"/user/update?ram=" + Math.random() + "&sex=" + ($(".bootbox input[name='sex']:checked").val()) + "&id=" + data.id + "&moduleId=" + currentModuleId,
								data : $(".bootbox #addUserForm").serializeArray(),
								success : function(obj) {
									if (obj.flag == "ok") {
										bootbox.alert("操作成功!");
										reload();
									} else {
										bootbox.alert("操作失败!");
									}
								}
							});
				        },
				        errorPlacement: function(error, element) {
			        	   if (element.attr('type') === 'radio') {
			        	       error.insertAfter($(".bootbox input[name='sexHidden']"));
			        	   }else {
			        	       error.insertAfter(element);
			        	   }
			        	},
			        	messages:{
				        	 email: { 
		                        required: "必填！"
		                     },
		                     realname: { 
		                    	 required: "必填！"
		                     },
		                     password: { 
		                    	 required: "必填！"
		                     },
		                     check_password: { 
		                    	 required: "必填！"
		                     },
		                     parentId: { 
		                    	 required: "必填！"
		                     },
		                     sex: { 
		                    	 required: "必填！"
		                     },
		                     mobilephone2: { 
		                    	 required: "必填！"
		                     },
		                     parentName: { 
		                    	 required: "必填！"
		                     }
		                     
		                }
				    }); 
					$(".bootbox #addUserForm").trigger("submit");
					if(!validater.valid()){
						return false;
					}
					
					
				}
			}

		}
	});
}

function deleteUser(id) {
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : PROJECT_URL+"/user/delete?ram=" + Math.random() + "&id=" + id,
				data: {moduleId: currentModuleId},
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
//角色
function authorize(id){
	var html = $("#roleWrapDIV").html();
	var str = $(html);
	$.ajax({
		type : "POST",
		url : PROJECT_URL+"/user/role?ram=" + Math.random() + "&lineUserId=" + id,
		data: {moduleId: currentModuleId},
		success : function(obj) {
			$.each(obj,function(key,value){
				str.append("<label><input id='"+value.id+"' "+(value.checked?'checked="checked"':'')+" name='roleCheckbox' type='checkbox' class='ace'><span class='lbl'> "+value.name+"</span></label>");
			});
		}
	});
	bootbox.dialog({
		message : str,
		title : "角色",
		buttons : {
			cancel : {
				label : "取消",
				className : "btn-default",
				callback : function() {
				}
			},
			submit : {
				label : "提交",
				className : "btn-success",
				callback : function(result) {
					var checkedRolesId=[];
					$.each($(".bootbox [name='roleCheckbox']:checked"),function(key,value){
						checkedRolesId.push($(value).attr("id"));
					});
					$.ajax({
						type : "POST",
						url : PROJECT_URL+"/user/authorize?ram=" + Math.random() + "&checkedRolesId=" + checkedRolesId.join(",")+ "&userId=" + id,						
						data: {moduleId: currentModuleId},
						success : function(obj) {
							if (obj.flag == "ok") {
								bootbox.alert("操作成功!");
							} else {
								bootbox.alert("操作失败!");
							}
						}
					});
				}
			}

		}
	});	
}
function reload() {
	$("#grid-table").trigger("reloadGrid");
}

jQuery(function($) {
	var param_location = getUrlArgs();
	if(param_location.moduleid) {
		currentModuleId = param_location.moduleid;
	}

	$("#search").on("click", reload);
	bootbox.setDefaults("locale", "zh_CN");
	bootbox.setDefaults({size: "small"});

	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";

	// resize to fit page size
	$(window).on('resize.jqGrid', function() {
		setTimeout(function() {
			$(grid_selector).jqGrid('setGridWidth', parent_column.width());
		}, 0);
	});
	// resize on sidebar collapse/expand
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid', function(ev, event_name, collapsed) {
		if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
			// setTimeout is for webkit only to give time for DOM changes and then redraw!!!
			setTimeout(function() {
				$(grid_selector).jqGrid('setGridWidth', parent_column.width());
			}, 0);
		}
	});

	jQuery(grid_selector).jqGrid({
		url : PROJECT_URL+"/user/get",
		postData : {
			"search_username" : function() {
				var search_username=$("#search_username").val();
				search_username=search_username.replace(/%/g, "").replace(/_/g, "");
				$("#search_username").val(search_username);
				return search_username;
			},
			"search_mobilephone" : function() {
				var search_mobilephone=$("#search_mobilephone").val();
				search_mobilephone=search_mobilephone.replace(/%/g, "").replace(/_/g, "");
				$("#search_mobilephone").val(search_mobilephone);
				return search_mobilephone;
			},
			"starttime" : function() {
				return $("#starttime").val();
			},
			"endtime" : function() {
				return $("#endtime").val();
			},
			"moduleId": currentModuleId
		},
		datatype : "json",
		mtype : 'POST',
		height : function(){
			var h=getWindowHeight()-240;
			return h+"";
		}(),
		width : 'auto',
		autowidth : true,
		colNames : [ 'ID', '用户名', '真实姓名', '手机', '邮件', '电话', '传真', '性别', '地址', '邮编', '备注', '状态', '创建时间', '企业', '部门','部门ID', '类型' ],
		colModel : [ {
			index : 'id',
			name : 'id',
			width : 60,
			sortable : false
		}, {
			index : 'username',
			name : 'username',
			width : 60,
			sortable : false
		}, {
			index : 'realname',
			name : 'realname',
			width : 60,
			sortable : false
		}, {
			index : 'mobilephone',
			name : 'mobilephone',
			width : 60,
			sortable : false
		}, {
			index : 'email',
			name : 'email',
			width : 60,
			sortable : false
		}, {
			index : 'telephone',
			name : 'telephone',
			width : 60,
			sortable : false,
			hidden : true
		}, {
			index : 'fax',
			name : 'fax',
			width : 60,
			sortable : false,
			hidden : true
		}, {
			index : 'sex',
			name : 'sex',
			width : 40,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				if (cellvalue == '1') {
					return "男";
				} else {
					return "女";
				}
			}
		}, {
			index : 'address',
			name : 'address',
			width : 60,
			sortable : false
		}, {
			index : 'zipCode',
			name : 'zipCode',
			width : 60,
			sortable : false
		}, {
			index : 'remark',
			name : 'remark',
			width : 60,
			sortable : false,
			hidden : true
		}, {
			index : 'stratusId',
			name : 'stratusId',
			width : 40,
			sortable : false,
			hidden : true,
			formatter : function(cellvalue, options, rowObject) {
				if (cellvalue == 1) {
					return "正常";
				} else if (cellvalue == 2) {
					return "禁用";
				} else if (cellvalue == 3) {
					return "待激活";
				} else if (cellvalue == 4) {
					return "待审核";
				} else if (cellvalue == 5) {
					return "其他";
				}
			}
		}, {
			index : 'createTime',
			name : 'createTime',
			width : 60,
			sortable : false
		}, {
			index : 'eid.name',
			name : 'eid.name',
			width : 60,
			sortable : false
		}, {
			index : 'deptId.name',
			name : 'deptId.name',
			width : 60,
			sortable : false
		}, {
			index : 'deptId.id',
			name : 'deptId.id',
			width : 60,
			hidden : true,
			sortable : false
		}, {
			index : 'sourceId',
			name : 'sourceId',
			width : 60,
			sortable : false,
			hidden : true,
			formatter : function(cellvalue, options, rowObject) {
				if (cellvalue == 1) {
					return "门户";
				} else if (cellvalue == 2) {
					return "运营支撑";
				} else if (cellvalue == 3) {
					return "自助式";
				} else if (cellvalue == 4) {
					return "其他";
				}
			}
		} ],

		viewrecords : true,
		recordtext : "查看第 {0} - {1} 条记录，共 {2}条",
		pgtext : "第 {0} 页,共 {1} 页",
		loadtext : "加载中...",
		emptyrecords : "无记录",
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : pager_selector,
		altRows : true,
		multiselect : true,
		multiboxonly : true,

		loadComplete : function() {
			var table = this;
			setTimeout(function() {
				updatePagerIcons(table);
			}, 0);
		},
		caption : "用户信息"
	});
	$(window).triggerHandler('resize.jqGrid');// trigger window resize to make the grid get the correct size
	
	// navButtons
	jQuery(grid_selector).jqGrid('navGrid', pager_selector, { // navbar options
		edit : true,
		editicon : 'ace-icon fa fa-pencil blue',
		add : true,
		addicon : 'ace-icon fa fa-plus-circle purple',
		del : true,
		delicon : 'ace-icon fa fa-trash-o red',
		search : false,
		searchicon : 'ace-icon fa fa-search orange',
		refresh : true,
		refreshicon : 'ace-icon fa fa-refresh green',
		view : false,
		viewicon : 'ace-icon fa fa-search-plus grey',
		searchtext : '查找',
		edittext : '编辑',
		addtext : '添加',
		refreshtext : '刷新',
		deltext : '删除',
		viewtext : '查看',
		searchtitle : '',
		edittitle : '',
		addtitle : '',
		refreshtitle : '',
		deltitle : '',
		viewtitle : '',
		addfunc : addUser,
		editfunc : updateUser,
		delfunc : deleteUser,
		alertcap : "提示",
		alerttext : "请选择一行记录"
	});
	
	$(grid_selector).navButtonAdd(pager_selector, {
        caption : "角色",
        buttonicon : "ace-icon fa fa-check-square-o orange",
        onClickButton : function(){ 
//      	var gr = jQuery(grid_selector).jqGrid('getGridParam','selarrrow');
        	var gr = jQuery(grid_selector).jqGrid('getGridParam','selrow');
        	if(!gr){
        		$.jgrid.viewModal("#alertmod_grid-table",{gbox:"#gbox_"+$.jgrid.jqID(this.p.id),jqm:true});
                $("#jqg_alrt").focus();
        	}else{
        		authorize(gr);
        	}
 	   },
        position : "first",
        title : "",
        cursor : "pointer"
    });
	// replace icons with FontAwesome icons like above
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

	$(document).on('ajaxloadstart', function(e) {
		$(grid_selector).jqGrid('GridUnload');
		$('.ui-jqdialog').remove();
	});
	
});