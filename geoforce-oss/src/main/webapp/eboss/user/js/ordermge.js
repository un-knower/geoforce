/**
 * 订单管理
 */
var Order = {
    /**
     * 所有的产品
     */
    productsList: [],
    /**
     * 保存查询的订单
     */
    data_orders: []
}

/**
 * 查询所有产品
 */
Order.searchAllProducts = function() {
	Order.productsList = [];
	var param = {
		pageNo: -1
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if( e && e.success ) {
        		var items = e.result.items;
        		Order.productsList = items;
        		var len = items.length;
                if(len === 0) {
                    showPopover("没有可选择的产品");
                    return;
                }

                var html = '<ul>';
                html += '   <li>';
                html += '       <input style="margin: 0;" type="checkbox" name="select_all"/>全选';
                html += '   </li>';
                var fathers = Order.getFathersFromArray(items);
                var len_fathers = fathers.length;

                for(var i=len_fathers; i--; ) {
                    var item = fathers[i];

                    var len_kids = item.children.length;

                    html += Order.getLiHtml(item, 0, (len_kids > 0) );  

                    for(var k=len_kids; k--; ) {
                        var kid = item.children[k];                        
                        html += Order.getLiHtml(kid, 50);  
                    }                   
                }
                html += '   <li>';
                html += '       <input style="margin: 0;" type="checkbox" name="select_all"/>全选';
                html += '   </li>';
                html += '</ul>';
                $("#div_products_list").html(html);

                Order.bindclickforcheckboxs();
                Order.setDefaultTime();
        	}
        	else {
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 重组cookie数据
 */
Order.getLiHtml = function(item, padding, type) {
    var html = '<li style="border-bottom: 1px solid #f5f5f5;padding-left:'+ padding +'px;">';
    html += '   <input type="checkbox" style="margin: 0;" ';
    //父节点
    if(padding === 0) {
        html += ' name="pro_parent_'+ item.id +'"';
    }
    // 子节点
    else {
        html += ' id="'+ item.id +'" name="pro_kids_'+ item.pid +'"';        
    }
    html += '   onClick="Order.subprice()">';
    html += '   <span class="name">'+ item.name + '</span>';

    if(padding !== 0 || type === false) {
        html += '<span style="display:none;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
        html += '价格:&nbsp;<input class="text-input" id="txt_price_'+ item.id +'" type="text" name="price" value="'+ item.price +'" onkeyup="Order.subprice()">';
        html += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
        html += '限制:&nbsp;<input  id="txt_limit_'+ item.id +'" type="text" class="text-input" name="limit" value="'+ item.limit +'"></span>';
    }
    html += '   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;时间:&nbsp;';
    html += '   <input type="text"  class="text-date-start" name="start" ';
    if( padding === 0 ) {
        html += 'isFather="true"';        
    }

    html += '   id="txt_orderitemdate_start_'+ item.id +'" ';
    html += '   readonly="true" onfocus="Order.pickDateStart()">';
    html += '   <span class="wordto" style="line-height: 22px;font-size: 12px;">至</span>';
    html += '   <input type="text"  class="text-date-end" name="end"  ';
    if( padding === 0 ) {
        html += 'isFather="true"';        
    }

    html += '   id="txt_orderitemdate_end_'+ item.id +'" ';
    html += '   readonly="true" onfocus="Order.pickDateEnd()">';
    html += '</li>';

    return html;
}

/**
 * 绑定点击事件
 */
Order.bindclickforcheckboxs = function() {
    $('input[name^="pro_parent_"]').change(function() {
        var me = $(this);
        var checked = me.prop("checked");

        var id = me.attr("name").replace("pro_parent_", "");

        var children = $("input[name='pro_kids_"+ id +"']");
        children.prop("checked", checked);
        if(!checked) {
            $('input[name="select_all"]').prop("checked", checked);
        }
        Order.subprice();
    });


    $('input[name="select_all"]').change(function(event) {        
        var me = $(this);
        var checked = me.prop("checked");

        var children = $("input[name^='pro_']");
        children.prop("checked", checked);
        $('input[name="select_all"]').prop("checked", checked);

        Order.subprice();
    });


    $('input[name^="pro_kids_"]').change(function(event) {       
        var me = $(this);
        var id = me.attr("name").replace("pro_kids_", "");
        var checked = me.prop("checked");

        if(checked) {
            $("input[name='pro_parent_"+ id +"']").prop("checked", checked);
        }
        else {
            var selects = $('input[name^="pro_kids_'+ id +'"]:checked');
            if(selects.length === 0) {
                $("input[name='pro_parent_"+ id +"']").prop("checked", checked);
            }
        }

        Order.subprice();
    });
}


/**
 * 重组cookie数据
 */
Order.getFathersFromArray = function(products) {
    var fathers = [];
    var len = products.length;
    if(len === 0 ) {
        return fathers;
    }
    for(var i=len; i--; ) {
        var item = products[i];

        if( item.pid === "") {
            item.children = [];
            fathers.push(item);
        }
    }

    var len_fathers = fathers.length;
    if(len_fathers === 0) {
        var h =  '<div class="hintno">';
            h += '  <span>尚未定制方案，现在去';
            h += '      <a href="../">定制</a>';
            h += '  </span>';
            h += '</div>';        
        $('.content-body').html(h);
        return;
    }
    for(var j=len; j--; ) {
        var item = products[j];

        for(var k=len_fathers; k--; ) {
            var f = fathers[k];
            if(item.pid === f.id) {
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
Order.checkRepeat = function(id, records) {
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
 * 选择时间
 */
Order.pickDateStart = function(){
    WdatePicker({skin:'whyGreen', minDate: "%y-%M-%d"});
}
/**
 * 选择时间
 */
Order.pickDateEnd = function(){
    WdatePicker({skin:'whyGreen', minDate: "%y-%M-%d"});
}

/**
 * 计算价格
 */
Order.subprice = function(){
	var price = 0;
    $("input[name^='pro_parent_']:checked").each(function(){
        var id = $(this).attr("name").replace('pro_parent_',  '');
        var txt = $("#txt_price_" + id);

        if(txt && txt.length > 0) {
            price += Number( txt.val() );
        }
    });

    $("input[name^='pro_kids_']:checked").each(function(){
        var id = $(this).attr("id");
        var txt = $("#txt_price_" + id);
        price += Number( txt.val() );
    });

	$("#txt_order_price").val(price);
}

/**
 * 设置默认时间，商务定制为1个月，免费试用为周
 */
Order.setDefaultTime = function() {
    var type = $('#txt_order_status').val();
    var startDate = new Date();

    var endDate_free = new Date(new Date().setDate( new Date().getDate() + 14));
    var endDate_business = new Date(new Date().setMonth( new Date().getMonth() + 1 ));
    var endDate = type==="0" ? endDate_free : endDate_business;

    $('input[id^="txt_orderitemdate_start_"][isFather="true"]').val( startDate.format('yyyy-MM-dd') );    
    $('input[id^="txt_orderitemdate_end_"][isFather="true"]').val( endDate.format('yyyy-MM-dd') );
}

/**
 * 验证用户ID
 */
Order.verifyUserIDForOrder = function(){
    var txt = $('#txt_order_userid').val();
    var id = $('#txt_order_userid').attr('userid')
    var span = $('#span_tip_order_userid');
    if(txt.length === 0 || txt === "" || !id) {
        span.html("请输入关键字回车搜索并选择用户" + "<img src='images/wrong.png'></img>");
        showPopover('请输入关键字回车搜索并选择用户');
        return false;
    }
    span.html("");
    return true;
}

/**
 * 验证服务开始时间
 */
Order.verifyOrderDateStart = function(){
    var txt = $('#txt_orderdate_start').val();
    var span = $('#span_tip_orderdate');
    if(txt.length === 0 || txt === "") {
        span.html("请输入服务开始时间");
        return false;
    }
    span.html("");
    return true;
}

/**
 * 验证服务结束时间
 */
Order.verifyOrderDateEnd = function(){
    var txt = $('#txt_orderdate_end').val();
    var span = $('#span_tip_orderdate');
    if(txt.length === 0 || txt === "") {
        span.html("请输入服务结束时间");
        return false;
    }
    var start = $('#txt_orderdate_start').val();
    if(start !== "") {        
        var startdate = new Date(start.replace(/-/g,"/"));
        var enddate = new Date(txt.replace(/-/g,"/")); 
        if(enddate <= startdate) {
            span.html("结束时间须大于开始时间");
            return false;
        }
    }
    span.html("");
    return true;
}

/**
 * 验证是否选择了服务
 */
Order.verifyProductSelected = function() {
    var selects = $("input[name^='pro_parent_']:checked");
    if(selects.length === 0) {
        showPopover("请选择服务模块");
        return false;
    }
    var flag = true;
    selects.each(function(){     
        var id = $(this).attr("name").replace('pro_parent_',  '');   
        var price = $("#txt_price_" + id).val();
        if(price === "") {
            showPopover("请为选中的产品输入价格");
            flag = false;
            return flag;
        }
        var start = $("#txt_orderitemdate_start_" + id).val();
        if(start === "") {
            showPopover("请为选中的产品输入开始时间");
            flag = false;
            return flag;
        }
        var end = $("#txt_orderitemdate_end_" + id).val();
        if(end === "") {
            showPopover("请为选中的产品输入结束时间");
            flag = false;
            return flag;
        }        

        var startdate = Number(start.replace(/-/g,""));
        var enddate = Number(end.replace(/-/g,"")); 
        if(enddate <= startdate) {
            showPopover("结束时间不能早于开始时间");
            flag = false;
            return flag;
        }
    });
    return flag;
}   

/**
 * 验证订单输入
 */
Order.verifyOrderInput = function() {
    var flag = Order.verifyUserIDForOrder();
    if(!flag) {
        return flag;
    }

    flag = Order.verifyProductSelected();
    return flag;
}

/**
 * 添加订单
 */
Order.add = function() {
    var flag = Order.verifyOrderInput();
    if(!flag) {
        return flag;
    }

    var children = [];
    var parents = $("input[name^='pro_parent_']:checked");
    parents.each(function(){        
        var id = $(this).attr("name").replace('pro_parent_',  '');

        var item = {
            moduleId: id,
            useTime: $("#txt_orderitemdate_start_" + id).val(),
            deadline: $("#txt_orderitemdate_end_" + id).val()
        };

        var limit = $("#txt_limit_" + id).val();
        if(limit && limit.length > 0) {
            var useLimit = Number( limit );
            item.useLimit = useLimit;
            item.consultPrice = $("#txt_price_" + id).val()
        }
        children.push(item);
    });


    var kids = $("input[name^='pro_kids_']:checked");
    kids.each(function(){   
        var me = $(this);     
        var pid =  me.attr("name").replace('pro_kids_',  '');
        var id = me.attr("id");

        var item = {
            moduleId: id,
            useLimit: $("#txt_limit_" + id).val(),
            consultPrice: $("#txt_price_" + id).val()
        };

        var date_start = $("#txt_orderitemdate_start_" + id).val();
        if(date_start === "") {
            item.useTime = $("#txt_orderitemdate_start_" + pid).val();
        }
        else {
            item.useTime = date_start;
        }
        var date_end = $("#txt_orderitemdate_end_" + id).val();
        if(date_end === "") {
            item.deadline = $("#txt_orderitemdate_end_" + pid).val();
        }
        else {
            item.deadline = date_end;
        }

        children.push(item);
    });


    var parameter =  {
        userId: $("#txt_order_userid").attr("userid"),
        remarks: $("#txt_order_remark").val(),
        consultPrice: $("#txt_order_price").val(),
        orderItems: children,
        orderType: $('#txt_order_status').val()
    };

    var param = {
        parameter: JSON.stringify( parameter )
    };

    $.ajax({
        type: 'POST',
        async: true,
        url: urls.order.add,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success) {
                showPopover("订单添加成功");
                $('#modal_add_order').modal('hide');
                Order.search();
            }
            else {              
                showPopover(e.info);
            }
        }
    });
}

/**
 * 查询订单
 */
Order.search = function(){
    Order.data_orders = [];
    var pageNo = Number($("#pager_orders").attr("page"));
    var param = {
        pageNo: pageNo,
        pageSize: pageSize
    };

    var table = $("#table_orders");
    table.html('');

    var keyword = $('#txt_search_order').val();
    if( keyword.length > 0 ) {
        param.info = keyword;
    }
    var status = $('#select_search_order_status').val();
    if( status !== "-1" ) {
        param.status = status;
    }

    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e && e.success ) {
                if( !e.result || !e.result.items || e.result.items.length === 0 ) {
                    showPopover("当前查询到0条数据");
                    return;
                }
                var items = e.result.items;
                Order.data_orders = items;
                var len = items.length, html = '';

                var per = user.privilegeCodes;
                var per_detail = jQuery.inArray("ordermge_detail", per);
                var per_update = jQuery.inArray("ordermge_update", per);
                var per_audit = jQuery.inArray("ordermge_audit", per);
                var per_remove = jQuery.inArray("ordermge_remove", per);

                for (var i=0; i<len; i++) {
                    var item = items[i];
                    html += '<tr>';
                    html += '   <td>'+ (i+1) +'</td>';
                    html += '   <td style="max-width: 150px;word-break:break-all;">'+ item.email +'</td>';
                    html += '   <td style="min-width: 100px;">'+ item.telephone +'</td>';
                    html += '   <td style="max-width: 150px;word-break:break-all;">'+ item.username +'</td>';
                    html += '   <td>'+ (item.orderType == '0' ? "免费试用" : "商务定制") +'</td>';
                    // html += '   <td>'+ item.consultPrice +'</td>';
                    html += '   <td  style="max-width: 55px;word-break:break-all;">'+ Order.getStatusDetails(item.status) +'</td>';
                    html += '   <td style="max-width: 80px;word-break:break-all;">'+ item.submit_time +'</td>';
                    if(per_audit !== -1) {
                        html += '   <td style="min-width: 50px;"><a href="javascript:Order.modalorderaudit('+ i +');" class="btn btn-sm btn-link  ';
                        if(item.status != 0) {
                            html += 'disabled';
                        }
                        html += '   ">审核</a></td>';
                    } 
                    html += '   <td>'                   
                    if(per_detail !== -1) {
                        html += '   <a href="javascript:Order.modalorderdetail('+ i +');">详情</a><i></i>';                       
                    }
                    if( per_update !== -1) {
                        html += '   <a href="javascript:Order.modalorderupdate('+ i +');">修改</a><i></i>';
                    }
                    if(per_remove !== -1) {
                        html += '   <a href="javascript:Order.modalorderdelete('+ i +');">删除</a></i>';
                    }
                    html += '</td></tr>';
                }
                table.html(html);
                var html_page = setPage(e.result.total, pageNo, '\'pager_orders\''); 
                $("#pager_orders > ul").html(html_page);   
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 获取订单状态
 */
Order.getStatusDetails = function(status) {
    var detail = '';
    switch(status) {
        case "0":
            detail = '待审核';
            break;
        case '1':
            detail = '商务定制审核通过';
            break;
        case '2':
            detail = '免费试用审核通过';
            break;
        case '3':
            detail = '不通过';
            break;
    }
    return detail;
}


/**
 * 显示订单详情
 * @param: index 选择的订单的索引
 */
Order.modalorderdetail = function(index) {
    var id = Order.data_orders[index].id;
    var param = {
        id: id
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.detail,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success && e.result) {
                var item = e.result;
                $("#span_detail_orderid").html(item.orderId);
                $("#span_detail_order_userid").html(item.userid);
                $("#span_detail_order_username").html(item.userName);
                // $("#span_detail_ordertotalprice").html(item.totalPrice);
                // $("#span_detail_orderconsultPrice").html(item.consultPrice);
                $("#span_detail_orderstatus").html( Order.getStatusDetails(item.status) );
                $("#span_detail_orderType").html(item.orderType == '0' ? "免费试用" : "商务定制");
                $("#span_detail_submitTime").html(item.submitTime);

                var html = '<ul class="list-group">';  
                var fathers = Order.getFathersFromOrderItems(item.orderItems);              
                var len = fathers.length;
                for(var i=len; i--; ) {
                    var dad = fathers[i];

                    html += '<li class="list-group-item" style="padding: 5px 10px;">';
                    html += '<span class="firstnodulename" style="font-weight: bold;">'+ dad.moduleName + '</span>';
                    if(dad.startUseTime !== "" && dad.type === 3) {
                        html += '&nbsp;&nbsp;<span class="span-s-12">价格：'+ dad.consultPrice +'</span>';
                        html += '&nbsp;&nbsp;<span class="span-s-12">限制：'+ dad.limit +'</span>';
                        html += '&nbsp;&nbsp;<span class="span-s-12">时间：'+ dad.startUseTime.substring(0, 10) +'至'+ dad.deadline.substring(0, 10) +'</span>';
                    }
                    html += '</li>';
                    var len_kids = dad.children.length;
                    for(var k=len_kids; k--; ) {
                        var kid = dad.children[k];
                        html += Order.getOrderItemDetailLiHtml(kid);
                    }
                }
                html += '</ul>';
                $("#div_detail_order_productlist").html(html);

                $("#span_detail_order_remarks").html(item.remarks ? item.remarks : "无");

                $("#modal_detail_order").modal("show");

            }
            else {
                showPopover(e.info);
            }
        }
    });
}

Order.getOrderItemDetailLiHtml = function(child) {
    var html = '<li class="list-group-item" style="padding: 5px 5px 5px 40px;">';
    html += '<span class="firstnodulename">'+ child.moduleName + '</span>';

    html += '&nbsp;&nbsp;<span class="span-s-12">价格：'+ child.consultPrice +'</span>';
    html += '&nbsp;&nbsp;<span class="span-s-12">限制：'+ child.limit +'</span>';  

    if(child.startUseTime !== "") {
        html += '&nbsp;&nbsp;<span class="span-s-12">时间：'+ child.startUseTime.substring(0, 10) +'至'+ child.deadline.substring(0, 10) +'</span>';
    }

    html += '</li>';
    return html;
}

/**
 * 审核订单
 * @param: index 选择的订单的索引
 */
Order.modalorderaudit = function(index) {    
    var item = Order.data_orders[index];
    $('#txt_audit_order').val('0');
    var id = item.id;
    if(item.orderType == '1') {
        $('#option_free').hide();
        $('#option_shangwu').show();
    }
    else {
        $('#option_free').show();
        $('#option_shangwu').hide();        
    }

    $("#txt_audit_orderid").val(id);
    $("#modal_audit_order").modal("show");
}

/**
 * 审核订单
 */
Order.audit = function() {
    var id = $("#txt_audit_orderid").val();
    var param = {
        id: id,
        status: $("#txt_audit_order").val(),
        auditRemarks: $("#txt_delete_order_auditRemarks").val()
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.audit,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                showPopover("审核成功");
                $("#modal_audit_order").modal('hide');
                Order.search();
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 删除订单
 * @param: index 选择的订单的索引
 */
Order.modalorderdelete = function(index) {    
    var id = Order.data_orders[index].id;
    $("#txt_delete_orderid").val(id);
    $("#span_delete_order").html("确定要删除该订单吗？删除后不可恢复");
    $("#modal_delete_order").modal("show");
}
/**
 * 删除订单
 */
Order.remove = function() {
    var id = $("#txt_delete_orderid").val();
    var param = {
        id: id
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.remove,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                showPopover("删除成功");
                $("#modal_delete_order").modal('hide');
                Order.search();
            }
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 * 重组cookie数据
 */
Order.getFathersFromOrderItems = function(products) {
    var fathers = [];
    var len = products.length;
    if(len === 0 ) {
        return fathers;
    }
    for(var i=len; i--; ) {
        var item = products[i];

        if( item.pModuleId === "") {
            item.children = [];
            fathers.push(item);
        }
    }

    var len_fathers = fathers.length;
    if(len_fathers === 0) {
        return fathers;
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
 * 显示修改订单的modal
 * @param: index 选择的订单的索引
 */
Order.modalorderupdate = function(index) {    
    var id = Order.data_orders[index].id;

    $('#txt_order_update_orderid').val(id);

    var param = {
        id: id
    };
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.detail,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success && e.result) {
                var r = e.result;
                $('#txt_order_update_date_start').val(r.startUserTime);
                $('#txt_order_update_date_end').val(r.deadline);
                $('#txt_order_update_price').val(r.consultPrice);
                $('#txt_order_update_remark').val(r.remarks);

                var html = '<ul class="list-group">';   

                var fathers = Order.getFathersFromOrderItems(r.orderItems);             
                var len = fathers.length;

                var per = user.privilegeCodes;
                var per_update_item = jQuery.inArray("ordermge_update", per);
                var per_remove_item = jQuery.inArray("ordermge_remove", per);

                for(var i=len; i--;) {
                    var dad = fathers[i];
                    /*
                    if(dad.type === 3) {
                        html += Order.getUpdateOrderItemsLiHtml(dad, per_update_item, per_remove_item, 'father');
                    }
                    else {
                        html += '<li class="list-group-item" style="padding: 10px 5px;" id="li_update_orderitem_'+ dad.id +'" moduleId="'+ dad.moduleId +'">';
                        html += '<span class="firstnodulename" style="font-weight:bold;">'+ dad.moduleName + '</span>';
                        html += '</li>';
                    }*/
                    html += Order.getUpdateOrderItemsLiHtml(dad, per_update_item, per_remove_item, 'father');
                                        
                    var kids = dad.children;
                    var len_kids = kids.length;
                    for(var k=len_kids; k--; ) {
                        var child = kids[k];
                        html += Order.getUpdateOrderItemsLiHtml(child, -1, -1, 'kid');
                    }
                }
                html += '</ul>';
                $("#div_update_order_products_list").html(html);

                $('#modal_update_order').modal('show');

                $('button[option="update-order-item"]').unbind('click').click(Order.updateOrderItem);
                $('button[option="delete-order-item"]').unbind('click').click(Order.deleteOrderItem);
            }   
            else {
                showPopover(e.info);
            }
        }
    });
}

Order.getUpdateOrderItemsLiHtml = function(child, per_update_item, per_remove_item, type) { 
    var h = '';

    if(child.type === 3 || type === "father") {
        h = '<li class="list-group-item" style="padding: 10px 5px;"  order-item-id="'+ child.id +'"  moduleId="'+ child.moduleId +'">';
        h += '<span class="firstnodulename" style="font-weight:bold;">'+ child.moduleName + '</span>';
    }
    else {
        h = '<li class="list-group-item" style="padding: 10px 5px 10px 40px;" order-item-id="'+ child.id +'" pModuleId="'+ child.pModuleId +'">';
        h += '<span class="firstnodulename">'+ child.moduleName + '</span>';
    }

    if(per_update_item !== -1) {
        /*
        h += '<span>折后价：</span>';
        h += '<input style="width: 80px; padding:0 6px;" id="txt_update_orderitem_price_'+ child.id +'" type="text" value="'+ child.consultPrice +'">';
        h += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';  
        h += '<span>限制：</span>';  
        h += '<input  style="width: 80px; padding:0 6px;" id="txt_update_orderitem_limit_'+ child.id +'" type="text" value="'+ child.limit +'">';
        */
        h += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>时间：</span>';  
        h += '<input type="text" class="text-date-start" name="start" id="txt_uporderitemdate_start_'+ child.id +'" value="'+ child.startUseTime.substring(0, 10) +'" style="background-color:#fff;cursor:pointer;width:80px;height: 22px;padding: 0 0;" readonly="true" onClick="WdatePicker()"/>';
        h += '   <span class="wordto" style="line-height: 22px;font-size: 12px;">至</span>';
        h += '   <input type="text" class="input-sm form-control" name="end" id="txt_uporderitemdate_end_'+ child.id +'" value="'+ child.deadline.substring(0, 10) +'" style="background-color:#fff;cursor:pointer;width:80px; height: 22px;padding: 0 0;" readonly="true" onClick="WdatePicker()"/>';
    }
    /*else{
        h += '<span>价格：</span><input id="txt_update_orderitem_price_'+ child.id +'" type="text" value="'+ child.consultPrice +'" readonly="true">';
        h += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';
        h += '<span>限制：</span><input id="txt_update_orderitem_limit_'+ child.id +'" type="text" value="'+ child.limit +'"  readonly="true">';  
    }*/

    h += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;';  
    var paramid = '\'' + child.id + '\'';
    if(per_update_item !== -1) {
        h += '<button class="btn btn-success btn-sm" type="button" option="update-order-item" order-item-id="'+ child.id +'" moduleId="'+ child.moduleId +'"  title="确认修改">保存</button>'; 
    }
    h += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'; 
    if( per_remove_item !== -1 ) {
        h += '<button class="btn btn-danger btn-sm" type="button" title="删除" option="delete-order-item"  order-item-id="'+ child.id +'">删除</button>';
    } 
    
    h += '</li>'; 
    return h;  
}

/**
 * 验证服务开始时间
 */
Order.verifyOrderUpdateDateStart = function(){
    var txt = $('#txt_order_update_date_start').val();
    var span = $('#span_tip_orderupdatedate');
    if(txt.length === 0 || txt === "") {
        span.html("请输入服务开始时间");
        return false;
    }
    span.html("");
    return true;
}

/**
 * 验证服务结束时间
 */
Order.verifyOrderUpdateDateEnd = function(){
    var txt = $('#txt_order_update_date_end').val();
    var span = $('#span_tip_orderupdatedate');
    if(txt.length === 0 || txt === "") {
        span.html("请输入服务结束时间");
        return false;
    }
    var start = $('#txt_order_update_date_start').val();
    if(start !== "") {        
        var startdate = new Date(start.replace(/-/g,"/"));
        var enddate = new Date(txt.replace(/-/g,"/")); 
        if(enddate <= startdate) {
            span.html("结束时间须大于开始时间");
            return false;
        }
    }
    span.html("");
    return true;
}

/**
 * 验证价格
 */
Order.verifyOrderUpdatePrice = function() {    
    var reg = /\d/;
    var txt = $("#txt_order_update_price").val();
    var span = $('#span_tip_orderupdate_price');
    if( !reg.test(txt) ) {
        span.html("价格格式不正确");
        return false;
    }
    span.html("");
    return true;
}

/**
 * 验证订单输入
 */
Order.verifyOrderUpdateInput = function() {
    var flag = Order.verifyOrderUpdatePrice();
    return flag;
}

/**
 * 修改订单属性
 */
Order.update = function() {
    var flag = Order.verifyOrderUpdateInput();
    if(!flag) {
        return flag;
    }

    var id = $('#txt_order_update_orderid').val();
    var param = {
        id: id,
        consultPrice: $("#txt_order_update_price").val(),
        remark: $("#txt_order_update_remark").val()
    };
    $.ajax({
        type: 'POST',
        async: true,
        url: urls.order.update,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                $('#modal_update_order').modal("hide");
                showPopover("修改成功");
            }   
            else {
                showPopover(e.info);
            }
        }
    });
}


/**
 * 修改订单项属性
 */
Order.updateOrderItem = function() {
    var me = $(this);
    var id = me.attr('order-item-id');
    var param = {
        id: id,
        useTime: $("#txt_uporderitemdate_start_" + id).val(),
        deadline: $("#txt_uporderitemdate_end_" + id).val()
    };
    var startdate = Number(param.useTime.replace(/-/g,""));
    var enddate = Number(param.deadline.replace(/-/g,"")); 
    if(enddate <= startdate) {
        showPopover("结束时间须晚于开始时间");
        return;
    }
    Order.updateOrderItemServer(param);
    var kids = $('li[pModuleId="'+ me.attr("moduleId") +'"]');

    kids.each(function(){
        var me = $(this);
        param.id = me.attr('order-item-id');
        Order.updateOrderItemServer(param);
    });
}
Order.updateOrderItemServer = function(param) {    
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.itemupdate,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                // $('#modal_update_order').modal("hide");
                showPopover("修改成功");
            }   
            else {
                showPopover(e.info);
            }
        }
    });
}

/**
 *  删除订单项
 */
Order.deleteOrderItem = function() {
    if ( confirm("确认要删除该订单项？删除后不可恢复") ) {
        var id = $(this).attr('order-item-id');
        var param = {
            id: id
        };
        Order.deleteOrderItemServer(param);
        /*
        var kids = $('li[pModuleId="'+ $(this).attr("moduleId") +'"]');        
        kids.each(function(){
            var me = $(this);
            param.id = me.attr('order-item-id');
            Order.deleteOrderItemServer(param);
        });*/
    }
}
Order.deleteOrderItemServer = function(param) {
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.order.itemremove,
        data: param,
        dataType: 'jsonp',
        success: function(e){
            if(e.success) {
                // $('#modal_update_order').modal("hide");
                showPopover("删除成功");
                var kid = $('li[order-item-id="'+ param.id +'"]');
                var parentId = kid.attr('moduleId');
                var kids = $("li[pModuleId='"+ parentId +"']");
                /*if(kids.length === 1) {
                    $("li[moduleId='"+ parentId +"']").remove();
                }*/
                kids.remove();
                kid.remove();
            }   
            else {
                showPopover(e.info);
            }
        }
    });
}


/**
 *  查询用户列表
 */
jQuery.PopupUserList = function(selector) {
    var elt = $(selector);
    var autoComplete,autoUl,autoLi;
    var h = '';
    h += '<div class="AutoComplete" style="background-color: #fff;border: 1px solid #ddd; " id="popup_userlist">';
    h += '  <div style="width: 100%;">';
    // h += '      <input type="text" placeholder="输入昵称、邮箱、手机号筛选" style="width: 200px; height: 26px;float:left;" value="">';
    h += '      <button type="button" id="popup_userlist_close" class="close"><span aria-hidden="true">&times;</span></button>';
    h += '  </div>';
    h += '  <ul class="AutoComplete_ul" id="popup_userlist_ul">';
    h += '  </ul>';
    h += '  <div id="pager_users_for_popup" style="padding-left: 10px; width: 100%; text-align: center;" page="0" data-target="userforpopup">';
    h += '      <ul class="pagination pagination-sm">';
    h += '      </ul>';
    h += '  </div>';
    h += '</div>';
    
    $('body').append(h);
    
    autoComplete = $('#popup_userlist');
    autoComplete.data('elt',elt);

    $('#popup_userlist_close').click(function(){
        autoComplete.hide();
    });
    autoComplete.css({
        position: 'absolute',
        zIndex: '9999999'
    });
    elt.keyup(function(e){      
        var me = $(this);
        me.removeAttr('userid');
        $('#span_tip_order_userid').html("<img src='images/wrong.png'></img>");
        if( e.keyCode === 13 ) {
            $('#pager_users_for_popup').attr('page', '0');
            Order.getUserListForPopup();  
            autoComplete.css({
                left: me.offset().left,
                top: me.offset().top + me.outerHeight(true) - 7
            }).show();            
        }

    }).focus(function(){
        autoComplete.data('elt',$(this));
    }).blur(function(){
        // autoComplete.hide();
    });
}

Order.getUserListForPopup = function() {
    var h = '';

    var pager = $('#pager_users_for_popup');    
    var pageNo = Number(pager.attr("page"));
    var param = {
        pageNo: pageNo,
        pageSize: 10
    };
    var txt = $('#txt_order_userid').val();
    if(txt) {
        param.info = txt;
    }

    $.ajax({
        type: 'GET',
        async: false,
        url: urls.user.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){            
            if(e.result && e.result.userInfos.length < 1) {
                showPopover("查询到0条用户数据");
                return h;
            }
            var userlist = e.result.userInfos;
            var len = userlist.length;
            for(var i=len; i--; ) {
                var u = userlist[i];
                u.username = u.username ? u.username : u.email;
                h += '<li email="'+ u.email +'" userid="'+ u.id +'" username="'+ u.username +'">'+ u.username  +'</li>';
            }
            $("#popup_userlist_ul").html(h);

            var autoLi = $('#popup_userlist_ul > li');
            autoLi.click(function(){             
                var me =  $(this);
                $('#txt_order_userid').val( me.attr('username') ).attr("userid", me.attr("userid"));
                $('#span_tip_order_userid').html("<img src='images/check.png'></img>");
                $('#popup_userlist').hide();
            });

            var html_page = setPage(e.result.totalCount, pageNo, '\'pager_users_for_popup\''); 
            $("#pager_users_for_popup > ul").html(html_page);  
            // return h;
        }
    });
    // return h;
}




