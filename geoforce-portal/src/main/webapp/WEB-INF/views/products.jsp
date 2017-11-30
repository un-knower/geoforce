<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>地图慧API</title>
    <link rel="stylesheet" type="text/css" href="resources/css/developmentDoc.css">
    <link href="resources/css/jquery-accordion-menu.css" rel="stylesheet" type="text/css" />
    <%--zr新增的样式--%>
    <link rel="stylesheet" type="text/css" href="resources/css/newproduct.css">
    <link rel="stylesheet" type="text/css" href="resources/lib/codemirror/css/codemirror.css">
</head>
<body>

    <!--右侧友情帮助链接-->
    <div id="helpLink">
        <ul>
            <li><img src="resources/img/info_kzt_normal.png"><a href="#top">回到顶部</a></li>
            <li><img src="resources/img/info_kzt_normal.png"><a href="##">我要开发</a></li>
            <li><img src="resources/img/info_kzt_normal.png"><a href="##">常见问题</a></li>
        </ul>
    </div>
    <!--用户反馈遮罩-->
	<div id="shade" class="shade"></div>


<main>
    <div class="content-wrap" style="padding-top: 25px;">
        <div class="content">
            <div class="left-menu" id="menu">
                <ul class="item" id="demo-list">
                    <li class="lv-1" data-link="gaishu">
                        <a href="javascript:void(0);">概述</a>
                    </li>
                    <li class="lv-1" data-link="getKey">
                        <a href="javascript:void(0);">获取KEY</a>
                    </li>

                    <li class="lv-1" data-link="useLimit">
                        <a href="javascript:void(0);">
                            使用限制
                        </a>
                    </li>
                </ul>
            </div>
            <div class="right-con">
                <div class="right-content">
                	<!--用户反馈-->
                    <!--
                	<div id="userFeed" class="userFeed">用户反馈</div>
                	<div id="userModal" class="modal-dialog1">
                		<div class="modal-content">
                			<div class="modal-header" style="display: none;"></div> 
                			<div class="modal-body">
                				<div>
                					<h4>请选择您要反馈的类型</h4> 
                					<div type="document" class="feedback-item document1">
                						<div class="item-icon document"></div> 
                						<div class="item-name">这篇文章</div> 
                						<div class="item-type">文章反馈</div>
                					</div> 
                					<div type="product" class="feedback-item product1">
                						<div class="item-icon pro"></div> 
                						<div class="item-name">Demo演示</div> 
                						<div class="item-type">产品反馈</div>
                					</div>
                				</div>
                			</div> 
                			<div class="modal-footer">
                				<div>
                					<div class="feedback-faq">使用问题及技术咨询请访问<a target="_blank" href="saleSupport">帮助与支持</a></div> 
                					<button id="modal-cancel">取消</button>
                				</div>
                			</div>
                		</div>
                	</div>
                	<div id="thankModal" class="modal-dialog2">
                        <div class="modal-content">
                            <div class="modal-header">
                                <div>
                                    <div class="modal-header-icon"></div> 
                                    <span id="close" class="close">×</span>
                                    <h4 class="modal-title">感谢您的反馈</h4>
                                 </div>
                            </div>
                            <div class="modal-body">
                                <div>
                                    <p class="rate-alert">通过该反馈，您可以将对高德开放平台产品和文档的意见和建议发送给我们。如需咨询产品使用问题或获取技术支持，请访问<a data-v-48a092ae="" href="saleSupport">帮助与支持</a></p>
                                    <div class="below-alert">
                                        <textarea id="textVal" placeholder="请具体描述您的建议或遇到的问题（500字内）"></textarea>
                                    </div>
                                </div>
                            </div> 
                            <div class="modal-footer">
                                <button id="cancel" class="cancel">取消</button> 
                                <button id="submit" class="submit">提交</button>
                            </div>
                        </div>
                    </div>-->

                    <!--星级评价-->
                    <!--
                    <div class="rate-container">
                        <div>
                            <img src="resources/img/star-off.png" alt="1">
                            <img src="resources/img/star-off.png" alt="2">
                            <img src="resources/img/star-off.png" alt="3">
                            <img src="resources/img/star-off.png" alt="4">
                            <img src="resources/img/star-off.png" alt="5">
                            <div class="desc" style="display: none;"></div>
                        </div> 
                    </div>
                    -->
                    <!--概述-->
                    <div id="gaishu" class="document-page fadeSlow" style="display: block;">
                        <h4>您现在的位置：产品 / 概述</h4>
                        <div class="title">概述</div>
                        <div class="update-time">最后更新时间2016年12月26日</div>
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
                    <!-- 申请key -->
                    <div id="getKey" class="document-page fadeSlow" style="width:100%;">
                        <h4>您现在的位置：产品 / 申请KEY</h4>
                        <div class="title">如何申请Key</div>
                        <h3>1、创建新应用</h3>
                        <p>
                            进入<a href="http://lbs.amap.com/dev/" class="" target="">控制台</a>，创建一个新应用。如果您之前已经创建过应用，可直接跳过这个步骤。</p>
                        <div>
                            <img src="http://a.amap.com/lbs/static/img/2017dev_dev.png" alt="">
                        </div>
                        <p><br></p>
                        <div>
                            <img src="http://a.amap.com/lbs/static/img/2017dev_create_app.png" alt="">
                        </div>
                        <h3>2、添加新Key</h3>
                        <p>在创建的应用上点击"添加新Key"按钮，在弹出的对话框中，依次：输入应用名名称，选择绑定的服务为“web服务API”，如下图所示：</p>
                        <div>
                            <img src="http://a.amap.com/lbs/static/img/2017dev_webservice_key.png" alt="">
                        </div>
                        <p>在阅读完高德地图API服务条款后，勾选此选项，点击“提交”，完成 Key 的申请，此时您可以在所创建的应用下面看到刚申请的 Key 了。</p>
                    </div>
                	<!--api内容-->
                    <div id="apiDoc" class="document-page fadeSlow" style="display:block;">

                    </div>
                    <!--使用限制-->
                    <div id="useLimit" class="document-page fadeSlow">
                        <h4>您现在的位置：产品 / 使用限制</h4>
                        <p>使用限制页面！！！！</p>
                    </div>
                </div>
        </div>
    </div>
</main>
<script type="text/javascript" src="resources/js/include.js"></script>
<script type="text/javascript" src="resources/libs/SuperMap.Include.js"></script>
 <!--代码编辑器-->
<script src="resources/lib/codemirror/js/codemirror.js"></script>
<script src="resources/lib/codemirror/js/xml.js"></script>
<script src="resources/lib/codemirror/js/javascript.js"></script>
<!--折叠插件-->
<script src="resources/lib/jquery_1.11.3/jquery.min.js"></script>
<script src="resources/lib/jquery-accordion-menu.js"></script>
<script type="text/javascript">
    //初始化菜单和事件
    $(document).ready(function(){
        initMenu();
        regEvent();
    });
    //初始化菜单
    function initMenu(){
        httpRequest({
            url: "api/findAll",
            type:"get",
            success: function(e){
                if(e && e.isSuccess){
                    var menu=e.result.data;
                    var h="";
                    $.each(menu,function(i,o){
                        h += '<li id="apiMenu'+o.groupId+'" class="lv-1 apiMenu" data-link=" ">';
                        h += '<a href="javascript:void(0);" data-id="'+o.groupId+'">'+o.groupName+'</a>';
                        h += '<ul class="submenu" style="display: none">';
                        var menuChild = o.apiVos;
                        $.each(menuChild,function(i,oChild){
                            h += '<li class="lv-2"><a href="javascript:void(0);" data-id=' +oChild.apiId+ '>'+ oChild.apiName +'</a></li>';
                        });
                        h+='</ul>';
                        h+='</li>';
                    });
                    $("#demo-list").find('li').eq(1).after(h);
                    // 菜单调用
                    jQuery("#menu").jqueryAccordionMenu();
                    //展开api菜单
                    var apiMenu = $("#demo-list li.apiMenu");
                    for (var i = 0;i<apiMenu.length;i++){
                        var idValue = $(this).attr('id');
                        $("#"+idValue).click();
                    }
                    //绑定二级菜单事件
                    $("#demo-list li.lv-2").click(function(e){
                        e.stopPropagation();
                        if($(this).hasClass("active")){
                            $(this).removeClass("active");
                        }else{
                            $("#demo-list li.lv-2.active").removeClass("active");
                            $("#demo-list li.lv-1.open").removeClass("open");
                            $(this).addClass("active");
                            var id=$($(this).find("a")).attr("data-id");
                            httpRequest({
                                url: "api/findOne/"+id,
                                type:"get",
                                //返回成功进行的操作
                                success: function(e){
                                    if(e && e.isSuccess){
                                        var api=e.result.data;
                                        //显示api对应的文档
                                        $(".document-page").hide();
                                        $("#apiDoc").show();
                                        buildApiDoc(api);
                                        demo();
                                    }
                                },
                                error: function(e){
                                    console.log("请求出错");
                                }
                            });
                        }
                    });
                }
            },
            error: function(e){
                console.log("请求出错");
            }
        });
    }

    //事件
    function regEvent(){
        //绑定一级菜单事件
        $("#demo-list li.lv-1").click(function(){
            if($(this).hasClass("open")){
                $(this).removeClass("open");
            }else{
                $("#demo-list li.lv-1.open").removeClass("open");
                $("#demo-list li.lv-2.active").removeClass("active");
                $(this).addClass("open");
                //显示一级菜单对应的内容
                var link=$(this).attr("data-link");
                if(link!=""){
                    $(".document-page").fadeOut(100);
                    $("#"+link).fadeIn(200);
                }
                if(link=="errorCode"){
                    var data=JSON.parse(error_code).result.data;
                    buildErrorCode(data);
                }
            }
        });
    }
    //构建api详情
    function buildApiDoc(data){
        var doc='';
        doc+='<h4>您现在的位置：产品 / '+data.groupSet.groupName+' / '+data.apiName+'</h4>';
        doc+='<div class="title">'+ data.apiName+'</div>';
        doc+='<div class="update-time">最后更新时间'+data.updateDate+'</div>';
        doc+='<h3>产品介绍</h3>'+
            '<p>'+data.description+'</p>';
        <!--功能示例-->
        doc+='<h3>功能示例</h3>';
        <!--地图显示的div-->
        doc+='<div class="Demo demoImg">'+
                 '<div id="mapImg" style="width:100%;height:350px;" ></div>'+
                 '<div id="iControlbox" class="control">'+
                <!--输入省、直辖市或县名称：<input type="text" id="districtName" value="四川省">-->
                 '<input type="button" onclick="getBoundary()" value="线路规划">'+
                '</div>'+
                '</div>'+
                '<div class="Demo demoCode">'+
                <!--示例代码-->
                '<textarea id="codeMirror">'+data.requestDemo+'</textarea>'+
            '</div>';
        <!--使用方法-->
        doc+='<h3>使用方法</h3>'+
             '<p>1.无需展现地图的场景下，进行线路查询，如以线路结果页形式展现换乘方案；<br>'+
             '2.根据返回线路数据，自行开发线路导航。</p>';
        <!--更新日志-->
        doc+='<h3>更新日志</h3>'+
             '<p>2017.11.1:新增一个属性！！<br>'+
             '2017.11.15:修复BUG!!</p>';
        $("#apiDoc").html(doc);
    }

    function demo(){
        var editor = CodeMirror.fromTextArea(document.getElementById("codeMirror"), {
            mode: "text/html",
            smartIndent : true,
            lineWrapping: true
        });
       //codeMirror获取转义之后的数据（getValue()方法）
        var html = editor.getValue();
        $('#mapImg').html(html);
    }


</script>
</body>
</html>