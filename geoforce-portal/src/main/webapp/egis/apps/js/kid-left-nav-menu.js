/**
 * 初始化左侧菜单及其数据
 */
$(function(){
    var products = data_products_v1.concat();
    var leftbar = $('#LeftNav');

    var data_products_cookie = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       data_products_cookie = jQuery.parseJSON(c).products;
    } 

    var len = products.length;
    var h = '';
    for(var i=0; i<len; i++) {
        var father = products[i];
        h += '<dl class="product-menu-1">';
        h += '  <dt>'+ father.name +'<i></i></dt>';

        var len_kids = father.list.length;
        for(var k=0; k<len_kids; k++) {
            var kid = father.list[k];            
            h += '<dd>';
            h += '  <a href="javascript:void(0)" data-href="'+ kid.href +'" data-option="changeFrame" data-id="'+ kid.id +'">';
            h += '      <span class="span-1"><img src="'+ kid.icon +'"/>' + kid.name + '</span>';
            var visible = isBoughtMe(kid.id, data_products_cookie);
            if( kid.href !== "javascript:void(0)" && kid.id !== "wuliu") {
                h += '  <input type="checkbox" style="display:'+ (visible ?  '' : 'none') +'"';
                if(visible) {
                    h += ' checked="checked"';
                }
                h += '   data-id="'+ kid.id +'"  name="add-solution" data-option="put">';
            }
            h += '  </a>';
            h += '</dd>';            
        }
        h += '</dl>';
    }
    leftbar.append(h);
    bindLeftMenuClicks();

    //点击a换白色背景
    var a_all = $("#LeftNav a");
    var current = $('a[data-id="10"]');
    if( current.length > 0 ) {            
        a_all.removeClass('link-ecs');
        current.addClass('link-ecs');
    }
});


function bindLeftMenuClicks() {
    var radio = $('input[data-option="put"]');
    radio.change(function(){
        var me = $(this);
        var checked = me.is(":checked");
        if( checked ) {
            var item = {
                id: me.attr("data-id"),
                s: 0
            };
            handleCookie(item);
            showSolutionBox();    
            window.frames[0].setBtns(me.attr("data-id"));        
        }
        else {
            removeAppForCookie( me.attr("data-id") );
        }
    });

    $('a[data-option="changeFrame"]').click(function(){
        var me = $(this);
        /*var data_href = me.attr('data-href');
        data_href = (data_href === "javascript:void(0)" ? "" : data_href);
        var src = data_href === "" ? getFrameSrc( me.attr("data-id") ) : data_href;
        var mysrc = $("#content_frame").attr("src");
        if(src === "item/blank" || src !== mysrc) {
            
        }*/
        location.href = '/apps/?sm=' + me.attr('data-id');
    });
}

function getFrameSrc(id) {
    var src = "item/blank";
    switch(id) {
        case "0":
            src = "item/home";
            break;
        case "1":
            src = "item/wangdian";
            break;
        case "2":
            src = "item/quhua";
            break;
        case "3":
            src = "item/fendan";
            break;
        case "4":
            src = "item/luxian";
            break;
        case "5":
            src = "item/xundian";
            break;
        case "6":
            src = "item/xiaoshou";
            break;
        case "7":
            src = "item/cheliang";
            break;
        case "8":
            src = "item/kaoqin";
            break;
        case "wuliu":
            src = "item/solutions/wuliu";
            break;
    }
    return src;
}


function hideLeftCheckbox(id) {
    $("input[data-id='"+ id +"'][data-option='put']").removeAttr('checked').css("display", "none");
}

function showLeftCheckbox(id) {
    $("input[data-id='"+ id +"'][data-option='put']").prop('checked', "checked").css("display", "");
}