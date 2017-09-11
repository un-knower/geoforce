
$(document).ready(function() {
    initcss();
    $(window).resize(initcss);
});
function initcss() {
    $(".inner-right").css("width", ($(".inner").width() - $(".inner-left").width() ) );

    var bodyHeight = getWindowHeight();

    // $(".content").height( bodyHeight - 106 - 45);
    $(".content").css( {
        "min-height": (bodyHeight - 106 - 45) , 
        "overflow": "hidden",
        "padding-bottom": "40px"
    } );
    
    /*var bodywidth = getWindowWidth();
    if(bodywidth > 1200) {
        $(".header .inner").css({"width": 1200});
        $(".footer").css({"width": 1200});
        $(".introduce-area").css({"width": 1200});
    }*/
    load();
}

function load(){
    var id = urls.getUrlArgs().q;

    $.ajax({
        type: 'GET',
        async: true,
        data: {id: id},
        url: urls.tradesdetail,
        dataType: 'jsonp',
        success: load_successHandler
    });
}

function load_successHandler(e) {
    if(e && e.success  && e.result) {
        var order = e.result;
        // $("#span_id").html( order.orderId );
        $("#span_createTime").html( order.submitTime );
        // $("#span_price").html( order.totalPrice );
        $("#span_remarks").html( order.remarks ? order.remarks : '暂无详细信息' );
        $("#span_status").html( getOrderStatus( order.status ) );

        var items = e.result.orderItems;
        var len = items.length;
        if(len === 0) {
            return;
        }
        var fathers = getFathersFromArray(items);
        var len = fathers.length;

        var h = '';
        for(var i=0; i<len; i++) {
            var f = fathers[i];
            h += '<li>';
            h += '<span class="title">' + (i + 1) +'. ' + f.moduleName + '</span>';
            var kids = f.children;
            var len_kids = kids.length;
            if(len_kids === 0) {

                var start = f.startUseTime.split('-');
                var end = f.deadline.split('-');
                var time = getDateDiff( f.startUseTime, f.deadline );
                if(time >= 365) {
                    time = parseInt(end[0]) - parseInt(start[0]);
                    time += "年";
                }
                else if(time < 7) {
                    time = "< 1周";
                }                
                else if(time >= 7 && time <= 28) {
                    time = Math.ceil( time / 7 ) + "周";
                }
                else {                    
                    time = (parseInt(end[0]) * 12 + parseInt(end[1]) - parseInt(start[0]) * 12 - parseInt(start[1])) + "个月";
                }
                h += '<br><span class="time">使用时间：'+ time +'</span>';
                h += '</li>';
                continue;
            }
            for(var k=0; k<len_kids; k++) {
                var kid = kids[k];
                if(k === 0) {
                    h += ' ：&nbsp;&nbsp;&nbsp;&nbsp;<br>';
                }
                h += '<span class="word">' + kid.moduleName+'</span>';
                if( k < (len_kids - 1)) {
                    h += ",";
                }
            }

            var start = f.startUseTime.split('-');
            var end = f.deadline.split('-');;
            var time = getDateDiff( f.startUseTime, f.deadline );
            if(time >= 365) {
                time = parseInt(end[0]) - parseInt(start[0]);
                time += "年";
            }
            else if(time < 7) {
                time = "< 1周";
            }              
            else if(time >= 7 && time < 28 && time) {
                if( time % 7 === 0 ) {
                    time = (time / 7) + "周";
                }
                else {
                    time =  time + "天";
                }
            }
            else {                    
                time = (parseInt(end[0]) * 12 + parseInt(end[1]) - parseInt(start[0]) * 12 - parseInt(start[1])) + "个月";
            }
            h += '<br><span class="time">使用时间：'+ time +'</span>';
            h += '</li>';
        }
        $('#span_solutions').html(h);
    }
    else {
        showPopover(e.info);
    }
}

function getOrderStatus(status) {
    var val = "";
    //状态：0，待审核；1，审核通过；2，审核不通过；3其它
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

/**
 * 重组cookie数据
 */
function getFathersFromArray(products) {
    var fathers = [];
    var len = products.length;
    if(len === 0 ) {
        return null;
    }
    for(var i=len; i--; ) {
        var item = products[i];

        if( item.pModuleId === "" ) {
            item.children = [];
            fathers.push(item);
        }
    }

    var len_fathers = fathers.length;
    if(len_fathers === 0) {
        return null;
    }
    for(var j=len; j--; ) {
        var item = products[j];

        for(var k=len_fathers; k--; ) {
            var f = fathers[k];
            if(item.pModuleId === f.moduleId) {
                f.children.push(item);
                fathers[k] = f;
                break;
            }
        }
    }
    return fathers;
}

/**
 * 查重
 */
function checkRepeat(id, records) {
    var len = records.length;
    if(len === 0) {
        return false;
    }
    for(var i=len; i--; ) {
        var r = records[i];
        if( id === r.id) {
            return true;
        }
    }
    return false;
}

/**
 * 计算两个日期之间的天数
 * date1: '2015-07-22'
 */
function getDateDiff(date1, date2) {
    var start = new Date( date1 ).getTime();
    var end = new Date( date2 ).getTime();
    var c = 60*60*24*1000;
    var diff = (end - start)/c;
    return diff;
}