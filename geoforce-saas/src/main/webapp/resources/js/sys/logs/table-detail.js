function TableDetail(option) {
	option = option || {}
		
	this.updatePagerIcons = function(table) {
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
	
	this.reload = function() {
		jQuery("#grid-table").trigger("reloadGrid", [{ page: 1}]);
	}
	
	
	var me = this;
	this.initNavbtns = function(grid_selector, pager_selector) {
		// navButtons navbar
		jQuery(grid_selector).jqGrid('navGrid', pager_selector, {
			add : false,
			edit : false,
			del : false,
			search : false,
			refresh : true,
			refreshicon : 'ace-icon fa fa-refresh green',
			refreshtext : '刷新',
			refreshtitle : '',
			refreshstate: "firstpage"
		})/*.jqGrid('navButtonAdd', pager_selector, {
			caption: "导出",
			buttonicon: "ace-icon fa fa-download green",
			onClickButton: function(){ 
			 	me.export();
			}, 
			position:"last" 
		})*/;
	}
	this.init = function() {
		var grid_selector = "#grid-table";
		var pager_selector = "#grid-pager";
		
		$('.search-logs').click(function(){
			var flag = Page.checkCustomTime();
			if(flag) {
				me.reload();
			}
		});
		
		jQuery(grid_selector).jqGrid({
			caption : "",
			height : function(){
				var h=getWindowHeight()-260;
				return h+"";
			}(),
			autowidth : true,
			altRows : true,
			url : urls.server + "/syslog/getAllLogs",
			mtype : 'GET',
			datatype : "json",
			postData : {
				"username" : function() {
					return $.trim($("#txt_username").val());
				},
				"starttime": function() {
					return Page.start || "";
				},
				"endtime": function() {
					return Page.end || "";
				},
				"moduleids": function() {
					var id = $("#txt_mudule_ids").val();
					return id!='-1' ? [id] : "";
				}
			},
			
			colNames : ['序号', '登录账号', '所属部门', '姓名', '手机号码', '操作内容', '描述', '操作时间'],
			colModel : [{
				index : 'rowid',
				name : 'rowid',
				width : 30,
				sortable : false
			}, {
				index : 'username',
				name : 'username',
				width : 80,
				sortable : false
			},{
				index : 'deptname',
				name : 'deptname',
				width : 80,
				sortable : false
			}, {
				index : 'realname',
				name : 'realname',
				width : 80,
				sortable : false
			}, {
				index : 'mobilephone',
				name : 'mobilephone',
				width : 80,
				sortable : false
			}, {
				index : 'moduleid',
				name : 'moduleid',
				width : 80,
				sortable : false
			}, {
				index : 'datadesc',
				name : 'datadesc',
				width : 320,
				sortable : false
			}, {
				index : 'opertime',
				name : 'opertime',
				width : 90,
				sortable : false
			}],
	
			viewrecords : true,
			recordtext : "查看第 {0} - {1} 条记录，共 {2}条",
			pgtext : "第 {0} 页,共 {1} 页",
			loadtext : "加载中...",
			emptyrecords : "无记录",
			rowNum : 20,
			rowList : [20, 50, 100],
			pager : pager_selector,
	
			loadComplete : function() {
				var table = this;
				setTimeout(function() {
					me.updatePagerIcons(table);
				}, 0);
			},
			loadError: function(evt) {
				Dituhui.showHint("当前查询到0条数据");
				$(grid_selector).clearGridData();
			}
		});
		
		me.initNavbtns(grid_selector, pager_selector);
	}
	
	this.init();
	return this;
}