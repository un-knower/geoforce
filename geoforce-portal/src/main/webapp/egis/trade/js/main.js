
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);

    $("#btn_search").click(changeStatus);
    $("#select_search_order_status").change(changeStatus);
    $('.header').css('margin-bottom', "0");

    $.ajax({
        type: 'GET',
        async: true,
        url: urls.userdetail,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success && e.result) {
                user = e.result;
                search();
            }
            else {    
                showPopover("用户未登录");
                setTimeout(function(){
                    location.href = urls.login;
                }, 1500);
            }
        }
    });

    var appsLength = urls.getSelectedAppsLength();
    $("#badge").attr("sum", appsLength).html( appsLength > 0 ? appsLength : '' );
});

function initcss() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();

    // $(".content-body").height( bodyHeight - 105 - 45 - 60 );
    $(".content-body").css( {"min-height":(bodyHeight - 105 - 45 - 60), "overflow": "hidden"} );
    
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
}

function changeStatus() {
    var pager = $("#pager");
    pager.attr('page', "0");
    search();
}

function search() {
    var table = $("#table_orders");
    var status = $("#select_search_order_status").val();

    var pager = $("#pager");
    var pageNo = Number( pager.attr('page') );
    var param = {
        pageNo: pageNo
    };
    if(status !== '-1') {
        param.status = status;
    }

    $.ajax({
        type: 'GET',
        async: true,
        url: urls.tradesearch,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                var orders = e.result.infos;
                var len = orders.length;
                if(len === 0) {
                    table.html("");
                    pager.hide();
                    // showPopover("查询到0条订单");
                    return;
                }   
                var h = '';
                for(var i=0; i<len; i++) {
                    var order = orders[i];
                    h += '<tr>';
                    h += '  <td>'+ (i+1) +'</td>';
                    h += '  <td>'+ order.submitTime +'</td>';
                    // h += '  <td>'+ order.totalPrice +'</td>';
                    h += '  <td>'+ (order.orderType == '0' ? "免费试用" : "商务定制") +'</td>';
                    h += '  <td>'+ getOrderStatus(order.status) +'</td>';

                    h += '  <td>';
                    h += '      <a href="item?q='+ order.orderId +'">查看详情</a>';
                    if(order.status != '1') {
                        h += '  <i></i>';
                        h += '  <a href="javascript:void(0);" id="a_'+ order.orderId +'" option="remove">删除</a>';
                    }
                    h += '  </td>';
                    h += '</tr>';
                }
                pager.show();
                table.html(h);

                var html_page = setPage(e.result.total, pageNo, '\'pager\'', 10); 
                $("#pager > ul").html(html_page);  

                $("a[option='remove']").click(function(){
                    var me = $(this);
                    var id = me.attr("id").replace("a_", "");
                    $("#txt_order_delete_id").val(id);
                    $('#modal_delete_order').modal('show');
                })
            }
            else {
                showPopover("查询订单失败，请稍候重试");
                $("#pager").hide();
            }
        }        
    });
}

function getOrderStatus(status, funcNames) {
    var val = "";
    /*//状态：0，待审核；1，审核通过；2，审核不通过；3其它
    if(typeof(funcNames) !== "undefined" && funcNames != "") {
        val = "试用方案";
        return val;
    }*/
    switch(status) {
        case "0":
            val = "待审核";
            break;
        case "1":
            val = "商务定制审核通过";
            break;
        case "2":
            val = "免费试用审核通过";
            break;
        case "3":
            val = "审核不通过";
            break;
    }
    return val;
}


function removeOrder() {   
    var id = $("#txt_order_delete_id").val();
    $.ajax({
        type: 'GET',
        async: true,
        data: { id: id },
        url: urls.tradesremove,
        dataType: 'jsonp',
        success: remove_successHandler
    });
}
function remove_successHandler(e) {
    if(e && e.success) {
        showPopover("删除成功");
        $('#modal_delete_order').modal('hide');

        search();
    }
    else {
        showPopover(e.info);
    }
}