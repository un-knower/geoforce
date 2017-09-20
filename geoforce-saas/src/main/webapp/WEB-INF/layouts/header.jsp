<%@ page language="java" import="java.util.*,com.supermap.egispservice.base.entity.UserEntity,com.supermap.egispservice.base.entity.ComEntity" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("projectName", request.getContextPath());
	UserEntity user = (UserEntity) request.getSession().getAttribute("user");
	if(null!=user){
		request.setAttribute("eid", user.getEid().getId());
		request.setAttribute("userid", user.getId());
	}
	String projectpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("location_url", projectpath);
%>
<!-- #section:basics/navbar.layout -->
<div id="navbar_free" class="navbar navbar-default" >
	当前为试用版本，您的操作数据仅可保存一周，您需要开通正式权限来保存并管理您的数据。
	<a href="http://e.dituhui.com/apps">进入产品定制页面</a>
</div>

<script type="text/javascript">
	var userid = '${userid}';
	var location_url = '${location_url}';
	
	if( ${user.sourceId} === 4 ) {
		$('#navbar_free').show();		
	}
</script>
<script src="${location_url}/resources/js/sys/header.js"></script>

<!--
<div class="user-expire">
	<div class="box">
		<div class="expire-alert"></div>
		<div class="footer">
			<button type="button" class="btn btn-warning">Warning</button>
		</div>
	</div>	
</div>
-->

<div id="navbar" class="navbar navbar-default navbar-system">
	<script type="text/javascript">
		try{ace.settings.check('navbar' , 'fixed')}catch(e){}
	</script>

	<div class="navbar-container" id="navbar-container">
		<!-- #section:basics/sidebar.mobile.toggle -->
		<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
			<span class="sr-only">Toggle sidebar</span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>

			<span class="icon-bar"></span>
		</button>

		<!-- /section:basics/sidebar.mobile.toggle -->
		<div class="navbar-header pull-left" style="">
			<!-- #section:basics/navbar.layout.brand -->
			<a id="header-logo" href="http://e.dituhui.com/" target="_blank">
				<img alt="企业可视化管理平台"
					src="${ctx}/resources/images/supercloud-logo.png" 
					>
			</a>
			<div style="padding-top: 15px;float: left;">
					
					<small id="systitle" style="font:blod;font-size:15px; color: #757575;margin-left: 30px;">
					<c:choose>
						<c:when test="${eid=='f9a8d6684a19c4b8014a5b8c5e4d0053'}">
						网点通
						</c:when>
						<c:when test="${eid=='f9a8d6684a19c4b8014a2eba642a0019'}">
						中铁物流分单管理系统
						</c:when>
						<c:otherwise>
						
						</c:otherwise>
					</c:choose>
					
					
					</small> 
				</div>
			<!-- /section:basics/navbar.layout.brand -->

			<!-- #section:basics/navbar.toggle -->

			<!-- /section:basics/navbar.toggle -->
		</div>
		
		<!--用户过期-->
		<!--<div class="user-expire">尊敬的用户，您的服务费用余额为0元，为了更好的服务，请您及时付费，电话联系：028-67077216</div>-->
		
		<div class="navbar-buttons navbar-header pull-right" role="navigation">
			<ul class="nav ace-nav">
				<!-- #section:basics/navbar.user_menu -->
				<li >
					<a class="header-phone">
						<i class="ace-icon fa fa-phone" style="color:#757575; margin-top:17px;float: left;"></i>
						<span>
							<small style="margin: 0 10px 0 0">400-966-1112</small>
						</span>
					</a>
				</li>
				<!--<li>
					<a class="header-nav-btn header-share">
						<i></i>
						<span><small>分享</small></span>
					</a>
					
				</li>-->
				<li >
					<a data-toggle="dropdown" href="#" class="dropdown-toggle" 
						style=" color:#757575; background:#DCDCDC url('${ctx}/resources/assets/css/img/top_title_bg.png')  fixed top;">
						<%-- <img class="nav-user-photo" src="${ctx}/resources/assets/avatars/user.jpg" /> --%>
						<span class="">
							<small></small>
							${user.realname }
						</span>

						<i class="ace-icon fa fa-caret-down" style="color:#757575; "></i>
					</a>

					<ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close" style="z-index: 88888;">
						<%-- <li>
							<a href="${ctx}/user/pass">
								<i class="ace-icon fa fa-cog"></i>
								修改密码
							</a>
						</li> --%>

						<li>
							<a href="${ctx}/user/info">
								<i class="ace-icon fa fa-user"></i>
								个人资料
							</a>
						</li>

						<li class="divider"></li>

						<li>
							<a href="${ctx}/welcome/logout">
								<i class="ace-icon fa fa-power-off"></i>
								退出
							</a>
						</li>
					</ul>
				</li>
				<li >
					<a data-toggle="dropdown" href="#" class="header-more">
						<span class="">
							<small id="logblink">..<span id="lognewflag">.</span></small>
						</span>
					</a>
					<ul class=" hide dropdown-menu dropdown-menu-right dropdown-close header-more-dropdown">
						<li>
							<small style="position:relative;">
								<a href="javascript:void(0);" id="log">更新日志</a>
								<span id="redpoint"  class="hide" style="display: inline-block; width: 4px; height: 4px; background-color: red; position: absolute; border-radius: 50%; top: 6px; right: -6px;"></span>
							</small>
						</li>
						<li>
							<small><a href="javascript:void(0);" id="help">帮助中心</a></small>
						</li>
						<!--<li class="divider"></li>-->
						<li>
							<small><a href="javascript:void(0);" id="feedback">意见反馈</a></small>
						</li>
					</ul>
				</li>
				<!-- /section:basics/navbar.user_menu -->
			</ul>
		</div>
		<script type="text/javascript">
			$(function(){
				$('.header-more').on('click', function(){
					var ul = $(this).siblings('.header-more-dropdown');
					if(!ul.is(':visible')) {
						ul.removeClass('hide');
					}
					else{
						ul.addClass('hide');
					}
				});
				//检查是否有日志更新
				$.ajax({
					type:"GET",
        			url:"/saas/user/getIsUserReadLastLogs",
        			data:{},
			        success:function(e){
			        	if(e.result==false){
			        		$("#lognewflag").css('color','red');
			        		$("#logblink").addClass('blink');
			        		$("#redpoint").removeClass('hide');
			        	}
			        }
				});
				$("#log").click(function(){
					$("#logblink").removeClass('blink');
					$("#redpoint").addClass('hide');
				    //保存用户阅读信息
					$.ajax({
						type:"GET",
						url:"/saas/user/saveUserReadInfo",
						data:{},
						success:function(e){
							/* if(e.isSuccess){
								console.log("成功！");
							} */
						}
					});
					window.open("http://e.dituhui.com/log?pageName=help&key="+userid);
				});
				$("#help").click(function(){
					window.open("http://e.dituhui.com/help?pageName=help&key="+userid);
				});
				$("#feedback").click(function(){
					window.open("http://e.dituhui.com/feedback?pageName=feedback&key="+userid);
				});
			});
		</script>

		<!-- /section:basics/navbar.dropdown -->
	</div><!-- /.navbar-container -->

	
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
	<script type="text/javascript">
		try{ace.settings.check('main-container' , 'fixed')}catch(e){}
	</script>

	<!-- #section:basics/sidebar -->
	<div id="sidebar" class="sidebar compact sidebar-fixed responsive">
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
		</script>


		<ul id="userMenus" class="nav nav-list">
		</ul><!-- /.nav-list -->

		<!-- #section:basics/sidebar.layout.minimize -->
		<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
			<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
		</div>

		<!-- /section:basics/sidebar.layout.minimize -->
		<script type="text/javascript">
			try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
		</script>
		
		<div class="bottom-logo hide">
			<img src="${ctx}/resources/images/logo/dituhui-yellow.png">
			<span>地图慧</span>
		</div>
	</div>
	
	
	<!-- /section:basics/sidebar -->
	<div class="main-content">
		<div class="page-content">
			<div class="page-content-area">
				<div class="row">
					<div class="col-xs-12 function-content">
						<!-- PAGE CONTENT BEGINS -->
