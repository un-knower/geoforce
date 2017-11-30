var hover_dir = {
	wrapper: $p(".major-list")[0],
	box: $p(".major-item"),
	target: $p(".back-face"),
	bindEvent: function(){
		var mouse_pos, x, y, stop_bubble;
		for(var i = 0; i < hover_dir.box.length; i++){
			(function(n){
				$p.addEvent(hover_dir.box[n], "mouseover", function(event){
					event = $p.getEvent(event);
					var relatedT = $p.getRelatedTarget(event);
					if(!$p.contains(hover_dir.box[n], relatedT)){
						var child = hover_dir.box[n].childNodes[0];
						$p.stopPropagation(event);
						$p.removeClass(hover_dir.target[n], "to-left") ||
						$p.removeClass(hover_dir.target[n], "to-right") ||
						$p.removeClass(hover_dir.target[n], "to-top") ||
						$p.removeClass(hover_dir.target[n], "to-bottom");
						x = event.pageX;
						y = event.pageY;
						mouse_pos = {"x": x, "y": y};
						var dir = $p.get_dir(hover_dir.box[n], mouse_pos);
						switch(dir){
							case "left": 
								$p.addClass(hover_dir.target[n], "from-left");
								break;
							case "right": 
								$p.addClass(hover_dir.target[n], "from-right");
								break;
							case "top": 
								$p.addClass(hover_dir.target[n], "from-top");
								break;
							case "bottom":
								$p.addClass(hover_dir.target[n], "from-bottom");
								break;
							default: break;
						}
					}
				});
				$p.addEvent(hover_dir.box[n], "mouseout", function(event){
					event = $p.getEvent(event);
					var relatedT = $p.getRelatedTarget(event);
					if(!$p.contains(hover_dir.box[n], relatedT)){
						$p.removeClass(hover_dir.target[n], "from-left") ||
						$p.removeClass(hover_dir.target[n], "from-right") ||
						$p.removeClass(hover_dir.target[n], "from-top") ||
						$p.removeClass(hover_dir.target[n], "from-bottom");
						x = event.pageX;
						y = event.pageY;
						mouse_pos = {"x": x, "y": y};
						var dir = $p.get_dir(hover_dir.box[n], mouse_pos);
						switch(dir){
							case "left": 
								$p.addClass(hover_dir.target[n], "to-left");
								break;
							case "right": 
								$p.addClass(hover_dir.target[n], "to-right");
								break;
							case "top": 
								$p.addClass(hover_dir.target[n], "to-top");
								break;
							case "bottom":
								$p.addClass(hover_dir.target[n], "to-bottom");
								break;
							default: break;
						}
					}	
				});
			})(i);
		}},
	init: function(){
		hover_dir.bindEvent();
	}
};
hover_dir.init();

