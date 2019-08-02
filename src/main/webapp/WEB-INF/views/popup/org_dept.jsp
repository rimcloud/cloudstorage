<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript">
	    
    var treeview = null;
      
	function  initPage(){
		
		initDept();
		initButtonEvent();
	}
	
	function initButtonEvent(){
		
		$(".rim-box-dept").find(".btn-search").on("click",function(e){
			searchDept();
		});
		
		$(".rim-box-dept").find(".input-search-dept").keydown(function(e){
			if (e.keyCode == 13 && !this.checkKeyEvent) {
				this.checkKeyEvent = true;
				searchDept();
			}
		}).keyup(function(e){
			this.checkKeyEvent = false;
		});
	}
	function searchDept(){
		var text = $(".input-search-dept").val();
		if(text.length < 2 && text.length != 0 ){
			JSDialog.Alert("<spring:message code='fail.search.text.length' arguments='2' />");
			return;
		}
		
		var postData = { rim_search_id : "deptNm" , rim_search_text : text };
		ajaxRequest( getAjaxUrlDeptSearchList() , postData , this, function(res){
			
			if(this.treeview != null ) {
				this.treeview.destory();
				delete this.treeview;
				this.treeview = null;
			}
			
			this.treeview = new JSTree("JSTree",this,function(nodeData){
				
			},function(nodeData){
				
				var sharingData = { dept : [] , emp : [] };
				var dept = { deptCd : nodeData.id , deptNm : nodeData.name , deptLevel : nodeData.deptLevel , whleDeptCd : nodeData.whleDeptCd };
				sharingData.dept.push(dept);
				JSPopup.setOpenerSharingData(sharingData);
				winClose();
				
			});
			
			for ( var idx in res.data) {
				var dept = res.data[idx];
				this.treeview.setCoreNode(dept.uprDeptCd,dept.deptCd,dept.deptNm,dept.whleDeptCd,false,false,"fa fa-rim-tree fa-sitemap", { deptLevel : dept.deptLevel , whleDeptCd : dept.whleDeptCd } );
			}
			this.treeview.setCoreData();
		});
		
	}
	function winClose(){
		window.close();
	}
	
	function initDept(){
		
		this.treeview = new JSTree("JSTree",this,function(nodeData){
					
		},function(nodeData){
			var sharingData = { dept : [] , emp : [] };
			var dept = { deptCd : nodeData.id , deptNm : nodeData.name , deptLevel : nodeData.deptLevel , whleDeptCd : nodeData.whleDeptCd };
		 	sharingData.dept.push(dept);
			JSPopup.setOpenerSharingData(sharingData);
			winClose();	
		}); 	
	
		var deptCd =  "${sessionScope.sessionVO.deptCd}";
		getDeptList(deptCd);		
	}
	
	function getDeptList(deptCd){
		
		ajaxRequest( getAjaxUrlDeptList() , {rim_dept_cd : deptCd} , this, function(res){

			var myDept = null;
			for ( var idx in res.data) {
				var dept = res.data[idx];
				if( dept.deptCd == deptCd ){
					myDept = dept;
				}
				this.treeview.setCoreNode(dept.uprDeptCd,dept.deptCd,dept.deptNm,dept.whleDeptCd,false,false,"fa fa-rim-tree fa-sitemap" , { deptLevel : dept.deptLevel , whleDeptCd : dept.whleDeptCd });
			}
			this.treeview.setCoreData(function(){
				this.treeview.openNodes(myDept.whleDeptCd.split(";"));
				
				setTimeout(function() { 
					this.treeview.selectNode(deptCd); 
				}, 500);
								
			});
		
		});		
	}

</script>
</head>
<body>

<section class="content">

<div class='row'>
	<div class="callout callout-info">
	  <h4><spring:message code='label.organization' /></h4>
	</div>
</div>
		<div class='row'>
			<div class="box box-primary rim-box-dept box-popup-top-left">
				<div class="box-header with-border">
					<h3 class="box-title">
						<spring:message code='label.dept.list' />
					</h3>
					<div class="pull-right">
						<div class="input-group input-group-xs">
							<input type="text" class="form-control input-search-dept"
								placeholder="<spring:message code='label.search.dept' />">
							<span class="input-group-btn">
								<button type="button" class="btn btn-info btn-flat btn-search">
									<i class="fa fa-search"></i>
								</button>
							</span>
						</div>
					</div>
				</div>

				<div class="box-body">
					<span id="JSTree" class="jstree-menu"></span>
				</div>
			</div>
		</div>
	</section>
   
</body>
</html>
