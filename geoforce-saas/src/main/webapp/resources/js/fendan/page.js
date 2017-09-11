
$(function(){
	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	var param_location = urls.getUrlArgs();
	Dituhui.User.currentModuleId = param_location.orderItemId ? param_location.orderItemId : '';
	if(param_location && param_location.isLogined == 'false') {
		$('.data-guide').show();
	}
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
	$('#btn_showRegion').change(Region.show);
	$('a.clear-map').click(Map.clear);
	$('a.pan-map').click(Map.pan);
	$('a[option="add-point"]').click(Map.drawPoint);

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


	$('.data-list .head .import').click(function(){
		$('.data-import').fadeIn('fast');
	});
	$('a.btn-select-file').click(function(){
		$(this).find('+ input').click();
	});

	$('#form_import_data').attr( "action", urls.server + "/orderService/upload?moduleId=" + Dituhui.User.currentModuleId);
	$('#btn_import_orders').click( Map.importOrders );

	$('a[option="update-cell"]').click(Map.updateCell);
	$('.text-cell-edit').keyup(function(e) {
		if(e.keyCode === 13) {
			Map.updateCell();
		}
	});
	$('a[option="add-column-server"]').click(Map.addColumn);
	$('a[option="rename-column-server"]').click(Map.renameColumn);
	$('.text-column-add').keyup(function(e) {
		if(e.keyCode === 13) {
			Map.addColumn();
		}
	});
	$('.a-data-success').click(Map.showSuccessData);
	$('.a-data-failed').click(Map.showFailedData);
	
	$('.data-list .search-input').keyup(function(e) {
		if( e.keyCode === 13 ) {
			var txt = $(this).val();
			if(txt == "" || txt.length === 0) {
				Dituhui.showPopover("请输入订单批次查询");
				return;
			}
			$('#pager_orders').attr('page', 0);
			Map.search();
		}
	});
	$('.data-list .btn-search').click( function(){
		var txt = $('.data-list .search-input').val();
		if(txt == "" || txt.length === 0) {
			Dituhui.showPopover("请输入订单批次查询");
			return;
		}
		$('#pager_orders').attr('page', 0);
		Map.search();
	});
	$('.data-list .head .import').click(function(){
		$('.data-import').fadeIn('fast');
	});

	if( navigator.appName == "Microsoft Internet Explorer" ) {
		var version = navigator.appVersion.split(";");
		version = parseInt(version[1].replace(/[ ]/g, "").replace(/MSIE/g, ""));
		if(version < 10) {
    		$('.btn-select-file').hide();
    		$('#txt_import_orders').css("display", "block");
    		urls.ie_case = true;    	
		}
	}
	$('.data-guide .start').click(function(){
		$('.data-guide').hide();
	})

	Page.initcss();	
	$(window).resize(Page.initcss);

	Map.search();
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
		var bodyWidth = getWindowWidth();
		var bodyHeight = getWindowHeight();
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
		var bodyWidth = getWindowWidth();
		var bodyHeight = getWindowHeight();
		var table = $('.data-list');
		if( table.attr("status") != "min" ) {
			return;
		}
		table.attr("status", "left-max");
		table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
		table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
	}
}

