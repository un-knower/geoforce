/**
 * 区划关联另一个区划的时候，选择区划
 */
function RelatedSelect() {
	/**
	 * 激活状态
	 */
	this.active = false;
	/**
	 * 正在修改的区划
	 */
	this.active_feature = null;
	/**
	 * 被选中关联的区划
	 */
	this.selectedFeature = null;
	
	var that = this;
	this.activate = function(feature) {
		if(that.active) {
			return;
		}
		that.active_feature = feature;
		that.active = true;
		using = typeof(using)==="undefined" ? true : using;
		that.using = using;
		
		var attr = that.active_feature.attributes;
		if(attr.area_status == 1) {
			$("#txt_relation_areaname").val(attr.relation_areaname);
			$("#txt_relation_areaid").val(attr.relation_areaid);
		}
	}
	this.indexOfFeature = function(id, layer) {
		var fs = layer.features;
		for(var i=fs.length; i--; ) {
			if(id === fs[i].attributes.id) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * 点击选择区划
	 */
	this.select = function(feature) {
		var attr = feature.attributes;
		if(attr.id == that.active_feature.attributes.id
			|| (that.selectedFeature !== null 
				&& attr.id == that.selectedFeature.attributes.id) ) {
			return;
		}
		if(that.selectedFeature != null) {
			var index = that.indexOfFeature(that.selectedFeature.attributes.id, layer_region);
			if(index != -1) {
				layer_region.features[index].style = that.selectedFeature.attributes.oldStyle;
			}
		}
		feature.style = Dituhui.Zone.getRegionRelatedSelectStyle();
		$("#txt_relation_areaname").val(attr.name);
		$("#txt_relation_areaid").val(attr.id);
		layer_region.redraw();
		that.selectedFeature = feature;
	}
	
	
	this.deactivate = function(isRecoverFeatureStyle) {		
		if(isRecoverFeatureStyle && that.selectedFeature != null) {
			var index = that.indexOfFeature(that.selectedFeature.attributes.id, layer_region);
			if(index != -1) {
				layer_region.features[index].style = that.selectedFeature.attributes.oldStyle;
				layer_region.redraw();
			}
		}
		
		that.selectedFeature = null;
		that.active_feature = null;		
		that.active = false;
	}
	
	this.init = function() {
		
	}
	
	this.init();
	return this;
}



