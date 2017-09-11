var PlaceHolder = {
    _support: (function() {
        return 'placeholder' in document.createElement('input');
    })(),
    //提示文字的样式，需要在页面中其他位置定义
    className: 'abc',
 
    init: function() {
        if (!PlaceHolder._support) {
            //未对textarea处理，需要的自己加上
            var inputs = document.getElementsByTagName('input');
            PlaceHolder.create(inputs);
        }
    },
 
    create: function(inputs) {
        var input;
        if (!inputs.length) {
            inputs = [inputs];
        }
        for (var i = 0, length = inputs.length; i <length; i++) {
            input = inputs[i];
            if (!PlaceHolder._support && input.attributes && input.attributes.placeholder) {
                PlaceHolder._setValue(input);
				input.attachEvent('onfocus', function(e) {
					if (input.value === input.attributes.placeholder.nodeValue) {
						input.value = '';
						//this.className = '';
					}
				});
				input.attachEvent('onblur', function(e) {
					if (this.value === '') {
						PlaceHolder._setValue(this);
					}
				});
            }
        }
    },
 
    _setValue: function(input) {
        input.value = input.attributes.placeholder.nodeValue;
        //input.className = PlaceHolder.className;
    }
};

//页面初始化时对所有input做初始化
//PlaceHolder.init();
//或者单独设置某个元素
PlaceHolder.create(document.getElementById('ssearch_box_input'));

//获取页面显示区域高度
function getWindowHeight() {
	if (jQuery.browser.msie) {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientHeight : document.body.clientHeight;
	}
	else {
		return self.innerHeight;
	} 
}

//获取页面显示区域宽度
function getWindowWidth() {
	if (jQuery.browser.msie) {
		return document.compatMode == "CSS1Compat" ? document.documentElement.clientWidth : document.body.clientWidth;
	}
	else {
		return self.innerWidth;
	} 
}

//高度自适应
function resizeApp(arg){
	//输入框默认焦点
	//document.getElementById("ssearch_box_input").focus();
	var bodyHeight  = getWindowHeight();
	var bodyWidth = getWindowWidth();
	var _head = document.getElementById('header');
	var hearderHeight = jQuery("#header").height();
	var toolsHeight = jQuery("#mainMap > .tools").height();
	
	if( _head.style.display!='none' && bodyHeight - hearderHeight> 0){//非全屏
		if(document.getElementById("leftbar") != null)
		document.getElementById("leftbar").style.height = bodyHeight - hearderHeight + "px" ;
		/*设置面板内容区高度*/
//		if(document.getElementById("left_body") != null)
//		document.getElementById("left_body").style.height = bodyHeight - hearderHeight - toolsHeight + "px" ;
//		if(document.getElementById("left_body2") != null)
//		document.getElementById("left_body2").style.height = bodyHeight - hearderHeight - toolsHeight + "px" ;
		/*设置地图面板内容区高度*/
		// if(document.getElementById("hideButton") != null)
		// document.getElementById("hideButton").style.height = bodyHeight - hearderHeight + "px" ;
		
		if(document.getElementById("midArrow") != null)
		document.getElementById("midArrow").style.height = bodyHeight - hearderHeight + "px" ;
		if(document.getElementById("mainMap") != null)
		document.getElementById("mainMap").style.height = bodyHeight - hearderHeight + "px" ;
		if(document.getElementById("smLayer") != null){
			if(bodyHeight - hearderHeight - toolsHeight > 0) {
				document.getElementById("smLayer").style.height = bodyHeight - hearderHeight - toolsHeight + "px";
			}
		}
		var mainMapWidth = bodyWidth - jQuery("#leftbar").width() - 1;
		if(jQuery("#hideButton > img").attr("src") == "images/midArrow_right.gif"){	//左侧已经展开
			mainMapWidth = bodyWidth;
		}
		jQuery('#mainMap, #smLayer').width(mainMapWidth);
	}else{
		document.getElementById("mainMap").style.height=bodyHeight+"px";
		document.getElementById("smLayer").style.height=bodyHeight-toolsHeight+"px";
		jQuery('#mainMap, #smLayer').width(bodyWidth);
	}

//	var mainMapWidth = bodyWidth -1;
//	if(jQuery("#entireScreen").attr("alt") == "entire"){//如果是非全屏状态
//		mainMapWidth = mainMapWidth - jQuery("#leftbar").width();
//	}
//	jQuery('#mainMap, #smLayer').width(mainMapWidth);
	
	//页面初始化完毕后出图
	if(arg == "initialize") {
		initializeMap();
	}
	else if(arg == "resize" && !map == false) {
		mapResize();
	}
}

function resize() {
	
	var _rWidth;
	var _rHeight;
	
	var bodyHeight  = getWindowHeight();
	var bodyWidth = getWindowWidth();
	
	var _leftBar = document.getElementById("leftbar");
	var _head = document.getElementById('header');
	var _arrow =document.getElementById('hideButton');
	if(_leftBar.style.display == "none") {
		if(_arrow.style.display == "none") {
			_rWidth = bodyWidth;
		}
		else {
			_rWidth = bodyWidth - 8;
		}
	}
	else {
		_rWidth = bodyWidth - jQuery("#leftbar").width()-5;
	}
	if(_head.style.display == "none") {
		_rHeight = bodyHeight - 28;
	}
	else {
		_rHeight = bodyHeight - 28 - 51;
	}
	
	jQuery('#mainMap').css('width', _rWidth + 'px');
	jQuery('#mainMap').css('height', _rHeight + 'px');
	
	map.resize(_rWidth, _rHeight);
}
/*
//左侧内容的收缩功能
function hideLeftDiv(){
	//获取左边的DIV对象和图片对象
	var _leftdiv =document.getElementById('leftbar');
	var _buttondiv =document.getElementById('midArrow');
	var _rightWidth =document.getElementById('mainMap');
	
	//如果对象存在，则进行控制
	if(_leftdiv){
		//1. 判断当前状态
		var _curStateShow = true;
		if(_buttondiv.className!="arrow_left"){
			_curStateShow=false;
		}
		
		//2. 根据状态修改DIV的显示隐藏和图片样式
		if(_curStateShow){
			_leftdiv.style.display='none';
			_buttondiv.className='arrow_right';
			
		}else{
			_rightWidth.style.position = 'absolute';
			_leftdiv.style.display='block';
			_buttondiv.className='arrow_left';
		}
	}
}*/

//地图上的全屏功能
var leftbarIsDisplay = false;
function screem(){
	var _leftdiv =document.getElementById('leftbar');
	var _arrow =document.getElementById('hideButton');
	var _header =document.getElementById('header');
	var _word =document.getElementById('entireScreen');
	var _right_map =document.getElementById('mainMap');
	var _map = document.getElementById("smLayer");
	var bodyHeight = getWindowHeight();
	
	if(_word.alt == "entire"){
		if(_leftdiv.style.display == "none"){
			leftbarIsDisplay = true;
		}else{
			_leftdiv.style.display="none";
		}
		_arrow.style.display="none";
		_header.style.display="none";
		_word.alt = "close";
		_word.title = "退出全屏";
		_right_map.style.height=bodyHeight+"px";
		_map.style.height=bodyHeight-28+"px";
		var html="<img src='images/map_marker/tuichuquanp.png'></img>退出全屏"
		jQuery("#a_fullscreen").html(html);
	}
	else{
		if(!leftbarIsDisplay){
			_leftdiv.style.display="";
		}else{
			leftbarIsDisplay = false;
		}
		_arrow.style.display="";
		_header.style.display="";
		_word.alt = "entire";
		_word.title = "全屏";
		_right_map.style.position = 'absolute';
		_right_map.style.height=bodyHeight - 77+"px";
		_map.style.height=bodyHeight - 77 - 28+"px";
		var html="<img src='images/map_marker/fullScreen.png'></img>全屏"
		jQuery("#a_fullscreen").html(html);
	}
}

//面板切换
function switchPanel(arg){
	jQuery("#search_result").html("");
	jQuery("#divPage").html("");
	jQuery("#bottomDistrictPosition").css("display","block");
	jQuery("#busPath_r").html("");
	jQuery("#busPath_road").html("");
	jQuery("#findPath_result").css("display","none");
	var _bt1 =document.getElementById('panel1');
	//var _bt2 =document.getElementById('panel2');
	//var _bt3 =document.getElementById('panel3');
	
	var _mapSearch =document.getElementById('left_body');
	var _mapSave =document.getElementById('left_body2');
	
	if(arg=='1'){
		if(_mapSearch.style.display=="block"){
			return;
		}else{
			_mapSearch.style.display="block";
			_mapSave.style.display="none";
			// _bt1.className="anchor-selected";
			// _bt1.href = "javascript:void(0)";
			// _bt2.className="anchor";
			// _bt2.href = "#";
		}
	}else if(arg=='2'){
		if(_mapSave.style.display=="block"){
			return;
		}else{
			_mapSave.style.display="block";
			_mapSearch.style.display="none";
			// _bt2.className="anchor-selected";
			// _bt2.href = "javascript:void(0)";
			// _bt1.className="anchor";
			// _bt1.href="#";
		}
	}
}

//折叠面板
function fold(arg) {
	var _btn1 = document.getElementById("fold1");
	var _btn2 = document.getElementById("fold2");
	var _panel1 = document.getElementById("bottomDistrictPosition");
	var _panel2 = document.getElementById("bottomTypePosition");
	
	if(arg == '1') {
		if(_panel1.style.display == "block") {
			_panel1.style.display = "none";
			_btn1.src = "images/expan.gif";
		}
		else {
			_panel1.style.display = "block";
			_btn1.src = "images/close.gif";
		}
	}
	else if (arg == '2') {
		if(_panel2.style.display == "block") {
			_panel2.style.display = "none";
			_btn2.src = "images/expan.gif";
		}
		else {
			_panel2.style.display = "block";
			_btn2.src = "images/close.gif";
		}
	}
}