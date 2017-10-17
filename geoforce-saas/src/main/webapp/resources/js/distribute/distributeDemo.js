$("#container").height($(window).height() - 50);

var map = new AMap.Map('container');
AMap.plugin([ 'AMap.ToolBar', 'AMap.Scale', 'AMap.OverView' ],
	function() {
		map.addControl(new AMap.ToolBar());

		map.addControl(new AMap.Scale());

		map.addControl(new AMap.OverView({
			isOpen : true
		}));
	});

$(window).resize(function() {
	$("#container").height($(window).height() - 50);
});