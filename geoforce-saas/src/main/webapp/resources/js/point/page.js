var poisearch;
$(function(){
	var param_location = urls.getUrlArgs();
	Dituhui.User.currentModuleId = param_location.moduleid ? param_location.moduleid : '';
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
	$('#btn_showRegion').change(Region.show);
	$('#btn_showCluster').change(Map.searchBranches);
	$('#show-point-label').change(Map.showPointLabels);
	$('a.clear-map').click(Map.clear);
	$('a.pan-map').click(Map.pan);
	$('a.add-point').click(Map.drawPoint);
	
	//针对一个用户进行移动端采集
	if(userid == "8a04a77b56a21dcc01570cbaf9450a15") { //鑫之源客户ID
//	if(userid == "40288e9f483f48e501483f48eb060000") { //地图慧测试账号ID		
		$('a.add-mobile-point').on("click", Map.showMobilePointWindow).parent().removeClass("hide");
		$('a.btn-search-mpoint').on("click", Map.searchMobilePoint);
		$(".hint-mobile-point").hover(function(){
			$('.hint-part').removeClass("hide");
		}, function(){
			$('.hint-part').addClass("hide");
		});
	}
	
	$('button[option="delete-branch"]').click(Map.deleteBranch);

	/*$('#txt_keyword_cloud').keyup(function(e) {
		if(e.keyCode === 13) {
			$("#pager_cloudpois").attr("page", 0);
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

	$('.data-list .head .import').click(function(){
		$('.data-import').fadeIn('fast');
	});
	$('a.btn-select-file').click(function(){
		$(this).find('+ input').click();
	});

	$('#form_import_branches').attr( "action", urls.server + "/pointService/import?&muduleId=" + Dituhui.User.currentModuleId);
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
	$('.data-list .select-group').change(function(){
		Map.searchBranches();
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
		$('#pager_cars').attr("page", "0");
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
	$('a[option="remove-branchgroup-server"]').click(Map.Group.removeHandler);
	$('#form_upload_custom_icon').attr('action', urls.server + '/pointService/saveCustomFile');
	$('input.icon-size').bind('change', Page.inputIconSize);
	$('#form_upload_point_picture').attr('action', urls.server + '/pointService/savePointPicture');
	$('.add-point-pictures').click(function(){
		$('#txt_upload_point_picture').click();
	});
	$('.data-results.head-fixed').scroll(function(e){
		var me = $(this);
		me.find('.overflow-y-auto').width( me.width() +  e.target.scrollLeft ).css({'max-width': e.target.scrollWidth + 'px'});
	});
	$('.setting-fields .setting-sure').click(Dituhui.Point.setFields);
	$('.setting-fields .setting-cancel').click(Dituhui.Point.cancelSetFields);

	Page.initcss();	
	$(window).resize(Page.initcss);
	
	var clickshare = false;
	$('.header-share').on('click', function(){
		$('.bk-share').removeClass('hide');
		if(!clickshare) {
			$("#share_current_url").zclip({
				path: urls.server + "/resources/js/public/zclip/ZeroClipboard.swf",
				copy: function(){
					return $('#currentPage_shareLink').val();
				},
				afterCopy:function(){
					$('.copy-success').removeClass('hide');
					setTimeout(function(){
						$('.copy-success').addClass('hide');
					}, 2500);
		        }
			});
		}
		clickshare = true;
	});	
	$('.closeBtn.close-share').on('click', function(){
		$(this).parent().addClass('hide');
	});
	var share_link = location.protocol + '//' + location.host +  urls.server + '/resources/mobile/point/index.html?key=' + userid;
	$('#currentPage_shareLink').val(share_link);
	
	$('#share_currentPage_code').qrcode(
		{width: 120,height: 120,text: share_link}
	);
	
	Page.initPoiIcos();
	Map.Style.search();
	Map.Group.search();
	Map.CustomIcon.search();
	Dituhui.Point.Table.bindTdClick();
}); 

var Page = {
	initcss: function() {
		setTimeout(function(){
			map.updateSize();
		}, 100);	
	},
	renameColumn: function() {
		var me = $(this);
		var td = me.parents('td');
        var left = td.offset().left;
        var top = td.offset().top;

		var bodyWidth = $('.container-point').width();
		var bodyHeight = getWindowHeight() - 58;

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
	var bodyWidth = $('.container-point').width();
	var bodyHeight = getWindowHeight() - 58;
	var me = $(this);

	var table = $(".data-list");
	var data_table = $(".data-list .data-results");

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
			me.removeClass('bottom').addClass('up').attr("title", "向上展开");
			table.find('.second').removeClass('up').addClass('right').attr("title", "向右展开");				
			break;
		case "max":
			table.attr("status", "left-max");
			me.removeClass('left').addClass('bottom').attr("title", "向下缩放");
			table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
			break;
	}
}
Page.cssTableSecond = function(){		
	var bodyWidth = $('.container-point').width();
	var bodyHeight = getWindowHeight() - 58;
	var me = $(this);
	var table = $(".data-list");
	var data_table = $(".data-list .data-results");
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
}
Page.cssTableThird = function(){
	var table = $('.data-list');
	table.attr("status", "min");
	table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
	table.find('.first').removeClass('left').addClass('up').attr("title", "向上展开");
}
Page.cssTableTitleClick = function(){	
	var bodyWidth = $('.container-point').width();
	var bodyHeight = getWindowHeight() - 58;
	var table = $('.data-list');
	if( table.attr("status") != "min" ) {
		return;
	}		
	table.attr("status", "left-max");
	table.find('.first').removeClass('up').addClass('bottom').attr("title", "向下缩放");
	table.find('.second').removeClass('bottom').addClass('right').attr("title", "向右展开");
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
	var style = Dituhui.Point.Style.getStyleSize( demo_attr['data-size'] , back, demo_attr['data-color']);	

	demo.css({
		'background-image': 'url('+ urls.server +'/resources/assets/map/'+ demo_attr['data-color-path'] +'/'+ back +'.svg)',		
		width: style.size.w + 'px',
		height: style.size.h + 'px',
		margin: style.offset.margin
	})
	.attr('data-back', back);

	$('.branch-icon-popup').attr('data-back', back).removeAttr('data-customfileid');
	Dituhui.Point.setPopupDivStyle();
	
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
		Dituhui.showHint('尺寸必须是8-48之间的整数');
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
	var style = Dituhui.Point.Style.getStyleSize( size,  demo_attr['data-back'], demo_attr['data-color']);
	demo.css({
		width: style.size.w + 'px',
		height: style.size.h + 'px',
		margin: style.offset.margin,
		'background-image': 'url('+ urls.server +'/resources/assets/map/'+ demo_attr['data-color-path'] +'/'+ demo_attr['data-back'] +'.svg)'
	})
	.attr('data-size', size);


	$('.branch-icon-popup').attr('data-size', size).removeAttr('data-customfileid');
	Dituhui.Point.setPopupDivStyle();

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
	var style = Dituhui.Point.Style.getStyleSize( demo.attr('data-size'),  demo.attr('data-back'), color);

	demo.attr({
		'data-color': color,
		'data-color-path': me.attr('data-color-path')
	}).css({	
		'width': style.size.w + 'px',
		'height': style.size.h + 'px',
		'margin': style.offset.margin,
		'background-image': 'url('+ urls.server +'/resources/assets/map/'+ me.attr('data-color-path') +'/'+ demo.attr('data-back') +'.svg)'
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
	Dituhui.Point.setPopupDivStyle();

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
	var style = Dituhui.Point.Style.getStyleSize( demo_attr['data-size'],  demo_attr['data-back'], demo_attr['data-color']);
	demo.css({	
		'width': style.size.w + 'px',
		'height': style.size.h + 'px',
		'margin': style.offset.margin,
		'background-image': 'url('+ urls.server +'/resources/assets/map/'+ demo_attr['data-color-path'] +'/'+ demo_attr['data-back'] +'.svg)'
	});

	$('.branch-icon-popup').attr('data-ico', me.attr('data-ico')).removeAttr('data-customfileid');
	Dituhui.Point.setPopupDivStyle();

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

	var style = Dituhui.Point.Style.getStyleSize(li.attr('data-height'),'transparent','fff');
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
	Dituhui.Point.setPopupDivStyle();

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
		icon = Dituhui.Point.Style.defaultStyle;
		icon.name = '';
		btn_save.attr('data-type', 'add');

		$('.delete-point-style').hide();
	}
	else {
		$('.delete-point-style').show();

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
		branch.attr('data-id') === '' ? btn_save.attr('data-type', 'add') : btn_save.removeAttr('data-type');
		$('#txt_branchGroup').val('0').attr('disabled', 'true');
		$('.delete-point-style').hide().unbind('click');
	}
	else {
		$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "0" ).removeAttr('disabled');	
		$('.delete-point-style').unbind('click').click(function(){
			var h = '确定要删除该样式？<br>';
			h += '删除后该样式下的网点将全部还原为默认样式。';
			Dituhui.Modal.alert(h, 
				Map.Style.remove,
				{"data-id": icon.id,}
			);
		});
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
	$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "0" );
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
		var style = Dituhui.Point.Style.getStyleSize(li.attr('data-height'),'transparent','fff');
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
		var style = Dituhui.Point.Style.getStyleSize( icon.size, icon.back, demo.attr('data-color'));
		demo.css({	
			width: style.size.w + 'px',
			height: style.size.h + 'px',
			margin: style.offset.margin,
			'background-image': 'url('+ urls.server +'/resources/assets/map/' + li_color.attr('data-color-path') + '/' + icon.back + '.svg)'
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
	
	if(!Dituhui.User.isTop) {
		$('.icon-edit-container .icon-selector').hide();
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
	
	var groupname = $('#txt_branchGroup option:selected').text();
	var groupvalue = $('#txt_branchGroup').val();
	$('#txt_styleName').val( groupname );
	$('.icon-demo + .bottom').html(groupname);
	
	if(me.val() == 'add-new') {	
		if(Dituhui.Point.Group.data.length == 10) {
			Dituhui.showHint('最多只可创建10个分组，有更多需求请联系商务');
		}
		else {
	        $('.data-branchgroup-add').fadeIn('fast');
	        $('.text-branchgroup-add').val("");
	        $('input[name="styleid"], input[name="stylename"], input[name="groupid"]').val('');	
		}
		
		/*$('#txt_styleName').val("默认样式");
		$('.icon-demo + .bottom').html("默认样式");	
		$('#txt_branchGroup').val("0");*/
        return;
	}
	else if(me.val() == "remove-group") {
		var data = Dituhui.Point.Group.data
		if(data.length == 0) {
			Dituhui.showHint("当前没有网点分组数据");
			return;
		}
		for(var i=data.length, h=''; i--; ) {
	        var item = data[i];
	        h += '<option value="'+ item.id +'">'+ Dituhui.setStringEsc(item.groupname) +'</option>';
	    }
//		$('#txt_styleName').val( "" );
		$('#txt_styleName').val( "默认样式" );
		$('.icon-demo + .bottom').html("默认样式");
		$('.select-groups-toremove').html(h);
		$('.data-branchgroup-remove').fadeIn('fast');
		Map.Group.switchStyle(Dituhui.Point.Style.defaultStyle, true);
		return;
	}
	else if(me.val() == 'none' || me.val() == '0') {
		$('input[name="styleid"], input[name="stylename"], input[name="groupid"]').val('');	
		$('#txt_styleName').val('默认样式');
		$('.icon-demo + .bottom').html('默认样式');
		Map.Group.switchStyle(Dituhui.Point.Style.defaultStyle, true);
		$(".delete-point-style").addClass("hide");
		return;
	}
	$(".delete-point-style").removeClass("hide");
	$('#txt_styleName').val( groupname );
	$('.icon-demo + .bottom').html(groupname);
	
	$('.select-group-popup').val(me.val());
	$('input[name="groupid"]').val( me.val() );
	
	//刷新样式
	if(me.hasClass('icon-style-input')) {
		Map.Group.getStyle(me.val());
	}
}

/**
 * 取消添加分组
 */
Page.reSetSelect = function() {
	$('.branch-group').val('none');
	$('input[name="groupid"]').val('0');
	$('input#txt_styleName').val('');
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

	if((!icon.groupid || icon.groupid == '' || icon.groupid == '0' ) && icon.name.length > 0) {
		$('#txt_styleName').val(icon.name);	
		$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "0" ).attr('disabled', 'true');	
	}
	else {
		$('#txt_styleName').val(icon.name);	
		$('#txt_branchGroup').val( icon.groupid ? icon.groupid : "0" ).removeAttr('disabled');			
	}
	$('.icon-save-container').hide();
	
	Page.setIconSelector(icon);
}

var rotateAngle = 0, galleria_zoom = 0;
Page.viewPictures = function(data) {	
	rotateAngle = 0;
	galleria_zoom = 0;

	if(Galleria.destroy) {
		Galleria.destroy();
	}
	if( 0 == Galleria.getLoadedThemes().length ) {
		Galleria.loadTheme(urls.server + '/resources/js/public/galleria/themes/classic/galleria.classic.min.js');
	}
	
    Galleria.run("#images", {
       	dataSource: data,
       	image_crop: true,
       	transition: 'fade',
       	maxScaleRatio: 1,
       	thumbCrop: false
    });
	
	//放大
    $('.show-img-btn.zoom-in').unbind('click').click(function(){
    	if (!(galleria_zoom >= 5)) {
	        var e = $($(".images").data("galleria").getActiveImage())
	          , t = e.width()
	          , a = e.height();
	        e.width(1.2 * t),
	        e.height(1.2 * a);
	        var r = e.parent(".galleria-image").width() / 2 - e.width() / 2;
	        e.css("left", r);
	        var i = e.parent(".galleria-image").height() / 2 - e.height() / 2;
	        e.css("top", i),
	        galleria_zoom++
	    }
    });
    //向左旋转
    $(".show-img-btn.turn-left").unbind('click').click(function() {
	    rotateAngle -= 90,
	    $($(".images").data("galleria").getActiveImage()).rotate({
	        animateTo: rotateAngle
	    })
	});
	//向右旋转
	$(".show-img-btn.turn-right").unbind('click').click(function() {
	    rotateAngle += 90;
	    $($(".images").data("galleria").getActiveImage()).rotate({
	        animateTo: rotateAngle
	    })
	});
	//全屏
	$(".show-img-btn.full-img").unbind('click').click(function() {
	    var e = $(".images").data("galleria").getActiveImage();
	    window.open(e.src.split("@")[0])
	});
	//关闭
	$(".show-img-btn.shut").click(function() {
	    $('.view-pictures').addClass("hide");
	});
	//缩小
	$(".show-img-btn.zoom-out").unbind('click').click(function() {
	    if (!(-5 >= galleria_zoom)) {
	        var e = $($(".images").data("galleria").getActiveImage())
	          , t = e.width()
	          , a = e.height();
	        e.width(t / 1.2),
	        e.height(a / 1.2);
	        var r = e.parent(".galleria-image").width() / 2 - e.width() / 2;
	        e.css("left", r);
	        var i = e.parent(".galleria-image").height() / 2 - e.height() / 2;
	        e.css("top", i),
	        galleria_zoom--
	    }
	});
}