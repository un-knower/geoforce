
var smcitys = {
	layer_boundry: null
};

smcitys.init = function() {
	/*smcitys.layer_boundry = new SuperMap.Layer.Vector("city_boundry");
	map.addLayers([smcitys.layer_boundry]);
	smcitys.layer_boundry.setVisibility(false);*/
	smcitys.initProvinces();
}

/**
 * 显示省页面
 */
smcitys.showProvinces = function() {
	jQuery('.sm-citys .content').addClass('hide');
	jQuery('.sm-citys .content.province').removeClass('hide');
	
	jQuery('.header .return').unbind('click').click(function(){
		jQuery('.sm-citys, .header .return').addClass('hide');
		jQuery('.header .logo, .header .search').removeClass('hide');
	});
}

/**
 * 显示市页面
 */
smcitys.showCitys = function() {
	jQuery('.sm-citys .content').addClass('hide');
	jQuery('.sm-citys .content.city').removeClass('hide');
	
	jQuery('.header .return').unbind('click').click(function(){
		smcitys.showProvinces();
	});
	/*jQuery('.header .return').attr('target-hide', '.sm-citys .content.city');
	jQuery('.header .return').attr('target-show', '.sm-citys .content.province');*/
	
}

/**
 * 显示区县页面
 */
smcitys.showCountys = function(zhixia) {
	jQuery('.sm-citys .content').addClass('hide');
	jQuery('.sm-citys .content.county').removeClass('hide');
	jQuery('.header .return').unbind('click').click(function(){
		if(zhixia == 'true') {
			console.log(zhixia)
			smcitys.showProvinces();
		}
		else {
			smcitys.showCitys();
		}
	});
	/*jQuery('.header .return').attr('target-hide', '.sm-citys .content.county');
	jQuery('.header .return').attr('target-show', '.sm-citys .content.city');*/
}

smcitys.initProvinces = function() {
	var ul = jQuery('.sm-citys .content.province ul').html('');
	ul.append( smcitys.getProvinceHtml(smcitys.provinces_zhixia, true) )
	ul.append( smcitys.getProvinceHtml(smcitys.provinces) );
	ul.append( smcitys.getProvinceHtml(smcitys.province_other) );
	
	jQuery('.toProvince').unbind('click').click(function(){
		var me = jQuery(this).parents('li');
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');
		var zhixia = me.attr('zhixia');
		smcitys.GetChildrenByCode( code, (zhixia ? 3 : 2) , zhixia);
	});
	jQuery('.locate-province').unbind('click').click(function(){
		var me = jQuery(this).parents('li');
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');
		jQuery('.smcity').attr({admincode: code, level: level, adminname: name}).html(name);
		jQuery('.sm-citys, .header .return').addClass('hide');
		jQuery('.header .return').addClass('hide');
		jQuery('.header .logo, .header .search').removeClass('hide');
		smcitys.GetBoundryByCode(code, level);
		Point.search();
	});
	jQuery('.set-default-city').unbind('click').click(User.setDefaultCity);	
	
	jQuery('.header .return').unbind('click').click(function(){
		jQuery('.sm-citys, .header .return').addClass('hide');
		jQuery('.header .logo, .header .search').removeClass('hide');
	});
}

smcitys.getProvinceHtml = function(pros, isZhixia) {
	var h = '';
	var len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];
		h += '<li class="mui-table-view-cell " admincode="'+ p.admincode +'"';
		h += ' level="1" value="'+ p.province +'"';
		h += isZhixia ? ' zhixia="true"' : '';
		h += '>';
		h += '  <div class="mui-slider-handle">';
		h += '		<div class="text locate-province">'+ p.province +'</div>';
		h += '		<div class="ft toProvince"></div>';
		h += '  </div>';
		h += '	<div class="mui-slider-right mui-disabled">';
		h += '  	<a class="mui-btn mui-btn-success set-default-city">设为默认</a>';
		h += ' 	</div>';
		h += '</li>';
	}
	return h;
}

/**
 * 根据admincode查下级
 */
smcitys.GetChildrenByCode = function(code, level, zhixia) {
	if( !code || !level ) {
		return;
	}
	SuperMap.Egisp.request({
		url: urls.server + "/orderService/getAdminElements?",
        data: {
        	admincode: code,
        	level: level
        },
        dataType: 'json',
        success: function(e){
        	if(e.isSuccess && e.result) {        		
        	 	if(level == 2) {
        	 		smcitys.showCitys();
        	 		smcitys.initCitys(e.result);
        	 	}
        	 	else if(level == 3) { 		
        	 		smcitys.showCountys(zhixia);
        	 		smcitys.initCountys(e.result);
        	 	}
        	}
        },
        error: function(){}
	});
}

/**
 * 初始化市页面数据
 */
smcitys.initCitys = function(data) {
	var ul = jQuery('.sm-citys .content.city ul').html('');
	ul.append( smcitys.getCityHtml(data) );
	
	jQuery('.toCity').unbind('click').click(function(){
		var me = jQuery(this).parents('li');
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');
		smcitys.GetChildrenByCode( code, 3 );
	});
	
	jQuery('.locate-city').unbind('click').click(function(){
		var me = jQuery(this).parents('li');
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');
		jQuery('.smcity').attr({admincode: code, level: level, adminname: name}).html(name);
		jQuery('.sm-citys, .header .return').addClass('hide');
		jQuery('.header .logo, .header .search').removeClass('hide');
		smcitys.GetBoundryByCode(code, level);
		Point.search();
	});
	jQuery('.set-default-city').unbind('click').click(User.setDefaultCity);
}

smcitys.getCityHtml = function(pros) {
	var h = '';
	var len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];		
		h += '<li class="mui-table-view-cell " admincode="'+ p.ADMINCODE +'"';
		h += ' level="2" value="'+ p.CITY +'"';
		h += '>';
		h += '  <div class="mui-slider-handle">';
		h += '		<div class="text locate-city">'+ p.CITY +'</div>';
		h += '		<div class="ft toCity"></div>';
		h += '  </div>';
		h += '	<div class="mui-slider-right mui-disabled">';
		h += '  	<a class="mui-btn mui-btn-success set-default-city">设为默认</a>';
		h += ' 	</div>';
		h += '</li>';
	}
	return h;
}

/**
 * 初始化区县页面数据
 */
smcitys.initCountys = function(data) {
	var ul = jQuery('.sm-citys .content.county ul').html('');
	ul.append( smcitys.getCountyHtml(data) );
	
	jQuery('li.toCounty').unbind('click').click(function(){
		var me = jQuery(this);
		var code = me.attr("admincode");
		var level = me.attr("level");
		var name = me.attr('value');
		jQuery('.sm-citys, .header .return').addClass('hide');
		jQuery('.header .logo, .header .search').removeClass('hide');
		jQuery('.smcity').attr({admincode: code, level: level, adminname: name}).html(name);
		smcitys.GetBoundryByCode(code, level);
		Point.search();
	});
	jQuery('.set-default-city').unbind('click').click(User.setDefaultCity);
}

smcitys.getCountyHtml = function(pros) {
	var h = '';
	var len = pros.length;
	for( var i=0; i<len; i++ ) {
		var p = pros[i];		
		h += '<li class="mui-table-view-cell toCounty" admincode="'+ p.ADMINCODE +'"';
		h += ' level="3" value="'+ p.COUNTY +'"';
		h += '>';
		h += '  <div class="mui-slider-handle">';
		h += '		<div class="text">'+ p.COUNTY +'</div>';
		h += '		<div class="ft"></div>';
		h += '  </div>';
		h += '	<div class="mui-slider-right mui-disabled">';
		h += '  	<a class="mui-btn mui-btn-success set-default-city">设为默认</a>';
		h += ' 	</div>';
		h += '</li>';
	}
	return h;
}

smcitys.isZhixia = function(admincode) {
	admincode = admincode.substring(0, 2) + '0000';
	var data = smcitys.provinces_zhixia.concat();
	for(var i=data.length; i--;) {
		if(admincode === data[i].admincode) {
			return true;
		}
	}
	return false;
}


/**
 * 根据admincode查边界
 */
smcitys.GetBoundryByCode = function(code, level) {
	if( !code || !level ) {
		return;
	}
	
	SuperMap.Egisp.request({
		url: urls.server + "/orderService/searchForAdminGeo?",
        data: {
        	admincode: code,
        	level: level
        },
        success: function(e){
        	if(e.isSuccess && e.result) {
        		var points = e.result.geo.points, pts=[];
        		var geometry = smcitys.DrawRegion(e.result.geo.parts, points);
        		map.zoomToExtent(geometry.getBounds());
        	}
        },
        error: function(){}
	});
}


/** 
 * 解析区域面数据，生成geometry
 */
smcitys.DrawRegion = function(parts, point2Ds) {
    var length_parts = ( parts.length == 0 ? 1 : parts.length) ;
    var length_point2Ds = point2Ds.length;

    //取点的索引
    var idxPoint = 0;
    var count = 0;

    var regions = [];

    for(var k=0; k < length_parts; k++) {
        //每个小多边形的点数
        var pntCount = parts[k];
        
        //得到区片
        var points = [];
        var index=-1;
        for (var j=idxPoint; j < idxPoint + pntCount; j++) {
            var item = point2Ds[j];
            var pp = new SuperMap.Geometry.Point( Number(item.x), Number(item.y) );
            for (var l=0; l < points.length; l++) {
                if (j == idxPoint + pntCount - 1 && l != 0 && points[l].x == pp.x && points[l].y == pp.y) {
                    if (points[l].x != points[0].x && points[l].y != points[0].y) {
                        index=l;
                    }
                }
            }
            points.push(pp);
        }
        
        if (index != -1) {
            regions.push( new SuperMap.Geometry.LinearRing( points.splice(index) ) );
        }
        idxPoint = idxPoint + pntCount;
        
        regions.push( new SuperMap.Geometry.LinearRing( points ) ); 
    }
    var georegion = new SuperMap.Geometry.Polygon(regions);    
    return georegion;
}












