<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
   <%@ include file="/WEB-INF/commons/taglibs.jsp"%>
    	<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/demo.css" type="text/css">
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		<script src="${ctxp}/resources/js/car/carDeptTree.js"></script>
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/js/map/mapSupport.js"></script>
		<script src="${base}/resources/js/region/carAlarmRrgion.js"></script>
    <title>车辆报警设置</title>

  </head>
  
  <body>
<div id="bindRiverBody" class="modal-body">
  		<div class="row">
  		      	 <div class="col-sm-2">
				       <div class="widget-box widget-color-blue2" style="width: 229px;">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">围栏信息</h4>
					       </div>
					       <div class="widget-body">
					            <div class="widget-main padding-8" >
					            		<input type="button" value="选择围栏" onclick="carListDivFun()">
					            		<input type="button" value="添加围栏" onclick="carAddDivFun()">
									<div id="carListDiv" style="height:300px;" data-options="region:'center'">
										 	<table id="grid-table"></table>
					           				<div id="grid-pager" ></div>
					           				123123123123123123123123123123
									</div>
									<div id="carAddDiv" style="height:300px; display: none;" data-options="region:'center'" >
										 	<form id="carAddDivFrom">
										 	<input type="text" id="carAddListType" value="1" name="type">
										 	<table>
										 			<tr>
										 				<td><label class="no-padding-right" for="mobilephone2" style="padding-top: 4px;"> 名称： </label></td>
														<td><input type="text" id="regionName" name="name" placeholder="" class="form-control" maxlength="11"/></td>
													</tr>
													<tr>
														<td><label class="no-padding-right" for="mobilephone2" style="padding-top: 4px;"> 是否共享： </label></td>
														<td>
															<select>
																<option value="0">共享</option>
																<option value="1">不共享</option>
															</select>
														</td>
													</tr>
													<tr>
														<td><label class="no-padding-right" for="mobilephone2" style="padding-top: 4px;"> 描述： </label></td>
														<td><input type="text" id="regionName" name="name" placeholder="" class="form-control" maxlength="11"/></td>
													</tr>
										 		</table>
										 	</form>
										 		<input type="button" value="绘制围栏">
										 		<input type="button" value="清屏">	
										 		<input type="button" onclick="bindDirver()">
										 		
									</div>
								</div>
							</div>	
						</div>
			</div>
			<div class="col-sm-2" style="margin-left: 130px;">
						<div class="widget-box widget-color-blue2" style="width: 450px;">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">地图数据</h4>
					       </div>
					       <div class="widget-body">
					            <div class="widget-main padding-8" >
									<div id="carTreeModelDiv" style="height:300px;" data-options="region:'center'">
										<ul id="carDeptTree" class="ztree" style="height:300px;"></ul>
									</div>
								</div>
							</div>	
						</div>
			</div>
		</div>
		  	</div>
		      <div class="modal-footer">
		        <button type="button" class="btn btn-primary" onclick="bindDirver()">提交</button>
		        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
		      </div>
  
 <%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
  </body>
</html>
