
/** 
 * 企业版在线接口
 * 命名空间:Dituhui\Dituhui.Point
 */
Dituhui.Point = Dituhui.Point || {};
/**
 * 查询
 */
Dituhui.Point.Search = function(param, success, failed) {
	if( !param || !param.pageNo) {
		return;
	}
    Dituhui.showMask();

    var smcity = $('.smcity');
    var admincode = smcity.attr('admincode');
    var level = smcity.attr('level');
    if(level && level != '') {
        param.admincode = admincode;
        param.level = level;
    }

    Dituhui.request({
        url: urls.server + "/pointService/queryAllPoint?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
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
            Dituhui.hideMask();
            failed();
        }
    })
}
/*
 * 查询聚合
 */
Dituhui.Point.SearchByCluster = function(param, success, failed) {
	if( !param || !param.pageNo) {
		return;
	}
    Dituhui.showMask();

    var smcity = $('.smcity');
    var admincode = smcity.attr('admincode');
    var level = smcity.attr('level');
    if(level && level != '') {
        param.admincode = admincode;
        param.level = level;
    }

    Dituhui.request({
        url: urls.server + "/pointService/queryAllForConvergeWeb?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
            	if(e.result == null || !e.result) {
            		e.result = {
            			records: []
            		}
            	}
                if(e.result && e.result.records && e.result.records != null && e.result.records.length !== 0 ) {
                   e.result.level = param.level;
                }
                success(e);
            }
            else {
                failed(e.info ? e.info : "网点查询失败");
            }
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }
    });
}
Dituhui.Point.getImportBranches = function(callback) {
    Dituhui.request({
        url: urls.server + "/pointService/getAllProcessingPoint?",
        dataType: 'json',
        success: function(e){
            if(e.isSuccess && e.result) {
                var info = '有' + e.result + '个导入的网点未解析，是否进行解析？<br>点击“确定”解析，点击“取消”删除未处理的网点。';
                var attr = {};
                Dituhui.Modal.ask(info, function(){
                    Dituhui.Modal.loading('正在解析数据...<br>');
                    Dituhui.Point.analysisPoints(true, function(){
                        Dituhui.Modal.loaded_right('数据解析完成。<br>', Map.afterImportRight); 
                    }, function(r){
                        Dituhui.Modal.loaded_wrong('数据解析失败<br>' + r, Map.afterImportWrong);
                    });
                }, attr, function(){
                    Dituhui.showMask();
                    Dituhui.Point.analysisPoints(false, function(){                        
                        Dituhui.hideMask();
                        Dituhui.Modal.hide();
                        Map.searchBranches();
                    }, function(r){
                        Dituhui.hideMask();
                        Dituhui.Modal.hide();
                        Map.searchBranches();
                    });
                });
            }
        },
        error: function(){}
    })
}
Dituhui.Point.analysisPoints = function(iscontinue, success, error) {
    Dituhui.request({
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
Dituhui.Point.SearchFailedBranches = function() {
    Dituhui.request({
        url: urls.server + "/pointService/queryFailedPoints?",
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess && e.result) {
                if(e.result.records) {
                    Map.showBranchesToMap(e.result.records);
                    Dituhui.Point.Table.data_failed = e.result.records;
                    Dituhui.Point.Table.refresh(e.result.records);
                }
            }
            else {
                Dituhui.showHint('查询失败');
            }
        },
        error: function(){
            Dituhui.hideMask();  
            Dituhui.showHint('查询失败');
        }
    });
}
/*
 * 移动端采集
 */
Dituhui.Point.SearchFromMobile = function(param, success, failed) {
	if( !param || !param.mapurl) {
		return;
	}
    Dituhui.showMask();

    Dituhui.request({
        url: urls.server + "/pointService/syncCdituhuiPoint?",
        data: param,
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                success( e );
            }
            else {
                failed(e.info ? e.info : "网点采集失败");
            }
        },
        error: function(){
            Dituhui.hideMask();
            failed(false);
        }
    });
}

/*
 * 获取用户大众版的地图
 */
Dituhui.Point.SearchMapsFromMobile = function(success, failed) {
    Dituhui.showMask();
    
      var uid = "5020284366"; //鑫之源客户ID
//  var uid = "313740771"; //liuyin大众版ID
    
    Dituhui.request({
        //url: "http://www.dituhui.com/api/v2/users/5020284366/maps.json", 
        url: urls.server + "/user/getDituhuiUserMap?",
        data: {
        	cuserid: uid
        },
        success: function(e){
            Dituhui.hideMask();
            if(e && e.length > 0) {
            	success( e );
            }
            else {
                failed("未获取到您在大众版的点标记地图");
            }
        },
        error: function(e){
            Dituhui.hideMask();
            failed("未获取到您在大众版的点标记地图");
        }
    });
}

/** 
 * 获取网点样式
 */
Dituhui.Point.BranchStyles = ['red', 'orange', 'green', 'tea', 'blue', 'purple', 'deep-purple', 'brown'];
/** 
 * 获取网点样式 列表可供选择
 */
Dituhui.Point.getIconsListHtml = function() {
    var icons = Dituhui.Point.BranchStyles.concat();
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
Dituhui.Point.getAddPopupHeaderHtml = function() {
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
 * @param: type - 类型，1为新增，2为修改,3为POI保存为网点
 */
Dituhui.Point.getAddPopupFormHtml = function(type, marker, feature_name, feature_id, attr) {
    var lonlat, action, me=null, netPicPath = urls.server + '/resources/css/imgs/point/add.png', iconStyle = urls.server + '/resources/assets/map/red/s.png';
    if( type === 1 ) {
        lonlat = marker;
        action = urls.server + "/pointService/add?moduleId=" + Dituhui.User.currentModuleId;
    }
    else if( type === 2 ) {
        lonlat = marker.lonlat;
        action = urls.server + "/pointService/update?moduleId=" + Dituhui.User.currentModuleId;
        me = marker;
        netPicPath = me.netPicPath ? 
            urls.server +'/pointService/getImg?path=' + me.netPicPath
            : urls.server + "/resources/css/imgs/point/add.png";
        iconStyle = me.iconStyle ? me.iconStyle : urls.server + '/resources/assets/map/red/s.png';
    }
    else if( type === 3 ) {
        me = attr;
        lonlat = attr.lonlat;
        action = urls.server + "/pointService/add?moduleId=" + Dituhui.User.currentModuleId;        
    }
    else {
        return false;
    }

    var defaultStyle = Dituhui.Point.Style.defaultStyle;
    if( me && me.groupid) {
        if(me.groupid.styleid) {
            me.styleid = me.groupid.styleid;
            me.styleid.groupid = me.groupid.id;
        }
        else {
            me.styleid = {};
            me.styleid.groupid = me.groupid.id;
        }
    }
    
    var attrString  = ' data-stylename="' + (me && me.styleid && me.styleid.stylename ? Dituhui.setStringEsc(me.styleid.stylename) : '') + '"';
        attrString += ' data-ico="' + (me && me.styleid && me.styleid.apppic ? me.styleid.apppic : defaultStyle.ico) + '"';
        attrString += ' data-back="' + (me && me.styleid && me.styleid.appearance ? me.styleid.appearance  : defaultStyle.back) + '"';
        attrString += ' data-color="' + (me && me.styleid && me.styleid.appcolor ? me.styleid.appcolor : defaultStyle.color) + '"';
        attrString += ' data-customfileid="' + (me && me.styleid && me.styleid.appcustom ? me.styleid.appcustom.replace(/\\/g, '/') : defaultStyle.img) + '"';
        attrString += ' data-size="' + (me && me.styleid && me.styleid.appsize ? me.styleid.appsize : defaultStyle.size) + '"';
        attrString += ' data-groupid="' + (me && me.styleid && me.styleid.groupid ? me.styleid.groupid : '0') + '"';

    var h = '';        
    h += '  <form id="form_add_branch" method="POST" enctype="multipart/form-data" action="'+ action +'" >';
    h += '  <div class="content">';
    h += '      <div class="pictures">';    
    h += '          <div class="branch-icon-popup" '+ attrString+ ' title="点击更改网点图标"><span></span></div>';
    if( !urls.ie_case ) {
        h += '      <div class="photo no-border '+ (type!=2 ? 'hide' : 'edit') +'" id="photo"><div class="caption">上传图片</div></div>';
    }
    h += '          <div class="caption" '+ (urls.ie_case ? 'style="width:60px;"' : '') +'>';
    h += '              <span class="first">更改图标</span>';
    h += '          </div>';
    h += '          <div class="preview-img '+ (type!=2 ? 'hide' : '') +'">';
    h += '          </div>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">名称：</span>';
    var name = me ? (me.name ? me.name : "" ) : "";
    h += '          <input class="input" id="txt_name" name="name" value="'+ Dituhui.setStringEsc(name) +'" type="text" maxlength="50">';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">地址：</span>';
    var address = me ? (me.address ? me.address : "" ) : "";
    h += '          <input class="input" id="txt_address" name="address" value="'+ Dituhui.setStringEsc(address) +'" type="text" maxlength="50">';
    h += '      </div>';

    var cols = Dituhui.Point.ConfigCols.concat();
    var customCols = Dituhui.Point.CustomColumns;
    for(var m in customCols) {
        if( cols.indexOf(m) != -1 && type==2 ) {
            h += '<div class="attr popup-row'+ m +'">';
            h += '  <span class="label" title="'+ Dituhui.setStringEsc(customCols[m]) +'">'+ Dituhui.setStringEsc(customCols[m]).substr(0, 4) +'：</span>';
            h += '  <input class="input"  name="'+ m +'" value="'+ (me[m] ? Dituhui.setStringEsc(me[m]) : '')+'" type="text">';
            h += '</div>';
        }
    }

    h += '      <div class="hide">';
    h += '          <span class="label">车辆：</span>';
    h += '          <input class="input-disable" id="txt_car_license"  value="" type="text" readonly="readonly">';
    h += '          <a class="choose-cars">选择车辆</a>';
    h += '      </div>';

    /*if(Baidu && Baidu.using) {
        var coord = Baidu.restoreCoord(lonlat.lon, lonlat.lat);
        lonlat.lon = coord.x;
        lonlat.lat = coord.y;
    }*/

    var coord = Dituhui.metersToLatLon( new SuperMap.LonLat(lonlat.lon, lonlat.lat) );
    if( cols.indexOf('smx') != -1 && type==2 ) {
        h += '      <div class="attr">';
        h += '          <span class="label">经度：</span>';
        h += '          <input class="input smx-popup" value="'+ coord.lon.toFixed(2) +'" type="text" readonly="true">';
        h += '      </div>';
    }
    if( cols.indexOf('smy') != -1 && type==2 ) {
        h += '      <div class="attr">';
        h += '          <span class="label">纬度：</span>';
        h += '          <input class="input smy-popup" value="'+ coord.lat.toFixed(2) +'" type="text" readonly="true">';
        h += '      </div>';
    }

    h += '      <div class="hide">';
    h += '          <span class="label">经度：</span>';
    h += '          <input class="input"  name="smx" value="'+ lonlat.lon +'" type="text">';
    h += '      </div>';
    h += '      <div class="hide">';
    h += '          <span class="label">纬度：</span>';
    h += '          <input class="input" name="smy" value="'+ lonlat.lat +'" type="text">';
    h += '      </div>';
    
    h += '      <div class="hide">';
    h += '          <input class="input" name="iconStyle" value="'+ iconStyle +'" type="text">';
    h += '      </div>';
    h += '      <div ' + (!urls.ie_case ? 'class="hide">' : 'class="attr"><span class="label">图片：</span>');
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
    h += '          <input name="customfileid" type="text" value="'+ (me && me.styleid && me.styleid.appcustomid ? me.styleid.appcustomid : '') +'">';
    h += '      </div>';


    h += '      <div class="'+ (cols.indexOf('areaName') != -1 && type==2 ? 'attr' : 'hide' ) +'">';
    h += '          <span class="label">范围：</span>';
    var areaName = me ? (me.areaName ? me.areaName : "" ) : "";
    if( type === 1 || (areaName === "" && type === 2) ) {
        areaName = feature_name;
    }
    h += '          <input class="input-disable" id="txt_areaName" value="'+areaName+'" type="text" readonly="readonly">';
    h += '          <span>请在地图上选择已有区划</span>';
    h += '      </div>';
    var areaId = me ? (me.areaId ? me.areaId : "" ) : "";
    if( type === 1 || (areaName === "" && type === 2) ) {
        areaId = feature_id;
    }
    h += '      <div class="hide">';
    h += '          <input class="input" id="txt_areaId"  name="areaId" value="'+ areaId +'" type="text">';
    h += '      </div>';

    if(cols.indexOf('group') != -1 && type==2) {
        h += '  <div class="attr">';
        h += '      <span class="label">选择分组：</span>';
        h += '      <select class="select-group-popup input" style="height:22px;"></select>';
        h += '  </div>';
    }
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
Dituhui.Point.getAttrPopupHtml = function(marker, editable) {    
    var customs = Dituhui.Point.CustomColumns;
    var me = marker;
    var netPicPath = me.netPicPath ? 
        urls.server +'/pointService/getImg?path=' + me.netPicPath 
        : urls.server + "/resources/css/imgs/point/add.png";

    var h = '';        
    h += '  <div class="title">';
    h += '      <span class="name" data-key="name" title="'+ Dituhui.setStringEsc(me.name) +'" option="popup-attr">'+ Dituhui.setStringEsc(me.name) +'</span>';
    h += '      <a class="popup-close" title="关闭"></a>';
    if( editable ) {
        h += '  <a class="popup-delete" title="删除网点"></a>';
        h += '  <a class="popup-edit" title="编辑网点"></a>';        
    }
    h += '  </div>';
    h += '  <div class="content">';
    h += '      <div class="pictures">';
    h += '          <div class="photo none"></div>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">网点名称：</span>';
    h += '          <span class="text" data-key="name" option="popup-attr">'+ Dituhui.setStringEsc(me.name) +'</span>';
    h += '      </div>';
    h += '      <div class="attr">';
    h += '          <span class="label">网点地址：</span>';
    h += '          <span class="text" data-key="address" option="popup-attr">'+ Dituhui.setStringEsc(me.address) +'</span>';
    h += '      </div>';


    var cols = Dituhui.Point.ConfigCols.concat();
    var customCols = Dituhui.Point.CustomColumns;
    for(var m in customCols) {
        if( cols.indexOf(m) != -1) {
            h += '<div class="attr popup-row'+ m +'">';
            h += '  <span class="label" title="'+ Dituhui.setStringEsc(customCols[m]) +'">'+ Dituhui.setStringEsc(customCols[m]).substr(0, 4) +'：</span>';
            if(customs[m] === "链接") {
                var href = me[m] || '';
                if( href && href.length>0 && !/^http:\/\//.test(href) && !/^https:\/\//.test(href) ) {            
                    href = "http://" + href;
                }

                h += '  <span class="text"><a href="'+ (href=="" ? "javascript:;" : href) +'" target="_blank">'+ (me[m] ? Dituhui.setStringEsc(me[m]) : '')+'</a></span>';
            }
            else {
                h += '  <span class="text">'+ (me[m] ? Dituhui.setStringEsc(me[m]) : '')+'</span>';
            }
            
            h += '</div>';
        }
    }

    var coord = Dituhui.metersToLatLon( new SuperMap.LonLat(me.smx, me.smy) );
    if( cols.indexOf('smx') != -1 ) {        
        h += '      <div class="attr">';
        h += '          <span class="label">经度：</span>';
        h += '          <span class="text">'+ (coord.lon ? coord.lon.toFixed(2) : '') +'</span>';
        h += '      </div>';
    }
    if( cols.indexOf('smy') != -1 ) { 
        h += '      <div class="attr">';
        h += '          <span class="label">纬度：</span>';
        h += '          <span class="text">'+ (coord.lat ? coord.lat.toFixed(2) : '') +'</span>';
        h += '      </div>';
    }

    if( cols.indexOf('areaName') != -1 ) { 
        h += '      <div class="attr">';
        h += '          <span class="label">范围：</span>';
        h += '          <span class="text">'+ (me.areaName ? me.areaName : "") +'</span>';
        h += '      </div>';
    }
    if( cols.indexOf('group') != -1 ) { 
        h += '      <div class="attr">';
        h += '          <span class="label">所属分组：</span>';
        h += '          <span class="text">'+ (me.groupid ? me.groupid.groupname : '无') +'</span>';
        h += '      </div>';
    }

   /* h += '      <div class="hide">';
    h += '          <span class="label">车辆：</span>';
    var bind_cars = '';    
    h += '          <span class="text bind-cars">'+ bind_cars +'</span>';
    h += '      </div>';*/
    h += '  </div>';
    return h;
}

/** 
 * 获取云平台搜索的poi的popup的header
 */
Dituhui.Point.getCloudPopupHtml = function(me, option) {
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
Dituhui.Point.getBranchStyle = function(branch) {
    var defaultStyle = Dituhui.Point.Style.defaultStyle;

    var icon, style = {}, styleid = false;
    if(branch.belongToOthers) {
    	return {
    		externalGraphic: urls.server + '/resources/assets/map/poi-non-editble.png',
            graphicWidth: 20,
            graphicHeight: 27
    	}
    }

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
            icon = Dituhui.Point.Style.getStyleSize(h,'transparent','fff'); 
            style.externalGraphic = urls.server + '/pointService/getImg?path='+ styleid.appcustom;
            style.graphicWidth = w;
            style.graphicHeight = h;
        }
        else {
            icon = Dituhui.Point.Style.getStyleSize( 
                styleid.appsize, 
                styleid.appearance ? styleid.appearance : 'transparent', 
                styleid.appcolor
            );
            style.isUnicode = styleid.apppic ? true : false;
            style.label = Dituhui.Point.Style.getFontLabel(styleid.apppic);
            style.fontSize = styleid.appsize;
            style.labelYOffset = 5;

            var color_path = Dituhui.Point.Style.getColor(styleid.appcolor);
            style.externalGraphic = urls.server + '/resources/assets/map/'+ color_path.path +'/'+ styleid.appearance +'.svg';    
            style.graphicWidth = icon.size.w;
            style.graphicHeight = icon.size.h;             
        }
    }
    else {
    	//根据dutyName是否为空判断是否是大众版的网点
    	if(branch.dutyName && branch.dutyName != "") {
    		return {
    			externalGraphic: urls.server + "/resources/assets/images/pois/poi-green.png",
    			graphicWidth: 20,
    			graphicHeight: 30
    		}
    	}
        var s = Dituhui.Point.Style.defaultStyle;
        if(s.img != '') {
            icon = Dituhui.Point.Style.getStyleSize( s.height,'transparent','fff');
            style.externalGraphic = urls.server + '/pointService/getImg?path='+  (s.img);
            style.graphicWidth = s.width;
            style.graphicHeight = s.height;
        }
        else {
            icon = Dituhui.Point.Style.getStyleSize( s.size, s.back, s.color);
            style.externalGraphic = urls.server + '/resources/assets/map/'+ s.colorpath +'/'+ s.back +'.svg';
            style.isUnicode = s.ico ? true : false;
            style.label = Dituhui.Point.Style.getFontLabel(s.ico);
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
Dituhui.Point.getNationalBranchStyle = function(count) {
	var color = 'blue', w=82;
	if(count <= 100) {
		color = 'green';
	}
	else if( count > 100 && count <= 500 ) {
		color = 'blue';
		w = 102;
	}
	else if(count > 500 && count <= 1000) {
		color = 'indigo';
		w = 122;
	}
	else if( count > 1000 && count <= 5000 ) {
		color = 'orange';
		w = 142;
	}
	else if(count > 5000 && count <= 8000) {
		color = 'red';
		w = 162;
	}
	else {
		color = 'purple';
		w = 182;
	}
    return {
        externalGraphic: urls.server + '/resources/images/point/'+color+'.png',
        graphicWidth: w,
        graphicHeight: w,
        fontColor: '#ffffff',
        fontFamily: 'microsoft yahei',
        cursor: 'pointer',
        fontSize: '12'
    }
}

/** 
 * 获取编辑网点popup的header
 */
Dituhui.Point.getEditPopupHeaderHtml = function(name, isDelete, type) {
    var h = '';    
    h += '  <div class="title">';
    h += '      <span class="name"  title="'+ Dituhui.setStringEsc(name) +'" >'+ Dituhui.setStringEsc(name) +'</span>';
    h += '      <a class="popup-close close-popup" title="取消"></a>';
    if( isDelete ) {
        h += '  <a class="popup-delete" title="删除网点"></a>';
    }
    h += '      <a class="popup-sure" title="确定"></a>';

    //编辑网点的时候可以设置自定义字段
    if( name != '' && !type) {
        h += '  <a class="popup-setting" title="设置"></a>';
    }
    h += '  </div>';
    return h;
}

/** 
 * 获取网点绑定的车辆
 */
Dituhui.Point.getBindingCars = function(id, success, failed) {
	if( !id ) {
		return;
	}
    Dituhui.request({
        url: urls.server + "/pointService/queryBindCars?",
        data: {id: id},
        success: function(e){
            var license = "", ids = "", data = [];
            if(e.isSuccess && e.result) {
                if( e.result === 0 ) {
                    success(license, ids, data);
                }
                else {
                    var len = e.result.length;
                    for( var i=len; i--; ) {
                        var item = e.result[i];
                        if( ids !== "" ) {
                            license += "，";
                            ids += "_";
                        }
                        license += item.license;
                        ids += item.id;
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
Dituhui.Point.remove = function(id, success, failed) {
    if( !id ) {
        return;
    }
    Dituhui.showMask();
    Dituhui.request({
        url: urls.server + "/pointService/delete?",
        data: {id: id},
        success: function(e){
            Dituhui.hideMask();
            if(e.isSuccess) {
                success();
            }
            else {
                failed();
            }
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }        
    });
}
/** 
 * 搜索POI
 */
Dituhui.Point.searchFromCloud = function(p, success, failed) {
	if( !p ) {
		return;
	}
    Dituhui.showMask();
    
    if(p.startRecord === null || typeof(p.startRecord) === "undefined") {
	    var pager = $("#pager_cloudpois");
	    var pageIndex = Number(pager.attr('page'));
	
	    p.startRecord = 10*pageIndex;
	    p.expectCount = 10;
    }
	
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
    Dituhui.request({
        url: urls.server + "/orderService/poiSearch?",
        data: param,        
        success: function(e){
            Dituhui.hideMask();
            var data = null;
            if(e.pois || e.results) {
            	e.pois = e.pois ? e.pois : e.results;
            }
            if(e && e.pois && e.pois.length > 0) {
            	data = e.pois;
            	if(!param.startRecord) {  
	            	var html_page = Dituhui.setPage(e.totalCount, pageIndex, '\'pager_cloudpois\''); 
	                $("#pager_cloudpois > ul").html(html_page);   
	                $("#pager_cloudpois").show();   
	                $(".page-cloud-pois").html( '第' + (pageIndex+1)+'/' + Math.ceil(e.totalCount/10) + '页' ).show();
                }
                success(e);
                return;
            }
            /*if(e && e.results && e.results.length > 0) {
            	data = e.results;
            	if(!param.startRecord) {  
	            	var html_page = Dituhui.setPage(e.totalCount, pageIndex, '\'pager_cloudpois\''); 
	                $("#pager_cloudpois > ul").html(html_page);   
	                $("#pager_cloudpois").show();   
	                $(".page-cloud-pois").html( '第' + (pageIndex+1)+'/' + Math.ceil(e.totalCount/10) + '页' ).show();
                }
                success(e);
                return;
            }  */          
            failed();
        },
        error: function(){
            Dituhui.hideMask();
            failed();
        }
    });
}

/** 
 * 显示云平台搜索结果到页面中
 */
Dituhui.Point.getCloudSearchHtml = function(data) {
    var len = data.length;
    var h =  '<ul class="list-group">';
    for(var i=0; i<len; i++) {
        var item = data[i];
        h += '  <li class="list-group-item">';
        h += '      <a href="javascript:void(0);" option="to-cloud-poi" data-id="'+ item.id +'">'
           + '			<span class="number">'+ (i+1) +'</span>'
           + '			<span class="name">'+ item.name +'</span>';
        if(item.address && item.address.length > 0) {
        	h += '		<p class="address">'+ item.address +'</p>';
        }
           + '		</a>';
        h += '  </li>';
    }
        h += '</ul>';
    return h;
}

/** 
 * 反向地址匹配
 * @param - Object - { smx: x, smy: y }
 */
Dituhui.Point.regeocode = function(param, success, failed) {
    if( !param || !param.smx || !param.smy ) {
        return;
    }
    Dituhui.request({
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
Dituhui.Point.Columns = [];
/** 
 * 网点列表 不可单元格修改的字段
 */
Dituhui.Point.unEditableColumns = ['order', 'smx', 'smy', 'username', 'areaName', 'createTime', 'updateTime'];

/** 
 * 网点列表，系统字段
 */
Dituhui.Point.sysColumns = {
    order: "序号",
    name: "网点名称",
    address: "网点地址",
    smx: "经度",
    smy: "纬度",
    areaName: "绑定区划",
    username: "所属账号",
    createTime: "创建时间",
    updateTime: "修改时间",
    group: "所属分组"
};

/** 
 * 网点列表，系统字段表头创建
 */
Dituhui.Point.createSysThead = function() {
    Dituhui.Point.Columns = [];
    var sysColumns = Dituhui.Point.sysColumns;
    var h = '<tr>';
    var key;
    for( key in sysColumns ) {     
        var style = Dituhui.Point.getTableTdStyleName( key );   
        h += '<td class="'+ style +'" id="td_'+ key +'">';
        h += Dituhui.Point.getSysHeadDropMenu( key, sysColumns[key] );
        h += '</td>';
        Dituhui.Point.Columns.push( key );
    }
    h += '</tr>';
    // $('#table_branches > thead').html( h );
    $('#table_branches_head > thead').html( h );
}

/** 
 * 网点列表，表头创建
 */
Dituhui.Point.getSysHeadDropMenu = function( key, value ) {
    var h =  '<div class="dropdown">';
        h += '  <a class="a-head" id="a_head_'+ key +'" href="javascript:void(0);" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
        h += Dituhui.setStringEsc(value);

        if(Dituhui.User.isTop == true) {
        	h += '<span class="caret"></span>';
        }
             
        h += '  </a>'; 
        if(Dituhui.User.isTop == true) {
        	h += '  <ul class="dropdown-menu" aria-labelledby="a_head_'+ key +'">';      
	        h += '      <li><a href="javascript:void(0);" option="add-column">新增列</a></li>';     
	        if(key == 'group') {
	        	h += '	<li><a href="javascript:void(0);" option="add-new-group">新建分组</a></li>';
	        	h += '	<li><a href="javascript:void(0);" option="remove-the-group">删除分组</a></li>';	 
	        }
	        h += '  </ul>';   
        }
           
        h += '</div>';  
    return h;
}

/** 
 * 网点列表 获取表格长度
 */
Dituhui.Point.getTableTdStyleName = function(key) {
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
Dituhui.Point.CustomColumns = {};
Dituhui.Point.ConfigCols = ['name', 'address'];
/** 
 * 获取自定义字段
 */
Dituhui.Point.getCustomColumns = function(first) {
    Dituhui.request({
        url: urls.server + "/pointService/getUserPointExtcol?",
        data: {},
        success: function(e){
            if( e && e.result ) {
                if( typeof(e.result.configcols) != 'undefined' ) {
                    if( e.result.configcols ) {
                        Dituhui.Point.ConfigCols = e.result.configcols.split(',');
                    }
                    
                    delete e.result.configcols;
                }
                Dituhui.Point.CustomColumns = e.result;

                Dituhui.Point.addCustomColumnHead();
                Dituhui.Point.initSettingFilds();
            }
            Dituhui.Point.cssDropdownMenu();
            if( typeof(first) !== 'undefined' && first === true ) {
                Map.searchBranches();
            }
        },
        error: function() {
            Dituhui.Point.cssDropdownMenu();  
            if( typeof(first) !== 'undefined' && first === true ) {
                Map.searchBranches();
            }         
        }        
    });
}
/** 
 * 网点列表 添加自定义列的表头
 */
Dituhui.Point.addCustomColumnHead = function() {
    var columns = Dituhui.Point.CustomColumns;
    var h = '';
    var key, keys = [];
    for( key in columns ) {    
        h += '<td class="td-150" id="td_'+ key +'">';
        h += Dituhui.Point.getCustomHeadDropMenu( key, columns[key] );
        h += '</td>';
        keys.push( key );
    }
    $('#td_smx').before( h );

    Dituhui.Point.insertColsToColumns( keys ); 
}

/** 
 * 往表头中添加自定义的字段
 * @param - keys 
 */
Dituhui.Point.insertColsToColumns = function(keys) {
    var columns = Dituhui.Point.Columns;
    var index = columns.indexOf('smx');
    var pre = columns.slice(0, index);
    var after = columns.slice( index );
    pre = pre.concat(keys);
    Dituhui.Point.Columns = pre.concat( after );
    Dituhui.Point.cssDropdownMenu();
}

/** 
 * 初始化需要设置的字段
 */
Dituhui.Point.initSettingFilds = function() {
    $('.setting-fields-body').html('');
    var fs = [
        {
            value: '网点名称',
            key: 'name',
            editable: false
        },
        {
            value: '网点地址',
            key: 'name',
            editable: false
        }
    ];
    var columns = Dituhui.Point.CustomColumns;
    var h = '';
    var key, keys = [];
    for( key in columns ) {  
        fs.push({
            value: columns[key],
            key: key
        })
    };
    fs = fs.concat([
        {
            value: '经度',
            key: 'smx'
        },
        {
            value: '纬度',
            key: 'smy'
        },
        {
            value: '绑定区划',
            key: 'areaName'
        },
        {
            value: '所属分组',
            key: 'group'
        }
    ]);
    Dituhui.Point.initSettingFildsToList(fs);
}
Dituhui.Point.initSettingFildsToList = function(fs) {
    var h = '', cols = Dituhui.Point.ConfigCols;
    for ( var i=0, len=fs.length; i<len; i++ ) {
        var item = fs[i];
        h += '<div class="mrow">'
        h += '  <span>'+ item.value +'</span>';
        if( typeof(item.editable) == 'undefined') {
            h += '<input class="check-setting-fields" type="checkbox" data-name="'+ item.key +'"';
            if( cols.indexOf(item.key) !== -1 ) {
                h += ' checked="true"';
            }
            h += '>';
        }
        h += '</div>';
    }
    $('.setting-fields-body').append(h);
}
Dituhui.Point.setFields = function(){
    var ids = ['name', 'address'];
    $('input.check-setting-fields:checked').each(function(){
        ids.push( $(this).attr('data-name') );
    });
    Dituhui.request({
        url: urls.server + "/pointService/setUserDefaultPointCols?",
        data: { cols: ids.join(',') },
        success: function(e){
            Dituhui.hideMask();
            if( e && e.isSuccess ) {
                Dituhui.showPopover('设置成功');  
                Dituhui.Point.ConfigCols = ids;
                $('.setting-fields').addClass("hide");
                Map.openEditBranchPopup(marker_editing);
            }
            else {
                var info = e.info ? e.info : "设置失败";
                Dituhui.showHint(info);  
            }
        },
        error: function(){
            Dituhui.hideMask();
            Dituhui.showHint("设置失败");           
        }
    });
}
Dituhui.Point.cancelSetFields = function() {
    var cols = Dituhui.Point.ConfigCols.concat();
    $('input.check-setting-fields').each(function(){
        var me = $(this);
        if( cols.indexOf( me.attr('data-name') ) != -1 ) {
            me.prop('checked', true);
        }
        else {
            me.prop('checked', false);
        }
    });
    $('.setting-fields').addClass("hide");
}

/** 
 * 网点列表 修改表头二级菜单的样式使其居中
 */
Dituhui.Point.cssDropdownMenu = function() {    
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

}

/** 
 * 网点列表，自定义表头创建
 */
Dituhui.Point.getCustomHeadDropMenu = function( key, value ) {
    var h =  '<div class="dropdown">';
        h += '  <a class="a-head" id="a_head_'+ key +'" href="javascript:void(0);" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">';
        h += Dituhui.setStringEsc(value);
        if(Dituhui.User.isTop) {
        	h += '      <span class="caret"></span>'; 
        }
        h += '  </a>'; 
        if(Dituhui.User.isTop) {
	        h += '  <ul class="dropdown-menu" aria-labelledby="a_head_'+ key +'">';      
	        h += '      <li><a href="javascript:void(0);" option="add-column">新增列</a></li>';      
	        h += '      <li><a href="javascript:void(0);" option="rename-column" data-key="'+ key +'" data-value="'+ value +'">重命名</a></li>';      
	        h += '      <li><a href="javascript:void(0);" option="remove-column" data-key="'+ key +'" data-value="'+ value +'">删除列</a></li>';      
	        h += '  </ul>';      
        }
        h += '</div>';  
    return h;
}

/**
 * 设置弹窗中的图标style
 */
Dituhui.Point.setPopupDivStyle = function(){
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
        var style = Dituhui.Point.Style.getStyleSize(li.attr('data-height'),'transparent','fff');
        me.css({
            'background': '#fff url('+ urls.server + '/pointService/getImg?path='+ icon.img +') no-repeat center center',
            'background-size': style.size.w + 'px ' + style.size.h + 'px'
        });
        me.find('span').removeClass();
    }
    else {
        var style = Dituhui.Point.Style.getStyleSize( icon.size > 20 ? 20 : icon.size, icon.back, icon.color);
        var li_color = $('.icon-selector .color li[data-color="'+ icon.color +'"]');
        me.css({  
            'background': '#fff url('+ urls.server +'/resources/assets/map/' + li_color.attr('data-color-path') + '/' + icon.back + '.svg) no-repeat center center',
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
Dituhui.Point.Cluster = {};
Dituhui.Point.Cluster.getLevelColor = function(count) {
	var color = 'blue';
	switch(count) {
		case 100:
			color = 'green';
			break;
		case 500:
			color = 'blue';
			break;
		case 1000:
			color = 'indigo';
			break;
		case 5000:
			color = 'orange';
			break;
		case 8000:
			color = 'red';
			break;
		default:
			color = 'purple';
			break;
	}
	return {
		externalGraphic: urls.server + '/resources/assets/map/'+color+'/circle.svg',
        graphicWidth: 50, 
        graphicHeight: 50,
        fontColor: '#ffffff',
        fontFamily: 'microsoft yahei',
        cursor: 'pointer',
        fontSize: '12'
	};
}

Dituhui.Point.Cluster.getStyle = function() {
	return [
        {
            "count": 100,
            "style": Dituhui.Point.Cluster.getLevelColor(100)
        },
        {
            "count": 500,
            "style": Dituhui.Point.Cluster.getLevelColor(500)
        },
        {
            "count": 1000,
            "style": Dituhui.Point.Cluster.getLevelColor(1000)
        },
        {
            "count": 5000,
            "style": Dituhui.Point.Cluster.getLevelColor(5000)
        },
        {
            "count": 8000,
            "style": Dituhui.Point.Cluster.getLevelColor(8000)
        },
        {
            "count":"moreThanMax",// 子节点大于50的聚散点
            "style": Dituhui.Point.Cluster.getLevelColor(9000)
        }
	]
}
