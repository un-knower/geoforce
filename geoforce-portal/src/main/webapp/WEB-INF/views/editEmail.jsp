<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>修改邮箱</title>
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
                <figcaption>输入新邮箱</figcaption>
                <section>
                    <i>2</i>
                </section>
            </figure>
            <img class="arrow_down" src="resources/img/arrow_down.png" alt="">
            <figure id="figure-third">
                <!--<img src="resources/img/icon_check.png" alt="">-->
                <figcaption>去邮箱激活</figcaption>
                <section>
                    <i>3</i>
                </section>
            </figure>
        </header>
        <form id="first" style="display: block" action="#" method="post">
            <p>
                <label for="editEmail-phone">手机号码</label>
                <input id="editEmail-phone" type="text" placeholder="请输入手机号码">
            </p>
            <aside><b>×</b> 请输入手机号码</aside>
            <p>
                <label for="editEmail-check">短信验证码</label>
                <input id="editEmail-check" class="check" type="text" placeholder="请输入验证码"><input class="checkBtn" type="button" value="免费获取短信验证码" style="margin-left: 3px">
            </p>
            <p style="line-height: 30px"><label>验证</label></p>
            <div id="slider" style="width: 237px;margin-top: -35px">
                <div id="slider_bg"></div>
                <span id="label"><img class="img_yz" src="resources/img/img_yz.png">>></span>
                <span id="labelTip">拖动滑块验证</span>
            </div>
            <button id="first-next">下一步</button>
        </form>
        <form id="second" style="display: none" action="#" method="post">
            <p>
                <label for="editEmail-email">邮箱</label>
                <input id="editEmail-email" type="email" placeholder="请输入新邮箱">
            </p>
            <aside class="second"><b>×</b> 请输入您要修改的邮箱</aside>
            <button id="second-next">下一步</button>
        </form>
        <form id="third" style="display: none" action="#" method="post">
            <div class="editEmail-div">
                <span>您好，hi,hey@163.com</span>
                <span>Cloud开放平台推出的一款开发者自有位置数据云存储产品，为用户提供位置数据存储、编辑、空间检索和地图渲染云服务。</span>
                <button id="email-active" class="go-activate">去邮箱完成激活</button>
            </div>
        </form>
    </article>
    <div id="fouth" class="alert" style="display: none">
        <div class="alertHead">
            <span class="warn">提示</span>
            <span class="close">×</span>
        </div>
        <section>
            <img src="resources/img/icon_check.png" alt="">
            <p class="btn">恭喜您成功激活邮箱！</p>
            <a href="index.html">返回首页</a>
        </section>
    </div>
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
            //验证第一步
            var phone=$("#editEmail-phone").val();
            //调后台发短信验证码

            //通过后，显示第二步
            $("form").hide();
            $("figure").removeClass("active");
            $("figure section").removeClass("active");

            $("#figure-second").addClass("active");
            $("#figure-second section ").addClass("active");
            $("#second").show();
        });
        $("#second-next").click(function(){
            //获取新邮箱
            var new_email=$("#editEmail-email").val();
            //调后台发激活邮件

            //显示第三步
            $("form").hide();
            $("figure").removeClass("active");
            $("figure section").removeClass("active");

            $("#figure-third").addClass("active");
            $("#figure-third section ").addClass("active");
            $("#third").show();
        });
        //跳转至邮箱，激活邮件，成功后显示第四步(修改成功)
        $("#email-active").click(function(){
            $("#fouth").show();
        });
        $(".close").click(function(){
            $("#fouth").hide();
        });
    }
</script>
</body>
</html>