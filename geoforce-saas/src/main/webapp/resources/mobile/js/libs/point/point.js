var Point = {};
Point.layer = null;
Point.labelLayer = null;
Point.init = function() {
	Point.layer = new SuperMap.Layer.Vector("branches");
	Point.labelLayer = new SuperMap.Layer.Vector("branches-label");
	map.addLayers([Point.layer, Point.labelLayer]);
	
	var control_select = new SuperMap.Control.SelectFeature([Point.layer, Point.labelLayer], {
	    onSelect: Point.selectPoint,
	    multiple: false,
	    hover: false
	});
	
	map.addControl(control_select);	
	control_select.activate();
	
	jQuery('.btn-search').click(function(){
		Point.searchByName();
	});
	jQuery('.clear-input').click(Point.clearSearchResult);
}

Point.selectPoint = function(feature) {
	var type = feature.attributes.type;
	switch(type) {
		case 'point':
			Point.getDetail(feature);
			break;
		case 'label':
			Point.showDetail();
			break;
		case 'point-national':
			var attr = feature.attributes;
			var name = attr.adminname, code = attr.admincode, level = attr.level;
			
			jQuery('.smcity').attr({
				name: name,
				admincode: code,
				level: level
			}).html(name);
			Point.search();
			smcitys.GetBoundryByCode(code, level);
			
			/*if(level == 1) {
        		var iszhixia = smcitys.isZhixia( code.substring(0, 2) + '0000' );
	        	if(iszhixia) {
	        		
	        	}
	        	else {
	        		
	        	}
	        	
	        }
	        else if(level == 2) {
	        	
	        }
	        else {
	        	
	        }*/
	        
			
        	break;
	}
}

Point.search = function() {
	var smcity = jQuery('.smcity');
	var admincode = smcity.attr('admincode');
	var level = smcity.attr('level');
	var name = smcity.attr('name');
	
	var param = {
		admincode: admincode,
		level: level,
		pageNo: -1,
		key: User.key
	}
	
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/queryAllForConvergeApp?",
        data: param,
        success: function(e){
            if(e.isSuccess && e.result) {
                var re = [];
                if( e.result.length === 0 ) {
                    Point.clear();
                }
                else {
                	e.result.level = param.level;
                    Point.display(e);
                }
            }
            else {
                failed(e.info ? e.info : "网点查询失败");
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed();
        }
    });
}

Point.display = function(e) {	
	Point.clear();
	var data = e.result.records;
	
	//显示聚合
	if(e.result.isConverge == true) {
		Point.displayCluster(e);
		return;
	}
	
	var data = e.result.records;
	var len = data ? data.length : 0;
	if( len === 0 ) {
		Map.showError("没有数据");
		return;
	}
	var pois = [];
	for(var i=data.length; i--; ) {	
		var item = data[i];
		if(!item.smx || item.smx < 1) {
			continue;
		}
        var style = Point.Style.getBranchStyle(item.styleid);
        
		var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);	
		var poi = new SuperMap.Feature.Vector(geo_point);

		poi.style = style;
		poi.attributes = item;
		poi.attributes.type = "point";
		pois.push(poi);

		var geoText = new SuperMap.Geometry.Point(item.smx, item.smy);
		var offset_y  = style.graphicHeight ? Number(style.graphicHeight) : 0;
		var ly = offset_y;
		offset_y = offset_y*0.5 + 32;
		ly = offset_y - 15;

        pois.push(poi);
	}
		
	Point.layer.addFeatures(pois);
}

Point.displayCluster = function(e) {
	var data = e.result.records;
	var level = e.result.level;
	if(level == '1') {
		var isZhixia = smcitys.isZhixia( jQuery('.smcity').attr('admincode').substring(0, 2) + '0000' );
		level = isZhixia ? 3 : 2;
	}
	else {
		level = Number(level) + 1;
	}
	
	var pois = [];
	for(var i=data.length; i--; ) {	
		var item = data[i];
        var style = Point.Style.getCluster(item.count);
        style.label = item.count;
        
        var geo_point = new SuperMap.Geometry.Point(item.x, item.y);   
        item.type = "point-national";  
		item.level = level;
		item.adminname = item.province ? item.province : (item.city ? item.city : item.county)
        var poi = new SuperMap.Feature.Vector(geo_point, item, style);
        pois.push(poi);
	}
	Point.layer.addFeatures(pois);
}

Point.clear = function() {
	Point.layer.removeAllFeatures();
	Point.labelLayer.removeAllFeatures();
}


Point.detail = null;
Point.getDetail = function(feature) {	
	var param = {
		id: feature.attributes.id,
		key: User.key
	};
	SuperMap.Egisp.request({
        url: urls.server + "/pointService/queryAllForConvergeApp?",
        data: param,
        success: function(e){
            if(e.result && e.result.id) {
                var re = [];
                if( e.result.length === 0 ) {
                    
                }
                else {
                    Point.showLabel(e, feature.style);
                }
            }
            else {
                
            }
        },
        error: function(){
            
        }
    });
}

Point.showLabel = function(e, style) {
	Point.detail = e.result;
	var item = e.result;
	Point.labelLayer.removeAllFeatures();
	var geoText = new SuperMap.Geometry.Point(item.smx, item.smy);
	var offset_y  = style.graphicHeight ? Number(style.graphicHeight) : 0;
	var ly = offset_y;
	offset_y = offset_y*0.5 + 32;
	ly = offset_y - 15;

	var css = Point.Style.getLabelStyle( item.name, -offset_y, ly);
	
	var geotextFeature = new SuperMap.Feature.Vector(geoText, { type: "label" }, css);
	Point.labelLayer.addFeatures([geotextFeature]);
}

Point.showDetail = function() {
	var detail = Point.detail;
	var div = jQuery('.modal-detail').removeClass('hide');
	div.find('.modal-header span.title').html(detail.name);
	var content = div.find('.content');
	var h =  '<div class="row-img">';
		h += '	<div class="img"><img><span class="count">0张</span></div>'
		h += '	<div class="box">'
		h += '		<label>网点名称</label>'
		h += '		<span>'+ detail.name +'</span>'
		h += '	</div>'
		h += '</div>';
		h += '<div class="row row-address">';
		h += '	<label>网点地址</label>';
		h += '	<span>'+ detail.address +'</span>';
		h += '</div>';
		
		var smx = '', smy = '';
		if( detail.status == 0 ) {
            coord = SuperMap.Egisp.metersToLatLon( new SuperMap.LonLat(detail.smx, detail.smy) );
            smx = coord.lon.toFixed(2);
            smy = coord.lat.toFixed(2);
        }
		h += '<div class="row">';
		h += '	<label>经度</label>';
		h += '	<span>'+ smx +'</span>';
		h += '</div>';		
        
		h += '<div class="row">';
		h += '	<label>纬度</label>';
		h += '	<span>'+ smy +'</span>';
		h += '</div>';
		
		h += '<div class="row">';
		h += '	<label>绑定区划</label>';
		h += '	<span>'+ (detail.areaName ? detail.areaName : '') +'</span>';
		h += '</div>';
		
		h += '<div class="row">';
		h += '	<label>创建时间</label>';
		h += '	<span>'+ (detail.createTime !== null ? detail.createTime : '' ) +'</span>';
		h += '</div>';
		
		h += '<div class="row">';
		h += '	<label>修改时间</label>';
		h += '	<span>'+ (detail.updateTime !== null ? detail.updateTime : '' )  +'</span>';
		h += '</div>';
		
		h += '<div class="row">';
		h += '	<label>所属分组</label>';
		h += '	<span>'+ (detail.groupname ? detail.groupname : '') +'</span>';
		h += '</div>';
	
	content.html(h);
	
	var cols = detail.extcols;
	h = '';
	if(cols && cols.length > 0) {
		/*if(customs[m] === "链接") {
                var href = me[m] || '';
                if( href && href.length>0 && !/^http:\/\//.test(href) && !/^https:\/\//.test(href) ) {            
                    href = "http://" + href;
                }

                h += '  <span class="text"><a href="'+ (href=="" ? "javascript:;" : href) +'" target="_blank">'+ (me[m] ? Dituhui.setStringEsc(me[m]) : '')+'</a></span>';
            }
            else {
                h += '  <span class="text">'+ (me[m] ? Dituhui.setStringEsc(me[m]) : '')+'</span>';
            }*/
		for(var i=0,len=cols.length; i<len; i++) {
			var col = cols[i];
			h += '<div class="row">';			
			h += '	<label>'+ col.colkey +'</label>';
			if(col.colkey === "链接") {
				var href = col.colvalue || '';
				if( href && href.length>0 && !/^http:\/\//.test(href) && !/^https:\/\//.test(href) ) {            
                    href = "http://" + href;
                }
                h += '  <span class="text"><a href="'+ (href=="" ? "javascript:;" : href) +'" target="_blank">'+ (col.colvalue || '') +'</a></span>';
			}
			else {
				h += '	<span>'+ (col.colvalue ? col.colvalue : '') +'</span>';
			}
			
			h += '</div>';
		}
	}
	jQuery('.row-address').after(h);	
	Point.Pictures.search(Point.detail.id);	
}

Point.searchList = null;
Point.searchByName = function(pageNo) {
	var name = jQuery('.txt-search').val();
	if(name == '') {
		return;
	}
	var smcity = jQuery('.smcity');
	var admincode = smcity.attr('admincode');
	var level = smcity.attr('level');
	
	var param = {
//		admincode: admincode,
//		level: level,
		pageNo: pageNo ? pageNo : 1,
		pageSize: 30,
		key: User.key,
		name: name
	}
	
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/queryAllForConvergeApp?",
        data: param,
        success: function(e){
            if(e.isSuccess && e.result) {
                var re = [];
                if(!e.result.records || e.result.records.length === 0 ) {
                    jQuery('.modal-search .content').html('搜索结果： 0');
                }
                else {
                    Point.displaySearchData(e);
                    Point.searchList = e;
                }
            }
            else {
                jQuery('.modal-search .content').html('搜索结果： 0');
                Point.searchList = null;
            }
        },
        error: function(){
            jQuery('.modal-search .content').html('搜索结果： 0');
            Point.searchList = null;
        }
    });
}
Point.clearSearchResult = function() {
	jQuery('.modal-search .content').html('');
	jQuery('.txt-search').val('');
	
	if(!Point.searchList != null) {
		Point.searchList = null;
		Point.clear();
	}
}
Point.displaySearchData = function(e) {
	var data = e.result.records;
	var h = '<ul class="mui-table-view">';
	for(var i=0, len = data.length; i<len; i++) {
		var item = data[i];
		h += '<li class="mui-table-view-cell">';
		h += '	<div class="location" smx="'+ item.smx +'" smy="'+ item.smy +'"></div>';
		h += '	<span class="name" data-id="'+ item.id +'">'+ item.name + '</span>';
		h += '</li>';
	}
	h += '</ul>';
	var page = e.result.page;
	if(e.result.totalPages > 1) {		
		h += '<div class="pager" page="'+ page +'">';
		h += '	<div class="btn prev '+ (page > 1 ? '' : 'disabled' ) +'">上一页</div>';
		h += '	<div class="btn next '+ (page < e.result.totalPages ? '' : 'disabled' ) +'">下一页</div>';
		h += '</div>';
	}
	jQuery('.modal-search .content').html(h);
	
	
	jQuery('li .location').unbind('click').click(function(){
		var me = jQuery(this);
		var center = new SuperMap.LonLat(Number(me.attr('smx')), Number(me.attr('smy')));
		if(center.lon && center.lon > 0) {
			map.setCenter(center, 16);
		}
		
		Point.display(Point.searchList);
		jQuery('.modal-search').addClass('hide');
	});
	
	jQuery('.pager .next:not(.disabled)').unbind('click').click(function(){
	    jQuery("html,body").animate({scrollTop: 0},382)
		var nextpage = Number(jQuery(this).parents('.pager').attr('page')) + 1;
		Point.searchByName( nextpage );
	});
	jQuery('.pager .prev:not(.disabled)').unbind('click').click(function(){
	    jQuery("html,body").animate({scrollTop: 0},382)
		var prevpage = Number(jQuery(this).parents('.pager').attr('page')) - 1;
		Point.searchByName( prevpage );
	});
	
	jQuery('li span.name').unbind('click').click(function(){
		var param = {
			id: jQuery(this).attr('data-id'),
			key: User.key
		};
		
		SuperMap.Egisp.request({
	        url: urls.server + "/pointService/queryAllForConvergeApp?",
	        data: param,
	        success: function(e){
	            if(e.result && e.result.id) {
	                var re = [];
	                if( !e.result) {
	                    
	                }
	                else {
	                    Point.detail = e.result;
	                    Point.showDetail();
	                }
	            }
	        },
	        error: function(){
	            
	        }
	    });
	});
}


































