
/**
 * 网点样式
 */
Map.Style = {};
/**
 * 新增样式
 */
Map.Style.editHandler = function(){
	var demo = $('.icon-demo');
	var me = $(this);
	if(!demo.attr('data-use-custom-ico')) {
		var icon = {
			appearance: demo.attr('data-back'),
			apppic: demo.attr('data-ico')
		};			
		if( (icon.appearance == 'transparent' || icon.appearance == '' ) && 
			(icon.apppic == '' || icon.apppic == 'none' ) 
		) {
			SuperMap.Egisp.showHint('外观和图案不允许都为无');
			return;
		}
	}

	var name = $('#txt_styleName').val();
	if(name === "") {
		SuperMap.Egisp.showHint('请输入样式名称');
		return;
	}
	var groupid = $('#txt_branchGroup').val();
	if((!groupid || groupid == "none" || groupid == "add-new" ) && name != '默认样式') {
		SuperMap.Egisp.showHint('请选择分组');
		return;
	}
	if(name == '默认样式' && !$('#txt_styleName').attr('readonly')) {
		SuperMap.Egisp.showHint('样式名称不能为“默认样式”');
		return;		
	}	


	if(me.attr('data-type') == 'add') {
		if((!groupid || groupid == "none" || groupid == "add-new" ) && name === '默认样式'){
			SuperMap.Egisp.Modal.alert('将更新所有默认样式，确定继续？', 
				Map.Style.add
			);
			return;
		}
		Map.Style.add();		
	}	
	else {
		if(me.attr('data-poiid')) {
			$('button.btn-save-border').attr({
				'data-poiid': me.attr('data-poiid'),
				'data-styleid': me.attr('data-styleid') ? me.attr('data-styleid') : ''
			});
		}	
		else {
			$('button.btn-save-border').removeAttr('data-poiid').removeAttr('data-styleid');
		}

		var alert_str = '';
		if(!groupid || groupid == "none" || groupid == "add-new" ) {
			if(name === '默认样式') {
				alert_str = '将更新所有默认样式，确定继续？';
			}
			else {
				alert_str = '将更新名称为“'+ name +'”的图标样式，确定继续？';
			}
		}
		else {
			alert_str = '将更新所属分组为“'+ $('#txt_branchGroup option:selected').text() +'”的图标样式，确定继续？';
		}

		SuperMap.Egisp.Modal.alert(alert_str, 
			Map.Style.edit
		);
	}
}
Map.Style.add = function() {
	var icon = {};

	var demo = $('.icon-demo');
	if(!demo.attr('data-use-custom-ico')) {
		icon = {
			appearance: demo.attr('data-back'),
			appsize: demo.attr('data-size'),
			appcolor: demo.attr('data-color'),
			apppic: demo.attr('data-ico')
		};
	}
	else {
		icon = {
			customfileid: $('.icon-selector .custom li.action').attr('data-id')
		}
	}

	
	var name = $('#txt_styleName').val();
	icon.stylename = name;
	if(name === '默认样式') {
		icon.allflag = 1;
	}

	var groupid = $('#txt_branchGroup').val();
	if(!groupid || groupid == "none" || groupid == "add-new" ) {
		groupid = '';
	}
	icon.groupid = groupid;

	SuperMap.Egisp.Point.Style.add(icon,
		function(){
			SuperMap.Egisp.showPopover('保存样式成功');
			Map.Style.search();
			Map.searchBranches();
			$('.tab-edit').hide();
			$('.tab-icon-list').show();
			SuperMap.Egisp.Modal.hide();
		},
		function(info){
			SuperMap.Egisp.showHint(info);
			SuperMap.Egisp.Modal.hide();
		}
	);
}
Map.Style.edit = function() {
	var me = $(this);
	var icon = {};

	var demo = $('.icon-demo');
	if(!demo.attr('data-use-custom-ico')) {
		icon = {
			appearance: demo.attr('data-back'),
			appsize: demo.attr('data-size'),
			appcolor: demo.attr('data-color'),
			apppic: demo.attr('data-ico'),
			customfileid: ''
		};
	}
	else {
		icon = {
			customfileid: $('.icon-selector .custom li.action').attr('data-id')
		}
	}
	
	var name = $('#txt_styleName').val();
	icon.stylename = name;

	if(name == '默认样式') {
		icon.allflag = "1";
	}

	var groupid = $('#txt_branchGroup').val();
	if(!groupid || groupid == "none" || groupid == "add-new" ) {
		groupid = '';
	}
	icon.groupid = groupid;
	icon.styleid = demo.attr('data-id');
	if(me.attr('data-poiid')) {
		icon.pointid = me.attr('data-poiid');
		icon.styleid = me.attr('data-styleid') ? me.attr('data-styleid') : '';
	}		
		
	SuperMap.Egisp.Point.Style.edit(icon,
		function(){
			SuperMap.Egisp.showPopover('保存样式成功');			
			SuperMap.Egisp.Modal.hide();
			Map.Style.search();
			$('.tab-edit').hide();
			$('.tab-icon-list').show();
			$('button.btn-save-border').removeAttr('data-type').removeAttr('data-poiid').removeAttr('data-styleid');
			Map.searchBranches();
		},
		function(info){
			SuperMap.Egisp.showHint(info);
		}
	);
}

/**
 * 刷新样式数据
 */
Map.Style.search = function() {
	SuperMap.Egisp.Point.Style.search();
}



/**
 * 网点分组
 */
Map.Group = {};
Map.Group.search = function() {
	SuperMap.Egisp.Point.Group.search();
}
Map.Group.add = function() {
	var name = $.trim( $('.text-branchgroup-add').val() );
	if(name == '' || name.length == 0) {
		SuperMap.Egisp.showHint('分组名称不能为空');
		return;
	}
	if(name == "无") {
		SuperMap.Egisp.showHint('分组名称不能为“无”');
		return;
	}
	var param = {
		groupname: name
	};
	// $('#txt_styleName').val('');
	SuperMap.Egisp.Point.Group.add(param);
}

/**
 * 网点分组-查询分组的详情并展示在样式修改页面中
 */
Map.Group.getStyle = function(id) {
	$('#txt_styleName').val('');
	SuperMap.Egisp.Point.Group.getStyle(id, 
		function(data) {
			var style;
			$('#txt_styleName').val(data.stylename);
			$('.icon-demo + .bottom').html(data.stylename);
			$('.icon-selector .back li, .icon-selector .color li, .icon-selector .ico li, .icon-selector .custom li').removeClass('action');

			var item = {
				back: data.appearance ? data.appearance : '',
				ico: data.apppic ? data.apppic : '',
				color: data.appcolor ? data.appcolor : '',
				size: data.appsize ? data.appsize : '',
				img: data.appcustom ? data.appcustom.replace(/\\/g, '/') : '',
				width: data.def1 ? data.def1.split(',')[0] : 16,
				height: data.def1 ? data.def1.split(',')[1] : 16,
				color_path: SuperMap.Egisp.Point.Style.getColor(data.appcolor).path,
				id: data.id
			}
			var demo = $('.icon-demo');
			//使用自定义图片
			if(data.appcustom) {
				data.appcustom = data.appcustom;
				style = SuperMap.Egisp.Point.Style.getStyleSize( item.height,'transparent','fff');    
				var width = item.width;
				var height = item.height;
				demo.css({
					'width': width + 'px',
					'height': height + 'px',
					'margin': style.offset.margin,
					'background-image': 'url('+ urls.server +'/pointService/getImg?path='+  item.img +')'
				});
				$('.icon-selector .custom li[data-path="'+ item.img +'"]').addClass('action');
				$('input[name="customfileid"]').val( $('.icon-selector .custom li.action').attr('data-id') );
				$('li[data-option="show-icon-box"][target=".icon-selector .custom"]').click();
			}
			//使用系统组合的外观
			else {
				style = SuperMap.Egisp.Point.Style.getStyleSize( item.size, item.back, item.color);
				demo.css({
					'width': style.size.w + 'px',
					'height': style.size.h + 'px',
					'margin': style.offset.margin,
					'background-image': 'url(assets/map/'+ item.color_path + '/'+ item.back +'.svg)',
				});
				demo.find('span').removeClass().addClass('iconfont ' + item.ico).css({
					'font-size': item.size + 'px',
					'color': '#' + style.color,
					'line-height': style.offset.line_height + 'px'
				});
				$('.icon-selector .back li[data-back="'+ item.back +'"]').addClass('action');
				Page.slider.slider('setValue', Number(item.size) );
				Page.disableIco(item.size);
				$('.icon-size').val(item.size);
				$('.icon-selector .color li[data-color="'+ item.color +'"]').addClass('action');
				$('.icon-selector .ico li[data-ico="'+ item.ico +'"]').addClass('action');
			}

			demo.attr({
				'data-back': item.back,
				'data-ico': item.ico,
				'data-color': item.color,
				'data-size': item.size,
				'data-img': item.img,
				'data-color-path': item.color_path,
				'data-id': item.id
			});
			$('.branch-icon-popup').attr({
				'data-back': item.back,
				'data-ico': item.ico,
				'data-color': item.color,
				'data-size': item.size,
				'data-img': item.img
			});
			SuperMap.Egisp.Point.setPopupDivStyle();

			$('input[name="appearance"]').val(item.back);
			$('input[name="appsize"]').val(item.size);
			$('input[name="appcolor"]').val(item.color);
			$('input[name="apppic"]').val(item.ico);
			$('input[name="appcustom"]').val(item.img);
			$('input[name="styleid"]').val(item.id);
		},
		function(error) {}
	);
}

/**
 * 网点自定义图片
 */
Map.CustomIcon = {};
Map.CustomIcon.data = [];
Map.CustomIcon.search = function() {
	SuperMap.Egisp.Point.CustomIcon.search();
}
Map.CustomIcon.add = function() {
	var file = $('#txt_import_custom_icon').val();
	if(file == '' || file.length == 0) {
		return;
	}
	SuperMap.Egisp.showMask();
    $("#form_upload_custom_icon").ajaxSubmit({
    	timeout: 15000,
    	error: function() {
	    	SuperMap.Egisp.hideMask();
	    	SuperMap.Egisp.showHint('上传失败');
    	},
    	success: function(e) { 
	    	SuperMap.Egisp.hideMask();
	    	if(e.isSuccess) {
	    		$('#txt_import_custom_icon').val('');
	    		Map.CustomIcon.search();
	    	}
	    	else {
	    		SuperMap.Egisp.showHint(e.info ? e.info : '上传失败');
	    	}
    	}
    });
}
Map.CustomIcon.remove = function() {
	SuperMap.Egisp.Point.CustomIcon.remove( $('.btn-delete-custom-icon').attr('data-id') );
}
