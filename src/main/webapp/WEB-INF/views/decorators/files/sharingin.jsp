<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">	

var contentSharingIn = {	
	bodyName 	:"sharingin",
	fileTable 	: null,	
	pageContent : null,
	storageId 	: null,	
	enableDropZone : true,
	sharingInTopList : true, 
	
	clear : function(){
		this.fileTable.empty();
		this.storageId = null;	
	},
	switchContent : function(storageId,path){
		
		this.storageId = storageId;
					
	 	if(rimCommon.isEmpty(path)){
	 		this.getList("/");	
	 	}else{
	 		this.getList(path);	
	 	}
	},
	getList : function(path,storageId){
		
		if( !rimCommon.isEmpty(getNowStorageId(this)) ){
			this.storageId = getNowStorageId(this);
		}
		
		if(rimCommon.isEmpty(path) ){
			path = getNowPath(this);
		}
			
		rimActions.empty(this);
		
		if( path == "/" ){
			
			var postData = { rim_path : path , rim_sid : this.storageId };
			$.extend( postData, this.fileTable.getSearchObject() );
			
			ajaxRequest( getAjaxUrlSharingInTopList(this.storageId) , postData , this, function(res){
				this.enableDropZone = false;
				this.sharingInTopList = true;
				initBreadcrumb(this,res.data);
				initRow(this,res.data);
				this.pageContent.find("input[type=checkbox]").addClass("hidden");
				this.pageContent.find(".rim-table-files .rim-fa-up").empty().addClass("hidden");	
				this.pageContent.find(".rim-table-files .fa-level-down").addClass("hidden");
			});	
		}else{
			
			this.getSubList(path);
		}
	},
	getSubList : function(path){
		
		if( !rimCommon.isEmpty(getNowStorageId(this)) ){
			this.storageId = getNowStorageId(this);
		}

		if(rimCommon.isEmpty(path) ){
			path = getNowPath(this);
		}
		
		var postData = { rim_path : path , rim_sid : this.storageId };
		$.extend( postData, this.fileTable.getSearchObject() );
		
		ajaxRequest( getAjaxUrlSharingInSubList(this.storageId) , postData , this, function(res){
			this.enableDropZone = true;
			this.sharingInTopList = false;
			initBreadcrumb(this,res.data);
			initRow(this,res.data);
			this.pageContent.find("input[type=checkbox]").removeClass("hidden");
			this.initActions();
		});	
	},
    OnUploadSuccess : function(row){    
    	var nowPath = getNowPath(this);
    	var path = rimCommon.getDirPath(row.path);    	
    	if( nowPath != path ) {
    		var dirPath = rimCommon.getDirPath(path);
    		if( nowPath == dirPath ){
    			if( this.fileTable.isRowByPath(path) ){
    				return;
    			}
    			var postData = { rim_path : path , rim_sid : this.storageId };
    			ajaxRequestNoLoading( getAjaxUrlFileInfoByPath(this.storageId) , postData , this, function(res){
    				if(!rimCommon.isEmpty(res.data)){
    					this.addRow(res.data);
    				}
    			});
    		}
    		return;
    	}
    	this.addRow(row);
    },
	initTable : function(){		
		this.fileTable = new JSTable( this.pageContent.find("#rim_table_files") ,
				{ 
					totalNotify : "<spring:message code='table.folderinfo.notify' arguments='0;0;0;0' argumentSeparator=';' />",
					emptyMessage : "<spring:message code='table.file.empty.msg' />",
					enableCheckAll : true,
					enableSearchDate : false,
					enableSearchText : false,
					searchCombo : {"" :"<spring:message code='label.select.default' />" ,name :"<spring:message code='table.file.header.name' />" },
					enablePaging : false,
					pagingNotify : "",
					limit : 30,
					enableContextMenu : true,
					enableHeaderSort : true,
					Headers : [ 
							{ id : "name" , title : "<spring:message code='table.file.header.name' />" , width : "75%" , sort : true  } ,
							{ id : "size" , title : "<spring:message code='table.file.header.size' />" , width : "10%" ,sort : true  } ,
					    	{ id : "shareDate", title :  "<spring:message code='table.sharing.header.sharedate' />"  , width : "15%" , sort : true } 
						]
				},this.getList,this);
	},	
	reloadRow : function(self,fileId){
		var postData = { rim_sid : getNowStorageId(self), rim_fid : fileId, rim_path : getNowPath(self) };
		ajaxRequest( getAjaxUrlFileInfo( getNowStorageId(self) ) , postData , self, function(res){
			this.renderRow(res.data);
		});	
	},
	addRow : function(row){	
		this.renderRow(row);
	},
	removeRow : function(row){
		this.fileTable.removeRow(row);
	},
	renderRow : function(row) {

		row.icon = getIcon(row.fileType,row.name);
		
		row.trId = this.bodyName + "-" + row.fileId;
		
		var tr = "<tr class='text-nowrap text-center' " ;
		tr += "id='" + row.trId + "' ";
		
		tr += "JSData='"+ this.fileTable.getJsonToJSDataString(row)  +"' >";
		
		tr += "<td>";
		tr += "<input type='checkbox' class='btn-rim-checkbox' value='"+ row.fileId +"' data-trId='"+row.trId+"' >";
		tr += "</td>";
		
		tr += "<td class='text-left'>";
		
		var img = "<c:url value='/resources/images/filetype/"+ row.icon +".png' />"
		tr += "<span><img src='"+ img +"' class='rim-filetype' style='padding-left:8px;padding-right:5px;' ></span>";
			
		tr += "<a href='javascript:void(0);' class='rim-filename' >";
		tr += rimCommon.convertEscapeToHtml(row.name);
		tr += "</a>";
		tr += "<input type='text' class='hidden rim-rename' style='width:80%;'>";
		tr += '<a href="javascript:void(0);" class="rim-tags" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-search-tags" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-version" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-detail" data-type="text" ></a>';
		tr += "<i class='fa fa-tag rim-fa "+ (rimCommon.isEmpty(row.searchTag)?"hidden":"") +"' data-toggle='tooltip' title data-original-title='"+ row.searchTag +"' style='margin-left : 5px;'></i>";
		
		if(!rimCommon.isEmpty(row.shareUserNm)){
			tr += "<i class='fa fa-user rim-fa pull-right rim-row-right-info' style='padding-top: 5px;' data-toggle='tooltip' title data-original-title='"+ row.shareUserId +"' >" + row.shareUserNm + "</i>";
		}
		
		tr += "</td>";
		
		tr += "<td >";
		if( row.fileType != "D" ){
			tr += row.displaySize
		}
		tr += "</td>";
		
		tr += "<td >";
		
		if(rimCommon.isEmpty(row.displayShareDate)){
			tr += row.displayModifyDate;
		}else{
			tr += row.displayShareDate;
		}
		
		tr += "<div style='float: right;'><i class='fa fa-ellipsis-v rim-fa' style='cursor: pointer;' ></i></div>";
		tr += "</td>";

		this.fileTable.addRow(tr);
		setRowIconSharingOut(this,row);
		setRowCheckBoxEvent(this,row);
		setRowClickFolderName(this,row);
		setRowDblFileName(this,row);
		setRowReName(this,row);
		setRowTags(this,row);
		setRowFavorites(this,row);
		setRowVersion(this,row);
		setRowProperty(this,row);
		setRowContextMenu(this,row);
	},
	initActions : function(){
	
		var actions = [];
		actions.push( { id:"actNewFolder" 	, icon:"fa-folder"			, permission : "W" , checkbox : false , name : "<spring:message code='button.newfolder' />" });
		actions.push( { id:"actUpload" 	  	, icon:"fa-upload"			, permission : "W" , checkbox : false , name : "<spring:message code='button.fileupload' />" });
		actions.push( { id:"actDownload" 	, icon:"fa-download"		, permission : "R" , checkbox : true  , name : "<spring:message code='button.filedownload' />" });
		actions.push( { id:"actTrashbin" 	, icon:"fa-trash"			, permission : "W" , checkbox : true  , name : "<spring:message code='button.delete' />" });
		actions.push( { id:"actCut" 		, icon:"fa-cut"				, permission : "W" , checkbox : true  , name : "<spring:message code='button.cut' />" });
		actions.push( { id:"actCopy" 		, icon:"fa-copy"			, permission : "R" , checkbox : true  , name : "<spring:message code='button.copy' />" });
		actions.push( { id:"actPaste" 		, icon:"fa-paste"			, permission : "W" , checkbox : true  , name : "<spring:message code='button.paste' />" });
		
		rimActions.initButtons(this,actions);

		var permissions = getNowPermissions(this);
		rimActions.setPermission(this,permissions);
		rimActions.initPasteData(this);
		
	}
};
</script>

<div id="rim_page_sharingin" class="rim-page-sharingin dropzone hidden">

<h3 class="pull-right" style="padding-right:20px;font-size:15px;">
	<spring:message code='label.files.sharingin' /><small style="margin-left:10px;" id="rim_storage_type"></small>
</h3>
<section class="content-header">
	
    <ol class="breadcrumb">
    </ol>
	
	<div class="box rim-actions" style="height: 54px;">
		<div class="box-body">
		</div>		
	</div>
		
</section>

<section class="content"  >
 
 <div id="rim_table_files" class="rim-table-files" >
 
	 <div class="box listbox JSTable">
	
		<div class="box-body no-padding">
			
			 <div class="row search">
			 
			 	<span id="totalNotify"></span>
	
				<div class="pull-right" >
				
					<div class="btn-group search-date hidden" style="padding-right:10px;">
					  <div class="input-group">
					    <div class="input-group-addon">
					      <i class="fa fa-calendar"></i>
					    </div>
					    <input type="text" class="form-control input-sm" id="search_date" readonly='readonly'>
					  </div>
					</div>
					
					<div class="btn-group search-text hidden">
					
						 <select class="btn btn-default" id="search_column" name="search_column">
						 </select>
					
						<div class="btn-group" style="margin-left:10px;">
						   <input type="text" class="form-control input-sm" placeholder="Search" id="search_text" name="search_text" >
						</div>
						   
					</div>
					
					<button type="button" class="btn btn-default btn-sm hidden" id="btnSearch" name="btnSearch" ><i class="fa fa-search"></i></button>
					
				</div>
			</div> 
		        
			<div class="row table-responsive">
					
				<table class="table table-bordered table-striped table-hover">
					<thead>
					</thead>
					<tbody>	
				   </tbody>
				</table>
			
			</div>
		    
		</div>
		
		<div class="box-footer">
			<div class="JSTablePaging hidden">
				<div class="dataTables_info" id="pagingNotify" role="status" aria-live="polite"></div>
				<div class="pull-right">
					<ul class="pagination pagination-sm">
					</ul>
				</div>
			</div>
		</div>
	</div> 
 </div>

</section>
		
</div>
