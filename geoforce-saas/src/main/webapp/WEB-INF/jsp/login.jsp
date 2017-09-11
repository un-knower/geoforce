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
                            <input id="username" class="input_user" type="text" placeholder="请输入用户名"/>
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

        <!-- 提示 -->
        <div id="popover_result" class="popover fade top in" style="">
          <div class="popover-content" id="popover_content"></div>
        </div>
	</body>
</html>