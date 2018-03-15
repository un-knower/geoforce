/**
 * Created by zr on 2018/1/31.
 */
$("body").show();
$(function(){

    //改变回答的显示状态
    var changeAnswerStatus = function(){
        $(".current .faq-question").click(function(){
            if($(this).next().hasClass("show")){
                $(this).next().removeClass("show");
                $(this).find("span").removeClass("glyphicon-minus").addClass("glyphicon-plus");
            }else if(!$(this).next().hasClass("show")){
                $(this).find("span").removeClass("glyphicon-plus").addClass("glyphicon-minus");
                $(this).next().addClass("show");
            }
        });
    };

    $(".has_child span").click(function(){
        if($(this).parent().hasClass("show")){
            $(this).parent().removeClass("show");
        }else{
            $(this).parent().addClass("show");
        }
    });
    //faq-content对应的盒子显示
    var faqLi = $(".faq-son li");
    var faqContent = $(".rightBar .faq-content");
    faqLi.click(function(){
        $(this).addClass("active").siblings().removeClass("active");
        var index = $(this).index();
        faqContent.eq(index).addClass("current").siblings().removeClass("current");

        changeAnswerStatus();
    });

    changeAnswerStatus();

});