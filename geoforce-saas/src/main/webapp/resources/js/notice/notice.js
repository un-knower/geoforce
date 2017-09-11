$(function(){
	createPersonTree();
})

/**
 * 发送消息
 * @return
 */
function sendMsg(){
	if(personZtree == null){
		bootbox.alert("未选择人员");
		return ;
	}
	var nodes = personZtree.getCheckedNodes(true);
	if(nodes == null || nodes.length == 0){
		bootbox.alert("未选择人员");
		return;
	}
	var personIds = "";
	var len = nodes.length;
	for(var i=0;i<len;i++){
		if(!nodes[i].isParent){
			personIds = personIds+nodes[i].id+",";
		}
	}
	$("#personIds").val(personIds);
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
		url : ctx+"/com/supermap/sendNotice?ram=" + Math.random(),
		type : "POST",
		data :$("#sendMsgForm").serialize(),
		success : function(obj) {
			if (obj.status == "1") {
				bootbox.alert("操作成功!");
				$("#title").val('');
				$("#content").val('');
			} else {
				
				bootbox.alert(obj.info);
			}
		}
	});
	
}