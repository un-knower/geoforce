<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <!--设置移动端-->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>意见反馈</title>

    <!--引入公共样式文件-->
    <link rel="stylesheet" href="resource/css/normalize.css">
    <link rel="stylesheet" href="resource/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resource/css/common.css">

    <link rel="stylesheet" href="resource/css/feedback.css">

</head>
<body>
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
                                API产品
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
                        <li><a href="javascript:void(0);">物流配送</a></li>
                        <li><a href="javascript:void(0);">房产行业</a></li>
                        <li><a href="javascript:void(0);">交通云</a></li>
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
                        <li><a href="FAQ.html">常见问题</a></li>
                        <li><a href="news_list.html">平台公告</a></li>
                    </ul>
                </div>
            </li>
            <li>
                <a href="javascript:void(0);">意见反馈
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
<div class="banner">
    <h1>意见反馈</h1>
</div>
<div class="main page_wrapper">
    <div class="feedback-center">
        <p class="location">当前位置：<a href="index.html">首页</a> / <span>意见反馈</span></p>
        <div class="feedback-content">
            <div class="feedback-title">
                <span class="feedback-title-sp1 fl">1、写下我们的不足和对我们的意见：</span>
                <span id="cue1" class="feedback-title-sp2 fl">(如涉及具体的页面，可以将相关链接粘贴在这里哦)</span>
                <span id="cue2" class="feedback-title-sp2 fl"><i class="glyphicon glyphicon-exclamation-sign"></i>内容不能为空哦~</span>
                <span class="feedback-title-sp3 fr"><span id="check-num" class="check-num">0</span>/500</span>
                <div style="clear: both;"></div>
            </div>
            <div class="feedback-textarea">
                <textarea maxlength="500" id="advance" class="form-control" placeholder="请将您的意见和遇到的问题详细告知我们，我们会认真对待解决和不断优化。"></textarea>
            </div>
            <div class="feedback-title">
                <span class="feedback-title-sp1">2、如需图片说明，请上传：</span>
                <span class="feedback-title-sp2">(最多不超过3张，每张不超过1M哦)</span>
            </div>
            <div class="img-box">
                <div class="addimg-son-box fl">
                    <div class="tip">
                        <span class="glyphicon glyphicon-plus"></span>
                        <span class="addImg-tip">点击或拖曳来添加图片</span>
                    </div>
                    <input type="file" name="file" class="file" id="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple="multiple" />
                </div>
                <div style="clear: both;"></div>
            </div>
            <div class="feedback-title">
                <span class="feedback-title-sp1 fl">3、请留下你的信息，以便我们回访或及时反馈：</span>
                <span id="cue3" class="feedback-title-sp1 fl">请输入正确的手机号哦~</span>
                <div style="clear: both;"></div>
            </div>
            <div class="tel-msg">
                <span class="feedback-title-sp1">您的手机号：</span>
                <input id="tel" type="tel" placeholder="请输入常用手机号" minlength="11" maxlength="11"/>
            </div>
        </div>
        <div class="save">
            <a id="save" href="javascript:void(0);">保存</a>
        </div>

    </div>
</div>
<aside class="mask works-mask">
    <div class="mask-content">
        <p class="del-p">您确定要删除该图片吗？</p>
        <p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>
    </div>
</aside>
<!--quick-link-->
<div class="quick-link">
    <ul>
        <li><a href="support_develop.html"><div class="link-box"><img src="resource/img/self-develop.png"/>我要开发</div></a></li>
        <li><a href="FAQ.html"><div class="link-box"><img src="resource/img/FAQ.png"/>常见问题</div></a></li>
        <li><a href="#footer-link"><div class="link-box"><img src="resource/img/contact-us.png"/>联系我们</div></a></li>
        <li><a href="javascript:void(0);"><div class="link-box return-top"><img src="resource/img/return-top.png"/>回到顶部</div></a></li>
    </ul>
</div>
<!--footer link-->
<div id="footer-link" class="footer-link">
    <ul class="footer-link-content">
        <li class="footer-link-list-1">
            <span>解决方案</span>
            <a href="javascript:void(0);">物流行业</a>
            <a href="javascript:void(0);">房产行业</a>
            <a href="javascript:void(0);">交通行业 </a>
        </li>
        <li class="footer-link-list-2">
            <span>帮助与支持</span>
            <a href="support_develop.html">开发文档</a>
            <a href="FAQ.html">常见问题</a>
            <a href="news_list.html">平台公告</a>
        </li>
        <li class="footer-link-list-3">
            <span>地图慧</span>
            <a href="javascript:void(0);">关于我们</a>
            <a href="javascript:void(0);">加入我们</a>
            <a href="javascript:void(0);">意见反馈</a>
        </li>
        <li class="footer-link-list-4">
            <p>客服电话：400-966-1112</p>
            <p>客服QQ:2405758643</p>
            <p>QQ群：516071434</p>
            <p>地址： 北京市朝阳区酒仙桥北路甲10<br>
                院107楼超图大厦6层</p>
        </li>
        <li class="footer-link-list-5">
            <div class="link-img-box">
                <img src="resource/img/weixin.png"/>
                <span>微信公众号</span>
            </div>
            <div class="link-img-box">
                <img src="resource/img/dituhui.jpg"/>
                <span>地图慧</span>
            </div>
        </li>
    </ul>
</div>
<!--footer-->
<div class="footer">
    <p class="copyright">
        版权所有@1997-2018 北京超图软件股份有限公司 京ICP备11032883号-1
    </p>
</div>
</body>
</html>
<!--加载jQuery文件-->
<script src="resource/lib/jquery-1.9.1/jquery.min.js"></script>
<!--bootstrap插件-->
<script src="resource/lib/bootstrap/js/bootstrap.min.js"></script>

<script src="resource/js/common.js"></script>
<script src="resource/js/feedback.js"></script>