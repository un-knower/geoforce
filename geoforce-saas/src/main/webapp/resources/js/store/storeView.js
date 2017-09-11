var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var storeListDivLayer = ""
var mapWd = 83;
var jqgridWidth;


$(document).ready(function(){
	//窗口变化自动
	$(window).resize(function(){
//		resizeMapDiv();
	});
	resizeMapDiv();
	//初始化地图
	initMap();
	setCurActive("storeManager");
	
	mapWd = 0;
//	setCurActive("alarmFence");//in mapSupport.js
	
	menu();
	initGrid();
	selectIco();
	$('#storeInput').on('hidden.bs.modal', function (e) {
		$("#addStoreForm").show();
		$("#storeSelectIco").hide();
		clearLayerFeatures();
		})
	
});


/**
 * 加载操作按钮
 * @return
 */
function menu(){
var sideBarw = $("#sidebar").width()-40;
var ww = $(window).width()-$("#sidebar").width()-40;
var menu = $("#menu").width();
var left = (ww +sideBarw)-menu
	$.layer({
	    type: 1,
	    shade : [0],
	    offset:['55px',left+'px'],
	    area: ['200px', '50px'],
	    title: false,
	    bgcolor: '',
	    border: [0],
	    page: {dom :'#menu'},
		closeBtn: [0, false]
	});
	
}
function storeList(btnEvent){
	var off = $('#menu').css("top");
	var sideBarw = $("#sidebar").width()-40;
	var ww = $(window).width()-$("#sidebar").width()-40;
	var left = (ww +sideBarw)-300
	var mapHt = $("div[id*='mapDiv_']").height();
	storeListDivLayer = $.layer({
		type:1,
		title: false,
		offset: ['100px',(left+47)+'px'],
		area:['300',(mapHt-60)],
		shade : [0.1 , '#000' , true],
		shade:[0],
		closeBtn:[0,false],
//		shift: 'right-top',
		page:{dom:'#storeList'}
	});
}

function layerStoreListeClose(){
	layer.close(storeListDivLayer);
}
/**
 * 标注门店
 */
function taggingPoi(){
	clearLayerFeatures();
	map_drawPoint()
}
/**
 * 加载列表在地图上打点
 * @param data
 * @return
 */
function displayPoi(data){
	setTimeout(function(){
		//在地图上显示poi点
		   var storeArr= data.result;
		   map_addPois(storeArr,0)
	},200);
}
function selectMapIco(){
	$("#storeSelectIco").show();
}
function selectIco(){
	$("#storeSelectIco").children().each(function(i){
		$(this).click( function () { 
			var imgName = $(this).attr("alt");
			var imgUrl = $(this).attr("src");
			$("#storeIco").attr("src",imgUrl);
			$("#ico").val(imgName);
			$("#storeSelectIco").hide();
		});
		
	})
	
}

function displayOnePoi(id){
	$.post(ctx+"/com/supermap/storeInfo",{id:id},function(data){
		
		if(data){
			var obj = new Object();
   			obj.id = data.id;
   			obj.name= data.name;
   			obj.lng =data.ctLng;
   			obj.lat = data.ctLat;
   			rePanToMark(obj)
		}
		
	});
}

//得到经纬度
function getLnglat(){
	var lng = $("#longitude").val();
	var lat = $("#latitude").val();
	
	if(lng == '' || lat == '') return '';
	return lng+","+lat;
}
//绘制完成后set经纬度  弹出输入框
function setLngLat(lng,lat){

    $(':input','#storeForm')  
    .not(':button, :submit, :reset')  
    .val('')  ;
    $("#ico").val("18.png")
//    .removeAttr('checked')  
//    .removeAttr('selected');  
	$("#ctLng").val(lng);
	$("#ctLat").val(lat);
	$("#storeWarning").empty()
	$("#myModalLabel").empty().append("添加门店信息");
	$("#storebtn").attr("onclick","saveStore();");
	$("#storeInput").modal('show');
}
/**
 * 添加门店
 * @return
 */
function saveStore(){
	
	if(!validate()){
		return false;
	}
	$.ajax({
		url : ctx+"/com/supermap/addStore?ram=" + Math.random(),
		type : "POST",
		data :$("#storeForm").serialize(),
		success : function(obj) {
			clearLayerFeatures();
			if (obj.status == 1) {
				
				$("#storeInput").modal('hide');
				bootbox.alert("操作成功!");
				reload();
				
			} else {
				var msg = obj.info
				showWarning(msg)
				//bootbox.alert(msg);
				return false;
				
			}
		}
	});
}

/**
 * 地图div高度自适应
 * @return
 */
function resizeMapDiv(){
	var wh = $(window).height();
	var ww = $(window).width()-$("#sidebar").width()-40;
	var top = $("#navbar").height()+5;
	
	var tbHt = $("#mapTable").height();

	$("div[id*='mapDiv_']").css({width:ww,height:(wh-top-20)});
	$("#storeListHiddenDiv").css({top:(wh-20)/2});
//	setTimeout(function() {
//		if($("#monitorBtns").width() < navBarWd){
//			$("div[id*='mapDiv_']").css({width:ww),height:(wh-top-footer-navBarh-tbHt)});
//			;
//		}
//	},100);
	refreshMap();
}
/**
 * 显示列表
 * @return
 */
function initGrid(){
	
	var colNames = ['操作','名称','地址'];
	var colModel = [{index:'LOOK',name:'LOOK', width:35},
	                {index:'name',name:'name', width:40},
					{index:'address',name:'address', width:40}];
	var name = $("#search").val();
	var parms={name:name};
	var url =ctx+"/com/supermap/storeList"
	loadGrid(grid_selector,url,"","",parms,colNames,colModel);
}




/**
 * 加载Grid主方法
 * @param obj
 * @param url
 * @param data
 * @param title
 * @param postData
 * @param colNames
 * @param colModel
 * @return
 */
function loadGrid(obj, url,data ,title,postData,colNames,colModel){
	var mapHt = $("div[id*='mapDiv_']").height();
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		postData: postData,
		width: '300',
		height: mapHt-210,
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		rowNum:10,
		sortable:false,
		viewsortcols:[false,'vertical',false],
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		multiselect: false,
        multiboxonly: false,
        jsonReader: { 
            root: "result",
			total: "pageNum",// 表示总页数
			page: "currentPageNum",//当前页
			records: "totalNum",//总行数
            repeatitems : false    
        },  

		loadComplete : function(data) {
        	resizeMapDiv();
        	clearPois();
        	displayPoi(data);
		},
		onSelectRow: function(id){
			 
	    },
	     gridComplete:function(){
	    	
	    　　	var ids=jQuery(obj).jqGrid('getDataIDs');
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i];   
	            focusNum = "<a href='#' class='glyphicon glyphicon-edit'  alt='编辑' title='编辑' onclick='tostoreEdit(\"" + id + "\")'></a>&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='glyphicon glyphicon-trash'  alt='删除' title='删除' onclick='delStor(\"" + id + "\")'></a>&nbsp;&nbsp;";
	            focusNum += "<a href='#' class='glyphicon glyphicon-picture'  alt='店面图片' title='店面图片' onclick='showImg(\"" + id + "\")'></a>";
	            jQuery(obj).jqGrid('setRowData', ids[i], { LOOK: focusNum});
	        }
	        $("td[role= 'gridcell']").removeAttr("title")
	    　　},
	    ondblClickRow:function(rowid,iRow,iCol,e){
	    	displayOnePoi(rowid);
	    }
	});
	$(obj).jqGrid('navGrid',pager_selector,{add:false,edit:false,del:false});
}

/**
 * 显示门店图片
 */
function showImg(id){
	$("#imgbody").empty();
	$.post(ctx+"/com/supermap/picList",{id:id,type:'1'},function(data){
		if(data.length>0){
		 $.each(data,function(index,d){
			 var url = d.url
			 var div = $("<div />");
			 div.addClass("item");
			 if(index == 0){
				 div.addClass("active"); 
			 }
			 var img = $("<img />");
			 img.attr("src",url)
			 div.append(img);
			 $("#imgbody").append(div)
			  
		 })
		 $("#storeImg").modal('show');
		}else{
			bootbox.alert("无图片信息");
			return false;
		}
	});
}
/**
 * 弹出编辑
 */
function tostoreEdit(id){
	$("#myModalLabel").empty().append("修改门店信息");
	$("#storeWarning").empty();
	$("#storebtn").attr("onclick","storeEdit();");
	$.post(ctx+"/com/supermap/storeInfo",{id:id},function(data){
		$("#id").val(data.id);
		$("#ctLat").val(data.ctLat);
		$("#ctLng").val(data.ctLng);
		$("#boxdepId").val(data.deptId);
		$("#name").val(data.name);
		$("#shopkeeperName").val(data.shopkeeperName);
		$("#shopkeeperPhone").val(data.shopkeeperPhone);
		$("#address").val(data.address)
		$("#storeInput").modal('show');
	
	});
}
/**
 * 执行编辑操作
 * @return
 */
function storeEdit(){
	if(!validate()){
		return false;
	}
	$.ajax({
		url : ctx+"/com/supermap/updateStore?ram=" + Math.random(),
		type : "POST",
		data :$("#storeForm").serialize(),
		success : function(obj) {
			if (obj.status == 1) {
				$("#storeInput").modal('hide');
				bootbox.alert("操作成功!");
				clearPois();
				reload();
				
			} else {
				var msg = obj.info
				showWarning(msg)
				//bootbox.alert(msg);
				return false;
				
			}
		}
	});
}

/**
 * 执行删除操作
 * @param id
 * @return
 */
function delStor(id){
	bootbox.confirm("确定删除吗?删除后将无法恢复！", function(result) {
		if (result) {
			$.ajax({
				type : "POST",
				url : ctx+"/com/supermap/delStore?ram=" + Math.random() + "&storeIds=" + id,
				success : function(obj) {
				if (obj.status == 1) {
						bootbox.alert("操作成功!");
						clearPois();
						reload();
					} else {
						var msg = obj.info;
						bootbox.alert(msg);
						reload();
					}
				}
			});
		}
	});
}
/**
 * 刷新列表
 * @return
 */
function reload() {
	var name = $("#search").val();
	var parms={name:name};
	var url =ctx+"/com/supermap/storeList"
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	//$("#grid-table").trigger("reloadGrid");
}
/**
 * 验证
 * @return
 */
function validate(){
	
	var name = $("#name").val();
	if(name == ""|| name.length==0){
		showWarning("请填写门店名称！")
		return false;
	}
	
	return true;
	
}

function showWarning(msg){
	$("#storeWarning").empty().append(msg)
}



