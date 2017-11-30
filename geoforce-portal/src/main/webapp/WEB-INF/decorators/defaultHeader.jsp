<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!-- header -->
<div class="header-wrap dark">
	<div class="header">
		<div class="logo">
			<a href="index"><img src="resources/img/logoWhite.png" alt=""></a>
		</div>
		<div class="navbar">
			<ul class="first-menue">
				<li class="first-menue-li">
					<a class="first-menue-link" href="index">
						<span>首页</span>
					</a>
				</li>
				<li class="first-menue-li">
					<a class="first-menue-link" href="javascript:void(0);">
						<span>产品</span>
						<span class="drop-down-white down-arrow"></span>
					</a>
					<div class="second-menue">
						<ul class="second-menue-left">
							<li class="second-li">
								<a class="link" href="/portal/products?a=xlgh">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_dlbm.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">线路规划</span>
										<span class="txt-description">综合定位服务</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=mgl">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_kjys.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">面管理/空间运算</span>
										<span class="txt-description">信息的精细化格网管理</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=xzqy">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_zbzh.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">行政区域</span>
										<span class="txt-description">查找特定的行政区域信息</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=cj">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_zbzh.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">测距</span>
										<span class="txt-description">海量数据，LBS云服务</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=zpscswt">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_zbzh.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">照片生成三维图</span>
										<span class="txt-description">海量数据，LBS云服务</span>
									</div>
								</a>
							</li>
						</ul>
						<ul class="second-menue-right">
							<li class="second-li">
								<a class="link" href="/portal/products?a=poi">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_ndzbm.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">POI服务</span>
										<span class="txt-description">多类型区划规划方案</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=znhq">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_qymcf.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">智能画区</span>
										<span class="txt-description">面的合并拆分</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=zbzh">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_qymhb.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">坐标转换</span>
										<span class="txt-description">不同坐标系间的坐标转换</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=dlbm">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_qymhb.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">地理编码</span>
										<span class="txt-description">地址和经纬度间的转换</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="/portal/products?a=qpfw">
									<div class="link-left">
										<span class="icon"><img src="resources/img/list_qymhb.png" alt=""></span>
									</div>
									<div class="link-right">
										<span class="txt-title">切片服务</span>
										<span class="txt-description">合理，高质量的切片</span>
									</div>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="first-menue-li">
					<a class="first-menue-link" href="javascript:void(0);">
						<span>解决方案</span>
						<span class="drop-down-white down-arrow "></span>
					</a>
					<div class="second-menue">
						<ul>
							<li class="second-li">
								<a class="link" href="">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">O2O上门服务</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">快递配送</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">房产行业</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">交通云</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="first-menue-li">
					<a class="first-menue-link" href="javascript:void(0);">
						<span>帮助与支持</span>
						<span class="drop-down-white down-arrow"></span>
					</a>
					<div class="second-menue">
						<ul>
							<li class="second-li">
								<a class="link" href="/portal/developmentDoc?a=gaishu">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">开发文档</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="faq">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">常见问题</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
							<li class="second-li">
								<a class="link" href="saleSupport">
									<!-- <div class="link-left">
										<span class="icon"></span>
									</div> -->
									<div class="link-right">
										<span class="txt-title">售后与支持</span>
										<span class="txt-description">全新的极致体验</span>
									</div>
								</a>
							</li>
						</ul>
					</div>
				</li>
				<li class="first-menue-li">
					<a class="first-menue-link" href="javascript:void(0);" id="feedback">反馈</a>
				</li>
			</ul>
		</div>
		<div class="menue-right">
			<ul id="not_logined">
				<li><a href="console" id="not_console">控制台</a></li>
				<li><a href="javascript:void(0);" id="login">登录</a></li>
				<li><a href="register">注册</a></li>
			</ul>
			<ul id="logined" class="hide">
				<li><a href="console">控制台</a></li>
				<li>
					<img class="avatar" id="avatar" src="resources/img/img_kzt_tx01.png">
				</li>
				<li><a href="javascript:void(0);" onclick="loginOut('noJump');">退出</a></li>
			</ul>
		</div>
	</div>
</div>