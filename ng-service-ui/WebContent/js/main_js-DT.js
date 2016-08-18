function check_all(element){
     var box = element.form;
     for(var i = 0; i < box.length; i++) 
    	 box[i].checked = element.checked;
}

function show_popup(){
	 BootstrapDialog.show({
     title: 'Say-hello dialog',
     message: 'Hi Apple!'
 });
}


function showModal() {
    $('.myModal').modal('show');
}