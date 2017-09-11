<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
request.setAttribute("ctx", basePath);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta name="description" content="地图慧-企业可视化管理平台" />
  <meta name="keywords" content="地图慧-企业可视化管理平台" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">  
  <title>地图慧-订单量统计</title>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/statistics/order.css">
</head>

<body>
  <div class="container-st-order"> 
  	
  	<div class="tab-sum">
  		
  		<div class="sum-head">
  			<div class="title"><span class="back"></span>全国订单量统计</div>
  		</div>
  		
  		<div class="sum-left">
  			<div class="content-during">
	        <ul class="during">
	          <li class="first action using" data-time="day">今日</li>
	          <li class="normal" data-time="week">7日内</li>
	          <li class="normal" data-time="month">30日内</li>
	          <li class="last" data-show-date="true">自定义</li>
	        </ul>
	        <div class="custom-during">	        	
  					<div class="arrow-up"></div>
	          <input class="input-during" name="start" 
	          	type="text" readonly="true" placeholder="请选择开始时间">
	          <span>至</span>
	          <input class="input-during" name="end" type="text" 
	          	readonly="true" placeholder="请选择结束时间">
	          <a class="button-search" href="javascript:void(0);">搜索</a>
	        </div>     
	        
	      </div>
	      
	      <div class="sum-content">
	      	<div class="sum-detail">
		      	<div class="title">订单量总数</div>
		      	
		      	<div class="the-number">0</div>
		      	<div class="the-time"></div>
		      </div>
	  		
		      <div class="sum-chart" >
		      	<div class="chart-content" id="sum_chart"></div>
		      </div>
	      </div>      
	      
  		</div>
  	</div>
  	
  	<div class="tab-map hide">
  		<div class="console-box">
	      <div class="title">订单量统计</div>
	      <div class="content-during">
	        <ul class="during">
	          <li class="first" data-time="day">今日</li>
	          <li class="normal" data-time="week">7日内</li>
	          <li class="normal" data-time="month">30日内</li>
	          <li class="last" data-show-date="true">自定义</li>
	        </ul>
	        <div class="custom-during">
	          <input class="input-during" name="start" 
	          	type="text" readonly="true" placeholder="请选择开始时间">
	          <span>至</span>
	          <input class="input-during" name="end" 
	          	type="text" readonly="true" placeholder="请选择结束时间">
	          <a class="button-search" href="javascript:void(0);">搜索</a>
	        </div>             
	      </div> 
	      <div class="data-list-group">
	        <ul class="list-group">
	        </ul>
	      </div>
	      <div class="console-foot">
	      	<span class="sum-number"></span>
	      	<a class="btn btn-success btn-sm btn-export hide" href="javascript:;">导出</a>
	      </div>
	    </div>	    
	    
	    <div class="map-box">
	      <div class="toolbox">
	        <div class="smcity">
			      <a href="javascript:void(0);" class="current-city">
			      	<span class="text">北京市</span><span class="caret"></span>
			      </a>
			      <div class="smcity-content hide">
			        <div class="smcity-title-top">
			          <span>城市列表</span>
			          <span class="show-default-city"></span>
			          <button class="close" type="button" data-target=".smcity-content">
			          	<span aria-hidden="true">&times;</span>
			          </button>
			        </div>
			        <div class="smcity-title">
			          <span>当前位置：</span>
			          <a href="javascript:void(0);" option="showWholeCountry">全国</a>
			
			          <span class="province level-1 level-2 level-3 hide" >></span>
			          <a class="province level-1 level-2 level-3 hide" option="toProvince" href="javascript:void(0);">
			          </a>
			
			          <span class="city level-3 level-2 level-city hide">></span>
			          <a class="city level-3 level-2 level-city hide" option="toCity" href="javascript:void(0);"></a>
			
			          <span class="county level-3 hide">></span>
			          <a  class="county level-3 hide" href="javascript:void(0);"></a>
			        </div>
			        <div class="line"></div>
			        <div class="all-provinces hide"></div>
			        <div class="child-citys hide"></div>
			      </div>
			    </div>
			    
			    <button type="button" class="close close-map" show-target=".tab-sum">
						<span aria-hidden="true">&times;</span>
						<span class="sr-only">Close</span>
					</button>
	      </div>
	      <div class="map" id="map"> 
	      </div>      
	    </div>

  	</div>
  	
  	
		<div class="tab-tabel hide">
			<div class="search-area">
				<label>
					订单编号:
					<input id="txt_search_ordernumber" type="text" value="" placeholder="请输入订单编号">
				</label>
				
				<label>
					订单批次:
					<input id="txt_search_orderbatch" type="text" value="" placeholder="请输入订单批次">
				</label>
				
				<label>
					详细地址:
					<input id="txt_search_orderaddress" type="text" value="" placeholder="请输入详细地址">
				</label>
				
				<a class="btn btn-info btn-searchorder btn-sm">查询</a>
				
				<button type="button" class="close close-table" show-target=".tab-map">
					<span aria-hidden="true">&times;</span>
					<span class="sr-only">Close</span>
				</button>
			</div>
			
			<div class="widget-box widget-color-blue light-border">
				<div class="widget-body">
					<table id="grid-table"></table>
					<div id="grid-pager" style="width: 100px;"></div>
				</div>
			</div>
		</div>
    
    <!-- 底部提示 -->
	  <div id="popover_result" class="popover-result">
	    <div class="popover-result-content" id="popover_content"></div>
	  </div>
	
	  <!-- 顶部提示 -->
	  <div id="popover_hint" class="popover-hint">
	    <div class="popover-hint-content">
	    	<a id="popover_hint_content" href="javascript:void(0);"></a>
	    </div>
	  </div>

    <!-- 正在加载 -->
    <div class="mask-loading">
      <div class="box">
        <div class="loader"></div>      
      </div>
    </div>
  </div>

	<script type="text/javascript" src="${ctx}/resources/js/Dituhui/iclient-8c/SuperMap.Include.js"></script>
	<script type="text/javascript" src="${ctx}/resources/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" 		src="${ctx}/resources/assets/js/echarts/echarts.common.min.js"></script>
  <script src="${ctx}/resources/js/Dituhui/dituhui.libs.min.js"></script>
  
  <script src='http://dev.dituhui.com/sdk/1.0.0/js/dituhui-flash.js'></script>
  <script src='http://dev.dituhui.com/sdk/1.0.0/js/sample-data.js'></script>
  
	<script type="text/javascript" src="${ctx}/resources/js/config.js"></script>
  <script src="${ctx}/resources/js/statistics/order/map.js"></script>
  <script src="${ctx}/resources/js/statistics/order/page.js"></script>
  <script src="${ctx}/resources/js/statistics/order/sumchart.js"></script>
</body>
</html>
