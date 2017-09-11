
$(function() {
    Page.initcss();
    $(window).resize(Page.initcss);
    Page.bindclick();

    Page.showSolutionBox();

    $('.header .nav .nav-right .items > a[data-type="chanpin"]')
    .removeClass('normal').addClass('active')
    .attr('href', 'javascript:void(0);')
    .css('cursor', 'default');
});

var Page = {};
Page.initcss = function() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();
    var bodyWidth = getWindowWidth();
    $('.fly-right-window').css({ height: bodyHeight });
    $('.fly-right-window-content').css({ height: bodyHeight });
    $('.fly-right-window .bars .solution').css({ top: bodyHeight*0.3});
    $('.fly-right-window .bars .contact-us').css({ top: bodyHeight*0.3 + 130});

    $('#content-right').width( bodyWidth - ( bodyWidth - 1200 )*0.5 - 180 - 40 );
    $('.content-left').css({
        'margin-left': ( (bodyWidth - 1200)*0.5 ) + 'px'
    });

    /*
    var userAgent = window.navigator.userAgent.toLowerCase();
    if( userAgent.match('msie 10') || userAgent.match('trident/7') ) {
        $('.fly-right-window').css('right', '17px');
    }*/
}

Page.bindclick = function() {
    //加入解决方案
    $('a[data-option="put"]').unbind('click').bind('click', Page.addAppForSingle);
    //免费试用
    $('a[data-option="put-free"]').click(function(){
        var me = $(this);
        var id = me.attr("data-id");
        trial.freeTrialClickHandler( id );
        Page.duringFreeTrial(me);
    });

    $('#modal_video').on('shown.bs.modal', Page.play);
    $('#modal_video').on('hidden.bs.modal', Page.pause);

    $('.movie-list ul li.item').click(function(){
        var me = $(this);
        Page.watchVideo(me.find('a[option="watch_video_home"]'));
    });
    //联系客服
    $('button[option="contact-kefu"], a[data-option="kefu"], a[option="watch_video"]').click(function(){        
        $('.modal').modal('hide');
        Page.openServiceWindow();
    });

    // 点击功能包中的免费试用
    $('a[option="trial-free"]').click(function(){
        trial.trialFreeClickHandler();
    });

    // 点击立即试用
    $('button[option="trial-immediately"]').click(function(){
        location.href = urls.swuliu + '/welcome/show?key='+ user.id;
    });

    $('#form_filluser').attr('action', urls.register );
    $('#btn_filluser1').unbind('click').bind('click', function(){
        fillUser();
    });
    $('a[option="filluser"]').unbind('click').bind('click', function(){        
        $('.modal').modal('hide');        
        $('#modal_filluser').modal('show');
        $('.modal-backdrop').css('background-color', '#ccc');
        $('#btn_filluser1').hide();
        $('#btn_filluser').show();
    });
    
    $('.fly-right-window .bars .solution').click(function(){
        var content = $(".fly-right-window-content");
        var content_kefu = $(".fly-right-window-content .kefu");
        var content_bag = $(".fly-right-window-content .bag");

        var visible = content.is(":visible");
        var visible_bag = content_bag.is(":visible");
        if(visible && visible_bag) {
            content.hide();
            $('.fly-right-window').css({right: 0});
        }
        else {
            content.show();
            content_bag.show();
            content_kefu.hide();
            $('.fly-right-window').css({right: 280});
        }
    });   
    
    $('.fly-right-window .bars .contact-us').click(function(){
        var content = $(".fly-right-window-content");
        var content_bag = $(".fly-right-window-content .bag");
        var content_kefu = $(".fly-right-window-content .kefu");

        var visible = content.is(":visible");
        var visible_kefu = content_kefu.is(":visible");
        if(visible && visible_kefu) {
            content.hide();
            $('.fly-right-window').css({right: 0});
        }
        else {
            content.show();
            content_kefu.show();
            content_bag.hide();
            $('.fly-right-window').css({right: 280});
        }
    });    
} 


Page.showSolutionBox = function() {
    var div = $('.fly-right-window-content .solutions');

    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       products = jQuery.parseJSON(c).products;
    } 
    var h = '';
    var len = products.length;
    var fathers = Page.getFathersFromCookie(products);

    if(fathers.length === 0) {
        h =  '<div class="hintno">您好！这里目前是空的哦。</div>';
        h += '<div class="hintno">请在左侧栏定制属于自己的方案。</div>';
        div.html(h);
        return;
    }

    var len_fathers = fathers.length;
    for(var i=0; i<len_fathers; i++ ) {
        var father = fathers[i];

        h += '<div class="row">';
        // h += '  <input type="checkbox" name="checkbox-in-bag" data-id="'+ father.id +'" data-kind="' + father.kind +'" style="margin:0;"></input>';
        var icon = (father.icon && father.icon != '') ? father.icon : 'images/nav/wangdian.gif';
        h += '  <img src="'+ icon +'"></img>';
        h += '  <span class="name" data-id="'+ father.id +'" product="pro-in-bag" data-kind="' + father.kind +'" >'+ father.name +'</span>';
        h += '  <a href="javascript:void(0)" data-option="remove-app" title="删除" data-id="'+ father.id +'"><span class="glyphicon glyphicon-remove" style="cursor: pointer;"></span></a>';
        h += '</div>';
    }

    div.html(h);

    Page.bindSolutionClick();
}

Page.duringFreeTrial = function(btn) {
    btn.html("正在处理...").unbind('click');
}

Page.addAppForSingle = function(){
    var me = $(this);
    Page.addAppForCookie(me.attr("data-id"));
    me.html("已加入").unbind('click').bind('click', Page.removeAppForSingle);
}
Page.removeAppForSingle = function(){
    var me = $(this);
    Page.removeAppFromCookie( me.attr("data-id") );
    var text = me.attr("oldtext") ? me.attr("oldtext") : "加入我的方案";
    me.html(text).unbind('click').bind('click', Page.addAppForSingle);
}
/**
 * 重组cookie数据
 */
Page.getFathersFromCookie = function(products) {
    var data_pros = data_products_v1.concat();
    var data_wuliu = data_apps_wuliu_v1.concat();

    var fathers = [];
    var len = products.length;
    if(len === 0 ) {
        return fathers;
    }

    for(var i=len; i--; ) {
        var id = products[i].id;
        var item = getItemFromArrayByID(id, data_pros[0].list);
        if(item) {
            //kind=0 表示普通产品
            item.kind = 0;
            fathers.push(item);
        }

        var item_wuliu = getItemFromArrayByID(id, data_wuliu);
        if(item_wuliu) {
            //kind=1 表示物流产品
            item_wuliu.kind = 1;
            fathers.push(item_wuliu);            
        }
    }

    return fathers;
}

getItemFromArrayByID = function(id, datas) {
    var len = datas.length;
    if(len === 0) {
        return null;
    }
    for(var i=len; i--; ) {
        var item = datas[i];
        if(id === item.id) {
            return item;
        }
    }
    return null;
}

Page.bindSolutionClick = function() {
    $('a[data-option="remove-app"]').unbind('click').bind('click', Page.removeApp);
}

Page.removeApp = function() {  
    var me = $(this);
    var id = me.attr("data-id");
    Page.removeAppForCookie(id);
}

Page.removeAppForCookie = function(id) {  
    Page.removeAppFromCookie(id);
    Page.resetbtn();
}

Page.removeAppFromCookie = function(id) {
    hideLeftCheckbox(id);
    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);

    if (!c || c === null || c === "") {
        showSolutionBox();
        return;
    } 
    products = jQuery.parseJSON(c).products;

    removeItemByIdFromArray(id, products);  

    var apps = data_apps_wuliu_v1.concat();
    var len = apps.length;
    for(var i=len; i--; ) {
        var item = apps[i];
        if( id === item.id ) {
            var len_kids = item.kids.length;
            if(len_kids < 1) {
                break;
            }
            for(var k=len_kids; k--; ) {
                var kid = item.kids[k];
                Page.removeItemByIdFromArray(kid.id, products); 
            } 
            break;
        }
    }    


    var cookie_kids = {
        products: products
    };
    $.cookie(urls.cookie_products_name, JSON.stringify( cookie_kids ), {
        path: '/',
        expires: 365
    });

    Page.showSolutionBox();

}

Page.addAppForCookie = function(id){    
    var item = {
        id: id,
        s: 0
    };
    handleCookie(item);
    Page.showSolutionBox();  

    Page.openAppWindow(); 
    showLeftCheckbox(id);
}


Page.resetbtn = function() {
    var me = $('[data-option="put"]');
    var text = me.attr("oldtext") ? me.attr("oldtext") : "加入我的方案";
    me.html(text).unbind('click').bind('click', Page.addAppForSingle);
}
Page.resetFreeBtn = function() {
    //免费试用
    $('a[data-option="put-free"]').unbind('click').click(function(){
        var me = $(this);
        var id = me.attr("data-id");
        trial.freeTrialClickHandler( id );
        Page.duringFreeTrial(me);
        me.html("免费试用");
    }).html("免费试用");
}

Page.openServiceWindow = function() {    
    var content = $(".fly-right-window-content");
    var content_bag = $(".fly-right-window-content .bag");
    var content_kefu = $(".fly-right-window-content .kefu");

    content.show();
    content_kefu.show();
    content_bag.hide();
    $('.fly-right-window').css({right: 280});
}
Page.openAppWindow = function() {    
    var content = $(".fly-right-window-content");
    var content_bag = $(".fly-right-window-content .bag");
    var content_kefu = $(".fly-right-window-content .kefu");

    content.show();
    content_kefu.hide();
    content_bag.show();
    $('.fly-right-window').css({right: 280});
}

/**
 * 自动播放
 */
Page.play = function() {    

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
Page.pause = function() {    
    var player = videojs('video');
    if(!player.paused()) {        
        player.pause();
    }
}

/**
 * 关闭窗口后停止播放
 */
Page.stopPlay = function() {    
    var player = videojs('video');
    // player.dispose();
}

/**
 * 观看演示
 */
Page.watchVideo = function(me) {
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


/**
 * 检查是否已存在cookie里面
 */
Page.checkIsBought = function(id) {    
    var products = data_products_v1.concat();
    var data_products_cookie = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       data_products_cookie = jQuery.parseJSON(c).products;
    } 
    return Page.isBoughtMe(id, data_products_cookie);
}

function fillUser(callbacks) {
    var flag = User.verifyUserInfo();
    if(!flag) {
        return false;
    }
    $('#btn_filluser').button('正在处理，请稍候...');

    var param = {
        realName: $("#txt_realname").val(),
        qq: $("#txt_qq").val(),
        mobilePhone: $("#txt_mobile").val(),
        email: $("#txt_email").val(),
        companyName: $("#txt_companyName").val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.register + "&callbacks=?",
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
                if(typeof(callbacks) === "function") {
                    callbacks();
                }
                else {                    
                    trial.fillUserSuccess();
                }
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
        }
    });
}