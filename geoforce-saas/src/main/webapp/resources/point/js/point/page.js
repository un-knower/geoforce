
$(function(){
	var param_location = urls.getUrlArgs();
	SuperMap.Egisp.User.currentModuleId = param_location.moduleid ? param_location.moduleid : '';
	if(param_location && param_location.isLogined == 'false') {
		$('.data-guide').show();
	}

	var bodyWidth = getWindowWidth();
	var bodyHeight = getWindowHeight();
	$('li[option="show-children"]').hover(function() {
		$(this).find(' > div').show();
	}, function() {
		$(this).find(' > div').hide();
	});

	$('.close, .btn-cancel').click(function(){
		var me = $(this);
		$(me.attr("data-target")).fadeOut('fast');
		if('.data-update-style' == me.attr("data-target")) {
    		$('button[option="update-style"]').unbind('click').click(Map.Style.edit);
		}
	});
	$('#btn_showBranch').change(Map.showBranchLayer);
	$('#btn_showRegion').change(Map.showRegionLayer);
	$('a[option="clear-map"]').click(Map.clear);
	$('a[option="pan-map"]').click(Map.pan);
	$('a[option="add-point"]').click(Map.drawPoint);
	$('button[option="delete-branch"]').click(Map.deleteBranch);

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

	$('#form_import_branches').attr( "action", urls.server + "/pointService/import?&muduleId=" + SuperMap.Egisp.User.currentModuleId);
	$('#btn_import_branches').click( Map.importBranches );

	$('a[option="update-cell"]').click(Map.updateCell);
	$('a[option="update-cell-group"]').click(Map.updateCellGroup);
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
			Map.searchBranches();
		}
	});
	$('.data-list .btn-search').click(Map.searchBranches);

	if( navigator.appName == "Microsoft Internet Explorer" ) {
		var version = navigator.appVersion.split(";");
		version = parseInt(version[1].replace(/[ ]/g, "").replace(/MSIE/g, ""));
		if(version < 10) {
    		$('.btn-select-file').hide();
    		$('#txt_import_branches').css("display", "block");
    		urls.ie_case = true;    	
		}
	}
	$('.btn-search-cars').click(function(){
		$('#pager_cars').attr("page", "1");
		Cars.search();		
	});	
	$('#btn_bind_cars').click(Cars.sure);
	$('#btn_select_all_cars').click(Cars.selectAllCar);
	$('.select-all-car').change(Cars.selectAll);
	$('.data-guide .next').click(function(){
		var me = $(this);
		$(me.attr('data-pre')).hide();
		$(me.attr('data-next')).show();
	});
	$('.data-guide .start').click(function(){
		$('.data-guide').hide();
	});
	$('.data-list .head .tab-icon-style').click(function(){
		$('.data-list .content[data-display="table"]').hide();
		$('.data-list .content[data-display="icon"]').show();

	});
	$('.data-list .head .tab-text').click(function(){
		$('.data-list .content[data-display="icon"]').hide();
		$('.data-list .content[data-display="table"]').show();
	});
	$('li[data-option="show-icon-box"]').click(Page.changeIconBox);

	Page.slider = $('#slider_size').slider({
		min: 8,
		max: 48,
		step: 1
	});
	Page.slider.on('slideStop', Page.changeIconSize);
	$('.icon-selector .color li').click(Page.changeIconColor);
	$('.icon-selector .back li').click(Page.changeIconBack);
	$('button[data-option="hide-tab-edit"]').click(Page.hideIconEdit);
	$('button[data-option="save-icon-style"]').click(Map.Style.editHandler);
	$('button[option="update-style"]').click(Map.Style.edit);
	$('select.branch-group').change(Page.changeGroupSelect);
	$('.text-branchgroup-add').keyup(function(e) {
		if( e.keyCode === 13 ) {
			Map.Group.add();
		}
	});
	$('a[option="add-branchgroup-server"]').click(Map.Group.add);
	$('#form_upload_custom_icon').attr('action', urls.server + '/pointService/saveCustomFile');
	$('input.icon-size').bind('change', Page.inputIconSize);

	Page.initcss();	
	$(window).resize(Page.initcss);
	
	Page.initPoiIcos();
	Map.Style.search();
	Map.Group.search();
	Map.CustomIcon.search();

	/*window.onbeforeunload = function () {		
        return '您还有数据未导入，是否确定离开？';
    }    */
}); 

var Page = {
	initcss: function() {
		var bodyWidth = getWindowWidth();
		var bodyHeight = getWindowHeight();
		$('.popover-result, .popover-hint').css({ left: (bodyWidth - 300)*0.5 + "px" });

		$('.data-import .box, .data-cars .box').css({
			'margin-top': ( bodyHeight > 310 ? ((bodyHeight-310)*0.5+"px") : 0)
		});
		$('.data-column-add .box, .data-branchgroup-add .box').css({
			'margin-top': ( bodyHeight > 150 ? ((bodyHeight-150)*0.5+"px") : 0)
		});
		$('.mask-loading .box').css({
			'margin-top': ( bodyHeight > 80 ? ((bodyHeight-80)*0.5+"px") : 0)
		});

		var table = $('.data-list');
		var status = table.attr("status");
		if(status === "max" || status === "left-max") {			
			table.css({
				height: (bodyHeight - 41) + "px"
			});
			$('.table-branches-detail .data-results').css({height: (bodyHeight - 191) + "px"});
			$('.table-branches-count .data-results').css({height: (bodyHeight - 131) + "px"});

			$('.tab-edit, .tab-icon-list').css({height: ( bodyHeight - 90 ) + 'px'});
			$('.icon-edit-container').css({height: ( bodyHeight - 140 ) + 'px'});
		}
		if( status === "max" ) {		
			table.css({
				width: (bodyWidth - 1) + "px"
			});			
			table.find(".title").css("width", (bodyWidth- 245)+"px" );
			table.find('.data-results').css({width: (bodyWidth - 1) + "px"});
			$('.icon-selector').css({width: (bodyWidth- 480)+"px"})

			$('.data-list .content .triangle-top.back').css('left', (bodyWidth - 235)*0.75+'px' )
		}
		if( status === "bottom-max" ) {	
			table.css({
				width: (bodyWidth -1) + "px"
			});			
			table.find(".title").css("width", (bodyWidth- 205)+"px" );
			table.find('.data-results').css({width: (bodyWidth -1 ) + "px"});	
			$('.icon-selector').css({width: (bodyWidth- 480)+"px"});		
			$('.data-list .content .triangle-top.back').css('left', (bodyWidth - 205)*0.75+'px' )
		}
	},
	renameColumn: function() {
		var me = $(this);
		var td = me.parents('td');
        var left = td.offset().left;
        var top = td.offset().top;

        var bodyWidth = SuperMap.Egisp.getWindowWidth();
        var bodyHeight = SuperMap.Egisp.getWindowHeight();

        var cell_top;
        if( bodyHeight - top - td.height() > 120 ) {
            cell_top = top + td.height() + 20;
        }
        else {
            cell_top = top - 120; 
        }

        var cell_left = left;
        if( bodyWidth - left < 250 ) {
            cell_left = bodyWidth - 250;
        }
        $('.text-column-rename').val( me.attr('data-value') ).attr({
            "data-key": me.attr("data-key"),
            "data-value": me.attr('data-value')
        })
        $('.data-column-rename .box').css({ left: cell_left + "px", top: cell_top + "px" });
        $('.data-column-rename').fadeIn('fast');
	}
}
Page.slider = null;
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
			table.find(".title").css({width: "246px"});
			table.find('.content:first').show();

			$('.table-branches-detail .data-results').css({width:"450px", height: (bodyHeight - 191) + "px"});
			$('.table-branches-count .data-results').css({width:"450px", height: (bodyHeight - 131) + "px"});

			me.removeClass('up').addClass('bottom').attr("title", "向下缩放");

			$('.tab-edit, .tab-icon-list').css({height: ( bodyHeight - 82 ) + 'px'});
			$('.icon-edit-container').css({height: ( bodyHeight - 132 ) + 'px'});
			$('.icon-selector').css('width', (400)+'px');
			$('.data-list .content .triangle-top.back').css({left: "190px"});
			break;
		case "left-max":
			table.css({
				width: "450px",
				height: "40px"
			}).attr("status", "min");
			table.find(".title").css({width: "246px"});
			table.find('.content').hide();
			me.removeClass('bottom').addClass('up').attr("title", "向上展开");
			break;
		case "bottom-max":
			table.css({
				width: "450px",
				height: "40px"
			}).attr("status", "min");
			table.find(".title").css({width: "246px"});
			table.find('.content').hide();
			me.removeClass('bottom').addClass('up').attr("title", "向上展开");
			table.find('.second').removeClass('up').addClass('right').attr("title", "向右展开");				
			break;
		case "max":
			table.css({
				width: "450px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "left-max");
			table.find(".title").css({width: "246px"});

			$('.table-branches-detail .data-results').css({width:"450px", height: (bodyHeight - 191) + "px"});
			$('.table-branches-count .data-results').css({width:"450px", height: (bodyHeight - 131) + "px"});
			// table.find('.content').show();
			me.removeClass('left').addClass('bottom').attr("title", "向下缩放");
			table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
			table.find('.third').hide();

			$('.tab-edit, .tab-icon-list').css({height: ( bodyHeight - 82 ) + 'px'});
			$('.icon-edit-container').css({height: ( bodyHeight - 132 ) + 'px'});
			$('.icon-selector').css('width', (400)+'px');
			$('.data-list .content .triangle-top.back').css({left: "190px"});
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
			table.find(".title").css("width", (bodyWidth - 205)+'px' );
			$('.table-branches-detail .data-results').css({width: (bodyWidth - 1) + "px", height: "170px"});
			$('.table-branches-count .data-results').css({width: (bodyWidth - 1) + "px", height: "230px"});
			table.find('.content:first').show();
			me.removeClass('right').addClass('up').attr("title", "向上展开");
			table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");

			$('.tab-edit, .tab-icon-list').css({height: '258px'});
			$('.icon-edit-container').css({height: '215px'});
			$('.icon-selector').css('width', (bodyWidth - 480)+'px');
			$('.data-list .content .triangle-top.back').css('left', (bodyWidth - 205)*0.75+'px' )
			break;
		case "bottom-max":
			table.css({
				width: (bodyWidth -1 ) + "px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "max");
			table.find(".title").css("width", (bodyWidth- 245)+"px" );

			$('.table-branches-detail .data-results').css({width:(bodyWidth - 1)+"px", height: (bodyHeight - 191) + "px"});
			$('.table-branches-count .data-results').css({width:(bodyWidth - 1)+"px", height: (bodyHeight - 131) + "px"});

			table.find(".third").show();
			// table.find('.content').show();
			me.removeClass('up').addClass('bottom').attr("title", "向下缩放");
			table.find('.first').removeClass('bottom').addClass('left').attr("title", "向左缩放");


			$('.tab-edit, .tab-icon-list').css({height: ( bodyHeight - 82 ) + 'px'});
			$('.icon-edit-container').css({height: ( bodyHeight - 132 ) + 'px'});
			$('.icon-selector').css('width', (bodyWidth - 480)+'px');
			$('.data-list .content .triangle-top.back').css('left', (bodyWidth - 245)*0.75+'px' )
			break;
		case "max":
			table.css({
				width: (bodyWidth - 1) + "px",
				height: "300px"
			}).attr("status", "bottom-max");
			table.find(".title").css("width", (bodyWidth - 205)+'px' );
			$('.table-branches-detail .data-results').css({width: (bodyWidth - 1) + "px", height: "170px"});
			$('.table-branches-count .data-results').css({width: (bodyWidth - 1) + "px", height: "230px"});

			table.find(".third").hide();
			// table.find('.content').show();
			me.removeClass('bottom').addClass('up').attr("title", "向上展开");
			table.find('.first').removeClass('left').addClass('bottom').attr("title", "向下缩放");

			$('.tab-edit, .tab-icon-list').css({height: '258px'});
			$('.icon-edit-container').css({height: '215px'});
			$('.icon-selector').css('width', (bodyWidth - 480)+'px');
			$('.data-list .content .triangle-top.back').css('left', (bodyWidth - 205)*0.75+'px' );
			break;
		case "left-max":
			table.css({
				width: (bodyWidth - 1) + "px",
				height: (bodyHeight - 41) + "px"
			}).attr("status", "max");
			table.find(".title").css('width', (bodyWidth - 245)+"px" );
			$('.table-branches-detail .data-results').css({width:(bodyWidth - 1)+"px", height: (bodyHeight - 191) + "px"});
			$('.table-branches-count .data-results').css({width:(bodyWidth - 1)+"px", height: (bodyHeight - 131) + "px"});
			table.find(".third").show();
			// table.find('.content').show();
			me.removeClass('right').addClass('bottom').attr("title", "向下缩放");
			table.find('.first').removeClass('bottom').addClass('left').attr("title", "向左缩放");

			$('.tab-edit, .tab-icon-list').css({height: ( bodyHeight - 82 ) + 'px'});
			$('.icon-edit-container').css({height: ( bodyHeight - 132 ) + 'px'});
			$('.icon-selector').css('width', (bodyWidth - 480)+'px');
			$('.data-list .content .triangle-top.back').css('left', (bodyWidth - 245)*0.75+'px' );
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
	table.find(".title").css({width: "246px"});
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
		height: (bodyHeight - 41) + "px"
	}).attr("status", "left-max");
	table.find(".title").css({width: "246px"});

	$('.table-branches-detail .data-results').css({width:"450px", height: (bodyHeight - 191) + "px"});
	$('.table-branches-count .data-results').css({width:"450px", height: (bodyHeight - 131) + "px"});

	// table.find('.content:first').show();
	table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
	table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
	
	$('.tab-edit, .tab-icon-list').css({height: ( bodyHeight - 82 ) + 'px'});
	$('.icon-edit-container').css({height: ( bodyHeight - 132 ) + 'px'});
	$('.icon-selector').css('width', '400px');
	$('.data-list .content .triangle-top.back').css({left: "190px"});
}
Page.changeIconBox = function(){
	var me = $(this);
	$('.icon-selector .box').hide();
	$(me.attr('target')).show();
}




/**
 * 初始化图案列表
 */
Page.initPoiIcos = function() {
	var h = '';
	h += '<li data-ico="none"><span class="none">无</span></li>';
	for(var i=1; i<56; i++) {
		var str = i+'';
		str = str.length < 2 ? '0'+str : str;
		h += '<li data-ico="icon-font'+ str +'"><span class="iconfont icon-font'+ str +'"></span></li>';
	}
	$('.icon-selector .box.ico ul').html(h);	
	$('.icon-selector .ico li').click(Page.changeIconFont);
}
/**
 * 获取demo的属性
 */
Page.getDeomoAttr = function() {
	var attrs = [
		'data-size', 
		'data-color', 
		'data-color-path',
		'data-back',
		'data-ico'
	];
	var values = [
		'16',       
		'e51c23', 
		'red',
		'default',
		''
	];
	var style = {};
	var demo = $('.icon-demo');
	for(var i=attrs.length; i--; ) {
		var attr = attrs[i];
		var v = demo.attr(attr);
		style[attr] = (v && v.length > 0 && v != 'null') ? v : values[i];
	}
	return style;
}
/**
 * 选择外观
 */
Page.changeIconBack = function() {	
	$('.icon-selector .back li').removeClass('action');
	var me = $(this).addClass('action');
	var back = me.attr('data-back');
	var demo = $('.icon-demo').removeAttr('data-use-custom-ico');
	$('.icon-selector .custom li.action').removeClass('action');
	
	var demo_attr = Page.getDeomoAttr();
	var style = SuperMap.Egisp.Point.Style.getStyleSize( demo_attr['data-size'] , back, demo_attr['data-color']);	

	demo.css({
		'background-image': 'url(assets/map/'+ demo_attr['data-color-path'] +'/'+ back +'.svg)',		
		width: style.size.w + 'px',
		height: style.size.h + 'px',
		margin: style.offset.margin
	})
	.attr('data-back', back);

	$('.branch-icon-popup').attr('data-back', back).removeAttr('data-customfileid');
	SuperMap.Egisp.Point.setPopupDivStyle();
	
	$('.icon-demo > span')
	.removeClass()
	.addClass('iconfont '+ demo_attr['data-ico'])
	.css({
		'color': '#' + style.color,
		'line-height': style.offset.line_height + 'px'
	});


	//新增网点或者修改网点
	if( $('.icon-save-container').is(':hidden') ) {
		$('input[name="appearance"]').val( back );
		$('input[name="customfileid"]').val( '' );
	}
}
/**
 * 选择尺寸
 */
Page.changeIconSize = function(e) {
	$('.icon-size').val(e.value);
	Page.selectIconSize(e.value);
}

/**
 * 输入尺寸
 */
Page.inputIconSize = function() {
	var input = $('.icon-size');
	var val = Number(input.val());
	val = Number(val);
	if(isNaN(val) || val > 48 || val < 8) {
		SuperMap.Egisp.showHint('尺寸必须是8-48之间的整数');
		input.val( Page.slider.slider('getValue') );
		return;
	}	
	Page.slider.slider('setValue', val );
	Page.selectIconSize(val);
}	
/**
 * 尺寸小于12时不允许选择图案
 */
Page.disableIco = function(size) {
	size = Number(size);	
	var li_icos = $('.icon-selector .ico li[data-ico!="none"]');
	var li_none = $('.icon-selector .ico li[data-ico="none"]');
	if( size < 12) {
		$('.icon-selector .ico li').removeClass('action');
		li_none.addClass('action');
		li_icos.addClass('disabled').unbind('click');
	}
	else {
		li_icos.removeClass('disabled').unbind('click').click(Page.changeIconFont);
	}
}
/**
 * 选择尺寸
 */
Page.selectIconSize = function(size) {	
	var demo = $('.icon-demo').removeAttr('data-use-custom-ico');
	$('.icon-selector .custom li.action').removeClass('action');
	if(size < 12) {
		demo.attr('data-ico', 'none');
		$('.branch-icon-popup').attr('data-ico', 'none');
	}
	Page.disableIco(size);

	var demo_attr = Page.getDeomoAttr();
	var style = SuperMap.Egisp.Point.Style.getStyleSize( size,  demo_attr['data-back'], demo_attr['data-color']);
	demo.css({
		width: style.size.w + 'px',
		height: style.size.h + 'px',
		margin: style.offset.margin,
		'background-image': 'url(assets/map/'+ demo_attr['data-color-path'] +'/'+ demo_attr['data-back'] +'.svg)'
	})
	.attr('data-size', size);


	$('.branch-icon-popup').attr('data-size', size).removeAttr('data-customfileid');
	SuperMap.Egisp.Point.setPopupDivStyle();

	$('.icon-demo > span')
	.removeClass()
	.addClass('iconfont '+ demo_attr['data-ico'])
	.css({
		'font-size': size + 'px',
		'color': '#' + style.color,
		'line-height': style.offset.line_height + 'px'
	});
	//新增网点或者修改网点
	if( $('.icon-save-container').is(':hidden') ) {
		$('input[name="appsize"]').val(size);
		$('input[name="customfileid"]').val( '' );
	}
}
/**
 * 选择颜色
 */
Page.changeIconColor = function() {
	$('.icon-selector .color li').removeClass('action');
	var me =$(this).addClass('action');
	var color = me.attr('data-color');

	var demo = $('.icon-demo').removeAttr('data-use-custom-ico');
	$('.icon-selector .custom li.action').removeClass('action');
	var demo_attr = Page.getDeomoAttr();
	var style = SuperMap.Egisp.Point.Style.getStyleSize( demo.attr('data-size'),  demo.attr('data-back'), color);

	demo.attr({
		'data-color': color,
		'data-color-path': me.attr('data-color-path')
	}).css({	
		'width': style.size.w + 'px',
		'height': style.size.h + 'px',
		'margin': style.offset.margin,
		'background-image': 'url(assets/map/'+ me.attr('data-color-path') +'/'+ demo.attr('data-back') +'.svg)'
	});
	$('.icon-demo > span')
	.removeClass()
	.addClass('iconfont '+ demo.attr('data-ico'))
	.css({
		'font-size': me.attr('data-size') + 'px',
		'color': '#' + style.color,
		'line-height': style.offset.line_height + 'px'
	});


	$('.branch-icon-popup').attr('data-color', color).removeAttr('data-customfileid');
	SuperMap.Egisp.Point.setPopupDivStyle();

	//新增网点或者修改网点
	if( $('.icon-save-container').is(':hidden') ) {
		$('input[name="appcolor"]').val(color);
		$('input[name="customfileid"]').val( '' );
	}
}

/**
 * 选择图案
 */
Page.changeIconFont = function() {	
	$('.icon-selector .ico li').removeClass('action');
	var me = $(this).addClass('action');

	var demo = $('.icon-demo').attr('data-ico', me.attr('data-ico')).removeAttr('data-use-custom-ico');
	$('.icon-selector .custom li.action').removeClass('action');

	var demo_attr = Page.getDeomoAttr();
	var style = SuperMap.Egisp.Point.Style.getStyleSize( demo_attr['data-size'],  demo_attr['data-back'], demo_attr['data-color']);
	demo.css({	
		'width': style.size.w + 'px',
		'height': style.size.h + 'px',
		'margin': style.offset.margin,
		'background-image': 'url(assets/map/'+ demo_attr['data-color-path'] +'/'+ demo_attr['data-back'] +'.svg)'
	});

	$('.branch-icon-popup').attr('data-ico', me.attr('data-ico')).removeAttr('data-customfileid');
	SuperMap.Egisp.Point.setPopupDivStyle();

	$('.icon-demo > span')
	.removeClass()
	.addClass('iconfont '+ me.attr('data-ico'))
	.css({
		'font-size': me.attr('data-size') + 'px',
		'color': '#' + style.color,
		'line-height': style.offset.line_height + 'px'
	});

	//新增网点或者修改网点
	if( $('.icon-save-container').is(':hidden') ) {
		$('input[name="apppic"]').val(me.attr('data-ico'));
		$('input[name="customfileid"]').val( '' );
	}
}
/**
 * 选择自定义图片
 */
Page.selectCustomIco = function() {
	$('.icon-selector .custom ul li:not(.upload-custom-icon)').removeClass('action').attr({title: '选择'});
	//父元素选中
	var li = $(this).parent('li:not(.upload-custom-icon)').addClass('action').removeAttr('title');

	var style = SuperMap.Egisp.Point.Style.getStyleSize(li.attr('data-height'),'transparent','fff');
	$('.icon-demo').attr({
		'data-use-custom-ico': 'true'
	}).css({
		'background-image': 'url(' + li.find('img.ico').attr('src') + ')',
		'width': li.attr('data-width') + 'px',
		'height': li.attr('data-height') + 'px',
		margin: style.offset.margin
	});
	$('.icon-demo > span').removeClass();

	$('.branch-icon-popup').attr('data-customfileid', li.attr('data-path'));
	SuperMap.Egisp.Point.setPopupDivStyle();

	//新增网点或者修改网点
	if( $('.icon-save-container').is(':hidden') ) {
		$('input[name="customfileid"]').val( li.attr('data-id') );
	}
}
/**
 * 显示修改样式的界面
 */
Page.showEditIconStyle = function() {
	var me = $(this);
	var branch = me.find('.icon-branch');
	var icon = {};

	var btn_save = $('button[data-option="save-icon-style"]');

	var input_name = $('#txt_styleName');

	if(me.attr('data-type') == 'add') {
		icon = SuperMap.Egisp.Point.Style.defaultStyle;
		icon.name = '';
		btn_save.attr('data-type', 'add');
	}
	else {
		btn_save.removeAttr('data-type');

		icon.name = branch.attr('data-name');
		icon.back = branch.attr('data-back');
		icon.size = branch.attr('data-size');
		icon.color = branch.attr('data-color');
		icon.ico = branch.attr('data-ico');
		icon.id = branch.attr('data-id');
		icon.img = branch.attr('data-img');
		icon.groupid = branch.attr('data-groupid');
	}
	input_name.val(icon.name);

	//默认样式的分组只能为“无”，名称只能为“默认样式”
	if( me.attr('data-attr') === 'default') {
		input_name.attr('readonly', 'readonly');
		branch.attr('data-id') === '' ? btn_save.attr('data-type', 'add') : btn_save.removeAttr('data-type');
		$('#txt_branchGroup').val('none').attr('disabled', 'true');
	}
	else {
		input_name.removeAttr('readonly');	
		$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "none" ).removeAttr('disabled');	
	}

	Page.setIconSelector(icon);

}
/**
 * 初始化待修改的样式显示
 */
Page.setIconSelector = function(icon) {		
	$('.data-list .content[data-display="table"]').hide();
	$('.data-list .content[data-display="icon"]').show();
	$('.tab-icon-list').hide();
	$('.tab-edit').show();

	//名称和分组
	$('#txt_styleName').val(icon.name);
	$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "none" );
	$('.icon-demo + .bottom').html(icon.name);

	var demo = $('.icon-demo');

	//外观
	$('.icon-selector .back li').removeClass('action');
	$('.icon-selector .back li[data-back="'+ icon.back +'"]').addClass('action');

	//大小
	Page.slider.slider('setValue', Number(icon.size) );
	$('.icon-size').val(icon.size);
	Page.disableIco(icon.size);

	// 颜色
	$('.icon-selector .color li').removeClass('action');
	$('.icon-selector .color li[data-color="'+ icon.color +'"]').addClass('action');

	//图案
	$('.icon-selector .ico li').removeClass('action');
	if(icon.ico && icon.ico.length > 0) {
		$('.icon-selector .ico li[data-ico="'+ icon.ico +'"]').addClass('action');
	}
	else {
		$('.icon-selector .ico li[data-ico="none"]').addClass('action');
	}
	
	
	//自定义图片
	$('.icon-selector .custom li:not(.upload-custom-icon)').removeClass('action');
	var li = $('.icon-selector .custom li[data-path="'+ icon.img +'"]').addClass('action');


	var li_color = $('.icon-selector .color li.action');
	demo.attr({
		'data-back': icon.back,
		'data-size': icon.size,
		'data-color-path': li_color.attr('data-color-path'),
		'data-color': icon.color,
		'data-ico': icon.ico,
		'data-img': icon.img
	});	

	
	if(icon.img && icon.img.length > 0) {
		var style = SuperMap.Egisp.Point.Style.getStyleSize(li.attr('data-height'),'transparent','fff');
		demo.attr({
			'data-use-custom-ico': 'true'
		}).css({
			'background-image': 'url('+ urls.server + '/pointService/getImg?path='+ icon.img +')',
			'width': li.attr('data-width') + 'px',
			'height': li.attr('data-height') + 'px',
			margin: style.offset.margin
		});
		$('.icon-demo > span').removeClass();
		$('li[data-option="show-icon-box"][target=".icon-selector .custom"]').click();
	}
	else {
		demo.removeAttr('data-use-custom-ico');
		var style = SuperMap.Egisp.Point.Style.getStyleSize( icon.size, icon.back, demo.attr('data-color'));
		demo.css({	
			width: style.size.w + 'px',
			height: style.size.h + 'px',
			margin: style.offset.margin,
			'background-image': 'url(assets/map/' + li_color.attr('data-color-path') + '/' + icon.back + '.svg)'
		});

		$('.icon-demo > span')
		.removeClass()
		.addClass('iconfont '+ icon.ico)
		.css({
			'font-size': icon.size + 'px',
			'color': '#' + style.color,
			'line-height': style.offset.line_height + 'px'
		});
		$('li[data-option="show-icon-box"][target=".icon-selector .back"]').click();
	}

	if(icon.id) {
		demo.attr('data-id', icon.id);
	}
	else {
		demo.removeAttr('data-id');
	}
}

/**
 * 初始化待修改的样式显示
 */
Page.hideIconEdit = function() {
	$('.tab-edit').hide();
	$('.tab-icon-list').show();
}

/**
 * 选择网点分组
 */
Page.changeGroupSelect = function() {
	var me = $(this);
	if(me.val() == 'add-new') {		
        $('.data-branchgroup-add').fadeIn('fast');
        $('.text-branchgroup-add').val("");
        $('input[name="styleid"], input[name="stylename"], input[name="groupid"]').val('');
        return;
	}
	else if(me.val() == 'none') {
		$('input[name="styleid"], input[name="stylename"], input[name="groupid"]').val('');
		return;
	}

	//刷新样式
	if(me.hasClass('icon-style-input')) {
		Map.Group.getStyle(me.val());
		$('input[name="groupid"]').val(me.val());
	}
}

/**
 * 取消添加分组
 */
Page.reSetSelect = function() {
	$('.branch-group').val('none');
	$('input[name="groupid"]').val('');
}

/**
 * 点击网点弹窗中的更改图标 已有网点修改样式
 */
Page.initPoiUpdateStyle = function() {
	if( $('.data-list').attr('status') == 'min' ) {
		$('.data-list .head .tab-text').click();
	}
	var me = $(this);
	var icon = {};
	icon.name = me.attr('data-stylename');
	icon.back = me.attr('data-back');
	icon.size = me.attr('data-size'); 
	icon.color = me.attr('data-color');
	icon.ico = me.attr('data-ico');
	icon.groupid = me.attr('data-groupid');
	icon.img = '';
	if(me.attr('data-customfileid') != "") {
		$('.icon-demo').attr('data-use-custom-ico', 'true');
		// icon.img =  $('.icon-selector .custom li[data-path="'+ me.attr('data-customfileid') +'"]').attr('data-path');
		icon.img = me.attr('data-customfileid');
		$('li[data-option="show-icon-box"][target=".icon-selector .custom"]').click();
	}
	else {
		$('.icon-demo').removeAttr('data-use-custom-ico');
	}
	$('input[name="appearance"]').val(icon.back);
	$('input[name="appsize"]').val(icon.size);
	$('input[name="appcolor"]').val(icon.color);
	$('input[name="apppic"]').val(icon.ico);
	$('input[name="appcustom"]').val(icon.img);	

	var attr = {},  isUpdateBranch = false;

	if((!icon.groupid || icon.groupid == '' || icon.groupid == 'none' ) && icon.name.length > 0) {
		$('#txt_styleName').attr('readonly', 'true').val(icon.name);	
		$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "none" ).attr('disabled', 'true');	
	}
	else {
		$('#txt_styleName').removeAttr('readonly').val(icon.name);	
		$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "none" ).removeAttr('disabled');			
	}

	$('.icon-save-container').hide();
	Page.setIconSelector(icon);
}