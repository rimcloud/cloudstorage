var UploadQueueData = (function(){ 
	
	var postURL;
	var postData;
	var trId;
	var file;	
	
	function UploadQueueData(trId,file,postURL,postData){
		this.trId = trId;		
		this.file = file;
		this.postURL = postURL;
		this.postData = postData;
	}

	UploadQueueData.prototype = { 
			
		getTrId : function(){
			return this.trId;
		},
		
		getFile : function(){
			return this.file;
		},
		
		getName : function(){
			return this.file.name;
		},
		
		getSize : function(){
			return this.file.size;
		},
		
		getFullPath : function(){
			return this.file.fullPath;
		},
		
		getMimeType : function(){
			return this.file.type;
		},
		
		getPostData : function(){
			return this.postData;
		},
		
		getPostURL : function(){
			return this.postURL;
		}
		
	}
	
	return UploadQueueData; 
	
})();

var Uploader = (function(){ 
	
	var myXhr;	
	var uploadQueueData;	
	var startTime;
	var lastProgressTime;
	
	function Uploader(uploadQueueData){
		this.uploadQueueData = uploadQueueData;
	}

	function onProgress(e,cbProgress,uploadFileDlg){
		if(typeof cbProgress === "function" ){			
			if(e.lengthComputable){
				
				var loaded = e.loaded;
				var total = e.total;
				var percentage = Math.round( ( loaded / total ) * 100 );

				var seconds_update = ( new Date().getTime() - this.lastProgressTime.getTime() )/1000;
				if( seconds_update < 1 && percentage < 100 ){
					return;
				}				
				this.lastProgressTime = (new Date());
				
				var recieved = loaded;
               
				var seconds_elapsed = ( new Date().getTime() - this.startTime.getTime() )/1000;
                
				var bytes_per_second =  seconds_elapsed ? loaded / seconds_elapsed : 0;
                var remaining_bytes =   total - loaded;
                var seconds_remaining = seconds_elapsed ? Math.round(remaining_bytes / bytes_per_second) : "calculating";
                
                if(bytes_per_second > total)
                	bytes_per_second = total;
                
                cbProgress.apply(uploadFileDlg,[ this.uploadQueueData,total,recieved,percentage,bytes_per_second,seconds_remaining ]);                 
	 	   }
		}		  
	}
	
	function onComplete(e,cbComplete,uploadFileDlg){
		if(typeof cbComplete === "function" ){
			cbComplete.apply(uploadFileDlg,[this.uploadQueueData]);
		}
	}
	
	function onError(e,cbError,uploadFileDlg) {
		if(typeof cbError === "function" ){
			cbError.apply(uploadFileDlg,[this.uploadQueueData]);
		}
	}
	
	function onAbort(e,cbAbort,uploadFileDlg) {
		if(typeof cbAbort === "function" ){
			cbAbort.apply(uploadFileDlg,[this.uploadQueueData]);
		}
	}
	
	function onSuccess(data,cbSuccess,uploadFileDlg) {
		if(typeof cbSuccess === "function" ){
			cbSuccess.apply(uploadFileDlg,[this.uploadQueueData,data]);
		}
	}
	
	Uploader.prototype = { 
		
			postAjax : function(uploadFileDlg,cbProgress,cbSuccess,cbComplete,cbAbort,cbError){
				
				var thisObj = this;
	
				var formData = new FormData();
				formData.append("rimUploadFile", this.uploadQueueData.file);

	            $.each( this.uploadQueueData.postData , function( key, value ) {
	            	formData.append(key,value);
	    		});

	            this.startTime = (new Date());
	            this.lastProgressTime = (new Date());

	            var promise = $.ajax({
	               url: this.uploadQueueData.postURL,
	               method: "POST",
	               data: formData,
	               dataType: "json",
	               async: true,
	               processData: false,
	               contentType: false,
	               cache: false,
	               xhr: function() {
	            	   thisObj.myXhr = $.ajaxSettings.xhr();
	                   if(thisObj.myXhr.upload){
	                	   thisObj.myXhr.upload.addEventListener("progress",function(e) { onProgress.apply(thisObj, [e,cbProgress,uploadFileDlg]); }, false);
	                	   thisObj.myXhr.upload.addEventListener("load",    function(e) { onComplete.apply(thisObj, [e,cbComplete,uploadFileDlg]); }, false);
	                	   thisObj.myXhr.upload.addEventListener("error",   function(e) { onError.apply(thisObj, [e,cbError,uploadFileDlg]); }, false);
	                	   thisObj.myXhr.upload.addEventListener("abort",   function(e) { onAbort.apply(thisObj, [e,cbAbort,uploadFileDlg]); }, false);
	                   }	                   
	                   return thisObj.myXhr;
	           		},	           		
	           		success: function(data) {
	            	  onSuccess.apply(thisObj, [data,cbSuccess , uploadFileDlg ]);	               	  
	           		}
	            });
	            
	        	return promise;
			},
			
			abort : function(){
				if( this.myXhr != null ){
					this.myXhr.abort();
				}
			}
	}
	
	return Uploader; 
	
})();

var UploadFileDlg = (function(){ 
	
	var timoutHide;
	var timoutHideCount;
	var $ele;
	var isShow;
		
	function UploadFileDlg(){
		this.timoutHide = null;
		this.timoutHideCount = 0;
		this.$ele = null;	
		this.isShow = false;
	}

	function getTemplate(msg) {
		var template = '<div class="row UploadFileDlg" style="display:none;font-size:10px;max-width:50%;min-width:50%;height: 400px;position:fixed;right:0px;bottom:0px;margin-bottom:10px;margin-right:20px;-webkit-transition: all 1s ease; -moz-transition: all 1s ease;-o-transition: all 1s ease; transition: all 1s ease;" >' +
		
		'<div class="detail" style="z-index:2;position: fixed;color:rgba(60,60,60,1);right:0px;margin-right:-140px;bottom:10px;-webkit-transition: all 1.25s ease; -moz-transition: all 1.25s ease; -o-transition: all 1.25s ease; transition: all 1.25s ease; cursor: pointer; text-align: left;letter-spacing: 1px;">' +		
		'<span  class="fa fa-upload" style="position: absolute; margin-top: 10px;left: 4px;"><span class="uploading-count" style="margin-right: 2px;">0/0</span> ' + msg.uploading + 
		'<span class="closing-count" style="margin-right: 2px;"></span><br><i class="fa fa-plus" style=" margin-top: 10px; text-align: center; ">' + msg.detail + '</i></span>' +
		'</div>' +
	    
        '<div class="col-md-12" style="padding:0;">' +
          '<div class="box">' +
            '<div class="box-header with-border">' +
             
             '<div class="total-label" style=" margin-top: 5px; margin-left : 10px;">' +
                 '<p class="label label-info"><i class="fa fa-upload" style="margin-right: 5px;"></i><span class="uploading-count" style="margin-right: 2px;">0/0</span>'+ msg.uploading +
			     '<span class="closing-count" style="margin-right: 2px;"></span></p>' +
			  '</div>' +
			    
              '<div class="box-tools pull-right">' +
                '<button type="button" class="btn btn-box-tool btn-close"><i class="fa fa-minus"></i></button>' +                
              '</div>' +              
             
            '</div>' +
            '<div class="box-body" style="height:280px;overflow-y:auto;" >' +
              '<table class="table table-bordered table-hover" >' +
                '<thead>' +
                '<tr>' +
                '<th style="text-align: center;">'+ msg.name +'</th>' +
                '<th style="text-align: center;width: 15%;">'+ msg.progress +'</th>' +
                '<th style="text-align: center;width: 15%;">'+ msg.size +'</th>' +
                '<th style="text-align: center;width: 10%;" >'+ msg.remaining +'</th>' +
                '<th style="text-align: center;width: 5%;" ></th>' +
                '</tr>' +
                '</thead>  ' +
                '<tbody>' +                                      
                '</tbody>' +
              '</table>' +
            '</div>' +
            '<div class="box-footer clearfix">' +      
             '<div class="pull-right box-tools">' +
                '<button type="button" class="btn btn-xs btn-default btn-sm daterange pull-right btn-success-clear">'+ msg.success_clear +'</button>' +
                '<button type="button" class="btn btn-xs btn-default btn-sm pull-right btn-cancel-all" style="margin-right: 5px;">'+ msg.cancel_all +'</button>' +
             '</div>' +               
            '</div>' +
          '</div>' +
        '</div>' +
       '</div>' +
        '<style type="text/css">' +
		'.UploadFileDlg .detail::after {' +
		'	  content: "";' +
		'	  display: block;' +
		'	  display: relative;' +
		'	  border-top: 40px solid #00c0ef;' +		
		'	  border-right: 140px solid #00c0ef;' +
		'	  border-bottom: 10px solid #00c0ef;' +
		'	  border-top-left-radius: 10px;' +
		'	  border-bottom-left-radius: 10px;' +	
		'	  opacity:0.2;' +	 
		'     filter: alpha(opacity=20); ' +	
		'	}' +
		'</style>';	
		 
		 return template;
	}
	
	function calcBytes(bytes) {
        var bytes = parseInt(bytes);
        var s = [ "B", "KB", "MB", "GB", "TB", "PB" ];
        var e = Math.floor(Math.log(bytes)/Math.log(1024));
       
        if(e == "-Infinity") return "0 "+s[0]; 
        else 
        return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];
	}
	
	function genUUID() {
        var d = new Date().getTime();
        var uuid = "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function(c) {
            var r = (d + Math.random()*16)%16 | 0;
            d = Math.floor(d/16);
            return (c=='x' ? r : (r&0x3|0x8)).toString(16);
        });
        return uuid.toUpperCase();
    }

	function show() {
		if(this.timoutHide != null ){
			clearInterval(this.timoutHide);
			this.$ele.find(".closing-count").text("");
			this.timoutHideCount = 0;
			this.timoutHide = null;
		}
		this.$ele.css("display","block");	
		this.isShow = true;
	}
	
	function hide(){
		if(this.timoutHide == null ){
			this.timoutHideCount = 5;
			this.timoutHide = setInterval(function() {
				this.timoutHideCount = this.timoutHideCount - 1;
				
				this.$ele.find(".closing-count").text(" (" + this.timoutHideCount + ")");
				
				if( this.timoutHideCount <= 0 ) {
					clearInterval(this.timoutHide);	
					this.$ele.find("table>tbody").empty();
					this.$ele.find(".closing-count").text("");
					this.$ele.css("display","none");
					this.timoutHideCount = 0;					
					this.timoutHide = null;	
					this.isShow = false;
				}				
			}.bind(this), 1000);
		}	
	}
	
	function onClickCancelAll() {
		
		this.$ele.find("table>tbody").find(".btn-cancel").each(function(index) {
			if($(this).hasClass("btn-cancel")){
				 $(this).trigger("click");
			 }
		});
	}
	
	function onClickSuccessClear() {
		this.$ele.find("table>tbody>tr").each(function(index) {
			if( $(this).hasClass("uploading-complete") ){
				$(this).remove();
			}
		});
		this.updateView();
	}
	
	function uploaderProgress(uploadQueueData,total,recieved,percentage,bytes_per_second,seconds_remaining){
		
		var findTr = this.$ele.find("#" + uploadQueueData.trId );	
		
		var progressBar = findTr.find(".progress-bar");
		progressBar.css("width", percentage + "%").attr("aria-valuenow", percentage + "%").find("span").text(calcBytes(bytes_per_second) + "/s");

		var hour = parseInt(seconds_remaining/3600);
		var min = parseInt((seconds_remaining%3600)/60);
		var sec = seconds_remaining%60;
		
		findTr.find(".seconds_remaining").text(  hour + ":" + min + ":" + sec );
		
	}
	
	function uploaderComplete(uploadQueueData){
	}
	
	function uploaderError(uploadQueueData) {
		this.updateView();
	}
	
	function uploaderAbort(uploadQueueData) {
		this.updateView();
	}
	
	function uploaderSuccess(uploadQueueData,data) {
		this.$ele.find("#" + uploadQueueData.getTrId()).addClass("uploading-complete");
		this.updateView();
	}
	
	UploadFileDlg.prototype = { 
			
		init : function(appendName,msg){

			var thisObj = this;
			
			$(appendName).append(getTemplate(msg));
			
			this.$ele = $(".UploadFileDlg");
			
			this.$ele.find(".detail").click(function() {
				thisObj.$ele.css("margin-right", "20px");
				thisObj.$ele.find(".detail").css("margin-right", "-140px");
			});

			this.$ele.find(".btn-close").click(function() {
				thisObj.$ele.css("margin-right", "-50%");
				thisObj.$ele.find(".detail").css("margin-right", "0px");
			});
			
			this.$ele.find(".btn-cancel-all").on("click",function(event){
				onClickCancelAll.apply(thisObj);
			});
			
			this.$ele.find(".btn-success-clear").on("click",function(event){
				onClickSuccessClear.apply(thisObj);
			});
		},		
		
		updateView : function(){
						
			var complateSize =  this.$ele.find("table>tbody .uploading-complete").size();
			var totalSize = this.$ele.find("table>tbody>tr").size();			
			if(complateSize == totalSize){
				hide.apply( this );
			}else if( totalSize > 0 ){			
				show.apply( this );
				
			}else {
				hide.apply( this );
			}	
			
			this.$ele.find(".uploading-count").text( complateSize + "/" + totalSize );
		},
		
		addFile : function(file){
			
			var id = genUUID();			
			var tr = '<tr id="' + id + '" >' +
             '<td style=""><strong>' + file.name + '</strong></td>' +
             '<td style="text-align: center;vertical-align: middle;">'+
               '<div class="progress" style="height: 15px; position:relative;" >  ' +
                 '<div class="progress-bar progress-bar-info" role="progressbar" style="width: 0%;" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" >' +
                  '<span style="position:absolute; left:0; width:100%; text-align:center; color:#000; line-height: 15px; height: 15px; font-size: 8px;font-weight: bold;">0/0bps</span>' +
                 '</div>' +  
               '</div> ' + 
             '</td>' +
             '<td style="text-align: center;vertical-align: middle;">' + calcBytes(file.size) + '</td>' +
             '<td class="seconds_remaining" style="text-align: center;vertical-align: middle;"></td>' +
             '<td style="text-align: center;vertical-align: middle;"><button type="button" class="btn btn-box-tool btn-cancel" ><i class="fa fa-times"></i></button></td>' +
             '</tr>';     
		
			this.$ele.find("table>tbody").append(tr);
		
			var thisObj = this;
			var findTr = this.$ele.find("#" + id );
			findTr.find(".btn-cancel").on("click",function(event){
	      		 if($(this).hasClass("btn-cancel")){	      	
	      			findTr.remove();
	      			thisObj.updateView();
	      		 }
	  	 	});
			
			this.updateView();
			return id;
		},
		
		startUploader : function(uploadQueueData,cbAlways){

			if( this.$ele.find( "#" + uploadQueueData.trId ).length > 0 ){
				var uploader = new Uploader(uploadQueueData);
				var promise = uploader.postAjax(this,uploaderProgress,uploaderSuccess,uploaderComplete,uploaderAbort,uploaderError).always(function(){
					if( typeof cbAlways === "function" ){
						cbAlways(10);
					}
				});
				
				var thisObj = this;
				var findTr = this.$ele.find("#" + uploadQueueData.trId );				
				findTr.find(".btn-cancel").off("click").on("click",function(event){
		      		 if($(this).hasClass("btn-cancel")){
		      			uploader.abort();
		      			findTr.remove();
		      			thisObj.updateView();
		      		 }
		  	 	});
			}	
			else {

				if( typeof cbAlways === "function" ){
					cbAlways(0);
				}	
			}
		},
		
		isUploading : function() {
			return this.isShow;
		}
		
		
	}
	return UploadFileDlg; 
	
})();



var UploadFileUtils = (function(){ 
	
	var inputClass;
	var dropZoneClass;
	var dragoverClass;	
	
	var uploadQueue;
	var uploadFileDlg;
	var isAllRewrite;
	var cbfnCheckFreeSpace;
	var cbfnCheckDupFile;
	var cbfnGetPostInfo;
	
	function UploadFileUtils( cbGetPostInfo ,cbCheckFreeSpace ,cbCheckDupFile ){
		this.uploadQueue = [];
		this.uploadFileDlg = new UploadFileDlg();	
		this.cbfnCheckFreeSpace = cbCheckFreeSpace;
		this.cbfnCheckDupFile = cbCheckDupFile;
		this.cbfnGetPostInfo = cbGetPostInfo;
		this.isAllRewrite = false;
	}
	
	function startUpload(timerId){
		
		if( timerId != null ){
			clearTimeout(timerId);
		}

		var uploadQueueData = this.uploadQueue.shift();
		if( uploadQueueData == null ){
			return;
		}
				
		var thisObj = this;
		this.uploadFileDlg.startUploader(uploadQueueData ,function(milliseconds){
			
			delete uploadQueueData;
			
			var timerId = setTimeout(function() { 
				startUpload.apply(thisObj , [timerId]);	
			}, milliseconds);
			
		});		
		
	}
	
	function calcUpload(uploadItems){
		
		if( typeof this.cbfnCheckFreeSpace === "function" ){	
			var queueFileSize = 0;
			for( var k = 0 ; k < this.uploadQueue.length ; k++ ){
				var uploadQueueData = this.uploadQueue[k];
				queueFileSize += uploadQueueData.getSize();
			}
			var fileTotalSize = queueFileSize + uploadItems.totalSize;
			if( this.cbfnCheckFreeSpace( fileTotalSize ) != true ) {
				return;
			}
		}
		
		
		for (var i = 0; i < uploadItems.files.length ; i++) {
						
			var file = uploadItems.files[i];		
			var isRewrite = false;
			if( typeof this.cbfnCheckDupFile === "function" ){

				if( this.isAllRewrite == true ){
					isRewrite = true;
				}else {
					var type = this.cbfnCheckDupFile(file);					
					if( type == 0 ){
						this.isAllRewrite = false;
						isRewrite = false;
					}else if( type == 1 ){
						this.isAllRewrite = false;
						isRewrite = true;
					}else if( type == 2 ){
						this.isAllRewrite = true;
						isRewrite = true;
					}else {
						this.isAllRewrite = false;
						isRewrite = false;
					}				
				}					
			}
			
			if(typeof this.cbfnGetPostInfo  === "function" ){
				
				var upInfo = this.cbfnGetPostInfo();
				upInfo.postData.isRewrite = (isRewrite==true)?"Y":"N";
				
				var trId = this.uploadFileDlg.addFile(file);		
				
				this.uploadQueue.push(new UploadQueueData(trId,file,upInfo.postURL,upInfo.postData));
				
			}else {
				break;
			}
		}
	    delete uploadItems;
	    
	    startUpload.apply(this);
	}
	 
	function entryFile(entry) {
	   return new Promise(function(resolve){		  		   
		   entry.file(function(file){
       		 resolve(file);
		  });
	   });
	}	
	
	function traverseDirectory(entry,uploadItems) {			
	  var reader = entry.createReader();
	  return new Promise(function(resolve, reject) {
	    var iterationAttempts = [];
	    function readEntries() {
	      reader.readEntries(function(entries){
	        if (!entries.length) {
	          resolve(Promise.all(iterationAttempts));
	        } else {
	          iterationAttempts.push(Promise.all(entries.map(function(ientry){
	            if (ientry.isFile) {
	              uploadItems.ientryFiles.push(ientry);
	              return ientry;
	            }
	            uploadItems.dirs.push(ientry);
	            return traverseDirectory(ientry,uploadItems);
	          })));
	          readEntries();
	        }
	      }, function(error){ reject(error); } );
	    }
	    readEntries();
	  });
	}
		
	UploadFileUtils.prototype = { 
	
		initUploadFileDlg : function(body,msg){
			this.uploadFileDlg.init(body,msg);
		},
	
		initInput : function(inputClass,reset){
	
			var thisObj = this;
			
			this.inputClass = inputClass;
	
			$(this.inputClass).on("change", function() {
				
				var uploadItems = { dirs : [] , files : [] , totalSize : 0  };					
		     	for (var i = 0; i < this.files.length; i++) {
		     		var theFile = this.files[i];			     		
		     		theFile.fullPath = "/" + theFile.name;
		     		uploadItems.totalSize += theFile.size;
					uploadItems.files.push(theFile);	 
		     	}
		     	calcUpload.apply(thisObj,[uploadItems]);
		     	
		     	if(reset){
					if (navigator.userAgent.toLowerCase().indexOf("msie") != -1  ){
						$(this).replaceWith( $(this).clone(true) );
				 	}
				 	else {
				 		$(this).val("");
					}
		     	}
			});	
			
		},
		
		initDropZone : function(dropZoneClass,dragoverClass){
			
			var thisObj = this;
			
			this.dropZoneClass = dropZoneClass;
			this.dragoverClass = dragoverClass;
			
			$(this.dropZoneClass).on("drop", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
				$(this).removeClass(dragoverClass);
				
				var dirs = [];		    	 
				var uploadItems = { dirs : [] , files : [] , totalSize : 0  , ientryFiles : [] };		
				
				if(e.originalEvent.dataTransfer.items){	
	
					var items = e.originalEvent.dataTransfer.items;		        
			     	for (var i = 0; i < items.length; i++) {
			     		var entry = items[i].webkitGetAsEntry();
						if (entry.isFile) {
							var theFile = items[i].getAsFile();
						    theFile.fullPath = "/" + theFile.name;
						  	uploadItems.totalSize += theFile.size;
							uploadItems.files.push(theFile);	
						}
						else if (entry.isDirectory){
							dirs.push(entry);											  	 
	       				}
			     	}    
			     	
			    	if( dirs.length == 0 ){
			    		calcUpload.apply(thisObj,[uploadItems]);	 
			    	}
			    	else {			        	
			    		 $.map(dirs, function (dir,index) {
			    			 traverseDirectory(dir,uploadItems).then(function(iterationAttempts){

				    			 if(index == (dirs.length-1) ){
				    				 $.map(uploadItems.ientryFiles, function (ientry,idx) {
				    					 entryFile(ientry).then(function(theFile){
				    					  theFile.fullPath = ientry.fullPath;
						            	  uploadItems.totalSize += theFile.size;
						            	  uploadItems.files.push(theFile);  				    	                   
				    					   if(idx == (uploadItems.ientryFiles.length - 1 ) ) {
				    						   delete uploadItems.ientryFiles;
				    						   calcUpload.apply(thisObj,[uploadItems]);	 
				    						}
							        	});
				    				 });
					    		 }
				    		 });
				    	 });						
			    	}	
			    	
			    	delete dirs;
			    	
				} else {
					
					var files = e.originalEvent.dataTransfer.files;		        
			     	for (var i = 0; i < files.length; i++) {
			     		var theFile = files[i];
			     		uploadItems.files.push(theFile);
				    	uploadItems.totalSize += theFile.size;	 
			     	}	
			     	calcUpload.apply(thisObj,[uploadItems]);
				}				
				return false;
		    }).on("dragstart", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
		       return false;
		    }).on("dragenter", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
				$(this).addClass(dragoverClass);
		        return false;
		    }).on("drag", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
		        return false;
		    }).on("dragend", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
		        return false;
		    }).on("dragleave", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
				$(this).removeClass(dragoverClass);
		        return false;
		    }).on("dragover", function (e) {
		    	e.stopPropagation();
				e.preventDefault();
		        return false;
		    });				
		},
		
		isUploading : function(){
			return this.uploadFileDlg.isUploading();
		}
	
	}
	return UploadFileUtils; 
})();
