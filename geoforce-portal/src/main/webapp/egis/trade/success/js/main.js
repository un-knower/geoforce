
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);
    $('.header').css('margin-bottom', "0");
});
function initcss() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();

    $(".content").height( bodyHeight - 106 - 45);
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
}