
/** 
 * 网点列表，数据表格类
 */
SuperMap.Egisp.Point.Table = {};
SuperMap.Egisp.Point.Table.data_success = [];
SuperMap.Egisp.Point.Table.data_failed = [];
SuperMap.Egisp.Point.Table.refresh = function(data) {    
    SuperMap.Egisp.Point.National.hide();
    var len = data ? data.length : 0;
    if( len < 1 ) {
        SuperMap.Egisp.Point.Table.clear();
        return;
    }
    // data.reverse();  
    var tbody = $('#table_branches tbody').html(''); 
    var h = '', order = 1;
    for(var i=0; i<len; i++ ) {
        var item = data[i];
        item.order = order;
        h += '<tr data-id="'+ item.id +'">';
        h += SuperMap.Egisp.Point.Table.getContentTd( item );
        h += '</tr>';
        order++;
        // SuperMap.Egisp.Point.remove( item.id, function(){}, function(){} )
    }
    // return;
    tbody.html(h);
    $('.data-list .totality').html( '('+ len +'条)' );
    SuperMap.Egisp.Point.Table.bindTdClick();
}

/** 
 * 网点列表，创建tbody 中的 td
 */
SuperMap.Egisp.Point.Table.getContentTd = function( item ) {
    var keys = SuperMap.Egisp.Point.Columns.concat();
    var disable_keys = SuperMap.Egisp.Point.unEditableColumns.concat();
    var len_keys = keys.length;
    var h = '';

    for( var i=0; i<len_keys; i++ ) {
        var key = keys[i];
        var value = item[ keys[i] ];
        value = value ? SuperMap.Egisp.setStringEsc(value + '') : "";

        var data_value = value;
        if(key == 'group') {
            data_value = item.groupid ? item.groupid.id : '';
        }

        h += '<td data-id="'+ item.id +'" data-key="'+ key +'" data-value="'+ data_value +'"';
        
        if( $.inArray(key, disable_keys) == -1 ) {
            h += ' data-editable="true" title="双击可编辑单元格" ';
        }
        var canBeEmpty = SuperMap.Egisp.Point.Table.getTdEmpty( key );
        if( canBeEmpty ) {
            h += ' data-empty="true"';
        }
        h += '>';

        if( item.status == 0 && (key == 'smx' ||key == 'smy' ) ) {
            var coord = SuperMap.Egisp.metersToLatLon( new SuperMap.LonLat(item.smx, item.smy) )
            h += ( key == 'smx' ? coord.lon.toFixed(2) : coord.lat.toFixed(2) ) ;
        }
        else if(key == 'group') {
            h += item.groupid ? item.groupid.groupname : '无';
        }
        else {
            h += value;
        }
        
        h += '</td>';
    }
    return h;
}

/** 
 * 网点列表，根据字段判断单元格是否可以修改
 */
SuperMap.Egisp.Point.Table.getTdEditable = function( key ) {
    var disable_keys = SuperMap.Egisp.Point.unEditableColumns.concat();
    var index = disable_keys.indexOf( key );
    if(index != -1) {
        return false;
    }
    return true;
}
/** 
 * 网点列表，根据字段判断单元格是否可以为空
 */
SuperMap.Egisp.Point.Table.getTdEmpty = function( myKey ) {
    var keys = SuperMap.Egisp.Point.sysColumns;
    var key;
    for(key in keys) {
        if(key === myKey) {
            return false;
        }
    }
    return true;
}

/** 
 * 网点列表，绑定修改单元格事件
 */
SuperMap.Egisp.Point.Table.bindTdClick = function() {
    $('#table_branches tbody td[data-editable]').unbind('dblclick').dblclick(function(){
        var me = $(this);
        var left = me.offset().left;
        var top = me.offset().top;

        var bodyWidth = SuperMap.Egisp.getWindowWidth();
        var bodyHeight = SuperMap.Egisp.getWindowHeight();

        var cell_top;
        if( bodyHeight - top - me.height() > 160 ) {
            cell_top = top + me.height() + 20;
        }
        else {
            cell_top = top - 160; 
        }

        var cell_left = left;
        if( bodyWidth - left < 250 ) {
            cell_left = bodyWidth - 250;
        }
        if(me.attr('data-key') != 'group') {
            var input = $('.text-cell-edit').val( me.attr('data-value') ).attr({
                "data-id": me.attr("data-id"),
                "data-key": me.attr("data-key"),
                "data-value": me.attr("data-value")
            });
            if( me.attr('data-empty') ) {
                input.attr('data-empty', 'true');
            }
            else {
                input.removeAttr('data-empty');
            }

            $('.data-cell-edit .box').css({ left: cell_left + "px", top: cell_top + "px" });
            $('.data-cell-edit').fadeIn('fast');
        }
        else {
            var box = $('.data-cell-edit-group');
            box.find('.box').css({ left: cell_left + "px", top: cell_top + "px" });
            var val = me.attr('data-value') ? me.attr('data-value') : 'none';
            box.find('select').val(val).attr({
                'data-poiid': me.attr('data-id')
            });
            box.fadeIn('fast');
        }
        
    });
    
    $('#table_branches tbody tr').unbind('click').click(function(){
        $('#table_branches tbody tr').removeClass('action');
        var me = $(this);
        me.addClass('action');
        var marker = Map.getMarkerById( me.attr("data-id") );
        layer_edit_branch.removeAllFeatures();
        Map.endDragBranch();
        if( marker && marker.attributes.smx && marker.attributes.smx != 0 ) {
            var lonlat = new SuperMap.LonLat( marker.attributes.smx, marker.attributes.smy );
            marker.attributes.lonlat = lonlat;
            Map.openBranchAttrPopup(marker.attributes);
            if( !layer_branches.visibility ) {
                layer_edit_branch.addFeatures(marker);
            }
            var zoom = map.getZoom();
            map.setCenter( lonlat, zoom > 10 ? zoom : 10);
        }
        else {
            marker = SuperMap.Egisp.Point.getMarkerFromFailed( me.attr("data-id") );
            marker.lonlat = map.getCenter();
            marker.failed = true;
            Map.showEditBranch(marker);

            // map.setCenter( lonlat = map.getCenter(), zoom > 10 ? zoom : 10);
        }
    });
}
SuperMap.Egisp.Point.getMarkerFromFailed = function(id){
    var items = SuperMap.Egisp.Point.Table.data_failed.concat();
    for( var i=items.length; i--; ) {
        var item = items[i];
        if( item.id === id ) {
            return item;
        }
    }
    return null;
}
/** 
 * 网点列表，更新单元格
 */
SuperMap.Egisp.Point.Table.updateCell = function() {
    var input = $('.text-cell-edit');
    var val = input.val();
    if( val.length < 1 && !input.attr('data-empty') ) {
        SuperMap.Egisp.showPopover("请输入要更新的信息");
        return;
    }
    if( !input.attr('data-empty') && val === input.attr('data-value') ) {
        return;
    }
    // val = SuperMap.Egisp.setStringEsc(val);
    SuperMap.Egisp.showMask();
    var param = {
        cellvalue: val,
        cellkey: input.attr("data-key"),
        id: input.attr("data-id")
    };
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/updatePointByCell?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if( e && e.isSuccess ) {
                // val = SuperMap.Egisp.setStringEsc(val);
                $('td[data-id="'+ param.id +'"][data-key="'+ param.cellkey +'"]').html(SuperMap.Egisp.setStringEsc(val)).attr('data-value', val);

                var feature = Map.getMarkerById(param.id);

                if(feature) {
                    feature.attributes[param.cellkey] = val;

                    var marker = feature.attributes;
                    if( marker.smx && marker.smy && marker.smx > 0 ) {
                        marker.lonlat = new SuperMap.LonLat(marker.smx, marker.smy);
                        Map.openBranchAttrPopup(marker);
                    }  
                    else {
                        $('input[name="'+ param.cellkey +'"]').val( val );
                    }                 
                }
                else {
                    $('span[data-key="'+ param.cellkey +'"][option="popup-attr"]').html(val);
                }
                $('.data-cell-edit').fadeOut('fast');          
                SuperMap.Egisp.showPopover("修改成功");
            }
            else {
                var info = e.info ? e.info : "修改失败";
                SuperMap.Egisp.showPopover(info);  
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            SuperMap.Egisp.showPopover("修改失败");           
        }
    });
}
/** 
 * 网点列表，更新分组
 */
SuperMap.Egisp.Point.Table.updateCellGroup = function() {
    var select = $('.data-cell-edit-group select');
    var poiid = select.attr('data-poiid');
    var styleid = select.val();
    if(!styleid || styleid == 'add-new' || styleid == 'none') {
        styleid = '';
    }
    var param = {
        cellvalue: styleid,
        cellkey: 'groupid',
        id: poiid
    };
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/updatePointByCell?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if( e && e.isSuccess ) {
                // var val = select.find("option:selected").text();
                // $('td[data-id="'+ param.id +'"][data-key="group"]').html(val).attr('data-value', param.id);
                $('.data-cell-edit-group').fadeOut('fast');          
                SuperMap.Egisp.showPopover("修改成功");
                Map.searchBranches();
            }
            else {
                var info = e.info ? e.info : "修改失败";
                SuperMap.Egisp.showHint(info);  
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            SuperMap.Egisp.showHint("修改失败");           
        }
    });
}

/** 
 * 网点列表，新增列
 */
SuperMap.Egisp.Point.Table.addColumn = function() {
    var value = $('.text-column-add').val();
    if( value.length < 1 ) {
        SuperMap.Egisp.showPopover("请输入列名称");
        return;
    }
    // value = SuperMap.Egisp.setStringEsc(value);

    SuperMap.Egisp.showMask();
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/getUserPointExtcol?",
        success: function(data){
            if( data && data.isSuccess ) {
                if( data.result ) {                    
                    var len = 0, key, again=false, keys=data.result;
                    for( key in keys ) {
                        if( value === keys[key] ) {
                            again = true;   
                        }
                        len++;
                    }
                    if(len >= 10) {
                        SuperMap.Egisp.showPopover("只能新增10个自定义列");
                        SuperMap.Egisp.hideMask();
                        return;
                    }

                    keys = SuperMap.Egisp.Point.sysColumns;
                    for( key in keys ) {
                        if( value === keys[key] ) {
                            again = true;        
                        }          
                    }

                    if(again) {
                        SuperMap.Egisp.showPopover("列名称重复");
                        SuperMap.Egisp.hideMask();
                        return;      
                    } 
                }
                var param = {
                    defaultcoldesc: value
                };
                $.ajax({
                    type: 'GET',
                    async: true,
                    url: urls.server + "/pointService/addPointExtcol?",
                    data: param,
                    dataType: 'json',
                    success: function(e){
                        SuperMap.Egisp.hideMask();
                        if( e && e.isSuccess && e.result ) {
                            SuperMap.Egisp.showPopover("新增列成功");
                            var col = {};
                            col[e.result.colkey] = e.result.colvalue;

                            SuperMap.Egisp.Point.Table.addColumnToTable( col );
                            $('.data-column-add').fadeOut('fast');
                        }
                        else {
                            SuperMap.Egisp.showPopover(e.info ? e.info : "新增列失败");                
                        }
                    },
                    error: function(){
                        SuperMap.Egisp.hideMask();
                        SuperMap.Egisp.showPopover("新增列失败");                       
                    }
                });
            }
            else {
                SuperMap.Egisp.hideMask();
                SuperMap.Egisp.showPopover(data.info ? data.info : "新增列失败");  
            }
        },
        error: function() {
            SuperMap.Egisp.hideMask();
            SuperMap.Egisp.showPopover(e.info ? e.info : "新增列失败");   
        }
    });
}
/** 
 * 表格中新增列
 */
SuperMap.Egisp.Point.Table.addColumnToTable = function(col) {
    var h = '';
    var key, keys = [];
    for( key in col ) {    
        h += '<td class="td-150" id="td_'+ key +'">';
        h += SuperMap.Egisp.Point.getCustomHeadDropMenu( key, col[key] );
        h += '</td>';
        keys.push( key );
    }
    $('#td_smx').before( h );


    $('.data-table tbody td[data-key="smx"]').each(function(){  
        var me = $(this);
        var id = me.attr('data-id');      
        var td = '<td data-id="'+ id +'" data-key="'+ key +'" data-value="" data-editable="true" title="双击可编辑单元格"></td>';
        me.before( td );
    });

    SuperMap.Egisp.Point.insertColsToColumns( keys ); 
    SuperMap.Egisp.Point.Table.bindTdClick();
}
/** 
 * 删除列
 */
SuperMap.Egisp.Point.Table.removeColumn = function() {
    SuperMap.Egisp.showMask();
    var span = $(this);
    var param = {
        cols: span.attr('data-key'),
        coldesc: ''
    }
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/updateOrdeletePointExtcol?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if( e && e.isSuccess ) {
                SuperMap.Egisp.Modal.hide();
                SuperMap.Egisp.Point.Table.removeColumnToTable( param.cols );
                SuperMap.Egisp.Point.Columns.removeItem( param.cols );

                delete SuperMap.Egisp.Point.CustomColumns[param.cols];
                SuperMap.Egisp.showPopover("删除列成功");   
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();  
            SuperMap.Egisp.showPopover("删除列失败");                       
        }
    });
}
/** 
 * 重命名列
 */
SuperMap.Egisp.Point.Table.renameColumn = function() {
    var input = $('.text-column-rename');
    var text = input.val();
    if( text.length < 1 ) {
        SuperMap.Egisp.showPopover('请输入列名称');
        return;
    }
    if( text === input.attr('data-value') ) {
        return;        
    }
    // text = SuperMap.Egisp.setStringEsc(text);
    SuperMap.Egisp.showMask();

    SuperMap.Egisp.request({
        url: urls.server + "/pointService/getUserPointExtcol?",
        success: function(data){
            if( data && data.result ) {
                var len = 0, key, keys = data.result;
                for( key in keys ) {
                    if( text === keys[key] ) {
                        SuperMap.Egisp.showPopover("列名称重复");
                        SuperMap.Egisp.hideMask();
                        return;                        
                    }
                }
                keys = SuperMap.Egisp.Point.sysColumns;
                for( key in keys ) {
                    if( text === keys[key] ) {
                        SuperMap.Egisp.showPopover("列名称重复");
                        SuperMap.Egisp.hideMask();
                        return;                        
                    }                    
                }

                var param = {
                    cols: input.attr('data-key'),
                    coldesc: text
                }
                SuperMap.Egisp.request({
                    url: urls.server + "/pointService/updateOrdeletePointExtcol?",
                    data: param,
                    success: function(e){
                        SuperMap.Egisp.hideMask();
                        if( e && e.isSuccess ) {
                            $('.data-column-rename').fadeOut('fast');
                            $('#a_head_' + param.cols).html( SuperMap.Egisp.setStringEsc(text) + '<span class="caret"></span>');
                            $('a[option="rename-column"][data-key="'+ param.cols +'"]').attr('data-value', text);
                            $('a[option="remove-column"][data-key="'+ param.cols +'"]').attr('data-value', text);
                            SuperMap.Egisp.showPopover("列重命名成功");   
                        }
                        else {                
                            SuperMap.Egisp.showPopover("列重命名失败");   
                        }
                    },
                    error: function(){
                        SuperMap.Egisp.hideMask();
                        SuperMap.Egisp.showPopover("列重命名失败");                          
                    }
                });
            }
        },
        error: function(){}
    });
}

/** 
 * 将列从表格中移除
 * @param - key 字段key
 */
SuperMap.Egisp.Point.Table.removeColumnToTable = function( key ) {
    $('#td_' + key).remove();
    $('#table_branches tbody td[data-key="'+ key +'"]').remove();
}

/** 
 * 网点列表，清空
 */
SuperMap.Egisp.Point.Table.clear = function() {
    $('#table_branches tbody').html('');
    $('.data-list .totality').html('');
    SuperMap.Egisp.Point.Table.data_success = [];
    SuperMap.Egisp.Point.Table.data_failed = [];
    $('.a-data-success > span, a-data-failed > span').html('');
}

/** 
 * 全国范围下的查询显示
 */
SuperMap.Egisp.Point.National = {};
/** 
 * 显示全国范围下的表格
 */
SuperMap.Egisp.Point.National.show = function() {
    $('.table-branches-detail').hide();
    $('.table-branches-count').show();
}
/** 
 * 隐藏全国范围下的表格
 */
SuperMap.Egisp.Point.National.hide = function() {
    $('.table-branches-count').hide();
    $('.table-branches-detail').show();
}
/** 
 * 全国范围下的网点
 */
SuperMap.Egisp.Point.National.clear = function() {
    $('#table_branches_count tbody').html('');
    $('.data-list .totality').html('');
}
/** 
 * 全国范围下的网点聚合显示
 */
SuperMap.Egisp.Point.National.display = function(data) {
    SuperMap.Egisp.Point.National.show();
    var table = $('#table_branches_count tbody').html('');

    if(!data || data.length == 0) {
        SuperMap.Egisp.showPopover('当前查询到0条网点数据');
        return;
    }
    var len = data.length, h='', total = 0, pois = [];
    for(var i=0; i<len; i++) {
        var item = data[i], cname='';
        if(!item.admincode || item.admincode == '' || !item.province || item.province == '') {
            item.admincode = ''
            item.province = "不明确";
            cname = ' class="error"';
        }
        h += '<tr '+ cname +' data-admincode="'+ item.admincode +'" data-adminname="'+ item.province +'">';
        h += '  <td>' + (i+1) + '</td>';
        h += '  <td>' + item.province + '</td>';
        h += '  <td>' + item.count + '</td>';
        h += '</tr>';
        var style = SuperMap.Egisp.Point.getNationalBranchStyle();
        style.label = item.count;
        style.graphicTitle = item.province;
        if(typeof Baidu == 'Object' && Baidu.using) {
            var coord = Baidu.getCoord(item.x, item.y);
            item.x = coord.x;
            item.y = coord.y;
        }
        var geo_point = new SuperMap.Geometry.Point(item.x, item.y);   
        item.type = "point-national";         
        var poi = new SuperMap.Feature.Vector(geo_point, item, style);
        pois.push(poi);
        total += Number(item.count);
    }
    table.html(h);
    $('.data-list .totality').html('(' + total + '条)');
    layer_branches.removeAllFeatures();
    layer_branches.addFeatures(pois);
    $('#table_branches_count tbody tr:not(.error)').unbind('click').click(function(){
        var me = $(this);
        SuperMap.Egisp.SMCity.showCurrentProvince(me.attr('data-adminname'), me.attr('data-admincode'));
        SuperMap.Egisp.SMCity.cityTagClick();
    });
    $('#table_branches_count tbody tr.error').unbind('click').click(function(){
        SuperMap.Egisp.Point.SearchFailedBranches();
    });
}
