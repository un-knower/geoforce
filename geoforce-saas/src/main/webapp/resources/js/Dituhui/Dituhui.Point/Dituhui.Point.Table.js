/** 
 * 网点列表，数据表格类
 */
Dituhui.Point.Table = {};
Dituhui.Point.Table.data_success = [];
Dituhui.Point.Table.data_failed = [];
Dituhui.Point.Table.refresh = function(data) {
    Dituhui.Point.National.hide();
    var len = data ? data.length : 0;
    if( len < 1 ) {
        $('#table_branches tbody').html('');
	    $('.data-list .totality').html('');
//	    Dituhui.Point.Table.data_success = [];
//	    Dituhui.Point.Table.data_failed = [];
	    $('.a-data-success > span, a-data-failed > span').html('');
        return;
    }
    // data.reverse();  
    var tbody = $('#table_branches tbody').html(''); 
    var h = '', order = 1;

    for(var i=0; i<len; i++ ) {
        var item = data[i];
        item.order = order;
        h += '<tr data-id="'+ item.id +'">';
        h += Dituhui.Point.Table.getContentTd( item );
        h += '</tr>';
        order++;
//        Dituhui.Point.remove( item.id, function(){}, function(){} )
    }
//    return;
    tbody.html(h);
    $('.data-list .totality').html( '('+ len +'条)' );
//  Dituhui.Point.Table.bindTdClick();
}

/** 
 * 网点列表，创建tbody 中的 td
 */
Dituhui.Point.Table.getContentTd = function( item ) {
    var keys = Dituhui.Point.Columns.concat();
    var disable_keys = Dituhui.Point.unEditableColumns.concat();
    var len_keys = keys.length;
    var h = '';
    var customs = Dituhui.Point.CustomColumns;
    for( var i=0; i<len_keys; i++ ) {
        var key = keys[i];
        var value = item[ keys[i] ];
        value = value ? Dituhui.setStringEsc(value + '') : "";

        var data_value = value;
        if(key == 'group') {
            data_value = item.groupid ? item.groupid.id : '';
        }

        var styleName = Dituhui.Point.getTableTdStyleName( key ); 
        h += '<td class="'+ styleName +'" data-id="'+ item.id +'" data-key="'+ key +'" data-value="'+ data_value +'"';
        
        if( $.inArray(key, disable_keys) == -1 && !item.belongToOthers ) {
            h += ' data-editable="true" title="双击可编辑单元格" ';
        }
        var canBeEmpty = Dituhui.Point.Table.getTdEmpty( key );
        if( canBeEmpty ) {
            h += ' data-empty="true"';
        }
        h += '>';

        if( key == 'smx' ||key == 'smy'  ) {
            if( item.status == 0 ) {
                var coord = Dituhui.metersToLatLon( new SuperMap.LonLat(item.smx, item.smy) );
                h += ( key == 'smx' ? coord.lon.toFixed(2) : coord.lat.toFixed(2) ) ;                
            }
            else {
                h += '';
            }
        }
        else if(key == 'group') {
            h += item.groupid ? item.groupid.groupname : '无';
        }
        else if( customs[key] == "链接") {
            var href = item[ keys[i] ];
            if( href && href.length>0 && !/^http:\/\//.test(href) && !/^https:\/\//.test(href) ) {            
                href = "http://" + value;
            } 
            h += '<a href="'+href+'" target="_blank">'+value+'</a>';
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
Dituhui.Point.Table.getTdEditable = function( key ) {
    var disable_keys = Dituhui.Point.unEditableColumns.concat();
    var index = disable_keys.indexOf( key );
    if(index != -1) {
        return false;
    }
    return true;
}
/** 
 * 网点列表，根据字段判断单元格是否可以为空
 */
Dituhui.Point.Table.getTdEmpty = function( myKey ) {
    var keys = Dituhui.Point.sysColumns;
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
Dituhui.Point.Table.bindTdClick = function() {
    //单元格修改
    $('#table_branches tbody').on("dblclick", "td[data-editable]", function(){
        var me = $(this);
        var left = me.offset().left - $('.sidebar').width() - 10;
        var top = me.offset().top - 55;

        var bodyWidth = Dituhui.getWindowWidth();
        var bodyHeight = Dituhui.getWindowHeight();

        var cell_top;
        if( (bodyHeight - top - me.height()) > 230 ) {
            cell_top = top + me.height() + 20;
        }
        else {
            cell_top = top - 110; 
        }

        var cell_left = left;
        if( bodyWidth - left < 410 ) {
            cell_left = bodyWidth - 410;
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
            var val = me.attr('data-value') ? me.attr('data-value') : '0';
            box.find('select').val(val).attr({
                'data-poiid': me.attr('data-id')
            });
            box.fadeIn('fast');
        }   
    });
    
    //单击行
    $('#table_branches tbody').on("click", "tr", function(){
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
                var geometry = new SuperMap.Geometry.Point(marker.attributes.smx, marker.attributes.smy);
                var newMarker = new SuperMap.Feature.Vector(geometry, marker.attributes, marker.style);
                layer_edit_branch.addFeatures(newMarker);
            }
            var zoom = map.getZoom();
            map.setCenter( lonlat, zoom > 10 ? zoom : 10);
        }
        else {
            marker = Dituhui.Point.getMarkerFromFailed( me.attr("data-id") );
            marker.lonlat = map.getCenter();
            marker.failed = true;

            $('#txt_upload_point_picture_pointid').val( me.attr("data-id") );
            Map.Pictures.search();
            Map.showEditBranch(marker);

            // map.setCenter( lonlat = map.getCenter(), zoom > 10 ? zoom : 10);
        }
    });
    
    $('#table_branches_count tbody ').on("click", "tr:not(.error)", function(){
        var me = $(this), level = me.attr('data-level');
        var name = me.attr('data-adminname');
        var code = me.attr('data-admincode');
        $('.smcity').attr({
        	smx: me.attr('data-smx'),
        	smy: me.attr('data-smy')
        });
        var count = Number(me.attr('data-count'));
        if(level == 1) {
        	var iszhixia = Dituhui.SMCity.isZhixia( code.substring(0, 2) + '0000' );
        	if(iszhixia) {
        		Dituhui.SMCity.showCurrentCounty(name, code, '');        		
	        	/*if(count >= 500 && $('#btn_showCluster').prop('checked')) {
	        		Map.showBranchesClusterByCounty(count);
	        	}
	        	else {
	        		Dituhui.SMCity.cityTagClick();
	        	}*/
        	}
        	else {
        		Dituhui.SMCity.showCurrentCity(name, code);
//      		Dituhui.SMCity.cityTagClick();
        	}
        }
        else if(level == 2) {
        	Dituhui.SMCity.showCurrentCounty(name, code, '');
//      	Dituhui.SMCity.cityTagClick();
        }
        else if(level == 3) {
        	Dituhui.SMCity.cityTagClick(false);
        	return;
        }
        else {
        	Dituhui.SMCity.showCurrentProvince(name, code);
//      	Dituhui.SMCity.cityTagClick();
        }
        Dituhui.SMCity.cityTagClick();
    });
    $('#table_branches_count tbody ').on("click", "tr.error", function(){
        Dituhui.Point.SearchFailedBranches();
    });
    
    //绑定添加列的事件
    $('#table_branches_head').on("click",'a[option="add-column"]', function(){
        $('.data-column-add').fadeIn('fast');
        $('.text-column-add').val("");
    });
    
    //绑定删除列的事件
    $('#table_branches_head').on("click",'a[option="remove-column"]', function(){
        var me = $(this);
        Dituhui.Modal.alert("确定删除列 \"" + me.attr('data-value') + "\"吗？数据删除后不可恢复。", 
            Dituhui.Point.Table.removeColumn, 
            {
                'data-value': me.attr('data-value'),
                'data-key': me.attr('data-key')
            }
        );
    });
    
    //绑定新建分组的事件
    $('#table_branches_head').on("click",'a[option="add-new-group"]', function(){
    	if(Dituhui.Point.Group.data.length == 10) {
			Dituhui.showHint('最多只可创建10个分组，有更多需求请联系商务');
		}
    	else {
    		$('.data-branchgroup-add').fadeIn('fast');
        	$('.text-branchgroup-add').val("");
    	}
        return;
    });
    //绑定删除分组的事件
    $('#table_branches_head').on("click",'a[option="remove-the-group"]', function(){
    	var data = Dituhui.Point.Group.data;
		if(data.length > 0) {
			for(var i=data.length, hg=''; i--; ) {
		        var item = data[i];
		        hg += '<option value="'+ item.id +'">'
		        + Dituhui.setStringEsc(item.groupname) +'</option>';
		    }
			$('.select-groups-toremove').html(hg);
			$('.data-branchgroup-remove').fadeIn('fast');
		}
		else {
			Dituhui.showHint("当前没有网点分组数据");
		}
    });

    $('#table_branches_head').on("click", 'a[option="rename-column"]', Page.renameColumn);
}
Dituhui.Point.getMarkerFromFailed = function(id){
    var items = Dituhui.Point.Table.data_failed.concat();
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
Dituhui.Point.Table.updateCell = function() {
    var input = $('.text-cell-edit');
    var val = input.val();
    if( val.length < 1 && !input.attr('data-empty') ) {
        Dituhui.showPopover("请输入要更新的信息");
        return;
    }
    if( !input.attr('data-empty') && val === input.attr('data-value') ) {
        return;
    }
    // val = Dituhui.setStringEsc(val);

    Dituhui.showMask();
    var customs = Dituhui.Point.CustomColumns;
    var param = {
        cellvalue: val,
        cellkey: input.attr("data-key"),
        id: input.attr("data-id")
    };
    Dituhui.request({
        url: urls.server + "/pointService/updatePointByCell?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if( e && e.isSuccess ) {
                // val = Dituhui.setStringEsc(val);
                var href = val;
                if(customs[param.cellkey] == "链接" && href && href.length>0 ) {
                    if(!/^http:\/\//.test(href) && !/^https:\/\//.test(href)) {
                        href = "http://" + href;
                    }
                    $('td[data-id="'+ param.id +'"][data-key="'+ param.cellkey +'"]').html(
                        '<a href="'+ (href=="" ? "javascript:;" : href) +'" target="_blank">'+val+'</a>'
                    );
                }
                else {
                    $('td[data-id="'+ param.id +'"][data-key="'+ param.cellkey +'"]').html(Dituhui.setStringEsc(val)).attr('data-value', val);
                }

                

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
                    var href = val;
                    if(customs[param.cellkey] == "链接" && href && href.length>0  ) {
                        if(!/^http:\/\//.test(href) && !/^https:\/\//.test(href)) {
                            href = "http://" + href;
                        }
                        $('span[data-key="'+ param.cellkey +'"][option="popup-attr"]').html(
                            '<a href="'+ (href=="" ? "javascript:;" : href) +'" target="_blank">'+val+'</a>'
                        );
                    }
                    else {
                        $('span[data-key="'+ param.cellkey +'"][option="popup-attr"]').html(val);
                    }
                    
                }
                $('.data-cell-edit').fadeOut('fast');          
                Dituhui.showPopover("修改成功");
            }
            else {
                var info = e.info ? e.info : "修改失败";
                Dituhui.showPopover(info);  
            }
        },
        error: function(){
            Dituhui.hideMask();
            Dituhui.showPopover("修改失败");           
        }
    });
}
/** 
 * 网点列表，更新分组
 */
Dituhui.Point.Table.updateCellGroup = function() {
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
    Dituhui.request({
        url: urls.server + "/pointService/updatePointByCell?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if( e && e.isSuccess ) {
                // var val = select.find("option:selected").text();
                // $('td[data-id="'+ param.id +'"][data-key="group"]').html(val).attr('data-value', param.id);
                $('.data-cell-edit-group').fadeOut('fast');          
                Dituhui.showPopover("修改成功");
                Map.searchBranches();
            }
            else {
                var info = e.info ? e.info : "修改失败";
                Dituhui.showHint(info);  
            }
        },
        error: function(){
            Dituhui.hideMask();
            Dituhui.showHint("修改失败");           
        }
    });
}

/** 
 * 网点列表，新增列
 */
Dituhui.Point.Table.addColumn = function() {
    var value = $('.text-column-add').val();
    if( value.length < 1 ) {
        Dituhui.showPopover("请输入列名称");
        return;
    }
    // value = Dituhui.setStringEsc(value);

    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/getUserPointExtcol?",
        success: function(data){
            if( data && data.isSuccess ) {
                if( data.result ) {                    
                    var len = 0, key, again=false, keys=data.result;
                    for( key in keys ) {
                        if( value === keys[key] ) {
                            again = true;   
                        }
                        if(key !== "configcols"){
                        	len++;
                        }
                    }
                    if(len >= 10) {
                        Dituhui.showPopover("只能新增10个自定义列");
                        Dituhui.hideMask();
                        return;
                    }

                    keys = Dituhui.Point.sysColumns;
                    for( key in keys ) {
                        if( value === keys[key] ) {
                            again = true;        
                        }
                    }

                    if(again) {
                        Dituhui.showPopover("列名称重复");
                        Dituhui.hideMask();
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
                        Dituhui.hideMask();
                        if( e && e.isSuccess && e.result ) {
                            Dituhui.showPopover("新增列成功");
                            var col = {};
                            col[e.result.colkey] = e.result.colvalue;

                            Dituhui.Point.CustomColumns[e.result.colkey] = e.result.colvalue;
                            Dituhui.Point.initSettingFilds();

                            Dituhui.Point.Table.addColumnToTable( col );
                            $('.data-column-add').fadeOut('fast');
                        }
                        else {
                            Dituhui.showPopover(e.info ? e.info : "新增列失败");                
                        }
                    },
                    error: function(){
                        Dituhui.hideMask();
                        Dituhui.showPopover("新增列失败");                       
                    }
                });
            }
            else {
                Dituhui.hideMask();
                Dituhui.showPopover(data.info ? data.info : "新增列失败");  
            }
        },
        error: function() {
            Dituhui.hideMask();
            Dituhui.showPopover(e.info ? e.info : "新增列失败");   
        }
    });
}
/** 
 * 表格中新增列
 */
Dituhui.Point.Table.addColumnToTable = function(col) {
    var h = '';
    var key, keys = [];
    for( key in col ) {    
        h += '<td class="td-150" id="td_'+ key +'">';
        h += Dituhui.Point.getCustomHeadDropMenu( key, col[key] );
        h += '</td>';
        keys.push( key );
    }
    $('#td_smx').before( h );

    $('.data-table tbody td[data-key="smx"]').each(function(){  
        var me = $(this);
        var id = me.attr('data-id'); 
        var td = '<td class="td-150" data-id="'+ id +'" data-key="'+ key +'" data-value="" data-editable="true" title="双击可编辑单元格" data-empty="true"></td>';
        me.before( td );
    });

    Dituhui.Point.insertColsToColumns( keys ); 
    Dituhui.Point.Table.bindTdClick();
}
/** 
 * 删除列
 */
Dituhui.Point.Table.removeColumn = function() {
    Dituhui.showMask();
    var span = $(this);
    var param = {
        cols: span.attr('data-key'),
        coldesc: ''
    }
    Dituhui.request({
        url: urls.server + "/pointService/updateOrdeletePointExtcol?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if( e && e.isSuccess ) {
                Dituhui.Modal.hide();
                Dituhui.Point.Table.removeColumnToTable( param.cols );
                Dituhui.Point.Columns.removeItem( param.cols );

                delete Dituhui.Point.CustomColumns[param.cols];

                var index = Dituhui.Point.ConfigCols.indexOf(param.cols);
                Dituhui.Point.ConfigCols.splice(index, 1);
                Dituhui.showPopover("删除列成功");  

                Dituhui.Point.initSettingFilds(); 
                $('.popup-row' + param.cols).remove();
            }
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.showPopover("删除列失败");                       
        }
    });
}
/** 
 * 重命名列
 */
Dituhui.Point.Table.renameColumn = function() {
    var input = $('.text-column-rename');
    var text = input.val();
    if( text.length < 1 ) {
        Dituhui.showPopover('请输入列名称');
        return;
    }
    if( text === input.attr('data-value') ) {
        return;        
    }
    // text = Dituhui.setStringEsc(text);
    Dituhui.showMask();

    Dituhui.request({
        url: urls.server + "/pointService/getUserPointExtcol?",
        success: function(data){
            if( data && data.result ) {
                var len = 0, key, keys = data.result;
                for( key in keys ) {
                    if( text === keys[key] ) {
                        Dituhui.showPopover("列名称重复");
                        Dituhui.hideMask();
                        return;                        
                    }
                }
                keys = Dituhui.Point.sysColumns;
                for( key in keys ) {
                    if( text === keys[key] ) {
                        Dituhui.showPopover("列名称重复");
                        Dituhui.hideMask();
                        return;                        
                    }                    
                }

                var param = {
                    cols: input.attr('data-key'),
                    coldesc: text
                }
                Dituhui.request({
                    url: urls.server + "/pointService/updateOrdeletePointExtcol?",
                    data: param,
                    success: function(e){
                        Dituhui.hideMask();
                        if( e && e.isSuccess ) {
                            $('.data-column-rename').fadeOut('fast');
                            $('#a_head_' + param.cols).html( Dituhui.setStringEsc(text) + '<span class="caret"></span>');
                            $('a[option="rename-column"][data-key="'+ param.cols +'"]').attr('data-value', text);
                            $('a[option="remove-column"][data-key="'+ param.cols +'"]').attr('data-value', text);
                            Dituhui.showPopover("列重命名成功");  

                            Dituhui.Point.CustomColumns[param.cols] = text;
                            Dituhui.Point.initSettingFilds();
                            $('.popup-row' + param.cols + ' .label').html(text.substr(0,4) + '：');
                        }
                        else {                
                            Dituhui.showPopover("列重命名失败");   
                        }
                    },
                    error: function(){
                        Dituhui.hideMask();
                        Dituhui.showPopover("列重命名失败");                          
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
Dituhui.Point.Table.removeColumnToTable = function( key ) {
    $('#td_' + key).remove();
    $('#table_branches tbody td[data-key="'+ key +'"]').remove();

    var table_width = $('#table_branches_head').width();
    $('#table_branches').width(table_width );

    var div = $('.data-results.head-fixed');
    var scroll_left = div.scrollLeft();
    var left = scroll_left;
    if( (scroll_left + div.width()) > table_width ) {
        left = table_width - div.width();
    }

    div.animate({scrollLeft: left}, 1);
}

/** 
 * 网点列表，清空
 */
Dituhui.Point.Table.clear = function() {
    $('#table_branches tbody').html('');
    $('.data-list .totality').html('');
    Dituhui.Point.Table.data_success = [];
    Dituhui.Point.Table.data_failed = [];
    $('.a-data-success > span, a-data-failed > span').html('');
}

/** 
 * 全国范围下的查询显示
 */
Dituhui.Point.National = {};
/** 
 * 显示全国范围下的表格
 */
Dituhui.Point.National.show = function() {
    $('.table-branches-detail').hide();
    $('.table-branches-count').show();
}
/** 
 * 隐藏全国范围下的表格
 */
Dituhui.Point.National.hide = function() {
    $('.table-branches-count').hide();
    $('.table-branches-detail').show();
}
/** 
 * 全国范围下的网点
 */
Dituhui.Point.National.clear = function() {
    $('#table_branches_count tbody').html('');
    $('.data-list .totality').html('');
}
/** 
 * 全国范围下的网点聚合显示
 */
Dituhui.Point.National.display = function(e) {
	var data = [];
	if(e.records && e.records != null) {
		data = e.records;
	}
	
    Dituhui.Point.National.show();
    var table = $('#table_branches_count tbody').html('');

    if(!data || data.length == 0) {
    	Dituhui.Point.National.clear();
        Dituhui.showPopover('当前查询到0条网点数据');
        return;
    }
    var len = data.length, h='', total = 0, pois = [];
    for(var i=0; i<len; i++) {
        var item = data[i], cname='';
        item.adminname = item.province ? item.province : (item.city ? item.city : item.county);
        if(!item.admincode || item.admincode == '' || !item.adminname || item.adminname == '') {
            item.admincode = '';
            item.adminname = "不明确";
            cname = ' class="error"';
        }
        h += '<tr '+ cname +' data-admincode="'+ item.admincode +'" data-adminname="'+ item.adminname +'" data-level="'+ e.level +'" data-smx="'+ item.x +'" data-smy="'+ item.y +'" data-count="'+ item.count +'">';
        h += '  <td>' + (i+1) + '</td>';
        h += '  <td>' + item.adminname + '</td>';
        h += '  <td>' + item.count + '</td>';
        h += '</tr>';
        
        var style = Dituhui.Point.getNationalBranchStyle(item.count);
        style.label = item.count;
        style.graphicTitle = item.province;
        if(typeof Baidu == 'Object' && Baidu.using) {
            var coord = Baidu.getCoord(item.x, item.y);
            item.x = coord.x;
            item.y = coord.y;
        }
        var geo_point = new SuperMap.Geometry.Point(item.x, item.y);   
        item.type = "point-national";  
        if(item.countyCluster || e.level == 3) {
        	item.type = "point-county"; 
        }
        item.level = e.level;
        var poi = new SuperMap.Feature.Vector(geo_point, item, style);
        pois.push(poi);
        total += Number(item.count);
    }
    table.html(h);
    $('.data-list .totality').html('(' + total + '条)');
    layer_branches.removeAllFeatures();
    layer_branches.addFeatures(pois);
   
}
