
Map.initZoomControl = function() {
	$('.zoom-in').on('click', function(){
		map.zoomIn();
	});
	$('.zoom-out').on('click', function(){
		map.zoomOut();
	});
	$('.map-zoom').html(map.getZoom());
	map.events.on({'moveend': function(){
		$('.map-zoom').html(map.getZoom());
	}});
	//重写鹰眼的事件
	var ovmap = map.getControl('overviewMap');	
	if(ovmap) {		
		ovmap.showToggle = function(minimize) {
	        this.maximizeDiv.style.display = minimize ? '' : 'none';
	        this.minimizeDiv.style.display = minimize ? 'none' : '';        
	        //最小化的时候
	        if(minimize) {
	        	$('.zoom-control').removeClass('higher');
	        }
	        else {
	        	$('.zoom-control').addClass('higher');
	        }
	    }
	}
}


/**
 * 地图切换
 */
Map.using = true;
Map.layer = null;
Map.layer_townboundry = null;
Map.showTownBoundry = false;
Map.cloud_resolutions = [156543.033928041, 78271.5169640203, 39135.7584820102, 
            19567.8792410051, 9783.93962050254, 4891.96981025127, 2445.98490512563, 
            1222.99245256282, 611.496226281409, 305.748113140704, 152.874056570352, 
            76.4370282851761, 38.218514142588, 19.109257071294, 9.55462853564701, 
            4.77731426782351, 2.38865713391175, 1.19432856695588, 0.59716428347793];

Map.init = function(showTownBoundry) {
	var url = !Dituhui.User.special ? urls.map_img : urls.map_img_special;
    Map.layer = new SuperMap.Layer.CloudLayer({url: url});
    map.addLayer(Map.layer);
    map.setLayerIndex(Map.layer, 0);
    map.setBaseLayer(Map.layer);
    
	if(showTownBoundry === true) {
		Map.showTownBoundry = true;
		Map.layer_townboundry = new SuperMap.Layer.CloudLayer(
			{
				url: urls.map_townboundry,
				s: 2,
				resolutions: Map.cloud_resolutions.slice(12)
				/*,
				getTileUrl:function (xyz) {
	                var me = this,
	                url = me.url;
	                return SuperMap.String.format(url, {
	                    mapName: me.mapName,
	                    type: me.type,
	                    x: xyz.x,
	                    y: xyz.y,
	                    z: xyz.z+11
	                });
	            }*/
			}
		);
		
		map.addLayer(Map.layer_townboundry);
		map.setLayerIndex(Map.layer_townboundry, 1);
	}
    
    $('a.to-supermap').click(Map.use);
    $('.nav-tools .second-menu a').removeClass('action').addClass('normal');
    $('a.to-supermap').removeClass('normal').addClass('action');
    
    Map.initZoomControl();
}

Map.use = function() {
    if(Map.using) {
        return;
    }
    var flag = Map.checkAction();
	if(!flag) {
		return;
	}
    var center = map.getCenter();
    var zoom = map.getZoom();

    Baidu.unuse();

    Map.layer = new SuperMap.Layer.CloudLayer({url: urls.map_img});
    map.addLayer(Map.layer);    
    map.setLayerIndex(Map.layer, 0);
    map.setBaseLayer(Map.layer);
    
    //显示区县界
    var btn_cb = $('#btn_showCountryBoundry');
    if(btn_cb.length > 0) {
    	$('#btn_showCountryBoundry').parents('li.sec').removeClass('hide');
   		map.addLayer(layer_countyboundry);
    	map.setLayerIndex(layer_countyboundry, 1);
    }
    
    if(Map.showTownBoundry === true) {
		Map.layer_townboundry = new SuperMap.Layer.CloudLayer(
			{
				url: urls.map_townboundry,
				s: 2,
				resolutions: Map.cloud_resolutions.slice(12)
				/*,
				getTileUrl:function (xyz) {
	                var me = this,
	                url = me.url;
	                return SuperMap.String.format(url, {
	                    mapName: me.mapName,
	                    type: me.type,
	                    x: xyz.x,
	                    y: xyz.y,
	                    z: xyz.z+11
	                });
	            }*/
			}
		);
		map.addLayer(Map.layer_townboundry);
		map.setLayerIndex(Map.layer_townboundry, 1);
	}
    Map.using = true;

    var center_b = Baidu.restoreCoord(center.lon, center.lat);
    map.setCenter(new SuperMap.LonLat(center_b.x, center_b.y), zoom+2);
    Map.redrawBoundry();
    Map.redrawDataPoints();
    $('.map-copyright').html('&copy;2014 SuperMap - GS(2014)6070号-data&copy;Navinfo');
    $('.nav-tools .second-menu a').removeClass('action').addClass('normal');
    $('a.to-supermap').removeClass('normal').addClass('action');
}

Map.unuse = function() {
    if(!Map.using) {
        return;
    }
    Map.using = false;
    map.removeLayer(Map.layer, false);
    Map.layer = null;
    
    if(Map.showTownBoundry === true) {
		map.removeLayer(Map.layer_townboundry, false);
    	Map.layer_townboundry = null;
	}
    
    //隐藏区县界
    var btn_cb = $('#btn_showCountryBoundry');
    if(btn_cb.length > 0) {
    	$('#btn_showCountryBoundry').parents('li.sec').addClass('hide');
    	map.removeLayer(layer_countyboundry);
    }
}

/**
 * 重新绘制边界数据
 */
Map.redrawBoundry = function() {
    var fs = layer_boundry.features;
    var fs_b = [];
    for(var i=fs.length; i--; ) {
        var f = fs[i], f_b;
        var attr = f.attributes;
        var geometry = Dituhui.Zone.DrawRegion(attr.geo.parts, attr.geo.points);
        var feature = new SuperMap.Feature.Vector( geometry, attr, f.style );
        fs_b.push(feature);
    }
    layer_boundry.removeAllFeatures();
    layer_boundry.addFeatures(fs_b);    
    // map.zoomToExtent(layer_boundry.getDataExtent());
}

Map.redrawDataPoints = function() {
	if( $('#btn_showBranch').prop('checked') == true ) {
		Map.searchBranches();
	}    
    if( $('#btn_showRegion').prop('checked') == true ) {
        Map.searchRegions();
    }
    if(layer_cloudpois.features.length > 0) {
        Map.searchFromCloud();
    }
    map.removeAllPopup();
}














