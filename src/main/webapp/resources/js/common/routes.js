function isGroupStorage(storageId) {
	if (rimCommon.isEmpty(storageId) == false && storageId[0] == "@")
		return true;

	return false;
}

function getWebRoot() {
	return rimCommon.getWebRoot();
}

function getStorageType(storageId) {
	var activeContent = $("#rim_main_content").attr("data-active");
	if (activeContent == "sharingin") {
		return "PS";
	}

	if (isGroupStorage(storageId)) {
		return "GA";
	}
	return "PA";
}

function getPrefixAjaxUrl(storageId) {
	var activeContent = $("#rim_main_content").attr("data-active");
	if (activeContent == "sharingin") {
		return "si/";
	} else if (activeContent == "sharingout") {
		return "so/";
	}

	if (isGroupStorage(storageId)) {
		return "g/";
	}
	return "p/";
}

function getAjaxUrlEncrypt() {
	return getWebRoot() + "en/en.ros";
}

function getAjaxUrlDecrypt() {
	return getWebRoot() + "en/de.ros";
}

function getAjaxUrlAccountLogin() {
	return getWebRoot() + "account/login/checklogin.ros";
}

function getAjaxUrlAccountLogout() {
	return getWebRoot() + "account/logout.ros";
}

function getAjaxUrlFileList(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/list.ros";
}

function getAjaxUrlFileInfo(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/info.ros";
}

function getAjaxUrlFileInfoByPath(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/infobypath.ros";
}

function getAjaxUrlFolderSubInfo(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/foldersubinfo.ros";
}

function getAjaxUrlDownload(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/down.ros";
}

function getAjaxUrlDownloadFileLinkByToken() {
	return getWebRoot() + "sl/guest/downloadtoken.ros";
}

function getAjaxUrlCheckUploadSize(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/checkUploadSize.ros";
}

function getAjaxUrlCheckUpload(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/checkUpload.ros";
}

function getAjaxUrlUpload(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/upload.ros";
}

function getAjaxUrlReName(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/rename.ros";
}

function getAjaxUrlNewFolder(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/newfolder.ros";
}

function getAjaxUrlDelete(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/delete.ros";
}

function getAjaxUrlPaste(storageId, action) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "files/" + action + ".ros";
}

function getAjaxUrlStorageInfo(storageId) {

	if (isGroupStorage(storageId)) {
		return getWebRoot() + "g/files/storageusage.ros";
	}
	return getWebRoot() + "p/files/storageusage.ros";
}

function getAjaxUrlTags(storageId) {
	return getWebRoot() + "tag/update.ros";
}

function getAjaxUrlVersionList(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "v/list.ros";
}

function getAjaxUrlVersionListAll(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "v/listall.ros";
}

function getAjaxUrlVersion(storageId, action) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "v/" + action + ".ros";
}

function getAjaxUrlVersionDownload(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "v/down.ros";
}

function getAjaxUrlSharingInTopList(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "toplist.ros";
}

function getAjaxUrlSharingInSubList(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "sublist.ros";
}

function getAjaxUrlSharingOutList(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "list.ros";
}

function getAjaxUrlSharingOutFileInfo(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "fileinfo.ros";
}

function getAjaxUrlSharingOutInfo(storageId) {
	return getWebRoot() + "so/shareinfo.ros";
}

function getAjaxUrlSharingOutAdd(storageId) {
	return getWebRoot() + "so/add.ros";
}

function getAjaxUrlSharingOutUpdate(storageId) {
	return getWebRoot() + "so/update.ros";
}
function getAjaxUrlSharingOutDelete(storageId) {
	return getWebRoot() + "so/delete.ros";
}


function getAjaxUrlSharingLinksList(storageId) {
	return getWebRoot() + "sl/list.ros";
}

function getAjaxUrlSharingLinksFileInfo(storageId) {
	return getWebRoot() + "sl/fileinfo.ros";
}

function getAjaxUrlSharingLinksInfo(storageId) {
	return getWebRoot() + "sl/info.ros";
}

function getAjaxUrlSharingLinksAdd(storageId) {
	return getWebRoot() + "sl/add.ros";
}

function getAjaxUrlSharingLinksUpdate(storageId) {
	return getWebRoot() + "sl/update.ros";
}
function getAjaxUrlSharingLinksDelete(storageId) {
	return getWebRoot() + "sl/delete.ros";
}

function getAjaxUrlBookMark(storageId, action) {
	return getWebRoot() + "bm/" + action + ".ros";
}

function getAjaxUrlBookMarkList(storageId, action) {
	return getWebRoot() + "bm/list.ros";
}

function getAjaxUrlBookMarkFileInfo(storageId) {
	return getWebRoot() + "bm/info.ros";
}
function getAjaxUrlSearchFolderList(storageId) {
	return getWebRoot() + "s/folderlist.ros";
}

function getAjaxUrlSearchList(storageId) {
	return getWebRoot() + "s/list.ros";
}

function getAjaxUrlTrashBinList(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "t/list.ros";
}

function getAjaxUrlTrashBinReStore(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "t/restore.ros";
}

function getAjaxUrlTrashBinDelete(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "t/delete.ros";
}

function getAjaxUrlTrashBinClear(storageId) {
	return getWebRoot() + getPrefixAjaxUrl(storageId) + "t/clear.ros";
}

function getPopupUrl(storageId) {
	return getWebRoot() + "page/popup.do";
}

function getWebOfficePopupUrl(mode, storageId, fileId, name, isReadOnly, isNewFile) {
	var url = getWebRoot();

	if (mode == "edit") {
		url += "external/api/weboffice/openDoc.ros";
	}

	url += "?rim_sid=" + encodeURIComponent(storageId);
	url += "&rim_fid=" + fileId;
	url += "&rim_nm=" + encodeURIComponent(name);
	url += "&rim_is_read_only=" + isReadOnly;
	url += "&rim_is_new_file=" + isNewFile;

	return url;
}

function getAjaxUrlDeptList() {
	return getWebRoot() + "account/dept/list.ros";
}
function getAjaxUrlDeptSearchList() {
	return getWebRoot() + "account/dept/search.ros";
}

function getAjaxUrlEmpList() {
	return getWebRoot() + "account/emp/list.ros";
}
function getAjaxUrlEmpSearchList() {
	return getWebRoot() + "account/emp/search.ros";
}

function getAjaxUrlApiFolderList() {
	return getWebRoot() + "api/files/folderlist";
}
