<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>常见问题</title>
    <link rel="stylesheet" type="text/css" href="resources/css/developmentDoc.css">
    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css'>
    <link rel="stylesheet" href="resources/css/paper-collapse.min.css">
</head>
<body>
<main>
    <div class="FAQ">
        <!-- <div class="FAQ-con">
            <h1>真诚服务，如友相随</h1>
            <p class="FAQ-one">您好，有什么可以帮助您？</p>
            <p class="FAQ-two">在这里您可以自助查到所需的问题解答，还可以联系客服人员为您提供帮助。</p>
        </div> -->
    </div>
    <div class="content-wrap">
        <div class="content">
            <div class="left-menu">
                <ul class="item">
                    <li class="dropdown"><a href="#gaopin" data-toggle="dropdown">高频问题</a></li>
                    <li class="dropdown"><a href="#" data-toggle="dropdown">定位问题</a></li>
                    <li class="dropdown"><a href="#" data-toggle="dropdown">权限问题</a></li>
                    <li class="dropdown"><a href="#" data-toggle="dropdown">开发问题</a></li>
                </ul>
            </div>
            <div class="right-con">
                <!--<h4>常见问题</h4>-->
                <div class="right-content">
                    <div id="gaopin" class="zzsc-container" style="display:none">
                        <div class="container">
                            <div class="collapse-card">
                                <div class="collapse-card__heading">
                                    <h5 class="collapse-card__title">
                                        <i class="fa fa-question-circle fa-2x fa-fw"></i>
    地图慧开放平台API是否收费？
                                    </h5>
                                </div>
                                <div class="collapse-card__body">
    地图慧API主要是服务器端Web服务API, 是免费申请使用的，不收取任何费用，也不存在付费版本功能有差异的情况。
                                </div>
                            </div>
                            <div class="collapse-card">
                                <div class="collapse-card__heading">
                                    <h5 class="collapse-card__title">
                                        <i class="fa fa-question-circle fa-2x fa-fw"></i>
    地图慧可以判断当前位置的地址以及省/市/区县的信息，还能够判断我在“××学校”，“××小区”内吗？
                                    </h5>
                                </div>
                                <div class="collapse-card__body">
    地图慧的逆地理编码服务可以获得让您在输入坐标的情况下，返回给这个坐标对应的地址，以及相应的省/市/区县信息。
    除此之外，为了满足开发者更多的应用需求，我们会在结果中增加更多的地理信息。
    区划面信息，表示当前坐标位于某个具体的，地图慧数据已知的地物对象的面内（具体如下图所示），这些面一般在地图慧中，可以搜索得到他具体的面状轮廓的显示。<br>
                                </div>
                            </div>
                            <div class="collapse-card">
                                <div class="collapse-card__heading">
                                    <h5 class="collapse-card__title">
                                        <i class="fa fa-question-circle fa-2x fa-fw"></i>
    在搜索时，输入“天安”，自动在下拉列表中提示 “天安门广场”,是如何实现的？
                                    </h5>
                                </div>
                                <div class="collapse-card__body">
    该功能是依赖与我们API中的“输入提示”功能，依据地图慧的数据库，我们会在输入未完成时，自动根据相关、相似的地址，地名库自动提示补全信息。
                                </div>
                            </div>
                            <div class="collapse-card">
                                <div class="collapse-card__heading">
                                    <h5 class="collapse-card__title">
                                        <i class="fa fa-question-circle fa-2x fa-fw"></i>
    如何进行商户标注？
                                    </h5>
                                </div>
                                <div class="collapse-card__body">
    首先,需要明确，地图慧并不能把所有的数据都标注在地图上，我们根据算法把一部分我们认为最重要的点直接显示在地图上，所以请先确认，您关注的数据是不是通过输入对应的关键词进行搜索，仍然无法展现出来。
    如果您发现地图中没有搜到您关注的数据，我们开放了数据标注的通道，您可以自助完成数据标注、提交的过程，待我们审核完成之后，在地图中就可以搜索到您提交的点数据了。
                                </div>
                            </div>
                            <div class="collapse-card">
                                <div class="collapse-card__heading">
                                    <h5 class="collapse-card__title">
                                        <i class="fa fa-question-circle fa-2x fa-fw"></i>
    地图里面的POI和AOI信息是什么意思？
                                    </h5>
                                </div>
                                <div class="collapse-card__body">
    您好，在地图里面，POI是指兴趣点，是Point of Interest的缩写，每个POI至少包含以下4项基本信息：名称（Name）、类别（Type）、经度（longitude）、纬度（latitude），所以poiName指的是兴趣点名称，它可以是一栋房子、一个商铺、一个公厕或一个公交站等等。而AOI是area of interest的缩写，顾名思义，其代指的是兴趣面，同样包含4项基本信息，指的是地图数据中的区域状的地理实体。希望能够解答您的疑问。
                                </div>
                            </div>
                            <div class="collapse-card">
                                <div class="collapse-card__heading">
                                    <h5 class="collapse-card__title">
                                        <i class="fa fa-question-circle fa-2x fa-fw"></i>
                                        为什么点标记在地图缩放后，偏离原来的位置？
                                    </h5>
                                </div>
                                <div class="collapse-card__body">
                                    在地图上添加某个坐标的点标记，如果点标记的图片大小不是API默认图片大小（36*36），需要根据点标记图片大小设置偏移量。偏移量设置方法请参考开发指南。
                                </div>
                            </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    Web服务API访问出错怎么办？
    </h5>
    </div>
    <div class="collapse-card__body">
    当您遇到Web服务API访问出错的情况，请您先对照错误码表，查询您的错误原因和解决方案。

    如仍然无法解决您的问题，请按照以下格式提交工单，我们会尽快为您解决。<br>

    Key：<br>

    完整请求串：<br>

    请求时间：（尽量精确到秒）<br>

    返回的错误信息：<br>

    请求响应的Header信息 ：（可选，建议添加，便于追查问题记录）
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    返回的路线规划方案不合理怎么办？
    </h5>
    </div>
    <div class="collapse-card__body">
    如果您使用我们的路线规划服务，发现返回的方案不合理，如包含禁行路段、特别拥堵、绕路等情况，请您提交工单，我们将尽快为您解决。<br>

    为了您能迅速收到反馈，请您在工单中详细填写以下信息：<br>

    IMEI（Android） /IDFV（iOS）：<br>

    路线规划时间：（尽量精确到秒）<br>

    起点：（POI名称或地图慧坐标）<br>

    终点：（POI名称或地图慧坐标）
    </div>
    </div>
                        </div>
                    </div>
    <div id="dingwei" class="zzsc-container" style="display:none">
    <div class="container">
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    为什么GPS定位时间长短不一？
    </h5>
    </div>
    <div class="collapse-card__body">
    GPS模块一般支持冷启动（Cold start） 和热启动（Hot start） 两种模式。（注：暖启动warm-start不讨论）
    一般冷启动比热启动时间要长很多，以信号足够好为例，SirFIII的热启动为15秒以内，冷启动则需要42秒以内；在信号弱的情况下则需要更长时间。<br>
    热启动条件如下：<br>
    1.上次关机前的位置信息（经纬度，高度）已知（所以在车库内关机下次就一定是冷启动了）；<br>
    2.当前时间、年历、星历已知（需要实时时钟支持）；<br>
    3.距离上次关机时间不超过4小时（时间过长则星历变化了，即以前的卫星看不到了） 。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    地图慧地图定位SDK和地图慧地图定位API有什么区别？
    </h5>
    </div>
    <div class="collapse-card__body">
    1、地图慧地图定位SDK<br>
    是为移动平台APP专门打造的定位工具。
    它在地图慧地图定位API基础上封装了大量的定位策略，充分利用WIFI、基站、GPS的特性，返回融合定位结果。
    不仅提高了定位效果，也最大程度的降低了开发者的使用成本。地图慧地图定位SDK返回地图慧坐标，结果能正确地显示在地图慧地图上，适用于Android和iOS两个移动平台。<br>

    2、地图慧地图定位API<br>
    是获取地图慧网络定位结果的一整套解决方案，它更适用于非Android、iOS的智能硬件解决方案中，API可以返回WIFI定位和基站定位结果。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    地图慧地图定位SDK的定位策略是什么？
    </h5>
    </div>
    <div class="collapse-card__body">
    定位SDK融合基站+WiFi+ GPS定位，输出了三种定位模式：高精度模式，低功耗模式，仅用设备模式。<br>

    定位SDK会根据设备所处的环境自动调整定位策略，如在同一个的位置没有移动时，SDK会返回缓存定位结果，如果APP不希望使用缓存结果可以通过定位类型进行过滤。
    </div>
    </div>
    </div>
    </div>
    <div id="quanxian" class="zzsc-container" style="display:none">
    <div class="container">
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    注册时收不到邮箱验证码怎么办？
    </h5>
    </div>
    <div class="collapse-card__body">
    1、换个邮箱试试<br>
    2、请在邮箱->设置-->域白名单里，将@autonavi.com设置成为白名单。<br>
    <img src="http://cache.amap.com/lbs/static/qa-email.png" alt="qa-email" width="900" height="416" class="alignnone size-full wp-image-5786">
    3、如果还不能收到验证邮件，请您提交工单联系我们，我们会帮您直接创建开发者账号。请注意信息的真实性，否则无法找回密码喔。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    能否删除已经申请过的Key？
    </h5>
    </div>
    <div class="collapse-card__body">
    目前，支持Key删除功能，但删除的Key无法恢复，还请您谨慎操作。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    如何使用数字签名？
    </h5>
    </div>
    <div class="collapse-card__body">
    使用步骤<br>
    1.从控制台中开通数字签名<br>
    2.点击申请数字签名的按钮后，获取用于生成签名的私钥（该签名与Key对应，请注意保存，不要泄露）。<br>
    3.根据规则生成签名：<br>
    签名=MD5(请求参数（参数名的hash值升序排序）+（+号无需输入）私钥)；<br>
    例如：请求服务为testservice，请求参数分别为a=23，b=12，d=48，f=8，c=67；私钥为bbbbb 则数字签名：sig=MD5(a=23&b=12&c=67&d=48&f=8bbbbb)<br>
    注：生成签名的内容，即MD5()当中的内容，必须为utf-8编码格式。<br>
    4.在请求中添加签名<br>
    将签名sig作为参数添加至请求参数中：参数名为sig，值为根据请求参数与私钥计算出的值。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    为何要开启数字签名？
    </h5>
    </div>
    <div class="collapse-card__body">
    开启数字签名，可以确保您的Key被安全地调用。<br>

    数字签名未开启的情况下，如果窃取者盗用您的Key发起非法访问，会被我们的策略自动识别。我们会对该Key进行强制处理，导致Key不能正常使用，继而影响到您的正常调用。<br>

    如果开启数字签名，即使Key被泄露，窃取者在拿不到数字签名的情况下，是无法正常使用该Key发起请求的，故不会对您造成损失。
    </div>
    </div>
    </div>
    </div>
    <div id="kaifa" class="zzsc-container" style="display:block">
    <div class="container">
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    申请Web服务API服务需要注意什么？
    </h5>
    </div>
    <div class="collapse-card__body">
    有两点需要注意:<br>
    1、申请地图慧Web服务API，务必绑定IP白名单，否则会被我们系统识别为非法key，被自动封停。<br>
    2、请各位合作伙伴注意：使用地图慧Web服务API，严禁做压力测试，如擅自做压力测试，系统立即会识别出来，并自动封停服务。由此引发的损失地图慧概不负责。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    路径规划中获取到的距离是否会变动？
    </h5>
    </div>
    <div class="collapse-card__body">
    我们的路径规划接口会按照路况返回规划结果，所以会有一定的浮动，一般浮动在1~2km。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    如何获取某小区位置数据，并将添加小区的标签数据在地图显示？
    </h5>
    </div>
    <div class="collapse-card__body">
    可以通过地理编码接口获取中心点坐标。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    路行规划结果（polyline)中的道路节点，是如何选取的？
    </h5>
    </div>
    <div class="collapse-card__body">
    会根据路线的情况每隔一段距离就取一个点，由于路况的不同，取点的范围也是不同的。
    </div>
    </div>
    <div class="collapse-card">
    <div class="collapse-card__heading">
    <h5 class="collapse-card__title">
    <i class="fa fa-question-circle fa-2x fa-fw"></i>
    接口访问超时了怎么办？
    </h5>
    </div>
    <div class="collapse-card__body">
    请提工单给我们，选择技术咨询 -> Web服务API，并提供如下信息：<br>

    接口的使用场景？（app-sdk 调用 或 客户服务器直接调用）<br>

    接口调用（超时/响应慢) 的 具体的影响面 或 特征 （哪些地区/运营商/集中的时间段）<br>

    调用 地图慧 api 请求的源地址,（公网出口IP）<br>

    用户机房所在地<br>

    用户访问访问地图慧服务超时时候的mtr结果以及ping结果<br>

    异常的url 、及错误提示等待<br>

    异常时间点(到秒)及 时间段<br>

    用户的key

    </div>
    </div>
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

<script type="text/javascript" src="resources/lib/jquery_1.11.3/jquery.min.js"></script>
<script src="resources/js/common.js"></script>
<script src="resources/lib/paper-collapse.min.js"></script>
<script>
    $('.collapse-card').paperCollapse();
</script>
</body>
</html>