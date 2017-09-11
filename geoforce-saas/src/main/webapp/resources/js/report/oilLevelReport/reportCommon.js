/**统计报表 查询提交部门树操作*/
var deptTree;
/**
 * easyui一个select combo的tree
 * @return
 */
function initComboDeptTree(){
	initDeptTree();
	//将选择部门的select变成combo
	$('#deptName').combo({
        editable:false
    });
	$('#deptTreeDiv').appendTo($('#deptName').combo('panel'));
}
/**
 * 选中一个部门事件
 * @param event
 * @param treeId
 * @param treeNode
 * @return
 */
function selectDept(event, treeId, treeNode){
	if(treeNode){
		var deptId = treeNode.id;
		var deptCode = treeNode.code;
		var deptName = treeNode.name;
		$('#deptName').combo('setValue', deptCode).combo('setText', deptName).combo('hidePanel');
	}
}
function initDeptTree(){
	var url = ctx+"/deptOther/deptAction!deptTree.do";
	var setting ={
		view: {
			selectedMulti: false,
			dblClickExpand: false,
			showTitle: true
		},
		data: {
			simpleData: {
				enable: true
			},
			key: {
				name:'ename',
				title:'name'
			}
		},
		async: {
			enable: true,
			url: url,
			autoParam:["id=treeId"]
		},
		callback: {
			onClick: selectDept
		}
	};
	if(setting != ''){
		$('#reportDeptTree').css("background-color", "transparent");
		deptTree = $.fn.zTree.init($('#reportDeptTree'), setting);
	}
}


/**曲线图表*/
var chart;//曲线图对象
/**
 * 曲线图获取chart部分样式
 * @param divId
 * @return
 */
function getChartStyle(divId){
	var chartStyle = {
	    renderTo: divId,          //放置图表的divId
	    plotBackgroundColor: null,
	    plotBorderWidth: null,
	    defaultSeriesType: 'spline',
	    zoomType: 'x',
        borderWidth:'1'
	};
	return chartStyle;
}
/**
 * 右下角友情链接
 * @param text
 * @return
 */
function getChartCredits(text){
	var credits = {//
    	enabled: true,
    	text: text
    };
	return credits;
}
/**
 * 曲线图标题
 * @param text
 * @return
 */
function getChartTitle(text){
	var title = {
        text: text,
        y: 10
    };
	return title;
}
/**
 * 曲线图副标题
 * @param text
 * @return
 */
function getChartSubTitle(text){
	var subTitle = {
        text: text,
        y: 30
    };
	return subTitle;
}
/**
 * 曲线图x数据
 * @param xArray
 * @return
 */
//function getChartXData(title,xArray){
//	var x = {//X轴数据
//    	title: {
//        	text: title
//   	 	},
//        categories: xArray,
//        labels: {
//            rotation: -45, //字体倾斜
//            align: 'right'
//            //style: { font: 'normal 13px 宋体' }
//        }
//    };
//	return x;
//}
/**
 * 曲线图x数据
 * @param xArray
 * @return
 */
function getChartXData(title,xArray){
	var x = {//X轴数据
    	title: {
        	text: title
   	 	},
        categories: xArray,
        labels: {
            rotation: -30, //字体倾斜
            align: 'right',
            style: { font: 'normal 12px 宋体' }
        }
    };
	return x;
}
/**
 * 曲线图y轴数据
 * @param yArray
 * @return
 */
function getChartYData(title,yArray){
	var y = {//X轴数据
    	title: {
        	text: title
   	 	},
        categories: yArray
    };
	return y;
}
/**
 * 曲线图提示框
 * @return
 */
function getChartTooltip(){
	var tooltip = {
        enabled: true,
        formatter: function() {
    	 	return this.series.name+reportLang.xsgl+'：' + this.x+ ';'+reportLang.youwei+'：' + Highcharts.numberFormat(this.y, 1) ;
           // return '<b>' + this.x + '</b><br/>' + this.series.name + ': ' + Highcharts.numberFormat(this.y, 1);
        }
    }
	return tooltip;
}
/**
 * 每个数据点是否显示
 * @return
 */
function getChartPlot(){
	var plotOptions = {
        line: {
            dataLabels: {
                enabled: true
            },
            enableMouseTracking: true//是否显示title
        }
    }
	return plotOptions;
}
/**
 * 插入的数据支持json
 * dataArray 数组/json
 * @return
 */
function getChartSeries(name,dataArray){
	var series = [{
        name: name,
        data: dataArray
	}
	//,{..}更多
	];
	return series;
}
