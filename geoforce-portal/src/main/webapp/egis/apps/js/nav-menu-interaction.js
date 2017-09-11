/**
 * 初始化左侧菜单鼠标交互
 */
$(function(){
    var leftbar = $('#LeftNav');

    //点击a换白色背景
    var a_all = $("#LeftNav a");
    a_all.click(function() {
        var me = $(this);
        a_all.removeClass('link-ecs');
        me.addClass('link-ecs');
    }).mouseover(function() {
        var me = $(this);
        var radio = me.find('input[type="checkbox"]');
        radio.show();
    }).mouseout(function() {
        var me = $(this);
        var radio = me.find('input[type="checkbox"]');
        if( !radio.is(':checked') ) {
            radio.hide();             
        }   
    });

    var param = urls.getUrlArgs();
    if(param && param.sm) {
        var current = $('a[data-id="'+ param.sm +'"]');
        if( current.length > 0 ) {            
            a_all.removeClass('link-ecs');
            current.addClass('link-ecs');
        }
    }


    //点击i展开收起
    var title_i = $("#LeftNav > dl > dt");
    title_i.click(function(){
        var me = $(this);
        var father = me.parent();
        var dd = father.find('dd');
        var i = me.find('i');
        if( i.hasClass('rotate') ) {
            i.removeClass('rotate');
            dd.each(function(){
                var myself = $(this);
                myself.slideDown('fast');
            });
        }
        else {
            i.addClass('rotate');
            dd.each(function(){
                var myself = $(this);
                myself.slideUp('fast');
            });
        }
    });

});