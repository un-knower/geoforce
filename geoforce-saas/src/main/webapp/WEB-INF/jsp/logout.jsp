<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
<title>地图慧-企业可视化管理平台</title>
<meta name="description" content="地图慧-企业可视化管理平台" />
<meta name="keywords" content="地图慧-企业可视化管理平台" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">

</style>

</head>

<body>
<script type="text/javascript">
location.href="${portalUrl}";
</script>

</body>
</html>
