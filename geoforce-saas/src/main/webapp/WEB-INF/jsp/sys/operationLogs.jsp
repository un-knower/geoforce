<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
	<title>日志管理</title>
	<meta name="description" content="地图慧-企业可视化管理平台"/>
	<meta name="keywords" content="地图慧-企业可视化管理平台"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<script type="text/javascript">//需放到base js中
		var ctx = '${ctx}';
	</script>
	<!-- page specific plugin scripts -->
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/sys/user-logs/operationLogs.css">
</head>
<body>
	<div class="container-logs">
		<!-- 搜索参数 -->
		<div class="search-area">
			<!--<label for="txt_department_ids">部门: </label>
			<div class="inputs-group list-department">
				<input class="inputs" id="txt_department_ids" type="text" value="" readonly="readonly">
				<span class="glyphicon glyphicon-chevron-down icons" aria-hidden="true"></span>
				<div class="choice"></div>
			</div>-->

			<label for="txt_username">登录账号: </label>
			<div class="inputs-group">
				<input class="inputs" id="txt_username" type="text" value=""
					placeholder="输入用户名、邮箱、手机号搜索">
			</div>

			<label for="txt_mudule_ids">操作内容: </label>
			<div class="inputs-group">
				<select class="inputs" id="txt_mudule_ids">
					<option value="-1" selected="selected">全部</option>
					<option value="40288e9f48448d8a0148448d90770004">区划管理</option>
					<option value="5">登录系统</option>
					<option value="6">登出系统</option>
				</select>
				<!--<input class="inputs" id="txt_mudule_ids" type="text" value="" readonly="readonly">
				<span class="glyphicon glyphicon-chevron-down icons" aria-hidden="true"></span>
				<div class="choice"></div>-->
			</div>

			<label for="txt_start">时间段: </label>
			<div class="inputs-group">
				<input class="inputs shorter input-during" id="txt_start" name="start" type="text" value="">
			</div>
			<span>至</span>
			<div class="inputs-group">
				<input class="inputs shorter input-during" 
					id="txt_end" name="end" type="text" value="">
			</div>

			<button class="search-logs">查询</button>
		</div>
	
		<div class="widget-box widget-color-blue light-border">
			<div class="widget-header">
				<h4 class="widget-title smaller">日志管理</h4>
		    	<!--<div class="widget-toolbar no-border">
				    <a class="collapse-table" href="#"><i class="ace-icon fa fa-chevron-up"></i></a>
		    	</div>-->
			</div>
			<div class="widget-body">
				<table id="grid-table"></table>
				<div id="grid-pager" style="width: 100px;"></div>
			</div>
		</div>
		
		
	  <!-- 底部提示 -->
	  <div id="popover_result" class="popover-result">
	    <div class="popover-result-content" id="popover_content"></div>
	  </div>
	
	  <!-- 顶部提示 -->
	  <div id="popover_hint" class="popover-hint">
	    <div class="popover-hint-content">
	    	<a id="popover_hint_content" href="javascript:void(0);"></a></div>
	  </div>
	
	  <!-- 正在加载 -->
	  <div class="mask-loading-text">
	    <div class="hint"><a href="javascript:void(0);"></a></div>
	  </div>
		
	</div>
	
	<script type="text/javascript" src="${ctx}/resources/js/Dituhui/iclient-8c/SuperMap.Include.js"></script>
  <script src="${ctx}/resources/js/Dituhui/dituhui.libs.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
	
	<script type="text/javascript" src="${ctx}/resources/js/config.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/sys/dropdown-trees.js"></script>
	<!--<script type="text/javascript" src="${ctx}/resources/js/sys/util.js"></script>-->
	<script type="text/javascript" src="${ctx}/resources/js/sys/operationLogs.js"></script>
	<script type="text/javascript" src="${ctx}/resources/js/sys/logs/table-detail.js"></script>
</body>
</html>