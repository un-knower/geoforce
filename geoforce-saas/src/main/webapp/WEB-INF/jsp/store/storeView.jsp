<%@ page language= "java" contentType ="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<%@ include file="/WEB-INF/commons/taglibs.jsp"%>
<title>门店设置</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<style type="text/css">
.trackBox {
	position: absolute;
    background: none repeat scroll 0 0 #f8f8f8;
    border: 1px solid #fff;
    border-radius: 3px;
    box-shadow: 0 1px 5px #666;
    font-size: 12px;
   
}
.trackBox img{
 cursor: pointer;
    height: 25px;
    margin: 12px;
    width: 25px;
}
</style>

</head>
<body>
<div class="page-content-area">

	<div class="row">
			<div class="container-fluid">
				<div id="mapDiv_supermap" class="col-xs-12" style="height: 100%;"></div>
				<div id="mapDiv_baidu" class="col-xs-12" style="height: 100%;border: 1px #3473B7 solid;"></div>
				<div id="mapDiv_google" class="col-xs-12" style="height:100%;border: 1px #3473B7 solid;"></div>
			</div>
	</div>
 </div>
<div id="storeListHiddenDiv" style="z-index: 1000;position: fixed;right: 20px;top: 100px;">
	<button class="btn btn-info btn-xs" style="height: 100px;width: 9px" type="button" onclick="storeList(this)">
		<span class="glyphicon glyphicon-chevron-left"></span>
	</button>
</div>
  	<div id="menu" class="btn-toolbar" role="toolbar" style="display: none;">
		<div class="btn-group">
			<button class="btn btn-info" title="添加门店" type="button" onclick="taggingPoi();">
				<span class="glyphicon glyphicon-plus"></span>
				<span class="sr-only">添加</span>
			</button>	
<%--			<button class="btn btn-info" title="标注" type="button">--%>
<%--				<span class="glyphicon glyphicon-map-marker"></span>--%>
<%--				<span class="sr-only">标注</span>--%>
<%--			</button>--%>
			<button class="btn btn-info" title="刷新" type="button" onclick="resizeMapDiv();">
				<span class="glyphicon glyphicon-refresh"></span>
				<span class="sr-only">刷新</span>
			</button>
<%--			<button class="btn btn-info"title="清除地图" type="button" onclick="clearLayerFeatures();">--%>
<%--				<span class="glyphicon glyphicon-trash"></span>--%>
<%--				<span class="sr-only">清除地图</span>--%>
<%--			</button>--%>
		</div>
	</div>

<div class="modal fade bs-example-modal-sm" id="storeInput" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="myModalLabel">添加门店信息</h4>
        <strong id="storeWarning" style="color: red"></strong>
      </div>
      <div id="bindRiverBody" class="modal-body">
         <div id="addStoreForm">
	      <form role="form" id ="storeForm" class="form-horizontal" method="post">
	      	  <input type="hidden" name="id" id="id">
				 <input type="hidden" name="ctLat" id="ctLat" />
 				 <input type="hidden"  name="ctLng" id="ctLng" />
				 <input type="hidden" name="deptId" id="boxdepId" value="${dept.id}">
				 <input type="hidden" name="ico" id="ico" value="18.png">
			<p>
		      	 <label class="no-padding-right" for="typeId" style="padding-top: 4px;">图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;标： </label>  	 
		      	<a title="更改图标" href="#" onclick="selectMapIco()"><img id="storeIco" alt="" src="${base}/resources/images/poi/18.png"></a>
		      	 <div id="storeSelectIco" class="trackBox" style="display: none; width: 268px;height: 215px;">
			     	
				      	<img id="18"  alt="18.png" src="${base}/resources/images/poi/18.png"/>
				      	<img id="6" alt="6.png" src="${base}/resources/images/poi/6.png"/>
				      	<img id="10" alt="10.png" src="${base}/resources/images/poi/10.png"/>
				      	<img id="13" alt="13.png" src="${base}/resources/images/poi/13.png"/>
				      	<img id="15" alt="15.png" src="${base}/resources/images/poi/15.png"/>
				      	<img id="1" alt="1.png" src="${base}/resources/images/poi/1.png"/>
				      	<img id="21" alt="21.png" src="${base}/resources/images/poi/21.png"/>
				      	<img id="25" alt="25.png" src="${base}/resources/images/poi/25.png"/>
				      	<img id="23" alt="23.png" src="${base}/resources/images/poi/23.png"/>
					 
			     </div>
		      	 
			 </p>
			 <p>
		      	 <label class="no-padding-right" for="typeId" style="padding-top: 4px;">门店类型： </label>  	 
		      	 <select style="width: 158px;" name="typeId" id="typeId">
		      	 	<c:forEach var="storeType" items="${TypeList}" >
		      	 		<option value="${storeType.id }">${storeType.name }</option>
		      	 	</c:forEach>
		      	 </select>
		      	
			 </p>
	      	 <p>
		      	 <label class="no-padding-right" for="name" style="padding-top: 4px;">店 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名： </label>  	 
		      	 <input type="text" id="name" name="name" maxlength="20"/>
		      	
			 </p>
			  
			 <p>
				 <label class="  no-padding-right" for="shopkeeperName" style="padding-top: 4px;">店长名称： </label> 
				 <input type="text"  id="shopkeeperName" name="shopkeeperName" maxlength="20"/>
	      	</p>
	      	 <p>
				 <label class="  no-padding-right" for="shopkeeperPhone" style="padding-top: 4px;">店长电话： </label> 
				 <input type="text" id="shopkeeperPhone" name="shopkeeperPhone" maxlength="20"/>
	      	</p>
	      	 <p>
				 <label class="  no-padding-right" for="address" style="padding-top: 4px;">地 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 址： </label> 
				 <input type="text" id="address" name="address" maxlength="20"/>
	      	</p>
	      </form>
	      </div>
	     
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" onclick="clearLayerFeatures();">取消</button>
        <button type="button" id="storebtn" class="btn btn-primary" onclick="saveStore()">确定</button>
      </div>
    </div>
  </div>
</div>

<div class="modal fade bs-example-modal-sm" id="storeImg" tabindex="-1" role="dialog" aria-labelledby="storeImgLabel" aria-hidden="true">
   <div class="modal-dialog modal-sm">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
        <h4 class="modal-title" id="storeImgLabel">门店图片</h4>
      </div>
      <div id="bindRiverBody" class="modal-body">
        <div id="picRemed">
        </div>
      	<div id="myCarousel" class="carousel slide">
		  <!-- Carousel items -->
		  <div id="imgbody" class="carousel-inner">
		    <div class="active item"><img src="${base}/resources/images/test/20141125154931.png"/></div>
		    <div class="item"><img src="${base}/resources/images/test/20141125155017.png"/></div>
		    <div class="item"><img src="${base}/resources/images/test/20141125155040.png"/></div>
		  </div>
		  <!-- Carousel nav -->
		  <a class="carousel-control left" href="#myCarousel" data-slide="prev">‹</a>
		  <a class="carousel-control right" href="#myCarousel" data-slide="next">›</a>
		</div>
	   </div>  
    </div>
  </div>
</div>

	<div id="storeList" style="display: none;">
		<nav class="navbar navbar-default nav-primary" role="navigation">
	  		<div class="navbar-header">
		      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#carMonitor-collapse">
		        <span class="sr-only">Toggle navigation</span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		        <span class="icon-bar"></span>
		      </button>
		      <a class="navbar-brand" href="javascript:void(0);" onclick="layerStoreListeClose()" style="font-size: 14px;color: #fff;">
		      	<span class="glyphicon glyphicon-share-alt"></span>
		      </a>
		    </div>
    		<div class="collapse navbar-collapse" id="carMonitor-collapse" style="width: 280px;">
    			<form class="navbar-form navbar-left" role="search">
			        <div class="form-group">
			          <input type="text" id=search class="form-control" placeholder="请输入门店名称">
			        </div>
			      	 <button type="button" class="btn btn-purple btn-sm" onclick="reload()"><i class="ace-icon fa fa-search icon-on-right bigger-110"></i></button>
		      </form>
    		</div>
    	</nav> 
    	<div style="width: 300px;overflow:auto;">	   	
		 	<table id="grid-table"></table>
			<div id="grid-pager"></div>
		</div>
	</div>

 
<%@ include file="/WEB-INF/commons/mapMeta.jsp" %>
<script src="${base}/resources/My97DatePicker/WdatePicker.js"></script>
<script src="${base}/resources/layer/layer.min.js"></script>
<script src="${base}/resources/assets/js/bootbox.min.js"></script>
<script src="${base}/resources/js/car/carDeptTree.js"></script>
<script src="${base}/resources/js/store/storeView.js"></script>


</body>
</html>