<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
%>

<!DOCTYPE html>
<html>
<head>
<title><sitemesh:title/></title>
<meta name="renderer" content="webkit" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="${ctx}/resources/assets/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx}/resources/assets/css/font-awesome.min.css" />

<!-- page specific plugin styles -->
<link rel="stylesheet" href="${ctx}/resources/assets/css/jquery-ui.min.css" />
<link rel="stylesheet" href="${ctx}/resources/assets/css/datepicker.css" />
<link rel="stylesheet" href="${ctx}/resources/assets/css/ui.jqgrid.css" />

<!-- text fonts -->
<link rel="stylesheet" href="${ctx}/resources/assets/css/ace-fonts.css" />

<!-- ace styles -->
<link rel="stylesheet" href="${ctx}/resources/assets/css/ace.min.css" id="main-ace-style" />

<!--[if lte IE 9]>
	<link rel="stylesheet" href="${ctx}/resources/assets/css/ace-part2.min.css" />
<![endif]-->
<link rel="stylesheet" href="${ctx}/resources/assets/css/ace-skins.min.css" />
<link rel="stylesheet" href="${ctx}/resources/assets/css/ace-rtl.min.css" />
<link rel="stylesheet" href="${ctx}/resources/css/layout/header.css" />

<!--[if lte IE 9]>
  <link rel="stylesheet" href="${ctx}/resources/assets/css/ace-ie.min.css" />
<![endif]-->

<!-- inline styles related to this page -->

<!-- ace settings handler -->
<script src="${ctx}/resources/assets/js/ace-extra.min.js"></script>

<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

<!--[if lte IE 8]>
<script src="${ctx}/resources/assets/js/html5shiv.min.js"></script>
<script src="${ctx}/resources/assets/js/respond.min.js"></script>
<![endif]-->



<!-- basic scripts -->

<!--[if !IE]>-->
<script type="text/javascript">
	window.jQuery || document.write("<script src='${ctx}/resources/assets/js/jquery.min.js'>"+"<"+"/script>");
</script> 
<!-- <![endif]--> 
<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${ctx}/resources/assets/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->



<script type="text/javascript">
	if('ontouchstart' in document.documentElement) document.write("<script src='${ctx}/resources/assets/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>
<script src="${ctx}/resources/assets/js/bootstrap.min.js"></script>
<script src="${ctx}/resources/assets/js/bootstrap.autocomplete.js"></script>
<!-- page specific plugin scripts -->
<script src="${ctx}/resources/assets/js/date-time/bootstrap-datepicker.min.js"></script>
<script src="${ctx}/resources/assets/js/jqGrid/jquery.jqGrid.min.js"></script>
<script src="${ctx}/resources/assets/js/jqGrid/i18n/grid.locale-cn.js"></script>
<script src="${ctx}/resources/js/divResize.js"></script>
<script src="${ctx}/resources/js/sys/util.js"></script>

<!-- ace scripts -->
<script src="${ctx}/resources/assets/js/ace-elements.min.js"></script>
<script src="${ctx}/resources/assets/js/ace.min.js"></script>
<!-- commons -->
<!-- inline scripts related to this page -->

<sitemesh:head/>
</head>

<body class="skin-2">
		
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
			<sitemesh:body/>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	
<script type="text/javascript">
$(function(){
	$(window).resize(function(){
		$('html').width(getWindowWidth());
	});
});
</script>

<!--[if IE 9]> 
	<div class="amap-sug-result"></div>
<![endif]-->
</body>
</html>