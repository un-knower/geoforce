//设置库文件与该文件的相对路径，用户可以根据 Lib 文件夹所在位置设置。
function _SospIncludeScript(inc) {
    var script = '<' + 'script type="text/javascript" src="' + inc + '"' + '><' + '/script>';
    document.writeln(script);
}

//设置CSS样式表与该文件的相对路径，用户可以根据 Styles 文件夹所在位置设置。
function _SospIncludeStyle(inc) {
    var style = '<' + 'link type="text/css" rel="stylesheet" href="' + inc + '"' + ' />';
    document.writeln(style);
}
function _GetBrowser() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.indexOf('opera') != -1) return 'opera';
    else if (ua.indexOf('msie') != -1) return 'ie';
    else if (ua.indexOf('safari') != -1) return 'safari';
    else if (ua.indexOf('gecko') != -1) return 'gecko';
    else return false;
}
//引用库文件和 CSS 样式。
if (!Function.__typeName) {


    _SospIncludeScript('script/libs/SuperMap.Include.js');
    _SospIncludeScript('script/osp.js');

}