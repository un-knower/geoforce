Date.prototype.pattern=function(fmt) {     
	var o = {     
		"M+" : this.getMonth()+1, //月份     
		"d+" : this.getDate(), //日     
		"h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时     
		"H+" : this.getHours(), //小时     
		"m+" : this.getMinutes(), //分     
		"s+" : this.getSeconds(), //秒     
		"q+" : Math.floor((this.getMonth()+3)/3), //季度     
		"S" : this.getMilliseconds() //毫秒     
	};     
	var week = {     
		"0" : "\u65e5",     
		"1" : "\u4e00",     
		"2" : "\u4e8c",     
		"3" : "\u4e09",     
		"4" : "\u56db",     
		"5" : "\u4e94",     
		"6" : "\u516d"    
	};     
	if(/(y+)/.test(fmt)){     
	    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));     
	}     
	if(/(E+)/.test(fmt)){
	    fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\u661f\u671f" : "\u5468") : "")+week[this.getDay()+""]);     
	}     
	for(var k in o){     
    if(new RegExp("("+ k +")").test(fmt)){     
        fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));     
        }     
    }     
    return fmt;     
}   
	
/**
 * 删除车辆行
 */
function deleteCar(that) {
	$(that).parent().remove();
}
/**
 * 计算体积
 */
function getVolume(that) {
	var row=$(that).parent();
	var carLength=row.find(".carLength").val()||0;
	var carWidth=row.find(".carWidth").val()||0;
	var carHeight=row.find(".carHeight").val()||0;
	var carVolume=carLength*carWidth*carHeight;
	row.find(".carVolume").html(carVolume.toFixed(2));
}

function updateEntity(id) {
}

function deleteEntity(id) {
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : PROJECT_URL+"/path/delete?ram=" + Math.random() + "&id=" + id,
				success : function(obj) {
					if (obj.flag == "ok") {
						bootbox.alert("操作成功!");
						reload();
					} else {
						bootbox.alert("操作失败!正在运行的任务,无法删除.");
					}
				}
			});
		}
	});
}
function hideAllPopDiv(){
	$("#topToolbarPathDiv_net").hide();
	$("#topToolbarPathDiv_orders").hide();
	$("#topToolbarPathDiv_setting").hide();
	$("#topToolbarPathDiv_cars").hide();
}
//显示结果
var currentTaskObj;//保存当前任务的路线结果信息
function showResult(id,areaId){
	//清除地图覆盖物
	clearMap();
	//隐藏弹出框
	hideAllPopDiv();
	$.ajax({
		type : "POST",
		url : PROJECT_URL+"/path/result?ram=" + Math.random() + "&id=" + id,
		data:{"areaId":areaId},
		success : function(obj) {
			currentTaskObj=obj;//保存当前任务的路线结果信息
			// layer_orders_vector.removeAllFeatures();
			var path = eval("("+ obj.path +")");   
			showPath(path,-1,"");	
			showBranches(obj, 0);
			showOrders(obj, -1);	
			showOrdersDetail();
			$("#pathInfoesContent .paths-detail").html('<p>点击上面的路线按钮查看具体的路线信息</p>');
			showRegion(obj.area);	
			showPathInfoContent(obj);	

			$('.count-paths').html( '(' + eval(currentTaskObj.stopIndexes).length + '条)' );
			$('#roadlist_title').unbind('click').click(function(){
				var path = eval("("+ currentTaskObj.path +")");   
				showPath(path,-1,"");	
				showBranches(currentTaskObj, 0);
				showOrders(currentTaskObj, -1);	
				showOrdersDetail();
				$("#pathInfoesContent .paths-detail").html('<p>点击上面的路线按钮查看具体的路线信息</p>');
				showRegion(currentTaskObj.area);	
				showPathInfoContent(currentTaskObj);
				//显示路线信息框
				$('#pathResultWindow').show();
				$('#pathResultWindow').widget_box('show');//展开
			});
			//显示路线信息框
			$('#pathResultWindow').show();
			$('#pathResultWindow').widget_box('show');//展开
		}
	});
}

/**
 * 显示单个线路信息
 */
function showSinglePath(index,lineColor){	
	//显示中文转向信息
	$("#pathInfoesContent .paths-detail").html( getPathInfoText( pathGuidesArray[index].text) );
	//显示此条线路的订单详情信息
	showOrdersDetail(true, index);

	//显示路径
	clearMapExceptRegionLayer();
	var path=eval("("+ currentTaskObj.path +")"); 
	showPath(path,index,lineColor);
	// 显示订单
	showOrders(currentTaskObj,index);	
	// 显示网点
	//显示对应线路的网点 二维数组中，里面的数组的下标0的值是哪一个网点的下标
	showBranches(currentTaskObj,eval(currentTaskObj.stopIndexes)[index][0]);
}
function showOrdersDetail(isSingle, index) {
	var div = $("#pathInfoesContent .orders-detail").html('');
	var h = '';
	var total_time = 0; data_times = eval(currentTaskObj.pathWeights), len_times = data_times.length;
	for(var j=0; j<len_times; j++) {
		total_time += Number(data_times[j]);
	}
	if(typeof(isSingle) == 'undefined' || !isSingle) {
		var total = Number(currentTaskObj.totalLength);
		total = total > 10 ? total/1000 : total;
		total = total.toFixed(2);
		h += '<div class="subtitle">';		
		h += '	<span class="left">总里程：<strong>'+ total +'KM</strong></span>';
		h += '	<span class="right">总时间：<strong>'+ total_time.toFixed(1) +'分钟</strong></span>';
		h += '</div>';
		h += '<p>点击上面的路线按钮查看具体的订单信息</p>';
		div.html(h);
		return;
	}

	$.ajax({
		type : "GET",
		url : PROJECT_URL+"/orderService/queryByIds?ram=" + Math.random(),
		data:{"orderIds": currentTaskObj.orderIds},
		success : function(e) {
			if(!e || !e.isSuccess || !e.result || e.result.length==0) {
				return;
			}
			var data = eval(currentTaskObj.stopIndexes)[index];
			var len = data.length;
			var total = Number(pathGuidesArray[index].length);
			total = total > 10 ? total/1000 : total;
			total = total.toFixed(2);

			h += '<div class="title" data-index='+ index +'>路线'+ (index+1) +'：共'+ (len-2) +'单</div>';
			h += '<div class="subtitle">';
			h += '	<span class="left">总里程：<strong>'+ total +'KM</strong></span>';
			h += '	<span class="right">总时间：<strong>'+ data_times[index].toFixed(1) +'分钟</strong></span>';
			h += '</div>';
			h += '<div class="list-orders">';
			var data_address = e.result;
			var len = data.length - 1;
			for(var i=1; i<len; i++) {
				var k = data[i];
				h += '<div class="item">';
				h += '	<div class="xuhao">'+ i +'</div>';
				h += '	<div class="content">'+ data_address[k].address +'</div>';
				h += '</div>';
			}
			h += '</div>';

			$('.orders-detail').html(h);
		}
	});

}

function printOrders() {
	var id = $('.print-orders').attr('data-id');
	if(!id) {
		return;
	}
	var index = $('.orders-detail .title').attr('data-index');
	if(!index) {
		return;
	}
	var url = PROJECT_URL + '/resources/point/pathplan/print-orders.html?id=' + id + '&index=' + index;
	window.open(url);
}

/**
 * 解析线路中文转向信息
 */
function getPathInfoText( text ) {	
	var roads = text.split('<br>');
	var len = roads.length-1;

	var h = '';
	for( var i=0; i<len; i++ ) {
		var type = ( i === 0 ? 'start' : ( i === len-1 ? 'end' : 'normal' ) );  
		var r = getRoadFromText( roads[i], type );
		h += '<div class="road">';
		if(r.along === '沿着') {
			h += '	<div class="direction">';
			h += '		<div class="mark"><span class="'+ r.dir_class +' dir-mark"></span></div>';
			h += '		<div class="mark-text"><span class="dir-text">'+ r.direction +'</span></div>';
			h += '	</div>';			
		}
		// else if(r.along === '到达') {
			// glyphicon glyphicon-map-marker
		// }
		h += '	<div class="description">';
		h += '		<span class="road-name">'+ r.along +'<strong>'+ r.name +'</strong>'+ r.go +'</span>';
		h += '		<span class="road-length">'+ r.roadLength +'</span>';
		h += '	</div>';
		h += '</div>';
	}
	return h;
}

/**
 * 解析路线
 */
function getRoadFromText(text, type) {
	var road = {
		direction: "",
		dir_class: "",
		roadLength: 0,
		name: '',
		along: '沿着',
		go: '行驶'
	};

	if(type === 'start') {
		road.name = '起点';
	}
	else if( type === 'end' ) {
		road.name = '终点';
		road.along  = '到达';
		road.go = '';
	}
	else {						
		if( text.match(',') && text.match('沿着') ) {	
			var name = text.split(",");
			road.name = name[0].replace("沿着", "");			
		}
		else if( text.match(',') && text.match('到达') ) {
			road.along  = '到达';
			road.go = '';
			road.name = text.substring( text.indexOf('[') + 1, text.indexOf(']') );
		}	
		else {
			road.name = '匿名路段';
		}
	}

	var num = text;
	num = num.replace(/[^0-9]/ig,"") + "m";
	road.roadLength = num;

	if( text.match("朝南") ) {
		road.direction = "朝南行驶";
		road.dir_class = "arrow-south";
	}
	else if( text.match("朝西") ) {
		road.direction = "朝西行驶";	
		road.dir_class = "arrow-west";	
	}
	else if( text.match("朝东") ) {
		road.direction = "朝东行驶";	
		road.dir_class = "arrow-east";	
	}
	else if( text.match("朝北") ) {
		road.direction = "朝北行驶";	
		road.dir_class = "arrow-north";	
	}
	return road;
}

/**
 * 显示线路
 * path,路线坐标对象
 * pathIndex，显示哪一条线
 * lineColor,路线颜色
 */
function showPath(path,pathIndex,lineColor){
	layer_path.removeAllFeatures();
	var parts = path.result.parts;
	var length_path = parts.length;

	var point2Ds = path.result.point2Ds;

	var index = 0;
	var features = [];
	for(var k=0; k<length_path; k++) {	
		var len = parts[k];
		if(pathIndex==-1 || pathIndex==k){
			var points = [];
			for(var j=index; j < index + len; j++ ){
				var item = point2Ds[j];
				point = new SuperMap.Geometry.Point(item.x, item.y);
				points.push(point);	
			}
			var pathline = new SuperMap.Geometry.LineString(points);
			var style = getLineStyle(k,lineColor);
			
			var pathF = new SuperMap.Feature.Vector(pathline, null, style);
			//给线路一个索引值，方便地图查找。订单点需要边框颜色的时候，需要查找对应的线路的颜色值。
			pathF.attributes={index:k};
			features.push(pathF);

			index += len;
		}else{
			index += len;
			continue;
		}
	}
	layer_path.addFeatures( features );
}

/**
 * 获取线样式
 */
function getLineStyle(index,lineColor){
	var strokeColor = '';
	if(lineColor==""){
		var a = Math.round(Math.random()*0x1000000);
		var c = "000000000".concat(a.toString(16));
		strokeColor = "#"+c.substr(c.length-6,6);
	}else{
		strokeColor=lineColor;
	}
	var style = {
		strokeColor : strokeColor,
		strokeWidth : 3,
		pointerEvents : "visiblePainted",
		fillColor : "#009966",
		fillOpacity : 0.4,
		pointRadius : 6,
		cursor : "pointer"
	}
	return style;
}

/**
 * 显示网点
 * netIndex ,具体显示哪个网点，-1显示所有网点
 */
function showBranches(obj,netIndex) {
	//ID
	var ids= obj.netid;
	var idsArray=[];
	if(ids.indexOf(",")>0){
		idsArray=obj.netid.split(",");
	}else{
		idsArray.push(ids);
	}
	
	var netcoord = obj.netcoord;
	var netid = obj.netid;

	var coords = netcoord.split(",");
	var len_coords = coords.length;

	var xys = [];
	for(var i=0; i<len_coords; i+=2 ) {
		var x = Number( coords[i] );
		var y = Number( coords[i+1] );
		var xy;
		if(x < 180) {
			var p = geoLoc2MeterXY(x, y);
			xy = new SuperMap.LonLat( p.lngX, p.latY );
		}
		else {
			xy = new SuperMap.LonLat(x, y);
		}

		xys.push(xy); 
	}
	
	var len = xys.length;
	var pointVectors=[];

	var centered=false;
	for(var j=0; j<len;j++ ){
		if(-1==netIndex || j==netIndex){
			//地图设置中心点位置
			if(!centered){
				var level = map.getZoom() > 13 ? map.getZoom() : 13;
				map.setCenter(xys[j], level);
				centered=true;
			}
			var pointCoord=xys[j];
			var pointGeometry = new SuperMap.Geometry.Point(pointCoord.lon,pointCoord.lat);
			var	style = {
			    strokeColor:"#FF0000",
			    strokeOpacity:1,
			    strokeWidth:2,
			    pointRadius:10,
			    graphicName:'star',
			    fillColor:'#FFFFFF',
			    cursor: 'pointer'
			};
			var pointVector = new SuperMap.Feature.Vector(pointGeometry,null,style);
			pointVector.attributes={netId:idsArray[j],source:"fromGridWatch"};
			pointVectors.push(pointVector);
		}
		else{
			continue;
		}
	}
	layer_orders_vector.addFeatures(pointVectors);
}

//TODO 显示网点
/**
 * 点击网点，查询其详细信息
 */
function openBranchPopup(feature) {
	clearMapPopups();
    var lonlat = new SuperMap.LonLat(feature.geometry.x,feature.geometry.y);
    var areaName=feature.attributes.areaName?feature.attributes.areaName:"";
    var carNames=feature.attributes.carNames?feature.attributes.carNames:"";
	var param = {
			id: feature.attributes.netId
	};
	
    $.post(
        "../pointService/queryAllPoint?",
        param,
        function(e){
        	if(e && e.result) {   
        		var branch = e.result;
				var ct = branch.netPicPath ? (PROJECT_URL+"/pointService/getImg?path="+branch.netPicPath) : PROJECT_URL+"/resources/zone/assets/map/branch.png";
				var ut = branch.dutyPicPath ? (PROJECT_URL+"/pointService/getImg?path="+branch.dutyPicPath)  : PROJECT_URL+"/resources/zone/assets/map/user.png";			
				var address = branch.address ? branch.address : "无";

				var divH=feature.attributes.source?150:200;
				var divW=feature.attributes.source?250:300;
    			var h =  '<div style="width:'+divW+'px;height:'+divH+'px;">';
    				h += '	<div class="infowindowTitle" style="padding-left: 11px;">';
    				h += '		网点：<span class="infowindowTitleTxt">'+ branch.name +'</span>';
    				h += '	</div>';
    				h += '	<div class="infowindowContent">';
    				h += '		<table class="infowindowContentTable">';
    				h += '			<tr><td><img style="width:155px;height:60px;" src='+"'"+ct+"'"+'></td></tr>';
    				h += '			<tr><td><span>地址: </span><strong>' + address + '</strong></td></tr>';
    				if(!feature.attributes.source){
    					h += '			<tr><td><span>范围: </span>' +"<button type='button' class='btn btn-minier btn-info btn-white' onclick='showHideRegion()'><i class='ace-icon fa fa-eye'>&nbsp;&nbsp;显示区划范围</i></button>" + areaName +'</td></tr>';
        				h += '			<tr><td><span>车辆: </span>' + "<button type='button' class='btn btn-minier btn-info btn-white' onclick='showCarPopDiv()'><i class='ace-icon fa fa-car'>&nbsp;&nbsp;选择配载车辆</i></button>" + 
        				"<label id='selectedCarNames'>"+carNames+"</label>" + '</td></tr>';
    				}
    				h += '		</table>';
    				h += '	</div>';
    				h += '</div>';

			    var popup = new SuperMap.Popup.FramedCloud("popwin",lonlat,null, h,null,true);
			    popup.anchor.offset = new SuperMap.Pixel(0, 0);

			    popup.fixedRelativePosition = true;
			    popup.relativePosition = "tr";
			    map.addPopup(popup);
        	}
        	else {
        		if(e.info && e.info.length < 20) {
        			showPopover(e.info);
        		}
        		else {
        			showPopover("获取起点信息失败，请稍候重试");
        		}
        	}
        },
        'json'
    );
}

/**
 * 显示订单
 * obj.返回的路线信息
 * pathOrderIndex.显示某个路线订单的下标值
 */
function showOrders(obj,pathOrderIndex) {
	//订单ID
	var ids = obj.orderIds;
	var idsArray = [];
	if(ids.indexOf(",")>0){
		idsArray = obj.orderIds.split(",");
	}else{
		idsArray.push(ids);
	}
	
	var coords = obj.ordersCoords.split(",");
	var len_coords = coords.length;

	var xys = [];
	for(var i=0; i<len_coords; i+=2 ) {
		var x = Number( coords[i] );
		var y = Number( coords[i+1] );
		var xy;
		if(x < 180) {
			var p = geoLoc2MeterXY(x, y);
			xy = new SuperMap.LonLat( p.lngX, p.latY );
		}
		else {
			xy = new SuperMap.LonLat(x, y);
		}

		xys.push(xy); 
	}
	var level = map.getZoom() > 13 ? map.getZoom() : 13;
	map.setCenter(xys[0], level);
	
	var pointVectors=[];
	var stopIndexes=eval(obj.stopIndexes);
	for(var j=0; j<stopIndexes.length;j++ ){
		if(pathOrderIndex==-1 || pathOrderIndex==j){
			var stopIndexesInner=stopIndexes[j];
			for(var k=1; k<stopIndexesInner.length-1;k++ ){
				var coordIndex=Number(stopIndexesInner[k]);
				var pointCoord=xys[coordIndex];
				var pointGeometry = new SuperMap.Geometry.Point(pointCoord.lon,pointCoord.lat);
				var style = getPOIStyle( ''+k, layer_path.getFeaturesByAttribute('index',j)[0].style.strokeColor );
				var pointVector = new SuperMap.Feature.Vector(pointGeometry,null, style);
				pointVector.attributes={orderId:idsArray[coordIndex]};
				// pointVector.label="";
				pointVectors.push(pointVector);
			}
		}else{
			continue;
		}
	}
	layer_orders_vector.addFeatures(pointVectors);
}

function getPOIStyle(label,strokeColor) {
	var pointStyle = {
	    strokeColor:strokeColor,
	    strokeOpacity:1,
	    strokeWidth:2,
	    pointRadius:12,
	    graphicName:'circle',
	    fillColor:'#FFFFFF',
	    fillOpacity: 1,
	    label:label,
	    cursor: 'pointer'
	};
	
	return pointStyle;
}

/**
 * 点击订单，查询其详细信息
 */
function openOrderPopup(feature) {
    clearMapPopups();
    var lonlat = new SuperMap.LonLat(feature.geometry.x,feature.geometry.y);
	var param = {
		orderId: feature.attributes.orderId
	};
    $.post(
        "../orderService/queryById?",
        param,
        function(e){
        	if(e) {

        		var branch = e;
				var ct = branch.import_time ? branch.import_time : "无";
				var address = branch.address ? branch.address : "无";
				var province = e.province ? e.province : "无";
				var city = e.city ? e.city : "无";
				var county = e.county ? e.county : "无";

    			var h =  '<div style="width:330px;height:220px;">';
    				h += '	<div class="infowindowTitle" style="padding-left: 11px;">';
    				h += '		订单点';
    				h += '	</div>';
    				h += '	<div class="infowindowContent">';
    				h += '		<table class="infowindowContentTable">';
    				h += '			<tr><td><span>订单地址: </span><strong>' + address + '</strong></td></tr>';
    				h += '			<tr><td><span>订单状态: </span><strong>' + e.status + '</strong></td></tr>';
    				h += '			<tr><td><span>导入时间: </span><strong>' + ct + '</strong></td></tr>';
    				h += '			<tr><td><span>省: </span><strong>'+ province + '</strong></td></tr>';
    				h += '			<tr><td><span>市: </span><strong>' + city + '</strong></td></tr>';
    				h += '			<tr><td><span>区: </span><strong>' + county + '</strong></td></tr>';
    				h += '		</table>';
    				h += '	</div>';
    				h += '</div>';

			    var popup = new SuperMap.Popup.FramedCloud("popwin",lonlat,null, h,null,true);
			    popup.anchor.offset = new SuperMap.Pixel(0, 0);

			    popup.fixedRelativePosition = true;
			    popup.relativePosition = "tr";
			    map.addPopup(popup);
        	}
        	else {
    			showPopover("获取终点信息失败，请稍候重试");
        	}
        },
        'json'
    );
}

/**
 * 创建路线信息窗口内容
 */
//中文转向信息
var pathGuidesArray;
function showPathInfoContent(obj) {
	//清空历史内容
	$("#pathDivContentWraper").html("");
	var len=layer_path.features.length;
	var path0LineColor="", h = '';
	for(var i=0;i<len;i++){
		var lineColor = layer_path.getFeaturesByAttribute('index',i)[0].style.strokeColor ;
		if(i==0){
			path0LineColor=lineColor;
		}
		h += '<div class="path-badge" onClick="showSinglePath('+i+',\''+lineColor+'\''+')" style="background-color:'+lineColor+'">';
		h += '	<div class="badge-title">路线'+ (i+1) +'</div>';
		h += '	<div class="orders-count">'+(eval(obj.stopIndexes)[i].length-2)+'单</div>';
		h += '</div>'
		// $("#pathDivContentWraper").append("<span class='badge ' onClick='showSinglePath("+i+",\""+lineColor+"\""+")' style='cursor: pointer;background-color:"+lineColor+" !important;color: white;'>"+(eval(obj.stopIndexes)[i].length-2)+"单</span>");
	}
	$("#pathDivContentWraper").append(h);
	//保存当前所有路径的转向信息
	pathGuidesArray=eval(obj.pathGuides);
	//显示转向信息，默认显示第一个
	//showSinglePath(0,path0LineColor);
}
/**
 * 显示要素信息，包括网点与订单
 */
function showFeature(feature){
	if(feature.attributes.orderId){
		openOrderPopup(feature);
	}else{
		openBranchPopup(feature);
	}
}
/**
 * 显示隐藏责任区
 */
var region_visible = false;
function showHideRegion(){
	region_visible = region_visible ? false : true;
	layer_region.setVisibility(region_visible);

	var button = $('#btn_showHideRegion');
	if( region_visible ) {
		button.removeClass('showregion').addClass('hideregion').html('隐藏区划');
	}
	else {
		button.removeClass('hideregion').addClass('showregion').html('显示区划');		
	}
}

/**
 * 绘制责任区
 */
function showRegion(e) {
	layer_region.removeAllFeatures();
	if(!e) {
		return;
	}


    var length_parts = e.parts.length;
    if( length_parts == 0) {
        return;
    }
    var point2Ds = e.points;
                    
    var feature = null, geos = [], index=0;

    for(var i=0; i<length_parts; i++) {

        var len = e.parts[i];
        var points = [];
        for(var k=index; k<(index + len); k++){
            var pp = new SuperMap.Geometry.Point(point2Ds[k].x, point2Ds[k].y);
            points.push(pp);
        }
        var linearRing = new SuperMap.Geometry.LinearRing(points);
        geos.push(linearRing);

        index += len;
    }
    var region = new SuperMap.Geometry.Polygon( geos );                
    
    var style = {
        strokeColor : "#0099CC",
        strokeWidth : 2,
        pointerEvents : "visiblePainted",
        fillColor : "#FFFF00",
        fillOpacity : 0.3,
        pointRadius : 2,
        cursor : "default"
    };
    feature = new SuperMap.Feature.Vector(region, null, style);

    layer_region.addFeatures([feature]);
}

/**
 * 清空地图
 */
function clearMap() {
	layer_branches.clearMarkers();
	layer_orders.clearMarkers();
	layer_orders_vector.removeAllFeatures();
	layer_path.removeAllFeatures();
	layer_region.removeAllFeatures();
	clearMeasureFeatures();
	clearMapPopups();
}
/**
 * 清空地图
 */
function clearMapExceptRegionLayer() {
	layer_branches.clearMarkers();
	layer_orders.clearMarkers();
	layer_orders_vector.removeAllFeatures();
	layer_path.removeAllFeatures();
	//layer_region.removeAllFeatures();
	clearMeasureFeatures();
	clearMapPopups();
}

/**
 * 清除地图上的信息窗
 */
function clearMapPopups() {
	var popups = map.popups;
	var length = popups.length;
	if(length == 0) {
		return;
	}
	for(var i=0; i<length; i++) {
		var popup = popups[i];
		map.removePopup(popup);
	}
}

/**
 * 查看结果
 */
function showPathInfoes(id) {
	var data = $("#grid-table").getRowData(id);
	if(data.taskStatusId=="规划结束"){
		showResult(data.id,data.areaId);
		$('a.print-orders').attr('data-id', data.id);
	}else{
		bootbox.alert("正在处理相关数据，请等待!");
		$('a.print-orders').removeAttr('data-id');
	}
}





function reload() {
	$("#grid-table").jqGrid('setGridParam',{ page:1  }).trigger("reloadGrid"); //重新载入 
}
function reloadStep1() {
	$("#grid-table-step1").jqGrid('setGridParam',{ page:1  }).trigger("reloadGrid"); //重新载入 
}
function reloadStep2() {
	$("#grid-table-step2").jqGrid('setGridParam',{ page:1  }).trigger("reloadGrid"); //重新载入 
}
function reloadStep3() {
	$("#grid-table-step3").jqGrid('setGridParam',{ page:1  }).trigger("reloadGrid"); //重新载入 
}

jQuery(function($) {	
	$('[data-toggle="tooltip"]').tooltip();
	bootbox.setDefaults("locale", "zh_CN");

	//初始化时分
	var h = '';
	for(var i=0; i<24; i++) {
		var v = i<10 ? '0' + i : i;
		h += '<option value="'+v+'">'+v+'</option>';
	}
	$('.time-h').html(h);
	for(var i=24; i<60; i++) {
		h += '<option value="'+i+'">'+i+'</option>';
	}
	$('.time-m').html(h);
	
	var grid_selector = "#grid-table";
	var pager_selector = "#grid-pager";
	//车辆
	var grid_selector_step1="#grid-table-step1";
	var pager_selector_step1="#grid-pager-step1";
	//绑定订单
	var grid_selector_step2="#grid-table-step2";
	var pager_selector_step2="#grid-pager-step2";
	//绑定网点
	var grid_selector_step3="#grid-table-step3";
	var pager_selector_step3="#grid-pager-step3";
	// resize to fit page size
	$(window).on('resize.jqGrid', function() {
		setTimeout(function() {
			//$(grid_selector_step1).jqGrid('setGridWidth', 380);
			$(grid_selector).jqGrid('setGridWidth', parent_column.width());
		}, 0);
	});
	// resize on sidebar collapse/expand
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid', function(ev, event_name, collapsed) {
		if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
			// setTimeout is for webkit only to give time for DOM changes and then redraw!!!
			setTimeout(function() {
				$(grid_selector).jqGrid('setGridWidth', parent_column.width());
			}, 0);
		}
	});

	jQuery(grid_selector).jqGrid({
		url : PROJECT_URL+"/path/get/",
		postData : {
			"taskId" : function() {
				var taskId=$("#taskId").val();
				return taskId?taskId:"";
			},
			"areaName" : function() {
				var areaName=$("#areaName").val();
				areaName=areaName?areaName:"";
				areaName=areaName.replace(/%/g, "").replace(/_/g, "");
				$("#areaName").val(areaName);
				return areaName;
			},
			"taskStatusId" : function() {
				var taskStatusId=$('#taskStatusId option:selected').val();
				return taskStatusId?taskStatusId:"0";
			}

		},
		datatype : "json",
		mtype : 'POST',
		height : 150,
		//width : 'auto',
		//autowidth : true,
		colNames : [ 'ID号' ,'任务编号','区域ID','区域名称', '订单数量',  '任务状态','任务耗时(秒)','任务时间','线路模式','线路数量', '分析模式','货车模式','操作' ],
		colModel : [ {
			index : 'id',
			name : 'id',
			width : 40,
			hidden : true,
			sortable : false
		}, {
			index : 'taskName',
			name : 'taskName',
			width : 40,
			sortable : false
		}, {
			index : 'areaId',
			name : 'areaId',
			width : 10,
			hidden : true,
			sortable : false
		}, {
			index : 'areaName',
			name : 'areaName',
			width : 30,
			sortable : false
		}, {
			index : 'orderCount',
			name : 'orderCount',
			width : 30,
			sortable : false
		}, {
			index : 'taskStatusId',
			name : 'taskStatusId',
			width : 30,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				switch(cellvalue)
				{
				case 1:
				  return '初始创建';
				case 2:
					return '已绑定面';
				case 3:
					return '绑定订单';
				case 4:
					return '绑定网点';
				case 5:
					return '绑定车辆';
				case 6:
					return '定时规划';
				case 7:
					return '正在运行';
				case 8:
					return '规划结束';
				default:
				  return '未知状态';
				}
			}
		}, {
			index : 'consumeTime',
			name : 'consumeTime',
			width : 30,
			sortable : false
		}, {
			index : 'planTime',
			name : 'planTime',
			width : 60,
			sortable : false
		}, {
			index : 'typePath',
			name : 'typePath',
			width : 60,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				switch(parseInt(cellvalue))
				{
				case 0:
				  return '常规模式';
				case 1:
					return '放射线路';
				default:
				  return '未知状态';
				}
			}
		},{
			index : 'pathCount',
			name : 'pathCount',
			width : 60,
			sortable : false,
			formatter : function(cellvalue) {
				return cellvalue + '条';
			}
		}, {
			index : 'typeAnalysis',
			name : 'typeAnalysis',
			width : 60,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				switch(parseInt(cellvalue))
				{
				case 0:
				  return '路程最短';
				case 1:
					return '时间最短';
				default:
				  return '未知状态';
				}
			}
		}, {
			index : 'typeTrac',
			name : 'typeTrac',
			width : 60,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				switch(parseInt(cellvalue))
				{
				case 0:
				  return '全通模式';
				case 1:
					return '货车通行';
				default:
				  return '未知状态';
				}
			}
		}, {
			index : '',
			name : '',
			width : 60,
			sortable : false,
			formatter : function(cellvalue, options, rowObject) {
				var html="<button type='button' class='btn btn-minier btn-info btn-white' onclick='showPathInfoes("+"\""+rowObject.id+"\""+")'><i class='ace-icon fa fa-eye'>&nbsp;&nbsp;查看</i></button>";
				html+="&nbsp;<button type='button' class='btn btn-minier btn-info btn-white' onclick='deleteEntity("+"\""+rowObject.id+"\""+")'><i class='ace-icon fa fa-trash-o red'>&nbsp;&nbsp;删除</i></button>";
				return html;
			}
		}],
		hiddengrid : false,
		hidegrid: true,
		viewrecords : true,
		pagerpos:"right",
		recordpos:"left",
		recordtext : "查看第 {0} - {1} 条记录，共 {2}条",
		pgtext : "第 {0} 页,共 {1} 页",
		loadtext : "加载中...",
		emptyrecords : "无记录",
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : pager_selector,
		altRows : true,
		multiselect : false,
		multiboxonly : true,

		loadComplete : function() {
			var table = this;
			setTimeout(function() {
				updatePagerIcons(table);
			}, 0);
		},
		//caption : "规划任务",
		toolbar:[true,"top"]
	});
	
	//添加toolbar 组件
	var toolbarSelector="#t_grid-table";
	$(toolbarSelector).append("<label class='  no-padding-right' for='taskId' style='padding-top: 4px;' > 任务编号： </label><input type='text' id='taskId' placeholder='任务编号' class=' ' maxlength='50'/>");
	$(toolbarSelector).append("<label class='  no-padding-right' for='areaName' style='padding-top: 4px;' > 区域名称： </label><input type='text' id='areaName' placeholder='区域名称' class=' ' maxlength='50'/>");
	$(toolbarSelector).append("<label class='  no-padding-right' for='taskStatusId' style='padding-top: 4px;' > 任务状态： <select id='taskStatusId' name='taskStatusId' >" +
			"<option value='0' selected=selected >全部</option>"+
//			"<option value='1'>初始创建</option>"+
//			"<option value='2'>已绑定面</option>"+
//			"<option value='3'>绑定订单</option>"+
//			"<option value='4'>绑定网点</option>"+
//			"<option value='5'>绑定车辆</option>"+
			"<option value='6'>定时设置</option>"+
			"<option value='7'>正在运行</option>"+
			"<option value='8'>规划结束</option>"+
			"</select>");
	$(toolbarSelector).append("<button type='button' class='btn btn-purple btn-sm ' style='margin:5px;' id='search'>搜索<i class='ace-icon fa fa-search icon-on-right bigger-110'></i></button>");
	$("#search").on("click", reload);
	
	// replace icons with FontAwesome icons like above
	function updatePagerIcons(table) {
		var replacement = {
			'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
			'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
			'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
			'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
		};
		$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
			var icon = $(this);
			var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

			if ($class in replacement)
				icon.attr('class', 'ui-icon ' + replacement[$class]);
		});
	}

	$(document).on('ajaxloadstart', function(e) {
		$(grid_selector).jqGrid('GridUnload');
		$('.ui-jqdialog').remove();
	});

	$(grid_selector).resize(function(e) {
		initcss();
	});
	
	//TODO 车辆
	//车辆列表
	window.cars=[];
	function pushCar(car){//保存已选择车辆
		var flag=true;
		$.each(cars,function(key,val){
			if(val.id==car){
				flag=false;
				return false;
			}
		});
		if(flag){
			var data = $(grid_selector_step1).getRowData(car);
			cars.push(data);
		}
	}
	function popCar(car){//删除取消选择的车辆
		$.each(cars,function(key,val){
			if(val.id==car){
				cars.splice(key,1);
				return false;
			}
		});
	}
	jQuery(grid_selector_step1).jqGrid({
		url : PROJECT_URL+"/pointService/queryCar/",
		postData : {
			"netId" : function() {
				var netId=$("#net_id_step3").val();
				return netId?netId:"";
			},
			"carPlate" : function() {
				var carPlate=$("#carPlate_step1_search").val();
				carPlate=carPlate?carPlate:"";
				carPlate=carPlate.replace(/%/g, "").replace(/_/g, "");
				$("#carPlate_step1_search").val(carPlate);
				return carPlate;
			}
		},
		datatype : "local",
		mtype : 'POST',
		height : 140,
		width : 380,
		//autowidth : true,
		colNames : [ 'ID' ,'车牌','品牌' ],
		colModel : [ {
			index : 'id',
			name : 'id',
			width : 60,
			sortable : false,
			hidden: true
		}, {
			index : 'license',
			name : 'license',
			width : 180,
			sortable : false
		}, {
			index : 'brand',
			name : 'brand',
			width : 150,
			sortable : false
		}],
		shrinkToFit:false,
		autoScroll: false,
		forceFit: true,
		hiddengrid : false,
		hidegrid: true,
		viewrecords : true,
		recordtext : "",
		pgtext : "第 {0} 页,共 {1} 页",
		loadtext : "加载中...",
		emptyrecords : "",
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : pager_selector_step1,
		pagerpos:"left",
		altRows : true,
		multiselect : true,
		multiboxonly : true,
		toolbar:[true,"top"],
		
		loadComplete:function(xhr){
			//默认选择前两个车
			if(!cars.length){
				var idsArray=$(grid_selector_step1).getDataIDs();
				var carNames="";
				$.each(idsArray,function(key,val){
					if(key>1){
						return false;
					}
					var data = $(grid_selector_step1).getRowData(val);
					carNames+=data.license+" ";
					pushCar(val);
				});
				//TODO pop中显示文本
				$("#selectedCarNames").html(carNames);
			}
			
			$.each(cars,function(key,val){
				$(grid_selector_step1).setSelection(val.id,false);
			});
		},
		onSelectAll:function(aRowids,status){
			if(status){
				$.each(aRowids,function(key,val){
					pushCar(val);
				});
			}else{
				$.each(aRowids,function(key,val){
					popCar(val);
				});
			}
		},
		beforeSelectRow: function (rowid, e) {
		    var $myGrid = $(this),
		        i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
		        cm = $myGrid.jqGrid('getGridParam', 'colModel');
		    return (cm[i].name === 'cb');
		},
		onSelectRow: function(id,status){
			if(status){
				pushCar(id);
			}else{
				popCar(id);
			}
		}
	});
	//添加toolbar 组件
	var toolbarSelector1="#t_grid-table-step1";
	/*$(toolbarSelector1).append("<label class='  no-padding-right' for='carPlate_step1_search' style='padding-top: 4px;' > 车牌： </label><input type='text' id='carPlate_step1_search' placeholder='车牌' class=' ' maxlength='50'/>");
	$(toolbarSelector1).append("<button type='button' class='btn btn-purple btn-sm ' style='margin:5px;' id='search-step1'>搜索<i class='ace-icon fa fa-search icon-on-right bigger-110'></i></button>");*/
	$(toolbarSelector1).append("<input type='text' id='carPlate_step1_search' placeholder='输入车牌号查询' maxlength='50'/>");
	$(toolbarSelector1).append("<input type='button' id='search-step1' />");
	$("#search-step1").on("click", reloadStep1);
	
	//TODO 订单
	//绑定订单
	window.orders=[];
	function pushOrder(orderId){//保存已选择订单
		var flag=true;
		$.each(orders,function(key,val){
			if(val.id==orderId){
				flag=false;
				return false;
			}
		});
		if(flag){
			var data = $(grid_selector_step2).getRowData(orderId);
			orders.push(data);
		}
		updateCount();
	}
	function popOrder(orderId){//删除取消选择的订单
		$.each(orders,function(key,val){
			if(val.id==orderId){
				orders.splice(key,1);
				return false;
			}
		});
		updateCount();
	}
	function updateCount(){
		$("#orderCount_step2").html(orders.length);
	}
	jQuery(grid_selector_step2).jqGrid({
		url : PROJECT_URL+"/orderService/queryByArea/",
		postData : {
			"lot" : function() {
				var lot=$("#lot_step2_search").val();
				lot=lot?lot:"";
				lot=lot.replace(/%/g, "").replace(/_/g, "");
				$("#lot_step2_search").val(lot);
				return lot;
			},
			"areaId" : function() {
				var areaid=$("#areaId_step1").val();
				return areaid?areaid:"";
			}
		},
		datatype : "local",
		mtype : 'POST',
		height : 140,
		width : 380,
		//autowidth : true,
		colNames : [ 'ID' ,'订单批号','订单地址','经度','纬度', 'start', 'end' ],
		colModel : [ {
			index : 'id',
			name : 'id',
			width : 60,
			sortable : false,
			hidden : true
		}, {
			index : 'lot',
			name : 'lot',
			width : 120,
			sortable : false
		}, {
			index : 'addr',
			name : 'addr',
			width : 210,
			sortable : false
		}, {
			index : 'lon',
			name : 'lon',
			hidden : true,
			width : 50,
			sortable : false
		}, {
			index : 'lat',
			name : 'lat',
			hidden : true,
			width : 50,
			sortable : false
		},{
			index : 'start',
			name : 'start',
			hidden : true,
			width : 0,
			sortable : false
		},{
			index : 'end',
			name : 'end',
			hidden : true,
			width : 0,
			sortable : false
		}],
		shrinkToFit:false,
		autoScroll: false,
		forceFit: true,
		hiddengrid : false,
		hidegrid: true,
		viewrecords : true,
		recordtext : "",
		pgtext : "第 {0} 页,共 {1} 页",
		loadtext : "加载中...",
		emptyrecords : "",
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : pager_selector_step2,
		pagerpos:"left",
		altRows : true,
		multiselect : true,
		multiboxonly : true,
		//caption : "订单信息",
		toolbar:[true,"top"],
		loadComplete:function(xhr){
			$.each(orders,function(key,val){
				$(grid_selector_step2).setSelection(val.id,false);
			});
		},
		onSelectAll:function(aRowids,status){
			if(status){
				$.each(aRowids,function(key,val){
					pushOrder(val);
				});

				$("#alert_div_step2").removeClass("alert-danger");
				$("#alert_div_step2").addClass("alert-info");
			}else{
				$.each(aRowids,function(key,val){
					popOrder(val);
				});
			}
		},
		beforeSelectRow: function (rowid, e) {
		    var $myGrid = $(this),
		        i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
		        cm = $myGrid.jqGrid('getGridParam', 'colModel');
		    return (cm[i].name === 'cb');
		},
		onSelectRow: function(id,status){
			if(status){
				pushOrder(id);
				$("#alert_div_step2").removeClass("alert-danger");
				$("#alert_div_step2").addClass("alert-info");
			}else{
				popOrder(id);
			}
		}
	});
	//添加toolbar 组件
	var toolbarSelector2="#t_grid-table-step2";
	/*$(toolbarSelector2).append("<label class='  no-padding-right' for='lot_step2_search' style='padding-top: 4px;' > 批号： </label><input type='text' id='lot_step2_search' placeholder='批号' class=' ' maxlength='50'/>");
	$(toolbarSelector2).append("<button type='button' class='btn btn-purple btn-sm ' style='margin:5px;' id='search-step2'>搜索<i class='ace-icon fa fa-search icon-on-right bigger-110'></i></button>");*/
	$(toolbarSelector2).append("<input type='text' id='lot_step2_search' placeholder='输入批号查询' maxlength='50'/>");
	$(toolbarSelector2).append("<input type='button' id='search-step2' />");
	$("#search-step2").on("click", reloadStep2);
	
	//TODO 网点
	//绑定网点
	jQuery(grid_selector_step3).jqGrid({
		url : PROJECT_URL+"/pointService/queryByName/",
		postData : {
			"netName" : function() {
				var netname=$("#netName_step3").val();
				netname=netname?netname:"";
				netname=netname.replace(/%/g, "").replace(/_/g, "");
				$("#netName_step3").val(netname);
				return netname;
			}
		},
		datatype : "json",
		mtype : 'POST',
		height : 140,
		width : 380,
		//autowidth : true,
		colNames : [ 'ID' ,'网点名称','负责人','电话','经度','纬度','网点ID','网点名称' ],
		colModel : [ {
			index : 'id',
			name : 'id',
			width : 60,
			sortable : false,
			hidden : true
		}, {
			index : 'name',
			name : 'name',
			width : 160,
			sortable : false
		}, {
			index : 'dutyName',
			name : 'dutyName',
			width : 100,
			sortable : false
		}, {
			index : 'dutyPhone',
			name : 'dutyPhone',
			width : 100,
			sortable : false
		}, {
			index : 'smx',
			name : 'smx',
			hidden : true,
			width : 50,
			sortable : false
		}, {
			index : 'smy',
			name : 'smy',
			hidden : true,
			width : 50,
			sortable : false
		}, {
			index : 'areaId',
			name : 'areaId',
			hidden : true,
			width : 50,
			sortable : false
		}, {
			index : 'areaName',
			name : 'areaName',
			hidden : true,
			width : 50,
			sortable : false
		}],
		shrinkToFit:false,
		autoScroll: false,
		forceFit: true,
		hiddengrid : false,
		hidegrid: true,
		viewrecords : true,
		recordtext : "",
		pgtext : "第 {0} 页,共 {1} 页",
		loadtext : "加载中...",
		emptyrecords : "",
		rowNum : 10,
		rowList : [ 10, 20, 30 ],
		pager : pager_selector_step3,
		pagerpos:"left",
		altRows : true,
		multiselect : false,
		multiboxonly : true,
		//caption : "网点信息",
		toolbar:[true,"top"],
		loadComplete:function(xhr){
			var netid=$("#net_id_step3").val();
			if(netid){
				$(grid_selector_step3).setSelection(netid,false);
			}
		},
		onSelectRow: function(id,status){
			if(status){
				var data = $(grid_selector_step3).getRowData(id);
				createNetPointWhileSelectRow(data);
			}
		},
		ondblClickRow: function(rowid, iRow, iCol, e){
			var id=$(grid_selector_step3).jqGrid('getGridParam',"selrow");
			if(id==rowid){
				var data = $(grid_selector_step3).getRowData(rowid);
				createNetPointWhileSelectRow(data);
			}
		}
	});
	//添加toolbar 组件
	var toolbarSelector3="#t_grid-table-step3";
	$(toolbarSelector3).append("<input type='text' id='netName_step3' placeholder='输入网点名称查询' maxlength='50'/>");
	// $(toolbarSelector3).append("<button type='button' class='btn btn-purple btn-sm ' style='margin:5px;' id='search-step3'>搜索<i class='ace-icon fa fa-search icon-on-right bigger-110'></i></button>");
	$(toolbarSelector3).append("<input type='button' id='search-step3' />");
	$("#search-step3").on("click", reloadStep3);
		
	//设置基础属性事件
	$("#timeFlagYes").on("click",function(){
		$("#jobRunTime").show();
		$("#topToolbarPathDiv_setting_content").height(306);
	});
	$("#timeFlagNo").on("click",function(){
		$("#jobRunTime").val("").hide();
		$("#topToolbarPathDiv_setting_content").height(286);
	});
		
	$(window).triggerHandler('resize.jqGrid');// trigger window resize to make the grid get the correct size	
});

/**
 * 创建网点，当点击表格行时
 */
function createNetPointWhileSelectRow(data){
	
	if(!data.areaId){
		showPopover("该网点没有绑定区域，请先绑定区域");
		return;
	}
	
	$("#net_id_step3").val(data.id);
	$("#net_name_step3").val(data.name);
	$("#areaId_step1").val(data.areaId);
	$("#areaName_step1").val(data.areaName);
	var p=meterXY2GeoLoc(data.smx,data.smy,3);
	$("#net_coord_step3").val(p.lngX+","+p.latY);
	
	var pointVectors=[];
	var pointStyle = {
		    strokeColor:"#FF0000",
		    strokeOpacity:1,
		    strokeWidth:2,
		    pointRadius:8,
		    graphicName:'star',
		    fillColor:'#FFFFFF',
		    cursor: 'pointer'
	};
	var pointGeometry = new SuperMap.Geometry.Point(data.smx,data.smy);
	var pointVector = new SuperMap.Feature.Vector(pointGeometry,null, pointStyle);
	pointVector.attributes={netId:data.id,areaName:data.areaName};
	pointVectors.push(pointVector);
	layer_orders_vector.addFeatures(pointVectors);
	map.setCenter(new SuperMap.LonLat(data.smx, data.smy),11);
	
	//显示POP信息
	openBranchPopup(pointVector);
	//showPopover("请选择配载车辆");//根据产品经理要求 默认加载车辆 2015-1-9 11:57:29
	//绘制区域
	$.ajax({
		type : "GET",
		url : PROJECT_URL+"/pointService/queryAreaByNetId?ram=" + Math.random(),
		data:{"netId":data.id},
		success : function(e) {
			showRegion(e);
		}
	});
	
	//隐藏弹出框
	$("#topToolbarPathDiv_net").hide();
	$("#topToolbarPathDiv_orders").hide();
	$("#topToolbarPathDiv_setting").hide();
	$("#topToolbarPathDiv_cars").hide();
	
	//将下拉选择按钮文字改为网点名称
	if(data.name){
		$("#selectNetBtnLabel").html(data.name).css({'color': '#000'});
	}
	
	//第一次显示车辆列表时，将数据类型改为json
	if($("#grid-table-step1").jqGrid('getGridParam','datatype')=='local'){
		$("#grid-table-step1").jqGrid('setGridParam',{datatype:'json', page:1 }).trigger('reloadGrid');
	}
}

function closeSettingDiv() {	
	var start_time = $('.batch-time-start .time-h').val() + $('.batch-time-start .time-m').val();
	var end_time = $('.batch-time-end .time-h').val() + $('.batch-time-end .time-m').val();
	if( Number(end_time) <= Number(start_time) ) {
		showPopover("车辆返回时间须晚于车辆出发时间");
		return;	
	}
	var txt_waste_time = $('#txt_waste_time').val();
	if(txt_waste_time == '') {
		showPopover("请输入非行驶消耗时间");
		return;
	}
	if( isNaN( Number(txt_waste_time) ) ) {
		showPopover("非行驶消耗时间须为数字");
		return;
	}
	var txt_car_load = $('#txt_car_load').val();
	if(txt_car_load == '') {
		showPopover("请输入线路最大承载订单数");
		return;
	}
	if( isNaN( Number(txt_car_load) ) ) {
		showPopover("线路最大承载订单数须为数字");
		return;
	}
	$('#topToolbarPathDiv_setting').hide();
}

/**
 * 提交路线规划
 */
function submitPathPlanJob(){
	$("#goPathPlanBtn").attr("disabled","disabled");
	
	var flag=true;
	var dt = new Date();
	var timeFlagValue=$("input[name='timeFlag']:checked").val();
	if(timeFlagValue==1){
		var jobRunTimeValue=$("#jobRunTime").val();
		if(jobRunTimeValue==""){
			$("#jobRunTime").val(dt.pattern("yyyy-MM-dd HH:mm:ss"));
		}
	}else{
		$("#jobRunTime").val(dt.pattern("yyyy-MM-dd HH:mm:ss"));
	}
	// var jobstarttimeValue=$("#jobstarttime").val()?$("#jobstarttime").val():dt.pattern("yyyy-MM-dd HH:mm:ss");
	// var jobendtimeValue=$("#jobendtime").val()?$("#jobendtime").val():dt.pattern("yyyy-MM-dd"+" 23:59:59");
	
	if(!$("#net_id_step3").val()){
		showPopover("请确定是否选择了起点");
		flag = false;
	}
	if(!orders.length){
		showPopover("请确定是否选择了终点");
		flag = false;
	}
	var start_time = $('.batch-time-start .time-h').val() + $('.batch-time-start .time-m').val();
	var end_time = $('.batch-time-end .time-h').val() + $('.batch-time-end .time-m').val();
	if( Number(end_time) <= Number(start_time) ) {
		showPopover("车辆返回时间必须晚于车辆出发时间");
		flag = false;		
	}	
	var txt_waste_time = $('#txt_waste_time').val();
	if(txt_waste_time == '') {
		showPopover("请输入非行驶消耗时间");
		flag = false;	
	}
	if( isNaN( Number(txt_waste_time) ) ) {
		showPopover("非行驶消耗时间须为数字");
		flag = false;	
	}
	var txt_car_load = $('#txt_car_load').val();
	if(txt_car_load == '') {
		showPopover("请输入线路最大承载订单数");
		flag = false;	
	}
	if( isNaN( Number(txt_car_load) ) ) {
		showPopover("线路最大承载订单数须为数字");
		flag = false;	
	}
	if(flag){
		var param = {};
		param.taskName=$("#taskName").val()?$("#taskName").val():dt.pattern("yyyyMMddHHmmss");

		//STEP1
		param.areaName = $("#areaName_step1").val();
		param.areaId = $("#areaId_step1").val();
		
		//STEP2
		var ordersIdsArray=[];
		var ordersCoordsArray=[], ordersTimesArray = [];
		$.each(orders, function(key,val){
			ordersIdsArray.push(val.id);
			ordersCoordsArray.push(val.lon);
			ordersCoordsArray.push(val.lat);
			ordersTimesArray.push(val.start);
			ordersTimesArray.push(val.end);
		});
		param.ordersIds = ordersIdsArray.join(",");
		param.ordersCoords = ordersCoordsArray.join(",");
		param.orderTimes = ordersTimesArray.join(",");
		param.ordersCount = ordersIdsArray.length;
		$("#selectOrderBtnLabel").html(orders[0].lot).css({'color': '#000'});
		
		//STEP3
		param.netid  =$("#net_id_step3").val();
		param.netcoord = $("#net_coord_step3").val();
		
		//STEP4
		/*var carArray=[];
		$.each(cars,function(key,val){
			carArray.push(4.2+","+2.1+","+2.5+","+22.05+","+2000+","+val.id+","+val.license);
		});*/
		
		//STEP5
		// var jobRunTime=$("#jobRunTime").val();
		param.groupType = $("input[name='groupType']:checked").val();
		param.weightNameType = $("input[name='weightNameType']:checked").val();
		param.directionType = $("input[name='directionType']:checked").val();
		param.batchTimeStart = $('.batch-time-start .time-h').val() + ":" + $('.batch-time-start .time-m').val();
		param.batchTimeEnd = $('.batch-time-end .time-h').val() + ":" + $('.batch-time-end .time-m').val();
		param.carLoad = $('#txt_car_load').val();
		param.fixConsumeMin = $('#txt_waste_time').val();
		param.type = 1;
		
		$.ajax({
			type : "POST",
			async : false,
			url : PROJECT_URL+"/path/save?ram=" + Math.random(),
			// data:{"taskName":taskName,"directionType":directionType,"weightNameType":weightNameType,"groupType":groupType,"carsProperties":carArray.join(";"),"netcoord":netcoord,"ordersCoords":ordersCoords,"areaName":areaName,"areaId":areaId,"ordersIds":ordersIds,"ordersCount":ordersCount,"netid":netid,"jobRunTime":jobRunTime,"jobstarttime":jobstarttimeValue,"jobendtime":jobendtimeValue},
			data: param,
			success : function(obj) {
				if (obj.flag == "ok") {
					bootbox.alert("操作成功!请隔段时间搜索下方列表,查看结果.");
					reload();
					cars=[];
					orders=[];
					reloadStep1();
					reloadStep2();
					$("#selectNetBtnLabel").html("请选择起点");
					$("#selectOrderBtnLabel").html("请选择终点");
					//清除地图覆盖物
					clearMap();
					//隐藏弹出框
					hideAllPopDiv();
					//显示结果列表
					if($('#bottomToolbarPathDiv_path .widget-body').css("display")=='none'){
						$('#bottomToolbarPathDiv_path').widget_box('show');
					}
				} else {
					cars=[];
					orders=[];
					reloadStep1();
					reloadStep2();
					$("#selectNetBtnLabel").html("起点");
					$("#selectOrderBtnLabel").html("终点");
					//清除地图覆盖物
					clearMap();
					//隐藏弹出框
					hideAllPopDiv();
					if(obj.info){
						bootbox.alert(obj.info);
					}else{
						bootbox.alert("操作失败!");
					}
					
				}
			}
		});
		
	}else{
		$("#goPathPlanBtn").removeAttr("disabled");
		return false;
	}
	$("#goPathPlanBtn").removeAttr("disabled");
}


/**
 * 初始化IP定位及是否显示用户引导
 */
$(function(){	
	$('.pathDivTitle li').click(function(){
		var me = $(this);
		if(me.hasClass('normal')) {
			$('.pathDivTitle li.action').removeClass('action').addClass('normal');
			me.removeClass('normal').addClass('action');
			$('#pathInfoesContent .content').hide();
			$(me.attr('data-target')).show();
		}
	});
	$('.collapse-table').click(function(){
		var me = $(this);
		var table = $('.data-list');
		if(table.hasClass('collapsed')) {
			table.removeClass('collapsed');
			me.find('i').removeClass('fa-chevron-up').addClass('fa-chevron-down');
		}
		else {
			table.addClass('collapsed');
			me.find('i').removeClass('fa-chevron-down').addClass('fa-chevron-up');
		}
	});
	var param = Map.getUrlArgs();
	if( param && param.isLogined == 'false' ) {
		Map.showUserGuide();
	}
	$.getJSON(
		"http://api.map.baidu.com/location/ip?ak=214c94f370aa31822201489ae44e4018&callback=?",
		function(json) {			
			if( json.status === 0 && json.content && json.content.address_detail && json.content.address_detail.city) {
				var currentCity = json.content.address_detail.city;
				var province = json.content.address_detail.province;

				for( i=citys.length; i--; ) {
					var c = citys[i];
					if( currentCity.match( c.city ) ) {
						$('#selectCityDiv div').html( c.city );
						map.setCenter( new SuperMap.LonLat(Number(c.x), Number(c.y) ), 11 );
					}
				}
			}		
			Map.cityChange();
			map.events.on({'moveend':Map.cityChange});
		}
	);
});
var Map = {
	Provinces: null,
	City: null,
	oldCityName: "", //记录上一个城市名，以便在城市名改变之后更新天气
	//地图和城市面板联动
	tempCenter: null,
	tempLevel: 11
};
/**
 * 地图联动
 */
Map.cityChange = function() {
	var center = map.getCenter();
	$.ajax({ 
		url: PROJECT_URL + "/orderService/searchForCounty?&smx=" + center.lon + "&smy=" + center.lat + "&callbacks=?",
		dataType: "jsonp",
		success: function(json){
			var level = map.getZoom();		
			var div = $('#selectCityDiv div');
			if(json.isSuccess && json.result) {
				if(level >= 8){
					if(json.result.CITY2){
						div.html( json.result.CITY2 );				
					}
					else if( json.result.PROVINCE){
						div.html( json.result.PROVINCE );			
					}
					else{
						div.html( "全国" );
					}
				}
				else if(level < 8 && json.result.PROVINCE){
					div.html( json.result.PROVINCE );
				}
				else {
					div.html( "全国" );
				}
			}
			else {
				div.html( "全国" );
			}
		}
	});
}

/**
 * 获得浏览器地址中的参数
 */
Map.getUrlArgs = function(){
    var url = location.href;
	var theRequest = {};
	
	var index = url.indexOf("?");
	if ( index != -1) {
		var str = url.substr( index + 1 );
		var strs = [ str ];
		if(str.match("&")) {
			strs = str.split("&");
		}
		
		for ( var i = 0; i < strs.length; i++) {
			theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
		}
	} 
	else {
		theRequest = null;
	}
	return theRequest;    
}

/**
 * 显示用户引导
 */
Map.showUserGuide = function() {
	$('.user-guide').show();
	$('.user-guide .step1 .next-step').click(function(){
		$('.user-guide .step1').hide();
		$('.user-guide .step2').show();
	});
	$('.user-guide .step2 .next-step').click(function(){
		$('.user-guide .step2').hide();
		$('.user-guide .step3').show();
	});
	$('.user-guide .step3 .next-step').click(function(){
		$('.user-guide .step3').hide();
		$('.user-guide .step4').show();
	});
	$('.user-guide .step4 .next-step').click(function(){
		$('.user-guide .step4').hide();
		$('.user-guide .step5').show();
	});
	$('.user-guide .step5 .start').click(function(){
		$('.user-guide .step5').hide();
		$('.user-guide').hide();
	});
}





