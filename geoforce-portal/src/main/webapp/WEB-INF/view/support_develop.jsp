<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <!--设置移动端-->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <title>帮助与支持</title>

    <!--图标-->
    <link rel="shortcut icon" type="image/x-icon" href="http://www.dituhui.com/favicon.ico">
    <!--引入公共样式文件-->
    <link rel="stylesheet" href="resource/css/normalize.css">
    <link rel="stylesheet" href="resource/lib/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="resource/css/common.css">

    <!--codeMirror-->
    <link rel="stylesheet" href="resource/lib/codemirror/lib/codemirror.css">

    <link rel="stylesheet" href="resource/css/support_develop.css">

    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body style="display: none;"  data-spy="scroll" data-target="#myScroll" data-offset="80">
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
<!--content start-->
<div class="main page_wrapper">
    <!--左侧菜单-->
    <div class="leftBar fl">
        <ul id="menu-grandpa" class="menu-grandpa">

        </ul>
    </div>
    <!--中间内容-->
    <div class="centerBar fl">
        <p class="location">当前位置：
            <a>帮助与支持 </a>/
            <a href="javascript:void(0);" class="level-1">API</a> /
            <a href="javascript:void(0);" class="level-2">线路规划</a> /
            <a class="active level-3">单中心点线路规划</a>
        </p>
        <h1 class="title">单中心点线路规划</h1>
        <h6 class="refresh-time">最后更新时间为2017年12月30日</h6>
        <!--服务地址-->
        <h3 id="address">服务地址</h3>
        <p class="url-address">/pathplan/scpp</p>
        <!--请求参数-->
        <h3 id="requestParam">请求参数</h3>
        <table width="800" class="table table-bordered">

        </table>
        <!--返回结果-->
        <h3 id="returnResults">返回结果</h3>
        <table width="800" class="table table-bordered">

        </table>
        <!--服务示例-->
        <h3 id="serviceDemo">服务示例</h3>
        <p class="url-address demoReqUrl">https://www.ruiwen.com/zhuchici/1183255.html</p>
        <table class="table table-bordered">
            <thead align="center">
                <tr class="active">
                    <td width="25%">参数</td>
                    <td width="25%">值</td>
                    <td width="25%">备注</td>
                    <td width="25%">必选</td>
                </tr>
            </thead>
            <tbody align="center">
                <tr>
                    <td>address</td>
                    <td><input class="form-control"/></td>
                    <td>解析结果</td>
                    <td>是</td>
                </tr>
                <tr>
                    <td>isPrecise</td>
                    <td><input class="form-control"/></td>
                    <td>是否精确</td>
                    <td>否</td>
                </tr>
            </tbody>
        </table>
        <div>
            <a href="javascript:void(0);" class="run" id="run">运行</a>
            <a href="javascript:void(0);" class="run" id="clear">清空</a>
        </div>
        <div id="return-result" class="return-result">
            <textarea id="demoCode"></textarea>
        </div>
        <!--错误代码说明-->
        <h3 id="errorCode">错误代码说明</h3>
        <table class="table table-bordered">

        </table>
    </div>
    <!--右侧导航-->
    <div class="rightBar" id="myScroll" >
        <ul class="nav">
            <li class="active"><a href="#address">服务地址</a></li>
            <li><a href="#requestParam">请求参数</a></li>
            <li><a href="#returnResults">返回结果</a></li>
            <li><a href="#serviceDemo">服务示例</a></li>
            <li><a href="#errorCode">错误代码说明</a></li>
        </ul>
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
            <span>地图慧</span>
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
                <span>地图慧</span>
            </div>
        </li>
    </ul>
</div>
<!--footer-->
<div class="footer">
    <p class="copyright">
        Copyright © 2018地图慧 京ICP备11032883号-5 京ICP证120611号 甲测资质1100363
    </p>
</div>
</body>
</html>
<!--加载jQuery文件-->
<script src="resource/lib/jquery-1.9.1/jquery.min.js"></script>
<!--bootstrap插件-->
<script src="resource/lib/bootstrap/js/bootstrap.min.js"></script>
<!--codemirror-->
<script type="text/javascript" src="resource/lib/codemirror/lib/codemirror.js"></script>
<script type="text/javascript" src="resource/lib/codemirror/mode/javascript/javascript.js"></script>
<script type="text/javascript" src="resource/lib/codemirror/addon/edit/matchbrackets.js"></script>
<script type="text/javascript" src="resource/lib/codemirror/addon/comment/continuecomment.js"></script>
<script type="text/javascript" src="resource/lib/codemirror/addon/comment/comment.js"></script>

<script src="resource/js/common.js"></script>
<script src="resource/js/support_develop.js"></script>