<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>控制台个人资料</title>
    <link rel="stylesheet" type="text/css" href="resources/css/consolePerson.css">
    <link rel="stylesheet" type="text/css" href="resources/css/kzt.css">
</head>
<body>
    <div class="main-page">
        <nav class="cd-side-navigation">
            <ul>
                <li>
                    <a href="#0" data-menu="console">
                        <img src="resources/img/img_kzt_wdyy.png" alt="">我的应用
                    </a>
                </li>
                <li>
                    <a href="#0" data-menu="permission">
                        <img src="resources/img/img_kzt_qxtj.png" alt="">权限统计
                    </a>
                </li>
                <li>
                    <a href="#0" class="selected" data-menu="consolePerson">
                        <img src="resources/img/img_kzt_grzl.png" alt="">个人资料
                    </a>
                </li>
            </ul>
        </nav> <!-- .cd-side-navigation -->
        <div class="cd-main">
            <section class="cd-section services visible">
                <div class="con-right">
                    <div class="base-info">
                        <div class="base-info-item">
                            <div class="head-picture-wrap">
                                <img class="head-picture" src="resources/img/img_kzt_tx01.png" alt="">
                                <div class="edit-head">
                                    <img src="resources/img/img_kzt_tx02.png" alt="">
                                    <img src="resources/img/img_kzt_tx01.png" alt="">
                                    <img src="resources/img/img_kzt_tx01.png" alt="">
                                    <img src="resources/img/img_kzt_tx01.png" alt="">
                                    <img src="resources/img/img_kzt_tx01.png" alt="">
                                </div>
                            </div>
                            <div class="nickname-wrap">
                                <input id="nickname" type="text" value="Hello,Fey1234" readonly="true" onclick="moveEnd(this);">
                                <i id="editHead">修改资料</i>
                                <p class="choose-btn"><i id="cancel">取消</i><i id="save">保存</i></p>
                            </div>
                        </div>
                        <!-- <div class="base-info-item">
                            <div class="content">
                                <b>余额</b>
                                <p>********************</p>
                                <p>********************</p>
                            </div>
                        </div>
                        <div class="base-info-item">
                            <div class="content">
                                <b>安全防护</b>
                                <p>********************</p>
                                <p>********************</p>
                            </div>
                        </div> -->
                        <div class="clear"></div>
                    </div>
                    <!-- <div class="head-portrait">
                        <figure>
                            <img class="head-picture" src="resources/img/img_kzt_tx01.png" alt="">
                            <figcaption>
                                <input id="name" type="text" value="Hello,Fey1234" readonly onclick="moveEnd(this);">
                                <label for="name"><i id="editHead">修改设置</i></label>
                            </figcaption>
                        </figure>
                        <div>
                        <section>
                        <b>余额</b>
                        <p>********************</p>
                        <p>********************</p>
                        </section>
                        <section>
                        <b>安全防护</b>
                        <p>********************</p>
                        <p>********************</p>
                        </section>
                        <section>
                        </section>
                        </div>
                    </div>
                    <div class="editHead">
                    <img src="resources/img/img_kzt_tx01.png" alt="">
                    <img src="resources/img/img_kzt_tx01.png" alt="">
                    <img src="resources/img/img_kzt_tx01.png" alt="">
                    <img src="resources/img/img_kzt_tx01.png" alt="">
                    <img src="resources/img/img_kzt_tx01.png" alt="">
                    </div> -->
                    <div class="console-person">
                        <h3 class="console-person-info">开发者信息</h3>
                        <label class="console-personInfo-icon"><img src="resources/img/icon_kzt_yx.png" alt="">邮箱
                            <input type="text" id="user_email" value="hi...da@163.com" readonly>
                            <a href="editEmail">修改</a>
                        </label>
                        <label class="console-personInfo-icon"><img src="resources/img/icon_kzt_mm.png" alt="">登录密码
                            <input type="password" id="user_pw" value="12423423" readonly>
                            <a href="editPassword">修改</a>
                        </label>
                    </div>
                    <div class="console-details">
                        <h3 class="console-person-info">详细信息</h3>
                        <section>
                            <figure>
                                <img src="resources/img/icon_kzt_sj.png" alt="">
                                <figcaption>
                                    <p>手机</p>
                                    <p id="user_phone"></p>
                                </figcaption>
                            </figure>
                            <figure>
                                <img src="resources/img/icon_kzt_zcsj.png" alt="">
                                <figcaption>
                                    <p>账号ID</p>
                                    <p id="user_id"></p>
                                </figcaption>
                            </figure>
                            <figure>
                                <img src="resources/img/icon_kzt_zh.png" alt="">
                                <figcaption>
                                    <p>注册时间</p>
                                    <date id="user_createDate"></date>
                                </figcaption>
                            </figure>
                        </section>
                    </div>
                </div>
            </section> <!-- .cd-section -->
        </div> <!-- .cd-main -->
        <div id="cd-loading-bar" data-scale="1" class="consolePerson"></div>
    </div>
<script type="text/javascript" src="resources/lib/jquery_1.11.3/jquery.min.js"></script>
<script src="resources/js/common.js"></script>
<script src="resources/lib/console/velocity.min.js"></script>
<script src="resources/lib/console/main.js"></script>
<script>
    $(function(){
        initUser();
        /*$("#editHead").click(function(){
            $(".head-picture").css('border','1px solid #A5C7FE');
            $(".head-picture").hover(function(){
                $(".editHead").fadeIn(500)});
        });*/
        var oldSrc=$(".head-picture").attr("src");
        $("#editHead").click(function(){
            $("#nickname").removeAttr("readonly").css('border','1px solid #A5C7FE').click();
            $(".head-picture-wrap").css('border','1px solid #A5C7FE');
            $(this).hide();
            $(".choose-btn").fadeIn(300);
            //$(".edit-head").fadeIn(500);
            $(".head-picture-wrap").hover(function(){
                $(".edit-head").fadeIn(500);
            },function(){
                $(".edit-head").fadeOut(200);
            });
        });
        $("#cancel").click(function(){
            $(".head-picture").attr("src",oldSrc);
            hideEditHead();
        });
        //修改用户信息
        $("#save").click(function(){
            hideEditHead();
            //修改昵称，头像
            var param={
                        id:"ff8080815b127958015b129bb411000e",
                        avatarId:"asd",
                        nickname:"aaa",
                        email:"aaa",
                        phone:"18708149522",
                        password:"123456"
                    };
            $.ajax({
                type: "post",
                async: true,
                url: "pUser/update",
                data: param,
                dataType:'json',
                success: function(e){
                    if(e && e.isSuccess){
                        //refreshData();
                    }
                },
                error: function(e){
                    console.log("请求出错");
                }
            });
        });
        $(".edit-head img").click(function(){
            var newSrc=$(this).attr("src");
            $(".head-picture").attr("src",newSrc);
            $(".edit-head").fadeOut(200);
        });
    });
    function moveEnd(obj) {
        obj.focus();
        var len = obj.value.length;
        if (document.selection) {
            var sel = obj.createTextRange();
            sel.moveStart('character', len);
            sel.collapse();
            sel.select();
        } else if (typeof obj.selectionStart == 'number'
                && typeof obj.selectionEnd == 'number') {
            obj.selectionStart = obj.selectionEnd = len;
        }
    }
    //初始化用户资料
    function initUser(){
        var user=localStorage.getItem("currUser");
        if(user==null){
            window.locarion.href="index";
            return false;
        }
        user=JSON.parse(user);
        $("#nickname").val(user.nickname);
        $("#user_email").val(user.email);
        $("#user_phone").html(user.phone);
        $("#user_id").html(user.id);
        $("#user_createDate").html(user.createDate);
    }
    function hideEditHead(){
        $("#nickname").attr("readonly","readonly").css("border","none");
        $(".head-picture-wrap").css('border','1px solid #e9ebed');
        $(".choose-btn").hide();
        $("#editHead").fadeIn(500);
        $(".edit-head").fadeOut(200);
    }
</script>
</body>
</html>