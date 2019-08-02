function getNowPath(pageClass) {
	var data = pageClass.pageContent.find(".content-header .breadcrumb").find(
			".fa-home").attr("data-folderinfo");
	if (rimCommon.isEmpty(data)) {
		return "/";
	}
	data = JSCrypto.decryptByAES(data, "/");
	var folderInfo = JSON.parse(data);
	if (rimCommon.isEmpty(folderInfo.path)) {
		return "/";
	}
	return folderInfo.path;
}
function getNowPathHash(pageClass) {
	var data = pageClass.pageContent.find(".content-header .breadcrumb").find(
			".fa-home").attr("data-folderinfo");
	if (rimCommon.isEmpty(data)) {
		return "/";
	}
	data = JSCrypto.decryptByAES(data, "/");
	var folderInfo = JSON.parse(data);
	if (rimCommon.isEmpty(folderInfo.pathHash)) {
		return "/";
	}
	return folderInfo.pathHash;
}
function getNowPermissions(pageClass) {
	var data = pageClass.pageContent.find(".content-header .breadcrumb").find(
			".fa-home").attr("data-folderinfo");

	if (rimCommon.isEmpty(data)) {
		return "R";
	}
	data = JSCrypto.decryptByAES(data, "/");
	var folderInfo = JSON.parse(data);
	if (rimCommon.isEmpty(folderInfo.permissions)) {
		return "R";
	}
	return folderInfo.permissions;
}

function getNowStorageId(pageClass) {
	var data = pageClass.pageContent.find(".content-header .breadcrumb").find(
			".fa-home").attr("data-folderinfo");
	if (rimCommon.isEmpty(data)) {
		return "";
	}
	data = JSCrypto.decryptByAES(data, "/");
	var folderInfo = JSON.parse(data);
	return folderInfo.storageId;
}

function getNowFileId(pageClass) {
	var data = pageClass.pageContent.find(".content-header .breadcrumb").find(
			".fa-home").attr("data-folderinfo");
	if (rimCommon.isEmpty(data)) {
		return "0";
	}
	data = JSCrypto.decryptByAES(data, "/");
	var folderInfo = JSON.parse(data);
	if (rimCommon.isEmpty(folderInfo.fileId)) {
		return "0";
	}
	return folderInfo.fileId;
}

function initBreadcrumb(pageClass, data) {
	if (rimCommon.isEmpty(data.folderInfo.storageId)) {
		return false;
	} else {
		pageClass.pageContent.find(".content-header .breadcrumb").empty();
		var breadcrumbPath = "";
		var s = JSON.stringify(data.folderInfo);
		var enFolderInfo = JSCrypto.encryptByAES(s, "/");

		if (rimCommon.isEmpty(data.folderInfo.originStorageId)) {
			pageClass.pageContent
					.find(".content-header .breadcrumb")
					.append("<li><a href='javascript:void(0);' data-path='/' ><i class='fa fa-home' data-folderinfo='" + enFolderInfo + "'></i></a></li>");
		} else {
			pageClass.pageContent
					.find(".content-header .breadcrumb").append("<li><a href='javascript:void(0);' data-path='/' ><i class='fa fa-home' data-folderinfo='"
									+ enFolderInfo + "'></i></a><code>" + rimCommon.convertSystemSourceToHtml(data.folderInfo.originUserName) + "</code></li>");
		}

		if (!rimCommon.isEmpty(data.folderInfo.path)) {
			var arrPath = data.folderInfo.path.split("/");
			for (var i = 0; i < arrPath.length; i++) {
				var path = arrPath[i];
				if (rimCommon.isEmpty(path))
					continue;
				breadcrumbPath += "/" + path;
				pageClass.pageContent
						.find(".content-header .breadcrumb")
						.append("<li><a href='javascript:void(0);' data-path='" + JSCrypto.encryptByAES(breadcrumbPath,"/")
										+ "' ><B>" + rimCommon.convertSystemSourceToHtml(path) + "</B></a></li>");
			}
		}
	}

	pageClass.pageContent.find(".content-header .breadcrumb").find("a").on('click', function(e) {
		var path = JSCrypto.decryptByAES($(this).attr("data-path"), "/");

		if (rimCommon.isEmpty(path)) {
			path = "/";
		}

		pageClass.getList(path);
	});

	rimActions.setPermission(pageClass, getNowPermissions(pageClass));

	return true;
}

function initRow(pageClass, data) {

	pageClass.fileTable.empty();

	initTotalNotify(pageClass, data);

	initTablePaging(pageClass, data.totalSize);

	for ( var idx in data.files) {
		var row = data.files[idx];
		pageClass.renderRow(row);
	}
}

function initTotalNotify(pageClass, data) {
	var notifyFolderInfo = rimMessage.NotifyFolderInfo;
	if (rimCommon.isEmpty(data.folderSubInfo)) {
		notifyFolderInfo = "";
	} else {
		notifyFolderInfo = notifyFolderInfo.replace("{0}", data.folderSubInfo.subDirCount);
		notifyFolderInfo = notifyFolderInfo.replace("{1}", data.folderSubInfo.subFileCount);
		notifyFolderInfo = notifyFolderInfo.replace("{2}", data.folderSubInfo.displaySize);
		notifyFolderInfo = notifyFolderInfo.replace("{3}", data.totalSize);
	}

	pageClass.fileTable.setTotalNotify(notifyFolderInfo);
}

function initTablePaging(pageClass, totalSize) {
	pageClass.fileTable.setTotalPage(totalSize);
}


function getIcon(fileType, name) {
	var icon = "folder";
	if (fileType != "D") {
		icon = rimCommon.getIconByName(name);
	}
	return icon;
}


