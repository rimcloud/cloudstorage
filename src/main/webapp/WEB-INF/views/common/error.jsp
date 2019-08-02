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

<script src="<c:url value='/webjars/adminlte/2.3.11/plugins/jQuery/jquery-2.2.3.min.js'/>"></script>
<script src="<c:url value='/webjars/adminlte/2.3.11/bootstrap/js/bootstrap.min.js'/>"></script>

</head>
<body>

	<section id="logo">	
		<img src="<c:url value='/resources/images/login/login_top.png'/>" alt="CLOUDRIM DRIVE" />
		
		<div class="container">
		  <h2 style="color: red;"><c:out value="${message}" /></h2>
		  <div class="panel-group">

			<c:if test="${!empty requestScope['javax.servlet.error.status_code']}">
			    <div class="panel panel-warning">
			      <div class="panel-heading">status_code</div>
			      <div class="panel-body"><c:out value="${requestScope['javax.servlet.error.status_code'] }" /></div>
			    </div>
			</c:if>
			
			<c:if test="${!empty requestScope['javax.servlet.error.exception_type']}">
			    <div class="panel panel-danger">
			      <div class="panel-heading">exception_type</div>
			      <div class="panel-body"><c:out value="${requestScope['javax.servlet.error.exception_type'] }" /></div>
			    </div>
			</c:if>

			<c:if test="${!empty requestScope['javax.servlet.error.message']}">
			     <div class="panel panel-warning">
			      <div class="panel-heading">message</div>
			      <div class="panel-body"><c:out value="${requestScope['javax.servlet.error.message'] }" /></div>
			    </div>
			</c:if>
			
			<c:if test="${!empty error_time}">
			    <div class="panel panel-danger">
			      <div class="panel-heading">error time</div>
			      <div class="panel-body"><c:out value="${error_time}" /></div>
			    </div>
			</c:if>
			
		  </div>
		</div>
	
	</section>
	
	<section class="container">
		<section class="row">
				<div class="footer">
					<p>C L O U D R I M @ 2 0 1 5</p>
				</div>
		</section>
	</section> 

</body>
</html>
