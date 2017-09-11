

$(document).ready(function() {
    initcss();
    $(window).resize(initcss);
   

    $('#fly_phone').popover({
        html: true,
        content: '<span style="color: #333; width: 100px;display:block;">010-59896519</span>',
        placement: 'top',
        trigger: 'hover'
    });

    $(".trial").hover(
        function(){
            $(this).animate(
                {
                    "margin-right": "4px"
                },
                100, 
                function() {
                }
            );
        },
        function(){
            $(this).animate(
                {
                    "margin-right": "0px"
                },
                100, 
                function() {
                }
            );
        }
    );
    
    $('a[option="trial-free"]').click(function(){
        location.href = "https://sso.isupermap.com/member/register?&service="+ urls.myself + "apps";
    });

    /*$('#modal_video').on('shown.bs.modal', play);
    $('#modal_video').on('hidden.bs.modal', pause);*/
    $('button[option="watch_video"]').click(function(){  
        // watchVideo($('button[option="watch_video"]'));  
        $('.onlineService .ico_os').click();
    });
    /*
    $('.movie-list ul li.item').click(function(){
        var me = $(this);
        watchVideo(me.find('a[option="watch_video_home"]'));
    });*/

    /*$('.intro .item').hover(function() {
        $(this).find('.ifade').fadeIn('200');
    }, function() {
        $(this).find('.ifade').fadeOut('200');        
    });*/

    
    $('.header .content .wangdian .circle').fadeIn(1000, function() {
        $('.header .content .wangdian  .text').fadeIn(1000, function(){
            $('.header .content .wangdian  .balloon').fadeIn(1000, function(){
                setTimeout(function(){                            
                    $('.header .content .wangdian').fadeOut(1000, function(){
                        $('.header .content .quhua').fadeIn(1000, function(){
                            var me = $(this);
                            setTimeout(function(){                            
                                me.fadeOut(500,function(){ 
                                    $('.header .content .right').css('margin-left', "60px");
                                    $('.header .content .road .branch').fadeIn(500, function(){ 
                                        $('.header .content .road').fadeIn(1000, function(){
                                            setTimeout(function(){
                                                $('.header .content .word, .header .content .road .shining').fadeIn(1000);
                                            }, 1500);
                                        });
                                    });
                                });

                            }, 2000);
                        });
                    });
                }, 2000);                
            });
        });
    });
                                    
    

    $('#btn_saveInfo').click(sendEmail);

    $('a[option="register"]').attr('href', urls.regist );
    $('a[option="login"]').attr('href', urls.login );
    $('#btn_trial').click(function(event) {
        location.href = 'apps/';
    });

    $('div[option="go"]').click(function(){
        var me = $(this);
        location.href = me.attr('data-target');
    });
    
    $('a[option="contact"]').click(function(){
        $('.modal').modal('hide');
        $('.onlineService .ico_os').click();
    });

    
});

function initcss() {
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );
    var bodywidth = getWindowWidth();
    $(".pendant").css( { "left":  ( bodywidth*0.5 + 280) } );

    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
}


/**
 * 自动播放
 */
function play() {    

    $('.movie-list').width( $("#modal_video .modal-dialog .modal-body").width() - 701 );

    var player = videojs('video');
    player.load();
    setTimeout(function(){
        player.play();
    }, 700);
}

/**
 * 退出后暂停
 */
function pause() {    
    var player = videojs('video');
    if(!player.paused()) {        
        player.pause();
    }
}

/**
 * 关闭窗口后停止播放
 */
function stopPlay() {    
    var player = videojs('video');
    // player.dispose();
}

/**
 * 观看演示
 */
function watchVideo(me) {
    var player = videojs('video');
    player.autoplay( false );
    var type = me.attr("data-movie");
    var openmodal = me.attr("open-modal");
    if(type !== "all") {        
        var source = data_movies.getMovie(type);
        if( !source ) {
            return;
        }
        $('a[data-list="true"]').each(function(){
            var me = $(this);
            var li = me.parent('li');
            li.removeClass('selected');
            var type = me.attr("data-movie");
            if(type === source[0].data_movie) {
                li.removeClass('item').addClass('selected');
                li.unbind('click');
            }
            else {
                li.addClass('item');
            }
        });

        $('.movie-list ul li.item').unbind('click').bind('click', function(){
            var me = $(this);
            watchVideo(me.find('a[option="watch_video_home"]'));
        });

        player.src(source);
        if(openmodal === "false") {
            setTimeout(function(){
                player.play();
            }, 100);         
        }
    }
    else {
        player.src(data_movies.movies);
        var list = $('a[data-list="true"]');
        list.each(function(){
            var me = $(this);
            var li = me.parent('li');
            li.removeClass('selected');
            var type = me.attr("data-movie");
            if(type === "wangdian") {
                li.addClass('selected');
                me.unbind('click');
            }
            else {
                li.addClass('item');
                me.unbind('click').bind("click", function(){
                    watchVideo(me);
                });
            }
        });
    }
    if(openmodal === "true") {
        $('#modal_video').modal('show');            
    }
}

function sendEmail(){ 

    var name = $('#txt_c_companyName').val();
    var mobile = $('#txt_c_mobile').val();
    if( name == '' || name.length === 0 ) {
        $('#txt_companyName').val('请输入公司名称').css('color', 'red');
        setTimeout(function(){
            $('#txt_companyName').val('').css('color', '#252525');
        }, 1000)
        return;
    }
    if( mobile == '' || mobile.length === 0 ) {
        $('#txt_mobile').val('请输入您的联系方式').css('color', 'red');
        setTimeout(function(){
            $('#txt_companyName').val('').css('color', '#252525');
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
        $('#txt_companyName').val('');
        $('#txt_mobile').val('');
    }
}
