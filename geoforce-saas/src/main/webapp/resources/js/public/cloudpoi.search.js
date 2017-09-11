/**
 * 右上角的POI搜索插件
 * 高德获取
 */
var POISearch = function(option){
	option = option || {};
	
	/*
	 * 搜索的城市
	 */
	this.city =  option.city; 
	/**
	 * 搜索的输入框ID
	 */
	this.input_id = option.input_id || "txt_keyword_cloud";
	
	this.map = option.map || null;
	
	this.layer = option.layer || null;
	
	this.savePOI = option.savePOI || false;
	this.placeSearch = null;
	this.autoCompelete = null;
	
	var me = this;
	this.clear = function() {
		me.layer.removeAllFeatures();
		me.map.removeAllPopup();
		$("#pager_cloudpois").attr("page", 0);
	}
	
	this.clearResults = function() {
		me.clear();
		$("#" + me.input_id).blur();
		$("#pager_cloudpois, .page-cloud-pois").hide();
		$('.header .search-from-cloud .child, .hide-cloudpois').addClass("hide");
	}
	
	/**
	 * 从下拉列表中选择
	 */
	this.selectFromTips = function(evt){
		var poi = evt.poi;
		if(!poi.location || !poi.location.lng) {			
			return;
		}
		me.clear();
		
		var position = Dituhui.latLonToMeters(new SuperMap.LonLat(poi.location.lng, poi.location.lat));		
    	poi.lonlat = position;
    	me.openPopup(poi);
    	
    	var geo_point = new SuperMap.Geometry.Point(position.lon, position.lat);			
		var feature = new SuperMap.Feature.Vector(geo_point);
		feature.style = {
			externalGraphic: urls.server + "/resources/assets//map/poi/red.png",
			graphicWidth: 22,
			graphicHeight: 32,
			graphicTitle: poi.name,
        	cursor: "pointer"
		};
		poi.type = "point-cloud";
		feature.attributes = poi;
		me.layer.addFeatures(feature);
	}
	
	/**
	 * 开启信息窗
	 */
	this.openPopup = function(attr) {
		var h =  '<div class="map-popup" style="min-height: 60px;">';
			h += '	<div class="map-popup-content">';
			h += Dituhui.Point.getCloudPopupHtml(attr, me.savePOI);
			h += '	</div>';	
			h += '</div>';
		
		Map.popup = new SuperMap.Popup.FramedCloud("popup-cloud",
			attr.lonlat,
			new SuperMap.Size(300, 70),
			h,
			null,
			false,
			null,
			true
		);
		
		Map.popup.autoSize = true;
		Map.popup.panMapIfOutOfView = true;
		Map.popup.relativePosition = "tr";
		map.removeAllPopup();
		map.addPopup(Map.popup);
		
		map.panTo(attr.lonlat);
		
		$('.popup-close').unbind("click").click(function(){
			map.removeAllPopup();
		});	
		
		if(me.savePOI) {
			$('a.a-save-as-branch').unbind('click').click(function(){
				Map.openAddBranchPopup( attr.lonlat,3,attr);
				if(attr.address == '' || $('.smcity').attr('level') == '0') {
					Map.regeocode(attr.lonlat, true);
				}
			});
		}
		
		if(!$(".search-from-cloud .child").hasClass("hide")) {
			$(".search-from-cloud .child").addClass("hide");
			if( $('a[option="to-cloud-poi"]').length > 1 
				|| $("#pager_cloudpois").attr("page") != "0"
			) {
				$(".search-from-cloud .hide-cloudpois").removeClass("hide");				
			}
		}
	}
	
	/**
	 * 文本框enter时查询
	 */
	this.search = function(address) {
		$.ajax({
        type: "GET",
	        url: "http://restapi.amap.com/v3/geocode/geo",
	        data: {
	        	address: address,
	        	city: this.city,
	        	output: "json",
	        	count: 5,
	        	roadlevel: 1,
	        	key: amapKey
	        },
	        success: function(re) {
	        	if(!re.geocodes || re.geocodes.length == 0) {
	        		me.placeSearch(address);
	        		return;
	        	}
	        	var data = re.geocodes;
				var len = data.length;
				var pois = [];
				var firstLocation ;
				for( var i=0; i<len; i++ ) {
					var item = data[i];
					data[i].name = item.formatted_address;
					data[i].address = item.formatted_address;
					data[i].id = "cloudpoi"+i;
					var location = item.location.split(",");
					var position = new SuperMap.LonLat(Number(location[0]), Number(location[1]));				
					position = Dituhui.latLonToMeters(position);
					
					if(i == 0) {
						firstLocation = position;
					}
					
					var geo_point = new SuperMap.Geometry.Point(position.lon, position.lat);			
					var poi = new SuperMap.Feature.Vector(geo_point);
					poi.style = {
						externalGraphic: urls.server + "/resources/assets/num/" + (i+1) + ".gif",
						graphicWidth: 37,
						graphicHeight: 27,
						graphicTitle: item.name,
			        	cursor: "pointer"
					};
					item.type = "point-cloud";
					item.lonlat = position;
					poi.attributes = item;
					pois.push(poi);
				}
				me.layer.removeAllFeatures();
				me.layer.addFeatures(pois);
				me.displayResult(data);  	
				
				$(".amap-sug-result").hide();
				if(pois.length == 1) {
					map.panTo(firstLocation);
				}
				else {
					map.zoomToExtent(me.layer.getDataExtent());
				}
				
	        },
	        error: function(re) {
	        	
	        }
	   });
	}
	
	/*
	 * 不重复添加数据
	 */
	this.addDataToArray = function(data, array) {
		for(var i=array.length; i--;){
			if(array[i].lng == data.lng && array[i].lat == data.lat){
				return;
			}
		}
		array.push(data);
	}
	
	/*
	 * 搜索服务
	 */
	this.placeSearchFunc = function(keyword) {
		var pager = $("#pager_cloudpois");
	    var pageIndex = Number(pager.attr('page'));
	    
	    me.placeSearch.setPageIndex(pageIndex+1);
//	    me.city = me.city.substr(0, 4) + "00";
	    me.placeSearch.setCity(me.city);
	    
	    me.placeSearch.search(keyword, function(status, result){
	    	var re = result.poiList;
	    	
			if( !re || !re.pois || re.pois.length == 0) {
				layer_cloudpois.removeAllFeatures(); 
	            $("#pager_cloudpois, .page-cloud-pois").hide();
	    		$('.header .search-from-cloud .cloud-pois .content')
	    		.html("未查询到结果，请尝试重新输入关键字");
	    		$('.header .search-from-cloud .child').removeClass("hide");
	    		return;
			}
	    	var data = re.pois;	
			var len = data.length;
			var firstLocation ;
			var pois = [], comLocations = [];
			for( var i=0; i<len; i++ ) {
				var item = data[i];
				
				me.addDataToArray(item.location, comLocations);
				
				data[i].id = "cloudpoi" + i;
				var location = item.location;
				var position = new SuperMap.LonLat(location.lng, location.lat);				
				position = Dituhui.latLonToMeters(position);
				
				var geo_point = new SuperMap.Geometry.Point(position.lon, position.lat);			
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = {
					externalGraphic: urls.server + "/resources/assets/num/" + (i+1) + ".gif",
					graphicWidth: 37,
					graphicHeight: 27,
					graphicTitle: item.name,
		        	cursor: "pointer"
				};
				item.type = "point-cloud";
				item.lonlat = position;
				if(i == 0) {
					firstLocation = position;
				}
				poi.attributes = item;
				pois.push(poi);
			}
			me.layer.removeAllFeatures();
			me.map.removeAllPopup();
			me.layer.addFeatures(pois);
			me.displayResult(data);  
			
			var total = Number(re.count);
			Dituhui.setPager({
				id: "pager_cloudpois",
				total: total,
				limit: 10,
				index: pageIndex
			});
            $("#pager_cloudpois").show();   
            $(".page-cloud-pois").html('第'+(pageIndex+1)+'/'+ Math.ceil(total/10)+'页' ).show();
            $(".search-from-cloud .child .content").scrollTop(0);
			
			$(".amap-sug-result").hide();
			if(pois.length == 1 || comLocations.length == 1) {
				map.panTo(firstLocation);
			}
			else {
				map.zoomToExtent(me.layer.getDataExtent());
			}
	    });
	}
	
	this.displayResult = function(data) {
		var div = $('.header .search-from-cloud .cloud-pois .content');
	    var h = Dituhui.Point.getCloudSearchHtml(data);
	    div.html(h);
	
	    $('.header .search-from-cloud .child').removeClass("hide");
	    $('a[option="to-cloud-poi"]').unbind('click').click(function(){
	        var myself = $(this);
	        var poi = me.getCloudPoiById( myself.attr("data-id") );
	        if(!poi) {
	        	return;
	        }
	        var attr = poi.attributes;
	        me.openPopup(attr);
	    });
	}
	
	this.getCloudPoiById = function(id) {
		var pois = me.layer.features.concat();
		if(pois.length < 1) {
			return false;
		}
		var len = pois.length;
		for(var i=len; i--; ) {
			var poi = pois[i];
			if(poi.attributes.id === id) {
				return poi;
			}
		}
		return false;
	}
	
	this.changeCity = function() {
		if(me.autocomplete) {
			me.autocomplete.setCity(me.city);
		}
		this.clearResults();
	}
	
	this.init = function(){
		AMap.plugin('AMap.Autocomplete',function(){
		    var autoOptions = {
		        city: me.city,
		        input: me.input_id,
		        citylimit: false
		    };
		    me.autocomplete = new AMap.Autocomplete(autoOptions);
	     	AMap.event.addListener(me.autocomplete, "select", me.selectFromTips);
		});
		
		AMap.service('AMap.PlaceSearch',function(){
		    me.placeSearch = new AMap.PlaceSearch();
		});
		
		var oldtext = '';
		$("#"+me.input_id).keyup(function(e){
			var text = $.trim($(this).val());
			if( text.length > 0) {
				if(oldtext !== text) {
					me.clear();
					$("#pager_cloudpois, .page-cloud-pois").hide();
			    	$('.header .search-from-cloud .child, .hide-cloudpois').addClass("hide");					
				}
				
				if(e.keyCode === 13){
					$(this).blur();
					me.placeSearchFunc(text);
				}
				$('.input-clear').removeClass("hide");	
			}
			else {
				$('.input-clear').addClass("hide");	
				me.clear();
				$("#pager_cloudpois, .page-cloud-pois").hide();
		    	$('.header .search-from-cloud .child, .hide-cloudpois').addClass("hide");
			}
		});
		
		$("#pager_cloudpois").on("click", "a[data-page-index]", function(){
			$("#pager_cloudpois").attr("page", $(this).attr("data-page-index"));
			me.placeSearchFunc($("#"+me.input_id).val());
		});
		
		$(".search-from-cloud .input-clear").on("click", function(){
			me.clear();
			$("#pager_cloudpois, .page-cloud-pois").hide();
		    $('.header .search-from-cloud .child, .hide-cloudpois').addClass("hide");
		    $("#"+me.input_id).val("");
		    $(this).addClass("hide");
		});
		
		$(".hide-cloudpois").on("click", function(){
			$(this).addClass("hide");
			$('.header .search-from-cloud .child').removeClass("hide");
		});
	}
	
	this.init();
}




/**
 * 从云平台搜索

Map.searchFromCloud = function() {
	layer_cloudpois.removeAllFeatures();
	map.removeAllPopup();	

    $('.header .search-from-cloud .child').addClass("hide");
	var keyword = $("#txt_keyword_cloud").val();
	if( keyword.length < 1 ) {
		Dituhui.showPopover("请输入搜索关键字");
		return;
	}
	var bounds = map.getExtent();
	var param = {
		keyword: keyword,
		max_x: bounds.right,
		min_x: bounds.left,
		max_y: bounds.top,
		min_y: bounds.bottom
	};
	Dituhui.Point.searchFromCloud(param, 
		function(e){
			var data = e.results ? e.results : e.pois;
			
			var len = data.length;
			var pois = [];
			for( var i=0; i<len; i++ ) {
				var item = data[i];
				var position = new SuperMap.LonLat(item.smx, item.smy);				
				
				var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = {
					externalGraphic: urls.server + "/resources/assets/num/" + (i+1) + ".gif",
					graphicWidth: 37,
					graphicHeight: 27,
					graphicTitle: item.name,
		        	cursor: "pointer"
				};
				item.type = "point-cloud";
				poi.attributes = item;
				pois.push(poi);
			}
			layer_cloudpois.addFeatures(pois);
			Map.showCloudSearchResult(e);
		}, 
		function(){
			layer_cloudpois.removeAllFeatures(); 
            $("#pager_cloudpois, .page-cloud-pois").hide();
    		$('.header .search-from-cloud .cloud-pois .content').html("未查询到结果，请尝试重新输入关键字");
    		$('.header .search-from-cloud .child').removeClass("hide");
		}
	);
} */

/**
 * 显示云平台搜索结果到div
 
Map.showCloudSearchResult = function(e){
    var div = $('.header .search-from-cloud .cloud-pois .content');
    var h = Dituhui.Point.getCloudSearchHtml(e.results ? e.results : e.pois);
    div.html(h);

    $('.header .search-from-cloud .child').removeClass("hide");
    $('a[option="to-cloud-poi"]').unbind('click').click(function(){
        var me = $(this);
        var poi = Map.getCloudPoiById( me.attr("data-id") );
        if(!poi) {
        	return;
        }
        var attr = poi.attributes;
        Map.openCloudPopup(attr);
    });
}
Map.getCloudPoiById = function(id) {
	var pois = layer_cloudpois.features.concat();
	if(pois.length < 1) {
		return false;
	}
	var len = pois.length;
	for(var i=len; i--; ) {
		var poi = pois[i];
		if(poi.attributes.poi_id === id) {
			return poi;
		}
	}
	return false;
}*/
/**
 * 显示云平台搜索结果popup
 
Map.openCloudPopup = function(attr) {
	var position = new SuperMap.LonLat( attr.smx, attr.smy );
    attr.lonlat = position;

	var h =  '<div class="map-popup" style="min-height: 60px;">';
		h +=  '<div class="map-popup-content">';
		h += Dituhui.Point.getCloudPopupHtml(attr);
		h += '</div>';

		h += '</div>';

	Map.popup = new SuperMap.Popup.FramedCloud("popup-cloud",
		position,
		new SuperMap.Size(300, 70),
		h,
		null,
		false,
		null,
		true
	);
	Map.popup.autoSize = true;
	Map.popup.panMapIfOutOfView = true;
	Map.popup.relativePosition = "tr";
	map.removeAllPopup();
	map.addPopup(Map.popup);
	var zoom = map.getZoom();
	map.setCenter(position, zoom );
	$('.popup-close').unbind("click").click(function(){
		map.removeAllPopup();
	});		
}*/