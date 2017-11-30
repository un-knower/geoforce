<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <link rel="stylesheet" type="text/css" href="resources/css/login.css">
</head>
<body>
<main>
    <article>
        <form action="#" method="post">
            <p>
                <label for="login_account">登录账号</label>
                <input id="login_account" type="text" placeholder="请输入手机号">
                <span class="tips"></span>
            </p>
            <p>
                <label for="login_password">密码</label>
                <input id="login_password" type="password" placeholder="请输入密码">
                <span class="tips"></span>
            </p>
            <p style="line-height: 30px"><label>验证</label><span class="tips slideTip"></span></p>
            <div id="slider">
                <div id="slider_bg"></div>
                <span id="label"><img class="img_yz" src="resources/img/img_yz.png">>></span>
                <span id="labelTip">拖动滑块验证</span>
            </div>
            <p class="p-login-state">
                <input class="login-state" type="checkbox">保持登录状态
                <span class="register"><a href="register.html">免费注册</a></span>
                <span class="forgetPwd"><a href="findPassword.html">忘记密码？</a></span>
            </p>
            <button id="login_btn">登录</button>
        </form>
    </article>
</main>
<script src="resources/js/include.js"></script>
<script src="resources/lib/jquery.slideunlock.js"></script>
<script>
    var slideCheck = false;
    $(function () {
        forbidDragImg();
        var slider = new SliderUnlock("#slider", {
            successLabelTip: "验证成功"
        }, function () {
            $('#label').empty().append("<img class='yz_check' src='resources/img/icon_yz_check.png'>");
            slideCheck = true;
            $(".slideTip").html("").hide();
        });
        slider.init();
        $("#login_btn").click(function (e) {
            e.stopPropagation();
            if (verifyLogin() == false) {
                return false;
            }
            var param={
                phone:$("#login_account").val(),
                password:$("#login_password").val()
            };
            login(param,"login");
        });
    })
    function verifyLogin() {
        var h="";
        var _phone=$("#login_account");
        var _pw=$("#login_password");
        
        var account=_phone.val();
        var pw=_pw.val();

        //验证手机
        var res = verifyPhonoNumber(account);
        if (res.success == false) {
            h='<b>×</b> <span>'+res.info+'</span>';
            _phone.focus();
            _phone.next().html(h).show();
            return false;
        }else{
            _phone.next().html("").hide();
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
        //验证滑块
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