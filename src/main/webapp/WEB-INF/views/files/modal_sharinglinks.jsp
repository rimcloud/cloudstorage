<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	var modalSharingLinks = {
		id : "sharinglinks",
		rimModal : null,
		row : null,
		clear : function(){
			this.rimModal = null;
			this.row = null;
		},
		Modal : function(row,pageClass){
			this.row = row;
			
			var postData = { rim_sid : row.storageId, rim_fid : row.fileId };
			
			ajaxRequest( getAjaxUrlSharingLinksInfo(this.storageId) , postData , this, function(res){
				this.rimModal = this.display(this,pageClass);
				this.setData(this,res.data);
			});	
		},
		display : function(self,pageClass){
			var div = $(".modal-"+ self.id );
			var rimModal = JSDialog.Modal(self.id,"<spring:message code='button.sharinglinks'/>",div.html(),function(){
				var fileId = self.row.fileId;
				self.clear();
				pageClass.reloadRow(pageClass,fileId);
			});
			
			rimModal.find(".modal-footer").css("text-align","left").append("<code>"+self.row.name+"</code>");
			
			rimModal.find(".btn-urllink").on("click",function(e){
				e.preventDefault();
				var url = self.rimModal.find("#link_url").val();
				if(rimCommon.isEmpty(url))
					return;
				setClipboard(url);
			});
			
			rimModal.find(".btn-share").on("click",function(e){
				e.preventDefault();
				self.setShare(self);
				return false;
			});	
			
			rimModal.find(".btn-updateshare").on("click",function(e){
				e.preventDefault();
				self.updateShare(self);
				return false;
			});	
			
			rimModal.find(".btn-unshare").on("click",function(e){
				e.preventDefault();
				self.setUnShare(self);
				return false;
			});
			
			return rimModal;
		},
		setData : function(self,data){
			if(rimCommon.isEmpty(data)){
				self.rimModal.find("#link_url").val('');
				self.rimModal.find("#shareId").val('');
				self.rimModal.find(".btn-unshare").addClass("hidden");
				self.rimModal.find(".btn-updateshare").addClass("hidden");
				self.rimModal.find(".btn-share").removeClass("hidden");
			}else{
				if(!rimCommon.isEmpty(data.token)){
					var url = rimCommon.getDomain() + "<c:url value='/page/guest/sl.do' />?rim_token="+ data.token;
					self.rimModal.find("#link_url").val(url);
					self.rimModal.find("#shareId").val(data.shareId);
					self.rimModal.find(".btn-share").addClass("hidden");
					self.rimModal.find(".btn-unshare").removeClass("hidden");
					self.rimModal.find(".btn-updateshare").removeClass("hidden");
				}
			}
		},
		setShare : function(self){
			
			var postData = { rim_sid : self.row.storageId, rim_fid : self.row.fileId };
			ajaxRequest( getAjaxUrlSharingLinksAdd(this.storageId) , postData , this, function(res){
				this.setData(this,res.data);
			});
		},
		setUnShare : function(self){
			var shareId = self.rimModal.find("#shareId").val();
			var postData = { rim_sid : self.row.storageId, rim_share_id : shareId };
			ajaxRequest( getAjaxUrlSharingLinksDelete(this.storageId) , postData , this, function(res){
				this.setData(this,res.data);
			});
			return false;
		}
	};
	
</script>


<div class="modal-sharinglinks hidden">
  <input type="hidden" id="shareId" name="shareId" value="">
  <div class="input-group-link">
	  <div class="input-group input-group-rim-btn">
	    <input type="text" class="form-control" readonly="readonly" id="link_url">
	    <div class="input-group-btn">
	      <button class="btn btn-default btn-urllink"><i class="fa fa-link"></i><spring:message code='button.link.copy'/></button>
	    </div>
	  </div>
	  
	  <div class="checkbox">
	    <label><input type="checkbox" id="accessPw" disabled="disabled"> <strike><spring:message code='label.password'/></strike></label>
	  </div> 
	  
	  <div class="checkbox">
	    <label><input type="checkbox" id="date" disabled="disabled"> <strike><spring:message code='label.expire.date'/></strike></label>
	  </div>
	  
	  <div class="checkbox">
	    <label><input type="checkbox" id="message" disabled="disabled"> <strike><spring:message code='label.share.message'/></strike></label>
	  </div>
	  
	   <div class="form-group input-group-button text-right" >
		   <button type='button' class='btn btn-xs btn-default btn-unshare' data-dismiss='modal'><spring:message code='button.share.delete'/></button>
		   <button type='button' class='btn btn-xs btn-info btn-updateshare' data-dismiss='modal'><spring:message code='button.share.update'/></button>
		   <button type='button' class='btn btn-xs btn-info btn-share' data-dismiss='modal'><spring:message code='button.share.add'/></button>
	  </div>
	  
   </div> 
</div>  
