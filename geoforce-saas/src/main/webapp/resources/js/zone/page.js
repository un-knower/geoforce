var poisearch;
$(function(){
	
	


	var param_location = urls.getUrlArgs();
	Dituhui.User.currentModuleId = param_location && param_location.moduleid ? param_location.moduleid : '';
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
		if('.data-update-style' == me.attr("data-target")) {
    		$('button[option="update-style"]').unbind('click').click(Map.Style.edit);
		}
	});
	
	$('#btn_showBranch').change(Map.showBranchLayer);
	$('#btn_showRegion').change(Map.showRegionLayer);
	$('a[option="clear-map"]').click(Map.clear);
	$('a[option="pan-map"]').click(Map.pan);

	$('.popover-result, .popover-hint').css(
		{ left: (Number($(".container-region").css("width").replace("px", "")) - 300)*0.5 + "px" }
	);
	/*$('#txt_keyword_cloud').keyup(function(e) {
		if(e.keyCode === 13) {
			$("#pager_cloudpois").attr("page", 0);
			Map.searchFromCloud();
		}		
	});*/
	
	$('.data-list .head .first').click(Page.cssTableFirst);
	$('#btn_showCountryBoundry').on('change', Map.showCountryBoundry);
	$('.region-import').on('click', Map.regionImportClick);
	$('.region-export').on('click', Map.regionExport);
	$('.select-region-import').on('change', Map.regionImport);
	$("#form_import_region").attr( "action", urls.server + "/areaService/import?&muduleId=" + Dituhui.User.currentModuleId);

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

//	$('.toolbox .town').click(Map.checkRegionsCount);
	$('.toolbox .town').click(function(){
		$(".data-import-regins").show();
	});
	$('.toolbox .add').click(Map.addRegionClick);
	$('.toolbox .add-path').click(Regionroute.addRouteRegionClick);
	$('.toolbox .attr').click(Map.attrRegionClick);
	
	$('.toolbox .edit').click(Regionedit.editRegionClick);
	$('.toolbox .delete').click(Map.deleteRegionClick);
	$('.toolbox .merge').click(Map.mergeRegionClick);
	$('.toolbox .line-split').click(Map.lineSplitRegionClick);
	$('.toolbox .area-split').click(Map.areaSplitRegionClick);
	
	$('button[option="delete-region"]').click(Map.deleteRegion);
	
	$(".btn-set-usrbelonging").on("click", Map.setUserBelonging);
	
	if(navigator.userAgent.indexOf("Firefox") > -1) {
		isFirefox = true;
		$(document).on("keydown",  function(e) {
			if(e.which == 17) {
				isCtrlKeydown = true;
			}
		});
		$(document).on("keyup",  function(e) {
			if(e.which == 17) {
				setTimeout(function(){
					isCtrlKeydown = false;
				}, 300);
			}
		});
	}
	
	Page.initcss();	
	$(window).resize(Page.initcss);

	Map.search();
	
	//初始化表格点击事件
	Dituhui.Zone.Table.initTrClick();
	
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
