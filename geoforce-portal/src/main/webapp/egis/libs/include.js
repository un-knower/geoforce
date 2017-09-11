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
        // var url = "/egispportal/egis/";
        var url = "/";
        inputMyCSS(url + 'libs/bootstrap/bootstrap.min.css');
        inputScript(url + 'libs/jquery/jquery-1.10.2.min.js');
        inputScript(url + 'libs/jquery/jquery.SuperSlide.2.1.1.js');
        inputScript(url + 'libs/jquery/jquery.form.js');
        inputScript(url + 'libs/bootstrap/bootstrap.min.js');
        inputMyCSS(url + 'libs/bootstrap/datepicker3.css');
        inputScript(url + 'libs/bootstrap/bootstrap-datepicker.js');
        inputScript(url + 'libs/base64.min.js');
    }
    loadSMLibs();
})();
