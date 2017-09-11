var strWrong="账号有误，请重新输入";
var strEmpty="请输入您的用户名";
var strUserName="请输入用户名";
$(document).ready(function(){	
	var userName=$("#username");
	//初始化时进行提示及获得焦点
	userName.attr("placeholder",strUserName).attr("isClick",false);
	if(userName.val()==''){
		userName.focus();
	}	
	//编辑状态不进行错误提示
	$("#password,#username").focus(function(){
		$("#msg").slideUp("fast");
		$(this).removeClass("input_wrong");
	});
	//关闭input自动补全
	$("input").attr("autocomplete", "off");
	//回车提交事件
	$(document).keypress(function(event){
		var e=event?event:(window.event?window.event:null);
		var keyCodeEnter=false;
		if(e&&e.keyCode==13){
			submitForm();
		}
	});
	//错误提示关闭点击
	$(".sso_tip_cross").click(function(){
		$("#msg").slideUp("fast");
	});
	//账号栏操作
	userName.blur(function(){
		var me=$(this);
		//不进行判断，在提交事件中blur后将再次触发judgeUserName方法
		if(me.attr("isClick")=="false"){
			judgeUserName(me);
		}
	}).focus(function(){
		var me=$(this);
		me.removeClass("input_wrong");
		if(me.val()==strWrong||me.val()==strEmpty){
			var content=me.attr("content");
			if(content==strWrong||content==strEmpty){
				content="";
			}
			me.val(content);				
		};
	});
	$('#btn_login').bind('click', submitForm);

	$('#txt_password').blur(User.verifyPassword);
	$('#txt_passwordsure').blur(User.verifyNewPasswordSure);
	$('#btn_resetpwd').click(User.resetPwd);

	$('#btn_upload_license').click( User.uploadLicense );
});

/*
 * 密码框判断及处理
 */
function judgePassWord(){
	var isPass,
	objPassword=$("#password"),
	password=objPassword.val();
	if(password==''){
		objPassword.addClass('input_wrong');
		isPass=false;
	}else{
		isPass=true;
	}	
	return isPass;
}
/*
 * 账号框判断及处理
 */
function judgeUserName(me){
	var isPass,_value=me.val();
	//输入记录
	if(_value!=strWrong&&_value!=strEmpty){
		me.attr("content",_value);
	}
	if (_value!='') {	
		if(_value.indexOf("@")!=-1){
			//邮箱格式
			isPass=checkEmail(_value)?true:false;
		}else{
			//昵称格式
			isPass=checkNickname(_value)?true:false;
		}
		//样式控制
		if(!isPass){
			me.addClass("input_wrong").val(strWrong);
		}
	}
	return isPass;
}
/*
 * email格式检查
 */
function checkEmail(inputValue){
	var checkMailReg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;	
	return inputValue.search(checkMailReg) != -1 ? true:false;
}
/*
 * 昵称格式检查 
 */
function checkNickname(nickname){
	var checkNicknameReg=/^[^\-|"'<>]*$/;
	return nickname.search(checkNicknameReg)!=-1?true:false;
}

/*
 *获取当前url中的某参数值 
 */
function getCurrentQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var parameter = window.location.search.substr(1).match(reg);
	if (parameter != null){
		return parameter[2];
	}else{
		return null;
	}
};
/*
 * form提交（是否阻止form提交，阻止返回false）
 */
function submitForm(){	
	var objUserName=$("#username");
	objUserName.attr("isClick",true);
	$("#password,#username").blur();
	if( !judgeUserName($("#username")) ){
		objUserName.attr("isClick",false);
		return false;
	}

	$('#btn_login').val('正在登录，请稍候...').unbind('click');

	var param = {
		nameOrEmail: objUserName.val(),
		password: $('#password').val()
	}
    $.ajax({
        type: 'GET',
        async: true,
        url: 'user/login?callbacks=?',
        data: param,
        dataType: 'jsonp',
        success: login_successHandler,
        error: login_successHandler
    });
}

/*
 * 登录成功
 */
function login_successHandler(e) {
	if( e && e.key ) {
        location.href = 'welcome/show?key=' + e.key;
	}
    else {
        showPopover('登录失败，用户名或密码错误');
    }

	$('#btn_login').val('登录').bind('click', submitForm);
}

/**
 * 打开结果提示
 */
var timer_popup = null;
function showPopover(string){   
    $("#popover_content").html(string);
    $("#popover_result").css("display", "block");

    if(timer_popup){
        clearTimeout(timer_popup);
    }       

    timer_popup = setTimeout("hidePopover();", 2500);
}

/**
 * 摧毁结果提示
 */
function hidePopover(){
    // $("#table_keys").popover("hide");
    $("#popover_result").css("display", "none");
}

var User = {};
User.showResetPwdModal = function() {	
	$('#modal_resetpwd').modal({
		backdrop: 'static',
		show: true
	});
	$('.span-username').html( $('#username').val() );
}
User.verifyPassword = function() {
	var txt = $('#txt_password').val();
	var txt_sure = $('#txt_passwordsure').val();

	var span = $('#hint_pwd');
	var regNull = /^\s*$/;
	if(txt === "") {
		span.html("请输入密码"  + "<img src='" + PROJECT_URL + "/resources/images/login/wrong.png'></img>");
		return false;
	}
	else if (txt.length < 6 || txt.length > 20) {
        span.html("密码长度必须大于6并小于20"  + "<img src='" + PROJECT_URL + "/resources/images/login/wrong.png'></img>");
        return false;
    } 
	else if( txt_sure !== txt && txt_sure !== '' ) {
		span.html("两次密码输入不一致"  + "<img src='" + PROJECT_URL + "/resources/images/login/wrong.png'></img>");
		return false;		
	}
    else if(regNull.test(txt) || txt.indexOf(" ")>=0){
        span.html("密码中不能包含空格"  + "<img src='" + PROJECT_URL + "/resources/images/login/wrong.png'></img>");
        return false;
    }
    span.html("<img src='" + PROJECT_URL + "/resources/images/login/check.png'></img>");
	return true;
}
User.verifyNewPasswordSure = function() {
	var txt = $('#txt_passwordsure').val();
	var txt_sure = $('#txt_password').val();
	if(txt === "") {
		$('#hint_pwdsure').html("请确认密码"  + "<img src='" + PROJECT_URL + "/resources/images/login/wrong.png'></img>");
		return false;
	}
	else if( txt_sure !== txt ) {
		$('#hint_pwdsure').html("两次密码输入不一致"  + "<img src='" + PROJECT_URL + "/resources/images/login/wrong.png'></img>");
		return false;		
	}
    $('#hint_pwdsure').html("<img src='" + PROJECT_URL + "/resources/images/login/check.png'></img>");
	return true;
}
User.resetPwd = function() {
	var flag = User.verifyPassword();
	if(!flag) {
		return;
	}
	flag = User.verifyNewPasswordSure();
	if(!flag) {
		return;
	}
	
	var param = {
		newPass: $('#txt_passwordsure').val()
	};

    $.ajax({
        type: 'GET',
        async: true,
        url: 'user/update/pass?callbacks=?',
        data: param,
        dataType: 'jsonp',
        success: User.resetPwd_successHandler,
        error: User.resetPwd_successHandler
    });
}
User.resetPwd_successHandler = function(e) {
	if(e && e.flag === 'ok') {
		location.href = 'welcome/show';
	}
	else if(e && e.info && e.info.length > 0) {
		showPopover(e.info);
	}
	else {
		showPopover('密码重置失败');
	}
}

User.showUploadLicenseWindow = function() {
	$('#modal_license').modal({
		backdrop: 'static',
		show: true
	});
}

User.uploadLicense = function() {
	User.checkFileSize();
	$("#form_upload_license").attr('action', 'user/uploadLicense?').ajaxSubmit(function(e) { 
    	if(e && e.isSuccess) {
    		$('.modal').modal('hide');
    		$('#username').val('');
    		$('#password').val('');
    		showPopover('许可导入成功，请重新登录');
    	}
    	else {
    		showPopover('许可导入失败，请重新选择许可文件');    		
    	}
    });
}

User.checkFileSize = function(){    
    var fs = document.getElementById("txt_licenseFiles").files;
    if( !fs || fs.length < 1 ) {
        showPopover('请选择文件');
        return false;
    }
    var filesize = fs[0].size;
    if(filesize >= 3072000) {
        showPopover('文件不能超过3M，请重新选择');
        return false;
    }
    return true;    
}