if (jQuery.browser.msie) {
	var script = '<' + 'script type="text/javascript" src="script/omp_ie.js"' + '><' + '/script>';
	document.writeln(script);
}
else {
	var script = '<' + 'script type="text/javascript" src="script/omp_other.js"' + '><' + '/script>';
	document.writeln(script);
}