
var map = null;

$(function(){	
	Map.initCss();
	$(document).resize(Map.initCss);
	Map.init();
});


var Map = {
	/**
	 * 地图初始化
	 */
	init: function(){
		var controls_zoombars = [        
		    new SuperMap.Control.PanZoomBar({showSlider:true}),
		    new SuperMap.Control.Navigation({
		        dragPanOptions: {
		            enableKinetic: true
		        }
		    })  
		];
		map = new SuperMap.Map("map", 
			{ 
				controls: controls_zoombars,
				allOverlays: true
			}
		);
        layer_map = new SuperMap.Layer.CloudLayer({
        	url: "http://t2.supermapcloud.com/FileService/image"
        });
		
		map.addLayers([ layer_map]);
        map.setCenter( new SuperMap.LonLat(12958400.883957,4852082.440605948), 11);
	},
    initCss: function() {
        var bodyWidth = Number($('body').css('width').replace('px', '') );
        var bodyHeight = Number($('body').css('height').replace('px', '') );
        $('.popover-hint').css({ left: (bodyWidth - 250)*0.5 + "px" });
        if(!Map.supportedCss3){
            $('.map').css({ 'height': (bodyHeight - 45)+'px' });
        }
    }			
}


