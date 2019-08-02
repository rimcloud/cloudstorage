function setRowShareIcon(pageClass,row){
	if( rimCommon.isEmpty(row.shareTp) ){
		return;
	}
	var selectedRow = pageClass.pageContent.find("#"+row.trId);

	if( row.shareTp == "LM" ||  row.shareTp == "ML" ){
		selectedRow.find(".rim-filetype").before("<i class='fa fa-sign-out rim-shareicon-overlay-top' > </i>");
		selectedRow.find(".rim-filetype").before("<i class='fa fa-link rim-shareicon-overlay-bottom' > </i>");
	}
	else if( row.shareTp == "L" ){
		selectedRow.find(".rim-filetype").before("<i class='fa fa-link rim-shareicon-overlay-bottom' > </i>");
	}
	else if( row.shareTp == "M"){
		selectedRow.find(".rim-filetype").before("<i class='fa fa-sign-out rim-shareicon-overlay-top' > </i>");
	}
}

function setRowCheckBoxEvent(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);
	selectedRow.find(".btn-rim-checkbox").on("change",function(e){
		if( pageClass.fileTable.getCheckedRowCount() > 0 ){
			rimActions.setCheckBoxEnabled(pageClass);
		}else{
			rimActions.setCheckBoxDisabled(pageClass);
		}
	});
}

function setRowClickFolderName(pageClass,row){

	var selectedRow = pageClass.pageContent.find("#"+row.trId);
	selectedRow.find(".rim-filename").on("click",function(e){

		if(row.fileType == "D"){
			pageClass.getList(row.path, row.storageId);
		}
	});
}

function setRowDblFileName(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);

	selectedRow.find(".rim-filename").on("dblclick",function(e){
		if(row.fileType == "F"){
			var postData = { rim_storage_type : getStorageType(getNowStorageId(pageClass)), rim_sid : row.storageId, rim_path : getNowPath(pageClass), rim_fids : row.fileId };
			
			JSRequest.download(getAjaxUrlDownload(postData.rim_sid), postData);
		}
	});
}

function setRowDblFileNameToAll(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);
	selectedRow.find(".rim-filename").on("dblclick",function(e){
			var postData = { rim_storage_type : getStorageType(getNowStorageId(pageClass)) , rim_sid : row.storageId, rim_path : row.path , rim_fids : row.fileId };
			JSRequest.download(getAjaxUrlDownload(postData.rim_sid), postData);
	});
}

function setRowReName(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);
	var checkEnter = false;

	selectedRow.find(".rim-rename").on("focusout",function(e){
		
		if (checkEnter == false) {
		
			$(document.activeElement).blur();
	
			var newName = $(this).val();
			var fileName = '';
	
			if ($.trim(newName).length <= 0) {
				JSDialog.Alert(rimMessage.AlertInputFileName);
				return;
			}
			if (row.name == newName) {
				selectedRow.find("a").removeClass("hidden").focus();
				$(this).addClass("hidden");
				$(document.activeElement).blur();
				
				return;
			}
			if(newName.lastIndexOf('.') > -1) {
				fileName = newName.substring(0, newName.lastIndexOf('.') );
			} else {
				fileName = newName;
			}
			if(rimCommon.isValidateName(fileName) == false ){
				JSDialog.Alert(fileName + rimMessage.ValidateName);
				return;
			}
	
			var postData = { rim_sid : getNowStorageId(pageClass), rim_path : getNowPath(pageClass), rim_fid: row.fileId, rim_re_nm : $(this).val() };
			
			selectedRow.find("a").removeClass("hidden").focus();
			$(this).addClass("hidden");
			
			ajaxRequest( getAjaxUrlReName( getNowStorageId(pageClass) ), postData, pageClass, function(res){
				var newRow = $.extend( row, res.data );
				this.renderRow(newRow);
			});
		}
	});

	selectedRow.find(".rim-rename").on("focus",function(e){
		var len =  $(this).val().lastIndexOf('.');
		if(len == -1 ){
			this.select();
		}else{
			this.setSelectionRange(0,len);
		}
	});

	selectedRow.find(".rim-rename").on("keydown",function(e){
		
		if(event.keyCode == 13 ){
			checkEnter = true;

			$(document.activeElement).blur();

			var newName = $(this).val();
			var fileName = '';

			if ($.trim(newName).length <= 0) {
				JSDialog.Alert(rimMessage.AlertInputFileName);
				return;
			}
			if (row.name == newName) {
				return;
			}
			if(newName.lastIndexOf('.') > -1) {
				fileName = newName.substring(0, newName.lastIndexOf('.') );
			} else {
				fileName = newName;
			}
			if(rimCommon.isValidateName(fileName) == false ){
				JSDialog.Alert(fileName + rimMessage.ValidateName);
				return;
			}

			var postData = { rim_sid : getNowStorageId(pageClass), rim_path : getNowPath(pageClass), rim_fid: row.fileId, rim_re_nm : $(this).val() };
			
			selectedRow.find("a").removeClass("hidden").focus();
			$(this).addClass("hidden");
			
			ajaxRequest( getAjaxUrlReName( getNowStorageId(pageClass) ), postData, pageClass, function(res){
				var newRow = $.extend( row, res.data );
				this.renderRow(newRow);
				
				checkEnter = false;
			});
		}
	});
}

function setRowTags(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);
	
	selectedRow.find(".rim-tags").editable({
		emptytext: "",
		placement : "right",
	    type: "text",
	    title: rimMessage.Tags,
	    tpl: "<input type='text' style='width: 300px' maxlength='200'>",
	    success: function(response, newValue) {
	    	var postData = { rim_sid : row.storageId , rim_fid : row.fileId , rim_search_tag : newValue };
			ajaxRequest( getAjaxUrlTags( getNowStorageId(pageClass) ) , postData , pageClass, function(res){
				selectedRow.find(".fa-tag").removeClass("hidden").attr("data-original-title", newValue);
	    		if( rimCommon.isEmpty(newValue) ){
	    			selectedRow.find(".fa-tag").addClass("hidden");
	    		}
			});
	    },
	    display: function (value, sourceData, response) {
	    	   $(this).html("");
	   	}
	});
}

function setRowVersion(pageClass,row){
	var selectedRow = pageClass.pageContent.find("#"+row.trId);

	selectedRow.find(".rim-version").on('shown', function(e, editable) {

		var layout = $("<div>").addClass("box box-version");
      	var body = $("<div>").addClass("box-body");
      	var title = $("<div>").addClass("box-header with-border text-center").append(  $("<small>").addClass("box-title") );
      	layout.append( title );

      	var postData = { rim_sid : getNowStorageId(pageClass) , rim_fid : row.fileId };
		ajaxRequest( getAjaxUrlVersionList( getNowStorageId(pageClass) ) , postData , pageClass, function(res){

			if(res.data.length == 0 ){
          		title.find("small").text(rimMessage.EmptyVersionList);
          	}
          	else{
          		title.append("<a id='deleteAll' data-fileId='"+ row.fileId +"' href='javascript:void(0);'  class='btn'><i class='fa fa-trash-o rim-fa'  data-toggle='tooltip' title='" + rimMessage.DeleteAll + "' ></i></a>");

          		for ( var idx in res.data ) {
        			var versionVO = res.data[idx];

        			var div = "<div id='"+ versionVO.versionNo +"' class='row' style='border-bottom: 1px solid #f4f4f4;'>";
              		div += "<small>"+ versionVO.displayCreateDate +"<code>"+ versionVO.createUserName +"</code></small>";
           			div += "<a id='download'  data-versionNo='"+ versionVO.versionNo +"' data-fileId='"+ versionVO.fileId +"' href='javascript:void(0);'  class='btn'><i class='fa fa-download rim-fa'  data-toggle='tooltip' title='" + rimMessage.Download + "' ></i></a>";
              		div += "<a id='restore' data-versionNo='"+ versionVO.versionNo +"' data-fileId='"+ versionVO.fileId +"' href='javascript:void(0);'  class='btn'><i class='fa fa-undo rim-fa' data-toggle='tooltip' title='" + rimMessage.ReStore + "' ></i></a>";
              		div += "<a id='delete' data-versionNo='"+ versionVO.versionNo +"' data-fileId='"+ versionVO.fileId +"' href='javascript:void(0);'  class='btn'><i class='fa fa-trash rim-fa' data-toggle='tooltip' title='" + rimMessage.Delete + "' ></i></a>";
              		div += "</div>";
    				body.append(div);
        		}
         	}

			layout.append( body );

			pageClass.pageContent.find('.popover-content').prepend(layout).find("a").on('click', function(e) {
          		e.preventDefault();
          		var aHref 	 = $(this);
				var action 	 = aHref.attr("id");
				var versionNo= aHref.attr("data-versionNo");
				var fileId 	 = aHref.attr("data-fileId");

				if(action == "download" ){

					var postData = { rim_storage_type : getStorageType(getNowStorageId(pageClass)), rim_sid :  getNowStorageId(pageClass), rim_fid : fileId, rim_vno : versionNo  };
					
					JSRequest.download(getAjaxUrlVersionDownload(postData.rim_sid), postData);

					return;
				}
				var postData =  { rim_sid : getNowStorageId(pageClass), rim_vno : versionNo, rim_fid : fileId };
				if( action == "delete" || action == "deleteAll" ){
					JSDialog.Confirm( rimMessage.ConfirmDeleteVersion ,function(flag){
						if(flag){
							ajaxRequest( getAjaxUrlVersion( getNowStorageId(pageClass) ,action) , postData , this, function(res){
								if( action == "deleteAll"){
									pageClass.pageContent.find('.popover-content').find(".box-body").find("div").remove();
								}else if( action == "delete"){
									aHref.parent().remove();
								}

								if(pageClass.pageContent.find('.popover-content').find(".box-body").find("div").length == 0 ){
									pageClass.pageContent.find('.popover-content').find("#deleteAll").remove();
									pageClass.pageContent.find('.popover-content').find("h4").text(rimMessage.EmptyVersionList);
								}
							});
						}
					});
				}
				else if(action == "restore" ){
					ajaxRequest( getAjaxUrlVersion( getNowStorageId(pageClass) ,action) , postData , this, function(res){
						pageClass.pageContent.find('.popover-content').find(".editable-cancel").trigger("click");
					});
				}
			});
		});
      	pageClass.pageContent.find('.popover-content').find(".editable-submit").addClass("hide");
	});

	selectedRow.find(".rim-version").editable({
		emptytext: "",
		placement : "bottom",
	    type: "text",
	    title: rimMessage.Version,
	    tpl:  " ",
	    success: function(response, newValue) {

	    },
	    display: function (value, sourceData, response) {
	    	   $(this).html("");
	   	}
	});
}

function setRowFavorites(pageClass,row){
	 var selectedRow = pageClass.pageContent.find("#"+row.trId);
	 selectedRow.find(".favorites").on("click",function(event){
		var tr = $(this);
		var action = "delete";
		if(tr.hasClass("fa-star-o")){
			action = "add";
		}
		var postData = { rim_sid : getNowStorageId(pageClass) , rim_fid : row.fileId };
		ajaxRequest( getAjaxUrlBookMark( getNowStorageId(pageClass) ,action) , postData , pageClass, function(res){
			if(action == "add" ){
				tr.removeClass("fa-star-o").addClass("fa-star");
			}
			else{
				tr.removeClass("fa-star").addClass("fa-star-o");
			}
		});
	});
}

function setRowContextMenu(pageClass,row){
	 var selectedRow = pageClass.pageContent.find("#"+row.trId);
	 selectedRow.find(".fa-ellipsis-v").on("click",function(event){
		var e = jQuery.Event( "contextmenu" );
		e.pageX = event.pageX;
		e.pageY= event.pageY;
		$("#" + row.trId ).trigger(e);
	});
}

function setRowProperty(pageClass,row){
	 var selectedRow = pageClass.pageContent.find("#"+row.trId);

	 selectedRow.find(".rim-detail").on('shown', function(e, editable) {

			var layout = $("<div>").addClass("box box-detail");
          	var body = $("<div>").addClass("box-body");

         	var div = "<div class='row'>";
         	div += "<small>"+ rimMessage.FileName +" : "+ row.name +"</small>";
         	div += "</div>";
			body.append(div);

			if( row.fileType != "D" ){
				div = "<div class='row'>";
	         	div += "<small>"+ rimMessage.FileSize +" : "+ row.displaySize +"</small>";
	         	div += "</div>";
				body.append(div);
			}

			div = "<div class='row'>";

         	if (pageClass.bodyName == 'sharingin') {
         		div += "<small>"+ rimMessage.FilePath +" : "+ getNowPath(pageClass) +"</small>";
	 		} else {
	 			div += "<small>"+ rimMessage.FilePath +" : "+ rimCommon.getDirPath(row.path) +"</small>";
	 		}
         	div += "</div>";
			body.append(div);

			div = "<div class='row'>";
         	div += "<small>"+ rimMessage.ModifyDateTime +" : "+ row.displayModifyDate +"</small>";
         	div += "</div>";
			body.append(div);

			div = "<div class='row'>";
         	div += "<small>"+ rimMessage.CreateDateTime +" : "+ row.displayCreateDate +"</small>";
         	div += "</div>";
         	body.append(div);
         	
			layout.append( body );
			pageClass.pageContent.find('.popover-content').prepend(layout);
			pageClass.pageContent.find('.popover-content').find(".editable-submit").addClass("hide");
			
			if( row.fileType == "D" && pageClass.bodyName == 'all' ){
         	
         		var postData = { rim_sid : getNowStorageId(pageClass) , rim_fid : row.fileId };
        		ajaxRequest( getAjaxUrlFolderSubInfo( getNowStorageId(pageClass) ) , postData , pageClass, function(res){
        		
        			div = "<div class='row'>";
                 	div += "<small>"+ rimMessage.FolderSize +" : "+ res.data.displaySize +"</small>";
                 	div += "</div>";
                 	layout.find('.box-body').append(div);
                 	div = "<div class='row'>";
                 	div += "<small>"+ rimMessage.FolderCount +" : "+ res.data.subDirCount +"</small>";
                 	div += "</div>";
                 	layout.find('.box-body').append(div);
                 	div = "<div class='row'>";
                 	div += "<small>"+ rimMessage.FileCount +" : "+ res.data.subFileCount +"</small>";
                 	div += "</div>";         	
                 	layout.find('.box-body').append(div);	
        			
        		});        		
         	}         	

	});

	selectedRow.find(".rim-detail").editable({
		emptytext: "",
		placement : "bottom",
	    type: "text",
	    title: rimMessage.Property,
	    tpl:  " ",
	    success: function(response, newValue) {

	    },
	    display: function (value, sourceData, response) {
	    	   $(this).html("");
	   	}
	});
}
