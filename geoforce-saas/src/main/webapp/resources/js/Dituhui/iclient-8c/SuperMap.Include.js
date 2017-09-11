(function() {
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
    function inputMyCSS(style){
        if (!isWinRT) {
            var css = '<' + 'link rel="stylesheet" href="' + style + '"' + '><' + '/>';
            document.writeln(css);
        } else { 
            var link = document.createElement("link");
            link.rel = "stylesheet";
            link.href =  style;
            document.getElementsByTagName("HEAD")[0].appendChild(link);
        }
    }
    //加载类库资源文件
    function loadSMLibs() {
        inputCSS('style.css');
        inputCSS('google.css');
        inputScript(baseurl+'SuperMap-8.0.2-13626.js');
        inputScript(baseurl+'CloudLayer.js');
        inputScript(baseurl+'layer/Baidu.js');
        /*inputScript(baseurl+'Class.js');
        inputScript(baseurl+'Snap.js');*/
    }
    //引入汉化资源文件
    function loadLocalization() {
        inputScript(baseurl + 'Lang/zh-CN.js');
    }
    loadSMLibs();loadLocalization();
})();
