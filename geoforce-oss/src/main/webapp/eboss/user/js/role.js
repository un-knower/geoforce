
/**
 * 角色管理
 */
var Role = {
    data_roles: []
}
/**
 * 查询角色
 */
Role.search = function() {
	Role.data_roles = [];
	var pageNo = Number($("#pager_roles").attr("page"));
    var param = {
        pageNo: pageNo,
        pageSize: pageSize
    };

    var keyword = $("#txt_search_role").val();
    if(keyword.length > 0) {
        param.name = keyword;
    }

    var table = $("#table_roles");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.role.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("当前查询到0条数据");
                    return;
                }
                var items = e.result.items;
                Role.data_roles = items;
                var len = items.length, html = '';

                var per = user.privilegeCodes;
                var per_update = jQuery.inArray("rolemge_update", per);
                var per_update_per = jQuery.inArray("rolemge_update_permission", per);
                var per_remove = jQuery.inArray("rolemge_remove", per);

                for (var i=0; i<len; i++) {
                    var item = items[i];
                    html += '<tr>';
                    html += '   <td>'+ (i+1) +'</td>';
                    html += '   <td>'+ item.name +'</td>';
                    html += '   <td>'+ item.status +'</td>';
                    if( per_update !== -1 ) {
                        html += '   <td><a href="javascript:Role.modalroleupdate('+ i +');">修改</a></td>';
                    }
                    if( per_update_per !== -1 ) {
                        html += '   <td><a href="javascript:Role.modalroleupdateper('+ i +');">更新权限</a></td>';
                    }
                    if( per_remove !== -1 ) {
                        html += '   <td><a href="javascript:Role.modalroledelete('+ i +');">删除</a></td>';
                    }
                    html += '</tr>';
                }
                table.html(html);
                var html_page = setPage(e.result.total, pageNo, '\'pager_roles\''); 
                $("#pager_roles > ul").html(html_page);   
            }
            else {
                showPopover(e.info);
            }
        }
    });
}


/**
 * 重组cookie数据
 */
Role.getFathersFromPermissions = function(products) {
    var fathers = [];
    var len = products.length;
    if(len === 0 ) {
        return fathers;
    }
    for(var i=len; i--; ) {
        var item = products[i];

        if( item.pid === "") {
            item.children = [];
            fathers.push(item);
        }
    }

    var len_fathers = fathers.length;
    if(len_fathers === 0) {
        return fathers;
    }
    for(var j=len; j--; ) {
        var item = products[j];

        for(var k=len_fathers; k--; ) {
            var f = fathers[k];
            if(item.pid === f.id) {
                f.children.push(item);
                fathers[k] = f;
                break;
            }
        }

    }

    return fathers;
}
/**
 * 查询权限
 */
Role.initRolePermissions = function() {
	var param = {
        pageNo: -1
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("暂未成功初始化权限列表");
                    return;
                }
                var items = e.result.items;

                Role.showPermissionsForRoleUpdate(items);
            }
        }
    });
}

Role.showPermissionsForRoleUpdate = function(items){

    var fathers = Role.getFathersFromPermissions(items);
    var len = fathers.length;

    var h = '<input style="margin: 0;" type="checkbox" name="select_all"/>全选<br>';
    for(var i=0; i<len; i++) {
        var item = fathers[i];

        h += '<input type="checkbox" name="pro_parent_'+ item.id +'">';
        h += '<span>' + item.name + '</span>';
        h += "<br>";

        var len_kids = item.children.length;
        for(var k=0; k<len_kids; k++) {
            var kid = item.children[k];
            h += '<input style="margin-left: 30px;" type="checkbox" id="'+ kid.id +'" name="pro_kids_'+ kid.pid +'" >';
            h += '<span>' + kid.name + '</span>';
            h += "<br>";
        }
    }
    $("#div_role_permissions").html(h);

    $('input[name^="pro_parent_"]').change(function() {
        var me = $(this);
        var checked = me.prop("checked");

        var id = me.attr("name").replace("pro_parent_", "");

        var children = $("input[name='pro_kids_"+ id +"']");
        children.prop("checked", checked);
        if(!checked) {
            $('input[name="select_all"]').prop("checked", checked);
        }
    });


    $('input[name="select_all"]').change(function(event) {        
        var me = $(this);
        var checked = me.prop("checked");

        var children = $("input[name^='pro_']");
        children.prop("checked", checked);
        $('input[name="select_all"]').prop("checked", checked);
    });

    $('input[name^="pro_kids_"]').change(function(event) {       
        var me = $(this);
        var id = me.attr("name").replace("pro_kids_", "");
        var checked = me.prop("checked");
        var father = $("input[name='pro_parent_"+ id +"']");
        if(!checked) {
            father.prop("checked", checked);                        
        }
        var selects = $('input[name^="pro_kids_'+ id +'"]:checked');
        if(selects.length === 0) {
            father.prop("checked", checked);
        }
    });

}


/**
 * 验证角色名称输入
 */
Role.verifyRoleName = function() {
	var txt = $('#txt_role_name').val();
	var span = $('#span_tip_role_name');
	if(txt === "" || txt.length === 0) {
		span.html("请输入角色名称");
		return false;
	}

    /*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
    if(!regSERule.test(txt)){
        span.html("不能以百分号或下划线开头结尾");
        return false;
    }*/
    span.html('');
	return true;
}

/**
 * 验证角色权限选择
 */
Role.verifyRolePers = function() {
	var selects = $("input[name^='pro_']:checked");
	if(selects.length === 0) {
		showPopover("请选择权限");
		return false;
	}
	return true;
}

/**
 * 添加角色
 */
Role.add = function() {
	var flag = Role.verifyRoleName();
	if(!flag) {
		return flag;
	}
	flag = Role.verifyRolePers();
	if(!flag) {
		return flag;
	}

	var children = [];

    var parents = $("input[name^='pro_parent_']:checked");
    parents.each(function(){        
        var id = $(this).attr("name").replace('pro_parent_',  '');
        children.push(id);
    });

    var kids = $("input[name^='pro_kids_']:checked");
    kids.each(function(){        
        var id = $(this).attr("name").replace('pro_kids_',  '');
        children.push(id);
    });

	var parameter = {
		name: $('#txt_role_name').val(),
		remarks: $('#txt_role_remarks').val(),
		privileges: children,
        status: $("#txt_role_addstate").val()
	};

    var param = {
        parameter: JSON.stringify( parameter  )
    };

	$.ajax({
        type: 'GET',
        async: true,
        url: urls.role.add,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("添加成功");
                $('#modal_add_role').modal("hide");
                Role.search();
            }
            else {
            	showPopover(e.info);
            }
        }
    });
}

/**
 * 打开修改角色的模态框
 */
Role.modalroleupdate = function(index) {
    var item = Role.data_roles[index];
    $("#txt_role_update_id").val( item.id );
    $('#span_update_role_name').html(item.name);
    $('#txt_role_update_state').val(item.status);
    $('#txt_role_update_remarks').val(item.remarks);

    $('#modal_update_role').modal("show");
}

/**
 * 修改角色的状态和备注
 */
Role.update = function() {
    var param = {
        id: $("#txt_role_update_id").val(),
        status: $("#txt_role_update_state").get(0).selectedIndex,
        remarks: $('#txt_role_update_remarks').val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.role.update,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("修改成功");
                $('#modal_update_role').modal("hide");
                Role.search();
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 打开修改角色的模态框
 */
Role.modalroleupdateper = function(index) {
    var item = Role.data_roles[index];

    $("#txt_role_permission_update_id").val( item.id );
    $('#span_update_role_permission_name').html( item.name );

    var param = {
        pageNo: -1
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.permission.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("暂未成功初始化权限列表");
                    return;
                }
                var items = e.result.items;

                var h = '';

                var fathers = Role.getFathersFromPermissions(items);
                var len = fathers.length;

                var h = '<input style="margin: 0;" type="checkbox" name="select_all"/>全选<br>';
                for(var i=0; i<len; i++) {
                    var item = fathers[i];

                    h += '<input type="checkbox" name="pro_parent_'+ item.id +'"';
                    var bool = Role.verfyHasPer(index, item.id);
                    if(bool) {                        
                        h += ' checked="' + bool + '"';
                    }
                    h += ' ><span>' + item.name + '</span>';
                    h += "<br>";

                    var len_kids = item.children.length;
                    for(var k=0; k<len_kids; k++) {
                        var kid = item.children[k];
                        h += '<input style="margin-left: 30px;" type="checkbox" id="'+ kid.id +'" name="pro_kids_'+ kid.pid +'"';
                        var bool = Role.verfyHasPer(index, item.id);
                        if(bool) {                        
                            h += ' checked="' + bool + '"';
                        }
                        h += ' ><span>' + kid.name + '</span>';
                        h += "<br>";
                    }
                }
                $("#div_role_permissions_update").html(h);

                $('input[name^="pro_parent_"]').change(function() {
                    var me = $(this);
                    var checked = me.prop("checked");

                    var id = me.attr("name").replace("pro_parent_", "");

                    var children = $("input[name='pro_kids_"+ id +"']");
                    children.prop("checked", checked);
                    if(!checked) {
                        $('input[name="select_all"]').prop("checked", checked);
                    }
                });


                $('input[name="select_all"]').change(function(event) {        
                    var me = $(this);
                    var checked = me.prop("checked");

                    var children = $("input[name^='pro_']");
                    children.prop("checked", checked);
                    $('input[name="select_all"]').prop("checked", checked);
                });

                $('input[name^="pro_kids_"]').change(function(event) {       
                    var me = $(this);
                    var id = me.attr("name").replace("pro_kids_", "");
                    var checked = me.prop("checked");
                    var father = $("input[name='pro_parent_"+ id +"']");
                    if(!checked) {
                        father.prop("checked", checked);                        
                    }
                    var selects = $('input[name^="pro_kids_'+ id +'"]:checked');
                    if(selects.length === 0) {
                        father.prop("checked", checked);
                    }
                });

                $('#modal_update_role_permission').modal("show");
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 更新角色的权限
 */
Role.updateRolePers = function() {
    var me = $("#txt_role_permission_update_id").val();

    var pers = [];
    
    var parents = $("input[name^='pro_parent_']:checked");
    parents.each(function(){        
        var id = $(this).attr("name").replace('pro_parent_',  '');
        pers.push(id);
    });

    var kids = $("input[name^='pro_kids_']:checked");
    kids.each(function(){        
        var id = $(this).attr("id");
        pers.push(id);
    });


    var parameter = {
        parameter: JSON.stringify( 
            {
                id: me,
                privileges: pers
            } 
        ) 
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.role.updateper,
        data: parameter,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("更新成功");
                $('#modal_update_role_permission').modal("hide");    
                Role.search();           
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 打开删除角色的模态框
 */
Role.verfyHasPer = function(index, id) {
    var ms = Role.data_roles[index].privileges;
    var len = ms.length;
    if(len === 0) {
        return false;
    }
    for( var k=len; k--; ) {
        var m = ms[k];
        if(m.id === id) {
            return true;
        }
    }
    return false;
}

/**
 * 打开删除角色的模态框
 */
Role.modalroledelete = function(index) {
    var item = Role.data_roles[index];
    $("#txt_role_delete_id").val(item.id);
    $('#span_delete_role_name').html("确实要删除角色\"" + item.name +"\"吗？删除后不可恢复");
    $('#modal_delete_role').modal("show");
}

/**
 * 删除角色
 */
Role.remove = function() {
    var param = {
        id: $("#txt_role_delete_id").val()
    };

    $.ajax({
        type: 'GET',
        async: true,
        url: urls.role.remove,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("删除成功");
                $('#modal_delete_role').modal("hide");
                Role.search();
            }
            else {
                showPopover(e.info);
            }
        }
    });
}