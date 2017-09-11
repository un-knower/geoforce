var tableDetail;
$(function(){
//	Page.initDeptsTree();
//	Page.initMenusTree();
	Page.initCss();
	$(window).resize(function(){
		Page.initCss();
	});
	tableDetail = TableDetail();
	
	$('input.input-during').click(function(){
		WdatePicker({
			dateFmt:'yyyy-MM-dd HH:mm:ss',
			maxDate:'%y-%M-#{%d} 23:59:59'
		});
	});
});

var Page = {};
Page.initCss = function() {
	var bodyHeight = getWindowHeight();
	$('.container-logs').css({
		'height': (bodyHeight - 70) + 'px'
	});
}

Page.checkCustomTime = function() {
	var start = $('input[name="start"]').val();
	var end = $('input[name="end"]').val();
	/*if(start === "") {
        Dituhui.showHint("请选择开始时间");
        return false;
	}
	if(end === "") {	
        Dituhui.showHint("请选择结束时间");
        return false;
	}*/
	
	if(start != "" && end != "") {		
	    var startdate = new Date(start);
	    var enddate = new Date(end); 
	    if(enddate <= startdate) {
	        Dituhui.showHint("结束时间须晚于开始时间");
	        return false;
	    }
	}
    Page.start = start;
    Page.end = end;
    return true;
}

/**
 * 初始化部门下拉树
 */
Page.initDeptsTree = function(){
	Dituhui.User.getDepts(function(data){
		ddTree.display('.list-department > .choice', data);
	}, function(){
		
	});
}

/**
 * 初始化模块下拉树
 */
Page.initMenusTree = function() {
	Dituhui.User.getMenu(function(data){
		ddTree.display('.list-menus > .choice', data);
	}, function(){		
	});
}

/**
 * 查询日志列表
 */
Page.search = function() {
	var param = {
		page: 1,
		rows: 10
	};
	var deptids = ddTree.getIdsFromCkb('.list-department > .choice');
	if(deptids != '') {
		param.deptids = deptids;
	}

	var moduleids = ddTree.getIdsFromCkb('.list-menus > .choice');
	if(moduleids != '') {
		param.moduleids = moduleids;
	}

	Dituhui.User.getOperationLogs(param, function(e){
		var data = e.rows;
		Table.display(data);
	}, function(){

	});
}


var Table = {
	operations: [
		{mark: '1', content: '进入'},
		{mark: '2', content: '新增数据'},
		{mark: '3', content: '修改数据'},
		{mark: '4', content: '删除数据'},
		{mark: '5', content: '登录系统'},
		{mark: '6', content: '注销'},
		{mark: '7', content: '修改个人资料'},
		{mark: '8', content: '导入数据'},
		{mark: '9', content: '合并'},
		{mark: '10', content: '拆分'},
		{mark: '11', content: '设置默认城市'},
		{mark: '12', content: '导出数据'}
	]
};
Table.display = function(data){
	Table.clear();
	var len = data ? data.length : 0, h='';
	for(var i=0; i<len; i++ ) {
		var item = data[i];
		if(!item.moduleid) {
			item.moduleid = "系统管理";
		}
		var content = Table.getOperationContent(item.operdesc);
		content += item.operdesc == '1' ? item.moduleid : '';
		var styleName = item.operdesc == '3' || item.operdesc == '4' ? 'red' : '';
		h += '<tr class="'+ styleName +'">';
		h += '	<td>'+ (i+1) +'</td>';
		h += '	<td>'+ item.username +'</td>';
		h += '	<td>'+ item.mobilephone +'</td>';
		h += '	<td>'+ item.email +'</td>';
		h += '	<td>'+ item.deptname +'</td>';
		h += '	<td>'+ item.moduleid +'</td>';
		h += '	<td>'+ content +'</td>';
		h += '	<td>'+ item.opertime +'</td>';
		h += '</tr>';
	}
	$('#table_logs > tbody').html(h);
}
Table.clear = function() {
	$('#table_logs > tbody').html('');
	Table.Pager.clear();
}
Table.getOperationContent = function(mark) {
	var data = Table.operations.concat();
	for(var i=data.length; i--; ) {
		var item = data[i];
		if(mark === item.mark) {
			return item.content;
		}
	}
	return '';
}

Table.Pager = {};
Table.Pager.clear = function() {
	$('.current-page').val('0');
	$('.total-page').html('0');
}
Table.Pager.display = function(current, total){
	
}