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
	
	<link rel="stylesheet" href="<c:url value='/resources/css/decorators/popuplayout.css'/>">
	
	<decorator:head/>
</head>

<body class="hold-transition skin-blue-light sidebar-mini">

	<input type="hidden" id="webroot" name="webroot" value="${pageContext.request.contextPath}">

<div class="wrapper">

<decorator:body />	

</div>
</body>
</html>