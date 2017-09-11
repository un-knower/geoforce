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
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/zTree/css/zTreeStyle/zTreeStyle.css">
	<link rel="stylesheet" type="text/css" href="${ctx}/resources/css/sys/dept.css">
	<!-- page specific plugin scripts -->
	<script type="text/javascript" src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctx}/resources/assets/js/bootbox.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/zTree/js/jquery.ztree.core-3.5.js"></script>
	<script type="text/javascript" src="${ctx}/resources/assets/js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="${ctx}/resources/assets/js/jquery.validate.message.cn.js"></script>
	<script type="text/javascript">//需放到base js中
		var ctx = '${ctx}';
	</script>
	<script type="text/javascript" src="${ctx}/resources/js/sys/dept.js"></script>
</head>

	<!-- PAGE CONTENT BEGINS -->
	<div class="row" style="margin-bottom: 8px;text-align: left;">
		<div class="col-xs-12">
			<label class="no-padding-right" for="name" style="padding-top: 4px;">部门名称：</label>
			<input type="text" id="name" placeholder="部门名称" class="" maxlength="10"/>
			<button type="button" class="btn btn-purple btn-sm" id="search">
				查询<i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
			</button>
		</div>
	</div>
	
	<table id="grid-table"></table>
	<div id="grid-pager"></div>
	
	<div id="addDept" style="display: none;">
		<div class="addDeptDIV">
			<form id="addDeptForm">
				<table>
					<tr>
						<td style="text-align: right;">
							<label class="no-padding-right" for="parentName" style="padding-top: 4px;">
								(<label class="star">*</label>)上级部门：
							</label>
						</td>
						<td>
							<div class="input-group">
								<input type="text" id="parentName" name="parentName" placeholder="请选择部门" readonly="readonly"/>
								<input type="hidden" id="parentId" name="parentId">
								<span class="input-group-btn">
									<button id="menuBtn" class="btn btn-sm btn-default" type="button" onclick="showMenu()">选择</button>
								</span>
								<div id="treeWrapDIV" style="display: none; position: absolute;">
									<ul id="tree" class="ztree" style="margin-top:0; width:178px;"></ul>
								</div>
							</div>
						</td>
						<td style="text-align: right;">
							<label class="no-padding-right" for="name" style="padding-top: 4px;">
								(<label class="star">*</label>)部门名称：
							</label>
						</td>
						<td>
							<input type="text" id="name" name="name" placeholder="部门名称" class="" minlength="2" maxlength="48"/>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">
							<label class="no-padding-right" for="phone" style="padding-top: 4px;">联系电话：</label>
						</td>
						<td>
							<input type="text" id="phone" name="phone" placeholder="联系电话" class="" maxlength="16"/>
						</td>
						<td style="text-align: right;">
							<label class="no-padding-right" for="headName" style="padding-top: 4px;">部门主管：</label>
						</td>
						<td>
							<input type="text" id="headName" name="headName" placeholder="部门主管" class="" maxlength="16"/>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">
							<label class="no-padding-right" for="headPhone" style="padding-top: 4px;">主管电话：</label>
						</td>
						<td>
							<input type="text" id="headPhone" name="headPhone" placeholder="主管电话" class="" maxlength="16"/>
						</td>
						<td style="text-align: right;">
							<label class="no-padding-right" for="address" style="padding-top: 4px;">地址：</label>
						</td>
						<td>
							<input type="text" id="address" name="address" placeholder="地址" class="" maxlength="128"/>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">
							<label class="no-padding-right" for="zipcode" style="padding-top: 4px;">邮编：</label>
						</td>
						<td rowspan="3">
							<input type="text" id="zipcode" name="zipcode" placeholder="邮编" class="" maxlength="8"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<!-- PAGE CONTENT ENDS -->
</body>
</html>