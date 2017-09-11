
$(function(){
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();

	var param_location = urls.getUrlArgs();
	Dituhui.User.currentModuleId = param_location.moduleid ? param_location.moduleid : '';
	$('ul.during > li').click(Page.initDuringClick);
	$('.custom-during .button-search').click(Map.checkCustomTime);
	
	$('.close, .btn-cancel, .close-modal').click(function(){
		var me = $(this);
		if(me.hasClass("close-fade-out")) {
			$(me.attr("data-target")).fadeOut("fast");
		}
		else {
			$(me.attr("data-target")).addClass("hide");
		}
		if('.data-update-style' == me.attr("data-target")) {
    		$('button[option="update-style"]').unbind('click').click(Map.Style.edit);
		}
	});
	
	$('input.input-during').click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM-dd HH:mm:ss',
			maxDate:'%y-%M-#{%d} 23:59:59'
		});
	});
}); 

var Page = {};
Page.initDuringClick = function(){
	$('ul.during > li').removeClass('action');
	var me = $(this);
	var div = $('.console-box .custom-during');
	me.addClass('action');
	if(me.attr('data-show-date')) {
		div.show();
		$(".data-list-group").addClass("shorter");
	}
	else {
		div.hide();
		var time = me.attr('data-time');
		var end = new Date().format("yyyy-MM-dd hh:mm:ss");
		var start;
		switch(time) {
			case 'day':
				start = new Date(new Date().setDate( new Date().getDate() - 1));
				break;
			case 'week':
				start = new Date(new Date().setDate( new Date().getDate() - 7));
				break;
			case 'month':
				start = new Date(new Date().setDate( new Date().getDate() - 30));
				break;
		}
		start = start.format("yyyy-MM-dd hh:mm:ss");
		Map.start = start;
		Map.end = end;
		Map.search();
		$(".data-list-group").removeClass("shorter");
	}
}

