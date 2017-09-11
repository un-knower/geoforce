/*
 * 下拉树 多选框
 */
var ddTree = {};
/*
 * 展示多选框列表
 */
ddTree.display = function(selector, datas){
	ddTree.clear(selector);
	var data = ddTree.getNodes(datas);
	var h  = '<ul class="branch">';
	 	h += '	<li><input class="select-all left-20" id="'+ selector +'-all" type="checkbox"><label for="'+ selector +'-all">全选</label></li>'
	for(var i=data.length; i--; ) {
		var item = data[i];
		h += '<li>';
		var len_kids = item.kids.length;
		if(len_kids > 0) {
			h += '<span class="glyphicon glyphicon-minus" title="收起"></span>';
		}
		h += '	<input class="select-branch '+ (len_kids > 0 ? '' : 'left-20') +'" id="f'+ selector + i +'" type="checkbox" value="'+ item.id +'">';
		h += '	<label for="f'+ selector + i +'">'+ item.name +'</label>';
		for(var k=item.kids.length; k--; ) {
			var kid = item.kids[k];
			h += '<ul class="node">';
			h += '	<li>';
			h += '		<input class="select-node" id="k'+ selector + k +'" type="checkbox" value="'+ kid.id +'">';
			h += '		<label for="k'+ selector + k +'">'+ kid.name +'</label>';
			h += '	<li>';
			h += '</ul>';
		}
		h += '</li>';
	}
	h += '</ul>';
	$(selector).html(h);
	ddTree.bindClicks(selector);
}
ddTree.getNode = function(item) {
	
}

/*
 * 获取树的节点
 */
ddTree.getNodes = function(data) {
	var parents = [];
	var len = data ? data.length : 0;
	if(len == 0) {
		return parents;
	}
	for(var i=len; i--; ) {
		var item = data[i];
		data[i].parentId = item.parentId ? item.parentId : item.pid;
		data[i].name = item.name ? item.name : item.menuName;
		item.kids = [];
		if(!item.parentId) {
			parents.push(item);
		}
		data.splice(i, 1);
	}

	parents = ddTree.getDataBranches(data, parents);
	return parents;
}
ddTree.getDataBranches = function(data, parents) {
	for(var k=parents.length; k--;) {
		var father = parents[k];
		ddTree.getNodesKids(data, parents);
	}
	return parents;
}
ddTree.getNodesKids = function(data, father) {
	var len = data.length;
	for(i=len; i--; ) {
		var item = data[i];
		if(item.parentId === father.id) {
			father.kids.push(item);
			data.splice(i, 1);
		}
	}
}

/*
 * 绑定点击事件
 */
ddTree.bindClicks = function(selector) {
	$(selector).click(function(event){
		event = event||window.event;    
    	event.stopPropagation();
	});
	$(document).click(function(e){  
	    $(selector).slideUp('fast');
	    $(selector).parent('div').find(' > span').removeClass('rotate-180'); 
	});    
	//全选，取消
	$(selector + ' input.select-all').unbind('click').click(function(){
		var me = $(this);
		var checked = me.prop('checked');
		var ul = me.parent('li').parent('ul');
		ul.find('input[type="checkbox"]').prop('checked', checked);
	});
	//展开，收起二级菜单
	$(selector + ' span.glyphicon').unbind('click').click(function(){
		var me = $(this);
		if(me.hasClass('glyphicon-minus')) {
			me.nextAll('ul.node').slideUp('fast');
			me.removeClass('glyphicon-minus').addClass("glyphicon-plus").attr('title', "展开");
		}
		else {
			me.nextAll('ul.node').slideDown('fast');
			me.removeClass('glyphicon-plus').addClass("glyphicon-minus").attr('title', "展开");			
		}
	});
	//点击父节点
	$(selector + ' input.select-branch').unbind('click').click(function(){
		var me = $(this);
		var checked = me.prop('checked');
		var ul = me.nextAll('ul.node');
		ul.find('input[type="checkbox"]').prop('checked', checked);
		if(!checked) {
			$(selector + ' input.select-all').prop('checked', checked);
		}
		else {
			if( $(selector + ' input:not(.select-all)').prop('checked') == true ) {
				$(selector + ' input.select-all').prop('checked', checked);
			}
		}
	});
	//点击子节点
	$(selector + ' input.select-node').unbind('click').click(function(){
		var me = $(this);
		var checked = me.prop('checked');
		if(!checked) {
			me.parents('li').find('input.select-branch').prop('checked', checked);
			$(selector + ' input.select-all').prop('checked', checked);
		}
		else {
			if(me.parent('li').parent('ul').find('input').prop('checked') == true) {
				me.parents('li').find('input.select-branch').prop('checked', checked);
			}
			if( $(selector + ' input:not(.select-all)').prop('checked') == true ) {
				$(selector + ' input.select-all').prop('checked', checked);
			}
		}
	});

	//展开、收起下拉多选框
	$(selector).parent('div').find(' > .inputs,  > span').click(function(event) {
		$(selector).slideToggle('fast');
		var span = $(selector).parent('div').find(' > span');
		if(span.hasClass('rotate-180')) {
			span.removeClass('rotate-180');
		}
		else {
			span.addClass('rotate-180');
		}

		if(selector.match('list-department')) {
			$('.list-menus > .choice').slideUp('fast');
			$('.list-menus > span').removeClass('rotate-180');
		}
		else if(selector.match('list-menus')) {
			$('.list-department > .choice').slideUp('fast');
			$('.list-department > span').removeClass('rotate-180');
		}
		event = event||window.event;    
    	event.stopPropagation();
	});


	//收起子菜单
	$(selector + ' span.glyphicon').click();
	//全选
	$(selector + ' input.select-all').click();
}
/*
 * 清空树
 */
ddTree.clear = function(selector) {

}
/*
 * 获取选中的内容
 */
ddTree.getIdsFromCkb = function(selector) {
	var ids = "";
	if( $(selector + " .select-all").prop('checked') == true ) {
		return ids;
	}
	$(selector + ' input.select-branch:checked, ' + selector + ' input.select-node:checked').each(function(){
		var me = $(this);
		ids += me.val() + ",";
	});
	ids = ids !== "" ? ids.substring(0, ids.length-1) : "";
	return ids;
}