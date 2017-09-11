/**
 * 员工管理
 */
var Employee = {
    /**
     * 保存已经查询的用户
     */
    data_employees: []
};


/**
 * 验证员工名称
 */
Employee.verifyEmployeeName = function(){
    var txt = $('#txt_employee_name').val();
    var span = $('#span_tip_employee_name');
    var regUserName = /^(?!_)(?!.*?_$)[a-zA-Z]+[a-zA-Z0-9_]*$/;

	var reg = /^[a-zA-Z0-9\_]{4,20}$/;	
    var regBlank = /^\w$|(^ )|( $)/;

	if(txt == "") {
		span.html("请输入用户名");
		return false;
	}
	else if(txt == "null") {
		span.html("用户名不能为\"null\"");
		return false;
	}
	else if(txt.length < 4 || txt.length > 20) {
		span.html("用户名的长度要求大于4并且小于20");
		return false;
	}
	else if( !reg.test(txt) ) {
		span.html("用户名不能包含特殊字符");
		return false;
	}
	else if( regBlank.test(txt) || txt.indexOf(" ") > 0 ){
		span.html("用户名不能包空格");
		return false;
	}
    /*else if( !regUserName.test(txt) ) {
        span.html("用户名只能包含英文、数字、下划线，不能以数字或下划线开头，不能以下划线结尾");
        return false;
    }*/

	span.html("");
	return true;
}

/**
 * 验证密码
 */
Employee.verifyEmployeePwd = function(){
    var txt = $('#txt_employee_password').val();
    var span = $('#span_tip_employee_password');

	var regNull = /^\s*$/;
	var psdVali = $("#txt_employee_passwordsure").val();
	var spanVali = $("#span_tip_employee_passwordsure");
    if(txt == "") {
		span.html("请输入密码");
		return false;
	}
	else if (txt.length < 6 || txt.length > 20) {
        span.html("密码长度必须大于6并小于20");
        return false;
    } 
    else if(regNull.test(txt) || txt.indexOf(" ")>=0){
        span.html("密码中不能包含空格");
        return false;
    }
    else if(psdVali !== "" && psdVali !== txt){
		spanVali.html("两次密码输入不一致");
		return false;
	}
    span.html("");
	return true; 
}

/**
 * 添加员工--验证输入的再次确认密码
 * 验证输入的密码
 */
Employee.verifyEmployeePasswordSure = function() {	
	var txt = $("#txt_employee_passwordsure").val();
	var pwd = $("#txt_employee_password").val();
	var span = $("#span_tip_employee_passwordsure");

	if(txt == "") {
		span.html("请再次确认密码");
		return false;
	}
	if(txt !== pwd) {        		
		span.html("两次密码输入不一致");
		return false;
	}
	span.html("");
	return true;
}

/**
 * 添加员工--验证邮箱
 */
Employee.verifyEmployeeEmail = function() {	
	var txt = $("#txt_employee_email").val();
	var span = $("#span_tip_employee_email");

	var reg = /[a-zA-Z0-9]+@\w+[\.][a-z]{2,3}$|[a-zA-Z0-9]+@\w+[\.][a-z]{2,3}[\.][a-z]{2,3}$/;
	 		
	if(txt == "") {
	 	span.html('请输入邮箱');
	 	return false;
	}
	if(reg.test(txt) && txt.split("@").length == 2) {	 			
	 	span.html("");
	 	return true;
	}
 	span.html('邮箱格式不正确');
 	return false;
}

/**
 * 添加员工--验证职务
 */
Employee.verifyEmployeeJob = function() {	
	var txt = $("#txt_employee_job").val();
	var span = $("#span_tip_employee_job");
	 		
	if(txt === "" ||　txt.length === 0) {
	 	span.html('请输入员工职务');
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
 * 添加用户--验证真实姓名
 */
Employee.verifyEmployeeRealname = function() {	
	var txt = $("#txt_employee_realname").val();
	var span = $("#span_tip_employee_realname");
	
	var reg = /^(?!\s)([\u4E00-\u9FA0]+$|^[a-z·\s]+)$/i;
	if(txt == "") {
		span.html("真实姓名不能为空");
		return false;
	}
	if(!reg.test(txt)){
		span.html("真实姓名只能为中文或者英文");
		return false;
	}
	if(txt.length<2 || txt.length>20){
		span.html("真实姓名长度必须不小于2并且不大于20");
		return false;
	}
	span.html("");
	return true;
}

/**
 * 添加用户-验证手机号
 */
Employee.verifyEmployeeMobile = function(){
	var txt = $("#txt_employee_mobile").val();
	var span = $("#span_tip_employee_mobile");

	if(txt == "") { 				
		span.html("请填写手机号码");
		return false;
	}
	var reg = /1[3,5,6,8]\d{9}$/;
	if(reg.test(txt)) {		 			
	 	span.html('');
	 	return true; 				
	}
 	span.html('手机号码只能以13、15、16或18开头，长度为11位');
 	return false;
}

/**
 * 添加员工-验证输入
 */
Employee.verifyAddEmployeeInput = function() {
	var flag = Employee.verifyEmployeeName();
	if(!flag) {
		return flag;
	}
	flag = Employee.verifyEmployeePwd();
	if(!flag) {
		return flag;
	}
	flag = Employee.verifyEmployeePasswordSure();
	if(!flag) {
		return flag;
	}
	flag = Employee.verifyEmployeeRealname();
	if(!flag) {
		return flag;
	}
	flag = Employee.verifyEmployeeEmail();
	if(!flag) {
		return flag;
	}
	flag = Employee.verifyEmployeeMobile();	
	if(!flag) {
		return flag;
	}
	flag = Employee.verifyEmployeeJob();
	return flag;
}

/**
 * 添加员工
 */
Employee.add = function() {
	var flag = Employee.verifyAddEmployeeInput();	
	if(!flag) {
		return flag;
	}

	var gender = $("[name='employee_sex']").filter(":checked").val();
	var param = {
		username: $('#txt_employee_name').val(),
		password: Base64.encode( $('#txt_employee_passwordsure').val() ),
		realName: $('#txt_employee_realname').val(),
		email: $('#txt_employee_email').val(),
		position: $('#txt_employee_job').val(),
		sex: gender,
		mobilePhone: $('#txt_employee_mobile').val(),
		phone: $('#txt_employee_phone').val(),
		department: $('#txt_employee_department').val()
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.add,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e && e.success) {
        		showPopover("用户添加成功");
        		$('#modal_add_employee').modal('hide');
        		Employee.search();
        	}
        	else {        		
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 重置添加员工的文本框
 */
Employee.resetEmployeeAddModal = function() {
    $('#txt_employee_name').val("");
    $('#span_tip_employee_name').html("");
    $('#txt_employee_password').val("");
    $('#span_tip_employee_password').html("");
    $('#txt_employee_passwordsure').val("");
    $('#span_tip_employee_passwordsure').html("");
    $('#txt_employee_realname').val("");
    $('#span_tip_employee_realname').html("");
    $('#txt_employee_email').val("");
    $('#span_tip_employee_email').html("");
    $('#txt_employee_job').val("");
    $('#span_tip_employee_job').html("");
    $('#txt_employee_mobile').val("");
    $('#span_tip_employee_mobile').html("");
    $('#txt_employee_phone').val("");
    $('#txt_employee_department').val("");
}

/**
 * 查询员工
 */
Employee.search = function() {
	Employee.data_employees = [];
    var pageNo = Number($("#pager_employees").attr("page"));
    var param = {
        pageNo: pageNo,
        pageSize: pageSize
    };

    var keyword = $("#txt_search_employee").val();
    var status = $("#select_search_employee_status").val();
    if(keyword && keyword.length > 0 ) {
        param.username = keyword;
    } 
    if(status !== "-1") {
        param.status = status;
    }

    var table = $("#table_employees");
    table.html('');
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("当前查询到0条数据");
                    return;
                }
                var items = e.result.items;
                Employee.data_employees = items;
                var len = items.length, html = '';

                var per = user.privilegeCodes;
                var per_detail = jQuery.inArray("employeemge_detail", per);
                var per_update = jQuery.inArray("employeemge_update", per);
                var per_role = jQuery.inArray("employeemge_role", per);
                var per_pwd = jQuery.inArray("employeemge_pwd", per);
                var per_remove = jQuery.inArray("employeemge_remove", per);

                for (var i=0; i<len; i++) {
                    var item = items[i];
                    html += '<tr>';
                    html += '   <td>'+ (i+1) +'</td>';
                    html += '   <td>'+ item.username +'</td>';
                    html += '   <td>'+ item.realName +'</td>';
                    html += '   <td>'+ item.gender +'</td>';
                    html += '   <td>'+ item.email +'</td>';
                    html += '   <td>'+ (item.department ? item.department : "无") +'</td>';
                    html += '   <td>'+ (item.position ? item.position : "无") +'</td>';
                    if( per_detail !== -1 ) {
                        html += '   <td><a href="javascript:Employee.modalemployeedetail('+ i +');">详情</a></td>';
                    }
                    if( per_update  !== -1) {
                        html += '   <td><a href="javascript:Employee.modalemployeeupdate('+ i +');">修改</a></td>';
                    }
                    if( per_role  !== -1) {
                        html += '   <td><a href="javascript:Employee.modalemployeerole('+ i +');">赋角色</a></td>';
                    }
                    if( per_pwd !== -1 ) {
                        html += '   <td><a href="javascript:Employee.modalemployeepassword('+ i +');">重置密码</a></td>';
                    }
                    if( per_remove  !== -1) {
                        html += '   <td><a href="javascript:Employee.modalemployeedelete('+ i +');">删除</a></td>';
                    }
                    html += '</tr>';
                }
                table.html(html);
                var html_page = setPage(e.result.total, pageNo, '\'pager_employees\''); 
                $("#pager_employees > ul").html(html_page);   
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 查询员工详情
 */
Employee.modalemployeedetail = function(index) {
	var id = Employee.data_employees[index].id + "";
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.detail,
        data: { id: id },
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success && e.result) {
            	var r = e.result;

            	$("#span_detail_employee_id").html( r.id );
            	$("#span_detail_employee_username").html( r.username );
            	$("#span_detail_employee_realName").html( r.realName );
            	$("#span_detail_employee_status").html( r.status );
            	$("#span_detail_employee_gender").html( r.gender );
            	$("#span_detail_employee_email").html( r.email );
            	$("#span_detail_employee_department").html( r.department ? r.department : "无" );
            	$("#span_detail_employee_position").html( r.position );
            	$("#span_detail_employee_mobilePhone").html( r.mobilePhone );
            	$("#span_detail_employee_phone").html( r.phone ? r.phone : "无" );
            	var len = r.roleNames ? r.roleNames.length : 0;
            	var h = '';
            	if(len > 0) {
            		for(var i=len; i--; ) {
            			h += r.roleNames[i];
            			if( i !== 0 ) {
            				h += ',';
            			}
            		}
            	}
            	$("#span_detail_employee_roles").html( h ? h : "无" );
            	$("#modal_detail_employee").modal("show");
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 显示修改员工的模态框
 */
Employee.modalemployeeupdate = function(index) {
	var id = Employee.data_employees[index].id;
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.detail,
        data: { id: id },
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success && e.result) {
            	var r = e.result;
            	$("#txt_employee_update_id").val(r.id);
            	$("#span_update_employee_username").html(r.username);
            	$("#txt_employee_update_department").val(r.department);
            	$("#txt_employee_update_job").val(r.position);
            	$("#txt_employee_update_mobile").val(r.mobilePhone);
            	$("#txt_employee_phone").val(r.phone);

            	$("#modal_update_employee").modal("show");
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 修改员工,验证职位输入
 */
Employee.verifyEmployeeJobUpdate = function() {
	var txt = $("#txt_employee_update_job").val();
	var span = $("#span_tip_employee_update_job");

	if(txt === "" ||　txt.length === 0) {
	 	span.html('请输入员工职务');
	 	return false;
	}
 	span.html('');
 	return true;
}

/**
 * 修改员工,验证手机输入
 */
Employee.verifyEmployeeMobileUpdate = function() {
	var txt = $("#txt_employee_update_mobile").val();
	var span = $("#span_tip_employee_update_mobile");

	if(txt == "") { 				
		span.html("请填写手机号码");
		return false;
	}
	var reg = /1[3,5,6,8]\d{9}$/;
	if(reg.test(txt)) {		 			
	 	span.html('');
	 	return true; 				
	}
 	span.html('手机号码只能以13、15、16或18开头，长度为11位');
 	return false;
}

/**
 * 修改员工
 */
Employee.update = function() {
	var flag = Employee.verifyEmployeeJobUpdate();
	if(!flag) {
		return false;
	}
	flag = Employee.verifyEmployeeMobileUpdate();
	if(!flag) {
		return false;
	}
	var param = {
		id: $("#txt_employee_update_id").val(),
		position: $("#txt_employee_update_job").val(),
		mobilePhone: $("#txt_employee_update_mobile").val(),
		phone: $("#txt_employee_update_phone").val(),
		department: $("#txt_employee_update_department").val()
	}
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.update,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
            	showPopover("修改成功");
            	$("#modal_update_employee").modal("hide");
            	Employee.search();
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 显示修改员工角色的模态框
 */
Employee.modalemployeerole = function(index) {
	var item = Employee.data_employees[index];
	$("#txt_employee_id_role").val(item.id);
	$("#span_employee_name_role").html(item.username);
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.role.search,
        data: { pageNo: -1 },
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success && e.result) {
            	if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("暂未成功初始化角色列表");
                    return;
                }
                var items = e.result.items;
                var len = items.length;
                var html = '';
                for(var i=0; i<len; i++) {
                    var item = items[i];
                    html += '<input type="checkbox" name="update_employee_role" id="checkbox_employee_roles_'+ item.id +'"';
                    var bool = Employee.verfyHasRole(index, item.id);
                    if(bool) {                        
                        html += ' checked="' + bool + '"';
                    }
                    html += '><span>' + item.name + '</span>';
                    if( (i+1) % 2 === 0 ) {
                        html += "<br>";
                    }
                }
                $("#div_role_employee_update").html(html);

            	$("#modal_employee_role").modal("show");
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
Employee.verfyHasRole = function(index, id) {
    var ms = Employee.data_employees[index].roleIds;
    var len = ms.length;
    if(len === 0) {
        return false;
    }
    for( var k=len; k--; ) {
        var m = ms[k];
        if(m === id) {
            return true;
        }
    }
    return false;
}

/**
 * 更新员工角色
 */
Employee.updateEmployeeRoles = function() {
	var selects = $('input[name="update_employee_role"]:checked');

	var me = $('#txt_employee_id_role').val();
	var roles = [];
	selects.each(function(){		
        var id = $(this).prop("id").replace("checkbox_employee_roles_", "");
        roles.push(id);
	});
	var param = {
        parameter: JSON.stringify( 
            {
                id: me,
                roles: roles
            } 
        ) 
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.updaterole,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("更新成功");
                $('#modal_employee_role').modal("hide");    
                Employee.search();            
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 更新员工角色
 */
Employee.modalemployeepassword = function(index) {
	var item = Employee.data_employees[index];
	$("#txt_employee_updatepwd_id").val( item.id );
	$("#span_updatepwd_employee_username").html( item.username );

	$("#txt_employee_update_password").val("");
	$("#txt_employee_update_passwordsure").val("");
	$("#span_tip_employee_update_password").html("");
	$("#span_tip_employee_update_passwordsure").html("");

	$("#modal_update_employee_pwd").modal("show");
}


/**
 * 验证密码
 */
Employee.verifyEmployeePwdUpdate = function(){
    var txt = $('#txt_employee_update_password').val();
    var span = $('#span_tip_employee_update_password');

	var regNull = /^\s*$/;
	var psdVali = $("#txt_employee_update_passwordsure").val();
	var spanVali = $("#span_tip_employee_update_passwordsure");
    if(txt == "") {
		span.html("请输入密码");
		return false;
	}
	else if (txt.length < 6 || txt.length > 20) {
        span.html("密码长度必须大于6并小于20");
        return false;
    } 
    else if(regNull.test(txt) || txt.indexOf(" ")>=0){
        span.html("密码中不能包含空格");
        return false;
    }
    else if(psdVali !== "" && psdVali !== txt){
		spanVali.html("两次密码输入不一致");
		return false;
	}
    span.html("");
	return true; 
}

/**
 * 添加员工--验证输入的再次确认密码
 * 验证输入的密码
 */
Employee.verifyEmployeePasswordSureUpdate = function() {	
	var txt = $("#txt_employee_update_passwordsure").val();
	var pwd = $("#txt_employee_update_password").val();
	var span = $("#span_tip_employee_update_passwordsure");

	if(txt == "") {
		span.html("请再次确认密码");
		return false;
	}
	if(txt !== pwd) {        		
		span.html("两次密码输入不一致");
		return false;
	}
	span.html("");
	return true;
}


/**
 * 更新员工密码
 */
Employee.updateemployeepwd = function() {
	var flag = Employee.verifyEmployeePwdUpdate();
	if(!flag) {
		return false;
	}
	flag = Employee.verifyEmployeePasswordSureUpdate();
	if(!flag) {
		return false;
	}

	var param = {
		id: $("#txt_employee_updatepwd_id").val(),
		password: Base64.encode( $("#txt_employee_update_passwordsure").val() )
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.updatepwd,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("密码重置成功");
                $('#modal_update_employee_pwd').modal("hide");    
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 删除员工
 */
Employee.modalemployeedelete = function(index) {
	var item = Employee.data_employees[index];
	$("#txt_employee_delete_id").val( item.id );
	$("#span_delete_employee_name").html("确定删除员工\"" + item.username + "\"吗？删除后不可恢复");

	$('#modal_delete_employee').modal("show");
}

/**
 * 删除员工
 */
Employee.remove = function() {
	var param = {
		id: $("#txt_employee_delete_id").val()
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.remove,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                showPopover("删除成功");
                $('#modal_delete_employee').modal("hide");  
                Employee.search();  
            }
            else {
                showPopover(e.info);
            }
        }
    });
}





/**
 ***** 用户修改个人密码
 * 修改密码时验证输入
 */
Employee.verifypassword = function(){
    var flag = false;
    flag = Employee.verifyOldPassword();
    if(flag === false) {
        return flag;
    }
    flag = Employee.verifyNewPassword();
    if(flag === false) {
        return flag;
    }
    flag = Employee.verifyNewPasswordSure(); 
    return flag;
}

/**
 * 验证输入的旧密码
 */
Employee.verifyOldPassword = function() {
    var txt = $('#txt_oldpassword').val();
    if(txt === "") {
        showPopover("请输入旧密码");
        return false;
    }
    return true;
}

/**
 * 验证输入的新密码
 */
Employee.verifyNewPassword = function() {
    var txt = $('#txt_newpassword').val();
    if(txt === "") {
        showPopover("请输入新密码");
        return false;
    }
    return true;
}

/**
 * 验证确认的新密码
 */
Employee.verifyNewPasswordSure = function() {
    var txt = $('#txt_newpasswordsure').val();
    if(txt === "") {
        showPopover("请确认新密码");
        return false;
    }
    return true;
}

/**
 * 重置密码
 */
Employee.resetPassword = function() {
    var flag = Employee.verifypassword();
    if(flag === false) {
        return;
    }
}
