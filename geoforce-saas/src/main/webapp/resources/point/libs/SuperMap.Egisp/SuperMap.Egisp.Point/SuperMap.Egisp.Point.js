
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp\SuperMap.Egisp.Point
 */
SuperMap.Egisp.Point = SuperMap.Egisp.Point || {};
/**
 * 查询
 */
SuperMap.Egisp.Point.Search = function(param, success, failed) {
	if( !param || !param.pageNo) {
		return;
	}
    SuperMap.Egisp.showMask();

    var smcity = $('.smcity');
    var admincode = smcity.attr('admincode');
    var level = smcity.attr('level');
    if(level && level != '') {
        param.admincode = admincode;
        param.level = level;
    }

    SuperMap.Egisp.request({
        url: urls.server + "/pointService/queryAllPoint?",
        data: param,
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess && e.result) {
                var re = [];
                if( e.result.length === 0 ) {
                    success(re);
                }
                else {
                    success( e.result.records );
                }
            }
            else {
                failed(e.info ? e.info : "网点查询失败");
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();  
            failed();
        }           
    })
}
SuperMap.Egisp.Point.getImportBranches = function(callback) {
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/getAllProcessingPoint?",
        dataType: 'json',
        success: function(e){
            if(e.isSuccess && e.result) {
                var info = '有' + e.result + '个导入的网点未解析，是否进行解析？<br>点击“确定”解析，点击“取消”删除未处理的网点。';
                var attr = {};
                SuperMap.Egisp.Modal.ask(info, function(){
                    SuperMap.Egisp.Modal.loading('正在解析数据...<br>');
                    SuperMap.Egisp.Point.analysisPoints(true, function(){
                        SuperMap.Egisp.Modal.loaded_right('数据解析完成。<br>', Map.afterImportRight); 
                    }, function(r){
                        SuperMap.Egisp.Modal.loaded_wrong('数据解析失败<br>' + r, Map.afterImportWrong);
                    });
                }, attr, function(){
                    SuperMap.Egisp.showMask();
                    SuperMap.Egisp.Point.analysisPoints(false, function(){                        
                        SuperMap.Egisp.hideMask();                        
                        SuperMap.Egisp.Modal.hide();
                        Map.searchBranches();
                    }, function(r){
                        SuperMap.Egisp.hideMask();
                        SuperMap.Egisp.Modal.hide();
                        Map.searchBranches();
                    });
                });
            }            
        },
        error: function(){}
    })
}
SuperMap.Egisp.Point.analysisPoints = function(iscontinue, success, error) {
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/analysisPoint?",
        data: {
            iscontinue: iscontinue
        },
        success: function(r){
            if(r.isSuccess) {
                success();                          
            }
            else {
                error(r.info);
            }
        },
        error: function(){
            error(false);
        }
    });
}
SuperMap.Egisp.Point.SearchFailedBranches = function() {
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/queryFailedPoints?",
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess && e.result) {
                if(e.result.records) {
                    Map.showBranchesToMap(e.result.records);
                    SuperMap.Egisp.Point.Table.refresh(e.result.records);
                }
            }
            else {
                SuperMap.Egisp.showHint('查询失败');
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();  
            SuperMap.Egisp.showHint('查询失败');
        }   
    });
}

/** 
 * 获取网点样式
 */
SuperMap.Egisp.Point.BranchStyles = ['red', 'orange', 'green', 'tea', 'blue', 'purple', 'deep-purple', 'brown'];
/** 
 * 获取网点样式 列表可供选择
 */
SuperMap.Egisp.Point.getIconsListHtml = function() {
    var icons = SuperMap.Egisp.Point.BranchStyles.concat();
    var len_icons = icons.length;
    var h = '';
        h += '<div class="map-popup-icons">';
        h += '  <div class="title">';
        h += '      <a class="popup-close close-icons" title="返回"></a>';
        h += '  </div>';
    for(var i=0; i<len_icons; i++) {
        var src = 'assets/map/'+ icons[i] +'/';
        h += '<ul>';
        h += '  <li><img src="'+ src +'s.png" data-src="'+ src +'s.png"></li>';
        h += '  <li><img src="'+ src +'m.png" data-src="'+ src +'m.png"></li>';
        h += '  <li><img src="'+ src +'b.png" data-src="'+ src +'b.png"></li>';
        h += '</ul>';
    }
    h += '</div>';
    return h;
}
/** 
 * 获取新增网点popup的header
 */
SuperMap.Egisp.Point.getAddPopupHeaderHtml = function() {
    var h = '';    
    h += '  <div class="title">';
    h += '      <span class="name">新增网点</span>';
    h += '      <a class="popup-close close-popup" title="取消"></a>';
    h += '      <a class="popup-sure" title="确认"></a>';
    h += '  </div>';
    return h;
}
/** 
 * 获取新增网点popup的表单
 * @param: type - 类型，1为新增，2为修改
 */
SuperMap.Egisp.Point.getAddPopupFormHtml = function(type, marker, feature_name, feature_id, attr) {
    var lonlat, action, me=null, netPicPath = 'assets/map/branch.png', iconStyle = 'assets/map/red/s.png';
    if( type === 1 ) {
        lonlat = marker;
        action = urls.server + "/pointService/add?moduleId=" + SuperMap.Egisp.User.currentModuleId;
    }
    else if( type === 2 ) {
        lonlat = marker.lonlat;
        action = urls.server + "/pointService/update?moduleId=" + SuperMap.Egisp.User.currentModuleId;
        me = marker;
        netPicPath = me.netPicPath ? 
            urls.server +'/pointService/getImg?path=' + me.netPicPath
            : "assets/map/branch.png";
        iconStyle = me.iconStyle ? me.iconStyle : 'assets/map/red/s.png';
    }
    else if( type === 3 ) {
        me = attr;
        lonlat = attr.lonlat;
        action = urls.server + "/pointService/add?moduleId=" + SuperMap.Egisp.User.currentModuleId;        
    }
    else {
        return false;
    }

    var defaultStyle = SuperMap.Egisp.Point.Style.defaultStyle;
    if( me && me.groupid && me.groupid.styleid) {
        me.styleid = me.groupid.styleid;
        me.styleid.groupid = me.groupid.id;
    }
    
    var attrString  = ' data-stylename="' + (me && me.styleid && me.styleid.stylename ? SuperMap.Egisp.setStringEsc(me.styleid.stylename) : '') + '"';
        attrString += ' data-ico="' + (me && me.styleid && me.styleid.apppic ? me.styleid.apppic : defaultStyle.ico) + '"';
        attrString += ' data-back="' + (me && me.styleid && me.styleid.appearance ? me.styleid.appearance  : defaultStyle.back) + '"';
        attrString += ' data-color="' + (me && me.styleid && me.styleid.appcolor ? me.styleid.appcolor : defaultStyle.color) + '"';
        attrString += ' data-customfileid="' + (me && me.styleid && me.styleid.appcustom ? me.styleid.appcustom.replace(/\\/g, '/') : defaultStyle.img) + '"';
        attrString += ' data-size="' + (me && me.styleid && me.styleid.appsize ? me.styleid.appsize : defaultStyle.size) + '"';
        attrString += ' data-groupid="' + (me && me.styleid && me.styleid.groupid ? me.styleid.groupid : 'none') + '"';

    var h = '';        
    h += '  <form id="form_add_branch" method="POST" enctype="multipart/form-data" action="'+ action +'" >';
    h += '  <div class="content">';
    h += '      <div class="pictures">';    
    h += '          <div class="branch-icon-popup" '+ attrString+ ' title="点击更改网点图标"><span></span></div>';
    if( !urls.ie_case ) {
        h += '      <div class="photo" id="photo"><img src="'+ netPicPath +'" title="点击上传网点照片"></div>';
    }    

    h += '          <div class="caption" '+ (urls.ie_case ? 'style="width:60px;"' : '') +'>';
    h += '              <span class="first">更改图标</span>';

    if( !urls.ie_case ) {
        h += '              <span class="second">上传门店照片</span>';
    }
    h += '          </div>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">名称：</span>';
    var name = me ? (me.name ? me.name : "" ) : "";
    h += '          <input class="input" id="txt_name" name="name" value="'+ SuperMap.Egisp.setStringEsc(name) +'" type="text" maxlength="50">';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">地址：</span>';
    var address = me ? (me.address ? me.address : "" ) : "";
    h += '          <input class="input" id="txt_address" name="address" value="'+ SuperMap.Egisp.setStringEsc(address) +'" type="text" maxlength="50">';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">范围：</span>';
    var areaName = me ? (me.areaName ? me.areaName : "" ) : "";
    if( type === 1 || (areaName === "" && type === 2) ) {
        areaName = feature_name;
    }
    h += '          <input class="input-disable" id="txt_areaName" value="'+areaName+'" type="text" readonly="readonly">';
    h += '          <span>请在地图上选择已有区划</span>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">车辆：</span>';
    h += '          <input class="input-disable" id="txt_car_license"  value="" type="text" readonly="readonly">';
    h += '          <a class="choose-cars">选择车辆</a>';
    h += '      </div>';
    var areaId = me ? (me.areaId ? me.areaId : "" ) : "";
    if( type === 1 || (areaName === "" && type === 2) ) {
        areaId = feature_id;
    }
    h += '      <div class="hide">';
    h += '          <input class="input" id="txt_areaId"  name="areaId" value="'+ areaId +'" type="text">';
    h += '      </div>';

    /*if(Baidu && Baidu.using) {
        var coord = Baidu.restoreCoord(lonlat.lon, lonlat.lat);
        lonlat.lon = coord.x;
        lonlat.lat = coord.y;
    }*/
    h += '      <div class="hide">';
    h += '          <input class="input"  name="smx" value="'+ lonlat.lon +'" type="text">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input class="input" name="smy" value="'+ lonlat.lat +'" type="text">';
    h += '      </div>';
    
    h += '      <div class="hide">';
    h += '          <input class="input" name="iconStyle" value="'+ iconStyle +'" type="text">';
    h += '      </div>';
    h += '      <div ' + (!urls.ie_case ? 'class="hide">' : 'class="attr"><span class="label">照片：</span>');
    h += '          <input class="input-file" name="netPicFile" id="txt_file" type="file"' + (!urls.ie_case ? ' onchange="previewImage(this)"' : '');
    h += '        accept="image/*" >';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="carIds" id="txt_car_ids" type="text" value="">';
    h += '      </div>';
    if(type === 2) {
        h += '  <div class="hide">';
        h += '      <input class="input" name="id" value="'+ me.id +'" type="text">';
        h += '  </div>';        
    }
    h += '      <div class="hide">';
    h += '          <input name="styleid" type="text" value="'+ (me && me.styleid && me.styleid.id ? me.styleid.id : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="stylename" type="text" value="'+ (me && me.styleid && me.styleid.stylename ? me.styleid.stylename : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="appearance" type="text" value="'+ (me && me.styleid && me.styleid.appearance ? me.styleid.appearance : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="appsize" type="text" value="'+ (me && me.styleid && me.styleid.appsize ? me.styleid.appsize : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="appcolor" type="text" value="'+ (me && me.styleid && me.styleid.appcolor ? me.styleid.appcolor : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="apppic" type="text" value="'+ (me && me.styleid && me.styleid.apppic ? me.styleid.apppic : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="customfileid" type="text" value="'+ (me && me.styleid && me.styleid.customfileid ? me.styleid.customfileid : '') +'">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <input name="groupid" type="text" value="'+ (me && me.styleid && me.styleid.groupid ? me.styleid.groupid : '') +'">';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="hint"></span>';
    h += '      </div>';
    h += '  </div>';
    h += '  </form>';
    return h;    
}
/** 
 * 获取网点属性popup内容
 */
SuperMap.Egisp.Point.getAttrPopupHtml = function(marker, editable) {    
    var me = marker;
    var netPicPath = me.netPicPath ? 
        urls.server +'/pointService/getImg?path=' + me.netPicPath 
        : "assets/map/branch.png";

    var h = '';        
    h += '  <div class="title">';
    h += '      <span class="name" data-key="name" title="'+ SuperMap.Egisp.setStringEsc(me.name) +'" option="popup-attr">'+ SuperMap.Egisp.setStringEsc(me.name) +'</span>';
    h += '      <a class="popup-close" title="关闭"></a>';
    if( editable ) {
        h += '  <a class="popup-delete" title="删除网点"></a>';
        h += '  <a class="popup-edit" title="编辑网点"></a>';        
    }
    h += '  </div>';
    h += '  <div class="content">';
    h += '      <div class="pictures">';
    h += '          <div class="photo"><img src="'+ netPicPath +'"></div>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">地址：</span>';
    h += '          <span class="text" data-key="address" option="popup-attr">'+ SuperMap.Egisp.setStringEsc(me.address) +'</span>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">范围：</span>';
    h += '          <span class="text">'+ (me.areaName ? me.areaName : "") +'</span>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">车辆：</span>';
    var bind_cars = '';    
    h += '          <span class="text bind-cars">'+ bind_cars +'</span>';
    h += '      </div>';
    h += '  </div>';
    return h;
}

/** 
 * 获取云平台搜索的poi的popup的header
 */
SuperMap.Egisp.Point.getCloudPopupHtml = function(me, option) {
    var h = '';    
    h += '  <div class="title">';
    h += '      <span class="name"  title="'+ me.name +'" >'+ me.name +'</span>';
    h += '      <a class="popup-close close-popup" title="关闭"></a>';
    h += '  </div>';
    h += '  <div class="content">';
    h += '      <div class="attr">';
    h += '          <span class="label">地址：</span>';
    h += '          <span class="text" data-key="address" option="popup-attr">'+ (me.address ? me.address : "暂无详细信息") +'</span>';
    h += '      </div>';
    if( typeof(option) !== 'undefined' && option ) {
        h += '  <div class="attr">';
        h += '      <a class="btn btn-info btn-sm a-save-as-branch">保存为网点</a>';
        h += '  </div>';        
    }
    h += '  </div>';
    return h;
}

/** 
 * 获取网点样式
 */
SuperMap.Egisp.Point.getBranchStyle = function(branch) {
    var defaultStyle = SuperMap.Egisp.Point.Style.defaultStyle;

    var icon, style = {}, styleid = false;

    if(branch.groupid && branch.groupid.styleid) {
        styleid = branch.groupid.styleid;
    }
    else if(branch.styleid) {
        styleid = branch.styleid;
    }

    if(styleid) {
        if(styleid.appcustom) {
            var h = styleid.def1 ? styleid.def1.split(',')[1] : 24;
            var w = styleid.def1 ? styleid.def1.split(',')[0] : 24;
            icon = SuperMap.Egisp.Point.Style.getStyleSize(h,'transparent','fff'); 
            style.externalGraphic = urls.server + '/pointService/getImg?path='+ styleid.appcustom;
            style.graphicWidth = w;
            style.graphicHeight = h;
        }
        else {
            icon = SuperMap.Egisp.Point.Style.getStyleSize( 
                styleid.appsize, 
                styleid.appearance ? styleid.appearance : 'transparent', 
                styleid.appcolor
            );
            style.isUnicode = styleid.apppic ? true : false;
            style.label = SuperMap.Egisp.Point.Style.getFontLabel(styleid.apppic);
            style.fontSize = styleid.appsize;
            style.labelYOffset = 5;

            var color_path = SuperMap.Egisp.Point.Style.getColor(styleid.appcolor);
            style.externalGraphic = 'assets/map/'+ color_path.path +'/'+ styleid.appearance +'.svg';    
            style.graphicWidth = icon.size.w;
            style.graphicHeight = icon.size.h;             
        }
    }
    else {
        var s = SuperMap.Egisp.Point.Style.defaultStyle;
        if(s.img != '') {
            icon = SuperMap.Egisp.Point.Style.getStyleSize( s.height,'transparent','fff');
            style.externalGraphic = urls.server + '/pointService/getImg?path='+  (s.img);
            style.graphicWidth = s.width;
            style.graphicHeight = s.height;
        }
        else {
            icon = SuperMap.Egisp.Point.Style.getStyleSize( s.size, s.back, s.color);
            style.externalGraphic = 'assets/map/'+ s.colorpath +'/'+ s.back +'.svg';
            style.isUnicode = s.ico ? true : false;
            style.label = SuperMap.Egisp.Point.Style.getFontLabel(s.ico);
            style.fontSize = s.size;    
            style.graphicWidth = icon.size.w;
            style.graphicHeight = icon.size.h;        
        }
    }

    style.graphicTitle = branch.name ? branch.name : '';
    style.fontColor = '#' + icon.color;
    style.fontFamily = "iconfont";
    style.cursor = 'pointer';
    style.labelXOffset = icon.offset.x;
    style.labelYOffset = icon.offset.y;
    return style;
}

/** 
 * 获取全国范围下聚合网点样式
 */
SuperMap.Egisp.Point.getNationalBranchStyle = function() {
    return {
        externalGraphic: 'assets/map/blue/circle.svg',
        graphicWidth: 50,
        graphicHeight: 50,
        fontColor: '#ffffff',
        fontFamily: 'microsoft yahei',
        cursor: 'pointer',
        fontSize: '12'
    }
}
/** 
 * 获取编辑网点popup的header
 */
SuperMap.Egisp.Point.getEditPopupHeaderHtml = function(name, isDelete) {
    var h = '';    
    h += '  <div class="title">';
    h += '      <span class="name"  title="'+ SuperMap.Egisp.setStringEsc(name) +'" >'+ SuperMap.Egisp.setStringEsc(name) +'</span>';
    h += '      <a class="popup-close close-popup" title="取消"></a>';
    if( isDelete ) {
        h += '  <a class="popup-delete" title="删除网点"></a>';
    }
    h += '      <a class="popup-sure" title="确定"></a>';
    h += '  </div>';
    return h;
}

/** 
 * 获取网点绑定的车辆
 */
SuperMap.Egisp.Point.getBindingCars = function(id, success, failed) {
	if( !id ) {
		return;
	}
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/queryBindCars?",
        data: {id: id},
        success: function(e){
            var license = "", ids = "", data = [];
            if(e.isSuccess && e.result) {
                if( e.result.length === 0 ) {
                    success(license, ids, data);
                }
                else {
                    var len = e.result.length;
                    for( var i=len; i--; ) {
                        if( ids !== "" ) {
                            license += "，";
                            ids += "_";
                        }
                        license += e.result[i].license;
                        ids += e.result[i].id;
                    }
                    success(license, ids, e.result );
                }
            }
            else {
                success(license, ids, data);
            }                   
        },
        error: function(){
            failed();
        }
    });
}
/** 
 * 删除网点
 */
SuperMap.Egisp.Point.remove = function(id, success, failed) {
    if( !id ) {
        return;
    }
    SuperMap.Egisp.showMask();
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/delete?",
        data: {id: id},
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed();
            }
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed();
        }        
    });
}
/** 
 * 搜索POI
 */
SuperMap.Egisp.Point.searchFromCloud = function(p, success, failed) {
	if( !p ) {
		return;
	}
    SuperMap.Egisp.showMask();
    var pager = $("#pager_cloudpois");
    var pageIndex = Number(pager.attr('page'));

    p.startRecord = 10*pageIndex;
    p.expectCount = 10;

    var param =  {
        parameter: JSON.stringify(
            {
                startRecord: p.startRecord,
                expectCount: p.expectCount,
                returnFields: ["smid","smx", "smy", "name","poi_id",
                "zipcode","address","telephone","admincode"],
                filter: "Name like '%" + p.keyword + "%'" 
                    +
                    " and x<'" + p.max_x + "'" +
                    " and y<'" + p.max_y + "'" +
                    " and x>'" + p.min_x + "'" +
                    " and y>'" + p.min_y + "'"      
            }
        )
    };
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/poiSearch?",
        data: param,        
        success: function(e){
            SuperMap.Egisp.hideMask();
            if(e && e.results) {
                if( e.results.length > 0 ) {                    
                    var html_page = SuperMap.Egisp.setPage(e.totalCount, pageIndex, '\'pager_cloudpois\''); 
                    $("#pager_cloudpois > ul").html(html_page);   
                    $("#pager_cloudpois").show();   
                    $(".page-cloud-pois").html( '第' + (pageIndex+1) + '/' + Math.ceil(e.totalCount/10) + '页' );
                    success(e);
                }
                else {
                    failed();
                }
                return;
            }
            failed();
        },
        error: function(){
            SuperMap.Egisp.hideMask();
            failed();
        }
    });
}

/** 
 * 显示云平台搜索结果到页面中
 */
SuperMap.Egisp.Point.getCloudSearchHtml = function(data) {
    var len = data.length;
    var h =  '<ul class="list-group">';
    for(var i=0; i<len; i++) {
        var item = data[i];
        h += '  <li class="list-group-item">' + (i+1);
        h += '      <a href="javascript:void(0);" option="to-cloud-poi" data-id="'+ item.poi_id +'">'+ item.name +'</a>';
        h += '  </li>';
    }
        h += '</ul>';
    return h;
}

/** 
 * 反向地址匹配
 * @param - Object - { smx: x, smy: y }
 */
SuperMap.Egisp.Point.regeocode = function(param, success, failed) {
    if( !param || !param.smx || !param.smy ) {
        return;
    }
    SuperMap.Egisp.request({
        url: urls.server + "/orderService/reverseMatch?",
        data: param,
        success: function(e){
            if(e.result) {
                success(e.result);
            }
            else {
                failed();
            }
        },
        error: function(){
            failed();
        }        
    });
}
/** 
 * 网点列表 表头所有字段的key集合
 */
SuperMap.Egisp.Point.Columns = [];
/** 
 * 网点列表 不可单元格修改的字段
 */
SuperMap.Egisp.Point.unEditableColumns = ['order', 'smx', 'smy', 'areaName', 'createTime', 'updateTime'];

/** 
 * 网点列表，系统字段
 */
SuperMap.Egisp.Point.sysColumns = {
    order: "序号",
    name: "网点名称",
    address: "网点地址",
    smx: "经度",
    smy: "纬度",
    areaName: "绑定区划",
    createTime: "创建时间",
    updateTime: "修改时间",
    group: "所属分组"
};

/** 
 * 网点列表，系统字段表头创建
 */
SuperMap.Egisp.Point.createSysThead = function() {
    SuperMap.Egisp.Point.Columns = [];
    var sysColumns = SuperMap.Egisp.Point.sysColumns;
    var h = '<tr>';
    var key;
    for( key in sysColumns ) {     
        var style = SuperMap.Egisp.Point.getTableTdStyleName( key );   
        h += '<td class="'+ style +'" id="td_'+ key +'">';
        h += SuperMap.Egisp.Point.getSysHeadDropMenu( key, sysColumns[key] );
        h += '</td>';
        SuperMap.Egisp.Point.Columns.push( key );
    }
    h += '</tr>';
    $('#table_branches > thead').html( h );
}

/** 
 * 网点列表，表头创建
 */
SuperMap.Egisp.Point.getSysHeadDropMenu = function( key, value ) {
    var h =  '<div class="dropdown">';
        h += '  <a class="a-head" id="a_head_'+ key +'" href="javascript:void(0);" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
        h += SuperMap.Egisp.setStringEsc(value);
        h += '      <span class="caret"></span>';     
        h += '  </a>'; 
        h += '  <ul class="dropdown-menu" aria-labelledby="a_head_'+ key +'">';      
        h += '      <li><a href="javascript:void(0);" option="add-column">新增列</a></li>';      
        h += '  </ul>';      
        h += '</div>';  
    return h;
}
/** 
 * 网点列表 获取表格长度
 */
SuperMap.Egisp.Point.getTableTdStyleName = function(key) {
    switch( key ) {
        case 'order':
            return 'td-60';
        case 'address':
            return 'td-300';
        case 'name':
            return 'td-200';
        default:
            return 'td-150';
    }
}

/** 
 * 网点列表，自定义字段
 */
SuperMap.Egisp.Point.CustomColumns = {};
/** 
 * 获取自定义字段
 */
SuperMap.Egisp.Point.getCustomColumns = function(first) {
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/getUserPointExtcol?",
        data: {},
        success: function(e){
            if( e && e.result ) {
                SuperMap.Egisp.Point.CustomColumns = e.result;
                SuperMap.Egisp.Point.addCustomColumnHead();
            }
            SuperMap.Egisp.Point.cssDropdownMenu();
            if( typeof(first) !== 'undefined' && first === true ) {
                Map.searchBranches();
            }
        },
        error: function() {
            SuperMap.Egisp.Point.cssDropdownMenu();  
            if( typeof(first) !== 'undefined' && first === true ) {
                Map.searchBranches();
            }         
        }        
    });
}
/** 
 * 网点列表 添加自定义列的表头
 */
SuperMap.Egisp.Point.addCustomColumnHead = function() {
    var columns = SuperMap.Egisp.Point.CustomColumns;
    var h = '';
    var key, keys = [];
    for( key in columns ) {    
        h += '<td class="td-150" id="td_'+ key +'">';
        h += SuperMap.Egisp.Point.getCustomHeadDropMenu( key, columns[key] );
        h += '</td>';
        keys.push( key );
    }
    $('#td_smx').before( h );

    SuperMap.Egisp.Point.insertColsToColumns( keys ); 

}
/** 
 * 往表头中添加自定义的字段
 * @param - keys 
 */
SuperMap.Egisp.Point.insertColsToColumns = function(keys) {
    var columns = SuperMap.Egisp.Point.Columns;
    var index = columns.indexOf('smx');
    var pre = columns.slice(0, index);
    var after = columns.slice( index );
    pre = pre.concat(keys);
    SuperMap.Egisp.Point.Columns = pre.concat( after );
    SuperMap.Egisp.Point.cssDropdownMenu();
}

/** 
 * 网点列表 修改表头二级菜单的样式使其居中
 */
SuperMap.Egisp.Point.cssDropdownMenu = function() {    
    $('.dropdown-menu').each(function(){
        var me = $(this);
        var parent_width = me.parent('div').parent('td').css('min-width');
        if(!parent_width) {
            return;
        }
        parent_width = Number( parent_width.replace("px", "") );
        var left = ( parent_width - 100 )*0.5 ;
        if( left > 0 ) {
            me.css({ left: left + 'px' });          
        }
    });

    //绑定添加列的事件
    $('a[option="add-column"]').unbind('click').click(function(){
        $('.data-column-add').fadeIn('fast');
        $('.text-column-add').val("");
    });
    //绑定删除列的事件
    $('a[option="remove-column"]').unbind('click').click(function(){
        var me = $(this);
        SuperMap.Egisp.Modal.alert("确定删除列 \"" + me.attr('data-value') + "\"吗？数据删除后不可恢复。", 
            SuperMap.Egisp.Point.Table.removeColumn, 
            {
                'data-value': me.attr('data-value'),
                'data-key': me.attr('data-key')
            }
        );
    });

    $('a[option="rename-column"]').unbind('click').click(Page.renameColumn);
}

/** 
 * 网点列表，自定义表头创建
 */
SuperMap.Egisp.Point.getCustomHeadDropMenu = function( key, value ) {
    var h =  '<div class="dropdown">';
        h += '  <a class="a-head" id="a_head_'+ key +'" href="javascript:void(0);" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
        h += SuperMap.Egisp.setStringEsc(value);
        h += '      <span class="caret"></span>';     
        h += '  </a>'; 
        h += '  <ul class="dropdown-menu" aria-labelledby="a_head_'+ key +'">';      
        h += '      <li><a href="javascript:void(0);" option="add-column">新增列</a></li>';      
        h += '      <li><a href="javascript:void(0);" option="rename-column" data-key="'+ key +'" data-value="'+ value +'">重命名</a></li>';      
        h += '      <li><a href="javascript:void(0);" option="remove-column" data-key="'+ key +'" data-value="'+ value +'">删除列</a></li>';      
        h += '  </ul>';      
        h += '</div>';  
    return h;
}

/**
 * 设置弹窗中的图标style
 */
SuperMap.Egisp.Point.setPopupDivStyle = function(){
    var me = $('.branch-icon-popup'), icon = {}, li=null;
    icon.name = me.attr('data-stylename');
    icon.back = me.attr('data-back');
    icon.size = me.attr('data-size'); 
    icon.color = me.attr('data-color');
    icon.ico = me.attr('data-ico');
    icon.groupid = me.attr('data-groupid');
    icon.img = me.attr('data-customfileid') != "" ? me.attr('data-customfileid') : '';
    /*if(me.attr('data-customfileid') != "") {
        li = $('.icon-selector .custom li[data-path="'+ icon.img +'"]');
        icon.img =  me.attr('data-customfileid');
    }*/
    if(icon.img && icon.img.length > 0) {
        // icon.img.replace(/\\/g, '\/');
        li = $('.icon-selector .custom li[data-path="'+ icon.img +'"]');
        var style = SuperMap.Egisp.Point.Style.getStyleSize(li.attr('data-height'),'transparent','fff');
        me.css({
            'background': '#fff url('+ urls.server + '/pointService/getImg?path='+ icon.img +') no-repeat center center',
            'background-size': style.size.w + 'px ' + style.size.h + 'px'
        });
        me.find('span').removeClass();
    }
    else {
        var style = SuperMap.Egisp.Point.Style.getStyleSize( icon.size > 20 ? 20 : icon.size, icon.back, icon.color);
        var li_color = $('.icon-selector .color li[data-color="'+ icon.color +'"]');
        me.css({  
            'background': '#fff url(assets/map/' + li_color.attr('data-color-path') + '/' + icon.back + '.svg) no-repeat center center',
            'background-size': style.size.w + 'px ' + style.size.h + 'px'
        });
        var l_h = (style.offset.line_height + (60-style.size.h)) + 'px';
        me.find('span')
        .removeClass()
        .addClass('iconfont '+ icon.ico)
        .css({
            'font-size': (icon.size > 20 ? 20 : icon.size) + 'px',
            'color': '#' + style.color,
            'line-height': l_h
        });
    }
}

