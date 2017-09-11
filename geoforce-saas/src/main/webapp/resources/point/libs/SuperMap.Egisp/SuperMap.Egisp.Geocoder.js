
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp
 */
SuperMap.Egisp.Geocoder = SuperMap.Egisp.Geocoder || {};

/**
 * 搜索订单
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Geocoder.search = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/queryAddressList?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.result && e.result.records) {
                var data = e.result.records;
                if( data.length > 0 ) {                    
                    var html_page = SuperMap.Egisp.setPage(e.result.total, (e.result.page-1), '\'data_pager\''); 
                    $("#data_pager > ul").html(html_page);   
                    $("#data_pager").show();   
                    $(".page-marker").html( '第' + e.result.page + '/' + Math.ceil(e.result.total/10) + '页' );
                    success(data);
                }
                else {
                    failed("查询到0条地址");
                }
                return;
            }
            failed(e.info ? e.info : "地址查询失败");
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed("地址查询失败");
        }
    });
}

/**
 * 搜索订单
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Geocoder.geocode = function(address, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/addressMatchAndStore?",
        data: {address: address},
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess) {
                success(e);
                return;
            }
            failed(e.info ? e.info : "地址匹配失败");
        },
        error: function(info){
            SuperMap.Egisp.hideMask();
            failed("地址匹配失败");
        }
    });
}


/**
 * 根据订单属性获取订单信息窗中的内容
 * @param - attr - object - 订单属性
 */
SuperMap.Egisp.Geocoder.getAttrPopupHtml = function(me) {
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ (me.order ? me.order : "" ) +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">地址：</span>';
        h += '          <span class="text" >'+ SuperMap.Egisp.setStringEsc(me.address) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">创建时间：</span>';
        h += '          <span class="text" >'+ (me.addTime ? me.addTime : "") +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">所属区划：</span>';
        h += '          <span class="text" >'+ (me.areaName ? me.areaName : "") +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划编号：</span>';
        h += '          <span class="text">'+ (me.areaNum ? me.areaNum : "") +'</span>';
        h += '          ';
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}





/**
 * 订单列表
 */
SuperMap.Egisp.Geocoder.Table = {
    data_success: [],
    data_failed: []
};

SuperMap.Egisp.Geocoder.Table.refresh = function(data, showtotal) {  
    var len = data.length;
    if( len < 1 ) {
        return;
    }
    var tbody = $('.data-list table tbody').html(''), h = '';
    for( var i=0; i<len; i++ ) {
        var item = data[i];
        item.order = (i+1);
        h += SuperMap.Egisp.Geocoder.Table.getContentTd(item);
        /*if( item.smx && item.smx > 0 ) {
            SuperMap.Egisp.Geocoder.Table.data_success.push(item);
        }
        else {
            SuperMap.Egisp.Geocoder.Table.data_failed.push(item);
        }*/
    }
    tbody.html(h);  
    if( typeof(showtotal) === 'undefined' )  {
        $('.data-list .totality').html( '('+ len +'条)' );
    }
    
    SuperMap.Egisp.Geocoder.Table.bindTrClick();
}
SuperMap.Egisp.Geocoder.Table.getContentTd = function( item ) {   
    var h = '';
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.address + '</td>';
    h += '  <td>' + item.addTime + '</td>';
    h += '  <td>' + (item.areaName ? item.areaName : "") + '</td>';    
    h += '  <td>' + (item.areaNum ? item.areaNum : "") + '</td>';
    h += '</tr>';
    return h;
}
SuperMap.Egisp.Geocoder.Table.bindTrClick = function() {
    $('.data-list table tbody tr').unbind('click').click(function(){
        $('.data-list table tbody tr').removeClass('action');
        var me = $(this);
        me.addClass('action');
        var marker = Map.getOrderFromLayerById(me.attr('data-id'));
        if( marker && marker.attributes.smx && marker.attributes.smx != 0 ) {            
            Map.openAddressAttrPopup(marker.attributes);
            var zoom = map.getZoom();
            lonlat = new SuperMap.LonLat(marker.attributes.smx, marker.attributes.smy);
            map.setCenter( lonlat, zoom > 10 ? zoom : 10);
        }
        else {
            map.removeAllPopup();
        }
    });
}
/**
 * 清空表格
 */
SuperMap.Egisp.Geocoder.Table.clear = function() {
    $('.data-list table tbody').html('');
    SuperMap.Egisp.Order.Table.data_success = [];
    SuperMap.Egisp.Order.Table.data_failed = [];
    $('#data_pager ul, .page-marker, .data-list .head .totality').html("");
    $('.a-data-success span, .a-data-failed span').html("");
}




