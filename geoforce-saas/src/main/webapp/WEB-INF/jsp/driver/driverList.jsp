<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
  		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
  		<script src="${ctxp}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${ctxp}/resources/assets/js/bootbox.min.js"></script>
		<script src="${ctxp}/resources/js/sys/util.js"></script>
		<script src="${ctxp}/resources/js/driver/driverList.js"></script>
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/demo.css" type="text/css">
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">

		<script src="${ctxp}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		
		<script src="${ctxp}/resources/js/car/carDeptTree.js"></script>
		<script src="${ctxp}/resources/assets/js/jquery.validate.min.js"></script>
		<style type="text/css">
		body {
			  margin: 0;
			  font-family: Verdana,"Helvetica Neue", Helvetica, Arial, sans-serif;
			  font-size: 13px;
			  line-height: 18px;
			  color: #333333;
			  background-color: #ffffff;
		}
		</style>
    <title>司机管理</title>
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
<%--				      <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="toAddDriver()">--%>
<%--			      	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--						<span class="ui-icon ace-icon fa fa-plus-circle purple"></span>添加--%>
<%--					</div>--%>
<%--				  </a>--%>
<%--				  <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="toEditDriver()">--%>
<%--				  	<div class="ui-pg-div" style="padding-top: 4px;">--%>
<%--						<span class="ui-icon ace-icon fa fa-pencil blue"></span>编辑--%>
<%--					</div>--%>
<%--				  </a>--%>
<%--				  <a class="navbar-brand" style="font-size: 14px;color: #2a6496;" href="#" onclick="delDriver()">--%>
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
<%--				  </a>			--%>
					 </div>
			   		 <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" >
			     	
				     	<form id=searchForm class="navbar-form navbar-left" role="search">
				     		<input type="hidden"  id="deptId" name="deptId" value="${dept.id}">
				     		<label class="  no-padding-right" for="license" style="padding-top: 4px;font-size:12px;" > 姓名： </label>
				     	   <div class="form-group">
				     		<input type="text" id="name" class="form-control" name="name" placeholder="请输司机姓名" style="font-size:12px; " maxlength="10" style="cursor:pointer"/>
				     	  </div>
				     	  <label class="  no-padding-right" for="deptId"  style="padding-top: 4px;font-size:12px;" style="cursor:pointer"> 部门： </label>
				     	  <div class="form-group">
				     		<div class="input-group">
				     		<input type="text" readonly="readonly" class="form-control" id="deptName" name="deptName" onclick="showMenu(); return false;" style="cursor:pointer" value="${dept.name }">
				     		<span onclick="showMenu(); return false;" class="glyphicon glyphicon-chevron-down input-group-addon" style="cursor:pointer" ></span>
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
			 <ul id="deptTree" class="ztree" style="margin-top: -50px;margin-left: -113px; width: 160px; -moz-user-select: none;  "></ul>
		</div>
		<div id="addDriver" style="display: none;">
		   	<div class="alert alert-danger" style="display: none;" role="alert" id="warning">
				<strong id="content"></strong> 
		  	</div>
			<form role="form" id ="addDriverForm" class="form-horizontal">
				 <input type="hidden" name="driverId" id="driverId"/>
				 <input type="hidden" name="deptId" id="boxdepId" value="${dept.id}">
				  <div class="form-group"  >
				    <label for="name" class="col-sm-2 control-label">姓名<span style="color: red">*</span></label>
				      <div class="col-sm-4">
					         <input type="text" id="name" name="name" maxlength="7" class="form-control" id="firstname" 
					            placeholder="">
				      </div>
				      
				      <label for="boxdeptName" class="col-sm-2 control-label">所在部门<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				      		<div class="input-group">
					         	<input type="text" id="boxdeptName" readonly="readonly" class="form-control" id="firstname" value="${dept.name }"  onclick="showboxMenu(); return false;" style="cursor:pointer"/>
					          	<span onclick="showboxMenu(); return false;" class="glyphicon glyphicon-chevron-down input-group-addon" style="cursor:pointer" ></span>
				          	</div>
				      </div>
				  </div>
				  
				  <div class="form-group" >
				    <label for="age"  class="col-sm-2 control-label">年龄<span style="color: red">&nbsp;</span></label>
				      <div class="col-sm-4">
				         <input type="text" id="age" name="age" maxlength="11" class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				      <label for="sex" class="col-sm-2 control-label">性别<span style="color: red">&nbsp;</span></label>
				      <div class="col-sm-4">
				         <select class="form-control"id="sex" name="sex">
					         <option id="sex1" value="1">男</option>
							 <option id="sex2" value="2">女</option>
				         </select>
				      </div>
				  </div>
				 <div class="form-group" >
				    <label for="address" class="col-sm-2 control-label">地址<span style="color: red">&nbsp;</span></label>
				      <div class="col-sm-4">
				         <input type="text"  id="address" name="address" maxlength="11"  class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				      <label for="phone" class="col-sm-2 control-label">手机号<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				         <input type="text" id="phone" name="phone" maxlength="11"  class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				  </div>
				  <div class="form-group" >
				    <label for="license" class="col-sm-2 control-label">驾驶证<span style="color: red">*</span></label>
				      <div class="col-sm-4">
				         <input type="text" id="license" name="license" maxlength="11" class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				      <label for="zipcode" class="col-sm-2 control-label">邮编<span style="color: red">&nbsp;</span></label>
				      <div class="col-sm-4">
				         <input type="text" id="zipcode" name="zipcode" maxlength="11"  class="form-control" id="firstname" 
				            placeholder="">
				      </div>
				  </div>
				  <div class="form-group"  >
				    <label  class="col-sm-2 control-label">驾照有效期<span style="color: red">*</span></label>
				      <div class="col-sm-5">
					       <input type="text" id="starttime" name="starttime" placeholder="开始时间" class="form-control"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
				      </div>
				      <div class="col-sm-5">
				        	<input type="text" id="endtime" name="endtime" placeholder="结束时间" class="form-control"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
				      </div>
				      
				  </div>
			</form>
		</div>
		
		
		<div class="modal fade" id="myModa2" tabindex="-1" role="dialog" aria-labelledby="myModalLabel2" aria-hidden="true">
		  <div class="modal-dialog"  style="width: 385px;">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel2">调整部门</h4>
		      </div>
		      <div id="changDeptBody" class="modal-body">
		      <input type="hidden" id="changdriverId">
		      <input type="hidden" id="changDeptId">
		      
		      </div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button id="bindButton" type="button" class="btn btn-primary" onclick="doChangDept()">确定</button>
		      </div>
		    </div>
		  </div>
		</div>
  </body>
</html>
