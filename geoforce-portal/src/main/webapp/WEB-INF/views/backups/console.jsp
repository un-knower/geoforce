<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>控制台</title>
    <link rel="stylesheet" type="text/css" href="resources/lib/bootstrap_3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/consolePerson.css">
    <link rel="stylesheet" type="text/css" href="resources/css/kzt.css">
</head>
<body>
<main>
    <!-- 遮罩层.mask -->
    <div class="mask"></div>
    </div>
    <!-- 创建弹出框 -->
    <div id="alertAdd-Key" class="alertAdd-Key" style="display: none">
                    <aside class="alertHead">
                        <span class="warn"><img src="resources/img/icon_add.png" alt="">创建应用</span>
                        <span id="close" class="close">×</span>
                    </aside>
                    <div class="box-main">
                        <div class="box-row">
                            <div class="left">
                                <label>应用名称</label>
                            </div>
                            <div class="right">
                                <input type="text" id="create_app_name" placeholder="支持汉字、字母、数字、下划线，不超过15个" onblur="checkAppName(this);">
                            </div>
                        </div>
                        <div class="box-row"> 
                            <div class="left">
                                <label><span class="txt-red">*</span>应用类型</label>
                            </div>
                            <div class="right">
                                <select id="create_app_type">
                                    <option value="1" selected="selected">服务端</option>
                                </select>
                            </div>
                        </div>
                        <div class="box-row">
                            <div class="left">
                                <label><span class="txt-red">*</span>Apis</label>
                            </div>
                            <div class="right" id="create_app_apiIds">
                                <!-- <ul>
                                    <li class="api-li">
                                        <div class="api-title">
                                            <label class="check-api"><input type="checkbox" data-id="ff8080815b3d2e1c015b42dfa8170000">路线规划</label>
                                        </div>
                                        <div class="api-content">
                                            <label class="check-api">接口1</label>
                                            <label class="check-api">接口2</label>
                                            <label class="check-api">接口3</label>
                                        </div>
                                    </li>
                                    <li class="api-li">
                                        <div class="api-title">
                                            <label class="check-api"><input type="checkbox" data-id="ff8080815b3d2e1c015b42dfa8170000">路线规划</label>
                                        </div>
                                        <div class="api-content">
                                            <label class="check-api">接口1</label>
                                            <label class="check-api">接口2</label>
                                            <label class="check-api">接口3</label>
                                        </div>
                                    </li>
                                </ul> -->
                            </div>
                        </div>
                        <div class="box-row">
                            <div class="left">
                                <label><span class="txt-red">*</span>IP白名单</label>
                            </div>
                            <div class="right">
                                <input type="text" id="create_app_ip" placeholder="输入白名单ip，回车之后可继续添加" onblur="checkIp(this);">
                                <textarea class="ips" id="create_app_ips" readonly="readonly" rows="5" placeholder="所有ip白名单(双击可全部清除)"></textarea> 
                                <p class="ipNote">
                                    <span class="txt-red">只有IP白名单内的服务器才能成功发起调用
                                    </span>
                                    填写IP地址或IP前缀网段，如果不想对IP做任何限制，请设置为0.0.0.0/0 
                                    <span class="txt-red">谨慎使用，(AK如果泄露配额会被其用户 消费，上线前可以用作Debug，线上正式ak请设置合理的IP白名单）</span>
                                </p>
                            </div>
                        </div>
                        <div class="box-row btn">
                            <button class="cancel">取消</button>
                            <button class="create" id="create_app_save">保存</button>
                        </div>
                    </div>
    </div>
    <!-- 修改弹出框 -->
    <div id="alertEdit-Key" class="alertAdd-Key" style="display: none">
        <aside class="alertHead">
            <span class="warn"><img src="resources/img/icon_add.png" alt="">修改应用</span>
            <span id="close" class="close">×</span>
        </aside>
        <div class="box-main">
            <div class="box-row">
                <div class="left">
                    <label>应用名称</label>
                </div>
                <div class="right">
                    <input type="text" disabled id="edit_app_name" >
                </div>
            </div>
            <div class="box-row"> 
                <div class="left">
                    <label>应用类型</label>
                </div>
                <div class="right">
                    <input type="text" disabled id="edit_app_type" ></span>
                </div>
            </div>
            <div class="box-row">
                <div class="left">
                    <label><span class="txt-red">*</span>Apis</label>
                </div>
                <div class="right" id="edit_app_apiIds">
                    
                </div>
            </div>
            <div class="box-row">
                <div class="left">
                    <label><span class="txt-red">*</span>IP白名单</label>
                </div>
                <div class="right">
                    <input type="text" id="edit_app_ip" placeholder="输入白名单ip，回车之后可继续添加;(双击下面文本框可全部清除)" onblur="checkIp(this);">
                    <textarea class="ips" id="edit_app_ips" readonly="readonly" rows="5" placeholder="ip白名单"></textarea>
                    <p class="ipNote">
                        <span class="txt-red">只有IP白名单内的服务器才能成功发起调用
                        </span>
                        填写IP地址或IP前缀网段，如果不想对IP做任何限制，请设置为0.0.0.0/0 
                        <span class="txt-red">谨慎使用，(AK如果泄露配额会被其用户 消费，上线前可以用作Debug，线上正式ak请设置合理的IP白名单）</span>
                    </p>
                </div>
            </div>
            <div class="box-row btn">
                <button class="cancel">取消</button>
                <button class="create" id="edit_app_save">保存</button>
            </div>
        </div>
    </div>
    <div class="main-page">
        <!-- 左边菜单栏 -->
        <nav class="cd-side-navigation">
            <ul>
                <li>
                    <a href="#0" class="selected" data-menu="console">
                        <img src="resources/img/img_kzt_wdyy.png" alt="">我的应用
                    </a>
                </li>
                <li>
                    <a href="#0" data-menu="permission">
                        <img src="resources/img/img_kzt_qxtj.png" alt="">权限统计
                    </a>
                </li>
                <li>
                    <a href="#0" data-menu="consolePerson">
                        <img src="resources/img/img_kzt_grzl.png" alt="">个人资料
                    </a>
                </li>
            </ul>
        </nav>
        <!-- 右边内容区 -->
        <div class="cd-main">
            <section class="cd-section index visible">
                <div class="con-right">
                    <div class="console-headPic">
                        <figure>
                            <img class="tip" src="resources/img/info_kzt_normal.png" alt="">
                            <figcaption>
                                <span id="myAppInfo" style="color:#929599">您还没有任何应用，先去新建一个应用吧！</span><br>
                                <span class="key">您可以在这里创建，设置并管理您的应用及Key！</span>
                            </figcaption>
                        </figure>
                        <button id="add_application"><img src="resources/img/icon_kzt_add.png" alt="">创建新应用</button>
                    </div>
                    <div>
                        <div class="col-sm-4 pull-right">
                            <div class="input-group">
                                <input type="text" placeholder="请输入AK查询" class="input-sm form-control" id="searchKeyword"> <span class="input-group-btn">
                                        <button type="button" class="btn btn-sm btn-primary" onclick="searchRecord();"> 搜索</button> </span>
                            </div>
                        </div>
                    </div>
                    <table cellspacing="0" id="my_apps">
                        <caption>
                            <span><img src="resources/img/icon_kzt_list.png" alt="">列表</span>
                            <!--<span class="date">2016-12-06创建</span>
                            <i>删除</i><i>编辑</i>-->
                        </caption>
                        <thead>
                        <tr>
                            <th>应用编号</th>
                            <th>应用名称</th>
                            <th>访问应用（AK）</th>
                            <th>应用类别</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        
                        </tbody>
                    </table>
                    <!-- 页码信息-->
                    <div class="page row">
                        <div class="col-sm-6">
                            <div class="dataTables_info" id="DataTables_Table_0_info" role="alert" aria-live="polite" aria-relevant="all">共<span id="totalCount"></span>条，<span id="pageCount"></span>页
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="dataTables_paginate paging_simple_numbers" id="DataTables_Table_0_paginate">
                                <ul id="pagination" class="pagination pagination-sm" style="margin: 0;float:right;"></ul>
                            </div>
                        </div>
                    </div>
                </div>
            </section> <!-- .cd-section -->
        </div>
        <div id="cd-loading-bar" data-scale="1" class="console"></div>
    </div>
</main>
<script type="text/javascript" src="resources/lib/jquery_1.11.3/jquery.min.js"></script>
<script src="resources/js/common.js"></script>
<script src="resources/lib/console/velocity.min.js"></script>
<script src="resources/lib/bootstrap_3.3.5/js/bootstrap.min.js"></script>
<script src="resources/lib/bootstrap_3.3.5/js/bootbox.min.js"></script>
<script src="resources/lib/bootstrap_3.3.5/js/bootstrap-paginator.min.js"></script>
<script src="resources/lib/console/main.js"></script>
<script src="resources/js/console.js"></script>
</body>
</html>