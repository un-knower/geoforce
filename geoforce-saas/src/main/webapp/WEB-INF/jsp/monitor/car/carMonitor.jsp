<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>车辆位置监控</title>
<meta name="description" content="车辆位置监控" />
<meta name="keywords" content="车辆位置监控" />
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
      <a id="navbarHeader" class="navbar-brand" href="${ctxp }/carMonitor/index" style="font-size: 14px;color: #2a6496;" title="车辆位置监控">刷新</a>
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
          		<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="color:#2a6496;">
          		<span class="glyphicon glyphicon-wrench">地图工具</span> 
          		<span class="caret"></span>
          		</a>
		        <ul class="dropdown-menu" role="menu" style="z-index: 999999999;">
		            <li><a href="javascript:void(0);" onclick="mapZoomInBox()" >拉框放大</a></li>
				    <li><a href="javascript:void(0);" onclick="mapZoomOutBox()">拉框缩小</a></li>
				    <li><a href="javascript:void(0);" onclick="mapRuler()">测距</a></li>
		       </ul>
        	</li>
     		<li>
     			<a href="javascript:void(0);" onclick="regionSearch('polygon')" style="color:#2a6496;"><span class="glyphicon glyphicon-retweet">区域查车</span></a>
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
		<table id="carGpsTable" class="col-xs-12"></table>
<%--	</div>--%>
</div>
</div>
</nav>
</div>
<!-- 部门车辆树 -->
<div id="carTreeDiv" style="display: none;">
	<div id="carTreePanelDiv" class="panel panel-primary" style="width: 100%;height: 450px;">
	  	<nav class="navbar navbar-default nav-primary" role="navigation">
	  		<div class="navbar-header">
		      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#carMonitor-collapse">
		        <span class="sr-only">Toggle navigation</span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		      </button>
		      <a class="navbar-brand" href="javascript:void(0);" onclick="layerCarTreeHidden()" style="font-size: 14px;color: #fff;">
		      <span class="glyphicon glyphicon-share-alt"></span>
		      </a>
		    </div>
    		<div class="collapse navbar-collapse" id="carMonitor-collapse" >
    			<ul class="nav navbar-nav">
		     		<li class="dropdown" >
		     			<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="color:white;">车辆位置 <span class="caret"></span></a>
		     			<ul class="dropdown-menu" role="menu" style="min-width: 108px;width: 108px;" >
				            <li><a href="javascript:void(0);" onclick="carLocate()" >实时定位</a></li>
				            <li class="divider"></li>
						    <li><a href="javascript:void(0);" onclick="carTrack()">锁定跟踪</a></li>
				       </ul>
		     		</li>	
		     		<li class="dropdown" >
		          		<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="color:white;">历史轨迹 <span class="caret"></span></a>
				        <ul class="dropdown-menu" role="menu" style="min-width: 108px;width: 108px;" >
				            <li><a href="javascript:void(0);" onclick="carHistory()" >今天</a></li>
				            <li class="divider"></li>
						    <li><a href="javascript:void(0);" onclick="historyShowTime()">时间段</a></li>
				       </ul>
		        	</li>
		     	</ul>
    		</div>
    	</nav>    	
	  	<div class="panel-body" style="width: 280px;">
	  		<div class="row-fluid">
	  			<div style="height: 50px;width: 100%;">
	  				<div class="input-group">
					     <input id="searchValue" type="text" style="width: 100%"  placeholder="车牌号\终端号\SIM卡号">
					     <span class="input-group-btn">
       						<button class="btn btn-primary btn-white" type="button" onclick="monitorCarSearch()">Search</button>
   						 </span>
	      			</div>
	  			</div>
	  			<div class="col-xs-12" id="carDeptTreeDiv" style="height: 200px;overflow: auto;">
		    		<ul id="carDeptTree" class="ztree" style="height:100%;"></ul>
		    	</div>
	  		</div>
	  	</div>
	</div>
</div>
<div id="carTreeHiddenDiv" style="z-index: 1000;position: fixed;right: 20px;top: 100px;">
	<button class="btn btn-default btn-xs" type="button" onclick="layerCarTree(this)">
		<span class="glyphicon glyphicon-chevron-down">选车</span>
	</button>
</div>
<%--<div id="carTreeHiddenDiv" style="display: none;cursor: pointer;">--%>
<%--	<img alt="" src="${ctxp }/resources/images/mapcloud/btn-left.png" onclick="layerCarTree(this)">--%>
<%--</div>--%>
<!-- 历史轨迹播放器 -->
<div id="historyControll" style="display: none;">
<div class="row">
	<div id="historyTitle" class="col-xs-12" style="height: 10px;margin-bottom: 5px;">
		<span class="label label-info col-xs-12" ></span>
	</div>
	<div class="col-xs-12" style="height: 25px;margin-bottom: 5px;">
		<div class="row">
			<div class="col-xs-4" >
				<span class="glyphicon glyphicon-backward" style="cursor: pointer;margin-left: 15px;"
					onclick="historyLast()" id="playLast" title="x1" disabled="disabled">快退</span>
				<span class="glyphicon glyphicon-play" style="cursor: pointer;margin-left: 5px;"
					onclick="historyStart()" id="playStart">播放</span>
				<span class="glyphicon glyphicon-forward" style="cursor: pointer;margin-left: 5px;"
				 	onclick="historyFast()" id="playFast" title="x1" >快进</span>
			</div>
			<div class="col-xs-8">
				<div id="historySilder" class="progress slider jm-slider-disabled ui-state-disabled" style="width: 300px;">
				  <div id="historyProgress" class="progress-bar progress-bar-warning"  role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" >
				    <span class="sr-only">0%</span>
				  </div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<!-- 历史轨迹时间面板 -->
<div class="modal fade bs-example-modal-sm" id="historyTime" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">历史轨迹时间段</h4>
      </div>
      <div id="bindRiverBody" class="modal-body">
      	 <input type="hidden" id="hTimeCarId"/>
      	 <p>
      	 <label class="no-padding-right" for="hTimeStart" style="padding-top: 4px;">开始时间： </label>  	 
      	 <input type="text" id="hTimeStart" name = "hTimeStart"  placeholder="开始时间"  
      	 	onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,maxDate:'%y-%M-%d'})"/>
		 </p>
		 <p>
		 <label class="  no-padding-right" for="hTimeEnd" style="padding-top: 4px;">结束时间： </label> 
		 <input type="text" id=hTimeEnd name="hTimeEnd"  placeholder="结束时间"  
			onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true,maxDate:'%y-%M-%d'})"/>
      	</p>
      	<div class="well well-sm" style="width: 250px;">
      		时间段只支持24小时内历史轨迹
      	</div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="historyTimeSave()">确定</button>
      </div>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
<link href="${base }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css" rel=stylesheet>
<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${base}/resources/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<script src="${base}/resources/layer/layer.min.js"></script>
<link   href="${base}/resources/bootstrapslider/css/slider.css" type="text/css" rel=stylesheet>
<script src="${base}/resources/bootstrapslider/jquery-ui-1.10.3.mouse_core.js"></script>
<script src="${base}/resources/bootstrapslider/bootstrapslider.js"></script>
<script src="${base}/resources/bootstrapslider/jquery-ui-touch-punch.js"></script>
<script src="${base}/resources/js/monitor/car/monitorUtil.js"></script>
<script src="${base}/resources/js/monitor/car/mapList.js"></script>
<script src="${base}/resources/js/monitor/car/monitor.js"></script>
<script src="${base}/resources/js/monitor/car/historyControl.js"></script>
<script src="${base}/resources/js/monitor/car/alarmRemind.js"></script>
</body>
</html>