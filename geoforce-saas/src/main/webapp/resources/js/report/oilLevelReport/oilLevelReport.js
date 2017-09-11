var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var carGridWith =null;
$(function(){
	bootstrapAutocomplete("license",ctx+"/com/supermap/getLicense");
	$(window).on('resize.jqGrid', function () {
		$(grid_selector).jqGrid( 'setGridWidth', $("#monitorBtns").width()-60);
		var width=$("#monitorBtns").width()-50;
		 $("#profile").width(width);
		carGridWith=1;
    });
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
		if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
			setTimeout(function() {
				$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
			}, 0);
		}
    })
    $('#oileveTab a:last').tab('show');//tab标签页显示
    initGrid();
})
function initGrid(mag){
	var colNames = ['车牌号','定位时间','当前里程/km','当前油位/升'];
	var colModel = [{index:'license',name:'license', width:40},
					{index:'gpsTime',name:'gpsTime', width:30},
					{index:'mile',name:'mile', width:60},
					{index:'oil',name:'oil', width:60}];//hidden:true}
	var url =basePath+"/com/supermap/OilLevelReportList";
	loadGrid(grid_selector,url,"","车辆油耗统计",colNames,colModel);
}
function loadGrid(obj, url,data ,title,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height: 'auto',
		colNames:colNames,
		colModel:colModel, 
		viewrecords : false,
		altRows: true,
		multiselect: false,
        multiboxonly: true,
		caption: title,
		autowidth: true
	});
	$(window).triggerHandler('resize.jqGrid');
}
function loadNavGrid(){
	 jQuery(grid_selector).jqGrid('navGrid',pager_selector,
				{ 	//navbar options
					edit: false,
					editicon : 'ace-icon fa fa-pencil blue',
					add: false,
					addicon : 'ace-icon fa fa-plus-circle purple',
					del: false,
					delicon : 'ace-icon fa fa-trash-o red',
					search: false,
					searchicon : 'ace-icon fa fa-search orange',
					refresh: false,
					refreshicon : 'ace-icon fa fa-refresh green',
					view: false,
					viewicon : 'ace-icon fa fa-search-plus grey',
					carBind:false,
					carBindicon:'ace-icon fa fa-search-plus grey',
					carRegion:false,
					carRegionicon:'ace-icon fa fa-search-plus grey',
					searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
					searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
					addfunc:text,
					editfunc:text,
					delfunc:text,
					alertcap : "提示",
					alerttext : "请选择一行记录"
				}
			) 
}
function searchOilLevel(){
	var license = $("#license").val();
	var startTime = $("#startTime").val();
	var endTime = $("#endTime").val();
	var oilType = $("#oilType").val();
	ajaxOilLevel(license,startTime,endTime,oilType);
	 $('#oileveTab a:last').tab('show');//tab标签页显示
	
}
function ajaxOilLevel(license,startTime,endTime,oilType){
	$.ajax({
		type : "POST",
		async : true,
		url : basePath+"/com/supermap/OilLevelReportList",
		data : "license="+license+"&startTime="+startTime+"&endTime="+endTime+"&oilType="+oilType,
		dataType : "json",
		success : function(msg) {
			var data=$("#grid-table")[0].addJSONData(msg.list);
			initGrid(data);
			createOilChart(msg.name,oilType,msg.xData,msg.yData);
		},
		error : function() {
			
        }
	});
}

function createOilChart(name,oilType,xData,yData){
	var xArray = xData;
	var yArray = yData;
	if(xArray && yArray){
		var titleText = "";
		if(oilType == "2"){
			titleText = name;
		}else{
			titleText = name;
		}
		var xShowTitle = oilType == "1"?"里程曲线":"时间曲线";
		var xMax = xArray.length > 40?40:xArray.length-1;//x轴最大值限制
		var xLen = xArray.length;
		var tickInter = 1;
		if(xLen > 30){//x抽只显示30个坐标点
			var tmp = xLen/20;
			tickInter = parseInt(tmp);
		}
		//生成曲线图
		oilChart = new Highcharts.Chart({
            chart: {
        	    renderTo: 'profile',          //放置图表的divId
        	    plotBackgroundColor: null,
        	    plotBorderWidth: null,
        	    defaultSeriesType: 'spline',
        	    zoomType: 'x',
                borderWidth:'1',
                resetZoomButton:{
					position: {
						x:0,
						y:-30
					},
					theme: {
						style:{
							cursor: 'pointer'
						}
					}
            	}
        	},
            colors: ['#AA4643'],//线的颜色
            credits: {//右下角友情链接
            	enabled: true,
            	text: ''
            },          
            title: {//正标题
                text: "车辆：     "+titleText,
                y: 10
            },
            subtitle: {//副标题
                text:xShowTitle ,
                y: 30,
                style:{
					color:'red'
                }
            },
            xAxis:  {
       	 	 	labels: {
       	            rotation: -30, //字体倾斜
       	            align: 'right',
       	            style: { font: 'normal 12px 宋体' }
       	        },
       	 		categories: xArray,
       	 		tickInterval: tickInter //根据数据条数分组，大概是一小时120条GPS记录
        	},
            yAxis: {
            	title: {
            		text: "单位/升"
       	 		}
        	},
            tooltip: {
                enabled: true,
                formatter: function() {
            		if(oilType == "1"){//油位-里程
            			return '<b>'+this.series.name+'</b> '+"车"+'：' + Highcharts.numberFormat(this.x, 2)+ "里程:剩余" + Highcharts.numberFormat(this.y, 2)+"升";
                    }else{//油位-时间
                    	return '<b>'+this.series.name+'</b> '+"车"+'：' + this.x+"剩余" + Highcharts.numberFormat(this.y, 2)+"升";
                    }
                }
            },
            plotOptions: {
            	series: {
                    marker: {
            			//fillColor: 'blue',
                        radius: 3,  //曲线点半径，默认是4
                        symbol: 'diamond' //曲线点类型："circle", "square", "diamond", "triangle","triangle-down"，默认是"circle"
                    }
            	}
            },
            series: [{
                name: name,
                data: yArray
        	}]
        });
	}else{
		$.messager.alert(commonLang.errorTip,oilLevelLang.clwyw,"error");
	}
}

