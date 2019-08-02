
var JSProgressView = {
		notify : null,
		uploading : false,
		show : function(callFn){
			
			if(this.uploading == false){
				$(window).bind("beforeunload", beforeUnloadHandler);
				this.uploading = true;

			    this.notify = $.notify({
					icon: 'glyphicon glyphicon-upload',
					title: '0 / 0 ' + rimMessage.Uploading,
					message: rimMessage.DetailViewOpen
				},{
					element: 'body',
					position: null,
					type: "info",
					allow_dismiss: false,
					newest_on_top: false,
					showProgressbar: false,
					placement: {
						from: "bottom",
						align: "right"
					},
					offset: 30,
					spacing: 10,
					z_index: 1031,
					delay: 0,
					timer: 0,
					url_target: '',
					mouse_over: null,
					animate: {
						enter: 'animated fadeInDown',
						exit: 'animated fadeOutUp'
					},
					onShow: null,
					onShown: function() { callFn(); },
					onClose: null,
					onClosed: function(){ JSProgressView.uploading = false; JSProgressView.notify = null; },
					icon_type: 'class',
					template: '<div data-notify="container" class="col-xs-11 col-sm-6 alert alert-{0} jsprogressview-content" role="alert">' +
					'<button type="button" aria-hidden="true" class="close hidden" data-notify="dismiss" >×</button>' +
					'<span data-notify="icon"></span>' +
					'<span data-notify="title" class="upload-title" style="padding-left: 5px;" >{1}</span>' +
					'<span data-notify="speed" style="text-center "></span>' +
					'<a href="javascript:JSProgressView.doUploadModal();" data-notify="url"><span class="upload-message pull-right" data-notify="message">{2}</span></a>' +
					'<div class="modal-progress-view hidden">' +
						'<div class="box no-padding JSTable">' +
							'<div class="box-header">' +
								'<button type="button" class="btn btn-default btn-xs btn-upload-cancel pull-left" onclick="javascript:JSProgressView.uploadCancelAll();">' +rimMessage.UploadCancelAll +'</button>' +
								'<button type="button" class="btn btn-primary btn-xs btn-uploaded-clear pull-right" onclick="javascript:JSProgressView.uploadDoneClear();">' +rimMessage.UploadDoneClear +'</button>' +
							'</div>' +
							'<div class="box-body">' +
								'<table class="table table-bordered table-striped table-hover">' +
									'<thead>' +
										'<tr>' +
											'<th style="width:40%">' +rimMessage.FileName +'</th>' +
											'<th style="width:15%">' +rimMessage.UploadProcess +'</th>' +
											'<th style="width:15%">' +rimMessage.UploadSize +'</th>' +
											'<th style="width:15%">' +rimMessage.FileSize +'</th>' +
											'<th style="width:10%">' + '남은시간' +'</th>' +
											'<th style="width:5%"></th>' +
										'</tr>' +
									'</thead>' +
									'<tbody>' +
									'</tbody>' +
								'</table>' +
							 '</div>' +
						  '</div>' +
						 '</div>' +
					 '</div>'
				});
			}else{
				
				callFn();
			}
		},
		uploadCancelAll : function(){
			this.notify.$ele.find(".box-body table>tbody>tr").find("i").each(function(index) {
				 if($(this).hasClass("fa-close")){
					 $(this).trigger("click");
				 }
			});
			this.updateView();
		},
		uploadDoneClear : function(){
			this.notify.$ele.find(".box-body table>tbody>tr").each(function(index) {
				if( $(this).hasClass("rim-complete") ){
					$(this).remove();
				}
			});
			this.updateView();
		},
		doUploadModal : function(){
			var view = this.notify.$ele.find(".modal-progress-view");
			if(view.hasClass("hidden")){
				view.removeClass("hidden");
				this.notify.update("message",rimMessage.DetailViewClose);
			}
			else{
				view.addClass("hidden");
				this.notify.update("message",rimMessage.DetailViewOpen);
			}
			this.updateView();
		},
		addItem : function(uploader){
			JSProgressView.show(function(){				
				
			});
			var progress = "<tr id='"+ uploader.id  +"' >";
			progress += "<td style='text-align: left;' data-toggle='tooltip' title='"+ uploader.postData.targetUri +"'>"+uploader.file.name +"</td>";
			progress += "<td><div class='progress'><div class='progress-bar' style='width: 0%'></div></div></td>";
			progress += "<td>0</td>";
			progress += "<td>"+ uploader.byteCalculation(uploader.file.size)  +"</td>";
			progress += "<td>0</td>";
			progress += "<td><i class='fa rim-fa fa-close' style='cursor: pointer;'></i></td>";			
			progress += "</tr>";

			JSProgressView.notify.$ele.find(".box-body table>tbody").append(progress);

			var tr = JSProgressView.notify.$ele.find("#" + uploader.id);
			tr.find("i").on("click",function(event){
	      		 if($(this).hasClass("fa-close")){
      				uploader.cancel();
	      			tr.remove();
	      			JSProgressView.updateView();
	      		 }
	  	 	});

			JSProgressView.updateView();
			
		},
		updateView : function(){
			JSProgressView.show(function(){
				var complateSize = JSProgressView.notify.$ele.find(".box-body table>tbody .rim-complete").size();
				var totalSize = JSProgressView.notify.$ele.find(".box-body table>tbody>tr").size();

				if( complateSize == totalSize ){
					JSProgressView.notify.update("title",complateSize  + ' / ' + totalSize + rimMessage.UploadDone );
						JSProgressView.hide();
				}
				else{
					JSProgressView.notify.update("title",complateSize  + ' / ' + totalSize + rimMessage.Uploading );
				}
			});	
			
		},
		updateItem : function(uploader,Percentage,current,max,speed,seconds_remaining){
			
			JSProgressView.show(function(){
				var tr = JSProgressView.notify.$ele.find("#" + uploader.id);

				tr.find(".progress-bar").css("width",Percentage+"%").html("<span>" + Percentage + "%</span>");
				if(Percentage >= 100){
					tr.find(".progress-bar").css("width","100%").html("<span>100%</span>");
			    }

				tr.find("td:eq(2)").html(current);
				tr.find("td:eq(3)").html(max);
				tr.find("td:eq(4)").html(seconds_remaining);	
				JSProgressView.notify.update("speed", " [" + speed + "]");
			});

			
		},
		successItem : function(uploader){
			JSProgressView.show(function(){
				var tr = JSProgressView.notify.$ele.find("#" + uploader.id);

				tr.addClass("rim-complete").find("i").removeClass("fa-close").addClass("hidden");

				JSProgressView.updateView();	
			});				
		},
		failItem : function(uploader){
			JSProgressView.show(function(){
				var tr = JSProgressView.notify.$ele.find("#" + uploader.id);
				tr.find(".progress-bar").css("width","0%");
				JSProgressView.updateView();		
			});
			
		},
		cancelItem : function(uploader){
			JSProgressView.updateView();
		},
		hide : function(){
			$(window).unbind("beforeunload", beforeUnloadHandler);
			$.notifyClose("all");
		}
};

function beforeUnloadHandler(){
    return rimMessage.RefreshMsg;
}

var JSProgress = {

	uploaderQueue : [],
	isUploading : false,

   	addItem : function(uploader){
   		this.addQueue(uploader);

   		JSProgressView.addItem(uploader);

   		this.startUpload();
   	},
   	addQueue : function(uploader){
   		this.uploaderQueue.push(uploader);
   	},
   	getQueue : function(){
   		return this.uploaderQueue.shift();
   	},
   	getQueueSize : function(){
   		return this.uploaderQueue.size();
   	},
   	clearQueue : function(){
   		this.uploaderQueue = [];
   	},
   	reStartUpload : function(uploader){
   		uploader.isFailed = false;
   		uploader.isCanceled = false;
   		JSProgress.addQueue(uploader);
   		JSProgress.startUpload();
   	},
   	startUpload : function(){

   		if(this.isUploading)
   			return;

   	   this.isUploading = true;
   	   setTimeout(this.doUploading,500);

   	},
   	doUploading : function(){

		var uploader =  JSProgress.getQueue();
		if( uploader == undefined ){
			JSProgress.isUploading = false;
			return;
		}

		if( uploader.isFailed == true || uploader.isCanceled == true ){

		}else{
			uploader.start(function(row){

				JSProgressView.successItem(uploader);

				if( uploader.pageClass != null && uploader.pageClass != undefined ){
		    		if(typeof  uploader.pageClass.OnUploadSuccess === "function" ){
		    			 uploader.pageClass.OnUploadSuccess(row);
			    	}
		    	}

		    	JSProgress.doUploading();
		  	},function(row){

		  		JSProgressView.failItem(uploader);
		  		JSProgress.doUploading();
		  	},function(){

		  		JSProgressView.cancelItem(uploader);
		  		JSProgress.doUploading();
		  	});
		}
   	},
   	updateItem : function(uploader,Percentage,current,max,speed,seconds_remaining){
   		JSProgressView.updateItem(uploader,Percentage,current,max,speed,seconds_remaining);
   	}
};

var JSUploader = function(url,file,postData,pageClass){
	this.initialize(url,file,postData,pageClass);
};

JSUploader.prototype = {
		id : null,
		url : null,
		file : null,
		myXhr : null,
		postData : null,
		pageClass : null,
		startTime : null,
		lastLoadedByte : 0,
		isFailed : false,
		isCanceled : false,
		lastProgressTime : null,

		initialize: function(url,file,postData,pageClass) {
			this.id  = rimCommon.generateUUID();
			this.url = url;
			this.file= file;
			this.postData = postData;
			this.pageClass = pageClass;
			this.lastProgressTime = (new Date());
			JSProgress.addItem(this);
		},

		start : function(successCallBack,failedCallBack,CanceledCallback){
			var self = this;
			self.isFailed = false;
			self.isCanceled = false;

			var data = new FormData();
            data.append('rimUploadFile', this.file);

            $.each( self.postData , function( key, value ) {
            	data.append(key,value);
    		});

            self.startTime = (new Date());
            self.lastProgressTime = (new Date());

            $.ajax({
               url: this.url,
               method: 'post',
               data: data,
               dataType: 'json',
               async: true,
               processData: false,
               contentType: false,
               xhr: function() {
                   self.myXhr = $.ajaxSettings.xhr();
                   if(self.myXhr.upload){
                	   self.myXhr.upload.addEventListener('progress',function(e){ self.onProgress(e,self); }, false);
                	   self.myXhr.upload.addEventListener("load", function(e)   { self.onComplete(e,self); }, false);
                	   self.myXhr.upload.addEventListener("error", function(e)  { self.onFailed(e,self,failedCallBack); }, false);
                	   self.myXhr.upload.addEventListener("abort", function(e)  { self.onCanceled(e,self,CanceledCallback); }, false);
                   }
                   return self.myXhr;
           	},
            success: function(data) {
               	  if( JSRequest.isSuccess(data.status.result) ){
            		if(typeof successCallBack === "function" ){
            			successCallBack(data.data);
			    	}
               	  }
               	  else{
               		if(typeof failedCallBack === "function" ){
               			failedCallBack(data.data);
			    	}
               		JSDialog.Alert(data.status.message);
		    	  }
              }
            });
	},
	onProgress : function(e,self){

 	   if(e.lengthComputable){

			var loaded = e.loaded;
			var total = e.total;
			var percentage = Math.round( ( loaded / total ) * 100 );

			var seconds_update = ( new Date().getTime() - self.lastProgressTime.getTime() )/1000;
			if( seconds_update < 0.5 && percentage < 100 ){
				return;
			}				
			this.lastProgressTime = (new Date());
			
			var recieved = loaded;
           
			var seconds_elapsed = ( new Date().getTime() - self.startTime.getTime() )/1000;
            
			var bytes_per_second =  seconds_elapsed ? loaded / seconds_elapsed : 0;
			var remaining_bytes =   total - loaded;
			var seconds_remaining = seconds_elapsed ? Math.round(remaining_bytes / bytes_per_second) : "calculating";            
            var hour = parseInt(seconds_remaining/3600);
    		var min = parseInt((seconds_remaining%3600)/60);
    		var sec = seconds_remaining%60;
                        
            JSProgress.updateItem(this,percentage,this.byteCalculation(recieved),this.byteCalculation(total),this.byteCalculation(bytes_per_second) + "/s",hour + ":" + min + ":" + sec);
 	   }
	},
	onComplete : function(e,self){
	},
	onFailed : function(e,self,failedCallBack){
		alert('onFailed => '+ self.file.name);
		self.isFailed = true;
		if(typeof failedCallBack === "function" ){
			failedCallBack();
		}
	},
	onCanceled : function(e,self,CanceledCallback){
		self.isCanceled = true;
		if(typeof CanceledCallback === "function" ){
			CanceledCallback();
		}
	},
	cancel : function(){
		if( this.myXhr != null ){
			this.myXhr.abort();
		}else{
			this.isCanceled = true;
		}
	},
    byteCalculation : function(bytes) {
        var bytes = parseInt(bytes);
        var s = ['B', 'KB', 'MB', 'GB', 'TB', 'PB'];
        var e = Math.floor(Math.log(bytes)/Math.log(1024));
       
        if(e == "-Infinity") return "0 "+s[0]; 
        else 
        return (bytes/Math.pow(1024, Math.floor(e))).toFixed(2)+" "+s[e];
   	},
   	getSpeed : function(loaded) {
   		var endTime = (new Date()).getTime();
   	    var duration = ( endTime - this.startTime) / 1000;
   	    var bitsLoaded = loaded * 8;
   	    var speedBps = (bitsLoaded / duration).toFixed(2);
   	    var speedKbps = (speedBps / 1024).toFixed(2);
   	    var speedMbps = (speedKbps / 1024).toFixed(2);

   	    var speedBytes = this.byteCalculation(speedBps/8);

   	    return  speedMbps + " Mbps";

   	}
};

function traverse_directory(entry,uploadItems) {
    var reader = entry.createReader();
    return new Promise(function(resolve_directory) {
        var iteration_attempts = [];
        (function read_entries() {
            reader.readEntries(function(entries){
                if (!entries.length) {
                    resolve_directory(Promise.all(iteration_attempts));
                } else {
                    iteration_attempts.push(Promise.all(entries.map(function (entry){
                        if (entry.isFile) {
                        	uploadItems.file_entries.push(entry);
                            return entry;
                        } else {
                        	uploadItems.dir.push(entry.fullPath);
                            return traverse_directory(entry,uploadItems);
                        }
                    })));
                    read_entries();
                }
            } );
        })();
    });
};

function entry_file(entry) {
   return new Promise(function(resolve_entry){
	   var iteration_attempts = [];
	   (function read_entry() {
		   entry.file(function(file){
			   iteration_attempts.push(file);
			   resolve_entry(Promise.all(iteration_attempts));
			});
	   })();
   });
};

var JSDropZone = {

	init : function(pageClass,uploadFileCallBack) {

		pageClass.pageContent.on('dragenter', function (e) {
			e.stopPropagation();
			e.preventDefault();
			$(this).removeClass("dragover");
		});

		pageClass.pageContent.on('dragleave', function (e) {
			e.stopPropagation();
			e.preventDefault();
			$(this).removeClass("dragover");
		});

		pageClass.pageContent.on('dragover', function (e) {
			e.stopPropagation();
			e.preventDefault();
			$(this).addClass("dragover");
		});

		pageClass.pageContent.on('drop', function (e) {
			e.preventDefault();
		    $(this).removeClass("dragover");

		    var uploadItems = { pageClass : pageClass , dir : [] , files : []  , file_entries : [] ,dropFiles : [] , dropFileNames : [] , fileTotalSize : 0 , nowPath : getNowPath(pageClass) };

		    var items = e.originalEvent.dataTransfer.items;
		     if(items){
		    	 var dirs = [];

		    	 $.map(items, function (item) {
	     			var entry = item.webkitGetAsEntry();
	                if (entry.isFile) {
	                	var file = item.getAsFile();
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
	                else if (entry.isDirectory){
	                	 dirs.push(entry);
	                	 uploadItems.dropFileNames.push(entry.name);
	                	 uploadItems.dropFiles.push(entry);
	                }
		         });

		    	 if( dirs.length == 0 ){
					 uploadFileCallBack.apply(pageClass,[uploadItems]);
		    	 }
		    	 else{
		    		 $.map(dirs, function (dir,index) {
			    		 traverse_directory(dir,uploadItems).then(function(entries){
			    			 if(index == (dirs.length-1) ){
			    				 $.map(uploadItems.file_entries, function (entry,idx) {
			    					entry_file(entry).then(function(fileArray){
			    					  var file = fileArray[0];
			    					  file.nowPath =  getNowPath(pageClass);
			    				    	 if( file.nowPath != "/"){
			    				    		 file.fullPath = file.nowPath + entry.fullPath;
			    				    	 }
			    				    	 else{
			    				    		 file.fullPath =  entry.fullPath;
			    				    	 }
			    	                   uploadItems.files.push(file);
			    	                   uploadItems.fileTotalSize += file.size;
			    					   if(idx == (uploadItems.file_entries.length - 1 ) ) {
			    						   uploadItems.file_entries = [];
			    						   uploadFileCallBack.apply(pageClass,[uploadItems]);
			    						}
						        	});
			    				 });
				    		 }
			    		 });
			    	 });
		    	 }

		      }else{
		     	 var files = e.originalEvent.dataTransfer.files;
			         if(files.length < 1)
			             return;

			     for (var i = 0; i < files.length; i++) {
			    	 var file =  files[i];
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
			     uploadFileCallBack.apply(pageClass,[uploadItems]);
		      }
		});
	}
};
