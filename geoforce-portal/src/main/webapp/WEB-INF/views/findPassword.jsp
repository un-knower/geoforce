<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>找回密码</title>
    <link rel="stylesheet" type="text/css" href="resources/css/findPassword.css">
</head>
<body>
<main>
    <article>
        <header>
            <figure id="figure-first" class="active">
                <figcaption>安全验证</figcaption>
                <section class="active">
                    <i>1</i>
                </section>
            </figure>
            <img class="arrow_up" src="resources/img/arrow_up.png" alt="">
            <figure id="figure-second">
                <figcaption>重置密码</figcaption>
                <section>
                    <i>2</i>
                </section>
            </figure>
            <img class="arrow_down" src="resources/img/arrow_down.png" alt="">
            <figure id="figure-third">
                <!--<img src="resources/img/icon_check.png" alt="">-->
                <figcaption>完成操作</figcaption>
                <section>
                    <i>3</i>
                </section>
            </figure>
        </header>
        <form id="first" style="display: block" action="#" method="post">
            <p>
                <label for="findPwd-phone">手机号码</label>
                <input id="findPwd-phone" type="text" placeholder="请输入手机号">
                <span class="tips"></span>
            </p>
            <p>
                <label for="findPwd-check">短信验证码</label>
                <input id="findPwd-check" class="check" type="text" placeholder="输入验证码">
                <span class="tips"></span>
                <input class="checkBtn" type="button" value="免费获取短信验证码">
            </p>
            <p style="line-height: 30px"><label>验证</label><span class="tips slideTip"></span></p>
            <div id="slider" style="width: 237px;margin-top: -35px">
                <div id="slider_bg"></div>
                <span id="label"><img class="img_yz" src="resources/img/img_yz.png">>></span>
                <span id="labelTip">拖动滑块验证</span>
                <span class="tips"></span>
            </div>
            <button id="first-next">下一步</button>
        </form>
        <form id="second" style="display: none" action="#" method="post">
            <p>
                <label for="findPwd-newPwd">新密码</label>
                <input id="findPwd-newPwd" type="password" placeholder="请输入密码">
                <span class="tips"></span>
            </p>
            <p>
                <label for="findPwd-newPwded">确认密码</label>
                <input id="findPwd-newPwded" type="password" placeholder="请确认密码">
                <span class="tips"></span>
            </p>
            <button id="reset-Pw">提交</button>
        </form>
        <div id="third" style="display: none">
            <img class="icon-check" src="resources/img/icon_check.png" alt="">
            <p>恭喜您完成密码修改！</p>
            <a href="index.html">返回首页</a>
        </div>
    </article>
</main>
<script src="resources/js/include.js"></script>
<script src="resources/lib/jquery.slideunlock.js"></script>
<script>
    $(function () {
        var slider = new SliderUnlock("#slider",{
            successLabelTip : "验证成功"
        },function(){
            $('#label').empty().append("<img class='yz_check' src='resources/img/icon_yz_check.png' style='width: 20px'>")
        });
        slider.init();
        registEvent();
    })
    function registEvent(){
        $("#first-next").click(function(){
            $("form").hide();
            $("figure").removeClass("active");
            $("figure section").removeClass("active");

            $("#figure-second").addClass("active");
            $("#figure-second section ").addClass("active");
            $("#second").show();
        });
        $("#reset-Pw").click(function(){
            $("form").hide();
            $("figure").removeClass("active");
            $("figure section").removeClass("active");

            $("#figure-third").addClass("active");
            $("#figure-third section ").addClass("active");
            $("#third").show();
        });
    }
</script>
</body>
</html>