<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!--设置移动端-->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>平台公告--详情页</title>

    <!--图标-->
    <link rel="shortcut icon" type="image/x-icon" href="http://www.dituhui.com/favicon.ico">

    <!--引入公共样式文件-->
    <link rel="stylesheet" href="resource/css/normalize.css">
    <link rel="stylesheet" href="resource/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resource/css/common.css">

    <link rel="stylesheet" href="resource/css/news.css">

</head>
<body style="display: none;">
<!--header-->
<div class="header">
    <div class="header-center">
        <div class="logo">
            <a href="index.html"><img class="logo-img" src="resource/img/logo.png"/></a>
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
<!--content start-->
<div class="main page_wrapper">
    <div class="leftBar fl">
        <ul>
            <li class="active"><a href="news_list.html">平台公告</a></li>
            <li><a href="javascript:void(0);">常见问题</a></li>
        </ul>
    </div>
    <div class="rightBar fr">
        <p class="location">当前位置：<a href="index.html">首页</a> / <a href="news_list.html">平台公告</a> / <span id="title">公告详情</span></p>
        <div class="news-content">

        </div>
    </div>
    <div style="clear: both"></div>
</div>
<!--content end-->

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
            <p>客服电话：400-966-1112</p>
            <p>客服电话：028-67077202</p>
            <p>地址：成都市高新区天府大道中段<br>
                1366号天府软件园E区6幢6楼</p>
        </li>
        <li class="footer-link-list-5">
            <div class="link-img-box">
                <img src="resource/img/weixin.png"/>
                <span>微信公众号</span>
            </div>
            <div class="link-img-box">
                <img src="resource/img/dituhui.jpg"/>
                <span>极图科技</span>
            </div>
        </li>
    </ul>
</div>
<!--footer-->
<div class="footer">
    <p class="copyright">
        Copyright © 2018 极图科技 京ICP备11032883号-5 京ICP证120611号 甲测资质1100363
    </p>
</div>
</body>
</html>
<!--加载jQuery文件-->
<script src="resource/lib/jquery-1.9.1/jquery.min.js"></script>
<!--bootstrap插件-->
<script src="resource/lib/bootstrap/js/bootstrap.min.js"></script>

<script src="resource/js/common.js"></script>
<script src="resource/js/news_detail.js"></script>