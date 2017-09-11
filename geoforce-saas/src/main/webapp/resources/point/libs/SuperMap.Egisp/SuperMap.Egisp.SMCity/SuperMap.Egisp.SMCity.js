
/** 
 * 城市列表
 */
SuperMap.Egisp.SMCity = SuperMap.Egisp.SMCity || {};
SuperMap.Egisp.SMCity.autoSearch = true;
SuperMap.Egisp.SMCity.cityTagClick = function(){};

/**
 * 初始化用户所在的城市
 */
SuperMap.Egisp.SMCity.initUserCity = function(callback) {
	SuperMap.Egisp.User.getDefaultCity(function(e){
		if(e == false) {
			SuperMap.Egisp.IPLocate(urls.ip_locate, 
				function(e) {	
					SuperMap.Egisp.SMCity.init(e);
					callback();
				}, 
				function() {
					SuperMap.Egisp.SMCity.init({city: "北京"});
					callback();
				}
			);
		}
		else {
			$('.show-default-city').html( '(默认: ' + e.defaultname + ')' );
			if(e.clevel == 1) {
				SuperMap.Egisp.SMCity.showCurrentProvince(e.defaultname, e.admincode);
				callback();
			}
			else if(e.clevel == 2) {
				SuperMap.Egisp.SMCity.showCurrentCity(e.defaultname, e.admincode);
				callback();
			}
			else if(e.clevel == 3) {
				SuperMap.Egisp.SMCity.showCurrentCounty(e.defaultname, e.admincode, e.city);
				callback();
			}
			SuperMap.Egisp.SMCity.initClicks();
		}
	});
}

/**
 * 初始化
 */
SuperMap.Egisp.SMCity.init = function( current_city) {
	var data = current_city;
	if( !data || !data.city) {
		data = {
			city: "北京"
		};
	}
	var a_cityname = $('.current-city');
	var a_cityname_text = a_cityname.find('span.text');
	var div_provices = $('.all-provinces');

	a_cityname_text.html( data.city );
	SuperMap.Egisp.SMCity.initClicks();

	if( data.callback ) {
		SuperMap.Egisp.SMCity.cityTagClick = data.callback;
	}
	if(data.noSearch === true) {	
		SuperMap.Egisp.SMCity.autoSearch = false;	
		return;
	}
	SuperMap.Egisp.SMCity.showCurrentCity(data.city);
}
SuperMap.Egisp.SMCity.initClicks = function() {	
	var a_cityname = $('.current-city');
	a_cityname.click(function(){
		var me = $('.smcity-content');
		var bool = me.is(":visible");
		if( bool ) {
			me.hide();
		}
		else {
			me.show();
		}
	});
	$('a.set-default-city').unbind('click').click(SuperMap.Egisp.User.setDefaultCity);
	SuperMap.Egisp.SMCity.LoadProvinces();
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
 * 根据省重新组合控件
 */
SuperMap.Egisp.SMCity.showCurrentProvince = function(provinceName, code) {
	var currentAdmincode = code ? code : false;
	var current = SuperMap.Egisp.SMCity.GetProvinceByName( provinceName, currentAdmincode );
	
	$('.current-city span.text').html(current.province);
	if(current.zhixia) {
		$('.smcity-title .province, .set-default-city').show();
		$('.smcity-title a.province')
		.attr({
			"admincode": current.admincode,
			"level": "1",
			'zhixia': 'true',
			'value': current.province
		})
		.html(current.province);
		SuperMap.Egisp.SMCity.GetChildrenByCode(current.admincode, 3);
		$('.smcity').attr({admincode: current.admincode, level: 1, adminname: current.province});

		if(SuperMap.Egisp.SMCity.autoSearch) {
			SuperMap.Egisp.SMCity.GetBoundryByCode(current.admincode, 1);
		}		
	}
	else {
		$('.smcity-title .province, .set-default-city').show();
		$('.smcity-title a.province').attr(
			{"admincode": (current.admincode.substr(0, 2) + "0000"), "level": "1",'value': current.province}
		).html(current.province).removeAttr('zhixia');
		$('.smcity').attr({admincode: current.admincode, level: 1, adminname: current.province});

		SuperMap.Egisp.SMCity.GetChildrenByCode(current.admincode, 2);	

		if(SuperMap.Egisp.SMCity.autoSearch) {
			SuperMap.Egisp.SMCity.GetBoundryByCode(current.admincode, 1);
		}		
	}
}
/**
 * 根据区县重新组合控件
 */
SuperMap.Egisp.SMCity.showCurrentCounty = function(countyName, admincode, cityname) {	
	var code = admincode ? admincode : false;
	var cCode = admincode.substring(0,4) + '00';
	var current = SuperMap.Egisp.SMCity.GetCityByName( cityname, cCode );

	$('.current-city span.text').html(countyName);
	if(current.zhixia) {
		$('.smcity-title .province').show();
		$('.smcity-title a.province').attr({"admincode": current.admincode, "level": "1", 'zhixia': 'true','value': current.city}).html(current.city);			
	}
	else {
		$('.smcity-title .province, .smcity-title .city').show();
		$('.smcity-title a.province').attr(
			{"admincode": (current.admincode.substr(0, 2) + "0000"), "level": "1",'value': current.province}
		).html(current.province).removeAttr('zhixia');

		$('.smcity-title a.city').attr(
			{"admincode": current.admincode, "level": "2"}
		).html(current.city);
	}

	$('.smcity-title .county, .set-default-city').show();
	$('.smcity-title a.county').attr(
		{"admincode": admincode, "level": "3"}
	).html(countyName);

	$('.smcity').attr({admincode: admincode, level: 3, adminname: countyName});
	if(SuperMap.Egisp.SMCity.autoSearch) {
		SuperMap.Egisp.SMCity.GetBoundryByCode(admincode, 3);
	}	

	$('.all-provinces').hide();
	$('.child-citys').html('');
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

		if( SuperMap.Egisp.SMCity.autoSearch ) {
			SuperMap.Egisp.SMCity.GetBoundryByCode( code, level );
			
			if( $('#btn_showRegion').is(":checked") ){
				Map.searchRegions();
			}			
		} 

		SuperMap.Egisp.SMCity.cityTagClick();
	});
}

/**
 * 初始化省数据
 */
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
	div_provices.html( h );

	$('a[option="toProvince"]').click(function(){		
		var me = $(this);
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');

		$('.smcity').attr({admincode: code, level: level, adminname: name});
		$('.smcity-title .level-2, .smcity-title .level-3').hide();

		//全国
		if(code == "" && level == "0" && SuperMap.Egisp.SMCity.autoSearch) {
			map.setCenter(new SuperMap.LonLat(1.3023822823483625E7, 3892550.351786297), 4);
			layer_region.removeAllFeatures();
			layer_region_label.removeAllFeatures();
			layer_boundry.removeAllFeatures();
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

		SuperMap.Egisp.SMCity.GetChildrenByCode( code, kids_index );
		if( SuperMap.Egisp.SMCity.autoSearch ) {			
			SuperMap.Egisp.SMCity.GetBoundryByCode( code, level );
			var btn = $('#btn_showRegion');
			var isShowRegion = btn.attr("isShowRegion") ? true : false;
			if( isShowRegion ){
				Map.searchRegions();
			}
		}
		SuperMap.Egisp.SMCity.cityTagClick();
	});

	$('a[option="showWholeCountry"]').click(function(){
		$('.smcity-title .level-3, .smcity-content .child-citys').hide();
		$('.smcity-content .all-provinces').show();

		$('.smcity').attr({admincode: '', level: 0, adminname: ''});
		$('.smcity .current-city .text').html('全国');

		if( SuperMap.Egisp.SMCity.autoSearch ) {
			layer_region.removeAllFeatures();
			layer_region_label.removeAllFeatures();
			layer_boundry.removeAllFeatures();
			map.setCenter(new SuperMap.LonLat(1.3023822823483625E7, 3892550.351786297), 4);			
		}
		SuperMap.Egisp.SMCity.cityTagClick();
	});
}

/**
 * 显示城市或区县
 */
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

		if( SuperMap.Egisp.SMCity.autoSearch ) {
			SuperMap.Egisp.SMCity.GetBoundryByCode( code, level );
			
			if( $('#btn_showRegion').is(":checked") ){
				Map.searchRegions();
			}			
		} 

		SuperMap.Egisp.SMCity.cityTagClick();
	});
}

/**
 * 根据城市名称获取admincode
 */
SuperMap.Egisp.SMCity.GetCityByName = function(cityname, admincode) {
	var citys = SuperMap.Egisp.SMCity.Citys.concat();
	var len = citys.length;
	var code = admincode ? admincode : false;
	
	for( var i=len; i--; ) {
		var c = citys[i];
		if( cityname.match(c.city) ) {
			return c;
		}
		if(code && c.admincode == admincode) {
			return c;
		}
	}
	return citys[0];
}
/**
 * 根据城市名称获取admincode
 */
SuperMap.Egisp.SMCity.GetProvinceByName = function(provinceName, code) {
	var admincode = code ? code : false;
	var citys = SuperMap.Egisp.SMCity.provinces_zhixia.concat();
	var len = citys.length;
	for( var i=len; i--; ) {
		var c = citys[i];
		if( provinceName.match(c.province) || (admincode == c.admincode)  ) {
			return c;
		}
	}
	citys = SuperMap.Egisp.SMCity.provinces.concat();
	len = citys.length;
	for( var i=len; i--; ) {
		var c = citys[i];
		if( provinceName.match(c.province) || (admincode == c.admincode)   ) {
			return c;
		}
	}
	citys = SuperMap.Egisp.SMCity.province_other.concat();
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
SuperMap.Egisp.SMCity.GetChildrenByCode = function(code, level) {
	if( !code || !level ) {
		return;
	}
	$('.child-citys').html('');
	$('.all-provinces').hide();
	SuperMap.Egisp.request({
		url: urls.server + "/orderService/getAdminElements?",
        data: {
        	admincode: code,
        	level: level
        },
        dataType: 'json',
        success: function(e){
        	if(e.isSuccess && e.result) {
        		SuperMap.Egisp.SMCity.LoadCitys(e.result);
        	}
        },
        error: function(){}
	});
}
/**
 * 根据admincode查边界
 */
SuperMap.Egisp.SMCity.GetBoundryByCode = function(code, level) {
	if( !code || !level ) {
		return;
	}
	layer_boundry.removeAllFeatures();
	SuperMap.Egisp.request({
		url: urls.server + "/orderService/searchForAdminGeo?",
        data: {
        	admincode: code,
        	level: level
        },
        success: function(e){
        	if(e.isSuccess && e.result) {
        		var style = SuperMap.Egisp.Zone.BoundryStyle;
        		var points = e.result.geo.points, pts=[];
        		 if( typeof(Baidu) != 'undefined' && Baidu.using) {
		        	for(var k=points.length; k--; ) {
		        		var item = points[k];
		        		var coord = Baidu.getCoord(item.x, item.y);
		        		points[k].x = coord.x;
		        		if(k==0)
		        			console.log(item)
		        			console.log(coord)
		        		points[k].y = coord.y;
		        	}
		        }

        		var geometry = SuperMap.Egisp.Zone.DrawRegion(e.result.geo.parts, points);

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
SuperMap.Egisp.SMCity.isZhixia = function(admincode) {
	var data = SuperMap.Egisp.SMCity.provinces_zhixia.concat();
	for(var i=data.length; i--;) {
		if(admincode === data[i].admincode) {
			return true;
		}
	}
	return false;
}