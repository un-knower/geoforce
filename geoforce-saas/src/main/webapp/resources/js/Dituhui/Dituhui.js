
/** 
 * 企业版在线接口
 * 命名空间:Dituhui
 */
var Dituhui = {};

/** 
 * 统一发送请求
 */
Dituhui.request = function(option) {
    var param = option.data ? option.data : {};
    param.moduleId = Dituhui.User.currentModuleId;
    var type = option.type ? option.type : "POST";
    $.ajax({
        type: type,
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
Dituhui.IPLocate = function(url, success, failed) {				
    //验证是否登陆
    $.ajax({
        type: 'GET',
        async: true,
        url: url,
        dataType: 'jsonp',
        success: function(data) {
        	if( data && data.status === 0 
        		&& data.content 
        		&& data.content.address_detail 
        		&& data.content.address_detail.city 
        		&& data.content.address_detail.city != ""
        	) {
        		var result = {
        			city: data.content.address_detail.city,
        			province: data.content.address_detail.province
        		};
        		success( result );
        	}
            else {
                failed();
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
Dituhui.showMask = function() {
    $('.mask-loading').show();
}

/** 
 * 关闭数据加载遮罩
 */
Dituhui.hideMask = function() {
    $('.mask-loading').hide();
}


Dituhui.timer_popup = null;
Dituhui.showPopover = function(string) {      
    $("#popover_content").html(string);
    $("#popover_result").show();
    if(Dituhui.timer_popup){
        clearTimeout(Dituhui.timer_popup);
    }       
    Dituhui.timer_popup = setTimeout("Dituhui.hidePopover();", 3000);
}
Dituhui.hidePopover = function() {
    $("#popover_result").hide();    
}

Dituhui.timer_hint = null;
Dituhui.showHint = function(string) {      
    $("#popover_hint_content").html(string);
    $("#popover_hint").show();
    if(Dituhui.timer_hint){
        clearTimeout(Dituhui.timer_hint);
    }       
    Dituhui.timer_hint = setTimeout("Dituhui.hideHint();", 3000);
}
Dituhui.hideHint = function() {
    $("#popover_hint").hide();    
}
Dituhui.showLoading = function(string) {
    $('.mask-loading-text a').html(string);
    $('.mask-loading-text').show();
}
Dituhui.hideLoading = function(){
    $('.mask-loading-text').hide();   
    $('.mask-loading-text a').html(''); 
}


Dituhui.earthCircumferenceInMeters = 40075016.685578488;
Dituhui.halfEarthCircumferenceInMeters = Dituhui.earthCircumferenceInMeters / 2;
Dituhui.earthRadiusInMeters = 6378137.0;
Dituhui.MercatorLatitudeLimit = 85.051128;

/**
 * 经纬度转米坐标
 */
Dituhui.latLonToMeters = function (point) {
    //如果用户传入的是SuperMap.Geometry.Point类型的对象就执行if语句，否则就执行else语句
    if(point.CLASS_NAME == "SuperMap.Geometry.Point" || point.CLASS_NAME == "SuperMap.OSP.Core.Point2D" || point.x != null){
        var mx = point.x / 180.0 * Dituhui.halfEarthCircumferenceInMeters;
        var my = Math.log(Math.tan((90 + point.y) * Math.PI / 360.0)) / (Math.PI / 180.0);
        my = my / 180.0 * Dituhui.halfEarthCircumferenceInMeters;
        return new SuperMap.Geometry.Point(mx, my);
    }else{
        var mx = point.lon / 180.0 * Dituhui.halfEarthCircumferenceInMeters;
        var my = Math.log(Math.tan((90 + point.lat) * Math.PI / 360.0)) / (Math.PI / 180.0);
        my = my / 180.0 * Dituhui.halfEarthCircumferenceInMeters;
        return new SuperMap.LonLat(mx, my);
    }
}

/**
 * 米坐标转经纬度
 */
Dituhui.metersToLatLon = function (point) {
    if(point.CLASS_NAME == "SuperMap.Geometry.Point" || point.CLASS_NAME == "SuperMap.OSP.Core.Point2D" || point.x != null){
        var lon = point.x / Dituhui.halfEarthCircumferenceInMeters * 180.0;
        var lat = point.y / Dituhui.halfEarthCircumferenceInMeters * 180.0;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2);
        return new SuperMap.Geometry.Point(lon, lat);
    }else{
        var lon = point.lon / Dituhui.halfEarthCircumferenceInMeters * 180.0;
        var lat = point.lat / Dituhui.halfEarthCircumferenceInMeters * 180.0;
        lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2);
        return new SuperMap.LonLat(lon, lat);
    }
}

//获取页面显示区域高度
Dituhui.getWindowHeight = function() {
    if (navigator.appName == "Microsoft Internet Explorer" ) {
        return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
    }
    else {
        return self.innerHeight;
    } 
}

//获取页面显示区域宽度
Dituhui.getWindowWidth = function() {
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
Dituhui.setPage = function(total, _pageIndex, divid, customlimit) {
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
        html += '<li  title="第一页"><a href="javascript:Dituhui.setpageno('+ divid +', '+ 0 +');">&laquo;</a></li>';        
    }

    if(_pageIndex < 3){
        for(var i=1; (i<=5 && i<=_pageCount && _pageIndex <= _pageCount); i++) {
            if(i !== (_pageIndex+1)) {
                html += '<li><a href="javascript:Dituhui.setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
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
                html += '<li><a href="javascript:Dituhui.setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
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
        html += '<li  title="最后一页"><a href="javascript:Dituhui.setpageno('+ divid +', '+ (_pageCount-1) +');">&raquo;</a></li>';        
    }

    return html;
}

/**
 * 为div pager绑定点击事件
 * 
 * @param: id - div的id
 * @param: index - 当前第几页
 */
Dituhui.setpageno = function(id, index) {
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
 * 分页控件
 */
Dituhui.setPager = function(pager) {
	var limit, total = pager.total;
    if(typeof(pager.limit) === 'undefined' || pager.limit == null) {
        limit = 10;
    }
    else {
        limit = pager.limit;
    }
	var _pageIndex = pager.index;
    var html = '';

    //计算总页数
    var _pageCount = Math.ceil(total/limit);

    if(_pageIndex == 0) {
        html += '<li class="disabled" title="第一页"><a href="javascript:;">&laquo;</a></li>';
    }
    else {
        html += '<li  title="第一页"><a href="javascript:;" data-page-index="0">&laquo;</a></li>';        
    }

    if(_pageIndex < 3){
        for(var i=1; (i<=5 && i<=_pageCount && _pageIndex <= _pageCount); i++) {
            if(i !== (_pageIndex+1)) {
                html += '<li><a href="javascript:;" data-page-index="'+ (i-1) +'">'+ i +'</a></li>';
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
                html += '<li><a href="javascript:;"  data-page-index="'+ (i-1) +'">'+ i +'</a></li>';
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
        html += '<li  title="最后一页"><a href="javascript:;"  data-page-index="'+ (_pageCount-1) +'">&raquo;</a></li>';  
    }
    
    $("#" + pager.id + " > ul").html(html);

}

/**
 * 字符串中含有<>转义
 * 
 * @param: txt -String
 */
Dituhui.setStringEsc = function(txt) {
    var str = '';
    if(!txt) {
        return str;
    }
    str = txt.toString().replace(/</g, '&lt;');
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
Dituhui.convertStringEsc = function(txt) {    
    var str = txt.replace(/<pre>/g, '');
    str = str.replace(/<\/pre>/g, '');
    str = str.replace(/&lt;/g, '<');
    str = str.replace(/&gt;/g, '>');
    return str;
}


/**
 * 遮罩
 */
Dituhui.Modal = {};
Dituhui.Modal.loading = function(str) {
    $('.data-modal').addClass("hide");
    $('.modal-loading .text').html(str);
    $('.modal-loading').removeClass("hide");
}
/**
 * 显示成功弹窗
 * @param {String} str
 * @param {Function} callback
 */
Dituhui.Modal.loaded_right = function(str, callback) {
    $('.data-modal').addClass("hide");
    $('.modal-loaded.modal-right .text').html(str);
    $('.modal-right .btn-save-border').unbind('click').click(callback);
    $('.modal-loaded.modal-right').removeClass("hide");
}
Dituhui.Modal.loaded_wrong = function(str, callback) {
    $('.data-modal').addClass("hide");
    $('.modal-loaded.modal-wrong .text').html(str);
    $('.modal-wrong .btn-save-border').unbind('click').click(callback);
    $('.modal-wrong').removeClass("hide");
}


/**
 * 弹出询问窗口
 * 默认窗口为“确定,取消”, no_callback 不为空时为“是,否,取消”三个选项
 * @param {string} str 需询问的内容
 * @param {function} callback 点击确定后的回调函数
 * @param {attr} 确定按钮需要绑定的属性
 * @param {function} cancel_callback 点击取消后的回调函数
 * @param {function} no_callback 点击否后的回调函数
 */
Dituhui.Modal.ask = function( str, callback, attr, cancel_callback, no_callback) {
    if(!callback) {
        callback = function(){};
    }
    if(!attr){
        attr = {};
    }
    if(!cancel_callback) {
        cancel_callback = function(){}
    }
    
    var btn_ok = $('.modal-ask .btn-save-border').attr(attr).unbind("click").click(function(){
        callback(attr);
        $('.modal-ask').addClass("hide");
    });

    $('.modal-ask .close').unbind('click').click(function(){
        var me = $(this);
        $(me.attr("data-target")).addClass("hide");
        cancel_callback();
    });
    
    //是,否,取消
    if(typeof(no_callback) == "function") {
    	btn_ok.html("是");
    	$('.modal-ask .btn-no-save-border').removeClass("hide").unbind("click").click(
            function(){
                no_callback();           
            }
        );
    }
    else {
    	btn_ok.html("确定");
    	$('.modal-ask .btn-no-save-border').addClass("hide");
    }
    
    $('.modal-ask .text').html( str );
    $('.modal-ask').removeClass("hide");
}

Dituhui.Modal.alert = function( str, callback, attr ) {
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
    $('.modal-alert').removeClass("hide");
}
Dituhui.Modal.alert = function( str, callback, attr ) {
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
    $('.modal-alert').removeClass("hide");
}

Dituhui.Modal.hide = function() {
    $('.data-modal').addClass("hide");
}








