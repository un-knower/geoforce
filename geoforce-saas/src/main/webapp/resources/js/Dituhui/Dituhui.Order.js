
/** 
 * 企业版在线接口
 * 命名空间:Dituhui
 */
Dituhui.Order = Dituhui.Order || {};

/**
 * 搜索订单
 * @param - param - 参数，batch批次必填
 * @param - success - 成功返回函数
 * @param - failed - 失败返回函数
 */
Dituhui.Order.search = function(param, success, failed) {
    Dituhui.request({
        url: urls.server + "/orderService/batchQuery?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e && e.result && e.result.records) {
                var data = e.result.records;
                if( data.length > 0 ) {                    
                    var html_page = Dituhui.setPage(e.result.totalCount, (e.result.page-1), '\'data_pager\''); 
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
            Dituhui.hideMask();
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
Dituhui.Order.manual = function(param, success, failed) {
    Dituhui.request({
        url: urls.server + "/orderService/logisticsService?",
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
Dituhui.Order.getAttrPopupHtml = function(me) {
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.order +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-edit" title="修改订单位置"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">订单地址：</span>';
        h += '          <span class="text" >'+ Dituhui.setStringEsc(me.address) +'</span>';
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
Dituhui.Order.getEditPopupHtml = function(me) { 
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
Dituhui.Order.statisticRegions = function(param, success, failed) {
    Dituhui.request({
        url: urls.server + "/statistic/area/getAreaCountByParm?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
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
                            if(Dituhui.SMCity.isZhixia(code)) {
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
            Dituhui.hideMask();
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
Dituhui.Order.statisticOrders = function(param, success, failed) {
    Dituhui.request({
        url: urls.server + "/statistic/order/getCountByAdminResulttype?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
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
                            if(Dituhui.SMCity.isZhixia(code)) {
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
            Dituhui.hideMask();
            failed("分单量统计失败");
        }
    });
}

/**
 * 全国订单量统计
 * @param - param - object - 查询参数 
 */
Dituhui.Order.statisticOrdersAll = function(param, success, failed) {
    Dituhui.request({
        url: urls.server + "/statistic/order/getAllByResulttype?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e && e.isSuccess) {
            	if( !e.result || e.result.length == 0) {
            		failed(e.info ? e.info : "共统计到0条订单数据");
            		return;
            	}
            	var data = [], all = 0;
                for(var i=0, len=e.result.length; i<len; i++) {
                	var item = e.result[i];
                	data.push({
                		value: item.sumcount,
                		name: Dituhui.Order.getStatisticsStatusName(item.resulttype),
                		key: Number(item.resulttype),
                		itemStyle: {
                			normal: {
                				color: Dituhui.Order.getColorOfType(item.resulttype)
                			}
                		},
                		label: {
                			normal: {                				
	                			textStyle: {
	                				color: "#333"
	                			}
                			}
                		}
                	});
                	all += item.sumcount;
                }
                function compare(property){
				    return function(a,b){
				        var value1 = a[property];
				        var value2 = b[property];
				        return value1 - value2;
				    }
				}
                data.sort(compare("key"));
                success({
                	total: all,
                	datas: data
                });
                return;
            }
            failed(e.info ? e.info : "分单量统计失败");
        },
        error: function(){
            Dituhui.hideMask();
            failed("分单量统计失败");
        }
    });
}
/**
 * 根据订单状态获取颜色
 */
Dituhui.Order.getColorOfType = function(type){
	switch(type) {
		case "1":
			return "#91C7AF";
		case "2": 
			return "#C33430";
		case "3":
			return "#2E4454";
		default:
			return "green";
	}
}
/**
 * 全国订单量统计 根据type返回分单名称
 * @param - type - string 
 */
Dituhui.Order.getStatisticsStatusName = function(type) {
	switch(type) {
		case "1":
			return "分单成功";
		case "2":
			return "分单失败-无区划";
		case "3":
			return "分单失败-无坐标";
		default:
			return "";
	}
}
/**
 * 订单列表
 */
Dituhui.Order.Table = {
    data_success: [],
    data_failed: []
};

Dituhui.Order.Table.refresh = function(data) {  
    $('#table_orders tbody').html('');
    Dituhui.Order.Table.data_success = [];
    Dituhui.Order.Table.data_failed = [];
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
        h += Dituhui.Order.Table.getContentTd(item);
        if( item.fendanStatus && item.fendanStatus.match( '分单成功') ) {
            data_success.push(item);
        }
        else {
            data_failed.push(item);
        }
    }
    tbody.html(h);    
    $('.data-list .totality').html( '('+ len +'条)' );
    Dituhui.Order.Table.data_success = data_success.concat();
    Dituhui.Order.Table.data_failed = data_failed.concat();

    $('.a-data-success > span').html('('+ data_success.length +')');
    $('.a-data-failed > span').html('('+ data_failed.length +')');

    Dituhui.Order.Table.bindTrClick();
}
Dituhui.Order.Table.getContentTd = function( item ) {   
    item.fendanStatus = item.fendanStatus ? item.fendanStatus : "";
    var color = item.fendanStatus.match('失败') ? "red" : "green";
    item.course = ( color == "green" ? "" : ( (item.smx > 0 && item.smy > 0) ? "无区划" : "地址匹配失败" ) );
    var h = '';
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.orderNum + '</td>';
    h += '  <td>' + item.batch + '</td>';
    h += '  <td>' + item.address + '</td>';
    h += '  <td>' + (item.areaName ? item.areaName : "--") + '</td>';
    // h += '  <td>' + item.course + '</td>';
    h += '  <td>' + (item.areaStatus && item.areaStatus == 1 ? "停用" :  (item.areaStatus == 0 ? "正常" : "--") ) + '</td>';
    h += '  <td>' + (item.relation_areaname || '--') + '</td>';
    h += '  <td style="color: '+ color +';">' + Dituhui.Order.Table.getFendanStatusLabel(item) + '</td>';
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
Dituhui.Order.Table.getFendanStatusLabel = function(item) {
    if(item.fendanStatus === "已导入") {
        return item.fendanStatus;
    }
    if(!item.smx || !item.smy) {
        return "分单失败-无坐标";
    }
    if(!item.areaId || !item.areaName) {
        return "分单失败-无区划";
    }
    return "分单成功";
}

Dituhui.Order.Table.bindTrClick = function() {
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
Dituhui.Order.Table.clear = function() {
    $('#table_orders tbody').html('');
    Dituhui.Order.Table.data_success = [];
    Dituhui.Order.Table.data_failed = [];
    $('.a-data-success > span').html('');
    $('.a-data-failed > span').html('');
    $('.data-list .totality').html('');
    $("#data_pager > ul").html('');
    $(".page-marker").html('');
}




