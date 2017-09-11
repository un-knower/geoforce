
$(function() {
    initcss();
    $(window).resize(initcss);
    bindclick();

    showSolutionBox();

    var param = urls.getUrlArgs();
    if(param && param.sm) {
        changeFrame(param.sm);
    }

   /* $(window).scroll(function(e){     
        if( window.frames[0].Scroll ) {
            window.frames[0].Scroll.window_scrollHandler( $(window).scrollTop() );
        }
    });*/

    $('.header .nav .nav-right .items > a[data-type="chanpin"]')
    .removeClass('normal').addClass('active')
    .attr('href', 'javascript:void(0);')
    .css('cursor', 'default');

});

function changeFrame(id) {
    var src = getFrameSrc( id );
    $("#content_frame").attr("src", src); 

    //点击a换白色背景
    var a_all = $("#LeftNav a");
    var current = $('a[data-id="'+ id +'"]');
    if( current.length > 0 ) {            
        a_all.removeClass('link-ecs');
        current.addClass('link-ecs');
    }
}

function initcss() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();
    var bodyWidth = getWindowWidth();
    $('.fly-right-window').css({ height: bodyHeight });
    $('.fly-right-window-content').css({ height: bodyHeight });
    $('.fly-right-window .bars .solution').css({ top: bodyHeight*0.3});
    $('.fly-right-window .bars .contact-us').css({ top: bodyHeight*0.3 + 130});

    $('#content-right').width( bodyWidth - ( bodyWidth - 1200 )*0.5 - 180 - 25 );

/*
    var userAgent = window.navigator.userAgent.toLowerCase();
    if( userAgent.match('msie 10') || userAgent.match('trident/7') ) {
        $('.fly-right-window').css('right', '17px');
    }*/
}

function bindclick() {
    $('.close-fly-window').click(function(){
        $(".fly-right-window-content").hide();
        $('.fly-right-window').css({right: 0});
        $('.close-fly-window').hide();
    });
    $('.fly-right-window .bars .solution').click(function(){
        var content = $(".fly-right-window-content");
        var content_kefu = $(".fly-right-window-content .kefu");
        var content_bag = $(".fly-right-window-content .bag");

        var visible = content.is(":visible");
        var visible_bag = content_bag.is(":visible");
        if(visible && visible_bag) {
            content.hide();
            $('.close-fly-window').hide();
            $('.fly-right-window').css({right: 0});
        }
        else {
            $('.close-fly-window').show();
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
            $('.close-fly-window').hide();
            $('.fly-right-window').css({right: 0});
        }
        else {
            $('.close-fly-window').show();
            content.show();
            content_kefu.show();
            content_bag.hide();
            $('.fly-right-window').css({right: 280});
        }
    });    

    $('#modal_video').on('shown.bs.modal', play);
    $('#modal_video').on('hidden.bs.modal', pause);

    $('.movie-list ul li.item').click(function(){
        var me = $(this);
        watchVideo(me.find('a[option="watch_video_home"]'));
    });

    /*
    $('#txt_realname').blur( verifyRealname );
    $('#txt_mobile').blur( verifyMobile );
    $('#txt_companyName').blur( verifyCompanyName );
    $('#txt_qq').blur( verifyQQ );
    */

    //联系客服
    $('button[option="contact-kefu"]').click(function(){        
        $('.modal').modal('hide');
        openServiceWindow();
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
} 


function showSolutionBox() {
    var div = $('.fly-right-window-content .solutions');

    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       products = jQuery.parseJSON(c).products;
    } 
    var h = '';
    var len = products.length;
    var fathers = getFathersFromCookie(products);

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

    bindSolutionClick();
}


/**
 * 重组cookie数据
 */
function getFathersFromCookie(products) {
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

function getItemFromArrayByID(id, datas) {
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

function bindSolutionClick() {
    $('a[data-option="remove-app"]').unbind('click').bind('click', removeApp);
}

function removeApp() {  
    var me = $(this);
    var id = me.attr("data-id");
    removeAppForCookie(id);
}

function removeAppForCookie(id) {  
    removeAppFromCookie(id);
    var c = jQuery.cookie(urls.cookie_products_name);
    if( !(!c || c === null || c === "") ) {        
        products = jQuery.parseJSON(c).products;
        if(products.length === 0) {
            window.frames[0].resetBtns();
        }
        else {
            window.frames[0].resetbtn(id);
        }
    } 
    else {
        window.frames[0].resetBtns();
    }
}

function removeAppFromCookie(id) {
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
            var len_kids = item.kids ? item.kids.length : 0;
            if(len_kids < 1) {
                break;
            }
            for(var k=len_kids; k--; ) {
                var kid = item.kids[k];
                removeItemByIdFromArray(kid.id, products); 
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

    showSolutionBox();

}

function addAppForCookie(id){    
    var item = {
        id: id,
        s: 0
    };
    handleCookie(item);
    showSolutionBox();  

    openAppWindow(); 
    showLeftCheckbox(id);
}

function addWuliuAppForCookie(id) {
    var apps = data_apps_wuliu_v1.concat();
    var len = apps.length;
    for(var i=len; i--; ) {
        var item = apps[i];
        if( id === item.id ) {
            //type=1表示物流产品
            var o = {
                id: item.id,
                s: 0
            };
            handleCookie(o);
            var len_kids = item.kids ? item.kids.length : 0;
            if(len_kids < 1) {
                break;
            }
            for(var k=len_kids; k--; ) {
                var kid = item.kids[k];
                var o = {
                    id: kid.id,
                    s: 0
                };
                handleCookie(o);
            } 

            break;
        }
    }    
    showSolutionBox();  

    openAppWindow(); 
}

function addWuliuAppsForCookie() {
    var apps = data_apps_wuliu_v1.concat();
    var len = apps.length;
    for(var i=len; i--; ) {
        var item = apps[i];
        var o = {
            id: item.id,
            s: 0
        };
        handleCookie(o);
        var len_kids = item.kids ? item.kids.length : 0;
        if(len_kids < 1) {
            continue;
        }
        for(var k=len_kids; k--; ) {
            var kid = item.kids[k];
            var o = {
                id: kid.id,
                s: 0
            };
            handleCookie(o);
        } 
    }    
    showSolutionBox();  
    openAppWindow(); 
}

function removeWuliuAppsForCookie() {
    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);

    if (!c || c === null || c === "") {
        showSolutionBox();
        return;
    } 
    products = jQuery.parseJSON(c).products;

    var apps = data_apps_wuliu_v1.concat();
    var len = apps.length;
    for(var i=len; i--; ) {
        var item = apps[i];
        removeItemByIdFromArray(item.id, products);  

        var len_kids = item.kids ? item.kids.length : 0;
        if(len_kids < 1) {
            continue;
        }
        for(var k=len_kids; k--; ) {
            var kid = item.kids[k];
            removeItemByIdFromArray(kid.id, products); 
        } 
    }    


    var cookie_kids = {
        products: products
    };
    $.cookie(urls.cookie_products_name, JSON.stringify( cookie_kids ), {
        path: '/',
        expires: 365
    });

    showSolutionBox(); 
}

function openServiceWindow() {    
    var content = $(".fly-right-window-content");
    var content_bag = $(".fly-right-window-content .bag");
    var content_kefu = $(".fly-right-window-content .kefu");

    content.show();
    content_kefu.show();
    content_bag.hide();
    $('.fly-right-window').css({right: 280});
}
function openAppWindow() {    
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


/**
 * 检查是否已存在cookie里面
 */
function checkIsBought(id) {    
    var products = data_products_v1.concat();
    var data_products_cookie = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       data_products_cookie = jQuery.parseJSON(c).products;
    } 
    return isBoughtMe(id, data_products_cookie);
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