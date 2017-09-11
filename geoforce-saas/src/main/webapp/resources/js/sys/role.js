
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

var zTreeSetting = {
	view : {
		dblClickExpand : false
	},
	data : {
		simpleData : {
			enable : true,
			idKey : "id",
			pIdKey : "pid",
			rootPId : 0
		}
	},
	check : {
		enable : true,
		autoCheckTrigger : true

	}
};
	
function addEntity() {
	var html = $("#addWrapDIV").html();
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
					var validater=$(".bootbox #addForm").validate({
						onsubmit:true,
						onfocusout:true,
				        submitHandler:function(form){
				        	$.ajax({
								type : "POST",
								url : PROJECT_URL+"/role/save?ram=" + Math.random() + "&moduleId=" + currentModuleId,
								data : $(".bootbox #addForm").serializeArray(),
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
			        	}
				    }); 
					$(".bootbox #addForm").trigger("submit");
					if(!validater.valid()){
						return false;
					}
					
				}
			}

		}
	});
}

function updateEntity(id) {
	var data = jQuery("#grid-table").getRowData(id);
	var html = $("#addWrapDIV").html();
	var str = $(html)
	.find("#rolename2").val(data.name).end()
	.find("#remark").val(data.remark).end();
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

					var validater=$(".bootbox #addForm").validate({
						onsubmit:true,
						onfocusout:true,
				        submitHandler:function(form){
				        	$.ajax({
								type : "POST",
								url : PROJECT_URL+"/role/update?ram=" + Math.random()+ "&id=" + data.id + "&moduleId=" + currentModuleId,
								data : $(".bootbox #addForm").serializeArray(),
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
			        	}
				    }); 
					$(".bootbox #addForm").trigger("submit");
					if(!validater.valid()){
						return false;
					}
					
				}
			}

		}
	});
}

function deleteEntity(id) {
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : PROJECT_URL+"/role/delete?ram=" + Math.random() + "&id=" + id,
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
//授权
function authorize(id){
	var html = $("#treeWrapDIV").html();
	var str = $(html);
	$.ajax({
		type : "POST",
		url : PROJECT_URL+"/role/tree?ram=" + Math.random() + "&roleId=" + id,
		data: {moduleId: currentModuleId},
		success : function(obj) {
			$.fn.zTree.init($(".bootbox #tree"), zTreeSetting, obj);
			zTree = $.fn.zTree.getZTreeObj("tree");
			zTree.expandAll(true);
		}
	});
	bootbox.dialog({
		message : str,
		title : "授权",
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
					var nodes = zTree.getCheckedNodes(true);
					var checkedMenuIds=[];
					$.each(nodes,function(key,value){
						checkedMenuIds.push(value.id);
					});
					$.ajax({
						type : "POST",
						url : PROJECT_URL+"/role/authorize?ram=" + Math.random() + "&checkedMenuIds=" + checkedMenuIds.join(",")+ "&roleId=" + id,
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
		url : PROJECT_URL+"/role/get",
		postData : {
			"rolename" : function() {
				var rolename=$("#rolename").val();
				rolename=rolename.replace(/%/g, "").replace(/_/g, "");
				$("#rolename").val(rolename);
				return rolename;
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
		colNames : [ 'ID', '角色名称', '角色描述',  '创建时间' ],
		colModel : [ {
			index : 'id',
			name : 'id',
			width : 60,
			sortable : false
		}, {
			index : 'name',
			name : 'name',
			width : 60,
			sortable : false
		}, {
			index : 'remark',
			name : 'remark',
			width : 60,
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
		caption : "角色信息"
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
		addfunc : addEntity,
		editfunc : updateEntity,
		delfunc : deleteEntity,
		alertcap : "提示",
		alerttext : "请选择一行记录"
	}); 

	$(grid_selector).navButtonAdd(pager_selector, {
        caption : "授权",
        buttonicon : "ace-icon fa fa-check-square-o orange",
        onClickButton : function(){ 
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
