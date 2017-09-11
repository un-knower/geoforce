
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);

    $("button[option='buy']").click(function(){
        location.href = "../../trade/success";
    })
    $("button[option='return']").click(function(){
        location.href = "../";
    })
    load();
});

function initcss() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();

    // $(".content").height( bodyHeight - 105 - 45);
    $(".content").css({ "min-height": ( bodyHeight - 105 - 45)  });
    
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
}

function load() {
    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);
    if (c) {
       products = jQuery.parseJSON(c).products;
    } 
    var len = products.length;

    var fathers = getFathersFromCookie(products);

    var len_fathers = fathers.length;

    var div = $('.content-body');
    if(len === 0 || len_fathers === 0) {
        var h =  '<div class="hintno">';
            h += '  <span>尚未定制方案，现在去';
            h += '      <a href="../">定制</a>';
            h += '  </span>';
            h += '</div>';
        div.html(h);
        return;
    }


    var h =  '';
        h += '<div class="title">';
        h += '  <span class="main-title">我的定制方案</span>';
        h += '  <span class="hint">提交方案，我们会尽快与您联系。</span>';
        h += '  <button class="btn intro-btn-yellow"  option="buy" data-loading-text="正在处理，请稍后...">确认方案</button>';
        // h += '  <span class="for-price">总价：<span class="sum-price">&yen;900</span></span>';
        h += '</div>';

        h += '<div class="orders">';
        h += '  <div class="all"> ';
        h += '      <input type="checkbox" checked="true" name="select_all"/>全选';
        h += '      <span class="headline-name">功能模块</span>';
        h += '      <span class="headline-time">使用时间</span>';
        h += '      <span class="headline-option">操作</span>';
        h += '  </div>';
        h += '  <div class="items">';
    var units = [];
    for(var i=len_fathers; i--; ) {
        var item = fathers[i];
        var len_kids = item.kids ? item.kids.length : 0;
        var type = len_kids > 0 ? 1 : 0;

        h += '      <div class="item">';
        var father_checked = ( $('input[name="pro_parent_'+ item.id +'"]').prop("checked") ? $('input[name="pro_parent_'+ item.id +'"]').prop("checked") : true );
        h += '          <div class="parent"><input checked="'+ father_checked +'" type="checkbox" removetype="'+ type +'" name="pro_parent_'+ item.id +'"/>'+ item.name +'</div>';
        h += '          <div class="children">';

        if(len_kids > 0) {
            for(var k=len_kids; k--; ) {
                var kid = item.kids[k];
                h += '      <span class="father">'+ kid.name +'</span>';
                if( kid.kids && kid.kids.length > 0 ) {
                    for(var m=kid.kids.length; m--; ) {
                        if( m===0 ) {
                            h += '  <span class="kid-last">'+ kid.kids[m].name +'</span>';
                        }
                        else {                            
                            h += '  <span class="kid">'+ kid.kids[m].name +'</span>';
                        }
                    }
                }
            } 
        }

        h += '          </div>';
        h += '          <div class="time">';
        h += '              <span class="btn btn-default btn-xs" id="minus_'+ item.id +'" option="minusTime">';
        h += '                  <img class="img-time" src="images/left.png"/>';
        h += '              </span>';
        var time_number = ( $('#txt_time_'+ item.id).val() ? $('#txt_time_'+ item.id).val() : '1' );

        h += '              <input type="text" id="txt_time_'+ item.id +'" class="form-control text-time" style="margin:0;" value="'+ time_number +'" maxlength="2"/>';
        h += '              <span class="btn btn-default btn-xs" id="add_'+ item.id +'"  option="addTime">';
        h += '                  <img class="img-time" src="images/right.png"/>';
        h += '              </span>';
        var value_unit = ( $("#txt_unit_"+ item.id).val() ? $("#txt_unit_"+ item.id).val() : 'month');
        units.push({
            id: item.id,
            val: value_unit
        });
        h += '              <select style="margin:0 0 0 10px;" id="txt_unit_'+ item.id +'">';
        h += '                  <option value="month">月</option>';
        h += '                  <option value="quarter">季</option>';
        h += '                  <option value="year">年</option>';
        h += '              </select>';
        h += '          </div>';

        h += '          <div class="remove">';
        h += '              <a href="javascript:void(0);" removetype="'+ type +'" id="pro_remove_'+ item.id +'">删除</a>';
        h += '          </div>';
        h += '      </div>';
    }
        h += '  </div>';

        h += '  <div class="all">';
        h += '      <input type="checkbox"  checked="true" name="select_all"/>全选';
        h += '      <button type="button" class="btn btn-default btn-sm" style="margin-left: 20px;border:none;" id="btn_remove_selected_items">';
        h += '          <span class="glyphicon glyphicon-remove"></span> 删除选中项';
        h += '      </button>';
        h += '      <a href="javascript:void(0);" id="rebuy" class="btn btn-default btn-sm" style="margin-left: 20px;border:none;"> ';
        h += '          <span class="glyphicon glyphicon-arrow-left"></span>返回重新定制';
        h += '      </a>';
        h += '  </div>';
        h += '  <div class="remark"> ';
        h += '      <span>备注：</span><input id="txt_remarks" type="text" placeholder="填写备注"/>';
        h += '  </div>';
        h += '  <div class="title">';
        h += '      <button class="btn intro-btn-yellow" option="buy"  data-loading-text="正在处理，请稍后...">确认方案</button>';
        h += '  </div>';
        h += '</div>';

    div.html(h);
    $('select[id^="txt_unit_"]').each(function(){
        var me = $(this);
        var id = me.attr('id').replace("txt_unit_", "");
        var len = units.length;
        for(var i=len; i--; ) {
            var o = units[i];
            if(id === o.id) {
                me.val(o.val)
                break;
            }
        }
    })

    bindclicks();
}

/**
 * 绑定按钮事件
 */
function bindclicks() {
    $('#rebuy').click(rebuy);

    // $('input[type="checkbox"]').prop("checked", true);

    $('input[name^="pro_parent_"]').change(function() {
        var me = $(this);
        var checked = me.prop("checked");

        var id = me.attr("name").replace("pro_parent_", "");

        var children = $("input[name='pro_kids_"+ id +"']");
        children.prop("checked", checked);
        if(!checked) {
            $('input[name="select_all"]').prop("checked", checked);
        }
    });

    $('input[name="select_all"]').change(function(event) {        
        var me = $(this);
        var checked = me.prop("checked");

        var children = $("input[name^='pro_']");
        children.prop("checked", checked);
        $('input[name="select_all"]').prop("checked", checked);
    });

    //删除选中项
    $("#btn_remove_selected_items").click(removeSelectedApps);

    //删除行
    $("a[id^='pro_remove_']").click(removeThisApp);

    $('span[option="minusTime"]').click(function(event) {
        var me = $(this);
        var id = me.attr('id').replace('minus_', "");
        var input = $("#txt_time_" + id);
        var number = Number( input.val() );
        if( isNaN(number) ) {
            number = 0;
        }
        number--;
        input.val( number < 1 ? 1 : number );
    });
    $('span[option="addTime"]').click(function(event) {
        var me = $(this);
        var id = me.attr('id').replace('add_', "");
        var input = $("#txt_time_" + id);
        var number = Number( input.val() );
        if( isNaN(number) ) {
            number = 0;
        }
        number++;
        input.val(number > 99 ? 99 : number);
    });

    $("input[id^='txt_time_']").bind("keyup", function() {
        var me = $(this);
        var reg = /\D/;
        if( reg.test( me.val() )  ){
            showPopover('请输入正整数');
            // me.val(1);
        }
    });

    $('button[option="buy"]').click(buy);
}

/**
 * 删除选中项
 */
function removeSelectedApps() {  
    var selects = $('input[name^="pro_parent_"]:checked');
    if(selects.length === 0) {
        showPopover("未选中任何项");
        return;
    }

    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);

    if (!c || c === null || c === "") {
       load();
       return;
    } 
    products = jQuery.parseJSON(c).products;   
    selects.each(function(){
        var me = $(this);
        var id = me.attr("name").replace("pro_parent_", "");
        var type = me.attr("removetype");
        if(type === '1') {
            var remove_father_flag = true;
            $('input[name="pro_kids_'+ id +'"]').each(function(){
                var it = $(this);
                var checked = it.is(":checked");
                if(checked) {
                    var kid_id = it.attr("id");
                    removeItemByIdFromArray(kid_id, products); 
                }
                else {
                    remove_father_flag = false;
                }
            });
            if(remove_father_flag) {
                removeItemByIdFromArray(id, products);
            }
        }
        else {
            removeItemByIdFromArray(id, products);
        }        
    });

    var cookie_kids = {
        products: products
    };
    $.cookie(urls.cookie_products_name, JSON.stringify( cookie_kids ), {
        path: '/',
        expires: 365
    });
    load();
}

/**
 * 删除当前行
 */
function removeThisApp(){
    var me = $(this);
    var id = me.attr("id").replace("pro_remove_", "");        
    var products = [];
    var c = jQuery.cookie(urls.cookie_products_name);

    if (!c || c === null || c === "") {
       load();
       return;
    } 
    products = jQuery.parseJSON(c).products;
    
    removeItemByIdFromArray(id, products);  
    var cookie_kids = {
        products: products
    };
    $.cookie(urls.cookie_products_name, JSON.stringify( cookie_kids ), {
        path: '/',
        expires: 365
    });
    load();
}


/**
 * 返回重新定制
 */
function rebuy() {    
    jQuery.cookie(urls.cookie_products_name, null,{path:"/"});
    location.href = "../";    
}

/**
 * 重组cookie数据
 */
function getFathersFromCookie(products) {
    var data_pros = data_products_v1.concat();
    var data_wuliu = data_apps_wuliu_v1.concat();

    var fathers = [];
    var len = products.length;
    if(len === 0 ) {
        return fathers;
    }
    for(var i=len; i--; ) {
        var id = products[i].id;
        var item = getItemFromArrayByID(id, data_pros[0].list);
        if(item) {
            fathers.push(item);
        }

        var item_wuliu = getItemFromArrayByID(id, data_wuliu);
        if(item_wuliu) {
            fathers.push(item_wuliu);            
        }
    }

    return fathers;
}
function getItemFromArrayByID(id, datas) {
    var len = datas.length;
    if(len === 0) {
        return null;
    }
    for(var i=len; i--; ) {
        var item = datas[i];
        if(id === item.id) {
            return item;
        }
    }
    return null;
}


/**
 * 订购
 */
function buy(){
    var me = $(this);
    var parents = $('input[name^="pro_parent_"]:checked');
    if(parents.length === 0) {
        showPopover("请至少选择一项产品");
        return;
    }
    var flag = true;
    $('input[id^="txt_time_"]').each(function(){
        var val = $(this).val();
        if(val === "") {
            showPopover("请输入产品使用时间");
            flag = false;
            return flag;
        }
        if( isNaN( Number(val) ) || Number(val) > 99) {
            showPopover("产品使用时间输入不正确");
            flag = false;
            return flag;            
        }
    });
    if(!flag) {
        return flag;
    }
    me.button('loading');
    var data_pros = data_products_v1.concat();
    var data_wuliu = data_apps_wuliu_v1.concat();

    var params = [];
    var times = [];
    var today = new Date();
    parents.each(function(){
        var me = $(this);
        var id = me.attr("name").replace("pro_parent_", "");
        var now = new Date();
        var unit = $('#txt_unit_' + id).val();
        var number = Number( $('#txt_time_' + id).val() );
        switch(unit) {
            case "month":
                now.setMonth(new Date().getMonth()+number);
                break;
            case "quarter":
                now.setMonth(new Date().getMonth() + number*3);
                break;
            case "year":
                now.setFullYear(new Date().getFullYear()+number);
                break;
        }

        times.push(now);

        var useTime = today.format('yyyy-MM-dd');
        var deadline = now.format('yyyy-MM-dd');

        var boys = getItemFromArrayByID(id, data_pros[0].list);
        var kids = [];
        if(boys){
            kids = boys.kids ? boys.kids : new Array();
        }
        else {
            boys = getItemFromArrayByID(id, data_wuliu);
            var o = {
                moduleId: id,
                useTime: useTime,
                deadline: deadline,
                type: boys.type
            }
            params.push(o);
            kids = boys.kids ? boys.kids : new Array();
        }

        for(var i=kids.length; i--; ) {
            var o = kids[i];
            var item = {};

            if( isSelectMe(o.id, params) ) {
                continue;
            }
            item.moduleId = o.id;
            item.useTime = useTime;
            item.deadline = deadline;
            item.type = o.type;
            params.push(item);

            var boys = [];
            boys = o.kids ? o.kids : boys;
            for(var m=boys.length; m--; ) {
                var boy = boys[m];

                params.push(
                    {
                        moduleId: boy.id,
                        useTime: useTime,
                        deadline: deadline,
                        type: boy.type
                    }
                );
            }
        }
    });
    times.sort();

    var len_system = urls.systemmges.length;
    for(var k=len_system; k--; ) {
        var myid = urls.systemmges[k];
        var o = {
            moduleId: myid,
            name: "free",
            useTime: today.format('yyyy-MM-dd'),
            deadline: times[(times.length - 1)].format('yyyy-MM-dd'),
            consultPrice: 0,
            useLimit: 0,
            type: 1
        };
        params.push(o);
    }

    var len_homepage = urls.homepagemges.length;
    for(var k=len_homepage; k--; ) {
        var myid = urls.homepagemges[k];
        var o = {
            moduleId: myid,
            name: "free",
            useTime: today.format('yyyy-MM-dd'),
            deadline: times[(times.length - 1)].format('yyyy-MM-dd'),
            consultPrice: 0,
            useLimit: 0,
            type: 1
        };
        params.push(o);
    }
    
    var parameter = {
        remarks: $("#txt_remarks").val(),
        orderItems: params,
        orderType: 1
    };

    //验证是否登陆
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: function(e) {
            me.button('reset');
            if(e && e.success && e.result) {                
                /*$.ajax({
                    type: 'GET',
                    async: true,
                    data: {parameter:  JSON.stringify( parameter ) },
                    url: urls.trade_buy,
                    dataType: 'jsonp',
                    success: buy_successHandler
                });
*/
                $.post(
                    urls.trade_buy,
                    {parameter:  JSON.stringify( parameter ) },
                    buy_successHandler
                )
            }
            else {
                setTimeout(toLogin, 2000);
            }
        }
    });
}

function isSelectMe(id, items) {
    var len = items.length;
    if(len === 0) {
        return false;
    }
    for(var i=len; i--; ) {
        var item = items[i];
        if( id === items[i].moduleId ) {
            return true;
        }
    }
    return false;
}

/**
 * 订购完成
 */
function buy_successHandler(e){
    e = jQuery.parseJSON(e);
    
    if(e.success) {
        jQuery.cookie(urls.cookie_products_name, null,{path:"/"});
        location.href = "../../trade/success"; 
    }
    else {
        var info = e.info;
        switch(info) {
            case "用户未登录":
                showPopover("用户未登录，请先登录");
                setTimeout(toLogin, 2000);
                break;
            case "id to load is required for loading":
                showPopover("请先补充个人详细资料");
                setTimeout(toFillUser, 1000);
                break;
            default:
                showPopover(info);
                break;
        }
    }
}

function toLogin() {
    location.href = urls.login;
}
function toFillUser() {
    $('#modal_filluser').modal('show');
}