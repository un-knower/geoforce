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
  <title>地图慧-分单管理</title>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/fendan/fendan.css">
</head>

<body>
<div class="container-fendan">
<!-- <iframe id="iframe" width="100%"  style="border-width : 0px;" src="${ctx }/resources/point/fendan.html?isLogined=${isLogined}"></iframe> -->
  
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

  <!-- 导航 -->
  <div class="header">
    <div class="smcity">
      <a href="javascript:void(0);" class="current-city"><span class="text">北京市</span><span class="caret"></span></a>
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

    <!-- <div class="layers-control">
      <input type="checkbox" id="btn_showBranch"><label for="btn_showBranch">显示网点</label>
      <i></i>
      <input type="checkbox" id="btn_showRegion"><label for="btn_showRegion">显示区划</label>
    </div> -->
    
    <div class="tools">
      <ul class="nav-tools">
        <li>
          <a href="javascript:void(0);" class="tool-button pan-map">平移</a>
        </li>
        <li>
          <a href="javascript:void(0);" class="tool-button clear-map">清空</a>
        </li>
        <li option="show-children" class="hide">
        <!-- <li option="show-children"> -->
          <a href="javascript:void(0);" class="tool-button relative" option="super"><img class="img-2" src="${ctx}/resources/css/imgs/nav-tools/super.png">高级</a>
          <div class="super-menu">
            <div class="triangle top"></div>
            <ul>
              <li option="show-children">
                <a href="javascript:void(0);" class="tool-button"><img class="img-2" src="${ctx}/resources/css/imgs/nav-tools/layer.png">地图</a>
                <div class="second-menu">
                  <div class="triangle left"></div>
                  <ul>
                    <li><a class="tool-button to-supermap" href="javascript:void(0);">超图地图</a></li>
                    <li><a class="tool-button to-google" href="javascript:void(0);">谷歌地图</a></li>
                    <li><a class="tool-button to-tianditu" href="javascript:void(0);">天地图</a></li>
                  </ul>
                </div>
              </li>
            </ul>
          </div>
        </li>        
      </ul>      
    </div>
  </div>

  <!-- 数据表格 -->
  <div class="data-list" status="min">
    <div class="head">
      <div class="title">订单列表<span class="totality"></span></div>
      <div class="import" title="根据模板导入订单数据">导入订单</div>
      <div class="resize up first" title="向上展开"></div>
      <div class="resize right second" title="向右展开"></div>
      <div class="resize min third" title="最小化"></div>
    </div>
    <div class="content">
      <!-- <div class="search-area">        
        <input class="search-input left" id="txt_keyword_search_branches" type="text" placeholder="输入订单批次查询" value="">
        <a class="btn-search left" href="javascript:void(0);"></a>
      </div> -->

      <div class="data-results">
        <table class="table data-table" id="table_orders">
          <thead>
            <tr>
              <!-- <td class="td-60" >序号</td>
              <td class="td-100">运单号</td>
              <td class="td-150">订单批次</td> -->
              <td class="td-300">订单地址</td>
              <td class="td-150">所属区划</td>
             <!--  <td class="td-100">区划状态</td>
              <td class="td-150">关联区划</td>
              <td class="td-150">分单状态</td>
              <td class="td-100">操作</td> -->
            </tr>         
          </thead>
          <tbody>
          </tbody>
        </table>        
      </div>

      <div class="foot">
        <div id="data_pager" class="content-pager left" page="0" data-target="orders">
          <ul class="pagination pagination-sm">
          </ul>
        </div>
        <span class="page-marker left"></span>
        <a class="btn btn-success btn-sm btn-export hide left" style="margin: 10px 15px;" href="javascript:;">导出</a>

        <!-- <a class="a-foot red right a-data-failed" href="javascript:void(0);" >分单失败<span></span></a>
        <a class="a-foot black right a-data-success" href="javascript:void(0);">分单成功<span></span></a> -->
      </div>
    </div>
  </div> 


  <!-- 导入订单 -->
  <div class="data-edit-back data-import">
    <div class="box">
      <div class="title">
        <span class="text">导入订单</span>
        <button type="button" class="close" data-target=".data-import"><span aria-hidden="true">&times;</span></button>
      </div>
      <form id="form_import_data"  method="POST" enctype="multipart/form-data" encoding="text/html">
        <div class="body">
          <div class="select-file">
            <a href="javascript:void(0);" class="btn-select-file"></a>
            <input type="file" id="txt_import_orders" style="display:none;" name="myFile" onChange="Map.checkImportFileSize()"
              accept="xlsx/*">
          </div>
          <div class="import-hint black"></div>
          <div class="import-hint gray">请按照模板格式导入数据</div>
        </div>        
      </form>
      <div class="foot">
        <a class="btn-download-branches left" href="${ctx}/resources/docs/data_orders_upload_dituhui.xlsx" target="_blank">下载模板</a>
        <a class="btn-sure right" id="btn_import_orders" href="javascript:void(0);">确定</a>
        <a class="btn-cancel right" href="javascript:void(0);" data-target=".data-import">取消</a>
      </div>
    </div>
  </div>
  
  <!-- 网点,查看照片大图 -->
  <!-- <div class="data-modal view-pictures hide">
    <div class="head">
      <ul>
        <li><a class="show-img-btn zoom-in" href="javascript:;" title="放大"></a></li>
        <li><a class="show-img-btn zoom-out" href="javascript:;" title="缩小"></a></li>
        <li><a class="show-img-btn turn-left" href="javascript:;" title="向左旋转"></a></li>
        <li><a class="show-img-btn turn-right" href="javascript:;" title="向右旋转"></a></li>
        <li><a class="show-img-btn full-img" href="javascript:;" title="查看图片"></a></li>
        <li><a class="show-img-btn shut" href="javascript:;" title="关闭"></a></li>
      </ul>
    </div>
    <div id="images" class="images">
      <img src="/saas/resources/images/point/1.jpg" alt="">
      <img src="/saas/resources/images/point/2.jpg" alt="">
      <img src="/saas/resources/images/point/3.jpg" alt="">
    </div>
  </div> -->

  <!-- 用户引导 -->
  <div class="data-edit-back data-guide">
    <div class="step-1">      
      <div class="import">导入订单</div>
      <div class="start"></div>
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
  <div class="mask-loading">
    <div class="box">
      <div class="loader"></div>      
    </div>
  </div>
</div>
 
  <script type="text/javascript" src="${ctx}/resources/js/Dituhui/iclient-8c/SuperMap.Include.js"></script>
  <script src="${ctx}/resources/js/Dituhui/dituhui.libs.min.js"></script>
  <script src="${ctx}/resources/assets/js/jquery.form.js" type="text/javascript" ></script>
  <script src="${ctx}/resources/js/public/jquery.rotate.min.js"></script>
  <script src="${ctx}/resources/js/config.js"></script>
  <script src="${ctx}/resources/js/fendan/map.js"></script>
  <script src="${ctx}/resources/js/fendan/page.js"></script>
  <script src="${ctx}/resources/js/fendan/LayerEdit.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/dth.map.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/region.search.js"></script>
  <%-- <script type="text/javascript" src="${ctx}/resources/js/public/branch.search.js"></script> --%>
  <%-- <script src="${ctx}/resources/js/public/galleria/galleria-1.4.2.min.js"></script> --%>
</body>
</html>









