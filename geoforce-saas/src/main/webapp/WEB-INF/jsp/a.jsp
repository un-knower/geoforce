<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	request.setAttribute("basePath", basePath);
%>
<html>
<head>
<title>地图慧-企业可视化管理平台</title>
<meta name="description" content="地图慧-企业可视化管理平台" />
<meta name="keywords" content="地图慧-企业可视化管理平台" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
html,body{
	margin: 0px;
	padding: 0px;
	font-size: 12;
	font-family: 微软雅黑;
}
</style>

</head>

<body>
<h1>test-主页a.jsp</h1>
<img alt="测试图片" src="${basePath }resources/Flexigrid/css/images/magnifier.png">

</body>
</html>
