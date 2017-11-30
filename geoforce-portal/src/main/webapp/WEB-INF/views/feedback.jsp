<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>反馈</title>
    <link rel="stylesheet" type="text/css" href="resources/lib/bootstrap_3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/feedback.css">
</head>
<body>
<div class="modal fade" id="modal_success" style="display: none;" id="modal_success" aria-hidden="true">
  <div class="modal-custom">
    <div class="modal-custom-content">
      <div class="back-img"><img src="resources/img/ok.png"></div>
      <span class="title">反馈发送成功，我们会尽快处理，感谢您的反馈</span>
    </div>
  </div>
</div>
<main>
    <section class="feedback">
        <h1>地图慧开放平台·意见反馈</h1>
        <p>望得到您客观的评价，虚心接受您最诚挚的意见和建议，对您留下的宝贵意见我们将第一时间给予解答并修正，并根据反馈意见的重要程度给予一定奖励，送上精美礼品一份。</p>
    </section>
    <div class="content">
    <div class="feedback-top">
    <p>
    <label for="fb_module">功能模块</label>
    <select name="fb_module" id="fb_module">
    <option value="1">API接口</option>
    <option value="2">开发文档</option>
    <option value="3">解决方案</option>
    <option value="4">常见问题</option>
    <option value="5">售后与支持</option>
    <option value="5">其他</option>
    </select>
    </p>
    <p>
    <label for="fb_type">反馈类别</label>
    <select id="fb_type">
    <option value="1">提问</option>
    <option value="2">纠错</option>
    <option value="3">意见</option>
    <option value="4">其他</option>
    </select>
    </p>
    <p>
    <label for="fb_content">反馈内容</label>
    <textarea id="fb_content" placeholder="请填写您的反馈内容(限120字)"></textarea>
    </p>
    <p>
    <label for="four">联系方式</label>
    <!-- <select name="one" id="four">
    <option value="1">邮箱或手机(非必填)</option>
    <option value="2">开发文档</option>
    <option value="3">开发文档</option>
    <option value="4">开发文档</option>
    <option value="5">开发文档</option>
    </select> -->
    <input type="text" id="fb_contact" placeholder="邮箱或手机(必填)">
    </p>
    <p><button id="fb_save">提交</button></p>
    </div>
    <div class="feedback-bottom">
    <img src="resources/img/icon_fank.png" alt="">
    <div class="right">
    <h2>意见反馈</h2>
    <p>客服随时准备为您服务</p>
    <p class="hour"><span>12h</span>之内答复</p>
    </div>
    <div>
    </div>
    </div>
    </div>
</main>
<script type="text/javascript" src="resources/js/include.js"></script>
<script type="text/javascript" src="resources/lib/bootstrap_3.3.5/js/bootstrap.min.js"></script>
<script>
    $(document).ready(function(){
        //提交反馈
        $("#fb_save").click(function(){
            var user=localStorage.getItem("currUser");
            if(user==null || user==""){
                return false;
            }
            var reportUserId=JSON.parse(user).id;
            //var userName=
            var remarkType=$("#fb_type").val();
            var remarkDesc=$("#fb_content").val();
            if(remarkDesc==""){
                $("#fb_content").addClass("red-border");
                return false;
            }else{
                $("#fb_content").removeClass("red-border");
            }
            var param={
                "reportUser.id":reportUserId,
                remarkType:remarkType,
                remarkDesc:remarkDesc
            };
            $.ajax({
                type: "post",
                async: true,
                url: "remark/save",
                data: param,
                dataType:'json',
                success: function(e){
                    if(e && e.isSuccess){
                        $("#fb_content").val("");
                        $("#fb_contact").val("");
                        $("#modal_success").modal("show");
                        setTimeout(function(){
                          $("#modal_success").modal("hide");
                        },2000);
                    }
                },
                error: function(e){
                    console.log("请求出错");
                }
            });
        });
    })
</script>
</body>
</html>