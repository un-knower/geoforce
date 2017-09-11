<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
    request.setAttribute("ctx", basePath);
%>

<html>
	<head>
        <title>登录</title>
        <link href="${ctx}/resources/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />

        <link href="${ctx}/resources/css/login/login.css" rel="stylesheet" type="text/css" />

        <script src="${ctx}/resources/assets/js/jquery.min.js"></script>
        <script src="${ctx}/resources/assets/js/jquery.form.js"></script>
        <script src="${ctx}/resources/assets/js/bootstrap.min.js"></script>
        <script src="${ctx}/resources/assets/js/jquery.easing.1.3.js"></script>

        <script src="${ctx}/resources/js/login/login.js"></script>
        <script type="text/javascript">
            var PROJECT_URL='${ctx}';
        </script>
    </head>
    <body>
        <!--top-->
        <div class="border_line border_line_bottom">
            <div class="width930 top clearfix">
                <div class="fl">
                    <img  alt="" src="${ctx}/resources/images/login/logo.png" />
                    <span class="line_vertical logo_line vm"></span>
                    <label class="logo_title vm">登录</label>
                </div>
            </div>
        </div>

        <!--content-->
        <div class="width930 pt20">
            <!--错误提示-->
            <div id="msg" class="sso_tip_block position_r"  style="display: none;">
                <p>您输入的邮箱或密码有误！</p>
                <span class="sso_tip_cross">×</span>
            </div>
            <!--登录框-->
            <div class="width447 pt50" >
                <h2 class="sso_title">登录</h2>
                <form method="POST" enctype="text/plain" onsubmit="return fillUser();">
                    <div class="sso_block mb100">
                        <div>
                            <h3>账号</h3>
                            <input id="username" class="input_user" type="text" />
                            <!--错误样式-->
                            <!--<input class="input_user input_wrong" type="text" />-->
                        </div>
                        <div class="mt25">
                            <div class="clearfix lh20">
                                <h3 class="fl">密码</h3>                            
                            </div>
                            <input id="password" class="input_psw" type="password"/>
                        </div>

                        <div class="clearfix mt25 lh35 zoom">
                            <input class="sso_button fl" id="btn_login" type="button" value="登录"/>                        
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <!--bottom-->
        <div class="width930 border_line border_line_top pt20">
            <p class="font_bottom f14 tc">
                版权所有 ©1997-2015，北京超图软件股份有限公司 京ICP证120611号 京ICP备11032883号-1
            </p>
        </div>



        <!-- Modal 补充用户信息  -->
        <div class="modal fade" style="z-index: 999999;display:none;" id="modal_resetpwd" tabindex="-1" role="dialog" aria-labelledby="model_label_video" aria-hidden="true">
          <div class="modal-custom">
            <div class="closediv">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-custom-header">
              <span class="title">首次登录请重置密码</span>
            </div>
              <div class="modal-custom-content">
                <div class="row" style="display:none">
                  <input type="text" class="text-input" name="email" id="txt_email">
                </div>
                
                <div class="row">
                  <span class="labelfor" style="display:inline-block;">用户名</span>
                  <span class="span-username"></span>
                </div>

                <div class="row">
                  <span class="labelfor">密码</span>
                  <input type="password" class="text-input" id="txt_password" maxlength="8" name="realName" placeholder="请输入密码">
                  <span class="hint" id="hint_pwd"></span>
                </div>
                <div class="row" style="border-bottom: 1px solid #ccc;">
                  <span class="labelfor">确认密码</span>
                  <input type="password" class="text-input" id="txt_passwordsure" maxlength="11" placeholder="请再次确认密码" name="mobilePhone">
                  <span class="hint" id="hint_pwdsure"></span>
                </div>
                <div class="sure">
                  <button type="button" class="btn btn-info" id="btn_resetpwd" style="min-width: 200px;">提交</button>
                </div>
              </div>
          </div>
        </div> <!-- modal end 补充用户信息 -->

        
        <!-- Modal 上传许可文件  -->
        <div class="modal fade" style="z-index: 999999;display:none;" id="modal_license" tabindex="-1" role="dialog" aria-hidden="true">
          <div class="modal-custom">
            <div class="closediv">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span></button>
            </div>
            <div class="modal-custom-header">
              <span class="title">首次使用请导入许可文件</span>
            </div>
            <div class="modal-custom-content">
                <p>详细介绍</p>
                <form id="form_upload_license"  method="POST" enctype="multipart/form-data">
                    <input name="licenseFile" type="file" id="txt_licenseFiles">
                    <div class="sure">
                      <button type="button" class="btn btn-info" id="btn_upload_license" style="min-width: 200px;">上传</button>
                    </div>                    
                </form>
            </div>
          </div>
        </div> <!-- modal end 上传许可文件 -->
        


        <!-- 提示 -->
        <div id="popover_result" class="popover fade top in" style="">
          <div class="popover-content" id="popover_content"></div>
        </div>
	</body>
</html>