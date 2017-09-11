$(document).ready(function(){
	//重写alert;
	if(typeof($.layer) != 'undefined'){
		window.alert = function(msg){
			layer.alert(msg,12);
		}
	}
	
});
/**
 * ajax返回值 session失效 控制跳转
 * @param ajaxMsg
 * @return
 */
function checkSession(ajaxMsg){
	if(ajaxMsg == "-999"){//session失效或无此功能 重新登录
//		if(parent.window){
//			parent.window.location.href = ctx+"/commons/timeout.jsp"
//		}else{
//			window.location.href = ctx+"/commons/timeout.jsp"
//		}
		window.location.href = ctx+"/commons/timeout.jsp"
		return false;
	}
	return true;
}
/**
 * 判断用户是否具有 增删 修改权限
 * @return
 */
function checkEditRight(){
	var right = $("#userEditRight").val();
	//企业管理 框架情况
	right = right?right:parent.$("#userEditRight").val();
	if(right == null || right == ""){
		right = "0";
	}
	if(right == "0"){
		$.messager.alert(commonLang.tooltip,validationLang.checkRight,"error");  //对不起，您只有查询的权限
		return false;
	}else{
		return true;
	}
}
/**
 * 检验用户是否有此功能权限
 * @return
 */
function checkHasFun(funName){
	var t = getMilTime();
	var ret = false;
	$.ajax({
		type : "POST",
		async : false,
		url : ctx+"/piccode/commonServiceAction!checkUserFuns.do",
		data : "t="+t+"&functionName="+funName,
		dataType : "json",
		success : function(msg) {
			if(msg == "1"){//存在
				ret = true;
			}
		},
		error : function() {
        }
	});
	return ret;
}
function appendIFrame(id){
	var objdiv=document.getElementById(id);//获取div对象   
	var w = objdiv.offsetWidth;//获取div对象的宽度   
	var h = objdiv.offsetHeight;//获取div对象的高度   
	var ifrm = document.createElement("iframe");//创建iframe节点   
	ifrm.src ="javascript:false";//设置iframe节点的src属性为空   
	ifrm.height=h;//设置iframe的高度与div对象一致   
	ifrm.width=w;//设置iframe的宽度与div对象一致   
	ifrm.style.cssText = "position:absolute;top:0;left:0;width:100%;height:100%;z-index:-1;filter:alpha(opacity=0);";//设置div的css属性，其中z-index表示iframe所在的层次，-1表示在div所在的层之下   
	objdiv.appendChild(ifrm);
}
/**
 * 加载数据显示loading页面
 * @param divObj
 * @param loadingObj
 * @return
 */
function showLoading(divObj,loadingObj){
	var postion = divObj.position();
	var wh = divObj.width();
	var ht = divObj.height();
	loadingObj.css("left",postion.left+wh*0.5);
	loadingObj.css("top",postion.top+ht*0.5);
	loadingObj.show();
}
/**
 * 隐藏loading页面
 * @param loadingObj
 * @return
 */
function hideLoading(loadingObj){
	loadingObj.hide();
}
/**
 * 处理空数据
 * @param value
 * @return
 */
function dealNull(value){
	if(value == null || value == "null" || value == "undefined"){
		return "";
	}
	return value;
}

/**
 * 判断传入的对象是否是数组
 * @param source
 * @return
 */
function arrayCheck(source){
	if(source == null){
		return false;
	}
	return '[object Array]' === Object.prototype.toString.call(source);
}

/**
 * 获得当前时间的长串
 * @return
 */
function getMilTime(){
	var t = new Date();
	return t.getTime();
}
/**
 * 时间数字串返回字符串 yyyy-MM-dd HH:mm:ss
 * @param dateTime
 * @return
 */
function formatDate(milTime){
	var dateTime = new Date(milTime);
	var year=dateTime.getFullYear();
	var month=dateTime.getMonth()+1;
	month = month < 10 ? "0"+month : month;
	var day =dateTime.getDate();
	day = day<10?"0"+day:day;
	var hour=dateTime.getHours();
	hour = hour<10?"0"+hour:hour;
	var minute=dateTime.getMinutes();
	minute = minute<10?"0"+minute:minute;
	var second=dateTime.getSeconds(); 
	second = second<10?"0"+second:second;

	return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;   
}
/**
 * 墨卡托投影坐标转换为经纬度坐标
 */
function meterXY2GeoLoc(x, y, precision){
	var earthCircumferenceInMeters = new Number(40075016.685578488);
	var halfEarthCircumferenceInMeters = earthCircumferenceInMeters / 2;
	
	var geoX = x/halfEarthCircumferenceInMeters*180;
	var geoY = y/halfEarthCircumferenceInMeters*180;
	geoY = Math.atan(Math.exp(geoY * (Math.PI / 180.0)))*360.0/Math.PI - 90;
	
	geoX = setPrecision(geoX, precision);
	geoY = setPrecision(geoY, precision);
	
	var obj = new Object();
	obj.lngX = geoX;
	obj.latY = geoY;
	return obj;
}

/**
 * 经纬度坐标转墨卡托投影坐标
 */
function geoLoc2MeterXY(x, y){
	var earthCircumferenceInMeters = new Number(40075016.685578488);
	var halfEarthCircumferenceInMeters = earthCircumferenceInMeters / 2;
	
	var geoX = new Number(x);
	var geoY = new Number(y);
	
	var mx = geoX / 180.0 * halfEarthCircumferenceInMeters;
	var my = Math.log(Math.tan((90 + geoY) * Math.PI / 360.0)) / (Math.PI / 180.0);
	my = my / 180.0 * halfEarthCircumferenceInMeters;
	
	var obj = new Object();
	obj.lngX = mx;
	obj.latY = my;
	return obj;
}
//按输入精度保留经纬度
function setPrecision(num, precision){
	var temp = new String(num);
	
	var pos = temp.indexOf(".");
	if (temp.length > (pos + precision)){
		var num = Number(temp).toFixed(precision);
		return num;
	}
	
	return temp;
}
/**
 * 校验是否是正整数
 * @param num
 * @return
 */
function intCheck(num){
	if(num == null || $.trim(num) == ""){
		return false;
	}
	var numReg = /^[1-9][\d]*$/;
	return numReg.test(num);
}
/**
 * 校验是数字范围0-255
 * @param num
 * @return
 */
function boundaryCheck(num){
	if(num == null || $.trim(num) == ""){
		return false;
	}
	var numReg = /^[1-9][\d]*$/;
	return numReg.test(num);
}
/**
 * 校验输入是否是数字
 * @param num
 * @return
 */
function numCheck(num){
	if (num == null || $.trim(num) == ""){
		return false;
	}
	var numReg = /^([1-9][\d]{0,7}|0)(\.[\d]{0,6})?$/; 
	return numReg.test(num);
}
/**
 * 正则表达式 电话校验 包括 固话和手机
 * @param phone
 * @return false 不符合要求 true 符合要求
 */
function phoneCheck(phone){
	if(phone == null || phone == ""){
		return false;
	}
	var phoneReg=/^(13[0-9]{9}$)|(15[0-35-9][0-9]{8}$)|(14[0-9]{9}$)|([0-9]{1}[0-9]{2,3}-[1-9]{1}[0-9]{5,8}$)|(18[05-9][0-9]{8}$)/;
	return phoneReg.test(phone);
}
/**
 * 正则表达式 手机校验
 * @param phone
 * @return false 不符合要求 true 符合要求
 */
function mobileCheck(mobile){
	if(mobile == null || mobile == ""){
		return false;
	}
	var mobileReg=/^0?(13[0-9]|15[012356789]|18[02356789]|14[57])[0-9]{8}$/;
	return mobileReg.test(mobile);
}
/**
 * 正则表达式 固话校验
 * @param phone
 * @return false 不符合要求 true 符合要求
 */
function phoneGhCheck(phoneGh){
	if(phoneGh == null || phoneGh == ""){
		return false;
	}
	var phoneGhReg=/^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/;
	return phoneGhReg.test(phoneGh);
}
/**
 * 正则表达式 邮编校验
 * @param zipCode
 * @return false 不符合要求 true 符合要求
 */
function zipcodeCheck(zipCode){
	if(zipCode == null || zipCode == ""){
		return false;
	}
	var zipcodeReg = /^[1-9][0-9]{5}$/;
	return zipcodeReg.test(zipCode);
}
/**
 * 正则表达式 邮箱校验
 * @param email
 * @return false 不符合要求 true 符合要求
 */
function emailCheck(email){
	if(email == null || email == ""){
		return false;
	}
	var emailReg = /^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
	return emailReg.test(email);
}
/**
 * 正则 验证身份证号
 * @param card
 * @return
 */
function cardNodeCheck(card){
	if(card == null || card == ""){
		return false;
	}
	var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
	return reg.test(card);
}
/**
 * 密码验证 6-16位 字母或数字
 * @param pwd
 * @return
 */
function pwdCheck(pwd){
	if(pwd == null || pwd == ""){
		return false;
	}
	var pwdReg = /^[a-zA-Z\d]{6,16}$/;
	return pwdReg.test(pwd);

}
/**
 * 名必须为英文 3-20位
 * @param name
 * @return
 */
function eNameCheck(name){
	if(name == null || name == ""){
		return false;
	}
	var nameReg = /^[a-zA-Z\d]{3,20}$/;
	return nameReg.test(name);
}
/**
 * 文本输入框校验 是否含有特殊字符
 * 含有特殊字符 返回false 否则返回true
 * @param text
 * @return
 */
function textCheck(text){
	if(text == null){
		text = "";
	}
	text = $.trim(text);
	var reg = /^[^\|"'<>`~!@#\$\^&*\{\}\?;:]*$/;
	return reg.test(text);
}
/**
 * 输入框绑定自动提示
 * @param inputId 输入框id
 * @param url 获取后台数据的url
 * @param itemType 可选 排车管理使用 用于查item表的类型
 * @return
 */
function bindAutocomplete(inputId,url,itemType){
	var type = '';
	if(itemType){
		type = itemType;
	}
	var inpWidth = $("#"+inputId).width();
	$("#"+inputId).autocomplete(url, 
	{
		minChars: 0,//输入几个字符开始提示
	    max:10,
	    width :inpWidth,
	    mustMatch: false,//如果用户输入在数据库查不到则将文本框置空
	    matchCase:false,//不区分大小写
	    dataType: 'json',
	    scroll: false,
	    scrollHeight: 300,
	    extraParams: {autoProperty:function() {
			return $('#'+inputId).val();},autoType:type
		},
	    parse: function(data) {
		    if(data == null || data == ""){
				return null;
		    }
            return $.map(eval(data), function(row) {
                return {
                 data: row,
                 value: row.name,
                 result: row.name
               }
            });
	    },
		formatItem: function (row, i, max) {
	    	return row.name;
	    }
	}).result(function(event, data, formatted) { //选中某个记录的回调
		if(data){
			$("#"+inputId).val(data.name);
		}
		
	});
}

function bootstrapAutocomplete(inputId,url){
	$('#'+inputId).autocomplete({
        source:function(query,process){
            var matchCount = this.options.items;//返回结果集最大数量
            $.post(url,{"text":query,"row":10},function(data){
                return process(data);
            });
        },
        formatItem:function(item){
            return item["name"];
        },
        setValue:function(item){
            return {'data-value':item["name"],'real-value':item["id"]};
        }
    });
}



/**
 * poi查询
 * 输入框绑定自动提示
 * @param inputId 输入框id
 * @param url 获取后台数据的url
 * @return
 */
function poiAutocomplete(inputId,url){
	var inpWidth = $("#"+inputId).width();
	$("#"+inputId).autocomplete(url, 
	{
		minChars:0,//输入几个字符开始提示
	    max:10,
	    width :inpWidth,
	    mustMatch: false,//如果用户输入在数据库查不到则将文本框置空
	    matchCase:false,//不区分大小写
	    dataType: 'json',
	    scroll: false,
	    scrollHeight: 300,
	    extraParams: {address:function() {
			return $('#'+inputId).val();
		  },startRecord:0
		},
	    parse: function(data) {
		    if(data == null || data == ""){
				return null;
		    }
		    var jsonstr = "[";
		    var pois = data.result[0].records;
		    var totalCount = data.result[0].totalCount;
		    if(pois != null && pois.length != 0){
			    for(var i=0;i<pois.length;i++){
			    	var vals = pois[i].fieldValues;
			    	var lng = vals[1];
			    	var lat = vals[2];
			    	var name = vals[3];
			    	var obj = meterXY2GeoLoc(lng,lat,6);
			    	jsonstr += "{'name':'"+name+"','lng':"+obj.lngX+",'lat':"+obj.latY+"},";
			    }
			    jsonstr = jsonstr.substring(0,jsonstr.length-1);
		    }
		    jsonstr += "]";
            return $.map(eval(jsonstr), function(row) {
                return {
                 data: row,
                 value: row.name,
                 result: row.name
               }
            });
	    },
		formatItem: function (row, i, max) {
	    	return row.name;
	    }
	}).result(function(event, data, formatted) { //选中某个记录的回调
		if(data){
			if(inputId == 'startAddr' || inputId == 'endAddr'){
				$("#longtitue_"+inputId).val(data.lng);
				$("#latitude_"+inputId).val(data.lat);
			}
			$("#"+inputId).val(data.name);
		}
		
	});
}

/**
 * 根据jquery对象绑定 自动提醒
 * @param jqObj
 * @param url
 * @return
 */
function bindAutoForObj(jqObj,url){
	jqObj.autocomplete(url, 
	{
		minChars: 2,//输入几个字符开始提示
	    max:10,
	    width :150,
	    mustMatch: true,//如果用户输入在数据库查不到则将文本框置空
	    matchCase:false,//不区分大小写
	    dataType: 'json',
	    scroll: false,
	    scrollHeight: 300,
	    extraParams:{autoProperty:function() {
			return jqObj.val();}
		},
	    parse: function(data) {
		    if(data == null || data == ""){
				return null;
		    }
            return $.map(eval(data), function(row) {
                return {
                 data: row,
                 value: row.name,
                 result: row.name
               }
            });
	    }
	    ,
	    formatItem: function (row, i, max) {
	    	//   var row=eval("("+row+")");//将JSON转换成对象 
	    	return row.name;
	    }
//	    ,
//	    formatResult: function(row, i, max) {
//        	alert(row.id);
//	    	if(showName){
//				$("#"+showName).val(row.id);
//			}
//	    	return row.license;
//	    }
	}).result(function(event, data, formatted) { //选中某个记录的回调
		if(data){
			jqObj.val(data.name);
		}
		
	});
}
/**
 * 默认的datagrid列表的设置
 * 返回datagrid对象
 */
function defDataGridOptions(isPage){
	var options = {
		loadMsg:commonLang.loadMsg,
		method:'post',
		pagination:isPage,
		rownumbers:true,
		singleSelect:false,
		fitColumns:true,
		fit:true,//自适应容器
		striped:true,
        selectOnCheck:true,
        checkOnSelect:true,
        pageNumber:1,// 当设置分页属性,初始化页号。 1 
        pageSize:10,//初始化每页条数
        pageList: [10,20,30],//可以设置每页记录条数的列表  
		onLoadSuccess:function(data){//数据加载成功后
			//权限及session校验
			if(!checkSession(data)){
				return;
			}
		},
		onLoadError:function(){
			$.messager.alert(commonLang.tooltip,commonLang.loadFail,"error");
		}
	};
	return options;
}
/**
 * datagrid分页默认显示格式
 * @return
 */
function defDatagridPageDisplay(datagridId){
	$('#'+datagridId).datagrid('getPager').pagination({//分页栏下方文字显示
		beforePageText: jqueryLang.dizi,//页数文本框前显示的汉字
		afterPageText: jqueryLang.yegong+' {pages} '+jqueryLang.yezi,
        displayMsg:jqueryLang.dangqian+'<font color="red">{from}</font> - <font color="red">{to}</font>'+jqueryLang.jilugong+'<font color="red">{total}</font>'+jqueryLang.tiaojilu
	});
}
/**
 * datagrid分页简单显示格式
 * @return
 */
function sampleDatagridPageDisplay(datagridId){
	$('#'+datagridId).datagrid('getPager').pagination({//分页栏下方文字显示
		beforePageText: '',//页数文本框前显示的汉字
		afterPageText: '/{pages}',
		displayMsg:jqueryLang.dangqian+'<font color="red">{from}</font> - <font color="red">{to}</font>'+jqueryLang.jilugong+'<font color="red">{total}</font>'+jqueryLang.tiaojilu
	});
}

/**
 * 添加分页样式-图片
 * @param table
 * @return ui-icon ui-icon-seek-prev
 *         ui-icon ace-icon fa fa-angle-left bigger-140
 */


/**
 * window.open已post方式跳转
 * url 跳转地址
 * name 跳转方式 如_self
 * @return
 */
function windowOpenWithPost(url,name){
	if(!url) return false;
	var windowOpen = null;
	if(url.indexOf('?') == -1){
		windowOpen = window.open(url,name);
		return windowOpen;
	}
	var urlArray = url.split("?");
	url = urlArray[0];
	
	var windowOpen = window.open(url,name);
	if(!windowOpen) return false;
	
	var params = urlArray[1].split("&");
	if(params.length == 0){
		windowOpen = window.open(url,name);
		return windowOpen;
	}
	var html = "";
	html += "<html><head></head><body>";
	html +="<form id='windowOpenFormId' method='post' action = '"+url+"'>";
	for(var i=0;i<params.length;i++){
		var keyVal = params[i].split("=");
		html += "<input type='hidden' name = '"+keyVal[0]+"' value = '"+keyVal[1]+"' />";
	}
	html += "</form>";
	html += "<script type = 'text/javascript'>";
	html += " document.getElementById(\"windowOpenFormId\").submit(); ";

	html += "</script></body></html>";
	
	windowOpen.document.write(html);
	return windowOpen;
}