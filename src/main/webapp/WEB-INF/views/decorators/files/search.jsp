<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<script type="text/javascript">	

var contentSearch = {
	bodyName 	:"search",
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
		
		this.pageContent.find(".content-header .breadcrumb").empty();
		this.pageContent.find(".content-header .breadcrumb").append("<li><i class='fa fa-home' ></i></li>");
		this.pageContent.find('.btn-search-init').trigger("click");
		
		this.pageContent.find("#search-group-select").prop('disabled', 'disabled');
		this.pageContent.find("#search-storagetype-select").prop('disabled', 'disabled');
		
	},
	getList :function(){
		
		var postData = {};
		postData.rim_search_doc 				= this.pageContent.find("#search-doc-select").val();
		postData.rim_search_tags 			= this.pageContent.find("#search-tags-input").val();
		postData.rim_search_size 			= this.pageContent.find("#search-size-select").val();
		postData.rim_search_modify_date_start  = this.pageContent.find("#search-modifydate-start").val();
		postData.rim_search_modify_date_end 	= this.pageContent.find("#search-modifydate-end").val();
		postData.rim_search_owner_nm		= $.trim(this.pageContent.find("#search-ownerid-input").val());
		postData.rim_search_text 			= $.trim(this.pageContent.find("#search-text-input").val());
		
		$.extend( postData, this.fileTable.getSearchObject() );
		
		if(rimCommon.isEmpty(postData.search_pos)){
			if( rimCommon.isEmpty(postData.rim_search_doc) && 
				rimCommon.isEmpty(postData.rim_search_tags) && 
				rimCommon.isEmpty(postData.rim_search_size) &&
				rimCommon.isEmpty( this.pageContent.find("#search-modifydate-input").val() ) && 
				rimCommon.isEmpty(postData.rim_search_owner_nm) && 
				rimCommon.isEmpty(postData.rim_search_text) ) {
					JSDialog.Alert( "<spring:message code='fail.search.query' />");
					return;
			}
		}
		
		if( !rimCommon.isEmpty(postData.rim_search_text) && postData.rim_search_text.length < 2 ) {
			JSDialog.Alert("<spring:message code='fail.search.text.length' arguments='2' />");
			return;
		}
		
		ajaxRequest( getAjaxUrlSearchList(this.storageId), postData , this, function(res){
			initRow(this, res.data);
		});
	},
	initTable : function(){
		this.fileTable = new JSTable( this.pageContent.find("#rim_table_files") ,
		{ 
			totalNotify : " ",
			emptyMessage : "<spring:message code='table.search.empty' />",
			enableCheckAll : false,
			enableSearchDate : false,
			enableSearchText : false,
			searchCombo : {"" :"<spring:message code='label.select.default' />" ,name :"<spring:message code='table.file.header.name' />" },
			enablePaging : true,
			pagingNotify : "<spring:message code='table.paging.notify'  arguments='0;0' argumentSeparator=';' />",
			limit : 50,
			enableContextMenu : true,
			enableHeaderSort : true,
			Headers : [ 
				{ id : "name" , title : "<spring:message code='table.file.header.name' />" , width : "60%" , sort : true  } ,
				{ id : "size" , title : "<spring:message code='table.file.header.size' />" , width : "10%" ,sort : true  } ,
				{ id : "modify_dt", title :  "<spring:message code='table.file.header.modifydate' />"  , width : "15%" , sort : true } ,
				{ id : "modify_username", title :  "<spring:message code='table.search.header.modify.username' />"  , width : "15%" , sort : true }
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
		
		var tr = "<tr class='text-nowrap text-center' ";
		tr += "id='" + row.trId + "' ";
		
		tr += "JSData='"+ this.fileTable.getJsonToJSDataString(row) +"' >";

		tr += "<td class='text-left'>";
		var img = "<c:url value='/resources/images/filetype/"+ row.icon +".png' />"
		tr += "<span><img src='"+ img +"' class='rim-filetype' style='padding-left:8px;padding-right:5px;' ></span>";
		
		tr += "<a href='javascript:void(0);' class='rim-filename' data-toggle='tooltip' data-original-title='"+ rimCommon.getParentPath(row.path) +"' >";
		tr += rimCommon.convertEscapeToHtml(row.name);
		tr += "</a>";
		tr += "<input type='text' class='hidden rim-rename' style='width:80%;'>";
		tr += '<a href="javascript:void(0);" class="rim-tags" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-search-tags" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-version" data-type="text" ></a>';
		tr += '<a href="javascript:void(0);" class="rim-detail" data-type="text" ></a>';
		tr += "<i class='fa fa-tag rim-fa "+ (rimCommon.isEmpty(row.searchTag)?"hidden":"") +"' data-toggle='tooltip' data-original-title='"+ row.searchTag +"' style='margin-left : 5px;'></i>";
		tr += "</td>";
		
		tr += "<td >";
		if( row.fileType != "D" ){
			tr += row.displaySize
		}
		tr += "</td>";
		
		tr += "<td >";
		tr += row.displayModifyDate
		tr += "</td>";
		
		tr += "<td >";
		tr += row.modifyUserName;
		tr += "</td>";

		this.fileTable.addRow(tr);
	
		setRowShareIcon(this,row);
	
	},
	initActions : function(){
	
		var self = this;
		
		this.pageContent.find('.box-search input').keydown(function(e){
			if (e.keyCode == 13 && !this.checkKeyEvent) {
				this.checkKeyEvent = true;
				self.pageContent.find('.btn-search').trigger('click');
			}
		}).keyup(function(e){
			this.checkKeyEvent = false;
		});
		
		this.pageContent.find("#search-groupstorage-select").on("change", function(e) {
			self.pageContent.find("#search-pos-input").val("").attr("data-storageId","").attr("data-path","");
		});
		
		this.pageContent.find("#search-storagetype-select").on("change", function(e) {
			self.pageContent.find("#search-pos-input").val("").attr("data-storageId","").attr("data-path","");
		});
		
		this.pageContent.find('.btn-search-init').on("click", function(e) {
			self.pageContent.find('.content-header').find("input").val('');
			self.pageContent.find('.content-header').find("select").find('option:first').prop("selected", true);
			self.pageContent.find("#search-pos-input").attr("data-storageId","").attr("data-path","");
			self.pageContent.find("#search-group-select").trigger("change");
		});
		
		this.pageContent.find('.btn-search').on("click", function(e) {
			self.fileTable.clearValue();
			self.getList();
		});
		
		this.pageContent.find("#search-modifydate-input").on("click", function(e) {
			self.pageContent.find("#search-modifydate-select").val('6');
		});

		this.pageContent.find("#search-modifydate-select").on("change", function(e) {
			var selVal = $(this).val();
			var today = new Date();
			var startDate = '';
			var endDate = rimCommon.getDate(today);
			if(selVal == "") {
				startDate = "";
				endDate = "";
			} else if(selVal == "1") {
				startDate =  rimCommon.getDate(today.setDate(today.getDate() - 7));
			}  else if(selVal == "2") {
				startDate =  rimCommon.getDate(today.setMonth(today.getMonth() - 1));
			} else if(selVal == "3") {
				startDate =  rimCommon.getDate(today.setMonth(today.getMonth() - 3));
			} else if(selVal == "4") {
				startDate =  rimCommon.getDate(today.setMonth(today.getMonth() - 6));
			} else if(selVal == "5") {
				startDate =  rimCommon.getDate(today.setMonth(today.getMonth() - 12));
			}else if(selVal == "6") {
			
			}
			self.pageContent.find("#search-modifydate-input").val(startDate + ' ~ ' + endDate);
			self.pageContent.find("#search-modifydate-end").val(endDate);
			self.pageContent.find("#search-modifydate-start").val(startDate);
			if(rimCommon.isEmpty(startDate) || rimCommon.isEmpty(endDate) )
			{
				self.pageContent.find("#search-modifydate-input").val('');
				self.pageContent.find("#search-modifydate-end").val('');
				self.pageContent.find("#search-modifydate-start").val('');
			}
		});
	}
};
</script>

<div id="rim_page_search" class="rim-page-search dropzone hidden">

<h3 class="pull-right" style="padding-right:20px;font-size:15px;">
	<spring:message code='label.files.search' />
</h3>
<section class="content-header">
	
    <ol class="breadcrumb">
    </ol>

	<div class="box box-default box-search">
        <div class="box-header with-border">
          <h3 class="box-title"><i class="fa fa-search rim-fa"></i></h3>
          <a class="btn btn-xs btn-social btn-vk btn-search-init pull-right"><i class="fa fa-eraser"></i><spring:message code='button.search.init' /></a>
        </div>
        <div class="box-body">
         
          <div class="row form-group">
            <div class="col-sm-6">
             	<div class="col-sm-4 input-group"  style="float: inherit;">
					<span class="input-group-addon" id="search-group"><spring:message code='label.search.group' /></span>
					<select class="form-control" id="search-group-select" aria-describedby="search-group">
						<option value="P"><spring:message code='label.storage.personal' /></option>
					</select>
				</div>
            
            	<div class="col-sm-8 input-group pull-right search-storagetype-select">
					<select class="form-control" id="search-storagetype-select" >
						<option value="A"><spring:message code='label.files.files' /></option>
				  	</select>			
				</div>
		  	</div>
		  
		  	<div class="col-sm-6">
				<div class="input-group">
					<span class="input-group-addon" id="search-tags"><spring:message code='label.search.tags' /></span>
					<input type="text" class="form-control" id="search-tags-input" aria-describedby="search-tags" maxlength="100" >
				</div>
            </div>
          </div>
          
          <div class="row form-group">
            <div class="col-sm-6">
             	<div class="input-group">
				  <span class="input-group-addon" id="search-doc"><spring:message code='label.search.doc' /></span>
				  <select class="form-control" id="search-doc-select" aria-describedby="search-doc">
						<option value=""><spring:message code='label.all' /></option>
						<option value="hwp"><spring:message code='label.doc.hwp' /></option>
						<option value="xls,xlsx"><spring:message code='label.doc.excel' /></option>
						<option value="doc,docx"><spring:message code='label.doc.word' /></option>
						<option value="ppt,pptx"><spring:message code='label.doc.ppt' /></option>
						<option value="pdf"><spring:message code='label.doc.pdf' /></option>
				 </select>
				</div>
			</div>
			
            <div class="col-sm-6">
             	<div class="input-group">
					<span class="input-group-addon" id="search-size"><spring:message code='label.file.size' /></span>
					<select class="form-control" id="search-size-select" aria-describedby="search-size">
						<option value="" selected="selected"><spring:message code='label.all' /></option>
						<option value="1"><spring:message code='label.search.size.quota' arguments="0B,1MB"/></option>
						<option value="2"><spring:message code='label.search.size.quota' arguments="1MB,10MB"/></option>
						<option value="3"><spring:message code='label.search.size.quota' arguments="10MB,100MB"/></option>
						<option value="4"><spring:message code='label.search.size.quota' arguments="100MB,1GB"/></option>
						<option value="5"><spring:message code='label.search.size.quota' arguments="1GB,4GB"/></option>
					</select>
				</div>
            </div>
            
            
          </div>
          
          <div class="row form-group">
           <div class="col-sm-6">
             	<div class="input-group">
				  <span class="input-group-addon" id="search-ownerid"><spring:message code='table.search.header.modify.username' /></span>
				  <input type="text" class="form-control" id="search-ownerid-input" aria-describedby="search-ownerid" maxlength="50">
				</div>
            </div>
           
           
            <div class="col-sm-6">
             	<div class="col-sm-4 input-group" style="float: inherit;">
				  <span class="input-group-addon" id="search-modifydate"><spring:message code='table.file.header.modifydate' /></span>
				   <select class="form-control" id="search-modifydate-select" aria-describedby="search-modifydate">
						<option value="" selected="selected"><spring:message code='label.all' /></option>
						<option value="1"><spring:message code='label.one.week'/></option>
						<option value="2"><spring:message code='label.one.month'/></option>
						<option value="3"><spring:message code='label.three.month'/></option>
						<option value="4"><spring:message code='label.six.month'/></option>
						<option value="5"><spring:message code='label.one.year'/></option>
				 </select>
				</div>
				<div class="col-sm-8 input-group pull-right">
				  <input type="text" class="hidden" id="search-modifydate-start">
				  <input type="text" class="hidden" id="search-modifydate-end">
				  <input type="text" class="form-control disabled" id="search-modifydate-input" style="cursor: pointer;" readonly>
				</div>
            </div>
          </div>
          
          <div class="row form-group">
            <div class="col-sm-12">
             	<div class="input-group">
				  <span class="input-group-addon" id="search-text"><spring:message code='label.search.text' /></span>
				  <input type="text" class="form-control" id="search-text-input" aria-describedby="search-text" maxlength="100">
				</div>
            </div>  
          </div>
        </div>
        
        <div class="box-footer">
      		<a class="btn btn-xs btn-social btn-vk btn-search pull-right"><i class="fa fa-search"></i><spring:message code='button.search' /></a>
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
