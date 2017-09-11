<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
	<title>地图慧-企业可视化管理平台</title>
	<meta name="description" content="地图慧-企业可视化管理平台"/>
	<meta name="keywords" content="地图慧-企业可视化管理平台"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<!-- page specific plugin styles -->
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/order/orderHistory.css">
	<!-- page specific plugin scripts -->
	<script type="text/javascript" src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/resources/assets/js/bootbox.min.js"></script>
	<script type="text/javascript">//需放到base js中
		var ctx = '${ctx}';
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/order/orderHistory.js"></script>
</head>
<body>
	<!-- PAGE CONTENT BEGINS -->
	<div class="row" style="margin-bottom: 8px;text-align: left;">
		<div class="col-xs-12">
			<label class="no-padding-right" for="number" style="padding-top: 4px;">订单编号：</label>
			<input type="text" id="number" placeholder="订单编号" class="" maxlength="16"/>
			<label class="no-padding-right" for="batch" style="padding-top: 4px;">订单批次：</label>
			<input type="text" id="batch" placeholder="订单批次" class="" maxlength="16"/>
			<%--<label class="no-padding-right" for="province" style="padding-top: 4px;">省：</label>
			<select class="form-control" id="province" name="province">
				<option value="0">全部</option>
			</select>
			<label class="no-padding-right" for="city" style="padding-top: 4px;">市：</label>
			<select class="form-control" id="city" name="city">
				<option value="0">全部</option>
			</select>
			<label class="no-padding-right" for="county" style="padding-top: 4px;">区县：</label>
			<select class="form-control" id="county" name="county">
				<option value="0">全部</option>
			</select>
			--%><label class="no-padding-right" for="address" style="padding-top: 4px;">详细地址：</label>
			<input type="text" id="address" placeholder="详细地址" class="" maxlength="32"/>
			<button type="button" class="btn btn-purple btn-sm" id="search">
				查询<i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
			</button>
		</div>
	</div>
	
	<table id="grid-table"></table>
	<div id="grid-pager"></div>
	<!-- PAGE CONTENT ENDS -->
</body>
</html>