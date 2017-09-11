
/**
 * 页面初始化
 */
$(function(){
	initcss();
	$(window).resize(function(){
		initcss();		
	});
	var input_username = $("#username");
	/*$("#login_form").validate({
		rules:{
		 	username:{
				required:true,
		 		usernameRule: false
	 		}
		}
	});*/

	var input_password = $("#password");
	var mycookiestr = jQuery.cookie('cookie_egispbossusername');
	if (mycookiestr) {
		var mycookie = jQuery.parseJSON( mycookiestr );
		$("#checkbox_remember_me").prop("checked", mycookie.checked);	
		if(mycookie.checked) {
			input_username.val(mycookie.name);
			input_password.focus();		
		}
		else {
			input_username.focus();
		}
	} else {
		input_username.focus();
	}

	$('input').placeholder();
	input_username.bind("keyup", enter);
	input_password.bind("keyup", enter);
	$("#btn_login").bind("click", login);

	$("#checkbox_remember_me").change(function() {
		var me = $(this);
		var selected = me.prop("checked");
		var str = JSON.stringify({name: input_username.val(), checked: selected});
		jQuery.cookie('cookie_egispbossusername', str, {path:"/"});
	});
});

function initcss() {
	var bodyHeight = getWindowHeight();
	$('.login-content').height(bodyHeight - 48 - 46 - 30);
}

/**
 * 提交登录信息
 */
function enter(e) {
	e =  e ? e : window.event;
	if(e.keyCode == 13){
		login();
	}
}

/**
 * 用户登录
 */
function login() {
	var username = $("#username").val();
	var password = $("#password").val();
	var span = $('.login-info');

	if(username === "" || username.length === 0 || username === "输入用户名") {
		showPopover("用户名不能为空");
		return;
	}
	
	if(password === "" || password.length === 0 || password === "输入密码") {
		showPopover("密码不能为空");
		return;
	}
	
	var selected = $("#checkbox_remember_me").prop("checked");
	var cookie_name = 'cookie_egispbossusername';

	var str = JSON.stringify({name: username, checked: selected});
	jQuery.cookie(cookie_name, str, {path:"/"});

	var url = urls.employee.login;
	span.html("正在登录，请稍候...");
	$.ajax({
        type: 'GET',
        async: true,
        url: url,
        data: {
        	username: username,
        	password: Base64.encode( password )
        },
        dataType: 'jsonp',
        success: function(e){
        	span.html("");
        	if(e && e.success) {
        		location.href = "eboss/user/index.html";
        	}
        	else {
        		showPopover( e.info );
        	}
        }        
    });
}








