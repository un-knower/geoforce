/**车辆监控 车辆报警提醒*/
var stopAlarmRemind = true;//报警提示 开始/关闭标识
var alarmRemindTimer = null;//报警提醒定时
$(document).ready(function(){
	alarmRemind();
});
/**
 * 报警提醒停止
 * @return
 */
function alarmRemindStop(){
	stopAlarmRemind = true;
	clearTimeout(alarmRemindTimer);
	alarmRemindTimer = null;
}
/**
 * 报警提醒
 */
function alarmRemind(){
	alarmRemindStop();
	stopAlarmRemind = false;
	var timer = 60000;//定时轮询时间
//	setTimeout(function(){ajaxAlarmRemind(timer);},timer);
	ajaxAlarmRemind(timer);
}
/**
 * 报警提醒长轮询
 * @return
 */
function ajaxAlarmRemind(timer){
	if(stopAlarmRemind){
		return;
	}
	
	$.ajax({
		type : "POST",
		async : true,
		url : ctx+"/common/alarmRemind",
		dataType : "json",
		success : function(msg) {
			if(msg && msg.status == 1){
				remindContentShow(msg.info);				
			}
			alarmRemindTimer = setTimeout(function(){ajaxAlarmRemind(timer);},timer);
		},
		error : function() {
			alarmRemindTimer = setTimeout(function(){ajaxAlarmRemind(timer);},timer);
        }
	});
}
/**
 * 信息提醒页面显示
 * @param alarmMsg
 * @return
 */
function remindContentShow(infoMsg){
	if(infoMsg == null || infoMsg == ""){
		return;
	}
	//报警信息显示
	var alarmInfo = '';
	//报警信息
	var alarmMsg = infoMsg;
	
	if(alarmMsg){
		var alarmBean = alarmMsg;
		var license = alarmBean.license|| '';//车牌号
		var totalNum = alarmBean.count;//未处理报警总数
		var alarmTime = alarmBean.alarmTime || '';//报警时间
		var alarmType = alarmBean.alarmType || '';//报警类型
		var addr = alarmBean.addr || '';
		var temCode = alarmBean.temCode || '';
		if(!license){
			license = temCode;
		}
		if(license && alarmType){//表示未查出结果	
			var showLicense = license;
			if(license.length > 7){
				showLicense = license.substring(0, 7)+"..";
			}
			var showAlarmType = alarmType;
			if(alarmType.length > 5){
				showAlarmType = alarmType.substring(0, 5)+"..";
			}
			var showAddr = addr;
			if(addr.length > 15){
				showAddr = addr.substring(0, 15)+"..";
			}
			//最新报警提醒
			alarmInfo = '<div><span title="'+alarmType+'">['+showAlarmType+']</span> '
					+'<span style="color:green;" title="'+license+'">'+showLicense+'</span>&nbsp;&nbsp;'
					+'<span style="color:red;">'+alarmTime+'</span></div>';
			if(addr){
				alarmInfo += '<div><span style="color:red;" title="'+addr+'">'+showAddr+'</span>&nbsp;&nbsp;';
			}
			//发生报警
//			alarmInfo += topLang.occurAlarm+'</div><div><a href="javascript:void(0);" onclick="inAlarmCenter()">'+topLang.moreDetail+'</a></div>';
		}
	}
	if(alarmInfo){
		var html = '';
		var msgHt = 70;//弹出框高度
		if(alarmInfo){
			html += alarmInfo;
			msgHt += 45;
		}
		//remindInfo:'信息提醒'
		$.layer({
			type:1,
			title: license,
			area:['250','auto'],
			shade : [0.1 , '#000' , true],
		    shift: 'right-bottom',
			closeBtn: [1,true],
			shade:[0],
			page:{html:'<p>'+license+'&nbsp;&nbsp;发生'+alarmType+'报警</p>'
						+'<p>时间：'+alarmTime+'</p>'
						+'<p>地点：'+addr+'</p>'},
			time:6,
			close: function(index){
			}
		});
//		$.layer({ 
//			title:false,
//			msg:html,
//			timeout:6000,
//			width:280,
//			height:msgHt,
//			showType:'fade'
//		}); 
	}
	
	
}