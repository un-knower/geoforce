Map.Pictures = {};

Map.Pictures.search = function(editable) {
	var id = $('#txt_upload_point_picture_pointid').val();
	//查询网点照片
    $('.ul-point-pictures li.normal').remove();
    Dituhui.Point.Pictures.search( id, 
        function(data){
            var len = data.length, h = '', h_1 = '';
            for(var i=0; i<len; i++) {
            	var item = data[i];
            	h += '<li class="normal">';
            	h += '	<img src="'+ urls.server + '/pointService/getImg?path=' + item.filepath.replace(/\\/g, '/') + '">';
            	h += '	<div class="del" title="删除" data-id="'+ item.id +'"></div>';
            	h += '</li>';
            }
            $('.add-point-pictures').before(h);

            $('.preview-img').show();

            if(len > 4) {
            	$('.add-point-pictures').hide();
            }
            else {
            	$('.add-point-pictures').show();
            }

    		var html  = '<img src="'+ urls.server + '/pointService/getImg?path=' + data[0].filepath.replace(/\\/g, '/') + '">';
    			html += '<div class="count"><span>'+ len +'张</span></div>';
    		$('.photo:not(.edit)').removeClass('none').html(html).unbind('click').click(function(){
                Map.Pictures.viewPictures();
    			$('.view-pictures').removeClass("hide");
    		});
            $('.photo.add:not(.edit)').removeClass('add');

    		$('.ul-point-pictures .del').unbind('click').click(Map.Pictures.remove);

    		$('.upload-pictures-title').html('图集('+ len +')');
        },
        function(){
        	$('.upload-pictures-title').html('图集');
        	$('.add-point-pictures').show();
            $('.ul-point-pictures li.normal').remove();
            $('.preview-img').hide();
			if(editable) {
				$('.photo').removeClass('none').addClass('add')
	        	.html('<div class="caption"><span>上传图片<span></div>')
	        	.unbind('click').click(function(){
	                Map.Pictures.viewPictures();
					$('.upload-pictures').removeClass("hide");
				});
			}
			else {
				$('.photo').removeClass('none').unbind('click').addClass('none').html('');
			}
        }
    );
}

/**
 * 上传网点照片
 */
Map.Pictures.upload = function(){
	var input = document.getElementById("txt_upload_point_picture");

	if(urls.ie_case) {
		if( input.value == "" ) {    			
	        Dituhui.showHint( "请选择文件" );
    		return false;	
		}	
		else {
			var val = input.value;
			var arr = val.split('\\');
			val = arr[ arr.length - 1 ];

			div.html( val );
			return true;
		}
	}

	var fs = input.files;
	if(!fs || fs.length < 1 ) {
        Dituhui.showHint( "请选择文件" );
		return false;
	}
	var file = fs[0];
	if(file.size > 2048000) {
		Dituhui.showHint("文件大于2M，请重新选择");
		return false;
	}

	if( $('#txt_upload_point_picture_pointid').val() == '' ) {
		Dituhui.showHint("获取网点ID失败");
		return false;
	}

	$('.add-point-pictures').addClass('load');
	$("#form_upload_point_picture").ajaxSubmit({
    	timeout: 30000,
    	error: function(e) {
    		Dituhui.showHint('上传失败');
            $('.add-point-pictures').removeClass('load');
    	},
    	success: function(e) {
    		$('.add-point-pictures').removeClass('load');
    		if(e && e.result) {
    			Dituhui.showPopover('上传成功');
                $('.preview-img').show();
    			var h = '';
            	h += '<li class="normal">';
            	h += '	<img src="'+ urls.server + '/pointService/getImg?path=' + e.result.picPath.replace(/\\/g, '/') + '">';
            	h += '	<div class="del" title="删除" data-id="'+ e.result.picid +'"></div>';
            	h += '</li>';

    			$('.add-point-pictures').before(h);

    			$('.ul-point-pictures .del').unbind('click').click(Map.Pictures.remove);

    			Map.Pictures.refreshPictures();
                $('.upload-pictures-title').html('图集('+ $('.ul-point-pictures li.normal').length +')');
    		}
    		else {
    			Dituhui.showHint( e && e.info ? e.info : '上传失败，请稍候重试');
    		}
    		var file = $("#txt_upload_point_picture");
			file.after(file.clone().val(""));      
			file.remove();
    	}
    });
}

Map.Pictures.remove = function() {
	var me = $(this);
	Dituhui.Point.Pictures.remove( me.attr('data-id'), 
		function(e){
			Dituhui.showPopover('删除成功');
			me.parent('li').remove();
			$('.add-point-pictures').show();
			Map.Pictures.refreshPictures();
            $('.upload-pictures-title').html('图集('+ $('.ul-point-pictures li.normal').length +')');
		},
		function(){
			Dituhui.showHint('删除失败');
		}
	);
}

Map.Pictures.refreshPictures = function() {	
	var count = $('.ul-point-pictures li.normal').length;
	if(count == 0) {
		var h = '<div class="caption"><span>上传照片</span></div>';
		$('.pictures .photo.edit').html(h).unbind('click').click(function(){
			$('.upload-pictures').removeClass("hide");
		});
		$('.preview-img').hide();
        $('.photo:not(.edit)').addClass('add');
		return;
	}
	if( count > 4 ) {
		$('.add-point-pictures').hide();
	}
	var src = $('.ul-point-pictures li.normal:first-child img').attr('src');
	var html  = '<img src="'+ src + '">';
		html += '<div class="count"><span>'+ count +'张</span></div>';
	$('.photo:not(.edit), .preview-img').html(html).unbind('click').click(function(){
		Map.Pictures.viewPictures();
		$('.view-pictures').removeClass("hide");
	});
    $('.photo.add:not(.edit)').removeClass('add');
}

Map.Pictures.viewPictures = function() {
	var data = [];
	$('.ul-point-pictures li.normal').each(function() {
        var src = $(this).find('img').attr('src');
        data.push({
        	image: src
        })
	});
	Page.viewPictures(data);
}