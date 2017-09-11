/*
 * 当前地图
 * sm - 超图地图 - 默认
 * tdt - 天地图
 * google - 谷歌地图
 */
Map.current_map = "sm";

/**
 * 切换至天地图
 */
Map.toTiandituMap = function() {
	if( Map.current_map === "tdt" ) {
		return;
	}
	var center = map.getCenter();
	var zoom = map.getZoom();

	layer_tdt_img = new SuperMap.Layer.Tianditu({
		projection: "EPSG:4326"
	});
	layer_tdt_label = new SuperMap.Layer.Tianditu({
		projection: "EPSG:4326",
		isLabel: true
	});

	if( Map.current_map === "sm" ) {
		map.removeLayer( layer_map );
	}
	else if( Map.current_map === "google" ) {
		map.removeLayer( layer_google );
	}

	map.addLayer(layer_tdt_img);
	map.setLayerIndex( layer_tdt_img, 0 );
	map.setBaseLayer(layer_tdt_img);
	map.addLayer(layer_tdt_label);
	map.setLayerIndex( layer_tdt_label, 1 );
	map.removeControl( controls_zoombars[0] );
	map.removeControl( controls_zoombars[1] );
	controls_zoombars = [];
	controls_zoombars = [        
	    new SuperMap.Control.PanZoomBar({showSlider:true}),
	    new SuperMap.Control.Navigation({
	        dragPanOptions: {
	            enableKinetic: true
	        }
	    })  
	];
	map.addControls( controls_zoombars );


	center = SuperMap.Egisp.Coords.sm2tdt( center );
	map.setCenter( center, zoom-1 );
	Map.current_map = "tdt";
}

/**
 * 切换至超图
 */
Map.toSuperMap = function() {
	if( Map.current_map === "sm" ) {
		return;
	}
	var center = map.getCenter();
	var zoom = map.getZoom();

	if( Map.current_map === "tdt" ) {
		map.removeLayer( layer_tdt_img );
		map.removeLayer( layer_tdt_label );
	}
	else if( Map.current_map === "google" ) {
		map.removeLayer( layer_google );
	}

	layer_map = new SuperMap.Layer.CloudLayer({url: urls.map_img});
	map.addLayer(layer_map);
	map.setLayerIndex( layer_map, 0 );
	map.setBaseLayer(layer_map);

	map.removeControl( controls_zoombars[0] );
	map.removeControl( controls_zoombars[1] );
	controls_zoombars = [];
	controls_zoombars = [        
	    new SuperMap.Control.PanZoomBar({showSlider:true}),
	    new SuperMap.Control.Navigation({
	        dragPanOptions: {
	            enableKinetic: true
	        }
	    })  
	];
	map.addControls( controls_zoombars );
	
	center = SuperMap.Egisp.Coords.tdt2sm( center );
	map.setCenter( center, zoom+1 );
	Map.current_map = "sm";
}

/**
 * 切换至谷歌地图
 */
Map.toGoogleMap = function() {
	if( Map.current_map === "google" ) {
		return;
	}
	var center = map.getCenter();
	var zoom = map.getZoom();

	if( Map.current_map === "tdt" ) {
		map.removeLayer( layer_tdt_img );
		map.removeLayer( layer_tdt_label );
	}
	else if( Map.current_map === "sm" ) {
		map.removeLayer( layer_map );
	}

	layer_google_img = new SuperMap.Layer.Google("google_img", 
		{
			version: '3', 
			sphericalMercator: true
		}
	);

	map.addLayer(layer_google_img);
	map.setLayerIndex( layer_google_img, 0 );
	map.setBaseLayer(layer_google_img);

	map.removeControl( controls_zoombars[0] );
	map.removeControl( controls_zoombars[1] );
	controls_zoombars = [];
	controls_zoombars = [        
	    new SuperMap.Control.PanZoomBar({showSlider:true}),
	    new SuperMap.Control.Navigation({
	        dragPanOptions: {
	            enableKinetic: true
	        }
	    })  
	];
	map.addControls( controls_zoombars );

	if( Map.current_map === "tdt" ) {
		center = SuperMap.Egisp.Coords.tdt2sm( center );
	}
	map.setCenter( center, zoom );
	Map.current_map = "google";
}

Map.refreshDataAfterChangeMap = function() {
	if( layer_branches.visibility && layer_branches.features.length > 0 ) {
		Map.searchBranches();
	}
	if(layer_boundry.visibility && layer_boundry.features.length > 0) {
		var sm_city = $('.mcity');
		SuperMap.Egisp.SMCity.GetBoundryByCode(sm_city.attr('admincode'), sm_city.attr('level'));
	}
	if(layer_region.visibility && layer_region.features.length > 0) {
		Map.searchRegions();
	}
}
