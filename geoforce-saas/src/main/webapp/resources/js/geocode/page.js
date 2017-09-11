var poisearch;
$(function(){
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var param_location = urls.getUrlArgs();
	Dituhui.User.currentModuleId = param_location.orderItemId ? param_location.orderItemId : '';
	$('li[option="show-children"]').hover(function() {
		$(this).find(' > div').show();
	}, function() {
		$(this).find(' > div').hide();
	});

	$('.close, .btn-cancel, .close-modal').click(function(){
		var me = $(this);
		if(me.hasClass("close-fade-out")) {
			$(me.attr("data-target")).fadeOut("fast");
		}
		else {
			$(me.attr("data-target")).addClass("hide");
		}
	});
	
	$('#btn_showBranch').change(Map.showBranchLayer);
	$('#btn_showRegion').change(Region.show);
	$('a.clear-map').click(Map.clear);
	$('a.pan-map').click(Map.pan);

	/*$('#txt_keyword_cloud').keyup(function(e) {
		if(e.keyCode === 13) {
			Map.searchFromCloud();
		}		
	});*/
	$('a.to-supermap').click(Map.toSuperMap);
	$('a.to-google').click(Map.toGoogleMap);
	$('a.to-tianditu').click(Map.toTiandituMap);
	$('.data-list .head .first').click(Page.cssTableFirst);

	$('.data-list .head .second').click(Page.cssTableSecond);
	$('.data-list .head .third').click(Page.cssTableThird);
	$('.data-list .head .title').click(Page.cssTableTitleClick);

	$('.a-data-success').click(Map.showSuccessData);
	$('.a-data-failed').click(Map.showFailedData);
	$('.data-list .search-input').keyup(function(e) {
		if( e.keyCode === 13 ) {
			$('#data_pager').attr("page", 0);
			Map.search();
		}
	});
	$('.data-list .btn-search').click(function(){		
		$('#data_pager').attr("page", 0);
		Map.search();
	});
	
	if( navigator.appName == "Microsoft Internet Explorer" ) {
		var version = navigator.appVersion.split(";");
		version = parseInt(version[1].replace(/[ ]/g, "").replace(/MSIE/g, ""));
		if(version < 10) {
    		$('.btn-select-file').hide();
    		$('#txt_import_branches').css("display", "block");
    		urls.ie_case = true;    	
		}
	}

	$('.text-geocode').keyup(function(e) {
		if(e.keyCode === 13) {
			Map.geocode();
		}
	});
	$('.button-geocode').click(Map.geocode);

	Page.initcss();	
	$(window).resize(Page.initcss);
		
}); 

var Page = {
	initcss: function() {
		setTimeout(function(){
			map.updateSize();
		}, 100);
	},
	cssTableFirst: function(){		
		var me = $(this);

		var table = $(".data-list");
		var status = table.attr("status");
		switch( status ) {
			case "min":
				table.attr("status", "left-max");
				me.removeClass('up').addClass('bottom').attr("title", "向下缩放");
				break;
			case "left-max":
				table.attr("status", "min");
				me.removeClass('bottom').addClass('up').attr("title", "向上展开");
				break;
			case "bottom-max":
				table.attr("status", "min");
				me.removeClass('bottom').addClass('up').attr("title", "向下缩放");
				table.find('.second').removeClass('up').addClass('right').attr("title", "向右展开");				
				break;
			case "max":
				table.attr("status", "left-max");
				me.removeClass('left').addClass('bottom').attr("title", "向下缩放");
				table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
				break;
		}
	},
	cssTableSecond: function(){		
		var me = $(this);
		var table = $(".data-list");
		var status = table.attr("status");
		switch( status ) {
			case "min":
				table.attr("status", "bottom-max");
				me.removeClass('right').addClass('up').attr("title", "向上展开");
				table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
				break;
			case "bottom-max":
				table.attr("status", "max");
				me.removeClass('up').addClass('bottom').attr("title", "向下缩放");
				table.find('.first').removeClass('bottom').addClass('left').attr("title", "向左缩放");
				break;
			case "max":
				table.attr("status", "bottom-max");
				me.removeClass('bottom').addClass('up').attr("title", "向上展开");
				table.find('.first').removeClass('left').addClass('bottom').attr("title", "向下缩放");
				break;
			case "left-max":
				table.attr("status", "max");
				me.removeClass('right').addClass('bottom').attr("title", "向下缩放");
				table.find('.first').removeClass('bottom').addClass('left').attr("title", "向左缩放");
				break;
		}
	},
	cssTableThird: function(){
		var table = $('.data-list');
		table.attr("status", "min");
		table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
		table.find('.first').removeClass('left').addClass('up').attr("title", "向上展开");
	},
	cssTableTitleClick: function(){	
		var table = $('.data-list');
		if( table.attr("status") != "min" ) {
			return;
		}		
		table.attr("status", "left-max");
		table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
		table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
	}
}

