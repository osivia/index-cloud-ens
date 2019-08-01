<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html" isELIgnored="false" %>

<portlet:actionURL name="exportData" var="exportDataUrl" copyCurrentRenderParameters="true"/>

<div class="page-heade">
	<div class="h2 m-0"><op:translate key="exportaccount.title" /></div>
</div>
<op:translate key="exportaccount.info" />

<div class="export-account">
	<form:form action="${exportDataUrl}" method="post" enctype="multipart/form-data" role="form">
	
		<div class="form-group row">
		    <div class="col-md-9 offset-md-3 col-lg-10 offset-lg-2">
		        <!-- Save -->
		        <button type="submit" name="export" class="btn btn-primary">
		            <span><op:translate key="exportaccount.submit" /></span>
		        </button>
		    </div>
		</div>
	
	</form:form>
</div>