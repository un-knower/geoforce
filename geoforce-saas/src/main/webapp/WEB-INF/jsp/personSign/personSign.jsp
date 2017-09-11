<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
   	<title>员工巡店管理</title>
	<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
		<meta content="IE=edge,Chrome=1" http-equiv="X-UA-Compatible">
  </head>
  <body>
  	<!--查询框  -->
  		<nav id="monitorBtns" class="navbar navbar-default nav-white" role="navigation" style="background-color: #e7e7e7;">
			  <div class="container-fluid">
			    <div class="navbar-header">
			      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
			        <span class="sr-only">Toggle navigation</span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			        <span class="icon-bar"></span>
			      </button>
				 </div>
		   		 <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1" >
			     	<form id="carSeeping_list" class="navbar-form navbar-left" role="search">
			     		<label class="  no-padding-right" for="license" style="padding-top: 4px;" >门店名称： </label>
			     	   <div class="form-group">
			     		<input type="text" id="name" name="name" placeholder="请输入名称" class=" " maxlength="10"/>
			     	  </div>
			     	  <label class="no-padding-right" for="license" style="padding-top: 4px;" >时间： </label>
			     	   <div class="form-group">
			     		<input type="text" id="begindate" name="begindate"  placeholder="签到开始时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
						<input type="text" id="enddate" name="enddate"  placeholder="签到结束时间"  onClick="WdatePicker({dateFmt:'yyyy-MM-dd'})"/>
			     	  </div>
			     	  <label class="no-padding-right" for="starttime" style="padding-top: 4px;"> 状态： </label>
					  <div class="form-group">
					    <select name="status" id="status">
					    	<option value="" >全部</option>
							<option value="0" >未完成</option>
							<option value="1" >正在巡店</option>
							<option value="2" >完成</option>
						</select>
					  </div>
					  <button type="button" class="btn btn-purple btn-sm" onclick="reload();"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
					</form>
		    	</div>
			  </div>
			</nav>	
		<table id="grid-table" ></table>
		<div id="grid-pager" style="height: 40px" ></div>
<!--门店照片  -->		
<div class="modal fade bs-example-modal-sm" id="storeImg" tabindex="-1" role="dialog" aria-labelledby="storeImgLabel" aria-hidden="true">
   <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="storeImgLabel">门店图片</h4>
      </div>
      <div id="bindRiverBody" class="modal-body">
      	<div id="myCarousel" class="carousel slide">
		  <!-- Carousel items -->
		 	<div id="imgbody" class="carousel-inner">
		   </div>
		  <!-- Carousel nav -->
		  <a class="carousel-control left" href="#myCarousel" data-slide="prev">‹</a>
		  <a class="carousel-control right" href="#myCarousel" data-slide="next">›</a>
		</div>
	   </div>  
    </div>
  </div>
</div>
		<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
		<script src="${base}/resources/assets/js/bootbox.min.js"></script>
		<script src="${base}/resources/js/sys/util.js"></script>
		<script src="${base}/resources/js/personSign/personSign.js"></script>
			<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>	
  </body>
</html>
