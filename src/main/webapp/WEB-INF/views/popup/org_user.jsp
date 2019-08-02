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
		
		$(".rim-box-users").find(".btn-search").on("click",function(e){
			searchUser();
		});
		
		$(".rim-box-users").find(".input-search-user").keydown(function(e){
			if (e.keyCode == 13 && !this.checkKeyEvent) {
				this.checkKeyEvent = true;
				searchUser(); 
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
		
		$(".rim-box-users").find(".box-body tbody").empty();
		
		if(this.treeview != null ) {
			this.treeview.destory();
			delete this.treeview;
			this.treeview = null;
		}
		
		this.treeview = new JSTree("JSTree",this,function(nodeData){
			getEmpList(nodeData);
		});
		
		var postData = { rim_search_id : "deptNm" , rim_search_text : text };
		ajaxRequest( getAjaxUrlDeptSearchList() , postData , this, function(res){
			
			for (var idx in res.data) {
				var dept = res.data[idx];
				this.treeview.setCoreNode(dept.uprDeptCd,dept.deptCd,dept.deptNm,dept.whleDeptCd,false,false,"fa fa-rim-tree fa-sitemap");
			}
			
			this.treeview.setCoreData();
		});
		
	}

	function searchUser(){
		var text = $(".input-search-user").val();
		if(text.length < 2){
			JSDialog.Alert("<spring:message code='fail.search.text.length' arguments='2' />");
			return;
		}
		
		var postData = { rim_search_id : "empNm" , rim_search_text : text };
		ajaxRequest( getAjaxUrlEmpSearchList() , postData , this, function(res){
			
			$(".rim-box-users").find(".box-body tbody").empty();
			
			for ( var idx in res.data) {
				var emp = res.data[idx];
				var tr = "<tr  data-empId='"+emp.empId+"' data-empNm='"+emp.empNm+"' >";
				tr += "<td></td>";
				tr += "<td>" + emp.deptNm + "</td>";
				tr += "<td data-toggle='tooltip' data-original-title='"+ emp.empId +"' style='cursor:pointer;' >" + emp.empNm + "</td>";
				tr += "<td>" + emp.grade + "</td>";
				tr += "</tr>";
				
				$(".rim-box-users").find(".box-body tbody").append(tr);
			}
			
			$(".rim-box-users").find(".box-body table > tbody > tr").on("dblclick",function(e){
				
				var sharingData = { dept : [] , emp : [] };
				var emp = { empId : $(this).attr("data-empId") , empNm : $(this).attr("data-empNm") };
				sharingData.emp.push(emp);
				JSPopup.setOpenerSharingData(sharingData);
				winClose();
				
			});
		});
	}
	
	function winClose(){
		window.close();
	}
	
	function initDept(){
		
		this.treeview = new JSTree("JSTree",this,function(nodeData){
			getEmpList(nodeData);
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
				this.treeview.setCoreNode(dept.uprDeptCd,dept.deptCd,dept.deptNm,dept.whleDeptCd,false,false,"fa fa-rim-tree fa-sitemap");
			}
			this.treeview.setCoreData(function(){
				this.treeview.openNodes(myDept.whleDeptCd.split(";"));
				
				setTimeout(function() { 
					this.treeview.selectNode(deptCd); 
				}, 500);
			
			});
		
		});
	}
	
	function getEmpList(nodeData){
		var deptCd = nodeData.id;
		
		$(".rim-box-users").find(".box-body tbody").empty();
		
		ajaxRequest( getAjaxUrlEmpList() , {rim_dept_cd : deptCd} , this, function(res){
			for ( var idx in res.data) {
				var emp = res.data[idx];
				var tr = "<tr data-empId='"+emp.empId+"' data-empNm='"+emp.empNm+"'>";
				tr += "<td></td>";
				tr += "<td>" + emp.deptNm + "</td>";
				tr += "<td data-toggle='tooltip' data-original-title='"+ emp.empId +"' style='cursor:pointer;'>" + emp.empNm + "</td>";
				tr += "<td>" + emp.grade + "</td>";
				tr += "</tr>";
				
				$(".rim-box-users").find(".box-body tbody").append(tr);
			}
			
			$(".rim-box-users").find(".box-body table > tbody > tr").on("dblclick",function(e){
				var sharingData = { dept : [] , emp : [] };
				var emp = { empId : $(this).attr("data-empId") , empNm : $(this).attr("data-empNm") };
				sharingData.emp.push(emp);
				JSPopup.setOpenerSharingData(sharingData);
				winClose();
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

	<div class='col-xs-6'>
		<div class="box box-primary rim-box-dept box-popup-top-left">
			<div class="box-header with-border">
				<h3 class="box-title"><spring:message code='label.dept.list' /></h3>
			<div class="pull-right" >
				<div class="input-group input-group-xs">
					<input type="text" class="form-control input-search-dept" placeholder="<spring:message code='label.search.dept' />">
					<span class="input-group-btn">
						<button type="button" class="btn btn-info btn-flat btn-search"><i class="fa fa-search"></i></button>
					</span>
				</div>
			</div>
			</div>	
			
			<div class="box-body" style="height: 410px;">
				<span id="JSTree" class="jstree-menu"></span>
			</div>
		</div>
	</div>

	<div class='col-xs-6'>
	<div class="box box-primary rim-box-users no-padding JSTable box-popup-top-right">
		<div class="box-header with-border">
		<h3 class="box-title"><spring:message code='label.user.list' /></h3>
	
		<div class="pull-right" >
			<div class="input-group input-group-xs">
				<input type="text" class="form-control input-search-user" placeholder="<spring:message code='label.search.user' />">
				<span class="input-group-btn">
					<button type="button" class="btn btn-info btn-flat btn-search"><i class="fa fa-search"></i></button>
				</span>
			</div>
		</div>
		</div>
		
		<div class="box-body" style="height: 410px;">
		<table class="table table-bordered table-striped table-hover">
			<thead>
				<tr>
					<th width="10%"></th>
					<th width="35%"><spring:message code='label.dept' /></th>
					<th width="25%"><spring:message code='label.name' /></th>
					<th width="35%"><spring:message code='label.grade' /></th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		</div>
	</div>

	</div>
</div>

</section>

</body>
</html>
