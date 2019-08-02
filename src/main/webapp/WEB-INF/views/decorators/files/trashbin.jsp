<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">

var contentTrashBin = {
	bodyName 	:"trashbin",
	fileTable 	: null,
	pageContent : null,
	storageId 	: null,
	enableDropZone : false,
	
	clear : function(){
		this.fileTable.empty(true);
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
	getList :function(path){
		
		if( !rimCommon.isEmpty(getNowStorageId(this)) ){
			this.storageId = getNowStorageId(this);
		}
		
		if(rimCommon.isEmpty(path) ){
			path = getNowPath(this);
		}
		
		var postData = { rim_path : path , rim_sid : this.storageId };
		
		$.extend( postData, this.fileTable.getSearchObject() );
		
		ajaxRequest( getAjaxUrlTrashBinList(this.storageId) , postData , this, function(res){
			initBreadcrumb(this,res.data);
			initRow(this,res.data);
		});	
		
		getLoginStorageInfo(getNowStorageId(this));
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
					enablePaging : true,
					pagingNotify : "<spring:message code='table.paging.notify'  arguments='0;0' argumentSeparator=';' />",
					limit : 50,
					enableContextMenu : false,
					enableHeaderSort : true,
					Headers : [
							{ id : "name" , title : "<spring:message code='table.file.header.name' />" , width : "75%" , sort : true } ,
							{ id : "size" , title : "<spring:message code='table.file.header.size' />" , width : "10%" , sort : true } ,
							{ id : "deleteDate", title :  "<spring:message code='table.trashbin.header.deletedate' />" , width : "15%" , sort : true }
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
	renderRow : function(row) {
		
		row.icon = getIcon(row.fileType,row.name);
		
		row.trId = this.bodyName + "-" + row.fileId;
		
		var tr = "<tr class='text-nowrap text-center' " ;
		tr += "id='" + row.trId + "' ";
		
		tr += "JSData='"+ this.fileTable.getJsonToJSDataString(row) +"' >";
		
		tr += "<td>";
		tr += "<input type='checkbox' class='btn-rim-checkbox' value='"+ row.fileId +"' data-trId='"+row.trId+"' >";
		tr += "</td>";
		
		tr += "<td class='text-left'>";
		
		var img = "<c:url value='/resources/images/filetype/"+ row.icon +".png' />"
		tr += "<span><img src='"+ img +"' class='rim-filetype' style='padding-left:8px;padding-right:5px;' ></span>";
		
		tr += "<a href='javascript:void(0);' class='rim-filename' data-toggle='tooltip' data-original-title='"+ rimMessage.ReStorePath + ":" + rimCommon.convertEscapeToHtml(row.originalPath) + "/" + row.originalName +"' >";
		tr += rimCommon.convertEscapeToHtml(row.originalName);
		tr += "</a>";
		tr += "<input type='text' class='hidden rim-rename' style='width:80%;'>";
		tr += '<a href="javascript:void(0);" class="rim-tags" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-search-tags" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-version" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-detail" data-type="text" ></a>';
		tr += "<i class='fa fa-tag rim-fa "+ (rimCommon.isEmpty(row.searchTag)?"hidden":"") +"' data-toggle='tooltip' title data-original-title='"+ row.searchTag +"' style='margin-left : 5px;'></i>";
		tr += "</td>";
		
		tr += "<td >";
		if( row.fileType != "D" ){
			tr += row.displaySize
		}
		tr += "</td>";
		
		tr += "<td >";
		tr += row.displayDeleteDate
		tr += "</td>";

		this.fileTable.addRow(tr);
		setRowCheckBoxEvent(this,row);
	},
	initActions : function(){
				
		var actions = [];

		actions.push( { id:"actRestore", 	icon:"fa-rotate-right", permission : "W", checkbox : true, name : "<spring:message code='button.restore' />" });
		actions.push( { id:"actDelete", 	icon:"fa-trash", 	permission : "W", checkbox : true, name : "<spring:message code='button.delete' />" });
		actions.push( { id:"actDeleteAll", 	icon:"fa-trash-o", 	permission : "W", checkbox : false, name : "<spring:message code='button.trashbin.empty' />" });
		
		rimActions.initButtons(this,actions);
	}
};
</script>

<div id="rim_page_trashbin" class="rim-page-trashbin dropzone hidden">

<h3 class="pull-right" style="padding-right:20px;font-size:15px;">
	<spring:message code='label.files.trashbin' /><small style="margin-left:10px;" id="rim_storage_type"></small>
</h3>
<section class="content-header">
	
	<ol class="breadcrumb">
	</ol>
	<div class="box rim-actions">
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
			<div class="JSTablePaging hidden" >
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
