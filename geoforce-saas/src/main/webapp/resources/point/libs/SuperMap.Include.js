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
        inputMyCSS(baseurl+'bootstrap/bootstrap.min.css');
        // inputScript(baseurl+'SuperMap_Basic-7.2.0_Beta-12908.js');
        inputScript(baseurl+'SuperMap_Basic-8.0.0-13029.js');
        inputScript(baseurl+'CloudLayer.js');
        inputScript(baseurl+'jquery.min.js');
        inputScript(baseurl+'bootstrap/bootstrap.min.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Coords.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Zone.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.SMCity/SuperMap.Egisp.SMCity.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.SMCity/SuperMap.Egisp.SMCity.City.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.User.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Point/SuperMap.Egisp.Point.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Point/SuperMap.Egisp.Point.Style.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Point/SuperMap.Egisp.Point.Table.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Order.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Geocoder.js');
        inputScript(baseurl+'SuperMap.Egisp/SuperMap.Egisp.Address.js');
        inputScript(baseurl+'jquery.form.js');
        inputScript(baseurl+'layer/Baidu.js');
        // inputScript(baseurl+'layer/Tianditu.js');
        // inputScript(baseurl+'layer/SphericalMercator.js');
        // inputScript(baseurl+'layer/EventPane.js');
        // inputScript(baseurl+'layer/FixedZoomLevels.js');
        // inputScript(baseurl+'layer/Google.js');
        // inputScript(baseurl+'layer/Google.v3.js');
    }
    //引入汉化资源文件
    function loadLocalization() {
        inputScript(baseurl + 'Lang/zh-CN.js');
    }
    loadSMLibs();loadLocalization();
})();
