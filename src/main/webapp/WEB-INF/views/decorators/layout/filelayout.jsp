<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>${rimDriveVO.logoText}</title>
	
	<page:applyDecorator name="header"/> 
	<link rel="stylesheet" href="<c:url value='/resources/css/decorators/filelayout.css'/>">
	
	<decorator:head/>
</head>

<body class="hold-transition skin-blue-light sidebar-mini">

	<input type="hidden" id="webroot" name="webroot" value="${pageContext.request.contextPath}">
	<input type="hidden" id="page_shared_data" name="page_shared_data" >
	<input type="hidden" id="popup_post_data" name="popup_post_data" >
	<input type="hidden" id="popup_shared_data" name="popup_shared_data" >
	<input type="hidden" id="webofficeyn" name="webofficeyn" value="${userConfig.webOfficeYn}">

<div class="wrapper">
	
	<header class="main-header">
	
		<a href="<c:url value='/'/>" class="logo">
			<span class="logo-mini"><img src="<c:url value='${rimDriveVO.logoImg}'/>" /></span>
			<span class="logo-lg text-left"><img src="<c:url value='${rimDriveVO.logoImg}'/>" style="width:40px;height:37px" /> <b>${rimDriveVO.logoText}</b></span>
		</a>
		
		<nav class="navbar navbar-static-top" role="navigation">
			<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"><span class="sr-only">Toggle navigation</span></a>
			<div class="navbar-custom-menu">
			
			<ul class="nav navbar-nav">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="false"><i class="fa fa-user"> ${sessionScope.sessionVO.userNm} </i><span class="caret"></span></a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="javascript:rimCommon.logout('${sessionScope.sessionVO.userId}');"><i class="fa fa-sign-out rim-fa"></i><spring:message code='button.logout' /></a></li>
					</ul>
				</li>
			</ul>
			</div>
		</nav>

	</header>
	
	<page:applyDecorator name="file_sidebar_menu"/>
	
	<div  id="rim_main_content" class="content-wrapper">
		<decorator:body />
		
	</div>
	
	<page:applyDecorator name="footer"/>

</div>
</body>
</html>