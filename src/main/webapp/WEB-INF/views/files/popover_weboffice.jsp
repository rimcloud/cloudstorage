<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div class="box popover-weboffice hide" >				
	<div class="box-body">	     
		<div class="btn-group">
		  <a id="hwp"  href="javascript:void(0);"  class="btn weboffice-selected"><img src="<c:url value='/resources/images/filetype/hwp.png'/>" alt="Hwp"></a>
		  <a id="xlsx" href="javascript:void(0);" class="btn"><img src="<c:url value='/resources/images/filetype/xls.png'/>" alt="Excel"></a>
		  <a id="pptx" href="javascript:void(0);" class="btn"><img src="<c:url value='/resources/images/filetype/ppt.png'/>" alt="Power Point"></a>
		  <a id="docx" href="javascript:void(0);" class="btn"><img src="<c:url value='/resources/images/filetype/doc.png'/>" alt="Word"></a>
		</div> 								
	</div>
	<div class="box-footer text-left">
		<p><spring:message code='label.filename' /> : <code class='weboffice-filename'>.hwp</code></p>
	</div>
</div>
