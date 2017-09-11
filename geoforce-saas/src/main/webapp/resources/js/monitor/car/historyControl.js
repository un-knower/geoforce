/**
 * 历史轨迹播放器操作类
 */
/**历史轨迹参数*/
var historyLayerIndex = null;//历史轨迹控制面板弹出框id
var startImg = imagesPath+"/mapcloud/stPoint.png";//历史轨迹开始点的图标
var endImg = imagesPath+"/mapcloud/edPoint.png";//历史轨迹结束点的图标
var playSpeed = 1;//播放步长
var gpsIndex = 0;//当前点
var hisHasPoints;//地图上已存在的point点集合
var gpsCount = 0;//轨迹播放总记录
var historyTimer = null;//定时播放器
var historyData = null;//历史轨迹数据
var startMile = null;//开始里程
/**历史轨迹参数*/
$(document).ready(function(){

	//历史轨迹进度条监听事件
	$("#historySilder").on("sliderchange",function(e,result){//拖动滑块
		historySliderStart();
	});
	$("#historySilder").on("slidercomplete",function(e,result){//滑块拖动完成
		
		historySliderEnd(result.value);
	});
});

/**
 * 历史轨迹播放开始/暂停
 * @return
 */
function historyStart(){
	if(historyData){
//		var obj = $("#playStart").find("img");
//		var imgSrc = obj.attr("src");
		if($("#playStart").attr("class").indexOf('glyphicon-play') != -1){
			if(gpsIndex == gpsCount){//播放到终点 重新播放
				gpsIndex = 0;
			}
			if(historyTimer){
				clearTimeout(historyTimer);
				historyTimer = null;
			}
			historyTimer=setTimeout("history_play()",1000);
			$("#playStart").removeClass('glyphicon-play').addClass('glyphicon-pause');
		}else{
			clearTimeout(historyTimer);
			historyTimer = null;
			$("#playStart").removeClass('glyphicon-pause').addClass('glyphicon-play');
		}
	}
}
/**
 * 历史轨迹播放
 * @return
 */
	
function history_play(){
	var filer = $("#playFiler").attr("checked");
	var list = historyData.list;
	if(filer){//如果忽略静止点选中，则变为加速模式 否则为普通模式
		var speed;
		var endFlag = true;//防止遍历到最后也没有大过当前里程的情况
		var listLen = list.length;
		for(var i=gpsIndex;i<listLen;i++){
			speed = list[i].speed;
			speed = (speed == null || speed == "")?0:speed;
			if(speed > 1){//速度大于1判断车辆移动
				gpsIndex = i;
				endFlag = false;
				break;
			}
		}
		gpsIndex = gpsIndex + playSpeed;
		if(endFlag){
			gpsIndex = gpsCount;
		}
	}else{
		gpsIndex = gpsIndex + playSpeed;
	}
	if(gpsIndex >= gpsCount){
		gpsIndex = gpsCount;
//		cleanMapList();//in mapList.js 清空列表
		historyStart();
	}else{
		if(historyTimer){
			clearTimeout(historyTimer);
			historyTimer = null;
		}
		historyTimer = setTimeout("history_play()",1000);
	}
	his_currentLine();//in mapSupport.js 播放过程中绘制轨迹线
	his_sliderMove(gpsIndex);
	
	var carObj = new Object();
	carObj.carId = historyData.carId;
	carObj.license = historyData.license;
	
	addGpsHistoryList(carObj,list[gpsIndex]);//in mapList.js在列表下方显示
}
/**
 * 关闭历史轨迹
 * @return
 */
function historyClosed(){
	controllClosed();
	clearLayerFeatures();
	cleanMapList();//清除地图下方列表数据 in main.js
}
/**
 * 关闭播放器控制面板
 * @return
 */
function controllClosed(){
	clearTimeout(historyTimer);
	historyTimer = null;
	if(historyLayerIndex)
		layer.close(historyLayerIndex);
//	$("#playStart").find("img").attr("src",ctx+"/images/suspend.png");
}
/**
 * 历史轨迹播放加速*2
 * @return
 */
function historyFast(){
	if(historyData){
		var tmp = playSpeed;
		tmp = tmp * 2;
		if(tmp > 16){
			return;
		}
		playSpeed = tmp;
		var playFast = $("#playFast");
		var playLast = $("#playLast");
		playFast.attr("title","x"+tmp);
		if(tmp == 16){
			playFast.attr("disabled","disabled");
		}
		playLast.attr("title","x"+tmp);
		playLast.removeAttr("disabled");
	}
}
/**
 * 历史轨迹播放减速*2
 * @return
 */
function historyLast(){
	if(historyData){
		var tmp = playSpeed;
		tmp = tmp / 2;
		if(tmp < 1){
			return;
		}
		playSpeed = tmp;
		var playFast = $("#playFast");
		var playLast = $("#playLast");
		playFast.attr("title","x"+tmp);
		playFast.removeAttr("disabled");
		
		playLast.attr("title","x"+tmp);
		if(tmp == 1){
			playLast.attr("disabled","disabled");
		}
	}
}
/**
 * 播放进度条拖动开始，如果是播放状态则暂停
 * @return
 */
function historySliderStart(){
//	var obj = $("#playStart").find("img");
//	var imgSrc = obj.attr("src");
	if($("#playStart").attr("class").indexOf('glyphicon-pause') != -1){//当前是播放状态
		historyStart();
	}
}
/**
 * 进度条拖动完成事件
 * @param curValue
 * @return
 */
function historySliderEnd(curValue){
	console.log(curValue);
	if(historyData){
		if(curValue > gpsCount){
			curValue = gpsCount;
		}
		gpsIndex = curValue;
		historyStart();
	}else{
		historyClosed();
	}
}
/**
 * 历史轨迹播放器进度条控制
 * @param curVal
 * @return
 */
function his_sliderMove(curVal){
	var max = 100;
	if(gpsCount){
		max = gpsCount;
	}
	
	$('#historyProgress').attr({'aria-valuenow':curVal});
	var progress = Math.round(curVal/max*100);
	$('#historyProgress').css({width:progress+'%'});
	$('#historyProgress').html(progress+'%');
	if(progress > 94){
		progress = 94
	}
	$('.slider-bar').css({left:progress+'%'});
}
/**
 * 重置历史轨迹控制面板各个控件到初始化状态
 * @return
 */
function his_resetControll(){
	//清空当前时间和当前里程
//	$("#historyTime").val("");
//	$("#historyMile").val("");
	//重新计算播放进度条
	$('#historyProgress').attr({'aria-valuenow':0,'aria-valuemin':0,'aria-valuemax':gpsCount});
	$('#historyProgress').css({width:'0%'});
	$('#historyProgress').html("0%");
	
	$("#playLast").attr({disabled:'disabled',title:'x1'});
	$("#playStart").removeClass('glyphicon-pause').addClass('glyphicon-play');
	$("#playFast").removeAttr("disabled");
	$("#playFast").attr("title","x1");
	
	$('.slider-bar').css({left:'0%'});
}
/**
 * 显示/隐藏轨迹播放器
 * @return
 */
function his_controll(){
//	if(historyLayerIndex){//当控制面板为关闭状态则打开
//		layer.close(historyLayerIndex);
//		historyLayerIndex = null;
//	}else{
//		popHistoryControll();
//	}
	popHistoryControll();
}
/**
 * 生成弹出框
 * @return
 */
function popHistoryControll(){
	if(historyData){
		$("#historyTitleFont").html(historyData.license);
	}
	historyLayerIndex = $.layer({
		type:1,
		title: false,
		move:'#historyTitle',
		offset: ['102px','500px'],
		area:['520','auto'],
		shade : [0],
		bgcolor:"",
		closeBtn: [1,true],
		shade:[0],
		page:{dom:'#historyControll'},
		success: function(){
			$(".xubox_page").css({width:'100%'});
		},
		close: function(index){
			historyClosed();
		}
	});
}