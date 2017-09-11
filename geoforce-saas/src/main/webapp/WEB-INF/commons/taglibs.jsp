<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<c:set var="ctxp" value="${pageContext.request.contextPath}"/>
<c:set var="egisTitle" value="地图慧-企业可视化管理平台" />
<c:choose>
	<c:when test="${pageContext.request.serverPort eq 80}">
		<c:set var="base"
			value="${pageContext.request.scheme}://${pageContext.request.serverName}${pageContext.request.contextPath}" />
	</c:when>
	<c:otherwise>
		<c:set var="base"
			value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}" />
	</c:otherwise>
</c:choose>
<c:set var="actionExt" value="html"/>
<script type="text/javascript">
	var basePath = "${base}";
	var actionExt ="${actionExt}";
	var ctx = "${ctxp}";
	var ps_cityName = "北京市";//个性化设置 城市名
	var ps_cityCode = "100100";//个性化设置 城市编码
	var ps_mapType = "supermap";//个性化设置 地图类型
	var ps_lng = "116.39808655";//个性化设置 经度
	var ps_lat = "39.90973623";//个性化设置 纬度
</script>
<link href="${base }/resources/css/common.css" type="text/css" rel=stylesheet>
<script src="${base}/resources/js/common.js"></script>