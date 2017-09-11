<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
<title>用户管理</title>
<meta name="description" content="地图慧-企业可视化管理平台" />
<meta name="keywords" content="地图慧-企业可视化管理平台" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- page specific plugin styles -->
<link rel="stylesheet" type="text/css" href="${ctx}/resources/zTree/css/zTreeStyle/zTreeStyle.css">
<!-- page specific plugin scripts -->
<script src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${ctx}/resources/assets/js/bootbox.min.js"></script>
<script src="${ctx }/resources/zTree/js/jquery.ztree.core-3.5.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.message.cn.js"></script>
<script src="${ctx}/resources/js/sys/util.js"></script>
<script src="${ctx}/resources/js/sys/user.js"></script>
<script type="text/javascript" >
	var PROJECT_URL='${ctx}';
	var ctx='${ctx}';
</script>
<style type="text/css">
table tr td {
padding: 2px 0px 2px 0px;
}
.star {
color: red;
}
ul.ztree {
	margin-top: 10px;
	border: 1px solid #617775;
	background: #f0f6e4;
	width: 220px;
	height: 200px;
	overflow-y: scroll;
	overflow-x: auto;
}
</style>

</head>

<body>
 
<!-- PAGE CONTENT BEGINS -->
<div class="row"  style="margin-bottom: 8px;text-align: left;"> 
	<div class="col-xs-12  ">
		<label class="  no-padding-right" for="search_username" style="padding-top: 4px;" > 姓名： </label>
		<input type="text" id="search_username" placeholder="姓名" class=" " maxlength="8"/>
		
		<label class="  no-padding-right" for="search_mobilephone" style="padding-top: 4px;"> 手机： </label>
		<input type="text" id="search_mobilephone" placeholder="手机" class="" maxlength="11"/>
		
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

<div id="adduser" style="display: none;">
	<div class="addUserDIV  ">
	<form id="addUserForm">
	<table>
		<tr>
			<td class=".form-group" style="text-align: right;">
				
				
				<label class="  no-padding-right" for="email" style="padding-top: 4px;width: 90px;"> (<label class="star">*</label>)电子邮箱： </label></td><td>
				<input type="text" id="email" name="email" placeholder="电子邮箱"  class="required email" minlength="5" maxlength="32" />
			</td>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="realname" style="padding-top: 4px;width: 90px;">(<label class="star">*</label>) 真实姓名： </label></td><td>
				<input type="text" id="realname" name="realname" placeholder="真实姓名" class="required" minlength="2" maxlength="8"/>
			</td>
		</tr>
		<tr>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="password" style="padding-top: 4px;" >(<label class="star">*</label>) 密码： </label></td><td>
				<input type="password" id="password" name="password" placeholder="密码" class="required" minlength="6" maxlength="64" onkeyup="this.value=this.value.replace(/[^\w]/g,'')"/>
			</td>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="check_password" style="padding-top: 4px;"> (<label class="star">*</label>)确认密码： </label></td><td>
				<input type="password" id="check_password" name="check_password" placeholder="确认密码" class="required" minlength="6" maxlength="64" equalTo=".bootbox #password" onkeyup="this.value=this.value.replace(/[^\w]/g,'')"/>
			</td>
		</tr>
		<tr>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="parentName" style="padding-top: 4px;" > (<label class="star">*</label>)部门： </label></td><td>
				<!-- <select class="form-control" id="dept" name="dept" >
				</select> -->
				<div class="input-group">
					<input type="text" id="parentName" name="parentName" placeholder="部门" readonly="readonly" onclick="showMenu()"/>
					<input type="hidden" id="parentId" name="parentId">
					<input type="hidden"  id="dept" name="dept" >
					<div id="treeWrapDIV" style="display: none; position: absolute;">
						<ul id="tree" class="ztree" style="margin-top:0; width:178px;"></ul>
					</div>
				</div>
			</td>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="sex" style="padding-top: 4px;"> (<label class="star">*</label>)性别： </label></td><td>
				<label >
					<input id="sex1" name="sex" type="radio" value="1" class="ace" >
					<span class="lbl" > 男</span>
				</label>
				<label>
					<input id="sex0" name="sex" type="radio" value="0" class="ace">
					<span class="lbl"> 女</span>
				</label>
				<input name="sexHidden" id="sexHidden" type="hidden" value="" class="ace">
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">
				<label class="  no-padding-right" for="address" style="padding-top: 4px;" > 联系地址： </label></td><td>
				<input type="text" id="address" name="address" placeholder="联系地址" class=" " maxlength="32"/>
			</td>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="mobilephone2" style="padding-top: 4px;"> (<label class="star">*</label>)手机号码： </label></td><td>
				<input type="text" id="mobilephone2" name="mobilephone2" placeholder="手机号码"  class="required" minlength="11" maxlength="11"  onkeyup="this.value=this.value.replace(/[^\d]/g,'') " />
			</td>
		</tr>
		<tr>
			<td style="text-align: right;">
				<label class="  no-padding-right" for="zipcode" style="padding-top: 4px;" > 邮政编码： </label></td><td>
				<input type="text" id="zipcode" name="zipcode" placeholder="邮政编码" class=" " maxlength="8"   />
			</td>
			<td class=".form-group" style="text-align: right;">
				<label class="  no-padding-right" for="username3" style="padding-top: 4px;width: 90px;display: none;" >(<label class="star">*</label>) 用户名： </label></td><td>
				<input type="text" id="username3" name="username3" placeholder="用户名" class=""  style="display: none;"/>
			</td>
		</tr>
	
	</table>
	</form>
	</div>
</div>
<div id="roleWrapDIV" style="display: none;">
	<div class="row"  style="margin-bottom: 8px;text-align: left;"> 
	</div>
</div>
<!-- PAGE CONTENT ENDS -->
</body>
</html>
