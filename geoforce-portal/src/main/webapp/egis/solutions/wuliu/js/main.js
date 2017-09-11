
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);

    $('a[option="put-wuliu-free"]').click(function(){
        var me = $(this);
        trial.trialFreeWuliu( me.attr('data-id') );
    });
    $('#btn_filluser1').unbind('click').bind('click', function() {
        trial.fillUser(function(){            
            trial.trialFreeWuliu( $('a[option="put-wuliu-free"]').attr('data-id') );
        });
    });
    // 点击立即试用
    $('button[option="trial-immediately"]').click(function(){
        location.href = urls.swuliu + '/welcome/show?key='+ user.id;
    });

    //联系客服
    $('button[option="contact-kefu"]').click(function(){        
        $('.modal').modal('hide');
        $('.onlineService').hide();
        $('.box_os').show();
    });
    

    $('.header .nav .nav-right .items > a[data-type="hangye"]')
    .removeClass('normal').addClass('active')
    .attr('href', 'javascript:void(0);')
    .css('cursor', 'default');
    $('.header').css('margin-bottom', '0');
});
function initcss() {
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodywidth = getWindowWidth();
    $(".pendant").css( { "left":  ( bodywidth*0.5 - 100) } );
    
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
}