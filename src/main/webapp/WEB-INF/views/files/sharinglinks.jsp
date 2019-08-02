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
<link rel="stylesheet" href="<c:url value='/webjars/adminlte/2.3.11/bootstrap/css/bootstrap.min.css' />">
<link rel="stylesheet" href="<c:url value='/webjars/font-awesome/4.7.0/css/font-awesome.min.css'/>">
<link rel="stylesheet" href="<c:url value='/webjars/ionicons/2.0.1/css/ionicons.min.css'/>">
<link rel="stylesheet" href="<c:url value='/webjars/adminlte/2.3.11/dist/css/AdminLTE.min.css'/>">
<link rel="stylesheet" href="<c:url value='/webjars/adminlte/2.3.11/dist/css/skins/skin-blue-light.min.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/css/login/login.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/css/common/font.css'/>"> 
<link rel="stylesheet" href="<c:url value='/resources/css/common/jsloading.css'/>">
<link rel="stylesheet" href="<c:url value='/resources/css/common/jsdialog.css'/>">

<script src="<c:url value='/webjars/adminlte/2.3.11/plugins/jQuery/jquery-2.2.3.min.js'/>"></script>
<script src="<c:url value='/webjars/adminlte/2.3.11/plugins/jQueryUI/jquery-ui.min.js'/>"></script>
<script src="<c:url value='/webjars/adminlte/2.3.11/bootstrap/js/bootstrap.min.js'/>"></script>
<script src="<c:url value='/webjars/adminlte/2.3.11/dist/js/app.min.js' /> "></script>
<script src="<c:url value='/webjars/jquery-validation/1.16.0/dist/jquery.validate.min.js'/>"></script>

<script src="<c:url value='/resources/js/common/routes.js'/>"></script>
<script src="<c:url value='/resources/js/common/rim_common.js'/>"></script>
<script src="<c:url value='/resources/js/common/jsloading.js'/>"></script>
<script src="<c:url value='/resources/js/common/jsdialog.js'/>"></script>
<script src="<c:url value='/resources/js/common/jsrequest.js'/>"></script>

<script src="<c:url value='/resources/js/message/message_ko.js'/>"></script>

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
				txt_pwd: { 
					required: true, 
					maxlength: 30
				}
			}, 
			messages: {
				txt_pwd: { 
					required: "<spring:message code='common.required.msg' arguments='["+ $("#txt_pwd").attr("placeholder") +"]'/>" , 
					maxlength:"<spring:message code='common.maxlength.msg' arguments='30' />" 
				}
			}
		});	
		
	});
 
	function initLoaded(){
		reloadForm();
	}
	 
	function reloadForm(){
		$(".form-login").addClass("hidden");
		$(".form-download").addClass("hidden");
		$(".form-error").addClass("hidden");
		
		var empNm = "${empVO.empNm}";
		
		$(".share-message").addClass("hidden");
		
		if( rimCommon.isEmpty("${sharingLinksVO.shareId}")){
			$(".form-error").removeClass("hidden");
		} else{
			$(".form-download").removeClass("hidden");
		}	
	}
	
	function fnDownload(){
		
		JSRequest.download(getAjaxUrlDownloadFileLinkByToken(), { rim_token : "${token}" });
	}
	
</script>
</head>
<body>
	
	<input type="hidden" id="webroot" name="webroot" value="${pageContext.request.contextPath}">
	<div class="language">
		<a href="<c:url value='/main.do'/>">HOME</a>
	</div>
		
	<section id="logo">	
		<img src="<c:url value='/resources/images/login/login_top.png'/>" alt="CLOUDRIM DRIVE" />
		<div class="share-message hidden">
			<h4><spring:message code='label.share.user' arguments="${empVO.empNm}" /></h4>
		</div>
		
		<div class="form-login hidden">
			<form method="post" role="login" id="loginForm">
					<div class="input-group">
					    <span class="input-group-addon icon-bg"><i class="glyphicon glyphicon-lock"></i></span>
					 	<input type="password" id="txt_pwd" name="txt_pwd" class="form-control" placeholder="<spring:message code='label.sharinglinks.passwd'/>" maxlength="30" tabindex="1" />
				  	</div>
				  	
			  		<div class="alert alert-danger">
					  <strong>Warning!</strong> <p></p>
					</div>
					
					<section>
						<button type="button" id="btn_login" class="btn btn-block btn-info login"><spring:message code='button.check.password' /></button>
					</section>
					
			</form>
		</div>
		
		<div class="form-download hidden">
			<a href="javascript:fnDownload();" class="btn btn-primary btn-social btn-vk" ><i class="fa fa-download"></i> ${fileListVO.name} <spring:message code='button.filedownload' /> (${fileListVO.displaySize})  </a>
		</div>
		
		<div class="form-error hidden">
			<div class="alert-danger">
				<strong><spring:message code='error.sharinglinks.empty' /></strong> <p></p>
			</div>
		</div>
	</section>
	
	
	<section class="container">
		<section class="row">
				<div class="footer">
					<p>${footer}</p>
				</div>
		</section>
	</section> 

</body>
</html>
