<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <!--设置移动端-->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>常见问题</title>

    <!--引入公共样式文件-->
    <link rel="stylesheet" href="resource/css/normalize.css">
    <link rel="stylesheet" href="resource/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resource/css/common.css">

    <link rel="stylesheet" href="resource/css/FAQ.css">

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
    <h1>常见问题</h1>
</div>
<div class="main page_wrapper">
    <div class="leftBar fl">
        <ul>
            <li><a href="news_list.html">平台公告</a></li>
            <li class="active"><a href="FAQ.html">常见问题</a></li>
        </ul>
    </div>
    <div class="rightBar fr">
        <p class="location">当前位置：<a href="index.html">首页</a> / <span>常见问题</span></p>
        <div class="faq-content current">
            <p class="faq-type">·常见问题</p>
            <div class="faq-list">
                <p class="faq-question">Q:申请Web服务API服务需要注意什么？<span class="glyphicon glyphicon-plus"></span></p>
                <div class="faq-answer">
                    <p>A:有两点需要注意:</p>
                    <p>1、申请地图慧Web服务API，务必绑定IP白名单，否则会被我们系统识别为非法key，被自动封停。</p>
                    <p>2、请各位合作伙伴注意：使用地图慧Web服务API，严禁做压力测试，如擅自做压力测试，系统立即会识别出来，并自动封停服务。由此引发的
                        损失地图慧概不负责。</p>
                </div>
            </div>
            <div class="faq-list">
                <p class="faq-question">Q:路径规划中获取到的距离是否会变动？<span class="glyphicon glyphicon-plus"></span></p>
                <div class="faq-answer">
                    <p>A:我们的路径规划接口会按照路况返回规划结果，所以会有一定的浮动，一般浮动在1~2km。</p>
                </div>
            </div>
            <div class="faq-list">
                <p class="faq-question">Q:如何获取某小区位置数据，并将添加小区的标签数据在地图显示？<span class="glyphicon glyphicon-plus"></span></p>
                <div class="faq-answer">
                    <p>A:可以通过地理编码接口获取中心点坐标。</p>
                </div>
            </div>
            <div class="faq-list">
                <p class="faq-question">Q:路线规划结果（polyline）中的道路节点，是如何选取的？<span class="glyphicon glyphicon-plus"></span></p>
                <div class="faq-answer">
                    <p>A:会根据路线的情况每隔一段距离就取一个点，由于路况的不同，取点的范围也是不同的。</p>
                </div>
            </div>
            <div class="faq-list">
                <p class="faq-question">Q:接口访问超时了怎么办？<span class="glyphicon glyphicon-plus"></span></p>
                <div class="faq-answer">
                    <p>A:请提工单给我们，选择技术咨询 -> Web服务API，并提供如下信息：</p>
                    <p>1、接口的使用场景？（app-sdk 调用 或 客户服务器直接调用）</p>
                    <p>2、接口调用（超时/响应慢) 的 具体的影响面 或 特征 （哪些地区/运营商/集中的时间段）</p>
                    <p>3、调用 地图慧 api 请求的源地址,（公网出口IP）</p>
                    <p>4、用户机房所在地</p>
                    <p>5、用户访问访问地图慧服务超时时候的mtr结果以及ping结果</p>
                    <p>6、异常的url 、及错误提示等待</p>
                    <p>7、异常时间点(到秒)及 时间段用户的key</p>
                </div>
            </div>
        </div>
    </div>
    <div style="clear: both"></div>
</div>
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
<script src="resource/js/faq.js"></script>