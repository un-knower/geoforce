<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>车辆围栏报警设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${base }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css" rel=stylesheet>
<style type="text/css">
		body { 
			overflow-x:hidden ; /*隐藏水平滚动条*/ 
			overflow-y:hidden ; /*隐藏水平滚动条*/
} 
		</style>
</head>
<body>
<div class="page-content-area">
<nav id="monitorBtns" class="navbar navbar-default navbar-white" role="navigation" style="background-color: #e7e7e7;">
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
    	<form id="" class="navbar-form navbar-left" role="search">
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;" > 名称： </label>
			     	   <div class="form-group">
			     		<input type="text" id="name"  name="name" placeholder="请输入名称" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 报警类型： </label>
					  <div class="form-group">
					   	<select id="typeId" name="typeId" >
								<option value="" >全部</option>
					            <option value="301">进围栏</option>
					            <option value="302">出围栏</option>
					</select>
					  </div>
					  <label class="  no-padding-right" for="license" style="padding-top: 4px;" >状态 ： </label>
			     	   <div class="form-group">
			     		<select name="status" id="status">
							<option value="" >全部 </option>
							<option value="0" >启用 </option>
							<option value="1" >未启用 </option>
						</select>
						  	 <input type="hidden" id="region" name="region">
   							<input type="hidden" id="editFlag" value=""  >
			     	  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();">
					<i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
    </form>
     	<ul class="nav navbar-nav navbar-right">
     		<li class="dropdown">
          		<a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown" style="color:#2a6496;">
          		<span class="glyphicon glyphicon-wrench">操作</span> 
          		<span class="caret"></span>
          		</a>
		        <ul class="dropdown-menu" role="menu" style="z-index: 999999999;">
		            <li><a href="javascript:void(0);" onclick="drawFenceInit('add')" >绘制围栏</a></li>
				    <li><a href="javascript:void(0);" onclick="drawFenceInit('edit')">修改围栏</a></li>
		       </ul>
        	</li>
        	<li>
        		<a href="javascript:void(0);" onclick="mapClear()" style="color:#2a6496;"><span class="glyphicon glyphicon-trash">地图清屏</span></a>
        	</li>
     	</ul>
     	
    </div>
  </div>
</nav>
<%-- 地图数据--%>
	<div class="row">
			<div class="container-fluid">
					<div id="mapDiv_supermap" class="col-xs-12"></div>
			</div>
		<nav id="mapTable" class="navbar navbar-default nav-white" role="navigation" style="background-color:white;">
		<div class="container-fluid">
				<table id="regionListTab" ></table>
				<div id="regionGridPager"  style="height: 40px"></div>
		</div>
		</nav>
	</div>
<!--添加/编辑围栏   -->
<div class="modal fade" id="regionDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		        <h4 class="modal-title" id="myModalLabel">围栏设置</h4>
		      </div>
		      <div class="alert alert-danger" style="display: none;" role="alert" id="warning">
				<strong id="content"></strong></div>
		      <div id="bindRiverBody" class="modal-body">
		     			<form id ="regionForm" action="">
					 <input type="hidden" id="id" name="regionSetId">
						<div class="form-group" id="form-group01">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>围栏名称：</div>
						     <input type="text" class="form-control" placeholder="名称"
							   id="regionName" name="name"  maxlength="10" >
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>围栏类型：</div>
						     	<select id="type" name="type" class="form-control" placeholder="类型">
							  			  <option value="301">进围栏</option>
					            			<option value="302">出围栏</option>	  		
									</select>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon" ><span style="color:red" >*</span>围栏状态： </div>
						     <select name="status" class="form-control"  id="select">
			                            	<option value="0" >启用</option>
			                                <option value="1" >未启用</option>
			                   </select>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>开始时间： </div>
						     <input type="text" id="staTime" name="staTime"  
						     class="form-control" placeholder="开始时间" 
						      onClick="WdatePicker({dateFmt:'HH:mm'})"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>结束时间： </div>
						      <input type="text" id="endTime" name="endTime" class="form-control"  placeholder="结束时间"  onClick="WdatePicker({dateFmt:'HH:mm'})"/>
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon"><span style="color:red" >*</span>有效日期： </div>
						 			  <input 	type="checkbox" id='1' name="workTime" value="1" >星期一
									<input  type="checkbox" id='2'  name="workTime" value="2" >星期二
									<input  type="checkbox" id='3' name="workTime" value="3" >星期三
									<input  type="checkbox" id='4'  name="workTime" value="4" >星期四
									<input  type="checkbox" id='5'  name="workTime" value="5" >星期五
									<input  type="checkbox" id='6' name="workTime" value="6">星期六
									<input  type="checkbox" id='7' name="workTime" value="7">星期日		
						    </div>
						  </div>
						  <div class="form-group">
						    <div class="input-group">
						      <div class="input-group-addon">备注信息： </div>
						      	<input type="text" id="remark" name="remark"  class="form-control"  maxlength="20" >
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
 <!--车辆关联报警div -->
	<div class="modal fade" id="regionlinkCarsDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		  <div class="modal-dialog">
		    <div class="modal-content" style="width: 552px">
		     <div id="bindRiverBody" class="modal-body" style=" height: 400px;">
		      <div class="row" style="width: 530px">
		      	 <div class="col-sm-2">
				       <div class="widget-box widget-color-blue2" style="width: 229px;">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">待选车辆</h4>
					       </div>
					       <div class="widget-body"  style="overflow: auto">
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
					       		<h4 class="widget-title lighter smaller">已关联车辆</h4>
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
<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${base}/resources/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<script src="${base}/resources/layer/layer.min.js"></script>
<script src="${base}/resources/assets/js/bootbox.min.js"></script>
<script src="${base}/resources/js/car/carDeptTree.js"></script>
<script src="${base}/resources/js/region/region.js"></script>
<script src="${base}/resources/js/region/regionListCar.js"></script>

</body>
</html>