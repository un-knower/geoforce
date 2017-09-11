<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
<title>角色管理</title>
<meta name="description" content="地图慧-企业可视化管理平台" />
<meta name="keywords" content="地图慧-企业可视化管理平台" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- page specific plugin styles -->
<link href="${ctx }/resources/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" />
<!-- page specific plugin scripts -->
<script src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${ctx}/resources/assets/js/bootbox.min.js"></script>
<script src="${ctx }/resources/zTree/js/jquery.ztree.all-3.5.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.message.cn.js"></script>
<script src="${ctx}/resources/js/sys/util.js"></script>
<script src="${ctx}/resources/js/sys/role.js"></script>
<script type="text/javascript" >
	var PROJECT_URL='${ctx}';
</script>
<style type="text/css">
table tr td {
padding: 2px 0px 2px 0px;
}
.star {
color: red;
}
</style>

</head>

<body>
 
<!-- PAGE CONTENT BEGINS -->
<div class="row"  style="margin-bottom: 8px;text-align: left;"> 
	<div class="col-xs-12  ">
		<label class="  no-padding-right" for="rolename" style="padding-top: 4px;" > 角色名称： </label>
		<input type="text" id="rolename" placeholder="角色名称" class=" " maxlength="10"/>
		
		<label class="  no-padding-right" for="starttime" style="padding-top: 4px;"> 创建时间： </label>
		<input type="text" id="starttime" placeholder="开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" />
		<label class="  no-padding-right" style="padding-top: 4px;">至</label>
		<input type="text" id="endtime" placeholder="结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
		
		<button type="button" class="btn btn-purple btn-sm "  id="search">
			搜索
			<i class="ace-icon fa fa-search icon-on-right bigger-110"></i>
		</button>
	</div>
</div>

<table id="grid-table"></table>

<div id="grid-pager" ></div>

<div id="addWrapDIV" style="display: none;">
	<div class="addDIV  ">
	<form id="addForm">
	<table >
		<tr>
			<td style="text-align: right;">
				<label class="  no-padding-right" for="rolename2" style="padding-top: 4px;" >(<label class="star">*</label>) 角色名称： </label></td><td>
				<input type="text" id="rolename2" name="rolename2" placeholder="角色名称" class="required" minlength="2" maxlength="10"/>
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">
				<label class="  no-padding-right" for="remark" style="padding-top: 4px;" > 角色描述： </label></td><td>
				<input type="text" id="remark" name="remark" placeholder="角色描述" class=" " maxlength="50"/>
			</td>
	
	</table>
	</form>
	</div>
</div>
<div id="treeWrapDIV" style="display: none;">
	<div class="row"  style="margin-bottom: 8px;text-align: left;"> 
		<ul id="tree" class="ztree"></ul>
	</div>
</div>

<!-- PAGE CONTENT ENDS -->
</body>
</html>
