<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
%>
<html>
<head>
<title>线路规划</title>
<meta name="description" content="地图慧-企业可视化管理平台" />
<meta name="keywords" content="地图慧-企业可视化管理平台" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<!-- page specific plugin styles -->
<!-- page specific plugin scripts -->
<!-- supermap -->

<!--<link href="${ctx}/resources/css/base-less.css" rel="stylesheet" type="text/css"/>-->
<link href="${ctx}/resources/css/pathplan/pathplan.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/resources/css/pathplan/pathplan-less.css" rel="stylesheet" type="text/css"/>
<!--<link href="${ctx}/resources/css/pathplan/setting.css" rel="stylesheet" type="text/css"/>-->

<script type="text/javascript" src="${ctx}/resources/js/Dituhui/iclient-8c/SuperMap.Include.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/Dituhui/dituhui.libs.min.js"></script>

<script src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${ctx}/resources/assets/js/bootbox.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.min.js"></script>
<script src="${ctx}/resources/assets/js/jquery.validate.message.cn.js"></script>
<script src="${ctx}/resources/assets/js/fuelux/fuelux.wizard.min.js"></script>

<script src="${ctx}/resources/js/pathplan/map.js"></script>
<script src="${ctx}/resources/js/pathplan/pathplan.js"></script>
<script src="${ctx}/resources/js/pathplan/setting.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/data_citys.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/data_provinces.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/data_town.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/map/supermap/libs7C/SMap/SMapCity.js"></script>
<script type="text/javascript" src="${ctx}/resources/js/config.js"></script>
<link href="${ctx}/resources/js/map/supermap/libs7C/SMap/SMapCity.css" rel="stylesheet" type="text/css"/>

<script type="text/javascript" >
	var PROJECT_URL='${ctx}';	
</script>

</head>

<body>
<div class="container-pathplan">
	<!-- PAGE CONTENT BEGINS -->
	<div id="map">
		<div class="map-copyright">&copy;2016 高德软件 GS(2016)710号</div>
		<div class="zoom-control">
			<span class="map-zoom"></span>
			<div class="zooms">
				<div class="zoom zoom-out"><div class="out"></div></div>
				<div class="zoom zoom-in"><div class="in"></div></div>
			</div>
		</div>
	</div>
		
	<!-- 顶部工具条 -->
	<div id="topToolbarDiv" class="header">
		<div class="widget-title smaller">
			<div id="selectCityDiv" class="popupSelectCity" ></div>
		</div>

	    <div class="tools">
	      <ul class="nav-tools">
	        <li>
	          <button type="button" id="btn_showHideRegion" class="showregion" onclick="showHideRegion()">
	          	显示区划
	          </button>
	        </li>
	        <li>
	          <a href="javascript:void(0);" class="tool-button" onclick="distanceMeasure()">
	            <img class="img-3" src="${ctx}/resources/css/imgs/nav-tools/measure.png">测距
	          </a>
	        </li>
	        <li>
	          <a href="javascript:void(0);" class="tool-button" onclick="deactivateMeasure()">
	            <img class="img-3" src="${ctx}/resources/css/imgs/nav-tools/pan.png">平移
	          </a>
	        </li>
	        <li>
	          <a href="javascript:void(0);" class="tool-button" onclick="clearMap()">
	            <img class="img-2" src="${ctx}/resources/css/imgs/nav-tools/clear.png">清空
	          </a>
	        </li>   
	      </ul>
	    </div>
	</div>

	<!-- 顶部路径规划条 -->
	<div class="toolbar-path" id="topToolbarPathDiv">
		<div class="btn-group">
			<div class="first step">				
				<label class="lead-button" id="selectNetBtnLabel" onclick="showNetPopDiv();">请选择起点</label>
				<!-- <i class="ace-icon fa fa-angle-down icon-on-right i-triangle"></i> -->
				<img class="i-guide" alt="" src="${ctx}/resources/images/to-next.png">

				<!-- 网点弹出框 -->
				<div class="widget-box widget-color-blue light-border popup" id="topToolbarPathDiv_net" >
					<div class="triangle-head">
						<div class="triangle"></div>
					</div>
					<div class="widget-header" style="border-bottom: 0px;backgroung-color: #0096FF;border:none;">
						<h4 class="widget-title smaller" id="branchlist_title">网点列表<label>(请在网点管理中导入网点数据)</label></h4>
				    	<div class="widget-toolbar no-border">
				    		<button  class="btn btn-minier btn-primary "  id="topToolbarPathDiv_net_ok" onclick="topToolbarPathDivNetOkClick()">
								确定
							</button>
				    	</div>
					</div>
					<div class="widget-body">
						<input type="hidden" id="net_id_step3" value="">
						<input type="hidden" id="net_name_step3" value="">
						<input type="hidden" id="net_coord_step3" value="">
						<input type="hidden" id="areaId_step1" value="">
						<input type="hidden" id="areaName_step1" value="">
						
						<table id="grid-table-step3"></table>
					 	<div id="grid-pager-step3" style="width: 378px !important;"></div>
					</div>
				</div>
			</div>

			<div class="second step">	
				<label class="lead-button" id="selectOrderBtnLabel" onclick="showOrderPopDiv();">请选择终点</label>
				<!-- <i class="ace-icon fa fa-angle-down icon-on-right i-triangle"></i> -->
				<img class="i-guide" alt="" src="${ctx}/resources/images/to-next.png">

				<!-- 订单弹出框 -->
				<div class="widget-box widget-color-blue light-border popup" id="topToolbarPathDiv_orders">
					<div class="triangle-head">
						<div class="triangle"></div>
					</div>
					<div class="widget-header" style="border-bottom: 0px;border:none;">
						<h4 class="widget-title smaller" id="orderlist_title">订单列表<label style="font-size: 10px;">(请在分单管理中导入订单地址)</label></h4>
				    	<div class="widget-toolbar no-border">
						   	<button  class="btn btn-minier btn-primary "  id="topToolbarPathDiv_orders_ok" onclick="topToolbarPathDivOrdersOkClick()">
								确定
							</button>
				    	</div>
					</div>
					<div class="widget-body">
						<table id="grid-table-step2"></table>
						<div id="grid-pager-step2" style="width: 378px !important;"></div>
					</div>
				</div>
			</div>

			<div class="third step">				
				<label class="lead-button" onclick="showSettingPopDiv();">相关设置</label>
				<!-- <i class="ace-icon fa fa-angle-down icon-on-right i-triangle"></i> -->
				<!-- 路线设置弹出框 -->
				<div id="topToolbarPathDiv_setting" class="widget-box widget-color-blue light-border popup">
					<div class="triangle-head">
						<div class="triangle"></div>
					</div>
					<div class="widget-header" style="border-bottom: 0px;border:none;">
						<h4 class="widget-title smaller" id="settinglist-title">相关设置</h4>
				    	<div class="widget-toolbar no-border">
						   	<button class="btn btn-minier btn-primary " onClick="closeSettingDiv()">
								确定
							</button>
				    	</div>
					</div>
					<div class="widget-body">
						<div id="topToolbarPathDiv_setting_content">
							<div class="form-group">
								<label class="no-padding-right forinput">*车辆出发时间： </label>
								<label class="batch-time-start">
									<select class="time-h">
										<option value="01">01</option>
									</select>
									<span class="lbl">时</span>
									<select class="time-m">
										<option value="01">01</option>
									</select>
									<span class="lbl">分</span>
								</label>
							</div>
							<div class="form-group">
								<label class="no-padding-right forinput"  style="padding-top: 4px;">*车辆返回时间： </label>
								<label class="batch-time-end">
									<select class="time-h">
										<option value="01">01</option>
									</select>
									<span class="lbl">时</span>
									<select class="time-m">
										<option value="01">01</option>
									</select>
									<span class="lbl">分</span>
								</label>
							</div>
							<div class="form-group">
								<label class="no-padding-right forinput" for="txt_waste_time" style="padding-top: 4px;">非行驶消耗时间
									<span class="ask-tooltip" data-toggle="tooltip" data-placement="top" title="非行驶消耗时间：指实际配送时在路上全部的非行驶消耗时间。非行驶消耗时间=实际配送时间-路上行驶时间。可以包括路上停靠时间、搬卸货时间等。">
										<sup>?</sup>
									</span>
									: 
								</label>
								<input class="text-setting" type="text" id="txt_waste_time" placeholder="非行驶消耗时间">
								<span class="lbl">分钟</span>
							</div>
							
							<div class="form-group">
								<label class="no-padding-right forinput" for="txt_car_load" style="padding-top: 4px;">线路最大承载订单数
									<span class="ask-tooltip" data-toggle="tooltip" data-placement="top" title="线路最大承载订单数：指每条线路上（即每辆车）能够承载的最多订单数量。假设每个订单的重量为一个标准值，线路最大承载订单数=每辆车的载重/每个订单的重量。系统在计算时，将默认不会超过这里设置的最大承载数。">
										<sup>?</sup>
									</span>
									: 
								</label>
								<input class="text-setting" type="text" id="txt_car_load" placeholder="线路最大承载订单数" val="">
								<span class="lbl">单</span>
							</div>
							
							<div class="form-group">
								<label class="no-padding-right forinput"  style="padding-top: 4px;"> 线路模式： </label></td><td>
								<label >
									<input id="noneGroup" name="groupType" type="radio" value="0" class="ace" checked="checked">
									<span class="lbl" > 常规模式</span>
								</label>
								<label>
									<input id="radialGroup" name="groupType" type="radio" value="1" class="ace" >
									<span class="lbl"> 放射线路</span>
								</label>
							</div>
							<div class="form-group">
								<label class="no-padding-right forinput"  style="padding-top: 4px;"> 分析模式： </label></td><td>
								<label >
									<input id="lengthWeightNameType" name="weightNameType" type="radio" value="0" class="ace" checked="checked">
									<span class="lbl" > 路程最短</span>
								</label>
								<label>
									<input id="timeWeightNameType" name="weightNameType" type="radio" value="1" class="ace" >
									<span class="lbl"> 时间最短</span>
								</label>
							</div>
							<div class="form-group">
								<label class="no-padding-right forinput"  style="padding-top: 4px;"> 货车模式： </label></td><td>
								<label >
									<input id="directionDirectionType" name="directionType" type="radio" value="0" class="ace" checked="checked">
									<span class="lbl" > 全通模式</span>
								</label>
								<label>
									<input id="truckDirectionDirectionType" name="directionType" type="radio" value="1" class="ace" >
									<span class="lbl"> 货车通行</span>
								</label>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="go-pathplan" id="goPathPlanBtn"  onclick="submitPathPlanJob()">规划路线</div>
		</div>
	</div>


	<!-- 车辆弹出框 -->
	<div id="topToolbarPathDiv_cars" class="widget-box widget-color-blue light-border" style="position: absolute;left: 30%;top: 78px;z-index: 2;width:380px;display:none;">
		<div class="widget-header" style="border-bottom: 0px;">
			<h4 class="widget-title smaller" id="carlist-title">车辆列表</h4>
	    	<div class="widget-toolbar no-border">
				<button  class="btn btn-minier btn-primary "  id="topToolbarPathDiv_cars_ok" onclick="topToolbarPathDivCarsOkClick()">
					确定
				</button>
	    	</div>
		</div>
		<div class="widget-body">
			<table id="grid-table-step1"></table>
			<div id="grid-pager-step1" style="width: 378px !important;"></div>
		</div>
	</div>

	<!-- 路线规划表格 -->
	<div class="widget-box collapsed widget-color-blue light-border data-list" 
		id="bottomToolbarPathDiv_path" >
		<div class="widget-header">
			<h4 class="widget-title smaller">路线规划列表</h4>
	    	<div class="widget-toolbar no-border">
			    <a class="collapse-table" href="#"><i class="ace-icon fa fa-chevron-up"></i></a>
	    	</div>
		</div>
		<div class="widget-body">
			<table id="grid-table"></table>
			<div id="grid-pager" style="width: 100px;"></div>
		</div>
	</div>


	<!-- 路线信息窗口 -->
	<div id="pathResultWindow" class="widget-box collpased widget-color-blue light-border pathResultWindow">
	   <div class="widget-header path-header" >
	   		<h4 class="widget-title smaller" id="roadlist_title" title="点击查看全部路线">路线规划结果<span class="count-paths"></span></h4>
	   		<div class="widget-toolbar no-border" >
			   	<a data-action="collapse" href="#"><i class="ace-icon fa fa-chevron-up"></i></a>
			   	<i class="ace-icon fa fa-times" style="cursor: pointer;" onClick="javascript:this.parentNode.parentNode.parentNode.style.display='none'" title="关闭"></i>
	   		</div>
	   </div>
	   <div class="widget-body" style="border-color: #307ECC;">
	   		<div class="pathResultWindowBodyMain" id="pathResultWindowBodyMain">
	   			<div id="pathDivTitleWraper">	   				
			   		<div id="pathDivContentWraper"></div>
	   			</div>
		   		
		   		<div id="pathDivInfoesTitle" class="pathDivTitle">		   			
		   			<ul>
		   				<li class="action" data-target=".orders-detail">订单详情</li>
		   				<li class="normal" data-target=".paths-detail">路线信息</li>
		   			</ul>
		   			<a class="print-orders" href="javascript:void(0);" onClick="printOrders()">打印</a>
				</div>
		   		<div id="pathInfoesContent" class="pathInfoesContent">
		   			<div class="content orders-detail"></div>
		   			<div class="content paths-detail">
		   				<p>点击上面的路线按钮查看具体的路线信息</p>
		   			</div>
					</div>	
		  	</div>
	   </div>
	</div>
	
	<!-- 用户引导 -->
	<div class="user-guide">
		<div class="step1">
			<button class="btn btn-primary btn-white ">
				<label id="selectNetBtnLabel">请选择起点</label>
				<i class="ace-icon fa fa-angle-down icon-on-right"></i>
			</button>
			<div class="img-word">				
			</div>
			<div class="next-step">				
			</div>
		</div>
		<div class="step2">
			<button class="btn btn-primary btn-white ">
				<label id="selectNetBtnLabel">请选择终点</label>
				<i class="ace-icon fa fa-angle-down icon-on-right"></i>
			</button>
			<div class="img-word">				
			</div>
			<div class="next-step">				
			</div>
		</div>
		<div class="step3">
			<button class="btn btn-primary btn-white ">
				<label id="selectNetBtnLabel">规划路线</label>
			</button>
			<div class="img-word">				
			</div>
			<div class="next-step">				
			</div>
		</div>
		<div class="step4">
			<button class="btn btn-primary btn-white ">
				<i class="ace-icon fa fa-cog icon-on-right"></i>
			</button>
			<div class="img-word">				
			</div>
			<div class="next-step">				
			</div>
		</div>

		<div class="step5">	
			<div class="img-word">				
			</div>
			<div class="start">				
			</div>
			<div class="widget-box collapsed widget-color-blue  light-border" style="width: 230px; height: 40px; z-index: 2;">
				<div class="widget-header">
					<h4 class="widget-title smaller">路线规划列表</h4>
			    	<div class="widget-toolbar no-border">
					    <a data-action="collapse" href="#"><i class="ace-icon fa fa-chevron-up"></i></a>
			    	</div>
			    </div>
			</div>
		</div>

	</div>
	
	<!--线路规划设置 选取起点终点等-->
	<div class="route-setting hide">
		<div class="box">
			<div class="get start">
				<input id="txt_setting_start" value="" placeholder="起点" readonly="readonly">
			</div>
			<div class="get passing">
				<input id="txt_setting_passing" value="" placeholder="途经点" readonly="readonly">
			</div>
			<div class="con set">
				<a class="btn btn-set-param">相关设置</a>
			</div>
			<div class="con do">
				<a class="btn btn-analyst-route">规划路线</a>
			</div>
		</div>
		
		<!--选择起点-->
		<div class="panel start hide">
			<div class="title"><span>选择起点</span></div>
			<div class="content">
				<div class="search-part">
					<select class="select-route start" id="select_route_start">
						<option value="1">网点名称</option>
						<option value="2">网点编号</option>
						<option value="3">POI关键词</option>
						<option value="4">规范地址</option>
					</select>
					<input class="search-route start" id="txt_search_rstart" value=""
						placeholder="">
					<a class="btn btn-success btn-sm btn-search-route start">查询</a>
				</div>
				
				<ul class="result-part start">
					<li>
						<div class="badge">1</div>
						<div class="left">
							<span class="name">超图软件</span>
							<span class="address">北京市朝阳区酒仙桥路</span>
						</div>
					</li>
					<li>
						<div class="badge">2</div>
						<div class="left">
							<span class="name">超图软件</span>
							<span class="address">北京市朝阳区酒仙桥路</span>
						</div>
					</li>
					<li>
						<div class="badge">3</div>
						<div class="left">
							<span class="name">超图软件</span>
							<span class="address">北京市朝阳区酒仙桥路</span>
						</div>
					</li>
					<li>
						<div class="badge">4</div>
						<div class="left">
							<span class="name">超图软件</span>
							<span class="address">北京市朝阳区酒仙桥路</span>
						</div>
					</li>
					<li>
						<div class="badge">5</div>
						<div class="left">
							<span class="name">超图软件</span>
							<span class="address">北京市朝阳区酒仙桥路</span>
						</div>
					</li>
				</ul>
				
				<div id="data_pager_rstart" class="content-pager left" page="0">
		          <ul class="pagination pagination-sm" id="ul_pager_users">
		          	<li class="disabled" title="第一页"><a href="javascript:;">«</a></li>
		          	<li class="active"><a href="javascript:;">1</a></li>
		          	<li><a href="javascript:;">2</a></li>
		          	<li title="最后一页" class="disabled"><a href="javascript:;">»</a></li>
		          </ul>
		        </div>
			</div>
		</div>
		
		<!--选择途经点-->
		<div class="panel passing hide">
			<div class="title"><span>选择途经点</span></div>
			<div class="content">
				<div class="search-part">
					<select class="select-route passing">
						<option value="1">网点分组</option>
						<option value="2">区划名称</option>
						<option value="3">订单批次</option>
						<option value="4">单条地址</option>
						<option value="4">POI关键词</option>
					</select>
					<input class="search-route passing" value=""
					<a class="btn btn-success btn-sm btn-search-route start">查询</a>
				</div>
				
				<div id="data_pager_rpassing" class="content-pager left" page="0">
		          <ul class="pagination pagination-sm" id="ul_pager_users">
		          	<li class="disabled" title="第一页"><a href="javascript:;">«</a></li>
		          	<li class="active"><a href="javascript:;">1</a></li>
		          	<li><a href="javascript:;">2</a></li>
		          	<li title="最后一页" class="disabled"><a href="javascript:;">»</a></li>
		          </ul>
		        </div>
			</div>
		</div>
	</div>
	
	
  <!-- 底部提示 -->
  <div id="popover_result" class="popover-result">
    <div class="popover-result-content" id="popover_content"></div>
  </div>

  <!-- 顶部提示 -->
  <div id="popover_hint" class="popover-hint">
    <div class="popover-hint-content"><a id="popover_hint_content" href="javascript:void(0);"></a></div>
  </div>

  <!-- 正在加载 -->
  <div class="mask-loading-text">
    <div class="hint"><a href="javascript:void(0);"></a></div>
  </div>
  
  <!-- 正在加载 -->
  <div class="mask-loading">
    <div class="box">
      <div class="loader"></div>      
    </div>
  </div>
</div>
<script type="text/javascript" src="${ctx}/resources/js/public/dth.map.js"></script>
</body>
</html>
