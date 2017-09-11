
var weather=function(){
	var SWther={};
    this.getWeather = function(divisionCode,data){//根据city的值获取天气信息
		//清空天气容器
		jQuery("#today").html("");
		jQuery("#tomorrow").html("");
		jQuery("#message").html("");
		var city = "";
		if(divisionCode == null || data == null){
			var cityname = jQuery("#selectCityDiv").text();
			city = cityname.replace(/市/, "");
			loadJS("http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=2&city="+city+"&dfc=3",
				function(){
					echo(city);
				},
				"GBK"
			);
		}
		// divisionCode != null
		else{
			city = data.replace(/市/, "");
			if(divisionCode.substr(2,6) == "0000"){
				//直辖市与特别行政区天气联动
				if(city =="北京" || city =="上海" || city =="重庆" || city =="天津" || city == "香港" || city == "澳门"){
					loadJS("http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=2&city="+city+"&dfc=3",
						function(){
							echo(city);
						},
						"GBK"
					);
				}
				//省级天气联动,获取其省会
				else{
					if(divisionCode == "710000"){
							jQuery("#weather").html("台湾省暂无天气信息！");
					}
					else{
						var cityName = "";
						districtSearch = new SuperMap.OSP.Service.DistrictSearch();
						districtSearch.search.url = "http://services.supermapcloud.com";
						var disInfos = districtSearch.getChildDistricts(divisionCode, function(records){
							if(records){
								for(var i = 0; i < records.length; i++){
									var tempCity = records[i];
									if(tempCity.code.substr(2,6) == "0100"){
										cityName = tempCity.name.replace(/市/, "");
										loadJS("http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=2&city="+cityName+"&dfc=3",
											function(){
												echo(cityName);
											},
											"GBK"
										);
									}
									break;
								}
							}
						});
					}
				}
			}
			//普通城市天气联动
			else{
				var city = data.replace(/市/, "");
				loadJS("http://php.weather.sina.com.cn/iframe/index/w_cl.php?code=js&day=2&city="+city+"&dfc=3",
					function(){
						echo(city);
					},
					"GBK"
				);
			}
		}
		
	}

	function weatherPic(weather){
		var picture = null;
		if(weather == '晴' || weather == '扬沙转晴' || weather == '浮尘转晴' || weather == '沙尘暴转晴'){
			picture = "images/weather/sun.jpg";
		}
		else if(weather.match("云转晴") || weather.match("阴转晴") || weather.match("多云转晴") || weather=='晴转多云'){
			picture = "images/weather/suntocloud.jpg";
		}
		else if(weather == '阴' || weather == '多云' || weather == '沙尘暴转多云' || weather.match("沙转多云") || weather == '阴转多云' || weather.match("转阴") || weather == '浮尘转多云'){
			picture = "images/weather/cloud.jpg";
		}
		else if(weather == '小雨' || weather.match("转小雨")){
			picture = "images/weather/srain.jpg";
		}
		else if(weather == '小到中雨' || weather.match("转小到中雨")){
			picture = "images/weather/moderateRain.jpg";
		}
		else if(weather == '阵雨' || weather.match("转阵雨") || weather.match("雨转多云")){
			picture = "images/weather/shower.jpg";
		}
		else if(weather == '中雨' || weather.match("转中雨")){
			picture = "images/weather/moderateRain.jpg";
		}
		else if(weather == '中到大雨' || weather.match("转中到大雨")){
			picture = "images/weather/thunderstorm.jpg"
		}
		else if(weather == '大雨' || weather.match("转大雨")){
			picture = "images/weather/moderateRain.jpg"
		}
		else if(weather == '大到暴雨' || weather.match("转大到暴雨")){
			picture = "images/weather/rainstorm.jpg"
		}
		else if(weather == '暴雨' || weather.match("转暴雨")){
			picture = "images/weather/rainstorm.jpg"
		}
		else if(weather == '雷阵雨' || weather.match("转雷阵雨")){
			picture = "images/weather/thunderstorm.jpg"
		}
		else if(weather == '雨夹雪' || weather.match("转雨夹雪")){
			picture = "images/weather/srain.jpg";
		}
		else if(weather == '小雪' || weather == '阴转小雪'  || weather.match("雪转多云") || weather.match("云转小雪") || weather.match("雨转小雪") || weather.match("雪转小雪")){
			picture = "images/weather/ssnow.jpg";
		}
		else if(weather == '晴转小雪' || weather.match("雪转晴")){
			picture = "images/weather/clearSnow.gif";
		}
		else if(weather == '小到中雪' || weather.match("转小到中雪")){
			picture = "images/weather/heavySnow.jpg"
		}
		else if(weather == '中雪' || weather.match("转中雪")){
			picture = "images/weather/heavySnow.jpg"
		}
		else if(weather == '阵雪' || weather.match("转阵雪")){
			picture = "images/weather/heavySnow.jpg"
		}
		else if(weather == '中到大雪' || weather.match("转中到大雪")){
			picture = "images/weather/heavySnow.jpg"
		}
		else if(weather == '大雪' || weather.match("转大雪")){
			picture = "images/weather/heavySnow.jpg"
		}
		else if(weather == '暴雪' || weather.match("转暴雪")){
			picture = "images/weather/heavySnow.jpg"
		}
		else if(weather == "台风" || weather == "暴风" || weather == "大风"){
			picture = "images/weather/feng.jpg"
		}
		else if(weather == '浮尘' || weather.match("转浮尘")){
			picture = "images/weather/dust.jpg"
		}
		else if(weather == '扬沙' || weather.match("转扬沙")){
			picture = "images/weather/yangsha.jpg";
		}
		else if(weather == '沙尘暴' || weather.match("转沙尘暴")){
			picture = "images/weather/sandstorm.jpg";
		}
		else if(weather == '多云有雾' || weather.match("转多云有雾")){
			picture = "images/weather/cloudyWithFog.jpg";
		}
		else if(weather == '雾' || weather.match("转雾") || weather.match("霾")){
			picture = "images/weather/cloudyWithFog.jpg";
		}
		return picture;
	}
	
	var tempToday = "";  //今天天气的内容
	var tempTomorrow = "";  //明天天气内容
	
	function echo(city){
		if(window.SWther.w[city] === undefined){
			return false;
		}
		var todayData = "";
		var tomorrowData = "";
		
		var todayTemperature = window.SWther.w[city][0].t1+'&#8451;&nbsp;-&nbsp;'+window.SWther.w[city][0].t2+'&#8451;';;
		var tomorrowTemperature = window.SWther.w[city][1].t1+'&#8451;&nbsp;-&nbsp;'+window.SWther.w[city][1].t2+'&#8451;';
		
		var message = '以上信息由<a target="_blank" style="color:#000; font-size:12px" href="http://www.weather.com.cn/weather/101010100.shtml">中国气象局</a>提供';
		todayData += '<strong>今天&nbsp;' + todayTemperature + '</strong>';
		tomorrowData += '<strong>明天&nbsp;' + tomorrowTemperature + '</strong>';
		
		todayData += ' <div style="padding-top:4px;"><img name="" src="' + weatherPic(window.SWther.w[city][0].s1) + '" width="48" height="48" alt="" style="float:left; margin-right:6px;"><span style="margin-bottom:5px;">' + window.SWther.w[city][0].s1 + '</span><br><span style="margin-bottom:5px;">风力'+ window.SWther.w[city][0].p1 +'级</span><br><span style="margin-bottom:5px;">'+ window.SWther.w[city][0].d1 +'</span></div>';
		
		tomorrowData += ' <div style="padding-top:4px;"><img name="" src="' + weatherPic(window.SWther.w[city][1].s1) + '" width="48" height="48" alt="" style="float:left; margin-right:6px;"><span style="margin-bottom:5px;">' + window.SWther.w[city][1].s1 + '</span><br><span style="margin-bottom:5px;">风力'+ window.SWther.w[city][1].p1 +'级</span><br><span style="margin-bottom:5px;">'+ window.SWther.w[city][1].d1 +'</span></div>';
		
		tempToday = todayData;
		tempTomorrow = tomorrowData;
		jQuery("#today").html(tempToday);
		jQuery("#tomorrow").html(tempTomorrow);
		if(tempToday != "" || tempTomorrow != ""){
			jQuery("#message").html(message);
		}
	}
}
//js天气请求加载
function loadJS(url,callback,charset){
	var script = document.createElement('script');
	script.onload = script.onreadystatechange = function ()
	{
		if (script && script.readyState && /^(?!(?:loaded|complete)$)/.test(script.readyState)) return;
		script.onload = script.onreadystatechange = null;
		script.src = '';
		script.parentNode.removeChild(script);
		script = null;
		if(callback)callback();
	};
	script.charset=charset || document.charset || document.characterSet;
	script.src = url;
	try {
		document.getElementsByTagName("head")[0].appendChild(script);
	} 
	catch (e) {
	}
}