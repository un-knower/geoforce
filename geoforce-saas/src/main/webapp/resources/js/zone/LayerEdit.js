/*
 * 修改区划
 */
Regionedit = {};

/*
 * 点击编辑区划
 */
Regionedit.editRegionClick = function() {
	var flag = Map.checkAction();
	if(!flag) {
		return;
	}
	if( selectedFeature == null || selectedFeatures.length != 1 ) {
		Dituhui.showHint("请先选择一个区划");
		return;
	}
	map.removeAllPopup();
	
	layer_region.removeFeatures([selectedFeature]);
	
	layer_edit.addFeatures(selectedFeature);
	
	Map.disableDbClick();
//	Map.disableDrag();
	
	control_select.deactivate();
	control_editPolygon.activate();
	control_editPolygon.selectFeature(layer_edit.features[0]);
	$('#map').one('dblclick', Regionedit.done);
	
	//可以使用方向键
	Map.setMapIndexTop();
}

/*
 * 双击地图结束编辑区划
 */
Regionedit.done = function(){
	control_editPolygon.unselectFeature(layer_edit.features[0]);
	control_editPolygon.deactivate();
	
	var info = '确定要修改区划' + selectedFeature.attributes.name + '的节点？';
	info += '<br>';
	info += '点击“是”提交修改，点击“否”不修改该区划，点击“取消”关闭弹窗，继续修改该区划。';
	Dituhui.Modal.ask(
		info,
		Regionedit.submit,
		null,
		function(){
			//取消,继续编辑
			control_editPolygon.activate();
			//可以使用方向键
			Map.setMapIndexTop();
			control_editPolygon.selectFeature(layer_edit.features[0]);
			$('#map').one('dblclick', Regionedit.done);
			Dituhui.Modal.hide();
		},
		function(){
			//不修改区划,重新查询
			Map.search();
			Map.enableDbClick();
//			Map.enableDrag();
			control_select.activate();
			Map.clearSelectedFeatures();
			Dituhui.Modal.hide();
			layer_edit.removeAllFeatures();
		}
	);
}

/*
 * 提交修改
 */
Regionedit.submit = function() {
	var f = layer_edit.features[0];
	
	Dituhui.Zone.update(
		{
			id: f.attributes.id,
			point2Ds: Dituhui.Zone.getPoint2DsFromRegion(f, "region", Baidu.using),
			parts: Dituhui.Zone.getPartsFromRegion(f)
		},
		function() {
			Dituhui.showPopover("区划修改成功");
			Map.search();
			Map.enableDbClick();
//			Map.enableDrag();
			control_select.activate();
			Map.clearSelectedFeatures();
			Dituhui.Modal.hide();
			layer_edit.removeAllFeatures();
		},
		function(info) {
			Dituhui.Modal.loaded_wrong(info, function(){
				Map.search();
				Map.enableDbClick();
//				Map.enableDrag();
				control_select.activate();
				Map.clearSelectedFeatures();
				Dituhui.Modal.hide();
				layer_edit.removeAllFeatures();
			});
		}
	)
}













