
//验证字符串中是否有空格
var regBlank = /(^ )|( $)/;


/**
 * 产品管理
 */
var Product = {
    /**
     * 保存查询的产品
     */
	products: []
}


/**
 * 验证产品名称输入
 */
Product.verifyProductName = function() {
	var txt = $('#txt_product_name').val();
	var span = $('#span_tip_productname');

    var regBlank = /(^ )|( $)/;
    if(txt == "") {
		span.html("请输入产品名称");
		return false;
	}
	else if(txt == "null") {
		span.html("产品名称不能为\"null\"");
		return false;
	}
	else if( regBlank.test(txt) || txt.indexOf(" ") > 0 ){
		span.html("产品名称不能包空格");
		return false;
	}

	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/

	span.html("");
	return true;
}

/**
 * 验证产品价格输入
 */
Product.verifyProductPrice = function() {
	var txt = $('#txt_product_price').val();
	var span = $('#span_tip_productprice');

	var reg = new RegExp("^[0-9]*$");

	if(txt == "") {
		span.html("请输入产品价格");
		return false;
	}
	if(reg.test(txt)) {
		span.html("");
		return true;
	}	
	span.html("产品价格只能包含数字");
	return false;
}

/**
 * 验证产品url输入
 */
Product.verifyProductUrl = function() {
	var txt = $('#txt_product_url').val();
	var span = $('#span_tip_producturl');

	if(txt == "") {
		span.html("请输入产品url");
		return false;
	}
	else if( regBlank.test(txt) || txt.indexOf(" ") > 0 ){
		span.html("产品url不能包空格");
		return false;
	}
	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/
	
	span.html("");
	return true;	
}

/**
 * 验证产品商务电话输入
 */
Product.verifyProductCommercialTel = function() {	
	var txt = $('#txt_product_commercial_tel').val();
	var span = $('#span_tip_commercial_tel');

	if(txt == "") {
		span.html("请输入商务电话");
		return false;
	}
	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/
	
	span.html("");
	return true;
}

/**
 * 验证产品图标名称输入
 */
Product.verifyProductIconUrl = function() {	
	var txt = $('#txt_product_icon_url').val();
	var span = $('#span_tip_product_icon_url');

	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/
	if(txt === "") {
		span.html("请输入产品图标样式名称");
		return false;
	}
	else{
		span.html("");
		return true;
	}
}
/**
 * 验证产品代码输入
 */
Product.verifyProductCode = function() {	
	var txt = $('#txt_product_code').val();
	var span = $('#span_tip_productcode');

	/*var regSERule = /^(?!_)(?!.*?_$)^(?!%)(?!.*?%$)/;
	if(!regSERule.test(txt)){
		span.html("不能以百分号或下划线开头结尾");
		return false;
	}*/
	if(txt === "") {
		span.html("请输入产品代码");
		return false;
	}
	else {
		span.html("");
		return true;
	}
}

/**
 * 验证产品输入
 */
Product.verfyProductInput = function(type) {
	var flag = Product.verifyProductName();
	if( !flag  && !(typeof(type) !=="undefined" && type === "update")  ) {
		return flag;
	}
	flag = Product.verifyProductUrl();
	if(!flag) {
		return flag;
	}
	/*flag = verifyProductCode();
	if(!flag) {
		return flag;
	}	*/
	flag = Product.verifyProductPrice();
	if(!flag) {
		return flag;
	}
	flag = Product.verifyProductIconUrl();
	if(!flag) {
		return flag;
	}	
	return true;
}

/**
 * 添加产品的模态框
 */
Product.modalAddProduct = function() {
	Product.resetProductInput();
	$("#model_label_add_product").html("新增产品");
	Product.initFatherCheckBox();

	$("#modal_add_product").attr("current", "0").modal("show");
	$("#div_product_name").css({"display": "block"});

}
/**
 * 初始化PID
 */
Product.initFatherCheckBox = function() {
	var param = {
		pageNo: -1
	};
	$.ajax({
        type: 'GET',
        async: false,
        url: urls.product.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if( e && e.success ) {
        		var items = e.result.items;
        		var len = items.length;
        		if(len === 0) {
        			return;
        		}
        		var h = '';
        		for(var i=0; i<len; i++ ) {
        			if(i === 0) {
        				h += '<option value="" selected="true">顶级产品</option>';
        			}
        			var item = items[i];
        			if(item.pid === "") {
        				h += '<option value="'+ item.id +'">'+ item.name +'</option>';
        			}
        		}
        		$("#txt_product_pid").html(h);

        	}
        }
    });
}

/**
 * 添加产品
 */
Product.add = function() {
	var flag = Product.verfyProductInput();
	if(flag === false) {
		return;
	}
	$("#modal_add_product").attr("current", "0");

	var param = {
		name: $('#txt_product_name').val(),
		pid: $('#txt_product_pid').val(),
		price: $('#txt_product_price').val(),
		status: $('#txt_product_status').val(),
		url: $('#txt_product_url').val(),
		useLimit: $('#txt_product_limit').val(),
		ref_url: $('#txt_product_helpurl').val(),
		remarks: $('#txt_product_remark').val(),
		code: $('#txt_product_code').val(),
		icon_url: $('#txt_product_icon_url').val(),
		type: $('#txt_product_kind').val()
	};

	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.add,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		showPopover("添加成功");
        		$("#modal_add_product").modal("hide");
        		Product.search();
        	}
        	else {
        		showPopover(e.info);
        	}

        }
    });
}

/**
 * 清空添加产品时的输入
 */
Product.resetProductInput = function() {
	$('#txt_product_name').val('');
	$('#span_tip_productname').val('');

	$('#txt_product_pid').val('');

	$('#txt_product_price').val('');
	$('#span_tip_productprice').val('');

	$('#txt_product_status').val('0');

	$('#txt_product_url').val('');
	$('#span_tip_producturl').val('');

	$('#txt_product_commercial_tel').val('');
	$('#span_tip_commercial_tel').val('');

	$('#txt_product_limit').val('');
	$('#txt_product_helpurl').val('');
	$('#txt_product_remark').val('');
	$('#txt_product_support_tel').val('');
}

/**
 * 查询产品
 */
Product.search = function() {
	Product.products = [];

	var pageNo = Number($("#pager_products").attr("page"));
	var param = {
		pageNo: pageNo,
		pageSize: pageSize
	};

	var keyword = $('#txt_search_product').val();
	if( keyword.length > 0 ) {
		param.name = keyword;
	}
	var status = $('#select_search_product_status').val();
	if(status !== "-1") {
		param.status = status;
	}

	var table = $("#table_products");
	table.html('');

	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.search,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e && e.success ) {
        		if(e.result && e.result.items.length < 1) {
        			showPopover("查询到0条产品数据");
        			return;
        		}
        		var records = e.result.items;
        		Product.products = records;


        		var per = user.privilegeCodes;
        		var per_detail = jQuery.inArray("productmge_detail", per);
        		var per_update = jQuery.inArray("productmge_update", per);
        		var per_remove = jQuery.inArray("productmge_remove", per);

        		var html = '';
        		var len = records.length;
        		for(var i=0; i<len; i++) {
        			var item = records[i];

        			html += '<tr>';
        			html += '	<td>'+ (i+1) +'</td>';
        			// html += '	<td>'+ item.id +'</td>';
        			html += '	<td>'+ item.name +'</td>';
        			html += '	<td>'+ item.price +'</td>';
        			html += '	<td>'+ item.limit +'</td>';
        			html += '	<td>'+ item.pid +'</td>';
        			html += '	<td>'+ item.status +'</td>';
        			if( per_detail !== -1 ) {
        				html += '	<td><a href="javascript:Product.modalproductdetail('+ i +');">详情</a></td>';
        			}
        			if( per_update !== -1 ) {
        				html += '	<td><a href="javascript:Product.modalproductupdate('+ i +');">修改</a></td>';
        			}
        			if( per_remove !== -1 ) {
	        			html += '	<td><a href="javascript:Product.modalproductdelete('+ i +');">删除</a></td>';
	        		}
        			html += '</tr>';
        		}
        		table.html(html);
        		var html_page = setPage(e.result.total, pageNo, '\'pager_products\''); 
        		$("#pager_products > ul").html(html_page);   
        	}
        	else {
        		showPopover(e.info);
        	}
        }	
    });
}

/**
 * 显示修改产品的模态框
 * @param: index 选择的产品的索引
 */
Product.modalproductupdate = function(index) {
	Product.initFatherCheckBox();
    var id = Product.products[index].id;
	var param = {
		id: id
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.detail,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		var item = e.result;
        		var modal = $("#modal_add_product");
				modal.attr("current", "1");
				
				$('#txt_product_id').val(item.id);

				$('#txt_product_pid').val(item.pid ? item.pid : '');
				$('#txt_product_price').val(item.price);

				// var status = map_status_product_key.get(item.status);
				$('#txt_product_status').val(item.status);

				$('#txt_product_url').val(item.url);
				$('#txt_product_code').val(item.code ? item.code : "");
				$('#txt_product_limit').val(item.useLimit ? item.useLimit : '');
				$('#txt_product_helpurl').val(item.ref_url ? item.ref_url : '');
				$('#txt_product_remark').val(item.remarks ? item.remarks : '');
				$('#txt_product_icon_url').val(item.icon_url ? item.icon_url : '');
				$('#txt_product_kind').val(item.type ? item.type : "1");

				$("#model_label_add_product").html("修改产品：" + item.name);
				$("#div_product_name").css({"display": "none"});

			    modal.modal("show");
        	}
        	else {
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 修改产品
 * @param: index 选择的产品的索引
 */
Product.update = function() {	
	var flag = Product.verfyProductInput("update");

	if(!flag) {
		return;
	}

	var param = {
		id: $('#txt_product_id').val(),
		pid: $('#txt_product_pid').val(),
		price: $('#txt_product_price').val(),
		status: $('#txt_product_status').val(),
		url: $('#txt_product_url').val(),
		icon_url: $('#txt_product_icon_url').val(),
		useLimit: $('#txt_product_limit').val(),
		ref_url: $('#txt_product_helpurl').val(),
		remarks: $('#txt_product_remark').val(),
		code: $('#txt_product_code').val(),
		type: $('#txt_product_kind').val()
	};

	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.update,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		showPopover("修改成功");
        		$("#modal_add_product").modal("hide");
        		Product.search();
        	}
        	else {
        		showPopover(e.info);
        	}

        }
    });
}


/**
 * 显示产品详情的模态框
 * @param: index 选择的产品的索引
 */
Product.modalproductdetail = function(index){
	var id = Product.products[index].id;
	var param = {
		id: id
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.detail,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
        		var item = e.result;
        		$('#span_detail_productid').html(item.id);
        		$('#span_detail_productname').html(item.name);
        		$('#span_detail_productstatus').html(item.status);
        		$('#span_detail_productprice').html(item.price);
        		$('#span_detail_producturl').html(item.url ? item.url : '无');
        		$('#span_detail_productlimit').html(item.useLimit ? item.useLimit : "无");
        		$('#span_detail_productcode').html(item.code ? item.code : "无");
        		$('#span_detail_product_ref_rul').html(item.ref_url ? item.ref_url : "无");
        		$('#span_detail_productl_icon_url').html(item.icon_url ? item.icon_url : "无");
        		$('#span_detail_product_remarks').html(item.remarks ? item.remarks : "无");


        		$('#modal_detail_product').modal({
					keyboard: false
				});
        	}
        	else {
        		showPopover(e.info);
        	}
        }
    });
}

/**
 * 显示删除产品的模态框
 * @param: index 选择的产品的索引
 */
Product.modalproductdelete = function(index){
	var item = Product.products[index];
	$("#txt_delete_productid").val(item.id);
	$("#span_delete_product").html("确定要删除产品\"" + item.name + "\"吗？删除后不可恢复。");
	$('#modal_delete_product').modal("show");
}	

/**
 * 删除产品
 */
Product.remove = function(){
	var id = $("#txt_delete_productid").val();

	var param = {
		id: id
	};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.product.remove,
        data: param,
        dataType: 'jsonp',
        success: function(e){
        	if(e.success) {
				$('#modal_delete_product').modal("hide");
				showPopover("删除成功");
				Product.search();
        	}	
        	else {
        		showPopover(e.info);
        	}
        }
    });
}	