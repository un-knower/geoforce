/**
 * 用户管理
 */
var User = {
	/**
	 * 保存已经查询的用户
	 */
	users: [],
	data_param_url:""
};

/**
 * 用户状态对应
 */
User.getUserStatusLabel = function(status) {
	var label = '';
	switch(status) {
		case '0':
			label = '普通';
			break;
		case '1':
			label = '禁用';
			break;
		case '2':
			label = '待激活';
			break;
		case '3':
			label = '待审核';
			break;
		case '4':
			label = '其他';
			break;
		case '5':
			label = '高级';
			break;
	}
	return label;
}

/**
 * 查询
 */
User.search_request = null;
User.search = function(){
	User.users = [];

    var pageNo = Number($("#pager_users").attr("page"));
    var param = {
        pageNo: pageNo,
        pageSize: $("#users_page_count").val()//pageSize
    };

    var startTime = $('.txt-searchuser-starttime').val();
    var endTime = $('.txt-searchuser-endtime').val();
    if(startTime != '' || endTime != '') {
        if(startTime == '') {
            showPopover("请选择开始时间");
            return;
        }
        if(endTime == '') {
            showPopover("请选择结束时间");
            return;
        }
        var st = new Date(startTime.replace(/-/g,"/"));
        var et = new Date(endTime.replace(/-/g,"/"));
        if(st >= et) {
            showPopover("结束时间须晚于开始时间");
            return;
        }
        param.btime = startTime;
        param.etime = endTime;
    }

	var status = $('#select_search_user_status').val();
	if(status !== "-1") {
		param.status = status;
	}
	var keyword = $('#txt_search_user').val();
	if(keyword !== "" && keyword.length !== 0) {
		param.info = keyword;
	} 
    var admincode = $('.user-location').val();
    if(admincode != '-1') {
        param.admincode = admincode;
    }

    var solution = $('.user-solutions').val();
    if(solution != '-1') {
        param.combusiness = solution;
    }


	var table = $("#table_users").html('');
    var loader = table.parent('table').find('+ .loader').addClass('show');
    if(User.search_request) {
        User.search_request.abort();
    }
	User.search_request = $.ajax({
        type: 'GET',
        async: true,
        url: urls.user.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            loader.removeClass('show');
        	if(e && e.success ) {
        		if(e.result && e.result.userInfos.length < 1) {
        			showPopover("查询到0条用户数据");
        			return;
        		}
        		User.users = e.result.userInfos;
        		var len = User.users.length;
        		var html = '';

        		var per = user.privilegeCodes;
        		var per_detail = jQuery.inArray("usermge_detail", per);
        		var per_update = jQuery.inArray("usermge_update", per);
        		var per_remove = jQuery.inArray("usermge_remove", per);

        		for(var i=0; i<len; i++) {
        			var item = User.users[i];

        			html += '<tr>';
        			html += '	<td>'+ (i+1) +'</td>';
        			html += '	<td>'+ item.username +'</td>';
        			html += '	<td>'+ item.telephone +'</td>';
                    html += '   <td>'+ item.companyName +'</td>';
                    html += '   <td>'+ item.adminname +'</td>';
        			html += '	<td>'+ item.combusiness +'</td>';
        			html += '	<td>'+ (item.firstLogin ? item.firstLogin : '暂无详细信息') +'</td>';
                    // html += '   <td>'+ User.getUserStatusLabel( item.status ) +'</td>';
        			html += '   <td>';
        			if(per_detail !== -1) {
        				html += '	<a href="javascript:User.modaluserdetail('+ i +')">详情</a><i></i>';
        			}
        			if(per_update !== -1) {
        				html += '	<a href="javascript:User.modaluserupdate('+ i +')">修改</a><i></i>';
        			}
        			if(per_remove !== -1) {
        				html += '	<a href="javascript:User.modaluserdelete('+ i +')">删除</a>';
        			}
        			html += '</td></tr>';
        		}
        		table.html(html); 
        		
        		var html_page = setPage(e.result.totalCount, pageNo, '\'pager_users\''); 
        		$("#pager_users > ul").html(html_page);  
        		//添加导出按钮
        		$("#users_total").text("共"+e.result.totalCount+"条");
        		//删除pageNo,pageSize
        		delete param.pageNo;
        		delete param.pageSize;
        		User.data_param_url=generateParamURL(param);
                
                var export_url="http://"+window.location.hostname+":"+window.location.port+urls.user.exportUserExcel+User.data_param_url;
                $("#export_a").remove();
        		var h="";
        		h+='<a href="'+export_url+'" target="_blank" class="export-a" id="export_a">导出</a>'; 
        		$("#export_wrap").append(h);   		
        	}
        	else {        		
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 显示用户详情的模态框
 */
User.modaluserdetail = function(index){
	var id = User.users[index].id;
	var param = {
		id: id
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.user.detail,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		var u = e.result;
        		$("#span_detail_userid").html(u.id);
        		$("#span_detail_username").html(u.username ? u.username : "无");
        		$("#span_detail_realname").html(u.realName ? u.realName : "无");
        		$("#span_detail_email").html(u.email ? u.email : "无");
        		$("#span_detail_status").html(u.status ? User.getUserStatusLabel(u.status) : "无");
        		$("#span_detail_fax").html(u.fax ? u.fax : "无");
        		$("#span_detail_company").html(u.companyName ? u.companyName : "无");
        		$("#span_detail_companyAddress").html(u.companyAddress ? u.companyAddress : "无");
        		$("#span_detail_companyEmail").html(u.companyEmail ? u.companyEmail : "无");
        		$("#span_detail_companyPhone").html(u.companyPhone ? u.companyPhone : "无");
        		$("#span_detail_companyRemarks").html(u.companyRemarks ? u.companyRemarks : "无");
        		$("#span_detail_mobile").html(u.telephone ? u.telephone : "无");
        		$("#span_detail_phone").html(u.telephone ? u.telephone : "无");
        		$("#span_detail_sex").html(u.sex ? u.sex : "无");
        		$("#span_detail_remarks").html(u.remarks ? u.remarks : "无");
        		$("#span_detail_zipcode").html(u.zipcode ? u.zipcode : "无");
                $("#span_detail_firstLogin").html(u.firstLogin ? u.firstLogin : "无");
                $("#span_detail_solution").html(u.combusiness ? u.combusiness : "无");
        		$("#span_detail_location").html(u.adminname ? u.adminname : "无");

        		$('#modal_detail_user').modal({
					keyboard: false
				});
        	}
        	else {
        		showPopover(e.info);
        	}
        }
    });
}	

/**
 * 显示修改用户的模态框
 */
User.modaluserupdate = function(index){
	var id = User.users[index].id;
	var param = {
		id: id
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.user.detail,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		var u = e.result;
        		$("#txt_update_userid").val(u.id);
        		$("#txt_update_user_email").html(u.email);
        		$("#txt_update_userstatus").val(u.status ? u.status : 4);
        		$("#txt_update_userremarks").val(u.remarks ? u.remarks : "");

        		$('#modal_update_user').modal({
					keyboard: false
				});
        	}
        	else {
        		showPopover(e.info);
        	}
        }
    });
}	

/**
 * 更新用户
 */
User.update = function(){
	var id = $("#txt_update_userid").val();
	var param = {
		id: id,
		status: $("#txt_update_userstatus").val(),
		remarks: $("#txt_update_userremarks").val()
	}
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.user.update,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e && e.success) {
        		showPopover("用户修改成功");
        		$('#modal_update_user').modal('hide');
        		User.search();
        	}
        	else {        		
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 显示删除用户的模态框
 * @param: index 选择的用户的索引
 */
User.modaluserdelete = function(index){
	var user = User.users[index];
	$("#txt_delete_userid").val(user.id);
	$("#span_delete_user").html("确定要删除用户\"" + user.email + "\"吗？删除后不可恢复。");
	$('#modal_delete_user').modal({
		keyboard: false
	});
}	

/**
 * 删除用户
 */
User.remove = function(){
	var id = $("#txt_delete_userid").val();
	var param = {
		id: id
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.user.remove,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		$('#modal_delete_user').modal("hide");
        		showPopover("删除成功");
        		User.search();
        	}
        	else {
        		showPopover(e.info);
        	}
        }
    });
}
/**
 * 验证输入的用户名
 */
User.verfyUsername = function(){
	var txt = $("#txt_user_name").val();
	var span = $("#span_tip_username");

	var reg = /^[a-zA-Z0-9\_]{4,20}$/;
	var regUserName = /^(?!_)(?!.*?_$)[a-zA-Z]+[a-zA-Z0-9_]*$/;
    var regBlank = /^\w$|(^ )|( $)/;
	var tip = "";
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
	else if( !regUserName.test(txt) ) {
		span.html("用户名只能包含英文、数字、下划线，不能以数字或下划线开头，不能以下划线结尾");
		return false;
	}
	else if( regBlank.test(txt) || txt.indexOf(" ") > 0 ){
		span.html("用户名不能包空格");
		return false;
	}
	span.html("");
	return true;
}

/**
 * 添加用户--验证输入的密码
 * 验证输入的密码
 */
User.verifyUserPassword = function() {
	var txt = $("#txt_user_password").val();
	var span = $("#span_tip_userpassword");

	var regNull = /^\s*$/;
	var psdVali = $("#txt_user_passwordsure").val();

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
    else if(psdVali != "" && psdVali !== txt){
		span.html("两次密码输入不一致");
		return false;
	}
    span.html("");
	return true; 
}

/**
 * 添加用户--验证输入的再次确认密码
 * 验证输入的密码
 */
User.verifyUserPasswordSure = function() {	
	var txt = $("#txt_user_passwordsure").val();
	var pwd = $("#txt_user_password").val();
	var span = $("#span_tip_userpasswordsure");

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
 * 添加用户--验证邮箱
 */
User.verifyEmail = function() {	
	var txt = $("#txt_user_email").val();
	var span = $("#span_tip_email");

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
 * 添加用户--验证邮箱
 */
User.verifyRealname = function() {	
	var txt = $("#txt_user_realname").val();
	var span = $("#span_tip_realname");
	
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
User.verifyMobile = function(){
	var txt = $("#txt_user_mobile").val();
	var span = $("#span_tip_mobile");

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
 * 添加用户-验证公司
 */
User.verifyCompany = function() {
	var txt = $("#txt_user_company").val();
	var span = $("#span_tip_company");

	if(txt == "") { 				
		span.html("请填写公司名称");
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
 * 添加用户-验证部门
 */
User.verifyDepartment = function() {
	var txt = $("#txt_user_department").val();
	var span = $("#span_tip_department");

	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/
 	span.html('');
 	return true;
}

/**
 * 添加用户-验证部门
 */
User.verifyPhone = function() {
	var txt = $("#txt_user_phone").val();
	var span = $("#span_tip_phone");
	
	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/
 	span.html('');
 	return true;
}

/**
 * 验证添加的用户
 */
User.verifyUserInfo = function() {
	var flag = false;
	flag = User.verfyUsername();

	if(!flag) {
		$("#collapse_add_userone").collapse("show");
		return flag;
	}
	flag = User.verifyUserPassword();	
	if(!flag) {
		$("#collapse_add_userone").collapse("show");
		return flag;
	}
	flag = User.verifyUserPasswordSure();	
	if(!flag) {
		$("#collapse_add_userone").collapse("show");
		return flag;
	}
	flag = User.verifyEmail();	
	if(!flag) {
		$("#collapse_add_usertwo").collapse("show");
		return flag;
	}
	flag = User.verifyCompany();	
	if(!flag) {
		$("#collapse_add_userone").collapse("show");
		return flag;
	}
	flag = User.verifyMobile();	
	if(!flag) {
		$("#collapse_add_userthree").collapse("show");
		return flag;
	}
	return flag;
}

/**
 * 添加用户
 */
User.add = function(){
	var flag = User.verifyUserInfo();
	if(!flag) {
		return false;
	}
	var gender = $("[name='sex']").filter(":checked").val();

	var param = {
		username: $("#txt_user_name").val(),
		password: Base64.encode( $("#txt_user_passwordsure").val() ),
		email: $("#txt_user_email").val(),
		realName:  $("#txt_user_realname").val() ,
		mobilephone: $("#txt_user_mobile").val(),
		telephone: $("#txt_user_phone").val(),
		fax: $("#txt_user_fax").val(),
		remarks:  $("#txt_user_remark").val() ,
		address:  $("#txt_user_address").val() ,
		zipcode: $("#txt_user_zipcode").val(),
		sex: gender,
		companyName:  $("#txt_user_company").val() ,
		companyEmail:  $("#txt_user_companyEmail").val() ,
		companyPhone:  $("#txt_user_companyPhone").val() ,
		companyAddress:  $("#txt_user_companyAddress").val() ,
		companyRemarks:  $("#txt_user_companyRemarks").val(),
		remarks: $("#txt_user_remarks").val()
	};
	
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.user.add,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e && e.success) {
        		showPopover("用户添加成功");
        		$('#modal_add_user').modal('hide');
        		searchUsers();
        	}
        	else {        		
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 重置添加用户的输入
 */
User.resetModalAddUser = function(){
	$("#txt_user_name").val("");
	$("#span_tip_username").html("4-20位，字母、数字、下划线组合");

	$("#txt_user_password").val("");
	$("#span_tip_userpassword").html("4-20位，字母、数字、下划线组合");

	$("#txt_user_passwordsure").val("");
	$("#span_tip_userpasswordsure").html("");

	$("#txt_user_email").val("");
	$("#span_tip_email").html("");

	$("#txt_user_realname").val("");
	$("#span_tip_realname").html("");

	$("#txt_user_mobile").val("");
	$("#span_tip_mobile").html("");

	$("#txt_user_company").val("");
	$("#span_tip_company").html("");

	$("#txt_user_department").val("");
	$("#span_tip_department").html("");

	$("#txt_user_phone").val("");
	$("#span_tip_phone").html("");

	$("#txt_user_fax").val("");
	$("#span_tip_fax").html("");

	$("#txt_user_zipcode").val("");
	$("#span_tip_zipcode").html("");

	$("#txt_user_address").val("");
	$("#span_tip_address").html("");

	$("#txt_user_remark").val("");
	$("#span_tip_remark").html("");	
}


User.initData = function() {
    var h = '<option value="-1" selected="true">全部</option>';
    var data = User.data.province;
    for(var i=0, len=data.length; i<len; i++) {
        var item = data[i];
        h += '<option value="'+ item.admincode +'">'+ item.province +'</option>';
    }
    $('.user-location').html(h).change(function(){
        $("#pager_users").attr("page", "0");
        User.search();
    });

    h = '<option value="-1" selected="true">全部</option>';
    data = User.data.solutions;
    for(var i=0, len=data.length; i<len; i++) {
        var item = data[i];
        h += '<option value="'+ item +'">'+ item +'</option>';
    }
    $('.user-solutions').html(h).change(function(){        
        $("#pager_users").attr("page", "0");
        User.search();
    });
    $('.txt-searchuser-starttime').val( new Date(new Date().setMonth( new Date().getMonth() - 1 )).format('yyyy-MM-dd') );
    $('.txt-searchuser-endtime').val( new Date().format('yyyy-MM-dd') );
}
User.data = {
    province: [ 
        {
            "province": "北京",
            "admincode": "110000",
            'zhixia': "true"
        },
        {
            "province": "天津",
            "admincode": "120000",
            'zhixia': "true"
        },
        {
            "province": "重庆",
            "admincode": "500000",
            'zhixia': "true"
        },
        {
            "province": "上海",
            "admincode": "310000",
            'zhixia': "true"
        },
        {
            "province": "安徽",
            "admincode": "340000"
        },
        {
            "province": "福建",
            "admincode": "350000"
        },
        {
            "province": "甘肃",
            "admincode": "620000"
        },
        {
            "province": "广东",
            "admincode": "440000"
        },
        {
            "province": "广西",
            "admincode": "450000"
        },
        {
            "province": "贵州",
            "admincode": "520000"
        },
        {
            "province": "海南",
            "admincode": "460000"
        },
        {
            "province": "河北",
            "admincode": "130000"
        },
        {
            "province": "河南",
            "admincode": "410000"
        },
        {
            "province": "黑龙江",
            "admincode": "230000"
        },
        {
            "province": "湖北",
            "admincode": "420000"
        },
        {
            "province": "湖南",
            "admincode": "430000"
        },
        {
            "province": "吉林",
            "admincode": "220000"
        },
        {
            "province": "江苏",
            "admincode": "320000"
        },
        {
            "province": "江西",
            "admincode": "360000"
        },
        {
            "province": "辽宁",
            "admincode": "210000"
        },
        {
            "province": "内蒙古",
            "admincode": "150000"
        },
        {
            "province": "宁夏",
            "admincode": "640000"
        },
        {
            "province": "青海",
            "admincode": "630000"
        },
        {
            "province": "山东",
            "admincode": "370000"
        },
        {
            "province": "山西",
            "admincode": "140000"
        },
        {
            "province": "陕西",
            "admincode": "610000"
        },
        {
            "province": "四川",
            "admincode": "510000"
        },
        {
            "province": "西藏",
            "admincode": "540000"
        },
        {
            "province": "新疆",
            "admincode": "650000"
        },
        {
            "province": "云南",
            "admincode": "530000"
        },
        {
            "province": "浙江",
            "admincode": "330000"
        },
        {
            "province": "香港",
            "admincode": "810000"
        },
        {
            "province": "澳门",
            "admincode": "820000"
        },
        {
            "province": "台湾",
            "admincode": "710000"
        }
    ],

    solutions : [
        '物流快递','家电','金融','保险','快销零售','医药','制造业','其他'
    ]
};