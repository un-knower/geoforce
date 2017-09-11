<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>调度管理</title>
		<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
		<script src="${ctxp}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${ctxp}/resources/assets/js/bootbox.min.js"></script>
		<link rel=stylesheet type=text/css href="${ctxp }/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script src="${ctxp}/resources/zTree/js/jquery.ztree.exedit-3.5.min.js"></script>
		
		<script src="${ctxp}/resources/js/car/carDeptTree.js"></script>
		<script src="${ctxp}/resources/js/car/carsendMsg.js"></script>
		<script src="${ctxp}/resources/assets/js/jquery.validate.min.js"></script>
		<script type="text/javascript">
		</script>
	</head>
	<body>
		<div class="row"  style="margin-bottom: 8px;text-align: left;">
		   <div class="col-xs-12 "> 
			   <div class="row">
			       <div class="col-sm-2">
				       <div class="widget-box widget-color-blue2">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">车辆列表</h4>
					       </div>
					       <div class="widget-body" style="overflow:auto;">
					            <div class="widget-main padding-8">
									<div id="carTreeModelDiv" style="height:430px;" data-options="region:'center'">
										<ul id="carDeptTree" class="ztree" style="height:426px;"></ul>
									</div>
								</div>
							</div>	
						</div>	
					</div>
					<div class="col-sm-10">
				       <div class="widget-box widget-color-blue2">
					       <div class="widget-header">
					       		<h4 class="widget-title lighter smaller">车辆调度</h4>
					       </div>
					       <div class="widget-body">
					            <div class="widget-main padding-8">
									<div style="height:430px;">
									    <form id="sendMsgForm" action="" class="form-horizontal" role="form">
									    <input type="hidden" name="carIds" id="carIds">
									    <div class="form-group">
										    <label for="type" class="col-sm-2 control-label">类型:</label>
										    <div class="col-sm-10">
												<select id="type" name="type" class="col-xs-10 col-sm-5">
													<option value="1">通知</option>
													<option value="2">任务</option>
												</select>
												
										    </div>
										  </div>
										  <div class="form-group">
										    <label for="title" class="col-sm-2 control-label">标题:</label>
										    <div class="col-sm-10">
										      <input type="title" class="col-xs-10 col-sm-5" id="title" name="title" placeholder="">
										    </div>
										  </div>
										  <div class="form-group">
										    <label for="content" class="col-sm-2 control-label">内容:</label>
										    <div class="col-sm-10">
										      <textarea class="col-xs-10 col-sm-5" rows="3" id="content" name="content"></textarea>
										    </div>
										  </div>
									    <div class="form-group">
										    <div class="col-sm-offset-2 col-sm-10">
										      <button type="button" onclick="sendMsg();" class="btn btn-default">发送信息</button>
										    </div>
										  </div>
									    </form>
								    </div>
								</div>
							</div>	
						</div>	
					</div>
				</div>
          </div>
		
			
		</div>
	</body>
</html>
