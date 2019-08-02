function setRowIconSharingIn(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);	
	selectedRow.find(".rim-filetype").before("<i class='fa fa-sign-in rim-shareicon-overlay-top' > </i>");
}
 	
function setRowIconSharingOut(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);	
	selectedRow.find(".rim-filetype").before("<i class='fa fa-rotate-180 fa-sign-in rim-shareicon-overlay-top' > </i>");
}

