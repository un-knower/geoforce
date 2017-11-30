$(function(){
	initcss();
	$(window).resize(function(){
	  initcss();    
	}); 
  //禁止表单提交
  $("form").submit(function () { return false;});
  //导航栏二级菜单显示
  $(".first-menue-li").mouseenter(function(){
    //切换小箭头方向
    /*var arrow=$(this).find(".down-arrow")[0];
    if($(arrow).hasClass("drop-down-white")){
      $(arrow).removeClass("drop-down-white down-arrow").addClass("drop-up-white up-arrow");
    }else{
      $(arrow).removeClass("drop-down-black down-arrow").addClass("drop-up-black up-arrow");
    }*/
    $(this).find(".second-menue").fadeIn(100);
  }).mouseleave(function(){
    //切换小箭头方向
    /*var arrow=$(this).find(".up-arrow")[0];
    if($(arrow).hasClass("drop-up-white")){
      $(arrow).removeClass("drop-up-white up-arrow").addClass("drop-down-white down-arrow");
    }else{
      $(arrow).removeClass("drop-up-black up-arrow").addClass("drop-down-black down-arrow");
    }*/
    $(this).find(".second-menue").fadeOut(50);
  });
  //头部反馈
  $("#feedback").click(function(){
    var currUser=localStorage.getItem("currUser");
    if(currUser==null || currUser==""){
      window.location.href="login";
    }else{
      window.location.href="feedback";
    }
  });
  //头部登录
  $("#login").click(function(){
    if(window.location.pathname=="/portal/index"){
      return false;
    }else{
      window.location.href="login";
    }
  });
  //发请求查看登录状态
  httpRequest({
    url: "pUser/getCurrentUser",
    type:"get",
    data:"",
    success: function(e){
        if(e && e.isSuccess && e.result){
          //更新缓存的用户信息
          if(e.result.total==1){
            var user=e.result.data;
            localStorage.setItem("currUser",JSON.stringify(user));
            $("#not_logined").addClass("hide");
            //头像
            //$("#avatar").attr("src","src");
            $("#logined").removeClass("hide");
          }else{
            localStorage.setItem("currUser","");
            $("#logined").addClass("hide");
            $("#not_logined").removeClass("hide");
          }
        }
    },
    error: function(e){
        console.log("请求出错");
    }
  });
});
//禁止图片拖拽在新标签打开
function forbidDragImg(){
  $("img").mousedown(function(e){
      e.preventDefault();
  });
}
//重置页面样式
function initcss() {
  var bodyWidth = getWindowWidth();
  var bodyHeight = getWindowHeight();
  var content_height = bodyHeight > 400 ? bodyHeight : 400;
  var content_width = bodyWidth > 1130 ? bodyWidth : 1130;
  $("main").css({"width": content_width + "px"});
  $('.banner').css({"width": content_width + "px","height": content_height + "px"});
}
//获取页面显示区域高度
function getWindowHeight() {
  if (navigator.appName == "Microsoft Internet Explorer" ) {
    return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
  }
  else {
    return $(window).height();//self.innerHeight;
  } 
}
//获取页面显示区域宽度
function getWindowWidth() {
  if (navigator.appName == "Microsoft Internet Explorer") {
    return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
  }
  else {
    return $(window).width();//self.innerWidth;
  } 
}
//验证手机号格式
function verifyPhonoNumber(number){
  var result={};
  var reg=/^1(3|4|5|7|8)\d{9}$/;
  if(number==""){
    result.success=false;
    result.info="请填写手机号";
    return result;
  }
  if(reg.test(number)==false){
    result.success=false;
    result.info="手机号格式错误";
    return result;
  }else{
    result.success=true;
    result.info="";
    return result;
  }
}
//验证邮箱格式
function verifyEmail(email){
  var result={};
  var reg=/(^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$)/;
  if(email==""){
    result.success=false;
    result.info="请填写邮箱";
    return result;
  }
  if(reg.test(email)==fasle){
    result.success=false;
    result.info="邮箱格式错误";
    return result;
  }else{
    result.success=true;
    result.info="";
    return result;
  }
}
//验证密码格式
function verifyPassword(pw){
  var result={};
  if(pw==""){
    result.success=false;
    result.info="密码不能为空";
    return result;
  }
  if(pw.length<6 || pw.length>14){
    result.success=false;
    result.info="密码长度为6~14个字符";
    return result;
  }else{
    result.success=true;
    result.info="";
    return result;
  }
}
//ajax请求
function httpRequest(option){
    var param = option.data ? option.data : {};
    var type = option.type ? option.type : "POST";
    $.ajax({
        type: type,
        async: true,
        url: option.url,
        data: param,
        dataType: 'json',
        success: option.success,
        error: option.error
    });
}
//登录
function login(param,currpage){
    httpRequest({
      url: "pUser/doLogin",
      type:"post",
      data:param,
      success: function(e){
          if(e && e.isSuccess){
            if(currpage=="login"){
              window.location.href="index";
            }else if(currpage=="console"){
              window.location.href="console";
            }else{
              $("#login_close").click();
            }
            $("#not_logined").addClass("hide");
            //头像信息
            //$("#avatar").attr("src","src");
            $("#logined").removeClass("hide");
          }
      },
      error: function(e){
          console.log("请求出错");
      }
    });
}
//退出
function loginOut(jumpPage){
  httpRequest({
    url: "pUser/doLogout",
    type:"get",
    data:"",
    success: function(e){
        if(e && e.isSuccess){
          $("#logined").addClass("hide");
          $("#not_logined").removeClass("hide");
          //清空缓存
          localStorage.setItem("currUser","");
          if(jumpPage=="toIndex"){
            window.location.href="index";
          }
        }
    },
    error: function(e){
        console.log("请求出错");
    }
  });
}