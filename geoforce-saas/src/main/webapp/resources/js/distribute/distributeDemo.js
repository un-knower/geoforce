var initArea = function(map) {
	var areaA = new Array();
    areaA.push([104.070649,30.667103]);
    areaA.push([104.072215,30.667103]);
    areaA.push([104.074168,30.669669]);
    areaA.push([104.084425,30.664962]);
    areaA.push([104.079833,30.657561]);
    areaA.push([104.077902,30.658373]);
    areaA.push([104.077279,30.658558]);
    areaA.push([104.070949,30.658484]);
    var polygonA = new AMap.Polygon({
        path: areaA,
        strokeColor: "green",	//线颜色
        strokeOpacity: 0.3,		//线透明度
        strokeWeight: 1,		//线宽
        fillColor: "green",		//填充色
        fillOpacity: 0.3		//填充透明度
    });
    polygonA.setMap(map);
    
    var areaB = new Array();
    areaB.push([104.062302,30.66703]);
    areaB.push([104.062388,30.664962]);
    areaB.push([104.062388,30.664704]);
    areaB.push([104.062688,30.658244]);
    areaB.push([104.058654,30.658133]);
    areaB.push([104.056208,30.660348]);
    areaB.push([104.056723,30.661308]);
    areaB.push([104.056852,30.662747]);
    areaB.push([104.056251,30.662895]);
    areaB.push([104.055993,30.666254]);
    areaB.push([104.056251,30.66858]);
    areaB.push([104.05801,30.667694]);
    areaB.push([104.060499,30.666845]);
    var polygonB = new AMap.Polygon({
        path: areaB,
        strokeColor: "red",		//线颜色
        strokeOpacity: 0.3,		//线透明度
        strokeWeight: 1,		//线宽
        fillColor: "red",		//填充色
        fillOpacity: 0.3		//填充透明度
    });
    polygonB.setMap(map);
    
    var areaC = new Array();
    areaC.push([104.060757,30.656324]);
    areaC.push([104.071014,30.656435]);
    areaC.push([104.071014,30.655807]);
    areaC.push([104.068868,30.652558]);
    areaC.push([104.064018,30.652558]);
    areaC.push([104.062087,30.649531]);
    areaC.push([104.05874,30.651008]);
    areaC.push([104.060757,30.654146]);
    areaC.push([104.0608,30.656287]);
    var polygonC = new AMap.Polygon({
        path: areaC,
        strokeColor: "blue",	//线颜色
        strokeOpacity: 0.3,		//线透明度
        strokeWeight: 1,		//线宽
        fillColor: "blue",		//填充色
        fillOpacity: 0.3		//填充透明度
    });
    polygonC.setMap(map);
    
    AMapUI.loadUI(['overlay/SimpleMarker'], function(SimpleMarker) {
		new SimpleMarker({
			iconLabel: {
				innerHTML: '<strong>AreaA</strong>',
				style: {
					color: 'red',
					marginTop: '15px'
				}
			},
			iconStyle: "<>",
			map: map,
			position: polygonA.getBounds().getCenter()
		});
		
		new SimpleMarker({
			iconLabel: {
				innerHTML: '<strong>AreaB</strong>',
				style: {
					color: 'red',
					marginTop: '15px'
				}
			},
			iconStyle: "<>",
			map: map,
			position: polygonB.getBounds().getCenter()
		});
		
		new SimpleMarker({
			iconLabel: {
				innerHTML: '<strong>AreaC</strong>',
				style: {
					color: 'red',
					marginTop: '15px'
				}
			},
			iconStyle: "<>",
			map: map,
			position: polygonC.getBounds().getCenter()
		});
	});
}

var initMap = function() {
	$("#map").height($(window).height() - 50);

	var map = new AMap.Map('map', {
		zoom: 15
	});
	AMap.plugin([ 'AMap.ToolBar', 'AMap.Scale', 'AMap.OverView' ],
		function() {
			map.addControl(new AMap.ToolBar());

			map.addControl(new AMap.Scale());

			map.addControl(new AMap.OverView({
				isOpen : true
			}));
		});
	
	initArea(map);
	
	$(window).resize(function() {
		$("#map").height($(window).height() - 50);
	});
}

$(function() {
	setTimeout("initMap()", 50);
})