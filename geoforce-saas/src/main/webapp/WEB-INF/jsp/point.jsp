<%@ page 
	language= "java" 
	import="java.util.*,com.supermap.egispservice.base.entity.UserEntity,
		com.supermap.egispservice.base.entity.ComEntity" 
	contentType ="text/html; charset=utf-8" 
	pageEncoding="utf-8" %>
<%
	String basePath = request.getScheme()+"://"+request.getServerName()
	+":"+request.getServerPort()+request.getContextPath()+"";
	request.setAttribute("ctx", basePath);
  UserEntity user = (UserEntity) request.getSession().getAttribute("user");  
  request.setAttribute("userid", user.getId());
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta name="description" content="地图慧-企业可视化管理平台" />
  <meta name="keywords" content="地图慧-企业可视化管理平台" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">  
  <title>地图慧-网点管理</title>

  <link rel="stylesheet" type="text/css" href="${ctx}/resources/css/point/point-less.css">
</head>

<body>
<div class="container-point">
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

  <div class="header">
    <div class="smcity">
      <a href="javascript:void(0);" class="current-city">
      	<span class="text">北京市</span>
      	<span class="caret"></span>
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
        <li class="hide">
          <img class="hint-mobile-point" src="${ctx}/resources/css/imgs/nav-tools/mobile.png">
          <a href="javascript:void(0);" class="tool-button add-mobile-point">采集</a>
        </li>
        <li>
          <a href="javascript:void(0);" class="tool-button add-point">标点</a>
        </li>
        <li>
          <a href="javascript:void(0);" class="tool-button pan-map">平移</a>
        </li>
        <li>
          <a href="javascript:void(0);" class="tool-button clear-map">清空</a>
        </li>
        <li option="show-children">
          <a href="javascript:void(0);" class="tool-button super">高级</a>
          <div class="super-menu">
            <div class="triangle top"></div>
            <ul>
              <li option="show-children">
                <a href="javascript:void(0);" class="tool-button map-layers">图层</a>
                <div class="second-menu">
                  <div class="triangle left"></div>
                  <ul>
                    <li>
                      <input class="check" type="checkbox" id="btn_showCluster" checked="true">
                      <label for="btn_showCluster">网点聚合</label>
                    </li>
                    <li>
                      <input class="check" id="show-point-label" type="checkbox">
                      <label for="show-point-label">网点标签</label>
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
                <a href="javascript:void(0);" class="tool-button header-share">分享</a>
              </li>
            </ul>
          </div>
        </li>        
      </ul>   
      <div class="hint-part hide">
      	<div class="triangle top"></div>
    	<p>
    	  <span class="badge">1</span>
    	      在大众制图网页端建立地图，设置网点属性字段，和企业服务系统保持一致；
    	</p>
    	<p>
    	  <span class="badge">2</span>扫码下载地图慧APP；
    	</p>
    	<img src="${ctx}/resources/images/logo/dituhui-app-code.png">
    	<p>
    	  <span class="badge">3</span>登录app后采集网点保存，在企业服务系统内同步采集。
    	</p>
      </div>
    </div>
  </div>
  
  <!-- 数据表格 -->
  <div class="data-list" status="min">
    <div class="head">
      <div class="title">
        <span class="tab-text">网点列表<span class="totality"></span></span>        
        <span class="tab-icon-style">图标样式</span>
      </div>
      <div class="import" title="根据模板导入网点数据">导入网点</div>
      <div class="resize up first" title="向上展开"></div>
      <div class="resize right second" title="向右展开"></div>
      <div class="resize min third" title="最小化"></div>
    </div>
    <div class="content content-table" data-display="table">
      <div class="triangle-top front"></div>

      <!-- 网点详情列表 -->
      <div class="table-branches-detail">
        <div class="search-area">  
          <select class="select-group left">
            <option value="-1" selected="true">全部分组</option>
          </select>      
          <input class="search-input left" id="txt_keyword_search_branches" type="text" 
          	placeholder="输入网点名称查询" value="">
          <a class="btn-search left" href="javascript:void(0);"></a>
        </div>
        <div class="data-results head-fixed"> 
          <table class="table data-table thead" id="table_branches_head">
            <thead>         
            </thead>         
          </table>  

          <div class="overflow-y-auto">
            <table class="table data-table" id="table_branches">
              <tbody>          
              </tbody>   
            </table> 
          </div>
        </div>
        <div class="foot">
          <a class="a-foot red right a-data-failed" href="javascript:void(0);" >定位失败<span></span></a>
          <a class="a-foot black right a-data-success" href="javascript:void(0);" >定位成功<span></span></a>
        </div>        
      </div>
      
      <!-- 网点数量列表 -->
      <div class="table-branches-count">
        <div class="data-results">
          <table class="table table-hover data-table" id="table_branches_count">
            <thead>         
              <tr>
                <td>序号</td>
                <td>行政区</td>
                <td>网点数量</td>
              </tr>
            </thead>
            <tbody>          
            </tbody>
          </table>        
        </div>
      </div>
    </div>
    <div class="content content-icon" data-display="icon">
      <div class="triangle-top back"></div>

      <!-- 样式列表 start -->
      <div class="tab-icon-list">
        <div class="branch-icon" data-attr="default" data-option="edit-icon-style">
          <img class="default" src="assets/map/red/default.svg">
          <div class="bottom">默认样式</div>
        </div>
        <div class="add-branch-icon" title="新建样式" data-option="edit-icon-style" data-type="add">
          <span class="glyphicon glyphicon-plus add-icon-style"></span>
          <div class="bottom">新建样式</div>
        </div>
      </div>
      <!-- 样式列表 end -->

      <!-- 新增/修改样式 start -->
      <div class="tab-edit">
        <div class="icon-edit-container">          
          <div class="icon-attr">
            <div class="branch-icon" data-attr="default">
              <div class="icon-demo" data-back="default" data-color="e51c23" 
              	data-color-path="red" data-size="48" data-ico="">
                <span class="iconfont"></span>
              </div>
              <div class="bottom" id="icon_styleName">默认样式</div>
            </div>  
            
            <div class="inputs-group">
              <div class="row-2 left">
                <label>样式名称</label>
                <input class="icon-style-input" id="txt_styleName" 
                	readonly="true" type="text" maxLength="20">
              </div>
              <div class="row-2 right">
                <label>所属分组</label>
                <select class="icon-style-input branch-group" id="txt_branchGroup">
                  <option value="">无</option>
                  <option value="add">新建分组</option>
                </select>
              </div>
            </div>
          </div>      


          <div class="icon-selector">
            <ul>
              <li data-option="show-icon-box" target=".icon-selector .back">外观</li>
              <li data-option="show-icon-box" target=".icon-selector .size">大小</li>
              <li data-option="show-icon-box" target=".icon-selector .color">颜色</li>
              <li data-option="show-icon-box" target=".icon-selector .ico">图案</li>
              <li data-option="show-icon-box" target=".icon-selector .custom">自定义</li>
            </ul>
            <div class="box back">
              <div class="top-mark"></div>
              <ul>
                <li data-back="transparent">
                  <img src="${ctx}/resources/assets/map/red/transparent.svg">
                  <span>无</span>
                </li>
                <li data-back="default">
                  <img src="${ctx}/resources/assets/map/red/default.svg">
                  <span>传统</span>
                </li>
                <li data-back="circle">
                  <img src="${ctx}/resources/assets/map/red/circle.svg">
                  <span>圆形</span>
                </li>
                <li data-back="square">
                  <img src="${ctx}/resources/assets/map/red/square.svg">
                  <span>方形</span>
                </li>
                <li data-back="diamond">
                  <img src="${ctx}/resources/assets/map/red/diamond.svg">
                  <span>菱形</span>
                </li>
                <li data-back="flag">
                  <img src="${ctx}/resources/assets/map/red/flag.svg">
                  <span>旗帜</span>
                </li>
              </ul>
            </div>
            <div class="box size">
              <div class="top-mark"></div>
              <div class="choose-size">
                <div class="size-slider">
                  <input id="slider_size" type="text" data-slider-handle="custom"> 
                  <div class="slider-textmark">
                    <span class="left">极小</span>
                    <span class="right">极大</span>
                  </div> 
                </div>
                <input class="icon-size" type="text" value="">               
              </div>                    
            </div>
            <div class="box color">
              <div class="top-mark"></div>
              <ul>
                <li class="red" data-color-path="red" data-color="e51c23" title="红色"></li>
                <li class="purple" data-color-path="purple" data-color="9c27b0" title="紫色"></li>
                <li class="blue" data-color-path="blue" data-color="0066ff" title="蓝色"></li>
                <li class="green" data-color-path="green" data-color="259b24" title="绿色"></li>
                <li class="orange" data-color-path="orange" data-color="ff8800" title="橙色"></li>
                <li class="brown" data-color-path="brown" data-color="795548" title="棕色"></li>
                <li class="indigo" data-color-path="indigo" data-color="10a0ad" title="靛蓝"></li>
                <li class="black" data-color-path="black" data-color="000" title="黑色"></li>
              </ul>            
            </div>
            <div class="box ico">
              <div class="top-mark"></div>
              <ul></ul>
            </div>
            <div class="box custom">
              <div class="top-mark"></div>    

              <ul>
                
              </ul>   
              <form id="form_upload_custom_icon" method="POST" enctype="multipart/form-data" 
              	encoding="multipart/form-data">
                <input type="file" id="txt_import_custom_icon" style="display:none;" 
                	name="multiFile" accept="image/*" onchange="Map.CustomIcon.add();">                
              </form>

              <!-- <img src="css/imgs/branch-icon/test-tif.tif"> -->

            </div>
          </div> 
        </div>
        <div class="icon-save-container">
          <a class="btn btn-red delete-point-style" href="javascript:void(0);">删除样式</a>
          <button type="button" class="btn btn-green" data-option="save-icon-style">保存</button>
          <button type="button" class="btn btn-gray" data-option="hide-tab-edit">取消</button>
        </div>
      </div> 
      <!-- 新增/修改样式 end -->
    </div>
  </div> 
  

  <!-- 网点列表-新增列 -->
  <div class="data-edit-back data-column-add">
    <div class="box">
      <input class="input-text text-column-add" maxlength="20" type="text" value="" placeholder="请填写列名称"> 
      <div class="foot">
        <a class="btn-cancel close-fade-out left" 
        	data-target=".data-column-add" href="javascript:void(0)">取消</a>
        <a class="btn-save right" href="javascript:void(0)" option="add-column-server">保存</a>
      </div>
    </div>
  </div>


  <!-- 重命名表头字段名称 -->
  <div class="data-edit-back data-column-rename">
    <div class="box">
      <input class="input-text text-column-rename" maxlength="20" type="text" value="" 
      	placeholder="请填写列名称"> 
      <div class="foot">
        <a class="btn-cancel left close-fade-out" data-target=".data-column-rename" 
        	href="javascript:void(0)">取消</a>
        <a class="btn-save right" href="javascript:void(0)" option="rename-column-server">确定</a>
      </div>
    </div>
  </div>

  <!-- 编辑网点的单元格 -->
  <div class="data-edit-back data-cell-edit">
    <div class="box">
      <textarea class="input-textarea text-cell-edit" maxlength="50"></textarea>
      <div class="foot">
        <a class="btn-cancel  close-fade-out left" 
        	data-target=".data-cell-edit" href="javascript:void(0)">取消</a>
        <a class="btn-save right" href="javascript:void(0)" option="update-cell">保存</a>
      </div>
    </div>
  </div>

  <!-- 编辑网点分组的单元格 -->
  <div class="data-edit-back data-cell-edit-group">
    <div class="box">
      <select class="branch-group margin-15" id=""></select>
      <div class="foot">
        <a class="btn-cancel close-fade-out left" 
        	data-target=".data-cell-edit-group" href="javascript:void(0)">取消</a>
        <a class="btn-save right" href="javascript:void(0)" option="update-cell-group">保存</a>
      </div>
    </div>
  </div>


  <!-- 新增网点分组 -->
  <div class="data-edit-back data-branchgroup-add">
    <div class="box">
      <input class="input-text text-branchgroup-add" maxlength="20" type="text" 
      	value="" placeholder="请填写分组名称"> 
      <div class="foot">
        <a class="btn-cancel close-fade-out left" 
        	data-target=".data-branchgroup-add" href="javascript:void(0)" 
        	onClick="Page.reSetSelect();">取消</a>
        <a class="btn-save right" href="javascript:void(0)" option="add-branchgroup-server">保存</a>
      </div>
    </div>
  </div>
  
  <!-- 删除网点分组 -->
  <div class="data-edit-back data-branchgroup-remove">
    <div class="box">
      <label class="input-label">请选择待删除的分组：</label>
      <select class="select-groups-toremove input-select"></select>
      <div class="foot">
        <a class="btn-cancel close-fade-out left" 
        	data-target=".data-branchgroup-remove" href="javascript:void(0)" 
        	onClick="Page.reSetSelect();">取消</a>
        <a class="btn-save right" href="javascript:void(0)" 
        	option="remove-branchgroup-server">确定</a>
      </div>
    </div>
  </div>
  
  <!-- 显示字段设置弹窗 -->
  <div class="data-modal setting-fields hide">
    <div class="box">
      <div class="title">
        <span>设置显示字段</span>
        <a class="setting-cancel" href="javascript:;"></a>
        <a class="setting-sure" href="javascript:;"></a>
      </div>
      <div class="body setting-fields-body">
        <div class="mrow">
          <span>网点名称</span><input type="checkbox" data->
        </div>
        <div class="mrow">
          <span>网点地址</span><input type="checkbox">
        </div>
      </div>
    </div>
  </div>


  <!-- 网点,上传门店照片 -->
  <div class="data-modal upload-pictures hide">
    <div class="box">
      <div class="title">
        <span class="upload-pictures-title">图集(2)</span>        
        <button type="button" class="close" data-target=".upload-pictures">
        	<span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="body">
        <ul class="ul-point-pictures">          
          <li class="add add-point-pictures">
            <div class="to-add">
              <div class="detail bg">上传图片</div>
              <div class="detail sm">(不超过2M)</div>              
            </div>
            <div class="loading"></div>
          </li>
        </ul>
        <form class="hide" id="form_upload_point_picture" method="POST" 
        	enctype="multipart/form-data" encoding="multipart/form-data">
          <input id="txt_upload_point_picture" type="file" name="multiFile" 
          	onChange="Map.Pictures.upload()">
          <input id="txt_upload_point_picture_pointid" type="text" name="pointid" value="">
        </form>
      </div>
      <div class="foot">
        <button class="btn btn-save-border right close-modal" data-target=".upload-pictures">确定</button>
      </div>
    </div>
  </div>
  
  <!-- 网点 移动端采集 -->
  <div class="data-modal sync-mobile-point hide">
    <div class="box">
      <!--<div class="title">
        <span class="upload-pictures-title">移动端网点采集</span>        
        <button type="button" class="close" data-target=".sync-mobile-point">
        	<span aria-hidden="true">&times;</span>
        </button>
      </div>-->
      <div class="body">
        <div class="input-part">
        	<!--<input id="txt_mpoint_url" class="input-mpoint-url" 
        		value="" 
        		placeholder="请输入需要采集的地图ID">
        	<a class="btn btn-success btn-sm btn-search-mpoint">采集</a>-->
        	<span>
        		请选择您在大众制图里的一个点标记地图：
        	</span>
        	<select id="select_mpoint"></select>
        </div>
      </div>
      <div class="foot">
        <a class="btn-cancel left" data-target=".sync-mobile-point" href="javascript:void(0)">取消</a>
        <a class="btn btn-success btn-sm right btn-search-mpoint" href="javascript:void(0)">采集</a>
      </div>
    </div>
  </div>

  <!-- 网点,查看照片大图 -->
  <div class="data-modal view-pictures  hide">
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

  <!-- 网点正在导入时候的提示 -->
  <div class="data-modal modal-loading hide">
    <div class="box-fixed box">
      <div class="back-img"> 
        <div class="loader"></div>
      </div>
      <div class="content">
        <div class="title">
          <span>提示</span>        
        </div>
        <div class="text">正在导入网点，请稍后...</div>        
      </div>
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

  <!-- 导入网点 -->
  <div class="data-edit-back data-import">
    <div class="box">
      <div class="title">
        <span class="text">批量导入网点</span>
        <button type="button" class="close close-fade-out" data-target=".data-import">
        	<span aria-hidden="true">&times;</span>
        </button>
      </div>
      <form id="form_import_branches"  method="POST" enctype="multipart/form-data" 
      	encoding="multipart/form-data">
        <div class="body">
          <div class="select-file">
            <a href="javascript:void(0);" class="btn-select-file"></a>
            <input type="file" id="txt_import_branches" style="display:none;" name="myFile" 
            	onChange="Map.checkImportFileSize()"
              accept="xlsx/*">
          </div>
          <div class="import-hint black"></div>
          <div class="import-hint gray">请按照模板格式导入数据</div>
        </div>        
      </form>
      <div class="download-area">
        <a class="btn-download-branches left" 
        	href="${ctx}/resources/docs/data_pois_hasll_upload_dituhui.zip">下载模板(有经纬度)</a>
        <a class="btn-download-branches right" 
        	href="${ctx}/resources/docs/data_pois_noll_upload_dituhui.zip">下载模板(无经纬度)</a>
      </div>
      <div class="foot">
        <a class="btn-sure right" id="btn_import_branches" href="javascript:void(0);">确定</a>
        <a class="btn-cancel close-fade-out right" href="javascript:void(0);" 
        	data-target=".data-import">取消</a>
      </div>
    </div>
  </div>

  <!-- 选择车辆 -->
  <div class="data-edit-back data-cars">
    <div class="box">
      <div class="title">
        <span class="text">绑定车辆</span>
        <button type="button" class="close" data-target=".data-cars">
        	<span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="search-area">
        <label>车牌号：</label><input id="txt_license" type="text" value="">
        <label>SIM卡号：</label><input id="txt_sim" type="text" value="">
        <label>车辆类型：</label>
        <select class="select-car-type">
        </select>
        <button class="btn btn-default btn-sm btn-search-cars" type="button">查询</button>
      </div>
      <div class="content">
        <table class="table table-cars">
          <thead>
            <tr>
              <td><input type="checkbox" class="select-all-car" title="全选"></td>
              <td>序号</td>
              <td>车牌号码</td>
              <td>终端号</td>
              <td>SIM卡号</td>
              <td>车辆类型</td>
              <td>司机姓名</td>
            </tr>
          </thead>
          <tbody></tbody>
        </table>        
      </div>      
      <div id="pager_cars" class="content-pager hide-input" page="0" data-target="data-cars">
        <ul class="pagination pagination-sm">
        </ul>
      </div>
      <div class="foot">
        <a class="btn-cancel close-fade-out right" href="javascript:void(0);" data-target=".data-cars">
        	取消
        </a>
        <a class="btn-save right" id="btn_bind_cars" href="javascript:void(0);">确定</a>
        <a class="btn-save right" id="btn_select_all_cars" href="javascript:void(0);">全选</a>
      </div>
    </div>
  </div>

  <!-- 用户引导 -->
  <div class="data-edit-back data-guide">
    <div class="step-1">      
      <div class="import">导入网点</div>
      <div class="next" data-pre=".step-1" data-next=".step-2"></div>
    </div>
    <div class="step-2">
      <input class="search-input" type="text" placeholder="输入关键字搜索POI" value="" readonly="readonly">
      <div class="next" data-pre=".step-2" data-next=".step-3"></div>
    </div>
    <div class="step-3">
      <a href="javascript:void(0);" class="tool-button">
        <img class="img-2" src="assets/nav-tools/pin-marker.png">标点
      </a>
      <div class="next" data-pre=".step-3" data-next=".step-4"></div>
    </div>
    <div class="step-4">
      <div class="layers-control">
        <input type="checkbox" readonly="readonly"><label>显示网点</label>
        <i></i>
        <input type="checkbox" readonly="readonly"><label>显示区划</label>
      </div>
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
  <div class="mask-loading-text">
    <div class="hint"><a href="javascript:void(0);"></a></div>
  </div>
  
  <!-- 正在加载 -->
  <div class="mask-loading">
    <div class="box">
      <div class="loader"></div>      
    </div>
  </div>
	<div class="bk-share hide">
		<a href="javascript:void(0)" id="close3" class="closeBtn close-share">&times;</a>
		<div id="shareMap" class="pull-left share-left">
	        <div class="share-wx">
	          <div class="img" id="share_currentPage_code"></div>
	          <span class='dth-show dth-mar12t'>打开微信扫一扫，将地图分享至朋友圈</span>
	        </div>
	    </div>
	    <div class="pull-right share-right">
	        <div>
	          <span style='color:#777;font-size:12px;'>复制链接地址给好友</span>
	          <br>
	          <input id='currentPage_shareLink' class='form-control dth-w192 pull-left'
	          	style="margin: 10px 0;"
	          	type="text" value="">
	          <a id='share_current_url' 
	          	class='btn-copy-link' 
	          	title='复制当前链接发给好友' 
	          	href="javascript:void(0)">
	          	复制
	          </a>
	          <br>
	          <span class="hide copy-success">复制成功</span>
	        </div>
	    </div>
	</div>
</div>
  <script src="${ctx }/resources/js/Dituhui/iclient-8c/SuperMap.Include.js" type="text/javascript"></script>
  <script src="${ctx}/resources/js/Dituhui/dituhui.libs.min.js"></script>
  <script src="${ctx}/resources/js/public/galleria/galleria-1.4.2.min.js"></script>
  <script src="${ctx}/resources/js/public/jquery.rotate.min.js"></script>
  <script type="text/javascript">
    $(function(){
      	//甘肃石油
   		Dituhui.User.special = ( ${userid == '8a04a77b510e17c801532c04261225f1'} );
      	// Dituhui.User.special = ( ${userid == '8a04a77b4ee30e56014f00e022800216'} );
    });
    var userid = '${userid}';
  </script>
  <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=19a634cb08e8b1d6802fe65323806427"></script>
  <script src="${ctx}/resources/js/config.js"></script>
	<script src="${ctx}/resources/js/public/zclip/jquery.qrcode.min.js"></script>
	<script src="${ctx}/resources/js/public/zclip/jquery.zclip.min.js"></script>
  <script src="${ctx}/resources/js/point/point.compress.min.js"></script>
  <script src="${ctx}/resources/js/public/region.search.js"></script>
  <script type="text/javascript" src="${ctx}/resources/js/public/cloudpoi.search.js"></script>
  
</body>
</html>


