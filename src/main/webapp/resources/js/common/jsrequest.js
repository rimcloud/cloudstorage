function ajaxRequest(url,data,pageClass,callback,loadingMsg,failCallBack,errorCallBack){
	if(rimCommon.isEmpty(loadingMsg) ){
		loadingMsg = rimMessage.LoadingProcessing;
	}
	JSRequest.postDataAjax(url, data ,loadingMsg,function(res){
		callback.apply(pageClass,[res]);
	},failCallBack,errorCallBack);
}

function ajaxRequestNoLoading(url,data,pageClass,callback,loadingMsg,failCallBack,errorCallBack){
	JSRequest.postDataAjax(url, data ,null,function(res){
		callback.apply(pageClass,[res]);
	},failCallBack,errorCallBack);
}

function ajaxRequestSyncNoLoading(url,data,pageClass,callback,loadingMsg,failCallBack,errorCallBack){
	JSRequestSync.postDataAjax(url, data ,null,function(res){
		callback.apply(pageClass,[res]);
	},failCallBack,errorCallBack);
}

var JSRequest = {

		lastPostDateTime : (new Date().getTime()),

		postFormAjax : function(actionURL,fromName,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("POST",actionURL, $("#" + fromName).serialize() , loadingMsg , successCallBack ,failCallBack, errorCallBack );
		},
		postDataAjax : function(actionURL,postData,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("POST",actionURL,postData, loadingMsg , successCallBack ,failCallBack, errorCallBack );
		},
		getFormAjax : function(actionURL,fromName,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("GET",actionURL, $("#" + fromName).serialize() , loadingMsg , successCallBack , failCallBack,errorCallBack );
		},
		getDataAjax : function(actionURL,postData,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("GET",actionURL,postData, loadingMsg , successCallBack ,failCallBack, errorCallBack );
		},
		reqAjax : function(method,actionURL, reqData , loadingMsg , successCallBack ,failCallBack, errorCallBack ){

			this.lastPostDateTime = (new Date().getTime());

			rimCommon.hideTooltipAll();

			var JSLoadingId = null;
			if(loadingMsg){ JSLoadingId = JSLoading.show(loadingMsg);}

			if(this.postAjaxObj){
			}

			this.postAjaxObj = $.ajax({
				type : method,
				dataType : "json",
				url : actionURL,
				data : reqData,
				success : function(data) {

					if(loadingMsg){ JSLoading.hide(JSLoadingId)};

					if( JSRequest.isSuccess(data.status.result) ){
						if(typeof successCallBack === "function"){
							successCallBack(data);
						}
					}
					else{
						if(typeof failCallBack === "function"){
							failCallBack(data);
						}else{
							JSDialog.Alert(data.status.message);
						}
					}
					delete this.postAjaxObj;
				},
				error : function(request, status, error) {

					if(loadingMsg){ JSLoading.hide(JSLoadingId)};

					var errmsg = "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error;
					if(request.status == "401"){
						JSDialog.Alert( rimMessage.FailSessionTimeout ,function(){
							location.href = rimCommon.getUrl("/index.do");
							delete JSRequest.postAjaxObj;
						});
					}
					else if(request.status == "20000"){
						location.href = rimCommon.getUrl("/index.do");

					}else{
						if(typeof errorCallBack === "function"){
							errorCallBack(errmsg);
						}
						else{
							JSDialog.Alert( rimMessage.FailMsg );
						}
						delete this.postAjaxObj;
					}

				}
			});
		 },
		 download : function(actionURL,postData){
			 var form = $('<form></form>').attr('action', actionURL).attr('method', 'post');

			Object.keys(postData).forEach(function(key){
				var value = postData[key];

				if(value instanceof Array) {
					value.forEach(function (v) {
						form.append($("<input></input>").attr('type', 'hidden').attr('name', key).attr('value', v));
					});
				} else {
					form.append($("<input></input>").attr('type', 'hidden').attr('name', key).attr('value', value));
				}
				});
				form.appendTo('body').submit().remove();
		},
		downloadToken : function(url,successCallBack){
			$.fileDownload(url)
				.done(function () {
					if(typeof successCallBack === "function"){
						successCallBack();
					}
				})
				.fail(function (response) {
					JSDialog.Alert(rimMessage.DownloadFailed,function(){
						if(response.indexOf("fnSubmitLogin") != -1 ){
							rimCommon.goUrl("/index.do");
						}
					});
				});
		},
		isSuccess : function(status){
			if( status == "SUCCESS")
				return true;
			return false;
		}
};

var JSRequestSync = {

		postFormAjax : function(actionURL,fromName,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("POST",actionURL, $("#" + fromName).serialize() , loadingMsg , successCallBack ,failCallBack, errorCallBack );
		},
		postDataAjax : function(actionURL,postData,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("POST",actionURL,postData, loadingMsg , successCallBack ,failCallBack, errorCallBack );
		},
		getFormAjax : function(actionURL,fromName,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("GET",actionURL, $("#" + fromName).serialize() , loadingMsg , successCallBack , failCallBack,errorCallBack );
		},
		getDataAjax : function(actionURL,postData,loadingMsg,successCallBack,failCallBack,errorCallBack){
			this.reqAjax("GET",actionURL,postData, loadingMsg , successCallBack ,failCallBack, errorCallBack );
		},
		reqAjax : function(method,actionURL, reqData , loadingMsg , successCallBack ,failCallBack, errorCallBack ){

			rimCommon.hideTooltipAll();

			var JSLoadingId = null;
			if(loadingMsg){ JSLoadingId = JSLoading.show(loadingMsg);}

			if(this.postAjaxObj){
			}

			this.postAjaxObj = $.ajax({
				type : method,
				dataType : "json",
				url : actionURL,
				data : reqData,
				async: false,
				timeout: 5000,
				success : function(data) {

					if(loadingMsg){ JSLoading.hide(JSLoadingId)};

					if( JSRequestSync.isSuccess(data.status.result) ){
						if(typeof successCallBack === "function"){
							successCallBack(data);
						}
					}
					else{
						if(typeof failCallBack === "function"){
							failCallBack(data);
						}else{
							JSDialog.Alert(data.status.message);
						}
					}
					delete this.postAjaxObj;
				},
				error : function(request, status, error) {

					if(loadingMsg){ JSLoading.hide(JSLoadingId)};

					var errmsg = "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error;
					if(request.status == "401"){
						JSDialog.Alert( rimMessage.FailSessionTimeout ,function(){
							location.href = rimCommon.getUrl("/index.do");
							delete JSRequestSync.postAjaxObj;
						});
					}else{
						if(typeof errorCallBack === "function"){
							errorCallBack(errmsg);
						}
						else{
							JSDialog.Alert(errmsg);
						}
						delete this.postAjaxObj;
					}
				}
			});
		},
		isSuccess : function(status){
			if( status == "SUCCESS")
				return true;
			return false;
		}
};
