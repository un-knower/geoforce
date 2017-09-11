
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp
 */
SuperMap.Egisp.Address = SuperMap.Egisp.Address || {};

/**
 * 搜索地址
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Address.search = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/correct_query?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.isSuccess) {
                var data = [];
                if(e.result && e.result.records) {
                    data = e.result.records;
                }
                if(data.length > 0 ) {                    
                    var html_page = SuperMap.Egisp.setPage(e.result.total, (e.result.pageNo-1), '\'data_pager\'', 20); 
                    $("#data_pager > ul").html(html_page);   
                    $("#data_pager").attr("data-total-page", Math.ceil(e.result.total/20) ).show();   
                    $(".page-marker").html( '第' + e.result.pageNo + '/' + Math.ceil(e.result.total/20) + '页' );
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
            SuperMap.Egisp.hideMask();
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
SuperMap.Egisp.Address.add = function(address, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/correct_add?",
        data: {address: address},
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.isSuccess) {
                success();
                return;
            }
            failed(e.info ? e.info : "地址纠错失败");
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed("地址纠错失败");
        }
    });
} 
/**
 * 添加纠错地址
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Address.remove = function(id, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/correct_del?",
        data: {id: id},
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.isSuccess) {
                success();
                return;
            }
            failed();
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed();
        }
    });
}
/**
 * 地址纠错
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Address.correct = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/correct_move?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.isSuccess) {
                success();
                return;
            }
            failed();
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed();
        }
    });
}

/**
 * 根据订单属性获取订单信息窗中的内容
 * @param - attr - object - 订单属性
 */
SuperMap.Egisp.Address.getAttrPopupHtml = function(me) {
    var h =  '<div class="map-popup" style="min-height: 80px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.order +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-edit" title="纠错"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">地址：</span>';
        h += '          <span class="text" >'+ SuperMap.Egisp.setStringEsc(me.address) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">创建批次：</span>';
        h += '          <span class="text" >'+ (me.batch ? me.batch : "") +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">所属区划：</span>';
        h += '          <span class="text" >'+ (me.areaName ? me.areaName : "") +'</span>';
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}

/**
 * 纠错-信息窗
 * @param - attr - object - 订单属性
 */
SuperMap.Egisp.Address.getEditPopupHtml = function(me) { 
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




/**
 * 订单列表
 */
SuperMap.Egisp.Address.Table = {};

SuperMap.Egisp.Address.Table.refresh = function(data) {  
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
        h += SuperMap.Egisp.Address.Table.getContentTd(item);
    }
    tbody.html(h);    
    $('.data-list .totality').html( '('+ len +'条)' );

    SuperMap.Egisp.Address.Table.bindTrClick();
}
SuperMap.Egisp.Address.Table.getContentTd = function( item ) {    
    var h = '';
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.batch + '</td>';
    h += '  <td>' + item.address + '</td>';
    h += '  <td>' + (item.areaName ? item.areaName : "") + '</td>';
    h += '  <td>';
    h += '      <a class="td-option" href="javascript:void(0);" option="correct" data-id="'+ item.id +'" data-address="'+ SuperMap.Egisp.setStringEsc(item.address) +'"';
    if( item.x && item.x > 0 ) {
        h += '   data-x="'+ item.x +'" data-y="'+ item.y +'"';
    }
    h += '      >纠错</a><i class="td-separator"></i>';
    h += '      <a class="td-option" href="javascript:void(0);" option="delete" data-id="'+ item.id +'" data-address="'+ SuperMap.Egisp.setStringEsc(item.address) +'">删除</a>';
    h += '  </td>';
    h += '  </td>';
    h += '</tr>';
    return h;
}
SuperMap.Egisp.Address.Table.bindTrClick = function() {
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
        $('.delete-address-name').html( me.attr('data-address') );
        $('button[option="delete-address"]').attr({
            "data-id": me.attr('data-id'),
            'data-address': me.attr('data-address')
        });
        $("#modal_delete_address").modal({
            backdrop: "static",
            keyboard: false
        }); 
    });
    $('a.td-option[option="correct"]').unbind('click').click(function(){
        var me = $(this);
        var attr = {
            id: me.attr('data-id'),
            address: me.attr('data-address')
        };
        if( me.attr('data-x') ) {
            attr.lonlat = new SuperMap.LonLat( Number(me.attr('data-x')), Number(me.attr('data-y')) );
            setTimeout(function(){
                Map.endDragBranch();
                Map.showEditAddress(attr);
            }, 200);
        }
        else {
            attr.lonlat = map.getCenter();
            setTimeout(function(){
                Map.endDragBranch();
                Map.showEditAddress(attr);
                Map.openEditAddressPopup( attr );
            }, 200);
        }        
    });
}
/**
 * 清空表格
 */
SuperMap.Egisp.Address.Table.clear = function() {
    $('.data-table tbody').html('');
    $('.data-list .totality').html('');
    $("#data_pager > ul").html('');
    $("#data_pager").attr({
        page: 0,
        'data-total-page': 0
    });
    $(".page-marker").html('');
}
