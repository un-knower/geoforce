var ztree_setting = {	
	async : {
		enable : true,
		url : urls.server + "/user/getChildUsers",
		autoParam : ["parentId"],
		// dataType: "text",//默认text
		// type:"get",//默认post
		dataFilter : filter
	// 异步返回后经过Filter
	},
	callback : {
		// beforeAsync: zTreeBeforeAsync, // 异步加载事件之前得到相应信息
		onAsyncSuccess : zTreeOnAsyncSuccess,// 异步加载成功的fun
		asyncError : zTreeOnAsyncError, // 加载错误的fun
		beforeClick : beforeClick,
		onClick: selectOtherUser
	}
};
// treeId是treeDemo
function filter(treeId, parentNode, childNodes) {
	if (!childNodes)
		return null;
	for ( var i = 0, l = childNodes.length; i < l; i++) {
		var item = childNodes[i];
		childNodes[i].name = item.name.replace('', '');
		childNodes[i].parentId = item.id;
	}
	return childNodes;
}
function beforeClick(treeId, treeNode) {
	if(treeNode.isParent == true) {
		return false;
	}
	return true;
}
function selectOtherUser(e, treeId, treeNode) {
	$('.span-selected-username').html("已选择用户：<b>" + treeNode.name + "</b>");
}
function zTreeOnAsyncError(event, treeId, treeNode) {
	alert("异步加载失败!");
}
function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	
}
$(document).ready(function() {
	$.fn.zTree.init($("#tree_usebelonging"), ztree_setting);
});

