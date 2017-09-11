<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
   	<title>工作计划管理</title>
	<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
		<link rel=stylesheet type=text/css href="${base}/resources/zTree/css/demo.css" type="text/css">
		<link rel=stylesheet type=text/css href="${base}/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
  </head>
  <body>
  	<!--查询框  -->
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
			     	<form id="carSeeping_list" class="navbar-form navbar-left" role="search">
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;" > 员工 ： </label>
			     	   <div class="form-group">
			     		<input type="text" id="name" name="name" placeholder="请输入名称" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="license" style="padding-top: 4px;" > 门店： </label>
			     	   <div class="form-group">
			     	   <input type="text" id="storeNanme" name="storeNanme" placeholder="请输入名称" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="no-padding-right" for="license" style="padding-top: 4px;" >时间： </label>
			     	   <div class="form-group">
			     		<input type="text" id="begindate" name="begindate"  placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						<input type="text" id="enddate" name="enddate"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 状态： </label>
					  <div class="form-group">
					    <select name="status" id="status">
					    	<option value="0" >待审批</option>
					    	<option value="" >全部</option>
							<option value="1" >审批通过</option>
							<option value="2" >驳回 </option>
						</select>
					  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>	
		<table id="grid-table" ></table>
		<div id="grid-pager" style="height: 40px" ></div>
<!--批量处理div -->
	<div class="modal fade" id="personPlanDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content" style="width: 412px;">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel">驳回计划</h4>
		      </div>
		     <div id="bindRiverBody" class="modal-body" style=" height: 150px";>
		      <div class="row" style="width: 530px">
		      			驳回原因：
		     		 <textarea class="form-control" name="alarmOpinionText" id="alarmOpinionText"
		     		 rows="3" style="width:400px; height:109px  " ></textarea>
			  	</div>
		      </div>
		      <div class="modal-footer">
		      	<button  type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-primary" onclick="saveReject()">提交</button>
		      </div>
		    </div>
		  </div>
		</div>

		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/js/sys/util.js"></script>
		<script src="${base}/resources/js/personPlan/personPlan.js"></script>
			<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>	
  </body>
</html>
