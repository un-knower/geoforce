//引用超图api
var key = "mRNOjJ9ovURL7g4b9ElMtsD9lGyUSwGDy87JN6H1hCM%3D";//192.168.14.31

//服务地址
//var ospServiceUrl="http://192.168.10.251:8080";
var serviceUrl = "http://services.supermapcloud.com";


var poiSearchUrl = serviceUrl;
var script = '<' + 'script type="text/javascript" src="'+serviceUrl+'/iserver/api?key='+key+'&v=1.1"' + '><' + '/script>';
document.writeln(script);

String.prototype.trim = function(){  
	return this.replace(/(^\s*)|(\s*$)/g, "");  
}  


