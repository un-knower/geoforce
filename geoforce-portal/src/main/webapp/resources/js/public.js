Function.prototype.method = function(name, func) {
	if(!this.prototype[name]){
		this.prototype[name] = func;
	}
	return this;
}

function $p(targ) {
	if(typeof targ === "string") {
		var first_letter = targ.substr(0, 1),
			other_letter = targ.substr(1);
		switch (first_letter) {
		case "#":
			return document.getElementById(other_letter);
			break;
		case ".":
			if (document.querySelectorAll) {
				return document.querySelectorAll(targ);
			} else {
				var targArr = [];
				function getNode(elem){
					if(elem.className){
						var classArr = elem.className.split(" ");
						for(var i = 0; i < classArr.length; i++){
							if(classArr[i] == other_letter) {
								targArr.push(elem);
								break;
							}
						}
					}
					if(elem.childNodes.length){
						for(var i = 0; i < elem.childNodes.length; i++){
							if(elem.childNodes[i].nodeType == 1){
								getNode(elem.childNodes[i]);
							}
						}
					}
				}
				getNode(document.body);
				return targArr;
			}
			break;
		default: 
			return document.getElementsByTagName(targ);
		}
	}
}

$p.addClass = function(elem, newClass){
	if(!elem) 
		return false;
	else if(!elem.className) {
		elem.className = newClass;
		return false; 
	}
	else {
		var ownClass = elem.className.split(" "), had = false;
		for(var i = 0; i < ownClass.length; i++){
			if(ownClass[i] === newClass){
				had = true;
				break;
			}
		}
		if(!had){
			elem.className += " " + newClass;
		}
		return had;
	}
};

$p.removeClass = function(elem, oneClass){
	if(!elem || !elem.className) return false;
	var ownClass = elem.className.split(" "),
		had = false;
	for(var i = 0; i < ownClass.length; i++){
		if(ownClass[i] === oneClass){
			ownClass.splice(i, 1);
			had = true;
			break;
		}
	}
	if(had){
		elem.className = "";
		if(ownClass.length < 1){
			return had;
		}else if(ownClass.length == 1){
			elem.className = ownClass[0];
		}else if(ownClass.length >1){
			for(var i = 0; i < ownClass.length; i++){
				if(i == ownClass.length - 1){
					elem.className += ownClass[i];
				}else{
					elem.className += ownClass[i] + " ";
				}
			}
		}
	}	
	return had;	
};

$p.addEvent = function(elem, eventName, handler){
	if(elem){
		if(elem.addEventListener){
			return elem.addEventListener(eventName, handler, false);
		}else if(elem.attachEvent){
			return elem.attachEvent("on" + eventName, handler);
		}else {
			elem["on" + eventName] = handler;
		}
	}
};

$p.removeEvent = function(elem, eventName, handler){
	if(elem){
		if(elem.removeEventListener){
			return elem.removeEventListener(eventName, handler, false);
		}else if(elem.detachEvent){
			return elem.detachEvent("on" + eventName, handler);
		}else {
			elem["on" + eventName] = null;
		}
	}	
};

$p.getEvent = function(event){
	return event ? event : window.event;
};

$p.getTarget = function(event){
	return event.target || event.srcElement;
};

$p.getRelatedTarget = function(event){
	return event.relatedTarget || event.toElement || event.fromElement || null;
};

$p.contains = function(parent, cur){
	while(cur.parentNode){
		if(cur.parentNode === parent){
			return true;
		}
		cur = cur.parentNode;
	}
	return false;
};

$p.preventDefault = function(event){
	if(event.preventDefault){
		event.preventDefault();
	}else{
		event.returnValue = false;
	}
};

$p.stopPropagation = function(event){
	if(event.stopPropagation){
		event.stopPropagation();
	}else{
		event.cancleBubble = true;
	}
};

$p.get_pos = function(elem){
	if(!elem) return false;
	var left = elem.offsetLeft,
		top = elem.offsetTop,
		current = elem.offsetParent;
	while(current !== null){
		left += current.offsetLeft;
		top += current.offsetTop;
		current = current.offsetParent;
	}
	return {"left": left, "top": top};
};

$p.get_dir = function(elem, mouse_pos){
	if(!elem) return false;
	var pos = $p.get_pos(elem),
		size = {"width": elem.offsetWidth, "height": elem.offsetHeight},
		dx = mouse_pos.x - pos.left - size.width/2,
		dy = (mouse_pos.y - pos.top - size.height/2)*-1,
		eve_tan = dy/dx,
		tan = size.height/size.width;
	if(dx != 0){
		if(eve_tan > tan*-1 && eve_tan < tan && dx < 0){
			return "left";
		}else if(eve_tan > tan*-1 && eve_tan < tan && dx > 0){
			return "right";
		}else if((eve_tan > tan || eve_tan < tan*-1) && dy > 0){
			return "top";
		}else if((eve_tan > tan || eve_tan < tan*-1) && dy <= 0){
			return "bottom";
		}
	}else if(dy > 0){
		return "top";
	}else {
		return "bottom";
	}
};