var showModalFlag = false;

var JSDialog = {
		Alert : function(msg,userCallback){

			if (showModalFlag == false) {

				showModalFlag = true;

				$("#rimAlert").remove();
				this.checkDivAlert();

				$("#rimAlert").modal("show").find(".modal-body>p").html(msg);

				$('#rimAlert').on('shown.bs.modal', function () {
					$('#rimAlert').find(".btn").focus();

					showModalFlag = false;
				});

				$('#rimAlert').on('hidden.bs.modal', function () {

					if (typeof userCallback === "function") {
			    		userCallback();
			    	}

					$("#rimAlert").remove();
					showModalFlag = false;
				});
			}
		},
		checkDivAlert : function(){

			if ($("#rimAlert").length == 0) {
				var modalDiv = "<div id='rimAlert' class='modal fade rim-dialog' role='dialog' data-backdrop='static' data-keyboard='false' aria-hidden='true' >";
				modalDiv += "<div class='modal-dialog'>";
				modalDiv += "<div class='modal-content'>";
				modalDiv += "<div class='modal-header modal-header-info'>";
				modalDiv += "<h5 class='modal-title'><span class='glyphicon glyphicon-exclamation-sign'></span>"+ rimMessage.AlertTitle +"</h5>";
				modalDiv += "</div>";
				modalDiv += "<div class='modal-body text-center'>";
				modalDiv += "<p style='word-break:break-all;'></p>";
				modalDiv += " </div>";
				modalDiv += "<div class='modal-footer'>";
				modalDiv += "<button type='button' class='btn btn-xs btn-info' data-dismiss='modal' aria-hidden='true'>"+ rimMessage.Ok +"</button>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				$("body").append(modalDiv);
			}
		},
		Confirm : function(msg,userCallback){

			if (showModalFlag == false) {

				showModalFlag = true;

				$("#rimConfirm").remove();

				this.checkDivConfirm();
				$("#rimConfirm").modal("show").find(".modal-body>p").html(msg);

				$("#rimConfirm").find(".btnOK").click(function(e){
					if (typeof userCallback === "function") {
			    		userCallback(true);
			    	}
				});

				$("#rimConfirm").find(".btnCancel").click(function(e){
					if (typeof userCallback === "function") {
			    		userCallback(false);
			    	}
				});

				$('#rimConfirm').on('shown.bs.modal', function () {
					$(document.activeElement).blur();

					showModalFlag = false;
				});

				$('#rimConfirm').on('hidden.bs.modal', function () {
					$("#rimConfirm").remove();
					showModalFlag = false;
				})
			}
		},
		checkDivConfirm : function(){
			if ($("#rimConfirm").length == 0) {
				var modalDiv = "<div id='rimConfirm' class='modal fade rim-dialog' role='dialog' data-backdrop='static' data-keyboard='false' >";
				modalDiv += "<div class='modal-dialog'>";
				modalDiv += "<div class='modal-content'>";
				modalDiv += "<div class='modal-header modal-header-info'>";
				modalDiv += "<h5 class='modal-title'><span class='glyphicon glyphicon-question-sign'></span>"+ rimMessage.ConfirmTitle +"</h5>";
				modalDiv += "</div>";
				modalDiv += "<div class='modal-body text-center'>";
				modalDiv += "<p style='word-break:break-all;'></p>";
				modalDiv += " </div>";
				modalDiv += "<div class='modal-footer'>";
				modalDiv += "<button type='button' class='btn btn-xs btn-info pull-left btnOK' data-dismiss='modal'>"+ rimMessage.Ok +"</button>";
				modalDiv += "<button type='button' class='btn btn-xs btn-default btnCancel' data-dismiss='modal'>"+ rimMessage.Cancel +"</button>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				$("body").append(modalDiv);
			}
		},
		
		ConfirmDate : function(msg,userCallback){

			if (showModalFlag == false) {

				showModalFlag = true;

				$("#rimConfirmDate").remove();

				this.checkDivConfirmDate();
				$("#rimConfirmDate").modal("show").find(".modal-body>p").html(msg);

				$("#rimConfirmDate").find(".btnOK").click(function(e){
					if (typeof userCallback === "function") {
			    		userCallback(true,$("#rimConfirmDate").find("#expiredDate").val());
			    	}
				});

				$("#rimConfirmDate").find(".btnCancel").click(function(e){
					if (typeof userCallback === "function") {
			    		userCallback(false);
			    	}
				});

				$('#rimConfirmDate').on('shown.bs.modal', function () {
					$(document.activeElement).blur();

					showModalFlag = false;
				});

				$('#rimConfirmDate').on('hidden.bs.modal', function () {
					$("#rimConfirmDate").remove();
					showModalFlag = false;
				})
			}
		},
		checkDivConfirmDate : function(){
			if ($("#rimConfirmDate").length == 0) {
				var modalDiv = "<div id='rimConfirmDate' class='modal fade rim-dialog' role='dialog' data-backdrop='static' data-keyboard='false' >";
				modalDiv += "<div class='modal-dialog'>";
				modalDiv += "<div class='modal-content'>";
				modalDiv += "<div class='modal-header modal-header-info'>";
				modalDiv += "<h5 class='modal-title'><span class='glyphicon glyphicon-question-sign'></span>"+ rimMessage.ConfirmTitle +"</h5>";
				modalDiv += "</div>";
				modalDiv += "<div class='modal-body text-center'>";
				modalDiv += "<p style='word-break:break-all;'></p>";
				
				modalDiv += "<div style='padding-left: 40px; padding-right: 40px;' >";
				modalDiv += "<div class='input-group input-group-date input-group-rim-btn'> ";
				modalDiv += "<input type='text' class='form-control input-xs' id='expiredDate' readonly='readonly'> ";
				modalDiv += "<div class='input-group-btn'> ";
				modalDiv += "<button class='btn btn-default disabled'><small>"+ rimMessage.ConfirmExpireDate +"</small></button> ";
				modalDiv += "</div> ";
				modalDiv += "</div> ";  
				modalDiv += "</div> ";  
				  
				modalDiv += " </div>";
				modalDiv += "<div class='modal-footer'>";
				modalDiv += "<button type='button' class='btn btn-xs btn-info pull-left btnOK' data-dismiss='modal'>"+ rimMessage.Ok +"</button>";
				modalDiv += "<button type='button' class='btn btn-xs btn-default btnCancel' data-dismiss='modal'>"+ rimMessage.Cancel +"</button>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				$("body").append(modalDiv);
					
				var nowDate = new Date();
				var today = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate() , 0, 0, 0, 0);
				var startDate = new Date(nowDate.getFullYear(), nowDate.getMonth(), nowDate.getDate() + 7, 0, 0, 0, 0);
				var maxMonth = 3;
				var maxLimitDate = new Date(nowDate.getFullYear(), nowDate.getMonth() + maxMonth, nowDate.getDate(), 0, 0, 0, 0);

				$("body").find("#expiredDate").val(rimCommon.getDate(startDate));
				
			}
		},
		Modal : function(id,title,page,hiddenCallback,shownCallBack) {

			if (showModalFlag == false) {

				showModalFlag = true;

				this.checkDivModal(id,title);
				var obj = $("#rimModal_" + id );
				obj.modal("show").find(".modal-body").append(page);

				obj.on('shown.bs.modal', function () {
					$(document.activeElement).blur();

					showModalFlag = false;

					if (typeof shownCallBack === "function") {
						shownCallBack();
					}
				});

				obj.on('hidden.bs.modal', function () {
					if (typeof hiddenCallback === "function") {
						hiddenCallback();
					}
					obj.remove();
					showModalFlag = false;
				});
				return obj;
			}
		},
		checkDivModal : function(id,title){
			var obj = $("#rimModal_" + id );

			if (obj.length == 0) {
				var modalDiv = "<div id='rimModal_"+id+"' class='modal fade rim-modal rim-modal-"+id+"' role='dialog' data-backdrop='static' data-keyboard='false' >";
				modalDiv += "<div class='modal-dialog'>";
				modalDiv += "<div class='modal-content'>";
				modalDiv += "<div class='modal-header modal-header-info' style='background-color: #fff;'>";
				modalDiv += "<button type='button' class='close' data-dismiss='modal'>&times;</button>";
				modalDiv += "<h5 class='modal-title'>"+ title +"</h5>";
				modalDiv += "</div>";
				modalDiv += "<div class='modal-body'>";
				modalDiv += " </div>";
				modalDiv += "<div class='modal-footer'>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				modalDiv += "</div>";
				$("body").append(modalDiv);
			}
		}
};