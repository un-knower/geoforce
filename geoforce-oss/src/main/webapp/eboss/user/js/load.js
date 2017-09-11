/**
 * 页面初始化
 */
var map_status_product = new HashMap();
var map_status_product_key = new HashMap();
var map_status_permission = new HashMap();
$(document).ready(function(){	
	initcss();

	$(window).resize(function(){
		initcss();		
	});	
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.employee.detail,
        dataType: 'jsonp',
        // data: {id: 4},
        success: function(e){
            if(e && e.success && e.result) {
            	initUser(e);
            }
            else {
                showPopover("登录已过有效期，请重新登录。");
                setTimeout("location.href='/egispboss/'", 1500);
            	// initUser(e);
            }
        }
    });
});

function initUser(e) {
	var r = e.result;
	/*var r = {
		username: '1',
		realName: '1',
		gender: '1',
		email: '1'
	}*/

	user = r;
	user.username = user.username ? user.username : user.email;

	$("#span_me_employee_username").html( r.username );
	$("#nav_login_username").html( r.username );

	$("#span_me_employee_realName").html( r.realName );
	$("#span_me_employee_status").html( r.status );
	$("#span_me_employee_gender").html( r.gender );
	$("#span_me_employee_email").html( r.email );
	$("#span_me_employee_department").html( r.department ? r.department : "无" );
	$("#span_me_employee_position").html( r.position );
	$("#span_me_employee_mobilePhone").html( r.mobilePhone );
	$("#span_me_employee_phone").html( r.phone ? r.phone : "无" );

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
	$("#span_me_employee_roles").html( h ? h : "无" );
	/*
	len = r.privilegeCodes ? r.privilegeCodes : 0;
	h = '';
	if(len > 0) {
		for(var i=len; i--;) {
			h += r.privilegeCodes[i];            			
			if( i !== 0 ) {
				h += ',';
			}
		}
	}*/
	
	/*user.privilegeCodes = ["usermge", "productmge", "ordermge", "usermge_add", "usermge_search", "usermge_detail", "usermge_update", "usermge_remove", "productmge_search", "productmge_add", "productmge_detail", "productmge_update", "productmge_remove", "ordermge_add", "ordermge_search", "ordermge_detail", "ordermge_audit", "ordermge_update", "ordermge_remove",
		"permissionmge", "permissionmge_add", "permissionmge_update", "permissionmge_remove", "permissionmge_search",
		"rolemge", "rolemge_add", "rolemge_update", "rolemge_remove", "rolemge_search", "rolemge_update_permission",
		"employeemge_add", "employeemge", "employeemge_pwd", "employeemge_role", "employeemge_remove", 
		"employeemge_search", "employeemge_update"
		];*/
	initPage();
}

/**
 * 初始化页面
 */
function initPage() {
	$('input').placeholder();
	User.initData();

	$.AutoComplete('input[name="email"]');

	//这一行有问题，需要修改
	$.PopupUserList('#txt_order_userid');

	map_status_product.put('0', 'RELEASE');
	map_status_product.put('1', 'BETA');
	map_status_product.put('2', 'REVOCATION');
	map_status_product.put('3', 'OTHER');

	map_status_product_key.put('RELEASE', '0');
	map_status_product_key.put('BETA', '1');
	map_status_product_key.put('REVOCATION', '2');
	map_status_product_key.put('OTHER', '3');

	map_status_permission.put('USING', '0');
	map_status_permission.put('FORBIDDEN', '1');
	map_status_permission.put('ABANDON', '1');

	dealPermissions();

	bindClicks();
}

/**
 * 处理权限
 */
function dealPermissions() {

	var per = user.privilegeCodes;

	var index = jQuery.inArray("usermge", per);
	var h = '';
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;" id="nav_a_usermanage" data-target="user" >';
        h += '   	<span class="glyphicon glyphicon-user btn-lg" style="padding: 5px 10px;"></span> 用户管理';
        h += ' 	</a>';
      	h += '</li>';
	}

	index = jQuery.inArray("keymge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_keymanage" data-target="key">';
        h += '   	<span class="glyphicon glyphicon-tag btn-lg" style="padding: 5px 10px;"></span> key管理';
        h += ' 	</a>';
      	h += '</li>';
	}
	index = jQuery.inArray("productmge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_productmanage" data-target="product" >';
        h += '   	<span class="glyphicon glyphicon-gift btn-lg" style="padding: 5px 10px;"></span> 产品管理';
        h += ' 	</a>';
      	h += '</li>';
	}
	index = jQuery.inArray("ordermge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_ordermanage" data-target="order"  >';
        h += '   	<span class="glyphicon glyphicon-file btn-lg" style="padding: 5px 10px;"></span> 订单管理';
        h += ' 	</a>';
      	h += '</li>';
	}	
	index = jQuery.inArray("permissionmge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_permissionmanage" data-target="permission"  >';
        h += '   	<span class="glyphicon glyphicon-cog btn-lg" style="padding: 5px 10px;"></span> 权限管理';
        h += ' 	</a>';
      	h += '</li>';
	}
	index = jQuery.inArray("rolemge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_rolemanage" data-target="role" >';
        h += '   	<span class="glyphicon glyphicon-credit-card btn-lg" style="padding: 5px 10px;"></span> 角色管理';
        h += ' 	</a>';
      	h += '</li>';
	}
	index = jQuery.inArray("rolemge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_employeemanage" data-target="employee" >';
        h += '   	<span class="glyphicon glyphicon-star-empty btn-lg" style="padding: 5px 10px;"></span> 员工管理';
        h += ' 	</a>';
      	h += '</li>';
	}
	//新增日志更新
	index = jQuery.inArray("logmge", per);
	if( index !== -1 ) {
		h += '<li>';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;"  id="nav_a_logmanage" data-target="log" >';
        h += '   	<span class="glyphicon glyphicon-calendar btn-lg" style="padding: 5px 10px;"></span> 日志更新';
        h += ' 	</a>';
      	h += '</li>';
    }
	//数据管理
		h += '<li id="nav_a_datali">';
       	h += '	<div class="pointer">';
        h += '		<div class="arrow"></div>';
        h += '   	<div class="arrow_border"></div>';
        h += ' 	</div>';
        h += ' 	<a href="javascript:;">';
        h += '   	<span class="glyphicon glyphicon-hdd btn-lg" style="padding: 5px 10px;"></span> 数据管理';
        h += ' 	</a>';
        h += '  <ul id="data_item" class="data-item click-menu">';
    //网点数据管理
	index = jQuery.inArray("pointmge", per);
	if( index !== -1 ) {
		h += ' 		<li><a href="javascript:;" id="nav_a_pointmanage" 				    data-target="point">网点数据</a></li>';
	}
	//区划数据管理
	index = jQuery.inArray("areamge", per);
	if( index !== -1 ) {
		h += ' 		<li><a href="javascript:;" id="nav_a_regionmanage"  data-target="region">区划数据</a></li>';
	}
	//分单数据管理
	index = jQuery.inArray("fendanmge", per);
	if( index !== -1 ) {
		h += ' 		<li><a href="javascript:;" id="nav_a_fendanmanage" data-target="fendan">分单数据</a></li>';
	}
		h += ' 	</ul>';
      	h += '</li>';
	$('#nav_left_menu').append(h);

	//用户权限
	index = jQuery.inArray("usermge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_adduser').css("display", "");
	}
	index = jQuery.inArray("usermge_search", per);
	if( index == -1 ) {
		$('#div_usermanage').html('');
	}	
	var thead = $("#thead_users");
	/*index = jQuery.inArray("usermge_detail", per);
	if( index !== -1 ) {
		thead.append('<td>详情</td>');
	}
	index = jQuery.inArray("usermge_update", per);
	if( index !== -1 ) {
		thead.append('<td>修改</td>');
	}
	index = jQuery.inArray("usermge_remove", per);
	if( index !== -1 ) {
		thead.append('<td>删除</td>');
	}*/

	//产品权限
	index = jQuery.inArray("productmge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_add_product').css("display", "");
	}
	index = jQuery.inArray("productmge_search", per);
	if( index === -1 ) {
		$('#div_productmanage').html('');
	}
	thead = $("#thead_products");
	index = jQuery.inArray("productmge_detail", per);
	if( index !== -1 ) {
		thead.append('<td>详情</td>');
	}
	index = jQuery.inArray("productmge_update", per);
	if( index !== -1 ) {
		thead.append('<td>修改</td>');
	}
	index = jQuery.inArray("productmge_remove", per);
	if( index !== -1 ) {
		thead.append('<td>删除</td>');
	}


	//订单权限
	index = jQuery.inArray("ordermge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_addorder').css("display", "");
	}
	index = jQuery.inArray("ordermge_search", per);
	if( index === -1 ) {
		$('#div_ordermanage').html('');
	}
	thead = $("#thead_orders");
	index = jQuery.inArray("ordermge_audit", per);
	if( index !== -1 ) {
		thead.append('<td>审核</td>');
	}
	thead.append('<td style="min-width: 150px;">操作</td>');
	/*index = jQuery.inArray("ordermge_detail", per);
	if( index !== -1 ) {
	}*/


	//权限
	index = jQuery.inArray("permissionmge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_addper').css("display", "");
	}
	index = jQuery.inArray("permissionmge_search", per);
	if( index === -1 ) {
		$('#div_permissionmanage').html('');
	}
	thead = $("#thead_pers");
	index = jQuery.inArray("permissionmge_update", per);
	if( index !== -1 ) {
		thead.append('<td>修改</td>');
	}
	index = jQuery.inArray("permissionmge_remove", per);
	if( index !== -1 ) {
		thead.append('<td>删除</td>');
	}


	//角色
	index = jQuery.inArray("rolemge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_addrole').css("display", "");
	}
	index = jQuery.inArray("rolemge_search", per);
	if( index === -1 ) {
		$('#div_rolemanage').html('');
	}
	thead = $("#thead_role");
	index = jQuery.inArray("rolemge_update", per);
	if( index !== -1 ) {
		thead.append('<td>修改</td>');
	}
	index = jQuery.inArray("rolemge_update_permission", per);
	if( index !== -1 ) {
		thead.append('<td>修改权限</td>');
	}
	index = jQuery.inArray("rolemge_remove", per);
	if( index !== -1 ) {
		thead.append('<td>删除</td>');
	}

	//员工
	index = jQuery.inArray("employeemge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_addemployee').css("display", "");
	}
	index = jQuery.inArray("employeemge_search", per);
	if( index === -1 ) {
		$('#div_employeemanage').html('');
	}
	thead = $("#thead_employee");
	index = jQuery.inArray("employeemge_detail", per);
	if( index !== -1 ) {
		thead.append('<td>详情</td>');
	}
	index = jQuery.inArray("employeemge_update", per);
	if( index !== -1 ) {
		thead.append('<td>修改</td>');
	}
	index = jQuery.inArray("employeemge_role", per);
	if( index !== -1 ) {
		thead.append('<td>赋角色</td>');
	}
	index = jQuery.inArray("employeemge_pwd", per);
	if( index !== -1 ) {
		thead.append('<td>重置密码</td>');
	}
	index = jQuery.inArray("employeemge_remove", per);
	if( index !== -1 ) {
		thead.append('<td>删除</td>');
	}
	/*日志权限*/
	index = jQuery.inArray("logmge_add", per);
	if( index !== -1 ) {
		$('#btn_modal_addlog').css("display", "");
	}
	index = jQuery.inArray("logmge_searchall", per);
	if( index === -1 ) {
		$('#div_logmanage').html('');
	}
	thead = $("#thead_logs");
	index = jQuery.inArray("logmge_update", per);
	if( index !== -1 ) {
		thead.append('<td>编辑</td>');
	}
	index = jQuery.inArray("logmge_delete", per);
	if( index !== -1 ) {
		thead.append('<td>删除</td>');
	}

}

/**
 * 重置页面样式
 */
function initcss() {
	var bodyHeight = getWindowHeight();
	var content_height = bodyHeight - 48 - 46;
	// $('.content').height(content_height);
	content_height = content_height > 680 ? content_height : 680;
	$('.content, .content-right').css({"min-height": content_height + "px"})

	$(".overlay").css({"height": $(document).height() });	
}

/**
 * 为按钮绑定事件
 */
function bindClicks(){	
	$('.click-menu > li > a').click(function(){
		var id = "div_" + $(this).attr('id').replace("nav_a_", "");

		var div = $('#' + id);
		if( div.css
			("display") !== "none" ) {
			return;
		}
		$('.dashboard-menu > li').removeClass('active');
		// $('.dashboard-menu ul.submenu a').removeClass('active');
		$(this).parent('li').addClass('active');

		$('.tab-contentbody').css('display', "none");
		div.fadeIn('300');

		var target = $(this).attr("data-target");
		var index = -1, per = user.privilegeCodes;
		switch(target) {
			case "user":
				index = jQuery.inArray("usermge_search", per);
				if(index !== -1) {
					User.search();
				}
				break;
			case "product":
				index = jQuery.inArray("productmge_search", per);
				if(index !== -1) {					
					Product.search();
				}
				break;
			case "permission":
				index = jQuery.inArray("permissionmge_search", per);
				if(index !== -1) {
					Permission.search();
				}
				break;
			case "order" :
				index = jQuery.inArray("ordermge_search", per);
				if(index !== -1) {
					Order.search();
				}
				break;
			case "employee":
				index = jQuery.inArray("employeemge_search", per);
				if(index !== -1) {
					Employee.search();
				}
				break;
			case "role":
				index = jQuery.inArray("rolemge_search", per);
				if(index !== -1) {
					Role.search();
				}
				break;
			case "log":
				index = jQuery.inArray("logmge_searchall", per);
				if(index !== -1) {
					Log.search();
				}
				break;
			case "point":
				Point.init();
				SuperMap.Egisp.SMCity.init();
				/*index = jQuery.inArray("logmge_searchall", per);
				if(index !== -1) {
					Log.search();
				}*/
				break;
			case "region":
				Region.init();
				SuperMap.Egisp.SMCity.init();
				/*index = jQuery.inArray("logmge_searchall", per);
				if(index !== -1) {
					Log.search();
				}*/
				break;
			case "fendan":
				Fendan.init();
				SuperMap.Egisp.SMCity.init();
				/*index = jQuery.inArray("logmge_searchall", per);
				if(index !== -1) {
					Log.search();
				}*/
				break;
			case 'key':
				break;
		}

	});

	/*$('.dashboard-menu ul.submenu a').click(function(){
		$('.dashboard-menu ul.submenu a').removeClass('active');
		$(this).addClass('active');

		$('.dashboard-menu > li').removeClass('active');
		$(this).parent('li').parent('ul').parent('li').addClass('active');

		var id = "div_" + $(this).attr('id').replace("nav_a_", "");
		$('.content-right > div').css('display', "none");
		$('#' + id).fadeIn('300');
	});*/

	$('#div_resetpassword > input').bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			verifypassword();
		}
	});
	
	$('#txt_oldpassword').blur(verifyOldPassword);
	$('#txt_newpassword').blur(verifyNewPassword);
	$('#txt_newpasswordsure').blur(verifyNewPasswordSure);
	$('#btn_resetpassword').click(updateMyPassword);
	

	/*用户管理*/
	$('#txt_user_name').blur(User.verfyUsername);
	$("#txt_user_password").blur(User.verifyUserPassword);
	$("#txt_user_passwordsure").blur(User.verifyUserPasswordSure);
	$("#txt_user_email").blur(User.verifyEmail);
	$("#txt_user_realname").blur(User.verifyRealname);
	$("#txt_user_mobile").blur(User.verifyMobile);
	$("#txt_user_company").blur(User.verifyCompany);

	$("#txt_search_user").bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			$("#pager_users").attr("page", "0");
			User.search();
		}
	});
	$("#select_search_user_status").change(function(){
		$("#pager_users").attr("page", "0");
		User.search();
	});
	$("#btn_search_user").click(function(){
		$("#pager_users").attr("page", "0");
		User.search();
	});
	$('#modal_add_user').on('show.bs.modal', User.resetModalAddUser);
	
	/*用户管理end*/


	/*产品管理*/
	$("#txt_product_name").blur(Product.verifyProductName);
	$("#txt_product_price").blur(Product.verifyProductPrice);
	$("#txt_product_url").blur(Product.verifyProductUrl);
	$("#txt_product_code").blur(Product.verifyProductCode);
	$("#txt_product_icon_url").blur(Product.verifyProductIconUrl);
	$("#btn_add_product").click(function(){
		var me = $('#modal_add_product').attr("current");
		if(me == "0") {
			Product.add();
		}
		else if(me == "1") {
			Product.update();
		}		
	});
	$("#txt_search_product").bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			$("#pager_products").attr("page", "0");
			Product.search();
		}
	});
	$("#select_search_product_status").change(function(){
		$("#pager_products").attr("page", "0");
		Product.search();
	});
	$("#btn_modal_add_product").click(Product.modalAddProduct);
	$("#btn_search_product").click( function(){
		$("#pager_products").attr("page", "0");
		Product.search();
	});
	/*产品管理 end*/

	/*订单管理*/
	// $("#txt_order_userid").blur(verifyUserIDForOrder);
	$("#txt_orderdate_start").blur(Order.verifyOrderDateStart);
	$("#txt_orderdate_end").blur(Order.verifyOrderDateEnd);
	$('#modal_update_order').on('hide.bs.modal', function (e) {
		$("#pager_orders").attr("page", "0");
		Order.search();
	});
	$('#btn_search_orders').click(function (e) {
		$("#pager_orders").attr("page", "0");
		Order.search();
	});
	$('#modal_add_order').on('show.bs.modal', function (e) {
		Order.searchAllProducts();
	});
	$('#modal_add_order').on('hidden.bs.modal', function (e) {
		$('#popup_userlist').hide();
	});
	$('.datepicker > input').datepicker({});
	$("#txt_search_order").bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			$("#pager_orders").attr("page", "0")
			Order.search()
		}
	});
	$("#select_search_order_status").change( function(){
		$("#pager_orders").attr("page", "0");
		Order.search();
	} );
	$("#collapse_update_order").on("show.bs.collapse", function(){
		var other = $("#collapse_update_orderitem");
		if(other.css("display") !== "none") {
			other.collapse('hide');
		}
		
	});
	$("#collapse_update_orderitem").on("show.bs.collapse", function(){
		var other = $("#collapse_update_order");
		if(other.css("display") !== "none") {
			other.collapse('hide');
		}
	});
	$('#txt_order_status').change(Order.setDefaultTime);

	/*权限管理*/	
	$("#btn_search_permissions").click( function(){
		$("#pager_permissions").attr("page", "0");
		Permission.search();
	});
	$("#txt_permission_name").blur(Permission.verifyPermissionName);
	$("#txt_permission_code").blur(Permission.verifyPermissionCode);
	$("#txt_permission_url").blur(Permission.verifyPermissionUrl);
	$("#modal_add_permission").on("show.bs.modal", Permission.initPermissionPid);
	$("#txt_search_per").bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			$("#pager_permissions").attr("page", "0");
			Permission.search();
		}
	});
	$("#select_search_per_status").change( function(){
		$("#pager_permissions").attr("page", "0");
		Permission.search();
	});

	/*角色管理*/
	$("#btn_search_roles").click(function(){
		$("#pager_roles").attr("page", "0");
		Role.search();
	} );
	$("#modal_add_role").on("show.bs.modal", Role.initRolePermissions);
	$("#txt_role_name").blur(Role.verifyRoleName);	
	$("#txt_search_role").bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			$("#pager_roles").attr("page", "0");
			Role.search();
		}
	});

	/*员工管理*/
	$('#btn_resetpassword').click(Employee.resetPassword);
	$("#txt_employee_name").blur(Employee.verifyEmployeeName);	
	$("#txt_employee_password").blur(Employee.verifyEmployeePwd);	
	$("#txt_employee_passwordsure").blur(Employee.verifyEmployeePasswordSure);	
	$("#txt_employee_realname").blur(Employee.verifyEmployeeRealname);	
	$("#txt_employee_job").blur(Employee.verifyEmployeeJob);	
	$("#txt_employee_email").blur(Employee.verifyEmployeeEmail);	
	$("#txt_employee_mobile").blur(Employee.verifyEmployeeMobile);	
	$("#btn_search_employee").click( function(){
		$("#pager_employees").attr("page", "0");
		Employee.search();
	} );
	$("#txt_employee_update_job").blur(Employee.verifyEmployeeJobUpdate);
	$("#txt_employee_update_mobile").blur(Employee.verifyEmployeeMobileUpdate);
	$("#txt_employee_update_password").blur(Employee.verifyEmployeePwdUpdate);
	$("#txt_employee_update_passwordsure").blur(Employee.verifyEmployeePasswordSureUpdate);
	$("#modal_add_employee").on("show.bs.modal", Employee.resetEmployeeAddModal);
	$("#txt_search_employee").bind('keyup', function(e){
		e = e ? e : window.event;
		if(e.keyCode == 13) {
			$("#pager_employees").attr("page", "0");
			Employee.search();
		}
	});
	$("#select_search_employee_status").change( function(){
		$("#pager_employees").attr("page", "0");
		Employee.search();
	} );
	/*日志管理*/
	$("#btn_search_log").click(function(){
		$("#pager_employees").attr("page", "0");
		Log.search();
	});
	$("#modal_add_log").on("show.bs.modal", Log.resetLogAddModal);
	/*数据管理*/
	$("#nav_a_datali").mouseover(function(){
		$("#data_item").css("display","block");
	});
	$("#nav_a_datali").mouseout(function(){
		$("#data_item").css("display","none");
	});
	/*网点数据*/
	$("#btn_search_point").click(function(){
		Point.search();
	});
	/*区划数据*/
	$("#btn_search_region").click(function(){
		$("#pager_employees").attr("page", "0");
		Region.search();
	});
	/*分单数据*/
	$("#btn_search_fendan").click(function(){
		$("#pager_employees").attr("page", "0");
		Fendan.search();
	});
}

/**
 * 退出
 */
function logout() {
	location.href = "/egispboss/";
}
























/**
 ***** 用户修改个人密码
 * 修改密码时验证输入
 */
function verifypassword(){
	var flag = false;
	flag = verifyOldPassword();
	if(!flag) {
		return flag;
	}
	flag = verifyNewPassword();
	if(!flag) {
		return flag;
	}
	flag = verifyNewPasswordSure();	
	return flag;
}
/**
 * 验证输入的旧密码
 */
function verifyOldPassword() {
	var txt = $('#txt_oldpassword').val();
	var span = $('.hint-resetpwd');
	if(txt === "") {
		span.html("请输入旧密码");
		return false;
	}
    span.html('');
	return true;
}

/**
 * 验证输入的新密码
 */
function verifyNewPassword() {
	var txt = $('#txt_newpassword').val();
	var txt_sure = $('#txt_newpasswordsure').val();

	var span = $('.hint-resetpwd');
	var regNull = /^\s*$/;
	if(txt === "") {
		span.html("请输入新密码");
		return false;
	}
	else if (txt.length < 6 || txt.length > 20) {
        span.html("密码长度必须大于6并小于20");
        return false;
    } 
	else if( txt_sure !== txt && txt_sure !== '' ) {
		span.html("两次密码输入不一致");
		return false;		
	}
    else if(regNull.test(txt) || txt.indexOf(" ")>=0){
        span.html("密码中不能包含空格");
        return false;
    }
    else if( txt === $('#txt_oldpassword').val() ) {
        span.html("新密码不能与旧密码相同");
        return false;    	
    }
    span.html('');
	return true;
}

/**
 * 验证确认的新密码
 */
function verifyNewPasswordSure() {
	var txt = $('#txt_newpasswordsure').val();
	var txt_sure = $('#txt_newpassword').val();

	var span = $('.hint-resetpwd');
	if(txt === "") {
		span.html("请确认新密码");
		return false;
	}
	else if( txt_sure !== txt ) {
		span.html("两次密码输入不一致");
		return false;		
	}
    span.html('');
	return true;
}

/**
 * 用户修改个人密码
 */
function updateMyPassword() {
	var flag = verifypassword();
	if(!flag) {
		return;
	}
	var param = {
		id: user.id,
		password: Base64.encode( $("#txt_newpasswordsure").val() ),
		oldPassword: Base64.encode( $("#txt_oldpassword").val() )
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
            }
            else {
            	var info = e.info;
            	if( info === "旧密码不一致" ) {
            		info = "旧密码错误";
            	}
                showPopover(info);
            }
        }
    });
}
