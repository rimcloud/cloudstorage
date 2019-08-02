<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${rimDriveVO.logoText}</title>
<link rel="shortcut icon" href="<c:url value='/resources/images/common/favicon.ico'/>" />
<link rel="stylesheet" href="<c:url value='/webjars/adminlte/2.3.11/bootstrap/css/bootstrap.min.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/css/login/login.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/css/common/jsloading.css'/>">

<script src="<c:url value='/webjars/adminlte/2.3.11/plugins/jQuery/jquery-2.2.3.min.js'/>"></script>
<script src="<c:url value='/webjars/adminlte/2.3.11/bootstrap/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/webjars/jquery-validation/1.16.0/dist/jquery.validate.min.js'/>"></script>
<script src="<c:url value='/resources/js/common/rim_common.js'/>"></script>
<script src="<c:url value='/resources/js/common/jsloading.js'/>"></script>
<script src="<c:url value='/resources/js/common/jsdialog.js'/>"></script>
<script src="<c:url value='/resources/js/common/jsrequest.js'/>"></script>
<script src="<c:url value='/resources/js/common/routes.js'/>"></script>

<script type="text/javascript">

	$(document).ready(function(){
		
		initLoaded();
		
		$("#loginForm").validate({ 
			errorElement: 'div',
			errorClass: 'error',
			errorPlacement: function(error, element) {
				 error.insertAfter(element);
			},
			rules: { 
				txt_userid: { 
					required: true, 
					maxlength: 30 
				}, 
				txt_pwd: { 
					required: true, 
					maxlength: 30
				}
			}, 
			messages: { 	
				txt_userid: { 
					required: "<spring:message code='common.required.msg' arguments='["+ $("#txt_userid").attr("placeholder") +"]' />", 
					maxlength:"<spring:message code='common.maxlength.msg' arguments='30' />" 
				}, 
				txt_pwd: { 
					required: "<spring:message code='common.required.msg' arguments='["+ $("#txt_pwd").attr("placeholder") +"]'/>" , 
					maxlength:"<spring:message code='common.maxlength.msg' arguments='30' />" 
				}
			}
		});
	});
	
	function noBack() {
		window.history.forward();
	}

	function initLoaded(){
		getCheckSaveId();
	}
	
	function getCheckSaveId() {
	    var cook = document.cookie + ";";
	    var idx = cook.indexOf("userid", 0);
	    var val = "";
	
	    if(idx != -1) {
	        cook = cook.substring(idx, cook.length);
	        begin = cook.indexOf("=", 0) + 1;
	        end = cook.indexOf(";", begin);
	        val = unescape( cook.substring(begin, end) );
	    }
	
	    if(val!= "") {
	    	$("#txt_userid").val(val);
	        $("input[id=chk_rememberid]:checkbox").prop("checked", true);
	    }
	}
	
	function setCookieSave(name, value, expiredays) {
	    var today = new Date();
	    today.setDate( today.getDate() + expiredays );
	    document.cookie = name + "=" + escape( value ) + "; path=/; expires=" + today.toGMTString() + ";"
	}
	
	function saveLogin(id) {
	    if(id != "") {
	    	setCookieSave("userid", id, 7);
	    } else {
	    	setCookieSave("userid", id, -1);
	    }
	}

	function fnSubmitLogin(){
	
		if ($("#chk_rememberid").is(":checked")) {
			saveLogin($("#txt_userid").val());
		} else {
			saveLogin("");
		}

		$(".alert").hide();
		
		var postData = { rim_uid : $.trim($("#txt_userid").val()), rim_pwd : $("#txt_pwd").val(), rim_scd : ""  };
		
		JSRequest.postDataAjax(getAjaxUrlAccountLogin(), postData ,"<spring:message code='loading.login.msg'/>",function(data) {
			location.href = "<c:url value='/main.do'/>";
		}, function(data){
			$(".alert p").text(data.status.message);
			$(".alert").slideDown();
		}, function(msg){
			$(".alert p").text("<spring:message code='common.fail.msg'/>");
			$(".alert").slideDown();
		} );
	}
</script>
</head>
<body>

	<input type="hidden" id="webroot" name="webroot" value="${pageContext.request.contextPath}">

	<section id="logo">
		<img src="<c:url value='/resources/images/login/login_top.png'/>" alt="CLOUDRIM DRIVE" />
		<form method="post" action="javascript:fnSubmitLogin();" role="login" id="loginForm">

			<div class="input-group">
				<span class="input-group-addon icon-bg"><i class="glyphicon glyphicon-user"></i></span> <input type="text" id="txt_userid" name="txt_userid" class="form-control" placeholder="<spring:message code='label.user.id'/>" maxlength="30" tabindex="1" />
			</div>

			<div class="input-group">
				<span class="input-group-addon icon-bg"><i class="glyphicon glyphicon-lock"></i></span> <input type="password" id="txt_pwd" name="txt_pwd" class="form-control" placeholder="<spring:message code='label.user.pwd'/>" maxlength="30" tabindex="2" />
			</div>

			<div class="alert alert-danger">
				<strong>Warning!</strong>
				<p></p>
			</div>

			<div class="checkbox save-id">
				<label><input type="checkbox" id="chk_rememberid" value="" tabindex="3">
				<spring:message code='label.chk.remember.id' /></label>
			</div>

			<section>
				<button type="submit" id="btn_login" class="btn btn-block btn-info login" tabindex="4">
					<spring:message code='button.login' />
				</button>
			</section>

		</form>
	</section>

	<section class="container">
		<section class="row">
			<div class="footer">
				<strong>Copyright &copy; 2015 C L O U D R I M .</strong> All rights reserved.
			</div>
		</section>
	</section>

</body>
</html>
