//滑块验证标记
var slideCheck=false;
var slider=null;
$(function(){
	showheader();
	showElementInSequence();
	forbidDragImg();
	/*slider = new SliderUnlock("#slider",{
	    initLabelTip:"拖动滑块验证",
	    initIconSrc:"resources/img/img_yz.png",
	    successLabelTip : "验证成功",
	    successIconSrc:"resources/img/icon_yz_check.png"
	},function(){
		$('#label').empty().append("<img class='yz_check' src='resources/img/icon_yz_check.png'>");
	    $('#label img').attr("src","resources/img/icon_yz_check.png");
	    slideCheck=true;
	});*/
	slider = new SliderUnlock("#slider",{
		    successLabelTip : "验证成功"
		},function(){
		    $('#label').empty().append("<img class='yz_check' src='resources/img/icon_yz_check.png'>");
		    slideCheck=true;
		});
	slider.init();
	//初始化活跃度
	initActivity();
	registEvent();
})
//注册事件
function registEvent () {
	$(window).scroll(function(){
		showheader();
		showElementInSequence();
	});
	/*$("#login").click(function(e){
		e.stopPropagation();
		$(".mask").show();
		$(".login").fadeIn(200);
	});*/
	/*$("#login_close").click(function(e){
		e.stopPropagation();
		$(".mask").hide();
		$(".login").fadeOut(100);
	});*/
	$("#login_btn").click(function(e){
		e.stopPropagation();
		/*if(verifyLogin()==false){
			return false;			
		}*/
		//登录请求
		var param={
			phone:$("#login_account").val(),
            password:$("#login_password").val()
		};
		if($("#login_flag").val()=="console"){
			login(param,"console");
		}else{
			login(param,"index");
		}
	});
	//鼠标滑过合作伙伴logo
	$('a.logo_partner').hover(function(){
		var $img=$(this).children();
		var newLogoSrc=$img.attr('src').replace("_gray","");
		$img.attr('src',newLogoSrc);
	},function(){
		var $img=$(this).children();
		var OldLogoSrc=$img.attr('src').split('.')[0]+"_gray.png";
		$img.attr('src',OldLogoSrc);
	});
	//登录框
	$('#login,#not_console').modal({
	    target: '.login',
	    speed : 300,
	    easing : 'easeInOutExpo',
	    animation : 'none',
	    position: '10% auto',
	    overlayClose : false,
        overlayOpacity : .5,
        close : '#login_close'
    });
	$('#login,#not_console').click(function(){

	});
    $("#login_close").click(function(){
    	$("#login_flag").val("login");
    	/*slider = new SliderUnlock("#slider",{
    		    successLabelTip : "验证成功"
    		},function(){
    		    $('#label').empty().append("<img class='yz_check' src='resources/img/icon_yz_check.png'>");
    		    slideCheck=true;
    		});
    	slider.init();*/
    });
    $("#not_console").click(function(){
    	$("#login_flag").val("console");
    });
}
//依次显示元素动画
function showElementInSequence(){
	var windowHeight=$(window).height();
	var scrollTop=$(window).scrollTop();
	var rowArr=$(".animated");
	for(var i=0;i<rowArr.length;i++){
		var rowTop=$(rowArr[i]).offset().top;
		if(windowHeight + scrollTop >= rowTop+$(rowArr[i]).height()/4){
			$(rowArr[i]).css({"animation-name":"fadeInUp"});
		}
	}
}
//判断header与咨询图标的显示状态
function showheader(){
	var scrollTop=$(window).scrollTop();
	if(scrollTop>$(window).height()-60){
		$(".notice-wrap").hide();
		$(".header-wrap").removeClass("dark").addClass("light");
		$(".logo img").attr("src","resources/img/logoGray.png");
		$(".first-menue-link").css({"color":"#1a183d"});
		$(".menue-right a").css({"color":"#1a183d"});
		$(".down-arrow").removeClass("drop-down-white").addClass("drop-down-black");
		$(".up-arrow").removeClass("drop-up-white").addClass("drop-up-black");
		//console.log(scrollTop);
	}else{
		$(".notice-wrap").show();
		$(".header-wrap").removeClass("light").addClass("dark");
		$(".logo img").attr("src","resources/img/logoWhite.png");
		$(".first-menue-link").css({"color":"#fff"});
		$(".menue-right a").css({"color":"#fff"});
		$(".down-arrow").removeClass("drop-down-black").addClass("drop-down-white");
		$(".up-arrow").removeClass("drop-up-black").addClass("drop-up-white");
	}
}
//初始化活跃度
function initActivity(){
	var number=13706005435;
	updateActivityNumber($(".app"),number);
	updateActivityNumber($(".api"),number);
	var timer=setInterval(function(){
		number=number+11;
		updateActivityNumber($(".app"),number);
		updateActivityNumber($(".api"),number);
	},3000);
}
//登录验证
function verifyLogin(){
	var account=$("#login_account").val();
	var pw=$("#login_password").val();
	var code=$("#verify_code").val();
	var res=verifyPhonoNumber(account);
	if(res.success==false){
		$("#login_account").focus();
		$(".error-info").html(res.info).show();
		return false;
	}else{
		$(".error-info").html("");
	}
	res=verifyPassword(pw);
	if(res.success==false){
		$("#login_password").focus();
		$(".error-info").html(res.info).show();
		return false;
	}else{
		$(".error-info").html("");
	}
	if(code==""){
		$("#verify_code").focus();
		$(".error-info").html("请填写验证码").show();
		return false;
	}else{
		$(".error-info").html("");
	}
	if(slideCheck==false){
		$(".error-info").html("请拖动滑块验证").show();
		return false;
	}
	return true;
}
//活跃度更新
function updateActivityNumber(tag,number){
	tag.children().remove();
	NumbersAnimate.Target = tag;
	NumbersAnimate.Numbers = number;
	NumbersAnimate.Duration = 1000;
	NumbersAnimate.Animate();
}
var NumbersAnimate = {
	Target: null,
	Numbers: 0,
	Duration: 100,
	Animate: function() {
		var array = NumbersAnimate.Numbers.toString().split("");
		//遍历数组
		for(var i = 0; i < array.length; i++) {
			var currentN = array[i];
			//数字append进容器
			var t = $("<span></span>");
			$(t).append("<span class=\"childNumber\">" + array[i] + "</span>");
			$(t).css("left", 32+18 * i + "px");
			$(NumbersAnimate.Target).append(t);
			//生成滚动数字,根据当前数字大小来定
			for(var j = 0; j <= currentN; j++) {
				var tt;
				if(j == currentN) {
					tt = $("<span class=\"main\"><span>" + j + "</span></span>");
				} else {
					tt = $("<span class=\"childNumber\">" + j + "</span>");
				}
				$(t).append(tt);
				$(tt).css("margin-top", (j + 1) * 25 + "px");
			}
			$(t).animate({
				marginTop: -((parseInt(currentN) + 1) * 25) + "px"
			}, NumbersAnimate.Duration, function() {
				$(this).find(".childNumber").remove();
			});
		}
	},
	ChangeNumber: function(numbers) {
		var oldArray = NumbersAnimate.Numbers.toString().split("");
		var newArray = numbers.toString().split("");
		for(var i = 0; i < oldArray.length; i++) {
			var o = oldArray[i];
			var n = newArray[i];
			if(o != n) {
				var c = $($(".main")[i]);
				var num = parseInt($(c).html());
				var top = parseInt($($(c).find("span")[0]).css("marginTop").replace('px', ''));

				for(var j = 0; j <= n; j++) {
					var nn = $("<span>" + j + "</span>");
					if(j == n) {
						nn = $("<span>" + j + "</span>");
					} else {
						nn = $("<span class=\"yy\">" + j + "</span>");
					}
					$(c).append(nn);
					$(nn).css("margin-top", (j + 1) * 25 + top + "px");
				}
				var margintop = parseInt($(c).css("marginTop").replace('px', ''));
				$(c).animate({
					marginTop: -((parseInt(n) + 1) * 25) + margintop + "px"
				}, NumbersAnimate.Duration, function() {
					$($(this).find("span")[0]).remove();
					$(".yy").remove();
				});
			}
		}
		NumbersAnimate.Numbers = numbers;
	},
	RandomNum: function(m, a) {
		var Range = a - m;
		var Rand = Math.random();
		return(m + Math.round(Rand * Range));
	}
}	