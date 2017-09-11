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