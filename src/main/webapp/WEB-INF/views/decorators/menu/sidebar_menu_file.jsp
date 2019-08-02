<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

  <aside class="main-sidebar">

    <section class="sidebar">
	   
      <ul class="sidebar-menu" data-widget="tree" >      
      
        <li class="header"><spring:message code='label.storage.personal' /></li>
        <li>
	       <div class="user-panel">
			   <div class="pull-left image">
			      <p><spring:message code='label.free.size' arguments="${storageVO.displayFreeSize}" /></p>
				  <span><spring:message code='label.used.size' arguments="${storageVO.displayUsedSize}" /></span>
			   </div>
		  </div>
        </li>
        
        
     	<li class="treeview active"><a href="#" data-storageid="${sessionScope.sessionVO.storageId}" ><b style="margin-left: 15px;"><spring:message code='label.storage.personal.folder' /></b><span class="pull-right-container" style="left: 5px;"><i class="fa fa fa-angle-right pull-left"></i></span></a>
             
         <ul class="treeview-menu menu-open" style="display: block;"> 
	        <li class="all" ><a href="javascript:switchContent('all','${sessionScope.sessionVO.storageId}');"><img src="<c:url value='/resources/images/icon/folder.png'/>"  class="personal-fa" /> <span><spring:message code='label.files.files' /></span></a></li>
			<li class="sharingin" ><a href="javascript:switchContent('sharingin','${sessionScope.sessionVO.storageId}');"><img src="<c:url value='/resources/images/icon/sharingin.png'/>" class="personal-fa"  /> <span><spring:message code='label.files.sharingin' /></span></a></li>
	        <li class="sharingout" ><a href="javascript:switchContent('sharingout','${sessionScope.sessionVO.storageId}');"><img src="<c:url value='/resources/images/icon/sharingout.png'/>" class="personal-fa"  /> <span><spring:message code='label.files.sharingout' /></span></a></li>
	        <li class="bookmark" ><a href="javascript:switchContent('bookmark','${sessionScope.sessionVO.storageId}');"><img src="<c:url value='/resources/images/icon/bookmark.png'/>" class="personal-fa"  /> <span><spring:message code='label.files.bookmark' /></span></a></li>	   
	        <li class="trashbin" ><a href="javascript:switchContent('trashbin','${sessionScope.sessionVO.storageId}');"><img src="<c:url value='/resources/images/icon/trashbin.png'/>" class="personal-fa"  /> <span><spring:message code='label.files.trashbin' /></span></a></li>
	     </ul>
        </li>
        
         <li class="search" ><a href="javascript:switchContent('search','${sessionScope.sessionVO.storageId}');"><img src="<c:url value='/resources/images/icon/search.png'/>" class="personal-fa"  /> <span><b><spring:message code='label.files.search' /></b></span></a></li>
      </ul>
  
    </section>   
  </aside>