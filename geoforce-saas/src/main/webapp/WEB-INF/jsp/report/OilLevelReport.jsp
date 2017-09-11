<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <%@ include file="/WEB-INF/commons/taglibs.jsp"%>
    <link rel=stylesheet type=text/css href="${base}/resources/zTree/css/demo.css" type="text/css">
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<%--<script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>
		 <script type="text/javascript" src="http://cdn.hcharts.cn/highcharts/exporting.js"> </script >
		 --%>
		  <script src="${base}/resources/js/report/oilLevelReport/highcharts.js"></script>
		 <script src="${base}/resources/js/report/oilLevelReport/exporting.js"></script>
		<script src="${base}/resources/js/report/oilLevelReport/oilLevelReport.js"></script>
	  </head>
	  <body>
<!--查询框  -->
  <div class="row"> 
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
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;"> 车牌号： </label>
			     	   <div class="form-group">
			     		<input value="京n44444" type="text" id="license" class="form-control" name="license" placeholder="请输入车牌号" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 时间： </label>
					  <div class="form-group">
					    <input  value="2014-10-22 00:03:37" type="text" id="startTime" class="form-control" name = "startTime"  placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
					    <input value="2014-10-22 23:03:43" type="text" id="endTime" class="form-control" name="endTime"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
					  </div>
					  <label class="  no-padding-right" for="deptId"  style="padding-top: 4px;">类型 ： </label>
			     	  <div class="form-group">
			     		<div class="input-group">
			     			<select name="oilType" id="oilType">
									<option value="2" >油位-时间曲线</option>
									<option value="1" >油位-里程曲线 </option>
						</select>
			     		</div>
			     	  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="searchOilLevel();">查询
					  <i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>
		</div>	
		<ul class="nav nav-tabs" role="tablist" id="oileveTab">
		  <li class="active"><a href="#home" role="tab" data-toggle="tab">数据</a></li>
		  <li><a href="#profile" role="tab" data-toggle="tab">曲线图</a></li>
		</ul>
		<div class="tab-content">
		  <div class="tab-pane active" id="home"  >
		  		<table id="grid-table" ></table>
				<div id="grid-pager" ></div>
		  </div>
		  <div class="tab-pane" id="profile" style="width: 800px"></div>
		</div>
  </body>
</html>
