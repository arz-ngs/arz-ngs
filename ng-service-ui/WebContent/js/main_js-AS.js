function onWindowLoad() {
	adjustCollumnSizes();
}

/**
 * this method is used to ensure that the left and right column have the same height as the middle column
 */
function adjustCollumnSizes() { 
	var leftC = document.getElementById("main_nav");
	
	var body = document.body,
  		html = document.documentElement;

	var max_height = Math.max( body.scrollHeight, body.offsetHeight, 
                     html.clientHeight, html.scrollHeight, html.offsetHeight );
	
	leftC.style.height = max_height + "px";
}

function typeServiceInput() {
		console.log("input");
		document.getElementById("ngs-form:overviewTable:serviceRegex").focus();
}