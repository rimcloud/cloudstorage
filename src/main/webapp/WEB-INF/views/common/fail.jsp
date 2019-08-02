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
			<div class="panel-group">

				<div class="panel panel-warning">
					<div class="panel-heading">
						<c:out value="${code}" />
					</div>
					<div class="panel-body">
						<h2 style="color: red;">
							<c:out value="${message}" escapeXml="false" />
						</h2>
					</div>
				</div>

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
