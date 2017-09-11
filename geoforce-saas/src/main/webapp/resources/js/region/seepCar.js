var seepCar_grid_selector = "#seepCar-grid-table";
var seepCar_pager_selector = "#seepCar-grid-pager";
var speedId;

function tableReload(id){
	var a=$("#seepCar-grid-table").children();
	if(a.length>0){
		speedId=id;
		seepReload(speedId);//实现刷新功能
	}else{
		speedId=id;
		seepCarInitGrid(speedId);//初次加载
	}
}
function seepReload(id){
	var url =basePath+"/com/supermap/getHasLinkedSeepCars?speedId="+speedId;
	jQuery(seepCar_grid_selector).jqGrid('setGridParam',{url:url}).trigger("reloadGrid")
	$("#seepCar-grid-table").trigger("reloadGrid");
}
function seepCarInitGrid(id){
	var colNames = ['车牌号','部门','id'];
	var colModel = [{index:'license',name:'license', width:200},
	                {index:'deptName',name:'deptName', width:100,hidden:true},
					{index:'id',name:'id', width:100,hidden:true}];//
	speedId=id;
	var url =basePath+"/com/supermap/getHasLinkedSeepCars?speedId="+id;
	seepCarLoadGrid(seepCar_grid_selector,url,"","",colNames,colModel);
}
function seepCarLoadGrid(obj, url,data ,title,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height: '286',
		width: 'auto',
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		caption: title,
		autowidth: true,
		multiselect: true,
        multiboxonly: true,
       
	});
	$(window).triggerHandler('resize.jqGrid');
}
function addSeepCarList(){
	 if(carZtree == null){
			bootbox.alert("没有得到车辆树对象");
			return ;
		}
		var nodes = carZtree.getCheckedNodes(true);
		if(nodes == null || nodes.length == 0){
			bootbox.alert("没有得到车辆对象");
			return;
		}
		var carObj,parentNode;
		
		var len = nodes.length;
		var obj = $("#seepCar-grid-table").jqGrid("getRowData");
		for(var i = 0;i < len;i++) {
				$("#seepCar-grid-table").jqGrid("delRowData", "123");	
				$("#seepCar-grid-table").jqGrid("delRowData", nodes[i].id);
			}  
		for(var i=0;i<=len;i++){
			if(!nodes[i].isParent){
				rowData = {license:nodes[i].name,deptName:nodes[i].name,id:nodes[i].id}
				 $('#seepCar-grid-table').jqGrid('addRowData', nodes[i].id, rowData);
			}
		}
}
function delrow(){
	var selectedRowIds = $("#seepCar-grid-table").jqGrid("getGridParam","selarrrow");
	if(selectedRowIds==123){
		return ;
	}
 	if(selectedRowIds==1)
 		return ;
 	   var tmp = [];
 	   var array = tmp.concat(selectedRowIds);
 	   var len = array.length;
 	   for(var i = 0;i < len;i++) {
 		   $("#seepCar-grid-table").jqGrid("delRowData", array[i]);  
 		}  
}
function saveSelectedCars(){
	var rows = jQuery(seepCar_grid_selector).jqGrid('getDataIDs');
	var seePcarIdArray = new Array();
	if(rows){
		var len = rows.length;
		for(var i=0;i<len;i++){
			seePcarIdArray.push(rows[i]);
		}
	}
	var seePcarIds = seePcarIdArray.join(",");
	//问题 得到车辆id集合；
	$.ajax({
		url : basePath+"/com/supermap/saveSelectedSeepCars?speedId="+speedId+"&carIds="+seePcarIds,
		type : "POST",
		success : function(obj) {
		if (obj.status == 1) {
			$('#seeplinkCarsDiv').modal('hide');
			bootbox.alert("操作成功!");
			 reload();
		} else {
			var msg = obj.info;
			$('#seeplinkCarsDiv').modal('hide');
			bootbox.alert(msg);
			 reload();
			return false;
			
		}
		}
	});
}
