$(function(){
    $("#toTop").click( function(){
        var e=$("body").offset().top;
        $("html,body").animate({scrollTop:e},382)
    })
});
window.onscroll = function(){
    var e=$(document).scrollTop();
    e>30?$("#toTop").show(618):$("#toTop").hide(382)
}