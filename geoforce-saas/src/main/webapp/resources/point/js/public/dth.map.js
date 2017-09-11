/**
 * 地图切换
 */
Map.using = true;
Map.layer = null;
Map.init = function() {
    Map.layer = new SuperMap.Layer.CloudLayer({url: urls.map_img});
    map.addLayer(Map.layer);
    map.setLayerIndex(Map.layer, 0);
    map.setBaseLayer(Map.layer);
    $('a.to-supermap').click(Map.use);
    $('.nav-tools .second-menu a').removeClass('action').addClass('normal');
    $('a.to-supermap').removeClass('normal').addClass('action');
}
Map.use = function() {
    if(Map.using) {
        return;
    }
    var center = map.getCenter();
    var zoom = map.getZoom();

    Baidu.unuse();

    Map.layer = new SuperMap.Layer.CloudLayer({url: urls.map_img});
    map.addLayer(Map.layer);    
    map.setLayerIndex(Map.layer, 0);
    map.setBaseLayer(Map.layer);
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
        var geometry = SuperMap.Egisp.Zone.DrawRegion(attr.geo.parts, attr.geo.points);
        var feature = new SuperMap.Feature.Vector( geometry, attr, f.style );
        fs_b.push(feature);
    }
    layer_boundry.removeAllFeatures();
    layer_boundry.addFeatures(fs_b);    
    // map.zoomToExtent(layer_boundry.getDataExtent());
}
Map.redrawDataPoints = function() {
    Map.searchBranches();
    if( $('#btn_showRegion').prop('checked') == true ) {
        Map.searchRegions();
    }
    if(layer_cloudpois.features.length > 0) {
        Map.searchFromCloud();
    }
}