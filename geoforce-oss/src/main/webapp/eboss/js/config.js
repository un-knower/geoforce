var urls = {};
// urls.server = "http://192.168.10.251:8080/egispboss";
urls.server = "/egispboss";

/*用户管理*/
urls.user = {};
urls.user.server = urls.server + "/userService?";
urls.user.add = urls.user.server + "method=addUser&callbacks=?";
urls.user.search = urls.user.server + "method=queryUserList&callbacks=?";
urls.user.update = urls.user.server + "method=updateUser&callbacks=?";
urls.user.remove = urls.user.server + "method=deleteUserInfo&callbacks=?";
urls.user.detail = urls.user.server + "method=queryUserInfo&callbacks=?";
urls.user.querychilduser = urls.user.server + "method=queryChildUser";
urls.user.exportUserExcel = urls.user.server +"method=exportUserExcel";

/*产品管理*/
urls.product = {};
urls.product.server = urls.server + "/moduleService?";
urls.product.add = urls.product.server + "method=addModule&callbacks=?";
urls.product.search = urls.product.server + "method=queryServiceList&callbacks=?";
urls.product.update = urls.product.server + "method=updateModule&callbacks=?";
urls.product.detail = urls.product.server + "method=queryModule&callbacks=?";
urls.product.remove = urls.product.server + "method=deleteModule&callbacks=?";

/*订单管理*/
urls.order = {};
urls.order.server = urls.server + "/orderService?";
urls.order.add = urls.order.server + "method=addOrder&callbacks=?";
urls.order.search = urls.order.server + "method=queryOrderList&callbacks=?";
urls.order.detail = urls.order.server + "method=queryOrderDetails&callbacks=?";
urls.order.audit = urls.order.server + "method=auditOrder&callbacks=?";
urls.order.remove = urls.order.server + "method=deleteOrder&callbacks=?";
urls.order.update = urls.order.server + "method=updateOrder&callbacks=?";
urls.order.itemupdate = urls.order.server + "method=updateOrderItem&callbacks=?";
urls.order.itemremove = urls.order.server + "method=deleteOrderItem&callbacks=?";

/*权限管理*/
urls.permission = {};
urls.permission.server = urls.server + "/privilegeService?";
urls.permission.add = urls.permission.server + "method=addPrivilege&callbacks=?";
urls.permission.search = urls.permission.server + "method=queryPrivilegeList&callbacks=?";
urls.permission.detail = urls.permission.server + "method=queryPrivilegeDetail&callbacks=?";
urls.permission.update = urls.permission.server + "method=updatePrivilege&callbacks=?";
urls.permission.remove = urls.permission.server + "method=deletePrivilege&callbacks=?";

/*角色管理*/
urls.role = {};
urls.role.server = urls.server + "/roleService?";
urls.role.add = urls.role.server + "method=addRole&callbacks=?";
urls.role.search = urls.role.server + "method=queryRoleList&callbacks=?";
urls.role.update = urls.role.server + "method=updateRole&callbacks=?";
urls.role.addper = urls.role.server + "method=addPrivileges&callbacks=?";
urls.role.delper = urls.role.server + "method=removePrivileges&callbacks=?";
urls.role.updateper = urls.role.server + "method=addOrRmPrivileges&callbacks=?";
urls.role.remove = urls.role.server + "method=deleteRole&callbacks=?";

/*员工管理*/
urls.employee = {};
urls.employee.server = urls.server + "/staffService?";
urls.employee.add = urls.employee.server + "method=addStaff&callbacks=?";
urls.employee.search = urls.employee.server + "method=queryStaffList&callbacks=?";
urls.employee.detail = urls.employee.server + "method=queryStaffDetail&callbacks=?";
urls.employee.update = urls.employee.server + "method=updateStaff&callbacks=?";
urls.employee.updatepwd = urls.employee.server + "method=changePassword&callbacks=?";
urls.employee.updaterole = urls.employee.server + "method=addOrRmRoles&callbacks=?";
urls.employee.login = urls.employee.server + "method=login&callbacks=?";
urls.employee.remove = urls.employee.server + "method=deleteStaff&callbacks=?";

/*日志管理*/
urls.log={};
urls.log.server=urls.server+"/sysUpdateLogService?";
urls.log.add=urls.log.server+"method=addLog";
urls.log.search = urls.log.server + "method=queryLogList";
urls.log.detail = urls.log.server + "method=queryLogInfo";
urls.log.update = urls.log.server + "method=updateLog";
urls.log.remove = urls.log.server + "method=deleteLog";

/*数据管理*/
/*网点数据*/
urls.point={};
urls.point.server = urls.server+"/pointService?";
urls.point.querypointscount = urls.point.server + "method=queryPointsCount";
urls.point.exportpoints = urls.point.server + "method=exportPoints";
urls.point.remove = urls.point.server + "method=deletePoints";
urls.point.getadminelements = urls.point.server + "method=getAdminElements";
urls.point.querypointgroup = urls.point.server + "method=queryPointGroup";

/*区划数据*/
urls.region={};
urls.region.server = urls.server+"/areaService?";
urls.region.queryareascount = urls.region.server + "method=queryAreasCount";
urls.region.exportareas = urls.region.server + "method=exportAreas";
urls.region.remove = urls.region.server + "method=deleteAreas";

/*分单数据*/
urls.fendan={};
urls.fendan.server = urls.server+"/fendanService?";
urls.fendan.queryorderscount = urls.fendan.server + "method=queryLogisticsOrderCount";
urls.fendan.exportOrders = urls.fendan.server + "method=exportLogisticsOrder";
urls.fendan.remove = urls.fendan.server + "method=deleteLogisticsOrder";
urls.fendan.querybatchs = urls.fendan.server + "method=queryBatchs";
urls.fendan.querystatus = urls.fendan.server + "method=queryStatus";

var pageSize = 10;

var user = {};
user.permissions = [];

/*字符串去空格*/
String.prototype.trim = function(){  
　 return this.replace(/(^\s*)|(\s*$)/g, "");  
}  

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


/**
 * 打开结果提示
 */
var timer_popup = null;
function showPopover(string) {   
    $("#popover_content").html(string);
    $("#popover_result").css("display", "block");

    if(timer_popup){
        clearTimeout(timer_popup);
    }      
    timer_popup = setTimeout("hidePopover();", 3500);
}

/**
 * 摧毁结果提示
 */
function hidePopover() {
    // $("#table_keys").popover("hide");
    if(timer_popup){
        clearTimeout(timer_popup);
    }       
    $("#popover_result").css("display", "none");
}

/**
 * loading
 */
function showMaskLoading() {
    $(".overlay").css({'display':'block','opacity':'0.8'});        
    $(".loadingbox").stop(true).animate({'margin-top':'300px','opacity':'1'},200);
}

/**
 * hide loading
 */
function hideMaskLoading() {    
    $(".loadingbox").stop(true).animate({'margin-top':'250px','opacity':'0'},400);
    $(".overlay").css({'display':'none','opacity':'0'});
}

jQuery.cookie = function(name, value, options) {
	if (typeof value != 'undefined') { // name and value given, set cookie
		options = options || {};
		if (value === null) {
			value = '';
			options.expires = -1;
		}
		var expires = '';
		if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
			var date;
			if (typeof options.expires == 'number') {
				date = new Date();
				date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
			} else {
				date = options.expires;
			}
			expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
		}
		var path = options.path ? '; path=' + options.path : '';
		var domain = options.domain ? '; domain=' + options.domain : '';
		var secure = options.secure ? '; secure' : '';
		document.cookie = [name, '=', encodeURIComponent(value), expires, path,
			domain, secure
		].join('');
	} else { // only name given, get cookie
		var cookieValue = null;
		if (document.cookie && document.cookie != '') {
			var cookies = document.cookie.split(';');
			for (var i = 0; i < cookies.length; i++) {
				var cookie = jQuery.trim(cookies[i]);
				// Does this cookie string begin with the name we want?
				if (cookie.substring(0, name.length + 1) == (name + '=')) {
					cookieValue = decodeURIComponent(cookie
						.substring(name.length + 1));
					break;
				}
			}
		}
		return cookieValue;
	}
};

/**
 * 生成页头
 */
function createHeader(){
    var html = '';
    html += '<div class="inner">';
    html += '   <>';
    html += '</div>';
}

/**
 * 计算页数
 * 
 * @param: total - 总页数
 * @param: _pageIndex - 当前第几页
 * @param: limit - 每页显示个数，默认为10
 *
 * @return: html - 分页控件显示的内容
 */
function setPage(total, _pageIndex, divid) {
    var limit = pageSize;

    var html = '';

    //计算总页数
    var _pageCount = Math.ceil(total/limit);
    if(_pageIndex == 0) {
        html += '<li class="disabled" title="第一页"><a href="javascript:;">&laquo;</a></li>';
    }
    else {
        html += '<li  title="第一页"><a href="javascript:setpageno('+ divid +', '+ 0 +');">&laquo;</a></li>';        
    }


    if(_pageIndex < 3)　{
        for(var i=1; (i<=5 && i<=_pageCount && _pageIndex <= _pageCount); i++) {
            if(i !== (_pageIndex+1)) {
                html += '<li><a href="javascript:setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
            }
            else {
                html += '<li class="active"><a href="javascript:;">'+ i +'</a></li>';
            }
        }
    }
    else {
        //pageIndex后应该有几个页数
        var behind_index = (　(_pageCount-_pageIndex > 3) ? 3  :　_pageCount-_pageIndex);
        
        //pageIndex前应该有几个页数
        var before_index = 5 - behind_index - 1;

        var startIndex = (_pageIndex - before_index > 0 ? _pageIndex - before_index : 1 );
        var limit_length = _pageIndex + behind_index;
        for( i = startIndex; i <= limit_length ; i++ ){
            if(i !== (_pageIndex + 1) ) {
                html += '<li><a href="javascript:setpageno('+ divid +', '+ (i-1) +');">'+ i +'</a></li>';
            }
            else {
                html += '<li class="active"><a href="javascript:;">'+ i +'</a></li>';              
            }
        }
    }

    if(_pageIndex == (_pageCount - 1)) {
        html += '<li  title="最后一页" class="disabled"><a href="javascript:;">&raquo;</a></li>';
    }
    else {
        html += '<li  title="最后一页"><a href="javascript:setpageno('+ divid +', '+ (_pageCount-1) +');">&raquo;</a></li>';        
    }

    return html;
}

/**
 * 为div pager绑定点击事件
 * 
 * @param: id - div的id
 * @param: index - 当前第几页
 */
function setpageno(id, index) {
    var pager = $("#" + id);
    pager.attr("page", index);

    var target = pager.attr("data-target");
    switch(target) {
        case "user":
            User.search();
            break;
        case "product":
            Product.search();
            break;
        case "permission":
            Permission.search();
            break;
        case "order" :
            Order.search();
            break;
        case "employee":
            Employee.search();
            break;
        case "role":
            Role.search();
            break;
        case 'key':
            break;
        case 'userforpopup':
            Order.getUserListForPopup();
            break;
        case 'log':
            Log.search();
            break;
    }
}

/**
 * hash表
 */
function HashMap(){
    this.size = 0;
    this.entry = new Object();

    //添加
    this.put = function (key, value) {
     ///<summary>添加子项</summary>
        ///<param name="key" type="object">索引key,任意类型</param>
        ///<param name="value" type="object">索引值value,任意类型</param>
        if (!this.containsKey(key)) {
            this.size++;
        }
            this.entry[key] = value;
    };
    //获取
    this.get = function (key) {
    ///<summary>通过索引key获取子项</summary>
        ///<param name="key" type="object">key,任意类型</param>
        return this.containsKey(key) ? this.entry[key] : null;
    };
    this.getkey = function (value) {
        ///<summary>通过索引key获取子项</summary>
        ///<param name="key" type="object">key,任意类型</param>
        for (var prop in this.entry) {
            if (this.entry[prop] == value) {
                return prop;
            }
        }
        return null;

    };
     //移除
    this.remove = function (key) {
    ///<summary>通过索引key删除子项</summary>
        ///<param name="key" type="object">key,任意类型</param>
        if (this.containsKey(key) && (delete this.entry[key])) {
            this.size--;
        }
    };
     //通过key查看是否已经存在
    this.containsKey = function (key) {
    ///<summary>检查索引key是否已经存在</summary>
        ///<param name="key" type="object">key,任意类型</param>
        return (key in this.entry);
    };
     //查看是否还有该值
    this.containsValue = function (value) {
    ///<summary>判断索引值value是否已经存在</summary>
        ///<param name="value" type="object">value,任意类型</param>
        for (var prop in this.entry) {
            if (this.entry[prop] == value) {
                return true;
            }
        }
        return false;
    },
    //清空
    this.clear = function () {
    ///<summary>清空HashMap对象</summary>
        this.size = 0;
        this.entry = {};
    },
     //取得所有值
    this.values = function () {
    ///<summary>获取HashMap所有索引值</summary>
        var values = new Array();
        for (var prop in this.entry) {
            values.push(this.entry[prop]);
        }
        return values;
    }
}
//兼容低级浏览器里的JSON
/*
(function(){
    window["JSON"] = {};
    function f(n){return n<10?"0"+n:n}if(typeof Date.prototype.toJSON!=="function"){Date.prototype.toJSON=function(key){return isFinite(this.valueOf())?this.getUTCFullYear()+"-"+f(this.getUTCMonth()+1)+"-"+f(this.getUTCDate())+"T"+f(this.getUTCHours())+":"+f(this.getUTCMinutes())+":"+f(this.getUTCSeconds())+"Z":null};String.prototype.toJSON=Number.prototype.toJSON=Boolean.prototype.toJSON=function(key){return this.valueOf()}}var cx=/[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,escapable=/[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,gap,indent,meta={"\b":"\\b","\t":"\\t","\n":"\\n","\f":"\\f","\r":"\\r",'"':'\\"',"\\":"\\\\"},rep;function quote(string){escapable.lastIndex=0;return escapable.test(string)?'"'+string.replace(escapable,function(a){var c=meta[a];return typeof c==="string"?c:"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})+'"':'"'+string+'"'}function str(key,holder){var i,k,v,length,mind=gap,partial,value=holder[key];if(value&&typeof value==="object"&&typeof value.toJSON==="function"){value=value.toJSON(key)}if(typeof rep==="function"){value=rep.call(holder,key,value)}switch(typeof value){case"string":return quote(value);case"number":return isFinite(value)?String(value):"null";case"boolean":case"null":return String(value);case"object":if(!value){return"null"}gap+=indent;partial=[];if(Object.prototype.toString.apply(value)==="[object Array]"){length=value.length;for(i=0;i<length;i+=1){partial[i]=str(i,value)||"null"}v=partial.length===0?"[]":gap?"[\n"+gap+partial.join(",\n"+gap)+"\n"+mind+"]":"["+partial.join(",")+"]";gap=mind;return v}if(rep&&typeof rep==="object"){length=rep.length;for(i=0;i<length;i+=1){k=rep[i];if(typeof k==="string"){v=str(k,value);if(v){partial.push(quote(k)+(gap?": ":":")+v)}}}}else{for(k in value){if(Object.hasOwnProperty.call(value,k)){v=str(k,value);if(v){partial.push(quote(k)+(gap?": ":":")+v)}}}}v=partial.length===0?"{}":gap?"{\n"+gap+partial.join(",\n"+gap)+"\n"+mind+"}":"{"+partial.join(",")+"}";gap=mind;return v}}if(typeof JSON.stringify!=="function"){JSON.stringify=function(value,replacer,space){var i;gap="";indent="";if(typeof space==="number"){for(i=0;i<space;i+=1){indent+=" "}}else{if(typeof space==="string"){indent=space}}rep=replacer;if(replacer&&typeof replacer!=="function"&&(typeof replacer!=="object"||typeof replacer.length!=="number")){SGIS.Debug("JSON.stringify Exception")}return str("",{"":value})}}if(typeof JSON.parse!=="function"){JSON.parse=function(text,reviver){var j;function walk(holder,key){var k,v,value=holder[key];if(value&&typeof value==="object"){for(k in value){if(Object.hasOwnProperty.call(value,k)){v=walk(value,k);if(v!==undefined){value[k]=v}else{delete value[k]}}}}return reviver.call(holder,key,value)}text=String(text);cx.lastIndex=0;if(cx.test(text)){text=text.replace(cx,function(a){return"\\u"+("0000"+a.charCodeAt(0).toString(16)).slice(-4)})}if(/^[\],:{}\s]*$/.test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,"@").replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,"]").replace(/(?:^|:|,)(?:\s*\[)+/g,""))){j=eval("("+text+")");return typeof reviver==="function"?walk({"":j},""):j}SGIS.Debug("JSON.parse Exception")}}
})();*/



Date.prototype.format =function(format){
    var o = {
        "M+" : this.getMonth()+1, //month
        "d+" : this.getDate(), //day
        "h+" : this.getHours(), //hour
        "m+" : this.getMinutes(), //minute
        "s+" : this.getSeconds(), //second
        "q+" : Math.floor((this.getMonth()+3)/3), //quarter
        "S" : this.getMilliseconds() //millisecond
    }
    if(/(y+)/.test(format)) 
        format=format.replace(RegExp.$1, (this.getFullYear()+"").substr(4- RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
    RegExp.$1.length==1? o[k] :
    ("00"+ o[k]).substr((""+ o[k]).length));
    return format;
}
/*传入参数拼接成url*/
function generateParamURL(param){
    var param_url="";
    $.each(param, function(i, val) {  
        param_url += "&" + i + "=" + val;  
    });
    return param_url;
}