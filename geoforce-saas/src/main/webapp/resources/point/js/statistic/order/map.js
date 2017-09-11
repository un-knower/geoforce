﻿$(function(){
	Map.init();
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

Map.init = function() {
	// map = new Dituhui.Map("map", "http://dev.dituhui.com/sdk/1.0.0/swfs/", "100000", Dituhui.MapType.RANGE, "Map.setData", "Map.onError");
	SuperMap.Egisp.SMCity.init({
        city: "全国", 
        noSearch: true,
        callback: Map.cityTagClick
    });
    Map.search();
}
Map.setData = function(){
	var option = {
		data: Map.data_orders, 
		styles: Map.map_styles, 
		valueField: 'count',
        regionField: 'admincode',
        fillColorType: 'green',
		isRLabelVisible: true,
		backgroundColor: "#f5f5f5",
		legendTitle: "订单量统计",
		legendPosition: "left_bottom",
		title: "订单量统计",
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
	var start = $('.custom-during input[name="start"]').val();
	var end = $('.custom-during input[name="end"]').val();
	if(start === "") {		
        SuperMap.Egisp.showHint("请选择开始时间");
        return false;
	}
	if(end === "") {		
        SuperMap.Egisp.showHint("请选择结束时间");
        return false;
	}

    var startdate = new Date(start);
    var enddate = new Date(end); 
    if(enddate <= startdate) {
        SuperMap.Egisp.showHint("结束时间须晚于开始时间");
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
    if(!e.code || e.code == "" || !e.data.level || e.data.level == 3) {
        return;
    }
    $('.smcity').attr({
        'admincode': e.data.admincode,
        'level': e.data.level
    });

    if(e.data.level == 1) {
        SuperMap.Egisp.SMCity.showCurrentProvince(e.data.name, e.data.admincode);
    }
    else {
        SuperMap.Egisp.SMCity.showCurrentCity(e.data.name, e.data.admincode);
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

    SuperMap.Egisp.showMask();
    SuperMap.Egisp.Order.statisticOrders(param,
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
                return;
            }
            data = data.sort(function(a, b) { return b.count - a.count });
            Map.data_orders = data;
            Map.setMapStyles();
            Map.showDataList();

            Map.loadMap(code);
        },
        function(info){
            SuperMap.Egisp.showHint(info);
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
    map = new Dituhui.Map("map", "http://dev.dituhui.com/sdk/1.0.0/swfs/", code, Dituhui.MapType.RANGE, "Map.setData", "Map.onError");
}
/**
 * 显示数据列表
 */
Map.showDataList = function() {
    var data = Map.data_orders.concat();
    var len = data.length;
    if(len === 0) {
        return;
    }
    var h = '';
    var max = data[0].count, max_width = 250;
    
    for(var i=0; i<len; i++) {
        var item = data[i];
        if(!item.count || item.count == 0 || !item.name) {
            continue;
        }
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
    $('.list-group-item .top span.name').unbind('click').click(function(){
        var me = $(this);
        var code = me.attr('data-code');
        var level = me.attr('data-level');
        if( !code || !level || level == 3) {
            return;
        }
        $('.smcity').attr({
            'admincode': code,
            'level': level
        });
        if(level == 1) {
            SuperMap.Egisp.SMCity.showCurrentProvince(me.attr('data-name'));
        }
        else {
            SuperMap.Egisp.SMCity.showCurrentCity(me.attr('data-name'));
        }        
        Map.search();
    });  
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
Map.cityTagClick = function() {
    var smcity = $('.smcity');
    var code = smcity.attr('admincode');
    var level = smcity.attr('level');

    if(level && level == '3') {
        return;
    }
    Map.search();
}