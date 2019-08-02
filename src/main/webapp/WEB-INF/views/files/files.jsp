<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>


<!DOCTYPE html>
<html>
<head>

<script src="<c:url value='/resources/js/files/row_event.js'/>"></script>
<script src="<c:url value='/resources/js/files/row_event_sharingin.js'/>"></script>
<script src="<c:url value='/resources/js/files/files.js'/>"></script>

<script type="text/javascript">
	
	var rimDrivePageContents = [];
	var rimTreeView = [];
	
 	function initPage(){
 		
 		rimDrivePageContents = [ contentAll , contentSharingIn , contentSharingOut , contentBookMark ,  contentSearch , contentTrashBin ] ;

 		for (var i = 0; i < rimDrivePageContents.length; i++) {
 			var pageClass = rimDrivePageContents[i]; 
 			pageClass.pageContent = $("#rim_main_content").find("#rim_page_" + pageClass.bodyName );
 			pageClass.initTable();
 			pageClass.initActions();

			if(pageClass.enableDropZone){
				JSDropZone.init(pageClass,function(uploadItems){
					
					if( !uploadItems.pageClass.enableDropZone ){
						return;
					}
					var postData = { rim_sid : getNowStorageId(uploadItems.pageClass), rim_t_uri : getNowPath(uploadItems.pageClass), rim_fsize : uploadItems.fileTotalSize };
					ajaxRequestNoLoading( getAjaxUrlCheckUploadSize(postData.rim_sid) , postData , this, function(res){
	 					rimActions.uploadStart(uploadItems.pageClass,uploadItems);
					});
				});
			}
		}

		var token = "${param.token}"; 
		
		if(!rimCommon.isEmpty(token)){
			
			ajaxRequestNoLoading( getAjaxUrlDecrypt() , { rim_token : token } , this , function(res){
				var data = res.data;
				var bodydName = "all"
				if( data.storageType == "PS"){
					bodydName = "sharingin";
				}
				switchContent(bodydName,data.storageId,data.path);
			});
		}
		else {
			switchContent("all",'${sessionScope.sessionVO.storageId}');
		}
		
 		$(".main-sidebar .sidebar-menu").find(".treeview a").on("click",function(e){
			if ( $(this).attr('href') == "#") {
				var stgId  = $(this).attr('data-storageid');
				var bodynm = $(this).attr('data-bodyname');	
				if( rimCommon.isEmpty(bodynm) ){
					bodynm = "all";
				}
				
				if( !rimCommon.isEmpty(stgId) ){
					if( !$(this).parents(".treeview").hasClass("active") ) {						
						switchContent( bodynm,$(this).attr('data-storageid') );											
					}
				}
			}			
		}); 
	}

	function switchContent(bodyName,storageId,path){
		
		$("#rim_main_content").find("[id^='rim_page_']").addClass("hidden");
		$("#rim_main_content").find("#rim_page_" + bodyName ).removeClass("hidden");
		$("#rim_main_content").attr("data-active",bodyName);
	
		$(".main-sidebar").find(".sidebar").find("li").removeClass("menu-selected");
		var name = bodyName;
		
		if( isGroupStorage(storageId) ){
			name ="groupid-" + storageId.replace("@","") + "-" + bodyName;
		}

		$(".main-sidebar").find(".sidebar ."+ name ).addClass("menu-selected");

		for (var i = 0; i < rimDrivePageContents.length ; i++) {
			var pageClass = rimDrivePageContents[i];
	
			pageClass.clear();
			pageClass.pageContent.find(".content-header .breadcrumb").find(".fa-home").attr("data-folderinfo","");
				
			if(pageClass.bodyName != bodyName){
				continue;
			}	
			
			var storageType = "<spring:message code='label.storage.personal'/>";
			if( isGroupStorage(storageId) ){
				
				if( pageClass.bodyName == "alldl" || pageClass.bodyName == "reqdl" || pageClass.bodyName == "resdl" || pageClass.bodyName == "reqdldownhist" || pageClass.bodyName == "reqdlapprhist"  ){
					storageType = "<code>"+ getGroupStorageInfo(storageId,"grpStrgNm") +"</code> <spring:message code='label.storage.group.dl'/>" ;
				}else{
					storageType = "<code>"+ getGroupStorageInfo(storageId,"grpStrgNm") +"</code> <spring:message code='label.storage.group'/>";
				}
			}
			pageClass.pageContent.find("#rim_storage_type").html(storageType);
			
			pageClass.switchContent(storageId,path);
		}	
	}	
	
	function getGroupStorageInfo(storageId,dataNm){
		if( rimCommon.isEmpty(storageId)  == false &&  storageId[0] == "@"  ){
			var name =".sidebar .groupid-" + storageId.replace("@","");
			return $(".main-sidebar").find(name).attr("data-" + dataNm.toLowerCase() );
		}
		return "";
	}
	
	function getStorageInfo(storageId,className){
		var postData = { rim_sid : storageId };
		ajaxRequestNoLoading( getAjaxUrlStorageInfo(storageId) , postData , this , function(res){
			$("." + className).find(".user-storage-progress").val(res.data.usedPercent + "%");
			$("." + className).find(".user-storage-notify").find("p").html("<spring:message code='label.free.size' arguments='"+ res.data.displayFreeSize +"' />");
			$("." + className).find(".user-storage-notify").find("span").html("<spring:message code='label.used.size' arguments='"+ res.data.displayUsedSize +"' />");
		});
	}
	
	function getLoginStorageInfo(storageId){
		if( '${sessionScope.sessionVO.storageId}' == storageId ){
			getStorageInfo(storageId,"main-sidebar");
		}
	}

	function setClipboard(data){
		if (window.clipboardData && window.clipboardData.setData) {
			if(  window.clipboardData.setData("Text", data ) ){
				JSDialog.Alert("<spring:message code='success.clipboard.copy'/><p><code>" + data + "</code></p>");
			}else{
				JSDialog.Alert("Copy to clipboard failed.");
			}
		} else if (document.queryCommandSupported && document.queryCommandSupported("Copy")) {
			var textarea = document.createElement("textarea");
			textarea.textContent = data;
			textarea.style.position = "fixed";
			document.body.appendChild(textarea);
			textarea.select();
			try {
				 if(document.execCommand("copy")){
					JSDialog.Alert("<spring:message code='success.clipboard.copy'/><p><code>" + data + "</code></p>");
				}else{
					JSDialog.Alert("Copy to clipboard failed.");
				}
			} catch (ex) {
				JSDialog.Alert("Copy to clipboard failed.");
			} finally {
				document.body.removeChild(textarea);
			} 
		}
	}
	
	function onClickContextMenu (event,menuItem){
		var tr  = menuItem.$el.find("#"+ menuItem.row.trId);
		var row = menuItem.row;
		var pageClass = menuItem.callbackObj;
		var storageId = getNowStorageId(pageClass);
		var storageType = getStorageType(storageId);
		var path = getNowPath(pageClass);
		var pathHash = getNowPathHash(pageClass);
		
        if( menuItem.mode == "rename" ){
			 tr.find("a").addClass("hidden");
			 tr.find(".rim-rename").removeClass("hidden").val(row.name).focus();
		}
		else if( menuItem.mode =="tag"){
			tr.find(".rim-tags").editable('setValue',tr.find(".fa-tag").attr("data-original-title") );
			tr.find(".rim-tags").editable('toggle');
		}
		else if( menuItem.mode =="download"){
			
			var postData = { rim_storage_type : storageType, rim_sid : storageId, rim_path : path, rim_fids : row.fileId };
			JSRequest.download(getAjaxUrlDownload(storageId), postData);
			
		}
		else if(menuItem.mode == "edit"){
			JSOFPopup.open(getWebOfficePopupUrl("edit", row.storageId , row.fileId, row.name, "N", "N"), row.name + " [edit mode]", "edit", {width : 800 , height : 600 , center : 1 , resizable : 0},function(winObj){});
		}
		else if(menuItem.mode == "sharing"){
			modalSharingOut.Modal(row,pageClass);
		}
		else if(menuItem.mode == "sharinglinks"){
			modalSharingLinks.Modal(row,pageClass);
		}
		else if(menuItem.mode == "version"){
			tr.find(".rim-version").attr("data-statetp","N").editable('toggle');
		}
		else if(menuItem.mode == "delete"){	
			
			JSDialog.Confirm( row.name + rimMessage.ConfirmDeleteFile ,function(flag){
				if(flag){
					var postData = { rim_sid : storageId, rim_path : path , rim_fids : row.fileId };
					ajaxRequest( getAjaxUrlDelete(storageId) , postData , pageClass, function(res){
						this.removeRow(row);
					});
				}
			});
		}
		else if(menuItem.mode == "detail"){
			tr.find(".rim-detail").editable('toggle');
		}
		else if( menuItem.mode == "copySharingIn" ){
			var clipboard = { action : "actCopy" , path : "/" , storageId : getNowStorageId(pageClass) , fileIds : row.fileId.toString() , storageType : getStorageType(getNowStorageId(pageClass)) };
			$("#page_shared_data").attr("data-clipboard", JSON.stringify(clipboard) );
		}
	}

</script>
	
</head>
<body>

<page:applyDecorator name="files_all"/>
<page:applyDecorator name="files_sharingin"/>
<page:applyDecorator name="files_sharingout"/>
<page:applyDecorator name="files_bookmark"/>
<page:applyDecorator name="files_search"/>
<page:applyDecorator name="files_trashbin"/>

<%@include file="popover_weboffice.jsp"%>
<%@include file="modal_sharingout.jsp"%>
<%@include file="modal_sharinglinks.jsp"%>
<%@include file="modal_checkupload.jsp"%>

</body>
</html>
