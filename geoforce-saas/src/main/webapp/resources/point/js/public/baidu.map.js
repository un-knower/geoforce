/**
 * 百度地图类
 */
var Baidu = {};
/**
 * 百度地图图层
 */
Baidu.layer = null;
Baidu.using = false;
/**
 * 百度地图图层初始化
 */
Baidu.init = function() {
    Baidu.layer = new SuperMap.Layer.Baidu();
    $('a.to-baidu').click(Baidu.use);
}
/**
 * 切换到百度地图
 */
Baidu.use = function() {
    if(Baidu.using === true) {
        return;
    }
    var center = map.getCenter();
    var zoom = map.getZoom();
    Map.unuse();
    Baidu.using = true;
    Baidu.layer = new SuperMap.Layer.Baidu();
    map.addLayer( Baidu.layer );
    map.setLayerIndex(Baidu.layer, 0);
    map.setBaseLayer(Baidu.layer);

    var center_b = Baidu.getCoord(center.lon, center.lat);
    map.setCenter(new SuperMap.LonLat(center_b.x, center_b.y), zoom-2);
    Baidu.redrawBoundry();
    Map.redrawDataPoints();
    $('.map-copyright').html('&copy; 2015 Baidu - GS(2015)2650号 - Data &copy; NavInfo & CenNavi');
    $('.nav-tools .second-menu a').removeClass('action').addClass('normal');
    $('a.to-baidu').removeClass('normal').addClass('action');
}
/**
 * 切换到百度地图
 */
Baidu.unuse = function() {
    if(!Baidu.using) {
        return;
    }
    Baidu.using = false;
    map.removeLayer(Baidu.layer, false);
}
/**
 * 超图墨卡托坐标转百度墨卡托坐标
 * @return - 返回百度墨卡托坐标
 */
Baidu.getCoord = function (x, y) { 
    var lonlat = SuperMap.Egisp.metersToLatLon(new SuperMap.LonLat(x, y));
    x = lonlat.lon;
    y = lonlat.lat; 

    var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
    var z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);  
    var theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);  
    bdLon = z * Math.cos(theta) + 0.0065;  
    bdLat = z * Math.sin(theta) + 0.006; 
    var coord = Baidu.coord.convertLL2MC(bdLon, bdLat);
    return coord;
}
/**
 * 百度墨卡托坐标转超图墨卡托坐标
 * @return - 返回超图墨卡托坐标
 */
Baidu.restoreCoord = function (x_b, y_b) {  
    var coord = Baidu.coord.convertMC2LL(x_b, y_b);

    var x = coord.x - 0.0065, y = coord.y - 0.006;  
    var x_pi = 3.14159265358979324 * 3000.0 / 180.0;    
    var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);  
    var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);  
    var gcjLon = z * Math.cos(theta);  
    var gcjLat = z * Math.sin(theta);
    var c = SuperMap.Egisp.latLonToMeters(new SuperMap.LonLat(gcjLon, gcjLat));
    return {'y' : c.lat, 'x' : c.lon};
}

/**
 * 重新绘制边界数据
 */
Baidu.redrawBoundry = function() {
    var fs = layer_boundry.features;
    var fs_b = [];
    for(var i=fs.length; i--; ) {
        var f = fs[i], f_b;
        var attr = f.attributes;
        var length = attr.geo.points.length, points = [];
        for(var k=0; k<length; k++) {
            var p = attr.geo.points[k];

            var p_b = Baidu.getCoord(p.x, p.y);
            points.push(p_b);
        }
        var geometry = SuperMap.Egisp.Zone.DrawRegion(attr.geo.parts, points);
        var feature = new SuperMap.Feature.Vector( geometry, attr, f.style );
        fs_b.push(feature);
    }
    layer_boundry.removeAllFeatures();
    layer_boundry.addFeatures(fs_b);    
    layer_boundry.redraw();
    // map.zoomToExtent(layer_boundry.getDataExtent());
}





Baidu.coord = {};
Baidu.coord.EARTHRADIUS = 6370996.81;
Baidu.coord.MCBAND = [12890594.86, 8362377.87, 5591021, 3481989.83, 1678043.12, 0];
Baidu.coord.LLBAND = [75, 60, 45, 30, 15, 0];
Baidu.coord.LL2MC = [[-0.0015702102444, 111320.7020616939, 1704480524535203, -10338987376042340, 26112667856603880, -35149669176653700, 26595700718403920, -10725012454188240, 1800819912950474, 82.5], [0.0008277824516172526, 111320.7020463578, 647795574.6671607, -4082003173.641316, 10774905663.51142, -15171875531.51559, 12053065338.62167, -5124939663.577472, 913311935.9512032, 67.5],[0.00337398766765, 111320.7020202162, 4481351.045890365, -23393751.19931662, 79682215.47186455, -115964993.2797253, 97236711.15602145, -43661946.33752821, 8477230.501135234, 52.5],[0.00220636496208, 111320.7020209128, 51751.86112841131, 3796837.749470245, 992013.7397791013, -1221952.21711287, 1340652.697009075, -620943.6990984312, 144416.9293806241, 37.5],[-0.0003441963504368392, 111320.7020576856, 278.2353980772752, 2485758.690035394, 6070.750963243378, 54821.18345352118, 9540.606633304236, -2710.55326746645, 1405.483844121726, 22.5],[-0.0003218135878613132, 111320.7020701615, 0.00369383431289, 823725.6402795718, 0.46104986909093, 2351.343141331292, 1.58060784298199, 8.77738589078284, 0.37238884252424, 7.45]];
Baidu.coord.MC2LL = [[1.410526172116255e-8, 0.00000898305509648872, -1.9939833816331, 200.9824383106796, -187.2403703815547, 91.6087516669843, -23.38765649603339, 2.57121317296198, -0.03801003308653, 17337981.2],[-7.435856389565537e-9, 0.000008983055097726239, -0.78625201886289, 96.32687599759846, -1.85204757529826, -59.36935905485877, 47.40033549296737, -16.50741931063887, 2.28786674699375, 10260144.86],[-3.030883460898826e-8, 0.00000898305509983578, 0.30071316287616, 59.74293618442277, 7.357984074871, -25.38371002664745, 13.45380521110908, -3.29883767235584, 0.32710905363475, 6856817.37],[-1.981981304930552e-8, 0.000008983055099779535, 0.03278182852591, 40.31678527705744, 0.65659298677277, -4.44255534477492, 0.85341911805263, 0.12923347998204, -0.04625736007561, 4482777.06],[3.09191371068437e-9, 0.000008983055096812155, 0.00006995724062, 23.10934304144901, -0.00023663490511, -0.6321817810242, -0.00663494467273, 0.03430082397953, -0.00466043876332, 2555164.4],[2.890871144776878e-9, 0.000008983055095805407, -3.068298e-8, 7.47137025468032, -0.00000353937994, -0.02145144861037, -0.00001234426596, 0.00010322952773, -0.00000323890364, 826088.5]];
Baidu.coord.getRange = function(lat, min, max) {
    if (min != null) {
        lat = Math.max(lat, min);
    }
    if (max != null) {
        lat = Math.min(lat, max);
    }
    return lat;
}
Baidu.coord.getLoop = function(lng, min, max) {
    while (lng > max) {
        lng -= max - min;
    }
    while (lng < min) {
        lng += max - min;
    }
    return lng;
}
Baidu.coord.converter = function(x, y, cE) {
    var xTemp = cE[0] + cE[1] * Math.abs(x);
    var cC = Math.abs(y) / cE[9];
    var yTemp = cE[2] + cE[3] * cC + cE[4] * cC * cC + cE[5] * cC * cC * cC + cE[6] * cC * cC * cC * cC + cE[7] * cC * cC * cC * cC * cC + cE[8] * cC * cC * cC * cC * cC * cC;
    xTemp *= (x < 0 ? -1 : 1);
    yTemp *= (y < 0 ? -1 : 1);
    
    var coord = {
        x: xTemp,
        y: yTemp
    }
    return coord;
}
Baidu.coord.convertLL2MC = function(lng, lat) {
    var cE = null;
    lng = Baidu.coord.getLoop(lng, -180, 180);
    lat = Baidu.coord.getRange(lat, -74, 74);
    var len = Baidu.coord.LLBAND.length;
    for (var i = 0; i < len; i++) {
        if (lat >= Baidu.coord.LLBAND[i]) {
            cE = Baidu.coord.LL2MC[i];
            break;
        }
    }
    if (cE!=null) {
        for (var i = Baidu.coord.LLBAND.length - 1; i >= 0; i--) {
            if (lat <= -Baidu.coord.LLBAND[i]) {
                cE = Baidu.coord.LL2MC[i];
                break;
            }
        }
    }
    return Baidu.coord.converter(lng,lat, cE);
}
Baidu.coord.convertMC2LL = function(x, y) {
    var cF = null;
    x = Math.abs(x);
    y = Math.abs(y);
    var len = Baidu.coord.MCBAND.length;
    for (var cE = 0; cE < len; cE++) {
        if (y >= Baidu.coord.MCBAND[cE]) {
            cF = Baidu.coord.MC2LL[cE];
            break;
        }
    }
    var coord = Baidu.coord.converter(x, y, cF);
    return coord;
}