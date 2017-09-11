/**
 * 权限管理
 */
var Permission = {
    data_permissions: []
}


/**
 * 验证权限名称
 */
Permission.verifyPermissionName = function(){
    var txt = $('#txt_permission_name').val();
    var span = $('#span_tip_permission_name');
    if(txt.length === 0 || txt === "") {
        span.html("请输入权限名称");
        return false;
    }
    /*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
    if(!regSERule.test(txt)){
        span.html("不能以百分号或下划线开头结尾");
        return false;
    }*/
    span.html("");
    return true;
}

/**
 * 验证权限代码
 */
Permission.verifyPermissionCode = function(){
    var txt = $('#txt_permission_code').val();
    var span = $('#span_tip_permission_code');
    if(txt.length === 0 || txt === "") {
        span.html("请输入权限代码");
        return false;
    }
    /*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
    if(!regSERule.test(txt)){
        span.html("不能以百分号或下划线开头结尾");
        return false;
    }*/
    span.html("");
    return true;
}

/**
 * 验证权限url
 */
Permission.verifyPermissionUrl = function(){
    var txt = $('#txt_permission_url').val();
    var span = $('#span_tip_permission_url');
    if(txt.length === 0 || txt === "") {
        span.html("请输入权限url");
        return false;
    }
    /*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
    if(!regSERule.test(txt)){
        span.html("不能以百分号或下划线开头结尾");
        return false;
    }*/
    span.html("");
    return true;
}


/**
 * 添加权限
 */
Permission.add = function(){
	var flag = Permission.verifyPermissionName();
	if(!flag) {
		return false;
	}
	flag = Permission.verifyPermissionCode();
	if(!flag) {
		return false;
	}
    flag = Permission.verifyPermissionUrl();
    if(!flag) {
        return false;
    }

	var param = {
		name: $('#txt_permission_name').val(),
		code: $('#txt_permission_code').val(),
		remarks: $('#txt_permission_remarks').val(),
		pid: $('#txt_permission_pid').val(),
        url: $('#txt_permission_url').val(),
        status: $('#txt_permission_addstate').val()
	}

	$.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.add,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
                showPopover("权限添加成功");
                $('#modal_add_permission').modal('hide');
                Permission.search();
            }
            else {              
                showPopover(e.info);
            }
        }
    });
}

/**
 * 查询权限
 */
Permission.search = function(){
	Permission.data_permissions = [];
    var pageNo = Number($("#pager_permissions").attr("page"));
    var param = {
        pageNo: pageNo,
        pageSize: pageSize
    };

    var level = $("#select_search_per_status").val();
    if(level !== "-1") {
        param.level = level;
    }
    var keyword = $("#txt_search_per").val();
    if(keyword.length > 0) {
        param.name = keyword;
    }

    var table = $("#table_permissions");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("当前查询到0条数据");
                    return;
                }
                var items = e.result.items;
                Permission.data_permissions = items;
                var len = items.length, html = '';

                var per = user.privilegeCodes;
                var per_update = jQuery.inArray("permissionmge_update", per);
                var per_remove = jQuery.inArray("permissionmge_remove", per);

                for (var i=0; i<len; i++) {
                    var item = items[i];
                    
                    html += '<tr>';
                    html += '   <td>'+ (i+1) +'</td>';
                    html += '   <td>'+ item.pid +'</td>';
                    html += '   <td>'+ item.name +'</td>';
                    html += '   <td>'+ item.status +'</td>';
                    html += '   <td>'+ item.level +'</td>';
                    html += '   <td>'+ item.code +'</td>';
                    html += '   <td>'+ item.url +'</td>';
                    html += '   <td>'+ (item.remarks ? item.remarks : "无") +'</td>';
                    if(item.id < 81 || item.code === "productmge_detail") {
                        if( per_update !== -1 ) {
                            html += '   <td><a href="javascript:;"class="btn btn-default btn-sm disabled" style="padding:2px;font-size:10px;" role="button">修改</a></td>';
                        }
                        if( per_remove !== -1) {
                            html += '   <td><a href="javascript:;"class="btn btn-default btn-sm disabled" style="padding:2px;font-size:10px;"  role="button">删除</a></td>';
                        }
                        continue;
                    }
                    if( per_update !== -1 ) {
                        html += '   <td><a href="javascript:Permission.modalperupdate('+ i +');">修改</a></td>';
                    }
                    if( per_remove !== -1) {
                        html += '   <td><a href="javascript:Permission.modalperdelete('+ i +');">删除</a></td>';
                    }
                    html += '</tr>';
                }
                table.html(html);
                var html_page = setPage(e.result.total, pageNo, '\'pager_permissions\''); 
                $("#pager_permissions > ul").html(html_page);   
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 查询PID
 */
Permission.initPermissionPid = function() {
	$('#txt_permission_name').val('');
	$('#txt_permission_code').val('');

	var param = {
        pageNo: -1,
        level: 1
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                var html = '<option value="">顶级权限</option>';
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    $("#txt_permission_pid").html(html);
                    showPopover("暂未成功初始化PID");
                    return;
                }
                var items = e.result.items;
                var len = items.length;
                for(var i=0; i<len; i++) {
                	var item = items[i];
                	html += '<option value="'+ item.id +'">' + item.name + '</option>';
                }
                $("#txt_permission_pid").html(html);
            }
            else {
                var html = '<option value="">顶级权限</option>';
                $("#txt_permission_pid").html(html);
            }
        }
    });
}

/**
 * 显示更新权限的模态框
 */
Permission.modalperupdate = function(index) {
	var item = Permission.data_permissions[index];
	var i = map_status_permission.get( item.status );
	$("#txt_permission_state").val(i);
	$("#txt_permission_update_id").val(item.id);
	$("#txt_permission_update_remarks").val( item.remarks );

	$("#modal_update_permission").modal( 'show' );
}

/**
 * 更新权限
 */
Permission.update = function() {
	var id = $("#txt_permission_update_id").val();
	var param = {
		id: id,
		status: $("#txt_permission_state").val(),
		remarks: $("#txt_permission_update_remarks").val()
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.update,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
            	showPopover("修改成功");
            	$("#modal_update_permission").modal('hide');
            	Permission.search();
            }
            else {
            	showPopover(e.info);
            }
        }
    });
}
/**
 * 显示删除权限的模态框
 */
Permission.modalperdelete = function(index) {
	var item = Permission.data_permissions[index];

	$("#txt_delete_permission_id").val(item.id);
	$('#span_delete_permission').html("确定要删除权限\"" + item.name + "\"吗？删除后不可恢复。");

	$("#modal_delete_permission").modal( 'show' );
}

/**
 * 删除权限
 */
Permission.remove = function() {
	var id = $("#txt_delete_permission_id").val();
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.remove,
        data: {id: id},
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
            	showPopover("删除成功");
            	$("#modal_delete_permission").modal('hide');
            	Permission.search();
            }
            else {
            	showPopover(e.info);
            }
        }
    });
}


