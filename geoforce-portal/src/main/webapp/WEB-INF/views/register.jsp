<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>注册</title>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">
</head>
<body>
<main>
    <article>
        <form method="post">
            <p>
                <label for="register-phone">手机号</label>
                <input id="register-phone" type="text" value="" placeholder="请输入手机号">
                <span class="tips"></span>
            </p>
            <p>
                <label for="register-check">短信验证码</label>
                <input id="register-check" class="check" type="password" placeholder="输入验证码">
                <span class="tips"></span>
                <input class="checkBtn" type="button" value="免费获取短信验证码">
            </p>
            <p>
                <label for="register-password">密码</label>
                <input id="register-password" type="password"  placeholder="请输入密码">
                <span class="tips"></span>
            </p>
            <p>
                <label for="register-passworded">确认密码</label>
                <input id="register-passworded" type="password" placeholder="请再次输入密码">
                <span class="tips"></span>
            </p>
            <p style="line-height: 30px"><label>验证</label><span class="tips slideTip"></span></p>
            <div id="slider">
                <div id="slider_bg"></div>
                <span id="label"><img class="img_yz" src="resources/img/img_yz.png">>></span>
                <span id="labelTip">拖动滑块验证</span>
            </div>
            <p class="p-login-state">
                <input class="login-state" type="checkbox" checked>
                我已阅读并同意地图慧开放平台《使用条款》
            </p>
            <button id="register_btn">立即注册</button>
        </form>
    </article>
</main>
<script src="resources/js/include.js"></script>
<script src="resources/lib/jquery.slideunlock.js"></script>
<script>
    var slideCheck = false;
    $(function () {
        forbidDragImg();
        var slider = new SliderUnlock("#slider",{
            successLabelTip : "验证成功"
        },function(){
            $('#label').empty().append("<img class='yz_check' src='resources/img/icon_yz_check.png'>");
            slideCheck = true;
            $(".slideTip").html("").hide();
        });
        slider.init();
        regEvent();
    });
    //注册事件
    function regEvent(){
        $("#register_btn").click(function (e) {
            e.stopPropagation();
            //注册验证
            /*if (verifyRegister() == false) {
                return false;
            }*/
            console.log("验证成功，注册");
            var param={
                phone:$("#register-phone").val(),
                password:$("#register-password").val()
            };
            httpRequest({
                url: "pUser/register ",
                type:"post",
                data: param,
                success: function(e){
                    if(e && e.isSuccess){
                        window.location.href = "index";
                    }else{
                        alert("注册错误，手机号已注册？");
                    }
                },
                error: function(e){
                    console.log("请求出错");
                }
            });
        });
    }
    //注册验证
    function verifyRegister() {
        var h="";
        var _phone=$("#register-phone");
        var _pw=$("#register-password");
        var _check=$("#register-check");
        
        var phoneNum=_phone.val();
        var phCode=_check.val();
        var pw=_pw.val();
        //验证手机
        var res = verifyPhonoNumber(phoneNum);
        if (res.success == false) {
            h='<b>×</b> <span>'+res.info+'</span>';
            _phone.focus();
            _phone.next().html(h).show();
            return false;
        }else{
            _phone.next().html("").hide();
        }
        //短信验证码
        if(phCode==""){
            h='<b>×</b> <span>'+'请填写短信验证码'+'</span>';
            _check.focus();
            _check.next().html(h).show();
            return false;
        }
        //验证密码
        res = verifyPassword(pw);
        if (res.success == false) {
            h='<b>×</b> <span>'+res.info+'</span>';
            _pw.focus();
            _pw.next().html(h).show();
            return false;
        }else{
            _pw.next().html("").hide();
        }
        if (slideCheck == false) {
            h='<b>×</b> <span>请拖动滑块验证</span>';
            $(".slideTip").html(h).show();
            return false;
        }
        return true;
    }
</script>
</body>
</html>