
$(document).ready(function() {
    $('.header .nav .nav-right .items > a[data-type="anli"]')
    .removeClass('normal').addClass('active')
    .attr('href', 'javascript:void(0);')
    .css('cursor', 'default');
    $('.header').css('margin-bottom', '0');

    var param = location.href.split('#')[1];

    if(param && param !== '') {
        switch(param) {
            case 'haier':
                location.href = 'kefu/';
                break;
            case 'zjs':
                location.href = 'fendanbao/';
                break;
            case 'yinghai':
                location.href = 'zxt/';
                break;
        }
    }

    initcss();
    $(window).resize(initcss);
    bindclick();
});
function initcss() {
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );
    var bodywidth = getWindowWidth();
    // $(".pendant").css( { "right":  ( bodywidth*0.5 - 480) } );
    
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/


}
function bindclick() {
    $("#btn_collapse_haier").click(function(){
        $(this).fadeOut(100, function(){
            $("#div_collapse_haier").fadeIn(100);
        });
    });
    $("#btn_hide_haier").click(function(){
        $("#div_collapse_haier").fadeOut(100, function(){
            $("#btn_collapse_haier").fadeIn(100);
        });
    });


    $("#btn_collapse_zjs").click(function(){
        $(this).fadeOut(100, function(){
            $("#div_collapse_zjs").fadeIn(100);
        });
    });
    $("#btn_hide_zjs").click(function(){
        $("#div_collapse_zjs").fadeOut(100, function(){
            $("#btn_collapse_zjs").fadeIn(100);
        });
    });


    $("#btn_collapse_yh").click(function(){
        $(this).fadeOut(100, function(){
            $("#div_collapse_yh").fadeIn(100);
        });
    });
    $("#btn_hide_yh").click(function(){
        $("#div_collapse_yh").fadeOut(100, function(){
            $("#btn_collapse_yh").fadeIn(100);
        });
    });


    $('#btn_saveInfo').click(sendEmail);
    $('.list-stories .list-row .title > span').click(function(){
        var url = $(this).parent('.title').attr('data-url');
        location.href = url;
    })
}

function sendEmail(){ 
    var input_name = $('#txt_c_companyName'), input_mobile = $('#txt_c_mobile');;
    var name = input_name.val();
    var mobile = input_mobile.val();
    if( name == '' || name.length === 0 ) {
        input_name.val('请输入公司名称').css('color', 'red');
        setTimeout(function(){
            input_name.css('color', '#252525').val('');
        }, 1000)
        return;
    }
    if( mobile == '' || mobile.length === 0 ) {
        input_mobile.val('请输入您的联系方式').css('color', 'red');
        setTimeout(function(){
            input_mobile.val('').css('color', '#252525');
        }, 1000)
        return;
    }

    $('#btn_saveInfo').button('loading');  
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.sendEmail,
        data: {
            name: name,
            phone: mobile
        },
        dataType: 'jsonp',
        success: sendEmail_successHandler,
        error: sendEmail_successHandler
    });    
}
function sendEmail_successHandler(e) {
    $('#btn_saveInfo').button('reset');
    if(e && e.success) {
        $('#modal_saveInfo').modal('show');
        $('#txt_c_companyName').val('');
        $('#txt_c_mobile').val('');
    }
}