var urls = {};
/**
 * 服务端url
 */
// urls.server = "http://192.168.10.251:8031/saas";
urls.server = ctx;
/**
 * 地图-底图url
 */
urls.map_img = "http://t2.supermapcloud.com/FileService/image";
/**
 * IP定位
 */
urls.ip_locate = "http://api.map.baidu.com/location/ip?ak=214c94f370aa31822201489ae44e4018";


//当前页面的产品id
function getUrlArgs(){
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
$(function(){	
	var param_location = getUrlArgs();
	if(param_location.moduleid) {
		Dituhui.User.currentModuleId = param_location.moduleid;
	}
})