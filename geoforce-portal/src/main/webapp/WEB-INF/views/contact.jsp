    <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
            <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>联系我们</title>
    <link rel="stylesheet" type="text/css" href="resources/css/feedback.css">
</head>
<body>
<main>
    <section class="contact">
        <h1>地图慧开放平台·联系我们</h1>
        <p>地图慧开放平台拥有基础地图服务开放能力：定位、影像、出行、轨迹、数据、分析，并将六大服务能力免费开放给开发者使用</p>
    </section>
    <div class="content" style="padding: 50px 100px">
        <div class="contact-top">
            <div class="left-con">
                <p>北京超图软件股份有限公司</p>
                <p>地址：北京市朝阳区酒仙桥北路 甲10号院电子城IT产业园107楼6层 （邮编：100015）</p>
                <p>公司总机：+86-10-59896655</p>
                <p>公司传真：+86-10-59896666</p>
                <p>技术支持与客户监督热线：400-8900-866</p>
                <p>北京地区技术支持热线： +86-10-59896699</p>
                <p>技术支持电子邮箱：support@supermap.com</p>
                <p>客户监督电子信箱：cs@supermap.com</p>
                <p>开户银行：招商银行北京分行望京支行</p>
                <p>开户名称：北京超图软件股份有限公司</p>
                <p>人民币账号：110902097610803</p>
            </div>
            <div class="right-pic">
                <img src="resources/img/contact.png" alt="">
            </div>
        </div>
        <div class="contact-middle">
            <dl>
                <dt>销售咨询</dt>
                <dd>咨询业务请致电：</dd>
                <dd>+86-10-80049324转3245</dd>
                <dd>sales@supermap.com</dd>
            </dl>
            <dl>
                <dt>客户服务</dt>
                <dd>相关的技术问题：</dd>
                <dd>邮件：support@supermap.com</dd>
                <dd>请拨：400-8900-866</dd>
            </dl>
            <dl>
                <dt>人力资源</dt>
                <dd>咨询业务请致电：</dd>
                <dd>邮件：hr@supermap.com</dd>
                <dd>sales@supermap.com</dd>
            </dl>
            <dl>
                <dt>客户监督邮箱</dt>
                <dd>咨询业务请致电：</dd>
                <dd>下载超图客户投诉处理表>.rar</dd>
                <dd>邮件：cs@supermap.com</dd>
            </dl>
        </div>
        <div class="contact-bottom">
            <h3>关注我们</h3>
            <a href="http://www.weibo.com/dituhui?is_hot=1" target="_blank"><img src="resources/img/Contact_weibo.png" alt=""></a>
            <a href=""><img src="resources/img/Contact_qq.png" alt=""></a>
            <a href=""><img src="resources/img/Contact_weixin.png" alt=""></a>
        </div>
    </div>
</main>
<script type="text/javascript" src="resources/js/include.js"></script>
</body>
</html>