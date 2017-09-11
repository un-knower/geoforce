/** ***************** base flow ****************** */
var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";

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

jQuery(function($) {
	var param_location = getUrlArgs();
	if(param_location.moduleid) {
		currentModuleId = param_location.moduleid;
	}
	initBaseUI();
	initGrid();
	initNavbar();
	setDefault();
});


/** ***************** validation ****************** */
jQuery.validator.addMethod("normalInput", function(value, element) {
	return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);
}, "只能包括中文字、英文字母、数字和下划线"); 

/** ***************** modify ****************** */
function addDept() {
	$("#menuBtn").removeClass("disabled");
	getChildDept();
	var html = $("#addDept").html();
	bootbox.dialog({
		message : html,
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
					var validater = $(".bootbox #addDeptForm").validate({
						onsubmit : true,
						rules : {
							parentName: {
								required : true
							},
							name : {
								required : true,
								normalInput: true
							},
							phone : {
								digits : true
							},
							headName : {
								normalInput: true
							},
							headPhone : {
								digits : true
							},
							address : {
								normalInput: true
							},
							zipcode : {
								digits : true
							}
						},
						messages: {
							phone: {
								digits: "只能输入数字"
							},
							headPhone : {
								digits: "只能输入数字"
							},
							zipcode : {
								digits: "只能输入数字"
							}
						},
						submitHandler : function(form) {
							$.ajax({
								type : "POST",
								url : ctx + "/dept/save?random=" + Math.random() + "&moduleId=" + currentModuleId,
								data : $(".bootbox #addDeptForm").serializeArray(),
								success : function(result) {
									if (result.success == true) {
										bootbox.alert("操作成功!");
										reload();
									} else {
										bootbox.alert(result.errorInfo);
									}
								}
							});
						},
						errorPlacement : function(error,element) {
							error.insertAfter(element);
						}
					});
					$(".bootbox #addDeptForm").trigger("submit");
					if (!validater.valid()) {
						return false;
					}
				}
			}
		}
	});
}

function updateDept(id) {
	$("#menuBtn").addClass("disabled");
	var data = jQuery("#grid-table").getRowData(id);
	$("#parentId").attr("value", data.parentId);
	$("#parentName").attr("value", data.parentName);
	//getChildDept(); 暂不允许修改上级部门
	var html = $("#addDept").html();
	var str = $(html).find("#name").val(data.name).end()
					.find("#phone").val(data.phone).end()
					.find("#headName").val(data.headName).end()
					.find("#headPhone").val(data.headPhone).end()
					.find("#address").val(data.address).end()
					.find("#zipcode").val(data.zipcode).end();
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
					var validater = $(".bootbox #addDeptForm").validate({
						onsubmit : true,
						rules : {
							parentName: {
								required : true
							},
							name : {
								required : true,
								normalInput: true
							},
							phone : {
								digits : true
							},
							headName : {
								normalInput: true
							},
							headPhone : {
								digits : true
							},
							address : {
								normalInput: true
							},
							zipcode : {
								digits : true
							}
						},
						messages: {
							phone: {
								digits: "只能输入数字"
							},
							headPhone : {
								digits: "只能输入数字"
							},
							zipcode : {
								digits: "只能输入数字"
							}
						},
						submitHandler : function(form) {
							$.ajax({
								type : "POST",
								url : ctx + "/dept/update?random=" + Math.random() + "&id=" + data.id + "&moduleId=" + currentModuleId,
								data : $(".bootbox #addDeptForm").serializeArray(),
								success : function(result) {
									if (result.success == true) {
										bootbox.alert("操作成功!");
										reload();
									} else {
										bootbox.alert(result.errorInfo);
									}
								}
							});
						},
						errorPlacement : function(error,element) {
							error.insertAfter(element);
						}
					});
					$(".bootbox #addDeptForm").trigger("submit");
					if (!validater.valid()) {
						return false;
					}
				}
			}
		}
	});
}

function deleteDept(id) {
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : ctx + "/dept/delete?random=" + Math.random() + "&id=" + id,
				data: {moduleId: currentModuleId},
				success : function(result) {
					if (result.success == true) {
						bootbox.alert("操作成功！");
						reload();
					} else {
						bootbox.alert(result.errorInfo);
					}
				}
			});
		}
	});
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
	$(".bootbox #parentName").attr("value", nodes[0].name);
	$(".bootbox #parentId").attr("value", nodes[0].id);
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
	if (!(event.target.id == "menuBtn" || event.target.id == "treeWrapDIV" || $(event.target)
			.parents("#treeWrapDIV").length > 0)) {
		hideMenu();
	}
}

function hideMenu() {
	$(".bootbox #treeWrapDIV").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}

/** ***************** grid ****************** */
function initGrid() {
	jQuery(grid_selector).jqGrid({
		caption : "部门管理",
		height : function(){
			var h=getWindowHeight()-240;
			return h+"";
		}(),
		autowidth : true,
		altRows : true,
		multiselect : true,
		multiboxonly : true,
		
		url : ctx + "/dept/get",
		mtype : 'POST',
		datatype : "json",
		postData : {
			"name" : function() {
				return $("#name").val();
			},
			"moduleId": currentModuleId
		},
		
		colNames : ['ID', '名称', '上级部门ID', '上级部门', '联系电话', '部门主管', '主管电话', '地址', '邮编', '操作时间'],
		colModel : [{
			index : 'id',
			name : 'id',
			width : 110,
			sortable : false
		}, {
			index : 'name',
			name : 'name',
			width : 60,
			sortable : false
		}, {
			index : 'parentId',
			name : 'parentId',
			hidden: true
		}, {
			index : 'parentName',
			name : 'parentName',
			width : 60,
			sortable : false
		}, {
			index : 'phone',
			name : 'phone',
			width : 50,
			sortable : false
		}, {
			index : 'headName',
			name : 'headName',
			width : 70,
			sortable : false
		}, {
			index : 'headPhone',
			name : 'headPhone',
			width : 50,
			sortable : false
		}, {
			index : 'address',
			name : 'address',
			width : 70,
			sortable : false
		}, {
			index : 'zipcode',
			name : 'zipcode',
			width : 40,
			sortable : false
		}, {
			index : 'operDate',
			name : 'operDate',
			width : 60,
			sortable : false
		}],

		viewrecords : true,
		recordtext : "查看第 {0} - {1} 条记录，共 {2}条",
		pgtext : "第 {0} 页,共 {1} 页",
		loadtext : "加载中...",
		emptyrecords : "无记录",
		rowNum : 10,
		rowList : [10, 20, 30],
		pager : pager_selector,

		loadComplete : function() {
			var table = this;
			setTimeout(function() {
				updatePagerIcons(table);
			}, 0);
		}
	});
}

function initNavbar() {
	// navButtons navbar
	jQuery(grid_selector).jqGrid('navGrid', pager_selector, {
		add : true,
		addicon : 'ace-icon fa fa-plus-circle purple',
		addtext : '添加',
		addtitle : '',
		addfunc : addDept,
		
		edit : true,
		editicon : 'ace-icon fa fa-pencil blue',
		edittext : '编辑',
		edittitle : '',
		editfunc : updateDept,
		
		del : true,
		delicon : 'ace-icon fa fa-trash-o red',
		deltext : '删除',
		deltitle : '',
		delfunc : deleteDept,
		
		search : false,
		
		refresh : true,
		refreshicon : 'ace-icon fa fa-refresh green',
		refreshtext : '刷新',
		refreshtitle : '',
		
		alertcap : "提示",
		alerttext : "请选择一行记录"
	})
}

/** ***************** other ****************** */
function initBaseUI() {
	bootbox.setDefaults("locale", "zh_CN");
	// resize to fit page size
	$(window).on('resize.jqGrid', function() {
		$(grid_selector).jqGrid('setGridWidth',
				$(".page-content").width());
	})
	// resize on sidebar collapse/expand
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid', function(ev, event_name, collapsed) {
		if (event_name === 'sidebar_collapsed'
				|| event_name === 'main_container_fixed') {
			// setTimeout is for webkit only to give time for DOM changes and
			// then redraw!!!
			setTimeout(function() {
				$(grid_selector).jqGrid('setGridWidth',
						parent_column.width());
			}, 0);
		}
	})
}

function setDefault() {
	$("#search").on("click", reload);
	$(window).triggerHandler('resize.jqGrid');// trigger window resize to make
	// the grid get the correct size
	$(document).on('ajaxloadstart', function(e) {
		$(grid_selector).jqGrid('GridUnload');
		$('.ui-jqdialog').remove();
	});
	$("#sidebar-collapse").trigger('click');
	$("#sidebar-collapse").trigger('click');
}

function updatePagerIcons(table) {
	// replace icons with FontAwesome icons like above
	var replacement = {
		'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
		'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
		'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
		'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
	};
	$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
		var icon = $(this);
		var $class = $.trim(icon.attr('class').replace(
				'ui-icon', ''));
		if ($class in replacement)
			icon.attr('class', 'ui-icon ' + replacement[$class]);
	})
}

function reload() {
	$("#grid-table").trigger("reloadGrid");
}