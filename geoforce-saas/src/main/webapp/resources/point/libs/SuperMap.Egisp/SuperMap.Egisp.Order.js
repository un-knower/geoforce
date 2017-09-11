
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp
 */
SuperMap.Egisp.Order = SuperMap.Egisp.Order || {};

/**
 * 搜索订单
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Order.search = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/batchQuery?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.result && e.result.records) {
                var data = e.result.records;
                if( data.length > 0 ) {                    
                    var html_page = SuperMap.Egisp.setPage(e.result.totalCount, (e.result.page-1), '\'data_pager\''); 
                    $("#data_pager > ul").html(html_page);   
                    $("#data_pager").show();   
                    $(".page-marker").html( '第' + e.result.page + '/' + Math.ceil(e.result.totalCount/10) + '页' );
                    success(data);
                }
                else {                    
                    $("#data_pager > ul").html('');
                    $(".page-marker").html('');
                    failed(e.info ? e.info : "查询到0条订单");
                }
                return;
            }
            failed(e.info ? e.info : "订单查询失败");       
            $("#data_pager > ul").html('');
            $(".page-marker").html('');
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed("订单查询失败");
        }
    });
}
/**
 * 手动分单
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
SuperMap.Egisp.Order.manual = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/logisticsService?",
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
SuperMap.Egisp.Order.getAttrPopupHtml = function(me) {
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.order +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-edit" title="修改订单位置"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">订单地址：</span>';
        h += '          <span class="text" >'+ SuperMap.Egisp.setStringEsc(me.address) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">所属区划：</span>';
        h += '          <span class="text" >'+ (me.areaName ? me.areaName : "") +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">分单状态：</span>';
        var status = me.fendanStatus ? me.fendanStatus : "";
        var className = status.match('失败') ? "red" : "green";
        h += '          <span class="'+ className +' text">'+ status +'</span>';
        h += '          ';
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}
/**
 * 手动分单-信息窗
 * @param - attr - object - 订单属性
 */
SuperMap.Egisp.Order.getEditPopupHtml = function(me) { 
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">确认</span>';
        h += '      <a class="popup-close" title="取消"></a>';
        h += '      <a class="popup-sure" title="保存"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <span class="detail">确定将地址为 ' + me.address + ' 的订单手动分单到此位置？</span>';
        h += '  </div>';
        h += '</div>';
    return h;
}

/**
 * 区划量统计
 * @param - param - object - 查询参数 
 *      param.admincode - 行政区划编码
 *      param.level - 行政区划级别
 */
SuperMap.Egisp.Order.statisticRegions = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/statistic/area/getAreaCountByParm?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.isSuccess) {
                var data = [], items = [];
                if(!e.result || e.result.length === 0) {
                    success(data);
                    return;
                }
                data = e.result;
                for(var i=data.length; i--;) {
                    var item = data[i];
                    var code = item.admincode;
                    if(code.substring(4, 6) == "00") {
                        if(code.substring(2, 4) == "00") {
                            item.level = 1;
                            if(SuperMap.Egisp.SMCity.isZhixia(code)) {
                                item.zhixia = true;
                            }
                        }
                        else {
                            item.level = 2;
                        }
                    }
                    else {
                        item.level = 3;
                    }
                    item.count = item.count + '';

                    var tooltip =  '<p>名称：' + '<strong>'+ item.name +'</strong></p>';
                        tooltip += '<p>区划量：' + '<strong>'+ item.count +'</strong></p>';
                    item.tooltip = tooltip;
                }
                success(data);
                return;
            }
            failed(e.info ? e.info : "区划量统计失败");
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed("区划量统计失败");
        }
    });
}

/**
 * 分单量统计
 * @param - param - object - 查询参数 
 *      param.admincode - 行政区划编码
 *      param.level - 行政区划级别
 */
SuperMap.Egisp.Order.statisticOrders = function(param, success, failed) {
    SuperMap.Egisp.request({
        url: urls.server + "/statistic/order/getOrderCountByAdminCode?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.isSuccess) {
                var data = [], items = [];
                if(!e.result || e.result.length === 0) {
                    success(data);
                    return;
                }
                data = e.result;
                for(var i=data.length; i--;) {
                    var item = data[i];
                    var code = item.admincode;
                    if(code.substring(4, 6) == "00") {
                        if(code.substring(2, 4) == "00") {
                            item.level = 1;
                            if(SuperMap.Egisp.SMCity.isZhixia(code)) {
                                item.zhixia = true;
                            }
                        }
                        else {
                            item.level = 2;
                        }
                    }
                    else {
                        item.level = 3;
                    }
                    item.count = item.count + '';

                    var tooltip =  '<div>名称：' + '<strong>'+ item.name +'</strong></div>';
                        tooltip += '<div>单量：' + '<strong>'+ item.count +'</strong></div>';
                    item.tooltip = tooltip;
                }
                success(data);
                return;
            }
            failed(e.info ? e.info : "分单量统计失败");
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed("分单量统计失败");
        }
    });
}


/**
 * 订单列表
 */
SuperMap.Egisp.Order.Table = {
    data_success: [],
    data_failed: []
};

SuperMap.Egisp.Order.Table.refresh = function(data) {  
    $('#table_orders tbody').html('');
    SuperMap.Egisp.Order.Table.data_success = [];
    SuperMap.Egisp.Order.Table.data_failed = [];
    $('.a-data-success > span').html('');
    $('.a-data-failed > span').html('');
    $('.data-list .totality').html('');    

    var len = data.length;
    if( len < 1 ) {
        return;
    }
    var data_success = [], data_failed = [];
    var tbody = $('#table_orders tbody').html(''), h = '';
    for( var i=0; i<len; i++ ) {
        var item = data[i];
        item.order = (i+1);
        h += SuperMap.Egisp.Order.Table.getContentTd(item);
        if( item.fendanStatus && item.fendanStatus.match( '分单成功') ) {
            data_success.push(item);
        }
        else {
            data_failed.push(item);
        }
    }
    tbody.html(h);    
    $('.data-list .totality').html( '('+ len +'条)' );
    SuperMap.Egisp.Order.Table.data_success = data_success.concat();
    SuperMap.Egisp.Order.Table.data_failed = data_failed.concat();

    $('.a-data-success > span').html('('+ data_success.length +')');
    $('.a-data-failed > span').html('('+ data_failed.length +')');

    SuperMap.Egisp.Order.Table.bindTrClick();
}
SuperMap.Egisp.Order.Table.getContentTd = function( item ) {   
    item.fendanStatus = item.fendanStatus ? item.fendanStatus : "";
    var color = item.fendanStatus.match('失败') ? "red" : "green";
    item.course = ( color == "green" ? "" : ( (item.smx > 0 && item.smy > 0) ? "无区划" : "地址匹配失败" ) );
    var h = '';
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.orderNum + '</td>';
    h += '  <td>' + item.batch + '</td>';
    h += '  <td>' + item.address + '</td>';
    h += '  <td>' + (item.areaName ? item.areaName : "") + '</td>';    
    h += '  <td>' + item.course + '</td>';
    h += '  <td style="color: '+ color +';">' + item.fendanStatus + '</td>';
    if( item.fendanStatus && item.fendanStatus.match( '分单成功') ) {
        h += '<td><a class="td-option" href="javascript:void(0);">定位详情</a></td>';        
    }
    else {
        h += '<td><a class="td-option" href="javascript:void(0);" option="manual"'; 
        h += ' data-address="'+ item.address +'" data-id="'+ item.id +'" ';   
        if(item.smx > 0 && item.smy > 0) {
            h += ' data-smx="'+ item.smx +'" data-smy="'+ item.smy +'"';
        }
        h += '>手动分单</a></td>';
    }
    h += '</tr>';
    return h;
}
SuperMap.Egisp.Order.Table.bindTrClick = function() {
    $('#table_orders tbody tr').unbind('click').click(function(){
        $('#table_orders tbody tr').removeClass('action');
        var me = $(this);
        me.addClass('action');
        var marker = Map.getOrderFromLayerById(me.attr('data-id'));
        if( marker && marker.attributes.smx && marker.attributes.smx != 0 ) {            
            Map.openOrderAttrPopup(marker.attributes);
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
    $('a[option="manual"]').unbind('click').click(function(){
        var me = $(this);
        var attr = {
            id: me.attr('data-id'),
            address: me.attr('data-address')
        };
        if( me.attr('data-smx') ) {
            attr.lonlat = new SuperMap.LonLat( Number(me.attr('data-smx')), Number(me.attr('data-smy')) );
            setTimeout(function(){
                Map.endDragBranch();
                Map.showEditOrder(attr);
            }, 200);
        }
        else {
            attr.lonlat = map.getCenter();
            setTimeout(function(){
                Map.endDragBranch();
                Map.showEditOrder(attr);
                Map.openEditOrderPopup( attr );
            }, 200);
        }        
    });
}
/**
 * 清空表格
 */
SuperMap.Egisp.Order.Table.clear = function() {
    $('#table_orders tbody').html('');
    SuperMap.Egisp.Order.Table.data_success = [];
    SuperMap.Egisp.Order.Table.data_failed = [];
    $('.a-data-success > span').html('');
    $('.a-data-failed > span').html('');
    $('.data-list .totality').html('');
    $("#data_pager > ul").html('');
    $(".page-marker").html('');
}




