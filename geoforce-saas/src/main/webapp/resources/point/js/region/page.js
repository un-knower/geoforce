
$(function(){
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var param_location = urls.getUrlArgs();
	SuperMap.Egisp.User.currentModuleId = param_location && param_location.orderItemId ? param_location.orderItemId : '';
	$('li[option="show-children"]').hover(function() {
		$(this).find(' > div').show();
	}, function() {
		$(this).find(' > div').hide();
	});

	$('.close, .btn-cancel').click(function(){
		var me = $(this);
		$(me.attr("data-target")).fadeOut('fast');
	});
	$('#btn_showBranch').change(Map.showBranchLayer);
	$('#btn_showRegion').change(Map.showRegionLayer);
	$('a[option="clear-map"]').click(Map.clear);
	$('a[option="pan-map"]').click(Map.pan);

	$('.popover-result, .popover-hint').css({ left: (bodyWidth - 250)*0.5 + "px" });
	$('#txt_keyword_cloud').keyup(function(e) {
		if(e.keyCode === 13) {
			Map.searchFromCloud();
		}		
	});
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
			Map.search();
		}
	});
	$('.data-list .btn-search').click(Map.search);
	
	if( navigator.appName == "Microsoft Internet Explorer" ) {
		var version = navigator.appVersion.split(";");
		version = parseInt(version[1].replace(/[ ]/g, "").replace(/MSIE/g, ""));
		if(version < 10) {
    		$('.btn-select-file').hide();
    		$('#txt_import_branches').css("display", "block");
    		urls.ie_case = true;    	
		}
	}

	$('.toolbox .add').click(Map.addRegionClick);
	$('.toolbox .add-path').click(Map.addPathRegionClick);
	$('.toolbox .attr').click(Map.attrRegionClick);
	$('.toolbox .delete').click(Map.deleteRegionClick);
	$('.toolbox .merge').click(Map.mergeRegionClick);
	$('.toolbox .line-split').click(Map.lineSplitRegionClick);
	$('.toolbox .area-split').click(Map.areaSplitRegionClick);
	$('button[option="delete-region"]').click(Map.deleteRegion);

	Page.initcss();	
	$(window).resize(Page.initcss);

	Map.search();
}); 

var Page = {
	initcss: function() {
		var bodyWidth = getWindowWidth();
		var bodyHeight = getWindowHeight();

		$('.data-import .box, .data-cars .box').css({
			'margin-top': ( bodyHeight > 310 ? ((bodyHeight-310)*0.5+"px") : 0)
		});
		$('.data-column-add .box').css({
			'margin-top': ( bodyHeight > 150 ? ((bodyHeight-150)*0.5+"px") : 0)
		});
		$('.mask-loading .box').css({
			'margin-top': ( bodyHeight > 240 ? ((bodyHeight-240)*0.5+"px") : 0)
		});

		var table = $('.data-list');
		var status = table.attr("status");
		if(status === "max" || status === "left-max") {			
			table.css({
				height: (bodyHeight - 41) + "px"
			});
			table.find('.data-results').css({height: (bodyHeight - 145) + "px"});
		}
		if( status === "max" ) {
			table.css({
				width: (bodyWidth - 1) + "px"
			});			
			table.find(".title").css("width", (bodyWidth- 122)+"px" );
			table.find('.data-results').css({width: (bodyWidth - 1) + "px"});
		}
		if( status === "bottom-max" ) {	
			table.css({
				width: (bodyWidth -1) + "px"
			});			
			table.find(".title").css("width", (bodyWidth- 83)+"px" );
			table.find('.data-results').css({width: (bodyWidth -1 ) + "px"});			
		}
	}
}
Page.cssTableFirst = function(){		
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var me = $(this);

	var table = $(".data-list");
	var data_table = $(".data-list .data-results");

	var status = table.attr("status");
	switch( status ) {
		case "min":
			table.css({
				width: "450px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "left-max");
			table.find(".title").css({width: "368px"});
			table.find('.content').show();
			data_table.css({width: "450px", height: (bodyHeight - 140) + "px"});
			me.removeClass('up').addClass('bottom').attr("title", "向下缩放");
			break;
		case "left-max":
			table.css({
				width: "450px",
				height: "40px"
			}).attr("status", "min");
			table.find(".title").css({width: "368px"});
			table.find('.content').hide();
			me.removeClass('bottom').addClass('up').attr("title", "向上展开");
			break;
		case "bottom-max":
			table.css({
				width: "450px",
				height: "40px"
			}).attr("status", "min");
			table.find(".title").css({width: "368px"});
			table.find('.content').hide();
			me.removeClass('bottom').addClass('up').attr("title", "向下缩放");
			table.find('.second').removeClass('up').addClass('right').attr("title", "向右展开");				
			break;
		case "max":
			table.css({
				width: "450px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "left-max");
			table.find(".title").css({width: "368px"});
			data_table.css({width: "450px", height: (bodyHeight - 140) + "px"});
			table.find('.content').show();
			me.removeClass('left').addClass('bottom').attr("title", "向下缩放");
			table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
			table.find('.third').hide();
			break;
	}
}
Page.cssTableSecond = function(){		
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var me = $(this);
	var table = $(".data-list");
	var data_table = $(".data-list .data-results");
	var status = table.attr("status");
	switch( status ) {
		case "min":
			table.css({
				width: (bodyWidth-1) + "px",
				height: "300px"
			}).attr("status", "bottom-max");
			table.find(".title").css("width", (bodyWidth - 83)+'px' );
			data_table.css({width: (bodyWidth - 1) + "px", height: "200px"});
			table.find('.content').show();
			me.removeClass('right').addClass('up').attr("title", "向上展开");
			table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
			break;
		case "bottom-max":
			table.css({
				width: (bodyWidth -1 ) + "px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "max");
			table.find(".title").css("width", (bodyWidth- 123)+"px" );
			data_table.css({width: (bodyWidth - 1) + "px", height: (bodyHeight - 145) + "px"});
			table.find(".third").show();
			table.find('.content').show();
			me.removeClass('up').addClass('bottom').attr("title", "向下缩放");
			table.find('.first').removeClass('bottom').addClass('left').attr("title", "向左缩放");
			break;
		case "max":
			table.css({
				width: (bodyWidth - 1) + "px",
				height: "300px"
			}).attr("status", "bottom-max");
			table.find(".title").css("width", (bodyWidth - 82)+'px' );
			data_table.css({width: (bodyWidth - 1) + "px", height: "200px"});
			table.find(".third").hide();
			table.find('.content').show();
			me.removeClass('bottom').addClass('up').attr("title", "向上展开");
			table.find('.first').removeClass('left').addClass('bottom').attr("title", "向下缩放");
			break;
		case "left-max":
			table.css({
				width: (bodyWidth - 1) + "px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "max");
			table.find(".title").css('width', (bodyWidth - 123)+"px" );
			data_table.css({width: (bodyWidth - 1) + "px", height: (bodyHeight - 145) + "px"});
			table.find(".third").show();
			table.find('.content').show();
			me.removeClass('right').addClass('bottom').attr("title", "向下缩放");
			table.find('.first').removeClass('bottom').addClass('left').attr("title", "向左缩放");
			break;
	}
}
Page.cssTableThird = function(){
	var table = $('.data-list');
	table.css({
		width: "450px",
		height: "40px"
	}).attr("status", "min");
	table.find('.content').hide();
	table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
	table.find('.first').removeClass('left').addClass('up').attr("title", "向上展开");
	table.find(".title").css({width: "368px"});
	$(this).hide();
}
Page.cssTableTitleClick = function(){		
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var table = $('.data-list');
	if( table.attr("status") != "min" ) {
		return;
	}		
	table.css({
		width: "450px",
		height: (bodyHeight - 50) + "px"
	}).attr("status", "left-max");
	table.find(".title").css({width: "368px"});
	$('.data-list .data-results').css({width: "450px", height: (bodyHeight - 145) + "px"});
	table.find('.content').show();
	table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
	table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
}