
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);
    $('.header').css('margin-bottom', "0");
});
function initcss() {
    $(".introduce-area").css({"width": 960});
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );
    var bodywidth = getWindowWidth();
    $(".pendant").css( { "left":  ( bodywidth*0.5 + 280) } );
    
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
}