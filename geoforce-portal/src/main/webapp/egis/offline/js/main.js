
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);

    $('.header .nav .nav-right .items > a[data-type="lixian"]')
    .removeClass('normal').addClass('active')
    .attr('href', 'javascript:void(0);')
    .css('cursor', 'default');

    $('a[option="license"]').attr("target", "_blank").click(getLicense);
});

function initcss() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();

    $(".content-body").css( {"min-height":(bodyHeight - 105 - 45 - 60), "overflow": "hidden"} );
    
}

function getLicense() {  
    //验证是否登陆
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: function(e){            
            if(e && e.success && e.result) {
                var me = e.result;
                user = me;
                if(!user.id || user.id === "" || user.id.length === 0) {
                    $('#modal_filluser').modal('show');
                }
                else {                    
                    window.open( urls.server + "/userService?method=requestLicense", "地图慧-离线许可下载", "_blank" );
                }
            }
            else {
                location.href = urls.login;
            }
        }
    });
}

/**
 * 补充用户信息
 */
User.fillUser = function() {
    var flag = User.verifyUserInfo();
    if(!flag) {
        return false;
    }

    $('#btn_filluser').button('正在处理，请稍候...');

    var param = {
        realName: $("#txt_realname").val(),
        qq: $("#txt_qq").val(),
        telephone: $("#txt_mobile").val(),
        email: $("#txt_email").val(),
        companyName: $("#txt_companyName").val(),
        username: user.username,
        admincode: $('.user-location').val(),
        adminname: $('.user-location').find('option:selected').text(),
        combusiness: $('.user-solutions').val(),
        businessremark: $('.text-user-solutions').val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.register + "&callbacks=?",
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
                $('.modal').modal('hide');
                showPopover('信息补充成功');
                User.refreshHeader();

                window.open( urls.server + "/userService?method=requestLicense" );
            }
            else {         
                var info = e.info;
                if( info.match("已使用") && info.match("email") ) {                    
                    scrollto("#txt_email");
                    $("#hint_email").html("该邮箱已被使用");
                }
                else {
                    showPopover(e.info);
                }
            }
        }
    });

    /*$("#form_filluser").ajaxSubmit(function(data) { 
        var e = eval('(' + data + ')');
        if(e && e.success) {
            showPopover('补充信息成功');  
            $('.modal').modal('hide');
            User.refreshHeader();         
        }
        else {         
            var info = e.info;
            if( info.match("已使用") && info.match("email") ) {                    
                scrollto("#txt_email");
                $("#hint_email").html("该邮箱已被使用");
            }
            else {
                showPopover(e.info);
            }
        }
    });
    return;*/
}