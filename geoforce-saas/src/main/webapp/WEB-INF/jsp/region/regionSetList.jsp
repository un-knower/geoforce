<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <%@ include file="/WEB-INF/commons/taglibs.jsp"%>
    	<link rel=stylesheet type=text/css href="${base}/resources/zTree/css/demo.css" type="text/css">
		<link rel=stylesheet type=text/css href="${base}/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script src="${base}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${base}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${base}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		<script src="${base}/resources/js/car/carDeptTree.js"></script>
		<script src="${base}/resources/js/sys/util.js"></script>
		<script src="${base}/resources/layer/layer.min.js"></script>
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/js/region/regionSetList.js"></script>
		<script src="${base}/resources/js/region/regionListCar.js"></script>
    <title>车辆围栏设置</title>
  </head>
 	 <body>
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
			     	<form id="region_set_list_form" class="navbar-form navbar-left" role="search">
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;" > 名称： </label>
			     	   <div class="form-group">
			     		<input type="text" id="name"  name="name" placeholder="请输入名称" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 报警类型： </label>
					  <div class="form-group">
					   	<select id="typeId" name="typeId" >
							<option value="" >全部</option>
							<c:forEach var="types" items="${lists}">
								<c:if test="${types.code>300}">
					            <option value="${types.code}">${types.name}</option>
					            </c:if>
					        </c:forEach>		  		
					</select>
					  </div>
					  <label class="  no-padding-right" for="license" style="padding-top: 4px;" >状态 ： </label>
			     	   <div class="form-group">
			     		<select name="status" id="status">
							<option value="" >全部 </option>
							<option value="1" >启用 </option>
							<option value="0" >未启用 </option>
						</select>
			     	  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();">搜索<i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>
		<table id="grid-table"></table>
		<div id="grid-pager" ></div>
	<div id="regionAddDiv" style="width: 900px; height: 500px; display:none;"> 
		<div style="width: 300px; height: 500px; float: left;">
					<input type="hidden" id="editFlag">
						<form id ="region_set_list_add_from" action="">
							 <input type="hidden" id="region" name="region">
							 <input type="hidden" id="id" name="regionSetId">
							<div class="input-group input-group-4g">
							  <span class="input-group-addon"><span style="color:red" >*</span>围栏名称：</span>
							  <input type="text" class="form-control" placeholder="名称"
							   id="regionSetName" name="name"  style="height: ">
							</div>
							<div class="input-group input-group-4g">
							  <span class="input-group-addon"><span style="color:red" >*</span>围栏类型：</span>
							  	<select id="type" name="type" class="form-control" placeholder="类型">
							  			<option value="">请选择类型</option>
									<c:forEach var="types" items="${lists}">
										<c:if test="${types.code>300}">
							            <option value="${types.code}">${types.name}</option>
							            </c:if>
							        </c:forEach>		  		
									</select>
							</div>
							<div class="input-group input-group-4g">
							  <span class="input-group-addon"><span style="color:red" >*</span>围栏状态：</span>
									 <select name="status" class="form-control"  id="select">
			                            	<option value="0" >启用</option>
			                                <option value="1" >未启用</option>
			                            </select>
							</div>
							<div class="input-group input-group-4g">
							  <span class="input-group-addon"><span style="color:red" >*</span>开始时间：</span>
								<input type="text" id="staTime" name="staTime"  class="form-control" placeholder="开始时间"  onClick="WdatePicker({dateFmt:'HH:mm'})"/>	
							</div>
							<div class="input-group input-group-4g">
							  <span class="input-group-addon"> <span style="color:red" >*</span>结束时间：</span>
								<input type="text" id="endTime" name="endTime" class="form-control"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'HH:mm'})"/>
							</div>
							<div class="input-group input-group-4g">
							  <span class="input-group-addon"><span style="color:red" >*</span>有效时间：</span>
									<input 	type="checkbox" id='1' name="workTime" value="1" >星期一<br>
									<input  type="checkbox" id='2'  name="workTime" value="2" >星期二<br>
									<input  type="checkbox" id='3' name="workTime" value="3" >星期三<br>
									<input  type="checkbox" id='4'  name="workTime" value="4" >星期四<br>
									<input  type="checkbox" id='5'  name="workTime" value="5" >星期五<br>
									<input  type="checkbox" id='6' name="workTime" value="6">星期六<br>
									<input  type="checkbox" id='7' name="workTime" value="7">星期日	
							</div>
							<div class="input-group input-group-1g">
							  <span class="input-group-addon">备注信息：</span>
									<input type="text" id="regionSetRemark" name="remark" placeholder="" class="form-control" maxlength="11"/>
							</div>
							<div class="input-group input-group-1g" id="undrawFence" style="margin-top: 70px; margin-left: 40px" >
							 	<button type="button"  class="btn btn-primary" onclick="drawFenceInit()">编辑围栏</button>
		        				<button type="button" class="btn btn-primary" onclick="undrawFence()">重新绘制</button>
		        				<button type="button" class="btn btn-primary" onclick="typeFun()">确定</button>
							</div>
							<div class="input-group input-group-1g" id="drawFenceInit" style="margin-top: 70px;margin-left: 40px" >
							 	<button type="button"  class="btn btn-primary" onclick="drawFenceInit()">绘制围栏</button>
		        				<button type="button" class="btn btn-primary" onclick="undrawFence()">取消绘制</button>
		        				<button type="button" class="btn btn-primary" onclick="typeFun()">确定</button>
							</div>
						</form>
			</div>
		<!--地图数据  -->
		<div id="mapDiv_supermap"  style="width: 600px; height: 500px; float: left;" >
		</div>
  </div>
<!--车辆关联报警div -->
	<div class="modal fade" id="regionlinkCarsDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content" style="width: 552px">
		     <div id="bindRiverBody" class="modal-body" style=" height: 400px";>
		      <div class="row" style="width: 530px">
		      	 <div class="col-sm-2">
				       <div class="widget-box widget-color-blue2" style="width: 229px;">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">待选车辆</h4>
					       </div>
					       <div class="widget-body">
					            <div class="widget-main padding-8" style=" padding-right: 0px; padding-bottom: 0px;
   										padding-left: 0px;  padding-top: 0px;">
									<div id="carTreeModelDiv" data-options="region:'center'">
										<ul id="carDeptTree" class="ztree" style="height: 320px"></ul>
									</div>
								</div>
							</div>	
						</div>
					</div>
					 <div class="col-sm-4" style="margin-left: 7px; left: 140px; top: 158px;width: 74px;">
					  <button onclick="addCarList()">添加</button><p>
					 <button onclick="delrow()">删除</button>
					</div>
					<div class="col-sm-4" style="margin-left: 128px;height:200px ">
						<div class="widget-box widget-color-blue2" style="width:230px; ">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">以关联车辆</h4>
					       </div>
					       <div class="widget-body">
					           <table id="regionCar-grid-table"></table>
					           <div id="regionCar-grid-pager" ></div>
							</div>	
						</div>
					</div>		
			  	</div>
		      </div>
		      <div class="modal-footer">
		      	<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button type="button" class="btn btn-primary" onclick="saveSelectedCars()">提交</button>
		      </div>
		    </div>
		  </div>
		</div>		
	<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
  </body>
</html>
