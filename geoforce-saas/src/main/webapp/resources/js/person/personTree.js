var personZtree = null;//车辆树

/**
 * 创建车树
 * @return
 */
function createPersonTree(){
	//全部车辆树
	var url = basePath+"/com/supermap/personTree";
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
	$('#personTree').css("background-color", "transparent");
	personZtree = $.fn.zTree.init($('#personTree'), setting);
}







