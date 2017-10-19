<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="icon" href="http://v3.bootcss.com/favicon.ico">
	
	<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	
	<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
	<!-- <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"> -->
	
	<link rel="stylesheet" href="resources/css/distribute/distributeDemo.css">
  </head>
  
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Geoforce</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">智能分单-示例</a></li>
            <li><a href="#">智能分单-免费</a></li>
            <li><a href="#">智能分单-正式</a></li>
            <li><a href="#">业务区管理</a></li>
            <li><a href="#">线路规划</a></li>
            <li><a href="#">车辆监控</a></li>
          </ul>
        </div>
      </div>
    </nav>

	<div class="content">
		<div class="leftPanel">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">单条分单</h3>
				</div>
				<div class="panel-body">introduce</div>
				<ul class="list-group">
					<li class="list-group-item">
						<form class="form-inline">
							<div class="form-group">
								<input type="text" class="form-control" id="address" placeholder="请输入地址">
							</div>
							<button type="submit" class="btn btn-primary">分单</button>
						</form>
					</li>
					<li class="list-group-item">
						<strong>分单结果：</strong>xxxx
					</li>
				</ul>
			</div>
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">批量分单</h3>
				</div>
				<div class="panel-body">introduce</div>
				<ul class="list-group">
					<li class="list-group-item">
						<form class="form-inline">
							<button type="submit" class="btn btn-primary">模版下载</button>
							<div class="form-group">
								<input type="file" id="file">
							</div>
							<button type="submit" class="btn btn-primary">上传</button>
						</form>
					</li>
				</ul>
				<table class="table table-striped table-hover">
					<tr><th>地址</th><th>业务区</th></tr>
					<tr><td>1</td><td>1</td></tr>
					<tr><td>2</td><td>2</td></tr>
					<tr><td>3</td><td>3</td></tr>
					<tr><td>4</td><td>4</td></tr>
					<tr><td>5</td><td>5</td></tr>
				</table>
				<nav class="pageBar">
					<ul class="pagination">
						<li><a href="#"><span>&laquo;</span></a></li>
						<li><a href="#">1</a></li>
						<li><a href="#">2</a></li>
						<li><a href="#">3</a></li>
						<li><a href="#">4</a></li>
						<li><a href="#">5</a></li>
						<li><a href="#"><span>&raquo;</span></a></li>
					</ul>
				</nav>
			</div>
		</div>
	    <div id="map"></div>
    </div>
    
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="resources/assets/jquery/jquery.min.js"></script>
	<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.0&key=c09e2be02abcd2e7d386eceaffc9b96a"></script>
	<script src="http://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
	<script type="text/javascript" src="resources/js/distribute/distributeDemo.js"></script>
	
  </body>
</html>
