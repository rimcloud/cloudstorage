$(document).ready(function() {
	if (typeof initPage === "function") {
		initPage();
	}

});
$(window).on("load", function() {
	setTimeout(function() {
		JSLoading.hide();
	}, 500);
});

var rimCommon = {

	getDomain : function() {
		var url = window.location.protocol + "//" + window.location.host;
		return url;
	},
	getWebRoot : function() {
		return $("#webroot").val() + "/";
	},
	getWebOfficeYn : function() {
		return $("#webofficeyn").val();
	},
	getUrl : function(url) {
		if (url.indexOf('/') == 0) {
			return this.getWebRoot() + url.substr(1);
		}
		return this.getWebRoot() + url;
	},
	goUrl : function(url) {
		location.href = rimCommon.getUrl(url);
	},
	logout : function(uid) {
		JSRequest.postDataAjax(getAjaxUrlAccountLogout(), "",
				"Logout...Please Wait...", function(data) {
					if (JSRequest.isSuccess(data.status.result)) {
						location.href = rimCommon.getUrl("/index.do");
					} else {
						JSDialog.Alert(data.status.message);
					}
				}, function(msg) {
					JSDialog.Alert(msg);
				});
	},
	hide : function(name) {
		$("." + name).hide();
	},
	show : function(name) {
		$("." + name).show();
	},
	isEmpty : function(text) {
		if (text == ""
				|| text == null
				|| text == undefined
				|| (text != null && typeof text == "object" && !Object
						.keys(text).length)) {
			return true;
		}
		return false;
	},
	getIconByExt : function(ext) {
		var extList = [ "doc", "docx", "hwp", "hwpx", "xls", "xlsx", "ppt", "pptx", "pdf" ];
		var extImgList = [ "gif", "png", "jpg", "jpeg" ];
		var extTextList = [ "txt" ];
		if (extList.indexOf(ext) != -1 || extImgList.indexOf(ext) != -1
				|| extTextList.indexOf(ext) != -1) {
			return ext;
		}
		return "file";
	},
	getIconByName : function(name) {
		var ext = name.substring(name.lastIndexOf('.') + 1, name.length)
				.toLowerCase();
		return this.getIconByExt(ext);
	},
	getParentPath : function(path) {
		return path.substring(0, path.lastIndexOf('/'));
	},
	generateUUID : function() {
		var d = new Date().getTime();
		var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
				function(c) {
					var r = (d + Math.random() * 16) % 16 | 0;
					d = Math.floor(d / 16);
					return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
				});
		return uuid;
	},
	byteCalculation : function(bytes) {
		var bytes = parseInt(bytes);
		var s = [ 'B', 'KB', 'MB', 'GB', 'TB', 'PB' ];
		var e = Math.floor(Math.log(bytes) / Math.log(1024));

		if (e == "-Infinity")
			return "0 " + s[0];
		else
			return (bytes / Math.pow(1024, Math.floor(e))).toFixed(0) + " "
					+ s[e];
	},
	getDirPath : function(fullPath) {
		if (fullPath === null)
			return '/';

		if (fullPath.indexOf("/") !== -1) {
			fullPath = fullPath.split('/');
			fullPath.pop();
			fullPath = fullPath.join('/');

			if (fullPath === '')
				return '/';

			return fullPath;
		}

		return "/";
	},
	hideTooltipAll : function() {
		$('[data-toggle="tooltip"], .tooltip').tooltip("hide");
	},
	isValidateName : function(name) {
		var stringRegx = /[/:*?"<>|\\]/gi;

		if (name == null || name.trim().length < 1) {
			return false;
		}

		if (stringRegx.test(name)) {
			return false;
		}

		if (name.substr(0, 1) == '.') {
			return false;
		}

		return true;
	},
	getDate : function(timestamp) {
		var d = null;
		if (timestamp == null || timestamp == undefined) {
			d = new Date();
		} else {
			d = new Date(timestamp);
		}

		var s = this.leadingZeros(d.getFullYear(), 4) + '-'
				+ this.leadingZeros(d.getMonth() + 1, 2) + '-'
				+ this.leadingZeros(d.getDate(), 2);
		return s;
	},
	getNowDateTime : function(timestamp) {
		var d = null;
		if (timestamp == null || timestamp == undefined) {
			d = new Date();
		} else {
			d = new Date(timestamp);
		}

		var s = this.leadingZeros(d.getFullYear(), 4) + '-'
				+ this.leadingZeros(d.getMonth() + 1, 2) + '-'
				+ this.leadingZeros(d.getDate(), 2) + ' ' +

				this.leadingZeros(d.getHours(), 2) + ':'
				+ this.leadingZeros(d.getMinutes(), 2) + ':'
				+ this.leadingZeros(d.getSeconds(), 2);
		return s;
	},
	leadingZeros : function(n, digits) {
		var zero = '';
		n = n.toString();

		if (n.length < digits) {
			for (i = 0; i < digits - n.length; i++)
				zero += '0';
		}
		return zero + n;
	},
	convertEscapeToHtml : function(str) {
		if (!rimCommon.isEmpty(str)) {
			str = str.replace(/&/g, "&amp;");
			str = str.replace(/ /g, "&nbsp;");
		}
		return str;
	},
	convertSystemSourceToHtml : function(str) {
		if (!rimCommon.isEmpty(str)) {
			str = str.replace(/</g, "&lt;");
			str = str.replace(/>/g, "&gt;");
			str = str.replace(/\"/g, "&quot;");
			str = str.replace(/\n/g, "<br />");
			str = str.replace(/&/g, "&amp;");
			str = str.replace(/ /g, "&nbsp;");
		}
		return str;
	}

};
