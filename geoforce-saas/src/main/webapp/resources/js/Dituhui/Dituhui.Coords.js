﻿
/** 
 * 企业版在线接口
 * 命名空间:Dituhui
 */
Dituhui.Coords = Dituhui.Coords || {};

Dituhui.Coords.pi = 3.14159265358979324;// 圆周率
Dituhui.Coords.a = 6378245.0;// WGS 长轴半径
Dituhui.Coords.ee = 0.00669342162296594323;// WGS 偏心率的平方

/**
 * 超图经纬度转为天地图经纬度
 * 
 * @param: lonlat - 超图坐标，墨卡托或经纬度都可以
 * @return: lonlat - 
 */
Dituhui.Coords.sm2tdt = function( lonlat ) {
    if( lonlat.lon && lonlat.lon > 180 ) {
        lonlat = Dituhui.metersToLatLon( lonlat );        
    }

    lonlat = {
        x: lonlat.lon,
        y: lonlat.lat
    }
    
    var lontitude = lonlat.x - ( Dituhui.Coords.transform(lonlat.x, lonlat.y).x - lonlat.x);
    var latitude = lonlat.y - ( Dituhui.Coords.transform(lonlat.x, lonlat.y).y - lonlat.y);
    
    var lonlat1 = new SuperMap.LonLat( lontitude, latitude );
    
    return lonlat1;
} 

/**
 * 天地图经纬度转超图墨卡托
 * 
 * param: lonlat - 天地图经纬度
 * return: Point2D - 超图墨卡托
 */
Dituhui.Coords.tdt2sm = function( lonlat ) {
    lonlat = Dituhui.Coords.tdt2smLonlat( lonlat );
    p = Dituhui.latLonToMeters(lonlat);
    return p;
}  

/**
 * 天地图经纬度转超图经纬度
 * 
 * param: lonlat - 天地图经纬度
 * return: Point2D - 超图墨卡托
 */
Dituhui.Coords.tdt2smLonlat = function( lonlat ) {
    if( Dituhui.Coords.outOfChina(lonlat.lon, lonlat.lat) ) {
        return lonlat;              
    }
    lonlat = {
        x: lonlat.lon,
        y: lonlat.lat
    };
    
    var d = Dituhui.Coords.delta(lonlat.x, lonlat.y);
    var p = new SuperMap.LonLat( lonlat.x + d.x, lonlat.y + d.y );
    return p;
}
Dituhui.Coords.transformLat = function(x, y) {
    var pi = Dituhui.Coords.pi;
    var ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
        + 0.2 * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
    ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
    return ret;
}
Dituhui.Coords.transformLon = function(x, y) {
    var pi = Dituhui.Coords.pi;
    var ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
        * Math.sqrt(Math.abs(x));
    ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
    ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
    ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0
        * pi)) * 2.0 / 3.0;
    return ret;
}
Dituhui.Coords.transform = function( lon, lat) {
    var pi = Dituhui.Coords.pi;
    var a = Dituhui.Coords.a;
    var ee = Dituhui.Coords.ee;
    var lonlat;
    if( Dituhui.Coords.outOfChina(lon, lat) ) {
        lonlat = {
            x: lon,
            y: lat
        }               
        return lonlat;
    }
    var dLat = Dituhui.Coords.transformLat(lon - 105.0, lat - 35.0);
    var dLon = Dituhui.Coords.transformLon(lon - 105.0, lat - 35.0);
    var radLat = lat / 180.0 * pi;
    var magic = Math.sin(radLat);
    magic = 1 - ee * magic * magic;
    var sqrtMagic = Math.sqrt(magic);
    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    var mgLat = lat + dLat;
    var mgLon = lon + dLon;
    
    lonlat = {
        x: mgLon,
        y: mgLat
    }
    return lonlat;
}

Dituhui.Coords.delta = function(lon, lat) {
    var pi = Dituhui.Coords.pi;
    var a = Dituhui.Coords.a;
    var ee = Dituhui.Coords.ee;

    var dLat = Dituhui.Coords.transformLat(lon - 105.0, lat - 35.0);
    var dLon = Dituhui.Coords.transformLon(lon - 105.0, lat - 35.0);
    var radLat = lat / 180.0 * pi;
    var magic = Math.sin(radLat);
    magic = 1 - ee * magic * magic;
    var sqrtMagic = Math.sqrt(magic);
    dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
    dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
    var p = { x: dLon, y: dLat };
    return p;
}

/**
 * 中国坐标内
 * 
 * @param lon - 经度
 * @param lat - 纬度
 * @return
 */
Dituhui.Coords.outOfChina = function(lon, lat) {
    if (lon < 72.004 || lon > 137.8347)
        return true;
    if (lat < 0.8293 || lat > 55.8271)
        return true;
    return false;
}