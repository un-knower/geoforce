/**
 * 地图右键菜单
 */
var Rightmenu = {
	menu: null,
	isFeature: false,
	createMenu: function(feature) {
		if(feature.attributes.type === "region") {
			Map.regionSelect(feature, false);
			$('.feature-right-menu li').addClass("hide");
			if(selectedFeatures.length < 1) {
				return;
			}
			else if(selectedFeatures.length == 1) {
				$('.feature-right-menu').find('.r-attr, .r-delete, .r-pan').removeClass('hide');
			}
			else {
				$('.feature-right-menu').find('.r-merge, .r-pan').removeClass('hide');
			}
			
			/*var evt = this.handlers.feature.evt;
			$('.feature-right-menu').css({
				left: evt.offsetX + 3 + "px",
				top: evt.offsetY + "px",
				visibility: "visible"
			});*/
			var evt = this.handlers.feature.up;
			$('.feature-right-menu').css({
				left: evt.x + 3 + "px",
				top: evt.y + "px",
				visibility: "visible"
			});
			
			Rightmenu.isFeature = true;
		}
	}
}
$(function() {
	/*
	$.contextMenu({
		selector: '#map',
		autoHide: true,
		delay: 400,
		zIndex: 80000,
		items: {
			"pan": {
				name: "平移地图",
				icon: "setpan",
				callback: Rightmenu.setpan
			}
		}
	});*/
	/*var callbacks = {
		rightclick: function(currentFeature) {
			console.log(currentFeature)
			
			var centerPoint = currentFeature.geometry.getCentroid();
			var pos = new SuperMap.LonLat(centerPoint.x, centerPoint.y);
			var p = map.getPixelFromLonLat(pos);
			menu.style.left = p.x + "px";
			menu.style.top = p.y + 50 + "px";
			menu.style.visibility = "visible";
		}
	};
	control_select.callbacks = SuperMap.Util.extend(callbacks, control_select.callbacks);*/
	
	map.events.on({
		"movestart": function() {
			if(Rightmenu.menu) {
				Rightmenu.menu.style.visibility = "hidden";
			}
		},
		"click": function() {
			if(Rightmenu.menu) {
				Rightmenu.menu.style.visibility = "hidden";
			}
		}
	});
	map.events.register("mousedown");
	
	//创建EventUtil对象
	var EventUtil = {
		addHandler: function(element, type, handler) {
			if(element.addEventListener) {
				element.addEventListener(type, handler, false);
			} else if(element.attachEvent) {
				element.attachEvent("on" + type, handler);
			}
		},
		getEvent: function(event) {
			return event ? event : window.event;
		},
		//取消事件的默认行为
		preventDefault: function(event) {
			if(event.preventDefault) {
				event.preventDefault();
			} else {
				event.returnValue = false;
			}
		}
	};
	EventUtil.addHandler(window, "load", function(event) {
		Rightmenu.menu = $("#featureRightMenu")[0];
		EventUtil.addHandler($('#map')[0], "contextmenu", function(event) {
			event = EventUtil.getEvent(event);
			EventUtil.preventDefault(event);
			
			if(!Rightmenu.isFeature) {
				$('.feature-right-menu li').addClass("hide");
				$('.feature-right-menu').find('.r-pan').removeClass('hide');
				
				Rightmenu.menu.style.left = event.offsetX + 3 + "px";
				Rightmenu.menu.style.top = event.offsetY + "px";
				Rightmenu.menu.style.visibility = "visible";
			}
			Rightmenu.isFeature = false;
		});
		
		EventUtil.addHandler(document, "click", function(event) {
			Rightmenu.menu.style.visibility = "hidden";
		});
	});
	
	$('.feature-right-menu .r-attr').on('click', Map.attrRegionClick);
	$('.feature-right-menu .r-delete').on('click', Map.deleteRegionClick);
	$('.feature-right-menu .r-merge').on('click', Map.mergeRegionClick);
	$('.feature-right-menu .r-pan').on('click', Map.pan);	
});








