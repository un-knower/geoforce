
$(function(){
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var param_location = urls.getUrlArgs();
	SuperMap.Egisp.User.currentModuleId = param_location.moduleid ? param_location.moduleid : '';

	$('ul.during > li').click(Page.initDuringClick);
	$('.custom-during .button-search').click(Map.checkCustomTime);
	
	$('.close, .btn-cancel').click(function(){
		var me = $(this);
		$(me.attr("data-target")).fadeOut('fast');
	});

	$('input.input-during').click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM-dd HH:mm:ss',
			maxDate:'%y-%M-#{%d} 23:59:59'
		});
	});
	var end = new Date().format("yyyy-MM-dd hh:mm:ss");
	var start = new Date(new Date().setDate( new Date().getDate() - 1));	
	start = start.format("yyyy-MM-dd hh:mm:ss");
	Map.start = start;
	Map.end = end;

	Page.initcss();	
	$(window).resize(Page.initcss);

}); 

var Page = {
	initcss: function() {
		var bodyWidth = getWindowWidth();
		var bodyHeight = getWindowHeight();
		$('.map-box').css({
			width: (bodyWidth - 353) + 'px',
			height: bodyHeight + 'px'
		});		
		$('.map').css({
			width: (bodyWidth - 353) + 'px',
			height: (bodyHeight - 40) + 'px'
		});		
		$('.mask-loading .box').css({
			'margin-top': ( bodyHeight > 240 ? ((bodyHeight-240)*0.5+"px") : 0)
		});
		$('.popover-result, .popover-hint').css({ left: (bodyWidth - 250)*0.5 + "px" });

		var listHeight = $('.custom-during').is(":visible") ? 160 : 100;
		$('.data-list').css({'height': (bodyHeight - listHeight)+'px'});
	}
}
Page.initDuringClick = function(){
	$('ul.during > li').removeClass('action');
	var me = $(this);
	var div = $('.console-box .custom-during');
	me.addClass('action');
	if(me.attr('data-show-date')) {
		div.show();
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
	}	

	var bodyHeight = getWindowHeight();
	var listHeight = $('.custom-during').is(":visible") ? 160 : 100;
	$('.data-list').css({'height': (bodyHeight - listHeight)+'px'});
}

