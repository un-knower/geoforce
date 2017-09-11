/**
 * 初始化时查询样式
 */
$(function(){
    SuperMap.Egisp.Point.Style.search();
})

/**
 * 查询网点的公共使用函数
 * 需要已定义Map
 */
/**
 * 查询网点
 */
Map.searchBranches = function() {
    layer_branches.removeAllFeatures();

    var param = { pageNo: -1 };
    SuperMap.Egisp.Point.Search(param, 
        function(data) {                
            var level = $('.smcity').attr('level');
            if(level != '0') {
                Map.showBranchesToMap(data);
                SuperMap.Egisp.Point.Table.refresh(data);
            }
            else {
                SuperMap.Egisp.Point.National.display(data);
            }
        },
        function(error) {

        }
    );
}
/**
 * 地图上显示网点数据
 */
Map.showBranchesToMap = function(data) {
    // map.removeAllPopup();
    layer_branches.removeAllFeatures();
    var len = data ? data.length : 0;
    if( len === 0 ) {
        SuperMap.Egisp.showPopover("当前查询到0条网点数据");
        return;
    }
    var pois = [];
    for( var i=0; i<len; i++ ) {
        var item = data[i];
        
        if(!item.smx || item.smx == 0) {
            continue;
        }

        if( !item.iconStyle ) {
            item.iconStyle = "assets/map/red/s.png";
        }
        var style = SuperMap.Egisp.Point.getBranchStyle( item.iconStyle );
        var size = new SuperMap.Size(style.width, style.height);

        var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);            
        var poi = new SuperMap.Feature.Vector(geo_point);
        poi.style = SuperMap.Egisp.Point.getBranchStyle( item );

        poi.attributes = item;
        poi.attributes.type = "point";
        pois.push(poi);
    }
    layer_branches.addFeatures(pois);
}

Map.selectBranch = function(poi) {
    var marker = poi.attributes;
    marker.lonlat = new SuperMap.LonLat(marker.smx, marker.smy);
    Map.openBranchAttrPopup(marker);
}
/**
 * 点击网点，弹出属性
 */
Map.openBranchAttrPopup = function(marker) {
    var netPicPath = marker.netPicPath ? 
        urls.server +'/pointService/getImg?path=' + marker.netPicPath 
        : "assets/map/branch.png";

    var h =  '<div class="map-popup">';
        h += SuperMap.Egisp.Point.getAttrPopupHtml(marker, false);
        h += '</div>';

    Map.popup = new SuperMap.Popup.FramedCloud("popup-near",
        marker.lonlat,
        new SuperMap.Size(300, 70),
        h,
        null,
        false,
        null
    );
    Map.popup.autoSize = true;
    Map.popup.panMapIfOutOfView = true;
    Map.popup.relativePosition = "tr";
    map.removeAllPopup();
    map.addPopup(Map.popup);

    var span = $(".bind-cars");
    SuperMap.Egisp.Point.getBindingCars( marker.id, 
        function(data){
            span.html(data);
        },
        function(){
            span.html("");
        }
    );

    $('.popup-close').unbind("click").click(function(){
        Map.popup.hide();
    });
    $('.popup-edit').unbind("click").click(function(){
        Map.showEditOrder(marker);
    });
}