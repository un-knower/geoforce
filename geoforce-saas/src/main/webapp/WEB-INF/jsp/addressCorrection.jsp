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
  <title>地图慧-地址纠错</title>
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/address-correction/ac.css">

<!-- page specific plugin styles -->

<!-- page specific plugin scripts 
<script src="${ctx}/resources/js/sys/util.js"></script>
<script type="text/javascript">

$(function(){
	initcss();
	$(window).resize(function(){
		initcss();
	})
});
function initcss() {
	var h = getWindowHeight();
	$("#iframe").css({height: ( h-58 )});	
}
</script>
<style type="text/css">

</style>-->

</head>

<body>
<div class="container-ac"> 
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

    <div class="layers-control">
      <input type="checkbox" id="btn_showBranch"><label for="btn_showBranch">显示网点</label>
      <i></i>
      <input type="checkbox" id="btn_showRegion"><label for="btn_showRegion">显示区划</label>
    </div>

    <div class="search-from-cloud">
      <input id="txt_keyword_cloud" class="search-input" type="text" placeholder="输入关键字搜索" value="" maxlength="64">
      <div class="input-clear hide"></div>
      <div class="hide-cloudpois hide">点击查看全部搜索结果</div>
      
      <div class="triangle top child hide"></div>
      <div class="cloud-pois child hide">
        <button class="close" type="button" data-target=".header .search-from-cloud .child">
          <span aria-hidden="true">&times;</span>
        </button>
        <div class="content"></div>
        <div id="pager_cloudpois" class="content-pager hide-input" page="0" data-target="cloud-pois">
          <ul class="pagination pagination-sm" id="ul_pager_users">
          </ul>
        </div>
        <span class="page-cloud-pois left"></span>
      </div>
    </div>

    
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
          <a href="javascript:void(0);" class="tool-button relative" option="super"><img class="img-2" src="assets/nav-tools/super.png">高级</a>
          <div class="super-menu">
            <div class="triangle top"></div>
            <ul>
              <li option="show-children">
                <a href="javascript:void(0);" class="tool-button"><img class="img-2" src="assets/nav-tools/layer.png">地图</a>
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

  <!-- 地址匹配框 -->
  <div class="tool-geocode">
    <input class="text-geocode" type="text" placeholder="输入地址进行地址纠错" maxlength="50">
    <div class="button-geocode">地址纠错</div>
  </div>

  <!-- 数据表格 -->
  <div class="data-list less" status="min">
    <div class="head">
      <div class="title">地址纠错库<span class="totality"></span></div>
      <div class="resize up first" title="向上展开"></div>
      <div class="resize right second" title="向右展开"></div>
      <div class="resize min third" title="最小化"></div>
    </div>
    <div class="content">
      <div class="search-area">
          <select id="status_option">
            <option status="0">未纠错</option>
            <option status="1">已纠错</option>
            <option status="2">全部</option>

          </select>
       
        <input class="search-input left" id="txt_keyword_search_branches" type="text" placeholder="输入地址查询" value="">
        <a class="btn-search left" href="javascript:void(0);"></a>
      </div>

      <div class="data-results">
        <table class="table data-table">
          <thead>
            <tr>
              <td class="td-60">序号</td>
              <td class="td-300">地址</td>
              <td class="td-100">经度</td>
              <td class="td-100">纬度</td>
              <td class="td-150">创建日期</td>                            
              <td class="td-200">修改时间</td>
              <td class="td-100">状态</td>              
              <td class="td-200">操作</td>
            </tr>         
          </thead>
          <tbody>          
          </tbody>
        </table>        
      </div>
      <div class="foot">
        <div class="foot-left">
          <div class="tols left">共<span class="tolsNum">0</span>条</div>
          <div class="left-controle left">
            每页
            <select id="numCurr">
              <option>20</option>
              <option>50</option>
              <option>100</option>
            </select>
          </div>

        </div>
        <div id="data_pager" class="content-pager left" page="0" data-target="geocoders" data-total-page="0">
          <ul class="pagination pagination-sm">
          </ul>
        </div>
        <span class="page-marker left"></span>
        <!-- <a class="a-foot red right a-data-failed" href="javascript:void(0);" >定位失败<span></span></a>
        <a class="a-foot black right a-data-success" href="javascript:void(0);">定位成功<span></span></a> -->
      </div>
    </div>
  </div> 


  <div class="data-modal modal-alert hide">
    <div class="box-fixed box">
      <div class="back-img">
      </div>
      <div class="content">
        <div class="title">
          <span>提示</span>        
        </div>
        <div class="text"></div>
        <div class="foot">
          <a class="close left" data-target=".data-modal" href="javascript:void(0);">取消</a>
          <button class="btn btn-save-border right">确定</button>
        </div>
      </div>      
    </div>
  </div>
  <div class="data-modal modal-ask hide">
    <div class="box-fixed box">
      <div class="back-img">
      </div>
      <div class="content">
        <div class="title">
          <span>提示</span>        
        </div>
        <div class="text"></div>
        <div class="foot">
          <a class="close left" data-target=".data-modal" href="javascript:void(0);">取消</a>
          <button class="btn btn-save-border right">确定</button>
        </div>
      </div>      
    </div>
  </div>
  
  <!-- 网点,查看照片大图 -->
  <div class="data-modal view-pictures hide">
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
  <script src="${ctx}/resources/js/public/galleria/galleria-1.4.2.min.js"></script>
  <script src="${ctx}/resources/js/public/jquery.rotate.min.js"></script>
  <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=19a634cb08e8b1d6802fe65323806427"></script>
  <script src="${ctx}/resources/js/config.js"></script>
  <script src="${ctx}/resources/js/address-correction/map.js"></script>
  <script src="${ctx}/resources/js/address-correction/page.js"></script>
  <script src="${ctx}/resources/js/address-correction/LayerEdit.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/dth.map.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/region.search.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/branch.search.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/cloudpoi.search.js"></script>
</body>
</html>
