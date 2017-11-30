<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>开发文档</title>
    <link rel="stylesheet" type="text/css" href="resources/css/developmentDoc.css">
</head>
<body>
<main>
    <section>
        <h1>一款优雅，轻便的在线文档分享…</h1>
    </section>
    <div class="content-wrap">
        <div class="content">
            <div class="left-menu">
                <ul class="item">
                    <li class="dropdown">
                        <a href="developmentDoc" data-toggle="dropdown">概述</a>
                    </li>
                    <li class="dropdown">
                        <a href="#" data-toggle="dropdown">获取key</a>
                    </li>
                    <li class="dropdown">
                        <a href="#" data-toggle="dropdown">API文档<i class="icon-arrow close"></i></a>
                        <ul class="dropdown-menu">
                            <li><a href="developmentDoc/coordinateConvert.html">坐标转换</a></li>
                            <li><a href="">地理编码</a></li>
                            <li><a href="">数据服务</a></li>
                            <li><a href="">POI检索</a></li>
                            <li><a href="">区域面合并</a></li>
                            <li><a href="">空间运算</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" data-toggle="dropdown">更新日志</a>
                    </li>
                </ul>
            </div>
            <div class="right-con">
                <h4>API参考 > 创建实例</h4>
                <div class="right-content">
                    <div class="title">概述</div>
                    <div class="update-time">最后更新时间2016年12月26日</div>
                    <div>
                        <h3>Web服务API</h3>
                        <p>地图慧Web服务API为开发者提供http接口，即开发者通过http形式发起检索请求，获取返回json或xml格式的检索数据。用户可以基于此开发JavaScript、C#、C++、Java等语言的地图应用。<br>
                        该套API免费对外开放，使用前请先<a href="console">申请密钥（key）</a>，通过在线方式调用。每个key有对应的访问限制（如：坐标转换API的每个key限制为2000次/天）。如果有更高配额需求，请点击<a href="console">配额申请</a>。<br>
                        </p>
                        <h3>功能介绍</h3>
                        <div class="pic-box">
                            <div class="item">
                                <img src="resources/img/wangdian.png" alt="">
                                <dl>
                                    <dt>坐标转换</dt>
                                    <dd>
                                    <a href="">该接口可实现火星坐标，百度坐标，墨卡托坐标间的相互转换。支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。</a>
                                    </dd>
                                </dl>
                            </div>
                            <div class="item">
                                <img src="resources/img/quhua.png" alt="">
                                <dl>
                                    <dt>地理编码</dt>
                                    <dd>
                                    <a href="">通过地址获取坐标值或通过坐标点获取详细地址信息描述服务。支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。</a>
                                    </dd>
                                </dl>
                            </div>
                            <div class="item">
                                <img src="resources/img/luxian.png" alt="">
                                <dl>
                                    <dt>数据服务</dt>
                                    <dd>
                                    <a href="">支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。</a>
                                    </dd>
                                </dl>
                            </div>
                            <div class="item">
                                <img src="resources/img/fendan.png" alt="">
                                <dl>
                                    <dt>POI检索</dt>
                                    <dd>
                                    <a href="">支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。</a>
                                    </dd>
                                </dl>
                            </div>
                            <div class="item">
                                <img src="resources/img/car.png" alt="">
                                <dl>
                                    <dt>区域面合并</dt>
                                    <dd>
                                    <a href="">支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。</a>
                                    </dd>
                                </dl>
                            </div>
                            <div class="item">
                                <img src="resources/img/more.png" alt="">
                                <dl>
                                    <dt>空间运算</dt>
                                    <dd>
                                    <a href="">支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。支持城市、矩形及圆形区域关键字检索POI，返回json/xml格式的POI数据。</a>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                        <h3>账号及key申请</h3>
                        <p>
                            注册成为地图慧开发者只需一步，注册一个地图慧账号，即可去控制台创建应用并申请key。<a href="#">获取API key</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
 <!--底部 -->
<!-- <footer>
    <div class="footer">
        <div class="copyright">版权所有@1997-2016 北京超图软件股份有限公司 京ICP备11032883号-1</div>
        <aside>
            <i class="phone"></i><span>400-8900-866</span>
            <i class="email"></i><span>联系我们</span>
        </aside>
    </div>
</footer> -->

<script type="text/javascript" src="resources/js/include.js"></script>
<script>
    // Dropdown Menu
    var dropdown = document.querySelectorAll('.dropdown');
    var dropdownArray = Array.prototype.slice.call(dropdown,0);
    dropdownArray.forEach(function(el){
        var button = el.querySelector('a[data-toggle="dropdown"]'),
                menu = el.querySelector('.dropdown-menu'),
                arrow = button.querySelector('i.icon-arrow');

        button.onclick = function(event) {
            if(!menu.hasClass('show')) {
                menu.classList.add('show');
                menu.classList.remove('hide');
                arrow.classList.add('open');
                arrow.classList.remove('close');
                event.preventDefault();
            }
            else {
                menu.classList.remove('show');
                menu.classList.add('hide');
                arrow.classList.remove('open');
                arrow.classList.add('close');
                event.preventDefault();
            }
        };
    });

    Element.prototype.hasClass = function(className) {
        return this.className && new RegExp("(^|\\s)" + className + "(\\s|$)").test(this.className);
    };
</script>
</body>
</html>