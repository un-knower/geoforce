Point.Pictures = {};

Point.Pictures.search = function(point_id) {
	var param = {
        pointid: point_id
    };
    SuperMap.Egisp.request({
        url: urls.server + "/pointService/getPointPicturesByPointid?",
        data: param,
        success: function(e){
            if(e.isSuccess && e.result && e.result.length > 0) {
            	Point.Pictures.display(e.result)
            }
        },
        error: function(){
            
        }
    })
}

Point.Pictures.swiper = null;
Point.Pictures.display = function(data) {
	var img_url = urls.server + '/pointService/getImg?path=' + data[0].filepath.replace(/\\/g, '/');
	jQuery('.modal-detail .row-img .img img')
	.attr('src', img_url)
	.unbind('click')
	.click(Point.Pictures.gotoSlide);
	jQuery('.modal-detail .row-img .img .count').html(data.length + '张');
	
	var h = '';
	for(var i = data.length; i--; ) {
		var src = urls.server + '/pointService/getImg?path=' + data[i].filepath.replace(/\\/g, '/')
		h += '<li>';
        h += '	<div class="pinch-zoom">';
        h += '  	<div class="big-image">';
        h += '  	<img src="'+ src +'"><span></span></div>';
        h += '	</div>';
		h += '</li>';
	}
	
	jQuery('ul.point-pictures').html(h);
	
	
    Point.Pictures.swiper = null;
    
    Point.Pictures.swiper = new Swipe(document.getElementById('slider_pics'), {
    	callback: Point.Pictures.next
    });
    $('div.pinch-zoom').each(function () {
        new RTP.PinchZoom($(this), {});
    });
    
    jQuery('.modal-pictures .content').unbind('click').click(function(){
    	jQuery('.modal-pictures').addClass('hide');	
    });
}
Point.Pictures.gotoSlide = function() {
	jQuery('.modal-pictures').removeClass('hide');	
	Point.Pictures.swiper.setup();
}
Point.Pictures.next = function() {
	
}


Point.Pictures.clear = function() {
	
}


























