<%@ page 
	language= "java" import="java.util.*,com.supermap.egispservice.base.entity.UserEntity,com.supermap.egispservice.base.entity.ComEntity" 
	contentType ="text/html; charset=utf-8" 
	pageEncoding="utf-8" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
	UserEntity user = (UserEntity) request.getSession().getAttribute("user");  
	request.setAttribute("userid", user.getId());
%>
<html>
<head>
  <meta name="description" content="地图慧-企业可视化管理平台" />
  <meta name="keywords" content="地图慧-企业可视化管理平台" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">  
  <title>地图慧-区划管理</title>

  <link rel="stylesheet" type="text/css" href="${ctx}/resources/assets/css/jquery.contextMenu.css">
  <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/region/region.css">
  <link rel="stylesheet" href="${ctx}/resources/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
</head>

<body>
<div class="container-region">
  <div id="map">
    <div class="map-copyright">&copy;2016 高德软件 GS(2016)710号 </div>  
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
          <a class="county level-3 hide" href="javascript:void(0);" option="toCity" check-town="true"></a>
          
          <span class="town level-4 hide">></span>
          <a  class="town level-4 hide" href="javascript:void(0);"></a>

          <a class="level-1 level-2 level-3 set-default-city hide" href="javascript:void(0);">[设为默认]</a>
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
        <!--<button class="close" type="button" data-target=".header .search-from-cloud .child">
          <span aria-hidden="true">&times;</span>
        </button>-->
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
          <a href="javascript:void(0);" class="tool-button measure">测距 </a>
        </li>
        <li>
          <a href="javascript:void(0);" class="tool-button pan-map" option="pan-map">平移 </a>
        </li>
        <li>
          <a href="javascript:void(0);" class="tool-button clear-map" option="clear-map">清空</a>
        </li>
        
        <li option="show-children">
          <a href="javascript:void(0);" class="tool-button super">高级</a>
          <div class="super-menu">
            <div class="triangle top"></div>
            <ul>
              <li option="show-children" class="sec">
                <a href="javascript:void(0);" class="tool-button map-layers">图层</a>
                <div class="second-menu">
                  <div class="triangle left"></div>
                  <ul>
                    <li>
                      <input class="check" type="checkbox" id="btn_showCountryBoundry">
                      <label for="btn_showCountryBoundry">区县界</label>
                    </li>
                  </ul>
                </div>
              </li>
              <li option="show-children">
                <a href="javascript:void(0);" class="tool-button map-img">地图</a>
                <div class="second-menu">
                  <div class="triangle left"></div>
                  <ul>
                    <li><a class="action to-supermap" href="javascript:void(0);">高德地图</a></li>
                    <li><a class="normal to-baidu" href="javascript:void(0);">百度地图</a></li>
                  </ul>
                </div>
              </li>
              <li>
                <a href="javascript:void(0);" class="tool-button region-import">导入</a>
                <form id="form_import_region" method="POST" enctype="multipart/form-data" encoding="multipart/form-data">
                	<input class="select-region-import hide" name="myFile" type="file">
                </form>                
              </li>
              <li>
                <a href="javascript:void(0);" class="tool-button region-export">导出</a>
              </li>
            </ul>
          </div>
        </li>         
      </ul>      
    </div>
  </div>

  <div class="toolbox  has-town-authority">
  	<ul>
    <li class="tool-button town">
      <div class="tool-icon add-town"></div>
      <span>行政区划</span>
    </li>
    
    <li class="tool-button add">
      <div class="tool-icon add-region"></div>
      <span>自由画区</span>      
    </li>

    <li class="tool-button add-path">
      <div class="tool-icon add-path-region"></div>
      <span>沿路画区</span>
    </li>

    <div class="line"></div>

    <li class="tool-button sm edit region-editable">
      <div class="tool-icon edit-region"></div>
      <span>编辑</span>      
    </li>
    
    <li class="tool-button sm attr">
      <div class="tool-icon attr-region"></div>
      <span>属性</span>      
    </li>
    <li class="tool-button sm delete region-editable">
      <div class="tool-icon delete-region"></div>
      <span>删除</span>      
    </li>

    <div class="line"></div>

    <li class="tool-button sm merge region-editable">
      <div class="tool-icon merge-region region-editable"></div>
      <span>合并</span>      
    </li>
    <li class="tool-button md line-split region-editable">
      <div class="tool-icon line-split-region"></div>
      <span>线拆分</span>      
    </li>
    <li class="tool-button md area-split region-editable">
      <div class="tool-icon area-split-region"></div>
      <span>面拆分</span>      
    </li>
  		
  	</ul>

  </div>
	  
  <!--沿路画区工具箱-->
  <div class="path-region-content hide">
  	<div class="title">
  		<span>沿路画区</span>
  	</div>
  	<div class="mrow">
  		<a class="btn btn-info btn-xs undo">撤销</a>
  		<a class="btn btn-info btn-xs drag">拖动节点</a>
  	</div>
  	<div class="mrow">
  		<a class="btn btn-info btn-xs done">结束绘制</a>
  		<a class="btn btn-info btn-xs cancel">取消画区</a>
  	</div>
  </div>    
    
  <!-- 数据表格 -->
  <div class="data-list less" status="min">
    <div class="head">
      <div class="title">区划列表<span class="totality"></span></div>
      <div class="resize up first" title="向上展开"></div>
      <div class="resize right second" title="向右展开"></div>
      <div class="resize min third" title="最小化"></div>
    </div>
    <div class="content">
      <div class="search-area">        
        <input class="search-input left" id="txt_keyword_search_regions" type="text" placeholder="输入区划名称或区划编号查询" value="">
        <a class="btn-search left" href="javascript:void(0);"></a>
      </div>

      <div class="data-results">
        <table class="table data-table">
          <thead>
            <tr>
              <td class="td-60">序号</td>
              <td class="td-200">区划名称</td>
              <td class="td-150">区划编号</td>
              <td class="td-150">区划状态</td>
              <td class="td-150">关联区划</td>
              <td class="td-150">绑定网点</td>
              <td class="td-150">所属账号</td>
              <td class="td-150 thead-split">创建时间</td>
              <td class="td-150">修改时间</td>
              <td class="td-200">操作</td>
            </tr>
          </thead>
          <tbody>          
          </tbody>
        </table>        
      </div>
      
      <!-- <div class="foot">
        <div id="data_pager" class="content-pager left" page="0" data-target="geocoders">
          <ul class="pagination pagination-sm">
          </ul>
        </div>
        <span class="page-marker left"></span>

        <a class="a-foot red right a-data-failed" href="javascript:void(0);" >定位失败<span></span></a>
        <a class="a-foot black right a-data-success" href="javascript:void(0);">定位成功<span></span></a>
      </div> -->
    </div>
  </div> 

  <div class="data-modal modal-loaded modal-right hide">
    <div class="box-fixed box">
      <div class="back-img">      
      </div>
      <div class="content">
        <div class="title">
          <span>提示</span>        
        </div>
        <div class="text"></div>   
        <div class="foot">
          <!-- <a class="close left" data-target=".data-modal" href="javascript:void(0);">取消</a> -->
          <button class="btn btn-save-border right">确定</button>
        </div> 
      </div>
    </div>
  </div>
  
  <div class="data-modal modal-loaded modal-wrong hide">
    <div class="box-fixed box">
      <div class="back-img">      
      </div>
      <div class="content">
        <div class="title">
          <span>提示</span>        
        </div>
        <div class="text"></div>
        <div class="foot">
          <!-- <a class="close left" data-target=".data-modal" href="javascript:void(0);">取消</a> -->
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
          <button class="btn btn-no-save-border right hide">否</button>
        </div>
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
  
  <!--矢量元素上的右键-->
  <div id="featureRightMenu" class="feature-right-menu">
	<ul>
	  <li class="r-attr">
	    <span>区划属性</span>
	  </li>
	  <li class="r-delete">
	    <span>删除区划</span>
	  </li>
	  <li class="r-merge">
	    <span>合并区划</span>
	  </li>
	  <li class="r-pan">
	    <span>平移地图</span>
	  </li>
	</ul>
  </div>
  
  <!-- 更改区划子账号 -->
  <div class="data-edit-back data-user-belonging">
    <div class="box">
      <div class="title">
        <span class="text">更新区划所属用户</span>
        <button type="button" class="close close-fade-out" data-target=".data-user-belonging">
        	<span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="search-area">
		<span>选择账号：</span>
		<ul id="tree_usebelonging" class="ztree"></ul>
      </div>
      <div class="foot">
      	<span class="left span-selected-username"></span>
        <a class="btn-sure right btn-set-usrbelonging" href="javascript:void(0);">确定</a>
        <a class="btn-cancel close-fade-out right" href="javascript:void(0);" data-target=".data-user-belonging">取消</a>
      </div>
    </div>
  </div>
  
  <!-- 更改区划状态 -->
  <div class="data-edit-back data-area-status">
    <div class="box">
      <div class="title">
        <span class="text">更新区划状态</span>
        <button type="button" class="close close-fade-out" data-target=".data-area-status">
        	<span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="search-area">
		<div class="mrow">
			<label class="lbl">区划名称：</label>
			<span class="text area-name-for-status"></span>
		</div>
		<div class="mrow">
			<label class="lbl">区划状态：</label>
			<select class="select-area-status">
				<option selected="selected" value="0">正常</option>
				<option value="1">停用</option>
				<option value="2">超区</option>
			</select>
		</div>
      </div>
      <div class="foot">
        <a class="btn-sure right btn-set-areastatus" href="javascript:void(0);">确定</a>
        <a class="btn-cancel close-fade-out right" href="javascript:void(0);" data-target=".data-area-status">取消</a>
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
  
<!-- 行政区划正在导入时候的提示 -->
  <div class="data-modal modal-loading hide">
    <div class="box-fixed box">
      <div class="back-img"> 
        <div class="loader"></div>
      </div>
      <div class="content">
        <div class="title">
          <span>提示</span>        
        </div>
        <div class="text">正在导入行政区划，请稍后...</div>        
      </div>
    </div>
  </div>
  <!-- 导入行政区划 -->
  <div class="data-edit-back data-import-regins">
  	<div class="box">
  		<div class="title">
  			<span class="title-name">载入行政区划</span>
  			<button type="button" class="close close-fade-out" aria-label="Close" data-target=".data-import-regins">
  				<span aria-hidden="true">&times;</span>
  			</button>
  		</div>
  		<div class="content">
  			<div class="admins">
  				<div class="admin-hint">选择级别 
  					<span>（提示：单击向下钻取，双击或ctrl+单击确认选择）</span>
  				</div>
  				<div class="content-shuttle">
  					<div class="shuttle shuttle-province">
	  					<div class="title">省界</div>
	  					<ul id="shuttle_province" class="scrollbar">
	  						
	  					</ul>
	  				</div>
	  				<div class="shuttle shuttle-city">
	  					<div class="title">市界</div>
	  					<ul id="shuttle_city" class="scrollbar"></ul>
	  				</div>
	  				<div class="shuttle shuttle-county">
	  					<div class="title">区县界</div>
	  					<ul id="shuttle_county" class="scrollbar"></ul>
	  				</div>
	  				<div class="shuttle shuttle-town">
	  					<div class="title">乡镇界</div>
	  					<ul id="shuttle_town" class="scrollbar"></ul>
	  					<div class="no-rights">无权限使用乡镇界，请联系商务开通</div>
	  				</div>
  				</div>  				
  			</div>
  			<div class="selected-admins">
  				<div class="title">
  					已添加
  				</div>
				<ul id="selected_adminnames" class=" scrollbar">
					<li>
						
					</li>
				</ul>
				<div class="footer-tools">
          			<button class="btn btn-no-save-border left close-fade-out">取消</button>
					<button class="btn btn-save-border right btn-import-adminregions">导入</button>
				</div>
  			</div>
  		</div>
  			
  	</div>
  </div>
  
</div>

  <script type="text/javascript" src="${ctx}/resources/zTree/js/jquery.ztree.core-3.5.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/zTree/js/jquery.ztree.excheck-3.5.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/Dituhui/iclient-8c/SuperMap.Include.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/Dituhui/dituhui.libs.min.js"></script>
  <script type="text/javascript">
    var userid = '${userid}';
  </script>
  <script type="text/javascript" src="${ctx}/resources/js/public/jquery.contextMenu.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/config.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/galleria/galleria-1.4.2.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/jquery.rotate.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/assets/js/jquery.form.js"></script>
  <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=19a634cb08e8b1d6802fe65323806427"></script>
  
  <script type="text/javascript" src="${ctx}/resources/js/zone/zone.min.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/branch.search.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/dth.map.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/baidu.map.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/cloudpoi.search.js"></script>
  
</body>

</html>











