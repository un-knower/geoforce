<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title><sitemesh:write property='title' /></title>
    
    <!-- SEO -->
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="resources/css/common.css">
  <link rel="stylesheet" type="text/css" href="resources/css/consoleHeader.css">

	<sitemesh:write property='head' />
  </head>
  
  <body>
 	<%@ include file="consoleHeader.jsp" %>
  	<%-- <%@ include file="consoleLeft.jsp" %> --%>
    <sitemesh:write property='body' />
  </body>
</html>
