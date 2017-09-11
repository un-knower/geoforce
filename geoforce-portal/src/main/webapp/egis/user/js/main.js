
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);
    load();

    $('input').placeholder();

    $("#txt_user_realName").blur(verifyRealname);
    $("#txt_user_company").blur(verifyCompanyName);

    $(".text-user-solutions").blur(User.verifySolutions);
    $(".user-location").change(User.verifyLocation);
    $(".user-solutions").change(User.verifySolutions);

    $('#btn_update_user').click(function(){
        $('#modal_update_user').modal('show');
    })
});

function initcss() {
    // var bodyheight = getWindowHeight();
    // $(".content").css({ "height": (bodyheight - 105 - 45), "min-height": 620 });
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );
    
    /*
    var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }
    */
}

function load() {
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success && e.result) {
                var me = e.result;
                user = me;
                if(!me.realName) {
                    location.href = "../";
                    return;
                }

                $("#nav_user").html( me.username );

                $("#span_me_realName").html(me.realName ? me.realName : "暂无详细信息");
                $("#span_me_username").html(me.username ? me.username : "暂无详细信息");

                if( me.telephone ) {
                    me.telephone = me.telephone.replace(/[\r\n]/g, "");
                    me.telephone = $.trim(me.telephone);
                }
                if( !me.telephone || me.telephone.length < 1 ) {
                    me.telephone = "暂无详细信息";
                }
                $("#span_me_mobilePhone").html(me.telephone);
                $("#span_me_company").html(me.companyName ?　me.companyName : "暂无详细信息");
                $("#span_me_adminname").html( me.adminname ? me.adminname : '暂无详细信息' );
                $("#span_me_solutions").html( me.combusiness ? me.combusiness : '暂无详细信息');

                $("#txt_user_realName").val( me.realName );
                $("#txt_user_company").val( me.companyName );
                $("#txt_user_adminname").val( me.adminname);

                $('.user-location').val(me.admincode ? me.admincode : '-1');

                if(me.combusiness.match('其他')){
                    me.combusiness =  '其他';
                    $(".text-user-solutions").removeClass('hide');
                }
                
                $('.user-solutions').val(me.combusiness  );
                $('.text-user-solutions').val(me.businessremark ? me.businessremark : '');
            }
            else {    
                showPopover("用户未登录");
                setTimeout("location.href='../'", 1500);
            }
        }
    });
}


/**
 * -验证手机号
 */
function verifyMobile(){
    var txt = $("#txt_user_mobile").val();
    var span = $("#span_tip_mobile").css("color", "red");

    if(txt == "") {                 
        span.html("请填写手机号码");
        return false;
    }
    var reg = /1[3,5,6,8]\d{9}$/;
    if(reg.test(txt)) {                 
        span.html('');
        return true;                
    }
    span.html('手机号码示例:13100000000');
    return false;
}




/**
 * 添加用户-验证QQ
 */
function verifyQQ() {
    var txt = $("#txt_user_qq").val();
    var span = $("#span_tip_qq").css("color", "red");;

    if(txt === "") {                 
        span.html("请填写QQ");
        return false;
    }

    var reg = new RegExp('^[0-9]*$');
    if(!reg.test(txt)) {             
        span.html("QQ号码格式不正确");
        return false;
    }
    span.html( "");
    return true;
}

/**
 * 添加用户-验证公司名称
 */
function verifyCompanyName() {
    var txt = $("#txt_user_company").val();
    var span = $("#span_tip_company").css("color", "red");;

    if(txt === "") {                 
        span.html("请填写公司名称");
        return false;
    }
    span.html("<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 添加用户--验证真实姓名
 */
function verifyRealname() { 
    var txt = $("#txt_user_realName").val();
    var span = $("#span_tip_realName").css("color", "red");;
    
    var reg = /^(?!\s)([\u4E00-\u9FA0]+$|^[a-z·\s]+)$/i;
    if(txt == "") {
        span.html("联系人姓名不能为空");
        return false;
    }
    if(!reg.test(txt)){
        span.html("联系人姓名只能为中文或者英文");
        return false;
    }
    if(txt.length<2 || txt.length>8){
        span.html("联系人姓名长度为2-8位");
        return false;
    }
    span.html("<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 添加用户-验证企业邮箱
 */
verifyCompanyEmail = function() {
    var txt = $("#txt_user_companyEmail").val();
    var span = $("#span_tip_companyEmail");
    if( txt == "" ) {   
        span.html( "");
        return true;        
    }

    var reg = /[a-zA-Z0-9]+@\w+[\.][a-z]{2,3}$|[a-zA-Z0-9]+@\w+[\.][a-z]{2,3}[\.][a-z]{2,3}$/;            
    if(reg.test(txt) && txt.split("@").length === 2) {    
        span.html( "");
        return true;
    }
    span.html('邮箱格式不正确');
    return false;
}


function updateUser() {       
    flag = verifyRealname();
    if(!flag) {
        return flag;
    }

    flag = verifyCompanyName(); 
    if(!flag) {
        return flag;
    }
    flag = User.verifyLocation();  
    if(!flag) {
        return flag;
    }
    flag = User.verifySolutions();  
    if(!flag) {
        return flag;
    }

    var param = {
        admincode: $('.user-location').val(),
        adminname: $('.user-location').find('option:selected').text(),
        combusiness: $('.user-solutions').val(),
        businessremark: $('.text-user-solutions').val(),
        realName: $("#txt_user_realName").val(),
        companyName: $("#txt_user_company").val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userupdate,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
                showPopover("更新成功");
                $("#modal_update_user").modal("hide");
                load();
                $('.span-tip').html('');
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

function verifypassword(){
    var flag = false;
    flag = verifyOldPassword();
    if(flag === false) {
        return flag;
    }
    flag = verifyNewPassword();
    if(flag === false) {
        return flag;
    }
    flag = verifyNewPasswordSure(); 
    return flag;
} */
/**
 * 验证输入的旧密码

function verifyOldPassword() {
    var txt = $('#txt_oldpassword').val();
    var span = $('#hint_pwdold');
    if(txt === "") {
        span.html("请输入旧密码").css("color", "red");
        return false;
    }
    span.html('');
    return true;
} */

/**
 * 验证输入的新密码

function verifyNewPassword() {
    var txt = $('#txt_newpassword').val();
    var span = $('#hint_pwd');

    var regNull = /^\s*$/;
    var psdVali = $("#txt_newpasswordsure").val();

    span.css("color", "red");
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
        $("#hint_pwdsure").html("两次密码输入不一致");
        return false;
    }
    span.html("");
    $("#hint_pwdsure").html("");
    return true; 
} */

/**
 * 验证确认的新密码

function verifyNewPasswordSure() {
    var txt = $("#txt_newpasswordsure").val();
    var pwd = $("#txt_newpassword").val();
    var span = $("#hint_pwdsure");
    span.css("color", "red");

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

function resetPassword() {
    var flag = verifyOldPassword();
    if(!flag) {
        return;
    }
    flag = verifyNewPassword();
    if(!flag) {
        return;
    }
    flag = verifyNewPasswordSure();
    if(!flag) {
        return;
    }
    var span = $("#hint_resetpwd");
    span.html("正在处理，请稍候...").css("color", "#999");
    var param = {
        oldPassword: Base64.encode( $('#txt_oldpassword').val() ),
        password: Base64.encode( $('#txt_newpassword').val() )
    };

    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userresetpwd,
        data: param,
        dataType: 'jsonp',
        success: function(e){               
            if(e && e.success) {
                span.html("");
                showPopover("修改成功");
                $('#txt_oldpassword').val('');
                $('#txt_newpassword').val('');
                $('#txt_newpasswordsure').val('');                
            }
            else {
                if(e.info) {
                    span.html(e.info).css("color", "red");
                }
                else{
                    span.html("修改密码失败").css("color", "red");                    
                }
            }
        }
    });
}
 */