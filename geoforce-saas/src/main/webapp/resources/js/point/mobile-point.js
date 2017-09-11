
/*
 * 移动端采集
 */
Map.showMobilePointWindow = function() {
	Dituhui.Point.SearchMapsFromMobile(
		function(e){
			var h = '';
			for(var len=e.length, i=0; i<len; i++ ) {
				var item = e[i];
				if(item.app_name != "MARKER") {
					continue;
				}
				h += '<option value="'+ item.id +'">';
				h += item.title;
				h += '</option>';
			}
			$("#select_mpoint").html(h).find("option:first").prop("selected", 'selected');
			$('.sync-mobile-point').removeClass("hide");
		},
		function(info) {
			Dituhui.showHint(info ? info : "未获取到您在大众版的点标记地图");
		}
	);
}

Map.searchMobilePoint = function() {
	var purl = $('#select_mpoint').val();
	if(!purl || purl == "") {
		Dituhui.showHint("请选择一个点标记地图");
		return;
	}
	Dituhui.Point.SearchFromMobile(
		{
			mapurl: "http://c.dituhui.com/maps/" + purl + "/get_markers"
		},
		function(e){
			$('.sync-mobile-point').addClass("hide");
			if(e.result == 0) {
				Dituhui.showPopover("采集到0个网点");
			}
			else {
				Dituhui.Modal.loaded_right("采集到" + e.result + "个网点", function(){
					Map.searchBranches();
					Dituhui.Modal.hide();
				})
			}
		},
		function(info){
			Dituhui.showHint(info ? info : "网点采集失败");
		}
	);
}
