<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath() + "/";
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
	
	<base href="<%=basePath%>">
	
	<link rel="icon" href="http://v3.bootcss.com/favicon.ico">
	
	<!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
	<link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	
	<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
	<!-- <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"> -->
	
	<link rel="stylesheet" href="resources/css/distribute/demo.css">
  </head>
  
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <a class="navbar-brand" href="javascrip:void(0);">Geoforce</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="distribute/demo">智能分单-示例</a></li>
            <li><a href="distribute/free">智能分单-免费<span class="label label-primary theLabel">New</span></a></li>
            <li><a href="javascrip:void(0);" data-toggle="popover" data-trigger="focus" data-placement="bottom" title="提示" data-content="即将上线...">智能分单-正式</a></li>
            <li><a href="javascrip:void(0);" data-toggle="popover" data-trigger="focus" data-placement="bottom" title="提示" data-content="即将上线...">业务区管理</a></li>
            <li><a href="javascrip:void(0);" data-toggle="popover" data-trigger="focus" data-placement="bottom" title="提示" data-content="敬请期待...">线路规划</a></li>
            <li><a href="javascrip:void(0);" data-toggle="popover" data-trigger="focus" data-placement="bottom" title="提示" data-content="敬请期待...">车辆监控</a></li>
          </ul>
          <ul class="nav navbar-nav navbar-right">
          	<p class="navbar-text">客服QQ:405820699</p>
          </ul>
        </div>
      </div>
    </nav>

	<div class="content">
		<div class="panel-group leftPanel" id="leftPanel">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<a href="#singleTitle" data-toggle="collapse" data-parent="#leftPanel">单条分单</a>
					</h3>
				</div>
				<div id="singleTitle" class="panel-collapse collapse in">
					<div class="panel-body">
						<p>
							<span class="glyphicon glyphicon-pushpin"></span> <span class="label label-default">业务前提</span><br>
							假设地图上的“业务区A”、“业务区B”、“业务区C”是您的业务区块。
						</p>
						<p>
							<span class="glyphicon glyphicon-th-list"></span> <span class="label label-default">操作步骤</span><br>
							1.输入地址，如：四川省成都市青羊区天府中心；<br>
							2.点击“分单”按钮或回车；<br>
							3.查看分单结果。
						</p>
						<p>
							<span class="glyphicon glyphicon-comment"></span> <span class="label label-default">操作提示</span><br>
							1.点击“重置”按钮能回到初始状态；<br>
							2.点击左侧面板的“单条分单”和“批量分单”可在两个面板之间切换。
						</p>
						<p>
							<span class="glyphicon glyphicon-ok"></span> <span class="label label-default">正式版</span><br>
							1.可自定义业务区；<br>
							2.可下载分单结果；<br>
							3.可放宽上传条数的限制；<br>
							4.可使用全国四级行政区数据；<br>
							5.可定制接口对接您的业务系统。
						</p>
					</div>
					<ul class="list-group">
						<li class="list-group-item">
							<form class="form-inline">
								<div class="form-group">
									<input type="text" class="form-control" id="address" placeholder="请输入地址">
								</div>
								<button type="button" class="btn btn-primary" id="singleDistribute">分单</button>
								<button type="button" class="btn btn-success" id="reset">重置</button>
							</form>
						</li>
						<li class="list-group-item"><strong>分单结果：</strong><span id="result">无</span></li>
					</ul>
				</div>
			</div>
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<a href="#batchTitle" data-toggle="collapse" data-parent="#leftPanel">批量分单</a>
					</h3>
				</div>
				<div id="batchTitle" class="panel-collapse collapse">
					<div class="panel-body">
						<p>
							<span class="glyphicon glyphicon-pushpin"></span> <span class="label label-default">业务前提</span><br>
							假设地图上的“业务区A”、“业务区B”、“业务区C”是您的业务区块。
						</p>
						<p>
							<span class="glyphicon glyphicon-th-list"></span> <span class="label label-default">操作步骤</span><br>
							1.下载模版；<br>
							2.打开模版，按照格式录入您的订单地址（示例与免费版的订单地址不超过50条）；<br>
							3.选择修改后的模板文件；<br>
							4.点击“上传”按钮；<br>
							5.查看分单结果。
						</p>
						<p>
							<span class="glyphicon glyphicon-comment"></span> <span class="label label-default">操作提示</span><br>
							点击表格记录，可快速定位到地图标注。
						</p>
						<p>
							<span class="glyphicon glyphicon-ok"></span> <span class="label label-default">正式版</span><br>
							1.可自定义业务区；<br>
							2.可下载分单结果；<br>
							3.可放宽上传条数的限制；<br>
							4.可使用全国四级行政区数据；<br>
							5.可定制接口对接您的业务系统。
						</p>
					</div>
					<ul class="list-group">
						<li class="list-group-item">
							<form class="form-inline" id="form">
								<button type="button" class="btn btn-primary" id="download">模版下载</button>
								<div class="form-group">
									<input type="file" id="file" name="file">
								</div>
								<button type="button" class="btn btn-primary" id="upload">上传</button>
							</form>
						</li>
					</ul>
					<table class="table table-striped table-hover table-bordered" id="table">
						<tr>
							<th>序号</th>
							<th>订单地址</th>
							<th>业务区</th>
						</tr>
					</table>
					<div id="pageBar">
						<ul></ul>
					</div>
				</div>
			</div>
		</div>
		
		<!-- <div class="leftPanel">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">单条分单</h3>
				</div>
				<div class="panel-body">
					<span class="glyphicon glyphicon-info-sign"></span> <span class="label label-default">前提</span>
					假设地图上的“业务区A”、“业务区B”、“业务区C”是您的业务区块。<br>
					<span class="glyphicon glyphicon-th-list"></span> <span class="label label-default">操作步骤</span>
					1.输入地址，如：四川省成都市青羊区天府中心。2.点击“分单”按钮或回车。3.查看分单结果。<br>
					<span class="glyphicon glyphicon-ok"></span> <span class="label label-default">正式版</span>
					1.可自定义业务区；2.可下载分单结果；3.可开放上传限制；4.可使用全国四级行政区数据；5.可定制接口对接您的业务系统。
				</div>
				<ul class="list-group">
					<li class="list-group-item">
						<form class="form-inline">
							<div class="form-group">
								<input type="text" class="form-control" id="address" placeholder="请输入地址">
							</div>
							<button type="button" class="btn btn-primary" id="singleDistribute">分单</button>
							<button type="button" class="btn btn-success" id="reset">重置</button>
						</form>
					</li>
					<li class="list-group-item">
						<strong>分单结果：</strong><span id="result">无</span>
					</li>
				</ul>
			</div>
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">批量分单</h3>
				</div>
				<div class="panel-body">
					<span class="glyphicon glyphicon-info-sign"></span> <span class="label label-default">前提</span>
					假设地图上的“业务区A”、“业务区B”、“业务区C”是您的业务区块。<br>
					<span class="glyphicon glyphicon-th-list"></span> <span class="label label-default">操作步骤</span>
					1.下载模版。2.打开模版，按照格式录入您的订单地址。3.选择修改后的模板文件。4.点击“上传”按钮。5.查看分单结果。<br>
					<span class="glyphicon glyphicon-ok"></span> <span class="label label-default">正式版</span>
					1.可自定义业务区；2.可下载分单结果；3.可开放上传限制；4.可使用全国四级行政区数据；5.可定制接口对接您的业务系统。
				</div>
				<ul class="list-group">
					<li class="list-group-item">
						<form class="form-inline" id="form">
							<button type="button" class="btn btn-primary" id="download">模版下载</button>
							<div class="form-group">
								<input type="file" id="file" name="file">
							</div>
							<button type="button" class="btn btn-primary" id="upload">上传</button>
						</form>
					</li>
				</ul>
				<table class="table table-striped table-hover table-bordered" id="table">
					<tr><th>序号</th><th>订单地址</th><th>业务区</th></tr>
				</table>
				<div id="pageBar">
					<ul></ul>
				</div>
			</div>
		</div> -->
	    
	    <div id="map"></div>
    </div>
    
    <div class="modal fade" id="tipWin">
  		<div class="modal-dialog modal-sm">
	    	<div class="modal-content">
	    		<div class="modal-header">
			        <h4 class="modal-title">提示</h4>
			  	</div>
			   	<div class="modal-body">
		    		<p>上传失败！您的模版中订单地址数量超过50条。开通正式版可放宽上传条数的限制。</p>
			  	</div>
		    	<div class="modal-footer">
			        <button type="button" class="btn btn-default">关闭</button>
		  		</div>
	    	</div>
  		</div>
	</div>
    
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script type="text/javascript" src="resources/assets/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="resources/assets/jquery/jquery.form.js"></script>
	<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<script type="text/javascript" src="resources/assets/bootstrap/bootstrap-paginator.min.js"></script>
	<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.4.0&key=c09e2be02abcd2e7d386eceaffc9b96a"></script>
	<script src="http://webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
	<script type="text/javascript" src="resources/js/distribute/demo.js"></script>
	<script type="text/javascript">
		var basePath = "<%=basePath%>";
	</script>
  </body>
</html>
