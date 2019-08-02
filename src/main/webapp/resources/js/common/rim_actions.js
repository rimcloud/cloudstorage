var rimActions = {

		initButtons : function(pageClass,actions){
			for (var i = 0; i < actions.length; i++) {
				this.addButton(pageClass,actions[i]);
			}
			pageClass.pageContent.find(".rim-actions").find("a").on('click', { self : this } , function(e) {
				var actId = $(this).attr("id");
				e.data.self.onClickActionAll(pageClass,actId);
				e.data.self.onClickUpload(pageClass,actId);
				e.data.self.onClickDownload(pageClass,actId);
				e.data.self.onClickTrashBin(pageClass,actId);
				e.data.self.onClickCut(pageClass,actId);
				e.data.self.onClickCopy(pageClass,actId);
				e.data.self.onClickPaste(pageClass,actId);
				e.data.self.onClickRestore(pageClass,actId);
				e.data.self.onClickDelete(pageClass,actId);
				e.data.self.onClickDeleteAll(pageClass,actId);
			});
		},
		setPermission : function(pageClass,permissions){
			this.setReadOnly(pageClass,(permissions=="W")?false:true);
			this.setCheckBoxDisabled(pageClass);
		},
		setReadOnly : function(pageClass,flag){
			pageClass.pageContent.find(".rim-actions").find("a").addClass("disabled");
			if(flag){
				pageClass.pageContent.find(".rim-actions").find("a").each(function(index) {
					if($(this).attr("data-permission") == "R" ){
						$(this).removeClass("disabled");
					}
				});

			}else{
				pageClass.pageContent.find(".rim-actions").find("a").removeClass("disabled");
			}

			this.onClickActionAll(pageClass,null);

		},
		setCheckBoxEnabled : function(pageClass){
			pageClass.pageContent.find(".rim-actions").find("a").each(function(index) {
				if($(this).attr("data-checkbox") == "true" ){
					if( getNowPermissions(pageClass) == "R" && $(this).attr("data-permission") == "R"  ){
						$(this).removeClass("disabled");
					}else if( getNowPermissions(pageClass) == "W" ){
						$(this).removeClass("disabled");
					}
				}
			});

			this.onClickActionAll(pageClass,null);
		},
		setCheckBoxDisabled : function(pageClass){
			pageClass.pageContent.find(".rim-actions").find("a").each(function(index) {
				if($(this).attr("data-checkbox") == "true" ){
					$(this).addClass("disabled");
				}
			});

			this.onClickActionAll(pageClass,null);
		},
		empty : function(pageClass){
			pageClass.pageContent.find(".rim-actions").find(".box-body").empty();
		},
		addButton : function(pageClass,action){
			var actionBody = pageClass.pageContent.find(".rim-actions").find(".box-body");
			var ahref = "<a id='"+ action.id +"' href='javascript:void(0);' class='btn btn-default rim-btn-action disabled' data-permission='"+action.permission+"' data-checkbox='"+ action.checkbox +"' > <i class='fa rim-fa "+ action.icon +"'></i> "+ action.name +"</a>";
			actionBody.append(ahref);

			this.addNewFolder(pageClass,action);
			this.addUpload(pageClass,action);
		},
		addNewFolder : function(pageClass,action){
			if( action.id != "actNewFolder") return;

			pageClass.pageContent.find('#' + action.id ).on('shown', function(e, editable) {
				editable.setValue('');
				editable.input.$input.val("");
			});

			pageClass.pageContent.find('#'+ action.id).editable({
				emptytext: "<i class='fa "+ action.icon +" rim-fa'></i> "+ action.name,
				placement : "bottom",
			  	type : "text",
			    title: action.name,
			    tpl: "<input type='text' style='width: 150px' maxlength='100'>",
			    success: function(response, newValue) {
			    	if ($.trim(newValue).length <= 0) {
						JSDialog.Alert(rimMessage.AlertInputFileName);
						return;
					}
			    	if(rimCommon.isValidateName(newValue) == false ){
			    		JSDialog.Alert(newValue + rimMessage.ValidateName);
						return;
					}

			    	var postData = { rim_sid : getNowStorageId(pageClass) , rim_path : getNowPath(pageClass) , rim_folder_nm : newValue };
			    	ajaxRequest( getAjaxUrlNewFolder(getNowStorageId(pageClass)) , postData , pageClass, function(res){
			    		pageClass.addRow(res.data);
					});

			    },
			    display: function (value, sourceData, response) {
			    	   $(this).html("");
			   	}
			});
		},
		addUpload : function(pageClass,action){
			if( action.id != "actUpload") return;
			var actionBody = pageClass.pageContent.find(".rim-actions").find(".box-body");
			var inputBtn = "<input type='file' name='rimUploadFile' class='hidden btn-rim-upload' multiple>";
			actionBody.append(inputBtn);

			pageClass.pageContent.find(".rim-actions").find(".btn-rim-upload").on('change', function() {
				var uploadItems ={ dir : [] , files : [] , dropFiles : [] , dropFileNames : [] , fileTotalSize : 0 , nowPath : getNowPath(pageClass) };
				for (var i = 0; i < this.files.length; i++) {
			    	 var file =  this.files[i];
			    	 file.nowPath = getNowPath(pageClass);
			    	 if( file.nowPath != "/"){
			    		 file.fullPath = file.nowPath + "/" +  file.name;
			    	 }
			    	 else{
			    		 file.fullPath =  file.nowPath + file.name;
			    	 }
			    	 uploadItems.files.push(file);

			    	 uploadItems.fileTotalSize += file.size;
	                 uploadItems.dropFileNames.push(file.name);
	                 uploadItems.dropFiles.push(file);
			     }
				var postData = { rim_sid : getNowStorageId(pageClass), rim_t_uri : getNowPath(pageClass), rim_fsize : uploadItems.fileTotalSize };
				ajaxRequestNoLoading( getAjaxUrlCheckUploadSize(postData.rim_sid) , postData , this, function(res){
					rimActions.uploadStart(pageClass,uploadItems);
				});
				if (navigator.userAgent.toLowerCase().indexOf("msie") != -1  ){
					$(this).replaceWith( $(this).clone(true) );
			 	}
			 	else {
			 		$(this).val("");
				}
			});

		},
		uploadStart : function(pageClass,uploadItems,mode){

			var file = uploadItems.files.shift();
			if(file == undefined) return;

			var storageId = getNowStorageId(pageClass);

			var targetUri = rimCommon.getDirPath(file.fullPath);
			var postData = { rim_sid : storageId , rim_fsize : file.size, rim_t_uri : targetUri, rim_fnm : file.name };
			ajaxRequestNoLoading( getAjaxUrlCheckUpload(storageId) , postData , this, function(res){
	    		if( rimCommon.isEmpty(res.data) ){
	    			var JSUpload = new JSUploader( getAjaxUrlUpload(storageId) , file , {  targetUri : targetUri, storageId : storageId , isRewrite : "N" } , pageClass );
	    			this.uploadStart(pageClass,uploadItems);
	    		}else{
	    			if( !rimCommon.isEmpty(mode) ){
	    				var JSUpload = new JSUploader( getAjaxUrlUpload(storageId) , file , {  targetUri : targetUri, storageId : storageId , isRewrite : mode } , pageClass  );
    					this.uploadStart(pageClass,uploadItems,mode);
	    			}
	    			else{
		    			modalCheckUpload.Modal(file,res.data,this,function(fileInfo,checkAll){
		    				if( fileInfo != null ){
		    					var JSUpload = new JSUploader( getAjaxUrlUpload(storageId) , fileInfo , {  targetUri : targetUri, storageId : storageId , isRewrite : fileInfo.isRewrite } , pageClass );
		    					if(checkAll){
		    						this.uploadStart(pageClass,uploadItems,fileInfo.isRewrite);
		    					}else{
		    						this.uploadStart(pageClass,uploadItems);
		    					}

		    				}else{
		    					if(checkAll){
		    						uploadItems.files = [];
		    					}
		    					else{
		    						this.uploadStart(pageClass,uploadItems);
		    					}
		    				}

		    			});
	    			}
	    		}
	    	});
	    },
	    onClickActionAll : function(pageClass,id){
			if(rimCommon.isEmpty($("#page_shared_data").attr("data-clipboard"))){
				pageClass.pageContent.find(".rim-actions").find("#actPaste").addClass("disabled");
			}
		},
		onClickUpload : function(pageClass,id){
			if(id != "actUpload") return;
			pageClass.pageContent.find(".rim-actions").find(".btn-rim-upload").trigger("click");
		},
		onClickDownload : function(pageClass,id){
			if(id != "actDownload") return;
			
			var postData = { rim_storage_type : getStorageType(getNowStorageId(pageClass)), rim_sid : getNowStorageId(pageClass), rim_path : getNowPath(pageClass) , rim_fids : pageClass.fileTable.getCheckedRowValue()};
			JSRequest.download(getAjaxUrlDownload(postData.rim_sid), postData);
		},
		onClickTrashBin : function(pageClass,id){
			if(id != "actTrashbin") return;
			this.clearPasteData(pageClass);

			var deleteRows = pageClass.fileTable.getCheckedRowData();

			JSDialog.Confirm("[Total : " + pageClass.fileTable.getCheckedRowCount() + "]" + rimMessage.ConfirmDeleteFile ,function(flag){
				if(flag){
					var postData = { rim_sid : getNowStorageId(pageClass), rim_fids : pageClass.fileTable.getCheckedRowValue(), rim_path : getNowPath(pageClass) };
			    	ajaxRequest( getAjaxUrlDelete( getNowStorageId(pageClass) ) , postData , pageClass, function(res){
			    		for(var i = 0 ; i < deleteRows.length ; i++ ){
			    			pageClass.removeRow(deleteRows[i]);
			    		}
					});
				}
			});
		},
		initPasteData : function(pageClass){
			var permissions = getNowPermissions(pageClass);
		 	if( permissions == "W" || pageClass.bodyName == "all" ) {
		 		var data = $("#page_shared_data").attr("data-clipboard");
		 		if( rimCommon.isEmpty(data) ) return;
				var clipboard = JSON.parse(data);
				if( rimCommon.isEmpty(clipboard.action) ){
					pageClass.pageContent.find(".rim-actions").find("#actPaste").addClass("disabled").removeAttr("data-original-title");
				}else{
					pageClass.pageContent.find(".rim-actions").find("#actPaste").removeClass("disabled").attr("data-toggle","tooltip").attr("data-original-title",rimMessage.Paste + " " + rimMessage.TotalCount + " : " + clipboard.fileIds.split(',').length  );
				}
			}
		},
		clearPasteData : function(pageClass){
			$("#page_shared_data").attr("data-clipboard","");
			pageClass.pageContent.find(".rim-actions").find("#actPaste").addClass("disabled").removeAttr("data-original-title");
			this.onClickActionAll(pageClass,null);
		},
		onClickCut : function(pageClass,id){
			if(id != "actCut") return;
			var clipboard = { action : id , path : getNowPath(pageClass) , storageId : getNowStorageId(pageClass) , fileIds : pageClass.fileTable.getCheckedRowValue() , storageType : getStorageType(getNowStorageId(pageClass)) };
			$("#page_shared_data").attr("data-clipboard", JSON.stringify(clipboard) );
			pageClass.pageContent.find(".rim-actions").find("#actPaste").removeClass("disabled").attr("data-toggle","tooltip").attr("data-original-title",rimMessage.Paste + " " + rimMessage.TotalCount + " : " + pageClass.fileTable.getCheckedRowCount()  );
		},
		onClickCopy : function(pageClass,id){
			if(id != "actCopy") return;
			var clipboard = { action : id , path : getNowPath(pageClass) , storageId : getNowStorageId(pageClass) , fileIds : pageClass.fileTable.getCheckedRowValue() , storageType : getStorageType(getNowStorageId(pageClass)) };
			$("#page_shared_data").attr("data-clipboard", JSON.stringify(clipboard) );
			pageClass.pageContent.find(".rim-actions").find("#actPaste").removeClass("disabled").attr("data-toggle","tooltip").attr("data-original-title",rimMessage.Paste + " " + rimMessage.TotalCount + " : " + pageClass.fileTable.getCheckedRowCount() );
		},
		onClickPaste : function(pageClass,id){
			if(id != "actPaste") return;

			var clipboard = JSON.parse($("#page_shared_data").attr("data-clipboard"));

			if( clipboard.storageId ==  getNowStorageId(pageClass) && clipboard.path == getNowPath(pageClass) ){
				JSDialog.Alert(rimMessage.DuplicatePaste);
				return;
			}

			if( rimCommon.isEmpty(clipboard.action) ){
				pageClass.pageContent.find(".rim-actions").find("#actPaste").addClass("disabled").removeAttr("data-original-title");
				return;
			}

			if( clipboard.action == "actCut"){
				this.clearPasteData(pageClass);
				clipboard.action = "move";
			}
			else if( clipboard.action == "actCopy"){
				this.clearPasteData(pageClass);
				clipboard.action = "copy";
			}
			pageClass.pageContent.find(".rim-actions").find("#actPaste").focusout();

			var postData = { rim_s_sid : clipboard.storageId, rim_s_uri : clipboard.path, rim_t_sid : getNowStorageId(pageClass), rim_t_uri : getNowPath(pageClass), rim_fids :  clipboard.fileIds, rim_storage_type : clipboard.storageType };
			
			ajaxRequest( getAjaxUrlPaste(getNowStorageId(pageClass), clipboard.action), postData, pageClass, function(res){
				pageClass.getList(postData.rim_t_uri, postData.rim_t_sid);
			});
		},
		onClickRestore : function(pageClass,id){
			if(id != "actRestore") return;

			JSDialog.Confirm(rimMessage.ConfirmRestore,function(flag){
				if(flag) {
					var postData = { rim_sid : getNowStorageId(pageClass) , rim_fids : pageClass.fileTable.getCheckedRowValue() };
					ajaxRequest( getAjaxUrlTrashBinReStore( getNowStorageId(pageClass) ) , postData , pageClass, function(res){
						pageClass.getList();
					});
				}
			});
		},
		onClickDelete : function(pageClass,id){
			if(id != "actDelete") return;
			this.clearPasteData(pageClass);

			JSDialog.Confirm(rimMessage.ConfirmDeleteTrashBin,function(flag){
				if(flag) {
					var postData = { rim_sid : getNowStorageId(pageClass) , rim_fids : pageClass.fileTable.getCheckedRowValue() };
					ajaxRequest( getAjaxUrlTrashBinDelete( getNowStorageId(pageClass) ) , postData , pageClass, function(res){
						pageClass.getList();
					});
				}
			});
		},
		onClickDeleteAll : function(pageClass,id){
			if(id != "actDeleteAll") return;
			this.clearPasteData(pageClass);
			JSDialog.Confirm(rimMessage.ConfirmDeleteTrashBin,function(flag){
				if(flag) {
					var postData = { rim_sid : getNowStorageId(pageClass) };
					ajaxRequest( getAjaxUrlTrashBinClear( getNowStorageId(pageClass) ) , postData , pageClass, function(res){
						pageClass.getList();
					});
				}
			});
		}
};
