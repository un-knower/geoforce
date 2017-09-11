$(function(){
	createCarTree();
})

/**
 * 发送消息
 * @return
 */
function sendMsg(){
	if(carZtree == null){
		bootbox.alert("未选择车辆");
		return ;
	}
	var nodes = carZtree.getCheckedNodes(true);
	if(nodes == null || nodes.length == 0){
		bootbox.alert("未选择车辆");
		return;
	}
	
	var carObj,parentNode;
	var carIds = "";
	var len = nodes.length;
	for(var i=0;i<len;i++){
		if(!nodes[i].isParent){
			carIds = carIds+nodes[i].id+",";
		}
	}
	$("#carIds").val(carIds);
	var title = $("#title").val();
	var content = $("#content").val();
	if(title == ''){
		bootbox.alert("请填写信息标题");
		return false;
	}
	if(content == ''){
		bootbox.alert("请填写信息内容");
		return false;
	}
	$.ajax({
		url : ctx+"/com/supermap/sendMsg?ram=" + Math.random(),
		type : "POST",
		data :$("#sendMsgForm").serialize(),
		success : function(obj) {
			if (obj.flag == "ok") {
				bootbox.alert("操作成功!");
				$("#title").val('');
				$("#content").val('');
			} else {
				
				bootbox.alert("操作失败!");
			}
		}
	});
	
}