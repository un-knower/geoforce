
/** 
 * 城市列表
 */
Dituhui.SMCity = Dituhui.SMCity || {};
Dituhui.SMCity.autoSearch = true;
Dituhui.SMCity.cityTagClick = function(){};
Dituhui.SMCity.isCheckAction = false;
Dituhui.SMCity.countyClick = true;

/**
 * 初始化用户所在的城市
 */
Dituhui.SMCity.initUserCity = function(callback) {
	Dituhui.User.getDefaultCity(function(e){		
		if(e == false) {
			Dituhui.IPLocate(urls.ip_locate, 
				function(e) {
					Dituhui.SMCity.init(e);
					callback();
				}, 
				function() {
					Dituhui.SMCity.init({city: "北京"});
					callback();
				}
			);
		}
		else {
			$('.show-default-city').html( '(默认: ' + e.defaultname + ')' );
			if(e.clevel == 1) {
				Dituhui.SMCity.showCurrentProvince(e.defaultname, e.admincode);
				callback();
			}
			else if(e.clevel == 2) {
				Dituhui.SMCity.showCurrentCity(e.defaultname, e.admincode);
				callback();
			}
			else if(e.clevel == 3) {
				Dituhui.SMCity.showCurrentCounty(e.defaultname, e.admincode, e.city);
				callback();
			}
			Dituhui.SMCity.initClicks();
		}
	});
}

/**
 * 初始化
 */
Dituhui.SMCity.init = function( current_city) {
	var data = current_city;
	if( !data || !data.city) {
		data = {
			city: "北京"
		};
	}
	var a_cityname = $('.current-city');
	var a_cityname_text = a_cityname.find('span.text');
	var div_provices = $('.all-provinces');

	if(data.city == "全国") {
		div_provices.removeClass("hide");
	}
	a_cityname_text.html( data.city );
	Dituhui.SMCity.initClicks();

	if( data.callback ) {
		Dituhui.SMCity.cityTagClick = data.callback;
	}
	if(data.countyClick == false) {
		Dituhui.SMCity.countyClick = false;
	}
	if(data.noSearch === true) {
		Dituhui.SMCity.autoSearch = false;	
		return;
	}
	Dituhui.SMCity.showCurrentCity(data.city);
}

/*
 * 初始化点击事件
 */
Dituhui.SMCity.initClicks = function() {	
	var a_cityname = $('.current-city');
	a_cityname.on("click", function(){
		$('.smcity-content').toggleClass("hide");
	});
	$('a.set-default-city').unbind('click').click(Dituhui.User.setDefaultCity);
	Dituhui.SMCity.LoadProvinces();
	
	if(Dituhui.User.hasTownAuthority) {
		$(".smcity-content").addClass("has-town-authority");
	}
	
	/*
	 * 选择省
	 */
	$('.smcity').on("click", 'a[option="toProvince"]', function(){
		if(Dituhui.SMCity.isCheckAction) {
			var flag = Map.checkAction();
			if(!flag) {
				return flag;
			}
		}
		var me = $(this);
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('data-value');

		$('.smcity').attr({admincode: code, level: level, adminname: name});
		$('.smcity-title .level-2, .smcity-title .level-3, .smcity-title .level-4').addClass("hide");

		//全国
		if(code == "" && level == "0" && Dituhui.SMCity.autoSearch) {
			map.setCenter(new SuperMap.LonLat(1.3023822823483625E7, 3892550.351786297), 4);
			layer_region.removeAllFeatures();
			layer_region_label.removeAllFeatures();
			layer_boundry.removeAllFeatures();
			return;
		}
		$('.smcity-title .level-1').removeClass("hide");
		$('.smcity-title a.province')
		.attr({admincode: code, level: level, "data-value": name})
		.html(name);
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

		Dituhui.SMCity.GetChildrenByCode( code, kids_index );
		if( Dituhui.SMCity.autoSearch ) {			
			Dituhui.SMCity.GetBoundryByCode( code, level );
			var btn = $('#btn_showRegion');
			var isShowRegion = btn.attr("isShowRegion") ? true : false;
			if( isShowRegion ){
				Map.searchRegions();
			}
		}
		Dituhui.SMCity.cityTagClick();
	});
	
	/*
	 * 选择全国
	 */
	$('.smcity').on("click", 'a[option="showWholeCountry"]', function(){
		if(Dituhui.SMCity.isCheckAction) {
			var flag = Map.checkAction();
			if(!flag) {
				return flag;
			}
		}
		$('.smcity-title .level-3, .smcity-content .child-citys, .smcity-title .level-4').addClass("hide");
		$('.smcity-content .all-provinces').removeClass("hide");

		$('.smcity').attr({admincode: '', level: 0, adminname: ''});
		$('.smcity .current-city .text').html('全国');

		if( Dituhui.SMCity.autoSearch ) {
			layer_region.removeAllFeatures();
			layer_region_label.removeAllFeatures();
			layer_boundry.removeAllFeatures();
			map.setCenter(new SuperMap.LonLat(1.3023822823483625E7, 3892550.351786297), 4);			
		}
		Dituhui.SMCity.cityTagClick();
	});
	
	/*
	 * 选择城市、区县、乡镇
	 */
	$('.smcity').on("click", 'a[option="toCity"]', function(event) {
		if(Dituhui.SMCity.isCheckAction) {
			var flag = Map.checkAction();
			if(!flag) {
				return flag;
			}
		}
		var me = $(this);
		if(me.attr("check-town") === "true" && !Dituhui.User.hasTownAuthority) {
			return;
		}
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('data-value');

		$('.smcity').attr({admincode: code, level: level, adminname: name});
		if( level == 2 ) {
			Dituhui.SMCity.GetChildrenByCode( code, 3 );
			$('.smcity-title .level-3, .smcity-title .level-4').addClass("hide");
			$('.smcity-title .level-2').removeClass("hide");
			$('.smcity-title a.city')
			.attr({admincode: code, level: level, "data-value": name}).html(name);
		}
		else if(level == 3){
			if(Dituhui.SMCity.countyClick){
				$('.child-citys').html('');
				//查乡镇
				if(Dituhui.User.hasTownAuthority) {
					Dituhui.SMCity.GetChildrenByCode( code, 4 );
				}
				$('.smcity-title .level-3').removeClass("hide");
				$('.smcity-title a.county')
				.attr({admincode: code, level: level, "data-value": name}).html(name);
				
				if( $('.smcity-title a.province').attr("zhixia") == "true" ) {
					$('.smcity-title .level-city').addClass("hide");				
				}
				$('.smcity-title .level-4').addClass("hide");
			}
		}
		//点击乡镇
		else {
			$('.child-citys').html('');
			$('.set-default-city').addClass("hide");
			$('.smcity-title .level-4').removeClass("hide");
			$('.smcity-title a.town')
			.attr({admincode: code, level: level, "data-value": name}).html(name);
		}
		
		if(level != 3 || Dituhui.SMCity.countyClick){
			$('.smcity .current-city .text').html(name);
		}
		

		if( Dituhui.SMCity.autoSearch ) {
			Dituhui.SMCity.GetBoundryByCode( code, level );
			
			if( $('#btn_showRegion').is(":checked") ){
				Map.searchRegions();
			}	
		}

		Dituhui.SMCity.cityTagClick(me);
	});
}

/**
 * 初始化省数据
 */
Dituhui.SMCity.LoadProvinces = function(){
	var div_provices = $('.all-provinces').html('');
	var h = '';
		h += '<a href="javascript:void(0);" option="toProvince" admincode="" level="0" data-value="全国">全国</a>';
	var pros = Dituhui.SMCity.provinces_zhixia.concat(), len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];		
		h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" zhixia="true"  data-value="'+ p.province +'">'+ p.province +'</a>';
	}
	pros = Dituhui.SMCity.provinces.concat();
	len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];		
		h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" data-value="'+ p.province +'">'+ p.province +'</a>';
	}
	pros = Dituhui.SMCity.province_other.concat();
	len = pros.length;
	for( var i=0; i<len; i++ ) {
		if(i === 0) {
			h += '<br>';
		}
		var p = pros[i];		
		h += '<a href="javascript:void(0);" option="toProvince" admincode="'+ p.admincode +'" level="1" data-value="'+ p.province +'">'+ p.province +'</a>';
	}
	div_provices.html( h );
}

/**
 * 根据城市重新组合控件
 */
Dituhui.SMCity.showCurrentCity = function(cityName, admincode) {	
	var code = admincode ? admincode : false;
	var current = Dituhui.SMCity.GetCityByName( cityName, code );

	$('.current-city span.text').html(current.city);
	if(current.zhixia) {
		$('.smcity-title .province, .set-default-city').removeClass("hide");
		$('.smcity-title a.province').attr({"admincode": current.admincode, "level": "1", 'zhixia': 'true',"data-value": current.city}).html(current.city);
		Dituhui.SMCity.GetChildrenByCode(current.admincode, 3);
		$('.smcity').attr({admincode: current.admincode, level: 1, adminname: current.city});

		if(Dituhui.SMCity.autoSearch) {
			Dituhui.SMCity.GetBoundryByCode(current.admincode, 1);
		}		
	}
	else {
		$('.smcity-title .province, .smcity-title .city, .set-default-city').removeClass("hide");
		$('.smcity-title a.province').attr(
			{
				"admincode": (current.admincode.substr(0, 2) + "0000"), 
				"level": "1", 
				"data-value": current.province
			}
		).html(current.province).removeAttr('zhixia');

		$('.smcity-title a.city').attr(
			{"admincode": current.admincode, "level": "2", "data-value": cityName}
		).html(cityName);
		Dituhui.SMCity.GetChildrenByCode(current.admincode, 3);
		$('.smcity').attr({admincode: current.admincode, level: 2, adminname: cityName});		
		if(Dituhui.SMCity.autoSearch) {
			Dituhui.SMCity.GetBoundryByCode(current.admincode, 2);
		}		
	}
}

/**
 * 根据省重新组合控件
 */
Dituhui.SMCity.showCurrentProvince = function(provinceName, code, level) {
	var currentAdmincode = code ? code : false;
	var current = Dituhui.SMCity.GetProvinceByName( provinceName, currentAdmincode );
	
	$('.current-city span.text').html(current.province);
	if(current.zhixia) {
		$('.smcity-title .province, .set-default-city').removeClass("hide");
		$('.smcity-title a.province')
		.attr({
			"admincode": current.admincode,
			"level": "1",
			'zhixia': 'true',
			"data-value": current.province
		})
		.html(current.province);
		Dituhui.SMCity.GetChildrenByCode(current.admincode, 3);
		$('.smcity').attr({admincode: current.admincode, level: 1, adminname: current.province});

		if(Dituhui.SMCity.autoSearch) {
			Dituhui.SMCity.GetBoundryByCode(current.admincode, 1);
		}
	}
	else {
		$('.smcity-title .province, .set-default-city').removeClass("hide");
		$('.smcity-title a.province').attr(
			{"admincode": (current.admincode.substr(0, 2) + "0000"), "level": "1",'data-value': current.province}
		).html(current.province).removeAttr('zhixia');
		$('.smcity').attr({admincode: current.admincode, level: 1, adminname: current.province});

		Dituhui.SMCity.GetChildrenByCode(current.admincode, 2);	

		if(Dituhui.SMCity.autoSearch) {
			Dituhui.SMCity.GetBoundryByCode(current.admincode, 1);
		}
	}
}

/**
 * 根据区县重新组合控件
 */
Dituhui.SMCity.showCurrentCounty = function(countyName, admincode, cityname) {	
	var code = admincode ? admincode : false;
	var cCode = admincode.substring(0,4) + '00';
	
	var zhixiacode = admincode.substring(0, 2) + '0000';
	if(Dituhui.SMCity.isZhixia(zhixiacode )) {
		cCode = zhixiacode;
	}
	var current = Dituhui.SMCity.GetCityByName( cityname, cCode );
	
	$('.current-city span.text').html(countyName);
	if(current.zhixia) {
		$('.smcity-title .province').removeClass("hide");
		$('.smcity-title a.province').attr(
			{
				"admincode": current.admincode, 
				"level": "1", 
				'zhixia': 'true',
				"data-value": current.city
			}
		).html(current.city);
	}
	else {
		$('.smcity-title .province, .smcity-title .city').removeClass("hide");
		$('.smcity-title a.province')
		.attr(
			{
				"admincode": (current.admincode.substr(0, 2) + "0000"),
				"level": "1", 
				"data-value": current.province
			}
		).html(current.province).removeAttr('zhixia');

		$('.smcity-title a.city').attr(
			{"admincode": current.admincode, "level": "2", "data-value": current.city}
		).html(current.city);
	}

	$('.smcity-title .county, .set-default-city').removeClass("hide");
	$('.smcity-title a.county').attr(
		{"admincode": admincode, "level": "3", "data-value": countyName}
	).html(countyName);

	$('.smcity').attr({admincode: admincode, level: 3, adminname: countyName});
	if(Dituhui.SMCity.autoSearch) {
		Dituhui.SMCity.GetBoundryByCode(admincode, 3);
	}	

	$('.all-provinces').addClass("hide");
	$('.child-citys').html('');
	
	if(Dituhui.User.hasTownAuthority) {
		Dituhui.SMCity.GetChildrenByCode(admincode, 4);
	}
}


/**
 * 显示城市或区县
 */
Dituhui.SMCity.LoadCitys = function(data) {
	var div_citys = $('.child-citys').html('');
	
	var len = data.length;
	if(len < 1) {
		return;
	}
	data = data.sort(function(a, b) { return a.ADMINCODE - b.ADMINCODE });

	var h = '';
	for( var i=0; i<len; i++ ) {
		var p = data[i];
		var level = p.CITY ? 2 : (p.COUNTY ? 3 : 4);
		var val = p.CITY ? p.CITY : (p.COUNTY ? p.COUNTY : p.TOWN);
		h += '<a href="javascript:void(0);" option="toCity" admincode="'+ p.ADMINCODE +'" level="'+ level +'" data-value="'+ val +'" data-smx="'+p.X+'" data-smy="'+ p.Y +'">'+ val +'</a>';
	}
	div_citys.html( h );
	div_citys.removeClass("hide");
	$('.all-provinces').addClass("hide");
}

/**
 * 根据城市名称获取admincode
 */
Dituhui.SMCity.GetCityByName = function(cityname, admincode) {
	var citys = Dituhui.SMCity.Citys.concat();
	var len = citys.length;
	var code = admincode ? admincode : false;
	var name = cityname ? cityname : false;
	for( var i=len; i--; ) {
		var c = citys[i];
		if(code && c.admincode == admincode) {
			return c;
		}
		if(name && cityname.match(c.city) ) {
			return c;
		}
	}
	return citys[0];
}
/**
 * 根据城市名称获取admincode
 */
Dituhui.SMCity.GetProvinceByName = function(provinceName, code) {
	var admincode = code ? code : false;
	var citys = Dituhui.SMCity.provinces_zhixia.concat();
	var len = citys.length;
	for( var i=len; i--; ) {
		var c = citys[i];
		if( provinceName.match(c.province) || (admincode == c.admincode)  ) {
			return c;
		}
	}
	citys = Dituhui.SMCity.provinces.concat();
	len = citys.length;
	for( var i=len; i--; ) {
		var c = citys[i];
		if( provinceName.match(c.province) || (admincode == c.admincode)   ) {
			return c;
		}
	}
	citys = Dituhui.SMCity.province_other.concat();
	len = citys.length;
	for( var i=len; i--; ) {
		var c = citys[i];
		if( provinceName.match(c.province ) || (admincode == c.admincode)   ) {
			return c;
		}
	}
	return false;
}

/**
 * 根据admincode查下级
 */
Dituhui.SMCity.GetChildrenByCode = function(code, level) {
	if( !code || !level ) {
		return;
	}
	$('.child-citys').html('');
	$('.all-provinces').addClass("hide");
	Dituhui.request({
		url: urls.server + "/orderService/getAdminElements?",
        data: {
        	admincode: code,
        	level: level
        },
        dataType: 'json',
        success: function(e){
        	if(e.isSuccess && e.result) {
        		Dituhui.SMCity.LoadCitys(e.result);
        	}
        },
        error: function(){}
	});
}
/**
 * 根据admincode查边界
 */
Dituhui.SMCity.GetBoundryByCode = function(code, level) {
	if( !code || !level ) {
		return;
	}
	layer_boundry.removeAllFeatures();
	Dituhui.request({
		url: urls.server + "/orderService/searchForAdminGeo?",
        data: {
        	admincode: code,
        	level: level
        },
        success: function(e){
        	if(e.isSuccess && e.result) {
        		var style = Dituhui.Zone.BoundryStyle;
        		var points = e.result.geo.points, pts=[];
        		 if( typeof(Baidu) != 'undefined' && Baidu.using) {
		        	for(var k=points.length; k--; ) {
		        		var item = points[k];
		        		var coord = Baidu.getCoord(item.x, item.y);
		        		points[k].x = coord.x;
		        		points[k].y = coord.y;
		        	}
		        }

        		var geometry = Dituhui.Zone.DrawRegion(e.result.geo.parts, points);

        		var attr = e.result;
        		var feature = new SuperMap.Feature.Vector( geometry, attr, style );
        		layer_boundry.addFeatures([feature]);

        		map.zoomToExtent(geometry.getBounds());
        	}
        },
        error: function(){}
	});
}

/**
 * 根据admincode判断是不是直辖市
 */
Dituhui.SMCity.isZhixia = function(admincode) {
	var data = Dituhui.SMCity.provinces_zhixia.concat();
	for(var i=data.length; i--;) {
		if(admincode === data[i].admincode) {
			return true;
		}
	}
	return false;
}










