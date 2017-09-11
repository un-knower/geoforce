
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);
    bindclick();

    $('.header .nav .nav-right .items > a[data-type="anli"]')
    .removeClass('normal').addClass('active')
    .attr('href', 'javascript:void(0);')
    .css('cursor', 'default');
});
function initcss() {
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );
    var bodywidth = getWindowWidth();
    $('.intro-right').css({
        right: ((bodywidth - 1100)*0.5 + 60 ) + 'px'
    }).show();
}
function bindclick() {
    $('#btn_saveInfo').click(sendEmail);
    $('.intro-right ul > li').click(function(){
        $('.intro-right ul > li.action').removeClass('action').addClass('normal');
        var me = $(this).removeClass('normal').addClass('action');
        var selector = 'p.title-a[data-scroll="' + me.attr('data-scroll') + '"]';
        var e=$(selector).offset().top - 100;
        $("html,body").animate({scrollTop:e},382)
    });

    window.onscroll = function(){
        var scrollTop = $(document).scrollTop();
        var bodywidth = getWindowWidth();

        //右侧滚动监听菜单位置
        var spyTop = scrollTop > 90 ? (scrollTop < 200 ? 110 : 15) : 200;
        var div = $('.intro-right');
        div.css({ 
            position: 'fixed',
            'margin' : 0,
            right: ((bodywidth - 1100)*0.5 + 60 ) + 'px',
            top: spyTop + 'px'
        });

        if(scrollTop < 386) {
            $('.intro-right ul > li.action').removeClass('action').addClass('normal');
            $('.intro-right ul > li:first-child').removeClass('normal').addClass('action');
            return false;
        }

        var max = $('.contact').offset().top - 500;

        if(scrollTop >= max) {
            if(div.css('position') != 'relative') {
                var marginTop = $('p.title-a[data-scroll="content-4"]').offset().top;
                div.css({ 
                    position: 'relative',
                    'margin': (marginTop - 200) + 'px 0 0 0',
                    'right': '0'
                });                
            }
        }
        /*else {
            div.css({ 
                position: 'fixed',
                'margin' : 0,
                right: ((bodywidth - 1100)*0.5 + 60 ) + 'px',
                top: spyTop + 'px'
            });
        }*/

        $('p.title-a').each(function(){
            var me = $(this);
            var myTop = me.offset().top - 180;
            if(scrollTop >= myTop && !me.hasClass('action')) {
                $('.intro-right ul > li.action').removeClass('action').addClass('normal');
                $('.intro-right ul > li[data-scroll="'+ me.attr('data-scroll') +'"]').removeClass('normal').addClass('action');
            }
        });
    }
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