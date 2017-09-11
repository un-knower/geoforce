$(function(){
	$("#txt_setting_start").on("click", function(){
		$(".panel").addClass("hide");
		$(".panel.start").removeClass("hide");
	});
	$("#txt_setting_passing").on("click", function(){
		$(".panel").addClass("hide");
		$(".panel.passing").removeClass("hide");
	});
	$("input.search-route.start").on("keyup", function(e){
		if(e.keyCode === 13) {
			Path.Start.search();
		}
	});
	$(".btn-search-route.start").on("click", Path.Start.search);
})

var Path = {};

/*
 * 起点
 */
Path.Start = {};
/*
 * 查询起点
 */
Path.Start.search = function(){
	var type = $("#select_route_start").val();
	switch(type) {
		case '3':
			Path.Start.searchFromCloud();
			break;
	}
}
/*
 * 搜索POI关键字
 */
Path.Start.searchFromCloud = function() {
	layer_orders_vector.removeAllFeatures();
	var keyword = $("#txt_search_rstart").val();
	if( keyword.length < 1 ) {
		Dituhui.showHint("请输入搜索关键字");
		return;
	}
	var bounds = map.getExtent();
	
	var pager = $('#data_pager_rstart');
	var pageIndex = Number(pager.attr('page'));
	    
	var param = {
		keyword: keyword,
		max_x: bounds.right,
		min_x: bounds.left,
		max_y: bounds.top,
		min_y: bounds.bottom,
		startRecord: 5*pageIndex,
		expectCount: 5
	};
	
	Dituhui.Point.searchFromCloud(param, 
		function(e){
			var data = e.pois ? e.pois : e.results;
			var len = data.length;
			var pois = [],h = '';
			for( var i=0; i<len; i++ ) {
				var item = data[i];
				var geo_point = new SuperMap.Geometry.Point(item.smx, item.smy);			
				var poi = new SuperMap.Feature.Vector(geo_point);
				poi.style = {
					externalGraphic: urls.server + "/resources/assets/num/" + (i+1) + ".gif",
					graphicWidth: 37,
					graphicHeight: 27,
					graphicTitle: item.name,
		        	cursor: "pointer"
				};
				poi.attributes = item;
				poi.attributes.type = "point-cloud";
				pois.push(poi);
				
				item.address = item.address ? item.address : "";
				item.name = item.name ? item.name : "";
				h += '<li>';
				h += '	<div class="badge">'+ (i+1) +'</div>';
				h += '	<div class="left">';
				h += '		<span class="name">'+item.name+'</span>';
				h += '		<span class="address">'+ item.address +'</span>';
				h += '	</div>';
				h += '</li>';
			}
			layer_orders_vector.addFeatures(pois);
			$('.result-part.start').html(h);
			
			Dituhui.setPager({
				limit: 5,
				total: e.totalCount,
				id: "pager_cloudpois",
				toPage: function(page){
					$("#pager_cloudpois").attr("page", index);
					Path.Start.searchFromCloud();
				}
			});
			
            /*$("#pager_cloudpois").show();   
            $(".page-cloud-pois").html( '第' + (pageIndex+1)+'/' + Math.ceil(e.totalCount/10) + '页' ).show();*/
		}, 
		function(){
			layer_orders_vector.removeAllFeatures(); 
            $("#pager_cloudpois").hide();
    		$('.header .search-from-cloud .cloud-pois .content').html("未查询到结果，请尝试重新输入关键字");
    		$('.header .search-from-cloud .child').removeClass("hide");
		}
	);
}

/*
 * 起点搜索，设置分页控件
 */
Path.Start.setPager = function() {
	
}












