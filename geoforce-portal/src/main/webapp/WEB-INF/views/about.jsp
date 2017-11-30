<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>关于我们</title>
    <link rel="stylesheet" type="text/css" href="resources/css/feedback.css">
</head>
<body>
<main>
    <section class="about">
        <h1>地图慧开放平台·关于我们</h1>
        <p>地图慧开放平台拥有基础地图服务开放能力：定位、影像、出行、轨迹、数据、分析，并将六大服务能力免费开放给开发者使用。</p>
    </section>
    <div class="content" style="padding: 50px 100px">
        <div class="briefing">
            <h3>1 什么是地图慧开放平台</h3>
            <p>地图慧开放平台作为全球首个开放的智能交互技术服务平台，致力于为开发者打造一站式智能人机交互解决方案。用户可通过互联网、移动互联网，使用任何设备、在任何时间、任何地点，随时随地享受讯飞开放平台提供的“听、说、读、写……”等全方位的人工智能服务。目前，开放平台以“云+端”的形式向开发者提供语音合成、语音识别、语音唤醒、语义理解、人脸识别、个性化彩铃、移动应用分析等多项服务。
                国内外企业、中小创业团队和个人开发者，均可在讯飞开放平台直接体验世界领先的语音技术，并简单快速集成到产品中，让产品具备“能听会说会思考会预测”的功能。</p>
        </div>
        <div class="briefing">
            <h3>2 平台特色</h3>
            <p>地图慧开放平台整合了科大讯飞研究院、中国科技大学讯飞语音实验室以及清华大学讯飞语音实验室等在语音识别、语音合成等技术上多年的技术成果，语音核心技术达到了国际领先水平，同时引进国内外最先进的人工智能技术，如人脸识别、语言云等，与学术界、产业界合作，共同打造以语音为核心的全新移动互联网生态圈。</p>
        </div>
        <div class="briefing">
            <h3>3 无限可扩展的开放能力</h3>
            <p>地图慧开放平台除了目前开放的语音识别、语音合成以及语义理解等能力外，随着智能人机交互技术的发展以及开发者的需求，语音唤醒、离线语音合成、离线命令词、声纹识别、人脸识别、声纹识别、语音评测等技术相继开放，打造无限人机智能交互的开放平台。</p>
        </div>
        <div class="briefing">
            <h3>4 应用领域</h3>
            <p>地图慧目前已与长虹、海信、康佳等国内六大电视厂商达成合作，由讯飞开放平台为电视厂商提供语音交互服务，同时为迈乐盒子等电视盒子厂商提供语音交互能力。</p>
        </div>
        <div class="briefing">
            <h3>5 其他</h3>
            <p>地图慧语音为智能音箱（讯飞智能音箱）、聊天机器人（小鱼在家）等智能硬件产品以及窗帘、空调等智能家居产品提供语音技术解决方案。</p>
        </div>
    </div>
</main>
<script type="text/javascript" src="resources/js/include.js"></script>
</body>
</html>