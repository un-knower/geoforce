var regionCar_grid_selector = "#regionCar-grid-table";
var regionCar_pager_selector = "#regionCar-grid-table";
var regionId;
var alarmTypeCode;
function regionTableReload(id,typeCode){
	var a=$("#regionCar-grid-table").children();
	regionId=id;
	alarmTypeCode=typeCode;
	if(a.length>0){
		regionCarreload(regionId);//实现刷新功能
	}else{
		regionCarInitGrid(regionId);//初次加载
	}
}
function regionCarreload(regionId) {
	var url =basePath+"/com/supermap/getHasLinkedRegiovCars?region="+regionId;
	jQuery(regionCar_grid_selector).jqGrid('setGridParam',{url:url}).trigger("reloadGrid")
	$("#regionCar-grid-table").trigger("reloadGrid");
}
function regionCarInitGrid(id){
	var colNames = ['车牌号','部门','id'];
	var colModel = [{index:'license',name:'license', width:200},
	                {index:'deptName',name:'deptName', width:100,hidden:true},
					{index:'id',name:'id', width:100,hidden:true}];//
	var url =basePath+"/com/supermap/getHasLinkedRegiovCars?region="+regionId;
	regionCarLoadGrid(regionCar_grid_selector,url,"","",colNames,colModel);
}
function regionCarLoadGrid(obj, url,data ,title,colNames,colModel){
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
        multiboxonly: true
	});
	$(window).triggerHandler('resize.jqGrid');
}
function addCarList(){
	   if(carZtree == null){
			bootbox.alert("请勾选车辆");
			return ;
		}
		var nodes = carZtree.getCheckedNodes(true);
		if(nodes == null || nodes.length == 0){
			bootbox.alert("请勾选车辆");
			return;
		}
		var carObj,parentNode;
		var len = nodes.length;
		//得到所有行id
		var obj = $("#regionCar-grid-table").jqGrid("getRowData");
			for(var i = 0;i < len;i++) {
				$("#regionCar-grid-table").jqGrid("delRowData", "123");
			  $("#regionCar-grid-table").jqGrid("delRowData", nodes[i].id);
			}  
		
		for(var i=0;i<=len;i++){	 
			if(!nodes[i].isParent){
				rowData = {license:nodes[i].name,deptName:nodes[i].name,id:nodes[i].id}
				 $('#regionCar-grid-table').jqGrid('addRowData', nodes[i].id, rowData);
			}
		}
}
function delrow(){
	 	var selectedRowIds = $("#regionCar-grid-table").jqGrid("getGridParam","selarrrow"); 
	 	if(selectedRowIds==1)
	 		return ;
		   var tmp = [];
		   var array = tmp.concat(selectedRowIds);
		   var len = array.length;
		   if(selectedRowIds==123){
			   return null;
		   }
		   for(var i = 0;i < len;i++) {
			   $("#regionCar-grid-table").jqGrid("delRowData", array[i]);  
			}  
	 		
}
function saveSelectedCars(){
	
	 var rows = jQuery(regionCar_grid_selector).jqGrid('getDataIDs');
	var carIdArray = new Array();
	if(rows){
		var len = rows.length;
		for(var i=0;i<len;i++){
			carIdArray.push(rows[i]);
		}
	}
	var carIds = carIdArray.join(",");
	
	$.ajax({
		url : basePath+"/com/supermap/saveSelectedCars?regionId="+regionId+"&carIds="+carIds+"&alarmTypeCode="+alarmTypeCode,
		type : "POST",
		success : function(obj) {
		if (obj.status == 1) {
			//$('#regionlinkCarsDiv').modal('toggle');
			$('#regionlinkCarsDiv').modal('hide');
			bootbox.alert("操作成功!");
			 reload();
		} else {
			$('#regionlinkCarsDiv').modal('hide');
			//$('#regionlinkCarsDiv').modal('toggle');
			bootbox.alert(msg);
			var msg = obj.info
			 reload();
			return false;
			
		}
		}
	});
}
