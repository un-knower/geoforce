
/**
 * 表格
 */
Dituhui.Zone.Table = {};
Dituhui.Zone.Table.init = function(){
	
}
Dituhui.Zone.Table.refresh = function(data) {  
    $('.data-table tbody').html('');
    $('.data-list .totality').html('').attr("data-value", 0);

    var len = data.length;
    if( len < 1 ) {
        return;
    }
    var data_success = [], data_failed = [];
    var tbody = $('.data-table tbody').html(''), h = '';
    for( var i=0; i<len; i++ ) {    	
        var item = data[i];
        /*
        Dituhui.Zone.remove(item.id);
        */        
        item.order = (i+1);
        h += Dituhui.Zone.Table.getContentTd(item);
    }
    tbody.html(h);    
    $('.data-list .totality').html( '('+ len +'条)' ).attr("data-value", len);
}
Dituhui.Zone.Table.getContentTd = function( item ) {  
    // item.branch_name = Dituhui.Zone.getBranchName(item.pointnames); 
    item.branch_name = item.pointnames; 
    item.username = (item.username && item.username != null) ? item.username : "";
    var h = '';
    h += '<tr data-id="'+ item.id +'">';
    h += '  <td>' + item.order + '</td>';
    h += '  <td>' + item.name + '</td>';
    h += '  <td>' + item.areaNumber + '</td>';
    
    
     
    if(item.style_status !== 2.5) {
    	h += '  <td class="td-update-area-status" title="双击可更改"' 
	      +  '	data-name="'+ item.name +'" data-initstatus="'+ item.area_status +'">'
	      + (item.area_status==0 ? "正常" : (item.area_status==1?"停用":"超区") ) + '</td>';
    }
    else {
    	h += '  <td>'+ (item.area_status==0 ? "正常" : "停用"  ) +'</td>';
    }
      
    h += '  <td>' + item.relation_areaname + '</td>';
    h += '  <td>' + item.branch_name + '</td>';
    
    if(item.style_status !== 2.5) {
    	h += '  <td class="td-update-user-belonging" title="双击可更改">'+ item.username +'</td>';
    }
    else {
    	h += '  <td>'+ item.username +'</td>';
    }
    if(Dituhui.User.regionTableExtend) {
    	h += '  <td>' + item.wgzCode + '</td>';
    	h += '  <td>' + item.wgzName + '</td>';
    	h += '  <td>' + item.lineCode + '</td>';
    	h += '  <td>' + item.lineName + '</td>';
    }
    h += '  <td>' + item.create_time + '</td>';
    h += '  <td>' + item.update_time + '</td>';
    h += '  <td>';
    if(item.style_status !== 2.5) {
	    h += '      <a class="td-option" option="edit-attr" href="javascript:void(0)" >修改属性</a>';
	    h += '      <i class="td-separator"></i>';
	    h += '      <a class="td-option" option="delete" href="javascript:void(0)" >删除</a>';
    }
    h += '  </td>';
    h += '</tr>';
    return h;
}

Dituhui.Zone.Table.initTrClick = function() {
	//点击行
    $('.data-table tbody').on('click', "tr", function(){
    	var flag = Map.checkAction();
    	if(!flag) {
    		return flag;
    	}
        $('.data-table tbody tr').removeClass('action');
        var me = $(this);
        me.addClass('action');
        var feature = Map.getFeatureFromLayerById(me.attr('data-id'));
        if( feature ) {
            Map.clearSelectedFeatures();    
            Map.regionSelect(feature);
            map.zoomToExtent(feature.geometry.getBounds());
        }
        else {
            if(Map.popup != null) {
                Map.popup.hide();
            }
        }
    });
    //点击表格中的修改属性
    $('.data-table tbody').on("click", 'a[option="edit-attr"]', function(){
    	var flag = Map.checkAction();
    	if(!flag) {
    		return flag;
    	}
        var me = $(this);
        var feature = Map.getFeatureFromLayerById(me.parents("tr").attr('data-id'));
        setTimeout(function(){
            Map.openEditRegionPopup(feature.attributes, false);
        }, 200);
    });
    //点击表格中的删除
    $('.data-table tbody').on("click", 'a[option="delete"]', function(){
    	var flag = Map.checkAction();
    	if(!flag) {
    		return flag;
    	}
        setTimeout(function(){
            Map.deleteRegionClick();
        }, 200);
    });
    //双击击表格中的子账号可更改
    $('.data-table tbody').on("dblclick", 'td.td-update-user-belonging', function(){
    	var flag = Map.checkAction();
    	if(!flag) {
    		return flag;
    	}
    	var areaid = $(this).parents("tr").attr('data-id');
    	$.fn.zTree.init($("#tree_usebelonging"), ztree_setting);
    	$(".span-selected-username").html("");
    	$(".data-user-belonging").show();
    });
    //双击击表格中的区划状态可更改
    $('.data-table tbody').on("dblclick", 'td.td-update-area-status', function(){
    	var flag = Map.checkAction();
    	if(!flag) {
    		return flag;
    	}
    	var areaid = $(this).parents("tr").attr('data-id');
    	$('.area-name-for-status').html($(this).attr("data-name"));
    	$('.btn-set-areastatus').attr("data-id", areaid);
    	$('.select-area-status').val($(this).attr("data-initstatus"));
    	$(".data-area-status").fadeIn("fast");
    });
    //修改区划状态
    $('.btn-set-areastatus').on("click", function(){
    	var me = $(this);
    	Dituhui.Zone.updateStatus({
    		id: me.attr("data-id"),
    		areastatus: $('.select-area-status').val()
    	}, function(json){
    		$(".data-area-status").fadeOut("fast");
    		if(json.isSuccess) {
    			Dituhui.showPopover("修改区划状态成功");
    			Map.search();
    		}
    		else {
    			Dituhui.showHint( json.info || "修改区划状态失败");
    		}
    	}, function(){
    		$(".data-area-status").fadeOut("fast");
    		Dituhui.showHint("修改区划状态失败");
    	})
    })
}

/**
 * 清空表格
 */
Dituhui.Zone.Table.clear = function() {
    $('.data-table tbody').html('');
    $('.data-list .totality').html('').attr("data-value", 0);
}

/**
 * 表格自动定位到指定feature
 */
Dituhui.Zone.Table.scrollToFeature = function(feature) {            
    $('.data-list table tbody tr').removeClass('action');
    var tr = $('.data-list table tbody tr[data-id="'+ feature.attributes.id +'"]').addClass('action');
    var data_table = $('.data-list .data-results');
    var offset_top = tr[0].offsetTop, scroll_top = data_table[0].scrollTop;
    if( Math.abs( offset_top - scroll_top ) > data_table.height() || offset_top < scroll_top ) {
        data_table.animate({scrollTop: (offset_top)}, 100);
    }
}