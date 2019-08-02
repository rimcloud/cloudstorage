var JSPopup = {

	height:600,
	width:600,
	toolbar:0,
	scrollbars:0,
	status:0,
	resizable:1,
	left:0,
	top:0, 
	center:0, 
	createnew:1, 
	location:0,
	menubar:0, 

	open : function(url,name,settings,onUnload,postKey,postData){

		$("#popup_shared_data").val("");

		url = url + "&postKey=" + postKey;
		$("#popup_post_data").attr("data-"+postKey ,postData);

		if(settings.height)this.height = settings.height;
		if(settings.width)this.width = settings.width;
		if(settings.toolbar)this.toolbar = settings.toolbar;
		if(settings.scrollbars)this.scrollbars = settings.scrollbars;
		if(settings.status)this.status = settings.status;
		if(settings.resizable)this.resizable = settings.resizable;
		if(settings.left)this.left = settings.left;
		if(settings.top)this.top = settings.top;
		if(settings.center)this.center = settings.center;
		if(settings.createnew)this.createnew = settings.createnew;
		if(settings.location)this.location = settings.location;
		if(settings.menubar)this.menubar = settings.menubar;

		if (this.center == 1) {
			this.top = (screen.height-(this.height + 110))/2;
			this.left = (screen.width-this.width)/2;
		}

		parameters = "location=" + this.location + ",menubar=" + this.menubar + ",height=" + this.height + ",width=" + this.width + ",toolbar=" + this.toolbar + ",scrollbars=" + this.scrollbars  + ",status=" + this.status + ",resizable=" + this.resizable + ",left=" + this.left  + ",screenX=" + this.left + ",top=" + this.top  + ",screenY=" + this.top;

		var winObj = window.open(url, name, parameters);

		winObj.focus();

		if (onUnload) {

			var unloadInterval = setInterval(function() {

				if (!winObj || winObj.closed) {

					clearInterval(unloadInterval);
					if(onUnload){

						var data = $("#popup_shared_data").val();

						if( rimCommon.isEmpty(data) ) {
							onUnload.apply($(this),[ "" ]);
						} else {
							onUnload.apply($(this),[ JSON.parse($("#popup_shared_data").val()) ]);
						}
						$("#popup_post_data").removeAttr("data-"+postKey);
					}
				}
			},500);
		}
		if(winObj){
			winObj.focus();
		}
		return winObj;
	},
	setOpenerSharingData : function(data){
		$("#popup_shared_data", opener.document).val(JSON.stringify(data));
	},
	getOpenerSharingData : function(data){
		return JSON.parse($("#popup_shared_data", opener.document).val());
	},
	getOpenerPostData : function(postKey){
		return JSON.parse($("#popup_post_data", opener.document).attr("data-"+postKey));
	},
	removeOpenerPostData : function(postKey){
		return $("#popup_post_data", opener.document).removeAttr("data-"+postKey);
	}

};