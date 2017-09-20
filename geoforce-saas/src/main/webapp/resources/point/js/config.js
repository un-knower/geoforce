var urls = {};

/**
 * 服务端url
 */
// urls.server = "http://192.168.10.251:8031/saas";
urls.server = "/saas";
/**
 * 地图-底图url
 */
urls.map_img = "http://t2.supermapcloud.com/FileService/image";
/**
 * IP定位
 */
urls.ip_locate = "http://api.map.baidu.com/location/ip?ak=214c94f370aa31822201489ae44e4018";

urls.ie_case = false;

urls.getUrlArgs = function(){
    var url = location.search; 
    var theRequest = {};
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for ( var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    } 
    else {
        theRequest = null;
    }
    return theRequest;    
}


var map = null;
var layer_map = null;
var layer_branches = null;
var layer_region = null;
var layer_region_label = null;
var layer_boundry = null;
var layer_cloudpois = null;
var selectedFeature = null;
var selectedFeatures = [];
var layer_edit_branch = null;
var layer_edit = null;
var control_drawPoint = null;
var contros_zoombars = null;
var control_select = null;
var layer_orders = null;


var layer_tdt_img = null;
var layer_tdt_label = null;
var layer_google_img = null;
var layer_google_label = null;



//获取页面显示区域高度
function getWindowHeight() {
	if (navigator.appName == "Microsoft Internet Explorer" ) {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
	}
	else {
		return self.innerHeight;
	} 
}

//获取页面显示区域宽度
function getWindowWidth() {
	if (navigator.appName == "Microsoft Internet Explorer") {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
	}
	else {
		return self.innerWidth;
	} 
}


function checkFileSize(){    
    var input = document.getElementById("txt_file");
    var fs = input.files;
    if( !fs || fs.length < 1 ) {
        if( input.value === "" ) {
            $('#txt_file').removeAttr('name');
        }      
        else {
            $('#txt_file').attr('name', 'netPicFile');
        }  
        return true;
    }
    var filesize = fs[0].size;
    if(filesize >= 102400) {
        $(".hint").html("图片大于100K，请重新选择");
        return false;
    }
    else {
        $(".hint").html("");    
        return true;    
    }
}
function previewImage(me){
    var file = me;
	
    checkFileSize();

    var MAXWIDTH  = 50; 
    var MAXHEIGHT = 50;
    var div = document.getElementById('photo');
    if (file.files && file.files[0]) {
        div.innerHTML ='<img id=imghead>';
        var img = document.getElementById('imghead');
        img.onload = function(){
            var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
            img.width  =  rect.width;
            img.height =  rect.height;
			
            img.style.marginTop = rect.top+'px';
        }
        var reader = new FileReader();
        reader.onload = function(evt){img.src = evt.target.result;}
        reader.readAsDataURL(file.files[0]);
    }
    else { //兼容IE  
        var sFilter='filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale,src="';
        file.select();
		
        var src = document.selection.createRange().text;
        div.innerHTML = '<img id=imghead>';
        var img = document.getElementById('imghead');
        img.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = src;
        var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, img.offsetWidth, img.offsetHeight);
        status =('rect:'+rect.top+','+rect.left+','+rect.width+','+rect.height);
        div.innerHTML = "<div id=divhead style='width:"+rect.width+"px;height:"+rect.height+"px;margin-top:"+rect.top+"px;"+sFilter+src+"\"'></div>";
    }
}
function clacImgZoomParam( maxWidth, maxHeight, width, height ){
    var param = {top:0, left:0, width:width, height:height};
    if( width>maxWidth || height>maxHeight ){
        rateWidth = width / maxWidth;
        rateHeight = height / maxHeight;
        
        if( rateWidth > rateHeight ){
            param.width =  maxWidth;
            param.height = Math.round(height / rateWidth);
        }
        else{
            param.width = Math.round(width / rateHeight);
            param.height = maxHeight;
        }
    }
    
    param.left = Math.round((maxWidth - param.width) / 2);
    param.top = Math.round((maxHeight - param.height) / 2);
    return param;
}



Date.prototype.format =function(format){
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) 
        format=format.replace(RegExp.$1, (this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
    RegExp.$1.length==1? o[k] :
    ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}