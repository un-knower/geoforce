/**
 * Created by zr on 2018/1/5.
 */

// var BASE_PATH = "http://192.168.10.252:8082/portal/";
var BASE_PATH = "/portal/";
//页面渲染
$(function(){
    $.ajax({
        url:BASE_PATH + "groupSet/findColumn",
        success:function(json){
            var data = json.result.data;
            var html = "";
            $.each(data,function(index,item){
                html += '<div class="list-column">';
                $.each(item,function(indexSon,itemSon){
                    html += '<div class="list-item">'+
                            '<span>'+itemSon.groupName+'</span>'+
                            '<ul>';
                    $.each(itemSon.apis,function(indexGrandSon,itemGrandSon){
                        if(itemGrandSon.status == 1){
                            html += '<li><a href="product.html?id='+itemGrandSon.id+'">'+itemGrandSon.apiName+'</a></li>';
                        }else if(itemGrandSon.status == 2){
                            html += '<li><a href="product.html?id='+itemGrandSon.id+'">'+itemGrandSon.apiName+'<img src="resource/img/new.png"/></a></li>';
                        }else if(itemGrandSon.status == 3){
                            html += '<li><a href="product.html?id='+itemGrandSon.id+'">'+itemGrandSon.apiName+'<img src="resource/img/api_come_soon.png"/></a></li>';
                        }
                    });
                    html += '</ul>'+
                            '</div>';
                });
                html += '</div>';
            });
            $("#api-list").html(html);
        }
    });
});
/** header start **/
$(function () {

    //滑块效果
    $(".menu>li").hover(function(){
        var wd = $(this).width();
        $(this).find("i").stop().animate({
            width : wd
        },300);
        $(this).children("div").css({
            borderTop: "0.1px solid rgb(32, 31, 31)"
        });
    },function(){
        $(this).find("i").stop().animate({
            width : 0
        },300);
    });
    // 产品
    $(".menu>li:nth-child(2)").hover(function(){
        $(".li-child2").show();
    },function(){
        $(".li-child2").hide();
    });
    $(".left-ul>li").on('click',function(){
        $(this).addClass('li-bg').siblings().removeClass('li-bg');
        var index = $(this).index();
        $(".li-child2 .right-li").eq(index).show().siblings().hide();
    });
    //解决方案
    $(".menu>li:nth-child(3)").hover(function(){
        $(".li-child3").show();
    },function(){
        $(".li-child3").hide();
    });
    //帮助与支持
    $(".menu>li:nth-child(4)").hover(function(){
        $(".li-child4").show();
    },function(){
        $(".li-child4").hide();
    });
    //我能做
    $(".menu>li:nth-child(5)").hover(function(){
        $(".li-child5").show();
    },function(){
        $(".li-child5").hide();
    });


    //设置.main的最小高度
    $(".page_wrapper").css({
        minHeight: window.screen.availHeight,
        padding: "20px 0 80px"
    });
});
/** header end **/

/**quick-link start**/
$(function(){
    $(window).scroll(function(){
        var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        if(scrollTop>=110){
            $(".quick-link").show();
        }else if(scrollTop<110){
            $(".quick-link").hide();
        }
    });
    $(".quick-link ul li").hover(function(){
        $(this).find(".link-box").stop().animate({"width":"124px"},200).css({
            background: "#449df2"
        });
    },function(){
        $(this).find(".link-box").stop().animate({"width":"54px"},200).css({
            background: "#323232"
        });
    });
    $(".return-top").click(function(){
        $("html,body").animate({
            scrollTop: 0
        },1000);
    });
});
/**quick-link end**/

//弹窗插件
$(function(){
    var timer = null;
    var autoHide = function(_this){
        timer = setTimeout(function(){
            $(_this).popover().click();
        },2000);
    }
    $('[data-toggle="popover"]').popover() //弹窗
        .on('show.bs.popover', function () { //展示时,关闭非当前所有弹窗
            $(this).parents().siblings().find('[data-toggle="popover"]').popover('hide');
            autoHide($(this));
        });
});

/****************公共方法*****************/

//产品与开发文档页的菜单的显示状态
var menuStatus = function(){
    /**一级菜单**/
    $(".menu-grandpa>li>span").click(function(){
        if($(this).parent().hasClass("show")){
            $(this).parent().removeClass("show");
        }else if(!$(this).parent().hasClass("show")){
            $(this).parent().addClass("show")
                .siblings().removeClass("show");
        }
    });
    $(".menu-grandpa>li>a").click(function(){
        $(this).parent().addClass("active").siblings().removeClass("active");
    });
    /**二级菜单**/
    $(".menu-father>li>span").click(function(){
        if($(this).parent().hasClass("show")){
            $(this).parent().removeClass("show");
        }else if(!$(this).parent().hasClass("show")){
            $(this).parent().addClass("show")
                .siblings().removeClass("show");
        }
    });
};


//解析地址参数
var UrlParm = function() { // url参数
    var data, index;
    (function init() {
        data = [];
        index = {};
        var u = window.location.search.substr(1);
        if (u != '') {
            var parms = decodeURIComponent(u).split('&');
            for (var i = 0, len = parms.length; i < len; i++) {
                if (parms[i] != '') {
                    var p = parms[i].split("=");
                    if (p.length == 1 || (p.length == 2 && p[1] == '')) {// p | p=
                        data.push(['']);
                        index[p[0]] = data.length - 1;
                    } else if (typeof(p[0]) == 'undefined' || p[0] == '') { // =c | =
                        data[0] = [p[1]];
                    } else if (typeof(index[p[0]]) == 'undefined') { // c=aaa
                        data.push([p[1]]);
                        index[p[0]] = data.length - 1;
                    } else {// c=aaa
                        data[index[p[0]]].push(p[1]);
                    }
                }
            }
        }
    })();
    return {
        // 获得参数,类似request.getParameter()
        parm : function(o) { // o: 参数名或者参数次序
            try {
                return (typeof(o) == 'number' ? data[o][0] : data[index[o]][0]);
            } catch (e) {
            }
        },
        //获得参数组, 类似request.getParameterValues()
        parmValues : function(o) { //  o: 参数名或者参数次序
            try {
                return (typeof(o) == 'number' ? data[o] : data[index[o]]);
            } catch (e) {}
        },
        //是否含有parmName参数
        hasParm : function(parmName) {
            return typeof(parmName) == 'string' ? typeof(index[parmName]) != 'undefined' : false;
        },
        // 获得参数Map ,类似request.getParameterMap()
        parmMap : function() {
            var map = {};
            try {
                for (var p in index) {  map[p] = data[index[p]];  }
            } catch (e) {}
            return map;
        }
    }
}();