<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
    <!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>地图慧开放平台</title>
    <link rel="stylesheet" type="text/css" href="resources/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="resources/css/animate.css"/>
</head>
<body>
    <!-- 遮罩层 -->
    <div class="mask">
    </div>
    <!-- 登录弹框 -->
    <div class="login">
        <span class="modal-close" id="login_close"></span>
        <div class="login-logo"><img src="resources/img/login_logo.png" alt=""></div>
        <div class="login-title">登录地图慧开放平台</div>
        <form action="" method="" onsubmit="return false;">
        <p class="login-error"><span class="error-info">sdhjkhdfjk</span></p>
        <div>
        <span class="login-icon user"></span>
        <input class="ty-text width-all" id="login_account" type="text" placeholder="手机号">
        </div>
        <div>
        <span class="login-icon pw"></span>
        <input class="ty-text width-all" id="login_password" type="password" placeholder="密码(长度为6~14个字符)">
        </div>
        <div>
        <input class="ty-text verify-code" id="verify_code" type="text" placeholder="验证码">
        <span class="verify-img"><img src="resources/img/verify_img.png" alt=""></span>
        </div>
        <div id="slider">
        <div id="slider_bg"></div>
        <span id="label">>></span> <span id="labelTip">拖动滑块验证</span></div>
        <div class="login-info">
        <div class="left">
        <input class="ty-check" type="checkbox">
        <span>自动登录</span>
        </div>
        <div class="right">
        <a href="register.html">免费注册</a>
        <a href="findPassword.html">忘记密码?</a>
        </div>
        </div>
        <button id="login_btn">登录</button>
        </form>
    </div>
    <!-- 快速入口 -->
    <div class="qick-entrance">
        <a target="blank" href="tencent://message/?uin=3563165632&amp;Site=中科协&amp;Menu=yes">
        </a>
    </div>
    <!-- 内容部分 -->
    <div class="content">
        <div class="banner">
            <div class="slider">
                <section class="video active">
                <div class="bg">
                <video src="resources/video_audio/Untitled.mp4" loop="" width="1920px" height="1080px" autoplay=true></video>
                </div>
                <div class="filter"></div>
                </section>
            </div>
            <!--<canvas id="canv" width="1920" height="950"></canvas>-->
            <div class="introduce-wrap">
                <div class="introduce">
                <div class="introduce-title">便捷牵手全新开放平台</div>
                <div class="introduce-sub-title">拥抱变革,让开发更简单</div>
                <div class="introduce-action"><a href="register.html">免费注册</a></div>
                </div>
            </div>
            <div class="notice-wrap dark">
                <div class="notice">
                <img src="resources/img/notice.png" alt="" width="16" height="16">
                <a href="">[2016-12-01]地图慧云平台服务DOC商用公告</a>
                <a href="">[2016-12-01]地图慧云平台服务DOC商用公告</a>
                <a href="">[2016-12-01]地图慧云平台服务DOC商用公告</a>
                <a href="" class="right">更多</a>
                </div>
            </div>
        </div>
        <div class="home-product">
            <h1 class="home-h1">产品与服务</h1>
            <h2 class="home-h2">服务互联网应用的完整生命周期</h2>
            <hr class="home-hr-gray">
            <div class="home-product-list">
                <ul class="product-row animated major-list"
                style="visibility: visible; animation-duration: 500ms; animation-delay: 400ms; animation-name: none;">
                    <li class="major-item">
                        <div class="list-content">
                            <a href="#">
                                <span class="txt-hide front-face" >
                                <img src="resources/img/icon_cpzb.png" alt="">
                                <span class="title">坐标转换</span>
                                <span class="introduce">基于GPS、WIFI的综合定位服务，具有定位精度高，覆盖率广，网络定位请求流量小，定位速度快</span>
                                </span>
                                <span class="back-face to-left" >
                                <img src="resources/img/icon_cpzb.png" alt="">
                                <span class="introduce">基于GPS、WIFI的综合定位服务，具有定位精度高，覆盖率广，网络定位请求流量小，定位速度快</span>
                                </span>
                            </a>
                        </div>
                    </li>
                <li class="major-item">
                <div class="list-content"
                style="visibility: visible; animation-duration: 500ms;animation-delay: 200ms;animation-name: none;">
                <a href="#">
                <span class="txt-hide front-face" >
                <img src="resources/img/icon_dlbm.png" alt="">
                <span class="title">地理编码</span>
                </span>
                <span class="back-face to-left" >
                <img src="resources/img/icon_dlbm.png" alt="">
                <span class="introduce">提供业内数据最新最全的矢量地图、卫星图、全景图、实时路况图，静态图和个性化地图服务。</span>
                </span>
                </a>
                </div>
                </li>
                <li class="major-item">
                <div class="list-content"
                style="visibility: visible; animation-duration: 500ms;animation-delay: 200ms;animation-name: none;">
                <a href="#">
                <span class="txt-hide front-face" >
                <img src="resources/img/icon_sjfw.png" alt="">
                <span class="title">数据服务</span>
                </span>
                <span class="back-face to-left" >
                <img src="resources/img/icon_sjfw.png" alt="">
                <span class="introduce">基于地图丰富的海量数据，开放检索，热词推荐，地理编码等服务，通过LBS云服务。</span>
                </span>
                </a>
                </div>
                </li>
                </ul>
                <ul class="product-row animated major-list"
                style="visibility: visible; animation-duration: 500ms; animation-delay: 400ms; animation-name: none;">
                <li class="major-item">
                <div class="list-content"
                style="visibility: visible; animation-duration: 500ms;animation-delay: 200ms;animation-name: none;">
                <a href="#">
                <span class="txt-hide front-face" >
                <img src="resources/img/icon_js.png" alt="">
                <span class="title">POI检索</span>
                </span>
                <span class="back-face to-left" >
                <img src="resources/img/icon_js.png" alt="">
                <span class="introduce">提供多类型区划规划方案，各方案还支持多种不同策略的检索面向所以平台提供导航。</span>
                </span>
                </a>
                </div>
                </li>
                <li class="major-item">
                <div class="list-content"
                style="visibility: visible; animation-duration: 500ms;animation-delay: 200ms;animation-name: none;">
                <a href="#">
                <span class="txt-hide front-face" >
                <img src="resources/img/icon_qyhbm.png" alt="">
                <span class="title">区域面合并</span>
                </span>
                <span class="back-face to-left" >
                <img src="resources/img/icon_qyhbm.png" alt="">
                <span class="introduce">提供轨迹追踪，存储，处理，查询和地理围栏等服务，具有轨迹精准，功耗低，海量终端支持优势。</span>
                </span>
                </a>
                </div>
                </li>
                <li class="major-item">
                <div class="list-content"
                style="visibility: visible; animation-duration: 500ms;animation-delay: 200ms;animation-name: none;">
                <a href="#">
                <span class="txt-hide front-face" >
                <img src="resources/img/icon_kjys.png" alt="">
                <span class="title">空间运算</span>
                </span>
                <span class="back-face to-left" >
                <img src="resources/img/icon_kjys.png" alt="">
                <span class="introduce">基于地理大数据，位置大数据，交通大数据和海量数据的地图产品，以领先的大数据分析和可视化技术。</span>
                </span>
                </a>
                </div>
                </li>
                </ul>
            </div>
        </div>
        <div class="home-solution">
            <h1 class="home-h1 color-white">解决方案</h1>
            <h2 class="home-h2 color-white">致力于提供最优质的解决方案</h2>
            <hr class="home-hr-light">
            <div class="slider">
            <span class="slider-left-arrow"></span>
            <span class="slider-right-arrow"></span>
            <div class="slider-list"><a href="#">
            <div class="slider-content">
            <img src="resources/img/icon_o2o.png" alt="">
            <p class="title">O2O上门服务</p>
            <p class="introduce">解决了用户在时间和位置上的众多不便，高频高效的连接人与服务</p>
            </div>
            </a></div>
            <div class="slider-list"><a href="#">
            <div class="slider-content">
            <img src="resources/img/icon_kdps.png" alt="">
            <p class="title">快递配送</p>
            <p class="introduce">快递物流行业正在经历变革，传统快递物流巨头在积极拥抱互联网</p>
            </div>
            </a></div>
            <div class="slider-list"><a href="#">
            <div class="slider-content">
            <img src="resources/img/icon_fchy.png" alt="">
            <p class="title">房产行业</p>
            <p class="introduce">我们认为用户通过房地产网站或APP获取信息的简单体验流程</p>
            </div>
            </a></div>
            <div class="slider-list"><a href="#">
            <div class="slider-content">
            <img src="resources/img/icon_jty.png" alt="">
            <p class="title">交通云</p>
            <p class="introduce">中国智慧交通云服务平台是业内云平台领导者，提供交通行业云服务</p>
            </div>
            </a></div>
            </div>
        </div>
        <div class="home-activity">
            <h1 class="home-h1">活跃度</h1>
            <h2 class="home-h2">数字验证了平台的可靠性</h2>
            <div class="home-activity-list">
            <div class="total app-wrap">
            <span class="title">应用总数</span>
            <div class="number app">
            </div>
            </div>
            <div class="total api-wrap">
            <span class="title">API调用次数</span>
            <div class="number api">
            </div>
            </div>
            </div>
        </div>
        <div class="home-partner">
            <h1 class="home-h1">合作伙伴</h1>
            <h2 class="home-h2">我们与行业最优秀的团队与品牌合作</h2>
            <div class="home-partner-list">
            <!-- <a class="logo_partner" href="#"><img src="resources/img/logo_haie.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_meid.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_rrs.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_zjs.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_and.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_quanf.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_longb.png" alt=""></a>
            <a class="logo_partner" href="#"><img src="resources/img/logo_dfwb.png" alt=""></a> -->
            </div>
        </div>
        <div class="home-footer">
            <div class="footer-content">
            <ul class="footer-left">
            <li>
            <strong>产品</strong>
            <a href="">逆地址编码</a>
            <a href="">分单POI检索</a>
            <a href="">区域面拆分</a>
            <a href="">区域面合并</a>
            <a href="">空间运算</a>
            <a href="">坐标转换</a>
            <a href="">地理编码</a>
            </li>
            <li>
            <strong>帮助</strong>
            <a href="">优先技术支持</a>
            <a href="">渠道打包</a>
            <a href="">培训课程</a>
            <a href="">优惠价格</a>
            <a href="">企业版</a>
            </li>
            <li>
            <strong>地图慧云平台</strong>
            <a href="">关于我们</a>
            <a href="">服务条款</a>
            <a href="">联系我们</a>
            </li>
            </ul>
            <div class="footer-right">
            <ul id="menu" class="mfb-component--br mfb-zoomin" data-mfb-toggle="hover">
            <li class="mfb-component__wrap">
            <a href="#" class="mfb-component__button--main"><span>关注我们</span></a>
            <ul class="mfb-component__list">
            <li>
            <a href="#" data-mfb-label="View on QQ" class="weibo mfb-component__button--child">
            <img src="resources/img/logo_gray-qq.png" alt="">
            </a>
            </li>
            <li>
            <a href="#" data-mfb-label="Follow me on WeChat" class="mfb-component__button--child">
            <img id="weixin" src="resources/img/logo_gray-weix.png" alt="">
            </a>
            </li>
            <li>
            <a href="#" data-mfb-label="Share on weibo" class="qq mfb-component__button--child">
            <img src="resources/img/icon_gray-weibo.png" alt="">
            </a>
            </li>
            </ul>
            </li>
            </ul>
            <!--<span class="concern-us">关注我们</span>
            <div class="share">
            <a href="" class="weibo"></a>
            <a href="" class="qq"></a>
            <a href="" class="weixin"></a>
            </div>-->
            </div>
            </div>
        </div>
    </div>
    <!--<script type="text/javascript" src="resources/lib/bannerAnimat.js"></script>-->
    <script type="text/javascript" src="resources/js/include.js"></script>
    <script src="resources/lib/jquery.slideunlock.js"></script>
    <script type="text/javascript" src="resources/lib/Sweefty.js"></script>
    <script type="text/javascript" src="resources/lib/moaModal.minified.js"></script>
    <script type="text/javascript" src="resources/js/index.js"></script>
    <script type="text/javascript" src="resources/js/public.js"></script><!--产品与服务的动画效果-->
    <script type="text/javascript" src="resources/js/main.js"></script><!--产品与服务的动画效果-->
    </body>
    </html>