/**
 * 页面加载完成后执行初始化操作
 */
//初始数据
var HistoryMap=[
    {
        province:"北京市",
        city:"市辖区",
        county:"市辖区"
    },
    {
    	province:"上海市",
    	city:"市辖区",
    	county:"黄浦区"
    },
    {
    	province:"广东省",
    	city:"广州市",
    	county:"市辖区"
    },
    {
    	province:"天津市",
    	city:"市辖区",
    	county:"市辖区"
    },
    {
    	province:"四川省",
    	city:"成都市",
    	county:"市辖区"
    },
    {
    	province:"重庆市",
    	city:"市辖区",
    	county:"市辖区"
    }
];
var PCAS_OBJECT;
$(function(){
	
    $("#province,#city,#county").change(function(){
    	setTimeout(selectChange,100);
    });
    
    displayHistory();
    
//	$( "#address" ).autocomplete({
//		source: PROJECT_URL+"address/data/get",
//		formatMatch: function(row, i, max) {
//	        return row.allInfo;
//	    },
//	    formatResult: function(row) {
//	        return row.address+"--poi--"+row.poi;
//	    }
//	});
	$( "#address" ).autocomplete({
		minLength: 1,
		source: PROJECT_URL+"address/data/get",
		focus: function( event, ui ) {
			$( "#address" ).val( ui.item.label );
			return false;
		},
		select: function( event, ui ) {
			$( "#project" ).val( ui.item.label );
			return false;
		}
	})
	.data( "ui-autocomplete" )._renderItem = function( ul, item ) {
		var info="";
		if(item.idcode){
			info="<label style='color:gray;margin: 0px;padding: 0px;border-bottom:1px dashed gray;'>[附近]"+item.label+ "</label>";
		}
		return $( "<li>" )
			.append( "<a><label style='color:black;margin: 0px;padding: 0px;border-bottom:1px solid gray;'>" + item.label + "</label><br>"+info+"</a>" )
			.appendTo( ul );
	};
	PCAS_OBJECT=new PCAS("province","city","county");
	
});
/**
 * 使用按钮事件
 */
function getInput(){
	alert("地址："+$("#address").val());
}
/**
 * 显示历史
 */
function displayHistory(){
	$("#historyTD").html("");
	$.each(HistoryMap,function(key,val){
    	var addr=(val.province+val.city+val.county).replace(/(市辖区|市辖县)/g,"");
    	$("#historyTD").append("<a href='javascript:void(0);' onclick='setInput(this);' title='"+addr+"'>"+addr+"</a>&nbsp;&nbsp;");
    	if(0===(key+1)%3){
    		$("#historyTD").append("<br>");
    	}
    }); 
}
/**
 * 省市县变更
 */
function selectChange(){
	var province=$("#province").val();
	var city=$("#city").val();
	var county=$("#county").val();
	var addr=(province+city+county).replace(/(市辖区|市辖县|--请选择省份--)/g,"");
	HistoryMap.shift();
	HistoryMap.push({
    	province:province,
    	city:city,
    	county:county
    });
	displayHistory();
	setInput({title:addr});
}
/**
 * 将地址填入地址框
 */
function setInput(that){
	$("#address").val(that.title);
	$("#address").trigger('keydown'); 
}