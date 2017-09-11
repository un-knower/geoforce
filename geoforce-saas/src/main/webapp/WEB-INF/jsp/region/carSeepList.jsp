<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
   	<title>车辆超速报警</title>
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
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;" > 名称： </label>
			     	   <div class="form-group">
			     		<input type="text" id="name" name="name" placeholder="请输入名称" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 状态： </label>
					  <div class="form-group">
					    <select name="status" id="status">
							<option value="" >全部</option>
							<option value="0" >未启用 </option>
							<option value="1" >启用 </option>
						</select>
					  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();">搜索<i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>	
		<table id="grid-table" ></table>
		<div id="grid-pager" ></div>
<!--添加/编辑超速 -->
	<div class="modal fade" id="seepDiv" tabindex="-1" role="dialog" 
	aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel">超速报警</h4>
		      </div>
		      <div class="alert alert-danger" style="display: none;" role="alert" id="warning">
				<strong id="content"></strong></div>
		      <div id="bindRiverBody" class="modal-body">
		     	<form id ="seepForm" action="">
					<div class="form-group" id="form-group01">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>超速名称：</div>
						      <input type="hidden" id="seepId" name="seepId">
						     <input type="text" id="seppName" name="seppName" placeholder="" class="form-control" maxlength="11"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>&nbsp;&nbsp;&nbsp;&nbsp;限速值：</div>
						     <input type="text" id="seepValue" name="seepValue" placeholder="" class="form-control" maxlength="11"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon" ><span style="color:red" >*</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态： </div>
						     <select name="seepStatus" class="form-control" id="seepStatus">
                            	<option value="0">未启用</option>
                                <option value="1">启用</option>
                            </select>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>开始时间： </div>
						     <input type="text" id="seepStaTime" name="seepStaTime"  
						     class="form-control" placeholder="开始时间" 
						      onClick="WdatePicker({dateFmt:'HH:mm'})"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>结束时间： </div>
						      <input type="text" id="seepEndTime" name="seepEndTime" class="form-control"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'HH:mm'})"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>有效日期： </div>
						    <input  type="checkbox" id='1' name="workTime" value="1">星期一
							<input     type="checkbox" id='2'  name="workTime" value="2" >星期二
							<input    type="checkbox" id='3' name="workTime" value="3" >星期三
							<input    type="checkbox" id='4'  name="workTime" value="4" >星期四
							<input    type="checkbox" id='5'  name="workTime" value="5" >星期五
							<input    type="checkbox" id='6' name="workTime" value="6">星期六
							<input   type="checkbox" id='7' name="workTime" value="7">星期日		
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon">备注信息： </div>
						      <input id="remark" name="remark" class="form-control"  />
						    </div>
						  </div>
					</form>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		        <button id="bindButton" type="button" class="btn btn-primary" onclick="typeFun()">确定</button>
		      </div>
		    </div>
		  </div>
		</div>
	</div>	
	<div class="modal fade" id="seeplinkCarsDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
					 <button onclick="addSeepCarList()">添加 </button><p>
					 <button onclick="delrow()">删除</button>
					</div>
					<div class="col-sm-4" style="margin-left: 128px;height:200px ">
						<div class="widget-box widget-color-blue2" style="width:230px; ">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">已关联车辆</h4>
					       </div>
					       <div class="widget-body">
					           <table id="seepCar-grid-table"></table>
					           <div id="seepCar-grid-pager" ></div>
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
		<script src="${base}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${base}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${base}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		<script src="${base}/resources/js/car/carDeptTree.js"></script>
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/js/sys/util.js"></script>
		<script src="${base}/resources/js/region/carSeep.js"></script>
		<script src="${base}/resources/js/region/seepCar.js"></script>
			<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>	
  </body>
</html>
