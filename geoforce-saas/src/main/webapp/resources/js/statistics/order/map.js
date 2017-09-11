var sumCharts, tableDetail = false;
$(function(){
	sumCharts = Sumcharts({
		dom_id: 'sum_chart'
	});
});

var Map = {};

/**
 * 保存区划量统计的数据
 */
Map.data_orders = [];
/**
 * 地图分段设置
 */
Map.map_styles = [];
Map.start = null;
Map.end = null;
Map.searchType = 1;

Map.initMap = function() {
//	map = new Dituhui.Map("map", "http://dev.dituhui.com/sdk/1.0.0/swfs/", "100000", Dituhui.MapType.RANGE, "Map.setData", "Map.onError");
	
	if(map) {
		$(".all-provinces a[data-value='全国']").click();
	}
	else {
		Dituhui.SMCity.init({
	        city: "全国", 
	        noSearch: true,
	        callback: Map.cityTagClick,
	        countyClick: false
	    });
    	Map.search();
	}
	
    
	/*var end = new Date().format("yyyy-MM-dd hh:mm:ss");
	var start = new Date(new Date().setDate( new Date().getDate() - 1));	
	start = start.format("yyyy-MM-dd hh:mm:ss");
	Map.start = start;
	Map.end = end;*/
}
Map.setData = function(){
	var option = {
		data: Map.data_orders, 
		styles: Map.map_styles, 
		valueField: 'count',
        regionField: 'admincode',
        fillColorType: Map.searchType == 2 ? "red" : "green",
		isRLabelVisible: true,
		backgroundColor: "#f5f5f5",
		legendTitle: "订单量统计",
		legendPosition: "left_bottom",
		title: "",
        titleStyle: {
            fontSize: 25,
            fontColor: '#333',
            fontFamily: 'microsoft yahei'
        },
        events:[
            {eventName:"mousemove", isShowTooltip:true},
            {eventName:"click", eventHandler:"Map.regionClick"}
        ]
	};
	map.setMapOptions(option);
}
Map.onError = function(errorcode){ 
	alert(errorcode);
}


/**
 * 自定义时间段检查
 */
Map.checkCustomTime = function() {
	var start = $('.tab-map .custom-during input[name="start"]').val();
	var end = $('.tab-map .custom-during input[name="end"]').val();
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
    Map.start = start;
    Map.end = end;
    Map.search();
}


/**
 * 点击地图中的区划
 */
Map.regionClick = function(e) {
    if(!e.code || e.code == "" || !e.data.level) {
        return;
    }
    if(e.data.level == 3) {
    	if(Number(e.data.count) > 0) {
    		$(".tab-map").addClass("hide");
        	$(".tab-tabel").removeClass("hide");
        	$(".close-table").attr("show-target", ".tab-map");
        	if(!tableDetail) {
        		tableDetail = TableDetail({
        			admincode: e.code,
        			level: e.data.level
        		});
        	}
        	else {
        		tableDetail.admincode = e.code;
        		tableDetail.level = e.data.level;
        		$("#grid-table").clearGridData();
        		tableDetail.reload();
        	}
    	}
    	return;
    }
    $('.smcity').attr({
        'admincode': e.data.admincode,
        'level': e.data.level
    });

    if(e.data.level == 1) {
        Dituhui.SMCity.showCurrentProvince(e.data.name, e.data.admincode);
    }
    else {
        Dituhui.SMCity.showCurrentCity(e.data.name, e.data.admincode);
    }        
    Map.search();
}

/**
 * 搜索 订单量统计
 * @param - start 
 * @param - end
 */
Map.search = function() {
    $('ul.list-group').html('');
    var smcity = $('.smcity');
    var param = {};
    var code = smcity.attr('admincode');
    var level = smcity.attr('level');
    if( code && code != "" ) {
        param.admincode = code;
        param.level = level;
    }
    param.start = Map.start;
    param.end = Map.end;
    param.resulttype = Map.searchType;

    Dituhui.showMask();
    Dituhui.Order.statisticOrders(param,
        function(data) {
            var code = $('.smcity').attr('admincode');
            if(!code || code == "") {
                code = '100000';
            }            
            if(level == 2) {
                code = code.substr(0, 4) + '00';
            }
            if(!data || data.length == 0){
                data = [];                
                Map.data_orders = data;
                Map.setMapStyles();
                Map.loadMap(code);
                Map.showDataList();
                return;
            }
            data = data.sort(function(a, b) { return b.count - a.count });
            Map.data_orders = data;
            Map.setMapStyles();
            Map.showDataList();

            Map.loadMap(code);
        },
        function(info){
            Dituhui.showHint(info);
        }
    );
}


/**
 * 加载/刷新地图
 * @param - code - 行政区划代码
 */
Map.loadMap = function(code) {
    if(map) {
        map.unload();
    }    
    map = new Dituhui.Map("map", "http://dev.dituhui.com/sdk/1.0.0/swfs/", 
    code, Dituhui.MapType.RANGE, "Map.setData", "Map.onError");
}

/**
 * 显示数据列表
 */
Map.showDataList = function() {
    var data = Map.data_orders.concat();
    var len = data.length;
    if(len === 0) {
    	$(".console-foot .sum-number").html("共0条");
    	$(".console-foot .btn-export").addClass("hide");
        return;
    }
    var h = '';
    var max = data[0].count, max_width = 250, all = 0;
    
    for(var i=0; i<len; i++) {
        var item = data[i];
        if(!item.count || item.count == 0 || !item.name) {
            continue;
        }
        $(".child-citys a[option='toCity'][admincode='"+ item.admincode +"']").addClass("hasDetail");
        all += Number(item.count);
        h += '<li class="list-group-item">';
        h += '  <div class="order">'+(i+1)+'</div>';
        h += '  <div class="content">';
        h += '      <div class="top">';
        h += '          <span class="name" data-code="'+item.admincode+'" data-level="'+item.level+'" data-name="'+item.name+'">'+item.name+'</span>';
        h += '          <span class="count">'+ item.count +'个</span>';
        h += '      </div>';
        var width = Math.floor( item.count/max*250 );
        h += '      <div class="cus-progress-bar" style="width:'+ width +'px"></div>';
        h += '  </div>';
        h += '</li>';
    }
    $('ul.list-group').html(h); 
    $(".console-foot .sum-number").html("共"+all+"条");
    if(all == 0) {
    	$(".console-foot .btn-export").addClass("hide");
    }
    else {
    	$(".console-foot .btn-export").removeClass("hide");
    }
}

/**
 * 重新组织图例分段
 */
Map.setMapStyles = function() {
    var data = Map.data_orders.concat();
    var max = data.length > 0 ? data[0].count : 0, len = data.length;
    var minValue = max == 0 ? 0 : 1;

    var power = (max + '').length;
    var maxValue = Math.pow(10, power);
    maxValue = max < maxValue*0.5 ? maxValue*0.5 : maxValue;
    maxValue = max < maxValue*0.5 ? maxValue*0.5 : maxValue;
    maxValue = maxValue < 10 ? 10 : maxValue;
    var firstStyle = {min: minValue, max: Math.ceil(maxValue*0.2) };
    if(max == 0) {
        firstStyle.fillColor = '#FFFFFF';
        firstStyle.borderColor = '#A65628';
        firstStyle.borderAlpha = 0.3;
    }
    Map.map_styles = [
        firstStyle,
        {min: Math.ceil(maxValue*0.2)+1, max: Math.ceil(maxValue*0.4)},
        {min: Math.ceil(maxValue*0.4)+1, max: Math.ceil(maxValue*0.6)},
        {min: Math.ceil(maxValue*0.6)+1, max: Math.ceil(maxValue*0.8)},
        {min: Math.ceil(maxValue*0.8)+1, max: maxValue}
    ];
}

/**
 * 城市列表点击
 */
Map.cityTagClick = function(me) {
    var smcity = $('.smcity');
    var code = smcity.attr('admincode');
    var level = smcity.attr('level');
    
    if(level && level == '3') {
    	if(me && !me.hasClass("hasDetail")) {
    		return;
    	}
    	$(".tab-map").addClass("hide");
    	$(".tab-tabel").removeClass("hide");
    	$(".close-table").attr("show-target", ".tab-map");
    	if(!tableDetail) {
    		tableDetail = TableDetail({
    			admincode: code,
    			level: level
    		});
    	}
    	else {
    		tableDetail.admincode = code;
    		tableDetail.level = level;
    		$("#grid-table").clearGridData();
    		tableDetail.reload();
    	}
    	return;
    }
    Map.search();
}