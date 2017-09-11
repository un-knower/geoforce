
/** 
 * 企业版在线接口
 * 命名空间:SuperMap.Egisp
 */
SuperMap.Egisp = SuperMap.Egisp || {};
/** 
 * 统一发送请求
 */
SuperMap.Egisp.request = function(option) {
    var param = option.data ? option.data : {};
    param.moduleId = SuperMap.Egisp.User.currentModuleId;
    $.ajax({
        type: 'POST',
        async: true,
        url: option.url,
        data: param,
        dataType: 'json',
        success: option.success,
        error: option.error
    });
}

/** 
 * IP定位
 */
SuperMap.Egisp.IPLocate = function(url, success, failed) {				
    //验证是否登陆
    $.ajax({
        type: 'GET',
        async: true,
        url: url,
        dataType: 'jsonp',
        success: function(data) {
        	if( data && data.status === 0 && data.content && data.content.address_detail && data.content.address_detail.city ) {
        		var result = {
        			city: data.content.address_detail.city,
        			province: data.content.address_detail.province
        		};
        		success( result );
        	}
        },
        error: function() {
        	failed();
        }
    });
}

/** 
 * 显示数据加载遮罩
 */
SuperMap.Egisp.showMask = function() {
    $('.mask-loading').show();
}

/** 
 * 关闭数据加载遮罩
 */
SuperMap.Egisp.hideMask = function() {
    $('.mask-loading').hide();
}



SuperMap.Egisp.timer_popup = null;
SuperMap.Egisp.showPopover = function(string) {      
    $("#popover_content").html(string);
    $("#popover_result").show();
    if(SuperMap.Egisp.timer_popup){
        clearTimeout(SuperMap.Egisp.timer_popup);
    }       
    SuperMap.Egisp.timer_popup = setTimeout("SuperMap.Egisp.hidePopover();", 3000);
}
SuperMap.Egisp.hidePopover = function() {
    $("#popover_result").hide();    
}

SuperMap.Egisp.timer_hint = null;
SuperMap.Egisp.showHint = function(string) {      
    $("#popover_hint_content").html(string);
    $("#popover_hint").show();
    if(SuperMap.Egisp.timer_hint){
        clearTimeout(SuperMap.Egisp.timer_hint);
    }       
    SuperMap.Egisp.timer_hint = setTimeout("SuperMap.Egisp.hideHint();", 3000);
}
SuperMap.Egisp.hideHint = function() {
    $("#popover_hint").hide();    
}
SuperMap.Egisp.showLoading = function(string) {
    $('.mask-loading-text a').html(string);
    $('.mask-loading-text').show();
}
SuperMap.Egisp.hideLoading = function(){
    $('.mask-loading-text').hide();   
    $('.mask-loading-text a').html(''); 
}





SuperMap.Egisp.earthCircumferenceInMeters = 40075016.685578488;
SuperMap.Egisp.halfEarthCircumferenceInMeters = SuperMap.Egisp.earthCircumferenceInMeters / 2;
SuperMap.Egisp.earthRadiusInMeters = 6378137.0;
SuperMap.Egisp.MercatorLatitudeLimit = 85.051128;

/**
 * 经纬度转米坐标
 */
SuperMap.Egisp.latLonToMeters = function (point) {
    //如果用户传入的是SuperMap.Geometry.Point类型的对象就执行if语句，否则就执行else语句
    if(point.CLASS_NAME == "SuperMap.Geometry.Point" || point.CLASS_NAME == "SuperMap.OSP.Core.Point2D" || point.x != null){
        var mx = point.x / 180.0 * SuperMap.Egisp.halfEarthCircumferenceInMeters;
        var my = Math.log(Math.tan((90 + point.y) * Math.PI / 360.0)) / (Math.PI / 180.0);
        my = my / 180.0 * SuperMap.Egisp.halfEarthCircumferenceInMeters;
        return new SuperMap.Geometry.Point(mx, my);
    }else{
        var mx = point.lon / 180.0 * SuperMap.Egisp.halfEarthCircumferenceInMeters;
        var my = Math.log(Math.tan((90 + point.lat) * Math.PI / 360.0)) / (Math.PI / 180.0);
        my = my / 180.0 * SuperMap.Egisp.halfEarthCircumferenceInMeters;
        return new SuperMap.LonLat(mx, my);
    }
}
/**
 * 米坐标转经纬度
 */
SuperMap.Egisp.metersToLatLon = function (point) {
    if(point.CLASS_NAME == "SuperMap.Geometry.Point" || point.CLASS_NAME == "SuperMap.OSP.Core.Point2D" || point.x != null){
        var lon = point.x / SuperMap.Egisp.halfEarthCircumferenceInMeters * 180.0;
        var lat = point.y / SuperMap.Egisp.halfEarthCircumferenceInMeters * 180.0;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2);
        return new SuperMap.Geometry.Point(lon, lat);
    }else{
        var lon = point.lon / SuperMap.Egisp.halfEarthCircumferenceInMeters * 180.0;
        var lat = point.lat / SuperMap.Egisp.halfEarthCircumferenceInMeters * 180.0;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2);
        return new SuperMap.LonLat(lon, lat);
    }
}

//获取页面显示区域高度
SuperMap.Egisp.getWindowHeight = function() {
    if (navigator.appName == "Microsoft Internet Explorer" ) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
    }
    else {
        return self.innerHeight;
    } 
}

//获取页面显示区域宽度
SuperMap.Egisp.getWindowWidth = function() {
    if (navigator.appName == "Microsoft Internet Explorer") {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
    }
    else {
        return self.innerWidth;
    } 
}
/**
 * 数组删除元素
 * @param - item
 */
Array.prototype.removeItem = function(item) {
    var index = this.indexOf(item);
    if( index !== -1 ) {
        this.splice(index, 1);
    }
}


/**
 * 计算页数
 * 
 * @param: total - 总页数
 * @param: _pageIndex - 当前第几页
 * @param: limit - 每页显示个数，默认为10
 *
 * @return: html - 分页控件显示的内容
 */
SuperMap.Egisp.setPage = function(total, _pageIndex, divid, customlimit) {
    var limit;
    if(typeof(customlimit) === 'undefined') {
        limit = 10;
    }
    else {
        limit = customlimit;
    }

    var html = '';

    //计算总页数
    var _pageCount = Math.ceil(total/limit);
    if(_pageIndex == 0) {
        html += '<li class="disabled" title="第一页"><a href="javascript:;">&laquo;</a></li>';
    }
    else {
        html += '<li  title="第一页"><a href="javascript:SuperMap.Egisp.setpageno('+ divid +', '+ 0 +');">&laquo;</a></li>';        
    }

    if(_pageIndex < 3){
        for(var i=1; (i<=5 && i<=_pageCount && _pageIndex <= _pageCount); i++) {
            if(i !== (_pageIndex+1)) {
                html += '<li><a href="javascript:SuperMap.Egisp.setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
            }
            else {
                html += '<li class="active"><a href="javascript:;">'+ i +'</a></li>';
            }
        }
    }
    else {
        //pageIndex后应该有几个页数
        var behind_index = (　(_pageCount-_pageIndex > 3) ? 3  :　_pageCount-_pageIndex);
        
        //pageIndex前应该有几个页数
        var before_index = 5 - behind_index - 1;

        var startIndex = (_pageIndex - before_index > 0 ? _pageIndex - before_index : 1 );
        var limit_length = _pageIndex + behind_index;
        for( i = startIndex; i <= limit_length ; i++ ){
            if(i !== (_pageIndex + 1) ) {
                html += '<li><a href="javascript:SuperMap.Egisp.setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
            }
            else {
                html += '<li class="active"><a href="javascript:;">'+ i +'</a></li>';              
            }
        }
    }

    if(_pageIndex == (_pageCount - 1)) {
        html += '<li  title="最后一页" class="disabled"><a href="javascript:;">&raquo;</a></li>';
    }
    else {
        html += '<li  title="最后一页"><a href="javascript:SuperMap.Egisp.setpageno('+ divid +', '+ (_pageCount-1) +');">&raquo;</a></li>';        
    }

    return html;
}

/**
 * 为div pager绑定点击事件
 * 
 * @param: id - div的id
 * @param: index - 当前第几页
 */
SuperMap.Egisp.setpageno = function(id, index) {
    var pager = $("#" + id);
    pager.attr("page", index);

    var target = pager.attr("data-target");
    switch(target) {
        case "cloud-pois":
            Map.searchFromCloud();
            break;
        case "orders":
            Map.search();
            break;
        case "data-cars":
            Cars.search();
            break;
        case "geocoders":
            Map.search();
            break;
    }
}

/**
 * 字符串中含有<>转义
 * 
 * @param: txt -String
 */
SuperMap.Egisp.setStringEsc = function(txt) {
    var str = '';
    if(!txt) {
        return str;
    }
    str = txt.replace(/</g, '&lt;');
    str = str.replace(/>/g, '&gt;');
    str = str.replace(/'/g, '&#39;');
    str = str.replace(/"/g, '&#34;');
    str = str.replace(/\{/g, '&#123;');
    str = str.replace(/\}/g, '&#125;');
    str = str.replace(/\[/g, '&#91;');
    str = str.replace(/\]/g, '&#93;');
    str = str.replace(/\:/g, '&#58;');
    str = str.replace(/\?/g, '&#63;');
    str = str.replace(/\=/g, '&#61;');
    str = str.replace(/\\/g, '&#92;');
    str = str.replace(/\//g, '&#47;');
    // var str = encodeURIComponent(txt);
    return str;
}
SuperMap.Egisp.convertStringEsc = function(txt) {    
    var str = txt.replace(/<pre>/g, '');
    str = str.replace(/<\/pre>/g, '');
    str = str.replace(/&lt;/g, '<');
    str = str.replace(/&gt;/g, '>');
    return str;
}


/**
 * 遮罩
 */
SuperMap.Egisp.Modal = {};
SuperMap.Egisp.Modal.loading = function(str) {
    $('.data-modal').hide();
    $('.modal-loading .text').html(str);
    $('.modal-loading').show();
}
SuperMap.Egisp.Modal.loaded_right = function(str, callback) {    
    $('.data-modal').hide();
    $('.modal-loaded.modal-right .text').html(str);
    $('.modal-right .btn-save-border').unbind('click').click(callback);
    $('.modal-loaded.modal-right').show();
}
SuperMap.Egisp.Modal.loaded_wrong = function(str, callback) {    
    $('.data-modal').hide();
    $('.modal-loaded.modal-wrong .text').html(str);
    $('.modal-wrong .btn-save-border').unbind('click').click(callback);
    $('.modal-wrong').show();
}

SuperMap.Egisp.Modal.ask = function( str, callback, attr, cancel_callback) {
    if(!callback) {
        callback = function(){};
    }
    if(!attr){
        attr = {};
    }
    if(!cancel_callback) {
        cancel_callback = function(){}
    }
    $('.modal-ask .btn-save-border')
    .attr(attr)
    .unbind('click').click(callback);

    $('.modal-ask .close').unbind('click').click(function(){
        var me = $(this);
        $(me.attr("data-target")).fadeOut('fast');
        cancel_callback();
    });
    $('.modal-ask .text').html( str );
    $('.modal-ask').show(); 
}
SuperMap.Egisp.Modal.alert = function( str, callback, attr ) {
    if(!callback) {
        callback = function(){};
    }
    if(!attr){
        attr = {};
    }
    $('.modal-alert .btn-save-border')
    .attr(attr)
    .unbind('click').click(callback);
    $('.modal-alert .text').html( str );
    $('.modal-alert').show(); 
}
SuperMap.Egisp.Modal.hide = function() {
    $('.data-modal').hide();    
}