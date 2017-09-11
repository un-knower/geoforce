$(function(){
	$('.fixed-contact-us').hover(function(){
        $(this).find('.ccontent').removeClass('show').removeClass('hide').addClass('show');
    }, function(){
        $(this).find('.ccontent').addClass('hide');
    })
});



