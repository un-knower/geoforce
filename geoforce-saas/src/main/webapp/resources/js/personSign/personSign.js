var grid_selector = "#grid-table";
var pager_selector = "#grid-pager";
var carGridWith = 86;
$(function(){
    initGrid();
	loadNavGrid();
});
function initGrid(){
	var colNames = ['门店id','门店名称','店长名称','店长手机号','签到时间','签到偏差/米','签退时间','签退偏差/米','停留时间/分钟','巡查结果','照片展示'];
	var colModel = [{index:'id',name:'id', width:1,hidden:true},
	                {index:'personStore.name',name:'personStore.name', width:40},
	                {index:'personStore.shopkeeperName',name:'personStore.shopkeeperName', width:1,width:60},
	                {index:'personStore.shopkeeperPhone',name:'personStore.shopkeeperPhone', width:1,width:60},
	                {index:'personSign.startDate',name:'personSign.startDate',width:60},
	                {index:'personSign.startDistance',name:'personSign.startDistance', width:60},
	                {index:'personSign.endDate',name:'personSign.endDate', width:60},
	                {index:'personSign.endDistance',name:'personSign.endDistance', width:60},
	                {index:'personSign.stayDate',name:'personSign.stayDate', width:60},
					{index :'personSign.results',name : 'personSign.results',width:40,sortable : false,
	                formatter : function(cellvalue, options, rowObject) {
        				if (cellvalue == '0') {
        					return "未完成";
        				} else if(cellvalue == '1') {
        					return "正在巡店";
        					}else{
        						return "完成";
        					}
        				}	
					},
					{index:'view',name:'view', width:40}];
	var url =basePath+"/com/supermap/personSignList";
	loadGrid(grid_selector,url,"","巡店列表",colNames,colModel);
}
function loadGrid(obj, url,data ,title,colNames,colModel){
	jQuery(obj).jqGrid({
		url:url,
		data:data,
		datatype: 'json',
		mtype: 'POST',
		height:  function(){var h=getWindowHeight()-240;return h+"";}(),
		width: '100%',
		loadtext : "加载中...",
		emptyrecords : "无记录",
		colNames:colNames,
		colModel:colModel, 
		viewrecords : true,
		recordtext: "查看第 {0} - {1} 条记录，共 {2}条",
		rowNum:10,
		scrollrows: false, // 是否显示行滚动条 
		rowList:[10,20,30],
		pager : pager_selector,
		altRows: true,
		multiselect: false,//复选框
        multiboxonly: true,
        jsonReader: {
			root: "result",
			total: "pageNum",// 表示总页数
			page: "currentPageNum",//当前页
			records: "totalNum",//总行数
            repeatitems : false    
        },
        loadComplete : function() {
        	var table = this;
			setTimeout(function() {
				updatePagerIcons(table);
			}, 0);
		}, gridComplete:function(){
	    　	　	var ids=jQuery(obj).jqGrid('getDataIDs');
	        for(var i=0; i<ids.length; i++){
	        	var id=ids[i];//门店id
	            focusNum = "<a class='glyphicon glyphicon-picture'  alt='查看门店照片' title='查看门店照片' onclick='signImg(\"" + id + "\")'></a>&nbsp;&nbsp;&nbsp;&nbsp;";
	            jQuery(obj).jqGrid('setRowData', ids[i], { view: focusNum});
	        }
	    　　},
		caption: title,
		autowidth: true
	});
	$(window).triggerHandler('resize.jqGrid');
}
function loadNavGrid(){
    jQuery(grid_selector).jqGrid('navGrid',pager_selector,
		{ 	//navbar options
			edit: false,
			editicon : 'ace-icon fa fa-pencil blue',
			add: false,
			addicon : 'ace-icon fa fa-plus-circle purple',
			del: false,
			delicon : 'ace-icon fa fa-trash-o red',
			search: false,
			searchicon : 'ace-icon fa fa-search orange',
			refresh: false,
			refreshicon : 'ace-icon fa fa-refresh green',
			view: false,
			viewicon : 'ace-icon fa fa-search-plus grey',
			searchtext:'查找',edittext:'编辑',addtext:'添加',refreshtext:'刷新', deltext:'删除',viewtext:'查看',
			searchtitle:'查找',edittitle:'编辑',addtitle:'添加',refreshtitle:'刷新', deltitle:'删除',viewtitle:'查看',
			addfunc:test,
			editfunc:test,
			delfunc:test,
			alertcap : "提示",
			alerttext : "请选择一行记录"
		}
	);}
/**
 * 显示门店图片
 */
function signImg(id){
	$("#imgbody").empty();
	$.post(ctx+"/com/supermap/personSignPic",{id:id,type:'1'},function(data){
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
 * 页面查询
 * @return
 */
function reload() {
	var name = $('#name').val();
	var status = $("#status").val();
	var begindate =$("#begindate").val();
	var enddate = $('#enddate').val();
	var parms={name:name,status:status,begindate:begindate,enddate:enddate};
	var url =basePath+"/com/supermap/personSignList";
	jQuery(grid_selector).jqGrid('setGridParam',{url:url,page:1,postData:parms}).trigger("reloadGrid")
	$("#grid-table").trigger("reloadGrid");
}
function test(){
}


