<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<script type="text/javascript">
	var modalSharingOut = {
		id : "sharingout",
		rimModal : null,
		row : null,
		pageClass : null,
		clear : function(){
			this.rimModal = null;
			this.row = null;
			this.pageClass = null;
		},
		Modal : function(row,pageClass){
			this.row = row;	
			this.pageClass = pageClass;
			var postData = { rim_sid : pageClass.storageId , rim_fid : row.fileId };
			
			ajaxRequest( getAjaxUrlSharingOutInfo(pageClass.storageId) , postData , this, function(res){			
				this.rimModal = this.display(this,pageClass);
				this.setData(this,res.data);
			});	
		},
		display : function(self,pageClass){
			var div = $(".modal-"+ self.id );			
			var rimModal = JSDialog.Modal(self.id,"<spring:message code='button.sharing'/>",div.html(),function(){
								var fileId = self.row.fileId;
								self.clear();
								pageClass.reloadRow(pageClass,fileId);
						   });
			
			rimModal.find(".modal-footer").css("text-align","left").append("<code>"+self.row.name+"</code>");
			
			rimModal.find("input[type=checkbox]").on("change",function(e){
				e.preventDefault();
				var id = $(this).attr("id");
				var checked = $(this).is(':checked');
				if(checked){
					self.rimModal.find(".input-group-" + id ).removeClass("hidden");
				}
				else{
					self.rimModal.find(".input-group-" + id ).addClass("hidden");
					self.rimModal.find(".input-group-" + id ).find(":input").each(function() {
			            $(this).val('');
			        });
				}			
			});
			
			rimModal.find(".btn-dept").on("click",function(e){
				e.preventDefault();
				
				JSPopup.open( getPopupUrl(self.row.storageId) + "?rim_view=org_all_site" ,"org_all_site",{width : 800 , height : 700 , center : 1 , resizable : 0},function(data){
					for ( var idx in data.dept) {
						var dept = data.dept[idx];
						var shareTargetVO = {targetTp : "D" , shareTargetNo : ''  , shareWithUid : dept.deptCd , shareWithName : dept.deptNm ,permissions : "R" };
						self.addList(self,shareTargetVO);
					}
					for ( var idx in data.emp) {
						var emp = data.emp[idx];
						var shareTargetVO = {targetTp : "U" , shareTargetNo : ''  , shareWithUid : emp.empId , shareWithName : emp.empNm ,permissions : "R" };
						self.addList(self,shareTargetVO);
					}
				});
				return false;
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
			self.rimModal.find(".box-dept-list").find(".table > tbody").empty();
			self.rimModal.find(".box-user-list").find(".table > tbody").empty();
			self.rimModal.find("#shareId").val('');
			
			if(rimCommon.isEmpty(data)){
				self.rimModal.find("input:checkbox[id='accessPw']").attr("checked", false).trigger("change");
				self.rimModal.find("input:checkbox[id='date']").attr("checked", false).trigger("change");
				self.rimModal.find("input:checkbox[id='message']").attr("checked", false).trigger("change");
				self.rimModal.find(".btn-unshare").addClass("hidden");
				self.rimModal.find(".btn-updateshare").addClass("hidden");
				self.rimModal.find(".btn-share").removeClass("hidden");
			}else{
				
				if(!rimCommon.isEmpty(data.listShareTargetVO)){
					for ( var idx in data.listShareTargetVO ) {
						var shareTargetVO = data.listShareTargetVO[idx];
						self.addList(self,shareTargetVO);
					}
					self.rimModal.find(".btn-share").addClass("hidden");
					self.rimModal.find(".btn-unshare").removeClass("hidden");
					self.rimModal.find(".btn-updateshare").removeClass("hidden");
					self.rimModal.find("#shareId").val(data.shareVO.shareId);
				}
			}
		},
		addList : function(self,shareTargetVO){
			var tr = "<tr id='"+shareTargetVO.shareTargetNo+"' data-uid='"+ shareTargetVO.shareWithUid+"' data-vo='"+ JSON.stringify(shareTargetVO) +"' >";
            tr += "<td class='text-left'>"+ shareTargetVO.shareWithName +"</td>";
            tr += "<td class='text-center'>";
            tr += "<div class='btn-group'>";
            tr += "<button id='read' type='button' class='btn btn-xs btn-read "+(shareTargetVO.permissions =="R"?"btn-info":"")+"'><spring:message code='button.permission.read'/></button>";
            tr += "<button id='modify' type='button' class='btn btn-xs btn-modify "+(shareTargetVO.permissions =="W"?"btn-info":"")+"'><spring:message code='button.permission.modify'/></button>";
            tr += "</div>";
            tr += "</td>";
            tr += "<td class='text-center'><input type='checkbox' id='delete' ></td>";
            tr += "</tr>";
            var el = shareTargetVO.targetTp == "U"?"user":"dept";
			var table = self.rimModal.find(".box-"+ el +"-list").find(".table > tbody");
			
			var duplicate = false;
			table.find("tr").each(function() {	
				var uid = $(this).attr("data-uid");
				if( uid == shareTargetVO.shareWithUid ){
					duplicate = true;
				}
			});
			
			var sessionUserId = '${sessionScope.sessionVO.userId}';
			if( sessionUserId == shareTargetVO.shareWithUid ){
				duplicate = true;
			}
			
			if(duplicate) {
				return;
			}
			
			table.append($(tr).on("click",":button",function(){
				 $(this).parent().find(":button").removeClass("btn-info");
				 $(this).addClass("btn-info");
			}));		
		},
		setShare : function(self){
			var postData = { rim_sid : self.pageClass.storageId, rim_fid : self.row.fileId, rim_access_pw : self.rimModal.find("#password1_sharingout").val(), rim_expire_date : self.rimModal.find("#expireDateSharingout").val(), rim_message : self.rimModal.find("#msg_sharingout").val() };
			var shareList = self.getShareList(self);
			
		    postData.rim_insert_target = JSON.stringify(shareList.insertTarget);
		   
			ajaxRequest( getAjaxUrlSharingOutAdd(self.pageClass.storageId) , postData , this, function(res){
				this.setData(this,res.data);
			});	
		},
		updateShare : function(self){

			var postData = { rim_sid : self.pageClass.storageId, rim_access_pw : self.rimModal.find("#password1_sharingout").val(), rim_expire_date : self.rimModal.find("#expireDateSharingout").val(), rim_message : self.rimModal.find("#msg_sharingout").val() };
			postData.rim_share_id = self.rimModal.find("#shareId").val();
			var shareList = self.getShareList(self);
			
			postData.rim_insert_target  = JSON.stringify(shareList.insertTarget);
			postData.rim_update_target_r = JSON.stringify(shareList.updateTargetR);
			postData.rim_update_target_w = JSON.stringify(shareList.updateTargetW);
			postData.rim_delete_target  = JSON.stringify(shareList.deleteTarget);
			
			var totalList = self.rimModal.find(".box-dept-list").find(".table > tbody > tr").length + self.rimModal.find(".box-user-list").find(".table > tbody > tr").length;
			
			if( (shareList.insertTarget.length == 0 
					&& shareList.updateTargetR.length == 0 
					&& shareList.updateTargetW.length == 0 
					&& shareList.deleteTarget.length == 0) || totalList == shareList.deleteTarget.length ){
				JSDialog.Confirm("<spring:message code='confirm.share.modify.empty' />",function(flag){
					if(flag){
						self.setUnShare(self);
					}
				});
			}
			else{			
				 ajaxRequest( getAjaxUrlSharingOutUpdate(self.pageClass.storageId) , postData , this, function(res){
					this.setData(this,res.data);
				});
			}
		},
		setUnShare : function(self){

			var shareId = self.rimModal.find("#shareId").val();
			var postData = { rim_sid : self.pageClass.storageId, rim_share_id : shareId };
			ajaxRequest( getAjaxUrlSharingOutDelete(self.pageClass.storageId) , postData , this, function(res){
				this.setData(this,res.data);
			});
			return false;
		},
		getShareList : function(self){
			var shareList = { insertTarget : [] , updateTargetR : [] , updateTargetW : [] , deleteTarget : [] };
			
			self.rimModal.find(".box-dept-list").find(".table > tbody > tr").each(function(index) {
				
				var shareTargetVO = JSON.parse($(this).attr("data-vo"));
				
				if( $(this).find("input:checkbox[id='delete']").is(":checked")) {
					if( rimCommon.isEmpty(shareTargetVO.shareTargetNo) ) return;
					shareList.deleteTarget.push(shareTargetVO);					
				}else if( rimCommon.isEmpty(shareTargetVO.shareTargetNo) ){
					
					if( $(this).find("#modify").hasClass("btn-info")){
						shareTargetVO.permissions = "W";
					}else{
						shareTargetVO.permissions = "R";
					}					
					shareList.insertTarget.push(shareTargetVO);
				}
				else {
					if( $(this).find("#modify").hasClass("btn-info")){
						shareList.updateTargetW.push(shareTargetVO);
					}else{
						shareList.updateTargetR.push(shareTargetVO);
					}
				}			
			});	
			
			self.rimModal.find(".box-user-list").find(".table > tbody > tr").each(function(index) {
				var shareTargetVO = JSON.parse($(this).attr("data-vo"));
				
				if( $(this).find("input:checkbox[id='delete']").is(":checked")) {
					if( rimCommon.isEmpty(shareTargetVO.shareTargetNo) ) return;
					shareList.deleteTarget.push(shareTargetVO);					
				}else if( rimCommon.isEmpty(shareTargetVO.shareTargetNo) ){
					if( $(this).find("#modify").hasClass("btn-info")){
						shareTargetVO.permissions = "W";
					}else{
						shareTargetVO.permissions = "R";
					}	
					shareList.insertTarget.push(shareTargetVO);
				}
				else {
					if( $(this).find("#modify").hasClass("btn-info")){
						shareList.updateTargetW.push(shareTargetVO);
					}else{
						shareList.updateTargetR.push(shareTargetVO);
					}
				}			
			});	
			return shareList;
		}		
	};
 	
</script>

<div class="modal-sharingout hidden">
  <input type="hidden" id="shareId" name="shareId" value="">
  <div class="input-group-out">	  
	   <div class="form-group input-group-button text-right" >
	     <a class="btn btn-xs btn-social btn-vk btn-dept"><i class="fa fa-sitemap"></i><spring:message code='button.dept.user'/></a>
	  </div>
	  
	   <div class="form-group input-group-button text-right" >
		    <div class='row'  >
		      <div class="col-xs-6" >
		    	<div class="box box-dept-list no-padding JSTable" style="overflow-x: auto;overflow-y: auto; height: 200px;border: 1px solid #e5e5e5;">
		            <div class="box-header">
		              <h6 class="box-title"><spring:message code='label.dept.list'/></h6>
		            </div>
		            <div class="box-body no-padding">
		              <table class="table table-bordered table-striped table-hover" >
		              <thead>
		              	<tr>
		                  <th><spring:message code='label.dept'/></th>
		                  <th><spring:message code='label.permission'/></th>
		                  <th><spring:message code='label.delete'/></th>
		                </tr>
		              </thead>
		              <tbody>
		              </tbody>
		              </table>
		            </div>
          		</div>
		    </div>
		    
		    <div class="col-xs-6" >
		    	<div class="box box-user-list no-padding JSTable" style="overflow-x: hidden;overflow-y: auto; height: 200px;border: 1px solid #e5e5e5;">
		            <div class="box-header">
		              <h6 class="box-title"><spring:message code='label.user.list'/></h6>
		            </div>
		            <div class="box-body no-padding">
		              <table class="table table-bordered table-striped table-hover" >
		              <thead>
		              	<tr>
		                  <th><spring:message code='label.name'/></th>
		                  <th><spring:message code='label.permission'/></th>
		                  <th><spring:message code='label.delete'/></th>
		                </tr>
		              </thead>
		              <tbody>
		              </tbody>
		              </table>
		            </div>
          		</div>
		    </div>
  		</div>
	  </div>
	 
	 <div class="form-group input-group-button text-right" >
		   <button type='button' class='btn btn-xs btn-default btn-unshare'><spring:message code='button.share.delete'/></button>
		   <button type='button' class='btn btn-xs btn-info btn-updateshare'><spring:message code='button.share.update'/></button>
		   <button type='button' class='btn btn-xs btn-info btn-share'><spring:message code='button.share.add'/></button>
	  </div>
	  
   </div> 
</div>  
