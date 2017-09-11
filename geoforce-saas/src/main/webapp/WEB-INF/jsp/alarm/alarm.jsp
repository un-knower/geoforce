<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>车辆报警</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0" />
		<meta content="IE=edge" http-equiv="X-UA-Compatible">
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/assets/js/jquery.validate.min.js"></script>
		<script src="${base}/resources/js/sys/util.js"></script>
		<script src="${base}/resources/js/alarm/alarmList.js"></script>
		<script src="${base}/resources/layer/layer.min.js"></script>
		<script type="text/javascript">
		</script>
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
			     	<form id="searchForm" class="navbar-form navbar-left" role="search">
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;" > 车牌号： </label>
			     	   <div class="form-group">
			     		<input type="text" id="license" name="license" placeholder="请输入车牌号" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 报警类型： </label>
					  <div class="form-group">
					   	<select id="typeId" name="typeId" >
							<option value="" >全部</option>
							<c:forEach var="types" items="${lists}">
								<c:if test="${types.code<300}">
					            <option value="${types.code}">${types.name}</option>
					            </c:if>
					        </c:forEach>		  		
						</select>
					  </div>
					  <label class="  no-padding-right" for="license" style="padding-top: 4px;" >状态 ： </label>
			     	   <div class="form-group">
			     		<select id="status" name="status">
							
							<option value="">全部</option>
							<option value="0">未处理</option>
							<option value="1">已处理</option>
						</select>
			     	  </div>
			     	   <label class="no-padding-right" for="license" style="padding-top: 4px;" >时间： </label>
			     	   <div class="form-group">
			     		<input type="text" id="starttime" name = "starttime"  placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
						<input type="text" id="endtime" name="endtime"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
			     	  </div>
			     	  
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>
			<table id="grid-table"></table>
			<div id="grid-pager" style=" height: 40px;" ></div>
			<div id="mapDiv" >
				<div id="mapDiv_supermap" style="width: auto; height: 100%;"></div>
				<div id="mapDiv_baidu" style="width: auto; height: 100%;border: 1px #3473B7 solid;"></div>
				<div id="mapDiv_google" style="width:auto; height:100%;border: 1px #3473B7 solid;"></div>
			</div>
		</div>
<!--批量处理div -->
		<div class="modal fade" id="alarmDisposeDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content" style="width: 412px;">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel">报警处理</h4>
		      </div>
		     <div id="bindRiverBody" class="modal-body" style=" height: 150px";>
		      <div class="row" style="width: 530px">
		      			处理内容：
		     		 <textarea class="form-control" name="alarmOpinionText" id="alarmOpinionText"
		     		 rows="3" style="width:400px; height:109px  " ></textarea>
			  	</div>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-primary" onclick="saveAlarm()">提交</button>
		      </div>
		    </div>
		  </div>
		</div>
	
	<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
	</body>
</html>
