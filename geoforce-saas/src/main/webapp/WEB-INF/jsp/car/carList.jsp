<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>车辆管理</title>
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
		<script src="${ctxp}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${ctxp}/resources/assets/js/bootbox.min.js"></script>
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/demo.css" type="text/css">
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">

		<link href="${ctxp }/resources/autocomplete/jquery.autocomplete.css" rel="stylesheet" type="text/css" />		

		<script src="${ctxp}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		
		<script src="${ctxp}/resources/js/car/carDeptTree.js"></script>
		<script src="${ctxp}/resources/js/sys/util.js"></script>
		<script src="${ctxp}/resources/js/car/carList.js"></script>
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
<%--			      <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="toAddCar()">--%>
<%--			      	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--						<span class="ui-icon ace-icon fa fa-plus-circle purple"></span>添加--%>
<%--					</div>--%>
<%--				  </a>--%>
<%--				  <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="toEditCar()">--%>
<%--				  	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--						<span class="ui-icon ace-icon fa fa-pencil blue"></span>编辑--%>
<%--					</div>--%>
<%--				  </a>--%>
<%--				  <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="delCar()">--%>
<%--				  	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--					 <span class="ui-icon ace-icon fa fa-trash-o red"></span>删除--%>
<%--					</div>--%>
<%--				  </a>--%>
<%--				  <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="carBind('')">--%>
<%--				  	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--						<span class="ace-icon fa fa-check-square-o orange"></span>绑定司机--%>
<%--					</div>--%>
<%--				  </a>--%>
<%--				   <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="changDept('')">--%>
<%--				  	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--						<span class="ace-icon fa fa-check-square-o orange"></span>调整部门--%>
<%--					</div>--%>
<%--				  </a>											--%>
				 </div>
		   		 <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" >
		     	 	
			     	<form id=searchForm class="navbar-form navbar-left" role="search">
			     		<input type="hidden"  id="deptId" name="deptId" value="${dept.id}">
			     		<label class="  no-padding-right"  for="license" style="padding-top: 4px; font-size: 12px;" > 车牌号： </label>
			     	   <div class="form-group">
			     		<input type="text" id="license" class="form-control" name="license" placeholder="请输入车牌号"  style="font-size:12px; " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="deptId"  style="padding-top: 4px;font-size:12px;"> 部门： </label>
			     	  <div class="form-group">
			     		<div class="input-group">
			     		<input type="text" readonly="readonly" class="form-control" id="deptName" name="deptName" onclick="showMenu(); return false;" value="${dept.name }" style="cursor:pointer"   >
			     		<span onclick="showMenu(); return false;" class="glyphicon glyphicon-chevron-down input-group-addon" style="cursor:pointer"    ></span>
			     		</div>
			     	  </div>
			     	 
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>
			<table id="grid-table"></table>
			<div id="grid-pager" style=" height: 40px;"></div>
		</div>
		<div id="menuContent" class="menuContent" style="position: absolute; left: 59.4px; top: 132px; display:none ; z-index: 9999999;">
			 <ul id="deptTree" class="ztree " style="margin-top: -50px;margin-left: -113px; width: 160px; -moz-user-select: none;  "></ul>
		</div>
		<!-- 添加车辆 -->
		<div id="addcar" style="display: none;">
			<div class=" ">
			<div class="alert alert-danger" style="display: none;" role="alert" id="warning">
				<strong id="content"></strong> 
			</div>

				<form role="form" id ="addCarForm" class="form-horizontal">
				 <input type="hidden" name="carId" id="carId">
				 <input type="hidden" name="deptId" id="boxdepId" value="${dept.id}">
				  <div class="form-group"  >
				    <label for="license" class="col-sm-2 control-label">车牌号码<span style="color: red">*</span></label>
				      <div class="col-sm-4">
					         <input type="text" id="license" name="license" maxlength="7" class="form-control" id="firstname" 
					            placeholder="">
				      </div>
				      
				      <label for="firstname" class="col-sm-2 control-label">所在部门<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				      		<div class="input-group">
					         	<input type="text" id="boxdeptName" readonly="readonly" class="form-control" id="firstname" value="${dept.name }"  onclick="showboxMenu(); return false;" style="cursor:pointer"   >
					          	<span onclick="showboxMenu(); return false;" class="glyphicon glyphicon-chevron-down input-group-addon" style="cursor:pointer" ></span>
				          	</div>
				      </div>
				  </div>
				  
				  <div class="form-group" >
				    <label for="terminalCode"  class="col-sm-2 control-label">终端号码<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				         <input type="text" id="terminalCode" name="terminalCode" maxlength="11" class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				      <label for="terminalType" class="col-sm-2 control-label">终端类型<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				         <select class="form-control" id="terminalType" name="terminalType">
					         <c:forEach items="${termList}" var="term">
					         	<option id="${term.id }" value="${term.id }">${term.name }</option>
					         </c:forEach>
				         </select>
				      </div>
				  </div>
				 <div class="form-group" >
				    <label for="mobile" class="col-sm-2 control-label">SIM卡号<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				         <input type="text"  id="mobile" name="mobile" maxlength="11"  class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				      
				      <label for=""carType"" class="col-sm-2 control-label">车辆类型<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				        <select id="carType" name="carType" class="form-control" >
				        	<c:forEach items="${dataWorkList}" var="data">
				        		<option id="${data.code}" value="${data.code }">${data.name }</option>
				        	</c:forEach>
				        </select>
				      </div>
				      
				      
				  </div>
				  <div class="form-group" >
				    <label for="brand" class="col-sm-2 control-label">品牌<span style="color: red"></span></label>
				      <div class="col-sm-4">
				         <input type="text" id="brand" name="brand" maxlength="11" class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				      <label for="petrol" class="col-sm-2 control-label">排气量<span style="color: red"></span></label>
				      <div class="col-sm-4">
				         <input type="text" id="petrol" name="petrol" maxlength="11"  class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				  </div>
				  <div class="form-group"  >
				    
				    <label for="color" class="col-sm-2 control-label">颜色<span style="color: red"></span></label>
				      <div class="col-sm-4">
				         <input type="text" id="color" name="color" maxlength="6"  class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				    
				      <label for="other" class="col-sm-2 control-label">其他<span style="color: red"></span></label>
				      <div class="col-sm-4">
				         <input type="text" id="other" name="other" class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				  </div>
				</form>

			</div>
		</div>

<!-- Modal -->
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel">绑定司机</h4>
		        
		      </div>
		      <div id="bindRiverBody" class="modal-body">
		      <input type="hidden" id="carDriverId">
		      <input type="text" id="driverName" name="driverName" placeholder="请输司机姓名" class=" " maxlength="10"/>
		      <button id="seachbtn" type="button" class="btn btn-purple btn-sm" onclick="reloadbind();">
					搜索
					<i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
				</button>
				<strong id="carWarning" style="color: red"></strong>
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button id="bindButton" type="button" class="btn btn-primary" onclick="bindDirver()">绑定</button>
		      </div>
		    </div>
		  </div>
		</div>
		
		<div class="modal fade" id="myModa2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
		  <div class="modal-dialog"  style="width: 385px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel2">调整部门</h4>
		      </div>
		      <div id="changDeptBody" class="modal-body">
		      <input type="hidden" id="changCarId">
		      <input type="hidden" id="changDeptId">
		      
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button id="bindButton" type="button" class="btn btn-primary" onclick="doChangDept()"> 确定</button>
		      </div>
		    </div>
		  </div>
		</div>
	</body>
</html>
