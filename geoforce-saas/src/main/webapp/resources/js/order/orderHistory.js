/** ***************** base flow ****************** */
var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";

jQuery(function($) {
	initBaseUI();
	initGrid();
	initNavbar();
	setDefault();
});

/** ***************** modify ****************** */
/*function getChildOrder(parentName) {
	$("#parentId").empty();
	$.ajax({
		type : "POST",
		url : ctx + "/order/childOrder?random=" + Math.random(),
		success : function(result) {
			$.each(result, function(index, item) {
				if(item.name==parentName) {
					$("select[id=parentId]").append("<option selected='selected' value='" + item.id + "'>" + item.name + "</option>"); 
				} else {
					$("select[id=parentId]").append("<option value='" + item.id + "'>" + item.name + "</option>");
				}
			})
		}
	});
}*/

function initGrid() {
	jQuery(grid_selector).jqGrid({
		caption : "历史订单",
		height : function(){
			var h=getWindowHeight()-240;
			return h+"";
		}(),
		autowidth : true,
		altRows : true,
		
		url : ctx + "/orderHistory/get",
		mtype : 'POST',
		datatype : "json",
		postData : {
			"number" : function() {
				return $("#number").val();
			},
			"batch" : function() {
				return $("#batch").val();
			},
			"address" : function() {
				return $("#address").val();
			}
		},
		
		colNames : ['运单号', '订单批次', '省', '市', '区县', '详细地址', '订单状态', '状态变更时间'],
		// colNames : ['ID', '订单编号', '订单批次', '省', '市', '区县', '详细地址', '订单状态', '状态变更时间'],
		colModel : [/*{
			index : 'id',
			name : 'id',
			width : 110,
			sortable : false
		},*/ {
			index : 'number',
			name : 'number',
			width : 60,
			sortable : false
		}, {
			index : 'batch',
			name : 'batch',
			width : 60,
			sortable : false
		}, {
			index : 'province',
			name : 'province',
			width : 50,
			sortable : false
		}, {
			index : 'city',
			name : 'city',
			width : 70,
			sortable : false
		}, {
			index : 'county',
			name : 'county',
			width : 50,
			sortable : false
		}, {
			index : 'address',
			name : 'address',
			width : 70,
			sortable : false
		}, {
			index : 'orderStatusId',
			name : 'orderStatusId',
			width : 40,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				switch (cellvalue) {
					case 1 :
						return "已导入"; 
					case 2 :
						return "分单中"; 
					case 3 :
						return "自动分单成功"; 
					case 4 :
						return "自动分单失败"; 
					case 5 :
						return "手动分单成功"; 
					case 6 :
						return "线路规划中"; 
					case 7 :
						return "完成线路规划"; 
					case 8 :
						return "配送中"; 
					case 9 :
						return "已签收"; 
					default:
						return "未知";
				}
			}
		}, {
			index : 'statusUpdateTime',
			name : 'statusUpdateTime',
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

function addOrder(){}
function updateOrder(){}
function deleteOrder(){}

function initNavbar() {
	// navButtons navbar
	jQuery(grid_selector).jqGrid('navGrid', pager_selector, {
		add : false,
		edit : false,
		del : false,
		search : false,
		refresh : true,
		refreshicon : 'ace-icon fa fa-refresh green',
		refreshtext : '刷新',
		refreshtitle : ''
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