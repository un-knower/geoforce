
function TableDetail(option) {
	option = option || {}
	this.admincode = option.admincode || "";
	this.level = option.level || "";
	
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
	
	this.export = function() {
		var url = "http://" + location.host + urls.server + "/statistic/order/exportOrderDetail?";
    	var smcity = $('.smcity');
	    var code = smcity.attr('admincode');
	    var level = smcity.attr('level'); 
	    if( code && code != "" ) {
	        url += "&admincode=" + code + "&level=" + level;
	    }
	    url += "&start="+ sumCharts.start_time + "&end=" + sumCharts.end_time;
	    url += "&resulttype=" + Map.searchType;
	    url += "&ordernum=" + $.trim($("#txt_search_ordernumber").val());
	    url += "&batch=" + $.trim($("#txt_search_orderbatch").val());
	    url += "&address=" + $.trim($("#txt_search_orderaddress").val());
	    window.open(url, "_blank");
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
		}).jqGrid('navButtonAdd', pager_selector, {
			caption: "导出",
			buttonicon: "ace-icon fa fa-download green",
			onClickButton: function(){ 
			 	me.export();
			}, 
			position:"last" 
		});
	}
	this.init = function() {
		var grid_selector = "#grid-table";
		var pager_selector = "#grid-pager";
		
		$(".btn-searchorder").on("click", me.reload);
		
		jQuery(grid_selector).jqGrid({
			caption : "",
			height : function(){
				var h=getWindowHeight()-210;
				return h+"";
			}(),
			autowidth : true,
			altRows : true,
			url : urls.server + "/statistic/order/getDetailByAdminResulttype",
			mtype : 'GET',
			datatype : "json",
			postData : {
				"ordernum" : function() {
					return $.trim($("#txt_search_ordernumber").val());
				},
				"batch" : function() {
					return $.trim($("#txt_search_orderbatch").val());
				},
				"address" : function() {
					return $.trim($("#txt_search_orderaddress").val());
				},
				"start": function() {
					return sumCharts.start_time;
				},
				"end": function() {
					return sumCharts.end_time;
				},
				"admincode": function(){
				    return me.admincode;
				},
				"level": function(){
				    return me.level;
				},
				"resulttype": function(){
					return Map.searchType;
				}
			},
			
			colNames : ['序号', '分单时间', '订单编号', '地址', '省', '市', '区', '所属区划', '区划状态', '关联区划', 'x', 'y', '结果类型'],
			colModel : [{
				index : 'rowid',
				name : 'rowid',
				width : 30,
				sortable : false
			}, {
				index : 'fendantime',
				name : 'fendantime',
				width : 90,
				sortable : false
			},{
				index : 'ordernum',
				name : 'ordernum',
				width : 60,
				sortable : false
			}, {
				index : 'address',
				name : 'address',
				width : 200,
				sortable : false
			}, {
				index : 'province',
				name : 'province',
				width : 50,
				sortable : false
			}, {
				index : 'city',
				name : 'city',
				width : 50,
				sortable : false
			}, {
				index : 'county',
				name : 'county',
				width : 50,
				sortable : false
			}, {
				index : 'areaname',
				name : 'areaname',
				width : 50,
				sortable : false,
				formatter: function(cellvalue){
					return (cellvalue && cellvalue != "" ? cellvalue : "--");
				}
			},{
				index : 'areastatus',
				name : 'areastatus',
				width : 50,
				sortable : false,
				formatter: function(cellvalue){
					if(cellvalue == '1') {
						return "停用";
					}
					else if(cellvalue == '0') {
						return "正常"
					}
					return "--";
				}
			},{
				index : 'relation_areaname',
				name : 'relation_areaname',
				width : 50,
				sortable : false,
				formatter: function(cellvalue){
					return (cellvalue && cellvalue != "" ? cellvalue : "--");
				}
			}, {
				index : 'smx',
				name : 'smx',
				width : 70,
				sortable : false
			},{
				index : 'smy',
				name : 'smy',
				width : 70,
				sortable : false
			},{
				index : 'resulttype',
				name : 'resulttype',
				width : 50,
				sortable : false,
				formatter : function(cellvalue, options, rowObject) {
					switch (cellvalue) {
						case "1" :
							return "分单成功"; 
						case "2" :
							return "无区划"; 
						case "3" :
							return "无坐标"; 
						default:
							return "未知";
					}
				}
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


/**
 * 
 */
function Sumcharts(option) {
	/**
	 * div-id
	 */
	this.dom_id = option.dom_id;
	
	this.start_time = new Date(new Date().setDate( new Date().getDate() - 1)).format("yyyy-MM-dd hh:mm:ss");
	this.end_time = new Date().format("yyyy-MM-dd hh:mm:ss");
	
	this.chart = null;
	
	var me = this;
	
	/**
	 * 刷新数据
	 */
	this.refresh = function() {
		Dituhui.showMask();
		Dituhui.Order.statisticOrdersAll(
			{
				start: me.start_time,
				end: me.end_time
			},
			function(e) {
				Dituhui.hideMask();
				var option = {
				    title: {
				        text: '全国订单量统计',
				        subtext: '',
				        x:'center',
				        textStyle: {
				        	color: '#999'
				        }
				    },
				    tooltip: {
				        trigger: 'item',
				        formatter: "{a} <br/>{b} : {c} ({d}%)"
				    },
				    legend: {
				        orient: 'horizontal',
				        left: 'center',
				        bottom: 'bottom',
				        data: ['分单成功','分单失败-无区划','分单失败-无坐标'],
				        selectedMode: false,
				        itemGap: 20
				    },
				    series: [
				        {
				            name: '分单状态',
				            type: 'pie',
				            radius : '65%',
				            center: ['50%', '50%'],
				            data: e.datas,
				            avoidLabelOverlap: true,
				            itemStyle: {
				                emphasis: {
				                    shadowBlur: 10,
				                    shadowOffsetX: 0,
				                    shadowColor: 'rgba(0, 0, 0, 0.5)'
				                }
				            },
				            label: {
	                			normal : {         				
		                			textStyle: {
		                				color: "#333"
		                			}
	                			}
	                		},
				            labelLine: {
	                			normal : {                				
		                			lineStyle: {
		                				color: "#333"
		                			}
	                			}
	                		}
				        }
				    ]
				};
				me.chart.setOption(option);
				
				$(".sum-detail .the-number").html(e.total);
				$(".sum-detail .the-time").html(me.start_time+"至"+me.end_time);
				$(".tab-sum .custom-during").hide();
			},
			function(info) {
				Dituhui.hideMask();
				Dituhui.showHint(info);
				$(".sum-detail .the-number").html("0");
				me.chart.clear();
				$(".sum-detail .the-time").html(me.start_time+"至"+me.end_time);
			}
		);
		
	}
	
	this.custom_time = false;
	
	
	/**
	 * 初始化
	 */
	this.init = function() {
		$('.tab-sum .custom-during .button-search').click(function(){
			var start = $('.tab-sum .custom-during input[name="start"]').val();
			var end = $('.tab-sum .custom-during input[name="end"]').val();
			if(start === "") {		
		        Dituhui.showHint("请选择开始时间");
		        return false;
			}
			if(end === "") {		
		        Dituhui.showHint("请选择结束时间");
		        return false;
			}
		
		    var startdate = new Date(start);
		    var enddate = new Date(end); 
		    if(enddate <= startdate) {
		        Dituhui.showHint("结束时间须晚于开始时间");
		        return false;
		    }
		    me.start_time = start;
		    me.end_time = end;
		    
		    $('.tab-sum ul.during > li').removeClass("using");
			$(this).addClass("using");
		    
			me.refresh();
			
			$(".sum-detail .the-time").html(me.start_time+"至"+me.end_time);
		});
		me.chart = echarts.init(document.getElementById(me.dom_id));
		$('.tab-sum ul.during > li[data-time]').click(function(){
			$('.tab-sum ul.during > li').removeClass("using");
			var time = $(this).addClass("using").attr('data-time');
			var end = new Date().format("yyyy-MM-dd hh:mm:ss");
			var start;
			switch(time) {
				case 'day':
					start = new Date(new Date().setDate( new Date().getDate() - 1));
					break;
				case 'week':
					start = new Date(new Date().setDate( new Date().getDate() - 7));
					break;
				case 'month':
					start = new Date(new Date().setDate( new Date().getDate() - 30));
					break;
			}
			start = start.format("yyyy-MM-dd hh:mm:ss");
			me.start_time = start;
			me.end_time = end;
			me.refresh();
			
			Map.start = me.start_time;
			Map.end = me.end_time;
			
			me.custom_time = false;
		});
		me.refresh();
		me.chart.on("click", function(e){
			$(".tab-sum").addClass("hide");		
			Map.searchType = e.data.key;
			
			if(e.data.key == 3){
				$(".tab-tabel").removeClass("hide");
				$(".close-table").attr("show-target", ".tab-sum");
				tableDetail = TableDetail();
				return;
			}
			$(".tab-map").removeClass("hide").addClass("result-"+e.data.key);
			Map.start = me.start_time;
			Map.end = me.end_time;
			
			Map.initMap();
			$(".tab-map .console-box .title").html("订单量统计 --" + e.data.name);
			
			var activeli = $('.tab-sum ul.during > li.using');
			$('.tab-map ul.during > li').removeClass("action");
			
			if(activeli.attr("data-time")){
				$('.tab-map ul.during > li[data-time="'+ activeli.attr("data-time") +'"]')
				.addClass("action");
				$('.tab-map .custom-during').hide();
				$(".tab-map .data-list-group").removeClass("shorter");
			}
			else {
				$('.tab-map ul.during > li:last-child').addClass("action");
				$('.tab-map .custom-during').show();
				
				var start = $('.tab-sum .custom-during input[name="start"]').val();
				var end = $('.tab-sum .custom-during input[name="end"]').val();
				$('.tab-map .custom-during input[name="start"]').val(start);
				$('.tab-map .custom-during input[name="end"]').val(end);
				$(".tab-map .data-list-group").addClass("shorter");
			}
		});
	}
		
	this.init();	
	return this;
}
















