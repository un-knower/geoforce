<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <!--设置移动端-->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>极图|Geoforce 专业的企业级GIS云开放平台</title>

    <!--图标-->
    <link rel="shortcut icon" type="image/x-icon" href="resource/img/favicon.png">
    <!--引入公共样式文件-->
    <link rel="stylesheet" href="resource/css/normalize.css">
    <link rel="stylesheet" href="resource/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resource/css/common.css">

    <!--首页样式-->
    <link rel="stylesheet" href="resource/css/index.css">
    <!--滚动条样式-->
    <link rel="stylesheet" href="resource/lib/scrollBar/css/scrollBar.css">
</head>
<body style="display: none;">
<!--header-->
<div class="header">
    <div class="header-center">
        <div class="logo">
            <a href="index.html"><img class="logo-img" src="resource/img/logo.png"/><span class="logo-span">极图·Geoforce</span></a>
        </div>
        <ul class="menu">
            <li>
                <a href="index.html">首页
                <i></i>
                </a>
            </li>
            <li class="special">
                <a href="javascript:void(0);" class="li-child2-1">产品
                <i></i>
                </a>
                <div class="li-child2">
                    <div class="li-child2-center">
                        <ul class="left-ul">
                            <li class="li-bg left-ul-1">
                                <img src="resource/img/li-1.png" height="18" width="19"/>
                                API
                                <img src="resource/img/li-3.png" height="14" width="8"/>
                            </li>
                        </ul>
                        <div class="right-bar">
                            <div id="api-list" class="api-list list show">

                            </div>
                        </div>
                    </div>
                </div>
            </li>
            <li>
                <a href="javascript:void(0);" class="li-child3-1">解决方案
                <i></i>
                </a>
                <div class="li-child3">
                    <ul class="child3-li">
                        <li><a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="right"
                               data-content="敬请期待！">物流行业</a></li>
                        <li><a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="right"
                               data-content="敬请期待！">快递行业</a></li>
                        <li><a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="right"
                               data-content="敬请期待！">家电行业</a></li>
                    </ul>
                </div>
            </li>
            <li>
                <a href="javascript:void(0);" class="li-child4-1">帮助与支持
                <i></i>
                </a>
                <div class="li-child4">
                    <ul class="child4-li">
                        <li><a href="support_develop.html">开发文档</a></li>
                        <li><a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="right"
                               data-content="敬请期待！">常见问题</a></li>
                        <li><a href="news_list.html">平台公告</a></li>
                    </ul>
                </div>
            </li>
            <li>
           		<a href="javascript:void(0);" class="li-child5-1">示例应用
                    <i></i>
                </a>
                <div class="li-child5">
                    <ul class="child5-li">
                        <li><a href="cando.html">点线面管理</a></li>
                        <li><a href="distribute/demo">智能分单示例</a></li>
                        <li><a href="distribute/free">免费智能分单</a></li>
                    </ul>
                </div>
            </li>
            <li>
                <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="right"
                   data-content="敬请期待！">意见反馈
                <i></i>
                </a>
            </li>
        </ul>
        <div class="control">
            <ul>
                <li> <button class="btn btn-blue btn-border-rev-o">
                    <a>控制台</a></button></li>
                <li><button class="btn btn-blue btn-border-rev-o">
                    <a href="javascript:void(0);">登陆</a></button></li>
                <li><button class="btn btn-blue btn-border-rev-o">
                    <a href="javascript:void(0);">注册</a></button></li>
            </ul>
        </div>
    </div>
</div>
<!--content-->
<div class="main">
    <!--banner-->
    <div class="banner_slides" id="banner_slides">
        <%--<div class="banner_change banner_prev">--%>
            <%--<a href="javascript:void(0);"><img src="resource/img/prev.png"/></a>--%>
        <%--</div>--%>
        <%--<div class="banner_change banner_next">--%>
            <%--<a href="javascript:void(0);"><img src="resource/img/next.png"/></a>--%>
        <%--</div>--%>
        <div class="slides_container">
            <div class="slide">
                <div class="banner_1">
                    <div class="banner1_center">
                        <div class="banner1_title">
                            <h1>极图 · Geoforce正式上线</h1>
                            <h3>专业面向所有行业的企业级云GIS开放平台</h3>
                        </div>
                        <a><img src="resource/img/index-banner1-center.png"/></a>
                    </div>
                </div>
            </div>
            <div class="slide">
                <div class="banner_2">
                    <div class="banner2_center">
                        <a><img src="resource/img/index-banner2-center.png"/></a>
                    </div>
                </div>
            </div>
            <div class="slide">
                <div class="banner_3">
                    <div class="banner3_center">
                        <a><img src="resource/img/index-banner3-center.png"/></a>
                    </div>
                </div>
            </div>
        </div>
        <div class="slide_link_box">
            <ul class="slide_link">
                <li class="active"></li>
                <li></li>
                <li></li>
            </ul>
        </div>
        <div class="news">
            <div class="news-center">
                <span class="trumpet glyphicon glyphicon-volume-up"></span>
                <ul id="announce">
                    <a class="more fr" href="news_list.html">更多</a>
                    <div style="clear: both;"></div>
                </ul>
            </div>
        </div>
    </div>
    <!--product&service-->
    <div class="pro-ser">
        <h5 class="home-h5">产品与服务</h5>
        <h6 class="home-h6">为具有研发能力的个人和企业提供专业的在线GIS能力</h6>
        <div id="pro-ser-content" class="pro-ser-content">
            <ul id="pro-father">
                <%--<li class="pro-father">--%>
                    <%--<div class="pro-son">--%>
                        <%--<div class="img-box">--%>
                            <%--<img src="resource/img/沿路寻路.png"/>--%>
                            <%--<p>沿路寻路</p>--%>
                            <%--<span>沿路寻路根据传入的坐标点串，自动沿道路连线，返回连线的点串。用于自动构面等场景。</span>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="pro-son-h">--%>
                        <%--<p>沿路寻路</p>--%>
                        <%--<img src="resource/img/沿路寻路.png"/>--%>
                        <%--<a href="javascript:void(0);">查看详情</a>--%>
                    <%--</div>--%>
                <%--</li>--%>

                <%--<li class="pro-father">--%>
                    <%--<div class="pro-son">--%>
                        <%--<div class="img-box">--%>
                            <%--<img src="resource/img/沿路寻路.png"/>--%>
                            <%--<p>沿路寻路</p>--%>
                            <%--<span>沿路寻路根据传入的坐标点串，自动沿道路连线，返回连线的点串。用于自动构面等场景。</span>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <%--<div class="pro-son-h">--%>
                        <%--<p>沿路寻路</p>--%>
                        <%--<img src="resource/img/沿路寻路.png"/>--%>
                        <%--<a href="javascript:void(0);">查看详情</a>--%>
                    <%--</div>--%>
                <%--</li>--%>
            </ul>
        </div>
        <p><a href="product.html" class="button">更多产品</a></p>
    </div>
    <!--solution-->
    <div class="solution">
        <h5 class="home-h5">解决方案</h5>
        <h6 class="home-h6">多个行业、多种应用场景的成熟解决方案，解决您的痛点难点问题</h6>
        <div class="slides" id="slides">
            <div class="prev1 change">
                <a href="javascript:void(0);"><img src="resource/img/prev.png"/></a>
            </div>
            <div class="next1 change">
                <a href="javascript:void(0);"><img src="resource/img/next.png"/></a>
            </div>
            <div class="slides_container" id="banner_nr">
                <div id="b_server" class="slide">
                    <div class="slide-1 active1">
                        <div class="slide-left">
                            <h5 class="doc-h5">车辆监控</h5>
                            <p><span class="glyphicon glyphicon-map-marker"></span>干线运输、城际运输车辆位置透明化管理<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>支持实时定位与轨迹回放<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>优质算法解决定位漂移问题<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>超高性能按需伸缩</p>
                            <%--<a class="button button-w" href="##">查看详情</a>--%>
                        </div>
                        <div class="slide-right">
                            <img src="resource/img/solu-1-1.png" style="height: 400px"/>
                        </div>
                        <div class="slide-left">
                            <h5 class="doc-h5">售后服务兵</h5>
                            <p><span class="glyphicon glyphicon-map-marker"></span>透明化管理外勤服务兵地理位置<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>通过客户地址快速定位<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>智能查找周边最优服务兵</p>
                            <%--<a class="button button-w" href="##">查看详情</a>--%>
                        </div>
                        <div class="slide-right">
                            <img src="resource/img/solu-1-2.png" style="height: 400px"/>
                        </div>
                    </div>
                </div>
                <div id="b_web" class="slide">
                    <div class="slide-1">
                        <div class="slide-left">
                            <h5 class="doc-h5">分单</h5>
                            <p><span class="glyphicon glyphicon-map-marker"></span>结合多边形、地理编码、POI搜索、行政区域等多个产品，实现地址分单的快、准、稳，为企业降低成本<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>解决传统分单人工成本高的问题<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>解决传统分单效率低下的问题<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>解决传统分单错误率高的问题</p>
                            <%--<a class="button button-w" href="##">查看详情</a>--%>
                        </div>
                        <div class="slide-right">
                            <img src="resource/img/solu-2-1.png" style="height: 400px"/>
                        </div>
                    </div>
                </div>
                <div id="b_backup" class="slide">
                    <div class="slide-1">
                        <div class="slide-left">
                            <h5 class="doc-h5">车辆监控</h5>
                            <p><span class="glyphicon glyphicon-map-marker"></span>干线运输、城际运输车辆位置透明化管理<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>支持实时定位与轨迹回放<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>优质算法解决定位漂移问题<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>超高性能按需伸缩</p>
                            <%--<a class="button button-w" href="##">查看详情</a>--%>
                        </div>
                        <div class="slide-right">
                            <img src="resource/img/solu-3-1.png" style="height: 400px"/>
                        </div>
                        <div class="slide-left">
                            <h5 class="doc-h5">路线规划</h5>
                            <p><span class="glyphicon glyphicon-map-marker"></span>解决同城多起点多途经点的配送问题，为企业降低成本，提高效率<br/>
                                <span class="glyphicon glyphicon-map-marker"></span>支持不同车辆类型、载重、体积维度</p>
                            <%--<a class="button button-w" href="##">查看详情</a>--%>
                        </div>
                        <div class="slide-right">
                            <img src="resource/img/solu-3-2.png" style="height: 400px"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="banner_nav">
                <ul class="industry">
                    <li class="b_text06">
                        <i></i>
                        <img name="sol-logi" src="resource/img/sol-logi.png"/><br/>
                        <a href="javascript:void(0);" id="navul1"  class="banner_navhover">物流行业</a>
                    </li>
                    <li class="b_text05">
                        <i></i>
                        <img name="sol-house" src="resource/img/sol-dot.png"/><br/>
                        <a href="javascript:void(0);" id="navul2">快递行业</a>
                    </li>
                    <li class="b_text03">
                        <i></i>
                        <img name="sol-traffic" src="resource/img/sol-dot.png"/><br/>
                        <a href="javascript:void(0);" id="navul3">家电行业</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!--develop documents-->
    <div class="dev-doc">
        <h5 class="home-h5">开发文档</h5>
        <h6 class="home-h6">查看在线开发文档</h6>
        <div id="dev-doc" class="dev-doc-content">
            <ul>
                <li class="dev-doc-child">
                    <div class="dev-doc-imgBar fl">
                        <img src="resource/img/API.png">
                    </div>
                    <div class="dev-doc-textBar fr">
                        <h5 class="doc-h5">API开发文档</h5>
                        <p>介绍如何使用各个API产品，包括详细的服务地址、请求参数、返回结果、错误码等，并提供简单调试示例</p>
                    </div>
                </li>
                <%--<li class="dev-doc-child">--%>
                    <%--<div class="dev-doc-imgBar fl">--%>
                        <%--<img src="resource/img/SDK.png">--%>
                    <%--</div>--%>
                    <%--<div class="dev-doc-textBar fr">--%>
                        <%--<h5 class="doc-h5">SDK开发文档</h5>--%>
                        <%--<p>线路规划 、面管理 、行政区域、 测距、 照片生 成三维图 POI服务 、智能画图 、坐标转换 、地理 编码 、切片服务等</p>--%>
                    <%--</div>--%>
                    <%--<div style="clear: both;"></div>--%>
                <%--</li>--%>
            </ul>
        </div>
        <ul id="dots" class="dots">
            <%--<li class="dots-li current"></li>--%>
            <%--<li class="dots-li"></li>--%>
        </ul>
        <p><a href="support_develop.html" class="button">了解更多</a></p>
    </div>
    <!--activity-->
    <div class="activity">
        <h5 class="home-h5">活跃度</h5>
        <h6 class="home-h6">数字验证平台可靠性</h6>
        <div class="activity-content row">
            <div class="act-data col-md-6">
                <h1 id="allNum">8000</h1>
                <h3>应用总数</h3>
            </div>
            <div class="act-data col-md-6">
                <h1 id="apiNum">100000000</h1>
                <h3>API调用次数</h3>
            </div>
        </div>
    </div>
    <!--partner-->
    <div class="partner">
        <h5 class="home-h5">合作伙伴</h5>
        <h6 class="home-h6">他们都在使用我们的产品和服务</h6>
        <div class="partner-content">
            <ul id="box">
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>澳柯玛</h4>
                                <p>世界知名制冷装备供应商</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-1.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>渤海银行</h4>
                                <p>一家全国性的股份制商业银行</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-2.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>朝宏网</h4>
                                <p>帮助同城实体商家、企业拥有市场竞争力</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-3.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>车享家</h4>
                                <p>车享旗下连锁实体服务网络，致力于成为用户身边的爱车好管家</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-4.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>华宇物流</h4>
                                <p>国际最具实力全球连锁搬家品牌</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-5.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>佳怡</h4>
                                <p>为客户提供物流与供应链一体化服务的综合性物流企业集团</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-6.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>龙邦快运</h4>
                                <p>一家以物流供应链管理为核心的创新型平台企业</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-7.png">
                </li>
                <li class="imgBox">
                    <div class="shade">
                        <div class="shadeChild">
                            <a>
                                <h4>新邦物流</h4>
                                <p>国家AAAAA级综合型物流企业</p>
                            </a>
                        </div>
                    </div>
                    <img src="resource/img/partner-8.png">
                </li>
            </ul>
        </div>
    </div>
</div>
<!--quick-link-->
<div class="quick-link">
    <ul>
        <li><a href="support_develop.html"><div class="link-box"><img src="resource/img/self-develop.png"/>我要开发</div></a></li>
        <li><a href="javascript:void(0);"><div class="link-box"><img src="resource/img/FAQ.png"/>常见问题</div></a></li>
        <li><a href="#footer-link"><div class="link-box"><img src="resource/img/contact-us.png"/>联系我们</div></a></li>
        <li><a href="javascript:void(0);"><div class="link-box return-top"><img src="resource/img/return-top.png"/>回到顶部</div></a></li>
    </ul>
</div>
<!--footer link-->
<div id="footer-link" class="footer-link">
    <ul class="footer-link-content">
        <li class="footer-link-list-1">
            <span>解决方案</span>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">物流行业</a>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">快递行业</a>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">家电行业 </a>
        </li>
        <li class="footer-link-list-2">
            <span>帮助与支持</span>
            <a href="support_develop.html">开发文档</a>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">常见问题</a>
            <a href="news_list.html">平台公告</a>
        </li>
        <li class="footer-link-list-3">
            <span>极图科技</span>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">关于我们</a>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">加入我们</a>
            <a href="javascript:void(0);" data-container="body" data-toggle="popover" data-placement="left"
               data-content="敬请期待！">意见反馈</a>
        </li>
        <li class="footer-link-list-4">
            <p>咨询电话：18628102813</p>
            <p>咨询电话：13518121873</p>
            <p>咨询电话：18108083056</p>
            <p>地址：成都市高新区天晖路360号3楼7号</p>
        </li>
        <li class="footer-link-list-5">
            <div class="link-img-box">
                <img src="resource/img/weixin.png"/>
            </div>
        </li>
    </ul>
</div>
<!--footer-->
<div class="footer">
    <p class="copyright">
        Copyright <span id="console">©</span> 2018 成都极图科技有限公司 蜀ICP备18007265号
    </p>
</div>
</body>
</html>
<!--加载jQuery文件-->
<script src="resource/lib/jquery-1.9.1/jquery.min.js"></script>
<!--bootstrap插件-->
<script src="resource/lib/bootstrap/js/bootstrap.min.js"></script>
<!--滚动条插件-->
<script src="resource/lib/scrollBar/js/scrollBar.js"></script>
<!-- 轮播插件 -->
<script src="resource/lib/slide/slides.min.jquery.js"></script>

<script src="resource/js/common.js"></script>
<script src="resource/js/index.js"></script>