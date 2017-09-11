
var Map = {};
Map.start = "";
Map.end = "";

$(function(){
	Map.init();
	
	$(".list-group").on("click", ".list-group-item .top span.name", function(){
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
            Dituhui.SMCity.showCurrentProvince(me.attr('data-name'));
        }
        else {
            Dituhui.SMCity.showCurrentCity(me.attr('data-name'));
        }        
        Map.search();
    });  
    
    $(".console-foot .btn-export").on("click", function(){
    	var url = "http://" + location.host + urls.server + "/areaService/exportAllArea?";
    	var smcity = $('.smcity');
	    var code = smcity.attr('admincode');
	    var level = smcity.attr('level'); 
	    if( code && code != "" ) {
	        url += "&admincode=" + code + "&level=" + level;
	    }
	    url += "&start="+Map.start + "&end=" + Map.end + "&isNeedPoint=false";
	    
	    window.open(url, "_blank");
    });
});

/**
 * 保存区划量统计的数据
 */
Map.data_regions = [];
/**
 * 地图分段设置
 */
Map.map_styles = [
    {min:1,max:25},
    {min:26,max:50},
    {min:51,max:75},
    {min:76,max:100}
];
/**
 * 地图初始化
 */
Map.init = function() {
	// map = new Dituhui.Map("map", "http://dev.dituhui.com/sdk/1.0.0/swfs/", "100000", Dituhui.MapType.RANGE, "Map.setData", "Map.onError");
	Dituhui.SMCity.init({
		city: "全国", 
		noSearch: true,
		callback: Map.cityTagClick,
		countyClick: false
	});
	
	/*var end = new Date().format("yyyy-MM-dd hh:mm:ss");
	var start = new Date(new Date().setDate( new Date().getDate() - 1));	
	start = start.format("yyyy-MM-dd hh:mm:ss");
	Map.start = start;
	Map.end = end;*/
    Map.search();
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
 * 地图初始化时设置统计数据
 */
Map.setData = function(){
	var option = {
		data: Map.data_regions, 
		styles: Map.map_styles, 
        regionField: 'admincode',
		valueField: 'count',
		isRLabelVisible:true,
		backgroundColor:"#f5f5f5",
        fillColorType: 'green',
		legendTitle:"区划量统计",
		legendPosition: "left_bottom",
		title: "区划量统计",
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

/**
 * 地图初始化时失败
 */
Map.onError = function(errorcode){
	alert(errorcode);
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
        Dituhui.SMCity.showCurrentProvince(e.data.name, e.data.admincode);
    }
    else {
        Dituhui.SMCity.showCurrentCity(e.data.name, e.data.admincode);
    }        
    Map.search();
}

/**
 * 搜索 区划量统计
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
    Dituhui.showMask();
    Dituhui.Order.statisticRegions(param,
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
                Map.data_regions = data;
                Map.setMapStyles();
                Map.loadMap(code);
            	Map.showDataList();
                return;
            }
            data = data.sort(function(a, b) { return b.count - a.count });
            Map.data_regions = data;
            Map.showDataList();
            Map.setMapStyles();

            Map.loadMap(code);
        },
        function(info){
            Dituhui.showHint(info);
        }
    );
}

/**
 * 显示数据列表
 */
Map.showDataList = function() {
    var data = Map.data_regions.concat();
    var len = data.length;
    if(len === 0) {
    	$(".console-foot .sum-number").html("共0条");
    	$(".console-foot .btn-export").addClass("hide");
        return;
    }
    var h = '';
    var max = data[0].count, max_width = 250;

    var data_usefull = [];    
    var index = 1, all=0;
    for(var i=0; i<len; i++) {
        var item = data[i];
        if(!item.name || item.count == '0') {
            continue;
        }
        all += Number(item.count);
        h += '<li class="list-group-item" data-code="'+ item.admincode +'">';
        h += '  <div class="order">'+index+'</div>';
        h += '  <div class="content">';
        h += '      <div class="top">';
        h += '          <span class="name" data-code="'+item.admincode+'" data-level="'+item.level+'" data-name="'+item.name+'">'+item.name+'</span>';
        h += '          <span class="count">'+ item.count +'个</span>';
        h += '      </div>';
        var width = Math.floor( item.count/max*250 );
        h += '      <div class="cus-progress-bar" style="width:'+ width +'px"></div>';
        h += '  </div>';
        h += '</li>';
        index++;
        // data_usefull.push(item);
    }
    $('ul.list-group').html(h); 
    // Map.data_regions = data_usefull.concat();   
    
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
    var data = Map.data_regions.concat();
    var max = data.length > 0 ? data[0].count : 0, len = data.length;
    var minValue = max == 0 ? 0 : 1;

    var power = (max + '').length;
    var maxValue = Math.pow(10, power);
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

/**
 * 自定义时间段检查
 */
Map.checkCustomTime = function() {
    var start = $('.custom-during input[name="start"]').val();
    var end = $('.custom-during input[name="end"]').val();
    if(start === "") {      
        Dituhui.showHint("请选择开始时间");
        return false;
    }
    if(end === "") {        
        Dituhui.showHint("请选择结束时间");
        return false;
    }

    var startdate = new Date(start.replace(/-/g,"/"));
    var enddate = new Date(end.replace(/-/g,"/")); 
    if(enddate <= startdate) {
        span.html("结束时间须晚于开始时间");
        return false;
    }
    
    Map.start = start;
    Map.end = end;
    Map.search();
}