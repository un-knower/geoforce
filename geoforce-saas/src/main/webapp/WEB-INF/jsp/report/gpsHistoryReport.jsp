<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>历史轨迹统计</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta content="IE=edge" http-equiv="X-UA-Compatible">
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>

		<link rel="stylesheet" type="text/css" href="">
		<script src="${ctxp}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${ctxp}/resources/assets/js/bootbox.min.js"></script>
		<script src="${ctxp}/resources/assets/js/jquery.validate.min.js"></script>
		<script src="${ctxp}/resources/js/sys/util.js"></script>
		<script src="${ctxp}/resources/js/report/gpsHistory.js"></script>
		<style type="text/css">
		body { 
			overflow-x:hidden ; /*隐藏水平滚动条*/ 
<%--			overflow-y:hidden ; /*隐藏水平滚动条*/ --%>
} 
		</style>
	</head>
	<body>
		<div class="page-content-area">
			<nav id="monitorBtns" class="navbar navbar-default nav-white" role="navigation" style="background-color: #e7e7e7;">
				  <div class="container-fluid">
				    <div class="navbar-header">
				      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				        <span class="sr-only">Toggle navigation</span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				        <span class="icon-bar"></span>
				      </button>
					 </div>
			   		 <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" >
			     	
				     	<form id=searchForm method="post" class="navbar-form navbar-left" role="search">
				     		<label class="  no-padding-right" for="license" style="padding-top: 4px;font-size:12px;" > 车牌号： </label>
				     	   <div class="form-group">
				     		<input type="text" id="license" class="form-control" name="license" placeholder="请输入车牌号" style="font-size:12px; " maxlength="10"/>
				     	  </div>
				     	  
				     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;font-size:12px;"> 查询时间： </label>
						  <div class="form-group">
						    <input type="text" id="starttime" style="font-size:12px; " class="form-control" name = "startTime" value="${statTime}"  placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
						    <input type="text" id="endtime" style="font-size:12px; " class="form-control" name="endTime" value="${endTime}"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})"/>
						  </div>
						 <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
						</form>
						
						 <ul class="nav navbar-nav navbar-right">
					       <li> <a title="导出数据" href="#" onclick="exportData()" style="color:#2a6496;"><i class="glyphicon glyphicon-export bigger-110"></i>导出</a></li>
					     </ul>
			    	</div>
				  </div>
				</nav>
			<table id="grid-table"></table>
			<div id="grid-pager" style=" height: 40px;"></div>
		
	  		<div id="mapDiv" >
				<div id="mapDiv_supermap" style="width: auto; height: 100%;"></div>
				<div id="mapDiv_baidu" style="width: auto; height: 100%;border: 1px #3473B7 solid;"></div>
				<div id="mapDiv_google" style="width:auto; height:100%;border: 1px #3473B7 solid;"></div>
			</div>
		</div>
		<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
	</body>
</html>
