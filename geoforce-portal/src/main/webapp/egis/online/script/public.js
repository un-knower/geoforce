//获取url地址
function GetUrlArgs(url) {
    try {
        var allArg = url.split("?");
        var ars = allArg[1].split("&");
        var result = new Object();
        for (var i in ars) {
            var vs = ars[i].split("=");
            if (vs[1])
                result[vs[0]] = vs[1];
        }

        return result;

    }
    catch (e) {
        return new Object();
    }
}

//服务地址
//var ospUrl = "http://cloud.supermap.com.cn";
var ospUrl = "http://services.supermapcloud.com";
//写入html,append为true时追加
function setHtml(divId, innerHtml, append) {
    if (!append) {
        document.getElementById(divId).innerHTML = innerHtml;
    } else {
    document.getElementById(divId).innerHTML += innerHtml;
    }
}
function divShow(divId) {
    document.getElementById(divId).className = "show";
}
//读取文本框的值
function getTxtValue(id) {
    return document.getElementById(id).value;
}
//向文本框写入值
function setTxtValue(id, value) {
    if (value) {
        document.getElementById(id).value = value;
    } else {
        //document.getElementById(id).value = value;
    }
}

//用户退出
function logout() {
	var userService = new SuperMap.OSP.SystemMaintenanceService.UserService();
    userService.url = ospUrl;
    userService.logout(
        function(result) {
            if (result) {
				document.getElementById("ospMap").href = "unLogin.jsp";
				document.getElementById("geoInfo").href = "unLogin.jsp";
				document.getElementById("appMark").href = "unLogin.jsp";
				document.getElementById("plIntorduce").href = "unLogin.jsp";
				document.getElementById("user_welcome").innerHTML = "";
				document.getElementById("logined").style.display="none";
				document.getElementById("login").style.display="block";
				document.getElementById("userName").value="";
				document.getElementById("password").value="";
            } else {
				
            }
        },
        function(errorResult) {
            
		});
}

//ie6下png图片透明
function alphaPNGBackgrounds() {
	var rslt = navigator.appVersion.match(/MSIE (\d+.\d+)/, '');
	var itsAllGood = (rslt != null && Number(rslt[1]) > 6.0);
	if(!itsAllGood) {
		for(i = 0; i < document.all.length; i++) {
			var bg = document.all[i].currentStyle.backgroundImage;
			if(bg) {
				if (bg.match(/.png/i) != null) {
					var mypng = bg.substring(5, bg.length-2);
					document.all[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + mypng + "', sizingMethod='crop')";
					document.all[i].style.backgroundImage = "url('')";
				}
			}
		}
	}
}