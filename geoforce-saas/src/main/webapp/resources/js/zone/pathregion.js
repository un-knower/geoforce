$(function(){
	$('.path-region-content .undo').on("click", Regionroute.undo);
	$('.path-region-content .done').on("click", Regionroute.done);
	$('.path-region-content .cancel').on("click", Regionroute.cancel);
	$('.path-region-content .drag').on("click", function(){
		var me = $(this);
		if(!me.hasClass('active')) {
			Regionroute.enableDrag();
			me.html('完成拖动');
		}
		else {
			Regionroute.disableDrag();
			me.html('拖动节点');
		}
		me.toggleClass('active');
	});
});

var Regionroute = {
	/*
	 * 是否正在画区
	 */
	drawing: false,
	/*
	 * 拖动点
	 */
	drag: null,
	dragging: false
};

/**
 * 点击工具栏中的沿路画区
 */
Regionroute.addRouteRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	control_select.deactivate();
	layer_edit.removeAllFeatures();
	control_drawPathRegion.activate();
	//可以使用方向键
	Map.setMapIndexTop();
		
	$('#btn_showRegion').prop('checked', true).attr('isShowRegion', 'true');
	layer_region.setVisibility(true);
	layer_region_label.setVisibility(true);
	
	if(layer_region.features.length == 0) {
		Map.search();
	}
	
	Regionroute.drawing = true;
	$('.path-region-content').removeClass('hide');
	
	Regionroute.drag = new SuperMap.Control.DragFeature(layer_edit);
	
	Regionroute.drag.onComplete = function(feature, pixel) {
        var lonlat = map.getLonLatFromPixel(pixel);		
        feature.attributes.lonlat = lonlat;
        
        var index = Number(feature.attributes.index) - 1;
        
		var numPFs = layer_edit.features.length - 1;	
		var numLFs = layer_edit_regionroute.features.length;
		
		var temp;
		if( numPFs > index && index > 0 ) {
			if(numLFs > index) {
				layer_edit_regionroute.removeFeatures([
					layer_edit_regionroute.features[index],
					layer_edit_regionroute.features[index-1]
				]);				
			}
			Regionroute.getPathWithPassPoints(index-1, index, index);
		}
		else if(index < 1 ) {
			temp = layer_edit_regionroute.features[0];
			if(temp)
			{
				layer_edit_regionroute.removeFeatures([layer_edit_regionroute.features[0]]);
			}
			Regionroute.getPathWithPassPoints(0, 1);
		}
		else if(index === numPFs) {
			temp = layer_edit_regionroute.features[index -1];
			if(temp)
			{
				layer_edit_regionroute.removeFeatures([layer_edit_regionroute.features[index-1]]);
			}
			Regionroute.getPathWithPassPoints(index-1, index);
		}
	}
	map.addControl( Regionroute.drag );
}

/*
 * 沿路画区打点
 */
Regionroute.drawRouteRegionCompleted = function(e) {
	var f = e.feature.clone();
	var index = layer_edit.features.length;
	
	f.style = Dituhui.Zone.getRouteRegionPointStyle(index+'');
	f.attributes.index = index;
	layer_edit.removeFeatures([e.feature]);
	layer_edit.addFeatures([f]);
	
	var num = layer_edit.features.length;
	if( num > 1) {
		Regionroute.getPath(num-2, num-1);
	}
}

/*
 * 沿路画区 获取两点之间的线
 */
Regionroute.getPath = function(startIndex, endIndex, isEnd) {
	Regionroute.duringGetPath();
	var pre = layer_edit.features[startIndex].geometry.components[0];
	var start;
	if(!Baidu.using) {
        start = Dituhui.metersToLatLon(new SuperMap.LonLat(pre.x, pre.y));
    }
	else {
		//百度地图中将坐标转为超图经纬度
		start = Baidu.restoreCoord(pre.x, pre.y, "lonlat");
	}
    
	var next = layer_edit.features[endIndex].geometry.components[0];
	var start;
	if(!Baidu.using) {
        end = Dituhui.metersToLatLon(new SuperMap.LonLat(next.x, next.y)); 
    }
	else {
		//百度地图中将坐标转为超图经纬度
		end = Baidu.restoreCoord(next.x, next.y, "lonlat");
	}
	
	var param = JSON.stringify({
		startPoint: {
			x: Number(start.lon.toFixed(5)),
			y: Number(start.lat.toFixed(5))
		},
		endPoint: {
			x: Number(end.lon.toFixed(5)),
			y: Number(end.lat.toFixed(5))
		},
		passPoints: [],
		routeType: 'MINLENGTH'
	});
	var parameter = {
		parameter: param
	}
	Dituhui.Zone.getPathFromCloud(parameter, 
		function(e){
			Regionroute.afterGetPath();
			if(e.success === false || !e.result || !e.result.path || e.result.path.length === 0) {
				Dituhui.showHint("路径分析失败，请重新画点");
				Regionroute.undo();
				return;
			}
			var geoLine = Dituhui.Zone.drawLineLonLat(e.result.path, Baidu.using);	
			var f = new SuperMap.Feature.Vector(geoLine, null, Dituhui.Zone.getPathRegionLineStyle());
			layer_edit_regionroute.addFeatures( [f] ); 
			
			if(isEnd === true) {
				control_drawPathRegion.deactivate();
				Regionroute.createRegion();
			}
		}, 
		function(info){
			Regionroute.afterGetPath();
			Dituhui.showHint(info);
		}
	);
}

/*
 * 正在获取线路
 */
Regionroute.duringGetPath = function() {
	control_drawPathRegion.deactivate();
	Dituhui.showMask();
}

/*
 * 获取线路完成
 */
Regionroute.afterGetPath = function() {	
	Dituhui.hideMask();
	if(Regionroute.drawing && !Regionroute.dragging) {
		control_drawPathRegion.activate();
		//可以使用方向键
		Map.setMapIndexTop();
	}
}

/*
 * 撤销
 */
Regionroute.undo = function() {
	var index = layer_edit.features.length - 1;
	if(index < 0) {
		return;
	}
	
	var pf = layer_edit.features[index];
	layer_edit.removeFeatures( [pf] );
	
	if((index - 1) < 0 ) {
		return;
	}
	
	if(layer_edit_regionroute.features.length === index) {
		var lf = layer_edit_regionroute.features[index - 1];
		layer_edit_regionroute.removeFeatures( [lf] );	
	}
}

/**
 * 沿路画区--结束绘制
 */
Regionroute.done = function() {	
	if(Regionroute.drawing) {
		var num = layer_edit.features.length;
		if(num < 3) {
			Dituhui.showHint("沿路画区至少需要3个点");			
			return;
		}
		
		Regionroute.getPath(num-1, 0, true);
		Regionroute.drag.deactivate();
		Regionroute.drag.destroy();
		map.removeControl(Regionroute.drag);
		
		control_select.activate();
	}
}

/**
 * 沿路画区--取消
 */
Regionroute.cancel = function() {
	control_drawPathRegion.deactivate();
	
	layer_edit.removeAllFeatures();
	layer_edit_regionroute.removeAllFeatures();
	
	Regionroute.drawing = false;
	$('.path-region-content').addClass('hide');
	if(Regionroute.dragging) {
		Regionroute.drag.deactivate();
		Regionroute.drag.destroy();
		Regionroute.dragging = false;
		$('.path-region-content .drag').html("拖动节点").removeClass("active");
	}
	
	control_select.activate();
}

/**
 * 沿路画区--拖动节点
 */
Regionroute.enableDrag = function() {
	control_drawPathRegion.deactivate();
	Regionroute.drag.activate();
	Regionroute.dragging = true;
}

/**
 * 沿路画区--取消拖动节点
 */
Regionroute.disableDrag = function() {
	Regionroute.drag.deactivate();
	control_drawPathRegion.activate();
	//可以使用方向键
	Map.setMapIndexTop();
	Regionroute.dragging = false;
}

/**
 * 沿路画区--绘制完成后，整理线路生成闭合区域
 */
Regionroute.createRegion = function() {
	var fs = layer_edit_regionroute.features, pois = [];
	for(var i=0,len=fs.length; i<len; i++) {
		var f = fs[i];
		var coms = f.geometry.components;
		for(var j=0,l=coms.length; j<l; j++) {
			pois.push(new SuperMap.Geometry.Point( coms[j].x, coms[j].y ));
		}
	}
	var geometry = Dituhui.Zone.DrawRegion([pois.length], pois);
	var feature = new SuperMap.Feature.Vector(geometry, null, Dituhui.Zone.getRegionStyle());
	layer_edit_regionroute.removeAllFeatures();
	layer_edit.removeAllFeatures();
	layer_edit.addFeatures([feature]);
	
	var center = feature.geometry.getCentroid();
	var lonlat = new SuperMap.LonLat( center.x, center.y );
	Map.openAddRegionPopup( lonlat );
	$('.path-region-content').addClass('hide');
}


/**
 * 沿路画区--生成线路
 */
Regionroute.getPathWithPassPoints = function(startIndex, endIndex, passIndex) {
	Regionroute.duringGetPath();
	var pre = layer_edit.features[startIndex].geometry.getCentroid();
	var start = Dituhui.metersToLatLon(new SuperMap.LonLat(pre.x, pre.y));
	
	var next = layer_edit.features[endIndex].geometry.getCentroid();
	
	var end = Dituhui.metersToLatLon(new SuperMap.LonLat(next.x, next.y));
	
	var o = {
		startPoint: {
			x: Number(start.lon.toFixed(5)),
			y: Number(start.lat.toFixed(5))
		},
		endPoint: {
			x: Number(end.lon.toFixed(5)),
			y: Number(end.lat.toFixed(5))
		},
		passPoints: [], 
		routeType: 'MINLENGTH'
	};
	
	
	var param = JSON.stringify( o );

	Dituhui.Zone.getPathFromCloud(
		{
			parameter: param
		},
		function(e) {
			Regionroute.afterGetPath();
			if(e.success === false || !e.result || !e.result.path || e.result.path.length === 0) {
				Dituhui.showHint("路径分析失败，请重新拖动");
		//		drawPathRegion_undo();
			}
			else 
			{
				var path = e.result.path;
				Regionroute.showDarggedPath(path, startIndex, passIndex);		
			}
		},
		function(){
			Regionroute.afterGetPath();
			Dituhui.showHint("路径分析失败，请重新拖动");
		}
	);
}

/**
 * 沿路画区--生成线路-显示线路
 */
Regionroute.showDarggedPath = function(path, index, passIndex) {
	var geoLine = Dituhui.Zone.drawLineLonLat( path );
	
	var f = new SuperMap.Feature.Vector(geoLine, null, Dituhui.Zone.getPathRegionLineStyle());
	
	var num = layer_edit_regionroute.features.length;
	
	var numPFs = layer_edit.features.length;
//	index--;

	index = index < 0 ? 0 : index;
	index = index > num ? num : index;
	
	var features = layer_edit_regionroute.features;
	features.splice(index, 0, f);
	layer_edit_regionroute.removeAllFeatures();
	layer_edit_regionroute.addFeatures(features);	
	
	if( !isNaN(passIndex) )
	{
		Regionroute.getPathWithPassPoints(passIndex, passIndex+1);
	}
	else if( !Regionroute.drawing && (index === 0 || index === num - 1 ) )
	{
		var temp = layer_add_region.features[numPFs - 1];
		if(temp)
		{
			layer_add_region.removeFeature( temp );
		}
			
		Regionroute.getPathWithPassPoints(numPFs - 1, 0);
	}
}














