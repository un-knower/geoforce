mui.init();

jQuery(function(){	
	var p = urls.getUrlArgs();
	if(p && p.key) {
		User.key = p.key;
		if(p.key == '8a04a77b54effacc0154ff4ea9cb0121') {
		// if(p.key == '8a04a77b4ee30e56014f00e022800216') {
			jQuery('.header .logo').addClass('boc');
		}
		else {
			jQuery('.header .logo').addClass('dth');
//			jQuery('.dth-service').addClass('hide');
		}
	}
	jQuery('.cancel-share').click(function(){
		jQuery('.modal-share').addClass('hide');
	});
	
	jQuery('.nav-btn.share').click(function(){
		jQuery('.modal-share').removeClass('hide');
	});
	jQuery('.nav-btn.search').click(function(){
		jQuery('.modal-search').removeClass('hide');
	});
	
	jQuery('.smcity').click(function(){
		jQuery('.sm-citys, .header .return').removeClass('hide');
		jQuery('.header .logo, .nav-btn.search').addClass('hide');
	});
		
	jQuery('.modal-search .return').click(function(){
		jQuery('.modal-search').addClass('hide');
		
		
		if(Point.layer.features.length == 0) {
			Point.search();
		}
	});
	
	jQuery('.made-qcode').click(function(){
		jQuery('.modal-qcode').removeClass('hide').find('.img').empty().qrcode(
			{width: 150,height: 150,text: location.href}
		);
	});
	jQuery('.modal-detail a.return').click(function(){
		jQuery('.modal-detail').addClass('hide');
	});
	jQuery('.modal-qcode').click(function(){
		jQuery(this).addClass('hide');
	});
	jQuery('.my-location').click(Map.locateToMe);
	Map.initMap();
});

var Map = {
	layer: null,
	layer_mylocation: null
};
var map = null;

/**
 * 地图初始化
 */
Map.initMap = function(){
	var controls_zoombars = [];
	map = new SuperMap.Map("map", 
		{ 
			controls:[
				new SuperMap.Control.Zoom(),
				new SuperMap.Control.Navigation({
				    dragPanOptions: {
				        enableKinetic: true
				    }
				})
			],
			allOverlays: true
		}
	);
	Map.layer = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	Map.layer_mylocation = new SuperMap.Layer.Vector('mylocation');

	map.addLayers([Map.layer, Map.layer_mylocation]);
	map.setCenter( new SuperMap.LonLat(11584493.820190,3589061.070760), 9);
	Point.init();
	User.login();
	smcitys.init();
}

/**
 * 复制链接
 */
Map.copyLinks = function() {
	var text = jQuery('input.for-copy').val('网点管理');
	var obj = document.getElementById('txt_for_copy').createTextRange();
	obj.select();
	obj.execCommand("Copy");
	Map.showSuccess('复制成功');
}

Map.showSuccess = function(str) {
	jQuery('.modal-success').removeClass('hide').find('span').html(str);
	setTimeout(function(){
		jQuery('.modal-success').addClass('hide');
	}, 1500)
}

Map.showError = function(str) {
	jQuery('.modal-error').removeClass('hide').find('span').html(str);
	setTimeout(function(){
		jQuery('.modal-error').addClass('hide');
	}, 1500);
}

Map.locateToMe = function() {
	if(navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(
			function(position){
				var lonlat = GPS.gcj_decrypt(position.coords.longitude, position.coords.latitude);
				var center = SuperMap.Egisp.latLonToMeters(new SuperMap.LonLat(lonlat.lat, lonlat.lon));
				map.setCenter(center, 15);
				var geo_point = new SuperMap.Geometry.Point(center.lon, center.lat);	
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = Point.Style.locationStyle();
				Map.layer_mylocation.removeAllFeatures();
				Map.layer_mylocation.addFeatures(poi);
			},
			function(e){
				Map.showError('定位失败');
			},
			{
		        enableHighAccuracy: false,
		        timeout: 5000,
		        maximumAge: 3000
	    	}
		);
	}
	else {
		Map.showError('定位失败');
	}
}




























