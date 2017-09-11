
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp
 */
SuperMap.Egisp.Zone = SuperMap.Egisp.Zone || {};

/** 
 * 边界样式
 */
SuperMap.Egisp.Zone.BoundryStyle = {
    fill: false,
    strokeColor: "#ff6666",
    strokeWidth: 3
}

/** 
 * 区划样式
 */
SuperMap.Egisp.Zone.getRegionStyle = function(){    
    var style = {
        fill: true,
        fillColor: "#b0fff3",
        fillOpacity: 0.2,   
        strokeColor: "#0090ff",
        strokeOpacity: 0.8,
        strokeWidth: 2,
        fontColor: "#006cff",
        fontOpacity: "1",
        fontFamily: "microsoft yahei",
        fontSize: "14px",
        fontWeight: "bold",
        fontStyle: "normal",
        labelSelect: "true",
        cursor: "pointer"
    }
    return style;
}
/** 
 * 区划文字样式
 */
SuperMap.Egisp.Zone.getRegionTextStyle = function(){    
    var style = {
        fontColor: "#fff",
        fontWeight: "normal",
        fontSize: "14px",
        fontFamily: "microsoft yahei",
        fill: true,
        fillColor: "#006cff",
        fillOpacity: 1,
        stroke: true,
        strokeColor:"#8B7B8B"
    }
    return style;
}
/** 
 * 区划选中样式
 */
SuperMap.Egisp.Zone.getRegionSelectStyle = function(){    
    var style = {
        fill: true,
        fillColor: "#ff4568",
        fillOpacity: 0.4,   
        strokeColor: "#0090ff",
        strokeOpacity: 0.8,
        strokeWidth: 2,
        fontColor: "#006cff",
        fontOpacity: "1",
        cursor: "pointer"
    }
    return style;
}

/** 
 * 解析区域面数据，生成geometry
 */
SuperMap.Egisp.Zone.DrawRegion = function(parts, point2Ds) {
    var length_parts = ( parts.length == 0 ? 1 : parts.length) ;
    var length_point2Ds = point2Ds.length;

    //取点的索引
    var idxPoint = 0;
    var count = 0;

    var regions = [];

    for(var k=0; k < length_parts; k++) {
        //每个小多边形的点数
        var pntCount = parts[k];
        
        //得到区片
        var points = [];
        var index=-1;
        for (var j=idxPoint; j < idxPoint + pntCount; j++) {
            var item = point2Ds[j];
            var pp = new SuperMap.Geometry.Point( Number(item.x), Number(item.y) );
            for (var l=0; l < points.length; l++) {
                if (j == idxPoint + pntCount - 1 && l != 0 && points[l].x == pp.x && points[l].y == pp.y) {
                    if (points[l].x != points[0].x && points[l].y != points[0].y) {
                        index=l;
                    }
                }
            }
            points.push(pp);
        }
        
        if (index != -1) {
            regions.push( new SuperMap.Geometry.LinearRing( points.splice(index) ) );
        }
        idxPoint = idxPoint + pntCount;
        
        regions.push( new SuperMap.Geometry.LinearRing( points ) ); 
    }
    var georegion = new SuperMap.Geometry.Polygon(regions);    
    return georegion;
}

/**
 * 查询
 */
SuperMap.Egisp.Zone.search = function(param, success, failed) {
    if( !param || !param.admincode || !param.level) {
        return;
    }    
    SuperMap.Egisp.request({
        url: urls.server + "/areaService/queryAllAreaByLevel?",
        data: param,
        success: function(e){
            if(e.isSuccess && e.result) {
                var re = [];
                if( e.result.length === 0 ) {
                    success(re);
                }
                else {
                    success( e.result );
                }
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();            
        }
    });
} 
/**
 * 根据ID查询区划
 */
SuperMap.Egisp.Zone.searchById = function(id, success, failed) {
    if( !id ) {
        return;
    }    
    SuperMap.Egisp.request({
        url: urls.server + "/areaService/queryArea?",
        data: {id: id},
        success: function(e){
            if(e.isSuccess && e.result) {
                success( e.result );                
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();            
        }
    });
}


/**
 * 获取区划归属
 */
SuperMap.Egisp.Zone.getAdminnameByCode = function(code, success, failed) {     
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/getAdminByCode?",
        data: {admincode: code},
        success: function(e){
            if(e.isSuccess && e.result && e.result.PROVINCE) {
                var adminname = e.result.PROVINCE;
                adminname += ( code.substr(2, 2) !== "00" && e.result.CITY) ? e.result.CITY : "";
                adminname += ( code.substr(4, 2) !== "00" && e.result.COUNTY) ? e.result.COUNTY : "";
                success(adminname);
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();            
        }
    });
} 

/**
 * 转换区划绑定的网点
 */
SuperMap.Egisp.Zone.getBranchName = function(pointnames) {
    var len = pointnames ? pointnames.length : 0;
    var branch_name = "";
    if(len > 0){
        for(var k=0; k<len; k++) {
            branch_name += pointnames[k];
            if(k < (len-1)){
                branch_name += ",";
            }
        }
    }
    return branch_name;
}

/**
 * 根据区划属性获取订单信息窗中的内容
 */
SuperMap.Egisp.Zone.getAttrPopupHtml = function(me) {
    var h =  '<div class="map-popup" style="min-height: 100px;">';
        h += '  <div class="title">';
        h += '      <span class="name">'+ me.name +'</span>';
        h += '      <a class="popup-close" title="关闭"></a>';
        h += '      <a class="popup-edit" title="编辑属性"></a>';
        h += '  </div>';
        h += '  <div class="content">';
        h += '      <div class="attr">';
        h += '          <span class="label">区划名称：</span>';
        h += '          <span class="text" >'+ SuperMap.Egisp.setStringEsc(me.name) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划编号：</span>';
        h += '          <span class="text" >'+ SuperMap.Egisp.setStringEsc(me.areaNumber) +'</span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">区划归属：</span>';
        h += '          <span class="text region-adminname"></span>';
        h += '      </div>';
        h += '      <div class="attr">';
        h += '          <span class="label">绑定网点：</span>';
        h += '          <span class="text">'+ me.branch_name +'</span>';
        h += '          ';
        h += '      </div>';
        h += '  </div>';
        h += '</div>';
    return h;
}
/**
 * 删除
 */
SuperMap.Egisp.Zone.remove = function(id, success, failed) {
    if( !id) {
        return;
    }    
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.server + "/areaService/deleteArea?",
        data: {id: id},
        dataType: 'json',
        success: function(e){
            if(e.isSuccess) {
                success();
            }
            else {
                failed(e.info ? e.info : "");
            }
        },
        error: function(){
            failed();            
        }
    });
}





/**
 * 表格
 */
SuperMap.Egisp.Zone.Table = {};
SuperMap.Egisp.Zone.Table.refresh = function(data) {  
    $('.data-table tbody').html('');
    $('.data-list .totality').html('');

    var len = data.length;
    if( len < 1 ) {
        return;
    }
    var data_success = [], data_failed = [];
    var tbody = $('.data-table tbody').html(''), h = '';
    for( var i=0; i<len; i++ ) {
        var item = data[i];
        item.order = (i+1);
        h += SuperMap.Egisp.Zone.Table.getContentTd(item);
    }
    tbody.html(h);    
    $('.data-list .totality').html( '('+ len +'条)' );

    SuperMap.Egisp.Zone.Table.bindTrClick();
}
SuperMap.Egisp.Zone.Table.getContentTd = function( item ) {  
    item.branch_name = SuperMap.Egisp.Zone.getBranchName(item.pointnames); 
    var h = '';
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.name + '</td>';
    h += '  <td>' + item.areaNumber + '</td>';
    h += '  <td>' + item.branch_name + '</td>';    
    h += '  <td>' + item.create_time + '</td>';
    h += '  <td>' + item.update_time + '</td>';
    h += '  <td>';
    h += '      <a class="td-option" href="javascript:void(0)" >修改属性</a>';
    h += '      <i class="td-separator"></i>';
    h += '      <a class="td-option" href="javascript:void(0)" >删除</a>';
    h += '  </td>';
    h += '</tr>';
    return h;
}
SuperMap.Egisp.Zone.Table.bindTrClick = function() {
    $('.data-table tbody tr').unbind('click').click(function(){
        $('.data-table tbody tr').removeClass('action');
        var me = $(this);
        me.addClass('action');
        var feature = Map.getFeatureFromLayerById(me.attr('data-id'));
        if( feature ) {    
            Map.clearSelectedFeatures();    
            Map.regionSelect(feature);
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
SuperMap.Egisp.Zone.Table.clear = function() {
    $('.data-table tbody').html('');
    $('.data-list .totality').html('');
}

/**
 * 表格自动定位到指定feature
 */
SuperMap.Egisp.Zone.Table.scrollToFeature = function(feature) {            
    $('.data-list table tbody tr').removeClass('action');
    var tr = $('.data-list table tbody tr[data-id="'+ feature.attributes.id +'"]').addClass('action');
    var data_table = $('.data-list .data-results');
    var offset_top = tr[0].offsetTop, scroll_top = data_table[0].scrollTop;
    if( Math.abs( offset_top - scroll_top ) > data_table.height() || offset_top < scroll_top ) {
        data_table.animate({scrollTop: (offset_top)}, 100);
    }
}