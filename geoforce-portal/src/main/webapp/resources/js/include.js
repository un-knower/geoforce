(function() {
    var isWinRT = (typeof Windows === "undefined") ? false : true;
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
        //var url = "";
        inputScript('resources/lib/jquery_1.11.3/jquery.min.js');
        inputScript('resources/js/common.js');
    }
    loadSMLibs();
})();
