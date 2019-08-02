<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	var modalCheckUpload = {
		id : "checkupload",
		rimModal : null,
		fileInfo : null,
		clear : function(){
			this.rimModal = null;
			this.fileInfo = null;
		},
		Modal : function(newFile,oldFile,pageClass,callback){
			var modal = this;
			
			modal.fileInfo = newFile;
			var div = $(".modal-"+ modal.id );
			modal.rimModal = JSDialog.Modal(this.id,"<spring:message code='label.exist.name'/>",div.html(),function(){
				callback.apply(pageClass,[modal.fileInfo , modal.rimModal.find("#checkall").is(":checked")] );
			});
			modal.addFileInfo(newFile,oldFile);
			modal.setEvent();
		},
		setEvent : function(){
	
			this.rimModal.find(".btn-overwrite").on("click",{ self : this } ,function(e){
				e.preventDefault();
				e.data.self.fileInfo.isRewrite = "Y";
				e.data.self.rimModal.find(".close").trigger("click");
				return false;
			});	
			
			this.rimModal.find(".btn-skip").on("click",{ self : this } ,function(e){
				e.preventDefault();
				e.data.self.fileInfo = null;
				e.data.self.rimModal.find(".close").trigger("click");
				return false;
			});	
			
			this.rimModal.find(".btn-rename").on("click",{ self : this } ,function(e){
				e.preventDefault();
				e.data.self.fileInfo.isRewrite = "N";
				e.data.self.rimModal.find(".close").trigger("click");
				return false;
			});	
			
			this.rimModal.find(".btn-cancel").on("click",{ self : this } ,function(e){
				e.preventDefault();
				e.data.self.fileInfo = null;
				e.data.self.rimModal.find(".close").trigger("click");
				return false;
			});
			
		},
		addFileInfo : function(newFile,oldFile){
			this.rimModal.find(".folder-notify").addClass("hidden");
			this.rimModal.find(".rim-fa").removeClass( "fa-folder");
			this.rimModal.find(".rim-fa").removeClass( "fa-file");
			this.rimModal.find("blockquote").remove();
			
			
			var isDir = (oldFile.fileType=="D")?true:false;
			var icon = (isDir) ? "fa-folder":"fa-file";
			
			if(isDir)this.rimModal.find(".folder-notify").removeClass("hidden");
			
			this.rimModal.find(".rim-fa").addClass(icon);
			this.rimModal.find(".box-title").text(newFile.name);
			
			var oldFileInfo = "<blockquote style='font-size:12px;'>";
			oldFileInfo += "<p>"+ oldFile.displayModifyDate +"</p>";
			if(!isDir){
				oldFileInfo +=	"<small>"+ oldFile.displaySize +"</small>";
			}
			oldFileInfo += "</blockquote>";
			this.rimModal.find(".rim-oldfile").append(oldFileInfo);
			
			var newFileInfo = "<blockquote style='font-size:12px;'>";
			if(isDir){
				newFileInfo += "<p>"+ rimCommon.getNowDateTime() +"</p>";
			}else{
				newFileInfo += "<p>"+ rimCommon.getNowDateTime(newFile.lastModified) +"</p>";
			}
			
			if(!isDir){
				newFileInfo += "<small>"+ rimCommon.byteCalculation(newFile.size) +"</small>";
			}
			newFileInfo += "</blockquote>";
			this.rimModal.find(".rim-newfile").append(newFileInfo);
		}
	};
</script>

<div class="modal-checkupload hidden">
	<div class="text-center">
		<h5><spring:message code='confirm.exist.upload'/></h5>
	</div>
	
	<div class="box box-solid">
            <div class="box-header with-border">
              <i class="fa rim-fa"></i>
              <h3 class="box-title"></h3>
              	<div class="folder-notify hidden">
					<code><spring:message code='label.upload.after.folder.all'/></code>
				</div>
            </div>
            <div class="box-body">
	             <div class="row">
		             <div class="col-sm-6 rim-newfile" >
		             	<h5><spring:message code='label.new.info'/></h5>
		             </div>
		             <div class="col-sm-6 rim-oldfile" >
		             	<h5><spring:message code='label.old.info'/></h5>
		             </div>
	             </div>
            </div>

      </div>
	  
	 <div class="checkbox text-right">
	    <label><input type="checkbox" id="checkall"> <spring:message code='label.upload.after.all'/></label>
	 </div>
	 
	 <div class="form-group input-group-button text-right" >
		   <button type='button' class='btn btn-xs btn-default btn-overwrite'><spring:message code='button.overwrite'/></button>
		   <button type='button' class='btn btn-xs btn-default btn-skip'><spring:message code='button.skip'/></button>
		   <button type='button' class='btn btn-xs btn-default btn-rename'><spring:message code='button.rename'/></button>
		   <button type='button' class='btn btn-xs btn-info btn-cancel'><spring:message code='button.cancel'/></button>
	 </div>

</div>
