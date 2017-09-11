﻿(function() {
    var isWinRT = (typeof Windows === "undefined") ? false : true;
    var r = new RegExp("(^|(.*?\\/))(SuperMap.Include\.js)(\\?|$)"),
    s = document.getElementsByTagName('script'),
    src, m, baseurl = "";
    for(var i=0, len=s.length; i<len; i++) {
        src = s[i].getAttribute('src');
        if(src) {
            var m = src.match(r);
            if(m) {
                baseurl = m[1];
                break;
            }
        }
    }
    function inputScript(inc){
        if (!isWinRT) {
            var script = '<' + 'script type="text/javascript" src="' + inc + '"' + '><' + '/script>';
            document.writeln(script);
        } else {
            var script = document.createElement("script");
            script.src = inc;
            document.getElementsByTagName("HEAD")[0].appendChild(script);
        }
    }
    function inputCSS(style){
        if (!isWinRT) {
            var css = '<' + 'link rel="stylesheet" href="' + baseurl + '../theme/default/' + style + '"' + '><' + '/>';
            document.writeln(css);
        } else { 
            var link = document.createElement("link");
            link.rel = "stylesheet";
            link.href =  "/theme/default/" + style;
            document.getElementsByTagName("HEAD")[0].appendChild(link);
        }
    }
    //加载类库资源文件
    function loadSMLibs() {
        inputScript(baseurl+'SuperMap-7.0.1-11323.js');
        inputCSS('style.css');
        inputCSS('google.css');
    }
    //引入汉化资源文件
    function loadLocalization() {
        inputScript(baseurl + 'Lang/zh-CN.js');
        inputScript(baseurl + 'osp.js');
    }
    loadSMLibs();loadLocalization();
})();

var ospServiceUrl="http://services.supermapcloud.com";
var poiSearchUrl = ospServiceUrl;
// var poiSearchUrl = "http://192.168.10.251:8080";
var cachedUrl = "http://t1.supermapcloud.com";

//影像图地址
var imageUrl = "http://dem.supermapcloud.com";

//影像图截图服务
var url_screenshot_image = "http://42.120.49.194:8050";
//地图切片访问模式,{0:旧模式, 1:新模式}
var mapSliceMode = 1;
//是否屏蔽不可用的功能
var isDataHide = true;

String.prototype.trim = function(){  
　 return this.replace(/(^\s*)|(\s*$)/g, "");  
}  
