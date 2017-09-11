//window.SWther.w[city][0].s1   晴
var weather=function(){
	var SWther={};
    this.getWeather = function(divisionCode,data){//根据city的值获取天气信息
		//清空天气容器
		jQuery("#today").html("");
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
	
	function echo(city){
		if(window.SWther.w[city] === undefined){
			return false;
		}
		var today = window.SWther.w[city][0].s1+'&nbsp;&nbsp;'+window.SWther.w[city][0].t1+'&nbsp;~&nbsp;'+window.SWther.w[city][0].t2+'&#8451;';
//		var tomorrow = window.SWther.w[city][1].s1+'&nbsp;&nbsp;'+window.SWther.w[city][1].t1+'&nbsp;~&nbsp;'+window.SWther.w[city][1].t2+'&#8451;';
		jQuery("#current_weather").html('&nbsp;&nbsp;'+today);
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