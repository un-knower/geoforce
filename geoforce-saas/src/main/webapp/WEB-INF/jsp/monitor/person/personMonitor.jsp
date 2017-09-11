<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>巡店人员位置</title>
<meta name="description" content="巡店人员位置" />
<meta name="keywords" content="巡店人员位置" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<body>
<div class="page-content-area">
<nav id="monitorBtns" class="navbar navbar-default navbar-white" role="navigation" style="background-color: #e7e7e7;">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a id="navbarHeader" class="navbar-brand" href="${ctxp }/personMonitor/index" style="font-size: 14px;color: #2a6496;" title="巡店人员位置管理">刷新</a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" >
     	<ul class="nav navbar-nav">
     		<li class="dropdown">
          		<a id="mapType" href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="color:#2a6496;">云地图 <span class="caret"></span></a>
		        <ul class="dropdown-menu" role="menu">
		            <li><a href="javascript:void(0);" onclick="changeMap('supermap')" >云地图</a></li>
				    <li><a href="javascript:void(0);" onclick="changeMap('baidu')">百度地图</a></li>
<%--				    <li><a href="javascript:void(0);" onclick="changeMap('google')">谷歌地图</a></li>--%>
		       </ul>
        	</li>
     	</ul>
     	<form class="navbar-form navbar-left" role="search">
			<div id="divSMapSel" ><a href="javascript:void(0);" onclick="initSMapSelWidget()" style="text-decoration:none;"></a></div>
        </form>
     	<ul class="nav navbar-nav navbar-right">
     		<li class="dropdown">
          		<a id="storePoi" href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="color:#2a6496;">显示门店 <span class="caret"></span></a>
		        <ul class="dropdown-menu" role="menu">
		            <li><a href="javascript:void(0);" onclick="poiDisChange('store_show')" >显示门店</a></li>
				    <li><a href="javascript:void(0);" onclick="poiDisChange('store_hide')">隐藏门店</a></li>
		       </ul>
        	</li>
        	<li>
        		<a href="javascript:void(0);" onclick="mapClear()" style="color:#2a6496;"><span class="glyphicon glyphicon-trash">地图清屏</span></a>
        	</li>
     	</ul>
    </div>
  </div>
</nav>
<div class="row">
<div class="container-fluid">
<%--	<div class="col-xs-12">--%>
		<div id="mapDiv_supermap" class="col-xs-12" style="height: 600px;"></div>
		<div id="mapDiv_baidu" class="col-xs-12" style="height: 100%;border: 1px #3473B7 solid;"></div>
		<div id="mapDiv_google" class="col-xs-12" style="height:100%;border: 1px #3473B7 solid;"></div>
<%--	</div>--%>
</div>
</div>

<nav id="mapTable" class="navbar navbar-default nav-white" role="navigation" style="background-color:white;">
<div class="row">
<div class="container-fluid">
<%--	<div id="mapListDiv"  >--%>
		<table id="personGpsTable" class="col-xs-12"></table>
<%--	</div>--%>
</div>
</div>
</nav>
</div>
<!-- 部门人员树 -->
<div id="personTreeDiv" style="display: none;">
	<div id="personTreePanelDiv" class="panel panel-primary" style="width: 100%;height: 450px;">
	  	<nav class="navbar navbar-default nav-primary" role="navigation">
	  		<div class="navbar-header">
		      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#personMonitor-collapse">
		        <span class="sr-only">Toggle navigation</span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		      </button>
		      <a class="navbar-brand" href="javascript:void(0);" onclick="layerPersonTreeHidden()" style="font-size: 14px;color: #fff;">
		      <span class="glyphicon glyphicon-share-alt"></span>
		      </a>
		    </div>
    		<div class="collapse navbar-collapse" id="personMonitor-collapse" >
    			<ul class="nav navbar-nav">
    				<li>
    					<a href="javascript:void(0);" onclick="personLocate()" style="color:white;width: 100px;text-align: center;">开始定位</a>
    				</li>
    				<li>
    					<a href="javascript:void(0);" onclick="sendMsgOpen()" style="color:white;width: 100px;text-align: center;">发送通知</a>
    				</li>
		     	</ul>
    		</div>
    	</nav>    	
	  	<div class="panel-body" style="width: 280px;">
	  		<div class="row-fluid">
	  			<div style="height: 50px;width: 100%;">
	  				<div class="input-group">
					     <input id="searchValue" type="text" style="width: 100%"  placeholder="姓名\终端号\手机号">
					     <span class="input-group-btn">
       						<button class="btn btn-primary btn-white" type="button" onclick="monitorPersonSearch()">Search</button>
   						 </span>
	      			</div>
	  			</div>
	  			<div class="col-xs-12" id="personDeptTreeDiv" style="height: 200px;overflow: auto;">
		    		<ul id="personDeptTree" class="ztree" style="height:100%;"></ul>
		    	</div>
	  		</div>
	  	</div>
	</div>
</div>
<div id="personTreeHiddenDiv" style="z-index: 1000;position: fixed;right: 20px;top: 100px;">
	<button class="btn btn-default btn-xs" type="button" onclick="layerPersonTree(this)">
		<span class="glyphicon glyphicon-chevron-down">展开</span>
	</button>
</div>
<!-- 发送消息弹出框 -->
<div id="personSendMsgDiv" class="col-xs-12" style="width: 400px;height: 300px;display: none;">
<form role="form" style="margin-top: 10px;">
  <div class="form-group">
    <label for="msgTitle" class="control-label">
    	通知标题
    	<span id="msgTitle_error" style="font-size: 13px;color: red;margin-left: 30px;">&nbsp;</span>
    </label>
    <input type="text" class="form-control" id="msgTitle" placeholder="请输入标题">
  </div>
  <div class="form-group">
    <label for="msgContent" class="control-label">
    	内容
    	<span id="msgContent_error" style="font-size: 13px;color: red;margin-left: 30px;">&nbsp;</span>
    </label>
    <textarea id="msgContent" rows="5" class="form-control" placeholder="请输入内容"></textarea>
  </div>
  <input type="hidden" id="sendPersonIds"/>
  <div class="btn-group" style="margin-left: 160px;">
  <button onclick="sendMsgClose()" type="button" class="btn btn-default" style="width: 100px;">取消</button>
  <button onclick="sendMsg()" type="button" class="btn btn-primary" style="width: 100px;">发送</button>
  </div>
</form>
</div>
<!-- 图片弹出slider -->
<div id="imgbody" style="display: none;">
</div>
<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
<link href="${base }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css" rel=stylesheet>
<script src="${base}/resources/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<script src="${base}/resources/layer/layer.min.js"></script>
<script src="${base}/resources/layer/extend/layer.ext.js"></script>
<script src="${base}/resources/js/monitor/person/personList.js"></script>
<script src="${base}/resources/js/monitor/person/main.js"></script>
</body>
</html>