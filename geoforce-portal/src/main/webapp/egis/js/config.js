var urls = {};
urls.server = "/egispportal";

// urls.server = "http://192.168.10.251:8070/egispportal";


//当前IP+端口+工程路径
urls.myself = "http://" + location.host + "/";

urls.swuliu = "/egispweb";
urls.cookie_products_name = 'cookie_suprmapcloud_chaotuyun_products';
// urls.register = urls.server + "/userService?method=sup&callbacks=?";
urls.register = urls.server + "/userService?method=sup";
urls.register_full = urls.server + "/regist?&callbacks=?";

//注册
urls.regist = 'http://c.dituhui.com/signup?&redirect_url='+ location.href;

urls.login = urls.server + "/userService?method=login&callbacks=?&service=" + location.href;
// urls.login = urls.server + "/userService?";
urls.logout_first = "http://sso.dituhui.com/logout?gateway=true&destination=" + location.href;

urls.logout = urls.server + "/logout?&callbacks=?";

urls.userdetail = urls.server + "/queryUserInfo?&callbacks=?";
urls.userupdate = urls.server + "/userService?method=updateUser&callbacks=?";
urls.userresetpwd = urls.server + "/userService?method=changePassword&callbacks=?";
urls.tradesearch = urls.server + "/orderService?method=queryOrderList&callbacks=?";
urls.tradesdetail = urls.server + "/orderService?method=queryOrderDetails&callbacks=?";
urls.tradesremove = urls.server + "/orderService?method=deleteOrder&callbacks=?";
urls.trade_buy = urls.server + "/orderService?method=addOrder";
urls.products_search = urls.server + "/moduleService?method=queryServiceList&callbacks=?";
urls.products_detail = urls.server + "/moduleService?method=queryModule&callbacks=?";
urls.sendEmail = urls.server + "/mailService?&callbacks=?";
urls.getnotecode = urls.server + "/userService?method=phoneService&callbacks=?";
urls.bindphone = urls.server + "/userService?method=bindPhone&callbacks=?";

var user = null;

//获得浏览器地址中的参数
urls.getUrlArgs = function(){
    var url = location.search; 
    var theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for ( var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    } 
    else {
        theRequest = null;
    }
    return theRequest;    
}

urls.getSelectedAppsLength = function() {
    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       products = jQuery.parseJSON(c).products;
    } 
    var len = products.length;

    var fathers = [];
    if(len === 0 ) {
        return 0;
    }
    for(var i=len; i--; ) {
        var item = products[i];

        var repeat = checkRepeat(item.pid, fathers);
        if( !repeat ) {
            var father = {
                id: item.pid ? item.pid : item.id, 
                name: item.pname ? item.pname : item.name,
                children: []
            }
            fathers.push(father);
        }
    }
    var len_fathers = fathers.length;
    if(len_fathers === 0) {
        return 0;
    }

    for(var j=len; j--; ) {
        var item = products[j];
        for(var k=len_fathers; k--; ) {
            var f = fathers[k];
            if(item.pid === f.id && item.pid !== item.id ) {
                f.children.push(item);
                fathers[k] = f;
                break;
            }
        }
    }
    var appsLength = 0;
    for(var k=len_fathers; k--; ) {
        var f = fathers[k];
        appsLength += (f.children.length > 0 ? f.children.length : 1);
    }

    return appsLength;
}


/**
 * 查重
 */
function checkRepeat(id, records) {
    var len = records.length;
    if(len === 0) {
        return false;
    }
    for(var i=len; i--; ) {
        var r = records[i];
        if( id === r.id) {
            return true;
        }
    }
    return false;
}

//系统管理
urls.systemmges = [
    //系统管理
    '40288e9f4844872a014844872fd50000',
    '40288e9f48448d8a0148448d90120000',
    '40288e9f48448d8a0148448d90690001',
    '40288e9f48448d8a0148448d906e0002',
    //主页
    '40288e9f48a123e80148a1240e330004'
];
//主页
urls.homepagemges = [
];

String.prototype.trim = function(){  
　 return this.replace(/(^\s*)|(\s*$)/g, "");  
}  

//获取页面显示区域高度
function getWindowHeight() {
	if (navigator.appName == "Microsoft Internet Explorer" ) {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
	}
	else {
		return self.innerHeight;
	} 
}

//获取页面显示区域宽度
function getWindowWidth() {
	if (navigator.appName == "Microsoft Internet Explorer") {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
	}
	else {
		return self.innerWidth;
	} 
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


jQuery.cookie = function(name, value, options) {
	if (typeof value != 'undefined') { // name and value given, set cookie
		options = options || {};
		if (value === null) {
			value = '';
			options.expires = -1;
		}
		var expires = '';
		if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
			var date;
			if (typeof options.expires == 'number') {
				date = new Date();
				date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
			} else {
				date = options.expires;
			}
			expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
		}
		var path = options.path ? '; path=' + options.path : '';
		var domain = options.domain ? '; domain=' + options.domain : '';
		var secure = options.secure ? '; secure' : '';
		document.cookie = [name, '=', encodeURIComponent(value), expires, path,
			domain, secure
		].join('');
	} else { // only name given, get cookie
		var cookieValue = null;
		if (document.cookie && document.cookie != '') {
			var cookies = document.cookie.split(';');
			for (var i = 0; i < cookies.length; i++) {
				var cookie = jQuery.trim(cookies[i]);
				// Does this cookie string begin with the name we want?
				if (cookie.substring(0, name.length + 1) == (name + '=')) {
					cookieValue = decodeURIComponent(cookie
						.substring(name.length + 1));
					break;
				}
			}
		}
		return cookieValue;
	}
};

/**
 * 生成页头
 */
function createHeader_v1(){
    $('.header .nav-right .items').html( Header.getNavMenus() );
    
    $('.header .logo img').css('cursor', 'pointer').click(function(){
        location.href = 'http://www.dituhui.com';
    });
    $('.header .logo span').css('cursor', 'pointer').click(function(){
        location.href = '/';
    });

    var p =  '<div id="popover_result" class="popover fade top in" style="">';
        p += '<div class="popover-content" id="popover_content"></div>';
        p += '</div>';
    $('body').append(p);

    //验证是否登陆
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: login_successHandler
    });
}

//处理用户登录结果
function login_successHandler(e) {
    if(e && e.success && e.result) {
        var me = e.result;
        user = me;
        if(!user.id || user.id === "" || user.id.length === 0) {
            $(".header .nav .nav-right .items .login").html( Header.getUserFillHtml() );
            Header.bindUserFillClick();
            Header.bindUserMseClick();
        }
        else {
            $(".header .nav .nav-right  .items .login").html( Header.getUserHtml() );
            Header.bindUserMseClick();
        }
    }
}

/**
 * 页首
 */
var Header = {};
/**
 * 获取url参数
 */
function getUrlParam(name) {  
         var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");  
         var r = window.location.search.substr(1).match(reg);  
         if (r!=null)   
             return unescape(r[2]);   
         return null;  
  }
/**
 * 生成导航栏
 */
Header.getNavMenus = function() { 
    var pageName=getUrlParam("pageName");  
    var h =  '<a data-type="chanpin" class="normal" href="'+ urls.myself +'apps/">产品</a>';
        h += '<a data-type="hangye"  class="normal" href="'+ urls.myself +'solutions/">行业</a>';
//      h += '<a data-type="ditu" class="normal" href="'+ urls.myself +'map/">地图</a>';
        h += '<a data-type="lixian" class="normal" href="'+ urls.myself +'offline/">离线</a>';
        h += '<a data-type="anli" class="normal" href="'+ urls.myself +'story/">案例</a>';
        h += '<a data-type="hezuo" class="normal" href="'+ urls.myself +'partner/">合作</a>';
        //帮助中心和意见反馈去掉登录
        if(!(pageName=="help"||pageName=="feedback")){
            h += '<div class="login">';
            h += '  <a href="'+ urls.regist +'">注册</a><i></i>';
            h += '  <a href="'+ urls.login +'">登录</a>';
            h += '</div>';
        }
    return h;
}

/**
 * 登录后需要填写用户详细信息
 */
Header.getUserFillHtml = function() {
    var h =  '<div class="mse_menu">';
        h += '   <a href="javascript:void(0);"><span>'+ (user.username ? user.username : user.email) +'</span></a>';
        h += '   <span class="caret"></span>';
        h += '   <div class="mse_full">';
        h += '      <div class="icaret"></div>';
        h += '      <ul>';
        h += '          <li><a href="javascript:void(0);" option="filluser"><img src="/images/user-nav.png">补充信息</a></li>';
        h += '          <li><a href="javascript:logout();"><img src="/images/logout.png">退出</a></li>';
        h += '      </ul>';
        h += '  </div>';
        h += '</div>';
    return h;
}

/**
 * 登录后显示用户菜单
 */
Header.getUserHtml = function() {    
    var me = user;
    var h =  '<div class="mse_menu">';
        h += '   <a href="javascript:void(0);"><span>'+ (me.username ? me.username : me.email) +'</span></a>';
        h += '   <span class="caret"></span>';
        h += '   <div class="mse_full">';
        h += '      <div class="icaret"></div>';
        h += '      <ul>';
        h += '          <li><a href="'+ urls.myself +'user/"><img src="/images/user-nav.png">我的资料</a></li>';
        h += '          <li><a href="'+ urls.myself +'trade/"><img src="/images/trade.png">历史方案</a></li>';
        h += '          <li><a href="'+ urls.swuliu +'/welcome/show?key='+ me.id +'"><img src="/images/system.png">进入系统</a></li>';
        h += '          <li><a href="javascript:logout();"><img src="/images/logout.png">退出</a></li>';
        h += '      </ul>';
        h += '  </div>';
        h += '</div>';
    return h;
}
/**
 * 用户填写个人信息-文本框等绑定鼠标事件
 */
Header.bindUserFillClick = function() {
    $('a[option="filluser"]').click( User.showFillUserModal );

    $('#txt_realname').blur( User.verifyRealname );
    $('#txt_companyName').blur( User.verifyCompanyName );
    $('#txt_companyEmail').blur( User.verifyCompanyEmail );
    $('#btn_filluser').click( User.fillUser );
    $('#txt_email').val( user.email );
    if( user.telephone && user.telephone.trim().length > 0 ) {
        $('#txt_mobile').val( user.telephone ).attr("readonly", "true");
    }
    else {        
        $('#txt_mobile').blur( User.verifyMobile );
        $('.note-code').css({"visibility": "visible"});
        $('#btn_getNoteCode').click(User.getNoteCode);
    }

    $('#form_filluser').attr('action', urls.register );
}
/**
 * 鼠标移至用户名上显示菜单
 */
Header.bindUserMseClick = function() {    
    var $menu = $('.header .login .mse_menu');

    $menu.hover(function() {
        var me = $(this);
        me.find('.mse_full').fadeIn('fast');
    }, function() {        
        var me = $(this);
        me.find('.mse_full').fadeOut('fast');
    });

    $('.header .logo img').css('cursor', 'pointer').click(function(){
        location.href = 'http://www.dituhui.com';
    });
    $('.header .logo span').css('cursor', 'pointer').click(function(){
        location.href = '/';
    });
}



function cutLastSlashForLink(link) {
    if(!link || link.length === 0 || link.match("register")) {
        return "";
    }
    var len = link.length - 1;
    if(link.charAt(len) === "/") {
        if(len === 0) {
            return "";
        }
        else {
            link = link.substring(0, len);
            if(link.charAt(link.length - 1) === "/") {
                cutLastSlashForLink(link); 
            }
            else {
                return link;
            }           
        }
    }
    else {
        return link;
    }
}

function logout(){        
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.logout,
        dataType: 'jsonp',
        success: function(){
            location.href = urls.logout_first ;
        }
    });
    /*
    setTimeout(function(){
        location.href = urls.logout_first + '?service=' + location.href;
    }, 1000);
    */
}

$(function(){
    $("#fly_top").click( function(){
        var e=$("body").offset().top;
        $("html,body").animate({scrollTop:e},382)
    });
    
    var w = getWindowWidth();
    if(w < 960) {
        $('body, html').css("overflow-x", "auto");
    }
    else {
        $('body, html').css("overflow-x", "hidden");
    }

    var h = getWindowHeight();
    if(h < 450) {
        $(".onlineService").css("bottom", (h-180)+'px' );
    }
    else {
        $(".onlineService").css("bottom", '250px' );
    }
    createHeader_v1();

    var qq_numbers = [
        '328475979',  //陈纬业
        '2792606383', //马蕊
        '351570757', //孟庆欣
        '3350679139' //孙志刚
    ];
    var i = Math.floor(Math.random()*4);
    $('.onlineService a.ico_pp, .fixed-contact-us a, .footer .tele div a:first, .fly-right-window-content .kefu .imgs a')
    .attr('href', "javascript:void(0)")
    .click(function(){     
        i++;
        i = i > 3 ? 0 : i;
        window.open('http://wpa.qq.com/msgrd?v=3&uin='+ qq_numbers[1] +'&site=qq&menu=yes','');
    });

    User.initData();
});

window.onscroll = function(){
    var e=$(document).scrollTop();
    e>30?$("#fly_top").show(618):$("#fly_top").hide(382);
}

function scrollto(selector) {
    var div = $(selector);
    if(div && div.offset() && div.offset().top) {
        $("body,html").stop(true);
        $("body,html").animate({scrollTop: div.offset().top}, 500);        
    }
}


/**
 * 计算页数
 * 
 * @param: total - 总页数
 * @param: _pageIndex - 当前第几页
 * @param: limit - 每页显示个数，默认为10
 *
 * @return: html - 分页控件显示的内容
 */

function setPage(total, _pageIndex, divid, limit) {
    var html = '';

    //计算总页数
    var _pageCount = Math.ceil(total/limit);
    if(_pageIndex == 0) {
        html += '<li class="disabled" title="第一页"><a href="javascript:;">&laquo;</a></li>';
    }
    else {
        html += '<li  title="第一页"><a href="javascript:setpageno('+ divid +', '+ 0 +');">&laquo;</a></li>';        
    }


    if(_pageIndex < 3)　{
        for(var i=1; (i<=5 && i<=_pageCount && _pageIndex <= _pageCount); i++) {
            if(i !== (_pageIndex+1)) {
                html += '<li><a href="javascript:setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
            }
            else {
                html += '<li class="active"><a href="javascript:;">'+ i +'</a></li>';
            }
        }
    }
    else {
        //pageIndex后应该有几个页数
        var behind_index = (　(_pageCount-_pageIndex > 3) ? 3  :　_pageCount-_pageIndex);
        
        //pageIndex前应该有几个页数
        var before_index = 5 - behind_index - 1;

        var startIndex = (_pageIndex - before_index > 0 ? _pageIndex - before_index : 1 );
        var limit_length = _pageIndex + behind_index;
        for( i = startIndex; i <= limit_length ; i++ ){
            if(i !== (_pageIndex + 1) ) {
                html += '<li><a href="javascript:setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
            }
            else {
                html += '<li class="active"><a href="javascript:;">'+ i +'</a></li>';              
            }
        }
    }

    if(_pageIndex == (_pageCount - 1)) {
        html += '<li  title="最后一页" class="disabled"><a href="javascript:;">&raquo;</a></li>';
    }
    else {
        html += '<li  title="最后一页"><a href="javascript:setpageno('+ divid +', '+ (_pageCount-1) +');">&raquo;</a></li>';        
    }

    return html;
}

/**
 * 为div pager绑定点击事件
 * 
 * @param: id - div的id
 * @param: index - 当前第几页
 */
function setpageno(id, index) {
    var pager = $("#" + id);
    pager.attr("page", index);

    var target = pager.attr("data-target");
    switch(target) {
        case "childrenproduct":
            searchKidProduct();
            break;
        case "tradelist":
            search();
            break;
    }
}


function handleCookie(item) {    
    var cookie_kids = {};
    cookie_kids.products = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       cookie_kids = jQuery.parseJSON(c);
    } 
    var buy = isBoughtMe(item.id, cookie_kids.products );
    if(!buy) {
        cookie_kids.products.push(item);

        // console.log(buy);  
        // console.log(cookie_kids);  

        $.cookie(urls.cookie_products_name, JSON.stringify( cookie_kids ), {
            path: '/',
            expires: 365
        });
        // console.log(document.cookie)
    }
    return cookie_kids.products;
}

function isBoughtMe(id, items) {
    var len = items.length;
    if(len === 0) {
        return false;
    }
    for(var i=len; i--; ) {
        var item = items[i];
        if( id === items[i].id ) {
            return true;
        }
    }
    return false;
}

function removeItemByPIdFromArray(id, items) {
    var len = items.length;
    if(len === 0) {
        return;
    }
    for(var i=len; i--; ) {
        var item = items[i];
        if(id === item.pid) {
            items.splice(i, 1);
        }
    }
}
function removeItemByIdFromArray(id, items) {
    var len = items.length;
    if(len === 0) {
        return;
    }
    for(var i=len; i--; ) {
        var item = items[i];
        if(id === item.id) {
            items.splice(i, 1);
        }
    }
}

//用户操作
var User = {};

/**
 * 补充用户信息--180s后才能重新发送验证码
 */
User.setCodeBtnLoading = function() {    
    var btn = $('#btn_getNoteCode').attr("data-loading-text", "180s后获取").button('loading');
    var time = 179;
    var id = setInterval(function(){
        btn.html(time + 's后重新获取');
        if(time === 1) {
            clearInterval(id);
            btn.button('reset');
        }
        time--;
    }, 1000);
}

/**
 * 补充用户信息--获取短信验证码
 */
User.getNoteCode = function(){
    var flag = User.verifyMobile();
    if(!flag) {
        return;
    }
    
    User.setCodeBtnLoading();
    
    var param = {
        phone: $("#txt_mobile").val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.getnotecode,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                showPopover( e.info ? e.info : "短信验证码发送成功" );
            }
            else {
                showPopover( "短信验证码发送失败" );                
            }
        }
    });
}

//用户没有详细信息时，点击右上角用户名弹出用户补充信息的窗口
User.showFillUserModal = function() {    
    $('.modal').modal('hide');
    
    $('#modal_filluser').modal('show');
    $('.modal-backdrop').css('background-color', '#ccc');
}

/**
 * 补充用户信息--验证真实姓名
 */
User.verifyRealname = function() { 
    var txt = $("#txt_realname").val();
    var span = $("#hint_realname");
    
    var reg = /^(?!\s)([\u4E00-\u9FA0]+$|^[a-z·\s]+)$/i;
    if(txt == "") {
        span.html("联系人姓名不能为空" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    if(!reg.test(txt)){
        span.html("联系人姓名只能为中文或者英文" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    if(txt.length<2 || txt.length>8){
        span.html("联系人姓名长度为2-8位" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    span.html("<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 添加用户-验证手机号
 */
User.verifyMobile = function() {
    var txt = $("#txt_mobile").val();
    var span = $("#hint_mobile");

    if(txt == "") {       
        showPopover("请输入手机号码");       
        span.html("<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    var reg = /1[3,5,6,8]\d{9}$/;
    if(reg.test(txt)) {                 
        // span.html( "<img src='/apps/images/check.png'></img>");
        span.html( "");
        return true;                
    }
    showPopover("手机号码格式不正确，参考13100000000");   
    span.html("<img src='/apps/images/wrong.png'></img>");
    return false;
    
}

/**
 * 添加用户-验证公司名称
 */
User.verifyCompanyName = function() {
    var txt = $("#txt_companyName").val();
    var span = $("#hint_companyName");

    var reg = /^[\u0391-\uFFE5\w]+$/;    

    if(txt === "") {                 
        span.html("请填写公司名称" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    if( !reg.test( txt ) ) {
        span.html("不能包含特殊字符" + "<img src='/apps/images/wrong.png'></img>");
        return false;        
    }
    span.html( "<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 添加用户-验证企业邮箱
 */
User.verifyCompanyEmail = function() {
    var txt = $("#txt_companyEmail").val();
    var span = $("#hint_companyEmail");
    if( txt == "" ) {   
        span.html( "");
        return true;        
    }

    var reg = /[a-zA-Z0-9]+@\w+[\.][a-z]{2,3}$|[a-zA-Z0-9]+@\w+[\.][a-z]{2,3}[\.][a-z]{2,3}$/;            
    if(reg.test(txt) && txt.split("@").length === 2) {    
        span.html( "<img src='/apps/images/check.png'></img>");
        return true;
    }
    span.html('邮箱格式不正确' + '<img src="/apps/images/wrong.png"></img>');
    return false;
}


/**
 * 添加用户-验证短信二维码
 */
User.verifyNoteCode = function() {
    var txt = $("#txt_noteCode").val();
    var span = $("#hint_mobile");

    if(txt === "") {                 
        span.html("<img src='/apps/images/wrong.png'></img>");
        showPopover('请填写短信验证码');
        return false;
    }
    span.html( "");
    return true;
}

/**
 * 添加用户-验证QQ
 */
User.verifyQQ = function() {
    var txt = $("#txt_qq").val();
    var span = $("#hint_qq");

    if(txt === "") {                 
        span.html("请填写QQ" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }

    var reg = new RegExp('^[0-9]*$');
    if(!reg.test(txt)) {             
        span.html("QQ号码格式不正确" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    span.html( "<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 添加用户-验证公司所在地
 */
User.verifyLocation = function() {
    var me = $(".user-location");
    var txt = me.val();
    var span = me.find(' + span');

    if(txt == "-1") {                 
        span.html("请选择公司所在地" + "<img src='/apps/images/wrong.png'></img>");
        return false;
    }
    span.html( "<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 添加用户-验证行业
 */
User.verifySolutions = function() {
    var me = $(".text-user-solutions");
    var txt = me.val();
    var span = me.find(' + span');

    if(txt == "") {
        var v = $('.user-solutions').val();
        if( v == '-1' ) {
            span.html("请选择行业" + "<img src='/apps/images/wrong.png'></img>");
            return false;
        }
        else if(v == '其他') {
            span.html("请输入行业" + "<img src='/apps/images/wrong.png'></img>");
            return false;
        }
    }
    span.html( "<img src='/apps/images/check.png'></img>");
    return true;
}

/**
 * 验证添加的用户
 */
User.verifyUserInfo = function() {
    var flag = false;
    flag = User.verifyCompanyName(); 
    if(!flag) {
        return flag;
    }
    flag = User.verifyRealname();
    if(!flag) {
        return flag;
    }
    flag = User.verifyMobile();  
    if(!flag) {
        return flag;
    }
    if( user.telephone && user.telephone.trim().length === 0 ) {
        flag = User.verifyNoteCode();  
        if(!flag) {
            return flag;
        }        
    }
    flag = User.verifyLocation(); 
    if(!flag) {
        return flag;
    }  
    flag = User.verifySolutions(); 

    return flag;
}

/**
 * 补充用户信息-绑定手机号码
 */
User.bindPhone = function(callback) {
    var btn = $('#btn_filluser').attr("data-loading-text", "正在提交，请稍候……").button('loading');
    var param = {
        phone: $("#txt_mobile").val(),
        captcha: $('#txt_noteCode').val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.bindphone,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if( e.success ) {
                User.fillUserFunc();
            }
            else {
                btn.button("reset");
                showPopover("手机号码绑定失败，请填写正确的手机号码及短信验证码");
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
    if( user.telephone && user.telephone.trim().length === 0 ) {
        User.bindPhone();
    }
    else {
        User.fillUserFunc();
    }
}

/**
 * 补充用户信息
 */
User.fillUserFunc = function() {    
    var param = {
        realName: $("#txt_realname").val(),
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
            }
            else {         
                var info = e.info;
                if( info && info.match("已使用") && info.match("email") ) {     
                    showPopover(e.info ? e.info : "该邮箱已使用");
                }
                else {
                    showPopover(e.info);
                }
            }
        }
    });
}

/**
 * 补充用户信息完成后刷新头部
 */
User.refreshHeader = function() {
    //查询用户
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: login_successHandler
    });
}

User.initData = function() {
    var h = '<option value="-1" selected="true">请选择</option>';
    var data = User.data.province;
    for(var i=0, len=data.length; i<len; i++) {
        var item = data[i];
        h += '<option value="'+ item.admincode +'">'+ item.province +'</option>';
    }
    $('.user-location').html(h);

    h = '<option value="-1" selected="true">请选择</option>';
    data = User.data.solutions;
    for(var i=0, len=data.length; i<len; i++) {
        var item = data[i];
        h += '<option value="'+ item +'">'+ item +'</option>';
    }
    $('.user-solutions').html(h).change(function(){
        var me = $(this);var me = $(this);
        if(me.val() == '-1') {
            $('.text-user-solutions').val('').addClass('hide');
        }
        else if(me.val() != '其他') {
            $('.text-user-solutions').val('').addClass('hide');
        }
        else {
            $('.text-user-solutions').val('').removeClass('hide');
        }
    });
}
User.data = {
    province: [ 
        {
            "province": "北京",
            "admincode": "110000",
            'zhixia': "true"
        },
        {
            "province": "天津",
            "admincode": "120000",
            'zhixia': "true"
        },
        {
            "province": "重庆",
            "admincode": "500000",
            'zhixia': "true"
        },
        {
            "province": "上海",
            "admincode": "310000",
            'zhixia': "true"
        },
        {
            "province": "安徽",
            "admincode": "340000"
        },
        {
            "province": "福建",
            "admincode": "350000"
        },
        {
            "province": "甘肃",
            "admincode": "620000"
        },
        {
            "province": "广东",
            "admincode": "440000"
        },
        {
            "province": "广西",
            "admincode": "450000"
        },
        {
            "province": "贵州",
            "admincode": "520000"
        },
        {
            "province": "海南",
            "admincode": "460000"
        },
        {
            "province": "河北",
            "admincode": "130000"
        },
        {
            "province": "河南",
            "admincode": "410000"
        },
        {
            "province": "黑龙江",
            "admincode": "230000"
        },
        {
            "province": "湖北",
            "admincode": "420000"
        },
        {
            "province": "湖南",
            "admincode": "430000"
        },
        {
            "province": "吉林",
            "admincode": "220000"
        },
        {
            "province": "江苏",
            "admincode": "320000"
        },
        {
            "province": "江西",
            "admincode": "360000"
        },
        {
            "province": "辽宁",
            "admincode": "210000"
        },
        {
            "province": "内蒙古",
            "admincode": "150000"
        },
        {
            "province": "宁夏",
            "admincode": "640000"
        },
        {
            "province": "青海",
            "admincode": "630000"
        },
        {
            "province": "山东",
            "admincode": "370000"
        },
        {
            "province": "山西",
            "admincode": "140000"
        },
        {
            "province": "陕西",
            "admincode": "610000"
        },
        {
            "province": "四川",
            "admincode": "510000"
        },
        {
            "province": "西藏",
            "admincode": "540000"
        },
        {
            "province": "新疆",
            "admincode": "650000"
        },
        {
            "province": "云南",
            "admincode": "530000"
        },
        {
            "province": "浙江",
            "admincode": "330000"
        },
        {
            "province": "香港",
            "admincode": "810000"
        },
        {
            "province": "澳门",
            "admincode": "820000"
        },
        {
            "province": "台湾",
            "admincode": "710000"
        }
    ],

    solutions : [
        '物流快递','家电','金融','保险','快销零售','医药','制造业','其他'
    ]
};





Date.prototype.format =function(format){
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) 
        format=format.replace(RegExp.$1, (this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
    RegExp.$1.length==1? o[k] :
    ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}



$(window).resize(function(){
    var w = getWindowWidth();
    if(w < 960) {
        $('body, html').css("overflow-x", "auto");
    }
    else {
        $('body, html').css("overflow-x", "hidden");
    }
    var h = getWindowHeight();
    if(h < 450) {
        $(".onlineService").css("bottom", (h-180)+'px' );
    }
    else {
        $(".onlineService").css("bottom", '250px' );
    }
}); 


/**
 * 视频数据
 */
var data_movies = {
    movies: [
        { type: "video/mp4", src: urls.myself + "/products/data/wangdian.mp4", data_movie: "wangdian" },
        { type: "video/mp4", src: urls.myself + "/products/data/quhua.mp4", data_movie: "quhua" },
        { type: "video/mp4", src: urls.myself + "/products/data/default.mp4", data_movie: "fendanbao" },
        { type: "video/mp4", src: urls.myself + "/products/data/pathplan.mp4", data_movie: "pathplan" },
        { type: "video/mp4", src: urls.myself + "/products/data/default.mp4", data_movie: "zhixingtong" },
        { type: "video/mp4", src: urls.myself + "/products/data/default.mp4", data_movie: "xundianbao" },
        { type: "video/mp4", src: urls.myself + "/products/data/default.mp4", data_movie: "kaoqinguanli" },
        { type: "video/mp4", src: urls.myself + "/products/data/default.mp4", data_movie: "xiaoshouguanli" }
    ]
};
data_movies.getMovie = function(data) {
    var movies = data_movies.movies;
    var len = movies.length;
    for(var i=len; i--; ) {
        var movie = movies[i];
        if(movie.data_movie === data) {
            var arr = [movie];
            return arr;
        }
    }
    return null;
}

String.prototype.trim=function() {
    return this.replace(/(^\s*)|(\s*$)/g,'');
}




var _hmt = _hmt || [];
(function() {
  var hm = document.createElement("script");
  hm.src = "//hm.baidu.com/hm.js?50bd5ef9817a81686050d93d397b0abc";
  var s = document.getElementsByTagName("script")[0]; 
  s.parentNode.insertBefore(hm, s);
})();