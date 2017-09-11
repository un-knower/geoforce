/*
 * 车辆模块
 */
var Cars = {
	/*
	 * 保存车辆类型数据
	 */
	data_type: [],
    /*
     * 保存查询的车辆
     */
	data_cars: [],
    /*
     * 保存选择的车辆
     */
	selected_cars_ids: '',
	selected_cars_license: '',
	selected_cars: []
};
/*
 * 初始化
 */
Cars.init = function() {
	Cars.search();

	if( Cars.data_type.length === 0 ) {
		Cars.searchType();
	}
}

/*
 * 查询车辆
 */
Cars.search = function() {	
    SuperMap.Egisp.showMask();
    var pager = $("#pager_cars");
    var pageIndex = Number(pager.attr('page')) + 1;
    var param = {
    	curPage: pageIndex
    };
    var license = $('#txt_license').val();
    if(license != "") {
    	param.lisence = license;
    }    
	var sim = $('#txt_sim').val();
	if(sim != "") {
		param.sim  = sim;
	}	
	var type = $('.select-car-type').val();
	if(type != null && type != "-1") {
		param.type = type;
	}
    $.ajax({
        type: 'GET',
        async: true,
        url: urls.server + "/pointService/queryCars?",
        data: param,
        dataType: 'json',
        success: function(e){
            SuperMap.Egisp.hideMask();
        	if(e && e.result && e.result.totalNum > 0) {
        		Cars.data_cars = e;
        		Cars.showCars();
        		return;
        	}
            SuperMap.Egisp.showPopover("查询到0条车辆数据");
            Cars.clear();
        },
        error: function(){
            SuperMap.Egisp.hideMask();
        	SuperMap.Egisp.showPopover("查询到0条车辆数据");
            Cars.clear();
        }
    });
}

/*
 * 显示车辆到表格中
 */
Cars.showCars = function(){
	var cars = Cars.data_cars.result.result;
	var len = cars.length;
	var h = '';
	for(var i=0; i<len; i++) {
		var car = cars[i];
		h += '<tr>';
		var flag = Cars.getSelected(car.id);
		h += '<td><input type="checkbox" class="select-car" data-id="'+ car.id +'" data-license="'+ car.license +'"';
		h += flag ? ' checked="true"' : '';
		h += ' ></td>';
		h += '<td>'+ (i+1) +'</td>';
		h += '<td>'+ car.license +'</td>';
		h += '<td>'+ car.terminalCode +'</td>';
		h += '<td>'+ car.sim +'</td>';
		h += '<td>'+ car.typeName +'</td>';
		h += '<td>'+ car.driverName +'</td>';
		h += '</tr>';
	}
	$('.table-cars tbody').html(h);
	$('.data-cars').fadeIn('fast');

    var html_page = SuperMap.Egisp.setPage(Cars.data_cars.result.totalNum, Cars.data_cars.result.currentPageNum-1, '\'pager_cars\''); 
    $("#pager_cars > ul").html(html_page);   
    $("#pager_cars").show();     
    $('.select-car').unbind('click').click(function(){
    	var me = $(this);
    	var bool = me.prop('checked');
    	if(bool) {
			Cars.selectCar( me.attr('data-id'), me.attr('data-license') );
    	}
    	else {
    		Cars.removeSelectCar( me.attr('data-id') );
    		$('.select-all-car').prop('checked', false);
    	}
    });
}

/*
 * 车辆全选
 */
Cars.selectAll = function() {
    var bool = $(this).prop('checked');
    if(bool) {
        $('.select-car').each(function(){
            var me = $(this);
            var flag = me.prop('checked');
            if(!flag) {
                me.prop('checked', true);
                Cars.selectCar( me.attr('data-id'), me.attr('data-license') );
            }
        });
    }
    else {
        $('.select-car').each(function(){
            var me = $(this);
            var flag = me.prop('checked');
            if(flag) {
                me.prop('checked', false);
                Cars.removeSelectCar(me.attr('data-id'));
            }
        });
    }
}

Cars.selectCar = function(id, license) {
	var cars = Cars.selected_cars.concat();
	var len = cars.length;
	if(len === 0) {
		Cars.selected_cars.push({
			id: id,
			license: license
		});
		return false;
	}
	for( var i=len; i--; ) {
		var car = cars[i];
		if( car.id === id ) {
			return false;
		}
	}
	Cars.selected_cars.push({
		id: id,
		license: license
	});
}
Cars.removeSelectCar = function(id) {
	var cars = Cars.selected_cars.concat();
	var len = cars.length;
	if(len === 0) {
		return false;
	}
	for( var i=len; i--; ) {
		var car = cars[i];
		if( car.id === id ) {
			cars = cars.splice(i);
			break;
		}
	}
	Cars.selected_cars = cars.concat();
}
Cars.getSelected = function(id) {
	var cars = Cars.selected_cars.concat();
	var len = cars.length;
	if(len === 0) {
		return false;
	}
	for( var i=len; i--; ) {
		var car = cars[i];
		if( car.id === id ) {
			return true;
		}
	}
	return false;
}

/*
 * 查询车辆类型
 */
Cars.searchType = function() {
	var param = {type: 19};
	$.ajax({
        type: 'GET',
        async: true,
        url: urls.server + "/pointService/queryCarTypes?",
        data: param,
        dataType: 'json',
        success: function(e){
            if(e.isSuccess && e.result) {
                var re = [];
                if( e.result.length === 0 ) {
					SuperMap.Egisp.showPopover("未查询到车辆类型数据");
                }
                else {
                    Cars.data_type = e.result;
                    Cars.showDataType();
                }
            }
            else {                
				SuperMap.Egisp.showPopover("获取车辆类型失败");
            }
        },
        error: function(){
			SuperMap.Egisp.showPopover("获取车辆类型失败");   
        }
    });
}

/*
 * 显示车辆类型
 */
Cars.showDataType = function() {
	var items = Cars.data_type.concat();
	if(items.length === 0) {
		return;
	}
	var len = items.length;
	var h = '<option value="-1" selected="true">全部</option>';
	for(var i=0; i<len; i++) {
		var item = items[i];
		h += '<option value="'+ item.code +'">'+ item.name +'</option>';
	};
	$('.select-car-type').html(h);
}
Cars.clear = function(){
	$('#pager_cars').attr('page', '1').hide();
	$('.table-cars tbody').html('');
	Cars.selected_cars = [];
	Cars.selected_cars_license = '';
	Cars.selected_cars_ids = '';
    $('.select-all-car').prop('checked', false);
}
Cars.sure = function() {
	var ids = "";
	var numbers = "";
	var cars = Cars.selected_cars.concat();
	for(var i=cars.length; i--; ) {
		var item = cars[i];
		if(ids !== "") {
			ids += "_";
			numbers += ",";
		}
		ids += item.id;
		numbers += item.license;
	}
	selected_cars_ids = ids;
	selected_cars_numbers = numbers;
	$('#txt_car_license').val(selected_cars_numbers);
	$('#txt_car_ids').val(selected_cars_ids);	
	$('.data-cars').fadeOut('fast');
}
Cars.selectAllCar = function() {
	$('.select-car').each(function(){
		var me = $(this);
		var flag = me.prop('checked');
		if(!flag) {
			me.prop('checked', true);
			Cars.selectCar( me.attr('data-id'), me.attr('data-license') );
		}
	});
	$('.select-all-car').prop('checked', true);
}
/*Cars.selectAllCars = function() {
    var bool = $(this).prop('checked');
    if(bool) {
        $('.select-car').each(function(){
            var me = $(this);
            var flag = me.prop('checked');
            if(!flag) {
                me.prop('checked', true);
                Cars.selectCar( me.attr('data-id'), me.attr('data-license') );
            }
        });
    }
    else {
        $('.select-car').each(function(){
            var me = $(this);
            var flag = me.prop('checked');
            if(flag) {
                me.prop('checked', false);
                Cars.removeSelectCar(me.attr('data-id'));
            }
        });
    }
}*/