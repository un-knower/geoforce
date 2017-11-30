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
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link href="resources/css/developmentDoc.css" rel="stylesheet" type="text/css" />
    <link href="resources/css/newdevelopment.css" rel="stylesheet" type="text/css" />
    <!--菜单插件样式-->
    <link href="resources/css/jquery-accordion-menu.css" rel="stylesheet" type="text/css" />
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
<main>
    <section>
        <h1>一款优雅，轻便的在线文档分享…</h1>
    </section>
    <div class="content-wrap">
        <div class="content">
            <!-- 左边菜单 -->
            <div class="left-menu">
                <div id="jquery-accordion-menu" class="jquery-accordion-menu white">
                    <ul id="demo-list">
                        <li class="level-1" data-link="errorCode"><a href="javascript:void(0);">全局错误码</a></li>
                    </ul>
                </div>
            </div>
            <!-- 右边内容 -->
            <div class="right-con">
                <div class="right-content">
                    <!-- 位置 -->
                    <div class="location">
                        <h4>您现在的位置：帮助与支持 / 开发文档
                            <span id="location_level1"></span>
                            <span id="location_level2"></span>
                        </h4>
                    </div>
                    <!--开发文档具体内容-->
                    <div id="api" class="document-page fadeSlow" style="display:block;">
                        <div class="title">获取省级行政区列表</div>
                        <div class="update-time">最后更新时间2016年12月26日</div>
                        <h3>服务地址</h3>
                        <p>http://api.dituhui.com/v1/district/getProvinces</p>
                        <h3>请求参数</h3>
                        <table style="margin-top: 5px;">
                            <tbody>
                            <tr>
                                <th width="15%">名称</th>
                                <th width="15%">含义</th>
                                <th width="10%">是否必填</th>
                                <th width="10%">默认值</th>
                                <th>描述</th>
                            </tr>
                            <tr>
                                <td>version</td>
                                <td>数据版本</td>
                                <td>否</td>
                                <td>无</td>
                                <td>当该参数不传或传入空值时，表示从【最新基准版本】中获取数据；当该参数传入5位整数时，表示从【与该ak绑定版本】中获取数据；格式为5位整数，数值越大版本越高，如：10000、20100</td>
                            </tr>
                            <tr>
                                <td>custom</td>
                                <td>是否使用定制数据</td>
                                <td>否</td>
                                <td>false</td>
                                <td>可选值：true: 使用定制数据;false: 使用官方数据</td>
                            </tr>
                            </tbody>
                        </table>
                        <h3>返回结果</h3>
                        <table style="margin-top: 5px;">
                            <tbody>
                            <tr>
                                <th colspan="3">名称</th>
                                <th>类型</th>
                                <th>含义</th>
                                <th>备注</th>
                            </tr>
                            <tr>
                                <td colspan="3">districts</td>
                                <td>array</td>
                                <td>行政区列表</td>
                                <td>无</td>
                            </tr>
                            <tr>
                                <td rowspan="6"></td>
                                <td colspan="2">district</td>
                                <td>object</td>
                                <td>行政区信息</td>
                                <td>无</td>
                            </tr>
                            <tr>
                                <td rowspan="5"></td>
                                <td colspan="1">admincode</td>
                                <td>string</td>
                                <td>行政区编码</td>
                                <td>无</td>
                            </tr>
                            <tr>
                                <td>name</td>
                                <td>string</td>
                                <td>行政区名称</td>
                                <td>无</td>
                            </tr>
                            <tr>
                                <td>fullname</td>
                                <td>string</td>
                                <td>行政区全名</td>
                                <td>无</td>
                            </tr>
                            <tr>
                                <td>abbreviation</td>
                                <td>string</td>
                                <td>行政区简称</td>
                                <td>无</td>
                            </tr>
                            <tr>
                                <td>level</td>
                                <td>string</td>
                                <td>行政区级别</td>
                                <td>无</td>
                            </tr>
                            </tbody>
                        </table>
                        <h3>服务示例</h3>
                        <table style="margin-top: 5px;">
                            <tbody>
                            <tr>
                                <th width="15%">参数</th>
                                <th width="25%">值</th>
                                <th width="50%">备注</th>
                                <th>必填</th>
                            </tr>
                            <tr>
                                <td>address</td>
                                <td><input width="60%" value="地图慧"></td>
                                <td>填写结构化地址信息:省份＋城市＋区县＋城镇＋乡村＋街道＋门牌号码</td>
                                <td>是</td>
                            </tr>
                            <tr>
                                <td>city</td>
                                <td><input width="60%" value="成都"></td>
                                <td>查询城市，可选：城市中文、中文全拼</td>
                                <td>否</td>
                            </tr>
                            </tbody>
                        </table>
                        <p><a href="##" id="demoRun">运行</a></p>
                        <div id="codeResult">
                            代码返回显示的结果
                        </div>
                        <h3>错误码说明</h3>
                        <table style="margin-top: 5px;">
                            <tbody>
                            <tr>
                                <th width="15%">status</th>
                                <th width="15%">info</th>
                                <th width="25%">描述</th>
                                <th>排查</th>
                            </tr>
                            <tr>
                                <td>API_21000</td>
                                <td>OK</td>
                                <td>返回正常</td>
                                <td>无</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <!-- 错误码定义 -->
                    <div id="errorCode" class="document-page fadeSlow">
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="resources/lib/jquery_1.11.3/jquery.min.js"></script>
<!--菜单插件JS-->
<script src="resources/lib/jquery-accordion-menu.js"></script>

<script src="resources/js/common.js"></script>
<script src="resources/js/data.js"></script>

<script type="text/javascript">
    $(document).ready(function(){
        //初始化菜单
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
                    <!--遍历一级菜单-->
                    $.each(menu,function(i,o){
                        h += '<li id="apiMenu'+o.groupId+'" class="level-1 apiMenu" data-link=" ">';
                        h += '<a href="javascript:void(0);" data-id="'+o.groupId+'">'+o.groupName+'</a>';
                        h += '<ul class="submenu">';
                        var menuChild = o.apiVos;
                        <!--遍历二级菜单-->
                        $.each(menuChild,function(i,oChild){
                            h += '<li class="level-2"><a href="javascript:void(0);" data-id=' +oChild.apiId+ '>'+ oChild.apiName +'</a></li>';
                        });
                        h+='</ul>';
                        h+='</li>';
                    });
                    $("#demo-list").prepend(h);
                    jQuery("#jquery-accordion-menu").jqueryAccordionMenu();
                    //展开api菜单
                    var apiMenu = $("#demo-list li.apiMenu");
                    for (var i = 0;i<apiMenu.length;i++){
                        var idValue = $(this).attr('id');
                        $("#"+idValue).click();
                    }
                   // 绑定二级菜单事件
                    $("#demo-list li.level-2").click(function(e){
                        e.stopPropagation();
                        if($(this).hasClass("active")){
                            $(this).removeClass("active");
                        }else{
                            $("#demo-list li.level-2.active").removeClass("active");
                            $("#demo-list li.level-1.open").removeClass("open");
                            $(this).addClass("active");
                            var id=$($(this).find("a")).attr("data-id");
                            httpRequest({
                                url: "api/findOne/"+id,
                                type:"get",
                                //返回成功进行的操作
                                success: function(e){
                                    if(e && e.isSuccess){
                                        var api=e.result.data;
                                        //显示位置
                                        api.groupSet.groupname = '/ '+api.groupSet.groupName;
                                        api.apiname = '/ '+api.apiName;
                                        $("#location_level1").html(api.groupSet.groupname);
                                        $("#location_level2").html(api.apiname);
                                        //显示api对应的文档
                                        $(".document-page").hide();
                                        $("#api").show();
                                        buildApiDoc(api);
                                        demoRun(id);
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
    function regEvent(){
        //绑定一级菜单事件
        $("#demo-list li.level-1").click(function(){
            if($(this).hasClass("open")){
                $(this).removeClass("open");
            }else{
                $("#demo-list li.level-1.open").removeClass("open");
                $("#demo-list li.level-2.active").removeClass("active");
                $(this).addClass("open");
                //显示位置
                var txt=' / '+$($(this).find("a")[0]).text();
                $("#location_level1").html(txt);
                $("#location_level2").html("");
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
    //构建api菜单
    function buildApiDoc(data){
        var doc='';
        doc+='<div class="title">'+ data.apiName+'</div>';
        doc+='<div class="update-time">最后更新时间'+data.updateDate+'</div>';
        doc+='<h3>描述</h3>'+
              '<p>'+data.description+'</p>';
        doc+='<h3>请求参数</h3>'+
                '<table style="...">'+
                '<tbody>'+
                '<tr>'+
                '<th width="15%">名称</th>'+
                '<th width="15%">含义</th>'+
                '<th width="10%">类型</th>'+
                '<th width="10%">必填</th>'+
                '<th>描述</th>'+
                '</tr>';
            //请求参数
            var requestParams=data.requestParameters;
            //console.dir(requestParams);
            $.each(requestParams,function(i,o){
                doc+='<tr>'+
                    '<td>'+o.apiParameterName+'</td>'+
                    '<td>'+o.meaning+'</td>'+
                    '<td>'+o.parameterType+'</td>'+
                    '<td>'+(o.required=="REQUIRED"?"是":"否")+'</td>'+
                    '<td>'+o.description+'</td>'+
                    '</tr>';
            });
            doc+='</tbody>';
            doc+='</table>';

        //返回结果
        doc+='<h3>返回结果</h3>'+
            '<table style="...">'+
            '<tbody>'+
            '<tr>'+
            '<th width="15%">名称</th>'+
            '<th width="15%">类型</th>'+
            '<th width="10%">含义</th>'+
            '<th width="10%">备注</th>'+
            '</tr>';
        //返回值
        var returnParams=data.returnResults;
        $.each(returnParams,function(i,o){
            doc+='<tr>'+
                '<td>'+o.name+'</td>'+
                '<td>'+o.type+'</td>'+
                '<td>'+o.meaning+'</td>'+
                '<td>'+o.description+'</td>'+
                '</tr>';
        });
        doc+='</tbody>';
        doc+='</table>';

        //服务示例
        doc+='<h3>服务示例</h3>';
        doc+='<table style="margin-top: 5px;">'+
             '<tbody>'+
                 '<tr>'+
                     '<th width="15%">参数</th>'+
                     '<th width="15%">值</th>'+
                     '<th width="55%">备注</th>'+
                     '<th>必填</th>'+
                 '</tr>';
        <!--关于Demo示例的传参-->
        $.each(requestParams,function(i,o){
            if(o.demoValue != '无'){
                doc+='<tr>'+
                          '<td>'+o.apiParameterName+'</td>'+
                          '<td><input width="60%" value="'+o.demoValue+'"></td>'+
                          '<td>'+o.description+'</td>'+
                          '<td>'+(o.required=="REQUIRED"?"是":"否")+'</td>'+
                      '</tr>';
            }
        });
        doc+='</tbody>';
        doc+='</table>';
        doc+='<p>'+
             '<a href="javascript:void(0);" class="demoRun" id="demoRun'+data.id+'">运行</a>'+
             '</p>'+
             '<div class="codeResult" id="codeResult'+data.id+'">代码返回显示的结果</div>';
        <!--错误码说明-->
        doc+='<h3>错误码说明</h3>';
        doc+='<table style="margin-top: 5px;">'+
             '<tbody>'+
             '<tr>'+
             '<th width="15%">status</th>'+
             '<th width="65%">描述</th>'+
             '<th>排查</th>'+
             '</tr>';
        var errorCodeParams = data.errorCodeSamples;
        $.each(errorCodeParams,function(i,o){
            doc+='<tr>'+
                 '<td>'+o.code+'</td>'+
                 '<td>'+o.description+'</td>'+
                 '<td>'+o.exclueStrategy+'</td>'+
                 '</tr>';
        });
        doc+='</tbody>';
        doc+='</table>';
        $("#api").html(doc);
        $("#resultSample").val(data.resultSample);
    }
    //构建错误码
    function buildErrorCode(data){
        //错误码
        var h='<div class="title">全局错误码定义</div>';
        h+='<div class="update-time">最后更新时间'+data.updateDate+'</div>';
        h+='<table>'+
                       '<tbody>'+
                        '<tr>'+
                          '<th>错误码</th>'+
                          '<th>错误信息</th>'+
                          '<th>描述</th>'+
                        '</tr>';
        var codes=data.errorCodeSamples;
        $.each(codes,function(i,o){
            h+='<tr>'+
                  '<td>'+o.code+'</td>'+
                  '<td>'+o.description+'</td>'+
                  '<td>'+o.exclueStrategy+'</td>'+
               '</tr>';
        });
        h+='</tbody>';
        h+='</table>';
        $("#errorCode").html(h).fadeIn(200);
    }
    //代码运行事件
    function demoRun(e){
        $("#demoRun"+e).click(function(){
            $("#codeResult"+e).slideToggle('slow');
        });
    }

</script>

</body>
</html>
