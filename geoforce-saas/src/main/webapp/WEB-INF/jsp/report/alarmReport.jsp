<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

		<title>车辆报警统计</title>
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
		<script src="${ctxp}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${ctxp}/resources/assets/js/bootbox.min.js"></script>
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/demo.css" type="text/css">
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">

		<script src="${ctxp}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		
		<script src="${ctxp}/resources/js/car/carDeptTree.js"></script>
		<script src="${ctxp}/resources/js/sys/util.js"></script>
		<script src="${ctxp}/resources/js/report/alarmReport.js"></script>
		<script type="text/javascript">
		</script>
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
<%--			     		<input type="hidden"  id="deptId" name="deptId">--%>
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;font-size:12px;" > 车牌号</label>
			     	   <div class="form-group">
			     		<input type="text" id="license" class="form-control" name="license" style="font-size:12px; " placeholder="请输入车牌号"  maxlength="10"/>
			     	  </div>
<%--			     	  <label class="  no-padding-right" for="deptId"  style="padding-top: 4px;"> 部门</label>--%>
<%--			     	  <div class="form-group">--%>
<%--			     		<div class="input-group">--%>
<%--			     		<input type="text"  class="form-control" style="background-color: white;" id="deptName" name="deptName" onclick="showMenu(); return false;" style="cursor:pointer">--%>
<%--			     		<span onclick="showMenu(); return false;" class="glyphicon glyphicon-chevron-down input-group-addon" style="cursor:pointer" ></span>--%>
<%--			     		</div>--%>
<%--			     	  </div>--%>
			     	   <label class="  no-padding-right" for="typeId"  style="padding-top: 4px;font-size:12px;">类型</label>
					  <div class="form-group">
			     		<select id="typeId" name="typeId">
						    <option value="" selected="selected">全部</option>
							<c:forEach var="types" items="${lists}">
								<c:if test="${types.code>=300}">
					           		 <option style="font-size:12px; " value="${types.id}">${types.name}</option>
					            </c:if>
					        </c:forEach>	
						</select>
			     	  </div>
			     	  <label class="  no-padding-right" for="status"  style="padding-top: 4px;font-size:12px;">状态</label>
			     	  <div class="form-group">
			     		<select id="status" name="status">
						    <option style="font-size:12px; " value="" selected="selected" >全部</option>
							<option style="font-size:12px; " value="0">未处理</option>
							<option style="font-size:12px; " value="1">已处理</option>
						</select>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;font-size:12px;"> 查询时间</label>
					  <div class="form-group">
					    <input type="text" id="starttime" class="form-control" name = "startTime" value="${statTime}" style="font-size:12px; " onClick="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'endtime\',{m:-1});}'})" />
					    <input type="text" id="endtime" class="form-control" name="endTime" value="${endTime}" style="font-size:12px; "  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'starttime\',{m:1});}'})"/>
					  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-left bigger-110"></i></button><br>
					</form>
					 <ul class="nav navbar-nav navbar-right">
					       <li> <a title="导出数据" href="#" onclick="exportData()" style="color:#2a6496;"><i class="glyphicon glyphicon-export bigger-110"></i>导出</a></li>
					 </ul>
		    	</div>
			 </div>
			</nav>
			<table id="grid-table"></table>
			<div id="grid-pager" style=" height: 40px;"></div>
		</div>
		<div id="mapDiv" style="display: none;">
			<div id="mapDiv_supermap" style="width: auto; height: 100%;"></div>
			<div id="mapDiv_baidu" style="width: auto; height: 100%;border: 1px #3473B7 solid;"></div>
			<div id="mapDiv_google" style="width:auto; height:100%;border: 1px #3473B7 solid;"></div>
		</div>
		<div id="menuContent" class="menuContent" style="position: absolute; left: 59.4px; top: 132px; display:none ; z-index: 9999999;">
			 <ul id="deptTree" class="ztree" style="margin-top: -50px;margin-left: -113px; width: 160px; -moz-user-select: none;  "></ul>
		</div>
		<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
	</body>
</html>
