

/** 
 * 企业版在线接口
 * 命名空间:Dituhui
 */
Dituhui.Address = Dituhui.Address || {};

/**
 * 搜索地址
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */

var pageNo=0;
var pageSize=20;
var status=0;
Dituhui.Address.search = function(param, success, failed) {
    pageSize=param.pageSize;
    status=param.status;
    Dituhui.request({
        url: urls.server + "/orderService/correct_query?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e && e.isSuccess) {
                var data = [];
                if(e.result && e.result.records) {
                    data = e.result.records;
                }
                if(data.length > 0 ) {
                    $(".tolsNum").html(e.result.total);
                    pageNo=e.result.pageNo-1;
                    if(pageSize!=param.pageSize||status!=param.status){
                        pageNo=0;
                    }
                    var html_page = Dituhui.setPage(e.result.total, pageNo, '\'data_pager\'', param.pageSize); 
                    $("#data_pager > ul").html(html_page);   
                    $("#data_pager").attr("data-total-page", Math.ceil(e.result.total/param.pageSize) ).show();   
                    $(".page-marker").html( '第' + e.result.pageNo + '/' + Math.ceil(e.result.total/param.pageSize) + '页' );
                    success(data);
                }
                else {
                    failed(e.info ? e.info : "查询到0条地址");
                }
                return;
            }
            failed(e.info ? e.info : "地址查询失败");
        },
        error: function(){
            Dituhui.hideMask();
            failed("地址查询失败");
        }
    });
}

/**
 * 添加纠错地址
 * @param - address - 待添加的地址
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
Dituhui.Address.add = function(address, success, failed) {
    Dituhui.request({
        url: urls.server + "/orderService/correct_add?",
        data: {address: address},
        success: function(e){
            Dituhui.hideMask();
            if(e && e.isSuccess) {
                success();
                return;
            }
            failed(e.info ? e.info : "地址纠错失败");
        },
        error: function(){
            Dituhui.hideMask();
            failed("地址纠错失败");
        }
    });
} 
/**
 * 添加纠错地址
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
Dituhui.Address.remove = function(id, success, failed) {
    Dituhui.request({
        url: urls.server + "/orderService/correct_del?",
        data: {id: id},
        success: function(e){
            Dituhui.hideMask();
            if(e && e.isSuccess) {
                success();
                return;
            }
            failed();
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }
    });
}
/**
 * 地址纠错
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
Dituhui.Address.correct = function(param, success, failed) {
    Dituhui.request({
        url: urls.server + "/orderService/correct_move?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e && e.isSuccess) {
                success();
                return;
            }
            failed();
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }
    });
}

/**
 * 根据订单属性获取订单信息窗中的内容
 * @param - attr - object - 订单属性
 */
Dituhui.Address.getAttrPopupHtml = function(me) {
    var coord = Dituhui.metersToLatLon( new SuperMap.LonLat(Number(me.x), Number(me.y)) );   
    if(me.status==0){
        coord.lon=0;
        coord.lat=0;
    }    
    var h =  '<div class="map-popup" style="min-height: 80px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.order +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-edit" title="纠错"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">地址：</span>';
        h += '          <span class="text" >'+ Dituhui.setStringEsc(me.address) +'</span>';
        h += '      </div>';
        // h += '      <div class="attr">';
        // h += '          <span class="label">创建批次：</span>';
        // h += '          <span class="text" >'+ (me.batch ? me.batch : "") +'</span>';
        // h += '      </div>';
        // h += '      <div class="attr">';
        // h += '          <span class="label">所属区划：</span>';
        // h += '          <span class="text" >'+ (me.areaName ? me.areaName : "") +'</span>';
        // h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">经度：</span>';
        h += '          <span class="text"  id="lonText">'+ (coord.lon ? coord.lon.toFixed(2) : "无") +'</span>';
        h += '          <input id="pointLon" placeholder="'+ (coord.lon ? coord.lon.toFixed(2) : "支持高德经度") +'"/>'
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">纬度：</span>';
        h += '          <span class="text" id="latText">'+ (coord.lat ? coord.lat.toFixed(2) : "无") +'</span>';
        h += '          <input id="pointLat" placeholder="'+ (coord.lat ? coord.lat.toFixed(2) : "支持高德纬度") +'"/>'
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}


// 数据更新弹窗内容

Dituhui.Address.getUpdataPopupHtml = function(me) {
    var coord = Dituhui.metersToLatLon( new SuperMap.LonLat(Number(me.lonlat.lon), Number(me.lonlat.lat)) );   
    // if(me.status==0){
    //     coord.lon=0;
    //     coord.lat=0;
    // } 

    var h =  '<div class="map-popup" style="min-height: 80px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.order +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-edit" title="纠错"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">地址：</span>';
        h += '          <span class="text" >'+ Dituhui.setStringEsc(me.address) +'</span>';
        h += '      </div>';
        // h += '      <div class="attr">';
        // h += '          <span class="label">创建批次：</span>';
        // h += '          <span class="text" >'+ (me.batch ? me.batch : "") +'</span>';
        // h += '      </div>';
        // h += '      <div class="attr">';
        // h += '          <span class="label">所属区划：</span>';
        // h += '          <span class="text" >'+ (me.areaName ? me.areaName : "") +'</span>';
        // h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">经度：</span>';
        // h += '          <span class="text"  id="lonText">'+ (coord.lon ? coord.lon.toFixed(2) : "无") +'</span>';
        h += '          <input id="pointLon" value="'+ (coord.lon ? coord.lon.toFixed(2) : "支持高德经度") +'"/>'
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">纬度：</span>';
        // h += '          <span class="text" id="latText">'+ (coord.lat ? coord.lat.toFixed(2) : "无") +'</span>';
        h += '          <input id="pointLat" value="'+ (coord.lat ? coord.lat.toFixed(2) : "支持高德纬度") +'"/>'
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}


Dituhui.Address.openCorrectionAddressPopup = function(me) {
    var coord1 = Dituhui.metersToLatLon( new SuperMap.LonLat(Number(me.x), Number(me.y)) );   
    var h =  '<div class="map-popup" style="min-height: 80px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.order +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-sure" title="纠错" id="popup-sure"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">地址：</span>';
        h += '          <span class="text" >'+ Dituhui.setStringEsc(me.address) +'</span>';
        h += '      </div>';
        // h += '      <div class="attr">';
        // h += '          <span class="label">创建批次：</span>';
        // h += '          <span class="text" >'+ (me.batch ? me.batch : "") +'</span>';
        // h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">所属区划：</span>';
        h += '          <span class="text" >'+ (me.areaName ? me.areaName : "") +'</span>';
        h += '      </div>';
        h += '      <div class="attr pointLon">';
        h += '          <span class="label" id="lonText">经度：</span>';
        h += '          <input id="pointLon" value="coord1.lon ? coord1.lon.toFixed(2) : "暂无详细信息""/>'
        // h += '          <span class="text">'+ (coord1.lon ? coord1.lon.toFixed(2) : "暂无详细信息") +'</span>';
        h += '      </div>';
        h += '      <div class="attr pointLat">';
        h += '          <span class="label" id="latText">纬度：</span>';
        h += '          <input id="pointLat" value="coord1.lat ? coord1.lat.toFixed(2) : "暂无详细信息""/>'

        // h += '          <span class="text">'+ (coord1.lat ? coord1.lat.toFixed(2) : "暂无详细信息") +'</span>';
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}


/**
 * 纠错-信息窗
 * @param - attr - object - 订单属性
 */
Dituhui.Address.getEditPopupHtml = function(me) { 

    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">确认</span>';
        h += '      <a class="popup-close" title="取消"></a>';
        h += '      <a class="popup-sure" title="保存"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <span class="detail">确定将地址 ' + me.address + ' 纠错到此位置？</span>';
        h += '  </div>';
        h += '</div>';
    return h;
}
Dituhui.Address.getEditPopupHtmlSure=function(me){
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="popLeft"></div>';
        h += '  <div class="popRight">';
        h += '      <div class="title_tit">';
        h += '          纠错后的地址将在分单时被调用，确定对此条地址进行纠错吗？   '
        h += '      </div>';
        h += '      <div class="content">';
        h += '          <div class="contentInput">'
        h += '              <input id="deleteCorrection" value="取消" type="submit">';
        h += '              <input id="SureCorrection" value="确定" type="submit">';
        h += '          </div>'      
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}




/**
 * 订单列表
 */
Dituhui.Address.Table = {};

Dituhui.Address.Table.refresh = function(data) {
    $('.data-table tbody').html('');
    $('.data-list .totality').html('');

    var len = data.length;
    if( len < 1 ) {
        return;
    }
    var tbody = $('.data-table tbody').html(''), h = '';
    for( var i=0; i<len; i++ ) {
        var item = data[i];
        item.order = (i+1);
        h += Dituhui.Address.Table.getContentTd(item);
    }
    tbody.html(h);    
    $('.data-list .totality').html( '('+ len +'条)' );

    Dituhui.Address.Table.bindTrClick();
}
var pointState='';
var coord2 = '';
Dituhui.Address.Table.getContentTd = function( item ) {
    var wordStatue='';
    if(item.status){
        wordStatue="修改";
    }
    else{
        wordStatue='纠错';
    }
    coord2 = Dituhui.metersToLatLon( new SuperMap.LonLat(Number(item.x), Number(item.y)) );   
    var h = '';
    if(item.status){
        pointState="已纠错";
    }
    else{
        pointState="未纠错";
    }
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.address + '</td>';
    h += '  <td>' + (coord2.lon ? coord2.lon.toFixed(2) : "无") + '</td>';
    h += '  <td>' + (coord2.lat ? coord2.lat.toFixed(2) : "无") + '</td>';
    h += '  <td>' + item.addTime + '</td>';    
    h += '  <td>' + (item.correctTime ? item.correctTime : "") +  '</td>';
    h += '  <td>' + pointState +  '</td>';

    h += '  <td>';
    h += '      <a class="td-option" href="javascript:void(0);" option="correct" data-id="'+ item.id +'" data-address="'+ Dituhui.setStringEsc(item.address) +'"';
    if( item.x && item.x > 0 ) {
        h += '   data-x="'+ item.x +'" data-y="'+ item.y +'"';
    }
    h += '      >'+wordStatue+'</a><i class="td-separator"></i>';
    h += '      <a class="td-option" href="javascript:void(0);" option="delete" data-id="'+ item.id +'" data-address="'+ Dituhui.setStringEsc(item.address) +'">删除</a>';
    h += '  </td>';
    h += '  </td>';
    h += '</tr>';
    return h;
}
Dituhui.Address.Table.bindTrClick = function() {
    $('.data-table tbody tr').unbind('click').click(function(){
        $('.data-table tbody tr').removeClass('action');
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
            Map.endDragBranch();
            if(Map.popup != null) {
                Map.popup.hide();
            }
        }
    });
    $('a.td-option[option="delete"]').unbind('click').click(function(){
        var me = $(this);
        Dituhui.Modal.alert('是否删除纠错地址 <strong>'+ me.attr('data-address') +'</strong> ? 删除后不可恢复。', 
			Map.deleteAddress,
			{"data-id": me.attr('data-id'), 'data-address': me.attr('data-address')}
		);
    });
    $('a.td-option[option="correct"]').unbind('click').click(function(){
        var me = $(this);
        var order=me.parents('tr').children().eq(0).html();
        var status=me.parents('tr').children().eq(6).html();
        if(status=="未纠错"){
            status=0;
        }
        else{
            status=1;
        }
        var attr = {
            id: me.attr('data-id'),
            address: me.attr('data-address'),
            order:order,
            status:status
        };
        if( me.attr('data-x') ) {
            attr.lonlat = new SuperMap.LonLat( Number(me.attr('data-x')), Number(me.attr('data-y')) );
            setTimeout(function(){
                Map.endDragBranch();
                Map.showEditAddress(attr);
                Map.openEditAddressPopup( attr,false);                
            }, 200);
        }
        else {
            attr.lonlat = map.getCenter();
            setTimeout(function(){
                Map.endDragBranch();
                Map.showEditAddress(attr);
                Map.openEditAddressPopup( attr,false);

            }, 200);
        }        
    });
}
/**
 * 清空表格
 */
Dituhui.Address.Table.clear = function() {
    $('.data-table tbody').html('');
    $('.data-list .totality').html('');
    $("#data_pager > ul").html('');
    $("#data_pager").attr({
        page: 0,
        'data-total-page': 0
    });
    $(".page-marker").html('');
}
