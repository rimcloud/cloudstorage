var JSLoading = {
	
	show : function(msg){
		var id = rimCommon.generateUUID();		

		this.checkDiv(id);
		
		$("#" + id).modal("show").find(".modal-body-loading span").text(msg);
		$("#" + id).on('hidden.bs.modal', function () {	
			$("#" + id).remove();
		});		
		return id;
	},	
	
	hide : function(id){
		$("#" + id).modal("hide");
	},
		
	checkDiv : function(id){
			var modalDiv = "<div class='modal modal-loading fade rim-loading' id='"+ id +"' tabindex='-1' role='dialog' aria-labelledby='myModalLabel' data-backdrop='static' data-keyboard='false'>";
			modalDiv += "<div class='modal-dialog modal-dialog-loading' role='document'>";
			modalDiv += "<div class='modal-content-loading'>";
			modalDiv += "<div class='modal-header-loading'> ";   
			modalDiv += "<img class='loading-img' src='" + rimCommon.getUrl("/resources/images/common/loading.gif") + "'  />";
			modalDiv += "</div>";
			modalDiv += "<div class='modal-body-loading'><span class='loading-msg'></span>";			
			modalDiv += "</div>";
			modalDiv += "<div class='modal-footer-loading'> ";     
			modalDiv += "</div>";
			modalDiv += "</div>";
			modalDiv += "</div>";
			modalDiv += "</div>";
			$("body").append(modalDiv);
	}
	

};