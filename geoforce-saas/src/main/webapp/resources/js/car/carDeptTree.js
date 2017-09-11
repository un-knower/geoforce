var carZtree = null;//车辆树
var deptTree = null;//部门树
/**
 * 创建车树
 * @return
 */
function createCarTree(){
	//全部车辆树
	var url = basePath+"/com/supermap/carTree";
//	if(license){
//		//检索出的车辆生成树
//		url = ctx+"/carOther/carAction!getCarSearchZTree.do";
//	}
	//全部车辆树
	var setting ={
		view: {
			dblClickExpand: false,
			showTitle: true
		},
		check:{
			enable: true,
			chkStyle:"checkbox"
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		async: {
			enable: true,
			url: url,
			autoParam:["id=treeId"]
			//otherParam:{"license":license}
		}
	};
	$('#carDeptTree').css("background-color", "transparent");
	carZtree = $.fn.zTree.init($('#carDeptTree'), setting);
}

function initComboDeptTree(objDiv){
	createDeptTree(objDiv);
	//将选择部门的select变成combo
//	$('#deptId').combo({
//        editable:false
//    }).combo('setText', deptName).combo('hidePanel');
//	$('#deptTreeDiv').appendTo($('#deptId').combo('panel'));
}

/**
 * 创建部门树
 * @param objDiv
 * @return
 */
function createDeptTree(objDiv){
	var url = basePath+"/com/supermap/deptTree";

	var setting ={
		view: {
			dblClickExpand: false
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		async: {
			enable: true,
			url: url,
			autoParam:["id=treeId"]
		},
		callback: {
			onAsyncSuccess: asyncSuccessBack,
			onClick: onClick
		}
	};
	//$('#deptTree').css("background-color", "");
	deptTree = $.fn.zTree.init(objDiv, setting);
}
/**
 * 选中一个部门事件
 * @param event
 * @param treeId
 * @param treeNode
 * @return
 */
function selectDept(event, treeId, treeNode){
	alert("sss");
	if(treeNode){
		var deptId = treeNode.id;
		var deptCode = treeNode.code;
		var deptName = treeNode.name;
		$("#parentId").val(deptId)
		$('#deptparentName').combo('setValue', deptCode).combo('setText', deptName).combo('hidePanel');
	}
}
/**
 * 部门树加载成功后选中当前节点
 * @param event
 * @param treeId
 * @param treeNode
 * @param msg
 * @return
 */
function asyncSuccessBack(event,treeId,treeNode,msg){
	var deptCode = $("#deptCode").val();
	if(deptCode){
		var node = deptTree.getNodeByParam("code", deptCode, null);
		if(node){
			deptTree.selectNode(node);
		}
	}else{
		var nodes = deptTree.getNodes();
		if(nodes.length>0){
			deptTree.selectNode(nodes[0]);
		}
	}
}

