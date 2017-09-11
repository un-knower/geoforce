<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>车辆调度信息</title>
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/assets/js/jquery.validate.min.js"></script>
		<script src="${base}/resources/js/sys/util.js"></script>
		<script src="${base}/resources/js/report/carDisMsgReport.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>

<%--		<div class="row"  style="margin-bottom: 8px;text-align: left;"> --%>
<%--			<div class="col-xs-12  ">--%>
<%--			    <form id=searchForm>--%>
<%--				<label class="  no-padding-right" for="license" style="padding-top: 4px;" > 车牌号： </label>--%>
<%--				<input type="text" id="license" name="license" placeholder="请输入车牌号" class=" " maxlength="10"/>--%>
<%--				--%>
<%--				<label class="  no-padding-right" for="deptId" style="padding-top: 4px;"> 消息类型： </label>--%>
<%--				<select id="type" name="type">--%>
<%--				    <option value="" selected="selected">全部</option>--%>
<%--					<option value="1">通知</option>--%>
<%--					<option value="2">任务</option>--%>
<%--				</select>--%>
<%--				--%>
<%--				<label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 创建时间： </label>--%>
<%--				<input type="text" id="starttime" name = "starttime"  placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />--%>
<%--				<input type="text" id="endtime" name="endtime"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>--%>
<%--				--%>
<%--				<button type="button" class="btn btn-purple btn-sm" onclick="reload();">--%>
<%--					搜索--%>
<%--					<i class="ace-icon fa fa-search icon-on-right bigger-110"></i>--%>
<%--				</button>--%>
<%--				</form>--%>
<%--			</div>--%>
<%--		</div>--%>
<%--		--%>
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
				     	<form id=searchForm class="navbar-form navbar-left" role="search">
				     		<input type="hidden"  id="deptId" name="deptId">
				     		<label class="  no-padding-right" for="license" style="padding-top: 4px;font-size:12px;" > 车牌号： </label>
				     	   <div class="form-group">
				     		<input type="text" id="license" class="form-control" name="license" placeholder="请输入车牌号" style="font-size:12px; " maxlength="10"/>
				     	  </div>
				     	  <label class="  no-padding-right" for="deptId"  style="padding-top: 4px;font-size:12px;"> 消息类型： </label>
				     	  <div class="form-group">
				     		<select id="type" name="type">
							    <option style="font-size:12px; "  value="" selected="selected">全部</option>
								<option style="font-size:12px; " value="1">通知</option>
								<option style="font-size:12px; " value="2">任务</option>
							</select>
				     	  </div>
				     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;font-size:12px;"> 发送时间： </label>
						  <div class="form-group">
						    <input type="text" id="starttime" class="form-control" name = "starttime" style="font-size:12px; " placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						    <input type="text" id="endtime" class="form-control" name="endtime" style="font-size:12px; "  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
						  </div>
						  <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
						</form>
			    	</div>
				 </div>
			</nav>
			<table id="grid-table"></table>
			<div id="grid-pager" style=" height: 40px;"></div>
		</div>
	</body>
</html>
