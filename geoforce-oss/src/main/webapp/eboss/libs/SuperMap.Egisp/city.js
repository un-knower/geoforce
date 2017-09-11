SuperMap = {};
SuperMap.Egisp = {};
/** 
 * 城市列表
 */
SuperMap.Egisp.SMCity = {};
SuperMap.Egisp.SMCity.autoSearch = true;
SuperMap.Egisp.SMCity.cityTagClick = function(){};

SuperMap.Egisp.SMCity.init = function(){
	$('.smcity').attr({admincode: '', level: 0, adminname: ''});
	$('.current-city span.text').html("全国");
	$('.smcity-title .level-1,.smcity-title .level-2, .smcity-title .level-3').hide();
	$('.smcity-content').hide();
	SuperMap.Egisp.SMCity.LoadProvinces();
	$('.smcity-content .child-citys').hide();
	SuperMap.Egisp.SMCity.initClicks();
}
SuperMap.Egisp.SMCity.initClicks = function(){
	var a_cityname = $('.current-city');
	a_cityname.unbind('click').bind('click',function(){
		var me = $('.smcity-content');
		var bool = me.is(":visible");
		if( bool ) {
			me.hide();
		}
		else {
			me.show();
		}
	});
	$(".smcity-title-top .close").click(function(){
		$('.smcity-content').hide();
	});
}
/*显示省*/
SuperMap.Egisp.SMCity.LoadProvinces = function(){
	var div_provices = $('.all-provinces').html('');
	var h = '';
		h += '<a href="javascript:void(0);" option="toProvince" admincode="" level="0" value="全国">全国</a>';
	var pros = SuperMap.Egisp.SMCity.provinces_zhixia.concat(), len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];		
		h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" zhixia="true"  value="'+ p.province +'">'+ p.province +'</a>';
	}
	pros = SuperMap.Egisp.SMCity.provinces.concat();
	len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];		
		h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" value="'+ p.province +'">'+ p.province +'</a>';
	}
	pros = SuperMap.Egisp.SMCity.province_other.concat();
	len = pros.length;
	for( var i=0; i<len; i++ ) {
		if(i === 0) {
			h += '<br>';
		}
		var p = pros[i];		
		h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" value="'+ p.province +'">'+ p.province +'</a>';
	}
	div_provices.html( h ).show();

	$('a[option="toProvince"]').click(function(){		
		var me = $(this);
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');

		$('.smcity').attr({admincode: code, level: level, adminname: name});
		$('.smcity-title .level-2, .smcity-title .level-3').hide();

		//全国
		if(code == "" && level == "0" && SuperMap.Egisp.SMCity.autoSearch) {
			return;
		}
		$('.smcity-title .level-1').show();
		$('.smcity-title a.province').attr({admincode: code, level: level, value: name}).html(name);
		$('.smcity .current-city .text').html(name);
		var kids_index;
		if( me.attr('zhixia') === "true" ) {
			kids_index = 3;
			$('.smcity-title a.province').attr( "zhixia", "true" );
		}
		else {
			kids_index = 2;
			$('.smcity-title a.province').removeAttr( "zhixia" );
		}

		//SuperMap.Egisp.SMCity.GetChildrenByName(name,kids_index);
		SuperMap.Egisp.SMCity.GetChildrenByCode(code,kids_index);

	});

	$('a[option="showWholeCountry"]').click(function(){
		$('.smcity-title .level-3, .smcity-content .child-citys').hide();
		$('.smcity-content .all-provinces').show();

		$('.smcity').attr({admincode: '', level: 0, adminname: ''});
		$('.smcity .current-city .text').html('全国');

	});
}
SuperMap.Egisp.SMCity.GetChildrenByName = function(name,kids_index){
	var thisLevelArray = SuperMap.Egisp.SMCity.Citys;
	var len = thisLevelArray.length;
	var data = [];
	if(kids_index==2){
		for(var i=0;i<len;i++){
			if(thisLevelArray[i].province==name){
				data.push(thisLevelArray[i]);
			}
		}
	}
SuperMap.Egisp.SMCity.LoadCitys(data);	
	
}
/**
 * 显示城市或区县
 */
/*SuperMap.Egisp.SMCity.LoadCitys = function(data) {
	var div_citys = $('.child-citys').html('');
	
	var len = data.length;
	if(len < 1) {
		$('.child-citys').hide();
		return;
	}
	data = data.sort(function(a, b) { return a.admincode - b.admincode });

	var h = '';
	for( var i=0; i<len; i++ ) {
		var p = data[i];		
		var level = p.city ? 2 : 3;
		var val = p.city ? p.city : p.county;
		h += '<a href="javascript:void(0);" option="toCity" admincode="'+ p.admincode +'" level="'+ level +'" value="'+ val +'">'+ val +'</a>';
	}
	div_citys.html( h );
	div_citys.show();
	$('.all-provinces').hide();

	$('a[option="toCity"]').unbind('click').bind('click', function(event) {
		var me = $(this);
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');

		$('.smcity').attr({admincode: code, level: level, adminname: name});
		if( level == 2 ) {
			//SuperMap.Egisp.SMCity.GetChildrenByName( name, 3 );
			SuperMap.Egisp.SMCity.GetChildrenByCode(code,kids_index);
			$('.smcity-title .level-3').hide();
			$('.smcity-title .level-2').show();
			$('.smcity-title a.city').attr({admincode: code, level: level, value: name}).html(name);
		}
		else {
			$('.child-citys').html('');
			$('.smcity-title .level-3').show();
			$('.smcity-title a.county').attr({admincode: code, level: level, value: name}).html(name);
			
			if( $('.smcity-title a.province').attr("zhixia") == "true" ) {
				$('.smcity-title .level-city').hide();				
			}
		}
		$('.smcity .current-city .text').html(name);

	});
}*/
SuperMap.Egisp.SMCity.LoadCitys = function(data) {
	var div_citys = $('.child-citys').html('');
	
	var len = data.length;
	if(len < 1) {
		return;
	}
	data = data.sort(function(a, b) { return a.ADMINCODE - b.ADMINCODE });

	var h = '';
	for( var i=0; i<len; i++ ) {
		var p = data[i];		
		var level = p.CITY ? 2 : 3;
		var val = p.CITY ? p.CITY : p.COUNTY;
		h += '<a href="javascript:void(0);" option="toCity" admincode="'+ p.ADMINCODE +'" level="'+ level +'" value="'+ val +'">'+ val +'</a>';
	}
	div_citys.html( h );
	div_citys.show();
	$('.all-provinces').hide();

	$('a[option="toCity"]').unbind('click').bind('click', function(event) {
		var me = $(this);
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');

		$('.smcity').attr({admincode: code, level: level, adminname: name});
		if( level == 2 ) {
			SuperMap.Egisp.SMCity.GetChildrenByCode( code, 3 );
			$('.smcity-title .level-3').hide();
			$('.smcity-title .level-2').show();
			$('.smcity-title a.city').attr({admincode: code, level: level, value: name}).html(name);
		}
		else {
			$('.child-citys').html('');
			$('.smcity-title .level-3').show();
			$('.smcity-title a.county').attr({admincode: code, level: level, value: name}).html(name);
			
			if( $('.smcity-title a.province').attr("zhixia") == "true" ) {
				$('.smcity-title .level-city').hide();				
			}
		}
		$('.smcity .current-city .text').html(name);

		/*if( SuperMap.Egisp.SMCity.autoSearch ) {
			SuperMap.Egisp.SMCity.GetBoundryByCode( code, level );
			
			if( $('#btn_showRegion').is(":checked") ){
				Map.searchRegions();
			}			
		} 

		SuperMap.Egisp.SMCity.cityTagClick();*/
	});
}
/**
 * 根据admincode查下级
 */
SuperMap.Egisp.SMCity.GetChildrenByCode = function(code, level) {
	if( !code || !level ) {
		return;
	}
	$('.child-citys').html('');
	$('.all-provinces').hide();
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.point.getadminelements,
        data: {
        	admincode: code,
        	levels: level
        },
        dataType: 'json',
        success: function(e){
           if(e.success && e.result) {
        		SuperMap.Egisp.SMCity.LoadCitys(e.result);
        	} 
        },
        error: function(){}
    });
}
/**
 * 根据城市重新组合控件
 */
SuperMap.Egisp.SMCity.showCurrentCity = function(cityName, admincode) {	
	var code = admincode ? admincode : false;
	var current = SuperMap.Egisp.SMCity.GetCityByName( cityName, code );

	$('.current-city span.text').html(current.city);
	if(current.zhixia) {
		$('.smcity-title .province, .set-default-city').show();
		$('.smcity-title a.province').attr({"admincode": current.admincode, "level": "1", 'zhixia': 'true','value': current.city}).html(current.city);
		SuperMap.Egisp.SMCity.GetChildrenByCode(current.admincode, 3);
		$('.smcity').attr({admincode: current.admincode, level: 1, adminname: current.city});

		if(SuperMap.Egisp.SMCity.autoSearch) {
			SuperMap.Egisp.SMCity.GetBoundryByCode(current.admincode, 1);
		}		
	}
	else {
		$('.smcity-title .province, .smcity-title .city, .set-default-city').show();
		$('.smcity-title a.province').attr(
			{"admincode": (current.admincode.substr(0, 2) + "0000"), "level": "1",'value': current.province}
		).html(current.province).removeAttr('zhixia');

		$('.smcity-title a.city').attr(
			{"admincode": current.admincode, "level": "2"}
		).html(cityName);
		SuperMap.Egisp.SMCity.GetChildrenByCode(current.admincode, 3);
		$('.smcity').attr({admincode: current.admincode, level: 2, adminname: cityName});		
		if(SuperMap.Egisp.SMCity.autoSearch) {
			SuperMap.Egisp.SMCity.GetBoundryByCode(current.admincode, 2);
		}		
	}
}

/**
 * 根据admincode判断是不是直辖市
 */
SuperMap.Egisp.SMCity.isZhixia = function(admincode) {
	var data = SuperMap.Egisp.SMCity.provinces_zhixia.concat();
	for(var i=data.length; i--;) {
		if(admincode === data[i].admincode) {
			return true;
		}
	}
	return false;
}

/**
 * 直辖市
 */
SuperMap.Egisp.SMCity.provinces_zhixia = [
	{
		"province": "北京",
		"admincode": "110000",
		'zhixia': "true"
	},
	{
		"province": "天津",
		"admincode": "120000",
		'zhixia': "true"
	},
	{
		"province": "重庆",
		"admincode": "500000",
		'zhixia': "true"
	},
	{
		"province": "上海",
		"admincode": "310000",
		'zhixia': "true"
	}
];

/**
 * 省数据
 */
SuperMap.Egisp.SMCity.provinces = [	
	{
		"province": "安徽",
		"admincode": "340000"
	},
	{
		"province": "福建",
		"admincode": "350000"
	},
	{
		"province": "甘肃",
		"admincode": "620000"
	},
	{
		"province": "广东",
		"admincode": "440000"
	},
	{
		"province": "广西",
		"admincode": "450000"
	},
	{
		"province": "贵州",
		"admincode": "520000"
	},
	{
		"province": "海南",
		"admincode": "460000"
	},
	{
		"province": "河北",
		"admincode": "130000"
	},
	{
		"province": "河南",
		"admincode": "410000"
	},
	{
		"province": "黑龙江",
		"admincode": "230000"
	},
	{
		"province": "湖北",
		"admincode": "420000"
	},
	{
		"province": "湖南",
		"admincode": "430000"
	},
	{
		"province": "吉林",
		"admincode": "220000"
	},
	{
		"province": "江苏",
		"admincode": "320000"
	},
	{
		"province": "江西",
		"admincode": "360000"
	},
	{
		"province": "辽宁",
		"admincode": "210000"
	},
	{
		"province": "内蒙古",
		"admincode": "150000"
	},
	{
		"province": "宁夏",
		"admincode": "640000"
	},
	{
		"province": "青海",
		"admincode": "630000"
	},
	{
		"province": "山东",
		"admincode": "370000"
	},
	{
		"province": "山西",
		"admincode": "140000"
	},
	{
		"province": "陕西",
		"admincode": "610000"
	},
	{
		"province": "四川",
		"admincode": "510000"
	},
	{
		"province": "西藏",
		"admincode": "540000"
	},
	{
		"province": "新疆",
		"admincode": "650000"
	},
	{
		"province": "云南",
		"admincode": "530000"
	},
	{
		"province": "浙江",
		"admincode": "330000"
	}
];

/**
 * 特别行政区数据
 */
SuperMap.Egisp.SMCity.province_other = [
	{
		"province": "香港",
		"admincode": "810000"
	},
	{
		"province": "澳门",
		"admincode": "820000"
	},
	{
		"province": "台湾",
		"admincode": "710000"
	}
];


SuperMap.Egisp.SMCity.Citys = [	
	{
		"province": "北京",
		"city": "北京",
		"x": "12957140.380859",
		"y": "4854144.165039",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "110000",
		"zhixia": "true"
	},
	{
		"province": "天津",
		"city": "天津",
		"x": "13048615.966797",
		"y": "4738858.764648",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "120000",
		"zhixia": "true"
	},
	{
		"province": "重庆",
		"city": "重庆",
		"x": "11858744.514465",
		"y": "3445894.521077",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "500000",
		"zhixia": "true"
	},
	{
		"province": "上海",
		"city": "上海",
		"x": "13522735.351563",
		"y": "3661025.756836",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "310000",
		"zhixia": "true"
	},
	{
		"province": "其它",
		"city": "香港",
		"x": "12708843.511652",
		"y": "2544629.016138",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "",
		"": ""
	},
	{
		"province": "其它",
		"city": "澳门",
		"x": "12639572.220046",
		"y": "2534002.408874",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "",
		"": ""
	},
	{
		"province": "其它",
		"city": "台湾",
		"x": "13473333.333300",
		"y": "2741666.666660",
		"scale": "3671875.000000",
		"level": "7",
		"admincode": "",
		"": ""
	},
	{
		"province": "安徽",
		"city": "合肥市",
		"x": "13055615.060171",
		"y": "3745556.395213",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "340100",
		"": ""
	},
	{
		"province": "安徽",
		"city": "芜湖市",
		"x": "13183825.746076",
		"y": "3678993.560791",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "340200",
		"": ""
	},
	{
		"province": "安徽",
		"city": "蚌埠市",
		"x": "13066652.737935",
		"y": "3886469.977061",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "340300",
		"": ""
	},
	{
		"province": "安徽",
		"city": "淮南市",
		"x": "13026305.769898",
		"y": "3848394.533793",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "340400",
		"": ""
	},
	{
		"province": "安徽",
		"city": "马鞍山市",
		"x": "13192473.929196",
		"y": "3721261.838277",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "340500",
		"": ""
	},
	{
		"province": "安徽",
		"city": "淮北市",
		"x": "13005361.111959",
		"y": "4021408.693949",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "340600",
		"": ""
	},
	{
		"province": "安徽",
		"city": "铜陵市",
		"x": "13115001.741192",
		"y": "3624763.720195",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "340700",
		"": ""
	},
	{
		"province": "安徽",
		"city": "安庆市",
		"x": "13030628.201803",
		"y": "3572590.583801",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "340800",
		"": ""
	},
	{
		"province": "安徽",
		"city": "黄山市",
		"x": "13169631.186496",
		"y": "3469517.992655",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "341000",
		"": ""
	},
	{
		"province": "安徽",
		"city": "滁州市",
		"x": "13172557.645162",
		"y": "3803379.521688",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "341100",
		"": ""
	},
	{
		"province": "安徽",
		"city": "阜阳市",
		"x": "12892433.743795",
		"y": "3881022.466024",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "341200",
		"": ""
	},
	{
		"province": "安徽",
		"city": "宿州市",
		"x": "13022739.407857",
		"y": "3979207.770030",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "341300",
		"": ""
	},
	{
		"province": "安徽",
		"city": "六安市",
		"x": "12969513.233484",
		"y": "3730503.560384",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "341500",
		"": ""
	},
	{
		"province": "安徽",
		"city": "亳州市",
		"x": "12890650.756836",
		"y": "4011613.441467",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "341600",
		"": ""
	},
	{
		"province": "安徽",
		"city": "池州市",
		"x": "13078792.059449",
		"y": "3587622.680664",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "341700",
		"": ""
	},
	{
		"province": "安徽",
		"city": "宣城市",
		"x": "13220413.818359",
		"y": "3624216.225942",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "341800",
		"": ""
	},
	{
		"province": "福建",
		"city": "福州市",
		"x": "13282472.555124",
		"y": "3003024.419825",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "350100",
		"": ""
	},
	{
		"province": "福建",
		"city": "厦门市",
		"x": "13151164.550781",
		"y": "2812705.973307",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "350200",
		"": ""
	},
	{
		"province": "福建",
		"city": "莆田市",
		"x": "13251984.653919",
		"y": "2929572.716987",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "350300",
		"": ""
	},
	{
		"province": "福建",
		"city": "三明市",
		"x": "13094264.471760",
		"y": "3031771.614174",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "350400",
		"": ""
	},
	{
		"province": "福建",
		"city": "泉州市",
		"x": "13200734.863281",
		"y": "2864382.832845",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "350500",
		"": ""
	},
	{
		"province": "福建",
		"city": "漳州市",
		"x": "13099124.569548",
		"y": "2816127.438863",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "350600",
		"": ""
	},
	{
		"province": "福建",
		"city": "南平市",
		"x": "13154052.046097",
		"y": "3078526.774089",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "350700",
		"": ""
	},
	{
		"province": "福建",
		"city": "龙岩市",
		"x": "13025999.511719",
		"y": "2884055.094401",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "350800",
		"": ""
	},
	{
		"province": "福建",
		"city": "宁德市",
		"x": "13307049.987793",
		"y": "3079898.468018",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "350900",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "兰州市",
		"x": "11558550.684611",
		"y": "4309769.571940",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "620100",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "嘉峪关市",
		"x": "10940039.357503",
		"y": "4834013.875326",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "620200",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "金昌市",
		"x": "11376854.787191",
		"y": "4652606.038411",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "620300",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "白银市",
		"x": "11596228.759766",
		"y": "4375050.537109",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "620400",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "天水市",
		"x": "11771317.911784",
		"y": "4106887.003581",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "620500",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "武威市",
		"x": "11425417.724609",
		"y": "4569315.673828",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "620600",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "张掖市",
		"x": "11183052.937826",
		"y": "4711671.590169",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "620700",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "平凉市",
		"x": "11878528.811006",
		"y": "4236953.613281",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "620800",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "酒泉市",
		"x": "10965704.711914",
		"y": "4827540.283203",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "620900",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "庆阳市",
		"x": "11981814.876496",
		"y": "4264340.637207",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "621000",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "定西市",
		"x": "11646911.479237",
		"y": "4243511.702606",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "621100",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "陇南市",
		"x": "11680563.946695",
		"y": "3946739.227295",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "621200",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "临夏州",//临夏回族自治州
		"x": "11491075.850990",
		"y": "4246193.542480",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "622900",
		"": ""
	},
	{
		"province": "甘肃",
		"city": "甘南州",//甘南藏族自治州
		"x": "11455891.418457",
		"y": "4162033.020020",
		"scale": "28686.523438",
		"level": "14",
		"admincode": "623000",
		"": ""
	},
	{
		"province": "广东",
		"city": "广州市",
		"x": "12608484.375000",
		"y": "2647840.698242",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "440100",
		"": ""
	},
	{
		"province": "广东",
		"city": "韶关市",
		"x": "12646207.153320",
		"y": "2846280.680339",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440200",
		"": ""
	},
	{
		"province": "广东",
		"city": "深圳市",
		"x": "12694429.199219",
		"y": "2580002.807617",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "440300",
		"": ""
	},
	{
		"province": "广东",
		"city": "珠海市",
		"x": "12640819.107056",
		"y": "2541664.703369",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440400",
		"": ""
	},
	{
		"province": "广东",
		"city": "汕头市",
		"x": "12993460.866292",
		"y": "2677990.234375",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440500",
		"": ""
	},
	{
		"province": "广东",
		"city": "佛山市",
		"x": "12593796.875000",
		"y": "2630399.291992",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440600",
		"": ""
	},
	{
		"province": "广东",
		"city": "江门市",
		"x": "12587870.239258",
		"y": "2582043.375651",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440700",
		"": ""
	},
	{
		"province": "广东",
		"city": "湛江市",
		"x": "12283724.243164",
		"y": "2419545.694987",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440800",
		"": ""
	},
	{
		"province": "广东",
		"city": "茂名市",
		"x": "12347255.808512",
		"y": "2470247.690837",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "440900",
		"": ""
	},
	{
		"province": "广东",
		"city": "肇庆市",
		"x": "12522180.491130",
		"y": "2638431.040446",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "441200",
		"": ""
	},
	{
		"province": "广东",
		"city": "惠州市",
		"x": "12737811.350505",
		"y": "2645224.009196",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "441300",
		"": ""
	},
	{
		"province": "广东",
		"city": "梅州市",
		"x": "12925949.045817",
		"y": "2789448.374430",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "441400",
		"": ""
	},
	{
		"province": "广东",
		"city": "汕尾市",
		"x": "12841283.640544",
		"y": "2607656.138102",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "441500",
		"": ""
	},
	{
		"province": "广东",
		"city": "河源市",
		"x": "12769722.239176",
		"y": "2721518.686930",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "441600",
		"": ""
	},
	{
		"province": "广东",
		"city": "阳江市",
		"x": "12466104.075114",
		"y": "2495170.542399",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "441700",
		"": ""
	},
	{
		"province": "广东",
		"city": "清远市",
		"x": "12582720.530192",
		"y": "2717318.979899",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "441800",
		"": ""
	},
	{
		"province": "广东",
		"city": "东莞市",
		"x": "12664480.468750",
		"y": "2634828.491211",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "441900",
		"": ""
	},
	{
		"province": "广东",
		"city": "中山市",
		"x": "12620242.502848",
		"y": "2574047.007243",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "442000",
		"": ""
	},
	{
		"province": "广东",
		"city": "潮州市",
		"x": "12983319.702148",
		"y": "2711945.037842",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "445100",
		"": ""
	},
	{
		"province": "广东",
		"city": "揭阳市",
		"x": "12955794.504801",
		"y": "2696802.378337",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "445200",
		"": ""
	},
	{
		"province": "广东",
		"city": "云浮市",
		"x": "12473746.164958",
		"y": "2623892.710368",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "445300",
		"": ""
	},
	{
		"province": "广西",
		"city": "南宁市",
		"x": "12057047.159831",
		"y": "2609596.781413",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "450100",
		"": ""
	},
	{
		"province": "广西",
		"city": "柳州市",
		"x": "12180854.370117",
		"y": "2792542.694092",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "450200",
		"": ""
	},
	{
		"province": "广西",
		"city": "桂林市",
		"x": "12278755.737305",
		"y": "2909560.760498",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "450300",
		"": ""
	},
	{
		"province": "广西",
		"city": "梧州市",
		"x": "12387583.323161",
		"y": "2691042.124430",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "450400",
		"": ""
	},
	{
		"province": "广西",
		"city": "北海市",
		"x": "12153534.520467",
		"y": "2450928.273519",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "450500",
		"": ""
	},
	{
		"province": "广西",
		"city": "防城港市",
		"x": "12061968.332926",
		"y": "2474917.856852",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "450600",
		"": ""
	},
	{
		"province": "广西",
		"city": "钦州市",
		"x": "12092525.217692",
		"y": "2504671.518962",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "450700",
		"": ""
	},
	{
		"province": "广西",
		"city": "贵港市",
		"x": "12202463.450114",
		"y": "2643147.104899",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "450800",
		"": ""
	},
	{
		"province": "广西",
		"city": "玉林市",
		"x": "12260134.358724",
		"y": "2589234.130859",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "450900",
		"": ""
	},
	{
		"province": "广西",
		"city": "百色市",
		"x": "11869825.520833",
		"y": "2737050.048828",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "451000",
		"": ""
	},
	{
		"province": "广西",
		"city": "贺州市",
		"x": "12420629.720052",
		"y": "2802283.203125",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "451100",
		"": ""
	},
	{
		"province": "广西",
		"city": "河池市",
		"x": "12031215.901693",
		"y": "2837435.668945",
		"scale": "28686.523438",
		"level": "14",
		"admincode": "451200",
		"": ""
	},
	{
		"province": "广西",
		"city": "来宾市",
		"x": "12157195.638021",
		"y": "2725552.490234",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "451300",
		"": ""
	},
	{
		"province": "广西",
		"city": "崇左市",
		"x": "11953693.440755",
		"y": "2559962.402344",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "451400",
		"": ""
	},
	{
		"province": "贵州",
		"city": "贵阳市",
		"x": "11876539.840698",
		"y": "3070315.495809",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "520100",
		"": ""
	},
	{
		"province": "贵州",
		"city": "六盘水市",
		"x": "11668549.397787",
		"y": "3074503.967285",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "520200",
		"": ""
	},
	{
		"province": "贵州",
		"city": "遵义市",
		"x": "11902608.479818",
		"y": "3212176.330566",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "520300",
		"": ""
	},
	{
		"province": "贵州",
		"city": "安顺市",
		"x": "11791511.311849",
		"y": "3027297.424316",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "520400",
		"": ""
	},
	{
		"province": "贵州",
		"city": "毕节市",
		"x": "11722181.722005",
		"y": "3160230.773926",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "520500",
		"": ""
	},
	{
		"province": "贵州",
		"city": "铜仁市",
		"x": "12154324.595134",
		"y": "3212711.812337",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "520600",
		"": ""
	},
	{
		"province": "贵州",
		"city": "黔西南州",//黔西南布依族苗族自治州
		"x": "11677594.736735",
		"y": "2887154.195150",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "522300",
		"": ""
	},
	{
		"province": "贵州",
		"city": "黔东南州",//黔东南苗族侗族自治州
		"x": "12023132.039388",
		"y": "3070671.447754",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "522600",
		"": ""
	},
	{
		"province": "贵州",
		"city": "黔南州",//黔南布依族苗族自治州
		"x": "11968449.788412",
		"y": "3033493.713379",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "522700",
		"": ""
	},
	{
		"province": "海南",
		"city": "海口市",
		"x": "12281731.246948",
		"y": "2275539.586385",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "460100",
		"": ""
	},
	{
		"province": "海南",
		"city": "三亚市",
		"x": "12190133.983507",
		"y": "2068168.830563",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "460200",
		"": ""
	},
	{
		"province": "海南",
		"city": "五指山市",
		"x": "12191379.912604",
		"y": "2128465.611811",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469001",
		"": ""
	},
	{
		"province": "海南",
		"city": "琼海市",
		"x": "12297978.466244",
		"y": "2185447.910882",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469002",
		"": ""
	},
	{
		"province": "海南",
		"city": "儋州市",
		"x": "12198464.028963",
		"y": "2216385.905847",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469003",
		"": ""
	},
	{
		"province": "海南",
		"city": "文昌市",
		"x": "12333946.405977",
		"y": "2219024.397249",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469005",
		"": ""
	},
	{
		"province": "海南",
		"city": "万宁市",
		"x": "12288729.174657",
		"y": "2130780.166512",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469006",
		"": ""
	},
	{
		"province": "海南",
		"city": "东方市",
		"x": "12095066.868031",
		"y": "2166168.978046",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469007",
		"": ""
	},
	{
		"province": "海南",
		"city": "定安县",
		"x": "12285133.510577",
		"y": "2235283.001541",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469021",
		"": ""
	},
	{
		"province": "海南",
		"city": "屯昌县",
		"x": "12256664.753057",
		"y": "2196391.361331",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469022",
		"": ""
	},
	{
		"province": "海南",
		"city": "澄迈县",
		"x": "12245937.728625",
		"y": "2242050.108648",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469023",
		"": ""
	},
	{
		"province": "海南",
		"city": "临高县",
		"x": "12210714.972696",
		"y": "2262673.945154",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469024",
		"": ""
	},
	{
		"province": "海南",
		"city": "白沙县",//白沙黎族自治县
		"x": "12184084.378268",
		"y": "2181488.270558",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469025",
		"": ""
	},
	{
		"province": "海南",
		"city": "昌江县",//昌江黎族自治县
		"x": "12140012.836016",
		"y": "2190066.139735",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469026",
		"": ""
	},
	{
		"province": "海南",
		"city": "乐东县",//乐东黎族自治县
		"x": "12153155.960939",
		"y": "2125491.333517",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469027",
		"": ""
	},
	{
		"province": "海南",
		"city": "陵水县",//陵水黎族自治县
		"x": "12249302.037408",
		"y": "2096856.485454",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469028",
		"": ""
	},
	{
		"province": "海南",
		"city": "保亭县",//保亭黎族苗族自治县
		"x": "12212031.537182",
		"y": "2112504.806688",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469029",
		"": ""
	},
	{
		"province": "海南",
		"city": "琼中县",//琼中黎族苗族自治县
		"x": "12227154.100763",
		"y": "2158880.312107",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "469030",
		"": ""
	},
	{
		"province": "河北",
		"city": "石家庄市",
		"x": "12747010.162354",
		"y": "4583250.152588",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "130100",
		"": ""
	},
	{
		"province": "河北",
		"city": "唐山市",
		"x": "13155815.592448",
		"y": "4813131.998698",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "130200",
		"": ""
	},
	{
		"province": "河北",
		"city": "秦皇岛市",
		"x": "13313397.359212",
		"y": "4857441.202799",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "130300",
		"": ""
	},
	{
		"province": "河北",
		"city": "邯郸市",
		"x": "12745167.770386",
		"y": "4385263.058980",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "130400",
		"": ""
	},
	{
		"province": "河北",
		"city": "邢台市",
		"x": "12747634.811401",
		"y": "4447495.722453",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "130500",
		"": ""
	},
	{
		"province": "河北",
		"city": "保定市",
		"x": "12854460.675558",
		"y": "4703551.511129",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "130600",
		"": ""
	},
	{
		"province": "河北",
		"city": "张家口市",
		"x": "12787911.919676",
		"y": "4981964.141846",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "130700",
		"": ""
	},
	{
		"province": "河北",
		"city": "承德市",
		"x": "13129438.931783",
		"y": "5007802.810669",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "130800",
		"": ""
	},
	{
		"province": "河北",
		"city": "沧州市",
		"x": "13008421.246847",
		"y": "4620767.941793",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "130900",
		"": ""
	},
	{
		"province": "河北",
		"city": "廊坊市",
		"x": "12990856.608073",
		"y": "4795893.310547",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "131000",
		"": ""
	},
	{
		"province": "河北",
		"city": "衡水市",
		"x": "12877915.733337",
		"y": "4542107.582092",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "131100",
		"": ""
	},
	{
		"province": "河南",
		"city": "郑州市",
		"x": "12653014.226278",
		"y": "4130380.549113",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "410100",
		"": ""
	},
	{
		"province": "河南",
		"city": "开封市",
		"x": "12731784.790039",
		"y": "4137845.499674",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "410200",
		"": ""
	},
	{
		"province": "河南",
		"city": "洛阳市",
		"x": "12517482.594808",
		"y": "4113871.215820",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "410300",
		"": ""
	},
	{
		"province": "河南",
		"city": "平顶山市",
		"x": "12616371.916305",
		"y": "3994214.947540",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "410400",
		"": ""
	},
	{
		"province": "河南",
		"city": "安阳市",
		"x": "12730935.668945",
		"y": "4313177.530924",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "410500",
		"": ""
	},
	{
		"province": "河南",
		"city": "鹤壁市",
		"x": "12723058.349609",
		"y": "4264284.220378",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "410600",
		"": ""
	},
	{
		"province": "河南",
		"city": "新乡市",
		"x": "12678978.637695",
		"y": "4204122.843424",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "410700",
		"": ""
	},
	{
		"province": "河南",
		"city": "焦作市",
		"x": "12606046.020508",
		"y": "4192028.605143",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "410800",
		"": ""
	},
	{
		"province": "河南",
		"city": "濮阳市",
		"x": "12804958.374023",
		"y": "4266716.837565",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "410900",
		"": ""
	},
	{
		"province": "河南",
		"city": "许昌市",
		"x": "12671829.956055",
		"y": "4030489.054362",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411000",
		"": ""
	},
	{
		"province": "河南",
		"city": "漯河市",
		"x": "12698875.610352",
		"y": "3971578.409831",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411100",
		"": ""
	},
	{
		"province": "河南",
		"city": "三门峡市",
		"x": "12381878.051758",
		"y": "4130616.495768",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "411200",
		"": ""
	},
	{
		"province": "河南",
		"city": "南阳市",
		"x": "12528615.356445",
		"y": "3895674.825033",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411300",
		"": ""
	},
	{
		"province": "河南",
		"city": "商丘市",
		"x": "12873255.249023",
		"y": "4086267.130534",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411400",
		"": ""
	},
	{
		"province": "河南",
		"city": "信阳市",
		"x": "12705221.069336",
		"y": "3780388.468424",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411500",
		"": ""
	},
	{
		"province": "河南",
		"city": "周口市",
		"x": "12762536.743164",
		"y": "3976443.644206",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411600",
		"": ""
	},
	{
		"province": "河南",
		"city": "驻马店市",
		"x": "12697865.844727",
		"y": "3892770.792643",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "411700",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "哈尔滨市",
		"x": "14099519.978841",
		"y": "5740124.572754",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "230100",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "齐齐哈尔市",
		"x": "13800458.669027",
		"y": "5998576.283773",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "230200",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "鸡西市",
		"x": "14577879.520105",
		"y": "5670390.622457",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "230300",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "鹤岗市",
		"x": "14503211.761703",
		"y": "5993647.939046",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "230400",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "双鸭山市",
		"x": "14603328.386498",
		"y": "5888631.835938",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "230500",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "大庆市",
		"x": "13923108.617147",
		"y": "5875053.548177",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "230600",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "伊春市",
		"x": "14346676.908988",
		"y": "6062399.495443",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "230700",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "佳木斯市",
		"x": "14511021.423848",
		"y": "5910349.446615",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "230800",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "七台河市",
		"x": "14586926.468311",
		"y": "5744046.259562",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "230900",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "牡丹江市",
		"x": "14429202.000936",
		"y": "5556455.520630",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "231000",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "黑河市",
		"x": "14193610.699972",
		"y": "6488667.966207",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "231100",
		"": ""
	},
	{
		"province": "黑龙江",
		"city": "绥化市",
		"x": "14136105.913006",
		"y": "5883888.640823",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "231200",
		"": ""
	},
	{
		"province": "湖北",
		"city": "武汉市",
		"x": "12724778.823853",
		"y": "3580065.216064",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "420100",
		"": ""
	},
	{
		"province": "湖北",
		"city": "黄石市",
		"x": "12806679.565430",
		"y": "3529542.032878",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "420200",
		"": ""
	},
	{
		"province": "湖北",
		"city": "十堰市",
		"x": "12337695.068359",
		"y": "3845891.276042",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "420300",
		"": ""
	},
	{
		"province": "湖北",
		"city": "宜昌市",
		"x": "12391407.714844",
		"y": "3597626.627604",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "420500",
		"": ""
	},
	{
		"province": "湖北",
		"city": "襄阳市",
		"x": "12479145.516990",
		"y": "3769232.281525",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "420600",
		"": ""
	},
	{
		"province": "湖北",
		"city": "鄂州市",
		"x": "12789984.008789",
		"y": "3553644.449870",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "420700",
		"": ""
	},
	{
		"province": "湖北",
		"city": "荆门市",
		"x": "12493049.804688",
		"y": "3632802.042643",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "420800",
		"": ""
	},
	{
		"province": "湖北",
		"city": "孝感市",
		"x": "12682535.766602",
		"y": "3623260.904948",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "420900",
		"": ""
	},
	{
		"province": "湖北",
		"city": "荆州市",
		"x": "12497639.648438",
		"y": "3545141.764323",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "421000",
		"": ""
	},
	{
		"province": "湖北",
		"city": "黄冈市",
		"x": "12790414.306641",
		"y": "3560780.700684",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "421100",
		"": ""
	},
	{
		"province": "湖北",
		"city": "咸宁市",
		"x": "12724951.660156",
		"y": "3485106.608073",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "421200",
		"": ""
	},
	{
		"province": "湖北",
		"city": "随州市",
		"x": "12620696.706136",
		"y": "3726069.580078",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "421300",
		"": ""
	},
	{
		"province": "湖北",
		"city": "恩施州",//恩施土家族苗族自治州
		"x": "12187943.288167",
		"y": "3539044.921875",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "422800",
		"": ""
	},
	{
		"province": "湖北",
		"city": "仙桃市",
		"x": "12628544.931053",
		"y": "3551134.381134",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "429004",
		"": ""
	},
	{
		"province": "湖北",
		"city": "潜江市",
		"x": "12565007.628400",
		"y": "3556638.368764",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "429005",
		"": ""
	},
	{
		"province": "湖北",
		"city": "天门市",
		"x": "12597294.255273",
		"y": "3586120.465119",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "429006",
		"": ""
	},
	{
		"province": "湖北",
		"city": "神农架林区",
		"x": "12312313.780744",
		"y": "3709273.622671",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "429021",
		"": ""
	},
	{
		"province": "湖南",
		"city": "长沙市",
		"x": "12576574.681600",
		"y": "3273126.825968",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "430100",
		"": ""
	},
	{
		"province": "湖南",
		"city": "株洲市",
		"x": "12594525.512695",
		"y": "3227799.967448",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430200",
		"": ""
	},
	{
		"province": "湖南",
		"city": "湘潭市",
		"x": "12570870.605469",
		"y": "3230106.363932",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430300",
		"": ""
	},
	{
		"province": "湖南",
		"city": "衡阳市",
		"x": "12534203.491211",
		"y": "3107855.875651",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "430400",
		"": ""
	},
	{
		"province": "湖南",
		"city": "邵阳市",
		"x": "12407598.388672",
		"y": "3153008.463542",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430500",
		"": ""
	},
	{
		"province": "湖南",
		"city": "岳阳市",
		"x": "12596232.838949",
		"y": "3423564.453125",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430600",
		"": ""
	},
	{
		"province": "湖南",
		"city": "常德市",
		"x": "12432891.774495",
		"y": "3379456.054688",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430700",
		"": ""
	},
	{
		"province": "湖南",
		"city": "张家界市",
		"x": "12301647.583008",
		"y": "3391381.998698",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430800",
		"": ""
	},
	{
		"province": "湖南",
		"city": "益阳市",
		"x": "12509481.445313",
		"y": "3322517.130534",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "430900",
		"": ""
	},
	{
		"province": "湖南",
		"city": "郴州市",
		"x": "12583521.362305",
		"y": "2971858.805339",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "431000",
		"": ""
	},
	{
		"province": "湖南",
		"city": "永州市",
		"x": "12424423.512777",
		"y": "3053749.267578",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "431100",
		"": ""
	},
	{
		"province": "湖南",
		"city": "怀化市",
		"x": "12244440.917969",
		"y": "3191976.236979",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "431200",
		"": ""
	},
	{
		"province": "湖南",
		"city": "娄底市",
		"x": "12468178.588867",
		"y": "3216508.951823",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "431300",
		"": ""
	},
	{
		"province": "湖南",
		"city": "湘西州",//湘西土家族苗族自治州
		"x": "12217728.027344",
		"y": "3285654.947917",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "433100",
		"": ""
	},
	{
		"province": "吉林",
		"city": "长春市",
		"x": "13949477.986654",
		"y": "5448072.814941",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "220100",
		"": ""
	},
	{
		"province": "吉林",
		"city": "吉林市",
		"x": "14089071.629842",
		"y": "5444859.207153",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "220200",
		"": ""
	},
	{
		"province": "吉林",
		"city": "四平市",
		"x": "13847679.913839",
		"y": "5337959.714254",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "220300",
		"": ""
	},
	{
		"province": "吉林",
		"city": "辽源市",
		"x": "13929172.589620",
		"y": "5299519.772848",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "220400",
		"": ""
	},
	{
		"province": "吉林",
		"city": "通化市",
		"x": "14019436.087940",
		"y": "5121301.877340",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "220500",
		"": ""
	},
	{
		"province": "吉林",
		"city": "白山市",
		"x": "14073897.412139",
		"y": "5151936.687237",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "220600",
		"": ""
	},
	{
		"province": "吉林",
		"city": "松原市",
		"x": "13895642.406464",
		"y": "5641328.245799",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "220700",
		"": ""
	},
	{
		"province": "吉林",
		"city": "白城市",
		"x": "13673922.414144",
		"y": "5717276.234945",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "220800",
		"": ""
	},
	{
		"province": "吉林",
		"city": "延边州",//延边朝鲜族自治州
		"x": "14416600.130717",
		"y": "5298615.191142",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "222400",
		"": ""
	},
	{
		"province": "江苏",
		"city": "南京市",
		"x": "13216445.0015",
		"y": "3777566.9711",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320100",
		"": ""
	},
	{
		"province": "江苏",
		"city": "无锡市",
		"x": "13396098.199626",
		"y": "3703220.584869",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320200",
		"": ""
	},
	{
		"province": "江苏",
		"city": "徐州市",
		"x": "13044772.450765",
		"y": "4063963.358561",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320300",
		"": ""
	},
	{
		"province": "江苏",
		"city": "常州市",
		"x": "13354698.781331",
		"y": "3731395.711263",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320400",
		"": ""
	},
	{
		"province": "江苏",
		"city": "苏州市",
		"x": "13433942.814476",
		"y": "3676066.667834",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320500",
		"": ""
	},
	{
		"province": "江苏",
		"city": "南通市",
		"x": "13459178.480702",
		"y": "3759232.755025",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320600",
		"": ""
	},
	{
		"province": "江苏",
		"city": "连云港市",
		"x": "13265151.945750",
		"y": "4107645.343781",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320700",
		"": ""
	},
	{
		"province": "江苏",
		"city": "淮安市",
		"x": "13251385.821025",
		"y": "3975903.022766",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320800",
		"": ""
	},
	{
		"province": "江苏",
		"city": "盐城市",
		"x": "13376664.352057",
		"y": "3944756.151835",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "320900",
		"": ""
	},
	{
		"province": "江苏",
		"city": "扬州市",
		"x": "13301182.219187",
		"y": "3818083.576202",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "321000",
		"": ""
	},
	{
		"province": "江苏",
		"city": "镇江市",
		"x": "13297453.030904",
		"y": "3786788.073222",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "321100",
		"": ""
	},
	{
		"province": "江苏",
		"city": "泰州市",
		"x": "13348798.303653",
		"y": "3822398.686727",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "321200",
		"": ""
	},
	{
		"province": "江苏",
		"city": "宿迁市",
		"x": "13168579.302470",
		"y": "4020182.703654",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "321300",
		"": ""
	},
	{
		"province": "江西",
		"city": "南昌市",
		"x": "12899427.637736",
		"y": "3334565.470378",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "360100",
		"": ""
	},
	{
		"province": "江西",
		"city": "景德镇市",
		"x": "13044397.852580",
		"y": "3414226.272583",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "360200",
		"": ""
	},
	{
		"province": "江西",
		"city": "萍乡市",
		"x": "12674985.473633",
		"y": "3202607.582092",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "360300",
		"": ""
	},
	{
		"province": "江西",
		"city": "九江市",
		"x": "12913014.770508",
		"y": "3464678.994497",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "360400",
		"": ""
	},
	{
		"province": "江西",
		"city": "新余市",
		"x": "12796134.399414",
		"y": "3223239.049276",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "360500",
		"": ""
	},
	{
		"province": "江西",
		"city": "鹰潭市",
		"x": "13028928.409113",
		"y": "3277017.913818",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "360600",
		"": ""
	},
	{
		"province": "江西",
		"city": "赣州市",
		"x": "12794464.843750",
		"y": "2979284.790039",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "360700",
		"": ""
	},
	{
		"province": "江西",
		"city": "吉安市",
		"x": "12797867.065430",
		"y": "3139975.219727",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "360800",
		"": ""
	},
	{
		"province": "江西",
		"city": "宜春市",
		"x": "12735181.274414",
		"y": "3224690.467834",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "360900",
		"": ""
	},
	{
		"province": "江西",
		"city": "抚州市",
		"x": "12953457.031250",
		"y": "3242969.520569",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "361000",
		"": ""
	},
	{
		"province": "江西",
		"city": "上饶市",
		"x": "13131422.485352",
		"y": "3304710.449219",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "361100",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "沈阳市",
		"x": "13742471.252441",
		"y": "5131859.354655",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "210100",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "大连市",
		"x": "13534608.801826",
		"y": "4712429.033915",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "210200",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "鞍山市",
		"x": "13689398.297851",
		"y": "5028820.432891",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "210300",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "抚顺市",
		"x": "13797814.130147",
		"y": "5143634.335836",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "210400",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "本溪市",
		"x": "13778287.258915",
		"y": "5055606.750488",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "210500",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "丹东市",
		"x": "13846570.538716",
		"y": "4884153.497048",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "210600",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "锦州市",
		"x": "13486005.067190",
		"y": "5029426.532745",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "210700",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "营口市",
		"x": "13605189.503988",
		"y": "4962444.875081",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "210800",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "阜新市",
		"x": "13541826.358137",
		"y": "5162809.146798",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "210900",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "辽阳市",
		"x": "13712444.128758",
		"y": "5048524.286906",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "211000",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "盘锦市",
		"x": "13588729.176839",
		"y": "5030965.504964",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "211100",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "铁岭市",
		"x": "13786298.540366",
		"y": "5202861.554570",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "211200",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "朝阳市",
		"x": "13409000.038129",
		"y": "5100090.851414",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "211300",
		"": ""
	},
	{
		"province": "辽宁",
		"city": "葫芦岛市",
		"x": "13451880.613843",
		"y": "4971819.621173",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "211400",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "呼和浩特市",
		"x": "12434141.563211",
		"y": "4982295.471191",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "150100",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "包头市",
		"x": "12230296.312726",
		"y": "4957903.559367",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "150200",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "乌海市",
		"x": "11891757.324219",
		"y": "4817727.579753",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "150300",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "赤峰市",
		"x": "13239790.890749",
		"y": "5201998.048477",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "150400",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "通辽市",
		"x": "13609968.678792",
		"y": "5407243.764242",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "150500",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "鄂尔多斯市",
		"x": "12222975.270589",
		"y": "4807098.744710",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "150600",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "呼伦贝尔市",
		"x": "13330774.459839",
		"y": "6309417.475382",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "150700",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "巴彦淖尔市",
		"x": "11957353.816581",
		"y": "4979440.652744",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "150800",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "乌兰察布市",
		"x": "12592993.588635",
		"y": "5011253.133669",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "150900",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "兴安盟",
		"x": "13591986.531576",
		"y": "5795815.633138",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "152200",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "锡林郭勒盟",
		"x": "12919560.408838",
		"y": "5455735.104879",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "152500",
		"": ""
	},
	{
		"province": "内蒙古",
		"city": "阿拉善盟",
		"x": "11765052.536011",
		"y": "4699415.033976",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "152900",
		"": ""
	},
	{
		"province": "宁夏",
		"city": "银川市",
		"x": "11823079.396566",
		"y": "4648825.154622",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "640100",
		"": ""
	},
	{
		"province": "宁夏",
		"city": "石嘴山市",
		"x": "11840746.948242",
		"y": "4724744.995117",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "640200",
		"": ""
	},
	{
		"province": "宁夏",
		"city": "吴忠市",
		"x": "11822135.131836",
		"y": "4577571.655273",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "640300",
		"": ""
	},
	{
		"province": "宁夏",
		"city": "固原市",
		"x": "11829301.025391",
		"y": "4302399.047852",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "640400",
		"": ""
	},
	{
		"province": "宁夏",
		"city": "中卫市",
		"x": "11714338.348389",
		"y": "4509819.824219",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "640500",
		"": ""
	},
	{
		"province": "青海",
		"city": "西宁市",
		"x": "11329636.197817",
		"y": "4386989.423117",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "630100",
		"": ""
	},
	{
		"province": "青海",
		"city": "海东地区",
		"x": "11363466.308594",
		"y": "4371344.238281",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "632100",
		"": ""
	},
	{
		"province": "青海",
		"city": "海北州",//海北藏族自治州
		"x": "11231382.080078",
		"y": "4434471.801758",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "632200",
		"": ""
	},
	{
		"province": "青海",
		"city": "黄南州",//黄南藏族自治州
		"x": "11356696.289063",
		"y": "4235088.989258",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "632300",
		"": ""
	},
	{
		"province": "青海",
		"city": "海南州",//海南藏族自治州
		"x": "11200188.354492",
		"y": "4338962.890625",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "632500",
		"": ""
	},
	{
		"province": "青海",
		"city": "果洛州",//果洛藏族自治州
		"x": "11159746.093750",
		"y": "4091817.016602",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "632600",
		"": ""
	},
	{
		"province": "青海",
		"city": "玉树州",//玉树藏族自治州
		"x": "10798204.101563",
		"y": "3896347.045898",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "632700",
		"": ""
	},
	{
		"province": "青海",
		"city": "海西州",//海西蒙古族藏族自治州
		"x": "10839868.408203",
		"y": "4489733.520508",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "632800",
		"": ""
	},
	{
		"province": "山东",
		"city": "济南市",
		"x": "13024530.761719",
		"y": "4393059.936523",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "370100",
		"": ""
	},
	{
		"province": "山东",
		"city": "青岛市",
		"x": "13401707.997050",
		"y": "4317356.041592",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "370200",
		"": ""
	},
	{
		"province": "山东",
		"city": "淄博市",
		"x": "13140498.901367",
		"y": "4411165.077209",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "370300",
		"": ""
	},
	{
		"province": "山东",
		"city": "枣庄市",
		"x": "13090383.544922",
		"y": "4138747.212728",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "370400",
		"": ""
	},
	{
		"province": "山东",
		"city": "东营市",
		"x": "13205020.629883",
		"y": "4508660.888672",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "370500",
		"": ""
	},
	{
		"province": "山东",
		"city": "烟台市",
		"x": "13505157.091563",
		"y": "4510443.397522",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "370600",
		"": ""
	},
	{
		"province": "山东",
		"city": "潍坊市",
		"x": "13260018.432617",
		"y": "4394349.873861",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "370700",
		"": ""
	},
	{
		"province": "山东",
		"city": "济宁市",
		"x": "12979492.919922",
		"y": "4217766.273499",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "370800",
		"": ""
	},
	{
		"province": "山东",
		"city": "泰安市",
		"x": "13034462.036133",
		"y": "4322496.826172",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "370900",
		"": ""
	},
	{
		"province": "山东",
		"city": "威海市",
		"x": "13588536.499023",
		"y": "4509238.563538",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "371000",
		"": ""
	},
	{
		"province": "山东",
		"city": "日照市",
		"x": "13302566.284180",
		"y": "4219567.787170",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "371100",
		"": ""
	},
	{
		"province": "山东",
		"city": "莱芜市",
		"x": "13100452.514648",
		"y": "4330561.564128",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "371200",
		"": ""
	},
	{
		"province": "山东",
		"city": "临沂市",
		"x": "13173568.725586",
		"y": "4173164.347331",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "371300",
		"": ""
	},
	{
		"province": "山东",
		"city": "德州市",
		"x": "12951879.272461",
		"y": "4499812.052409",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "371400",
		"": ""
	},
	{
		"province": "山东",
		"city": "聊城市",
		"x": "12910432.983398",
		"y": "4363115.030924",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "371500",
		"": ""
	},
	{
		"province": "山东",
		"city": "滨州市",
		"x": "13133442.016602",
		"y": "4496186.075846",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "371600",
		"": ""
	},
	{
		"province": "山东",
		"city": "菏泽市",
		"x": "12857890.747070",
		"y": "4198775.675456",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "371700",
		"": ""
	},
	{
		"province": "山西",
		"city": "太原市",
		"x": "12529500.096639",
		"y": "4563968.505859",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "140100",
		"": ""
	},
	{
		"province": "山西",
		"city": "大同市",
		"x": "12612417.058309",
		"y": "4877148.844401",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "140200",
		"": ""
	},
	{
		"province": "山西",
		"city": "阳泉市",
		"x": "12644885.182699",
		"y": "4559964.345296",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "140300",
		"": ""
	},
	{
		"province": "山西",
		"city": "长治市",
		"x": "12591738.855998",
		"y": "4327533.223470",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "140400",
		"": ""
	},
	{
		"province": "山西",
		"city": "晋城市",
		"x": "12564574.391683",
		"y": "4231612.660726",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "140500",
		"": ""
	},
	{
		"province": "山西",
		"city": "朔州市",
		"x": "12518266.095479",
		"y": "4769060.056051",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "140600",
		"": ""
	},
	{
		"province": "山西",
		"city": "晋中市",
		"x": "12550611.106873",
		"y": "4535079.503377",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "140700",
		"": ""
	},
	{
		"province": "山西",
		"city": "运城市",
		"x": "12357127.808182",
		"y": "4167449.551971",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "140800",
		"": ""
	},
	{
		"province": "山西",
		"city": "忻州市",
		"x": "12548271.123250",
		"y": "4639863.723755",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "140900",
		"": ""
	},
	{
		"province": "山西",
		"city": "临汾市",
		"x": "12414469.719272",
		"y": "4311363.373026",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "141000",
		"": ""
	},
	{
		"province": "山西",
		"city": "吕梁市",
		"x": "12372460.374139",
		"y": "4513406.117757",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "141100",
		"": ""
	},
	{
		"province": "陕西",
		"city": "西安市",
		"x": "12127619.832357",
		"y": "4065332.422892",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "610100",
		"": ""
	},
	{
		"province": "陕西",
		"city": "铜川市",
		"x": "12130390.950521",
		"y": "4150231.384277",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "610200",
		"": ""
	},
	{
		"province": "陕西",
		"city": "宝鸡市",
		"x": "11940354.207357",
		"y": "4077591.369629",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "610300",
		"": ""
	},
	{
		"province": "陕西",
		"city": "咸阳市",
		"x": "12101661.875407",
		"y": "4076423.350016",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "610400",
		"": ""
	},
	{
		"province": "陕西",
		"city": "渭南市",
		"x": "12189261.433919",
		"y": "4097144.104004",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "610500",
		"": ""
	},
	{
		"province": "陕西",
		"city": "延安市",
		"x": "12189754.842122",
		"y": "4384537.170410",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "610600",
		"": ""
	},
	{
		"province": "陕西",
		"city": "汉中市",
		"x": "11916656.748454",
		"y": "3904929.097493",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "610700",
		"": ""
	},
	{
		"province": "陕西",
		"city": "榆林市",
		"x": "12216752.207438",
		"y": "4620118.550618",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "610800",
		"": ""
	},
	{
		"province": "陕西",
		"city": "安康市",
		"x": "12134080.037435",
		"y": "3856246.154785",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "610900",
		"": ""
	},
	{
		"province": "陕西",
		"city": "商洛市",
		"x": "12236938.435872",
		"y": "4011864.807129",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "611000",
		"": ""
	},
	{
		"province": "四川",
		"city": "成都市",
		"x": "11584493.820190",
		"y": "3589061.070760",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "510100",
		"": ""
	},
	{
		"province": "四川",
		"city": "自贡市",
		"x": "11662823.567708",
		"y": "3420213.867188",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "510300",
		"": ""
	},
	{
		"province": "四川",
		"city": "攀枝花市",
		"x": "11322910.018921",
		"y": "3070746.988932",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "510400",
		"": ""
	},
	{
		"province": "四川",
		"city": "泸州市",
		"x": "11736507.771810",
		"y": "3362691.650391",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "510500",
		"": ""
	},
	{
		"province": "四川",
		"city": "德阳市",
		"x": "11619937.215169",
		"y": "3647244.750977",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "510600",
		"": ""
	},
	{
		"province": "四川",
		"city": "绵阳市",
		"x": "11657155.110677",
		"y": "3695776.611328",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "510700",
		"": ""
	},
	{
		"province": "四川",
		"city": "广元市",
		"x": "11777925.374349",
		"y": "3821939.941406",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "510800",
		"": ""
	},
	{
		"province": "四川",
		"city": "遂宁市",
		"x": "11751774.739583",
		"y": "3570473.876953",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "510900",
		"": ""
	},
	{
		"province": "四川",
		"city": "内江市",
		"x": "11695560.628255",
		"y": "3449192.993164",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511000",
		"": ""
	},
	{
		"province": "四川",
		"city": "乐山市",
		"x": "11548410.237630",
		"y": "3450914.184570",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511100",
		"": ""
	},
	{
		"province": "四川",
		"city": "南充市",
		"x": "11809324.447632",
		"y": "3606071.940104",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511300",
		"": ""
	},
	{
		"province": "四川",
		"city": "眉山市",
		"x": "11558415.499369",
		"y": "3510383.260091",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511400",
		"": ""
	},
	{
		"province": "四川",
		"city": "宜宾市",
		"x": "11647399.302165",
		"y": "3345217.732747",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511500",
		"": ""
	},
	{
		"province": "四川",
		"city": "广安市",
		"x": "11869102.620443",
		"y": "3562401.489258",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511600",
		"": ""
	},
	{
		"province": "四川",
		"city": "达州市",
		"x": "11966453.206380",
		"y": "3661071.655273",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "511700",
		"": ""
	},
	{
		"province": "四川",
		"city": "雅安市",
		"x": "11466068.440755",
		"y": "3502578.613281",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "511800",
		"": ""
	},
	{
		"province": "四川",
		"city": "巴中市",
		"x": "11884579.477946",
		"y": "3744901.326497",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "511900",
		"": ""
	},
	{
		"province": "四川",
		"city": "资阳市",
		"x": "11646805.013021",
		"y": "3519876.586914",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "512000",
		"": ""
	},
	{
		"province": "四川",
		"city": "阿坝州",//阿坝藏族羌族自治州
		"x": "11379996.509829",
		"y": "3750325.950145",
		"scale": "14343.261719",
		"level": "15",
		"admincode": "513200",
		"": ""
	},
	{
		"province": "四川",
		"city": "甘孜州",//甘孜藏族自治州
		"x": "11350372.654238",
		"y": "3509542.747020",
		"scale": "14343.261719",
		"level": "15",
		"admincode": "513300",
		"": ""
	},
	{
		"province": "四川",
		"city": "凉山州",//凉山彝族自治州
		"x": "11381248.128255",
		"y": "3228998.107910",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "513400",
		"": ""
	},
	{
		"province": "西藏",
		"city": "拉萨市",
		"x": "10144030.405680",
		"y": "3459794.576009",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "540100",
		"": ""
	},
	{
		"province": "西藏",
		"city": "昌都地区",
		"x": "10817781.697591",
		"y": "3650695.739746",
		"scale": "28686.523438",
		"level": "14",
		"admincode": "542100",
		"": ""
	},
	{
		"province": "西藏",
		"city": "山南地区",
		"x": "10216535.115560",
		"y": "3406762.756348",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "542200",
		"": ""
	},
	{
		"province": "西藏",
		"city": "日喀则地区",
		"x": "9894075.642904",
		"y": "3409493.713379",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "542300",
		"": ""
	},
	{
		"province": "西藏",
		"city": "那曲地区",
		"x": "10248205.037435",
		"y": "3696645.812988",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "542400",
		"": ""
	},
	{
		"province": "西藏",
		"city": "阿里地区",
		"x": "8916106.160482",
		"y": "3829705.383301",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "542500",
		"": ""
	},
	{
		"province": "西藏",
		"city": "林芝地区",
		"x": "10505546.101888",
		"y": "3457044.494629",
		"scale": "458984.375000",
		"level": "10",
		"admincode": "542600",
		"": ""
	},
	{
		"province": "新疆",
		"city": "乌鲁木齐市",
		"x": "9749676.289876",
		"y": "5438651.204427",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "650100",
		"": ""
	},
	{
		"province": "新疆",
		"city": "克拉玛依市",
		"x": "9449617.645264",
		"y": "5715147.216797",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "650200",
		"": ""
	},
	{
		"province": "新疆",
		"city": "吐鲁番地区",
		"x": "9927080.686984",
		"y": "5305450.113932",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "652100",
		"": ""
	},
	{
		"province": "新疆",
		"city": "哈密地区",
		"x": "10409428.558350",
		"y": "5286564.819336",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "652200",
		"": ""
	},
	{
		"province": "新疆",
		"city": "昌吉州",//昌吉回族自治州
		"x": "9715618.746437",
		"y": "5464404.052734",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "652300",
		"": ""
	},
	{
		"province": "新疆",
		"city": "博尔塔拉州",//博尔塔拉蒙古自治州
		"x": "9137129.608154",
		"y": "5607435.058594",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "652700",
		"": ""
	},
	{
		"province": "新疆",
		"city": "巴音郭楞州",//巴音郭楞蒙古自治州
		"x": "9595161.590576",
		"y": "5121829.589844",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "652800",
		"": ""
	},
	{
		"province": "新疆",
		"city": "阿克苏地区",
		"x": "8935365.814209",
		"y": "5036986.328125",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "652900",
		"": ""
	},
	{
		"province": "新疆",
		"city": "克孜勒苏州",//克孜勒苏柯尔克孜自治州
		"x": "8479827.9891",
		"y": "4824002.2747",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "653000",
		"": ""
	},
	{
		"province": "新疆",
		"city": "喀什地区",
		"x": "8457660.614014",
		"y": "4790798.583984",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "653100",
		"": ""
	},
	{
		"province": "新疆",
		"city": "和田地区",
		"x": "8896988.983154",
		"y": "4454730.224609",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "653200",
		"": ""
	},
	{
		"province": "新疆",
		"city": "伊犁州",//伊犁哈萨克自治州
		"x": "9067902.083530",
		"y": "5455999.837522",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "654000",
		"": ""
	},
	{
		"province": "新疆",
		"city": "塔城地区",
		"x": "9237819.305420",
		"y": "5901014.373779",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "654200",
		"": ""
	},
	{
		"province": "新疆",
		"city": "阿勒泰地区",
		"x": "9811447.937012",
		"y": "6081145.660400",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "654300",
		"": ""
	},
	{
		"province": "新疆",
		"city": "石河子市",
		"x": "9580945.983887",
		"y": "5512647.613525",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "659001",
		"": ""
	},
	{
		"province": "新疆",
		"city": "阿拉尔市",
		"x": "9048319.0453",
		"y": "4941069.2576",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "659002",
		"": ""
	},
	{
		"province": "新疆",
		"city": "图木舒克市",
		"x": "8802630.401611",
		"y": "4845988.108317",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "659003",
		"": ""
	},
	{
		"province": "云南",
		"city": "昆明市",
		"x": "11434425.875532",
		"y": "2880797.022502",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "530100",
		"": ""
	},
	{
		"province": "云南",
		"city": "曲靖市",
		"x": "11555834.309896",
		"y": "2941019.836426",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "530300",
		"": ""
	},
	{
		"province": "云南",
		"city": "玉溪市",
		"x": "11414444.173177",
		"y": "2796726.623535",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "530400",
		"": ""
	},
	{
		"province": "云南",
		"city": "保山市",
		"x": "11039838.338216",
		"y": "2890795.471191",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "530500",
		"": ""
	},
	{
		"province": "云南",
		"city": "昭通市",
		"x": "11547159.505208",
		"y": "3169192.443848",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "530600",
		"": ""
	},
	{
		"province": "云南",
		"city": "丽江市",
		"x": "11157789.194743",
		"y": "3108201.070150",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "530700",
		"": ""
	},
	{
		"province": "云南",
		"city": "普洱市",
		"x": "11240305.501302",
		"y": "2605112.121582",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "530800",
		"": ""
	},
	{
		"province": "云南",
		"city": "临沧市",
		"x": "11142576.253255",
		"y": "2739456.848145",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "530900",
		"": ""
	},
	{
		"province": "云南",
		"city": "楚雄州",//楚雄彝族自治州
		"x": "11302130.696615",
		"y": "2883543.518066",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "532300",
		"": ""
	},
	{
		"province": "云南",
		"city": "红河州",//红河哈尼族彝族自治州
		"x": "11510498.128255",
		"y": "2674441.711426",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "532500",
		"": ""
	},
	{
		"province": "云南",
		"city": "文山州",//文山壮族苗族自治州
		"x": "11605450.520833",
		"y": "2676323.547363",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "532600",
		"": ""
	},
	{
		"province": "云南",
		"city": "西双版纳州",//西双版纳傣族自治州
		"x": "11223110.799154",
		"y": "2512362.854004",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "532800",
		"": ""
	},
	{
		"province": "云南",
		"city": "大理州",//大理白族自治州
		"x": "11161027.425130",
		"y": "2949453.674316",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "532900",
		"": ""
	},
	{
		"province": "云南",
		"city": "德宏州",//德宏傣族景颇族自治州
		"x": "10975012.532552",
		"y": "2808465.148926",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "533100",
		"": ""
	},
	{
		"province": "云南",
		"city": "怒江州",//怒江傈僳族自治州
		"x": "11004100.667318",
		"y": "2981892.395020",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "533300",
		"": ""
	},
	{
		"province": "云南",
		"city": "迪庆州",//迪庆藏族自治州
		"x": "11099328.450521",
		"y": "3225475.402832",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "533400",
		"": ""
	},
	{
		"province": "浙江",
		"city": "杭州市",
		"x": "13383070.768992",
		"y": "3539319.416046",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "330100",
		"": ""
	},
	{
		"province": "浙江",
		"city": "宁波市",
		"x": "13526549.284617",
		"y": "3489260.894775",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "330200",
		"": ""
	},
	{
		"province": "浙江",
		"city": "温州市",
		"x": "13434362.272898",
		"y": "3250703.765869",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "330300",
		"": ""
	},
	{
		"province": "浙江",
		"city": "嘉兴市",
		"x": "13442727.322896",
		"y": "3602024.869283",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "330400",
		"": ""
	},
	{
		"province": "浙江",
		"city": "湖州市",
		"x": "13379193.534331",
		"y": "3615594.202919",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "330500",
		"": ""
	},
	{
		"province": "浙江",
		"city": "绍兴市",
		"x": "13423518.707275",
		"y": "3503110.748291",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "330600",
		"": ""
	},
	{
		"province": "浙江",
		"city": "金华市",
		"x": "13318910.550435",
		"y": "3388572.512309",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "330700",
		"": ""
	},
	{
		"province": "浙江",
		"city": "衢州市",
		"x": "13234715.604146",
		"y": "3371440.800985",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "330800",
		"": ""
	},
	{
		"province": "浙江",
		"city": "舟山市",
		"x": "13601439.232863",
		"y": "3503565.190633",
		"scale": "229492.187500",
		"level": "11",
		"admincode": "330900",
		"": ""
	},
	{
		"province": "浙江",
		"city": "台州市",
		"x": "13517329.495748",
		"y": "3332071.774801",
		"scale": "114746.093750",
		"level": "12",
		"admincode": "331000",
		"": ""
	},
	{
		"province": "浙江",
		"city": "丽水市",
		"x": "13349794.461568",
		"y": "3305617.062887",
		"scale": "57373.046875",
		"level": "13",
		"admincode": "331100",
		"": ""
	}
]